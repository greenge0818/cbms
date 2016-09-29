var PurchasePage = new function() {
    this.dt;
    tabIndex;
}
function orderHanlded() {
    var menu = getUrlParam('menu');
    var url = Context.PATH + "/order/query/" + menu + ".html";
    location.replace(url);
}
//获取url中的参数
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]" +
    "*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return decodeURIComponent(r[2]); return null; //返回参数值
}

$(document).ready(function() {
	//add lixiang 判断从控制台跳转过来的后缀，如果有则解析
	var curtabIndex= getUrlParam("tabIndex");
	if (curtabIndex !=null && curtabIndex!='') {
		tabIndex=curtabIndex;
	}
	if(!utils.isEmpty(tabIndex)){		
		var index = parseInt(tabIndex);
		PurchasePage.tabIndex=index;
		$("#myTab4 li").removeClass("active");
        $("#myTab4").find("li input[value='"+index+"']").parent().addClass("active")
		changeTabClass();
	}
	//如果初始化后没有默认选择的TAB页则设置默认选择页
	var active_tab = $("#myTab4 li.active input").val();
	if(typeof(active_tab) == "undefined"){
		var arry = $("#myTab4 li");
		$(arry[0]).addClass("active");//默认选择第一个TAB页
		PurchasePage.tabIndex=$("#myTab4 li.active input").val();
	}
	
	$(document).on("click","#myTab4 li a",function(){
		$("#myTab4 li").removeClass("active");
		$(this).closest("li").addClass("active");
		PurchasePage.tabIndex=$("#myTab4 li.active input").val();
		$("#status").empty();
		PurchasePage.dt.ajax.reload();
		changeTabClass();
		if(0 == PurchasePage.tabIndex){
			$("#status").append('<option value="-1">全部</option>');
			$("#status").append('<option value="PENDING_ACCEPTE">待受理</option>');
			$("#status").append('<option value="PENDING_QUOTE">待报价</option>'); 
			$("#status").append('<option value="PENDING_CLERK_ACCEPTE">待业务员处理</option>'); 
			$("#status").append('<option value="QUOTED">已报价</option>'); 
		}else if(1 == PurchasePage.tabIndex){
			$("#status").append('<option value="-1">全部</option>');
			$("#status").append('<option value="PENDING_DIRECTOR_ASSIGNED">待网销主管分配</option>');
			$("#status").append('<option value="PENDING_SERVER_MANAGER_ASSIGNED">待服总分配</option>'); 
		}else if(2 == PurchasePage.tabIndex){
			$("#status").append('<option value="-1">全部</option>');
			$("#status").append('<option value="BILLED">已开单</option>');
			$("#status").append('<option value="CLOSED">已关闭</option>'); 
		}else{
			$("#status").append('<option value="-1">全部</option>');
			$("#status").append('<option value="PENDING_OPEN_BILL">待开单</option>');
		}
    });
	
    searchClick();
    var url = Context.PATH + "/smartmatch/purchaseorder/search.html";
    PurchasePage.dt = jQuery("#dynamic-table").DataTable({
        "processing" : false,
        "serverSide" : true,
        "searching" : false,
        "ordering" : false,
        "paging" : true,
        "bAutoWidth" : false,
        "iDisplayLength":50,
        "aLengthMenu": [10,30,50,100],//定义每页显示数据数量
        "ajax" : {
            "url" : url,
            "type" : "POST",
            data : function(d) {
                return $.extend({}, d, {
                    code:$("#code").val(),
                    buyerName : $("#buyerName").val(),
                    categoryName : $("#categoryName").val(),
                    deliveryName : $("#deliveryName").val(),
                    status : $("#status").children('option:selected').val(),
                    // orgName : $("#sorganization").val(),
                    startTime : $("#startTime").val(),
                    tabIndex:PurchasePage.tabIndex,
                    endTime : $("#endTime").val(),
                    requirementCode : $("#requirementCode").val()
                });
            }
        },
        columns : [
            {data : 'code'},
            {data : 'createdTime'},
            {data : 'requestCode'},
            {data : 'buyerName'},
            {data : 'contact'},
            {data : 'tel'},
            {data : 'categoryName'},
            {data : 'deliveryName'},
            {data : 'accepter'},
            {data : 'lastUpdatedBy'},
            {data : 'status'},
            {defaultContent : ''}
        ],
        drawCallback:function(e,t) {
        	var status=$("#myTab4 li.active input").val();
        	hideclumns(8);//隐藏受理人
        	hideclumns(9);//隐藏操作人
        	if(status==1){//待指派
        		showclumns(8);//显示受理人
        	}else if(status==2){//已完成
        		showclumns(9);//显示操作人
        	}
        },
        columnDefs: [
			{
			    "targets": 0, //第几列 从0开始
			    "data": "code",
			    "render": function (data, type, full, meta) {
			        //var url = Context.PATH + "/smartmatch/purchaseorder/"+full.id+"/inquiryorderdetail.html";
			        var url = Context.PATH + "/smartmatch/purchaseorder/created.html?id="+full.id+"&isDetail=1";
			        return "<a href='" + url + "'>" + data + "</a>";
			    }
			},
            {
                "targets": 1, //第几列 从0开始
                "data": "createdTime",
                "render": renderTime
            },
            {
                "targets": 11, //第几列 从0开始
                "data": "status",
                "render": renderOperation
            }

        ]
    });
});

