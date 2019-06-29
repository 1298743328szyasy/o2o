package cn.imnu.o2o.web.shopadmin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.imnu.o2o.dto.ImageHolder;
import cn.imnu.o2o.dto.ProductExecution;
import cn.imnu.o2o.entity.Product;
import cn.imnu.o2o.entity.Shop;
import cn.imnu.o2o.enums.ProductStateEnum;
import cn.imnu.o2o.exceptions.ProductOperationException;
import cn.imnu.o2o.service.ProductService;
import cn.imnu.o2o.util.CodeUtil;
import cn.imnu.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/shopadmin")
public class ProductManagementController {
	@Autowired
	private ProductService productService;
	private static final int IMAGEMAXCOUNT = 6;

	@SuppressWarnings("unused")
	@RequestMapping(value = "/addproduct", method = RequestMethod.POST)
	// 将return内的值转换为Json格式传到页面
	@ResponseBody
	private Map<String, Object> addProduct(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 验证码校验
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		ObjectMapper mapper = new ObjectMapper();
		Product product = null;
		String productStr = HttpServletRequestUtil.getString(request, "productStr");
		MultipartHttpServletRequest multipartRequest = null;
		ImageHolder thumbnail = null;
		List<ImageHolder> productImgList = null;
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try {
			

		// 判断request中是否有相关的文件流
		if (multipartResolver.isMultipart(request)) {
			multipartRequest = (MultipartHttpServletRequest) request;
			// 取出缩略图并构建ImageHolder
			CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
			thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
			for (int i = 0; i < IMAGEMAXCOUNT; i++) {
				CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartRequest.getFile("productImg" + i);
				// 若取出的第i个详情图片文件流不为空,则将其加入详情图列表
				if (productImgFile != null) {
					ImageHolder productImg = new ImageHolder(productImgFile.getOriginalFilename(),
							productImgFile.getInputStream());
					productImgList.add(productImg);
				}else {
					break;
				}
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "上传图片不能为空");
			return modelMap;
		}
		}catch(Exception e){
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;

		}
		//若Product信息,缩略图,详情图非空   开始添加商品
		if(product!=null && thumbnail!=null && productImgList.size()>0) {
			try {
				//从session中获取当前店铺的Id并赋值给product
				Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
				Shop shop = new Shop();
				shop.setShopId(currentShop.getShopId());
				product.setShop(shop);
				//执行添加操作
				ProductExecution pe = productService.addProduct(product, thumbnail, productImgList);
				if(pe.getState()==ProductStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);	
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			}catch(ProductOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg",e.toString());
				return modelMap;
				
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入商品信息");
		}
		return modelMap;
	}
}
