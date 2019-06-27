package cn.imnu.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.imnu.o2o.entity.ProductCategory;

public interface ProductCategoryDao {
	/**
	 * 通过商铺id查询店铺类别
	 * 返回该商铺下所有商品类别
	 * @param shopId
	 * @return
	 */
	List<ProductCategory> queryProductCategoryList(long shopId);
	/**
	 * 批量新增商品类别
	 * @param productCategoryList
	 * @return
	 */
	int batchInsertProductCategory(List<ProductCategory> productCategoryList);
	/**
	 * 删除指定商品类别
	 * @param productCategoryId
	 * @param shopId
	 * @return
	 */
	int deleteProductCategory(@Param("productCategoryId")long productCategoryId,@Param("shopId")long shopId);
}
