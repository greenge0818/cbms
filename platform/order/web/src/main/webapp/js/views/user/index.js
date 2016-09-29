/**
 * Created by Rabbit Mao on 2015/7/14.
 */

var add = "<p align='center'><a class='button btn-info btn-lg' id='add'>提交</a></p>";
var update = "<p align='center'><a class='button btn-info btn-lg' id='update'>提交</a></p>";
jQuery(function ($) {
    initPage();

    /** user **/
    $("table").on("click", ".lock", function () {
        var d = $(this).attr("rel");
        cbms.confirm("确定要锁定该服务中心人员？", null, function () {
            disableclick(d);
        });
    });

    $("table").on("click", ".unlock", function () {
        var d = $(this).attr("rel");
        cbms.confirm("确定要解锁该服务中心人员？", null, function () {
            activateclick(d);
        });
    });

    $("table").on("click", ".edit", function () {
        var d = $(this).attr("rel");
        cbms.getDialog("编辑服务中心人员", $("#userDialog").html());
        loadUserData(d);
    });


    $("#addUser").on(ace.click_event, function () {
        $("#userId").val("");
        cbms.getDialog("添加服务中心人员", $("#userDialog").html());
    });

    $("#addDepartment").on(ace.click_event, function () {
        cbms.getDialog("添加部门", "#department");
    });

    $(document).on("click", "#submit", function () {
        if (!setlistensSave("#editUser")) {
            return;
        }
        saveUser();
    });

    /** org **/
    $("#addOrg").on(ace.click_event, function () {
        cbms.getDialog('<a href="javascript:;" id="addOrgW">添加服务中心</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" id="addDepartmentW">添加部门</a>', $("#orgDialog").html() + add);
        bindRegionData($("#province"), $("#city"), $("#district"));
        $("#DELIVERYLETTER").attr("checked", true);   //默认选中放货函
});
    $(document).on(ace.click_event,"#addDepartmentW", function () {
        cbms.getDialog("添加部门","#department")
      
    });
    $(document).on(ace.click_event,"#addOrgW", function () {
        cbms.getDialog("添加服务中心", $("#orgDialog").html() + add);
        bindRegionData($("#province"), $("#city"), $("#district"));
        $("#DELIVERYLETTER").attr("checked", true);   //默认选中放货函
      
    });

    $("#editOrg").on(ace.click_event, function () {
        $.ajax({
            type: "POST",
            url: Context.PATH + '/org/loadByOrgId.html',
            data: {
                orgId: $("#selectOrgId").val()
            },
            dataType: "json",
            success: function (response, textStatus, xhr) {
                if (response.success) {
                    var data = response.data;
                    if(data.isOrg==1){
                        orgId = $(this).attr("rel");
                        cbms.getDialog("编辑服务中心", $("#orgDialog").html() + update);
                        bindRegionData($("#province"), $("#city"), $("#district"));
                    $("#sorganization").attr("value", null == data.parentOrgName ? "无" : data.parentOrgName);
                    $("#sorganizationHidden").attr("value", null == data.parentOrgId ? 0 : data.parentOrgId);
                    $("#orgChargerListDia").val(null == data.chargerId ? 0 : data.chargerId);
                    bindRegionData($("#province"), $("#city"), $("#district"),data.provinceId, data.cityId, data.districtId);
                    $("#orgCodeDia").val(data.seqCode);
                    $("#orgNameDia").val(data.orgName);
                    $("#orgCreditLimitDia").val(data.creditLimit);
                    $("#orgAddressDia").val(data.address);
                    $("#orgTel").val(data.tel),
                        $("#orgFaxDia").val(data.fax);
                    $("#contractAddress").val(data.contractAddress);
                    $("#invoicedHost").val(data.invoicedHost);
                    for (var i in data.deliveryTypeList) {
                        $("#" + data.deliveryTypeList[i]).attr("checked", true);
                    }
                    if(data.enabled==0){
                    	$(".enabledOrg:eq(1)").attr("checked", true);
                    }else{
                    	$(".enabledOrg:eq(0)").attr("checked", true);
                    }
                    if(data.isDraftAccepted==0){
                    	$(".isDraftAccepted:eq(1)").attr("checked", true);
                    }else{
                    	$(".isDraftAccepted:eq(0)").attr("checked", true);
                    }
                }else{
                        orgId = $(this).attr("rel");
                        cbms.getDialog("编辑部门", $("#department").html());
                        $("#departmentName").val(data.orgName);
                       $("#orgChargerList").val(null == data.chargerId ? 0 : data.chargerId);
                       if(data.enabled==0){
                       	$(".enabled:eq(1)").attr("checked", true);
                       }else{
                       	$(".enabled:eq(0)").attr("checked", true);
                       }
                       $("#submitDepartment").attr("id","updateDepartment");
                       
                    }
                }
            },
            error: function (xhr, textStatus, errorThrown) {
            }
        });

    //------------------------------------------------------
    //    orgId = $(this).attr("rel");
    //    cbms.getDialog("编辑服务中心", $("#orgDialog").html() + update);
    //    bindRegionData($("#province"), $("#city"), $("#district"));
    //    loadOrgData();
    });

    $(document).on("click", "#add", function () {
        if (!setlistensSave("#editOrg")) {
            return;
        }
        addOrg();
    });
    $(document).on("click", "#submitDepartment", function () {
        if (!setlistensSave("#formDepartment")) {
            return;
        }
        var b = $("#orgChargerList").children('option:selected').val();
        if(b==0){
            cbms.alert("请选择负责人！");
            return;
        }
        addDepartment();
    });
    
    $(document).on("click", "#updateDepartment", function () {
        if (!setlistensSave("#formDepartment")) {
            return;
        }
        var b = $("#orgChargerList").children('option:selected').val();
        if(b==0){
            cbms.alert("请选择负责人！");
            return;
        }
        updateDepartment();
    });


    $(document).on("click", "#update", function () {
        if (!setlistensSave("#editOrg")) {
            return;
        }
        updateOrg();
    });
});

