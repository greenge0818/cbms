/**
 * Created by lcw on 2015/7/16.
 */

$(document).ready(function () {
    bindRegionData($("#province"), $("#city"));
    var table;
    table = $('#dynamic-table').dataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "bLengthChange": false,
        "ajax": {
            url: Context.PATH + '/account/sellersearchdata.html',
            type: 'POST',
            data: function (d) {
                d.companyName = $("#companyName").val();
                d.business = $("#business").val();
                d.provinceId = $("#province").val();
                d.cityId = $("#city").val();
                d.status = $("#status").val()
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
            var dt = new Date(aData.created);
            $('td:eq(5)', nRow).html(dateFormat(dt, "yyyy-MM-dd hh:mm:ss"));
            $('td:eq(6)', nRow).html(aData.manager + "（" + aData.managerTel + "）");
            $('td:eq(7)', nRow).html(aData.status == 0 ? "锁定" : "正常");
        },
        columns: [
            {data: 'name'},
            {data: 'business'},
            {data: 'proxyFactory'},
            {data: 'legalPersonName'},
            {data: 'mobil'},
            {data: 'created'},
            {data: 'manager'},
            {data: 'status'}
        ]
    });

    $("#submit").on("click", function () {
        table.fnDraw();
    });

    function dateFormat(date, fmt) {
        var o = {
            "M+" : date.getMonth()+1,                 //月份
            "d+" : date.getDate(),                    //日
            "h+" : date.getHours(),                   //小时
            "m+" : date.getMinutes(),                 //分
            "s+" : date.getSeconds(),                 //秒
            "q+" : Math.floor((date.getMonth()+3)/3), //季度
            "S"  : date.getMilliseconds()             //毫秒
        };
        if(/(y+)/.test(fmt))
            fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));
        for(var k in o)
            if(new RegExp("("+ k +")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        return fmt;
    }
});