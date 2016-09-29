$(function () {

    initRole();

    $('#save-perm').click(function () {
        var prams = [];
        $('input.ace:checked').each(function () {
            prams.push($(this).val());
        });
        save(prams);
    });

    $(document).on("click", 'input[type="checkbox"]', function () {
        var b = $(this).is(":checked"),
            pid  = $(this).attr("data-pid"),
            v = $(this).val();

        $("body").find('input[data-pid="' + v + '"]').each(function () {
            checked($(this), b);
        });
    });

});

function checked(e, b) {
    $(e).prop("checked", b);

    var v = $(e).val(),
        child = $("body").find('input[data-pid="'+v+'"]');

    if(child != undefined && child!=null) {
        $("body").find('input[data-pid="'+v+'"]').each(function(){
            $(this).prop("checked", b);
        });
    }
}

function getRolePermission() {
    var roleId = $('#sroleHidden').val();
    if (roleId == '0') {
        $('input.ace').prop("checked", false);
    } else {
        $('input.ace').prop("checked", false);
        $.ajax({
            type: "POST",
            url: Context.PATH + '/perm/role.html',
            data: {
                "roleId": roleId
            },
            dataType: "json",
            success: function (response, textStatus, xhr) {
                if (response.success) {
                    var datas = response.data;
                    for (var i in datas) {
                        $('input.ace[value=' + datas[i].permissionId + ']').prop("checked", true);
                    }
                }
                var userInfo = $("#user").attr("rel");
                if(userInfo != null){
                    var strs= new Array(); //定义一数组
                    strs = userInfo.split("_"); //字符分割
                    var userId = strs[0];
                    var roleId = roleId;
                    $.ajax({
                        type: "POST",
                        url: Context.PATH + '/perm/user.html',
                        data: {
                            "userId": userId
                        },
                        dataType: "json",
                        success: function (response, textStatus, xhr) {
                            if (response.success) {
                                var datas = response.data;
                                for (var i in datas) {
                                    if(datas[i].type == 1) {
                                        $('input.ace[value=' + datas[i].permissionId + ']').prop("checked", true);
                                    }else if(datas[i].type == 0){
                                        $('input.ace[value=' + datas[i].permissionId + ']').prop("checked", false);
                                    }
                                }
                            }
                        }
                    });
            }

        }
        });
    }
}

function initRole() {
    $.ajax({
        type: "POST",
        url: Context.PATH + '/perm/role/init.html',
        data: {},
        dataType: "json",
        success: function (response, textStatus, xhr) {
            if (response.success) {
                $.fn.zTree.init($("#roleTree"), setting, response.data);
            }
            var userInfo = $("#user").attr("rel");
            if(userInfo != null) {
                var strs= new Array(); //定义一数组
                strs = userInfo.split("_"); //字符分割
                $("#sroleHidden").val(strs[1]);
                getRolePermission();
            }
        }
    });
}

function save(prams){
    var roleId = $('#sroleHidden').val();
    if(roleId == 0){
        cbms.alert("请选择角色");
        return false;
    }
    cbms.loading();
    var userInfo = $("#user").attr("rel");
    if(userInfo == null) {
        $.ajax({
            type: "POST",
            url: Context.PATH + '/perm/role/save.html',
            data: {
                p: prams,
                roleId: roleId
            },
            dataType: "json",
            success: function (response, textStatus, xhr) {
                cbms.closeLoading();
                if (response.success) {
                    cbms.alert("设置权限成功");
                }else{
                    cbms.alert(response.data);
                }
            },
            error: function (xhr, textStatus, errorThrown) {
                cbms.closeLoading();
            }
        });
    }else{
        var strs= new Array(); //定义一数组
        strs = userInfo.split("_"); //字符分割
        var userId = strs[0];
        $.ajax({
            type: "POST",
            url: Context.PATH + '/perm/user/save.html',
            data: {
                p: prams,
                roleId: roleId,
                userId: userId
            },
            dataType: "json",
            success: function (response, textStatus, xhr) {
                cbms.closeLoading();
                if (response.success) {
                    cbms.alert("设置权限成功");
                }else{
                    cbms.alert(response.data);
                }
            },
            error: function (xhr, textStatus, errorThrown) {
                cbms.closeLoading();
            }
        });
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

function showMenu() {
    var cityObj = $("#srole");
    var cityOffset = $("#srole").offset();
    $("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

    $("body").bind("mousedown", onBodyDown);
}
function hideMenu() {
    $("#menuContent").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDown);
}
function onBodyDown(event) {
    if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
        hideMenu();
    }
}

function beforeClick(treeId, treeNode) {
}

function onClick(e, treeId, treeNode) {
    var zTree = $.fn.zTree.getZTreeObj("roleTree"),
        nodes = zTree.getSelectedNodes(),
        v = "",vid = "";
    nodes.sort(function compare(a,b){return a.id-b.id;});
    for (var i=0, l=nodes.length; i<l; i++) {
        v += nodes[i].name + ",";
        vid+= nodes[i].id ;
    }
    if (v.length > 0 ) v = v.substring(0, v.length-1);
    var cityObj = $("#srole");
    cityObj.attr("value", v);
    $("#sroleHidden").attr("value",vid);

    hideMenu();
    getRolePermission();

    if(vid.length > 0){

    }
}