/** 初始化 **/
function initPage() {
    fillDataTable();
    initRole();
    initOrganization();
    initUser();
    getOrgData();
    $("#editUser").verifyForm();
    $("#editOrg").verifyForm();
}

function initRole() {
    $.ajax({
        type: "POST",
        url: Context.PATH + '/role/roleList.html',
        data: {},
        dataType: "json",
        success: function (response, textStatus, xhr) {
            if (response.success) {
                var roles = response.data;
                $("#userRole").empty();
                $("#userRole").append('<option value ="0">请选择</option>');
                for (var i in roles) {
                    $("#userRole").append('<option value ="' + roles[i].id + '">' + roles[i].name + '</option>');
                }
            }
        }
    });
}

function initUser() {
    $.ajax({
        type: "POST",
        url: Context.PATH + '/org/loadAllUser.html',
        data: {},
        dataType: "json",
        success: function (response, textStatus, xhr) {
            if (response.success) {
                var datas = response.data;
                $("#orgChargerListDia").empty();
                $("#orgChargerList").empty();
                $("#orgChargerListDia").append('<option value ="0">无</option>');
                $("#orgChargerList").append('<option value ="0">请选择</option>');
                for (var i in datas) {
                    $("#orgChargerListDia").append('<option value ="' + datas[i].id + '">' + datas[i].name + '</option>');
                    $("#orgChargerList").append('<option value ="' + datas[i].id + '">' + datas[i].name + '</option>');
                }
            }
        },
        error: function (xhr, textStatus, errorThrown) {
        }
    });
}



