//交易凭证
//code by tuxianming

var _tableHeight = ""; //列表高度
var _table = "";  //列表的datatable对象
var _orders = null; //orders json对象
var _dialog_thead = null; //弹窗列表 表头
var _code = null; //凭证号，默认为空，点击凭证号之后，给这个字段赋值
jQuery(function ($) {
	initEvent();
	_tableHeight = ($(window).height()-(300)<300?300:$(window).height()-(300))+"px";
});

function initTable() {
	_table = $("#dynamic-table").DataTable({
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "lengthChange": false, //不显示pageSize的下拉框 50 100 150
        "bInfo": false,  //页脚显示信息
        "bPaginate": false,	//上一页，下一页按钮
        "fnServerData": function(sSource, aoData, fnCallback){  //这个可以用来代替默认的请求
        	var d = {};
			var sellerId = $("#sellerName").attr("accountid");
			if(sellerId) {
				d.sellerId = sellerId;
				$("#sellerId").val(sellerId);
			}
			var buyerId =  $("#buyerName").attr("accountid");
			if(buyerId){
				d.buyerId = buyerId;
			}

			var ownerName = $("#ownerName").val();
			if(ownerName) d.ownerName = ownerName;
			
			var startTime = $("#startTime").val();
			if(startTime)
				d.startTime = startTime;
			
			var endTime = $("#endTime").val();
			if(endTime)
				d.endTime = endTime;
			
			var credentialType = $("input[name='credentialType']").val();
			if(credentialType)
				d.credentialType = credentialType;
			
			d.needPage = false;  //不需要分页
			d.moreInfo = true;	//需要返回订单详情 
			d.start = 0;
			d.length =-1;
        	
        	$.ajax({
    		   type: "POST",
    		   url: Context.PATH + "/order/query/loadtradecertificate.html",
    		   data: d,
    		   success: function(msg){
    			   _orders = msg.data;
    			   fnCallback(msg);
    		   }
    		});
        	
        	
        }
        //数据源为数组时，定义数据列的对应
        , columns:[
                   {defaultContent: ''},
                   {data: 'openOrderDate'},  		//开单时间
                   {data: 'code'},   				//交易单号 
                   {data: 'buyerName'},   			//买家全称
                   {data: 'sellerName'},  			//卖家全称
                   {data: 'ownerName'},   			//交易员
                   {data: 'quantity'},  			//件数
                   {data: 'actualPickTotalQuantity'},	//实提总件数
                   {data: 'totalWeight'},			//总重量
                   {data: 'actualPickTotalWeight'},	//实提总重量
                   {data: 'totalAmount'},			//总金额
                   {data: 'actualPickTotalAmount'},	//实提总重量
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
        	}
        ]
        ,"fnRowCallback": function (nRow, aData, iDataIndex) {
        	var checkbox;
        	if(aData.payStatus <= 6 || (aData.payStatus > 6 && aData.actualPickTotalWeight <= 0)){
        		//未二结不能选
        		 checkbox ="<label class='pos-rel'>&nbsp;&nbsp;<input type='checkbox' name='unselect' value='" + aData.orderId + "' class='ace' disabled='true'><span class='lbl'>&nbsp;</span></label>";
        	}else{
        		checkbox ="<label class='pos-rel'>&nbsp;&nbsp;<input type='checkbox' name='item' value='" + aData.orderId + "' class='ace' ><span class='lbl'>&nbsp;</span></label>";
        	}
        	 
            $('td:eq(0)', nRow).html(checkbox);
        	
            var control = "<a class='control' index='"+iDataIndex+"' aid='"+aData.orderId+"' href='javascript:;'>&nbsp;∨&nbsp;</a>";
            $('td:eq(-1)', nRow).html(control);
        }
        
        ,"scrollY": _tableHeight
        ,"scrollX": true
    });
}