function hideclumns(index){
	$("#dynamic-table th:eq("+index+")").hide();
	$("#dynamic-table tr").each(function(){
		$(this).find("td:eq("+index+")").hide();
	});
}

function showclumns(index){
	$("#dynamic-table th:eq("+index+")").show();
	$("#dynamic-table tr").each(function(){
		$(this).find("td:eq("+index+")").show();
	});
}

//清空按钮
$(document).on("click","#cleanSearch",function(){
	resetForm($("form.form-inline"));
	$("#status").val(-1);
});

function searchClick(){
    jQuery("#searchList").on(ace.click_event, function() {
        searchData(true);
    });
}

function searchData(isNewSearch) {
	if(isNewSearch){
		PurchasePage.dt.ajax.reload();
	}else{
		PurchasePage.dt.ajax.reload(null, false);
	}
}

function renderTime(data, type, full, meta){
    return date2String(new Date(data));
}

//js将Date类型转换为String类型：
function date2String(aDate){
    var year=aDate.getFullYear();
    var month=aDate.getMonth();
    month++;
    var mydate=aDate.getDate();
    var hour=aDate.getHours();
    var minute=aDate.getMinutes();
    var mytime=year+"-"+(month<10?"0":"")+month+"-"+(mydate<10?"0":"")+mydate+" "+(hour<10?"0":"")+hour+":"+(minute<10?"0":"")+minute;
    return mytime;
}

function changeTabClass(){
	var status=$("#myTab4 li.active input").val();
	//0,待处理，1待指派，2，已完成
	if(status==='0'){
		
	}
}

