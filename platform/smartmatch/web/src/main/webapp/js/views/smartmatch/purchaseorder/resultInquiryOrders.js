
var _saveNum=0;
var _htmlString='';
var _inquiryOrder={};
var _factoryData=[];
var _warehouseData=[];
var _noExistWarehouseName;
var _factoryData;

var _itemAttributes=[];//item选项设置
var _itemCloset=[];//新增修改的节点


jQuery(function ($) {

	$(document).on("click",".cpshow",function(){
		$(".appendResDiv").html("");
		$(".itemsShow").hide();
		$(".itemsShow").closest(".menuContent").find(".cp_msg_detail").addClass("none");
		$(".compDivhead").addClass("cpshow");
		$(this).removeClass("cpshow");
		$(this).next(".itemsShow").show();
		$("input[name='is_item_hide']").val(0);
		$(this).find("input[name='is_item_hide']").val(1);
		$(this).find(".cp_msg_detail").removeClass("none");

		// 所有当前城市下的询价状态显示,当前的询价状态隐藏

		$(this).closest(".compDiv").find(".isinquiredshow").removeClass("none");
		$(this).find(".isinquiredshow").addClass("none");
		// 屏蔽报价按钮
		$(this).closest(".compDiv").find(".isSaveSellerdiv").addClass("none");
		$(this).find(".isSaveSellerdiv").removeClass("none");
		//加载公司联系人
		findCpmContacts($(this).find(".cp_msg_detail").eq(0));

		//初始化导入匹配
		openSellerInit($(this).closest(".seller-barl"));
	});

	if (orderStatus == 'BILLED' || orderStatus == 'CLOSED') {
		$("#purchaseOrderClose").hide();
	}

	/**
	 * 资源编辑时，厂家联动
	 */
	// $(document).on('input propertychange','#addorupdate-factoryName', function () {
    //
	// 	showPYMatchList($(this),_factory,"id","name",null);
    //
	// });
	/**
	 * 资源编辑时，厂家联动
	 */
	$(document).on('input propertychange','#addorupdate-category', function () {

		//showPYMatchList($(this),_nsort,"id","name",null);
		var input = $(this);
		showPYMatchList(input, _nsort, 'uuid', 'name', function(){
			$(_nsort).each(function (i, e) {
				if(e.name == $(input).val()) {
					$(input).attr("nsortid", e.uuid);
				}
			});

			loadData(input,'#addorupdate-material');
		});


	});

	/**
	 * 资源编辑时，仓库选中后的回调函数，选中仓库后获取仓库所在城市并填入城市中 
	 */
	function findWhId(){
		var whId = parseInt($('#addorupdate-warehouse').attr("val"));
		$(_resAttr.warehouseCacheData).each(function (i, e) {
			if(e.id==whId){
				$(_resAttr.city).each(function(j,w){
					if(w.id==e.cityId){
						$("#addorupdate-cityName").attr("val",e.cityId);
						$("#addorupdate-cityName").val(w.name);
						return false;
					}
				})
				return false;
			}
		});
	}
	/**
	 * 资源编辑时，仓库
	 */
	$(document).on('input propertychange','#addorupdate-warehouse', function () {

		showPYMatchList($(this),_resAttr.warehouseCacheData,"id","name",findWhId);
		$("#dropdown").css("z-index",9999);
		
	});

	/**
	 * 资源编辑时，城市
	 */
	$(document).on('input propertychange','#addorupdate-cityName', function () {

		showPYMatchList($(this),_resAttr.city,"id","name",null);
		$("#dropdown").css("z-index",9999);

	});
	/**
	 * 资源编辑时，城市
	 */
	$(document).on('input propertychange','#deliveryGoods', function () {
		showPYMatchList($(this),_resAttr.city,"id","name",null);
		$("#dropdown").css("z-index",9999);

	});

	$(document).on("click",".btnClose",function(){

		cbms.closeDialog();
	});
	getWarehouseData();





	//关闭
	$(document).on("click", "#purchaseOrderClose", function () {
		var ele = '<div class="dialog-m" id="dialog">' +
			'<p><label>关闭原因　　：</label><select id="reasonSelect"><option value="0">价格不合适</option><option value="1">没找到货</option>' +
			'<option value="2">交货地不合适</option><option value="3">货不全</option><option value="4">其他</option></select></p>' +
			'<p id="reasonDiv"><label>输入其他原因：</label><textarea id="reason"></textarea></p>' +
			'<div class="btn-bar text-center"><button id="commit" type="button" class="btn btn-primary btn-sm">关闭</button>&nbsp;<button type="button" class="btn btn-default btn-sm" id="cancel">取消</button></div></div>';
		cbms.getDialog("采购单关闭", ele);

		$("#reasonDiv").hide();

		$("#reasonSelect").change(function () {
			var select = $('#reasonSelect').children('option:selected').val();
			if (select != 4) {
				$("#reasonDiv").hide();
			} else {
				$("#reasonDiv").show();
			}
		});
		$("#dialog").on("click", "#cancel", function () {
			cbms.closeDialog();
		});

		$("#dialog").on("click", "#commit", function () {
			var selected = $("#reasonSelect").children("option:selected");
			var reason;
			if($(selected).val() == 4) {
				reason = $("#reason").val();
			}else{
				reason = $(selected).html();
			}
			if(reason == ""){
				utils.showMsg("请填写关闭理由", null, "error", 2000);
				return false;
			}
			$.ajax({
				type: 'post',
				url: Context.PATH + "/smartmatch/purchaseorder/"+ $("#id").val() +"/close.html",
				data: {
					reason: reason
				},
				error: function (s) {
				},
				success: function (result) {
					if(result.success) {
						utils.showMsg("关闭成功", null, null, 2000);
						cbms.closeDialog();
						window.location.href=Context.PATH +"/smartmatch/purchaseorder/list.html";
					}else{
						cbms.alert(result.data);
					}
				}
			});
		});
	});




	/**
	 * 保存询价单的操作，吨位不满足时，继续补充匹配
	 */
	$(document).on("click","button[name='saveInquiryOrder']",function(){

		if(!$(this).verifyDiv(this)){
			return;
		}

		purchaseOrder.clickedSaveInquiry = true;
		_noExistWarehouseName=[];
		var sellerBar = null;// 用于保存后返回的询价单ID保存
		var inqueryOrderId = null;

		var append  =$(this).closest(".appendResDiv");//判断是否是补充匹配里面的 
		if(append!=null && append!=undefined && append.length>0){
			inqueryOrderId = $(this).closest(".menuContent").find("input[name='res_inqueryOrderId']").val();
		}else{
			inqueryOrderId = $(this).closest(".menuContent").find("input[name='res_inqueryOrderId']").eq(0).val();
		}


		var isSaveOrder=false;
		var _inquirySeller=[];
		//itemInfo, item-resInfo
		//获取组合询价信息
		var form_valid=true;
		var  e = $(this).closest(".seller-barl");

		var _inquiryItem=[];
		//是否保存询价单
		var isSaveSeller= $(e).find("input[name='isSaveSeller']").is(':checked')?false:true;

		sellerBar = e;
		var row_valid=true;
		var intrex = /^[0-9]+$/;
		var contactId =null;//联系人ID 
		if(append!=null && append!=undefined && append.length>0){//补充匹配的保存
			 contactId = $(e).find(".compDivhead").find("select[name='contact']").children('option:selected').attr("val");//联系人ID 
			$(e).find(".subitemShow").find(".sunItems").each(function(index,element){
				$(element).find(".table").find("tbody").children("tr").each(function(index,element){
					var row_quantity = $(element).find("input[name='res_count']")
					if (row_quantity.length > 0 && row_quantity.val() != '' && !intrex.test(row_quantity.val())) {
						row_valid = false;
						row_quantity.focus();
						return false;
					}
					if($(element).find("input[name='res_price']").val()){//有价格时才保存
						var item = {categoryUuid:$(element).find("input[name='res_categoryUuid']").val(),
							categoryName:$(element).find("input[name='res_categoryName']").val(),
							materialUuid:$(element).find("input[name='res_materialUuid']").val(),
							materialName:$(element).find("input[name='res_materialName']").val(),
							factoryId:$(element).find("input[name='res_factoryId']").val(),
							factoryName:$(element).find("input[name='res_factoryName']").val(),
							warehouseId:$(element).find("input[name='res_warehouseId']").val(),
							warehouseName:$(element).find("input[name='res_warehouseName']").val(),
							weightConcept:$(element).find("select[name='weightConcept']").val(),
							resultWeight:$(element).find("input[name='res_weight']").val(),
							resultQuantity:$(element).find("input[name='res_count']").val(),
							price:$(element).find("input[name='res_price']").val(),
							purchaseOrderItemId:$(element).find("input[name='res_itemId']").val(),
							resourceId:$(element).find("input[name='res_resourceId']").val(),
							normsName:$(element).find("input[name='res_spec']").val() ,
							cityId:$(element).find("input[name='res_cityId']").val() ,
							cityName:$(element).find("input[name='res_cityName']").val() ,
							option:'save'
						};

						_inquiryItem.push(item);
					}

				});

			});

		}else{
			 contactId = $(e).find(".compDivhead").find("select[name='contact']").children('option:selected').attr("val");//联系人ID 
			$(e).find(".itemsShow").find(".sunItems").each(function(index,element){
				$(element).find(".table").find("tbody").children("tr").each(function(index,element){

					var row_quantity = $(element).find("input[name='res_count']")
					if (row_quantity.length > 0 && row_quantity.val() != '' && !intrex.test(row_quantity.val())) {
						row_valid = false;
						row_quantity.focus();
						return false;
					}
					if($(element).find("input[name='res_price']").val()){//有价格时才保存
						var item = {categoryUuid:$(element).find("input[name='res_categoryUuid']").val(),
							categoryName:$(element).find("input[name='res_categoryName']").val(),
							materialUuid:$(element).find("input[name='res_materialUuid']").val(),
							materialName:$(element).find("input[name='res_materialName']").val(),
							factoryId:$(element).find("input[name='res_factoryId']").val(),
							factoryName:$(element).find("input[name='res_factoryName']").val(),
							warehouseId:$(element).find("input[name='res_warehouseId']").val(),
							warehouseName:$(element).find("input[name='res_warehouseName']").val(),
							weightConcept:$(element).find("select[name='weightConcept']").val(),
							resultWeight:$(element).find("input[name='res_weight']").val(),
							resultQuantity:$(element).find("input[name='res_count']").val(),
							price:$(element).find("input[name='res_price']").val(),
							purchaseOrderItemId:$(element).find("input[name='res_itemId']").val(),
							resourceId:$(element).find("input[name='res_resourceId']").val(),
							normsName:$(element).find("input[name='res_spec']").val() ,
							cityId:$(element).find("input[name='res_cityId']").val() ,
							cityName:$(element).find("input[name='res_cityName']").val() ,
							option:'save'
						};

						_inquiryItem.push(item);
					}

				});

			});

		}



		if (!row_valid) {
			form_valid = false;
			return false;
		}
		var seller = $(e).find("input[name='res_sellerName']").val();
		var sellerId = $(e).find("input[name='res_sellerId']").val();
		
		inqueryOrderId = $(e).find("input[name='res_inqueryOrderId']").val();
		var seller={sellerName:seller,isSaveSeller:isSaveSeller,sellerId:sellerId,contactId:contactId ,itemList:_inquiryItem};
		if(_inquiryItem.length>0){
			_inquirySeller.push(seller);
		}



		if(!form_valid){
			return false;
		}

		var order={purchaseOrderId:$("#id").val(),sellerList:_inquirySeller,inquiryOrderId:inqueryOrderId,isSaveOrder:isSaveOrder};
		var itemCount=0;
		$(order.sellerList).each(function(i,e){
			itemCount+=e.itemList.length;
		});
		if(itemCount==0){
			cbms.alert("没资源需要保存");
			return false;
		}
		/**
		 * 保存询价资源
		 */
		saveInquiryOrder(JSON.stringify(order),sellerBar,_noExistWarehouseName,this);



	});



	/**
	 * 点击询价单删除按钮
	 */
	$(document).on("click","a[name='deleteInfoBtn']",function(){
		var tr = $(this).closest("tr");
		var purchaseOrderItemId = $(tr).find("input[name='purchaseOrderItemId']").val();
		var op = $(tr).find("td:last a").attr("title")=="删除"?"del":"edit";
		if(op=="del"){
			clearItemContent(tr);
			var sellerBar = $(this).closest(".seller-bar");
			checkContinueMatching(sellerBar,purchaseOrderItemId);
		}else{
			showItemContent(tr);
		}

	});

	/**
	 * 修改匹配结果重量
	 */
	$(document).on("change","input[name='resultWeight']",function(){
		var self = this;
		setTimeout(function () {//延迟执行，为了让保存询价单的事件先执行
			var quantity = $(self).closest("tr").find("input[name='resultQuantity']");
			if($(quantity).attr("singleweight")!="null"){
				$(quantity).val(Math.round($(quantity).val()));
				$(self).val(cbms.convertFloat($(quantity).attr("singleweight")*$(quantity).val(),3));
			}
			//如果到门口的是保存询价单的按钮就不要执行下去了
			if(purchaseOrder.clickedSaveInquiry){
				return false;
			}

			var tr = $(self).closest("tr");
			var purchaseOrderItemId = $(tr).find("input[name='purchaseOrderItemId']").val();

			var sellerBar = $(self).closest(".seller-bar");
			checkContinueMatching(sellerBar,purchaseOrderItemId);
		}, 300);
	});

	/**
	 * 填写报价单
	 */
	$(document).on("click","#quotation",function(){
		var blockInquiryOrderSellerIds=[];
		$("input[name='isSaveSeller']").each(function(){
			if($(this).is(':checked')){
				blockInquiryOrderSellerIds.push( $(this).val() );
			}
		})

		//根据已保存的卖家数判断是否可以填写报价单
		if($("#saveSellerNum").text()>0){
			blockInquiryOrderSellerIds = unique(blockInquiryOrderSellerIds);
			if(blockInquiryOrderSellerIds.length == 0){
				location.href=Context.PATH + "/smartmatch/inquiryorder/create.html?id=" + $("#id").val();
			}else{
				location.href=Context.PATH + "/smartmatch/inquiryorder/create.html?id=" + $("#id").val()+"&blockInquiryOrderSellerIds=" +blockInquiryOrderSellerIds.join(",");
			}
		}else{
			cbms.alert("暂无保存询价信息，请先保存询价！");
		}
	});

	/**
	 * 单件重量反算
	 */
	$(document).on("input","input[name='resultQuantity']",function(){
		if($(this).attr("singleweight")!="null"){
			$(this).closest("tr").find("td:eq(8) input[name='resultWeight']").val(cbms.convertFloat($(this).attr("singleweight")*$(this).val(),3));

		}
	});

	/**
	 * 修改匹配结果件数
	 */
	$(document).on("change","input[name='resultQuantity']",function(){
		var self = this;
		setTimeout(function () {//延迟执行，为了让保存询价单的事件先执行
			//如果到门口的是保存询价单的按钮就不要执行下去了
			if(purchaseOrder.clickedSaveInquiry){
				return false;
			}

			var tr = $(self).closest("tr");
			var purchaseOrderItemId = $(tr).find("input[name='purchaseOrderItemId']").val();

			var sellerBar = $(self).closest(".seller-bar");
			checkContinueMatching(sellerBar,purchaseOrderItemId);
		}, 300);
	});

	$(document).on("input","input[name='resultWeight']",function(){
		var quantity = $(this).closest("tr").find("input[name='resultQuantity']");
		if($(quantity).attr("singleweight")!="null"){
			$(quantity).val(cbms.convertFloat($(this).val() / $(quantity).attr("singleweight"), 2));
		}

	});



	$(document).on("click",".fa-qq",function(){
		var qq =$(this).attr("val");
		if(!qq){
			return false;
		}
		window.open("http://wpa.qq.com/msgrd?v=3&uin="+qq+"&site=qq&menu=yes");
	})

	/**
	 * 卖家名称得到焦点时显示一个月内历史成交记录
	 * -- 公司名鼠标移动显示历史成交记录
	 *
	 */
	$(document).on("mouseenter",'.seller-name',function () {
		getBillHistory($(this));
	});


	$(document).on("mouseleave",'span.seller-name , .historyShow',function () {
		$(".historyShow").html('');
	});
	/**
	 * 根据仓库Id获取仓库所在的城市
	 */
	$(document).on("change",'input[name="warehouseName"]',function(){
		var self = this;
		setTimeout(function () {
			getWarehouseCity($(self));
		}, 300);
	});

	//旧代码，影响JS事件注册，删除

	//填写询价单联系人change事件
	$(document).on("change", "select[name='contact']", function(e){
		cbms.stopF(e);
		var qq = $(this).children('option:selected').attr("qq");
		var html = (qq != "" ? "<a class='fa fa-qq fa-2x' href='javascript:;' title='QQ' val='"+qq+"'>&nbsp;</a>" : "");
		$(this).closest(".toggle-info").find("span.qq").html(html);
	})

	$(document).on("click", "select[name='contact']", function(e){
		cbms.stopF(e);

	})

	initFactoryEvent();
});//初始化方法结束

