/**
 * Created by lcw on 8/4/2015.
 */

$(document).ready(function () {

    //加载时更新“服务中心”和“录入人员”
    $("#traderName").val('');
    $("#sorganizationHidden").val('-1');

    //tuxianming 20150517
    var consumeApplyControlSwitch = $("input[name='ConsumeApplyControlSwitch']").val();

    var refs = [];
    if(consumeApplyControlSwitch=='1'){
        refs = [
            {data: 'id'},
            {data: 'invoiceDate'},
            {data: 'code'},
            {data: 'sellerName'},
            {data: 'totalAmount'},
            {data: 'inputUserName'},
            {data: 'inputUserMobil'},
            {data: 'created'},
            {defaultContent: '待寄出'},
            {data: 'orgName'},
            {defaultContent: ''},
            {defaultContent: ''}
        ];
    }else{
        refs = [
            {data: 'id'},
            {data: 'invoiceDate'},
            {data: 'code'},
            {data: 'sellerName'},
            {data: 'totalAmount'},
            {data: 'inputUserName'},
            {data: 'inputUserMobil'},
            {data: 'created'},
            {defaultContent: '待寄出'},
            {data: 'orgName'},
            {defaultContent: ''}
        ];
    }
    //end 

    var canEdit = ($("#edit").val() == 'true');
    var table;
    table = $('#dynamic-table').dataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "bLengthChange": true,
        "iDisplayLength": 50,
        "aLengthMenu": [50,100,150], //这个为选择每页的条目数default [10, 25, 50, 100]
        "ajax": {
            url: Context.PATH + '/invoice/in/searchinvoice.html',
            type: 'POST',
            data: function (d) {
                d.status = $("#status").val();
                d.startTime = $("#startTime").val();
                d.endTime = $("#endTime").val();
                d.sellerName = $("#seller").val();
                d.selectsend=$("#selectsend").val();
                d.orgIds = getSelectOrgIds();
                d.userName = $("#traderName").val()
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
            var inputHtml = "<input type='checkbox' name='check' value='" + aData.id + "'>";
            var totalAmount = parseFloat(aData.totalAmount);
            var dt = new Date(aData.created);
            var time = dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate() + " " +
                ((dt.getHours()/1)<10?("0"+dt.getHours()):dt.getHours()) + ":" + ((dt.getMinutes()/1)<10?("0"+dt.getMinutes()):dt.getMinutes()) + ":" + ((dt.getSeconds()/1)<10?("0"+dt.getSeconds()):dt.getSeconds());
            var statusText = "待寄出";
            var invoiceDate = dateToStringTODay(aData.invoiceDate);

            $('td:eq(1)', nRow).html(invoiceDate);
            $('td:eq(4)', nRow).html(formatMoney(totalAmount, 2)).addClass("text-right");
            $('td:eq(7)', nRow).html(time);

            if(canEdit){  //最后一列
                var link = Context.PATH + "/invoice/in/send/" + aData.departmentId + "/inputinvoice.html?invoiceId="+aData.id;
                $('td:eq(-1)', nRow).html("<a href='" +link +"' class='blue'>编辑</a>");
            }
            if(consumeApplyControlSwitch=='1'){
                if (aData.invoiceIsSend == "不可寄出") {
                    inputHtml = "<input type='checkbox' name='checka' disabled='true' value='" + aData.id + "'>";

                    var showTipHtml = "<a href='#' onmouseout='hiddenTip(this);' onmouseover='showTip(" + aData.id + ",this);'>不可寄出</a><span class='pos-rel setInfobar'></span>" ;

                    //$("#allCheck").attr('disabled', 'disabled');
                    $('td:eq(10)', nRow).html(showTipHtml);
                } else {
                    $('td:eq(10)', nRow).html(aData.invoiceIsSend);
                }
            }
            $('td:eq(0)', nRow).html(inputHtml);
        },
        columns: refs
    });

    $("#queryBtn").on("click", function () {
        table.fnDraw();
    });
    $("#selectsend").change(function () {
        table.fnDraw();
    });
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
        form.attr('accept-charset', 'UTF-8');
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
        input5.attr('value',$("#selectsend").val());

        $('body').append(form);
        form.append(input1);
        form.append(input2);
        form.append(input3);
        form.append(input4);
        form.append(input5);

        form.submit();
        form.remove();
    }

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

    // 登记发票
    $("#checkIn").click(function () {
        if (invoiceIds == null || invoiceIds.length == 0) {
            cbms.alert("请选择快递单！");
            return;
        }

        $.ajax({
            type: 'post',
            url: Context.PATH + "/invoice/in/queryalldeliver.html",
            data: {
            },
            error: function(s)
            {
            }
            ,
            success: function (result) {
                var content = "<ul>";
                content += "<li>快递公司：";
                content += "<select id='expressCompany'>";
                if (result && result.success) {
                    for (var i = 0; i < result.data.length; i++) {
                        content += "<option selected='selected' value='" + result.data[i].name + "'>" + result.data[i].name +"</option>";
                    }
                }
                content += "</select>";
                content += "</li>";
                content += "<li>";
                content += "快递单号：<input type='text' maxlength='30' id='expressName' />";
                content += "</li>";
                content += "<li class='btn-bar text-center'>";
                content += "<button type='button' class='btn btn-sm btn-primary' id='submit' >提交</button>&nbsp;&nbsp;";
                content += "<button type='button' class='btn btn-sm btn-default' id='cancel' >取消</button>";
                content += "</li>";
                content += "</ul>";
                cbms.getDialog("请正确填写快递信息", content);
                content = '';
            }

        });


    });

    // 提交
    $("body").on("click", "#submit", function () {
        var expressCompany = $("#expressCompany").val();
        var expressName = $.trim($("#expressName").val());
        if (expressCompany != "其他" && expressName == "") {
            cbms.alert("请输入快递单！");
            return;
        }
        var expressData = {
            expressName: expressName,
            expressCompany: expressCompany,
            invoiceIds: invoiceIds
        }
        cbms.loading();
        $.ajax({
            type: 'post',
            url: Context.PATH + "/invoice/in/checkinexpress.html",
            data: {
                expressJson: JSON.stringify(expressData)
            },
            error: function (s) {
                cbms.closeLoading();
            }
            , success: function (result) {
                cbms.closeLoading();
                if (result) {
                    if (result.success) {
                        cbms.alert("登记快递单号成功！",function(){
                            location.reload(true);
                        });
                    }
                    else {
                        cbms.alert(result.data,function(){
                            location.reload(true);
                        });
                    }
                } else {
                    cbms.alert("登记快递单号失败",function(){
                        location.reload(true);
                    });
                }
            }
        });
    });

    // 取消
    $("body").on("click", "#cancel", function () {
        cbms.dialog.close();
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
        $(cloneTable).css({"font-size":"12px"});
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

    /**
     * 点击：选择报表中心，服务中心选择框
     */
    $("#orgSelectBtn").click(showSelectOptionsBox);

    /**
     * 选中：所有服务中心时，取消其它服务中心选项框, 选中其它服务中心时，取消：所有服务中心
     */
    clickSelectAll();
});

/**
 * 不可寄出，鼠标悬停tip提示
 */
function showTip(aDataId,o){
    var tipHtml = Context.PATH + "/invoice/in/invoiceinissend.html?invoiceId="+aDataId+"&";
    $.ajax({
        type: 'get',
        url: tipHtml,
        data: {
        },
        success: function (result) {
            if (result) {
                $(o).closest("td").find(".setInfobar").html(result);
            }
        }
    });
};

/**
 * 不可寄出，鼠标移开tip提示消失
 */
function hiddenTip(o){
    $(o).closest("td").find(".setInfobar").html("");
    $(o).closest("tbody").find(".setInfobar").empty();
};

/**
 * 点击：选择报表中心，服务中心选择框
 */
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

function getSelectOrgIds(){
    var orgIds = [];
    var orgIdsStr="";
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
        orgIdsStr = orgIds.toString();
    }
    return orgIdsStr;
}