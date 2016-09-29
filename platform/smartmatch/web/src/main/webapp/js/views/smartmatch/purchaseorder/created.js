/**
 * Create by xuefei
 */
( function() {
	
	$("#search").verifyForm();

	$('.nsortName').inputFocus();
	$('.material').inputFocus();

	bindEvent();

	$("#purchaseFooter").hide();
	//设置采购单明细颜色
	resetPurchaseOrderItemsDisplay();
	

	/**
	 * 详情页与询价页头部切换
	 */
	if($("#isDetail").val()==1){
		
		//详情
		$("#detail_head").removeClass("none");
		$("#detail_tab").removeClass("none");
		$("#breadcrumbs").addClass("none");

		if (canAssignToBusi) {
			$("#btnAssignToBusi").removeClass("none");
		}
		if (canAssignToSale) {
			$("#btnAssignToSale").removeClass("none");
		}
		$("#btnCreateOrder").removeClass("none");
		 
	}else{
		
		//新增
		$("#detail_phone").empty();
		$("#detail_tab").addClass("none");
		$("#btnCreateOrder").addClass("none");
	}
	
	//根据询价单ID获取采购单详情信息，并初始化页面。
	if(!utils.isEmpty(purchaseOrder.poid)) {
		
		$("#id").val(purchaseOrder.poid);
		if(purchaseOrder.poid){
			//修改
			$("#detail_city").removeClass("none");
			$("#new_add_city").addClass("none");
			$("#new_add_phone").addClass("none");
		}else{
			//新增，poid空
			
			$("#detail_city").addClass("none");
			
			$("#new_add_city").removeClass("none");
			$("#new_add_phone").removeClass("none");
		}
		
		getPurchaseOrderItemsList(purchaseOrder.poid);
		$("#guidpic").hide();
		
		retrieve(purchaseOrder.poid);//init()
		if($("#id").val()){
			$("#bread_title").text("询价单详情");
		}
		if(orderStatus=='PENDING_CLERK_ACCEPTE'){
			$("#btn_reassignto").text(" 推给服总").removeClass("none");
		}else if(orderStatus=='PENDING_QUOTE'||orderStatus=='QUOTED'){
			$("#btn_reassignto").text(" 推给主管").removeClass("none");
		}
	}else{
		$("#btn_reassignto").addClass("none");
		$("#purchaseOrderClose").addClass("none");
	}

	//如果本次会话中已经取消了流程图的显示，则不显示流程图
	if(window.sessionStorage.hideGuide == undefined){
		$("div.guide").removeClass("none");
	}
}());

//查询出所有城市
(function(){
	$.ajax({
		type: "POST",
		url: Context.PATH+ '/common/allcity.html',
		dataType: "json",
		success: function (response, textStatus, xhr) {
			
        		_resAttr.city=response;
        		var cityId = $("#deliveryGoods").attr("val");
        	for(var i in response){
        		if(response[i].id==cityId){
        			$("#deliveryGoods").val(response[i].name);
        			break;
        		}
        	}
		}
	});

}());

$(document).on('input propertychange',"input[search='city']", function (e) {
	var input = this;
	showPYMatchList($(this),Context.CITYDATA,"id","name",function(){getRefCitys(input)});
//	$("#dropdown").css("z-index",9999);
	cbms.stopF(e);
});

$(document).on('click',"#showLayer_factory .textures-nav a[nav]", function (index,e) {
	$(this).parent().addClass("active").siblings().removeClass("active");
	var nav_index=$(this).attr("nav");
	$("#showLayer_factory .textures-con .factory-nav-"+nav_index).removeClass("none").siblings().addClass("none");
});

var setFoucsFirst = function(o) {
	if ($(o).closest(".m-search").find('.nsortName').val() == "") {
		$(o).closest(".m-search").find(".nsortName").focus();
		$(o).closest(".show-layer").hide();
		return true;
	}
};

$(document).on("focus", "#restcity", function() {
	if (setFoucsFirst(this))
		return;
	$(this).closest(".form-item").find(".show-layer").show();
});

/**
 * 页面事件绑定
 */
