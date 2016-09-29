/**
 * 查询用户
 * a标签添加 class="menuBtn" 即可
 * 示例：
 * <a class="menuBtn" href="javascript:;" >选择</a>
 * Created by dengxiyan on 2016/2/26.
 */

var userOrgCacheData = null;
var orgIdCacheData = -1;
var userOrgLayerId = "userOrgLayerId";
var orgControlId="sorganizationHidden"; //服务中心控件ID
var zTree = null;
$().ready(function () {
    $(document).on("click", ".menuBtn", function () {
        getUserOrg($(this));
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
	if(typeof(_orgId) != "undefined" ){
		orgId =  _orgId;
	}
    if (userOrgCacheData == null || userOrgCacheData == "" || orgIdCacheData != orgId) {
        var callback = function (obj) {
            loadUserOrgTree(userOrgCacheData);
            showUserOrgLayer(obj);
        };
        loadUserData(orgId,callback,inputBox);
    }
    else {
        loadUserOrgTree(userOrgCacheData);
        showUserOrgLayer(inputBox);
    }
}

function loadUserData(orgId,callback,inputBox){
    $.ajax({
        type: 'post',
        url: Context.PATH + "/contact/getTradersBusinessOrg.html",
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

// 显示交易员弹层在对应的输入框下
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
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "parentId",
            rootPId: 0
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
    var userOrgHtml = "<div class='ztree menuContent' style='position: absolute;max-height:300px;overflow-y:auto;overflow-x:hidden' id='" + userOrgLayerId + "'></div>";
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
        //获得事件源对象
        var inputId = $("#" + treeId).attr("inputId");
        var inputbox = $("a[inputid='" + inputId + "']");
        var $setBar = inputbox.closest(".parentLimit").find(".first");
        var $jyyBars = inputbox.closest(".parentLimit").find(".jyy-bar");
        //控制个数在5个以内
        if($jyyBars.length > 4){
            return;
        }
        //控制重复添加
        var $span = inputbox.closest(".parentLimit").find('span[class="jyy-bar"][rel="'+id+'"]');
        if($span.length > 0){
            return;
        }

        var ele = '<span class="jyy-bar" rel="'+id+'">'+name+'<a href="javascript:;" class="del-jyy"><i class="red fa fa-times"></i></a></span>';
        $setBar.after(ele);
    }
}

function beforeClick(treeId, treeNode) {
    var check = (treeNode && !treeNode.isParent);
    if (!check) alert("只能选择交易员");
    return check;
}

// 控制浮层显示隐藏
function controlLayerShow(showLayer) {
    $(".product-complete,#showLayer_nsortName,.menuContent").hide();
    $(showLayer).show();
}