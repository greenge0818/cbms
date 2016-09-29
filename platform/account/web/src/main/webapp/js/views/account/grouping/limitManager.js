/**
 * Created by lixiang on 2016/04/11.
 * 审核额度列表
 */

var dt;
jQuery(function ($) {
    initTable();
    //搜索事件
    $("#searchBtn").click(function () {
        dt.ajax.reload();
    });
	
});




function initTable() {
	$("#addGroupInfo").hide();
	$("#ckeckLimit").addClass("active").find("a").attr("href", "javascript:void(0);");
    var url = Context.PATH + "/account/grouping/query/limitManager.html";
    dt = $("#dynamic-table").DataTable({
        ajax: {
            url: url,
            type: "POST",
            data: function (d) {
                d.accountName = $("#accountName").val();
                d.groupInfoName = $("#groupingName").val();
            }
        },
        serverSide: true, //服务模式
        processing: true,//显示数据加载进度
        searching: false, //是否启用搜索
        ordering: false, //是否启用排序
        lengthChange: false, //不显示pageSize的下拉框
        oLanguage: {sUrl: Context.PATH + "/js/DT_zh.txt"}, //自定义语言包
        bFilter: false,
        bInfo: false,
        iDisplayLength: 15,
        bLengthChange: false, //不显示每页长度的选择条
        bPaginate: false,  //不显示分页器
        columns: [
	        {defaultContent: '',sWidth:'30px'},   //序号
	        {defaultContent: ''},  //组名称
	        {defaultContent: ''}, //组信用额度
	        {data: 'num'},  //客户数
	        {data: 'groupInfoStatus'}, //状态
	        {defaultContent: ''} //操作
        ],
        columnDefs: [
             {
                 "targets": 4, //第几列 从0开始
                 "data": "groupInfoStatus",
                 "render": getStatus
             }        
        ],
        fnRowCallback: function (nRow, aData, iDataIndex) {
        	$('td:eq(0)', nRow).html("<i class='fa fa-lg fa-angle-up'></i><span style='padding-left:15px;'>" + (iDataIndex + 1) + "</span>");
        	var custSerials = new Array();//定义客户流水号数组
        	for (var i=0; i<aData.companyList.length; i++) {
        		custSerials.push(aData.companyList[i].serial);
        	}
        	var link = "";
        	if (aData.groupInfoStatus == "REQUESTED") {
        		$('td:eq(1)', nRow).html(aData.groupNameAudit);
        		link = "<a href='javascript:void(0)' class='group_agree' groupId="+aData.groupId+" group_serial="+aData.groupInfoSerial+" cust_serial="+custSerials+">同意</a>      <a href='javascript:void(0)' class='group_not_agree' groupId="+aData.groupId+" group_serial="+aData.groupInfoSerial+" cust_serial="+custSerials+">不同意</a>";
        	} else if (aData.groupInfoStatus == "APPROVED") {
        		$('td:eq(1)', nRow).html(aData.groupName);
        		link = '-';
        	}
        	$('td:eq(-1)', nRow).html(link);
        	$('td:eq(2)', nRow).html('<span class="groupLimit" value='+aData.groupLimit+'>'+formatMoney(aData.groupLimit,2)+'</span>').addClass("text-right");   
        },
        fnDrawCallback:function(aaData){//fnInitComplete：datatables初始化完毕后
        	$("#databody").find("tr").each(function(index,obj){//循环遍历公司信息
        		if (aaData.aoData.length == 0) {
        			return;
        		}
        		var aData = aaData.aoData[index]._aData;
            	$(obj).attr("group",aData.groupId);//给table的tr设置属性（组id）
//        		var header=$("#templateheader").clone().removeAttr("id").addClass("none");
//        		header.attr("groupid",aData.groupId);//给表头设置属性和table的一致
//        		$(obj).after(header);
//				for(var i=0 ;i<aData.companyList.length;i++){
//					var link = "<a href='javascript:void(0)' class='receipt_view'>修改</a> <a href='javascript:void(0)' class='commit_view none'>确定</a>";
//	        		var subrow=$("#templatetr").clone().removeAttr("id").addClass("none");
//	        		subrow.attr("groupid",aData.groupId);//给td设置属性
//	        		var tds = subrow.find("td");
//	        		var custName = aData.companyList[i].accountName;
//	        		if (aData.companyList[i].departmentCount > 1) {
//	        			custName = custName+"【"+aData.companyList[i].departmentName+"】"
//	        		}
//	        		tds.eq(1).text(custName);
//	        		tds.eq(3).text(aData.companyList[i].userName);
//	        		tds.eq(4).text(aData.companyList[i].status =="REQUESTED" ? "待审核" : (aData.companyList[i].status =="APPROVED") ? "审核通过" : "审核不通过");	
//	        		if (aData.companyList[i].status == "REQUESTED") {
//	        			tds.eq(2).html('<span class="amount">'+formatMoney(aData.companyList[i].creditLimitAudit)+'</span> <input type="text" companyid='+aData.companyList[i].id+' value='+aData.companyList[i].creditLimitAudit+'  name="fname" class="insertAmount none pull-right" />').addClass("text-right");
//	        			tds.eq(5).html(link);
//	        		} else {
//	        			tds.eq(2).text(formatMoney(aData.companyList[i].creditLimit)).addClass("text-right");
//	        			tds.eq(5).text('-');
//	        		}
//	        		header.after(subrow);
//				}
        		var childTable = $("#account_group").clone().removeClass("none").removeAttr("id");//移除ID并显示
                InitChildBlock(aData, childTable);
                var totalcolumns = aaData.aoColumns.length;
            	var html="<tr groupid='" + aData.groupId + "'><td colspan='" + totalcolumns + "' subid='22' class=''>"+childTable.prop("outerHTML")+"</td></tr>";
            	$(obj).after(html);
        	});      
        }
    });
    
    $(document).on("click", "#dynamic-table tr[group]", function () {
        var groupid = $(this).attr("group");
        var subtr = $("#databody tr[groupid=" + groupid + "]");//将属性一致的隐藏
        if (subtr.hasClass('none')) {
            subtr.removeClass('none');
            $(this).find(".fa-angle-down").removeClass("fa-angle-down").addClass("fa-angle-up");
        }
        else {
            subtr.addClass('none');
            $(this).find(".fa-angle-up").removeClass("fa-angle-up").addClass("fa-angle-down");
        }
    });
}


