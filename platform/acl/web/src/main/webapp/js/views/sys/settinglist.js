/**
 * 系统设置项设置
 * Created by lcw on 2015/10/12.
 */
$(document).ready(function () {
    var table;
    table = $('#dynamic-table').dataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "ajax": {
            url: Context.PATH + '/sys/searchsettinglist.html',
            type: 'POST',
            data: function (d) {
                d.type = $("#type").val();
                d.name = $("#name").val();
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
            var dt = new Date(aData.lastUpdated);
            var lastUpdated = dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate();
            $('td:eq(3)', nRow).html(lastUpdated);
            var editLink = "";
            if ($("#settingEdit").val() == "true") {
                editLink = "&nbsp;<a href='javascript:;' name='edit' eid='" + aData.id + "'>编辑</a>";
            }
            $('td:eq(5)', nRow).html(editLink);
        },
        columns: [
            {data: 'settingName'},
            {data: 'settingType'},
            {data: 'settingValue'},
            {defaultContent: ''},
            {data: 'lastUpdatedBy'},
            {defaultContent: ''}
        ]
    });

    $("#queryBtn").on("click", function () {
        table.fnDraw();
    });

    /**
     * 编辑
     */
    $('#dynamic-table').on("click", "a[name='edit']", function () {
        var id = $(this).attr("eid");
        cbms.getDialog("编辑设置", Context.PATH + "/sys/" + id + "/editsetting.html");
    });

    /**
     * 保存修改
     */
    $(document).on("click","#submit",function(){
        updateSetting();
    });

    /**
     * 更新
     */
    function updateSetting() {
        var settingId = $("#settingId").val();
        var settingName = $("#settingName").val();
        var settingType = $("#settingType").val();
        var settingValue = $("#settingValue").val();
        var defaultValue = $("#defaultValue").val();
        var displayName = $("#displayName").val();
        var description = $("#description").val();
        cbms.loading();
        $("#submit").prop("disabled", true);
        $.ajax({
            type: "POST",
            url: Context.PATH + '/sys/saveeditsetting.html',
            data: {
                id: settingId,
                settingName: settingName,
                settingType: settingType,
                settingValue: settingValue,
                defaultValue: defaultValue,
                displayName: displayName,
                description: description
            },
            dataType: "json",
            success: function (response, textStatus, xhr) {
                if (response.success) {
                    cbms.alert("更新设置成功");
                    cbms.closeDialog();
                    table.fnDraw();
                } else {
                    $("#submit").prop("disabled", false);
                    cbms.alert(response.data);
                }
                cbms.closeLoading();
            },
            error: function (s) {
                $("#submit").prop("disabled", false);
                cbms.closeLoading();
            }
        });
    }
});
