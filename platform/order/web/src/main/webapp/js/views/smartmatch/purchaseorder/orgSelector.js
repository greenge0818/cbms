jQuery(function ($) {
    initOrganization();
});

var selectedInput;
function initOrganization() {
    $.ajax({
        type: "POST",
        url: Context.PATH + '/common/organizationList.html',
        async: false,
        dataType: "json",
        success: function (response, textStatus, xhr) {
            if (response.success) {
                $.fn.zTree.init($("#orgTree"), settingTree, response.data);
            }
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
    var zTree = $.fn.zTree.getZTreeObj("orgTree"),
        nodes = zTree.getSelectedNodes(),
        vid = nodes[0].id,
        v = nodes[0].name,
    	cityId = nodes[0].cityId,
    	cityName= nodes[0].cityName;
    $("#sorganization").val(v);
    $("#sorganizationHidden").val(vid).attr("cityId",cityId);
    $("input[search='userorg']").val("").attr("userid", "");
    $("#deliveryGoods").val(cityName).attr("val",cityId);
	getRefCitys($("#deliveryGoods"));
    hideMenu();
   
}

function showMenu(obj) {
	selectedInput = obj;
    var offset = $(obj).offset();
    $("#menuContent").css({
        left: offset.left + "px",
        top: offset.top + $(obj).outerHeight() + "px"
    }).slideDown("fast");
    $("#menuContent li a").removeClass("curSelectedNode");
    $("#menuContent li a[title='"+$(obj).val()+"']").addClass("curSelectedNode");
    
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