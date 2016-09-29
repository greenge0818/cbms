var dt;
var weightScale = 6;   //重量保留6位小数
var moneyScale = 2;
var cols = [];
var _rowSpanCount = 6;

jQuery(function ($) {

    getCols();

    $("#queryBtn").click(function () {
        dt.ajax.reload();
    });

    $("#output").click(function () {
        exportExcel();
    });

});

function getCols(){
    $.ajax({
        type : "POST",
        url : Context.PATH + "/report/business/getorgmonthreportcolumns.html",
        success : function(result) {
            if(result && result.data){
                cols = result.data;
            }
            initTable(cols);

        },
        error : function(xhr, textStatus, errorThrown) {

        }
    });
}

function initTable() {
    dt = $("#dynamic-table").DataTable({
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "lengthChange": true, //显示pageSize的下拉框 50 100 150
        "aLengthMenu": [50, 100, 150],
        "ajax": {
            "url": Context.PATH + "/report/business/loadorgmonthreportdata.html",
            "type": "POST",
            data: function (d) {
                d.startTimeStr = $("#startTime").val();
                d.endTimeStr = $("#endTime").val();
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns:cols
        , columnDefs: [
            {
                sDefaultContent: '-', //解决请求参数未知的异常
                aTargets: ['_all']
            }
        ],
        fnRowCallback:function(nRow, aData, iDataIndex){
            //隐藏分页的信息
            $("#dynamic-table_info").hide();

            //交易数据列格式化
            $('td:eq(1)', nRow).html('<span class="bolder">' + aData.tradeData + '</span>');

            //交易数据列以后其他列格式化 过滤掉最后一行（沉淀资金）
            $('td:gt(1)', nRow).not("td:last").each(function(){
                var text =$(this).text();
                var floatValue = parseFloat(text);
                if(isNaN(floatValue)){
                    $(this).html(text);
                }else{
                    //销售金额和采购金额行
                    if(aData.rowDataType == 'sale' || aData.rowDataType == 'purchase'){
                        $(this).html(formatMoney(floatValue,moneyScale));
                    }
                    //交易重量行
                    if(aData.rowDataType == 'weight' ){
                        $(this).html(floatValue.toFixed(weightScale));
                    }
                }
            });

            //沉淀资金
//            $('td:last', nRow).each(function(){
//                var text =$(this).text();
//                var floatValue = parseFloat(text);
//                if(isNaN(floatValue)){
//                    $(this).html(text);
//                }else{
//                    $(this).html(formatMoney(floatValue,moneyScale));
//                }
//            });

            //合计行添加背景色
            if($(nRow).find('td:eq(0)').text().indexOf("合计") >= 0){
                $(nRow).addClass("recordbar");
            }
        },
        drawCallback : function(setting){
            $("#dynamic-table tbody tr").each(function(idx){
                if($(this).attr("merged")){
                    return;
                }
                $(this).attr("merged",true);//标记为已处理过，防止多次处理
                if((idx % _rowSpanCount) == 0){
                    $("td:first",this).attr("rowspan", _rowSpanCount).css({"vertical-align":'middle',"text-align" : 'center'});
                }else{
                    $("td:first",this).remove();
                }
            });
        }
        ,"scrollY": $(document.body).height() -10
        ,"scrollX": true
    });
}

function exportExcel() {
    var form = $("<form>");
    form.attr('style', 'display:none');
    form.attr('target', '');
    form.attr('method', 'post');
    form.attr('action', Context.PATH + "/report/business/exportorgmonthreport.html");

    var input1 = $('<input>');
    input1.attr('type', 'hidden');
    input1.attr('name', 'startTimeStr');
    input1.attr('value', $("#startTime").val());

    var input2 = $('<input>');
    input2.attr('type', 'hidden');
    input2.attr('name', 'endTimeStr');
    input2.attr('value', $("#endTime").val());

    $('body').append(form);
    form.append(input1);
    form.append(input2);

    form.submit();
    form.remove();
}



