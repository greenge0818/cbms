$(document).ready(function () {
    var table;
    table = $('#dynamic-table').dataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "bLengthChange": false,
        "ajax": {
            url: Context.PATH + '/invoice/out/checklist/search.html',
            type: 'POST',
            data: function (d) {
                d.id = $("#checkListId").val();
                d.buyerName = $("#buyerName").val();
                d.beginTime = $("#beginTime").val();
                d.endTime = $("#endTime").val();
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {

            var weight = parseFloat(aData.weight),
                amount = parseFloat(aData.amount);
            $('td:eq(1)', nRow).html((new Date(aData.applyTime).Format("yyyy-MM-dd")));
            $('td:eq(5)', nRow).html(weight.toFixed(4)).addClass("text-right");
            $('td:eq(6)', nRow).html(formatMoney((amount), 2)).addClass("text-right");
            var td3 = aData.nsortName;
            if(aData.material){
            	td3 += " " + aData.material;
            }
            $('td:eq(3)', nRow).html(td3);
            //隐藏列
            if(hidden) {
                $('td:eq(0)', nRow).addClass("none");
                $('td:eq(1)', nRow).addClass("none");

                $('th:eq(0)', nRow).addClass("none");
                $('th:eq(1)', nRow).addClass("none");
            }
            if(aData.typeOfSpec && _specSignList[aData.typeOfSpec] && aData.typeOfSpec != "none"){
            	$('td:eq(4)', nRow).html(_specSignList[aData.typeOfSpec] + " " + $('td:eq(4)', nRow).text());
            }
            

        },
        columns: [
            {data: 'id'},
            {data: 'applyTime'},
            {data: 'buyerName'},
            {data: 'nsortName'},
            {data: 'spec'},
            {data: 'weight'},
            {data: 'amount'},
            {data: "orgName"},
            {data: "invoiceType"}
        ]
    });

    /**
     * 搜索
     */
    $("#queryBtn").on("click", function () {
        table.fnDraw();
    });

    $(document).on("click", ".btn-export", function(){
        exportExcel();
    })

    /**
     * 返回
     */
    $(document).on("click", ".go-back", function(){
        history.go(-1);
    })

});

/**
 * 导出excel文件
 */
function exportExcel() {
    var form = $("<form>");
    form.attr('style', 'display:none');
    form.attr('accept-charset', 'utf-8');
    form.attr('target', '');
    form.attr('method', 'post');
    form.attr('enctype', 'multipart/form-data');
    form.attr('action', Context.PATH + "/invoice/out/checklist/export.html");

    var input1 = $('<input>');
    input1.attr('type', 'hidden');
    input1.attr('name', 'id');
    input1.attr('value', $("#checkListId").val());

    var input2 = $('<input>');
    input2.attr('type', 'hidden');
    input2.attr('name', 'buyerName');
    input2.attr('value', $("#buyerName").val());

    var input3 = $('<input>');
    input3.attr('type', 'hidden');
    input3.attr('name', 'beginTime');
    input3.attr('value', $("#beginTime").val());

    var input4 = $('<input>');
    input4.attr('type', 'hidden');
    input4.attr('name', 'endTime');
    input4.attr('value', $("#endTime").val());


    $('body').append(form);
    form.append(input1);
    form.append(input2);
    form.append(input3);
    form.append(input4);

    form.submit();
    form.remove();
}