/**
 * Created by lcw on 8/4/2015.
 */
$(document).ready(function () {

    //加载时更新“服务中心”和“录入人员”
    $("#traderName").val('');
    $("#sorganizationHidden").val('-1');

    var table;
    table = $('#dynamic-table').dataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "iDisplayLength": 50,
        "aLengthMenu": [50,100,150],
        "ajax": {
            url: Context.PATH + '/invoice/in/searchinvoice.html',
            type: 'POST',
            data: function (d) {
                d.orgid=$("#sorganizationHidden").val();
                d.uid=$("#traderName").attr("userid");
                d.status = $("#status").val();
                d.expressName = $("#express").val();
                d.sendStartTime = $("#startTime").val();
                d.sendEndTime = $("#endTime").val();
                d.sellerName = $("#seller").val()
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
            var inputHtml = "<input type='checkbox' name='check' value='" + aData.id + "'>";
            var totalAmount = parseFloat(aData.totalAmount);
            var dt = new Date(aData.sendTime);
            var time = dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate() + " " +
                ((dt.getHours()/1)<10?("0"+dt.getHours()):dt.getHours()) + ":" + ((dt.getMinutes()/1)<10?("0"+dt.getMinutes()):dt.getMinutes()) + ":" + ((dt.getSeconds()/1)<10?("0"+dt.getSeconds()):dt.getSeconds());
            var link = Context.PATH + "/invoice/in/confirm/" + aData.departmentId + "/inputinvoice.html?invoiceId="+aData.id;
            var invoiceDate = dateToStringTODay(aData.invoiceDate);
            $('td:eq(0)', nRow).html(inputHtml);
            $('td:eq(1)', nRow).html(invoiceDate);
            $('td:eq(4)', nRow).html(formatMoney(totalAmount, 2)).addClass("text-right");
            $('td:eq(7)', nRow).html(time);
           var judge= comptime(aData.sendTime,aData.expressName,aData.orgId)
           if(judge){
               $('td:eq(8)', nRow).html(aData.expressName).addClass("red");
           }else{
               $('td:eq(8)', nRow).html(aData.expressName);
           }

            $('td:eq(10)', nRow).html(generateOptHtml(link));
            if(aData.checkTotalAmount && aData.checkTotalAmount>0){
            	$('td:eq(4)', nRow).html(formatMoney(aData.checkTotalAmount, 2)).addClass("text-right");
            }
        },

        columns: [
            {data: 'id'},
            {data: 'invoiceDate'},
            {data: 'code'},
            {data: 'sellerName'},
            {data: 'totalAmount'},
            {data: 'inputUserName'},
            {data: 'inputUserMobil'},
            {data: 'sendTime'},
            {data: 'expressName'},
            {defaultContent: '待确认'},
            {defaultContent: ''}
        ]
    });

    $("#queryBtn").on("click", function () {
        table.fnDraw();
    });

    // 全选/全不选
    $("#allCheck").click(function () {
        var checked = $(this).is(':checked');
        // 取消全选
        if (!checked) {
            $("input[name='check']").removeAttr("checked");
            $(this).removeAttr("checked");
        }
        else {
            $("input[name='check']").prop('checked', true);
            $(this).prop('checked', true);
        }

        invoiceTotal();
    });

    // 单选
    $("body").on("click", "input[name='check']", function () {
        var checked = $(this).is(':checked');
        if (!checked) {
            $(this).removeAttr("checked");
            $("#allCheck").removeAttr("checked");   // 取消全选
        }
        else {
            $(this).prop('checked', true);
        }

        // 如果全部选中，那么全选checkbox选中
        var checkCount = $("input[name='check']").length;
        var checkedCount = $("input[name='check']:checked").length;
        if (checkCount == checkedCount) {
            $("#allCheck").prop('checked', true);
        }

        invoiceTotal();
    });

    // 打印清单
    $("#prints").click(function () {
        if (invoiceIds == null || invoiceIds.length == 0) {
            cbms.alert("请选择发票！");
            return;
        }
        var cloneTable = $("#dynamic-table").clone();
        $(cloneTable).find("tfoot").remove();
        var rows = $(cloneTable).find("tr");
        $(rows).eq(0).find("th").first().remove();
        $(rows).eq(0).find("th").last().remove();
        for (var i = 1; i < rows.length; i++) {
            // 删除未选中
            var checked = $(rows).eq(i).find("input[name='check']").is(':checked');
            if (checked) {
                $(rows).eq(i).find("td").first().remove();
                $(rows).eq(i).find("td").last().remove();
            }
            else {
                $(rows).eq(i).remove();
            }
        }
        $(cloneTable).print();

        cbms.loading();
        $.ajax({
            type: 'post',
            url: Context.PATH + "/invoice/in/updateprintstatus.html",
            data: {
                invoiceIdsJson: JSON.stringify(invoiceIds)
            },
            error: function (s) {
                cbms.closeLoading();
            }
            , success: function (result) {
                cbms.closeLoading();
                if (result) {
                    if (!result.success) {
                        cbms.alert(result.data);
                    }
                } else {
                    cbms.alert("更新打印状态失败");
                }
            }
        });
    });

});
function comptime(Time,expressName,orgid) {

    var beginTime = new Date().format("yyyy-MM-dd hh:mm:ss");
    var endTime=new Date(Time).format("yyyy-MM-dd hh:mm:ss");
    var beginTimes = beginTime.substring(0, 10).split('-');
    var endTimes = endTime.substring(0, 10).split('-');
    beginTime = beginTimes[1] + '-' + beginTimes[2] + '-' + beginTimes[0] + ' ' + beginTime.substring(10, 19);
    endTime = endTimes[1] + '-' + endTimes[2] + '-' + endTimes[0] + ' ' + endTime.substring(10, 19);
    var a = Date.parse(beginTime)-Date.parse(endTime);
    var b=0;
    $.ajax({
        type: 'post',
        url: Context.PATH + "/invoice/in/getexpressfate.html",
        data: {
            expressName: expressName,
            orgid:orgid
        }
        , success: function (result) {
                 b=parseInt(result.data)*86400000
        }
    });
    if(b=0){return false;}
    if (a>b) {return true;
    }else{
        return false;
    }

}

