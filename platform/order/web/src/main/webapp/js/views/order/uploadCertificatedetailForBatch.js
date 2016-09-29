//交易凭证
//code by tuxianming

var _tableHeight = ""; //列表高度
var _table = "";  //列表的datatable对象
var _orders = null; //orders json对象
var _dialog_thead = null; //弹窗列表 表头
var _accountType = $("#accountType").val();//客户类型，正在操作什么客户的凭证，buyer/seller
var _addArr = [];
var _delArr = [];
var _originArr = [];
jQuery(function ($) {
	initEvent();
	initTable();
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

			if(_accountType){
				//d.credentialType = _accountType;
				d.accountType = _accountType;
			}
			var accountId = $("#accountId").val();
			if(_accountType=="seller"){
				d.sellerId = accountId;
				var certificateNO = $("#certificateNO").val();
				if(certificateNO) d.certificateNO = certificateNO;
			}
			/*else if(_accountType=="buyer"){
				//d.buyerId = accountId;
			}*/

			d.credentialNullOfBatch = false;
			
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
        	_originArr.push(aData.orderId);
            var control = "<a class='control' index='"+iDataIndex+"' aid='"+aData.orderId+"' href='javascript:;'>&nbsp;∨&nbsp;</a><button class='none del' orderId="+aData.orderId+" accountId="+$("#accountId").val()+">删除</button>";
            $('td:eq(-1)', nRow).html(control);
        }
        ,"scrollY": _tableHeight
        ,"scrollX": true
    });
}


function initEvent(){
	//点击编辑按钮
	$(document).on("click",".edit",function(){
		$(".del,.add,#saveArea").show();
		$(".edit,#printArea").hide();
	});
	
	//点击新增按钮
	$(document).on("click",".add",function(){
		var certificateNO = $("#certificateNO").val();

		var accountId = $("#accountId").val();
		var url = Context.PATH+"/order/certificate/openAddDialog/"+_accountType+"/"+certificateNO+"/"+accountId+".html"; 
		cbms.getDialog("新增凭证关联的交易单",url);
	});
	
	//点击保存按钮
	$(document).on("click",".save",function(){
		saveOrderInfo();
	});
	//点击返回按钮
	$(document).on("click",".back",function(){
//		$(".del,.add,#saveArea").hide();
//		$(".edit,#printArea").show();
		location.reload();
	});
	
	//点击删除按钮
	$(document).on("click",".del",function(){
		var row = $(this).closest("tr");
		delRow(row);
	});
	
	//control 查看详情 
	$(document.body).on("click", ".control", function(e){
		var a = $(this);

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
					
				if(_accountType == 'seller')	{
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
	
	
	//显示二维码
	$("#print-seller-code").click(function(){
		cbms.getDialog("二维码", "#qrcode-dialog");
	});
	
	//打印二维码
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
                if (re.success) {
                	printCretificateRQCodePage();
                	//更新打印次数
                	var certi = re.data;
                	var info = "已打印"+certi.printCodeNumber+"次<br/>"+certi.printCodeBy+"上次打印时间: "+certi.printCodeDateStr+"<br/>IP:"+certi.printCodeIp+"<br/>";
                	$("#print-code-info").html(info);
                }else{
                	cbms.alert("更新打印信息失败， 原因："+re.data);
                }
            }
            ,
            error: function (re) {
                cbms.alert("更新打印信息失败！");
            }
        });
		
	})
	
	
}
/**
 * @desc 删除当前行
 * @param row
 */
function delRow(row){
	cbms.confirm("确实删除？","",function(){
		var orderId = parseInt($(row).find("button.del").attr("orderId"));
		//不要跟后台交互了
//		$.get(Context.PATH+"/order/certificate/removeBatchCertificateNO/"+_accountType+"/"+$("#accountId").val()+"/"+orderId+".html","",function(data){
//			if(data.success){
//				
//			}else{
//				alert(data.msg);
//			}
//		});
		$(row).remove();
		//从新增列表里删掉
		var idx = $.inArray(orderId,_addArr);
		if(idx>-1){
			_addArr.splice(idx,1);
		}
		//加到删除列表里
		idx = $.inArray(orderId,_originArr);
		if(idx>-1){
			_delArr.push(orderId);
		}
		//去重
		uniqueArr(_delArr);
	});
}
/**
 * 保存提交整个页面
 */
function saveOrderInfo(){
	$.post(Context.PATH+"/order/certificate/saveBatchUploadCertificateNO.html",
			{accountType:$("#accountType").val(),accountId:$("#accountId").val(),certificateNO:$("#certificateNO").val(),addIds:_addArr.join(","),delIds:_delArr.join(","),originIds:_originArr.join(",")},
			function(res){
				if(res.success){
					cbms.alert("批量凭证信息已发生改变，将生成新的凭证号！请重新打印批量凭证，并上传",function(){
						window.location = Context.PATH+"/order/certificate/"+res.data+"/"+$("#accountType").val()+"/"+$("#accountId").val()+"/pendsubmit/detailbatch.html";
					});
				}else{
					cbms.alert(data.data);
				}
		
	})
}

function checkNull(){
	var credentialType = $("input[name='credentialType']").val();
	if(credentialType=='seller'){
		var sellerName = $("#sellerName").val();
		if(sellerName)
			return false;
	}else{
		var buyerName = $("#buyerName").val();
		if(buyerName)
			return false;
	}
	
	var ownerName = $("#ownerName").val();
	if(ownerName)
		return false;
	
	var startTime = $("#startTime").val();
	if(startTime)
		return false;
	
	var endTime = $("#endTime").val();
	if(endTime)
		return false;
	
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
/**
 * 
 * @desc 数组去重
 * @returns 去重后的数组
 */
function uniqueArr(arr){
	var res = [];
	var json = {};
	for(var i = 0; i < arr.length; i++){
		if(!json[arr[i]]){
		   res.push(arr[i]);
		   json[arr[i]] = 1;
		}
	}
	return res;
}

/**
 * @desc 数组相减
 * @param 被减数
 * @param 减数
 * @returns 相减后的数组
 */
function arr_dive(aArr,bArr){   //第一个数组减去第二个数组
    if(bArr.length==0){return aArr}
    var diff=[];
    for(var e in aArr){
    	if($.inArray(aArr[e],bArr)==-1){
    		 diff.push(aArr[e]);
    	}
    }
    return diff;
}