function bindEvent(){
	//查询复制
	$(document).on("click", ".clone-btn", function () {
		var c = $(this).closest(".m-search").clone(), len = 0;
		var craft = $(c).find(".craft-bar").attr("data");
		len = $(".m-search").length + 1;
		$(c).find(".craft-bar").find('input[type="radio"]').attr("name", "craft" + len);
		$(c).find(".performance-bar");
		$(c).find(".quality-bar");
		$(this).closest(".m-search").after(c);
		$(c).find(".spec .f-text").inputFocus();
		$(c).find("#showLayer_spec1 .clear-btn").click();
		$(c).find("#showLayer_spec1 .confirm-btn").click();
		$(".show-layer").hide();
		resetPurchaseOrderItemsDisplay();
	});

	//删除复制
	$(document).on("click", ".del-btn", function () {
		if ($("form#search .m-search").length > 1) {
			$(this).closest(".m-search").remove();
			resetPurchaseOrderItemsDisplay();
		} else {
			cbms.alert("最少需要保留一条资源");
		}
	});

	//隐藏引导图片
	$(document).on("click", ".hideGuide", function () {
		window.sessionStorage.hideGuide = true;
		$("div.guide").addClass("none");
	});

	/**
	 * 点击弹层以外的地方会使弹层消失
	 */
	$("body").click(function (event) {
		var t = event.target;
		if ($(t).closest('.form-item').length > 0 || $(t).closest("#dropdown").length>0) {
			return;
		}
		if($(t).attr("name")!="saveInquiryOrder"){
			purchaseOrder.clickedSaveInquiry = false;
		}
		if ($(t).closest('.show-layer').length <= 0) {
			$(".show-layer").hide();
		}
	});
	/**
	 * 触发公司名称下拉列表
	 */
	$(document).on("input", "#buyer", function (e) {
		var event = e || window.event;
		if (event.keyCode == 38 || event.keyCode == 40 || event.keyCode == 13) {
			return;
		}
		var buyerName = $.trim($("#buyer").val());

		getBuyer(buyerName);
	});

	/**
	 * 触发指定卖家名称下拉列表
	 */
	$(document).on("input", "#specificSeller", function (e) {
		var event = e || window.event;
		if (event.keyCode == 38 || event.keyCode == 40 || event.keyCode == 13) {
			return;
		}
		showPYMatchList($("#specificSeller"), _specificSeller, "uuid", "name");
	});
	/**
	 * 选择电话
	 */
	$(document).on("input", "#tel", function (e) {
		var event = e || window.event;
		if (event.keyCode == 38 || event.keyCode == 40 || event.keyCode == 13) {
			return;
		}
		var contactTel = $.trim($("#tel").val());

		// 大于3时配置电话列表
		if(!utils.isEmpty(contactTel) && contactTel.length>=3){
			getTel(contactTel);
		}
	});

	/**
	 * 选择公司
	 */
	$("body").on("click", "#purchasePlace input[name='country']", function () {
		if($(this).prop("checked")){
			$("#purchasePlace input[name='refcity']").attr("disabled", "disabled");
			$("#restcity").attr("disabled", "disabled");
		}else{
			$("#purchasePlace input[name='refcity']").removeAttr("disabled");
			$("#restcity").removeAttr("disabled");
		}
	});

	$("body").on("click", "#buyerDropdown ul li a", function () {
		var accountid = $(this).attr("accountid");
		$("#buyer").val($(this).attr("accountname"));
		$("#buyer").attr("val", accountid);
		$("#buyerDropdown").hide();
		getContact($(this).text());
	});

	/**
	 * 选择联系人
	 */
	$("body").on("click", "#contactDropdown ul li a", function () {
		$("#contact").val($(this).text());
		$("#contact").attr("contactid", $(this).attr("contactid"));
		$("#tel").val($(this).attr("tel"));
		$("#contactDropdown,#contactMsg").hide();

		//getAccount("", $(this).text());
	});

	/**
	 * 选择Tel
	 */
	$("body").on("click", "#telDropdown ul li a", function () {
		$("#tel").val($(this).text()).change();
		$("#contact").val($(this).attr("contactname"));
		$("#telDropdown,#contactMsg").hide();

		getAccount($(this).text(),undefined);
	});

	/**
	 * 选择其他城市
	 */
	$("body").on("click", "#showLayer_restcity span a", function () {
		if ($(this).hasClass("hover")) {
			//设置当前选中值
			$(this).removeClass("hover");
		} else {
			//设置当前选中值
			$(this).addClass("hover");
		}
//		$(this).closest(".form-item").find(".f-text").inputFocus();
//		$(this).closest(".form-item").find(".f-text").focus();
		cbms.stopF(event);
	});

	/**
	 * 点击保存
	 */
	$("body").on("click", "#btnSave", function () {
		save(true);
	});

	/**
	 * 点击重置
	 */
	$(document).on("click","#reset",function(){
		var form =$("#search");
		$(form).find("select,input[type='checkbox'],input[type='radio']").val("");
		$(form).find("input").not("input[id='id']," +
			"input[name='id']," +
			"input[id='sorganization']," +
			"input[id='sorganizationHidden'],"+
			"input[id='traderName']"
		).val("");
	});

	//保存并匹配卖家
	$(document).on("click","#btnSaveAndMatch",function(){
		
		save(false,function(){

			var params ={
				purchaseOrderId:$("#id").val(),
//				specificSellerId:specificSellerId
			};
			match(params,renderSearchResult);
//			$(".assign-bar, .purchase-footer").show();
		})

	});

	//指定卖家搜索
	$(document).on("click","#specificSearch",function(){
		var purchaseOrderId =$("#id").val();
		if(purchaseOrderId==""){
			cbms.alert("请先保存采购单");
			return false;
		}
//		var specificSeller = $("input[name='checkSeller']").is(":checked");
		var specificSellerId=null;
//		if(specificSeller){
			specificSellerId = $("#specificSeller").attr("val");
			if(!specificSellerId){
				cbms.alert("请指定一个卖家");
				return false;
			}
//		}else{
//			return false;
//		}
		//获取当高亮的城市页签
		var city_li = $("li[id^='cityLi_'][class='active']");
		var _cityIdStr = city_li.attr("id");
		var cityArray = _cityIdStr.split("_");
		var _cityId = null;
		if(cityArray.length==2){
			_cityId = cityArray[1];
		}
		
		var params ={
			purchaseOrderId:$("#id").val(),
			specificSellerId:specificSellerId,
			cityId:_cityId,
			isAppend:1
		};
		match(params,renderSearchResult,"specificAppend",_cityId);
		return false;
	});

	//改派
	$(document).on("click","#btn_reassignto",function(){
		var orderid = $("#id").val();
		if (orderid && orderid > 0) {
			$("#reassign_purchase_orderid").val(orderid);
			cbms.getDialog("改派理由","#reassign_dialog");
		}
		else{
			cbms.alert("未找到对应的询价单!");
		}
	});

	//开单
	$(document).on("click","#btnCreateOrder",function(){
		/*var href = "";
		if(requirementCode==null||requirementCode==""){
			 href =   $("#orderDomain").val() + '/order/'+$("#id").val()+'/create.html';
		}else{
			 href =   $("#orderDomain").val() + '/order/'+$("#id").val()+'/create.html?requirementCode='+requirementCode;
		}
		window.location.href =href;*/
		_openOrder($("#id").val(),requirementCode);
		
	});
	
	function _openOrder(purchaseOrderId,requestCode){
		cbms.loading();
		$.ajax({
			type: 'post',
			data: {
				"purchaseOrderId":purchaseOrderId,   //询价单id
				"requestCode":requestCode   //需求单号
			},
			url: Context.PATH + "/smartmatch/purchaseorder/preopenpid.html",
			error: function (s) {
				cbms.closeLoading();
			},
			success: function (result) {
				cbms.closeLoading();
				if(result.success) {
					if(result.success){
						var requrimentcode = result.data;
						url=$("#orderDomain").val()+"/order/"+purchaseOrderId+"/create.html";
						if(requrimentcode != null){
							url+='?requirementCode='+requrimentcode;
						}
						window.location.href=url;
					}else{
						cbms.alert(result.data);
					}
				}else{
					cbms.alert(result.data);
				}
			}
		});
	}
	
	//推送业务员
	$(document).on("click", "#btnAssignToBusi", function () {
		var orderid = $("#id").val();
		if (orderid && orderid > 0) {
			$("#assign_purchase_orderid").val(orderid);
			if (IsOrgManager == "true") {
				$("#assignto_manager_panel").hide();
			}
			cbms.getDialog("推送业务", "#assign_dialog");
		}
		else {
			cbms.alert("未找到对应的询价单!");
		}
	});
	//退回
	$(document).on("click", "#btnAssignToSale", function () {
		var orderid = $("#id").val();
		if (orderid && orderid > 0) {
			$("#assigntonetsale_purchase_orderid").val(orderid);
			initTraders(purchaseOrder.preOwnerId);
			cbms.getDialog("回退", "#assigntonetsale_dialog");
		}
		else {
			cbms.alert("未找到对应的询价单!");
		}
	});
	
	//当HTML高度小于视口高度提交订单按钮固定在底部
	function setHeight() {
		$(".purchase-footer").removeAttr("style");
		var browser_h = cbms.getTotalHeight(), sH = $(window).scrollTop(), html_h = $('body').innerHeight();
		var ro = $(".list_return")[0].getBoundingClientRect();
		var boxwidth = $(".inquiryOrders").width();
		if (ro.top+100 >= (browser_h)) {
			$(".purchase-footer").css({
				"position" : "fixed",
				"bottom" : "0px",
				"margin-bottom":"0px",
				"width" : boxwidth
				/*"left" : "0"*/
			});
		} else {
			$(".purchase-footer").removeAttr("style");
		}

	}

	$(window).scroll(function() {
		setTimeout(setHeight, 200);
	});

}

