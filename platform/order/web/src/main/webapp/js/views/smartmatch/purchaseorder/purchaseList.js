var PurchasePage = new function() {
    this.dt;
}

$(document).ready(function() {
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
                    orgName : $("#sorganization").val(),
                    startTime : $("#startTime").val(),
                    endTime : $("#endTime").val()
                });
            }
        },
        columns : [
            {data : 'code'},
            {data : 'createdTime'},
            {data : 'buyerName'},
            {data : 'deliveryName'},
            {data : 'categoryName'},
            {data : 'totalWeight'},
            {data : 'status'},
            {defaultContent : ''}
        ],
        columnDefs: [
			{
			    "targets": 0, //第几列 从0开始
			    "data": "code",
			    "render": function (data, type, full, meta) {
			        var url = Context.PATH + "/smartmatch/purchaseorder/"+full.id+"/show.html";
			        return "<a href='" + url + "'>" + data + "</a>";
			    }
			},
            {
                "targets": 1, //第几列 从0开始
                "data": "createdTime",
                "render": renderTime
            },
            {
                "targets": 7, //第几列 从0开始
                "data": "status",
                "render": renderOperation
            }

        ]
    });
});

//清空按钮
$(document).on("click","#cleanSearch",function(){
	resetForm($("form.form-inline"));
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

function renderOperation(data, type, full, meta) {
    var html = '';
    if(data == '待报价'){
        html += '<a  href="' + Context.PATH + '/smartmatch/purchaseorder/created.html?id='+full.id+'">继续询价</a>&nbsp;&nbsp;&nbsp';
        html += '<a  href="javascript:void(0)" class="closeOrder" orderId="'+full.id+'">关闭</a>';
    }
    if(data == '已报价'){
        html +=  '<a  href="' + Context.PATH + '/smartmatch/purchaseorder/created.html?id='+full.id+'">继续询价</a>&nbsp;&nbsp;&nbsp;'
        html +=  '<a  href="' + Context.PATH + '/order/'+full.id+'/create.html">开单</a>&nbsp;&nbsp;&nbsp;'
        html +=  '<a  href="javascript:void(0)" class="closeOrder" orderId="'+full.id+'">关闭</a>';
    }
    if(data == '已开单'){
//        html +=  '<a  href="">查看</a>&nbsp;&nbsp;&nbsp;'
        html +=  '<a  href="' + Context.PATH + '/order/'+full.id+'/create.html">再次开单</a>';
    }
    if(data == '已关闭'){
        html +=  '<a  href="javascript:void(0)" class="activeOrder" orderId="'+full.id+'">激活</a>';
    }
    return html;
}

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