function checkContinueMatching(sellerBar,purchaseOrderItemId){
	var returnObj = getExceptSellerIds(sellerBar,purchaseOrderItemId);
	if(returnObj.itemIds!=""){
		var params ={
			purchaseOrderId:$("#id").val(),
			purchaseOrderItemIds:purchaseOrderItemId?purchaseOrderItemId:returnObj.itemIds,
			notSpecificSellerIds:returnObj.sellers,
			specificSellerId:null,
			isAppend:1
		};
		match(params,renderSearchResult,sellerBar);
	}
}

function clearItemContent(tr){
	$(tr).find("input[name='normsName'],input[name='factoryName'],input[name='warehouseName']," +
	"input[name='resultWeight'],input[name='resultQuantity'],input[name='price']").hide();
	$(tr).find("input[name='resultWeight'],input[name='resultQuantity']").val(0);
	$(tr).find("select[name='weightConcept']").hide();
	$(tr).find("td:eq(2) span").css("display", "inline-block");
	$(tr).find("td:eq(4) span").hide().removeClass("inline");
	$(tr).find("td:last a").attr("title","编辑");
	//$(tr).find("td:eq(6)").css("text-align","center");
	$(tr).find("td:last a").removeClass();
	$(tr).find("td:last a").addClass("fa fa-edit fa-lg");
	$(tr).find("#factoryId").attr("must","");
	$(tr).find("select[name='materialName']").attr("must","");
}

function showItemContent(tr){
	$(tr).find("input[name='normsName'],input[name='factoryName'],input[name='warehouseName']," +
	"input[name='resultWeight'],input[name='resultQuantity'],input[name='price']").show();
	$(tr).find("select[name='weightConcept']").show();
	$(tr).find("td:eq(2) span").css("display", "none");
	$(tr).find("td:eq(4) span").show().addClass("inline");
	$(tr).find("td:last a").attr("title","删除");
	//$(tr).find("td:eq(6)").css("text-align","right");
	$(tr).find("td:last a").removeClass();
	$(tr).find("td:last a").addClass("fa fa-trash-o fa-lg");
	$(tr).find("#factoryId").attr("must","1");
	$(tr).find("select[name='materialName']").attr("must","1");
}


/**
 * 城市点击后，公司切换
 * @param o
 * @param id
 */
function compTabShow(o,id){

	$("#resultCitiesTab").find("li").removeClass("active");
	$(o).closest("li").addClass("active");

	$(o).closest(".tabbar_match").find(".compDiv").hide();
	$("#company_"+id).show();

	$(".cp_msg_detail").addClass("none");
	$("#company_"+id).find(".cp_msg_detail").eq(0).removeClass("none");
	$(".itemsShow").hide();
	$("#company_"+id).find(".itemsShow").eq(0).show();
	//对切换过去的tab第一家公司进行补充匹配
	supplement($("#company_"+id).find(".cp_msg_detail").eq(0));
	//对切换过去的tab第一家公司加载联系人
	findCpmContacts( $("#company_"+id).find(".cp_msg_detail").eq(0));
}

/**
 * 渲染匹配结果
 */

function renderSearchResult(data,sellerBar,sellerId,active_cityId){

	loadFactoryByCategory();

//	var data = response.data;
	if(data==null){
		utils.showMsg("未匹配到任何结果", '', 'error', 1000);
		$(".seller-bar").each(function(i,e){
			var isInquiry =isInqueryBar(e);
			if(!isInquiry){
				$(e).remove();
			}
		});
		return false;
	}

	var isInquiry =isInqueryBar(sellerBar);
	var inquiryList = data != null && data.inquiryList !=null ? data.inquiryList : null;
	if(sellerBar=="specificAppend"){//指定卖家搜索
		if(inquiryList.length==0){
			utils.showMsg("未匹配到任何结果", '', 'error', 1000);
			return false;
		}
		_itemAttributes=[];//清空
		_itemCloset=[];//清空
		//全部展现
		$(".assign-bar, #purchaseFooter").show();
		if(inquiryList.length==0){
			utils.showMsg("未匹配到任何结果", '', 'error', 1000);
			$("#purchaseFooter").hide();
//			return false;
		}
		$(".inquiryOrders").empty();

		/**
		 * 询价资源生成页面
		 */
		initMatchedRes(inquiryList);
		//指定城市高亮
		if(typeof(active_cityId)!='undefined'){
			var tab_ele = $("#city_tab_"+active_cityId).removeClass("active");
			compTabShow(tab_ele,active_cityId);
		}
		$("#purchaseFooter").show();
	}else if(!sellerBar){

		_itemAttributes=[];//清空
		_itemCloset=[];//清空
		//全部展现
		$(".assign-bar, #purchaseFooter").show();
		if(inquiryList.length==0){
			utils.showMsg("未匹配到任何结果", '', 'error', 1000);
			$("#purchaseFooter").hide();
//			return false;
		}
		$(".inquiryOrders").empty();

		/**
		 * 询价资源生成页面
		 */
		initMatchedRes(inquiryList);

	}else{//展现到指定的询价单
		if(inquiryList.length>0){
			var inqeryOrder = inquiryList[0];
			if(inqeryOrder){
				var seller = inqeryOrder.sellerList[0];
				if(seller){
					if(isInquiry){//补充到已经保存的询价单
						if(sellerId){//补充到当前询价单
							var divSeller = $(sellerBar).find(".seller-name[value='"+sellerId+"']").parent().next();
							var savedItemList =[];
							$(divSeller).find("tbody tr input[name='purchaseOrderItemId']").each(function(){
								savedItemList.push($(this).val());
							});
							var items = seller.itemList;
							for(var k in items){
								if($.inArray(""+items[k].purchaseOrderItemId,savedItemList)>-1){
									continue;
								}
								var item = $(createInquiryItem(items[k],isInquiry));
								$(divSeller).find("tbody").append(item);
							}
							//$(sellerBar).find(".sellerDiv").append(divSeller);
						}
					}else{
						var divSeller = $(createInquirySeller(seller));
						var items = seller.itemList;
						for(var k in items){
							var item = $(createInquiryItem(items[k]));
							$(divSeller).find("tbody").append(item);
						}
						$(sellerBar).find(".sellerDiv").append(divSeller);
//						$(sellerBar).find(".seller-footer").before(divSeller);
					}

				}
			}
		}else{
			utils.showMsg("未匹配到任何候补资源", '', 'error', 2000);
			return false;
		}

	}


	//处理第一条
	defaultProcessHead();

	renderWeightConceptList();
	$(sellerBar).find(".toggle-bar, .toggle-info, .seller-footer").show();
	var isInquiry =isInqueryBar(sellerBar);
	if(sellerBar!="specificAppend"&&!isInquiry){
		checkContinueMatching(sellerBar);
	}
	cbms.closeLoading();
	return false;
}



/**
 * 根据查询结果生成城市/公司对应的html代码
 * @param inquiryList
 */
function initMatchedRes(inquiryList){
	if(inquiryList && inquiryList.length >0){

		var html = '<div class="tabbar_match">'
		html+='<ul id="resultCitiesTab">',
			zdmj = '<li class="assign-bar text-left">'+
			'<label class="pos-rel">指定卖家:</label>'+
			'<input type="text" id="specificSeller" />'+
			'<button class="btn btn-xs btn-light" id="specificSearch">确定</button>'+
			'</li>';
		//标记循环展示的城市是否是首位
		var isFirstFlag = -1
		for (var i in inquiryList){
			//数量大于0 显示
			if(inquiryList[i].cpCount > 0){
				isFirstFlag++;
				if(isFirstFlag==0){
					if(inquiryList[i].city){
						html+='<li id="cityLi_' + inquiryList[i].city.id +  '"class="active">'
					}else{
						html+='<li id="cityLi_0"class="active">'
					}

				}else{
					if(inquiryList[i].city){
						html+='<li id="cityLi_' + inquiryList[i].city.id +  '">'
					}else{
						html+='<li id="cityLi_0">'
					}
				}
				if(inquiryList[i].city){
					html+='<a href="JavaScript:;" id="city_tab_'+inquiryList[i].city.id+'" onclick="compTabShow(this,'+inquiryList[i].city.id+')">'+inquiryList[i].city.name+ "(" + inquiryList[i].cpCount+' )</a> </li>';
				}else{
					html+='<a href="JavaScript:;" id="city_tab_0" onclick="compTabShow(this,0)">指定卖家</a> </li>';
				}
			}
		}
		html+=zdmj+'</ul><p class=\"clearfix\"></p></div>';
		//生成城市tab 切换，然后设置到inquiryOrders这个div中
		$(".inquiryOrders").append(html);
		//
		var setFirst =false;
		for (var i in inquiryList){

			if(!setFirst){
				if( inquiryList[i].city && inquiryList[i].cpCount > 0){
					setFirst = true;
					$(".tabbar_match").append('<div class="compDiv" id="company_' + inquiryList[i].city.id +  '"> </div>');
				}
			}else{

				if( inquiryList[i].city && inquiryList[i].cpCount > 0){
					$(".tabbar_match").append('<div class="compDiv   none" id="company_' + inquiryList[i].city.id +  '" ></div>');;
				}
			}
		}
	}

	initCompRes(inquiryList);
}





/**
 * 设置公司层资源,第一层company，第二层资源对应的询价详情item， 第三层匹配到的资源matchedres
 *   -company
 *   --item
 *   ---matchedres
 * @param inquiryList
 */
function initCompRes( inquiryList,sellerBar,isSupplement){
	for (var i in inquiryList){

		var innerHtml = "";
		//公司列表
		var compamies = inquiryList[i].compamies;
		for( var j in compamies){

			innerHtml+=initCompany(compamies[j],j,isSupplement);//设置公司

			if(sellerBar){//补充匹配的 div  subitemShow ，   第一次点击匹配后生成的DIV为itemsShow
				innerHtml+="<div class='subitemShow' >  ";
			}else{
				if(j==0){
					innerHtml+="<div   class=\"itemsShow\" >  ";
				}else{
					innerHtml+="<div   class=\"itemsShow   none  \" > ";
				}
			}//if ｅｎｄ

			for(var h in compamies[j].itermResourceList){
				innerHtml+="<div   class=\"sunItems\" > ";
				var itemList = compamies[j].itermResourceList[h];
				/**
				 * 生成询价详情item的table显示
				 */
				innerHtml+=initItems(itemList);
				/**
				 * 生成当前公司询价详情item对应的资源table table头部
				 */
				if(itemList.resourceList !=null && itemList.resourceList.length > 0 ){
					innerHtml+="<table class=\"table\">";
				}else{
					innerHtml+="<table class=\"table none \">";
				}

				innerHtml+="<tr class=\"tabhead\"><td class=\"text-center\"> 基础要素</td><td>计重方式</td><td>数量（件）</td><td>重量（吨）</td><td>单价（元/吨）</td><td>更新价格</td>"
				+"<td>询价价格</td><td  class=\"text-center\" width=\"90px\">成交价格</td><td class=\"text-center\" width=\"80px\">操作"

				+"<input type='hidden' name='res_itemId' value='"+(itemList.item.id)+"'/></td></tr>";
				/**
				 * 列出所有的当前item对应的资源tr
				 */
				if(itemList.resourceList.length>0){
					//生成匹配到的资源表格显示
					for(var k in   itemList.resourceList){
						var res = itemList.resourceList[k]
						/**
						 * 生成一条资源的tr
						 */
						innerHtml+=initRes(res,itemList.item);
					}
				}
				innerHtml+="</table>";
				innerHtml+="</div> ";
			}//生成数据table结束　　 end of  for( var h in compamies[j].itermResourceList )

			//生成询价保存按钮
			innerHtml+="<div class=\"text-center seller-barl-btn\"> <button type='button' class='btn btn-info btn-sm' name='saveInquiryOrder'>保存</button></div></div> <div class='appendResDiv'></div></form> </div>  ";
		}//end of  for( var j in compamies)

		//将组装完成的公司找货详情设置到当前公司对应的div中
		var divId   = null;

		//数量大于零时
		if(inquiryList[i].cpCount > 0){
			if( inquiryList[i].city){
				divId = "company_"+ inquiryList[i].city.id;
			}
			if(sellerBar){
				$(sellerBar).find(".appendResDiv").eq(0).append(innerHtml);
			}else{
				$("#"+divId).append(innerHtml);
			}
		}


	}// end of  for (var i in inquiryList)
}



