package cn.imnu.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.imnu.o2o.entity.Product;

public interface ProductDao {
	/**
	 * 查询商品列表并分页,输入商品名  商品状态  店铺Id 商品类别
 	 * @param productCondition
	 * @param pageSize
	 * @return
	 */
	List<Product> queryProductList(@Param("productCondition")Product productCondition,@Param("pageSize")int pageSize);
	/**
	 * 查询对应商品总数
	 * @param product
	 * @return
	 */
	Product queryProductByProductId(long product);
	/**
	 * 插入商品
	 * @param product
	 * @return
	 */
	int insertProduct(Product product);
	/**
	 * 更新商品信息
	 * @param product
	 * @return
	 */
	int updateProduct(Product product);
}