/**
 * 重设采购单明细的背景色
 */
function resetPurchaseOrderItemsDisplay(){
	$(".page-content .m-search").each(function(i, e){
		if(i % 2 == 0){
			$(e).attr("index", "odd");
		}else{
			$(e).attr("index", "even");
		}
	})
}

/**
 * 资源匹配
 * @param params
 */
function match(params,callback,sellerBar,sellerId){
	var cityId = params.cityId;
	cbms.loading();
	$.ajax({
		type : "POST",
		url : Context.PATH + "/smartmatch/match.html",
		data :params,
		dataType : "json",
		success : function(response) {
			
			if (response.success) {
//                cbms.alert(response.data);
//                cbms.closeDialog();
//                searchData();
				callback(response.data,sellerBar,sellerId,cityId);
			} else {
				cbms.alert(response.data);
			}
		
		},
		error : function(xhr, textStatus, errorThrown) {}
	});
}

/**
 * input 联动执行方法 获取中心城市的周边城市 或者 周边城市的中心城市
 * @param input
 * @param po 采购单信息
 * @returns {boolean}
 */
function getRefCitys(input, po){
	if($(input).attr("id")!="deliveryGoods"){
		return false;
	}
	var cityId=$('#deliveryGoods').attr("val");
	if(!cityId){
		return false;
	}
	$.ajax({
		type: "POST",
		url: Context.PATH + '/smartmatch/purchaseorder/getCitys.html',
		dataType: "json",
		data: {
			cityId:cityId
		},
		success: function (response, textStatus, xhr) {
			if (response.success) {
				var areaCity=response.data.city;
				var restCity=response.data.restCity;
				var isCenter=response.data.isCenter;
				var contentHtml="<label class='pos-rel'>";
				contentHtml+="<input type='checkbox' name='"+ purchaseOrder.el.refcity +"' value='"+cityId+"' class='ace' checked>"
				contentHtml+="<span class='lbl' style='margin:5px;'></span>"+$('#deliveryGoods').val();
				contentHtml+="</label>";
				if(areaCity){
					for(var i=0;i<areaCity.length;i++){
						contentHtml+="<label class='pos-rel'>"

						if(isCenter){
							contentHtml+="<input type='checkbox' name='"+ purchaseOrder.el.refcity+"' value='"+areaCity[i].centerCityId+"'  class='ace'>"
						}else{
							contentHtml+="<input type='checkbox' name='"+ purchaseOrder.el.refcity+"' value='"+areaCity[i].centerCityId+"' checked class='ace'>"
						}
						contentHtml+="<span class='lbl' style='margin:5px;'></span>"+areaCity[i].centerCityName
						contentHtml+="</label>";
					}
				}
				//其他城市
				if(restCity){
					$("#showLayer_restcity .textures-con-bar-list").empty();
					var h='';
					for(var i=0;i<restCity.length;i++){
						h+='<span><a href="javascript:;" cityid="'+restCity[i].id+'">'+restCity[i].name+'</a></span>';
					}
					$("#showLayer_restcity .textures-con-bar-list").append(h);
				}else{
					$("#showLayer_restcity .textures-con-bar-list").append("暂无城市数据");
				}

				if(po&&po.purchaseOrder.purchaseCityOtherId){
					$("#restcity").val(po.purchaseOrder.otherPurchaseCityName);
					$("#restcity").attr("cityids",po.purchaseOrder.purchaseCityOtherId);

					/**
					 * 其他城市div城市选中
					 */
					var all_rest_c=$("#showLayer_restcity .textures-con-bar-list a");
					var sel_rest_c=po.purchaseOrder.purchaseCityOtherId.split(",");

					$(all_rest_c).each(function(i,e){
						$(sel_rest_c).each(function(k,m){
							if($(e).attr("cityid")==m){
								$(e).addClass("hover");
								sel_rest_c.splice(k, 1);
							}
						});
					});
					if(sel_rest_c.length==1&&sel_rest_c!="0"){
						$("#restCheck")[0].checked=true;
						$("#restcity_spec").attr("cityid",sel_rest_c[0]);
						var last_c=$.grep(Context.CITYDATA,function(e,i){return e.id==sel_rest_c[0]});
						$("#restcity_spec").val(last_c[0].name);
					}
				}
				contentHtml+="<label class='pos-rel'>"
				contentHtml+="<input type='checkbox' name='country' value='' class='ace'>"
				contentHtml+="<span class='lbl' style='margin:5px;'></span>全国"
				contentHtml+="</label>";
				$("#purchasePlace span").empty();
				$("#purchasePlace span").append(contentHtml);

				// 选中相关城市
				if(po&&po.purchaseOrder&&!utils.isEmpty(po.purchaseOrder.purchaseCityIds)) {
					var purchaseCityIds = po.purchaseOrder.purchaseCityIds.split(",");
					for (i in purchaseCityIds) {
						$("input[name='refcity'][value='"+purchaseCityIds[i]+"']").attr("checked",'true');
					}
				}
			}
		}
	});
}

