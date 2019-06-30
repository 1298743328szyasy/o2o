package cn.imnu.o2o.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.imnu.o2o.BaseTest;
import cn.imnu.o2o.dto.ImageHolder;
import cn.imnu.o2o.dto.ProductExecution;
import cn.imnu.o2o.entity.Product;
import cn.imnu.o2o.entity.ProductCategory;
import cn.imnu.o2o.entity.Shop;
import cn.imnu.o2o.enums.ProductStateEnum;
import cn.imnu.o2o.exceptions.ProductOperationException;

public class ProductServiceTest extends BaseTest{
	@Autowired
	private ProductService productService;
	@Test
	public void testAddProduct() throws ProductOperationException, FileNotFoundException, UnsupportedEncodingException{
		Product product = new Product();
		Shop shop=new Shop();
		shop.setShopId(1L);
		ProductCategory pc = new ProductCategory();
		pc.setProductCategoryId(1L);
		product.setShop(shop);
		product.setProductCategory(pc);
		product.setProductName("测试商品1");
		product.setProductDesc("test1");
		product.setPriority(20);
		product.setCreateTime(new Date());
		product.setEnableStatus(ProductStateEnum.SUCCESS.getState());
		//创建缩略图文件流
		File thumbnailFile=new File("/Program Files/dabai.jpg");
		InputStream is=new FileInputStream(thumbnailFile);
		ImageHolder thumbnail = new ImageHolder(thumbnailFile.getName(),is);
		File productImg1=new File("/Program Files/dabai.jpg");
		InputStream is1 = new FileInputStream(productImg1);
		File productImg2 = new File("/Program Files/xunlonggaoshou.jpg");
		InputStream is2 = new FileInputStream(productImg2);
		List<ImageHolder> productImgList=new ArrayList<ImageHolder>();
		productImgList.add(new ImageHolder(productImg1.getName(),is1));
		productImgList.add(new ImageHolder(productImg2.getName(),is2));
		ProductExecution pe = productService.addProduct(product, thumbnail, productImgList);
		assertEquals(ProductStateEnum.SUCCESS.getState(),pe.getState());
	}
}
