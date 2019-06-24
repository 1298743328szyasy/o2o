package cn.imnu.o2o.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
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

import cn.imnu.o2o.dto.ShopExecution;
import cn.imnu.o2o.entity.Area;
import cn.imnu.o2o.entity.PersonInfo;
import cn.imnu.o2o.entity.Shop;
import cn.imnu.o2o.entity.ShopCategory;
import cn.imnu.o2o.enums.ShopStateEnum;
import cn.imnu.o2o.exceptions.ShopOperationException;
import cn.imnu.o2o.service.AreaService;
import cn.imnu.o2o.service.ShopCategoryService;
import cn.imnu.o2o.service.ShopService;
import cn.imnu.o2o.util.CodeUtil;
import cn.imnu.o2o.util.HttpServletRequestUtil;
@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
	@Autowired
	private ShopService shopService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private AreaService areaService;
	@RequestMapping(value="/getshopinitinfo",method=RequestMethod.GET)
	@ResponseBody
		private Map<String,Object> getShopInitInfo(){
	        Map<String, Object> modelMap = new HashMap<String, Object>();
	        List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
	        List<Area> areaList = new ArrayList<Area>();
	        try {
	            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
	            areaList = areaService.getAreaList();
	            modelMap.put("shopCategoryList", shopCategoryList);
	            modelMap.put("areaList", areaList);
	            modelMap.put("success", true);
	        } catch (Exception e) {
	            modelMap.put("success", false);
	            modelMap.put("errMsg", e.getMessage());
	        }
	        return modelMap;
	    }

	@RequestMapping(value="/registershop",method=RequestMethod.POST)
	@ResponseBody
	private Map<String,Object> registerShop(HttpServletRequest request){
		Map<String,Object> modelMap=new HashMap<String,Object>();
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg","输入了错误的验证码");
			return modelMap;
		}
		//1.接受并转化相应的参数，包括店铺信息以及图片信息
		//获取前端传回的信息  并将其转化为实体类对象  
		String shopStr = HttpServletRequestUtil.getString(request,"shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop=mapper.readValue(shopStr,Shop.class);
		}catch(Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		//获取图片信息
		CommonsMultipartFile shopImg = null;
		//文件信息解析器
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		//判断request内是否有文件信息
		if(commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
			shopImg = (CommonsMultipartFile)multipartHttpServletRequest.getFile("shopImg");
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg","上传图片不能为空");
			return modelMap;
		}
		
		//2.注册店铺
		if(shop!=null&&shopImg!=null) {
			PersonInfo owner = new PersonInfo();
			owner.setUserId(1L);
			shop.setOwner(owner);
			ShopExecution se;
			try {
				se = shopService.addShop(shop,shopImg.getInputStream(),shopImg.getOriginalFilename());
				if(se.getState()==ShopStateEnum.CHECK.getState()) {
					modelMap.put("success", true);
				}
				else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (ShopOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			return modelMap;
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺信息");
			return modelMap;
		}
	}
	
}
