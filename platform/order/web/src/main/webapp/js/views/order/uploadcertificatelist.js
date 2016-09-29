//交易凭证
//copied by Green.Ge

var _tableHeight = "";
var _table = "";
jQuery(function ($) {
	initEvent();
	_tableHeight = ($(window).height()-(300)<300?300:$(window).height()-(300))+"px";
	initTable();
});

//卖家 -> 品名
function initTable() {
	_table = $("#dynamic-table").DataTable({
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "lengthChange": true, //显示pageSize的下拉框 50 100 150
        iDisplayLength: 50,
        "ajax": {
            "url": Context.PATH + "/order/certificate/loadcertificateforupload.html",
            "type": "POST",
            data: function (d) {
            	var accountType = $("#accountType").val();
            	if(accountType) d.accountType = accountType;
            	//凭证号
            	var certificateNO = $("#certificateNO").val();
            	if(certificateNO) d.certificateNO = certificateNO;
            	//交易单号
            	var code = $("#code").val();
            	if(code) d.code = code;
            	
            	
            	if(accountType=='seller'){
            		//卖家全称
            		var sellerId = $("#sellerName").attr("accountid");
                	if(sellerId) d.sellerId = sellerId;
            	}else{
            		//买家全称
    				var buyerId = $("#buyerName").attr("accountid");
    				if(buyerId) d.buyerId = buyerId;
            	}
				
            	
            	
            	//交易员
            	var ownerName = $("#ownerName").val();
            	if(ownerName) d.ownerName = ownerName;
            	//创建凭证开始时间
            	var startTime = $("#startTime").val();
            	if(startTime) d.startTime = startTime;
            	//创建凭证结束时间
            	var endTime = $("#endTime").val();
            	if(endTime) d.endTime = endTime;
            	//类型
            	var blacklist = $("#blacklist").val();
            	if(blacklist)
            		d.blacklist = blacklist;
            	//凭证状态
            	var status = $("#status").val();
            	if(status) d.status = status;
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns:[
//                   {defaultContent: ''},
				   {data: 'certificateNO'},   		//凭证号 
				   {data: 'certificateCreated'},   			//凭证创建时间 
                   {data: 'code'},   				//交易单号 
                   {data: 'orderCreated'},  		//开单时间
                   {data: 'buyerName'},   			//买家全称
                   {data: 'ownerName'},   			//交易员
                   {data: 'sellerName'},  			//卖家全称
                   {data: 'quantity'},  			//件数
                   {data: 'totalWeight'},			//总重量
                   {data: 'actualPickTotalWeight'},	//实提总重量
                   {data: 'totalAmount'},			//总金额
                   {data: 'actualPickTotalAmount'},	//实提总重量
                   {data: 'approvalRequired'},		//类型
                   {data: 'status'},				//状态
                   {defaultContent: ''}				//操作
               ]
        , columnDefs:[
         	{
        	    "targets": 8, //第几列 从0开始
        	    "data": "totalWeight",
        	    "render": renderWeight
        	},
        	{
        	    "targets": 9, //第几列 从0开始
        	    "data": "actualPickTotalWeight",
        	    "render": renderWeight
        	},
        	{
        	    "targets": 10, //第几列 从0开始
        	    "data": "totalAmount",
        	    "render": renderAmount
        	},
        	{
        	    "targets": 11, //第几列 从0开始
        	    "data": "actualPickTotalAmount",
        	    "render": renderAmount
        	},
        	{
        	    "targets": 12, //第几列 从0开始
        	    "data": "approvalRequired",
        	    "render": renderType
        	},
        	
        	{
        	    "targets": 13, //第几列 从0开始
        	    "data": "status",
        	    "render": renderStatus
        	}
        ]
        ,"fnRowCallback": function (nRow, aData, iDataIndex) {
        	
//        	var checkbox ="<label class='pos-rel'>&nbsp;&nbsp;<input type='checkbox' value='" + aData.orderId + "' class='ace'><span class='lbl'>&nbsp;</span></label>"; 
//            $('td:eq(0)', nRow).html(checkbox);
        	
            var controller = "";
            if(aData.orderId && aData.name.indexOf('批') == -1){
            	if($("#accountType").val() == 'seller')
            	   controller = "<a href='"+Context.PATH+"/order/certificate/"+aData.orderId+"/"+aData.sellerId+"/"+$("#accountType").val()+"/pendsubmit/detail.html'>查看详情</a>";
            	else
            		controller = "<a href='"+Context.PATH+"/order/certificate/"+aData.orderId+"/"+aData.buyerId+"/"+$("#accountType").val()+"/pendsubmit/detail.html'>查看详情</a>";
            }else{
            	if($("#accountType").val() == 'seller')
            		controller = "<a href='"+Context.PATH+"/order/certificate/"+aData.certificateId+"/"+$("#accountType").val()+"/"+aData.sellerId+"/pendsubmit/detailbatch.html'>查看详情</a>";
            	else
            		controller = "<a href='"+Context.PATH+"/order/certificate/"+aData.certificateId+"/"+$("#accountType").val()+"/"+aData.buyerId+"/pendsubmit/detailbatch.html'>查看详情</a>";
            }

            $('td:eq(-1)', nRow).html(controller);
        }
        ,"scrollY": _tableHeight
        ,"scrollX": true
    });
}


function initEvent(){
	
	//点击搜索时，根据条件重新加载报表 
	$("#queryBtn").click(function(){
		_table.ajax.reload();
	});

	//点击导出时，根据条件导出数据到Excel
	$("#export").click(function () {
		exportExcel();
	});
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

function renderStatus(data, type, full, meta) {
    if (data) {
    	if(data == "PENDING_SUBMIT")
    		return "待提交";
    	if(data == "PENDING_APPROVAL")
            return "待审核";
    	if(data == "APPROVED")
            return "审核通过";
    	if(data == "DISAPPROVE")
            return "审核不通过";
    }
    return "-";
}

function renderType(data, type, full, meta) {
    if (data) {
        return "必须审核通过才能开票";
    }else{
    	return "不须审核通过也能开票";
    }
}

function exportExcel(){
	var form = $("<form>");
	form.attr('style', 'display:none');
	form.attr('target', '');
	form.attr('method', 'post');
	form.attr('action', Context.PATH + "/order/certificate/exportuploadcertificagelist.html");
	form.attr('enctype', 'multipart/form-data');//解决乱码问题

	//参数设置
	var accountType = $("#accountType").val();//凭证类型： 买家/卖家
	var input1 = $('<input>');
	input1.attr('type', 'hidden');
	input1.attr('name', 'accountType');
	input1.attr('value',accountType);
	$('body').append(form);
	form.append(input1);

	//凭证号
	var certificateNO = $("#certificateNO").val();
	if(certificateNO) {
		var input = $('<input>');
		input.attr('type', 'hidden');
		input.attr('name', 'certificateNO');
		input.attr('value', certificateNO);
		form.append(input);
	}

	//根据凭证类型判断是上传买家凭证还是上传卖家凭证 确定传买/卖家id
	if(accountType == 'seller'){
		var sellerId = $("#sellerName").attr("accountid");
		if(sellerId){ //卖家全称
			var input = $('<input>');
			input.attr('type', 'hidden');
			input.attr('name', 'sellerId');
			input.attr('value', sellerId);
			form.append(input);
		}
	}else{
		var buyerId = $("#buyerName").attr("accountid");
		if(buyerId) { //买家全称
			var input = $('<input>');
			input.attr('type', 'hidden');
			input.attr('name', 'buyerId');
			input.attr('value', buyerId);
			form.append(input);
		}
	}

	//交易单号
	var code = $("#code").val();
	if(code){
		var input = $('<input>');
		input.attr('type', 'hidden');
		input.attr('name', 'code');
		input.attr('value', code);
		form.append(input);
	}

    //类型
	var blacklist = $("#blacklist").val();
	if(blacklist){
		var input = $('<input>');
		input.attr('type', 'hidden');
		input.attr('name', 'blacklist');
		input.attr('value', blacklist);
		form.append(input);
	}

	//交易员
	var ownerName = $("#ownerName").val();
	if(ownerName){
		var input = $('<input>');
		input.attr('type', 'hidden');
		input.attr('name', 'ownerName');
		input.attr('value', ownerName);
		form.append(input);
	}

	//创建凭证开始时间
	var startTime = $("#startTime").val();
	if(startTime){
		var input = $('<input>');
		input.attr('type', 'hidden');
		input.attr('name', 'startTime');
		input.attr('value', startTime);
		form.append(input);
	}

	//创建凭证结束时间
	var endTime = $("#endTime").val();
	if(endTime){
		var input = $('<input>');
		input.attr('type', 'hidden');
		input.attr('name', 'endTime');
		input.attr('value', endTime);
		form.append(input);
	}

	//凭证状态
	var status = $("#status").val();
	if(status) {
		var input = $('<input>');
		input.attr('type', 'hidden');
		input.attr('name', 'status');
		input.attr('value', status);
		form.append(input);
	}

	form.submit();
	form.remove();
}