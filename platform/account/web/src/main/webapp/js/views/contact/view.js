var dt;
var _orgId = -1;
var _add_repeat_submit = false;
jQuery(function ($) {
    initTable();

    initClickEvent();

    setOrgId();


});

function initTable() {
    var accountId = $("#accountId").val();
    var url = Context.PATH + "/contact/" + accountId + "/loadDeptsAndContacts.html";
    dt = $("#dynamic-table").DataTable({
        ajax: {
            url: url,
            type: "POST",
            data: function (d) {
            }
        },
        serverSide: true, //服务模式
        processing: true,//显示数据加载进度
        searching: false, //是否启用搜索
        ordering: false, //是否启用排序
        lengthChange: false, //不显示pageSize的下拉框
        oLanguage: {sUrl: Context.PATH + "/js/DT_zh.txt"}, //自定义语言包
        bFilter: false,
        bLengthChange: false, //不显示每页长度的选择条
        bPaginate: false,  //不显示分页器
        bInfo: false,//是否显示表格的一些信息
        columns: [{defaultContent: ''}],
        columnDefs: [{
            sDefaultContent: '', //解决请求参数未知的异常
            aTargets: ['_all']
        }],
        fnRowCallback: function (nRow, aData, iDataIndex) {
            //初始化每一行
            var childTable = $("#contacts").clone().removeClass("none").removeAttr("id");//移除ID并显示
            InitChildBlock(aData, childTable);
            $('td:eq(0)', nRow).html(childTable);
        }
    });
}

function InitChildBlock(jsonData, itemsBlock) {
    //部门id及名称初始化
    itemsBlock.find(".dept-id").val(jsonData.id);
    itemsBlock.find(".dept-name").html(jsonData.name);
    var  head="";
    if(jsonData.deptFax!=null){
     head="部门传真："+jsonData.deptFax}
    itemsBlock.find(".dept-fax").html(head);
    var spanTest = (jsonData.status == '1' ? '<i class="fa fa-unlock-alt">'+'锁定' : '<i class="fa fa-unlock">'+'解锁');
    itemsBlock.find(".dept-del").html(spanTest);
    itemsBlock.find(".dept-del").attr("status",jsonData.status);

    $(itemsBlock).find(".table").DataTable({
        aaData: jsonData.contacts,

        columns: [
            {sTitle: "姓名", data: 'name'},
            {sTitle: "手机", data: 'tel'},
            {sTitle: "QQ", data: 'qq'},
            {sTitle: "邮箱", data: 'email'},
            {sTitle: "状态", data: 'status'},
            {sTitle: "交易员", data: 'managerName'},
            {sTitle: "备注", data: 'note'},
            {sTitle: "操作", defaultContent: ''}
        ],

        bPaginate: false,  //不显示分页器
        bLengthChange: false, //不显示每页长度的选择条
        bProcessing: false, //不显示"正在处理"
        bPaginate: false,
        bInfo: false,
        bFilter: false,
        bSort: false,
        bAutoWidth: false,

        fnRowCallback: function (nRow, aData, iDataIndex) {
            //操作列：根据是否可以显示操作来初始化 行内按钮 编辑、锁定、解锁、划转

            var edit = "-";
            if (!aData.hiddenBtn) {
                var status = aData.status;
                var hrefVar = Context.PATH + "/account/contact/accountcontact.html";
                var text = (status == '1' ? '锁定' : '解锁');
                var edit = '<a deptid="' + jsonData.id + '" userid="' + aData.id + '" deptid="' + jsonData.id + '" class="table-user-edit" href="javascript:;">编辑</a>' +
                    '&nbsp;<a class="table-user-lock" href="javascript:;" userid="' + aData.id + '" status="' + status + '" deptid="' + jsonData.id + '" >' + text + '</a>' +
                    '&nbsp;<a class="table-user-assign" href=' + hrefVar + '>划转</a>';
            }
            $('td:eq(-1)', nRow).html(edit);
            $('td:eq(4)', nRow).html(aData.status == '1' ? '正常' : '锁定'); //1正常0锁定
        }
    });


}

