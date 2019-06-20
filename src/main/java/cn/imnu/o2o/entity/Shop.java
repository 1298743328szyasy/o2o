package cn.imnu.o2o.entity;

import java.util.Date;

public class Shop {
	
	private Long shopId;
	//店铺名
	private String shopName;
	//店铺描述
	private String shopDesc;
	//店铺地址
	private String shopAddr;
	//联系电话
	private String phone;
	//店铺图片
	private String shopImg;
	//店铺出现前后顺序
	private Integer priority;
	private Date createtime;
	private Date lastEditTime;
	//-1.不可用 0.审核中 1.可用 
	private Integer enableStatus;
	//管理员给商家的建议
	private String advice;
	//店铺所在区域
	private Area area;
	//店主
	private PersonInfo owner;
	//店铺类别
	private ShopCategory shopCategory;
	
}
