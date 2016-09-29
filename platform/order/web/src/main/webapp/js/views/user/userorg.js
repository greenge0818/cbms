/**
 * 查询用户
 * 输入框添加 search='user' 属性即可
 * 示例：
 * <input type='text' search='userorg' />
 * Created by lcw on 2015/7/29.
 */

var userOrgCacheData = null;
var orgIdCacheData =4 -1;
var userOrgLayerId = "userOrgLayerId";
var orgControlId="sorganizationHidden"; //服务中心控件ID
var userOrgType="userOrgType";//用户类型
var zTree = null;
$().ready(function () {
    $(document).on("focus", "input[search='userorg']", function () {
        getUserOrg($(this));

        setTimeout(function() {
            $(".menuContent").css({"overflow": "auto", "height": "350px", "overflow-x": "hidden"});
        },"200");

    })
    $(document).on("click",".curSelectedNode",function(){
        //  $(".menuContent").css( { "overflow": "auto","height": "350px","overflow-x":"hidden"});
        var tit =  $(this).attr("title");
        $("#ownerId").val(tit);
    })


    //文本内容被改动，清除选项
    $(document).on("keyup", "input[search='userorg']", function () {
        $(this).attr("userid", 0);
    })

    // 点击页面其他地方隐藏该div
    $(document).mousedown(function (e) {
        var event = e || window.event;
        var targetId = $(event.target).attr("id");
        if (targetId != userOrgLayerId) {
            $("#" + userOrgLayerId).hide();
        }
    });

    // 阻止事件继续冒泡
    $(document).on("mousedown", "#" + userOrgLayerId, function (e) {
        var event = e || window.event;
        event.stopPropagation();
    });
});

function getUserOrg(inputBox) {
    var orgId = -1;
    if ($("#" + orgControlId).length > 0) {
        orgId = $("#" + orgControlId).val();
    }

//	if(orgId==-1||orgId==""){
//    	cbms.alert("请先选择服务中心");
//    	return false;
//    }    
    var roleType = "";
    if ($("#" + userOrgType).length > 0) {
        roleType = $("#" + userOrgType).val();
    }
    if (userOrgCacheData == null || userOrgCacheData == "" || orgIdCacheData != orgId) {

        var callback = function (obj) {
            loadUserOrgTree(userOrgCacheData);
            showUserOrgLayer(obj);
        };
        loadUserData(orgId,roleType,callback,inputBox);
    }
    else {
        loadUserOrgTree(userOrgCacheData);
        showUserOrgLayer(inputBox);
    }
}

function loadUserData(orgId,roleType,callback,inputBox){
    $.ajax({
        type: 'post',
        url: Context.PATH + "/user/getuserorg.html",
        data: {orgId: orgId,roleType:roleType},
        dataType: "json",
        error: function (s) {
        }
        , success: function (result) {
            if (result) {
                if (result.success) {
                    userOrgCacheData = result.data;
                    orgIdCacheData = orgId;
                    callback(inputBox);
                } else {
                    cbms.alert(result.data);
                }
            } else {
                cbms.alert("查询失败");
            }
        }
    });
}

function loadCurrentUser(inputBox) {
    var orgId = -1;
    if ($("#" + orgControlId).length > 0) {
        orgId = $("#" + orgControlId).val();
    }
    var roleType = "";
    if ($("#" + userOrgType).length > 0) {
        roleType = $("#" + userOrgType).val();
    }
    if (userOrgCacheData == null || userOrgCacheData == "" || orgIdCacheData != orgId) {
        var callback = function (obj) {
            selectCurrentUser(obj);
        };
        loadUserData(orgId,roleType,callback,inputBox);
    }
    else {
        selectCurrentUser(inputBox);
    }
}
function selectCurrentUser(inputBox) {
    for (var i = 0; i < userOrgCacheData.length; i++) {
        if (userOrgCacheData[i].name == inputBox.val()) {
            inputBox.attr("userid", userOrgCacheData[i].id - 1000);
        }
    }
}

// 显示品名弹层在对应的输入框下
function showUserOrgLayer(inputBox) {
    // 通过唯一属性 inputId 来关联
    var showLayer = $("#" + userOrgLayerId);
    var inputId = "userOrg" + new Date().getTime();
    $(inputBox).attr("inputid", inputId);
    $(showLayer).attr("inputid", inputId);

    setLayerPosition($(inputBox), $(showLayer));
    controlLayerShow($(showLayer));
}

var userOrgSetting = {
    check: {
        enable: true
    },
    /*data: {
     simpleData: {
     enable: true
     }
     }*/
    data: {
        simpleData: {
            enable: true
        }
    },
    view: {
        dblClickExpand: false
    },
    callback: {
        beforeClick: beforeClick,
        onClick: onClick
    }

};

function loadUserOrgTree(zNodes) {
    $("#" + userOrgLayerId).remove();
    var userOrgHtml = "<div class='ztree menuContent' style='position:absolute;' id='" + userOrgLayerId + "'></div>";
    $("body").append(userOrgHtml);
    zTree = $.fn.zTree.init($("#" + userOrgLayerId), userOrgSetting, zNodes);
}

function onClick(e, treeId, treeNode) {
    if (treeNode.isParent) {
        if (treeNode.open)
            zTree.expandNode(treeNode, false);
        else
            zTree.expandNode(treeNode, true);
        return false;
    }
    var id = parseInt(treeNode.id);
    if (id >= 1000) {
        id = id - 1000;     // 防止ID重复，查询的时候ID+1000
        var name = treeNode.name;
        var inputId = $("#" + treeId).attr("inputId");
        var inputbox = $("input[inputid='" + inputId + "']");
        $(inputbox).val(name).attr("userid", id);
        $("#" + treeId).remove();
    }
}

function beforeClick(treeId, treeNode) {
    //var check = (treeNode && !treeNode.isParent);
    //if (!check) alert("只能选择业务员");
    //return check;
}

// 控制浮层显示隐藏
function controlLayerShow(showLayer) {
    $(".product-complete,#showLayer_nsortName,.menuContent").hide();
    $(showLayer).show();
}