//点击其他城市下div的清除按钮
$(document).on("click", "#showLayer_restcity button.clear-btn", function() {
	$(this).closest(".show-layer").find(".textures-con span a").removeClass("hover");
	$(this).closest(".show-layer").find("input").val("");
	$(this).closest(".show-layer").find("input").removeAttr("val");
	$("#restCheck")[0].checked=false;
	$(this).closest(".form-item").find(".f-text").val("");
	$(this).closest(".form-item").find(".f-text").removeAttr("cityids");
	cbms.stopF(event);
	return false;
});

//点击其他城市下div的确认按钮
$(document).on("click", "#showLayer_restcity button.confirm-btn", function() {
	var val = "";
	var cityIds="";
	$(this).closest(".show-layer").find(".textures-con a.hover").each(function(i, e) {
		val += $(e).text();
		cityIds+=$(e).attr("cityid");
		if (i < $(this).closest(".show-layer").find(".textures-con a.hover").length - 1) {
			val += "/";
			cityIds+=",";
		}
	});
	if($("#restCheck")[0].checked && !utils.isEmpty($("#restcity_spec").attr("val"))){
		var restCity=$("#restcity_spec").val();
		var restId=$("#restcity_spec").attr("val");
		//采购地城市 id集
		var purCityIds=$("#purchasePlace input:checkbox").map(function () {
			return $(this).val();
		}).get();
		if(cityIds.split(",").indexOf(restId)<0 && purCityIds.indexOf(restId)<0){
			cityIds+=","+restId;
			val+="/"+restCity;
		}
	}
	//过滤多余字符
	if(cityIds.indexOf(",")==0 && val.indexOf("/")==0){
		cityIds=cityIds.substr(1);
		val=val.substr(1);
	}
//	$(this).closest(".form-item").find(".f-text").inputFocus();
	$(this).closest(".form-item").find(".f-text").val(val).attr("cityids",cityIds).focus();
	$(this).closest(".show-layer").hide();
	cbms.stopF(event);
	return false;
});
/**
 * 保存
 */
function save(needShowMsg,callback){
	if (!setlistensSave()) {
		return;
	}
	var city =  $("#deliveryGoods").attr("val");
	if(city==null || city==""){
		utils.showMsg("请先选择交货地", "", "error", 2000);
		return;
	}
	
	var quantityCheck = true;
	$("#m-search input[name='quantity']").each(function(){
		if($(this).val() === "0"){
			quantityCheck = false;
		}
	});
	if(!quantityCheck){
		utils.showMsg("件数不能为0", "", "error", 2000);
		return;
	}
	var items = bulidPurchaseOrderItems();
	if(!infoVerify(items)) {
		utils.showMsg("资源信息不完整", "", "error", 2000);
		return;
	}

	var updateBuyerContact = $("#info2_target_contact_name").val();
	if(updateBuyerContact&& updateBuyerContact!=""){
		$("#contact").val(updateBuyerContact);
	}

	cbms.loading();
	var buyerNameVal = $("#buyer").val();
	var buyerIdVal = $("#buyer").attr("val");
	var bid = null;
	if(buyerNameVal == ''){//名称为空,则id也取空
		bid = null;
	}else{ 
		if(buyerIdVal==''||buyerIdVal=='null'){//当前买家名称没有id
			bid = null;
		}else{
			bid = buyerIdVal;
		}
	}
	$.ajax({
		type: "post",
		url: Context.PATH + "/smartmatch/purchaseorder/save.html",
		data: {
			id: $("#id").val(),
			code: $("#poid").html()==null?"":$("#poid").html(),
			tel:$("#tel").val(),
			contact:$("#contact").val(),
			buyerId: bid,
			buyerName:$("#buyer").val(),
			deliveryCityId: $("#deliveryGoods").attr("val"),
			deliveryCityName: $("#deliveryGoods").val(),
			remark:$("#remark").val(),
			itemList: JSON.stringify(items)
		},
		success: function (result) {
			
			if (result && result.success) {
				
				getPurchaseOrderItemsList(result.data.id);
				var dt = result.data;
				$("#poid").text(dt.code);
				$("#id").val(dt.id);
				if(dt.created){
					var html  = (new Date(dt.created)).Format("yyyy-MM-dd hh:mm");
					$("#purchase_created").html(html );
				}
				
				//刷新报价数量及匹配的资源数量
				sellerNum(dt.id, false);
				if(needShowMsg){
					utils.showMsg("保存成功！", null, null, 1000);
				}
				if(callback){
					callback();
				}
				
			}else{
				utils.showMsg(result.data, '', 'error', 2000);
			}
		
		},
		error: function() {
			cbms.closeLoading();
		}
	});
}

function getPurchaseOrderItemsList(id){
	$.ajax({
		type: "post",
		url: Context.PATH + "/smartmatch/purchaseorder/"+id+"/itemlist.html",

		success: function (result) {
			//cbms.closeLoading();
			if (result && result.success) {
				purchaseOrder.purchaseOrderItemList = result.data;
			}else{
				utils.showMsg(result.data, '', 'error', 2000);
			}
		},
		error: function() {
			//cbms.closeLoading();
		}
	});
}
/**
 * 检查是否输入了采购资源信息
 * @param items
 * @returns {boolean}
 */
