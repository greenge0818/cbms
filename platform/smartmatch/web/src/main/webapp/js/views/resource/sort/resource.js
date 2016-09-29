//全局变量
var _resourcePage = {
	selectedIds:[], //数据行选中id记录集
    dt:null,        //datatables
    oneKeyOprtObj:null //一键操作查询对象
}

var limitPrices=[];

$(document).ready(function() {
	//加载服务中心
    $.post(Context.PATH + '/common/allOrganizationList.html','',function(result){
        $("#serviceArea").empty();
        $("#serviceArea").append("<option value=''>-选择服务中心-</option>");
        if(result.success){
            for (var i = 0; i < result.data.length; i++) {
                $("#serviceArea").append("<option value='" + result.data[i].id + "'>" + result.data[i].name + "</option>");
            }
        }
    });
	
	if(!utils.isEmpty(tabIndex)){
		var index = parseInt(tabIndex);
		$("#myTab4 li").removeClass("active");
		$("#myTab4").find("li:eq("+index+")").addClass("active");
		changeTabClass();
	}else{
		changeTabClass();
	}
	reloadQueryObj();
	
    var url = Context.PATH + "/resource/search.html";
    _resourcePage.dt = jQuery("#dynamic-table").DataTable({
        "processing" : false,
        "serverSide" : true,
        "searching" : false,
        "ordering" : false,
        "paging" : true,
        //"bInfo" : false,
        "autoWidth": false,
		"iDisplayLength" : 50,
		"aLengthMenu" : [10,30,50,100],//定义每页显示数据数量
        "ajax" : {
            "url" : url,
            "type" : "POST",
            data : function(d) {
                return $.extend({}, d, _resourcePage.oneKeyOprtObj);
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
            // operation button
            $('td:last', nRow).html(generateOptHtml(aData));
        },
        columns : [
            {data:"id","mRender":function(e,t,f){
            	if(f.sourceType=='history_transaction'&& f.isException=='normal')
            		return '';
            	else 
            		return "<label class='pos-rel'><input type='checkbox' name='checkSource' val='"+e+"'class='ace'><span class='lbl'></span></label>";
            }},
            {data : 'accountName'},
            {data : 'categoryName'},
            {data : 'materialName',"mRender":function(e,t,f){return exceptionRender(e,f.materialUuid);}},
            {data : 'spec',"mRender":function(e,t,f){return exceptionRender(e,f.spec);}},
            {data : 'factoryName',"mRender":function(e,t,f){return exceptionRender(e,f.factoryId);}},
            {data : 'cityName'},
            {data : 'warehouseName',"mRender":function(e,t,f){return exceptionRender(e,f.warehouseId);}},
            {data : 'weightConcept'},
            {data : 'quantity'},
            {data : 'weight',"mRender":function(e,t,f){
            	if(e!=null){
            		return e.toFixed(3);
            	}else{
            		return e;
            	}
        	}},
            {data : 'price', "sClass": "text-right","mRender":function(e,t,f){
            	if(e!=null&& e!=undefined)
            		if(e==99999){
            			return "电话议价";
            		}else{
            			return e.toFixed(2);
            		}
            	}
        	},
            {data : 'priceChange',"sClass": "text-center","mRender":function(e,t,f){if(e>0){return "+"+e}else if(e==0){return "持平"}else{return e};}},
            {data : 'lastUpdatedBy'},
            {data : 'status',"mRender":function(e){
            	if(e=='approved'){ return '已审核';}
            	else if(e=='declined'){ return '待审核';}
            }},
            {data : 'sourceType',"mRender":function(e,t,f){
            	if(e=='history_transaction'){
            		return "历史成交"
            	}else if(e=='daily_common'){
            		return "日常资源"
            	}else if(e=='inquiry'){
            		return "询价资源"
            	}else{
            		return "-"
            	};
            	}
            },
            {defaultContent:""}
        ],
        columnDefs: [
		{
			"targets": 9, //第几列 从0开始
			"data": "price",
			"render": renderAmount
		},
		{
			sDefaultContent: '', //解决请求参数未知的异常
			aTargets: ['_all']
		},
		{
			"targets": 13, //第几列 从0开始
			"render": function (data, type, full, meta) {
				var status=$("#myTab4 li.active input").val();
				if (status == 8) {
					return dateFormat(new Date(full.lastUpdated), "yyyy-MM-dd hh:mm:ss");
				}else{
					return data;
				}
			}
		}
	 	],
        "drawCallback": function(e,t) {
        	var status=$("#myTab4 li.active input").val();
        	if(status==3){//历史成交tab隐藏多选
        		$("#dynamic-table th:eq(0)").hide();
        		$("#dynamic-table tr").each(function(){
        			$(this).find("td:first").hide();
        		});
        		$("#dynamic-table th:eq(15)").show();
        	}else if(status==4){//异常资源TAB隐藏
        		$("#dynamic-table th:eq(0)").show();
        		$("#dynamic-table tr").each(function(){
        			$(this).find("td:first").show();
        		});
        	//	$("#dynamic-table th:eq(13)").show();
        		$("#dynamic-table th:eq(15)").show();
        	}else{
        		$("#dynamic-table th:eq(0)").show();
        		$("#dynamic-table th:eq(15)").hide();
				$("#dynamic-table tr").each(function(){
					$(this).find("td:eq(15)").hide();
				});
        	}
        	//modify by caosulin@prcsteel.com 待审核显示字段 所在城市和跌涨
        	if(status=='0'){
        		$("#dynamic-table th:eq(6)").show();
        		$("#dynamic-table th:eq(12)").show();
        		$("#dynamic-table th:eq(14)").show();
        		$("#dynamic-table th:eq(13)").show();
        	}
			else if(status=='8'){
				$("#dynamic-table th:eq(0)").hide();
				$("#dynamic-table tr").each(function(){
					$(this).find("td:eq(0)").hide();
				});
				$("#dynamic-table th:eq(1)").hide();
				$("#dynamic-table tr").each(function () {
					$(this).find("td:eq(1)").hide();
				});
				// $("#dynamic-table th:eq(7)").hide();
				// $("#dynamic-table tr").each(function () {
				// 	$(this).find("td:eq(7)").hide();
				// });
				$("#dynamic-table th:eq(9)").hide();
				$("#dynamic-table tr").each(function () {
					$(this).find("td:eq(9)").hide();
				});
				$("#dynamic-table th:eq(10)").hide();
				$("#dynamic-table tr").each(function () {
					$(this).find("td:eq(10)").hide();
				});
				$("#dynamic-table th:eq(12)").hide();
				$("#dynamic-table tr").each(function () {
					$(this).find("td:eq(12)").hide();
				});
				// $("#dynamic-table th:eq(13)").hide();
				// $("#dynamic-table tr").each(function () {
				// 	$(this).find("td:eq(13)").hide();
				// });
				$("#dynamic-table th:eq(14)").hide();
				$("#dynamic-table tr").each(function () {
					$(this).find("td:eq(14)").hide();
				});
				$("#dynamic-table th:eq(16)").hide();
				$("#dynamic-table tr").each(function () {
					$(this).find("td:eq(16)").hide();
				});
			}
			else {
        		/*$("#dynamic-table th:eq(6)").hide();
        		$("#dynamic-table tr").each(function(){
        			$(this).find("td:eq(6)").hide();
        		});*/
        		$("#dynamic-table th:eq(12)").hide();
        		$("#dynamic-table tr").each(function(){
        			$(this).find("td:eq(12)").hide();
        		});
        		$("#dynamic-table th:eq(14)").hide();
        		$("#dynamic-table tr").each(function(){
        			$(this).find("td:eq(14)").hide();
        		});
        		//if(status=='4'){
        		//	$("#dynamic-table th:eq(13)").hide();
            	//	$("#dynamic-table tr").each(function(){
            	//		$(this).find("td:eq(13)").hide();
            	//	});
        		//}else{
        			$("#dynamic-table th:eq(13)").show();
        		//}
        		
        	}
        	
        	//modify by caosulin 20160630 控制资源类型列的显隐
        	if(status=='4'||status=='5'){//历史，异常，全部资源,显示资源类型
        		$("#dynamic-table th:eq(15)").show();
        		$("#dynamic-table tr").each(function(){
        			$(this).find("td:eq(15)").show();
        		});
        	}else if(status=='0'||status=='1'||status=='2'||status=='3'){//待审核，日常，询价不显示
        		$("#dynamic-table th:eq(15)").hide();
        		$("#dynamic-table tr").each(function(){
        			$(this).find("td:eq(15)").hide();
        		});
        	}
        	
            updateCountByStatus();
        }
    });

    //全选框选中、反选事件
    $("#allSelect").on("click",function(){
    	var v=$(this)[0].checked;
    	$("#dynamic-table").find('input[name="checkSource"]').each(function(i,e){
    		$(e)[0].checked=v;
    	});
    });

    //excel 导入
    $(document).on("click", "#excelBatch", function(){
    	var url=Context.PATH + "/resource/templet/index.html";
    	window.location.href=url;
    });

 	 //批量调价事件
    $("#bulkPricing").click(function(){
    	if(isSelectedRows()){
    		$("#bulkPricingShow .red")[0].innerText=_resourcePage.selectedIds.length;
    		cbms.getDialog("批量调价","#bulkPricingShow");
    	}
	});
    //批量调价dialog 确定事件
    $(document).on("click","#bulkPriceBtn",function(){
    	if (setlistensSave("#bulkPriceForm")) {
    		//提交按钮不可用，防止重复提交
    		$("#bulkPriceBtn").attr("disabled","disabled");
    		doBatchUpdate("price",11,"价格");
    		$("#allSelect").removeAttr("checked");
    	}
    	return false;
    });

    //批量改库存事件
	$("#changeCont").click(function(){
		if(isSelectedRows()){
			$("#changeContShow .red")[0].innerText=_resourcePage.selectedIds.length;
			cbms.getDialog("批量改库存","#changeContShow");
		}
	});

	 //批量改库存dialog 确定事件
    $(document).on("click","#changeContBtn",function(){
    	if (setlistensSave("#changeContForm")) {
    		//提交按钮不可用，防止重复提交
    		$("#changeContBtn").attr("disabled","disabled");
    		doBatchUpdate("weight",10,"库存");
    		$("#allSelect").removeAttr("checked");
    	}
    	return false;
    });

    //一键挂牌
    $(document).on("click", "#putUp", function(){
		if(!utils.isEmpty($("#dynamic-table").dataTable().fnGetNodes())){
	    	cbms.confirm("您确定审核全部查询到的资源吗?",null,function(){
	    		doOneKeyOprt(0,1);
	    		$("#allSelect").removeAttr("checked");
	    	});
    	}else{
			cbms.alert("暂无数据,请先添加资源!");
			return false;
		}
    });
    //选中挂牌
    $(document).on("click", "#selPutUp", function(){
    	if(isSelectedRows()){
    		//modify by caosulin@prcsteel.com 16.6.2 修改提示信息
			cbms.confirm("您确定审核选中的资源吗?",null,function(){
				var _btn = $("#selPutUp");
				changeStatus(0,1,_resourcePage.selectedIds.join(","),_btn);
				$("#allSelect").removeAttr("checked");

			});
		}
    });
    //一键撤牌
    $(document).on("click", "#putDown", function(){
    	if(!utils.isEmpty($("#dynamic-table").dataTable().fnGetNodes())){
	    	cbms.confirm("您确定发布撤牌全部查询到的资源吗?",null,function(){
	    		doOneKeyOprt(1,0);
	    		$("#allSelect").removeAttr("checked");
	    	});
    	}else{
    		cbms.alert("暂无数据,请先添加资源!");
    		return false;
    	}
    });

    //选中撤牌
    $(document).on("click", "#selPutDown", function(){
		if(isSelectedRows()){
			cbms.confirm("您确定撤牌选中的资源吗?",null,function(){
				var _btn = $("#selPutDown");
				changeStatus(1,0,_resourcePage.selectedIds.join(","),_btn);
				$("#allSelect").removeAttr("checked");
			});
		}
    });

    //选中删除
    $(document).on("click", "#selDel", function(){
    	if(isSelectedRows()){
    		cbms.confirm("您确定删除选中的资源吗?",null,function(){
    			var _btn = $("#selDel");
				delResource(_resourcePage.selectedIds.join(","),_btn);
				$("#allSelect").removeAttr("checked");
			});
    	}
    });
    
    //刷新异常
    $(document).on("click", "#refresh", function(){
		cbms.confirm("您确定要刷新异常资源吗?",null,function(){
			var status=$("#myTab4 li.active input").val();
			cbms.loading();
			$.ajax({
		        type: 'post',
		        url: Context.PATH + "/resource/refreshException.html",
		        data:{status:status},
		        success: function (result) {
		        	if(result.success){
		        		cbms.alert(result.data);
		        		_resourcePage.dt.ajax.reload();
		        		updateCountByStatus();
		        		cbms.closeLoading();
		        	}
		        }
		    });
			$("#allSelect").removeAttr("checked");
		});
    });

    //状态转换
    $(document).on("click", "#doAudit", function(){
    	var tr = $(this).closest('tr'), data = _resourcePage.dt.row(tr).data(),statusNum=data.statusNum,id=data.id;
    		if(statusNum=='declined'){
    			cbms.confirm("您确定审核选中的资源吗?",null,function(){
    				var _btn = $(".exchange").closest("a[title='审核']");
    				changeStatus(0,1,id,_btn);
    			});
        	}else if(statusNum==1){
        		cbms.confirm("您确定撤牌选中的资源吗?",null,function(){
        			var _btn = $(".exchange").closest("a[title='审核']");
        			changeStatus(1,0,id,_btn);
    			});
        	}
    	});
    
    //挂牌tab点击处理
	$(document).on("click","#myTab4 li a",function(){
		$("#myTab4 li").removeClass("active");
		$(this).closest("li").addClass("active");
		_resourcePage.oneKeyOprtObj.statusNum=$("#myTab4 li.active input").val();
		_resourcePage.dt.ajax.reload();
		changeTabClass();
    });

	//清空按钮
	$("#cleanSearch").on("click", function () {
		resetForm($("form.form-inline"));
	});

	//删除
	$(document).on("click", "#doDel", function(){
		var tr = $(this).closest('tr'), data = _resourcePage.dt.row(tr).data();
		if(data.statusNum==1){
			cbms.alert("该条资源已挂牌，请撤牌后再做删除!");
			return false;
		}
		cbms.confirm("您确定删除该条资源吗?",null,function(){
			delResource(data.id);
		});

	});

	//添加
	$(document).on("click", "#addResource", function(){
		cbms.getDialog("添加","#m-s");
		$("#sourceType").val("daily_common");
	});
	
	$(document).on("click", "#doEdit", function(){
		var tr = $(this).closest('tr'), data = _resourcePage.dt.row(tr).data();
//        if(data.statusNum==1){
//        	cbms.alert("该条资源已挂牌，请撤牌后再做修改!");
//        	return false;
//        }
		cbms.getDialog("编辑","#m-s");
		$("#resourceId").val(data.id);
		$("#account").attr("val",data.accountId);
		$("#account").val(data.accountName);
		$("#account").attr("readOnly",true);
		$("#nsortName").attr("nsortid",data.categoryUuid);
		$("#nsortName").val(data.categoryName);
		$("#nsortName").attr("spec",data.spec);
		$("#sourceType").val(data.sourceType);

		$("#material").attr("materialid",data.materialUuid);
		$("#material").val(data.materialName);

		$("#inputspec3").val(data.spec);

		$("#facotory").attr("val",data.factoryId);
		$("#facotory").val(data.factoryName);

        $("#warehouse").attr("val",data.warehouseId);
        
        if(data.warehouseName && data.warehouseName.indexOf("-")>=0){
        	 $("#warehouse").val(data.warehouseName.substr(data.warehouseName.indexOf("-")+1));
        }else{
        	$("#warehouse").val(data.warehouseName);
        }
        $("#weight").val(data.weight);
        $("#quantity").val(data.quantity);
        $("#weightConcept").val(data.weightConcept);
        $("#price").val(data.price);
        $("#remark").val(data.remark);
		loadPriceLimit(data.categoryUuid,$("#price"));
	});	
	
	jQuery("#searchList").on(ace.click_event, function() {
		cbms.loading();
		//重构查询对象
		reloadQueryObj();
        searchData(true,true);
        
    });
	
	//资源更新统计
	$(document).on("click", "#statisRes", function(){
		var url=Context.PATH + "/resource/tostatisres.html";
		window.location.href = url;
	});
	
	//导出
	$(document).on("click", "#exportRes", function(){
		cbms.confirm("确定导出资源?",null,function(){
			
			var url=Context.PATH + "/resource/exportRes.html";
			var form = $("<form>");   //定义一个form表单
	        form.attr('style','display:none');   //在form表单中添加参数
	        form.attr('target','_blank');
	        form.attr('method','post');
	        form.attr('action',url);
//	    	form.attr('accept-charset','UTF-8');
//	    	form.attr('onsubmit',"document.charset='ISO8859-1'");
		       
	        $('body').append(form);  //将表单放置在web中
	        
	        var upStart=$("#upStart").val();
		   	//写死查询起始时间，取项目开始时间:2015年11月1日
		   	if(utils.isEmpty(upStart)){
		   		upStart='2015-11-1 00:00';
		   	}
		   	//将查询参数控件提交到表单上
		   	form.append($('<input>').attr('type','hidden').attr('name','accountName').attr('value',$.trim($("#name").val()))); 
		   	form.append($('<input>').attr('type','hidden').attr('name','warehouseName').attr('value',$.trim($("#warehouseName").val()))); 
		   	form.append($('<input>').attr('type','hidden').attr('name','lastUpdatedBy').attr('value',$.trim($("#lastUpdatedBy").val()))); 
		   	form.append($('<input>').attr('type','hidden').attr('name','cagetoryName').attr('value',$.trim($("#cagetoryName").val()))); 
		   	form.append($('<input>').attr('type','hidden').attr('name','materialName').attr('value',$.trim($("#materialName").val()))); 
		   	form.append($('<input>').attr('type','hidden').attr('name','spec').attr('value',$.trim($("#spec").val()))); 
		   	form.append($('<input>').attr('type','hidden').attr('name','factoryName').attr('value',$.trim($("#factoryName").val()))); 
		   	form.append($('<input>').attr('type','hidden').attr('name','areaName').attr('value',$.trim($("#areaName").val()))); 
		   	form.append($('<input>').attr('type','hidden').attr('name','updateType').attr('value',$("#updateType").val())); 
		   	form.append($('<input>').attr('type','hidden').attr('name','upEnd').attr('value',$("#upEnd").val())); 
		   	form.append($('<input>').attr('type','hidden').attr('name','upStart').attr('value',upStart)); 
		   	form.append($('<input>').attr('type','hidden').attr('name','statusNum').attr('value',$("#myTab4 li.active input").val())); 
		    
		   
	       form.submit();   //表单提交
		});
	});
});

/***
 * 重新加载查询对象
 */
function reloadQueryObj(){
	var upStart=$("#upStart").val();
	//写死查询起始时间，取项目开始时间:2015年11月1日
	if(utils.isEmpty(upStart)){
		upStart='2015-11-1 00:00';
	}
	var pricemin = '', pricemax = '';
	if (!isNaN($("#pricemin").val())) {
		pricemin = $("#pricemin").val();
	}
	if (!isNaN($("#pricemax").val())) {
		pricemax = $("#pricemax").val();
	}
	_resourcePage.oneKeyOprtObj={
			 accountName:$.trim($("#name").val()),
		     warehouseName:$.trim($("#warehouseName").val()),
		     lastUpdatedBy:$.trim($("#lastUpdatedBy").val()),
		     categoryName:$.trim($("#cagetoryName").val()),
		     materialName:$.trim($("#materialName").val()),
		     spec:$.trim($("#spec").val()),
		     factoryName:$.trim($("#factoryName").val()),
		     area:$.trim($("#areaName").val()),
		     updateType:$("#updateType").val(),
		     upStart:upStart,
		     upEnd:$("#upEnd").val(),
		     statusNum:$("#myTab4 li.active input").val(),
		     orgId:$("#serviceArea").val(),
			 priceMin:pricemin,
			 priceMax:pricemax
		}
}
/**
 * 修改各个tab按钮样式
 */
function changeTabClass(){
	var status=$("#myTab4 li.active input").val();
	//0.待审核  1.日常资源 2.询价资源 3.历史成交4.异常资源 5.缺失资源 6.超市资源 7.基础资源
	//资源类型隐藏 输入框
	$("#lab_sourceType").addClass("none");
	$("#lab_serviceArea").removeClass("none");
//	$("#allSelect").removeAttr("checked");
	if(status==='1'){ //已挂牌tab ,日常资源
		hideAll();
		//    添加
		$("#addResource").removeClass("none");
		//  批量导入
		$("#excelBatch").removeClass("none");
		//批量调价
		$("#bulkPricing").removeClass("none");
		//批量改库存
		$("#changeCont").removeClass("none");
		//选中删除
		$("#selDel").removeClass("none");
		//资源导出
		$("#exportRes").removeClass("none");
		//资源更新统计
		$("#statisRes").removeClass("none");
		//全选
		$("#allSelect").closest("label").removeClass("none");
	}else if(status==='0'){ //未挂牌tab，待审核资源
		hideAll();
		//    添加
		$("#addResource").removeClass("none");
		//  批量导入
		$("#excelBatch").removeClass("none");
		//批量调价
		$("#bulkPricing").removeClass("none");
		//批量改库存
		$("#changeCont").removeClass("none");
		//选中挂牌
		$("#selPutUp").removeClass("none");
		//一键挂牌
		//$("#putUp").removeClass("none");
		//选中删除
		$("#selDel").removeClass("none");
		//资源更新统计
		$("#statisRes").removeClass("none");
		//全选
		$("#allSelect").closest("label").addClass("none");
	}else if(status==='2'){ //询价资源
		hideAll();
		//	    添加
		$("#addResource").removeClass("none");
		//  批量导入
		$("#excelBatch").removeClass("none");
		//批量调价
		$("#bulkPricing").removeClass("none");
		//批量改库存
		$("#changeCont").removeClass("none");
		//选中删除
		$("#selDel").removeClass("none");
		//资源导出
		$("#exportRes").removeClass("none");
		//资源更新统计
		$("#statisRes").removeClass("none");
		//全选
		$("#allSelect").closest("label").removeClass("none");
	}else if(status==='4'){
		hideAll();
		//异常 tab   
		//	    添加
		$("#addResource").removeClass("none");
		//  批量导入
		$("#excelBatch").removeClass("none");
		//选中删除
		$("#selDel").removeClass("none");
		//刷新异常
		$("#refresh").removeClass("none");
		//资源更新统计
		$("#statisRes").removeClass("none");
		//全选
		$("#allSelect").closest("label").removeClass("none");
	}else if(status==='3'){ //历史成交
		hideAll();
		//资源更新统计
		$("#statisRes").removeClass("none");
	}else if(status==='6'){ //缺失资源
		window.location.href=Context.PATH + "/resource/lost/index.html?tabIndex=6";
	}else if(status==='8'){//超市资源
		hideAll();
		$("#update_type_panel").addClass("none");
		$("#seller_panel").addClass("none");
		//$("#warehoure_panel").addClass("none");
	}else if(status==='9') { //基础资源
		window.location.href = Context.PATH + "/resource/base/index.html?tabIndex=9";
	}
	else{
		hideAll();
		//    添加
		$("#addResource").removeClass("none");
		//  批量导入
		$("#excelBatch").removeClass("none");
		//批量调价
		$("#bulkPricing").removeClass("none");
		//批量改库存
		$("#changeCont").removeClass("none");
		//选中删除
		$("#selDel").removeClass("none");
		//资源导出
		$("#exportRes").removeClass("none");
		$("#lab_sourceType").removeClass("none");
		$("#lab_serviceArea").addClass("none");
		//资源更新统计
		$("#statisRes").removeClass("none");
		//全选
		$("#allSelect").closest("label").removeClass("none");
	}
}


//隐藏所有按钮
function hideAll(){
	//    添加
	$("#addResource").addClass("none");
	//  批量导入
	$("#excelBatch").addClass("none");
	//批量调价
	$("#bulkPricing").addClass("none");
	//批量改库存
	$("#changeCont").addClass("none");
	//选中挂牌
	$("#selPutUp").addClass("none");
	//一键挂牌
	$("#putUp").addClass("none");
	//选中撤牌
	$("#selPutDown").addClass("none");
	//一键撤牌
	$("#putDown").addClass("none");
	//选中删除
	$("#selDel").addClass("none");
	//刷新异常
	$("#refresh").addClass("none");
	//资源导出
	$("#exportRes").addClass("none");
	//资源更新统计
	$("#statisRes").addClass("none");
	//全选
	$("#allSelect").closest("label").addClass("none");
}




/**
 * 一健操作(挂牌或撤牌)
 * @param oriStatus 原始状态
 * @param toStatus  目的状态
 */
function doOneKeyOprt(oriStatus,toStatus){
	_resourcePage.oneKeyOprtObj.oriStatus=oriStatus;
	_resourcePage.oneKeyOprtObj.toStatus=toStatus;
	$.ajax({
        type: 'post',
        url: Context.PATH + "/resource/oneKeyOprt.html",
        data : _resourcePage.oneKeyOprtObj,
        success: function (result) {
        	if(result.success){
        		cbms.alert(result.data);
        		_resourcePage.dt.ajax.reload();
        		updateCountByStatus();
        	}
        }
    });
}
/**
 * 异常数据加红渲染
 * @param t  内容
 * @param id 校验id
 * @returns {String}
 */
function exceptionRender(t,id){
	var res="";
	if(utils.isEmpty(id)||id<0){
		res="<span class='green'>"+t+"</span>";
	}else{
		res=t;
	}
	return res;
}
/**
 * 批量更新操作
 * @param column    资源表对应列
 * @param num       资源列表应列数
 * @param type      类型
 */
function doBatchUpdate(column,num,type){
	//选中id集
	var ids=_resourcePage.selectedIds.join(",");
	//调整方式
	var adjustWay=$("#dialogContBox select[name='adjustWay']").val();
    //调整值
	var adjustValue=$("#dialogContBox input[name='adjustValue']").val();
	var msg=checkAdjustValue(adjustWay,adjustValue,num,type);
	if(msg.length>0){
		cbms.alert(msg.join(""));
		return false;
	}
	var url = Context.PATH + "/resource/batchUpdate.html";
	$.ajax({
        type: 'post',
        url: url,
        data:{"adjustWay":adjustWay,"adjustValue":adjustValue,"column":column,"ids":ids},
        dataType:"json",
        success: function(data){
			if(data.success){
				_resourcePage.dt.ajax.reload();
			}
			cbms.alert(data.data);
			cbms.closeDialog();
        }
    });
}
/**
 * 检查调整值是否越界
 * @param adjustWay     调整方式
 * @param adjustValue   调整值
 * @param type          检查类型
 * @returns {Array}     错误信息
 */
function checkAdjustValue(adjustWay,adjustValue,column,type){
	var errorMsg=new Array();
	if("上调指定值"!=adjustWay){
		if("设为指定值"==adjustWay){
			if(adjustValue<1){
				errorMsg.push("指定值不能小于1;");
			}
			return  errorMsg
		}
		///下调指定值
		var rows=$("#dynamic-table").dataTable().fnGetNodes();
		$(rows).each(function(i,e){
			var el=$(e).find("input[type='checkbox']");
			if(el[0].checked){
				var v=$(e).find("td").eq(column)[0].innerText;
				if(!utils.isEmpty(v)){
					var r=v-adjustValue;
					if(r<1){
						errorMsg.push("<p>卖家: "+$(e).find("td").eq(1)[0].innerText+" 品名:"+$(e).find("td").eq(2)[0].innerText+" 材质: "+$(e).find("td").eq(3)[0].innerText+" 规格: "+$(e).find("td").eq(4)[0].innerText+" 的"+type+" 调整后小于1;</p>");
					}
				}
			}
		});
	}
	return errorMsg;
}

/**
 * 获取datatables指定列的数据数组
 * @param tableObj          datatables 对象，例：$("#dynamic-table")
 * @param columnName        datatables 列名，例: "id"
 * @returns   指定列数据数组，例:id列数据集 [1,2,3]
 */
function getDataTableColumn(tableObj,columnName) {
	 return $.map(tableObj.dataTable().fnGetData(), function(val) {
		 return val[columnName];
	 });
}

/**
 * 检查数据行是否选中，并把资源id存入变量
 */
function isSelectedRows(){
	//清空数组
	_resourcePage.selectedIds.length=0;
	var rows=$("#dynamic-table").dataTable().fnGetNodes();
	if(utils.isEmpty(rows)){
		cbms.alert("暂无数据,请先添加资源!");
		return false;
	}else{
		for(var k=0;k<rows.length;k++){
			var inp=$(rows[k]).find("input[name='checkSource']");
			if (inp.length > 0 && inp[0].checked) {
				_resourcePage.selectedIds.push(Number($(inp).attr("val")));
			}
		}
		if(utils.isEmpty(_resourcePage.selectedIds)){
			cbms.alert("请先选中记录!");
			return false;
		}
	}
	return true;
}
/**
 * 操作列图片组装
 * @param id
 * @returns {String}
 */
function generateOptHtml(aData) {
	
	var status = aData.statusNum;
	var sourceType= aData.sourceType;
	var isExcepton = aData.isException;
	var optHtml = '<div class="fa-hover">';
	var toStatus="";
	if(status=='declined'&&sourceType=='daily_common'&&isExcepton=='normal'){//待审核
		toStatus="审核";
		optHtml += "<a id='doAudit' href='javascript:void(0);' target='_blank' title='"+toStatus+"'>";
		optHtml += toStatus+"</a>&nbsp;&nbsp;";
		optHtml += "<a id='doEdit' href='javascript:void(0);' title='编辑'>";
		optHtml += "编辑</a>";
		optHtml += " <a id='doDel' href='javascript:void(0);' title='删除'>删除</a>";
	}else if(status=='approved'&&sourceType=='daily_common'&&isExcepton=='normal'){//日常资源
		optHtml += "<a id='doEdit' href='javascript:void(0);' title='编辑'>";
		optHtml += "编辑</a>";
		optHtml += " <a id='doDel' href='javascript:void(0);' title='删除'>删除</a>";
	}else if(sourceType=='inquiry'&&isExcepton=='normal'){//询价资源
		optHtml += "<a id='doEdit' href='javascript:void(0);' title='编辑'>";
		optHtml += "编辑</a>";
		optHtml += " <a id='doDel' href='javascript:void(0);' title='删除'>删除</a>";
	}else if(sourceType=='history_transaction'){//历史资源
		optHtml += "<a id='doEdit' href='javascript:void(0);' title='编辑'>";
		optHtml += "编辑</a>";
		/*modify by zhoucai@prcstell.com 历史成交资源异常数据可删除*/
		if(isExcepton=='exception'){//异常数据
			optHtml += " <a id='doDel' href='javascript:void(0);' title='删除'>删除</a>";
		}
	}else if(isExcepton=='exception'){//异常数据
		optHtml += "<a id='doEdit' href='javascript:void(0);' title='编辑'>";
		optHtml += "编辑</a>";
		optHtml += " <a id='doDel' href='javascript:void(0);' title='删除'>删除</a>";
	}else if(status==2){
		optHtml += "<a id='doEdit' href='javascript:void(0);' title='编辑'>";
		optHtml += "编辑</a>";
	}
	else{
		/*toStatus="撤牌";
		 optHtml += "<a href='javascript:void(0);' title='"+toStatus+"'>";
		 optHtml += "<i class=\"fa fa-cloud-download exchange fa-2x\"></i></a>&nbsp;&nbsp;";
		 optHtml += "<a href='javascript:void(0);' title='编辑'>";
	     optHtml += "<i class=\"fa fa-edit fa-2x\"></i></a>";*/
	}
    optHtml += '</div>';
    return optHtml;
}

/**
 * 改变资源状态
 * @param oriStatus 原始状态
 * @param toStatus  目的状态
 * @param ids    修改的资源id
 */
function changeStatus(oriStatus,toStatus,ids,cur_input){
	//提交按钮不可用，防止重复提交
	if(typeof(cur_input) != "undefined"){
		cur_input.attr("disabled","disabled");
	}
	$.ajax({
        type: 'post',
        url: Context.PATH + "/resource/changeStatus.html",
        data : {"oriStatus":oriStatus,"toStatus":toStatus,"ids":ids},
        success: function (result) {
        	if(result.success){
        		cbms.alert(result.data);
        		searchData();
        		updateCountByStatus();
        	}

        	//提交按钮重新可用
        	if(typeof(cur_input) != "undefined"){
        		cur_input.removeAttr("disabled");
        	}
        }
    });
}
/**
 * 删除资源
 * @param ids  资源id集
 */
function delResource(ids,cur_input){
	//提交按钮不可用，防止重复提交
	if(typeof(cur_input) != "undefined"){
		cur_input.attr("disabled","disabled");
	}
	
	if(!utils.isEmpty(ids)){
		$.ajax({
	        type: 'post',
	        url: Context.PATH + "/resource/delResource.html",
	        data : {"ids":ids},
	        success: function (result) {
	        	if(result.success){
	        		cbms.alert(result.data);
	        		_resourcePage.dt.ajax.reload();
	        		updateCountByStatus();
	        	}
	        	
	        	//提交按钮重新可用
	        	if(typeof(cur_input) != "undefined"){
	        		cur_input.removeAttr("disabled");
	        	}
	        }
	    });
	}
}
/**
 * 更新tab数量值
 * 资源审核总数量，需要修改
 */
function updateCountByStatus(){
	$.ajax({
        type: 'post',
        url: Context.PATH + "/resource/getCountByStatus.html",
        success: function (result) {
        	if(result.success){
        		var allCount=result.data.allCount|0;
        		var listedCount=result.data.listedCount|0;
        		var unListedCount=result.data.unListedCount|0;
        		var exceptionCount=result.data.exceptionCount|0;
        		var historyTransactionCount=result.data.historyTransactionCount|0;
        		var inqueryCount = result.data.inqueryCount|0;
        		$("#myTab4 li span")[5].innerText="（"+allCount+"）";//全部资源
        		$("#myTab4 li span")[1].innerText="（"+listedCount+"）";//日常资源
        		$("#myTab4 li span")[0].innerText="（"+unListedCount+"）";//未审核资源
        		$("#myTab4 li span")[3].innerText="（"+historyTransactionCount+"）";//历史成交
        		$("#myTab4 li span")[4].innerText="（"+exceptionCount+"）";//异常资源
        		$("#myTab4 li span")[2].innerText="（"+inqueryCount+"）";//询价资源
        	}
        }
    });
}
function searchData(isNewSearch,isAddFlower) {
	if(isNewSearch){
		_resourcePage.dt.ajax.reload(function(){
			closeLoadding(isAddFlower);
		});
	}else{
		_resourcePage.dt.ajax.reload(function(){
			closeLoadding(isAddFlower);
		},false);
	}
}

function closeLoadding(isAddFlower){
	if(isAddFlower){
		cbms.closeLoading();
	}
}

function dateFormat(date, fmt) {
	var o = {
		"M+": date.getMonth() + 1,                 //月份
		"d+": date.getDate(),                    //日
		"h+": date.getHours(),                   //小时
		"m+": date.getMinutes(),                 //分
		"s+": date.getSeconds(),                 //秒
		"q+": Math.floor((date.getMonth() + 3) / 3), //季度
		"S": date.getMilliseconds()             //毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
	for (var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}

function loadPriceLimit(uuid,input){
	for(var i=0;i<limitPrices.length;i++){
		if(limitPrices[i].uuid==uuid){
			input.attr("min", limitPrices[i].priceMin).attr("max", limitPrices[i].priceMax);
			return;
		}
	}
	$.ajax({
		type: 'post',
		url: Context.PATH + "/resource/getpricelimit.html",
		data: {uuid: uuid},
		success: function (result) {
			if (result.success) {
				limitPrices.push(result.data);
				input.attr("min", result.data.priceMin).attr("max", result.data.priceMax);
			}
		}
	});
}