function renderOperation(data, type, full, meta) {
    var html = '';
    var index=$("#myTab4 li.active input").val();
    if(data == '待受理'){
        html += '<a  href="' + Context.PATH + '/smartmatch/purchaseorder/created.html?id='+full.id+'&status=PENDING_ACCEPTE">受理</a>&nbsp;&nbsp;&nbsp';
        html +=  '<a  href="javascript:void(0);" option="reassign" orderid="'+full.id+'">推给主管</a>&nbsp;&nbsp;&nbsp;'
    }
    if(data == '待报价'){
        html +=  '<a  href="' + Context.PATH + '/smartmatch/purchaseorder/created.html?id='+full.id+'">继续询价</a>&nbsp;&nbsp;&nbsp;'
        html +=  '<a  href="javascript:void(0);" option="reassign" orderid="'+full.id+'">推给主管</a>&nbsp;&nbsp;&nbsp;'
        html +=  '<a  href="javascript:void(0)" class="closeOrder" orderId="'+full.id+'">关闭</a>';
    }
    if(data == '已报价'){
    	html +=  '<a  href="' + Context.PATH + '/smartmatch/purchaseorder/created.html?id='+full.id+'">继续询价</a>&nbsp;&nbsp;&nbsp;'

        html +=  '<a  href="javascript:void(0);" option="reassign" orderid="'+full.id+'">推给主管</a>&nbsp;&nbsp;&nbsp;'
       // html +=  '<a  href="javascript:void(0)" class="openOrder" pid="'+full.id+'" requestCode="'+full.requestCode+'" >开单</a>';
        html +=  '<a  href="javascript:void(0)" class="closeOrder" orderId="'+full.id+'">关闭</a>';
    }
    if(data == '待业务员处理'){

    	html +=  '<a  href="' + Context.PATH + '/smartmatch/purchaseorder/created.html?id='+full.id+'&isDetail=1">查看详情</a>&nbsp;&nbsp;&nbsp;'
        if (index == "0") {
            html +=  '<a  href="javascript:void(0);" option="reassign" orderid="'+full.id+'">推给服总</a>&nbsp;&nbsp;&nbsp;'
            html +=  '<a  href="javascript:void(0)" class="openOrder" pid="'+full.id+'" requestCode="'+full.requestCode+'" >开单</a>&nbsp;&nbsp;&nbsp;';
            if(canAssignToBusi){
                html +=  '<a  href="javascript:void(0);" option="assigntobusi" orderid="'+full.id+'" status="'+data+'">推送业务</a>&nbsp;&nbsp;&nbsp;'
            }
            if(canAssignToSale) {
                html += '<a  href="javascript:void(0);" option="assigntosale" orderid="' + full.id + '" preownerid="' + full.preOwnerId + '" status="' + data + '" accepter= "'+full.accepterId+'" >回退</a>&nbsp;&nbsp;&nbsp;'
            }
        }
        else if(index=="1"){
            if(canAssignToBusi){
                html +=  '<a  href="javascript:void(0);" option="assigntobusi" orderid="'+full.id+'" status="'+data+'">推送业务</a>&nbsp;&nbsp;&nbsp;'
            }
            if(canAssignToSale) {
                html += '<a  href="javascript:void(0);" option="assigntosale" orderid="' + full.id + '" preownerid="' + full.preOwnerId + '" status="' + data + '" accepter= "'+full.accepterId+'" >回退</a>&nbsp;&nbsp;&nbsp;'
            }
        }
    }
    if(data == '已关闭'){
        if (canClose == "true") {
            html +=  '<a  href="javascript:void(0);" option="reactive" orderid="'+full.id+'" status="'+data+'">激活</a>&nbsp;&nbsp;&nbsp;'
        }
    }
    if(data == '待网销主管分配'||data == '待服总分配'){
      	html +=  '<a  href="' + Context.PATH + '/smartmatch/purchaseorder/created.html?id='+full.id+'&isDetail=1">查看详情</a>&nbsp;&nbsp;&nbsp;'
        if(canAssignToBusi){
            html +=  '<a  href="javascript:void(0);" option="assigntobusi" orderid="'+full.id+'" status="'+data+'">推送业务</a>&nbsp;&nbsp;&nbsp;'
        }
        if(canAssignToSale) {
            html += '<a  href="javascript:void(0);" option="assigntosale" orderid="' + full.id + '" preownerid="' + full.preOwnerId + '" status="' + data + '" accepter= '+full.accepterId+' >回退</a>&nbsp;&nbsp;&nbsp;'
        }
        html +=  '<a  href="javascript:void(0)" class="closeOrder" orderId="'+full.id+'">关闭</a>';
    }
    if(data == '已开单'){
      	html +=  '<a  href="' + Context.PATH + '/smartmatch/purchaseorder/created.html?id='+full.id+'">查看</a>&nbsp;&nbsp;&nbsp;'
      	html +=  '<a  href="javascript:void(0)" class="openOrder" id="open_ag" pid="'+full.id+'" requestCode="'+full.requestCode+'"  >再次开单</a>';

    }
    if(data == '待开单'){
      	html +=  '<a  href="' + Context.PATH + '/smartmatch/quotation/info/'+full.id+'.html">报价详情</a>&nbsp;&nbsp;&nbsp;'
      	if(quotaionAssi_permission=="true"){
      		html +=  '<a  href="javascript:void(0);" option="assigntobusi_op" orderid="'+full.id+'" status="'+data+'">指派</a>';
      	}

    }
    return html;
}
$(document).on("click",".openOrder",function(){
    var input = $(this);
    var pid = input.attr("pid");
    var action_type = input.attr("id");
    var requestCode = input.attr("requestCode");
    if(requestCode=='null'){
    	requestCode=null;
    }
    var type = null;
    if(action_type == 'open_ag'){
    	type = "F";
    }
    openOrder(pid,requestCode,type);

});
$(document).on("click",".activeOrder",function(){
    var input = $(this);
    cbms.confirm("确定要激活吗？", null, function(){
        $.ajax({
            type: 'post',
            url: Context.PATH + "/smartmatch/purchaseorder/"+ $(input).attr("orderId") +"/active.html",
            data: {},
            error: function (s) {
            },
            success: function (result) {
                if(result.success) {
                    PurchasePage.dt.ajax.reload();
                    utils.showMsg("激活成功", null, null, 2000);
                }else{
                    cbms.alert(result.data);
                }
            }
        });
    });
});

//新增询价单
$(document).on("click", "#addInquiryOrder", function(){
	var url=Context.PATH + "/smartmatch/purchaseorder/created.html";
	window.location.href=url;
});