/**
 * 设置公司DIV 的内容以及公司信息
 * index，当前公司列表的INDEX值
 * isSupplement 是否补充匹配
 */
function initCompany(comp,index,isSupplement){
	if(comp){
		var htmlStr ="<div class=\"seller-barl  menuContent mg-t-15\"><form> <div class='pos-rel historyShow '> </div>" ;

		if(isSupplement){
			//补充匹配，所有的都不允许点击
			htmlStr+="<table class=\" compDivhead\" width=100%>  " ;
		}else{
			//匹配卖家，第一条默认不允许点击
			if(index==0){
				htmlStr+="<table class=\" compDivhead\" width=100%>  " ;
			}else{
				htmlStr+="<table class=\"cpshow compDivhead\" width=100%>  " ;
			}
		}


		htmlStr+="<tr><th colspan=\"6\">  <div class=\"seller-head clearfix\"> " +
		"<div class=\"f-fl\">" ;
		if('白名单'==comp.supplierLabel){
			htmlStr+=  "<span class=\"font-bai\">白</span>";
		}else{
			htmlStr+=  '<span class="font-kong">&nbsp;</span>';
		}
		if(1==comp.payMentLaybel){
			htmlStr+=  "<span class=\"font-yu\">预</span>";
		}else{
			htmlStr+=  '<span class="font-kong">&nbsp;</span>';
		}
		/*	历史成交
		 if(comp.lastTradeTime!=null ){
		 var now = new Date();
		 var aa = new Date(comp.lastTradeTime);
		 now.setMonth(now.getMonth-1);
		 if(aa<now){
		 htmlStr+=  "<span >最近一个月无成交</span>";
		 }else{
		 htmlStr+=  "<span> 最近一次成交时间："+  (new Date(comp.lastTradeTime)).Format("yyyy-M-d hh:mm:ss")  +"</span>";
		 }
		 }else{
		 htmlStr+=  "<span >无成交记录</span>";
		 }	*/

		htmlStr+=" <span class='seller-name' style='cursor: pointer'  value=\"公司名称\">"
		+comp.sellerName+"</span>"
		+"</div>";
		//联系人后加载，在点开公司后加载公司的联系人
		if(comp.contactList){
			htmlStr+="<div class=\"f-fl pd-l-25  cp_msg_detail \"> 联系人 :<div  class=\"inline cp_msg_detail_contact\">";
			htmlStr +="<select class='contact'  name='contact'>"
			for(var  i in comp.contactList ){
				var ct =  comp.contactList[i];
				htmlStr+=" <option value='"+ct.qq+"' val='"+ct.id+"'> " + ct.name +"  "+ ct.tel +"  交易员名称： " + ct.traderName+"</option>";
			}
			htmlStr +="</select>"
		}else{
			htmlStr+="<div class=\"f-fl pd-l-25  cp_msg_detail none\"> 联系人 :<div  class=\"inline cp_msg_detail_contact\">";
		}

		//	htmlStr+="</div><a class=\"fa fa-phone-square fa-2x\" href=\"javascript:;\" title=\"电话\">&nbsp;</a>"
		htmlStr+= "</div><a class=\"fa fa-qq fa-2x\" href=\"javascript:;\" name='qq_attr' title=\"QQ\">&nbsp;</a> <div class=\"lastInquiredTime   inline\"></div>  </div> "
		+"<div class=\"f-fr\">  " ;
		//div    lastInquiredTime 用于展示最新一次成交时间距离现在多久，点击公司tab后，会去获取该值，然后填充
		if(comp.isInquired==1){
			if(index==0){
				htmlStr+=  " <div class=\"isinquiredshow none\" > 已询价</div>"+
				"<div class=\"isSaveSellerdiv \">屏蔽报价  <input name=\"isSaveSeller\" type=\"checkbox\" value=\""+comp.sellerId+"\" />  </div>";
			}else{
				htmlStr+=  " <div class=\"isinquiredshow\" > 已询价</div>" +
				"<div class=\"isSaveSellerdiv none\">屏蔽报价  <input name=\"isSaveSeller\" type=\"checkbox\" value=\""+comp.sellerId+"\" />  </div>";
			}

			// +"<div > 最新询价时间："+ (new Date(comp.lastInquiryedTime)).Format("yyyy-M-d hh:mm:ss") +"</div>";
		}else{
			if(index==0){
				htmlStr+=  " <div class=\"isinquiredshow none\"> 未询价</div> " +
				"<div class=\"isSaveSellerdiv \">屏蔽报价  <input name=\"isSaveSeller\" type=\"checkbox\" value=\""+comp.sellerId+"\" />  </div>";
			}else{
				htmlStr+=  " <div class=\"isinquiredshow\"> 未询价</div> " +
				"<div class=\"isSaveSellerdiv none\">屏蔽报价  <input name=\"isSaveSeller\" type=\"checkbox\" value=\""+comp.sellerId+"\" />  </div>";
			}
		}
		htmlStr+="  </div>	"
		+"</div> </th></tr> ";



		//公司属性值，用于页面操作时获取公司信息。	
		htmlStr+="<input type = \"hidden\"  name=\"res_sellerName\" value=" +checkNull( comp.sellerName ) +">"
		+"<input type = \"hidden\"  name=\"res_sellerId\" value="+checkNull( comp.sellerId ) +">"
		+"<input type = \"hidden\"  name=\"res_isInquired\" value="+checkNull( comp.isInquired ) +">"
		+"<input type = \"hidden\"  name=\"res_inqueryOrderId\" value=" +checkNull( comp.inqueryOrderId ) +">"
		+"<input type = \"hidden\"  name=\"res_city\" value=" +checkNull( comp.cityId ) +">"
		+"<input type='hidden' name ='is_item_hide' value ='1'/>"
		+"</table>";
		return htmlStr;
	}else{
		return "";
	}

}

//获取QQ号,弹出qq对话框
$(document).on("click", ".seller-barl a[name='qq_attr']", function () {
	var qq = $(this).closest("tr").find(".contact").val();
	if (qq == "") {
		cbms.alert("该联系人没有QQ号！");
		return;
	}
	window.location.href="tencent://message/?uin="+qq+"&Site=admin5.com&menu=yes";
});

/**
 * 设置 item的div
 */
function initItems(itemList){
	var item = itemList.item;
	var length = 0;
	if( itemList.resourceList){
		length =itemList.resourceList.length;
	}

	if(item){
		var htmlStr ="<table class=\"table\">    ";
		if(length==0){
			htmlStr	+="<tr class=\"items-info bg-gray\">" ;
		}else{
			htmlStr	+="<tr class=\"items-info \">" ;
		}
			
	htmlStr	+="<input type = 'hidden' name = 'item_res_count'  value = '"+length+"'> </input>"//当前资源的条数，判断点击后是否需要补充匹配
			+"<input type = 'hidden' name = 'item_res_quantity'  value = '"+item.quantity+"'> </input>"//询价详情的数量
			+"<input type = 'hidden' name = 'item_res_weight'  value = '"+item.weight+"'> </input>"//询价详情的重量
			+"<input type = 'hidden' name = 'item_res_id'  value = '"+item.id+"'> </input>"//询价详情ID
			+"<input type = 'hidden' name = 'item_res_categoryId'  value = '"+item.categoryUuid+"'> </input>"//
			+"<input type = 'hidden' name = 'item_res_categoryName'  value = '"+item.categoryName+"'> </input>"//
			+"<input type = 'hidden' name = 'item_res_materialName'  value = '"+item.materialName+"'> </input>"//
			+"<input type = 'hidden' name = 'item_res_materialUuid'  value = '"+item.materialUuid+"'> </input>"//
			+"<input type = 'hidden' name = 'item_res_factoryNames'  value = '"+item.factoryNames+"'> </input>"//
			+"<input type = 'hidden' name = 'item_res_spec'  value = '"+item.spec+"'> </input>"//
			+" <td class=\"pd-l-25\">品名：<span class=\"pd-r-25\">"
			+checkNull( item.categoryName )+"</span> 材质：<span class=\"pd-r-25\">"
			+checkNull( item.materialName )+"</span>规格：<span class=\"pd-r-25\">"
			+checkNull( item.spec )+"</span>厂家： <span class=\"pd-r-25\">"
			+checkNull(item.factoryNames)+" </span>求购重量：<span class=\"pd-r-25\">"
			+checkNull(item.weight)+" </span>求购数量：<span class=\"pd-r-25\">"
			+checkNull(item.quantity)
			+"</span>";
		if (item.attributeList != null && item.attributeList.length > 0) {
			htmlStr+=renderResourceExtentionAttributes(item.attributeList);

		}
		if(itemList.resourceList ==null||itemList.resourceList.length==0){
			htmlStr+="<a class=\"fa fa-plus \" href=\"JavaScript:;\" onclick=\"itemShowResDetail(this,'add',-1)\"  title=\"新增\" ></a>" ;
		}
		htmlStr +=" </td></tr></table>"
		return htmlStr;
	}else{
		return "";
	}

}
/**
 * 拼接item的 扩展属性
 * @param attributesList
 * @returns {String}
 */
function renderResourceExtentionAttributes(attributesList) {
	if (attributesList != null && attributesList.length > 0) {
		var str = '';
		for (var i = 0; i < attributesList.length; i++) {
			if (i != 0) {
				str += ' ';
			}
			str += attributesList[i].name+":";
			if (attributesList[i].type == "input") {
				str +="<span class=\"pd-r-25\"><input style=\"width:65px;height:29px;font-size:inherit\"  type='text' value='"+ checkNull(attributesList[i].value)+"' id='attr"+i+"_type'></span>"
			} else if (attributesList[i].type == "select") {
				str +="<span class=\"pd-r-25\"> <select    >";
				var options = attributesList[i].options.split(",");
				for (var j = 0; j < options.length; j++) {
					str += "<option";
					if (options[j] == attributesList[i].value) {
						str += " selected = selected";
					}
					str += " value ="+options[j]+">"+options[j]+"</option>";
				}
				str +="</select></span>";
			} else if (attributesList[i].type == "radio") {
				var options = attributesList[i].options.split(",");
				str+="<span class=\"pd-r-25\">";
				for (var j = 0; j < options.length; j++) {

					if(attributesList[i].value!=undefined && attributesList[i].value!=null && attributesList[i].value==options[j]){
						str +=" <input type='radio'  checked='checked' id='attributeId_"+attributesList[i].attributeId+"' name='attr_radio_"+attributesList[i].name+"' value='"+options[j]+"'>"+options[j];
					}else{
						str +=" <input type='radio'   id='attributeId_"+attributesList[i].attributeId+"' name='attr_radio_"+attributesList[i].name+"' value='"+options[j]+"'>"+options[j];
					}
					
				}
				str+="</span>"

			} else if (attributesList[i].type == "checkbox") {
				var options = attributesList[i].options.split(",");
				for (var j = 0; j < options.length; j++) {
					
					if(attributesList[i].value!=undefined && attributesList[i].value!=null && attributesList[i].value==options[j]){
						str +="<span class=\"pd-r-25\"> <label><input checked='checked'  type='checkbox' name='attr_checkbox_"+[j]+"' value='"+options[j]+"'>"+options[j]+"</label> </span>"
					}else{
						str +="<span class=\"pd-r-25\"> <label><input  type='checkbox' name='attr_checkbox_"+[j]+"' value='"+options[j]+"'>"+options[j]+"</label> </span>"
					}
					
				}
			}
		}
		return str;
	}
	else {
		return '';
	}
}
/**
 * 设置资源div
 * @param res
 */
