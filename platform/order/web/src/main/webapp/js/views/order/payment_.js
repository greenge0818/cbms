/**
 * Created by lx on 2016/03/03.
 * 付款申请单列表
 */

var dt;
jQuery(function ($) {
    initClickEvent();
});
$(document).ready(function () {
	    dt = $('#dynamic-table').DataTable({
        ajax: {
            url: Context.PATH + '/order/paymentmanager/get/payment.html',
            type: "POST", 
            data: function (d) {
            	d.type = $("#type").val();//付款申请单类型
            	d.code = $("#code").val();//付款申请单号
            	d.accountName = $("#buyer_name").val();//客户名称
            	d.payAmount =  $("#pay_amount").val();//申请付款金额
            	d.startTime = $("#startTime").val();//起始时间
            	d.endTime = $("#endTime").val()//终止时间
            	var closedStatus = $("#closed").prop("checked");
            	if(closedStatus){
            		d.closedStatus = closedStatus;
            	}
            }
        },
        aLengthMenu: [50, 100, 150],
        serverSide: true, //服务模式
        processing: true,//显示数据加载进度
        searching: false, //是否启用搜索
        ordering: false, //是否启用排序
        bFilter: false,
        bLengthChange: true, //不显示每页长度的选择条
        fnRowCallback: function (nRow, aData, iDataIndex) {
        	 //买家/卖家全称显示，如果有多个部门则显示部门，否则不显示部门lixiang 2016-04-08
        	 var custName = aData.accountName;
	       	 if (aData.departmentCount > 1) {
	       		 custName = custName+"【"+aData.departmentName+"】";
	       	 }
	       	 $('td:eq(1)', nRow).html(custName);
             $('td:eq(5)', nRow).html(formatMoney(aData.payAmount,2)).addClass("text-right");
             var link = "";
             if (aData.type == "2") {
            	 link = '<a href="'+Context.PATH+'/order/query/payrequest.html?requestId='+aData.requsetId+'&type=2">查看详情</a>';
             } else if (aData.type == "3") {
            	 link = '<a href="'+ Context.PATH +'/payment/'+aData.requsetId+'/paymentrequisition.html?type=2">查看详情</a>';
             } else if (aData.type == "1" || aData.type == "5") {
            	 link = '<a href="'+Context.PATH + '/order/query/receipts.html?requestitemid=' + aData.payItemId + '&orderid='+ aData.consignOrderId +'&pay">查看回执单</a></br>'
            		 + '<a href="'+Context.PATH+'/order/paymentmanager/paymentrequest.html?payItemId='+aData.payItemId+'">查看详情</a>';
             } else if (aData.type == "4") {
            	 link = '<a href="'+Context.PATH+'/order/query/payrequest.html?requestId='+aData.requsetId+'&type=4">查看详情</a>';
             }
             $('td:eq(-1)', nRow).html(link);
             var payMode = "现金支付";
             $('td:eq(9)', nRow).html(payMode);
             if (aData.code == null) {
            	 $('td:eq(0)', nRow).html(aData.oldCode); 
             } else {
            	 $('td:eq(0)', nRow).html(aData.code); 
             }
             return nRow;
        },
        columns: [
            {data: 'code'},   //付款申请单号
            {defaultContent: ''}, //客户名称
            {data: 'requesterName'},  //申请人
            {data: 'orgName'},  //服务中心
            {data: 'created'}, //初次申请时间
            {defaultContent: ''}, //付款金额
            {data: 'cashier'},// 出纳
            {data: 'paymentTime'},// 确认付款时间
            {data: 'type'},//付款类型
            {defaultContent: ''},// 支付方式
            {defaultContent: ''}//操作
        ],
        "oLanguage": {                          //汉化
            "sLengthMenu": "每页显示 _MENU_ 条记录",
            "sZeroRecords": "没有检索到数据",
            "sInfo": "当前数据为从第 _START_ 到第 _END_ 条数据；总共有 _TOTAL_ 条记录",
            "sInfoEmtpy": "没有数据",
            "sProcessing": "正在加载数据...",
            "oPaginate": {
                "sFirst": "首页",
		        "sPrevious": "上一页",
		        "sNext": "下一页",
                "sLast": "尾页"
            }
        },
        columnDefs: [
             {
                 "targets": 4, //第几列 从0开始
                 "data": "created",
                 "render": formatDay
             }
             ,{
                 "targets": 8, //第几列 从0开始
                 "data": "type",
                 "render": getType
             }
             ,{
                 "targets": 7, //第几列 从0开始
                 "data": "paymentTime",
                 "render": formatDay
             }
        ]
    });
	    
    function formatDay(data) {
    	var dt = new Date(data);
    	var time = dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate() + " " + 
		((dt.getHours()/1)<10?("0"+dt.getHours()):dt.getHours()) + ":" + ((dt.getMinutes()/1)<10?("0"+dt.getMinutes()):dt.getMinutes()) + ":" 
		+ ((dt.getSeconds()/1)<10?("0"+dt.getSeconds()):dt.getSeconds());
    	return time;
    }
    
    function getType(data) {
    	var type = "";
    	if (data == "1" || data == "5") {
    		type = "订单付款";
    	} else if (data == "2") {
    		type = "二结付款";
    	} else if (data == "3") {
    		type = "提现付款";
    	} else if (data == "4") {
    		type = "预付款";
    	}
    	return type;
    }
    
    function toStatus(data) {
    	var status ="";
    	if(data == "REQUESTED ") {
    		status = "待审核";
    	} else if (data == "APPROVED") {
    		status = "待确认";
    	} else if (data == "DECLINED") {
    		status = "审核不通过";
    	} else if (data == "CLOSED") {
    		status = "已关闭";
    	}
    	return status;
    }
});

$("#btn").click(function(){
	var forms = setlistensSave("#searchForm");
	if (!forms)return;
	dt.ajax.reload();
});


function initClickEvent() {
    
    //状态点击选择，弹出选项框
    $("#payStatusBtn").click(showSelectOptionsBox);
    
    //交易状态单，选中checkbox， 出现在一个已经选中的label, 
    selectOptions();
    
} 

function buildParam(d){
	var result = [];
	//查看哪些状态被选中了，如果选中了: 全部 选项则不往后台传输数据
	var status = [];
	$("#status li").each(function(){
		var li = $(this);
		var checkbox = li.find("input[type='checkbox']");
		if(checkbox.prop('checked')){
			var status = checkbox.val();
			if(status!='all'){
				orgIds.push(status);
			}
		}
	});
	if(status && status.length>0){
		if(d) d.status = status.toString();
		result.push({"key":"status", "value": status.toString()});
	}
	return result;
}

function showSelectOptionsBox(){
	var optionbox = $("#status");
	if(optionbox.css("display") == "none"){
		optionbox.show();
		$(document).on("mouseleave","#pay_status", function(){
			optionbox.hide();
		});
	}else{
		optionbox.hide();
	}
}

function selectOptions(){
	$("#selectAllStatus").click(function(){
		var checked=$(this).prop('checked');
		if(checked){
			$("#status li input[type='checkbox']").removeAttr("checked");
			$(this).prop("checked", "checked");
		}
	})
	
	$("#status li input[type='checkbox']").not("#selectAllStatus").click(function(){
		var selectAll = $("#selectAllStatus");
		if(selectAll.prop("checked")){
			//$(this).removeAttr("checked");
			selectAll.removeAttr("checked");
		}
	});
}

function isNull(obj){
	//undefined, '', 0, null, '   '
	if(obj){
		return true;
	}
	return false;
	
	
	
}