function saveDept() {
    if (!setlistensSave("#editDeptForm")) return;

    var belongOrgVaules = getBelongOrgVaules();
    if("" == belongOrgVaules){
        alert("请选择归属服务中心");
        return;
    }

    //去掉信用额度
    /*if ($("#creditAmount").length > 0) {
        var creditAmount = $("#creditAmount").val();
        var companyCreditAmount = $("#companyCreditAmount").val();
        if (creditAmount > companyCreditAmount) {
            alert("分配额度不能大于公司总信用额度");
            return;
        }
    }*/

    $.ajax({
        type: "POST",
        url: Context.PATH + "/contact/saveDept.html",
        data: {
            "id": $("#deptId").val(),
            "name": $("#deptName").val(),
            "creditAmount": $.trim($("#creditAmount").val()),
            "parentId": $("#accountId").val(),
            "deptFax": $("#deptFax").val(),
            "belongOrg": belongOrgVaules
        },
        dataType: "json",
        success: function (response) {
            if (response.success) {
                cbms.alert(response.data, function () {
                    cbms.closeDialog();
                    //查询数据
                    searchData();
                });
            } else {
                cbms.alert(response.data);
            }
        },
        error: function (xhr, textStatus, errorThrown) {
        }
    });
}

function searchData() {
    dt.ajax.reload();
}

