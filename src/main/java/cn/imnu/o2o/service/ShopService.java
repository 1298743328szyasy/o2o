package cn.imnu.o2o.service;

import java.io.File;

import cn.imnu.o2o.dto.ShopExecution;
import cn.imnu.o2o.entity.Shop;

public interface ShopService {
	ShopExecution addShop(Shop shop,File shopImg);
}
