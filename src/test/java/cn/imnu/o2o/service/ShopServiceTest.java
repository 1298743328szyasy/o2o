package cn.imnu.o2o.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.imnu.o2o.BaseTest;
import cn.imnu.o2o.dto.ShopExecution;
import cn.imnu.o2o.entity.Area;
import cn.imnu.o2o.entity.PersonInfo;
import cn.imnu.o2o.entity.Shop;
import cn.imnu.o2o.entity.ShopCategory;
import cn.imnu.o2o.enums.ShopStateEnum;
import cn.imnu.o2o.exceptions.ShopOperationException;

public class ShopServiceTest extends BaseTest{
	@Autowired
	private ShopService shopService;
	@Test
	public void testGetShopList() {
		Shop shopCondition = new Shop();
		ShopCategory sc = new ShopCategory();
		sc.setShopCategoryId(1L);
		shopCondition.setShopCategory(sc);
		ShopExecution se=shopService.getShopList(shopCondition, 2, 5);
		System.out.println("店铺列表数为:"+se.getShopList().size());
		System.out.println("店铺总数为:"+se.getCount());
	}
	@Test
	@Ignore
	public void testModifyShop() throws ShopOperationException,FileNotFoundException{
		Shop shop = shopService.getByShopId(1L);
		shop.setShopId(1L);
		shop.setShopName("修改后的店铺名称");
		File shopImg=new File("D:\\Program Files/dabai.jpg");
		InputStream is = new FileInputStream(shopImg);
		ShopExecution shopExecution = shopService.modifyShop(shop,is,"dabai.jpg");
		System.out.println("新的图片地址为"+shopExecution.getShop().getShopImg());
	}
	@Test
	@Ignore
	public void testAddShop() throws FileNotFoundException {
		Shop shop = new Shop();
		PersonInfo owner = new PersonInfo();
		Area area = new Area();
		ShopCategory shopCategory = new ShopCategory();
		owner.setUserId(1L);
		area.setAreaId(2);
		shopCategory.setShopCategoryId(1L);
		shop.setOwner(owner);
		shop.setArea(area);
		shop.setShopCategory(shopCategory);
		shop.setShopName("测试的店铺3");
		shop.setShopDesc("test1");
		shop.setShopAddr("test1");
		shop.setPhone("test1");
		shop.setCreatetime(new Date());
		shop.setEnableStatus(ShopStateEnum.CHECK.getState());
		shop.setAdvice("审核中");
		File shopImg=new File("D:\\Program Files/timg.jpg");
		InputStream is = new FileInputStream(shopImg);
		ShopExecution se = shopService.addShop(shop,is, shopImg.getName());
		assertEquals(ShopStateEnum.CHECK.getState(),se.getState());
	}
}