function initOrganization() {
    $.ajax({
        type: "POST",
        url: Context.PATH + '/user/init/organization.html',
        data: {},
        dataType: "json",
        success: function (response, textStatus, xhr) {
            if (response.success) {
                $.fn.zTree.init($("#show"), settingShow, response.data);
            }
        }
    });
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

/** org **/
function addOrg() {
    var parentOrganization = $("#sorganizationHidden").val();
    var name = $("#orgNameDia").val();
    var provinceId = $("#province").val();
    var cityId  = $("#city").val();
    var districtId = $("#district").val();
    var code = $("#orgCodeDia").val();
    var creditLimit = $("#orgCreditLimitDia").val();
    var charger = $("#orgChargerListDia").children('option:selected').val();
    var invoicedHost = $("#invoicedHost").val();
    parentOrganization = parentOrganization == -1 ? 0 : parentOrganization;
    var checkboxes = $(".check:checked");
    var isOrg=1;
    var enabled=$('input:radio[name="enabled"]:enabled').val();
    var isDraftAccepted=$('input:radio[name="isDraftAccepted"]:checked').val();
    var types = [];
    $(checkboxes).each(function () {
        types.push($(this).val());
    });
    $.ajax({
        type: "POST",
        url: Context.PATH + '/org/addOrganization.html',
        data: {
            parentId: parentOrganization,
            name: name,
            provinceId: provinceId,
            cityId: cityId,
            districtId: districtId,
            charger: charger,
            seqCode: code,
            creditLimit: creditLimit,
            tel:$("#orgTel").val(),
            fax: $("#orgFaxDia").val(),
            address: $("#orgAddressDia").val(),
            contractAddress: $("#contractAddress").val(),
            invoicedHost: invoicedHost,
            isOrg:isOrg,
            enabled:enabled,
            deliveryTypes: types,
            isDraftAccepted:isDraftAccepted
        },
        dataType: "json",
        success: function (response, textStatus, xhr) {
            if (response.success) {
                dt.fnDraw(); //add by Rabbit 刷新但不翻页
                cbms.alert("添加服务中心成功");
                cbms.closeDialog();
                initOrganization();
            } else {
                cbms.alert(response.data);
            }
        }
    });
}

function addDepartment() {
    var parentOrganization = $("#orgwanization").val();
    var departmentName = $("#departmentName").val();
    var charger = $("#orgChargerList").children('option:selected').val();
    var isOrg=0;
    var enabled=$('input:radio[name="enabled"]:checked').val();
    $.ajax({
        type: "POST",
        url: Context.PATH + '/org/addDepartment.html',
        data: {
            parentId: parentOrganization,
            name: departmentName,
            charger: charger,
            isOrg:isOrg,
            enabled:enabled,
        },
        dataType: "json",
        success: function (response, textStatus, xhr) {
            if (response.success) {
                dt.fnDraw();  //add by Rabbit 刷新但不翻页
                cbms.alert("添加部门成功");
                cbms.closeDialog();
            } else {
                cbms.alert(response.data);
            }
        }
    });
}

function updateDepartment() {
	    var parentOrganization = $("#orgwanization").val();
	    var departmentName = $("#departmentName").val();
	    var charger = $("#orgChargerList").children('option:selected').val();
	    var isOrg=0;
	    var enabled=$('input:radio[name="enabled"]:checked').val();

    $.ajax({
    	   type: "POST",
           url: Context.PATH + '/org/updateDepartment.html',
           data: {
               parentId: parentOrganization,
               name: departmentName,
               charger: charger,
               isOrg:isOrg,
               enabled:enabled
        },
        dataType: "json",
        success: function (response, textStatus, xhr) {
            if (response.success) {
                dt.fnDraw();  //add by Rabbit 刷新但不翻页
                cbms.alert("更新部门成功");
                cbms.closeDialog();
                getOrgData();
                initOrganization();
            } else {
                cbms.alert(response.data);
            }
        }
    });
}
function updateOrg() {
    var orgId = $("#selectOrgId").val();
    var parentOrganization = $("#sorganizationHidden").val();
    var name = $("#orgNameDia").val();
    var provinceId = $("#province").val();
    var cityId  = $("#city").val();
    var districtId = $("#district").val();
    var charger = $("#orgChargerListDia").children('option:selected').val();
    var code = $("#orgCodeDia").val();
    var creditLimit = $("#orgCreditLimitDia").val();
    var invoicedHost = $("#invoicedHost").val();
    parentOrganization = parentOrganization == -1 ? 0 : parentOrganization;
    var checkboxes = $(".check:checked");
    var types = [];
    var enabled=$('input:radio[name="enabled"]:checked').val();
    var isDraftAccepted=$('input:radio[name="isDraftAccepted"]:checked').val();

    $(checkboxes).each(function () {
        types.push($(this).val());
    });
    $.ajax({
        type: "POST",
        url: Context.PATH + '/org/updateOrganization.html',
        data: {
            id: orgId,
            provinceId: provinceId,
            cityId: cityId,
            districtId: districtId,
            parentId: parentOrganization,
            name: name,
            charger: charger,
            seqCode: code,
            creditLimit: creditLimit,
            tel:$("#orgTel").val(),
            fax: $("#orgFaxDia").val(),
            address: $("#orgAddressDia").val(),
            contractAddress: $("#contractAddress").val(),
            invoicedHost: invoicedHost,
            deliveryTypes: types,
            enabled:enabled,
            isDraftAccepted:isDraftAccepted
        },
        dataType: "json",
        success: function (response, textStatus, xhr) {
            if (response.success) {
                dt.fnDraw();  //add by Rabbit 刷新但不翻页
                cbms.alert("更新服务中心成功");
                cbms.closeDialog();
                getOrgData();
                initOrganization();
            } else {
                cbms.alert(response.data);
            }
        }
    });
}
function getOrgData() {     //加载服务中心详情，点击Tree时触发
    $.ajax({
        type: "POST",
        url: Context.PATH + '/org/loadByOrgId.html',
        data: {
            orgId: $("#selectOrgId").val()
        },
        dataType: "json",
        success: function (response, textStatus, xhr) {
            if (response.success) {
                var data = response.data;
                $("#orgName").empty().append(data.orgName);
                $("#orgCharger").empty().append(null == data.chargerName ? "无" : data.chargerName);
                $("#orgCode").empty().append(data.seqCode);
                $("#orgCreditLimit").empty().append(data.creditLimit);
                $("#orgUserCount").empty().append(data.userCount);
                var status = data.status == 1 ? "正常" : "锁定";
                $("#orgStatus").empty().append(status);
                $("#orgDeliveryType").empty();
                for (var i in data.deliveryTypeList) {
                    $("#orgDeliveryType").append("<span>" + renderDeliveryType(data.deliveryTypeList[i]) + "</span>");
                }
            }
        },
        error: function (xhr, textStatus, errorThrown) {
        }
    });
}

function renderDeliveryType(type) {
//    DELIVERY_LETTER("DELIVERYLETTER", "放货函"),
//    DELIVERY_ORDER("DELIVERYORDER", "放货单"),
//    TRANSFER_ORDER("TRANSFERORDER", "货权转让单"),
//    TRANSFER_LETTER("TRANSFERLETTER", "货权转让函");
    var str;
    switch (type) {
        case "DELIVERYLETTER":
            str = "放货函";
            break;
        case "DELIVERYORDER":
            str = "放货单";
            break;
        case "TRANSFERORDER":
            str = "货权转让单";
            break;
        case "TRANSFERLETTER":
            str = "货权转让函";
            break;
        default:
            str = "";
    }
    return str;
}

/** end of org**/


/** user **/
function saveUser() {
    var id = $("#userId").val();
    var name = $("#userName").val();
    var role = $("#userRole").children('option:selected').val();
    var tel = $("#userTel").val();
    var loginId = $("#userLoginId").val();
    var jobNumber = $("#userJobNumber").val();
    var org_id = $("#sorganizationHidden").val();
    if (org_id <= 0) {
        cbms.alert("服务中心不能为空");
        return false;
    }

    var url;
    if (id == null || id == "") {
        url = '/user/save.html';
        id = null;
    } else {
        url = '/user/update.html';
    }
    $.ajax({
        type: "POST",
        url: Context.PATH + url,
        data: {
            id: id,
            orgId: org_id,
            name: name,
            roleId: role,
            tel: tel,
            loginId: loginId,
            jobNumber: jobNumber
        },
        dataType: "json",
        success: function (response, textStatus, xhr) {
            if (response.success) {
                dt.fnDraw(); //add by Rabbit 刷新但不翻页
                cbms.closeDialog();
                if (id == null || id == "") {
                    cbms.alert("添加用户成功");
                } else {
                    cbms.alert("编辑成功");
                }
                getOrgData();
            } else {
                cbms.alert(response.data);
            }
        }
    });
}

function loadUserData(id) {
    $.ajax({
        type: "POST",
        url: Context.PATH + '/user/edit/' + id + '.html',
        data: {},
        dataType: "json",
        success: function (response, textStatus, xhr) {
            if (response.success) {
                var data = response.data;
                $("#userName").val(data.userName);
                $("#userLoginId").val(data.loginId);
                $("#userLoginId").attr("readonly", "readonly");
                $("#userJobNumber").val(data.jobNumber);
                $("#userJobNumber").attr("readonly", "readonly");
                $("#userTel").val(data.tel);
                $("#userRole").val(null == data.roleId ? 0 : data.roleId);
                $("#userId").val(data.userId);
                $("#sorganization").attr("value", data.orgName);
                $("#sorganizationHidden").attr("value", data.orgId);
            } else {
                cbms.alert("加载当前用户数据失败，请重试");
            }
        },
        error: function (xhr, textStatus, errorThrown) {
        }
    });
}

function activateclick(id) {
    $.ajax({
        type: "POST",
        url: Context.PATH + '/user/activate/' + id + '.html',
        data: {},
        dataType: "json",
        success: function (response, textStatus, xhr) {
            if (response.success) {
                cbms.alert("解锁成功");
                dt.fnDraw();  //add by Rabbit 刷新但不翻页
                getOrgData();
            }
        },
        error: function (xhr, textStatus, errorThrown) {
        }
    });
}

function disableclick(id) {
    $.ajax({
        type: "POST",
        url: Context.PATH + '/user/disable/' + id + '.html',
        data: {},
        dataType: "json",
        success: function (response, textStatus, xhr) {
            if (response.success) {
                cbms.alert("锁定成功");
                 dt.fnDraw(); //add by Rabbit 刷新但不翻页
                getOrgData();
            }
        },
        error: function (xhr, textStatus, errorThrown) {
        }
    });
}

/** end of user **/
var dt;
function fillDataTable() {
    dt = $('#dynamicTable').dataTable({
        ajax: {
            url: Context.PATH + '/user/list.html',
            type: "POST",
            data: function (d) {
                return $.extend({}, d, {
                    orgId: $("#selectOrgId").val(),
                    status: $("#orgcheck").is(":checked")
                });
            }
        },
        searching: false,
        processing: false,
        serverSide: true,
        ordering: false,
        bLengthChange: false,
        fnRowCallback: function (nRow, aData, iDataIndex) {

            var html1 = '<div class="hidden-sm hidden-xs action-buttons"><a rel="' + aData.userId + '" class="edit" data-rel="tooltip" title="修改用户信息"> <span class="green"> <i class="ace-icon fa fa-edit bigger-120"></i> </span> </a>';
            var html2 = '<a href="../perm/index/' + aData.userId + '.html" class="tooltip-success" data-rel="tooltip" title="修改用户权限"> <span class="green"> <i class="ace-icon fa fa-pencil bigger-120"></i> </span> </a><a href="' + Context.PATH + '/sys/oplog/index.html?userId=' + aData.userId + '" class="tooltip-info" data-rel="tooltip" title="查看用户操作记录"><span class="blue"><i class="ace-icon fa fa-search-plus bigger-120"></i></span></a></div><div class="hidden-md hidden-lg"><div class="inline pos-rel"><button class="btn btn-minier btn-yellow dropdown-toggle" data-toggle="dropdown" data-position="auto"><i class="ace-icon fa fa-caret-down icon-only bigger-120"></i></button><ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close"><li><a rel="' + aData.userId + '"  class="edit" data-rel="tooltip" title="修改用户信息"> <span class="green"> <i class="ace-icon fa fa-edit bigger-120"></i> </span> </a> </li>';
            var html3 = '<li><a href="' + Context.PATH + '/sys/oplog/index.html?userId=' + aData.userId + '" class="tooltip-info" data-rel="tooltip" title="查看用户操作记录"><span class="blue"><i class="ace-icon fa fa-search-plus bigger-120"></i></span></a> </li><li><a href="../perm/index/' + aData.userId + '.html" class="tooltip-success" data-rel="tooltip" title="修改用户权限"> <span class="green"> <i class="ace-icon fa fa-pencil bigger-120"></i> </span> </a> </li> </ul> </div> </div>';

            if (aData.status === 1) {
                $('td:eq(-1)', nRow).html(html1 + '<a rel="' + aData.userId + '"  class="red lock" title="锁定" href="javascript:;"><i class="ace-icon fa fa-lock bigger-130"></i></a>' + html2 + '<li><a  rel="' + aData.userId + '" class="red lock" title="锁定" href="javascript:;"><i class="ace-icon fa fa-lock bigger-130"></i></a></li>' + html3);
            } else if (aData.status === 0) {
                $('td:eq(-1)', nRow).html(html1 + '<a rel="' + aData.userId + '" class="green unlock" title="解锁" href="javascript:;"><i class="ace-icon fa fa-unlock bigger-130"></i></a>' + html2 + '<li><a rel="' + aData.userId + '" class="green unlock" title="解锁" href="javascript:;"><i class="ace-icon fa fa-unlock bigger-130"></i></a></li>' + html3);
            }
            return nRow;
        },
        columns: [
            {data: 'userName', sClass: "text-center"},
            {data: 'tel', sClass: "text-center"},
            {data: 'roleName', sClass: "text-center"},
            {data: 'status', sClass: "text-center"},
            {defaultContent: '', sClass: "text-center"}
        ],
        "oLanguage": {                          //汉化
            "sLengthMenu": "每页显示 _MENU_ 条记录",
            "sZeroRecords": "没有检索到数据",
            "sInfo": "当前数据为从第 _START_ 到第 _END_ 条数据；总共有 _TOTAL_ 条记录",
            "sInfoEmtpy": "没有数据",
            "sProcessing": "正在加载数据...",
            "oPaginate": {
                "sFirst": "首页",
                "sPrevious": "上一页",
      			"sNext": "下一页",
                "sLast": "尾页"
            }
        },
        columnDefs: [
            {
                "targets": 3, //第几列 从0开始
                "data": "status",
                "render": renderStatus
            },
            {
                sDefaultContent: '', //解决请求参数未知的异常
                aTargets: ['_all']
            }
        ]
    });

    $("#orgcheck").click(function(){
        dt.fnDraw();
    });

}

function renderStatus(status) {
    if (status == 1) return '<span>正常</span>';
    else if (status == 0) return '<span class="red">锁定</span>';
}

var settingShow = {
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
    var zTree = $.fn.zTree.getZTreeObj("show"),
        nodes = zTree.getSelectedNodes(),
        vid = "";
    nodes.sort(function compare(a, b) {
        return a.id - b.id;
    });
    for (var i = 0, l = nodes.length; i < l; i++) {
        vid += nodes[i].id;
    }
    $("#selectOrgId").val(vid);
    getOrgData();
    dt.fnDraw();  //add by Rabbit 刷新但不翻页
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
        onClick: onClickTree
    }
};

function onClickTree(e, treeId, treeNode) {
    var zTree = $.fn.zTree.getZTreeObj("tree"),
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