function InitChildBlock(jsonData, childTable) {
    childTable.DataTable({
        aaData: jsonData.companyList,
        columns: [
            {defaultContent: ''},
            {defaultContent: ''},
            {defaultContent: ''},
            {defaultContent: ''},
            {data: 'status'},
            {defaultContent: ''}
        ],
        columnDefs: [
            {
                "targets": 4, //第几列 从0开始
                "data": "status",
                "render": getStatus
            }
        ],
        bPaginate: false,  //不显示分页器
        bLengthChange: false, //不显示每页长度的选择条
        bProcessing: false, //不显示"正在处理"
        bPaginate: false,
        bInfo: false,
        bFilter: false,
        bSort: false,
        fnRowCallback: function (nRow, aData, iDataIndex) {
        	$(nRow).attr("groupid",aData.groupInfoId);//给表头设置属性和table的一致
        	var link = "<a href='javascript:void(0)' class='receipt_view'>修改</a> <a href='javascript:void(0)' class='commit_view none'>确定</a>";
            $('td:eq(0)', nRow).html(iDataIndex + 1);
    		var custName = aData.accountName;
    		if (aData.departmentCount > 1) {
    			custName = custName+"【"+aData.departmentName+"】"
    		}
    		$('td:eq(1)', nRow).html(custName);
    		$('td:eq(3)', nRow).html(aData.userName);
    		$('td:eq(4)', nRow).html(aData.status =="REQUESTED" ? "待审核" : (aData.status =="APPROVED") ? "审核通过" : "审核不通过");	
    		if (aData.status == "REQUESTED") {
    			$('td:eq(2)', nRow).html('<span class="amount">'+formatMoney(aData.creditLimitAudit)+'</span> <input type="text" verify="rmb" companyid='+aData.id+' value='+aData.creditLimitAudit+'  name="fname" class="insertAmount none pull-right" />').addClass("text-right");
    			$('td:eq(5)', nRow).html(link);
    		} else {
    			$('td:eq(2)', nRow).text(formatMoney(aData.creditLimit)).addClass("text-right");
    			$('td:eq(5)', nRow).html('-');
    		}
        }
    });
}


//点击修改按钮，修改客户额度值
$(document).on("click", ".receipt_view", function () {
	var closesttr = $(this).closest("tr");
	closesttr.find(".amount").addClass("none");//找到对应的父节点找到对应的span标签 隐藏金额 设置属input性框
	closesttr.find(".insertAmount").removeClass("none").addClass("show");//显示input框修改额度
	closesttr.find(".receipt_view").addClass("none");//隐藏修改按钮
	closesttr.find(".commit_view").addClass("show");//显示确定按钮
	closesttr.find(".receipt_view").removeClass("show");//移除修改按钮show属性
});

