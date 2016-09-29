//全局变量
var _resourcePage = {
	selectedIds:[], //数据行选中id记录集
    dt:null,        //datatables
    oneKeyOprtObj:null //一键操作查询对象
}

$(document).ready(function() {
	
	
	if(!utils.isEmpty(tabIndex)){
		var index = parseInt(tabIndex);
		$("#myTab4 li").removeClass("active");
		$("#myTab4").find("li:eq("+index+")").addClass("active");
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
            $('td:last', nRow).html(generateOptHtml(aData.statusNum));
        },
        columns : [
            {data:"id","mRender":function(e,t,f){
        		return "<label class='pos-rel'><input type='checkbox' name='checkSource' val='"+e+"'class='ace'><span class='lbl'></span></label>";
            }},
            {data : 'accountName'},
            {data : 'categoryName'},
            {data : 'materialName',"mRender":function(e,t,f){return exceptionRender(e,f.materialUuid);}},
            {data : 'spec',"mRender":function(e,t,f){return exceptionRender(e,f.spec);}},
            {data : 'factoryName',"mRender":function(e,t,f){return exceptionRender(e,f.factoryId);}},
            {data : 'warehouseName',"mRender":function(e,t,f){return exceptionRender(e,f.warehouseId);}},
            {data : 'weightConcept'},
            {data : 'quantity'},
            {data : 'weight', "sClass": "text-right"},
            {data : 'price', "sClass": "text-right","mRender":function(e,t,f){if(e==99999){return "电话议价"}else{return e}}},
            {data : 'status'},
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

		                }
		         ] ,

        "drawCallback": function(e,t) {
        	var status=$("#myTab4 li.active input").val();
        	if(status==2){   //历史成交tab隐藏多选
        		$("#dynamic-table th:eq(0)").hide();
        		$("#dynamic-table tr").each(function(){
        			$(this).find("td:first").hide();
        		})
        	}else{
        		$("#dynamic-table th:eq(0)").show();
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
    		doBatchUpdate("price",10,"价格");
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
    		doBatchUpdate("weight",9,"库存");
    		$("#allSelect").removeAttr("checked");
    	}
    	return false;
    });

    //一键挂牌
    $(document).on("click", "#putUp", function(){
		if(!utils.isEmpty($("#dynamic-table").dataTable().fnGetNodes())){
	    	cbms.confirm("您确定发布挂牌全部查询到的资源吗?",null,function(){
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
			cbms.confirm("您确定挂牌选中的资源吗?",null,function(){
				changeStatus(0,1,_resourcePage.selectedIds.join(","));
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
				changeStatus(1,0,_resourcePage.selectedIds.join(","));
				$("#allSelect").removeAttr("checked");
			});
		}
    });

    //选中删除
    $(document).on("click", "#selDel", function(){
    	if(isSelectedRows()){
    		cbms.confirm("您确定删除选中的资源吗?",null,function(){
				delResource(_resourcePage.selectedIds.join(","));
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
    $(document).on("click", ".exchange", function(){
    	var tr = $(this).closest('tr'), data = _resourcePage.dt.row(tr).data(),statusNum=data.statusNum,id=data.id;
    		if(statusNum==0){
    			cbms.confirm("您确定挂牌选中的资源吗?",null,function(){
    				changeStatus(0,1,id);
    			});
        	}else if(statusNum==1){
        		cbms.confirm("您确定撤牌选中的资源吗?",null,function(){
        			changeStatus(1,0,id);
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
	$(document).on("click", ".fa-trash-o", function(){
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
	});
	//编辑
	$(document).on("click", ".fa-edit", function(){
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
	});
	
	
	jQuery("#searchList").on(ace.click_event, function() {
		cbms.loading();
		//重构查询对象
		reloadQueryObj();
        searchData(true,true);
        
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
		     statusNum:$("#myTab4 li.active input").val()
		}
}
/**
 * 修改各个tab按钮样式
 */
function changeTabClass(){
	var status=$("#myTab4 li.active input").val();

//	$("#allSelect").removeAttr("checked");
	if(status==='1'){ //已挂牌tab
		$("#selPutDown").removeClass("none");
		$("#putDown").removeClass("none");
		$("#bulkPricing").removeClass("none");
		$("#changeCont").removeClass("none");
		$("#allSelect").closest("label").removeClass("none");

		$("#selPutUp").addClass("none");
		$("#putUp").addClass("none");
		$("#selDel").addClass("none");
		$("#exportRes").addClass("none");
//		$("#bulkPricing").addClass("none");
//		$("#changeCont").addClass("none");
		$("#refresh").addClass("none");
	}else if(status==='0'){ //未挂牌tab
		$("#selPutUp").removeClass("none");
		$("#selDel").removeClass("none");
		$("#putUp").removeClass("none");
		$("#bulkPricing").removeClass("none");
		$("#changeCont").removeClass("none");
		$("#allSelect").closest("label").removeClass("none");

		$("#selPutDown").addClass("none");
		$("#putDown").addClass("none");
		$("#refresh").addClass("none");
		$("#exportRes").addClass("none");
	}else if(status==='-1'){
		//导常 tab
		$("#selDel").removeClass("none");
		$("#refresh").removeClass("none");
		$("#allSelect").closest("label").removeClass("none");

		$("#selPutDown").addClass("none");
		$("#putDown").addClass("none");
		$("#selPutUp").addClass("none");
		$("#putUp").addClass("none");
		$("#bulkPricing").addClass("none");
		$("#changeCont").addClass("none");
		$("#exportRes").addClass("none");
	}else if(status==='2'){ //历史成交
		$("#refresh").removeClass("none");
		
		$("#selPutDown").addClass("none");
		$("#putDown").addClass("none");
		$("#selPutUp").addClass("none");
		$("#putUp").addClass("none");
		$("#selDel").addClass("none");
		$("#bulkPricing").addClass("none");
		$("#changeCont").addClass("none");
		$("#allSelect").closest("label").addClass("none");
		$("#exportRes").addClass("none");
	}else if(status==='-2'){ //缺失资源
		window.location.href=Context.PATH + "/resource/lost/index.html?tabIndex=5";
	}
	else{
		//全部
		$("#selPutDown").addClass("none");
		$("#putDown").addClass("none");
		$("#selPutUp").addClass("none");
		$("#putUp").addClass("none");
		$("#selDel").addClass("none");
//		$("#bulkPricing").addClass("none");
//		$("#changeCont").addClass("none");
		$("#refresh").addClass("none");
//		$("#allSelect").closest("label").addClass("none");
		$("#exportRes").removeClass("none");
	}
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
	if(utils.isEmpty(id)){
		res="<span class='red'>"+t+"</span>";
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
			if(inp[0].checked){
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
function generateOptHtml(status) {
	var optHtml = '<div class="fa-hover">';
	var toStatus="";
	if(status==0){
		toStatus="挂牌";
		optHtml += "<a href='javascript:void(0);' target='_blank' title='"+toStatus+"'>";
		optHtml += "<i class=\"fa fa-cloud-upload exchange fa-2x\"></i></a>&nbsp;&nbsp;";
		optHtml += "<a href='javascript:void(0);' title='编辑'>";
	    optHtml += "<i class=\"fa fa-edit fa-2x\"></i></a>";
	   	optHtml += " <a href='javascript:void(0);' title='删除'><i class=\"fa fa-trash-o fa-fw fa-2x\"></i></a>";
	}else if(status==-1){
		optHtml += "<a href='javascript:void(0);' title='编辑'>";
	    optHtml += "<i class=\"fa fa-edit fa-2x\"></i></a>";
	   	optHtml += " <a href='javascript:void(0);' title='删除'><i class=\"fa fa-trash-o fa-fw fa-2x\"></i></a>";
	}else if(status==2){
		optHtml += "<a href='javascript:void(0);' title='编辑'>";
		  optHtml += "<i class=\"fa fa-edit fa-2x\"></i></a>";
	}
	else{
		toStatus="撤牌";
		 optHtml += "<a href='javascript:void(0);' title='"+toStatus+"'>";
		 optHtml += "<i class=\"fa fa-cloud-download exchange fa-2x\"></i></a>&nbsp;&nbsp;";
		 optHtml += "<a href='javascript:void(0);' title='编辑'>";
	     optHtml += "<i class=\"fa fa-edit fa-2x\"></i></a>";
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
function changeStatus(oriStatus,toStatus,ids){
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
        }
    });
}
/**
 * 删除资源
 * @param ids  资源id集
 */
function delResource(ids){
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
	        }
	    });
	}
}
/**
 * 更新tab数量值
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
        		$("#myTab4 li span")[0].innerText="（"+allCount+"）";
        		$("#myTab4 li span")[1].innerText="（"+listedCount+"）";
        		$("#myTab4 li span")[2].innerText="（"+unListedCount+"）";
        		$("#myTab4 li span")[3].innerText="（"+historyTransactionCount+"）";
        		$("#myTab4 li span")[4].innerText="（"+exceptionCount+"）";
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