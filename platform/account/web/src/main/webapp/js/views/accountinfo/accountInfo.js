/**
 * 账户信息基础页面js
 * 
 */

//账户信息全局变量
var _accountInfoAttr={
	oTable:null,         //datatable 对象
	deptId:null          //部门ID
}

//账户银票信息全局变量
var _acceptDraftList;

$(document).ready(function() {
	loadAcceptDraftInfo();


	//initiate dataTables plugin
	//部门id
	_accountInfoAttr.deptId=$(".ck li.acti a").attr("val");
	_accountInfoAttr.oTable = $('#dynamic-table')
	//.wrap("<div class='dataTables_borderWrap' />")   //if you are applying horizontal scrolling (sScrollX)
	.dataTable({
		"dom" : 'lrTt<"bottom"p>i<"clear">',
		"pageLength": 100, //每页记录数
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "ajax": {
            "url" : Context.PATH + "/accountinfo/search.html",
            "type" : "POST",
            data : function(d) {
                return $.extend({}, d, {
                	consignOrderCode:$.trim($("#consignOrderCode").val()),
                	applyType:$("#applyType").val(),
                	startTime:$("#startTime").val(),
                	endTime:$("#endTime").val(),
                	departmentId:_accountInfoAttr.deptId,
                	accountId:$("#accountId").val()
                });
            },
            "dataSrc": function ( json ) {
                  return json.data;
                }
        },
        columns: [
          {data: 'created',"mRender":function(e,t,f){return formatDay(e);}},
          {data: 'associationType'},
          {data: 'consignOrderCode'},
          {data: 'applyType'},
          {data: 'cashHappenBalance',"mRender":function(e,t,f){return formatMoney(e);}, "sClass": "text-right"},
          {data: 'cashCurrentBalance',"mRender":function(e,t,f){return formatMoney(e);}, "sClass": "text-right"},
          {data: 'amount',"mRender":function(e,t,f){return formatMoney(e);}, "sClass": "text-right"},
          {data: 'currentBalance',"mRender":function(e,t,f){return formatMoney(e);}, "sClass": "text-right"},
          {data: 'credit',"mRender":function(e,t,f){return formatMoney(e);}, "sClass": "text-right"},
          {data: 'creditBalance',"mRender":function(e,t,f){return formatMoney(e);}, "sClass": "text-right"},
          {data: 'applyerName'}]

	});

	//多部门的公司流水隐藏二结、信用额度4列，否则显示
	isShowTableColumns();

	//搜索
	$(document).on("click", "#searchBtn", function(){
		reloadList();
	});

	//导出
	$(document).on("click", "#exportBtn", function(){
		cbms.confirm("确定导出文件吗?",null,function(){
			 exportExcel();
		});
	});
	
	$("#accountInfo").addClass("active").find("a").attr("href","javascript:void(0);");
	
	//部门li点击切换
	$(document).on("click",".ck li a",function(){
		$(".ck li").removeClass("acti");
		$(this).closest('li').addClass("acti");
		_accountInfoAttr.deptId=$(this).closest('li a').attr("val");
		$("div[id*='dAccountInfo_']").addClass("none");
		$("#dAccountInfo_"+_accountInfoAttr.deptId).removeClass("none");
		reloadList();
	});

	//提现按钮点击事件
	$(document).on("click","div[id*='dAccountInfo_'] button",function(){
		var id = $(this).closest(".borderq").find(".ck li.acti a").attr("val");
		if($(this).hasClass("balance")) {
			window.location.href = Context.PATH + "/accountinfo/" + id + "/withdrawal.html";
		}
	});
	
	//点击资金分配 chengui
	$(document).on("click","#fundAllocations",function(){
		cbms.getDialog("分配资金","#fundAllocationsForm");

		//分配类型选择
		$("#allocationTypeSelect").change(function(){

			if("" == $("#allocationTypeSelect").find("option:selected").val()){
				$("#draftCodeInfo").hide();
				$("#draftAmountInfo").hide();
				$("#remainingCashInfo").hide();
			}
			//银票资金分配
			else if(2 == $("#allocationTypeSelect").find("option:selected").val()){
				$("#draftCodeInfo").show();
				$("#draftAmountInfo").show();
				$("#remainingCashInfo").hide();
			}
			//现金分配
			else{
				$("#remainingCashInfo").html("");
				var totalDraftRemainingAmount = parseFloat(0.00);
				for(var i=0; i<_acceptDraftList.length; i++){
					totalDraftRemainingAmount = totalDraftRemainingAmount  + parseFloat( _acceptDraftList[i].remainingAmount);
				}
				var remainingCashSum = parseFloat($("#totalSum").text().split(',').join("")) - totalDraftRemainingAmount;
				$("#remainingCashInfo").append($("#remainingCashInfoTemplate").html().replace(/__remainingCashSum__/g, formatMoney(remainingCashSum)));
				$("#remainingCashInfo").show();
				$("#draftCodeInfo").hide();
				$("#draftAmountInfo").hide();
			}

		});

		//银票票号选择
		$("#draftCodeSelect").change(function(){
			var selectedDraftId = $("#draftCodeSelect").find("option:selected").val();

			$("#draftAmountInfo").html("")
			for(var i=0; i<_acceptDraftList.length; i++){
				var acceptDraft = _acceptDraftList[i];
				if(selectedDraftId == _acceptDraftList[i].id){
					var html = $("#draftAmountInfoTemplate").html().replace(/__draftSum__/g, formatMoney(acceptDraft.amount))
							.replace(/__allocatedSum__/g, formatMoney(acceptDraft.amount - acceptDraft.remainingAmount))
							.replace(/__remainingSum__/g, formatMoney(acceptDraft.remainingAmount));

					$("#draftAmountInfo").append(html);
					$("#draftAmountInfo").show();
				}
			}

		});

	});
	
	//点击撤回
	$(document).on("click","#withdraw",function(){
		cbms.getDialog("撤回已分配的资金","#withdrawForm");
	});

	//取消按钮
    $(document).on("click","button[name='cancel']",function(){
        cbms.closeDialog();
    });
    
    //资金分配保存
    $(document).on("click","#saveFundAllocations",function(){

		if (!setlistensSave("#FPZJform"))return false;

    	var div = $(this).closest("#dialogContBox");
    		var inputValue = parseFloat($(div).find("#sum").val().replace(/\s+/g, ""));
        	var value = parseFloat($(div).find("#totalSum").text().split(',').join(""));

        	var accounts=[];

			if("" == $("#allocationTypeSelect").val()){
				cbms.alert("请选择分配类型");
				return false;
			}

        	if(isNaN($(div).find("#sum").val().replace(/\s+/g, ""))){
        		cbms.alert("请输入正确的金额");
        		return false;
        	}
    		if(inputValue<=0){
    			cbms.alert("金额需大于0");
    		}else if(inputValue>value){
    			cbms.alert("金额不能大于账户总金额");
    		}else if($("#allocationTypeSelect").find("option:selected").val() == 1 && $(div).find(".remainingCashSum").size()>0 && inputValue > $(div).find(".remainingCashSum").html().split(',').join("")){
				cbms.alert("金额不能大于现金剩余可分配总金额");
			}else if($("#allocationTypeSelect").find("option:selected").val() == 2 && "" == $("#draftCodeSelect").find("option:selected").val()){
				cbms.alert("银票号不能为空，请选择银票号");
			}else if($("#allocationTypeSelect").find("option:selected").val() == 2 && inputValue > $(div).find(".remainingSum").html().split(',').join("")){
				cbms.alert("金额不能大于银票剩余可分配总金额");
			}else if(inputValue>0 && inputValue<=value){
    			cbms.confirm("分配"+formatMoney($(div).find("#sum").val().replace(/\s+/g, ""))+"元到"+$(div).find("#departmentId option:selected").text()+",分配后资金将无法回调，确定操作吗?",null,function(){
    				var data=[];
					//银票资金分配
					if(2 == $("#allocationTypeSelect").find("option:selected").val()){
						data.push({accountId:$(div).find("#FPZJform").attr("accountId"),balance:-inputValue,
							associationType:"MONEYALLOCATION",accountTransApplyType:"COMPANYMONEY_TRANSTO_DEPART",isDraftAllocation:true});
						data.push({accountId:$(div).find("#departmentId option:selected").attr("departmentid"),balance:inputValue,associationType:"MONEYALLOCATION",
							accountTransApplyType:"CHARGE",isDraftAllocation:true,acceptDraftId:$("#draftCodeSelect").find("option:selected").val(),
							acceptDraftAmount:inputValue});
					}
					//现金分配
					else{
						data.push({accountId:$(div).find("#FPZJform").attr("accountId"),balance:-inputValue,
							associationType:"MONEYALLOCATION",accountTransApplyType:"COMPANYMONEY_TRANSTO_DEPART",isDraftAllocation:false});
						data.push({accountId:$(div).find("#departmentId option:selected").attr("departmentid"),balance:inputValue,associationType:"MONEYALLOCATION",
							accountTransApplyType:"CHARGE", isDraftAllocation: false});
					}

        			accounts.push(data);
        			saveFunc(JSON.stringify(accounts),$(div).find("#FPZJform").attr("accountId"),"allocations");
    			});
    		}else{
    			$(div).find("#sum").focus();
    		}	
    });
    
    //撤回已分配资金保存
    $(document).on("click","#saveWithdraw",function(){

		if (!setlistensSave("#CHform"))return false;

    	var div = $(this).closest("#dialogContBox");
    	var isSave;
    	var isZero=false;
    	var accounts=[];
    	$(div).find("input[name='departmentSum']").each(function(i,e){
    		var inputValue = parseFloat($(e).val().replace(/\s+/g, ""));
    		var value = parseFloat($(e).closest("div").find(".treeBtnBar").html().split(',').join(""));
    		if(isNaN($(e).val().replace(/\s+/g, ""))){
    			isSave=0;
    			cbms.alert("请输入正确的金额");
    			return false;
    		}
    		if(inputValue > value){
    			isSave=1;
    		}else if(0 < inputValue && inputValue <= value){
    			isSave=2;
    			var data=[];
    			data.push({accountId:$(div).find("#CHform").attr("accountId"),balance:inputValue,associationType:"MONEYBACK",
    				accountTransApplyType:"DEPARMONEY_BACKTO_COMPANY"});
    			data.push({accountId:$(e).attr("departmentid"),balance:-inputValue,associationType:"MONEYBACK",
    				accountTransApplyType:"DEPARMONEY_BACKTO_COMPANY"});
    			accounts.push(data);
    		}else if(inputValue<0){
    			isZero=true;
    		}
    	});
    	if(isSave==1){
    		cbms.alert("金额不能大于部门账户总金额");
    	}else if(isSave==2){
    		if(isZero){
    			cbms.alert("金额需大于0");
    		}else{
    			saveFunc(JSON.stringify(accounts),$(div).find("#CHform").attr("accountId"),"withdraw");
    		}
    	}	
    });

	//信用额度抵扣或还款
	$(document).on("click","#creditPay",function(){
		var dialog;
		var departmentId = _accountInfoAttr.deptId;
		var departmentInfo = $("#dAccountInfo_" + departmentId);
		if($(this).hasClass("deduction")){
			dialog = "信用额度抵扣二结欠款";
			$("#creditDialogForm #creditConfirm").attr("option", "deduction");
			$("#creditDialogForm .restitution").addClass("none");
			$("#creditDialogForm .deduction").removeClass("none");
			$("#creditDialogForm #creditBalance").html($(departmentInfo).find("#creditBalance").html());
		}else if($(this).hasClass("restitution")){
			dialog = "二结余额还款信用额度";
			$("#creditDialogForm #creditConfirm").attr("option", "restitution");
			$("#creditDialogForm .deduction").addClass("none");
			$("#creditDialogForm .restitution").removeClass("none");
			$("#creditDialogForm #creditUsed").html($(departmentInfo).find("#creditUsed").html());
		}
		$("#creditDialogForm form").attr("departmentId", departmentId);
		$("#creditDialogForm #balanceSecondSettlement").html($(departmentInfo).find("#balanceSecondSettlement").html());
		cbms.getDialog(dialog, $("#creditDialogForm").html());
	});

	$(document).on("click", "#creditConfirm", function(){
		var form = $(this).closest("form");
		var inputValue = $(form).find("#sum").val().replace(/,/g,'');   //输入的金额
		var pattern = /^\d+(\.\d{1,2})?$/;    //正数且最多两位小数
		if(!pattern.test(inputValue) || inputValue == 0){
			cbms.alert("金额只能为正数且最多两位小数");
			return false;
		}
		inputValue = inputValue * 1;
		var option = $(this).attr("option"), maxValue;
		var secondary = Math.abs($(form).find("#balanceSecondSettlement").html().replace(/,/g,'') * 1);
		if(option == "restitution") {
			var creditUsed = Math.abs($(form).find("#creditUsed").html().replace(/,/g,'') * 1);
			maxValue = Math.min(secondary, creditUsed);
		}else if(option == "deduction"){
			var creditBalance = Math.abs($(form).find("#creditBalance").html().replace(/,/g,'') * 1);
			maxValue = Math.min(secondary, creditBalance);
		}
		if(maxValue < inputValue){
			cbms.alert("请输入正确的金额");
			return false;
		}
		creditFunc($(form).attr("departmentId"), $(form).find("#sum").val(), $(this).attr("option"));
	});
    
    //信用总额弹框qianxinzi20160301
	$(document).on('click','.opi',function(){
		var title = '信用总额';
		message = "<div class='well ' style='width:350px;margin-top:25px;'>"+
			"<h5>建材部门</h5>"+
			"<div><span class='wid inline-block'>信用总额：</span><span>40000.00元</span></div>"+
			"<div><span class='wid inline-block'>已使用额度：</span><span>0.00元</span></div>"+
			"<div><span class='wid inline-block'>信用可用额度：</span><span>40000.00元</span></div>"+
			"<h5>建材部门</h5>"+
			"<div><span class='wid inline-block'>信用总额：</span><span>40000.00元</span></div>"+
			"<div><span class='wid inline-block'>已使用额度：</span><span>0.00元</span></div>"+
			"<div><span class='wid inline-block'>信用可用额度：</span><span>40000.00元</span></div>"+
			"</div>";
		cbms.getDialog(title,message);
	});
});