function initRes(res,item){
	_itemAttributes.push(item);
	if(res){
		var htmlStr ="<tr >"
				//基础数据  保存询价时或者编辑保存时候使用
			+'<input type = "hidden"  name="res_categoryUuid" value='+checkNull( res.categoryUuid ) +'>'
			+'<input type = "hidden"  name="res_categoryName" value='+checkNull( res.categoryName ) +'>'
			+'<input type = "hidden"  name="res_factoryId" value='+checkNull( res.factoryId ) +'>'
			+'<input type = "hidden"  name="res_factoryName" value='+checkNull( res.factoryName ) +'>'
			+'<input type = "hidden"  name="res_materialUuid" value='+checkNull( res.materialUuid ) +'>'
			+'<input type = "hidden"  name="res_materialName" value='+checkNull( res.materialName ) +'>'
			+'<input type = "hidden"  name="res_spec" value='+checkNull( res.spec ) +'>'
			+'<input type = "hidden"  name="res_warehouseId" value='+checkNull( res.warehouseId ) +'>'
			+'<input type = "hidden"  name="res_warehouseName" value='+checkNull( res.warehouseName ) +'>'
			+'<input type = "hidden"  name="res_cityName" value='+checkNull( res.cityName ) +'>'
			+'<input type = "hidden"  name="res_cityId" value='+checkNull( res.cityId ) +'>'
			+'<input type = "hidden"  name="res_resourceId" value='+checkNull( res.resourceId ) +'>'
			+'<input type = "hidden"  name="res_itemId" value='+ res.itemId+'>'
			+"<td class=\"text-center\">  <span class=\"resBaseInfo\">"+res.factoryName+ " "+res.categoryName+" "+res.materialName+ " " +checkNull(res.spec)+  " " +checkNull(res.warehouseName) +" " +checkNull(res.cityName )+ "</span></td>";

		//设置计重方式
		htmlStr	+="<td>  <select name='weightConcept'>" ;
		if(res.weightConcept=='磅计'){
			htmlStr	+=      "<option value=\"磅计\"  selected = \"selected\" >磅计</option>";
		}else{
			htmlStr	+=  "<option value=\"磅计\">磅计</option>" ;
		}
		if(res.weightConcept=='理计'){
			htmlStr	+=      "<option value=\"理计\"  selected = \"selected\" >理计</option>";
		}else{
			htmlStr	+=  "<option value=\"理计\">理计</option>" ;
		}
		if(res.weightConcept=='抄码'){
			htmlStr	+=      "<option value=\"抄码\"  selected = \"selected\" >抄码</option>";
		}else{
			htmlStr	+=  "<option value=\"抄码\">抄码</option>"  ;
		}
		htmlStr	+	"</select> </td>" ;
		//设置计重方式


		htmlStr	+=	"<td>  <input style=\"width:65px\" verify=\"number\"  name='res_count' value = '"+checkNull(item.quantity )+"'></input> </td>" //数量（件）
		+"<td><input style=\"width:65px\"  verify=\"numeric\" must='1'  name = 'res_weight' value = '"+checkNull(item.weight)+"'></input>  </td>" //重量
		+"<td><input style=\"width:65px\" verify=\"numeric\"  name='res_price' ></input>  </td>" //单价
		+"<td><span>"+checkPriceNull(res.updatePrice)+"</span>  </td>" //更新价格
		+"<td><span>"+checkPriceNull(res.price)+"</span> </td>" //询价价格
		+"<td ><span>"+(res.historyPrice?(res.historyPrice +"</span><br /><small>"+dateFormat(new Date(res.lastUpdateTime),"MM-dd")):"--")+"</small></td>" //成交价格

		+"<td><span ><a class=\"fa fa-plus\" href=\"JavaScript:;\" onclick=\"showResDetail(this,'add',"+(_itemAttributes.length-1)+")\"   title=\"新增\"></a> &nbsp; <a  class=\"fa fa-pencil-square-o\"  href=\"JavaScript:;\" onclick=\"showResDetail(this,'update',"+(_itemAttributes.length-1)+")\"  title=\"修改\"></a>"
		+" &nbsp; <a  class=\"fa fa-minus\" title=\"删除\" onclick=\"deleteTrResource(this)\" ></a></span>  </td>"
		+"</tr>";
		return htmlStr;
	}else{
		return "";
	}
}

/**
 * 设置价格，当价格为空时 需要显示‘--’，当价格》90000时，是议价资源，价格不做参考，所以价格也设置成'--'
 */
function checkPriceNull(a){
	if(a){
		if(a>90000){
			return "--";
		}
		else{
			return a;
		}

	}else{
		return "--";
	}
}

function checkNull(a){
	if(a){
		return a;
	}else{
		return "";
	}
}
/**
 * 删除, 公司资源tab中， 删除按钮调用该方法，调用后，将点击删除按钮的那一行从当前table中移除
 */
function deleteTrResource(o){
	if(o){
		$(o).closest("tr").remove();
	}
}

/**
 * item对应的头部
 */
function itemShowResDetail(o,oper,itemIndex){

	showResDetail(o,oper,itemIndex);
}
/**
 * 新增、修改。公司资源tab中，编辑资源时使用
 *
 * oper是操作类型 ：add  update ；在新增或者修改的编辑时，做区分使用
 * _itemAttributes 是暂存在全局变量中，用于保存当前资源对应的item， 编辑时通过itemIndex获取当前资源对应的item内容。
 */
function showResDetail(o,oper,itemIndex){
	
	/**
	 * itemIndex ==-1时，
	 * 点击添加以后显示资源table
	 */
	if(itemIndex==-1){
		$(o).closest(".sunItems").find(".table").eq(1).find(".tabhead").closest(".table").removeClass("none");
	}
	
	var title = "";
	var  baseInfo =null;
	var message  = "<form class='form-horizontal' id='layerSearchId' role='form'><div class='addOrUpdateResClass ' id='m-search' style='width:560px;margin-top:25px;'>" ;

	var item = _itemAttributes[itemIndex];
	var itemId= $(o).closest(".sunItems").find("input[name='item_res_id']").val();
	/**
	 * 获取该条详情的item重量与数量
	 */
	var quantity =  $(o).closest(".sunItems").find("input[name='item_res_quantity']").val();
	var	weight= $(o).closest(".sunItems").find("input[name='item_res_weight']").val()  ;
	var company_factory=$(o).closest(".sunItems").find("input[name='item_res_factoryNames']").val();
	//获取规格,判断规格是否可以编辑
	var categoryUUid =   $(o).closest(".sunItems").find("input[name='item_res_categoryId']").val();
	var spec = $(o).closest(".sunItems").find("input[name='item_res_spec']").val();
	var spec_readonly = specReadonlyFlag(spec,categoryUUid);
	if('add'==oper){
		//生成新增时的html页面
		title =  '新增';
		var categoryName = null,categoryId =null, materialName = null;
		var displaySpec = spec_readonly =='readonly' ? spec :"";
		if(-1==itemIndex){
			itemId= $(o).closest("tr").find("input[name='item_res_id']").val();
			 quantity= $(o).closest("tr").find("input[name='item_res_quantity']").val();
			 weight= $(o).closest("tr").find("input[name='item_res_weight']").val();					
			 categoryName= $(o).closest("tr").find("input[name='item_res_categoryName']").val();
			  categoryId =$(o).closest("tr").find("input[name='item_res_categoryId']").val();
			  materialName =$(o).closest("tr").find("input[name='item_res_materialName']").val();
			  materialUuid =$(o).closest("tr").find("input[name='item_res_materialUuid']").val();
			  factoryName =$(o).closest("tr").find("input[name='item_res_factoryNames']").val();
			  var item = {
				id:itemId,  
				quantity :quantity,
				weight:weight,
				materialName:materialName,
				materialUuid:materialUuid,
				categoryName:categoryName,
				categoryUuid:categoryId,
				factoryNames:factoryName
			  }
			  _itemAttributes.push(item);
			  o=	$(o).closest(".sunItems").find(".table").eq(1).find(".tabhead");
			  
		}else{
			  baseInfo = $(o).closest("tr").find(".resBaseInfo").text().split(' ');
			  categoryName= baseInfo[1];
			  categoryId = $(o).closest("tr").find("input[name='res_categoryUuid']").val();
			  materialName = baseInfo[2];
		}
		message+=" <div class='form-group'><label class='col-sm-2 control-label no-padding-right' for='txtNewPwd1'>品名</label>"
		+ "<div class='col-sm-10'><input type='text' id='addorupdate-category' value='"+categoryName+"'val='"+categoryId+"' readonly='readonly' class='c-text' />"
		+ "</div></div><div class='space-4'></div>"

		+" <div class='form-group'><input type='hidden' id='addorupdate-itemId' value='"+itemId +"'> <label class='col-sm-2 control-label no-padding-right' for='txtOldPwd'>厂家</label>"
		+ "<div class='col-sm-10'><input type='text' id='addorupdate-factoryName' value='' class='c-text' readonly limitfactory='" + company_factory + "' />"
		+ "</div></div><div class='space-4'></div>"

		+ "<div class='show-layer breadth-bar extent-bar single_factory_layer' id='single_factory'><div class='textures-nav'>"
		+ "<ul class='list-unstyled list-inline factory-nav-link'></ul></div><div class='textures-con'></div>"
		+ "<div class='btn-bar f-clrfix'><div class='' id='single_factory_others'><span style='position: static'>其他：</span><input name='otherFactoryIds' style='position: relative; width: 150px'/>"
		+ "</div><button class='clear-btn'>清除</button><button class='confirm-btn'>确认</button></div></div>"
		
		+  "<div class='form-group '><label class='col-sm-2 control-label no-padding-right' for='txtNewPwd1'>材质</label>"
		+ "<div class='col-sm-10 material'>"
		+ generateMaterialHtml(item, materialName)
		+"<div class='show-layer textures-bar none' id='showLayer_material' style='width:453px'><div class='textures-t'><a class='layer-del'></a>"
		+"</div><div class='textures-con'  style='width:450px'><div class='textures-con-bar-list bder-b-dashed'></div></div></div>"
		+ "</div></div><div class='space-4'></div>"

		+  "<div class='form-group'><label class='col-sm-2 control-label no-padding-right' for='txtNewPwd1'>规格</label>"
		+ "<div class='col-sm-10 '><input type='text'  id='addorupdate-spec'  value='"+displaySpec+"' class='c-text' "+spec_readonly+"/>"
		+ "</div></div><div class='space-4'></div>"

		+  "<div class='form-group'><label class='col-sm-2 control-label no-padding-right' for='txtNewPwd1'>仓库</label>"
		+ "<div class='col-sm-10'><input type='text' id='addorupdate-warehouse'  value='' class='c-text' />"
		+ "</div></div><div class='space-4'></div>"

		+  "<div class='form-group'><label class='col-sm-2 control-label no-padding-right' for='txtNewPwd1'>城市</label>"
		+ "<div class='col-sm-10'><input type='text'  id='addorupdate-cityName'  value='' class='c-text' />"
		+ "</div></div><div class='space-4'></div>" ;


	}else{
		//生成修改时的html页面
		title =  '修改';
		baseInfo = $(o).closest("tr").find(".resBaseInfo").text().split(' ');
		var  factoryId = $(o).closest("tr").find("input[name='res_factoryId']").val();
		var  categoryId = $(o).closest("tr").find("input[name='res_categoryUuid']").val();
		var  materialId = $(o).closest("tr").find("input[name='res_materialUuid']").val();
		var  warehouseId = $(o).closest("tr").find("input[name='res_warehouseId']").val();
		var  cityId = $(o).closest("tr").find("input[name='res_cityId']").val();
		//修改时规格不完全一致,可以修改
		if(baseInfo[3] != spec){
			spec_readonly ="";
		}
		message += "<div class='form-group'><label class='col-sm-2 control-label no-padding-right' for='txtNewPwd1'>品名</label>"
		+ "<div class='col-sm-10'><input type='text'  id='addorupdate-category'   value='"+baseInfo[1]+"'val='"+categoryId+"' readonly='readonly' class='c-text' />"
		+ "</div></div><div class='space-4'></div>"
		
		+"<div class='form-group'> <input type='hidden' id='addorupdate-itemId' value='"+itemId +"'><label class='col-sm-2 control-label no-padding-right' for='txtOldPwd'>厂家</label>"
		+ "<div class='col-sm-10'> <input type='text' id='addorupdate-factoryName'  value='"+baseInfo[0]+"' val='"+factoryId+"'  class='c-text' readonly limitfactory='" + company_factory + "'  />"
		+ "</div></div><div class='space-4'></div>"

		+"<div class='show-layer breadth-bar extent-bar single_factory_layer' id='single_factory'><div class='textures-nav'>"
		+" <ul class='list-unstyled list-inline factory-nav-link'></ul></div><div class='textures-con'></div>"
		+"<div class='btn-bar f-clrfix'><div class='' id='single_factory_others'><span style='position: static'>其他：</span><input name='otherFactoryIds' style='position: relative; width: 150px'/>"
		+"</div><button class='clear-btn'>清除</button><button class='confirm-btn'>确认</button></div></div>"
		
		+  "<div class='form-group'><label class='col-sm-2 control-label no-padding-right' for='txtNewPwd1'>材质</label>"
		+ "<div class='col-sm-10 material'>"
		+ generateMaterialHtml(item,baseInfo[2])
		+"<div class='show-layer textures-bar none' id='showLayer_material' style='width:453px'><div class='textures-t'><a class='layer-del'></a>"
		+"</div><div class='textures-con'  style='width:450px'><div class='textures-con-bar-list bder-b-dashed'></div></div>"
		+"</div>"
		+ "</div></div><div class='space-4'></div>"

		+  "<div class='form-group'><label class='col-sm-2 control-label no-padding-right' for='txtNewPwd1'>规格</label>"
		+ "<div class='col-sm-10'><input type='text' id='addorupdate-spec'  value='"+baseInfo[3]+"'  class='c-text' "+spec_readonly+" /> <input type='hidden' id='addorupdate-spec-old'  value='"+baseInfo[3]+"'  class='c-text' />"
		+ "</div></div><div class='space-4'></div>"

		+  "<div class='form-group'><label class='col-sm-2 control-label no-padding-right' for='txtNewPwd1'>仓库</label>"
		+ "<div class='col-sm-10'><input type='text' id='addorupdate-warehouse'   value='"+baseInfo[4]+"' val='"+warehouseId+"' class='c-text' />"
		+ "</div></div><div class='space-4'></div>"

		+  "<div class='form-group'><label class='col-sm-2 control-label no-padding-right' for='txtNewPwd1'>城市</label>"
		+ "<div class='col-sm-10'><input type='text' id='addorupdate-cityName'  value='"+baseInfo[5]+"'  val='"+cityId+"' class='c-text' />"
		+ "</div></div><div class='space-4'></div>" ;
	}

	// var trs = "<tr><td>1111</td><td>222</td><tr>";
//  $(o).closest("tr").closest("table").append(trs);

	/**
	 * 缓存当前编辑的tr行
	 */
	_itemCloset.push($(o).closest("tr"));

	if(item){
		if(item.attributeList){
			for(var i in item.attributeList){
				message += 	geAttribute(item.attributeList[i]);
			}
		}
	}

	if('add'==oper){
	
		message +=	""
			+ "<div class='text-center form-group'><button class='btn btn-sm btn-primary' type='button' onclick=\"addResItem(this,'"+oper+"',"+quantity+","+weight+","+(_itemCloset.length-1) +","+ (_itemAttributes.length-1) +")\">保存</button>"
			+ "&nbsp;<button class='btn btn-sm btnClose' type='button' value=''>取消</button></div>"
			+ "</div></form>";
		
	}else{
		message +=	""
			+ "<div class='text-center form-group'><button class='btn btn-sm btn-primary' type='button' onclick=\"addResItem(this,'"+oper+"',"+quantity+","+weight+","+(_itemCloset.length-1)+")\">保存</button>"
			+ "&nbsp;<button class='btn btn-sm btnClose' type='button' value=''>取消</button></div>"
			+ "</div></form>";
	}





	cbms.getDialog(title, message);
}

