/**
 * Created by caochao on 2015/7/15.
 */
$(document).ready(function() {
    $('#assignlogs').dataTable( {
        "processing": true,
        "serverSide": true,
        "ajax": {
            url: Context.PATH + '/account/ajaxassignlog.html',
            type: 'POST',
            data: {"accountId" : $("#accountId").val()}
        },
        "fnRowCallback":function(nRow, aData, iDataIndex){
           var dt = new Date(aData.regTime),dt2=new Date(aData.created);
            $('td:eq(1)', nRow).html(dateFormat(dt, "yyyy-MM-dd hh:mm:ss"));
            $('td:eq(5)', nRow).html(dateFormat(dt2, "yyyy-MM-dd hh:mm:ss"));
        },
        columns: [
            { data: 'accountName' },
            { data: 'regTime' },
            { data: 'managerExName' },
            { data: 'managerNextName' },
            { data: 'assignerName' },
            { data: 'created' }
        ]
    } );

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
} );