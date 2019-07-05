package cn.imnu.o2o.service.impl;

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
import cn.imnu.o2o.util.PageCalculator;
import cn.imnu.o2o.util.PathUtil;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImgDao productImgDao;


	@Override
	public Product getProductById(long productId) {
		return productDao.queryProductById(productId);
	}

	@Override
	@Transactional
	// 1.处理缩略图，获取缩略图相对路径并赋值给product
	// 2.往tb_product写入商品信息，获取productId
	// 3.结合productId批量处理商品详情图
	// 4.将商品详情图列表批量插入tb_product_img中
	public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList)
			throws ProductOperationException {
		// 空值判断
		if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
			// 给商品设置上默认属性
			product.setCreateTime(new Date());
			product.setLastEditTime(new Date());
			// 默认为上架的状态
			product.setEnableStatus(1);
			// 若商品缩略图不为空则添加
			if (thumbnail != null) {
				addThumbnail(product, thumbnail);
			}
			try {
				// 创建商品信息
				int effectedNum = productDao.insertProduct(product);
				if (effectedNum <= 0) {
					throw new ProductOperationException("创建商品失败");
				}
			} catch (Exception e) {
				throw new ProductOperationException("创建商品失败:" + e.toString());
			}
			// 若商品详情图不为空则添加
			if (productImgHolderList != null && productImgHolderList.size() > 0) {
				addProductImgList(product, productImgHolderList);
			}
			return new ProductExecution(ProductStateEnum.SUCCESS, product);
		} else {
			// 传参为空则返回空值错误信息
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}

	
	/**
	 * 添加缩略图
	 * 
	 * @param product
	 * @param thumbnail
	 */
	private void addThumbnail(Product product, ImageHolder thumbnail) {
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		product.setImgAddr(thumbnailAddr);
	}

	/**
	 * 批量添加图片
	 * 
	 * @param product
	 * @param productImgHolderList
	 */
	private void addProductImgList(Product product, List<ImageHolder> productImgHolderList) {
		// 获取图片存储路径，这里直接存放到相应店铺的文件夹底下
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		List<ProductImg> productImgList = new ArrayList<ProductImg>();
		// 遍历图片一次去处理，并添加进productImg实体类里
		for (ImageHolder productImgHolder : productImgHolderList) {
			String imgAddr = ImageUtil.generateNormalImg(productImgHolder, dest);
			ProductImg productImg = new ProductImg();
			productImg.setImgAddr(imgAddr);
			productImg.setProductId(product.getProductId());
			productImg.setCreateTime(new Date());
			productImgList.add(productImg);
		}
		// 如果确实是有图片需要添加的，就执行批量添加操作
		if (productImgList.size() > 0) {
			try {
				int effectedNum = productImgDao.batchInsertProductImg(productImgList);
				if (effectedNum <= 0) {
					throw new ProductOperationException("创建商品详情图片失败");
				}
			} catch (Exception e) {
				throw new ProductOperationException("创建商品详情图片失败:" + e.toString());
			}
		}
	}

	@Override
	@Transactional
	//若原来缩略图有值,先删除再添加
	//详情图同理
	public ProductExecution modifyProduct(Product product, ImageHolder thumbnail,
			List<ImageHolder> productImgHolderList) throws ProductOperationException {
		
		if(product!=null&&product.getShop()!=null&&product.getShop().getShopId()!=null) {
			product.setLastEditTime(new Date());
			//若商品缩略图不为空
			if(thumbnail!=null) {
				//先获取一遍原有信息，原有信息里有原图片地址
				Product tempProduct = productDao.queryProductById(product.getProductId());
				if(tempProduct.getImgAddr() != null) {
					ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
				}
				addThumbnail(product,thumbnail);
			}
			if(productImgHolderList!=null&&productImgHolderList.size()>0) {
				deleteProductImgList(product.getProductId());
				addProductImgList(product,productImgHolderList);
			}
			try {
				
			
			int effectedNum = productDao.updateProduct(product);
			if(effectedNum<=0) {
				throw new ProductOperationException("更新商品信息失败");
			}
			return new ProductExecution(ProductStateEnum.SUCCESS,product);
			}catch(Exception e) {
				throw new ProductOperationException("更新商品信息失败"+e.toString());
			}
		}else {
			return new ProductExecution(ProductStateEnum.EMPTY);

		}
	}
	/**
	 * 删除某个商品的所有详情图
	 * @param productId
	 */
	private void deleteProductImgList(long productId) {
		//根据productId获取原来的图片
		List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
		//删除原来的图片
		for(ProductImg productImg:productImgList) {
			ImageUtil.deleteFileOrPath(productImg.getImgAddr());
		}
		//删除数据库里原有图片的信息
		productImgDao.deleteProductImgByProductId(productId);
	}

	@Override
	public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
		//页码转换为数据库的行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		//调用Dao取回指定行码的商品
		List<Product> productList=productDao.queryProductList(productCondition, rowIndex, pageSize);
		int count = productDao.queryProductCount(productCondition);
		ProductExecution pe = new ProductExecution();
		pe.setProductList(productList);
		pe.setCount(count);
		return pe;
	}

}
