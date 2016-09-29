//迁移风控代码，增加5个字段 add by zhoucai@prcsteel.com 2016-5-6
/*
 * 服务中心交易量设置js
 */

var _globalAttr = {
    dt: null
}
$().ready(function () {
    var url = Context.PATH + "/sys/targetweight/search.html";
    _globalAttr.dt = jQuery("#dynamic-table").DataTable({
        "paging": false,//分页
        "bInfo": false,//显示页脚
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "ajax": {
            "url": url
            , "type": "POST"
            , data: function (d) {
                d.years = $("#years").val();
            }
        },
        columns: [
            {data: "orgId"},
            {
                data: 'orgName', "mRender": function (d, t, o) {
                return o.orgName ? (d ? d : "-") : "操作";
            }
            },
            {
                data: 'targetWeightOne', "mRender": function (d, t, o) {
                return renderHtml(d, o, 'targetWeightOne', "1")
            }
            },
            {
                data: 'targetWeightTwo', "mRender": function (d, t, o) {
                return renderHtml(d, o, 'targetWeightTwo', "2")
            }
            },
            {
                data: 'targetWeightThree', "mRender": function (d, t, o) {
                return renderHtml(d, o, 'targetWeightThree', "3")
            }
            },
            {
                data: 'targetWeightFour', "mRender": function (d, t, o) {
                return renderHtml(d, o, 'targetWeightFour', "4")
            }
            },
            {
                data: 'targetWeightFive', "mRender": function (d, t, o) {
                return renderHtml(d, o, 'targetWeightFive', "5")
            }
            },
            {
                data: 'targetWeightSix', "mRender": function (d, t, o) {
                return renderHtml(d, o, 'targetWeightSix', "6")
            }
            },
            {
                data: 'targetWeightSeven', "mRender": function (d, t, o) {
                return renderHtml(d, o, 'targetWeightSeven', "7")
            }
            },
            {
                data: 'targetWeightEight', "mRender": function (d, t, o) {
                return renderHtml(d, o, 'targetWeightEight', "8")
            }
            },
            {
                data: 'targetWeightNine', "mRender": function (d, t, o) {
                return renderHtml(d, o, 'targetWeightNine', "9")
            }
            },
            {
                data: 'targetWeightTen', "mRender": function (d, t, o) {
                return renderHtml(d, o, 'targetWeightTen', "10")
            }
            },
            {
                data: 'targetWeightEleven', "mRender": function (d, t, o) {
                return renderHtml(d, o, 'targetWeightEleven', "11")
            }
            },
            {
                data: 'targetWeightTwelve', "mRender": function (d, t, o) {
                return renderHtml(d, o, 'targetWeightTwelve', "12")
            }
            }
        ],
        columnDefs: [
            {
                "targets": [0],
                "visible": false,
                "searchable": false
            }, {
                sDefaultContent: '', //解决请求参数未知的异常
                aTargets: ['_all']
            }],
        fnInitComplete: renderEdit
    });

    //编辑
    $(document).on("click", ".editWeight", function (e) {
        //月份数字
        var monnum = $(this).closest("a").attr("monnum");
        //月份英文
        var monen = $(this).closest("a").attr("monen");
        //年份
        var years = $("#years").val();

        var edit = true;
        var now = new Date();
        var nowY = now.getFullYear();
        var nowM = now.getMonth() + 1;

        if (years < nowY || monnum < nowM) edit = false;
        //取列值
        var orgIds = getDataTableColumn($("#dynamic-table"), "orgId");
        var orgNames = getDataTableColumn($("#dynamic-table"), "orgName");
        var monValues = getDataTableColumn($("#dynamic-table"), monen);
        //去掉最后一行操作行
        orgIds = orgIds.slice(0, orgIds.length - 1);
        orgNames = orgNames.slice(0, orgNames.length - 1);
        monValues = monValues.slice(0, monValues.length - 1);
        if (orgIds.length != orgNames.length || orgNames.length != monValues.length || orgIds.length != monValues.length) {
            cbms.alert("数据有误，请尝试刷新!");
            return;
        }
        var h = new Array();
        for (var i = 0; i < orgNames.length; i++) {
            h.push("<tr><td>");
            h.push("<span class='none' month='" + monen + "'>");
            h.push(orgIds[i]);
            h.push("</span>");
            h.push(orgNames[i]);
            h.push("</td>");
            h.push("<td>");
            var v=monValues[i]=="99999999"?"-":monValues[i];
            if (edit) {
                h.push("<input  type='text' class='weight' verify='nolimitweight' value='" + v+ "' />");
            } else {
                h.push("<input  type='text' readonly class='weight' verify='nolimitweight' value='" + v+ "' />");
            }
            h.push("</td>");
            h.push("</tr>");
        }
        //驼峰转下划线
        monen = monen.replace(/([A-Z])/g, "_$1").toLowerCase();
        $("#monthEdit").find("thead").find("input").remove();
        $("#monthEdit").find("thead").append("<input type='hidden' value='" + monen + "'>");
        $("#monthEdit").find("tbody").empty();
        $("#monthEdit").find("tbody").append(h.join(""));
        edit ? $("#monthEdit").find(".doEdit").removeClass("none") : $("#monthEdit").find(".doEdit").addClass("none");
        cbms.getDialog("编辑" + monnum + "月份服务中心的目标交易量", "#monthEdit");
    });

    //取消
    $(document).on("click", ".cancelEdit", function (e) {
        cbms.closeDialog();
    });

    // 确定
    $(document).on("click", ".doEdit", function (e) {
        if (setlistensSave("#weightEditForm")) {
            var data = new Array();
            var years = $("#years").val();
            var month = $("#weightEditForm").find("thead input").val();
            var trv = $("#weightEditForm").find("tbody tr");
            trv.each(function (i, e) {
                var v = {};
                v.orgId = $(e).find("span").text();
                var w= $(e).find("input").val();
                if(w&& w.indexOf("-")>=0){
                    if('-'!=w){

                    }else{
                        v.weight=99999999;
                    }
                } else {
                    v.weight =w;
                }
                v.years = years;
                v.month = month;
                data.push(v);
            });
            var url = Context.PATH + "/sys/targetweight/updateweight.html";
            $.ajax({
                type: 'post',
                url: url,
                data: JSON.stringify(data),
                contentType: "application/json",
                dataType: "json",
                success: function (data) {
                    if (data.success) {
                        reloadList();
                    }
                    cbms.alert(data.data);
                    cbms.closeDialog();
                }
            });

        }
    });
});
/**
 * 获取datatables指定列的数据数组
 * @param tableObj          datatables 对象，例：$("#dynamic-table")
 * @param columnName        datatables 列名，例: "id"
 * @returns   指定列数据数组，例:id列数据集 [1,2,3]
 */
function getDataTableColumn(tableObj, columnName) {
    return $.map(tableObj.dataTable().fnGetData(), function (val) {
        return val[columnName]?val[columnName]:"";
    });
}
/**
 * 列表重加载
 */
function reloadList() {
    _globalAttr.dt.ajax.reload(function () {
        renderEdit()
    });
}

/**
 * 添加表格最后一行
 */
function renderEdit() {
    $("#dynamic-table tbody").find("tr[role='row']").length > 0 ? _globalAttr.dt.row.add([]).draw(false) : _globalAttr.dt.clear();
}
/**
 * 渲染列表最后一行编辑行
 * @param d  单元格数据
 * @param o  一行数据
 * @param monen  对应月份英文
 * @param monnum  对应月份数字
 * @returns {string}
 */
function renderHtml(d, o, monen, monnum) {
    return o.orgName ? (d ? (d=="99999999"?"-":d) : "") : "<a style='cursor: pointer' class='editWeight' monen=\"" + monen + "\" monnum=\"" + monnum + "\">编辑</a>";
}