function initClickEvent() {
    //添加部门
    $("#addDept").click(function () {
        cbms.getDialog("添加部门", $("#editDeptDiv").html());

        //归属服务中心，点击选择，弹出选项框
        $("#belongOrgBtn").click(showSelectBelongOrgsBox);
        clickSelectAll();

        $("#editDeptForm").verifyForm();
    });

    //部门编辑
    $(document).on("click", ".dept-edit", function () {
        var $This = $(this);
        var id = $This.closest(".department-bar").find(".dept-id").val();
        $.ajax({
            type: "POST",
            url: Context.PATH + "/contact/" + id + "/getDept.html",
            dataType: "json",
            success: function (response) {
                cbms.getDialog("编辑部门", $("#editDeptDiv").html());
                //归属服务中心，点击选择，弹出选项框
                $("#belongOrgBtn").click(showSelectBelongOrgsBox);
                clickSelectAll();
                $("#editDeptForm").verifyForm();
                if (response.success) {
                    var data = response.data;
                    $("#deptId").val(id);
                    $("#deptName").val(data.name);
                    $("#deptFax").val(data.deptFax);
                    $("#creditAmount").val(data.creditAmount);

                    // added by chengui 部门增加服务中心归属
                    var belongOrgValue = data.belongOrg;

                    if(belongOrgValue != null && belongOrgValue != "" && belongOrgValue != "undefined" ){
                        var belongOrgBoxes = $("#belongOrgList").find("ul").find("input[type='checkbox']");
                        // 归属服务中心下拉框第一个选项为“全部”，包含所有服务中心，若相等只选中第一个选项即可
                        if(belongOrgBoxes[0].value == belongOrgValue){
                            belongOrgBoxes[0].checked = true;
                        }else{
                            var array = belongOrgValue.split(",");
                            for(var i = 1; i < belongOrgBoxes.length; i++){
                                for(var j = 0; j < array.length; j++){
                                    if(belongOrgBoxes[i].value == array[j]){
                                        belongOrgBoxes[i].checked = true;
                                    }
                                }
                            }
                        }
                    }




                }
            },
            error: function (xhr, textStatus, errorThrown) {
            }
        });
    });

    //部门删除 改成部门锁定和解锁
    $(document).on("click", ".dept-del", function () {
        var $This = $(this);
        var id = $This.closest(".department-bar").find(".dept-id").val();
        var name = $This.closest(".department-bar").find(".dept-name").html();
        var status = $This.attr("status");
        var lockMsg = "锁定后将无法使用该部门进行开单,确定锁定" + name + "部门吗？";
        var unlockMsg = "确定解锁" + name + "部门吗？";
        var msg = (status == '1' ? lockMsg : unlockMsg);

        cbms.confirm(msg, null, function () {
            //deleteDept(id);
            lockDept({ids:id,status: status == '1' ? '0' : '1'},$This.text());
        })
    });

    //部门折叠
    $(document).on("click", ".department-name", function () {
        var $This = $(this);
        var table = $This.closest(".dept-template").find(".table");
        $This.find('i').toggleClass('fa-angle-down');
        if (table.hasClass('shown')) {
            table.addClass('none');
            table.removeClass('shown');
        } else {
            table.removeClass('none');
            table.addClass("shown");
        }
    });

    //添加人员
    $(document).on("click", ".user-add", function () {
    	_add_repeat_submit = false;
        var $This = $(this);
        var id = $This.closest(".department-bar").find(".dept-id").val();
        cbms.getDialog("添加联系人", "#addUserMode");
        $("#addUserMode").attr("deptid", id);
        $("#addNewUserForm").verifyForm();
    });


    //保存部门
    $("body").on("click", "#saveDept", function () {
        saveDept();
    });

    //编辑用户
    $(document).on("click", ".table-user-edit", function () {
        var $this = $(this);
        var deptid = $this.attr("deptid");
        var userid = $this.attr("userid");

        $.ajax({
            type: "POST",
            url: Context.PATH + "/contact/getContact.html",
            data: {
                accountId: deptid,
                contactId: userid
            },
            dataType: "json",
            success: function (response) {
                cbms.getDialog("编辑用户", $("#editUserDiv").html());
                $("#editUserForm").verifyForm();
                if (response.success) {
                    var data = response.data;
                    $("#editDeptId").val(deptid);
                    $("#editContactId").val(userid);
                    $("#editUserForm").find("input[name='name']").val(data.contact.name);
                    $("#editUserForm").find("input[name='tel']").val(data.contact.tel);
                    $("#editUserForm").find("select[name='status']").val(data.contact.status);
                    $("#editUserForm").find("input[name='qq']").val(data.contact.qq);
                    $("#editUserForm").find("input[name='email']").val(data.contact.email);
                    $("#editUserForm").find("input[name='note']").val(data.contact.note);

                    //init manager
                    var $setBar = $("#editUserForm").find(".first");
                    if (data.users) {
                        $.each(data.users, function (i, item) {
                            var ele = '<span class="jyy-bar" rel="' + item.id + '">' + item.name + '<a href="javascript:;" class="del-jyy">';
                            if (data.userIds == null || data.userIds.indexOf(item.id) > -1) {
                                ele += '<i class="red fa fa-times"></i>';
                            }
                            ele += '</a></span>';
                            $setBar.after(ele);
                        })
                    }
                }
            },
            error: function (xhr, textStatus, errorThrown) {
            }
        });
    })

    //锁定/解锁用户
    $(document).on("click", ".table-user-lock", function () {
        var userId = $(this).attr("userid");
        var deptId = $(this).attr("deptid");
        var status = $(this).attr("status");
        var text = (status == '1' ? '锁定' : '解锁');
        var updateStatus = (status == '1' ? '0' : '1');
        lockAndUnLock(userId, deptId, updateStatus, text);
    });

    //TODO 划转
    $(document).on("click", ".table-user-assign", function () {
        //location = Context.PATH + "";
    });

    //添加人员弹窗中界面层控制
    $(document).on("click", ".seluser", function () {
        var v = $(this).val();
        var userMode = $(this).closest("#userMode");
        if (v == "0") {
            userMode.find(".userMode-s").hide();
            userMode.find(".userMode-f").show();
        } else {
            userMode.find(".userMode-f").hide();
            userMode.find(".userMode-s").show();

            //设置已添加联系人的数据
            var select = userMode.find(".userMode-s").find("select[name=contact]");
            var url = Context.PATH + "/contact/getContactData.html";
            $.getJSON(url, {
                companyId: $("#accountId").val(),
                deptId: $("#addUserMode").attr("deptid")
            }, function (json) {
                select.empty();
                select.append('<option value="">请选择</option>');
                if (json.data) {
                    $.each(json.data, function (i, item) {
                        select.append('<option value="' + item.id + '">' + item.name + '</option>');
                    });
                }
            });
        }
    });

    //交易员名称删除事件
    $(document).on("click", ".del-jyy", function () {
        $(this).closest(".jyy-bar").remove();
    });

    //添加新联系人 保存事件
    $(document).on("click", "#addNewUserBtn", function () {
        if (!setlistensSave("#addNewUserForm"))return;

        var $ul = $(this).closest(".userMode-f");
        var data = getNewUserData($ul);
        saveUser(data);
        $("#addNewUserBtn").attr("disabled","disabled");
        setTimeout(function(){
            $("#addNewUserBtn").attr("disabled",false);
        },3000);
    });

    //选择已有联系人 保存事件
    $(document).on("click", "#addUserBtn", function () {
        var $ul = $(this).closest(".userMode-s");
        var managerIds = getManagerIds($ul);
        var contactId = $ul.find("select[name='contact']").val();
        if (!contactId) {
            cbms.alert("请选择联系人");
            return;
        }
        if (managerIds.length == 0) {
            cbms.alert("请选择交易员");
            return;
        }
        var data = {
            "contact.id": contactId,
            "managerIdList": managerIds,
            "deptId": $("#addUserMode").attr("deptid"),
            "isInEditMode": false
        }
        saveUser(data);
    });


    //编辑联系人 保存事件
    $(document).on("click", "#editUserBtn", function () {
        if (!setlistensSave("#editUserForm"))return;

        var $ul = $(this).closest(".editUserMode");
        var data = getNewUserData($ul);
        if (data) {
            data.deptId = $("#editDeptId").val();
            data.isInEditMode = true;
            data["contact.id"] = $("#editContactId").val();
        }
        saveUser(data);
    });

}

