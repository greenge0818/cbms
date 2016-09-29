/**
 * 快递公司设置
 * Created by lcw on 2016/01/21.
 */

$(document).ready(function () {
    var table;
    table = $('#formExp').dataTable({
        "processing": true,
        "serverSide": false,
        "searching": false,
        "ordering": false,
        "ajax": {
            url: Context.PATH + '/sys/expressfirmsetlist.html',
            type: 'POST',
            data: function (d) {
                d.createdBy = $("#createdBy").val();
                d.name = $("#name").val();
                d.dateStartStr= $("#startTime").val();
                d.dateEndStr= $("#endTime").val();

            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {

            var editLink = "";
            if ($("#settingEdit").val() == "true") {
                editLink = "&nbsp;<a href='javascript:;' name='edit' eid='" + aData.id + "'>删除</a>";
            }

           $('td:eq(1)', nRow).html(renderTime(aData.created));
            $('td:eq(3)', nRow).html(editLink);
        }
        ,
        columns: [
            {data: 'name'},
            {data: 'created' },
            {data: 'createdBy'},
            {defaultContent: ''}
        ]

    });

    $("#queryBtn").on("click", function () {
        table.fnDraw();
    });

});
/**
 * 删除
 */
$('#formExp').on("click", "a[name='edit']", function () {
    var id = $(this).attr("eid");
    cbms.confirm("确定要删除？",null,function(){
        delexpre(id)
    });
});

 function delexpre(id){
    $.ajax({
        type: "POST",
        url: Context.PATH + "/sys/"+id+"/delexpress.html",
        dataType: "json",
        success: function (response) {
            if (response.success) {
                cbms.alert("删除成功");
                location.reload();
            } else {
                cbms.alert("删除失败");
            }
        }
    });
}

$("#addExpress").on(ace.click_event, function () {
    cbms.getDialog("添加快递公司", "#userDialog");
});

$(document).on("click", "#submit", function () {
    var name = $("#expressName").val();
    if (name <= 0) {
        cbms.alert("快递公司名称不能为空！");
        return false;
    }
    $.ajax({
        type: "POST",
        url: Context.PATH + "/sys/addexpress.html",
        data: {
            name: name
        },
        dataType: "json",
        success: function (response) {
            if (response.success) {
                cbms.closeDialog();
                cbms.alert("添加成功");
                location.reload();
            } else {
                cbms.alert(response.data);

            }
        }
    });
});

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
    var mytime=year+"-"+(month<10?"0":"")+month+"-"+(mydate<10?"0":"")+mydate;
    return mytime;
}
