//交易凭证
//code by tuxianming
//at 2016/04/08

var _tableHeight = "";
var _check_table, _uncheck_table;
var _checkType=$("#isAudit").val();
var url = Context.PATH + "/order/certificate/loadchecklist.html";
jQuery(function ($) {
	initEvent();
	_tableHeight = ($(window).height()-(300)<300?300:$(window).height()-(300))+"px";
	if(_checkType  == 'check'){
		initCheckTable();
		$("#uncheck-container").hide();
		$("#check-container").show();
	}else{
		initUncheckTable();
		$("#uncheck-container").show();
		$("#check-container").hide();
	}
});

//卖家凭证列表
function initCheckTable() {
	_check_table = $("#check-trade-credential").DataTable({
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "lengthChange": true, //显示pageSize的下拉框 50 100 150
        "ajax": {
            "url": url,
            "type": "POST",
            data: function (d) {
            	d.check = true;
            	buildParam(d);
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns:[
           {data: 'credentialCode'},   	//凭证号
           {data: 'submitDateStr'},  	//凭证提交时间
           {data: 'type'},   			//凭证类型
           {data: 'name'},   			//凭证名称 
           {data: 'sellerName'},  		//卖家客户
           {data: 'buyerName'},  		//买家客户
           {data: 'code'},			//订单号
           {data: 'durationDay'},		//距提交时间
           {data: 'status'},			//状态
           {defaultContent: ''}			//操作
       ],
       columnDefs:[
           {
          	    "targets": 2, //第几列 从0开始
          	    "data": "type",
          	    "render": function(data, type, full, meta){
          	    	if(data=='seller'){
          	    		return "卖家凭证";
          	    	}else{
          	    		return "买家凭证";
          	    	}
          	    }
          	},
          	{
          	    "targets": 8, //第几列 从0开始
          	    "data": "status",
          	    "render": function(data, type, full, meta){
          	    	if(data=='APPROVED'){
          	    		return "审核已通过";
          	    	}else{
          	    		return "待审核";
          	    	}
          	    }
          	}
       ]
        ,"fnRowCallback": function (nRow, aData, iDataIndex) {
        	buildControl(nRow, aData, iDataIndex);
        }
        ,"scrollY": _tableHeight
        //,"scrollX": true
    });
}

//买家凭证列表
function initUncheckTable() {
	_uncheck_table = $("#uncheck-trade-credential").DataTable({
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "lengthChange": true, //显示pageSize的下拉框 50 100 150
        iDisplayLength: 50,
        "ajax": {
            "url": url,
            "type": "POST",
            data: function (d) {
            	d.check = false;
            	buildParam(d);
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns:[
           {data: 'credentialCode'},   	//凭证号 
           {data: 'name'},  			//类型
           {data: 'submitDateStr'}, 	//凭证提交时间
           {data: 'type'},   			//凭证类型
           {data: 'sellerName'},  		//订单号
           {data: 'buyerName'},  		//卖家客户
           {data: 'code'},			//买家客户
           {data: 'status'},			//订单号
           {defaultContent: ''}			//操作
       ],
       columnDefs:[
        	{
           	    "targets": 3, //第几列 从0开始
           	    "data": "type",
           	    "render": function(data, type, full, meta){
           	    	if(data=='seller'){
           	    		return "卖家凭证";
           	    	}else{
           	    		return "买家凭证";
           	    	}
           	    }
           	},
           	{
           	    "targets": 7, //第几列 从0开始
           	    "data": "status",
           	    "render": function(data, type, full, meta){
           	    	if(data=='APPROVED'){
           	    		return "审核已通过";
           	    	}else{
           	    		return "待审核";
           	    	}
           	    }
           	}
        ]
        ,"fnRowCallback": function (nRow, aData, iDataIndex) {
        	//aData.id
        	buildControl(nRow, aData, iDataIndex);
            
        }
        ,"scrollY": _tableHeight
       // ,"scrollX": true
    });
}

function buildControl(nRow, aData, iDataIndex){
	var auditUrl;
	 	// uncheck  来自不需审核风控列表，check 来自需要审核风控列表
	var isAudit;
	if(aData.settingValue == '1'){
		isAudit = 'check';
	}else{
		isAudit = 'uncheck';
	}
	if(aData.orderId != '0' && aData.name.indexOf('批') == -1){
		if(aData.type == 'seller')
			auditUrl = Context.PATH +"/order/certificate/"+ aData.orderId+"/"+aData.sellerId+"/"+aData.type+"/"+ isAudit +"/detail.html";
		else
			auditUrl = Context.PATH +"/order/certificate/"+ aData.orderId+"/"+aData.buyerId+"/"+aData.type+"/"+ isAudit +"/detail.html";
	}else{
		if(aData.type == 'seller')
			auditUrl = Context.PATH +"/order/certificate/"+ aData.id+"/"+aData.type+"/"+aData.sellerId+"/"+ isAudit +"/detailbatch.html";
		else
			auditUrl = Context.PATH +"/order/certificate/"+ aData.id+"/"+aData.type+"/"+aData.buyerId+"/"+ isAudit +"/detailbatch.html";
	}
		
	var text = "审核";
	if(aData.status=='APPROVED'){
		text = "查看详情";
	}
		
	var controller = "<a href='"+auditUrl+"'>"+text+"</a>";
    $('td:eq(-1)', nRow).html(controller);
	
}

function buildParam(d){
	var credentialCode = $("#credentialCode").val();
	if(credentialCode) d.credentialCode = credentialCode;
	
	var orgIds = [];
	$("#orgSelect li").each(function(){
		var li = $(this);
		var checkbox = li.find("input[type='checkbox']");
		if(checkbox.prop('checked')){
			var orgId = checkbox.val();
			if(orgId!='all'){
				orgIds.push(orgId);
			}
		}
	});

	/*if(orgIds.length==0){ //如果没有选中，则默认查询所有
		$("#orgSelect li").each(function(){
			var li = $(this);
			var checkbox = li.find("input[type='checkbox']");
			var orgId = checkbox.val();
			if(orgId!='all'){
				orgIds.push(orgId);
			}
		});
		
	}*/
	if(orgIds.length!=0){
		d.orgIds = orgIds.toString();
	}
	
	var code = $("#code").val();
	if(code)
		d.code = code;
	
	var status = $("#status").val();
	if(status) d.status = status;
	
	var startTime = $("#startTime").val();
	if(startTime)
		d.startTime = startTime;
	
	var endTime = $("#endTime").val();
	if(endTime)
		d.endTime = endTime;
	
	var buyerId = $("#buyerName").attr("accountid");
	if(buyerId) d.buyerId = buyerId;
	
	var sellerId = $("#sellerName").attr("accountid");
	if(sellerId) d.sellerId = sellerId;
	
}

function initEvent(){
	
	//点击搜索时，根据条件重新加载报表 
	$("#queryBtn").click(function(){
		reload(true);
	});
	
	//改变tab
	$(".cer-tab").click(function(){
		var tab = $(this);
		tab.siblings().removeClass("active");
		tab.addClass("active");
		_checkType = tab.attr("type");
		
		if(_checkType=='check'){
			$("#uncheck-container").hide();
			$("#check-container").show();
		}
		else{
			$("#uncheck-container").show();
			$("#check-container").hide();
		}
		reload(false);
	});
	
	/**
	 * 点击：选择报表中心，服务中心选择框
	 */
	$("#orgSelectBtn").click(showSelectOptionsBox);
	
	/**
	 * 选中：所有服务中心时，取消其它服务中心选项框, 选中其它服务中心时，取消：所有服务中心
	 */
	clickSelectAll();
}

function reload(refresh){
	
	if(_checkType=='check'){
		if(!_check_table)
			initCheckTable();
		else{
			if(refresh)
				_check_table.ajax.reload();
		}
	}else{
		if(!_uncheck_table)
			initUncheckTable()
		else
			if(refresh)
				_uncheck_table.ajax.reload();
	}
}

/**
 * 点击：选择报表中心，服务中心选择框
 */
function showSelectOptionsBox(){
	var optionbox = $("#orgSelect");
	if(optionbox.css("display") == "none"){
		optionbox.show();
		$(document).on("mouseleave","#org_options", function(){
			optionbox.hide();
		});
	}else{
		optionbox.hide();
	}
}

/**
 * 选中：所有服务中心时，取消其它服务中心选项框, 选中其它服务中心时，取消：所有服务中心
 */
function clickSelectAll(){
	
	$("#selectAllOrg").click(function(){
		var checked=$(this).prop('checked');
		if(checked){
			$("#orgSelect li input[type='checkbox']").removeAttr("checked");
			$(this).prop("checked", "checked");
		}
	})
	
	$("#orgSelect li input[type='checkbox']").not("#selectAllOrg").click(function(){
		var selectAll = $("#selectAllOrg");
		if(selectAll.prop("checked")){
			//$(this).removeAttr("checked");
			selectAll.removeAttr("checked");
		}
	});
	
}

