var dt;
jQuery(function ($) {
    initTable();
    initClickEvent();
    onSearchClick();
    //添加配置
    $("#addProcess").on(ace.click_event, function () {
        location = "create.html";
    });
});
function initTable() {
    var url = Context.PATH + "/sys/busiprocess/search.html";
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
                	orgName: $("#orgName").val(),
                    userName: $("#userName").val()
                });
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'userName'},
            {data: 'orgName'},
            {data: 'tel'},
            {data: 'roleName'}
        ]

        , columnDefs: [
            {
                "targets": 0, //第几列 从0开始
                "data": "account_name",
                "render": function (data, type, full, meta) {
                    //data:当前单元格数值 type：display full整行数据 meta:html
                    var url = Context.PATH + "/sys/busiprocess/" + full.userId + ".html";
                    return "<a href='" + url + "'>" + data + "</a>";
                }
            },
            {
                "targets": 4, //第几列 从0开始
                "data": "account_name",
                "render": function (data, type, full, meta) {
                    //data:当前单元格数值 type：display full整行数据 meta:html
                    return "<a class = 'delete' href='javascript:;'>删除</a>";
                }
            },
            {
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
    jQuery("#search").on(ace.click_event, function () {
        searchData();
    });
    $('input').keydown(function(e){
		if(e.keyCode==13){
			searchData();
		}
	});
}

function initClickEvent() {
    //锁定
    $(".table").on("click", ".delete", function(){
        var tr = $(this).closest('tr'), data = dt.row(tr).data();
        cbms.confirm("确定删除？",null,function(){
            $.ajax({
                type: 'post',
                url: Context.PATH + "/sys/busiprocess/" + data.userId + "/delete.html",
                
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
    });
}