/**
 * 材质修改Select控件Html
 * @param item
 * @param materialName
 * @returns {String}
 */
function generateMaterialHtml(item,materialName){
	var materialUuids = item.materialUuid.split(",");
	var materialNames = item.materialName.split(",");
	var content = '<select class="select-left" id="addorupdate-material">';
	for(var i=0;i<materialUuids.length;i++){
		if(materialNames[i] == materialName){
			content+='<option selected="true" value="'+materialUuids[i]+'">'+materialNames[i]+'</option>'
		}else{
			content+='<option value="'+materialUuids[i]+'">'+materialNames[i]+'</option>'
		}
	}
	content+="</select>"
	return content;
}

/**
 * 渲染属性控件,生成html代码
 * 当前匹配资源的item如果有扩展属性 ，那么把扩展属性也生成出来
 */
function geAttribute(attribute) {
	var str = '<div class="form-group">';
	if (!attribute) {
		return str+"</div>";
	}
	str +="<label class='col-sm-2 control-label no-padding-right' >" +attribute.name+"</label>";
	if (attribute.type == "input") {
		str +="<div class='col-sm-10'><input type='text' value='' id='attr"+i+"_type'></div>"
	} else if (attribute.type == "select") {
		str +="<div class='col-sm-10'><select>";
		var options = attribute.options.split(",");
		for (var j = 0; j < options.length; j++) {
			str += "<option";
			if (options[j] == attribute.value) {
				str += " selected = selected";
			}
			str += " value ="+options[j]+">"+options[j]+"</option>";
		}
		str +="</select></div>";
	} else if (attribute.type == "radio") {
		var options =attribute.options.split(",");
		for (var j = 0; j < options.length; j++) {
			str +="<div class='col-sm-10'><input type='radio' id='attributeId_"+attribute.attributeId+"' name='attr_radio_"+attribute.name+"' value='"+options[j]+"'>"+options[j]+"</div>;"
		}

	} else if (attribute.type == "checkbox") {
		var options = attribute.options.split(",");
		for (var j = 0; j < options.length; j++) {
			str +="<label><input type='checkbox' name='attr_checkbox_"+[j]+"' value='"+options[j]+"'>"+options[j]+"</label>"
		}
	}

	return str+"</div>";
}




/**
 *  编辑资源弹框后的保存方法。
 * @param o
 * @param operType 操作类型 add,update 区分是做新增还是做修改操作的
 * @param q  重量，w 质量
 * @param index  当前修改的tr对象的数组位置
 * @param index2  _itemAttributes的数组位置
 */
function  addResItem(o,operType,q,w,index,index2){
	if($('#addorupdate-factoryName').attr("val")==null ||''==$('#addorupdate-factoryName').attr("val")){
		  cbms.alert("请先选中工厂！");
		  return;
	}
	if($('#addorupdate-category').attr("val")==null ||''==$('#addorupdate-category').attr("val")){
		  cbms.alert("请先选中品名！");
		  return;
	}
//	if($('#addorupdate-material').attr("val")==null ||''==$('#addorupdate-material').attr("val")){
//		  cbms.alert("请先选中材质！");
//		  return;
//	}
	if($('#addorupdate-warehouse').attr("val")==null ||''==$('#addorupdate-warehouse').attr("val")){
		  cbms.alert("请先选中仓库！");
		  return;
	}
	if($('#addorupdate-cityName').attr("val")==null ||''==$('#addorupdate-cityName').attr("val")){
		  cbms.alert("请先选中城市！");
		  return;
	}
	//res 从当前弹窗获取值
	var res = {
		resourceId:'',
		factoryId:$('#addorupdate-factoryName').attr("val"),
		factoryName:$('#addorupdate-factoryName').val(),
		categoryUuid:$('#addorupdate-category').attr("val"),
		categoryName:$('#addorupdate-category').val(),
		materialUuid:$('#addorupdate-material').val(),
		materialName:$('#addorupdate-material').find("option:selected").text(),
		spec:$('#addorupdate-spec').val(),
		warehouseId:$('#addorupdate-warehouse').attr("val"),
		warehouseName:$('#addorupdate-warehouse').val(),
		cityId:$('#addorupdate-cityName').attr("val"),
		cityName:$('#addorupdate-cityName').val(),
		weightConcept:'磅计',
		itemId:$('#addorupdate-itemId').val(),
		price:''
	}
	var item = null;
	if(  index2!=undefined  && index2!=null ){
		item = _itemAttributes[index2];
	}else{
		item = {
				quantity:q,
				weight:w
		}
	}
	 
	//生成tr

	if('add'==operType){

		/**
		 * 生成一条资源的tr  与最初匹配后的生成tr一致，
		 * 生成res对象会自动将值填充到input中
		 */
		var trs = initRes(res,item);
		_itemCloset[index].closest("table").append(trs);
	}else{
		/**
		 * 修改资源，需要手动设置已经修改了的值
		 */
		_itemCloset[index].find(".resBaseInfo").empty();
		var  changedRes = res.factoryName +" "+ res.categoryName +" " +res.materialName +" "+ res.spec +" "+ res.warehouseName +" " +res.cityName ;
		_itemCloset[index].find(".resBaseInfo").html(changedRes);
		_itemCloset[index].find("input[name='res_categoryUuid']").val(res.categoryUuid);
		_itemCloset[index].find("input[name='res_categoryName']").val(res.categoryName);
		_itemCloset[index].find("input[name='res_factoryId']").val(res.factoryId);
		_itemCloset[index].find("input[name='res_factoryName']").val(res.factoryName);
		_itemCloset[index].find("input[name='res_materialUuid']").val(res.materialUuid);
		_itemCloset[index].find("input[name='res_materialName']").val(res.materialName);
		_itemCloset[index].find("input[name='res_spec']").val(res.spec);
		_itemCloset[index].find("input[name='res_warehouseId']").val(res.warehouseId);
		_itemCloset[index].find("input[name='res_warehouseName']").val(res.warehouseName);
		_itemCloset[index].find("input[name='res_cityId']").val(res.cityId);
		_itemCloset[index].find("input[name='res_cityName']").val(res.cityName);

	}

	//新增后关闭
	cbms.closeDialog();

}









//1.0老方法
function contains(arr, obj) {
	var i = arr.length;
	while (i--) {
		if (arr[i] === obj) {
			return true;
		}
	}
	return false;
}
//1.0老方法
function resetInquiryShowStatus(){
	$("input[name='isInquiry']").show();
	$("input[name='isInquiry']:eq(0)").hide();
	$(".seller-bar").find(".seller-head:gt(0)").hide();
	$(".seller-bar:eq(0)").find(".seller-head,.toggle-bar, .toggle-info, .seller-footer").show();
	$(".seller-bar").attr("isOpen","0");
	$(".seller-bar:eq(0)").attr("isOpen","1");
}

/**
 * 获取询价单ID
 * @param sellerBar
 * @returns {Boolean}
 */
function isInqueryBar(sellerBar){
	return $(sellerBar).find("input[name='inquiryOrderId']").val()!="0";
}
//1、0老代码
function getExceptSellerIds(sellerBar,purchaseItemId){
	//定义返回对象
	var returnObj = {sellers:"",itemIds:""};
	var arrItemId =[];
	var strSellers="";
	//这张询价单中有多少卖家组合，如果达到三家就不要再去匹配了
	var sellers = $(sellerBar).find(".seller-name");
	strSellers = $(sellers).map(function() {
		return $(this).attr("value");
	}).get().join(',');
	returnObj.sellers=strSellers;
	var totalSellers = sellers.length;
	$(purchaseOrder.purchaseOrderItemList).each(function(i,e){
		var totalWeight = 0;
		//汇总某个卖家采购品规的卖家供应量
		$(sellerBar).find(".table tbody tr").each(function(j,f){
			var purchaseOrderId = $(this).find("input[name='purchaseOrderItemId']").val();
			if(e.id == purchaseOrderId){
				var resultWeight = $(this).find("input[name='resultWeight']").val()*1.0;
				totalWeight+=resultWeight;
			}
		});

		//总共加起来还不能满足客户要求的
		if(totalWeight<e.weight*0.85 && totalSellers<3 ){
			if(!purchaseItemId||(purchaseItemId&&purchaseItemId==e.id)){
				arrItemId.push(e.id);
			}
		}
	});
	returnObj.itemIds = arrItemId.join(",");
	return returnObj;

}

/*
 * 获取所有的仓库数据
 */
function getWarehouseData(){

	if(_warehouseData.length==0){
		$.ajax({
			type : "POST",
			url : Context.PATH + "/smartmatch/warehouse/getAllWarehouse.html",
			dataType : "json",
			success : function(response) {
				if (response.success) {
					_warehouseData = response.data;
				} else {
					cbms.alert(response.data);
				}
			},
			error : function(xhr, textStatus, errorThrown) {}
		});
	}
}

/**
 * 渲染计量方式列表
 * @param sellerBar
 */
