package cn.imnu.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.imnu.o2o.BaseTest;
import cn.imnu.o2o.entity.Product;
import cn.imnu.o2o.entity.ProductCategory;
import cn.imnu.o2o.entity.Shop;

public class ProductDaoTest extends BaseTest{
	@Autowired
	private ProductImgDao productImgDao;
	@Autowired
	private ProductDao productDao;
	@Test
	public void testABatchInsertProduct() throws Exception{
		Shop shop=new Shop();
		shop.setShopId(1L);
		ProductCategory pc=new ProductCategory();
		pc.setProductCategoryId(1L);
		//初始化三个商品实例并添加进shopId为1的店铺里,
		//商品类别统一
		Product product1 = new Product();
		product1.setProductName("测试1");
		product1.setProductDesc("test1");
		product1.setImgAddr("test");
		product1.setPriority(1);
		product1.setEnableStatus(1);
		product1.setCreateTime(new Date());
		product1.setLastEditTime(new Date());
		product1.setShop(shop);
		product1.setProductCategory(pc);
		Product product2 = new Product();
		product2.setProductName("测试2");
		product2.setProductDesc("test2");
		product2.setImgAddr("test");
		product2.setPriority(2);
		product2.setEnableStatus(1);
		product2.setCreateTime(new Date());
		product2.setLastEditTime(new Date());
		product2.setShop(shop);
		product2.setProductCategory(pc);
		Product product3 = new Product();
		product3.setProductName("测试3");
		product3.setProductDesc("test3");
		product3.setImgAddr("test");
		product3.setPriority(3);
		product3.setEnableStatus(1);
		product3.setCreateTime(new Date());
		product3.setLastEditTime(new Date());
		product3.setShop(shop);
		product3.setProductCategory(pc);
		int effectedNum = productDao.insertProduct(product1);
		assertEquals(1,effectedNum);
		effectedNum = productDao.insertProduct(product2);
		assertEquals(1,effectedNum);
		effectedNum = productDao.insertProduct(product3);
		assertEquals(1,effectedNum);
	}
	@Test
	public void testDUpdateProduct() throws Exception{
		Product product = new Product();
		ProductCategory pc = new ProductCategory();
		Shop shop = new Shop();
		shop.setShopId(1L);
		pc.setProductCategoryId(2L);
		product.setProductId(1L);
		product.setShop(shop);
		product.setProductName("测试商品1");
//		product.setProductCategory(pc);
		int effectedNum = productDao.updateProduct(product);
		assertEquals(1,effectedNum);
		
	}
}