function loadAcceptDraftInfo(){
	//设置 账户银票信息全局变量值
	$.ajax({
		type: "POST",
		async : false,
		url: Context.PATH + "/accountinfo/getAcceptDrafts.html?accountId=" + $("#accountId").val(),
		success: function(result){
			_acceptDraftList = result.data;
		}
	});
}

//二次结算页面跳转
$(document).on("click","#settlementBtn",function(){
	var departmentId = $(".ck li.acti a").attr("val");
	location.href = Context.PATH + "/accountinfo/"+departmentId+"/settlementpayabledetail.html";
});

/**
 * 保存资金分配或撤回
 * @param accountList
 * @param user
 */
function saveFunc(accountList,accountId,type) {
	$.ajax({
        type : "POST",
        url : Context.PATH + "/accountinfo/saveSum.html",
        data :{accountList:accountList,
        	   accountId:accountId},
        dataType : "json",
        success : function(response) {
            if (response.success) {
            	if(type=="allocations"){
            		cbms.alert("分配成功！"); 
            	}else{
            		cbms.alert("已成功撤回已分配的资金！"); 
            	}
                
                cbms.closeDialog();
                var accountList = response.data;
                $(accountList).each(function(i,e){
                	$("span[departmentId="+e.id+"]").text(formatMoney(e.balance));
					$("#dAccountInfo_" + e.id).find("#creditUsed").text(formatMoney(e.creditAmountUsed));
					$("#dAccountInfo_" + e.id).find("#creditBalance").text(formatMoney(e.creditAmount - e.creditAmountUsed));
            	});
				reloadList();
				loadAcceptDraftInfo();
            } else {
                cbms.alert(response.data);
            }
        },
        error : function(xhr, textStatus, errorThrown) {}
    });
}