function deleteDept(id) {
    $.ajax({
        type: "POST",
        url: Context.PATH + "/contact/" + id + "/deleteDept.html",
        dataType: "json",
        success: function (response) {
            if (response.success) {
                cbms.alert(response.data, function () {
                    searchData();
                });
            } else {
                cbms.alert(response.data);
            }
        },
        error: function (xhr, textStatus, errorThrown) {
        }
    });
}

//锁定和解锁
function lockAndUnLock(contactId, accountId, status, msg) {
    cbms.confirm("确定要" + msg + "该用户？", null, function () {
        $.ajax({
            type: "POST",
            url: Context.PATH + "/contact/lockAndUnlockContact.html",
            data: {
                "contactId": contactId,
                "accountId": accountId,
                "status": status
            },
            dataType: "json",
            success: function (response, textStatus, xhr) {
                if (response.success) {
                    cbms.alert(msg + "成功！", function () {
                        searchData();
                    });
                } else {
                    cbms.alert(msg + "失败！");
                }
            },
            error: function (xhr, textStatus, errorThrown) {
            }
        });
    })
}

function getNewUserData($ul) {

    var name = $ul.find("input[name='name']").val();
    var managerIds = getManagerIds($ul);
    var tel = $ul.find("input[name='tel']").val();
    var status = $ul.find("select[name='status']").val();
    var qq = $ul.find("input[name='qq']").val();
    var email = $ul.find("input[name='email']").val();
    var note = $ul.find("input[name='note']").val();
    var deptId = $("#addUserMode").attr("deptid");

    if (managerIds.length == 0) {
        cbms.alert("交易员不能为空");
        return null;
    }


    var data = {
        "contact.name": name,
        "contact.tel": tel,
        "contact.qq": qq,
        "contact.email": email,
        "contact.note": note,
        "managerIdList": managerIds,
        "deptId": deptId,
        "status": status,
        "isInEditMode": false
    }
    return data;
}