function renderWeightConceptList(sellerBar){
	$.ajax({
		type : "POST",
		url : Context.PATH + "/resource/getallweightConcept.html",
		data :{},

		dataType : "json",
		success : function(weightConceptList) {
			var temp = "";
			$(weightConceptList).each(function(i,e){
				temp+="<option >"+e+"</option>";
			})
			if(!sellerBar){
				sellerBar = $(".inquiryOrders");
			}
			$(sellerBar).find("table tbody tr").each(function(i,e){
				var hiddenWeightConcept = $(this).find("input[name='hiddenWeightConcept']").val();
				var select = $(e).find("td:eq(6) select");
				$(select).empty().append(temp);
				$(select).val(hiddenWeightConcept);
			})
		},
		error : function(xhr, textStatus, errorThrown) {}
	});

}
//1.0老代码
function createInquiryOrder(inquiryOrder){
	var htmlStr = "<div class='tableBar seller-bar' style='border-width: 2px; border-color: #555'><input type='hidden' name='inquiryOrderId' value='"+inquiryOrder.inquiryOrderId+"'/><div class='sellerDiv'></div>" +
		"<div class='seller-footer clearfix none'>" +
//			"<span class='red red-info' name='isSave'>"+inquiryOrder.isSave+"</span><span class='red red-info' name='saveNum'>，共保存0家</span>" +
		"<span class='pull-right'><button class='btn btn-info btn-sm' name='saveInquiryOrder'>保存</button> </div></div>"
//			"<span class='fa fa-long-arrow-right fa-2x'>&nbsp;</span><button class='btn btn-info btn-sm' name='quotation'>填写报价单</button> </span></div></div>"

	return htmlStr;
}
//1.0老代码
function createInquirySeller(inquiryOrderSeller,isFirst,inquiryOrder){
	var sellerConsignType = inquiryOrderSeller.consignType;
	var sellerConsignTypeHtml = "";
	if(sellerConsignType=="consign"){
		sellerConsignTypeHtml='<span class="font-dai">代</span>';
	}else if(sellerConsignType=="temp"){
		sellerConsignTypeHtml='<span class="font-lin">临</span>';
	}else{
		sellerConsignTypeHtml='<span ></span>';
	}
	var htmlStr = "<div class='seller-head clearfix  pos-rel'>"+sellerConsignTypeHtml+"<span class='seller-name' name='sellerName' style='cursor: pointer' seller='"+inquiryOrderSeller.sellerName+"' value='"+inquiryOrderSeller.sellerId+"'>"+inquiryOrderSeller.sellerName+"</span>" +
		"<span class='toggle-info none'> <span class='contacts'> <label>联系人：<select name='contact'>" ;

	var temp = "";
	var tel ="";
	var qq="";
	$(inquiryOrderSeller.contactList).each(function(i,e){
		temp+="<option qq='"+ e.qq+"'>"+e.name+","+e.tel+"</option>";
		if(i==0&&e.qq){
//			tel="<a class='fa fa-phone-square fa-2x' href='javascript:;' title='电话'>&nbsp;</a> ";
			qq = "<a class='fa fa-qq fa-2x' href='javascript:;' title='QQ' val='"+e.qq+"'>&nbsp;</a>";
		}
	})
	htmlStr+=temp;


	htmlStr+="</select></label> </span><span class='qq'>" +
//			"<a class='fa fa-phone-square fa-2x' href='javascript:;' title='电话'>&nbsp;</a> " +
//			"<a class='fa fa-qq fa-2x' href='javascript:;' title='QQ'>&nbsp;</a> " +
	qq+"</span>&nbsp;&nbsp;&nbsp;&nbsp;<span>交易员："+ inquiryOrderSeller.traderName+"</span><span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>" +
	"<span class='red red-info' name='inquiryDate'>" +
	(inquiryOrderSeller.inquiryDateShow?"<"+inquiryOrderSeller.lastUserShow+">"+inquiryOrderSeller.inquiryDateShow+"已询价":"")+"</span>"+
	"<label style='float: right;margin-top: 10px; margin-right: 10px;' class='jyy'><input type='checkbox' name='check' class='ace'><span class='lbl'></span>屏蔽报价</label></span>" +
	(isFirst?"<input type='button' class='btn btn-info btn-sm pull-right' name='isInquiry' value='"+inquiryOrder.isInquiry+"' />":"")+"</div>"+
	"<div class='toggle-bar none'><table class='table'>" +
	"<thead><tr><td style='width: 96px'>品名</td><td style='width: 86px'>材质</td><td style='width: 136px'>规格</td><td style='width: 96px'>厂家</td><td style='width: 76px'>城市</td><td style='width: 96px'>仓库</td>" +
	"<td style='width: 86px'>计量方式</td><td style='width: 76px' class='text-center'>求购重量</td><td style='width: 86px'>重量(吨)</td><td style='width: 76px'>数量(件)</td>" +
	"<td style='width: 86px'>单价(元/吨)</td><td style='width: 70px'>操作</td></tr></thead><tbody></tbody></table></div>"

	return htmlStr;

}
//1.0老代码
function createInquiryItem(inquiryOrderItem,appendForInquiryOrder){
	var htmlStr = "<tr"+(appendForInquiryOrder?" bgcolor='#ffe4d1' ":"") +"><td><span title='"+inquiryOrderItem.categoryName+"' class='ellipsis inline' style='line-height:2.2;font-size:14px;font-weight: bold;width: 76px;' name='categoryName' value='"+inquiryOrderItem.categoryUuid+"'>"+inquiryOrderItem.categoryName+"</span></td>" +
		"<input type='hidden' name='purchaseOrderItemId' value='"+(inquiryOrderItem.purchaseOrderItemId?inquiryOrderItem.purchaseOrderItemId:"")+"'/>"+
		"<input type='hidden' name='resourceId' value='"+(inquiryOrderItem.resourceId?inquiryOrderItem.resourceId:"")+"'/>"+
		"<input type='hidden' name='status' value='"+(inquiryOrderItem.status?inquiryOrderItem.status:"")+"'/>"+
		"<input type='hidden' name='isOK' value='"+(inquiryOrderItem.isOK?inquiryOrderItem.isOK:"")+"'/>" ;
//			"<input type='hidden' name='isFullSpec' value='"+(inquiryOrderItem.purchaseSpecLength==inquiryOrderItem.commonSpecLength)+"'/>" +
//			"<td><span title='"+inquiryOrderItem.materialName+"' class='ellipsis inline' style='line-height:2.2;width: 66px' name='materialName' value='"+inquiryOrderItem.materialUuid+"'>"+inquiryOrderItem.materialName+"</span></td>" ;

	var materialStr = "<td>";
	var materialUuids = inquiryOrderItem.purchaseMaterialUuids;
	var materialUuidArr = materialUuids.split(",");
	var materialNames = inquiryOrderItem.purchaseMaterialNames;
	var materialNameArr = materialNames.split(",");

	materialStr+="<select name='materialName' must='1' style='width: 96px'>";
	var matched = false;
	var options="";
	$(materialUuidArr).each(function(i,e){
		options+="<option value="+materialUuidArr[i];
		if(inquiryOrderItem.materialUuid==materialUuidArr[i]){
			options+=" selected ";
			matched=true;
		}else if(materialUuidArr.length == 1){
			options+=" selected ";
			matched=true;
		}
		options+=">"+materialNameArr[i]+"</option>";
	});
	if(!matched){
		options="<option></option>"+options ;
	}
	materialStr+=options+"</select>";

	materialStr+="</td>";
	htmlStr+=materialStr;


	//"<td><input type='text' must=1 value='"+inquiryOrderItem.spec+"' name='normsName' style='width:90px;'/></td>" ;
//			if(inquiryOrderItem.spec==inquiryOrderItem.purchaseSpec){
//				htmlStr += "<td><input readonly type='text' must=1 value='"+inquiryOrderItem.spec+"' name='normsName' style='width:90px;'/></td>" ;
//			}else{
//				htmlStr += "<td><input type='text' must=1 value='"+inquiryOrderItem.spec+"' name='normsName' style='width:90px;'/></td>" ;
//			}
	//"<td><input type='text' must=1 value='"+inquiryOrderItem.factoryName+"' name='factoryName' factoryId='"+inquiryOrderItem.factoryId+"' style='width:90px;'/></td>" +
	var span = "<span title='"+inquiryOrderItem.purchaseSpec+"' class='ellipsis' style='display: inline-block; line-height:2.2;width: 81px'>"+inquiryOrderItem.purchaseSpec+"</span>";
	var hiddenSpan = "<span title='"+inquiryOrderItem.purchaseSpec+"' class='ellipsis' style='display: none; line-height:2.2;width: 96px'>"+inquiryOrderItem.purchaseSpec+"</span>";
	if(inquiryOrderItem.purchaseSpecLength&&inquiryOrderItem.purchaseSpecLength==inquiryOrderItem.commonSpecLength){
		if(inquiryOrderItem.inquiryOrderItemId){//询价单数据
			htmlStr += "<td><input readonly type='text' must=1 value='"+inquiryOrderItem.spec+"' name='normsName' style='width:80px;'/>"+hiddenSpan+"</td>" ;
		}else{
			if(inquiryOrderItem.resourceId){//有资源
				//查出来的资源规格长度是否满足改品名定义好的的长度，如果一样则锁定
				var specLength = inquiryOrderItem.spec.split("*").length;
				htmlStr += "<td><input "+(specLength==inquiryOrderItem.commonSpecLength?"readonly":"")+" type='text' must=1 value='"+inquiryOrderItem.spec+"' name='normsName' style='width:80px;'/>"+hiddenSpan+"</td>" ;
			}else{//无资源按照被删除的样式走
				htmlStr += "<td><input type='text' must=1 value='"+inquiryOrderItem.purchaseSpec+"' name='normsName' style='width:80px; display: none;'/>"+span+"</td>" ;
			}
		}
	}else{
		if(inquiryOrderItem.inquiryOrderItemId){//询价单数据
			htmlStr += "<td><input type='text' must=1 value='"+inquiryOrderItem.spec+"' name='normsName' style='width:80px;'/>"+hiddenSpan+"</td>" ;
		}else{
			if(inquiryOrderItem.resourceId){
				htmlStr += "<td><input type='text' must=1 value='"+inquiryOrderItem.spec+"' name='normsName' style='width:80px;'/>"+hiddenSpan+"</td>" ;
			}else{//无资源按照被删除的样式走
				htmlStr += "<td><input type='text' must=1 value='"+inquiryOrderItem.spec+"' name='normsName' style='width:80px; display: none;'/>"+span+"</td>" ;
			}
		}
	}

	//钢厂如果指定的话显示指定钢厂，如果没有指定就根据品名Uuid查询钢厂数据
	var factoryStr = "<td>";
	var factorys;
	var factoryIdArr = [];
	var factoryNameArr = [];
	if(inquiryOrderItem.purchaseFactoryIds && inquiryOrderItem.purchaseFactoryIds != "null"){
		var factoryIds = inquiryOrderItem.purchaseFactoryIds;
		factoryIdArr = factoryIds.split(",");
		var factoryNames = inquiryOrderItem.purchaseFactoryNames;
		factoryNameArr = factoryNames.split(",");
	}else{
		factorys = _factoryData[inquiryOrderItem.categoryUuid];
		$(factorys).each(function(i,e){
			factoryIdArr.push(e.uuid);
			factoryNameArr.push(e.name);
		});
	}

	var content = "" , rowSize = 5;
	$(factoryIdArr).each(function (i, e) {
		if (i % rowSize === 0) {
			content += "<div class='textures-con-bar-list bder-b-dashed'>";
		}
		if(inquiryOrderItem.factoryId==e){
			content += '<span><a href="javascript:;" id="" value="' + e + '" class="hover">' + factoryNameArr[i] + '</a></span>';
		}else{
			content += '<span><a href="javascript:;" id="" value="' + e + '" class="">' + factoryNameArr[i] + '</a></span>';
		}

		if ((i + 1) % rowSize === 0 || (i + 1) === factoryIdArr.length) {
			content += "</div>";
		}
	});

	factoryStr += "<div class='form-item factory' style='width: 65px'><input type='text' must='1' id='factoryId' style='width: 65px' class='f-text' factoryId='"+(inquiryOrderItem.factoryId?inquiryOrderItem.factoryId:"")+"' value='"+(inquiryOrderItem.factoryName?inquiryOrderItem.factoryName:"")+"' autocomplete='off' placeholder='单选' readonly>" +
	"<span class='icon down-arr-icon'></span><div class='show-layer textures-bar standard-bar factory_div none' id='showLayerFactory'><div class='textures-con'>"
	factoryStr +=content;
	factoryStr += "</div><div class='btn-bar f-clrfix "+(inquiryOrderItem.purchaseFactoryIds&&inquiryOrderItem.purchaseFactoryIds!="null"?"pull-right":"")+"'>"
	if(!inquiryOrderItem.purchaseFactoryIds || inquiryOrderItem.purchaseFactoryIds == "null"){
		factoryStr += "<div><span style='position: static;line-height:2;'>其他：</span><input name='otherFactoryId' style='position: relative; width: 150px'/></div>"
	}
	factoryStr +="<button class='clear-btn'>清除</button> <button class='confirm-button'>确认</button></div></div></div>"


//			var factoryIds = inquiryOrderItem.purchaseFactoryIds;
//			
//			var factoryIdArr = factoryIds.split(",");
//			var factoryNames = inquiryOrderItem.purchaseFactoryNames;
//			var factoryNameArr = factoryNames.split(",");
//				
//			factoryStr+="<select id='factoryId' style='width: 65px'>";
//			var matched = false;
//			var options="";
//			$(factoryIdArr).each(function(i,e){
//				options+="<option value="+factoryIdArr[i];
//				if(inquiryOrderItem.factoryId==factoryIdArr[i]){
//					options+=" selected ";
//					matched=true;
//				}else if(factoryIdArr.length == 1){
//					options+=" selected ";
//					matched=true;
//				}
//				options+=">"+factoryNameArr[i]+"</option>";
//			});
//			if(!matched){
//				options="<option></option>"+options ;
//			}
//			factoryStr+=options+"</select>";

	factoryStr+="</td>";
	htmlStr+=factoryStr;
	var warehouseStr = "";
	if(inquiryOrderItem.resourceId){//有资源,根据资源所在仓库的所在城市
		warehouseStr="<td><span style='width: 56px;line-height:2.2' class='ellipsis inline' name='cityName' title='"+(inquiryOrderItem.warehouseCityName?inquiryOrderItem.warehouseCityName:'未设置')+"' " +
		"cityId="+(inquiryOrderItem.warehouseCityId?inquiryOrderItem.warehouseCityId:'')+">"+(inquiryOrderItem.warehouseCityName?inquiryOrderItem.warehouseCityName:'未设置')+"</span></td>";

	}else{//无资源，不用显示城市
		warehouseStr="<td><span class='ellipsis inline' style='width: 56px;line-height:2.2' name='cityName'></span></td>";
	}

	inquiryOrderItem.abnormalWarehouse = inquiryOrderItem.abnormalWarehouse?inquiryOrderItem.abnormalWarehouse:"";

	htmlStr+=warehouseStr;
	htmlStr+="<td><input type='text' must=1 value='"+(inquiryOrderItem.warehouseId==null?inquiryOrderItem.abnormalWarehouse:inquiryOrderItem.warehouseName)+"' name='warehouseName' "+(inquiryOrderItem.warehouseId==null?"val=''":"val='"+inquiryOrderItem.warehouseId+"'")+" style='width:60px;'/></td>" +
	"<td><span><select name='weightConcept' must=1 style='width: 61px'></select><input type='hidden' name='hiddenWeightConcept' value='"+inquiryOrderItem.weightConcept+"'></span></td>" +
	"<td class='text-right'><span title='"+inquiryOrderItem.purchaseWeight+"' class='ellipsis inline' style='line-height:2.2;width: 56px' name='purchaseWeight'>"+inquiryOrderItem.purchaseWeight+"&nbsp;&nbsp;&nbsp;</span></td>" +
	"<td class='weightTd'><input must=1 verify='numeric' type='text' style='width:55px;' name='resultWeight' value='"+inquiryOrderItem.resultWeight+"' /></td>" +
	"<td class='quantityTd'><span><input must=1 verify='number' type='number' class='quantity' name='resultQuantity' style='width:40px;' singleWeight='"+inquiryOrderItem.singleWeight+"' value='"+inquiryOrderItem.resultQuantity+"' min='0' /></span></td>" +
	"<td><span><input must=1 verify='numeric' type='text' class='d-text' name='price' value='"+(inquiryOrderItem.price == '99999' ? '电话议价':inquiryOrderItem.price)+"' min='0' /></span></td>" +
//			if(inquiryOrderItem.inquiryOrderItemId){
//				htmlStr+="<td class='weightTd'><input must=1 verify='numeric' type='text' style='width:70px;' name='resultWeight' value='"+inquiryOrderItem.purchaseWeight+"'/></td>"
//						 +"<td class='quantityTd'><span><input must=1 verify='number' type='number' class='quantity' name='resultQuantity' style='width:50px;' singleWeight='"+inquiryOrderItem.singleWeight+"' value='"+inquiryOrderItem.purchaseQuantity+"' min='0'/></span></td>";
//			}else{
//				htmlStr+="<td class='weightTd'><input must=1 verify='numeric' type='text' style='width:70px;' name='resultWeight' value='"+inquiryOrderItem.resultWeight+"' /></td>"
//						 +"<td class='quantityTd'><span><input must=1 verify='number' type='number' class='quantity' name='resultQuantity' style='width:50px;' singleWeight='"+inquiryOrderItem.singleWeight+"' value='"+inquiryOrderItem.resultQuantity+"' min='0' /></span></td>";
//			}

	"<td><a href='javascript:;' class='fa fa-trash-o fa-lg' name='deleteInfoBtn' title='删除'></a></td></tr>"
	var tr = $(htmlStr);
	if(!inquiryOrderItem.inquiryOrderItemId&&!inquiryOrderItem.resourceId||(inquiryOrderItem.inquiryOrderItemId&&!inquiryOrderItem.warehouseId&&!inquiryOrderItem.abnormalWarehouse)){
		clearItemContent(tr);
	}
	return tr;

}

/**
 * 保存询价单
 * @param inquiryOrder
 * @param sellerBar
 * @param noExistWarehouseName
 */
function saveInquiryOrder(inquiryOrder,sellerBar,noExistWarehouseName,e) {
	cbms.loading();
	$.ajax({
		type : "POST",
		url : Context.PATH + "/smartmatch/saveInquiryOrder.html",
		data :{inquiryOrder:inquiryOrder},
		dataType : "json",
		success : function(response) {

			if (response.success) {
				cbms.alert("保存成功！");
				$(sellerBar).find("input[name='res_inqueryOrderId']").eq(0).val(response.data);
				//	$(sellerBar).find(".seller-head").find("input[name='isInquiry']").val("已询价");
				//询价保存过，则“保存”按钮显示成“再次保存”
				$(sellerBar).find("button[name='saveInquiryOrder']").text("再次保存");
				//未保存与已保存的切换
				var isinquired = $(sellerBar).find(".isinquiredshow");
				if(isinquired){
					$(isinquired).html("已询价");
				}

				//保存后刷新询报价信息
				sellerNum($("#id").val(), true, sellerBar);
				//展开匹配卖家详情时获取该组合内卖家当天的询价提示
				getSellersInquiryRecord(sellerBar);

				//补充匹配
				supplementMatchByWeight(e);

			} else {
				cbms.alert(response.data);
			}
			cbms.closeLoading();
		},
		error : function(xhr, textStatus, errorThrown) {}
	});
}