function initEvent(){
	//点击搜索时，根据条件重新加载报表 
	$("#queryBtn").click(function(){
		if(checkNull()){
			/*cbms.confirm("",null, function(){
				if(_table){
					_table.ajax.reload();
				}else{
					initTable();
				}
			});
		}else{*/
			if(_table){
				_table.ajax.reload();
			}else{
				initTable();
			}
		}
	});
	
	//全选与反选
	$("#checkall").click(function(){
		
		var checked = $(this).is(':checked');
        // 取消全选
        if (!checked) {
            $("input[name='item']").removeAttr("checked");
            $(this).removeAttr("checked");
        }
        else {
            $("input[name='item']").prop('checked', true);
            $(this).prop('checked', true);
        }
		
	});
	
	//control 查看详情 
	$(document.body).on("click", ".control", function(e){
		var a = $(this);
		var credentialType = $("input[name='credentialType']").val();
		var popDetail = $("#detail_dialog");
		popDetail.hide();
		
		var table = $("#detail_table");
		if(!_dialog_thead)
			_dialog_thead = table.find(".thead");
		
		table.html("");
		table.append(_dialog_thead);
		
		//得到数据
		var index = a.attr("index");
		var details = _orders[index].items;
		for(var i=0; i<details.length; i++){
			var detail = details[i];
			var html = "<tr><td>"+detail.nsortName+"</td>" +
					"<td>"+detail.material+"</td>" +
					"<td>"+detail.spec+"</td>";
				if(_orders[index].payStatus >6){
					//二结后
					html = html + "<td>"+detail.actualPickQuantityServer+"</td>" +
							"<td>"+detail.actualPickWeightServer+"</td>" ;
				}else{
					html = html + "<td>"+detail.quantity+"</td>" +
					"<td>"+detail.actualPickWeightServer+"</td>" ;
				}	
					
				if(credentialType == 'seller')	{
					html = html + "<td>"+detail.costPrice+"</td>" +
					       "<td>"+detail.allowanceAmount+"</td></tr>";
				}else{
					html = html + "<td>"+detail.dealPrice+"</td>" +
					       "<td>"+detail.allowanceBuyerAmount+"</td></tr>";
				}
			table.append($(html));
		}
		
		var apoint = a.offset();
		popDetail.css("top", (apoint.top+40)+"px").css("left",(apoint.left-780)+"px")
		popDetail.show();
		
	});
	
	//control 关闭详情 
	$(document.body).on("click", function(e){
		var ele = $(e.target);
		if(!ele.hasClass('control')){  //当点中每个品名的时候
			var popDetail = $("#detail_dialog");
			popDetail.hide();
		}
	});
	
	$("#print-seller-certi").click(function(){
		
		var items = getSelectItems();
		var sellerId = $("#sellerId").val();
		if(items.length==0){
			cbms.alert("请选择至少一条记录！");
			return;
		}
		
		//?orderIds=19844&sellerId=3163
		var idsParam = "";
		for(var i=0; i<items.length; i++){
			idsParam += "&orderIds="+items[i];
		}
		
		window.location.href=Context.PATH+"/order/print/printsaleslip.html?type=2&sellerId="+sellerId+"&needPage=false"+idsParam;
		
	});
	
	$("#print-buyer-certi").click(function(){
		
		var items = getSelectItems();
		if(items.length==0){
			cbms.alert("请选择至少一条记录！");
			return;
		}
		
		//?orderIds=19844&sellerId=3163
		var idsParam = "";
		for(var i=0; i<items.length; i++){
			idsParam += "&orderIds="+items[i];
		}
		
		window.location.href=Context.PATH+"/order/print/printdeliveryletter.html?type=4&needPage=false"+idsParam;
		
	});
	
	//获取二维码，并显示弹窗
	$(".print-code").click(function(){
		//_code
		cbms.loading();
		var d = {};
		var type = $("input[name='credentialType']").val();
		
        d.credentialType = type;
        d.needPage = false;
        
        var orderIds = getSelectItems();
        if(orderIds.length==0){
			cbms.alert("请选择至少一条记录！");
			return;
		}
        for(var i=0; i<orderIds.length; i++){
        	d['orderIds['+i+']'] = orderIds[i];
        }
		d.printQRCode = true;

		var sellerid = $("#sellerId").val();
		if(type=='seller')
			if(sellerid){
				d.sellerId = sellerid;
			}else{
				return ; //如果是打印卖家，而且sellerid 为空，则不提交
			}
		
        $.ajax({
            type: "POST",
            url: Context.PATH + '/order/certificate/saveprintinfo.html',
            data: d,
            success:function(re){
            	cbms.closeLoading();
                if (re.success) {
                	var data = re.data.split('|');
                	
                	$(".qrcode-img").attr("src", data[1]);
                	$(".certificatecode").text(data[0]);
                	code = data[0];
                	cbms.getDialog("二维码", "#qrcode-dialog");
                }else{
                	cbms.alert("获取二维码失败，原因："+re.data);
                }
            }
            ,
            error: function (re) {
                cbms.alert("获取二维码失败！");
            }
        });
	});
	
	//点击弹出二维码层的打印按钮：打印二维码
	$(document.body).on("click", ".print-qrcode-btn", function(){
		
		if (!setlistensSave(".credentialNumLine")) return;
		
		var d = {};
		d.type = true;
		d.code = $(".certificatecode").eq(0).text(); //打印次数类型：true：更新打印凭证号次数，false ：更新单子的次数
		d.isResult = true; //是不是要返回更新后的的数据,如果是：则返回更新后的BusiConsignOrderCredential对象 
		d.credentialNum = $(".credentialNum").val();
		
		$.ajax({
	        type: "POST",
	        url: Context.PATH + '/order/certificate/updateprinttimes.html',
	        data: d,
	        success:function(re){
	            if (re) {
	            	printCretificateRQCodePage();
	            	//更新打印次数
	            }
	        }
	        ,
	        error: function (re) {
	            cbms.alert("更新打印信息失败！");
	        }
	    });
	
	});
	
	
}

function getSelectItems(){
	var items = [];
	var checks = $("input[name='item']");
	checks.each(function(){
		var checkbox = $(this);
		if(checkbox.prop('checked')){
			items.push(checkbox.val());
		}
	});
	return items;
}

function checkNull(){
	var credentialType = $("input[name='credentialType']").val();
	if(credentialType=='seller'){
		var sellerId = $("#sellerName").attr("accountid");
		if(sellerId)
			return true;
		else{
			cbms.alert("卖家全称必须输入！");
			return false;
		}

	}else{
		var buyerId = $("#buyerName").attr("accountid");
		if(buyerId)
			return true;
		else{
			cbms.alert("卖家全称必须输入！");
			return false;
		}
	}
	
	return true;
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