//点击确定后，到后台修改客户额度
$(document).on("click", ".commit_view", function () {
	var closesttr = $(this).closest("tr");
	var companyid = closesttr.find(".insertAmount").attr("companyid");//拿到客户关联表id
	var groupid = closesttr.attr("groupid");//取到当前行的groupid属性值
	var groupLimit = $("#databody tr[group='"+groupid+"'] .groupLimit").attr("value");//取到当前组的组信用额度
	var newCreditLimit = closesttr.find(".insertAmount").val();//修改后的额度
	var forms = setlistensSave("#createForm");
	if (!forms)return;
	if (newCreditLimit == "") {
		closesttr.find(".insertAmount").attr("title", "请输入要修改的金额");
		closesttr.find(".insertAmount").attr("data-original-title", "请输入要修改的金额");
		closesttr.find(".insertAmount").tooltip({
            trigger: 'focus'
        });
		closesttr.find(".insertAmount").focus();
        return false;
	}
	if (parseFloat(newCreditLimit) > parseFloat(groupLimit)) {
		cbms.alert("设置的公司信用额度最大值不能超过组信用额度！");
		return;
	}
	//点击确定后 到后台改变修改后的金额
	$.ajax({
        url: Context.PATH + '/account/grouping/update/cust/creditLimit.html',
        type: "POST", 
        data : {
			"custId" : companyid,
			"creditLimit" : newCreditLimit
		},
        success: function (result) {
    		if (result.success) {
    			closesttr.find(".commit_view").removeClass("show").addClass("none");//移除确定按钮show属性,加上确定按钮none属性
    			closesttr.find(".receipt_view").removeClass("none").addClass("show");//显示修改按钮;//移除确定按钮隐藏属性
    			closesttr.find(".insertAmount").removeClass("show").addClass("none");//去掉显示input的show属性
    			closesttr.find(".amount").removeClass("none");//去掉信用额度none属性
    			closesttr.find(".amount").text(formatMoney(newCreditLimit));//显示修改后的信用额度
    		} else {
    			cbms.alert(result.data);
    		}
        }
    });

});

function audit(groupSerial, custSerials, groupId, status) {
	$.ajax({
        url: Context.PATH + '/account/grouping/audit/creditlimit.html',
        type: "POST", 
        data : {
			"groupId" : groupId,
			"groupSerial" : groupSerial,
			"custSerials" : custSerials,
			"status" : status
		},
        success: function (result) {
    		if (result.success) {
    			cbms.closeDialog();
    			cbms.alert(result.data, function() {
    				location.href = Context.PATH + "/account/grouping/limitManager.html";
    			});
    		} else {
    			cbms.closeDialog();
    			cbms.alert(result.data);
    		}
        }
    });
}

//同意操作
$(document).on("click", ".group_agree", function ()  { 
	var groupSerial = $(this).attr("group_serial");//组流水号
	var custSerials = $(this).attr("cust_serial");//组下客户的流水号
	var groupId = $(this).attr("groupId");//组id
	var status = "APPROVED";
	
	var html = $('#determine').html();
	cbms.getDialog("提示信息", html);
	$("#span_text").text("确认同意该信用组内公司信用额度？");
	$(document).on("click", "#btnClose",function () {
       cbms.closeDialog();
    });
	 
	$(document).off('click', '#btncommit');
		
	$(document).on('click', '#btncommit', function() {
		audit(groupSerial, custSerials, groupId, status);
	});
	
});

//不同意操作
$(document).on("click", ".group_not_agree", function ()  { 
	var groupSerial = $(this).attr("group_serial");//组流水号
	var custSerials = $(this).attr("cust_serial");//组下客户的流水号
	var groupId = $(this).attr("groupId");//组id
	var status = "DECLINED";
	
	var html = $('#determine').html();
	cbms.getDialog("提示信息", html);
	$("#span_text").text("确认不同意该信用组内公司信用额度？");
	
	$(document).on("click", "#btnClose",function () {
       cbms.closeDialog();
    });
	 
	$(document).off('click', '#btncommit');
	
	$(document).on('click', '#btncommit', function() {
		audit(groupSerial, custSerials, groupId, status);
	});
	
});

function getStatus(data) {
	var status = "";
	if (data == "REQUESTED") {
		status = "待审核";
	} else if (data == "APPROVED") {
		status = "审核通过";
	} else if (data == "DECLINED") {
		status = "审核不通过";
	} 
	return status;
}

function dTScrollFun() {
	var  xTW = $(".page-content").width();
	$(".sTWidth").width(xTW);
	//$(".department-box").width(xTW);
	$(".leftWidth").width(xTW-80);

}

$(window).resize(function(){
	dTScrollFun();
})
