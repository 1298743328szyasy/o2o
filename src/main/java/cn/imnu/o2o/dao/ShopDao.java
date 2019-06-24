package cn.imnu.o2o.dao;

import cn.imnu.o2o.entity.Shop;

public interface ShopDao {
	/**
	 * 通过shop id查询店铺
	 * 
	 * @param shopId
	 * @return
	 */
	Shop queryByShopId(long shopId);
	/**
	 * 新增店铺
	 * @param shop
	 * @return
	 */
	int insertShop(Shop shop);
	/**
	 * 更新店铺信息
	 * @param shop
	 * @return
	 */
	int updateShop(Shop shop);
	
}