$(document).on("click", ".closeOrder", function () {
    var input = $(this);
    var ele = '<div class="dialog-m" id="dialog">' +
        '<p><label>关闭原因　　：</label><select id="reasonSelect"><option value="0">价格不合适</option><option value="1">没找到货</option>' +
        '<option value="2">交货地不合适</option><option value="3">货不全</option><option value="4">其他</option></select></p>' +
        '<p id="reasonDiv"><label>输入其他原因：</label><textarea id="reason"></textarea></p>' +
        '<div class="btn-bar text-center"><button id="commit" type="button" class="btn btn-primary btn-sm">关闭</button>&nbsp;<button type="button" class="btn btn-default btn-sm" id="cancel">取消</button></div></div>';
    cbms.getDialog("采购单关闭", ele);
    $("#reasonDiv").hide();
    $("#reasonSelect").change(function () {
        var select = $('#reasonSelect').children('option:selected').val();
        if (select != 4) {
            $("#reasonDiv").hide();
        }else{
            $("#reasonDiv").show();
        }
    });

    $("#dialog").on("click", "#cancel", function () {
        cbms.closeDialog();
    });

    $("#dialog").on("click", "#commit", function () {
        var selected = $("#reasonSelect").children("option:selected");
        var reason;
        if($(selected).val() == 4) {
            reason = $("#reason").val();
        }else{
            reason = $(selected).html();
        }
        if(reason == ""){
            utils.showMsg("请填写关闭理由", null, "error", 2000);
            return false;
        }
        $.ajax({
            type: 'post',
            url: Context.PATH + "/smartmatch/purchaseorder/"+ $(input).attr("orderId") +"/close.html",
            data: {
                reason: reason
            },
            error: function (s) {
            },
            success: function (result) {
                if(result.success) {
                    PurchasePage.dt.ajax.reload();
                    utils.showMsg("关闭成功", null, null, 2000);
                    cbms.closeDialog();
                }else{
                    cbms.alert(result.data);
                }
            }
        });
    });

});

$(document).on("click", "#dynamic-table tr a[option=assigntobusi]", function () {
    var orderid = $(this).attr("orderid");
    $("#assign_purchase_orderid").val(orderid);
    var status=$(this).attr("status");
    if (status == "待服总分配") {
        $("#assignto_manager_panel").hide();
    }
    else{
        $("#assignto_manager_panel").show();
    }
    cbms.getDialog("推送业务","#assign_dialog");
});

var is_pd_open_bill;
$(document).on("click", "#dynamic-table tr a[option=assigntobusi_op]", function () {
    var orderid = $(this).attr("orderid");
    $("#assign_purchase_orderid").val(orderid);
    $("#assignto_manager_panel").hide();
    is_pd_open_bill = true;
    cbms.getDialog("询价单指派","#assign_dialog");
});


$(document).on("click", "#dynamic-table tr a[option=assigntosale]", function () {
    var orderid = $(this).attr("orderid");
    $("#assigntonetsale_purchase_orderid").val(orderid);
    var accepter= $(this).attr("accepter");
    cbms.getDialog("回退","#assigntonetsale_dialog");
    var preownerid=$(this).attr("preownerid");
    initTraders(preownerid);
});

$(document).on("click", "#dynamic-table tr a[option=reassign]", function () {
    var orderid = $(this).attr("orderid");
    $("#reassign_purchase_orderid").val(orderid);
    cbms.getDialog("改派理由","#reassign_dialog");
});

$(document).on("click", "#dynamic-table tr a[option=reactive]", function () {
    var orderid = $(this).attr("orderid");
    if(IsOrgManager=="true"){
        $("#reactiveto_manager_panel").hide();
        $("#reactiveto_iseller_panel").hide();
        $("#reactiveto_user_panel").show();
    }else{
        $("#reactiveto_manager_panel").show();
        $("#reactiveto_iseller_panel").show();
        $("#reactiveto_user_panel").show();
    }
    $("#reactive_purchase_orderid").val(orderid);
    cbms.getDialog("重新激活询价单","#reactive_dialog");
});

function openOrder(purchaseOrderId,requestCode,type){
	cbms.loading();
	$.ajax({
		type: 'post',
		data: {
			"purchaseOrderId":purchaseOrderId,   //询价单id
			"requestCode":requestCode   //需求单号
		},
		url: Context.PATH + "/smartmatch/purchaseorder/preopenpid.html",
		error: function (s) {
			cbms.closeLoading();
		},
		success: function (result) {
			cbms.closeLoading();
			if(result.success) {
				if(result.success){
					var requrimentcode = result.data;

					url=$("#orderDomain").val()+"/order/"+purchaseOrderId+"/create.html";
					if(type != 'F'){//如果不是再次开单，有需求单号要传需求单号，再次开单则不需要传需求单号
						if(requrimentcode != null && requrimentcode!='null'&&requrimentcode!=''){
							url+='?requirementCode='+requrimentcode;
						}
					}
					window.location.href=url;
				}else{
					cbms.alert(result.data);
				}
			}else{
				cbms.alert(result.data);
			}
		}
	});
}
