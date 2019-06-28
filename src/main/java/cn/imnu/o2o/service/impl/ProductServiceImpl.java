package cn.imnu.o2o.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.imnu.o2o.dao.ProductDao;
import cn.imnu.o2o.dao.ProductImgDao;
import cn.imnu.o2o.dto.ImageHolder;
import cn.imnu.o2o.dto.ProductExecution;
import cn.imnu.o2o.entity.Product;
import cn.imnu.o2o.entity.ProductImg;
import cn.imnu.o2o.enums.ProductStateEnum;
import cn.imnu.o2o.exceptions.ProductOperationException;
import cn.imnu.o2o.service.ProductService;
import cn.imnu.o2o.util.ImageUtil;
import cn.imnu.o2o.util.PathUtil;
@Service
public class ProductServiceImpl implements ProductService{
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImgDao productImgDao;
	@Override
	//添加事务管理 遇到错误会回滚
	@Transactional
	//1.创建product信息
	//2.处理缩略图,获取缩略图相对路径并赋值给product
	//3.处理详情图，将其批量放入productimg中
	public ProductExecution addProduct(Product product,ImageHolder thumbnail,List<ImageHolder> productImgHolderList) throws ProductOperationException{
		if(product!=null&&product.getShop()!=null&&product.getShop().getShopId()!=null) {
			product.setCreateTime(new Date());
			product.setLastEditTime(new Date());
			product.setEnableStatus(1);
			//商品缩略图不为空
			if(thumbnail!=null) {
				addThumbnail(product,thumbnail);
			}
			try {
			//创建商品信息
			int effectedNum=productDao.insertProduct(product);
			if(effectedNum<=0) {
				throw new ProductOperationException("创建商品失败");
			}
			}catch(Exception e){
				throw new ProductOperationException("创建商品失败:"+e.toString());
			}
			//若商品详情不为空则添加
			if(productImgHolderList!=null&&productImgHolderList.size()>0) {
				addProductImgList(product,productImgHolderList);
			}
			return new ProductExecution(ProductStateEnum.SUCCESS,product);
		}else {
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}
	//缩略图
	private void addThumbnail(Product product, ImageHolder thumbnail) {
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		try {
			String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
			product.setImgAddr(thumbnailAddr);
		} catch (Exception e) {
			throw new ProductOperationException("创建商品缩略图片失败" + e.toString());
		}
	}
	//详情图
	private void addProductImgList(Product product,List<ImageHolder> productImgHolderList) {		
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		List<ProductImg> productImgList=new ArrayList<ProductImg>();
			try {
				for(ImageHolder productImgHolder:productImgHolderList) {
					String imgAddr = ImageUtil.generateNormalImg(productImgHolder,dest);
					ProductImg productImg = new ProductImg();
					productImg.setImgAddr(imgAddr);
				    productImg.setProductId(product.getProductId());
				    productImg.setCreateTime(new Date());
				    productImgList.add(productImg);
				    if(productImgList.size()>0){
				    	int effectedNum=productImgDao.batchInsertProductImg(productImgList);
				    	if(effectedNum<=0) {
				    		throw new ProductOperationException("创建商品详情图片失败");
				    	}
				    }
				}
			} catch (UnsupportedEncodingException e) {
				throw new ProductOperationException("创建商品详情图片失败"+e.toString());
				}
	}
	
}