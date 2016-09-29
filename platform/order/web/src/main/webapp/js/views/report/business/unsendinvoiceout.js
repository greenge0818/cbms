/**
 * tuxianming on 20160621.
 * 应收应付发票报表
 */
var table, _tableHeight = null;
var balanceSecondDebtLimit = null;

jQuery(function ($) {
	_tableHeight = ($(window).height()-(400)<400?400:$(window).height()-(400))+"px"
	balanceSecondDebtLimit = $("input[name='balanceSecondDebtLimit']").val() || null;
	initTable();
    initEvent();
    
});

function initTable() {
	table = $("#dynamic-table").DataTable({
        "pageLength": 50, //每页记录数
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "aLengthMenu": [10, 20, 50,100,150], //这个为选择每页的条目数default [10, 25, 50, 100]
        "lengthChange": true, //是否显示pageSize的下拉框
        "oLanguage": {sUrl: Context.PATH + "/js/DT_zh.txt"},
        "bAutoWidth": false, //是否自动计算表格各列宽度
        "scrollY": _tableHeight,
        "scrollX": true,
        "ajax": {
            "url": Context.PATH + "/report/business/loadunsendinvoiceout.html"
            , "type": "POST"
            , data: function (d) {
            	buildParams(d);
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'buyerName'},
            {data: 'orderCode'},
            {data: 'balanceSecondSettlement'},
            {data: 'buyerCredentialStatus'},
            {data: 'sellerCredentialStatus'},
            {data: 'nsortName'},
            {data: 'spec'},
            {data: 'weight'},
            {data: 'amount'},
            {data: 'orgName'},
            {data: 'invoiceType'},
            {data: 'checklistId'},
            {data: 'created'},
            {data: 'isSend'},
            {data: 'checklistDetailId'}
        ],
        columnDefs: [
               {
                   "targets": 2, //第几列 从0开始
                   "data": "balanceSecondSettlement",
                   "render": function (data, type, full, meta){
                	
                	   if(full.isLimitDebet){
                		   if(data){
                			   if(data>=0)
                				   return "0.00";
                			   return formatMoney(Math.abs(data), 2);
                		   }
                		   return "0.00";
                	   }else{
                		   if(data && Math.abs(data) > 0){
                			   return '<span style="color:red;">'+formatMoney(Math.abs(data), 2)+'</span>'
                		   }
                		   return "0.00";
                	   }
                   }
               }
               ,{
                   "targets": 3, //第几列 从0开始
                   "data": "buyerCredentialStatus",
                   "render": function (data, type, full, meta){
                	   if(data != '审核通过'){
                		   return '<span style="color:red;">'+data+'</span>';
                	   }
                	   return data;
                   }
               }
               ,{
                   "targets": 4, //第几列 从0开始
                   "data": "sellerCredentialStatus",
                   "render": function (data, type, full, meta){
                	   if(data != '审核通过'){
                		   return '<span style="color:red;">'+data+'</span>';
                	   }
                	   return data;
                   }
               }
               ,{
                   "targets": 7, //第几列 从0开始
                   "data": "weight",
                   "render": renderWeight
               }
               ,{
                   "targets": 8, //第几列 从0开始
                   "data": "amount",
                   "render": renderAmount
               }
               ,{
                   "targets": 13, //第几列 从0开始
                   "data": "checklistDetailId",
                   "render": function (data, type, full, meta){
                	   return data? '已寄出':'未寄出';
                   }
               }
               ,{
            	   "targets": 14, //第几列 从0开始
            	   "data": "checklistDetailId",
            	   "render": function (data, type, full, meta){
            		   
            		   var checked = "";
            		   if(full.isSend){
            			   checked= " checked='checked' ";
            		   }
            		   
            		   return "<input type='checkbox' class='select_send' "+checked+" value='"+data+"'/>";
            	   }
               }
         ]
        , fnRowCallback: function (nRow, aData, iDataIndex) {
        	
        	 $('td:eq(2)', nRow).css("text-align", "right");
        	 $('td:eq(7)', nRow).css("text-align", "right");
        	 $('td:eq(8)', nRow).css("text-align", "right");
        	 $('td:eq(3)', nRow).css("width","70px");
        	 $('td:eq(4)', nRow).css("width","70px");
        	 $('td:eq(10)', nRow).css("width","90px");
        }
        , fnDrawCallback: function(){
        	//$(".pagination").append('<li id="submitBtn"><a href="#"> 确定 </a></li>');
        }
    });
}