/**
 * 信用额度还款或抵扣
 * @param accountList
 * @param user
 */
function creditFunc(accountId, amount, option) {
	$.ajax({
		type : "POST",
		url : Context.PATH + "/accountinfo/payForCredit.html",
		data :{
			accountId:accountId,
			amount: amount,
			option: option
		},
		dataType : "json",
		success : function(response) {
			if (response.success) {
				if(option=="restitution"){
					cbms.alert("成功还款信用额度" + amount + "元！");
				}else if(option=="deduction"){
					cbms.alert("成功抵扣二结欠款" + amount + "元！！");
				}
				cbms.closeDialog();
				var accountInfo = response.data;
				$("#dAccountInfo_" + accountInfo.id).find("#balanceSecondSettlement").html(formatMoney(accountInfo.balanceSecondSettlement));
				$("#dAccountInfo_" + accountInfo.id).find("#creditUsed").html(formatMoney(accountInfo.creditAmountUsed));
				$("#dAccountInfo_" + accountInfo.id).find("#creditBalance").html(formatMoney(accountInfo.actualCreditBalance));
				if (accountInfo.balanceSecondSettlement == 0) {
					$("#dAccountInfo_" + accountInfo.id).find("#creditPay").addClass("none");
				} else if (option == "restitution" && accountInfo.creditAmountUsed == 0) {
					$("#dAccountInfo_" + accountInfo.id).find("#creditPay").attr("disabled", "disabled");
				} else if (option == "deduction" && accountInfo.actualCreditBalance == 0) {
					$("#dAccountInfo_" + accountInfo.id).find("#creditPay").attr("disabled", "disabled");
				}
				reloadList();
			} else {
				cbms.alert(response.data);
			}
		},
		error : function(xhr, textStatus, errorThrown) {}
	});
}

