jQuery(function ($) {
    initOrganization();
    initUserChangeEvent();
    initSaveButton();
});

function initUserChangeEvent(){
	$("#userIds").change(function(){
		var id=$(this).val();
		$("#userId").val(id);
		$("input[name='orgIds']").attr("checked",false);
	});
}

function initOrganization() {
    $.ajax({
        type: "POST",
        url: Context.PATH + '/common/organizationList.html',
        async: false,
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
        url: Context.PATH + '/user/query.html',
        data: {
            orgId: orgId,
            forUserOrgConfig:true
        },
        dataType: "json",
        success: function (response, textStatus, xhr) {
            if (response.success) {
                $("#userIds").empty();
                var datas = response.data;
                for (var i in datas) {
                    $("#userIds").append("<option value='" + datas[i].id +"'>" + datas[i].name + "</option>");
                }
                $("#userIds").change();
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

function showMenu(obj) {
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

function initSaveButton(){
	$(document).on("click", "#saveInfoBtn", function () {
		cbms.loading();
		$.ajax({
	        type: 'post',
	        url: Context.PATH + "/sys/userorg/save.html",
	        data: $("#form1").serialize(),
	        error: function (s) {
	            $("#saveInfoBtn").prop("disabled", false);
	            cbms.closeLoading();
	        }
	        , success: function (result) {
	            cbms.closeLoading();
	            if (result) {
	                if (result.success) {
	                   cbms.alert(result.data,function(){
	                   	location.reload();
	                   });
	                }else {
	                    $("#saveInfoBtn").prop("disabled", false);
	                    cbms.alert(result.data);
	                }
	            } else {
	                $("#saveInfoBtn").prop("disabled", false);
	                cbms.alert("提交失败");
	            }
	        }
	    });
	});
}