/**
 * 根据重量判断是否补充匹配
 * @param e
 */
function supplementMatchByWeight(e){
	/**
	 * 保存后查看当前保存的资源是否能够满足吨位，不能满足则继续补充匹配
	 * 1）如果是补充匹配的公司，则不再进行补充匹配
	 * 2）如果是当前点击展开的公司，吨位不满足时，补充匹配 a.如果已经补充匹配了两家则不进行匹配
	 *                                        b.如果补充匹配不足两家，则进行补充匹配
	 *                                        补充匹配的应该在保存成功之后进行
	 */
	var appendCloset = $(e).closest(".appendResDiv");//appendResDiv 是补充匹配填充的div，如果保存补充匹配的保存按钮，则不进行补充匹配
	if(appendCloset.length==0){//判断是否是在补充匹配的内容中的
		var subItems = $(e).closest(".menuContent").find(".appendResDiv").find(".menuContent");
		if(subItems.length<2){
			//判断补充匹配时候已经有2家则不再补充匹配
			var supplementItem = [];
			var sunItems = $(e).closest(".itemsShow").find(".sunItems");
			for(var i =0;i< sunItems.length;i++){//检查当前item 是否满足吨位，如果不满足吨位 ，则补充匹配一次
				var weight = parseInt(  $(sunItems[i]).find("input[name='item_res_weight']").val(),10);//获取求购询价详情item的重量
				var inqTotalWeight=0;
				var inqWeight= $(sunItems[i]).find("input[name='res_weight']");
				var inqPrice= $(sunItems[i]).find("input[name='res_price']");
				var priceDetail =null;
				if(inqWeight.length>0){
					for(var j=0;j<inqWeight.length;j++ ){
						if(inqWeight[j]!=null && ''!=inqWeight[j]   ){
							if($(inqPrice).eq(j).val()!=null && ''!=$(inqPrice).eq(j).val()){
								priceDetail = parseInt($(inqPrice).eq(j).val(),10);
								if(priceDetail!=null && priceDetail >0){
									inqTotalWeight+=parseInt($(inqWeight).eq(j).val(),10);
								}
							}
							
						}
					}
					if(inqTotalWeight<weight ){//item_res_id
						supplementItem.push($(sunItems[i]).find("input[name='item_res_id']").val());
					}
				}else{
					supplementItem.push($(sunItems[i]).find("input[name='item_res_id']").val());
				}


			}
			
			//如果已经有补充匹配了一家，检测已经补充匹配的厂家，如果补充匹配的项已经在范围内，则将该项移除不做补充匹配 added 2016.8.10
			if(subItems.length==1){
				var sumitems = $(e).closest(".seller-barl").find(".appendResDiv").find(".seller-barl").eq(0).find(".subitemShow").find(".sunItems");
				for(var k=0;k<sumitems.length;k++){
					var sunItemId =  $(sumitems[k]).find("input[name='item_res_id']").val() ;
					if (supplementItem.indexOf( sunItemId)!=-1){
						supplementItem.splice( supplementItem.indexOf(  sunItemId ) , 1);
					}
				}
			}
			
			
			if(supplementItem.length>0){
				var sellerIds  = $(e).closest(".seller-barl").find("input[name='res_sellerId']");
				var notSpecificSellerId  =  [];
				for(var j=0;j<sellerIds.length;j++ ){
					notSpecificSellerId.push($(sellerIds).eq(j).val());
				}

				var params ={
					purchaseOrderId:$("#id").val(),
					purchaseOrderItemIds:supplementItem.join(","),
					cityId:$(e).closest(".seller-barl").find("input[name='res_city']").val(),
					notSpecificSellerIds:notSpecificSellerId.join(","),
					isAppend:1
				};
				supplementMatch(params,$(e).closest(".seller-barl"));
			}


		}else{
			alert("已补充匹配2家，不再补充匹配");
		}
	}
}



/**
 * 获取已保存的卖家数，以及已保存的采购单明细数量
 * @param purchaseOrderId
 * @param isShow
 * @param sellerBar
 */
function sellerNum(purchaseOrderId, isShow, sellerBar){

	$.ajax({
		type : "POST",
		url : Context.PATH + "/smartmatch/inquiryorder/getSellerNum.html",
		data:{
			purchaseOrderId:purchaseOrderId
		},
		dataType : "json",
		success : function(data) {

//   			$("span[name='saveNum']").text("，共保存"+data.sellerNum+"家");	
			var fractionOfCoverage = cbms.convertFloat(data.itemsNum/purchaseOrder.purchaseOrderItemList.length*100,2);
			$("#saveSellerNum").text(data.sellerNum);
			if (quotaCount == 0) {
				$("#btnCreateOrder").hide();
			}
			$("#fractionOfCoverage").text(fractionOfCoverage);
//    		$("#saveNum").text("已保存"+data.sellerNum+"家报价，覆盖"+fractionOfCoverage+"%资源");
			var html = '';
			if(data.itemsSatisfaction){
				for(var i in data.itemsSatisfaction){
					var en  =  data.itemsSatisfaction[i];
					var rateStr = en.rate.toFixed(2);
					var categoryName = en.categoryName == null ?"":en.categoryName;
					var materialName = en.materialName == null ?"":en.materialName;
					var spec = en.spec == null ?"":en.spec;
					html+="<div> "+categoryName+" "+ materialName +" "+ spec+" 已询价"+en.totalInquiryCount +"次，满足吨位："+ en.totalWeight+" ,满足率："+rateStr+"%.";
				}

			}

			$("#inquiryItemsDetail").html(html);

			if(isShow){
				$(sellerBar).find(".seller-head").find("span[name='inquiryDate']").text("<"+data.userName+">刚刚已询价");
			}

		},
		error : function(xhr, textStatus, errorThrown) {}
	});
}

/**
 * 获取历史成交记录数据
 * @param target
 */
function getBillHistory(target){

	var accountId = $(target).closest(".compDivhead").find("input[name='res_sellerId']").val();
	var isopen = $(target).closest(".compDivhead").find("input[name='is_item_hide']").val();
	if(isopen == 1) {
		//展开时获取
		$.ajax({
			type: "POST",
			url: Context.PATH + "/smartmatch/getBillHistory.html",
			data: {
				accountId: accountId
			},
			dataType: "json",
			success: function (data) {
				renderBillHistory(data, $(target).closest(".compDivhead"));
			},
			error: function (xhr, textStatus, errorThrown) {
			}
		});
	}else{
		//$(target).closest(".seller-head").find(".billHistory").show();

	}
}

/**
 * 显示历史成交的记录到当前展开的公司或者保存询价的公司
 * @param data
 * @param target
 */
function renderBillHistory(data, target){
	var datas = data.data, topheigh, datalength = datas.length,isOdd = "";
	if(data.success){
		topheigh = -(datalength*25+30);
	}else{
		topheigh = -50;
	}
	var html = '<div class="pos-abs" style="border:1px solid #ccc;background:#fff;left: 0px; top:'+topheigh+'px"><table class="table">';
	if(data.success) {
		var datas = data.data;
		for (var i in datas) {
			if(i%2 !=0){
				isOdd = "bgh";
			}

			html += '<tr class="'+isOdd+'"><td>' + datas[i].time + '</td>' +
			"<td>成交</td>"+
			"<td>" + (datas[i].nsortName.length < 6 ? datas[i].nsortName: datas[i].nsortName.substring(0,6) + "...")+ "</td>" +
			"<td>" + datas[i].weight + "吨</td></tr>";
		}

	}else{
		html += "<tr><td>" + data.data + "</td></tr>";
	}
	html += "</table></div>";
	$(target).closest('.menuContent').find('.historyShow').html(html);


}

/**
 * 获取卖家最近一次的询价信息（询价时间及操作人）
 */
function getSellersInquiryRecord(sellerBar){

	var sellerIds=[];
	var  sellerId = $(sellerBar).find("input[name='res_sellerId']").val();

	if(sellerId== undefined ||  sellerId==null || 'null'==sellerId || ''==sellerId){
		return ;
	}
	sellerIds.push( sellerId);



	$.ajax({
		type : "POST",
		url : Context.PATH + "/smartmatch/inquiryorder/getSellersInquiryRecord.html",
		data : {
			sellerIds:JSON.stringify(sellerIds)
		},
		dataType : "json",
		success : function(response) {

			if(response.success){

				$(sellerBar).find('.lastInquiredTime').html('');
				var sellerList = response.data;
				if(sellerList && sellerList.length==1){
					var html = "<span >"+"<"+sellerList[0].lastUserName+">" +sellerList[0].lastInquirytime+"已询价"+"</span>"
					$(sellerBar).find('.lastInquiredTime').append(html);
				}

			}else{
				$(sellerBar).find('.lastInquiredTime').html('');
			}
		},
		error : function(xhr, textStatus, errorThrown) {}
	});

}

/**
 * 根据仓库Id获取仓库所在城市
 * @param warehouseData
 */
function getWarehouseCity(warehouseData){
	var warehouseId = $(warehouseData).attr('val');
	if(warehouseId){

		$.ajax({
			type : "POST",
			url : Context.PATH + "/smartmatch/inquiryorder/getCityByWarehouseId.html",
			data : {
				warehouseId:warehouseId
			},
			dataType : "json",
			success : function(response){
				$(warehouseData).closest("tr").find("span[name='cityName']").text(response.data);
				$(warehouseData).closest("tr").find("span[name='cityName']").attr("title",response.data);
			},
			error : function(xhr, textStatus, errorThrown){}
		});

	}else{
		$(warehouseData).closest("tr").find("span[name='cityName']").text("未设置");
	}
}

/**
 * 根据品名uuid加载钢厂数据
 */
function loadFactoryByCategory() {
	$.ajax({
		type: 'post',
		url: Context.PATH + "/smartmatch/purchaseorder/loadFactoryByCategory.html",
		data: {
			purchaseOrderId:$("#id").val()
		},
		dataType: "json",
		async: false,
		error: function (s) {
		},
		success: function (data) {
			if(data.success){
				_factoryData = data.data;
			}
		}
	});
}







/**
 * 资源匹配，补充匹配资源，并将补充匹配的资源设置到当前展开的公司之后
 * @param params
 */
function supplementMatch(params,sellerBar){
	$.ajax({
		type : "POST",
		url : Context.PATH + "/smartmatch/match.html",
		data :params,
		dataType : "json",
		success : function(response) {

			if (response.success) {
				if(response.data.inquiryList){
					initCompRes(response.data.inquiryList,sellerBar,true);
					//检测匹配的结果 是否包含了需要补充匹配的询价资源，如果没有包含全，则提示没有匹配全补充资源
					checkSupplementMatch(params,sellerBar);
				}else{
					cbms.alert("资源补充匹配不全！");
				}
				//	callback(response.data,sellerBar,sellerId);
			} else {
				cbms.alert("资源补充匹配不全！");
			}
		},
		error : function(xhr, textStatus, errorThrown) {}
	});
}

//检测补充匹配的数据是否完全

function checkSupplementMatch(params,sellerBar){
	var _matchedtItems=[];
	//var append = 	$(sellerBar).find(".appendResDiv");
	//	var subselbar = $(sellerBar).find(".appendResDiv").eq(0).find(".seller-barl");
	$(sellerBar).find(".appendResDiv").eq(0).find(".seller-barl").each(function(index,element){
		
		//var subitemShow =$(element).find(".subitemShow");
		//var sunitems = $(element).find(".subitemShow").find(".sunItems");
		$(element).find(".subitemShow").find(".sunItems").each(function(index,element){
			var count = $(element).find("input[name='item_res_count']" ).val();
			var  id =  $(element).find("input[name='item_res_id']").val();
			
			if($(element).find("input[name='item_res_count']" ).val()>0){
				_matchedtItems.push($(element).find("input[name='item_res_id']").val());
			}
		});
	});
	var _suppItems = params.purchaseOrderItemIds.split(",");
	//var  str1= _suppItems.sort().toString();
	//var str2 = _matchedtItems.sort().toString();
	if(!isContained(_matchedtItems,_suppItems)){
		cbms.alert("资源补充匹配不全！");
	}
		
}

/**
 * a数组包含b数组
 * @param a
 * @param b
 * @returns {Boolean}
 */
function isContained(a, b){
    if(!(a instanceof Array) || !(b instanceof Array)) return false;
    if(a.length < b.length) return false;
    var aStr = a.toString();
    for(var i = 0, len = b.length; i < len; i++){
       if(aStr.indexOf(b[i]) == -1) return false;
    }
    return true;
}




