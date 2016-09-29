jQuery(function ($) {
    load();

    $("#addEnter").click(function () {
        add();
    });

    $(document).on("click", ".delete" ,function () {
        del(this);
    });
});

function load() {
    $("#filterList").empty();
    $.ajax({
        type: "POST",
        url: Context.PATH + '/sys/loadFilter.html',
        data: {},
        dataType: "json",
        success: function (response, textStatus, xhr) {
            if (response.success) {
                var datas = response.data;
                for (var i in datas) {
                    $("#filterList").append('<li><span>' + datas[i].settingValue + '</span><a title="删除" class="delete" rel=' + datas[i].id +'>删除</a></li>');
                }
            } else {
                cbms.alert("加载数据失败，请重试");
            }
        },
        error: function (xhr, textStatus, errorThrown) {
        }
    });
}

function del(i){
    var id = $(i).attr("rel");
    $.ajax({
        type: "POST",
        url: Context.PATH + '/sys/deleteFilter.html',
        data: {
            id: id
        },
        dataType: "json",
        success: function (response, textStatus, xhr) {
            if (response.success) {
                load();
            } else {
                cbms.alert(response.data);
            }
        },
        error: function (xhr, textStatus, errorThrown) {
        }
    });
}

function add() {
    if (setlistensSave("#addEnterForm")) {
        var data = $("#enterIpt").val();
        $.ajax({
            type: "POST",
            url: Context.PATH + '/sys/addFilter.html',
            data: {
                settingValue: data
            },
            dataType: "json",
            success: function (response, textStatus, xhr) {
                if (response.success) {
                    load();
                } else {
                    cbms.alert(response.data);
                }
            },
            error: function (xhr, textStatus, errorThrown) {
            }
        });
    }
}