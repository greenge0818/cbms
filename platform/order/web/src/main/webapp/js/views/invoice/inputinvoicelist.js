/**
 * Created by dengxiyan on 2015/8/4.
 */
var dt;
jQuery(function ($) {
    initTable();
    initClickEvent();
});

function initTable() {
    dt = jQuery("#dynamic-table").DataTable({
        "pageLength": 15, //每页记录数
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "lengthChange": false, //不显示pageSize的下拉框
        "oLanguage": {sUrl: Context.PATH + "/js/DT_zh.txt"}, //自定义语言包
        "ajax": {
            "url": Context.PATH + "/invoice/out/loadinputinvoiceoutdata.html"
            , "type": "POST"
            , data: function (d) {
                d.buyerName = $("#buyerName").val();
                d.orgId = $("#sorganizationHidden").val();
                d.createdStartTime = $("#startTime").val();
                d.createdEndTime = $("#endTime").val();
                d.status = "0";//待审核
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'buyerName'},
            {data: 'totalWeight'},
            {data: 'totalAmount'},
            {data: 'createdStr'}
            , {defaultContent: ''}
            , {defaultContent: ''}
        ]

        , columnDefs: [
            {
                "targets": 1, //第几列 从0开始
                "data": "totalWeight"
                , "render": function (data, type, full, meta) {
                return data.toFixed(4);//保留两位小数
            }
            }, {
                "targets": 2, //第几列 从0开始
                "data": "totalAmount"
                , "render": function (data, type, full, meta) {
                    return formatMoney(data, 2);//按千分位 四舍五入保留2位小数
                }
            }
            ,
            {
                "targets": 4, //第几列 从0开始
                "data": "status"
                , "render": function (data, type, full, meta) {
                return "待录入";
            }
            }
            , {
                "targets": 5, //第几列 从0开始
                "data": "buyerId"
                , "render": function (data, type, full, meta) {
                    if($('#inputinvoiceinfo').val()=="true") {
                        var param = "ids=" + full.ids + "&orgId=" + full.orgId + "&buyerId=" + full.buyerId;
                        var url = Context.PATH + "/invoice/out/inputinvoiceinfo.html?" + param;
                        return "<a href='" + url + "'>录入发票号</>";
                    } else {
                        return "";
                    }
                }
            }
        ]
    });
}

function initClickEvent() {
    //搜索事件
    $("#queryBtn").click(function () {
        dt.ajax.reload();
    });
}

/********* tree s ***********/
$.ajax({
    type: "POST",
    url: Context.PATH + '/common/organizationList.html',
    data: {},
    dataType: "json",
    success: function (response, textStatus, xhr) {
        if (response.success) {
            var data = response.data;
            $.fn.zTree.init($("#orgTree"), setting, data);

        }
    },
    error: function (xhr, textStatus, errorThrown) {
    }
});

function showMenu() {
    var cityObj = $("#sorganization");
    var cityOffset = $("#sorganization").offset();
    $("#menuContent").css({
        left: cityOffset.left + "px",
        top: cityOffset.top + cityObj.outerHeight() + "px"
    }).slideDown("fast");

    $("body").bind("mousedown", onBodyDown);
}
function hideMenu() {
    $("#menuContent").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDown);
}
function onBodyDown(event) {
    if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length > 0)) {
        hideMenu();
    }
}
var setting = {
    view: {
        dblClickExpand: false
    },
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "parentId",
            rootPId: 0
        }
    },
    callback: {
        beforeClick: beforeClick,
        onClick: onClick
    }
};

function beforeClick(treeId, treeNode) {
}

function onClick(e, treeId, treeNode) {
    var zTree = $.fn.zTree.getZTreeObj("orgTree"),
        nodes = zTree.getSelectedNodes(),
        v = "", vid = "";
    nodes.sort(function compare(a, b) {
        return a.id - b.id;
    });
    for (var i = 0, l = nodes.length; i < l; i++) {
        v += nodes[i].name + ",";
        vid += nodes[i].id;
    }
    if (v.length > 0) v = v.substring(0, v.length - 1);
    var cityObj = $("#sorganization");
    cityObj.attr("value", v);
    $("#sorganizationHidden").attr("value", vid);

    if (vid.length > 0) {

    }
}
/*********tree e ***********/