function dateFormat(date, fmt) {
	var o = {
		"M+" : date.getMonth()+1,                 //月份
		"d+" : date.getDate(),                    //日
		"h+" : date.getHours(),                   //小时
		"m+" : date.getMinutes(),                 //分
		"s+" : date.getSeconds(),                 //秒
		"q+" : Math.floor((date.getMonth()+3)/3), //季度
		"S"  : date.getMilliseconds()             //毫秒
	};
	if(/(y+)/.test(fmt))
		fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));
	for(var k in o)
		if(new RegExp("("+ k +")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
	return fmt;
}


/**
 * 默认处理第一个， 把当前城市tab的第一条展开！切换过程中，如果该城市打开了第一条外的其他资源，切换回来时，也能重新展开第一条
 */

function defaultProcessHead(){

	$(".compDiv").find(".cp_msg_detail").addClass("none");
	$(".compDiv").find(".cp_msg_detail").eq(0).removeClass("none");
	cbms.loading();
	supplement($(".cp_msg_detail").eq(0));
	findCpmContacts($(".cp_msg_detail").eq(0));

}

/**
 * 查询公司联系人，点击公司时，加载联系人
 * @param self
 */
function findCpmContacts(self){

	var params ={
		accountId:$(self).closest(".seller-barl").find("input[name='res_sellerId']").val()
	};
	$.ajax({
		type : "POST",
		url : Context.PATH + "/smartmatch/findComContacts.html",
		data :params,
		dataType : "json",
		success : function(response) {
			if (response.success) {
				var htmlStr = "<span  style='cursor: pointer' class='comp_contact' value=\"联系人电话\">";
				if(response.data){
					htmlStr +="<select class='contact' name='contact'>"
					for(var  i in response.data ){
						var ct =  response.data[i];
						htmlStr+=" <option value='"+checkNull(ct.qq)+"' val='"+ ct.id+"'  > " + ct.name +"  "+ ct.tel +"  交易员： " + ct.traderName+"</option>";
					}
					htmlStr +="</select></span>"
				}
				$(self).closest( ".seller-head").find(".cp_msg_detail_contact").html("");
				$(self).closest( ".seller-head").find(".cp_msg_detail_contact").append(htmlStr);
			} else {
				cbms.alert("没有查询到公司联系人！");
			}
		},
		error : function(xhr, textStatus, errorThrown) {}
	});
}


/**
 * 当前公司点击后，进行补充匹配
 * @param self
 */
function  supplement(self){
	setTimeout(function () {
		var sellerBar =$(self).closest(".seller-barl");
		var sellerId  = $(self).closest(".seller-barl").find("input[name='res_sellerId']").val();
		var notSpecificSellerId  =  [];
		notSpecificSellerId.push(sellerId);//用户补充匹配时排除当前公司

		var cityId  = $(self).closest(".seller-barl").find("input[name='res_city']").val();
		var isOpen = $(sellerBar).attr(".isOpen");
		$(sellerBar).find(".appendResDiv").html("");

		if(isOpen=="1"){
			return false;
		}
		/**
		 * 弹窗出该公司最新的成交记录, 最后一次交易时间已经带出来了，不需要重新查询
		 */
			//展开匹配卖家详情时获取该组合内卖家当天的询价提示
		getSellersInquiryRecord(sellerBar);

		//询价保存过，则“保存”按钮显示成“再次保存”
		var isCpInquired = $(sellerBar).find(".compDivhead").find("input[name='res_isInquired']").val() ;
		if(1==isCpInquired){
			$(sellerBar).find(".itemsShow").find(".text-center ").find("button[name='saveInquiryOrder']").text("再次保存");
		}
		var itemids=[],itemCounts=[],quryItem=[];

		$(sellerBar).closest(".seller-barl").find(".itemsShow").find(".sunItems").each(function(i,e){
			itemCounts.push($(e).find("input[name='item_res_count']").val());
			itemids.push($(e).find("input[name='item_res_id']").val());
		});

		for(var count in itemCounts){
			if(itemCounts[count]==0){
				quryItem.push(itemids[count]);
			}
		}
		//需要补充匹配的资源ID列表
		if(quryItem.length==0){
			return ;
		}else{
			var params ={
				purchaseOrderId:$("#id").val(),
				purchaseOrderItemIds:quryItem.join(","),
				notSpecificSellerIds:notSpecificSellerId.join(","),
				cityId:cityId,
				isAppend:1
			};
			supplementMatch(params,sellerBar);
		}

	}, 100);

}




/**
 * 点击卖家展开询价单
 */
function openSellerInit(self){

	setTimeout(function () {
		var sellerBar =self;
		var sellerId  = $(sellerBar).find("input[name='res_sellerId']").val();
		var notSpecificSellerId  =  [];
		notSpecificSellerId.push(sellerId);

		var cityId  = $(sellerBar).find("input[name='res_city']").val();
		var isOpen = $(sellerBar).attr("isOpen");
		$(sellerBar).find(".appendResDiv").html("");

		if(isOpen=="1"){
			return false;
		}

		/**
		 * 弹窗出该公司最新的成交记录, 最后一次交易时间已经带出来了，不需要重新查询
		 */
			//展开匹配卖家详情时获取该组合内卖家当天的询价提示
		getSellersInquiryRecord(sellerBar);

		//询价保存过，则“保存”按钮显示成“再次保存”
		var isCpInquired = $(sellerBar).find(".compDivhead").find("input[name='res_isInquired']").val() ;
		if(1==isCpInquired){
			$(sellerBar).find(".itemsShow").find(".text-center ").find("button[name='saveInquiryOrder']").text("再次保存");
		}
		var itemids=[],itemCounts=[],quryItem=[];

		$(sellerBar).closest(".seller-barl").find(".itemsShow").find(".sunItems").each(function(i,e){
			itemCounts.push($(e).find("input[name='item_res_count']").val());
			itemids.push($(e).find("input[name='item_res_id']").val());
		});

		for(var count in itemCounts){
			if(itemCounts[count]==0){
				quryItem.push(itemids[count]);
			}
		}
		//需要补充匹配的资源ID列表
		if(quryItem.length==0){
			return ;
		}else{
			var params ={
				purchaseOrderId:$("#id").val(),
				purchaseOrderItemIds:quryItem.join(","),
				notSpecificSellerIds:notSpecificSellerId.join(","),
				cityId:cityId,
				isAppend:1
			};
			supplementMatch(params,sellerBar);
		}

	}, 100);

}

function loadSingleFactory(input,nsortUuid,limitFactorys) {
	$.ajax({
		type: 'post',
		url: Context.PATH + "/smartmatch/purchaseorder/GetFactory.html",
		data: {
			nosrtUUID:  nsortUuid
		},
		dataType: "json",
		async: false,
		error: function (s) {
		},
		success: function (data) {
			var showPlace = $(input).closest('#m-search').find('.single_factory_layer .textures-con');
			var factory_nav_link=$(input).closest('#m-search').find('.single_factory_layer .factory-nav-link');
			if (data.statusCode === 0) {
				$(showPlace).empty();
				$(factory_nav_link).empty();
				var content = "", rowSize = 5, navSize = 20;

				var filterData = data.data;
				//过滤当前公司钢厂
				if (limitFactorys && limitFactorys != 'undefined' && limitFactorys.length > 0) {
					filterData = $.grep(data.data, function (iteminfo) {
						return $.inArray(iteminfo.factory.name,limitFactorys)>=0;
					});
				}

				if (filterData.length == 0) {
					content += '<div class="textures-con-bar-list bder-b-dashed">';
					content += '你所选择的品类材质组合下没有厂家';
					content += "</div>";
				} else {
					var first_initials='A',last_initials='Z';
					var start_initials = '', //开始字母
						cur_initials = '', //结束字母
						end_initials = ''; //当前字母
					var temp_initials = '';
					var sfactory = [], efactory = [];
					var nav_index=0;
					$(filterData).each(function (i, e) {
						var temp_initials = (e.factory.initials == null || e.factory.initials == '') ? "其它" : e.factory.initials;

						if (temp_initials != cur_initials) {
							if (sfactory.length == 0) {
								if (efactory.length > 0) {
									sfactory = efactory;
									efactory = [];
									start_initials = cur_initials;
									end_initials = cur_initials;
									cur_initials = '';
								}
							}
							else if ((sfactory.length > 0 && efactory.length > 0)) {
								if (sfactory.length + efactory.length <= navSize) {
									sfactory = sfactory.concat(efactory);
									end_initials = cur_initials;
									cur_initials = '';
									efactory = [];
								}
								else {
									if (nav_index == 0 && first_initials != start_initials.toUpperCase())
										start_initials = first_initials;
									var nav_link = start_initials.toUpperCase() == end_initials.toUpperCase() ? start_initials.toUpperCase() : (start_initials.toUpperCase() + "-" + end_initials.toUpperCase());
									factory_nav_link.append("<li " + (nav_index == 0 ? "class='active'" : "") + "><a href='javascript:void(0)' nav='" + nav_index + "'>" + nav_link + "</a></li>");
									var temp_html = getFactoryRenderHtml(sfactory, rowSize);
									content += "<div class='factory-nav-" + nav_index + (nav_index == 0 ? "" : " none") + "'>" + temp_html + "</div>";

									sfactory = efactory;
									start_initials = cur_initials;
									end_initials = cur_initials;
									cur_initials = '';
									efactory = [];
									nav_index++;

									if ((i + 1) == filterData.length) {
										return false; //已经处理最后一条，直接退出循环
									}
								}
							}
							cur_initials = temp_initials;
						}
						efactory.push(e.factory);
						if ((i + 1) == filterData.length) {
							if (sfactory.length + efactory.length <= navSize) {
								sfactory = sfactory.concat(efactory);
								end_initials = cur_initials;
								cur_initials = '';
								efactory = [];

							}
							if (nav_index == 0 && first_initials != start_initials.toUpperCase())
								start_initials = first_initials;
							if (efactory.length == 0 && cur_initials != last_initials.toUpperCase())
								end_initials = last_initials;
							var nav_link = start_initials.toUpperCase() == end_initials.toUpperCase() ? start_initials.toUpperCase() : (start_initials.toUpperCase() + "-" + end_initials.toUpperCase());
							factory_nav_link.append("<li " + (nav_index == 0 ? "class='active'" : "") + "><a href='javascript:void(0)' nav='" + nav_index + "'>" + nav_link + "</a></li>");
							var temp_html = getFactoryRenderHtml(sfactory, rowSize);
							content += "<div class='factory-nav-" + nav_index + (nav_index == 0 ? "" : " none") + "'>" + temp_html + "</div>";

							sfactory = efactory;
							start_initials = cur_initials;
							end_initials = cur_initials;
							cur_initials = '';
							efactory = [];
							nav_index++;

							if (efactory.length > 0) {
								if (cur_initials != last_initials.toUpperCase())
									cur_initials = last_initials;
								var nav_link = end_initials.toUpperCase() == last_initials ? last_initials : (end_initials.toUpperCase() + "-" + last_initials);
								factory_nav_link.append("<li " + (nav_index == 0 ? "class='active'" : "") + "><a href='javascript:void(0)' nav='" + nav_index + "'>" + nav_link + "</a></li>");
								var temp_html = getFactoryRenderHtml(efactory, rowSize);
								content += "<div class='factory-nav-" + nav_index + (nav_index == 0 ? "" : " none") + "'>" + temp_html + "</div>";
								nav_index++;
							}
						}
					});
				}
				$(showPlace).append(content);
			} else {
				alert("加载厂家发生错误:" + data.message);
			}
		}
	});
}

function initFactoryEvent(){
	//厂家Tab点击事件
	$(document).on("click", "input#addorupdate-factoryName", function () {
		var limitfactory = $(this).attr("limitfactory");
		var factoryArray=[];
		if (limitfactory && limitfactory != '' && limitfactory != 'null' && limitfactory != 'undefined') {
			factoryArray = limitfactory.split(',');
		}
		loadSingleFactory($(this), $("#addorupdate-category").attr("val"), factoryArray);
		if (factoryArray.length == 0) {
			$(".single_factory_layer #single_factory_others").removeClass("none");
		}
		else {
			$(".single_factory_layer #single_factory_others").addClass("none");
		}
		$(this).closest('#m-search').find('.single_factory_layer').show();
	});

	//厂家点击事件
	$(document).on("click", ".single_factory_layer .textures-con span a", function () {
		if ($(this).hasClass("hover")) {
			$(this).removeClass("hover");
		}
		$('#addorupdate-factoryName').attr("val",$(this).attr("factoryid"));
		$('#addorupdate-factoryName').val($(this).text());
		$(".single_factory_layer").hide();
		return false;
	});

	//厂家内确定按钮点击事件
	$(document).on("click", ".single_factory_layer button.confirm-btn", function () {
		$(".single_factory_layer").hide();
		return false;
	});

	//厂家内清除按钮点击事件
	$(document).on("click", ".single_factory_layer button.clear-btn", function () {
		$(this).closest("div.factory").find(".single_factory_layer a").removeClass("hover");
		$(this).closest("div.factory").find("input[name=otherFactoryIds]").attr("val", "");
		$(this).closest("div.factory").find("input[name=otherFactoryIds]").val("");
		return false;
	});

	$(document).on('click',".single_factory_layer .textures-nav a[nav]", function (index,e) {
		$(this).parent().addClass("active").siblings().removeClass("active");
		var nav_index=$(this).attr("nav");
		$(".single_factory_layer .textures-con .factory-nav-"+nav_index).removeClass("none").siblings().addClass("none");
	});

	//厂家内其他输入框输入事件
	$(document).on("input", ".single_factory_layer input[name=otherFactoryIds]", function () {
		var input = $(this);
		$(this).attr("val", "");
		var factoryDiv = $(this).closest("div.factory");
		//去掉界面上选中的那些仓库
		var temp = _factory.slice(0);
		var factoryinput=$(this);
		showPYMatchList(input, temp, 'id', 'name', function(){
			$('#addorupdate-factoryName').attr("val",factoryinput.attr("val"));
			$('#addorupdate-factoryName').val(factoryinput.val());
			$(".single_factory_layer").hide();
		});
		$("#dropdown").css("z-index", 9999);
		return false;
	});
}

/**
 * 规格是否可编辑
 * 
 * 匹配出的规格也是唯一的修改时不能修改，匹配出的规格不是唯一的修改时可能修改;
 * 规格小于等于3个的，各字段都有唯一值，大于3个的前3个字段都有唯一值
 * @param spec
 */
function specReadonlyFlag(spec,categoryUUid){
	if(spec == null || spec ==""){
		return "";
	}
	if(spec.indexOf(',') >= 0){
		return "";
	}
	if(spec.indexOf('-') >= 0){
		return "";
	}
	var specs = spec.split("*");
	var resu = '';
	$.ajax({
		type: 'get',
		url: Context.PATH + "/resource/getNorms.html",
		data: {
			categoryUuid:  categoryUUid
		},
		dataType: "json",
		async: false,
		error: function (s) {
		},
		success: function (result) {
			if(result.success){
				var normsLength= result.data.length;
				if(normsLength <= 3){
					if(specs.length<normsLength){
						resu='';
					}else{
						resu = "readonly";
					}
				}else {
					if(specs.length <3){
						resu= '';
					}else{
						resu = "readonly";
					}
				}
			}
			
		}
	});
	
	return resu;
}
