/**
 * Created by dengxiyan on 2015/8/19.
 */


jQuery(function ($) {
    //settings default
    //$.extend($.fn.dataTable.defaults, {
    //    "pageLength": 15, //每页记录数
    //    "processing": true,//显示数据加载进度
    //    "serverSide": true, //服务模式
    //    "searching": false, //是否启用搜索
    //    "ordering": false, //是否启用排序
    //    "lengthChange": false, //不显示pageSize的下拉框
    //    "oLanguage": {sUrl: Context.PATH + "/js/DT_zh.txt"} //自定义语言包
    //});
});


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

function showMenu(o) {

    var cityObj = $("#sorganization");
    var cityOffset = $("#sorganization").position();
    $(o).closest("span.pos-rel").append($("#menuContent"));
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

    cityObj.val(v);
    $("#sorganizationHidden").val(vid);
    hideMenu();
}
/*********tree e ***********/


/********render*******/
function renderType(data){
    if(data == 'consign'){
        return "品牌店";
    }else if(data == 'temp'){
       return "临采"
    }
    return "";
}

function renderWeight(data) {
    if (data) {
        return parseFloat((data + "").replace(/[^\d\.-]/g, "")).toFixed(6) + "";
    }
    return "0.000000";
}

function renderPercent(num1,num2){
    if(num2){
        return ((num1/num2) * 100).toFixed(2) + "%";
    }
    return "";
}