function infoVerify(items){
	if (items!=null && items.length==0) {
		return false;
	}

	return true;
}

/**
 * 构造采购资源信息
 * @returns {Array}
 */
function bulidPurchaseOrderItems(){
	var itemList = [];
	var b = true;
	$("div#m-search").each(function () {
		var attributeList = [];
		$(this).find("div.attrs").find("span.search-radio-bar").each(function(i, e){
			var value = [];
			var attributeUuid = $(e).find('input[name="attribute"]').val();
			var attributeType =  $(e).find('input[name="attribute"]').attr("element");
			switch(attributeType){
				case "radio":
					$(e).find('input[type="radio"]:checked').each(function(i, e){
						value.push($(e).val());
					});
					break;
				case "checkbox":
					$(e).find('input[type="checkbox"]:checked').each(function(i, e){
						value.push($(e).val());
					});
					break;
				case "select":
					$(e).find('select option:selected').each(function(i, e){
						value.push($(e).val());
					});
					break;
				case "input":
					value.push($(e).find('input[type="text"][name="'+attributeUuid+'"]').val());
					break;
			}
			var attribute = {
				attributeUuid :attributeUuid,
				value :value.join(',')
			};
			attributeList.push(attribute);
		});

		var _categoryUuid = $(this).find("#nsortName").attr("nsortid"),
			_categoryName = $(this).find("#nsortName").val(),
			_materialUuid = $(this).find("#material").attr("materialid"),
			_materialName = $(this).find("#material").val(),
			_spec1 = $(this).find("#inputspec1").val(),
			_spec2 = $(this).find("#inputspec2").val(),
			_spec3 = $(this).find("#inputspec3").val(),
			_factoryIds = $(this).find("#factory").attr("selectids"),
			_factorynames = $(this).find("#factory").val(),
			_weight = $(this).find("input[name='weight']").val(),
			_quantity = $(this).find("input[name='quantity']").val();
		_quantity = (_quantity == undefined || _quantity == "") ? 0 : _quantity;
		var _remark = $(this).find("input[name='detailRemark']").val();
		_remark = (_remark == undefined || _remark == null) ? "" : _remark;
		var item = {
			categoryUuid: _categoryUuid,
			categoryName: _categoryName,
			materialUuid: _materialUuid,
			materialName: _materialName,
			spec1: _spec1,
			spec2: _spec2,
			spec3: _spec3,
			factoryIds: _factoryIds,
			factoryNames:_factorynames,
			weight: _weight,
			quantity: _quantity,
			attributeList: attributeList,
			remark:_remark,
		};
		itemList.push(item);

	});
	return itemList;
}

/**
 * 重置信息
 * @param t
 */
function resetData(t) {
	$(".ipt-dynamic").each(function(){
		var id = $(this).attr("id");
		if(id!=t) {
			$(this).val("");
		}
	});
}

/**
 * 获取公司
 * @param userId
 * @param name
 */
function getBuyer(name) {
	var buyerDropdown = "buyerDropdown";
	$("#" + buyerDropdown).remove();
	//resetData("buyer");//重置
	if (!utils.isEmpty($.trim(name))) {
		showPYMatchList($("#buyer"), _account, "uuid", "name", function () {
			$("#buyerDropdown").hide();
			getContact($("#buyer").val());
		});
	}
}

/**
 * 获取联系人
 * @param accountId
 * @param name
 */
function getContact(name) {
	var contactDropdown = "contactDropdown";
	$("#" + contactDropdown).remove();
	//resetData("contact");

	$.ajax({
		type: 'post',
		url: Context.PATH + "/smartmatch/purchaseorder/fetchAccountContact.html",
		data: {
			name: name
		},
		error: function (s) {
		},
		success: function (result) {
			if (result && result.success) {
				if (result.data != null && $(result.data).length > 0) {
					//加载历史记录
					var r = true;
					$(result.data).each(function(i, e){
						if(e.tel == $("#tel").val()){
							r = false;
						}
					});
					if(r){
						//cbms.loading();
						getHistory(undefined, name);
						//cbms.closeLoading();
					}

					var contactDropdownHtml = "<div id='" + contactDropdown + "' class='show-layer' style='width:150px'><ul>";
					for (var i = 0; i < $(result.data).length; i++) {
						// 如果刚好匹配就直接选中，不出现下拉列表

						if (name == result.data[i].name || result.data.length==1) {
							$("#contact").val(result.data[i].name);
							$("#contact").attr("contactid", result.data[i].id);
							$("#tel").val(result.data[i].tel);
							$("#contactMsg").hide();
							return;
						}

						contactDropdownHtml += "<li><a href='javascript:;' tel='" + result.data[i].tel + "' contactid='" + result.data[i].id + "'>" + result.data[i].name + "</a></li>";
					}
					contactDropdownHtml += "</ul></div>";
					$("body").append(contactDropdownHtml);
					setLayerPosition("#contact", $("#" + contactDropdown));
					controlLayerShow($("#" + contactDropdown));
				}
			}
		}

	});
}

/**
 * 获取电话列表
 * @param tel
 */
function getTel(tel) {
	var telDropdown = "telDropdown";
	$("#" + telDropdown).remove();
	//resetData("tel");

	$.ajax({
		type: 'post',
		url: Context.PATH + "/smartmatch/purchaseorder/fetchAccountContact.html",
		data: {
			tel: tel
		},
		error: function (s) {
		},
		success: function (result) {
			if (result && result.success) {
				if (result.data != null && $(result.data).length > 0) {
					var telDropdownHtml = "<div id='" + telDropdown + "' class='show-layer' style='width:110px;max-height: 300px;overflow-y: auto;overflow-x: hidden;'><ul>";
					for (var i = 0; i < $(result.data).length; i++) {
						// 如果刚好匹配就直接选中，不出现下拉列表
						if (tel == result.data[i].tel) {
							$("#tel").val(result.data[i].tel);
							$("#contact").val(result.data[i].name);
							getAccount(tel, undefined);
							return;
						}

						telDropdownHtml += "<li><a href='javascript:;' contactname='" + result.data[i].name + "' contactid='" + result.data[i].id + "'>" + result.data[i].tel + "</a></li>";
					}
					telDropdownHtml += "</ul></div>";
					$("body").append(telDropdownHtml);
					setLayerPosition("#tel", $("#" + telDropdown));
					controlLayerShow($("#" + telDropdown));
				}
			}
		}

	});
}

