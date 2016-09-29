var _saveNum=0;
var _htmlString='';
var _inquiryOrder={};
var _factoryData=[];
var _warehouseData=[];
var _noExistWarehouseName;
var _factoryData;

jQuery(function ($) {   
	
	getWarehouseData();
	
	/**
	 * 保存询价单
	 */
    $(document).on("click","button[name='saveInquiryOrder']",function(){ 
    	purchaseOrder.clickedSaveInquiry = true;
    	_noExistWarehouseName=[];
    	var sellerBar = $(this).closest(".seller-bar");
    	if (!setlistensSave(sellerBar)){
    		return false;
    	}
    	var isSaveOrder=false;
    	var _inquirySeller=[];
    	
    	//获取组合询价信息
    	$(this).closest(".seller-bar").find(".sellerDiv").find(".seller-head").each(function(i,e){
    		var _inquiryItem=[];
    		var isSaveSeller=false;
    		$(e).next(".toggle-bar").find("tbody").children("tr").each(function(index,element){
    			var op = $(this).find("td:last a").attr("title") != "编辑" ? "save" : "del";
//    			if(op=="del" && !$(element).find("input[name='resourceId']").val()) return true;
    			if(op=="save" && $(element).find("input[name='warehouseName']").attr('val')){
    				isSaveSeller=true;
    				isSaveOrder=true;
    			}else if(op=="save" && !$(element).find("input[name='warehouseName']").attr('val')){
    				_noExistWarehouseName.push($(element).find("input[name='warehouseName']").val());
    			}
    			var items = {categoryUuid:$(element).find("span[name='categoryName']").attr('value'), 
    					categoryName:$(element).find("span[name='categoryName']").text(), 
    					materialUuid:$(element).find("select[name='materialName']").val(),
    					materialName:$(element).find("select[name='materialName'] option:selected").text(),
    					normsName:$(element).find("input[name='normsName']").val(),
    					factoryId:$(element).find("#factoryId").attr("factoryId"),
    					factoryName:$(element).find("#factoryId").val(),
    					warehouseId:$(element).find("input[name='warehouseName']").attr('val'),
    					warehouseName:$(element).find("input[name='warehouseName']").val(),
    					weightConcept:$(element).find("select[name='weightConcept']").val(),
    					resultWeight:$(element).find("input[name='resultWeight']").val(),
    					resultQuantity:$(element).find("input[name='resultQuantity']").val(),
    					price:$(element).find("input[name='price']").val(),
    					purchaseOrderItemId:$(element).find("input[name='purchaseOrderItemId']").val(),
    					resourceId:$(element).find("input[name='resourceId']").val(),
    					status:$(element).find("input[name='status']").val(),
    					option:op};
    					
    			_inquiryItem.push(items);
    			
    		});		
    		var seller={sellerName:$(e).find("span[name='sellerName']").attr('seller'),isSaveSeller:isSaveSeller,
    				sellerId:$(e).find("span[name='sellerName']").attr('value'),itemList:_inquiryItem};
    		if(_inquiryItem.length>0){
    			_inquirySeller.push(seller);
    		}
    		
    	});
    	var order={purchaseOrderId:$("#id").val(),sellerList:_inquirySeller,inquiryOrderId:$(sellerBar).find("input[name='inquiryOrderId']").val(),isSaveOrder:isSaveOrder};
    	var itemCount=0;
    	$(order.sellerList).each(function(i,e){
    		itemCount+=e.itemList.length;
    	});
    	if(itemCount==0){
    		cbms.alert("没资源需要保存");
    		return false;
    	}
    	saveInquiryOrder(JSON.stringify(order),sellerBar,_noExistWarehouseName);
    	
    });
    
    /**
     * 点击卖家展开询价单
     */
    $(document).on("click",".seller-head",function(){
    	var self = this;
    	setTimeout(function () {
    		var sellerBar =$(self).closest(".seller-bar");
        	var isOpen = $(sellerBar).attr("isOpen");
	    	if(isOpen=="1"){
	    		return false;
	    	}
	    	
	        $(".seller-head").find(".btn").show();
	        $(self).find(".btn").hide();
	        $(".toggle-bar, .toggle-info, .seller-footer").hide();
	        $(".seller-bar").find(".seller-head:gt(0)").hide();
	        $(".seller-bar").attr("isOpen","0");
	        $(sellerBar).attr("isOpen","1").find(".seller-head,.toggle-bar, .toggle-info, .seller-footer").show();
	        
	        //展开匹配卖家详情时获取该组合内卖家当天的询价提示
	        getSellersInquiryRecord(sellerBar);
	        
	        //询价保存过，则“保存”按钮显示成“再次保存”
	        if($(self).find("input[name='isInquiry']").val() == "已询价"){
				$(self).closest(".seller-bar").find("button[name='saveInquiryOrder']").text("再次保存")
			}
	        
	        var isInquiry = isInqueryBar(sellerBar);
	        if(isInquiry){
	        	$(sellerBar).find(".seller-name").each(function(i,e){
	        		var sellerId =$(e).attr("value");
	        		var purchaseOrderItemIdArr=[];
	        		var itemList = $(e).parent().next().find("tbody tr input[name='purchaseOrderItemId']");
	        		var itemArr = [];
	        		$(itemList).each(function(m,n){
	        			var id = $(n).val();
	        			itemArr.push(id);
	        		});
	        		
	        		$(purchaseOrder.purchaseOrderItemList).each(function(j,f){
	        			if($.inArray(""+f.id,itemArr)==-1){
	        				purchaseOrderItemIdArr.push(f.id);
	        			}
	        		});
	        		var params ={
		    				purchaseOrderId:$("#id").val(),
		    				purchaseOrderItemIds:purchaseOrderItemIdArr.join(","),
		    				notSpecificSellerIds:null,
		    				specificSellerId:sellerId,
		    				isAppend:1
		    			};
	        		if(params.purchaseOrderItemIds!=""){
	        			match(params,renderSearchResult,sellerBar,sellerId);
	        		}
		    		
	        	});
	        	
	        }else{
	        	checkContinueMatching(sellerBar);
	        }
	        
    	}, 100);

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
    	
    	$(".seller-bar").each(function(index,element){
    		if(isInqueryBar($(element))){

    			//获取屏蔽的卖家id
    			$(element).find(".sellerDiv").find(".seller-head").each(function(i,e){
        			if($(e).find("input[name='check']").is(":checked")){
            			blockInquiryOrderSellerIds.push($(e).find("span[name='sellerName']").attr('value'));
            		}
        		});
//        		location.href=Context.PATH + "/smartmatch/inquiryorder/create.html?id=" + $("#id").val()+"&blockInquiryOrderSellerIds=" +blockInquiryOrderSellerIds.join(",");
    		}
    	});

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
    
	/**
	 * 仓库模糊匹配
	 */
    $(document).on('input propertychange','input[name="warehouseName"]', function () {
    	showPYMatchList($(this),_warehouseData,"id","name",null);
    	$("#dropdown").css("z-index",999999);
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
	 */
	$(document).on("mouseover",'span.seller-name',function () {
		if($(this).closest(".seller-bar").attr("isopen") == "1") {
			getBillHistory($(this));
		}
	});
	$(document).on("mouseout",'span.seller-name',function () {
		$(".billHistory").hide();
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
	
	//厂家Tab点击事件
	$(document).on("click","#factoryId",function(){
		$(".show-layer").hide();
		$(this).closest(".factory").find("#showLayerFactory").attr("style","display: block;");
    });
	
	//厂家内确定按钮点击事件
    $(document).on("click", ".factory_div button.confirm-button", function () {
        $(".factory_div").hide();
        if(!$(this).closest('div.factory').find("input[name=otherFactoryId]").attr("val")){
            $(this).closest(".factory").find("#factoryId").val($(this).closest('div.factory').find(".factory_div a.hover").html());
            $(this).closest(".factory").find("#factoryId").attr("factoryId",$(this).closest('div.factory').find(".factory_div a.hover").attr("value"));
        }
    });
    
  //厂家点击事件
    $(document).on("click", ".factory_div .textures-con span a", function () {
        if ($(this).hasClass("hover")) {
            $(this).removeClass("hover");
        } else {
        	$(this).closest("div.factory").find("input[name=otherFactoryId]").attr("val", "");
            $(this).closest("div.factory").find("input[name=otherFactoryId]").val("");
        	$(this).closest("div.factory").find(".factory_div a.hover").removeClass("hover");
            $(this).addClass("hover");
        }
    });
    
  //厂家内清除按钮点击事件
    $(document).on("click", ".factory_div button.clear-btn", function () {
        $(this).closest("div.factory").find(".factory_div a").removeClass("hover");
        //$(this).closest("div.factory").find("input[name=otherFactory]").prop("checked",false);
        $(this).closest("div.factory").find("input[name=otherFactoryId]").attr("val", "");
        $(this).closest("div.factory").find("input[name=otherFactoryId]").val("");
    });
    
    
  //厂家内其他输入框输入事件
    $(document).on("input", "input[name=otherFactoryId]", function () {
        var input = $(this);
        $(this).closest("div.factory").find(".factory_div a.hover").removeClass("hover");
        $(this).attr("val", "");
        var factoryDiv = $(this).closest("div.factory");
        //去掉界面上选中的那些仓库
        var temp = _factory.slice(0);
        var existsIds = $(factoryDiv).find(".factory_div a");
        if(existsIds.length>0){
            $(existsIds).each(function () {
                for (var i in temp) {
                    if (temp[i].id == $(this).attr("factoryid")) {
                        temp.splice(i, 1);
                    }
                }
            });
        }
        showPYMatchList(input, temp, 'id', 'name', function(){
    		$(input).closest(".factory").find("#factoryId").val($(input).closest('div.factory').find("input[name=otherFactoryId]").val());
            $(input).closest(".factory").find("#factoryId").attr("factoryId",$(input).closest('div.factory').find("input[name=otherFactoryId]").attr("val"));

        });
        $("#dropdown").css("z-index", 9999);
        return false;
    });

	//填写询价单联系人change事件
	$(document).on("change", "select[name='contact']", function(){
		var qq = $(this).children('option:selected').attr("qq");
		var html = (qq != "" ? "<a class='fa fa-qq fa-2x' href='javascript:;' title='QQ' val='"+qq+"'>&nbsp;</a>" : "");
		$(this).closest(".toggle-info").find("span.qq").html(html);
	})
});

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
 * 渲染匹配结果
 */
function renderSearchResult(data,sellerBar,sellerId){
	
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
		var firstInqueryOrder = $(".inquiryOrders").find(".seller-bar:eq(0)");
		var sellerCount = $(firstInqueryOrder).find(".seller-head").length;
		if(sellerCount>0){
			var sellerId=$(firstInqueryOrder).find(".seller-head:eq(0) span.seller-name").attr("value");
			if(sellerId==$("#specificSeller").attr("val")){
				//1.不处理
				//2.替换掉第一个
				//$(firstInqueryOrder).remove();
				return false;
			}
		}
		$(".toggle-bar, .toggle-info, .seller-footer").hide();
		
        //$(".seller-bar").find(".seller-head:gt(0)").hide();
//        $(this).closest(".seller-bar").find(".seller-head,.toggle-bar, .toggle-info, .seller-footer").show();
		for (var i in inquiryList){
			var inquiryOrders = $(createInquiryOrder(inquiryList[i]));
			for(var j in inquiryList[i].sellerList){
				var seller = $(createInquirySeller(inquiryList[i].sellerList[j],true,inquiryList[i]));
				for(var k in inquiryList[i].sellerList[j].itemList){
					var item = $(createInquiryItem(inquiryList[i].sellerList[j].itemList[k]));
					$(seller).find("tbody").append(item);
				} 
				$(inquiryOrders).find(".sellerDiv").append(seller);  
			}
			$(".inquiryOrders").prepend(inquiryOrders);
//			$(".inquiryOrders").find(".seller-head:eq(0)").click();
			$(".toggle-bar, .toggle-info, .seller-footer").hide();
			
		}
		resetInquiryShowStatus();
		$("#purchaseFooter").show();
	}else if(!sellerBar){//全部展现
		$(".assign-bar, #purchaseFooter").show();
		if(inquiryList.length==0){
			utils.showMsg("未匹配到任何结果", '', 'error', 1000);
			$("#purchaseFooter").hide();
//			return false;
		}
		$(".inquiryOrders").empty();
//		$(".seller-bar").each(function(i,e){
//			var isInquiry =isInqueryBar(e);
//			if(!isInquiry){
//				$(e).remove();
//			}
//		});
		for (var i in inquiryList){
			var inquiryOrders = $(createInquiryOrder(inquiryList[i]));
			for(var j in inquiryList[i].sellerList){
				if(j == 0){
					var seller = $(createInquirySeller(inquiryList[i].sellerList[j],true,inquiryList[i]));
				}else{
					var seller = $(createInquirySeller(inquiryList[i].sellerList[j],false,inquiryList[i]));
				}
				for(var k in inquiryList[i].sellerList[j].itemList){
					var item = $(createInquiryItem(inquiryList[i].sellerList[j].itemList[k]));
					$(seller).find("tbody").append(item);
				} 
				$(inquiryOrders).find(".sellerDiv").append(seller);  
			}
			$(".inquiryOrders").append(inquiryOrders);
			if(i == 0 && $(".inquiryOrders .seller-bar").length==1){//
//				$("input[name='isInquiry']").hide();
//				$(".toggle-bar, .toggle-info, .seller-footer").show();
				$(".seller-bar .seller-head").click();
//				$(".seller-bar").find(".seller-head").show();
//				$(sellerBar).attr("isOpen","1");
			}
			sellerNum($("#id").val(), false);
		}
		$(".seller-bar:gt(0)").find(".seller-head:gt(0)").hide();
		
		//询价保存过，则“保存”按钮显示成“再次保存”
		if($(".seller-bar:eq(0)").find(".seller-head:eq(0)").find("input[name='isInquiry']").val() == "已询价"){
			$(".seller-bar:eq(0)").find("button[name='saveInquiryOrder']").text("再次保存")
		}

		var _blockInquiryOrderSellerIds = purchaseOrder.blockInquiryOrderSellerIds;
		if(_blockInquiryOrderSellerIds !=null && _blockInquiryOrderSellerIds.toString().length>0){
			var blockSellers= _blockInquiryOrderSellerIds.split(",");
			$(".seller-head").each(function(i,e){
				if(contains(blockSellers,$(e).find("span[name='sellerName']").attr('value'))){
					$(e).find("input[name='check']").attr("checked",'true');
				}
    		});
			_blockInquiryOrderSellerIds='';
		}

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
	renderWeightConceptList();
	$(sellerBar).find(".toggle-bar, .toggle-info, .seller-footer").show();
	var isInquiry =isInqueryBar(sellerBar);
	if(sellerBar!="specificAppend"&&!isInquiry){
		checkContinueMatching(sellerBar);
	}
	return false;
}   

function contains(arr, obj) {  
    var i = arr.length;  
    while (i--) {  
        if (arr[i] === obj) {  
            return true;  
        }  
    }  
    return false;  
} 

function resetInquiryShowStatus(){
	$("input[name='isInquiry']").show();
	$("input[name='isInquiry']:eq(0)").hide();
	$(".seller-bar").find(".seller-head:gt(0)").hide();
    $(".seller-bar:eq(0)").find(".seller-head,.toggle-bar, .toggle-info, .seller-footer").show();
	$(".seller-bar").attr("isOpen","0");
	$(".seller-bar:eq(0)").attr("isOpen","1");
}

function isInqueryBar(sellerBar){
	return $(sellerBar).find("input[name='inquiryOrderId']").val()!="0";
}

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

function createInquiryOrder(inquiryOrder){
	var htmlStr = "<div class='tableBar seller-bar' style='border-width: 2px; border-color: #555'><input type='hidden' name='inquiryOrderId' value='"+inquiryOrder.inquiryOrderId+"'/><div class='sellerDiv'></div>" +
			"<div class='seller-footer clearfix none'>" +
//			"<span class='red red-info' name='isSave'>"+inquiryOrder.isSave+"</span><span class='red red-info' name='saveNum'>，共保存0家</span>" +
			"<span class='pull-right'><button class='btn btn-info btn-sm' name='saveInquiryOrder'>保存</button> </div></div>" 
//			"<span class='fa fa-long-arrow-right fa-2x'>&nbsp;</span><button class='btn btn-info btn-sm' name='quotation'>填写报价单</button> </span></div></div>"

	return htmlStr;
}

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
function saveInquiryOrder(inquiryOrder,sellerBar,noExistWarehouseName) {
    $.ajax({
        type : "POST",
        url : Context.PATH + "/smartmatch/saveInquiryOrder.html",
        data :{inquiryOrder:inquiryOrder},
        dataType : "json",
        success : function(response) {
            if (response.success) {
//            	if(noExistWarehouseName.length>0){
//            		cbms.alert("'"+ noExistWarehouseName.join("仓库,") +"仓库'没有在基础数据中维护，相应询价信息未保存，请确认后联系管理员添加！");
//            	}else{
            		cbms.alert("保存成功！");
//            	}
                $(sellerBar).find("input[name='inquiryOrderId']").val(response.data);
        		$(sellerBar).find(".seller-head").find("input[name='isInquiry']").val("已询价");
        		//询价保存过，则“保存”按钮显示成“再次保存”
        		$(sellerBar).find("button[name='saveInquiryOrder']").text("再次保存");
        		sellerNum($("#id").val(), true, sellerBar);
        		
            } else {
                cbms.alert(response.data);
            }
        },
        error : function(xhr, textStatus, errorThrown) {}
    });
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
        	$("#fractionOfCoverage").text(fractionOfCoverage);
//    		$("#saveNum").text("已保存"+data.sellerNum+"家报价，覆盖"+fractionOfCoverage+"%资源");
    		if(isShow){
    			$(sellerBar).find(".seller-head").find("span[name='inquiryDate']").text("<"+data.userName+">刚刚已询价");
    		}
        },
        error : function(xhr, textStatus, errorThrown) {}
    });
}

function getBillHistory(target){
	var accountId = $(target).attr("value");
	if($(target).closest(".seller-head").find('.billHistory').size() == 0) {
		$.ajax({
			type: "POST",
			url: Context.PATH + "/smartmatch/getBillHistory.html",
			data: {
				accountId: accountId
			},
			dataType: "json",
			success: function (data) {
				renderBillHistory(data, $(target));
			},
			error: function (xhr, textStatus, errorThrown) {
			}
		});
	}else{
		$(target).closest(".seller-head").find(".billHistory").show();
	}
}

function renderBillHistory(data, target){
	var html = '<div class="show-layer product-bar billHistory" style=""><table class="table"><tbody>';
	if(data.success) {
		var datas = data.data;
		for (var i in datas) {
			html += "<tr><td>" + datas[i].time + "</td>" +
			"<td>成交</td>" +
			"<td>" + (datas[i].nsortName.length < 6 ? datas[i].nsortName: datas[i].nsortName.substring(0,6) + "...")+ "</td>" +
			"<td>" + datas[i].weight + "吨</td></tr>";
		}
	}else{
		html += "<tr><td>" + data.data + "</td></tr>";
	}
	html += "</tbody></table></div>";
	$(target).closest(".seller-head").append(html);
	var div = $(target).closest(".seller-head").find(".billHistory");
	$(div).find("tr:odd").css("background-color","#F7F7F7");
	//$(div).find("tr").css("border-bottom", "1px dashed #68DEEA");
	$(div).css("width", "300px").css("top", "-" + $(div).height() + "px").css("left", "40px").css('padding-bottom', "0px");
    $(div).show();
}

/**
 * 获取卖家最近一次的询价信息（询价时间及操作人）
 */
function getSellersInquiryRecord(sellerBar){
	var sellerIds=[];
	$(sellerBar).find(".seller-head").each(function(i,e){
		sellerIds.push($(e).find("span[name='sellerName']").attr('value'));
	});
	
	$.ajax({
		type : "POST",
		url : Context.PATH + "/smartmatch/inquiryorder/getSellersInquiryRecord.html",
		data : {
			sellerIds:JSON.stringify(sellerIds)
		},
		dataType : "json",
		success : function(response) {
			if(response.success){
				var sellerList = response.data;
				for(var i in sellerList){
					$(sellerBar).find(".seller-head").each(function(index,element){
						if($(element).find("span[name='sellerName']").attr('value') == sellerList[i].sellerId){
							$(element).find("span[name='inquiryDate']").text("<"+sellerList[i].lastUserName+">"+sellerList[i].lastInquirytime+"已询价");
						}
					});
				}
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