function generateOptHtml(link) {
    var invoiceConfirm = $("#invoiceConfirm").val();
    if (invoiceConfirm != "true")
        return "";

    var optHtml = '<div class="hidden-sm hidden-xs action-buttons">';
    optHtml += "<a href='" + link + "' title='确认'>";
    optHtml += "<i class='ace-icon fa fa-search-plus bigger-130 blue'></i></a>";
    optHtml += '</div>';
    optHtml += '<div class="hidden-md hidden-lg">';
    optHtml += '<div class="inline pos-rel">';
    optHtml += '<button class="btn btn-minier btn-yellow dropdown-toggle" data-toggle="dropdown"';
    optHtml += 'data-position="auto">';
    optHtml += '<i class="ace-icon fa fa-caret-down icon-only bigger-120"></i></button>';
    optHtml += '<ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">';
    optHtml += '<li>';
    optHtml += "<a href='" + link + "' title='确认'><i class='ace-icon fa fa-search-plus bigger-130 blue'></i></a>";
    optHtml += '</li>';
    optHtml += '</ul>';
    optHtml += '</div>';
    optHtml += '</div>';
    return optHtml;
}
//导出EXCEL
$("#exportBtn").on("click", function () {
    var codeIds = "";
    $("input[name='check']:checkbox:checked").each(function(){
        codeIds += $(this).val() + ","
    });
    codeIds = codeIds.substr(0, codeIds.length - 1);
    var msg = "";
    if(codeIds == '' || codeIds == undefined){
        msg = "全部";
    }else{
        msg = "选中";
    }
    cbms.confirm("确认导出" + msg + "数据？", null, function () {
        exportExcel(codeIds)
    });
});
// 导出EXCEL
function exportExcel(codeIds){
    var form = $("<form>");
    form.attr('style', 'display:none');
    form.attr('target', '');
    form.attr('method', 'post');
    form.attr('action',Context.PATH + '/invoice/in/exportexcel.html');
    //
    var input1 = $('<input>');
    input1.attr('type', 'hidden');
    input1.attr('name', 'status');
    input1.attr('value', $("#status").val());
    // 开始时间
    var input2 = $('<input>');
    input2.attr('type', 'hidden');
    input2.attr('name', 'startTime');
    input2.attr('value', $("#startTime").val());
    // 结束时间
    var input3 = $('<input>');
    input3.attr('type', 'hidden');
    input3.attr('name', 'endTime');
    input3.attr('value', $("#endTime").val());

    var input4 = $('<input>');
    input4.attr('type', 'hidden');
    input4.attr('name', 'codeIds');
    input4.attr('value', codeIds);

    var input5 = $('<input>');
    input5.attr('type', 'hidden');
    input5.attr('name', 'whichName');
    input5.attr('value',"confirm");


    $('body').append(form);
    form.append(input1);
    form.append(input2);
    form.append(input3);
    form.append(input4);
    form.append(input5);

    form.submit();
    form.remove();
}

