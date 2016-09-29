/**
 * Created by dengxiyan on 2015/9/11.
 * 进项发票清单
 */
var dt;
var selectIds = [];//选中行的id 包括分页
jQuery(function ($) {
    initTable();
    initEvent();
});

function initTable() {
    dt = $("#dynamic-table").DataTable({
        "pageLength": 15, //每页记录数
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "lengthChange": false, //不显示pageSize的下拉框
        "oLanguage": {sUrl: Context.PATH + "/js/DT_zh.txt"},
        "ajax": {
            "url": Context.PATH + "/report/finance/loadinvoiceindetailsdata.html"
            , "type": "POST"
            , data: function (d) {
                setQueryParam(d);
            }
            //操作服务器返回的数据
            , "dataSrc": function (result) {
                //设置总发票张数
                $("#total").text(result.recordsFiltered);
                return result.data;//返回data
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {defaultContent: ""},
            {data: 'code'},
            {data: 'invoiceDate'},
            {data: 'sellerName'},
            {data: 'nsortName'},
            {data: 'spec'},
            {data: 'material'},
            {data: 'weight', "sClass": "text-right"},
            {data: 'amount', "sClass": "text-right"},
            {data: 'orgName'},
            {data: 'status'}
        ]
        , fnRowCallback: function (nRow, aData, iDataIndex) {
            //设置之前已选中的checkbox的状态
            var checked  = $.inArray(aData.id.toString(),selectIds) >= 0 ? 'checked=checked' :'';
            var inputHtml = "<label class='pos-rel'><input class='ace' type='checkbox' name='check' value='" + aData.id + "' " + checked + "><span class='lbl'></span></label>";
            $('td:eq(0)', nRow).html(inputHtml);
            $('td:eq(7)', nRow).html(renderWeight(aData.weight));
            $('td:eq(8)', nRow).html(formatMoney(aData.amount, 2));

            // 表格绘制前 取消全选
            $("#allCheck").removeAttr("checked");

        }
        ,fnDrawCallback:function(){
            //表格绘制完成后  如果全部选中，那么全选checkbox选中
            var checkCount = $("input[name='check']").length;
            var checkedCount = $("input[name='check']:checked").length;
            if(checkedCount > 0 && checkedCount > 0){
                $("#allCheck").prop('checked', checkCount == checkedCount);
            }
        }
    });
}


function initEvent() {
    $("#queryBtn").click(function () {
        dt.ajax.reload();
        //清空
        selectIds = [];
        //设置选中的票据张数
        invoiceTotal();
    });

    $("#exportAll").click(function () {
        exportExcle(true);
    });

    $("#exportChecked").click(function () {
        exportExcle(false);
    });

    // 全选/全不选(当前页)
    $("#allCheck").click(function () {
        var checked = $(this).is(':checked');
        var childCheckbox = $("input[name='check']");

        //选中状态和全选/全不选框保持一致
        childCheckbox.prop('checked', checked);

        //设置选中的值
        if(checked){
            childCheckbox.each(function(){
                //不存在则添加选中的id
                if($.inArray($(this).val(),selectIds) == -1){
                    selectIds.push($(this).val());
                }
            });
        }else{
            //不勾选，删除值
            childCheckbox.each(function(){
                selectIds.splice($.inArray($(this).val(),selectIds),1);
            });
        }

        //设置选中的票据张数
        invoiceTotal();
    });

    // 单选
    $("body").on("click", "input[name='check']", function () {
        var checked = $(this).is(':checked');
        if (!checked) {
            //不勾选，删除值
            selectIds.splice($.inArray($(this).val(),selectIds),1);

            // 取消全选
            $("#allCheck").removeAttr("checked");
        } else {
            //勾选，插入值
            selectIds.push($(this).val());

            //全选
            // 如果全部选中，那么全选checkbox选中
            var checkCount = $("input[name='check']").length;
            var checkedCount = $("input[name='check']:checked").length;
            $("#allCheck").prop('checked', checkCount == checkedCount);

        }
        //设置选中的票据张数
        invoiceTotal();
    });
}


function exportExcle(isExportAll) {
    var form = $("<form>");
    form.attr('style', 'display:none');
    form.attr('target', '');
    form.attr('method', 'post');
    form.attr('action', Context.PATH + "/report/finance/buyerorderdetailexcel.html");
    form.attr("enctype", "multipart/form-data");//防止提交数据乱码

    $('body').append(form);

    if(!isExportAll) {
       if(selectIds.length == 0){
            cbms.alert("请选择要导出的记录后重试！");
            form.remove();
            return false;
        }
        var input1 = $('<input>');
        input1.attr('type', 'hidden');
        input1.attr('name', 'detailIdList');
        input1.attr('value', selectIds.join());
        form.append(input1);
    }else{
        var input1 = $('<input>');
        input1.attr('type', 'hidden');
        input1.attr('name', 'strStartTime');
        input1.attr('value', $("#startTime").val());
        form.append(input1);

        var input2 = $('<input>');
        input2.attr('type', 'hidden');
        input2.attr('name', 'strEndTime');
        input2.attr('value', $("#endTime").val());
        form.append(input2);

        var input3 = $('<input>');
        input3.attr('type', 'hidden');
        input3.attr('name', 'orgId');
        input3.attr('value', $("#sorganizationHidden").val());
        form.append(input3);

        var input4 = $('<input>');
        input4.attr('type', 'hidden');
        input4.attr('name', 'sellerName');
        input4.attr('value', $("#sellerName").val());
        form.append(input4);

        var input5 = $('<input>');
        input5.attr('type', 'hidden');
        input5.attr('name', 'code');
        input5.attr('value', $("#code").val());
        form.append(input5);

        var input6 = $('<input>');
        input6.attr('type', 'hidden');
        input6.attr('name', 'statusList');
        input6.attr('value', getCheckedStatus());
        form.append(input6);
    }

    var inputTitles = $('<input>');
    inputTitles.attr('type', 'hidden');
    inputTitles.attr('name', 'excelTitles');
    inputTitles.attr('value', '发票号码,开票时间,卖家全称,品名,规格,材质,数量（吨）,价税合计（元）,服务中心,状态');
    form.append(inputTitles);

    var inputIsPage = $('<input>');//是否分页
    inputIsPage.attr('type', 'hidden');
    inputIsPage.attr('name', 'isPage');
    inputIsPage.attr('value',false);
    form.append(inputIsPage);

    form.submit();
    form.remove();
}


// 统计选中的发票（包含分页）
function invoiceTotal() {
    //设置总共选中的数量
    $('#checkCount').text(selectIds.length);
}

function getCheckedStatus() {
    var status = [];
    $("input[name='status']:checked").each(function () {
        status.push($(this).val());
    });
    if (status.length == 0) {
        status.push("emptyValue");
    }
    return status.join();
}

function setQueryParam(d) {
    d.orgId = $("#sorganizationHidden").val();
    d.sellerName = $("#sellerName").val();
    d.code = $("#code").val();
    d.strStartTime = $("#startTime").val();
    d.strEndTime = $("#endTime").val();
    d.statusList = getCheckedStatus();
}




