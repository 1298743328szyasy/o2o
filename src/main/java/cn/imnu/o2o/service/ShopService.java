package cn.imnu.o2o.service;

import java.io.File;
import java.io.InputStream;

import cn.imnu.o2o.dto.ShopExecution;
import cn.imnu.o2o.entity.Shop;
import cn.imnu.o2o.exceptions.ShopOperationException;

public interface ShopService {
	ShopExecution addShop(Shop shop,InputStream shopImgInputStream,String fileName) throws ShopOperationException;
}