/**
 * 根据电话/名称获取公司
 * @param tel
 * @param name
 */
function getAccount(tel, name){
	var buyerDropdown = "buyerDropdown";
	$("#" + buyerDropdown).remove();
	//resetData("buyer");//重置
	$.ajax({
		type: 'post',
		url: Context.PATH + "/smartmatch/purchaseorder/fetchAccount.html",
		data: {
			tel:tel,
			name: name
		},
		success: function (result) {
			if (result && result.success) {
				if ($(result.data) != null && $(result.data).length > 0) {
					//加载历史记录
					var r = true;
					$(result.data).each(function(i, e){
						if(e.name == $("#buyer").val() && $("#buyer").val() != ""){
							r = false;
						}
					});
					if(r){
						//cbms.loading();
						getHistory(tel);
						//cbms.closeLoading();
					}

					var buyerDropdownHtml = "<div id='" + buyerDropdown + "' class='product-complete'>";
					buyerDropdownHtml += "<ul>";
					for (var i = 0; i < $(result.data).length; i++) {
						// 如果刚好匹配就直接选中，不出现下拉列表
						if (name == result.data[i].name || result.data.length == 1) {
							$("#buyer").val(result.data[i].name);
							$("#buyer").attr("val", result.data[i].id);
							return;
						}
						buyerDropdownHtml += "<li><a href='javascript:;' accountid='" + result.data[i].id + "' accountname='" + result.data[i].name + "' >"
						+ result.data[i].name + "</a></li>";
					}
					buyerDropdownHtml += "</ul></div>";
					$("body").append(buyerDropdownHtml);
					setLayerPosition($("#buyer"), $("#" + buyerDropdown));
					controlLayerShow($("#" + buyerDropdown));
				}
			}
		},
		error: function (s) {
		}
	});
}

/**
 * 加载用户最近2次历史采购数据
 * @param tel
 * @param accountName
 */
function getHistory(tel, accountName){
	cbms.loading();
	$.ajax({
		url: Context.PATH + "/smartmatch/purchaseorder/getHistory.html",
		type: "POST",
		dataType: "json",
		data: {
			tel: tel,
			accountName: accountName
		},
		success: function (response, textStatus, xhr) {
			if (response.data && response.success) {
				renderItems(response.data, (tel==undefined?"showTel":"showAccount"));
			}else{
				cbms.closeLoading();
			}
		},
		error: function (xhr, textStatus, errorThrown) {
			//TODO: 错误处理。
			cbms.closeLoading();
		}
	});
}

/**
 * 根据询价ID获取采购单详情信息
 *
 * @param id 询价单ID
 */
function retrieve(id){
	cbms.loading()
	$.ajax({
		url: Context.PATH + "/smartmatch/purchaseorder/retrieve/"+id+".html",
		type: "POST",
		dataType: "json",
		data: {},
		success: function (response, textStatus, xhr) {
			cbms.closeLoading();
			if (response.data && response.success) {
				//TODO: 解析数据并初始化页面。
				init(response.data);
			}else{
				cbms.alert(response.data);
			}
		},
		error: function (xhr, textStatus, errorThrown) {
			cbms.closeLoading();
		}
	});
}

