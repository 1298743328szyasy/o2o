/**
 * 1.从后台获取商店分类和区域等信息反馈给前台 2.将前台表单信息获取到转发到后台注册店铺
 */
$(function(){
	var initUrl='/o2o/shopadmin/getshopinitinfo';
	var registerShopUrl='/o2o/shopadmin/registershop';
	getShopInitInfo();
	function getShopInitInfo(){
		$.getJSON(initUrl,function(data){
			if(data.success){
				var tempHtml='';
				var tempAreaHtml='';
				data.shopCategoryList.map(function(item,index){
					tempHtml +='<option data-id="'+item.shopCategoryId+'">'+item.shopCategoryName+'</option>';
				});
				data.areaList.map(function(item,index){
					tempAreaHtml+='<option data-id="'+item.areaId+'">'+item.areaName+'</option>';
				});
				$('#shopCategory').html(tempHtml);
				$('#area').html(tempAreaHtml);
			}
		});
		$('#submit').click(function(){
			var shop = {};
			shop.shopName=$('#shop-name').val();
			shop.shopAddr=$('#shop-addr').val();
			shop.phone=$('shop-phone').val();
			shop.shopDesc=$('shop-desc').val();
			shop.shopCategory={
					shopCategoryId:$('#shop-category').find('option').not(function(){
						return !this.selected;
					}).data('id')
			};
			shop.area={
					areaId:$('#area').find('option').not(function(){
						return !this.selected;
					}).data('id')
			};
			var shopImg=$('#shop-img')[0].files[0];
			var formData = new FormData();
			formData.append('shopImg',shopImg);
			formData.append('shopStr',JSON.stringify(shop));
			var verifyCodeCodeActual = $('#j_captcha').val();
			if(!verifyCodeActual){
				$.toast('请输入验证码！');
				return;
			}
			$.ajax({
				url:registerShopUrl,
				type:'POST',
				data:formData,
				contentType:false,
				proceesData:false,
				cache:false,
				success:function(data){
					if(data.success){
						$.toast('提交成功!');
					}else{
						$.toast('提交失败!'+data.errMsg);
					}
					$('#captcha_img').click();
				}
			});
		});
	}
})
