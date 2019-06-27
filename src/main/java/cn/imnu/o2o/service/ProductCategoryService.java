package cn.imnu.o2o.service;

import java.util.List;

import cn.imnu.o2o.dto.ProductCategoryExecution;
import cn.imnu.o2o.entity.ProductCategory;
import cn.imnu.o2o.exceptions.ProductCategoryOperationException;
/**
 * 查询指定某个店铺下所有商品类别信息
 * @author song
 *
 */
public interface ProductCategoryService {
	List<ProductCategory> getProductCategoryList(long shopId);
	/**
	 * 新增商品类别
	 * @param productCategoryList
	 * @return
	 * @throws ProductCategoryOperationException
	 */
	ProductCategoryExecution batchInsertProductCategory(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException;
	/**
	 * 将此类别下的商品里的类别id置为空，再删掉该商品类别
	 * @param productCategoryId
	 * @param shopId
	 * @return
	 * @throws ProductCategoryOperationException
	 */
	ProductCategoryExecution deleteProductCategory(long productCategoryId,long shopId) throws ProductCategoryOperationException;
}