function renderItems(items, showDropDown,isInit){
	$("div.m-s").empty();
	//表体
	for (var i in items) {
		$("div.m-s").append($(".base_m_search").html());
		var newDiv = $("#base_m_search");
		var spec2=null, minSpec2=null, maxSpec2=null, spec3=null, minSpec3=null, maxSpec3=null;
		$(newDiv).attr("id", "m-search");
		if (items[i].spec2 != undefined && items[i].spec2.indexOf("-") >= 0) {
			var temp = items[i].spec2.split("-");
			spec2 = "";
			minSpec2 = temp[0];
			maxSpec2 = (temp[1] == undefined ? "" : temp[1]);
		} else {
			spec2 = items[i].spec2;
		}
		if (items[i].spec3 != undefined && items[i].spec3.indexOf("-") >= 0) {
			var temp = items[i].spec3.split("-");
			spec3 = "";
			minSpec3 = temp[0];
			maxSpec3 = (temp[1] == undefined ? "" : temp[1]);
		} else {
			spec3 = items[i].spec3;
		}
		var html = '<div id="forRenderItems">' +
			'<input type="hidden" id="nsortloadok" sortID="'+ items[i].sortUuid + '" nsortID="' + items[i].categoryUuid + '"/>' +
			'<input type="hidden" id="materialloadok" material="' + items[i].materialUuid + '"/>' +
			'<input type="hidden" id="specloadok" spec1="' + items[i].spec1 + '" spec2= "' + checkNull(spec2) + '" minSpec2="' + checkNull(minSpec2) + '" maxSpec2="' + checkNull(maxSpec2) + '" spec3="' + checkNull(spec3) + '" minSpec3="' + checkNull(minSpec3) + '" maxSpec3="' + checkNull(maxSpec3) + '"/> ' +
			'<input type="hidden" id="factroyloadok" factoryIds="' + (items[i].factoryIds == null ? "" : items[i].factoryIds) + '" factoryNames="' + items[i].factoryNames + '"/>' +
			'<input type="hidden" id="attrloadok"/><textarea id="attribute" class="none">' + JSON.stringify(items[i].attributeList) + '</textarea>' +
			'<input type="hidden" id="isallok" show="' + showDropDown + '"/>' +
			'</div>';
		$(newDiv).append(html);
		$(newDiv).find("#nsortName").click();
		//重量
		$(newDiv).find(".weight-bar input[name='weight']").val(items[i].weight);
		//件数
		$(newDiv).find(".num-bar input[name='quantity']").val(items[i].quantity == 0 ? "" : items[i].quantity);
		//备注
		$(newDiv).find("input[id='detailRemark']").eq(0).val(items[i].remark == null ? "" : items[i].remark);
		if(items[i].remark == null || items[i].remark == ""){
    		$(newDiv).find("#remarkLab").addClass("none");
		}else{
		    $(newDiv).find("input[id='detailRemark']").eq(0).val(items[i].remark);
		    $(newDiv).find("#remarkLab").removeClass("none");
		}
		//只在初始化的时候执行该代码
		if(isInit){
			var itemdiv =$("div.m-s").find(".m-search"); 
			//因为初始化的时候，spec2 sepc3 回去获取GetSpec方法，根据品名材质以及规格属性（长宽高）获取spec spec2 spec3的一个列表，但是没有将值范围取值填充进去
			//下面的代码主要是在取值为范围的时候，将范围的值在初始化完成以后，重新将值填入到相应的输入框中去
			var  spec2it = $(itemdiv).eq(i).find(".spec").find("#spec2");
			if(spec2it!=null && spec2it.length>0){
				var spec2Obj = $(itemdiv).eq(i).find(".spec").find("#spec2").find("#showLayer_spec2");
				if(minSpec2!=null && minSpec2!=undefined  && maxSpec2!=null &&  maxSpec2!=undefined ){
					
					spec2Obj.find("input").eq(0).val(minSpec2);
					spec2Obj.find("input").eq(1).val(maxSpec2);
					spec2Obj.find(".confirm-btn").click();
				}
				var selectedCount = $(itemdiv).eq(i).find(".spec").find("#spec2").find("#showLayer_spec2").find(".textures-con").find(".hover");
				if(selectedCount==null || selectedCount==undefined|| selectedCount.length<1){
					if(spec2!=null && spec2!=undefined && ""!=spec2){
						spec2Obj.find("input").eq(0).val(spec2);
						spec2Obj.find("input").eq(1).val("");
						spec2Obj.find(".confirm-btn").click();
					}else{
						spec2Obj.find("input").eq(0).val("");
						spec2Obj.find("input").eq(1).val("");
						spec2Obj.find(".confirm-btn").click();
					}
				}
			}
		
			var  spec3it = $(itemdiv).eq(i).find(".spec").find("#spec3");
			if(spec3it!=null && spec3it.length>0){
				var spec3Obj = $(itemdiv).eq(i).find(".spec").find("#spec3").find("#showLayer_spec3");
				if(minSpec3!=null && minSpec3!=undefined  && maxSpec3!=null &&  maxSpec3!=undefined ){
					spec3Obj.find("input").eq(0).val(minSpec3);
					spec3Obj.find("input").eq(1).val(maxSpec3);
					spec3Obj.find(".confirm-btn").click();
				}
				var selectedCount = $(itemdiv).eq(i).find(".spec").find("#spec3").find("#showLayer_spec3").find(".textures-con").find(".hover");
				if(selectedCount==null || selectedCount==undefined|| selectedCount.length<1){
					if(spec3!=null && spec3!=undefined && ""!=spec3){
						spec3Obj.find("input").eq(0).val(spec3);
						spec3Obj.find("input").eq(1).val("");
						spec3Obj.find(".confirm-btn").click();
					}else{
						spec3Obj.find("input").eq(0).val("");
						spec3Obj.find("input").eq(1).val("");
						spec3Obj.find(".confirm-btn").click();
					}
				}
			}
		}
	}
}

/**
 * 页面数据初始化
 * @param data
 */
function init(data){
	//设置采购单表头
	renderPOHeader(data);
	//设置采购单表体
	renderItems(data.purchaseOrderItems,null,true);
	//设置询价单内容+最新匹配内容
	//renderSearchResult(data.searchResult);
	$(window).scroll();
}

function renderPOHeader(data){

	//默认值
	$("#poid").html(data.purchaseOrder.code);
	$("#purchase_created").html((new Date(data.purchaseOrder.created)).Format("yyyy-M-d hh:mm") );
	$("#tel").val(data.purchaseOrder.tel);
	$("#contact").val(data.purchaseOrder.contact)
	$("#buyer").val(data.purchaseOrder.buyerName);
	$("#buyer").attr("val",data.purchaseOrder.buyerId);
	$("#deliveryGoods").val(data.purchaseOrder.deliveryName);
	$("#deliveryGoods").attr("val",data.purchaseOrder.deliveryCityId);
	getRefCitys($("#deliveryGoods"), data);
	$("#remark").val(data.purchaseOrder.remark);
	//详情：$("#detail_city").removeClass("none");
	$("#detail_city").html(data.purchaseOrder.deliveryName);
	$("#detail_phone").html(data.purchaseOrder.tel+"<em></em>");
	sellerNum(data.purchaseOrder.id,true);
}