/**
 * 重新加载列表
 */
function reloadList(){
	isShowTableColumns();
	cbms.loading();
	_accountInfoAttr.oTable.api().ajax.reload(function(){
		cbms.closeLoading();
//		doOtherThingsAfterReload();
	},false);
}

/**
 * 多部门的公司流水隐藏二结、信用额度4列，否则显示
 */
function isShowTableColumns(){
	//var oTable = $('#dynamic-table').DataTable();
	if(typeof($(".ck li.acti a").attr("val"))=="undefined"){
		_accountInfoAttr.oTable.api().column(6).visible(false);
		_accountInfoAttr.oTable.api().column(7).visible(false);
		_accountInfoAttr.oTable.api().column(8).visible(false);
		_accountInfoAttr.oTable.api().column(9).visible(false);
		/*_accountInfoAttr.oTable.fnSetColumnVis( 6, false);
		_accountInfoAttr.oTable.fnSetColumnVis( 7, false);
		_accountInfoAttr.oTable.fnSetColumnVis( 8, false);
		_accountInfoAttr.oTable.fnSetColumnVis( 9, false);*/
	}else{
		/*_accountInfoAttr.oTable.fnSetColumnVis( 6, true);
		_accountInfoAttr.oTable.fnSetColumnVis( 7, true);
		_accountInfoAttr.oTable.fnSetColumnVis( 8, true);
		_accountInfoAttr.oTable.fnSetColumnVis( 9, true);*/
		_accountInfoAttr.oTable.api().column(6).visible(true);
		_accountInfoAttr.oTable.api().column(7).visible(true);
		_accountInfoAttr.oTable.api().column(8).visible(true);
		_accountInfoAttr.oTable.api().column(9).visible(true);
	}
}

