package cn.imnu.o2o.service;

import java.util.List;

import cn.imnu.o2o.dto.ImageHolder;
import cn.imnu.o2o.dto.ProductExecution;
import cn.imnu.o2o.entity.Product;
import cn.imnu.o2o.exceptions.ProductOperationException;

public interface ProductService {
	/**
	 * 添加商品信息以及图片处理
	 * 
	 * @param product
	 * @param thumbnail缩略图
	 * @param productImgList详情图
	 * @return
	 * @throws ProductOperationException
	 */
	ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList)
			throws ProductOperationException;
}