function renderAmount(data, type, full, meta) {
    if (data) {
        return formatMoney(data, 2);
    }
    return "0.00";
}

function renderWeight(data, type, full, meta) {
    if (data) {
        return formatMoney(data, 4);
    }
    return "0.00000";
}

function initEvent() {
	
    $("#queryBtn").click(function () {
		if(table)
			table.ajax.reload();	
		else
			initTable();
    });

    $("#export").click(function () {
        exportExcle();
    });

	
	/**
	 * 点击：服务中心选择框
	 */
	$("#orgSelectBtn").click(showSelectOptionsBox);
	
	/**
	 * 选中：所有服务中心时，取消其它服务中心选项框, 选中其它服务中心时，取消：所有服务中心
	 */
	clickSelectAll();
	
	$("#getNew").click(getNew);
    
	checkNodes = $(".select_send");
	
	$(document).on("click", ".select_send", function(){
		
		var _this = $(this);
		if(_this.is(":checked")){
			cbms.confirm("是否确定该销项票清单已寄出？", "", function(){
				submitUpdate(_this);
			});
		}else{
			cbms.confirm("是否修改该销项票清单为未寄出？", "", function(){
				submitUpdate(_this);
			});
		}
		
	});
	
	
	/*
	$(document).on("click", "#submitBtn", function(){
		
		cbms.confirm("确定改变该清单的状态吗？", "", submitUpdate);
		
	});
	*/
	
}

function submitUpdate(_this){
	
	var checkNodes = _this; //|| $(".select_send");
	var checks = [];
	var ids = [];
	
	try{
		for(var i=0; i<checkNodes.length; i++){
			checks.push(checkNodes.eq(i).is(":checked")?1:0);
			ids.push(checkNodes.eq(i).val());
		}
	}catch(err){
		
	}
	
	if(ids.length==0){
		cbms.alert("没有要更新的数据");
		return;
	}
	
	cbms.loading();
	$.post(Context.PATH + "/invoice/out/checklist/update/send.html", 
		{"ids": ids.toString(), "sends": checks.toString()},
		function(result){
			cbms.closeLoading();
			if(result.success){
				cbms.closeLoading();
				table.ajax.reload();
				cbms.alert("更新成功！");
			}else{
				cbms.alert("更新失败！");
			}
		}
	);  
}

function exportExcle() {
    var form = $("<form>");
    form.attr('style', 'display:none');
    form.attr('target', '');
    form.attr('method', 'post');
    
	form.attr('action', Context.PATH + "/report/business/exportunsendinvoiceout.html");

    form.attr("enctype", "multipart/form-data");//防止提交数据乱码

    $('body').append(form);

    var params = buildParams({});
    for(var p in params){
    	var input = $('<input>');
    	input.attr('type', 'hidden');
    	input.attr('name', p);
    	input.attr('value', params[p]);
        form.append(input);
    }

    form.submit();
    form.remove();
}

function buildParams(d) {
	if(d==null) 
		d = {};
    var accountId = $("#accountId").attr("accountid");
    if(accountId){
    	d.buyerId = accountId;
    }
    
    var status = $("#status-selector").val();
    if(status){
    	d.isSend = status;
    }
    
    var startTime = $("#startTime").val();
    if(startTime){
    	d.beginTime = startTime;
    }
    
    var endTime = $("#endTime").val();
    if(endTime){
    	d.endTime = endTime;
    }

    //查看哪些服务中心选中了，如果选中了: 全部服务中心 选项则不往后台传输数据
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
	if(orgIds && orgIds.length>0){
		for(var i=0; i<orgIds.length; i++){
			d["orgIds["+i+"]"] = orgIds[i];
		}
	}
    
	var checklistId = $("input[name='checklistId']").val();
    if(checklistId){
    	d.id = checklistId;
    }
    
    var orderCode = $("#orderCode").val();
    if(orderCode){
    	d.orderCode = orderCode;
    }
    
    return d;
}


var repeatGet = false;
function getNew(){
	
	//防止重复提交
	if(repeatGet)
		return ;
	
	repeatGet = true;
	
	cbms.confirm("获取最新数据，可能需等待较长时间，是否确定获取？",null,function(){
		cbms.loading();
		$.get(Context.PATH + "/invoice/out/checklist/update/secondsettlement.html", {},
				function(result){
					repeatGet = false;
					if(result.success){
						cbms.closeLoading();
						cbms.alert("获取成功！");
						table.ajax.reload();
					}else{
						cbms.alert("获取失败！");
					}
					
				}
			);  
	});
}

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