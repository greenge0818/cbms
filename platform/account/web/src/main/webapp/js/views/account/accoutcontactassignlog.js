var dt;

$(document).ready(function() {
    dt = $('#dynamic-table').DataTable( {
        "pageLength": 50, //每页记录数
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "oLanguage": {sUrl: Context.PATH + "/js/DT_zh.txt"}, //自定义语言包
        "ajax": {
            url: Context.PATH + '/account/contact/loadAccountContactAssignLog.html',
            type: 'POST',
            data: function(d){
               //客户名称 服务中心
                d.accountName = $.trim($("#accountName").val());
                d.orgId = $("#org").val();
            }
        },
        "fnRowCallback":function(nRow, aData, iDataIndex){
            $('td:first', nRow).html(iDataIndex + 1);
            if(null == aData.managerExName || "" == aData.managerExName){
            	$('td:eq(3)', nRow).html("-");
            }else{
            	$('td:eq(3)', nRow).html(aData.managerExName+"（"+aData.orgExName+"）");
            }
            $('td:eq(4)', nRow).html(aData.managerNextName+"（"+aData.orgNextName+"）");
            $('td:eq(5)', nRow).html(formatDay(aData.created));
            
        },
        columns: [
            {defaultContent: ''},
            { data: 'accountName' },
            { data: 'contactName' },
            { data: 'managerExName' },
            { data: 'managerNextName' },
            { data: 'created' },
            { data: 'assignName' }
        ]
    } );

    $("#queryBtn").on("click", function () {
        dt.ajax.reload();
    });
   
} );

function formatDay(data) {
    var dt = new Date(data);
    var time = dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate() + " " +
        ((dt.getHours()/1)<10?("0"+dt.getHours()):dt.getHours()) + ":" + ((dt.getMinutes()/1)<10?("0"+dt.getMinutes()):dt.getMinutes()) + ":" + ((dt.getSeconds()/1)<10?("0"+dt.getSeconds()):dt.getSeconds());
    return time;
}