$(document).on("change","#nsortloadok", function(){
	if($(this).val() == "done"){
		$("#showLayer_nsortName .product-t-ul li a[sortid=" + $(this).attr("sortID") + "]").parent().trigger('mouseenter');
		$("#showLayer_nsortName span a[nsortid='" + $(this).attr("nsortID") + "']").click();
		$(this).closest("#forRenderItems").find("#isallok").attr("nsortok", "ok").change();
	}
});
$(document).on("change","#materialloadok", function(){
	if($(this).val() == "done"){
		var input = $(this).closest(".m-search");
		$(input).find("#showLayer_material .textures-con a").removeClass("hover");
		var arr = $(this).attr("material").split(",");
		$(arr).each(function (i, e) {
			$(input).find("#showLayer_material .textures-con a").each(function (j, f) {
				if ($(f).attr("materialid") === e) {
					$(f).click();
				}
			});
		});
		$(input).find("#showLayer_material button.confirm-btn").click();
		$(this).closest("#forRenderItems").find("#isallok").attr("materialok", "ok").change();
	}
});
$(document).on("change","#specloadok", function(){
	if($(this).val() == "done"){
		var input = $(this).closest(".m-search");
		var spec1 = $(this).attr("spec1"), spec2 = $(this).attr("spec2"), maxSpec2 = $(this).attr("maxSpec2"), minSpec2 = $(this).attr("minSpec2");
		var spec3 = $(this).attr("spec3"), maxSpec3 = $(this).attr("maxSpec3"), minSpec3 = $(this).attr("minSpec3");
		//规格1
		$(input).find("#showLayer_spec1 .textures-con a").removeClass("hover");
		$(input).find("#inputspec1").val("");
		var arr = spec1.split(",");
		$(arr).each(function (i, e) {
			$(input).find("#showLayer_spec1 .textures-con a").each(function (j, f) {
				if ($(f).text() === e) {
					$(f).click();
					remove(arr, e);
				}
			});
		});
		$(input).find("#otherspec1").val(arr.join(","));
		$(input).find("#showLayer_spec1 button.confirm-btn").click();
		$(input).find("#inputspec1").blur();

		//规格2
		$(input).find("#inputspec2").click();
		$(input).find("#showLayer_spec2 .textures-con a").removeClass("hover");
		$(input).find("#inputspec2").val("");
		$(input).find("#from_spec2").val("");
		$(input).find("#to_spec2").val("");
		if (spec2 !== undefined && spec2 !== "") {
			var arr = spec2.split(",");
			$(arr).each(function (i, e) {
				$(input).find("#showLayer_spec2 .textures-con a").each(function (j, f) {
					if ($(f).text() === e) {
						$(f).click();
					}
				});
			});
			$(input).find("#inputspec2").val(spec2);
			$(input).find("#inputspec2").keydown();
			$(input).find(".show-layer").hide();
		} else {
			if (minSpec2 !== undefined && minSpec2 !== "" && minSpec3 !== "undefined") {
				$(input).find("#from_spec2").val(minSpec2);
			}
			if (maxSpec2 !== undefined && maxSpec2 !== "" && minSpec3 !== "undefined") {
				$(input).find("#to_spec2").val(maxSpec2);
			}
			$(input).find("#showLayer_spec2 button.confirm-btn").click();
		}

		//规格3
		$(input).find("#inputspec3").click();
		$(input).find("#showLayer_spec3 .textures-con a").removeClass("hover");
		$(input).find("#inputspec3").val("");
		$(input).find("#from_spec3").val("");
		$(input).find("#to_spec3").val("");
		if (!utils.isEmpty(spec3)) {
			var arr = spec3.split(",");
			$(arr).each(function (i, e) {
				$(input).find("#showLayer_spec3 .textures-con a").each(function (j, f) {
					if ($(f).text() === e) {
						$(f).click();
					}
				});
			});
		} else {
			if (minSpec3 !== undefined && minSpec3 !== "" && minSpec3 !== "undefined") {
				$(input).find("#from_spec3").val(minSpec3);
			}
			if (maxSpec3 !== undefined && maxSpec3 !== "" && maxSpec3 !== "undefined") {
				$(input).find("#to_spec3").val(maxSpec3);
			}
			$(input).find("#showLayer_spec3 button.confirm-btn").click();
		}
		$(this).closest("#forRenderItems").find("#isallok").attr("specok", "ok").change();
	}
});
$(document).on("change","#factroyloadok", function(){
	if($(this).val() == "done"){
		var input = $(this).closest(".m-search");
		var factoryIds = $(this).attr("factoryIds"), factoryNames = $(this).attr("factoryNames");
		$(input).find("#showLayer_factory .textures-con a").removeClass("hover");
		var arr = factoryIds.split(",");
		var names = factoryNames.split(",");
		$(arr).each(function (i, e) {
			$(input).find("#showLayer_factory .textures-con a").each(function (j, f) {
				if ($(f).attr("factoryid") === e) {
					$(f).click();
					remove(arr, e);
					remove(names, $(f).text());
					return false;
				}
			});
		});
		if(arr.length > 0) {     //其他城市
			$(input).find("#showLayer_factory input[name=otherFactoryIds]").val(names.join(","));
			$(input).find("#showLayer_factory input[name=otherFactoryIds]").attr("val", arr.join(","));
		}
		$(input).find("#showLayer_factory .confirm-btn").click();
		$(this).closest("#forRenderItems").find("#isallok").attr("factroyok", "ok").change();
	}
});
$(document).on("change","#attrloadok", function(){
	if($(this).val() == "done"){
		var input = $(this).closest(".m-search");
		$(eval("(" + $(input).find("#forRenderItems #attribute").val() + ")")).each(function (i, e) {
			var values = [];
			if(e.value != null) {
				values = e.value.split(',');
			}
			$(values).each(function(j, f){
				switch (e.type) {
					case "radio":
						$(input).find("input[type='radio']").each(function(){
							if(f==$(this).val()){
								$(this).attr("checked", "checked");
							}
						});
						break;
					case "checkbox":
						$(input).find("input[type='checkbox'][name='" + e.uuid + "']").each(function(){
							if(f==$(this).val()){
								$(this).attr("checked", "checked");
							}
						});
						break;
					case "select":
						$(input).find("select[name='" + e.uuid + "']").find("option[value='" + f + "']").attr("selected", "selected");
						break;
					case "input":
						$(input).find("input[type='text'][name='"+ e.uuid+"']").val(f);
						break;
				}
			});
		});
		$(this).closest("#forRenderItems").find("#isallok").attr("attrok", "ok").change();
	}
});
$(document).on("change","#isallok", function(){
	if($(this).attr("nsortok") == "ok" &&
		$(this).attr("materialok") == "ok" &&
		$(this).attr("specok") == "ok" &&
		$(this).attr("factroyok") == "ok" &&
		$(this).attr("attrok") == "ok"){
		$(this).val("ok");
	}
	var isallok = true;
	$("#isallok").each(function () {
		if($(this).val() !== "ok"){
			isallok = false;
		}
	});
	if(isallok){
		cbms.closeLoading();
		if($(this).attr("show") == "showTel"){
			$("#telDropdown").show();
		}
		if($(this).attr("show") == "showAccount"){
			$("#buyerDropdown").show();
		}
		$(this).closest("#forRenderItems").remove();
		//设置采购单明细背景色
		resetPurchaseOrderItemsDisplay();
	}
});