/**
 * 日期转换
 * @param data
 * @returns {String}
 */
function formatDay(data) {
    var dt = new Date(data);
    var time = dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate() + " " +
        ((dt.getHours()/1)<10?("0"+dt.getHours()):dt.getHours()) + ":" + ((dt.getMinutes()/1)<10?("0"+dt.getMinutes()):dt.getMinutes()) + ":" + ((dt.getSeconds()/1)<10?("0"+dt.getSeconds()):dt.getSeconds());
    return time;
}
//导出EXCEL
function exportExcel(){
     var form = $("<form>");
        form.attr('style', 'display:none');
        form.attr('target', '');
        form.attr('method', 'post');
        form.attr('action', Context.PATH + "/accountinfo/exportexcel.html");
        // 客户ID
        var input1 = $('<input>');
        input1.attr('type', 'hidden');
        input1.attr('name', 'accountId');
        input1.attr('value', $("#accountId").val());
        // 流水开始时间
        var input2 = $('<input>');
        input2.attr('type', 'hidden');
        input2.attr('name', 'startTime');
        input2.attr('value', $("#startTime").val());
        // 流水结束时间
        var input3 = $('<input>');
        input3.attr('type', 'hidden');
        input3.attr('name', 'endTime');
        input3.attr('value', $("#endTime").val());
        // 关联单号
        var input4 = $('<input>');
        input4.attr('type', 'hidden');
        input4.attr('name', 'consignOrderCode');
        input4.attr('value', $("#consignOrderCode").val());
        // 类型
        var input5 = $('<input>');
        input5.attr('type', 'hidden');
        input5.attr('name', 'applyType');
        input5.attr('value', $("#applyType").val());
        // 部门Id
        var input6 = $('<input>');
        input6.attr('type', 'hidden');
        input6.attr('name', 'departmentId');
        input6.attr('value',_accountInfoAttr.deptId);

        $('body').append(form);
        form.append(input1);
        form.append(input2);
        form.append(input3);
        form.append(input4);
        form.append(input5);
        form.append(input6);

        form.submit();
        form.remove();
}

//查看额度分组
$(document).on("click", "#limitManager", function () {
    cbms.getDialog("信用额度组详情", Context.PATH + '/account/grouping/get/accountGroup.html?id='+_accountInfoAttr.deptId+''+"&");
});
