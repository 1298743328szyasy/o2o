package cn.imnu.o2o.service;

import java.io.File;
import java.io.InputStream;

import cn.imnu.o2o.dto.ShopExecution;
import cn.imnu.o2o.entity.Shop;
import cn.imnu.o2o.exceptions.ShopOperationException;

public interface ShopService {
	/**
	 * 注册店铺信息,包括对图片的处理
	 * @param shop
	 * @param shopImgInputStream
	 * @param fileName
	 * @return
	 * @throws ShopOperationException
	 */
	ShopExecution addShop(Shop shop,InputStream shopImgInputStream,String fileName) throws ShopOperationException;
	/**
	 * 根据店铺Id获取店铺信息
	 * @param shopId
	 * @return
	 */
	Shop getByShopId(long shopId);
	/**
	 * 更新店铺信息,包括对图片的处理
	 * @param shop
	 * @param shopImgInputStream
	 * @param fileName
	 * @return
	 * @throws ShopOperationException
	 */
	ShopExecution modifyShop(Shop shop,InputStream shopImgInputStream,String fileName) throws ShopOperationException;
}
