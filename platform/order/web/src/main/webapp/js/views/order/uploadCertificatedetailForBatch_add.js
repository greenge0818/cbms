//交易凭证
//copied by Green.Ge

var _tableHeight2 = ""; //列表高度
var _table2 = "";  //列表的datatable对象
var _orders2 = null; //orders json对象
var _dialog_thead2 = null; //弹窗列表 表头
var _accountType = $("#accountType").val();
jQuery(function ($) {
	initEvent2();
	initTable2();
	_tableHeight2 = ($(window).height()-(300)<300?300:$(window).height()-(300))+"px";
});

function initTable2() {
	_table2 = $("#dynamic-table-add").DataTable({
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "lengthChange": false, //不显示pageSize的下拉框 50 100 150
        "bInfo": false,  //页脚显示信息
        "bPaginate": false,	//上一页，下一页按钮
        "fnServerData": function(sSource, aoData, fnCallback){  //这个可以用来代替默认的请求
        	var d = {};
        	//凭证号
			var certificateNO = $("#certificateNO").val();
			if(certificateNO) d.certificateNO = certificateNO;
			//模块类型
			if(_accountType) d.accountType = _accountType;
			//公司ID
			var accountId = $("#accountId").val();
			
			/*if(_accountType=="seller"){
				d.sellerId = accountId;
			}else if(_accountType=="buyer"){
				d.buyerId = accountId;
			}*/
			
			//交易员
        	var ownerName = $("#ownerName").val();
        	if(ownerName) d.ownerName = ownerName;
        	
        	if(_accountType=='buyer'){
        		//卖家全称
        		var sellerId = $("#sellerName").attr("accountid");
            	if(sellerId) d.sellerId = sellerId;
            	if(accountId) d.buyerId = accountId;
        	}else{
        		//买家全称
				var buyerId = $("#buyerName").attr("accountid");
				if(buyerId) d.buyerId = buyerId;
				if(accountId) d.sellerId = accountId;
        	}
        	
        	var startTime = $("#startTime").val();
        	if(startTime)
        		d.startTime = startTime;
        	
        	var endTime = $("#endTime").val();
        	if(endTime)
        		d.endTime = endTime;

        	var excludeIdArr = arr_dive(_originArr.concat(_addArr),_delArr);
        	
        	d.excludeIds=excludeIdArr.join(",");
        	d.includeIds = _delArr.join(",");
        	$.ajax({
    		   type: "POST",
    		   url: Context.PATH + "/order/certificate/loadtradecertificateForAdd.html",
    		   data: d,
    		   success: function(msg){
    			   _orders2 = msg.data;
    			   fnCallback(msg);
    		   }
    		});
        }
        //数据源为数组时，定义数据列的对应
        , columns:[
                   {defaultContent: ''},			
                   {data: 'orderCreated'},  		//开单时间
                   {data: 'orderCode'},   				//交易单号 
                   {data: 'buyerName'},   			//买家全称
                   {data: 'sellerName'},  			//卖家全称
                   {data: 'ownerName'},   			//交易员
                   {data: 'totalQuantity'},  			//件数
                   {data: 'actualPickTotalQuantity'},	//实提总件数
                   {data: 'totalWeight'},			//总重量
                   {data: 'actualPickTotalWeight'},	//实提总重量
                   {data: 'totalAmount'},			//总金额
                   {data: 'actualPickTotalAmount'},	//实提总金额
                   {data: 'orderId'}				//订单ID
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
        	}
        ]
        ,"fnRowCallback": function (nRow, aData, iDataIndex) {
        	var checkbox;
        	 if(aData.payStatus <= 6 || (aData.payStatus > 6 && aData.actualPickTotalWeight <= 0))
        		//未二结不能选     二结后实提为0不能选
        		checkbox ="<label class='pos-rel'>&nbsp;&nbsp;<input type='checkbox' name='unselect' value='" + aData.orderId + "' class='ace' disabled='true'><span class='lbl'>&nbsp;</span></label>";
        	else
        		 checkbox ="<label class='pos-rel'>&nbsp;&nbsp;<input type='checkbox' name='item' value='" + aData.orderId + "' class='ace' ><span class='lbl'>&nbsp;</span></label>";
        	
        	 
           
        	$('td:eq(0)', nRow).html(checkbox);
            $('td:eq(-1)', nRow).hide()
//            _table2.column( -1 ).visible( false );
        }
        ,"scrollY": _tableHeight2
        ,"scrollX": true
    });
}


function initEvent2(){
	//点击搜索时，根据条件重新加载报表 
	$("#queryBtn2").click(function(){
		_table2.ajax.reload();
	});
	
	//全选与反选
	$("#checkall").click(function(){
		
		var checked = $(this).is(':checked');
        // 取消全选
        if (!checked) {
            $("input[name='item']").removeAttr("checked");
            $(this).removeAttr("checked");
        }else {
            $("input[name='item']").prop('checked', true);
            $(this).prop('checked', true);
        }
	});
	
	$("#save").click(function(){
		save();
	});
	
	$("#cancel").click(function(){
		cbms.closeDialog();
	});
}

function save(){
	$("input[name='item']:checked").each(function(i,e){
		
		var tds = $(e).closest("tr").find("td");
		var orderId=  parseInt(tds.eq(12).text());
		if(isInBatchList(orderId)){
			return true;
		}
		var orderCreated = tds.eq(1).text();
		var orderCode =  tds.eq(2).text();
		var buyerName =  tds.eq(3).text();
		var sellerName = tds.eq(4).text();
		var ownerName = tds.eq(5).text();
		var totalQuantity = tds.eq(6).text();
		var actualPickTotalQuantity = tds.eq(7).text();
		var totalWeight = tds.eq(7).text();
		var actualPickTotalWeight = tds.eq(9).text();
		var totalAmount = tds.eq(10).text();
		var actualPickTotalAmount=  tds.eq(11).text();
		
		var appendTr = '<tr role="row" ><td>'+orderCreated+'</td><td>'+orderCode+'</td>'
					 +'<td>'+buyerName+'</td><td>'+sellerName+'</td>'
					 +'<td>'+ownerName+'</td><td>'+totalQuantity+'</td>'
					 +'<td>'+actualPickTotalQuantity+'</td>'
					 +'<td>'+totalWeight+'</td><td>'+actualPickTotalWeight+'</td>'
					 +'<td>'+totalAmount+'</td><td>'+actualPickTotalAmount+'</td>'
					 +'<td><a class="control"href="javascript:;">&nbsp;∨&nbsp;</a>'
					 +'<button class="del" orderId="'+orderId+'">删除</button></td></tr>';
		$("#dynamic-table").append(appendTr);
		
		//从删除列表里删掉
		var idx = $.inArray(orderId,_delArr);
		if(idx>-1){
			_delArr.splice(idx,1);
		}
		//加到新增列表里
		idx = $.inArray(orderId,_originArr);
		if(idx==-1){
			_addArr.push(orderId);
		}
	});
	uniqueArr(_addArr);
	cbms.closeDialog();
	
}

function isInBatchList(p_orderId){
	var isIn =false;
	$("#dynamic-table .del").each(function(i,e){
		var orderId= $(e).attr("orderId");
		if(orderId == p_orderId){
			isIn = true;
			return false;
		}
	});
	return isIn;
}