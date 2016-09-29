jQuery(function ($) {
    initOrganization();
});

function initOrganization() {
    $.ajax({
        type: "POST",
        url: Context.PATH + '/common/organizationList.html',
        data: {},
        dataType: "json",
        success: function (response, textStatus, xhr) {
            if (response.success) {
                $.fn.zTree.init($("#tree"), settingTree, response.data);
            }
        }
    });
}

function getUserIds(orgId) {
    $.ajax({
        type: "POST",
        url: Context.PATH + '/account/contact/userIds.html',
        data: {
            orgId: orgId
        },
        dataType: "json",
        success: function (response, textStatus, xhr) {
            if (response.success) {
                $("#userId").empty();
                var datas = response.data;
                for (var i in datas) {
                    $("#userId").append("<option value='" + datas[i].id + "' nameNext='" + datas[i].name +"'>" + datas[i].name + "</option>");
                }
            }
        },
        error: function (xhr, textStatus, errorThrown) {
        }
    });
}

var settingTree = {
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
        onClick: onClickShow
    }
};


function onClickShow(e, treeId, treeNode) {
    var zTree = $.fn.zTree.getZTreeObj("tree"),
        nodes = zTree.getSelectedNodes(),
        vid = nodes[0].id,
        v = nodes[0].name;
    $("#sorganization").attr("value", v);
    $("#sorganizationHidden").attr("value", vid);
    hideMenu();
    getUserIds(vid);
}

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