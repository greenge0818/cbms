var type = "buyer";
var dt;
jQuery(function ($) {
    initTable();
    initClickEvent()
    onSearchClick();
    //添加客户
    $("#addAccount").on(ace.click_event, function () {
        location = $("#account_type").val() + "/create.html";
    });
    var op_type = $("#op_type").val();
    var account_type = $("#account_type").val();
    if (op_type == "account_view") {
        $(".vendor-edit").hide();
    }

    $("#editInfoBtn").on(ace.click_event, function () {
        $(".vendor-text").hide();
        $(".vendor-edit").show();
        $("#hideArea").hide();
        $(this).hide();
        $("#saveInfoBtn").show();
    });
    $("#back").on(ace.click_event, function () {
        if ($("#editInfoBtn").is(":hidden")) {
            $(".vendor-text").show();
            $(".vendor-edit").hide();
            $("#hideArea").show();
            $("#saveInfoBtn").hide();
            $("#editInfoBtn").show();
        } else {
            location = Context.PATH + "/account/" + $("#account_type").val() + ".html";
        }
    });
});
function initTable() {
    var url = Context.PATH + "/account/search.html";
    dt = jQuery("#dynamic-table").DataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        //"bLengthChange": false, //显示pageSize的下拉框
        "ajax": {
            "url": url
            , "type": "POST"
            , data: function (d) {
                return $.extend({}, d, {
                    type: type,
                    accountName: $("#accountName").val(),
                    contactName: $("#contactName").val(),
                    contactTel: $("#contactTel").val()
                    
                });
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'account_name'},
            {data: 'business'},
            {data: 'proxy_factory'},
            {data: 'contact_name'}
            , {data: 'tel'}
            , {data: 'reg_time'}
            , {data: 'status'}
            , {defaultContent: ''}
        ]

        , columnDefs: [
            {
                "targets": 0, //第几列 从0开始
                "data": "account_name",
                "render": function (data, type, full, meta) {
                    //data:当前单元格数值 type：display full整行数据 meta:html
                    var url = Context.PATH + "/account/buyer/" + full.account_id + ".html";
                    return "<a href='" + url + "'>" + data + "</a>";
                }
            }
            ,
            {
                "targets": 6, //第几列 从0开始
                "data": "status",
                "render": renderStatus
            }
            , {
                "targets": 7, //第几列 从0开始
                "data": "status",
                "render": renderOperation
            }, {
                sDefaultContent: '', //解决请求参数未知的异常
                aTargets: ['_all']

            }
        ]
    });

}

function searchData() {
    dt.ajax.reload();
}


function onSearchClick() {
    jQuery("#searchVendorlist").on(ace.click_event, function () {
        searchData();
    });
}

function renderStatus(data, type, full, meta) {
    return data === 0 ? '<span class="red">锁定</span>' : '<span>正常</span>';
}

function renderOperation(data, type, full, meta) {
    //0锁定 1正常
    var statsHtml = "";
    if(data == 1){
        statsHtml = '<a class="red lock" title="锁定" href="javascript:;"><i class="ace-icon fa fa-lock bigger-130"></i></a>';
    }else if(data == 0){
        statsHtml = '<a class="green unlock" title="解锁" href="javascript:;"><i class="ace-icon fa fa-unlock bigger-130"></i></a>';
    }

    //第一个div
    var html = '<div class="hidden-sm hidden-xs action-buttons"><a class="blue authority" title="划转" href="' + Context.PATH + '/account/buyer/accountassign.html?id=' + full.account_id + '"><i class="ace-icon fa fa-exchange bigger-130"></i></a>';
    html += statsHtml;
    html += '<a class="red view" title="查看采购记录" href="javascript:;"> <i class="ace-icon fa fa-eye bigger-130"></i> </a>';
    html += '</div>';

    //第二个div
    html += '<div class="hidden-md hidden-lg"><div class="inline pos-rel"><button class="btn btn-minier btn-yellow dropdown-toggle" data-toggle="dropdown" data-position="auto">'
    '<i class="ace-icon fa fa-caret-down icon-only bigger-120"></i></button>';
    html += '<ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">';
    html += '<li><a class="blue authority" title="划转" href="' + Context.PATH + '/account/buyer/accountassign.html?id=' + full.account_id + '"> <i class="ace-icon fa fa-exchange bigger-130"></i></a></li>';
    html += '<li>' + statsHtml + "</i>";
    html += '<li><a class="red view" title="查看采购记录" href="javascript:;"><i class="ace-icon fa fa-eye bigger-130"></i></a></li>';
    html += '</ul></div></div>';


    return html;
}

function initClickEvent() {
    //锁定
    $(".table").on("click", ".lock", function(){
        var tr = $(this).closest('tr'), data = dt.row(tr).data();
        lockAndUnLock(data.account_id, '0',"确定要锁定该用户？");
    });

    //解锁
    $(".table").on("click", ".unlock", function(){
        var tr = $(this).closest('tr'), data = dt.row(tr).data();
        lockAndUnLock(data.account_id, '1',"确定要解锁该用户？");
    });

    //划转
    $(".table").on('click', '.authority', function () {
        var tr = $(this).closest('tr'), data = dt.row(tr).data();
        console.log("data:" + JSON.stringify(data));
    });

    //查看采购记录
    $(".table").on('click', '.view', function () {
        var tr = $(this).closest('tr'), data = dt.row(tr).data();
        location = Context.PATH + "/account/buyer/" + data.account_id + "/purchaselist.html";
    });
}


//锁定和解锁
function lockAndUnLock(id, status,msg){
    cbms.confirm(msg,null,function(){
        $.ajax({
            type: 'post',
            url: Context.PATH + "/account/" + id + "/lockandunlock.html",
            data: {
                status: status
            },
            error: function (s) {

            }
            , success: function (result) {
                if (result && result.success) {
                    cbms.alert("操作成功");
                    searchData();
                } else {
                    cbms.alert("操作失败");
                }
            }

        });
    });
}
