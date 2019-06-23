package cn.imnu.o2o.service;

import java.util.List;

import cn.imnu.o2o.entity.ShopCategory;

public interface ShopCategoryService {
	List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
	
}
