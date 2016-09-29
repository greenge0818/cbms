jQuery(function ($) {
    initOrganization();
    initOperatorSelection();
    initUserChangeEvent();
    initUserChangeForCreate();
});

var selectedInput;
function initUserChangeEvent(){
	$("select[name='userIds']").change(function(){
		var id=$(this).val();
		var _this = $(this);
		$.ajax({
	        type: "POST",
	        url: Context.PATH + '/user/queryById.html',
	        data: {id:id},
	        dataType: "json",
	        success: function (response, textStatus, xhr) {
	            if (response.success) {
	            	var user = response.data;
	            	var index = $(_this).closest("tr").index();
	            	var oldOperatorId = $(_this).parent().find("input[name='operatorId']").val();
	            	$(_this).parent().find("input[name='operatorId']").val(user.id);
	            	$(_this).parent().find("input[name='operatorName']").val(user.name);
	            	$(_this).closest("tr").find("input[name='operatorMobile']").val(user.tel);
	            	var operatorInfo = $(_this).closest("tr").find("td:eq(1)"),
	            		operatorTel = $(_this).closest("tr").find("td:eq(2)");
	            	console.log($(operatorInfo).html());
	            	cbms.confirm("是否将之前的流程中相同操作人一并更新？","",function(){
	                	$("#dynamic-table tbody tr").each(function(i,e){
	                		if(i==index){
	                			return;
	                		}
	                		var operatorId = $(this).find("input[name='operatorId']").val();
	                		if(oldOperatorId==operatorId){
	                			$(this).closest("tr").find("td:eq(1)").replaceWith($(operatorInfo).clone(true));
	                			$(this).closest("tr").find("select[name='userIds']").val(id)
	                			$(this).closest("tr").find("td:eq(2)").replaceWith($(operatorTel).clone(true));
	                		}
	                	});
	                });
	            }
	        }
	    });
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
            orgId: orgId
        },
        async: false,
        dataType: "json",
        success: function (response, textStatus, xhr) {
            if (response.success) {
                var userIds = $(selectedInput).parent().find("[name='userIds']");
                var operatorId = $(selectedInput).parent().find("input[name='operatorId']").val();
                userIds.empty();
                var datas = response.data;
                $(userIds).append("<option value=''>--请选择--</option>");
                for (var i in datas) {
                    $(userIds).append("<option value='" + datas[i].id + "'"+(operatorId==datas[i].id?"selected":"")+">" + datas[i].name + "</option>");
                }
                //$(userIds).change();
            }
        },
        error: function (xhr, textStatus, errorThrown) {
        }
    });
}

function initOperatorSelection(){
	$("#dynamic-table tbody tr").each(function(i,e){
		selectedInput = $(this).find("input[name='sorganization']");
		var orgName = $(selectedInput).val();
		$("#menuContent li a").removeClass("curSelectedNode");
		$("#menuContent li a[title='"+orgName+"']").click();
	});
}


function initUserChangeForCreate(){
	$("select#userId").change(function(){
		var userId=$(this).val();
		$.ajax({
	        type: "POST",
	        url: Context.PATH + '/sys/busiprocess/getProcessForUnsetUser.html',
	        data: {userId:userId},
	        dataType: "json",
	        success: function (response, textStatus, xhr) {
	            if (response.success) {
	            	var data = response.data;
	            	$("#dynamic-table tbody").empty();
	            	$(data).each(function(i,e){
	            		var tr='<tr><td><input type="hidden" name="orgId" value="'+e.orgId+'"/>'
	            			+'		<input type="hidden" name="orgName" value="'+e.orgName+'"/>'
	            			+'		<input type="hidden" name="userId" value="'+e.userId+'"/>'
	            			+'		<input type="hidden" name="userName" value="'+e.userName+'"/>'
	            			+'		<input type="hidden" name="type" value="'+e.type+'"/>'
	            			+'		<input type="hidden" name="orderStatusCode" value="'+e.orderStatusCode+'"/>'
	            			+'		<input type="hidden" name="orderStatusName" value="'+e.orderStatusName+'"/>'
	            			+'		'+e.orderStatusName+'</td>'
	            			+'	<td><input type="hidden"name="operatorId" value="'+e.operatorId+'"/>'
	            			+'		<input type="hidden" name="operatorName" value="'+e.operatorName+'"/>'
	            			+'		<input placeholder="请选择服务中心" must=\'1\' name="sorganization" type="text"'
	            			+'									   class="ipt-text" readonly="" value="'+e.operatorOrgName+'"'
	            			+'									   onclick="showMenu(this); style=\'width: 200px\'"/>'
	            			+'		<select name="userIds"/>'
	            			+'	</td>'
	            			+'	<td><input name="operatorMobile" value="'+e.operatorMobile+'" verify="mobile"/></td>'
	            			+'	<td><input type="" name="operatorRoleName" value="'+e.operatorRoleName+'"/></td>'
	            			+'</tr>';
	            		$("#dynamic-table tbody").append(tr);
	            	});
	            	initOperatorSelection();
	            }
	        }
	    });
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
    $(selectedInput).parent().find("input[name='sorganization']").val(v);
    $(selectedInput).parent().find("input[name='sorganizationHidden']").val(vid);
    hideMenu();
    getUserIds(vid);
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