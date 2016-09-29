//交易凭证
//code by tuxianming

var _tableHeight = "";
var _buyer_table, _seller_table;
var _credentialType=$("#accountType").val();
var _batch_url = Context.PATH + "/order/query/tradecertificateforbatch.html";
jQuery(function ($) {
	initEvent();
	_tableHeight = ($(window).height()-(300)<300?300:$(window).height()-(300))+"px";
	if(_credentialType == 'seller'){
		$("#buyer-container").hide();
		$("#seller-container").show();
		initSellerTable();
	}
	else{
		$("#buyer-container").show();
		$("#seller-container").hide();
		initBuyerTable();
	}
		
});

//卖家凭证列表
function initSellerTable() {
	_seller_table = $("#seller-trade-credential").DataTable({
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "lengthChange": true, //显示pageSize的下拉框 50 100 150
        iDisplayLength: 50,
        "ajax": {
            "url": Context.PATH + "/order/query/tradecertificatelist.html",
            "type": "POST",
            data: function (d) {
            	var sellerId = $("#sellerName").attr("accountid");
            	if(sellerId) d.sellerId = sellerId;
            	buildParam(d);
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns:[
//                   {defaultContent: ''},
                   {data: 'code'},   				//交易单号 
                   {data: 'openOrderDate'},  		//开单时间
                   {data: 'buyerName'},   			//买家全称
                   {data: 'ownerName'},   			//交易员
                   {data: 'sellerName'},  			//卖家全称
                   {data: 'quantity'},  			//件数
                   {data: 'totalWeight'},			//总重量
                   {data: 'actualPickTotalWeight'},	//实提总重量
                   {data: 'totalAmount'},			//总金额
                   {data: 'actualPickTotalAmount'},	//实提总重量
                   {data: 'printBuyerTradeCertificateStatus'},			//打印状态 
                   {data: 'durationDay'},			//距离开单时间已有
                   {data: 'settingValue'},			//类型 
                   {defaultContent: ''}				//操作
               ]
        , columnDefs:[
         	{
        	    "targets": 6, //第几列 从0开始
        	    "data": "totalWeight",
        	    "render": renderWeight
        	},
        	{
        	    "targets": 7, //第几列 从0开始
        	    "data": "actualPickTotalWeight",
        	    "render": renderWeight
        	},
        	{
        	    "targets": 8, //第几列 从0开始
        	    "data": "totalAmount",
        	    "render": renderAmount
        	},
        	{
        	    "targets": 9, //第几列 从0开始
        	    "data": "actualPickTotalAmount",
        	    "render": renderAmount
        	},
        	{
        	    "targets": 12, //第几列 从0开始
        	    "data": "settingValue",
        	    "render": renderType
        	}
        ]
        ,"fnRowCallback": function (nRow, aData, iDataIndex) {
            var controller = "<a href='"+Context.PATH+"/order/query/tradecertificatedetail.html?orderIds="+aData.orderId+"&type="+_credentialType+"&sellerId="+aData.sellerId+"'>查看详情</a>";
            $('td:eq(-1)', nRow).html(controller);
        }
        ,"scrollY": _tableHeight
        ,"scrollX": true
    });
}

//买家凭证列表
function initBuyerTable() {
	_buyer_table = $("#buyer-trade-credential").DataTable({
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "lengthChange": true, //显示pageSize的下拉框 50 100 150
        iDisplayLength: 50,
        "ajax": {
            "url": Context.PATH + "/order/query/tradecertificatelist.html",
            "type": "POST",
            data: function (d) {
            	var buyerId = $("#buyerName").attr("accountid");
            	if(buyerId) d.buyerId = buyerId;
            	_credentialType ='buyer';
            	buildParam(d);
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns:[
//                   {defaultContent: ''},
                   {data: 'code'},   				//交易单号 
                   {data: 'openOrderDate'},  		//开单时间
                   {data: 'buyerName'},   			//买家全称
                   {data: 'ownerName'},   			//交易员
                   {data: 'sellerName'},  			//卖家全称
                   {data: 'quantity'},  			//件数
                   {data: 'totalWeight'},			//总重量
                   {data: 'actualPickTotalWeight'},	//实提总重量
                   {data: 'totalAmount'},			//总金额
                   {data: 'actualPickTotalAmount'},	//实提总重量
                   {data: 'printBuyerTradeCertificateStatus'},			//打印状态 
                   {data: 'durationDay'},			//距离开单时间已有
                   {data: 'settingValue'},			//类型 
                   {defaultContent: ''}				//操作
               ]
        , columnDefs:[
         	{
        	    "targets": 6, //第几列 从0开始
        	    "data": "totalWeight",
        	    "render": renderWeight
        	},
        	{
        	    "targets": 7, //第几列 从0开始
        	    "data": "actualPickTotalWeight",
        	    "render": renderWeight
        	},
        	{
        	    "targets": 8, //第几列 从0开始
        	    "data": "totalAmount",
        	    "render": renderAmount
        	},
        	{
        	    "targets": 9, //第几列 从0开始
        	    "data": "actualPickTotalAmount",
        	    "render": renderAmount
        	},
        	{
        	    "targets": 12, //第几列 从0开始
        	    "data": "settingValue",
        	    "render": renderType
        	}
        ]
        ,"fnRowCallback": function (nRow, aData, iDataIndex) {
            var controller = "<a href='"+Context.PATH+"/order/query/tradecertificatedetail.html?orderIds="+aData.orderId+"&type="+_credentialType+"'>查看详情</a>";
            $('td:eq(-1)', nRow).html(controller);
        }
        ,"scrollY": _tableHeight
        ,"scrollX": true
    });
}
function renderType(data, type, full, meta) {
    if (data) {
        return "必须审核通过才能开票";
    }else{
    	return "不须审核通过也能开票";
    }
}
function buildParam(d){
	var code = $("#code").val();
	if(code) d.code = code;
	
	var blacklist = $("#blacklist").val();
	if(blacklist)
		d.blacklist = blacklist;
	
	var ownerName = $("#ownerName").val();
	if(ownerName) d.ownerName = ownerName;
	
	var startTime = $("#startTime").val();
	if(startTime)
		d.startTime = startTime;
	
	var endTime = $("#endTime").val();
	if(endTime)
		d.endTime = endTime;
	
	d.credentialType = _credentialType;
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
		_credentialType = tab.attr("credentialType");
		
		if(_credentialType=='seller'){
			$(".crumb-nav").text("打印卖家凭证");
			$("#buyerName").closest("label").hide();
			$("#sellerName").closest("label").show();
			$("#buyer-container").hide();
			$("#seller-container").show();
		}
		else{
			$(".crumb-nav").text("打印买家凭证");
			$("#buyerName").closest("label").show();
			$("#sellerName").closest("label").hide();
			$("#buyer-container").show();
			$("#seller-container").hide();
		}
		
		$("#batchPrintBtn").attr("href", _batch_url+"?type="+_credentialType);
		
		reload(false);
	});

	//点击导出时，根据条件导出数据到Excel
	$("#export").click(function () {
		exportExcel();
	});
	
}

function reload(refresh){
	
	if(_credentialType=='seller'){
		if(!_seller_table)
			initSellerTable();
		else{
			if(refresh)
				_seller_table.ajax.reload();
		}
	}else{
		if(!_buyer_table)
			initBuyerTable()
		else
			if(refresh)
				_buyer_table.ajax.reload();
	}
}

/**
 * 格式化金额
 * @param data
 * @param type
 * @param full
 * @param meta
 * @returns
 */
function renderAmount(data, type, full, meta) {
    if (data) {
        return formatMoney(data, 2);
    }
    return "-";
}

/**
 * 格式化重量
 * @param data
 * @param type
 * @param full
 * @param meta
 * @returns
 */
function renderWeight(data, type, full, meta) {
    if (data) {
        return formatMoney(data, 4);
    }
    return "-";
}


function exportExcel(){
	var form = $("<form>");
	form.attr('style', 'display:none');
	form.attr('target', '');
	form.attr('method', 'post');
	form.attr('action', Context.PATH + "/order/query/exporttradecertificatelist.html");
	form.attr('enctype', 'multipart/form-data');//解决乱码问题

	//参数设置
	var input1 = $('<input>');
	input1.attr('type', 'hidden');
	input1.attr('name', 'credentialType');
	input1.attr('value',_credentialType);
	$('body').append(form);
	form.append(input1);

	//根据凭证类型判断是打印买家凭证还是打印卖家凭证 确定传买/卖家id
	if(_credentialType == 'seller'){
		var sellerId = $("#sellerName").attr("accountid");
		if(sellerId){
			var input = $('<input>');
			input.attr('type', 'hidden');
			input.attr('name', 'sellerId');
			input.attr('value', sellerId);
			form.append(input);
		}
	}else{
		var buyerId = $("#buyerName").attr("accountid");
		if(buyerId) {
			var input = $('<input>');
			input.attr('type', 'hidden');
			input.attr('name', 'buyerId');
			input.attr('value', buyerId);
			form.append(input);
		}
	}

	var code = $("#code").val();
	if(code){
		var input = $('<input>');
		input.attr('type', 'hidden');
		input.attr('name', 'code');
		input.attr('value', code);
		form.append(input);
	}

	var blacklist = $("#blacklist").val();
	if(blacklist){
		var input = $('<input>');
		input.attr('type', 'hidden');
		input.attr('name', 'blacklist');
		input.attr('value', blacklist);
		form.append(input);
	}

	var ownerName = $("#ownerName").val();
	if(ownerName){
		var input = $('<input>');
		input.attr('type', 'hidden');
		input.attr('name', 'ownerName');
		input.attr('value', ownerName);
		form.append(input);
	}

	var startTime = $("#startTime").val();
	if(startTime){
		var input = $('<input>');
		input.attr('type', 'hidden');
		input.attr('name', 'startTime');
		input.attr('value', startTime);
		form.append(input);
	}

	var endTime = $("#endTime").val();
	if(endTime){
		var input = $('<input>');
		input.attr('type', 'hidden');
		input.attr('name', 'endTime');
		input.attr('value', endTime);
		form.append(input);
	}

	form.submit();
	form.remove();
}