function getManagerIds($ul) {
    var ids = [];
    $ul.find(".jyy-bar").each(function () {
        ids.push($(this).attr("rel"));
    });
    return ids.join();
}


function saveUser(data) {
	if(_add_repeat_submit)
		return;
	
	_add_repeat_submit = true;
	
    if (data) {
        $.ajax({
            type: "POST",
            url: Context.PATH + "/contact/saveContact.html",
            data: data,
            dataType: "json",
            success: function (response, textStatus, xhr) {
                if (response.success) {
                    cbms.alert(response.data, function () {
                        cbms.closeDialog();
                        searchData();
                        _add_repeat_submit = false;
                    });
                } else {
                    cbms.alert(response.data);
                    _add_repeat_submit= false;
                }
            },
            error: function (xhr, textStatus, errorThrown) {
            	  _add_repeat_submit= false;
            }
        });
    }
}

function setOrgId() {
    //客户性质包含买家 所有服务中心 不包含买家则是客户所属服务中心
    var accountTagName = $("#accountTagName").val();
    if (accountTagName.indexOf("买家") < 0) {
        _orgId = $("#orgId").val();
    }
    ;
}

//锁定部门
function lockDept(data,msg) {
    $.ajax({
        type: "POST",
        url: Context.PATH + "/company/lockAndUnlockCompany.html",
        data: data,
        dataType: "json",
        success: function (response, textStatus, xhr) {
            if (response.success) {
                cbms.gritter("部门"+msg+"成功！", true, function () {
                    //查询数据
                    searchData();
                });
            } else {
                cbms.gritter("部门"+msg+"失败！", false);
            }
        },
        error: function (xhr, textStatus, errorThrown) {
        }
    });

}

//选择归属服务中心展示
function showSelectBelongOrgsBox(){
    var optionbox = $("#belongOrgList");
    if(optionbox.css("display") == "none"){
        optionbox.show();
        $(document).on("mouseleave","#belongOrg", function(){
            optionbox.hide();
        });
    }else{
        optionbox.hide();
    }

    // 根据登陆者判断是否有操作归属服务中心的权限
    var belongOrgBoxes = $("#belongOrgList").find("ul").find("input[type='checkbox']");
    var permissionOrgId = $("#permissionOrgId").val();
    if(permissionOrgId != null && permissionOrgId != "" && permissionOrgId != "undefined"){
        for(var i = 0; i < belongOrgBoxes.length; i++){
            if(belongOrgBoxes[i].value != permissionOrgId){
                belongOrgBoxes[i].disabled = true;
            }
        }
    }

}

// 获取归属服务中心选中项的值
function getBelongOrgVaules(){
    var checkedBoxes = $("#belongOrgList").find("ul").find("input[type='checkbox']:checked");
    var array = [];
    var allValue;
    checkedBoxes.each(function (){
        // "全部"选项的值为所有服务中心值拼接的字符串，其他选项值是数值
        if(isNaN($(this).val())){
            allValue = $(this).val();
            return;
        }
        array.push($(this).val());
    });

    if(allValue){
        return allValue;
    }
    return array.join(",");
}

/**
 * 选中：所有服务中心时，取消其它服务中心选项框, 选中其它服务中心时，取消：所有服务中心
 */
function clickSelectAll(){

    $("#selectAllOrg").click(function(){
        var checked=$(this).prop('checked');
        if(checked){
            $("#belongOrgList li input[type='checkbox']").removeAttr("checked");
            $(this).prop("checked", "checked");
        }
    })

    $("#belongOrgList li input[type='checkbox']").not("#selectAllOrg").click(function(){
        var selectAll = $("#selectAllOrg");
        if(selectAll.prop("checked")){
            selectAll.removeAttr("checked");
        }
    });

}
