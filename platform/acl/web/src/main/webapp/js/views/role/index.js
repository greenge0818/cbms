
/* role2.html xuefei20150715  fuelux tree*/


//Tree数据加载 xuefei20150715 END


jQuery(function($){

    /***********树形结构 s*******xuefei20150721*******/

    var setting = {
        view: {
            addHoverDom: addHoverDom,
            removeHoverDom: removeHoverDom,
            selectedMulti: false
        },
        edit: {
            enable: true,
            editNameSelectAll: true,
            showRemoveBtn: true,
            showRenameBtn: true
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback: {
            beforeDrag: beforeDrag,
            beforeEditName: beforeEditName,
            beforeRemove: beforeRemove,
            beforeRename: beforeRename
        }
    };

    var log, className = "dark";
    function beforeDrag(treeId, treeNodes) {
        return false;
    }
    function beforeEditName(treeId, treeNode) {
        $.ajax({
            type: "POST",
            url: Context.PATH + '/role/query.html',
            data: {
                id: treeNode.id
            },
            dataType: "json",
            success: function (response, textStatus, xhr) {
                if (response.success) {

                    var data = response.data;

                    cbms.getDialog("编辑角色", $('#addNewRoleWin').html());

                    $("#dialogContBox").find('.form-horizontal').find('input[name="id"]').val(data.id);
                    $("#dialogContBox").find('.form-horizontal').find('input[name="name"]').val(data.name);
                    $("#dialogContBox").find('.form-horizontal').find('input[name="code"]').val(data.name);
                    $("#dialogContBox").find('.form-horizontal').find('select[name="type"]').val(data.type);
                    $("#dialogContBox").find('.form-horizontal').find('select[name="roleType"]').val(data.roleType);
                    $("#dialogContBox").find('.form-horizontal').find('input[name="parentId"]').val(data.parentId);
                }
            }
        });


    }

    function beforeRemove(treeId, treeNode) {
        className = (className === "dark" ? "":"dark");

        var zTree = $.fn.zTree.getZTreeObj("roleTree");
        zTree.selectNode(treeNode);

        cbms.confirm("确认删除此节点吗?", treeNode.id, del);

        return false;
    }

    function beforeRename(treeId, treeNode, newName, isCancel) {
        className = (className === "dark" ? "":"dark");

        if (newName.length == 0) {
            var zTree = $.fn.zTree.getZTreeObj("roleTree");
            setTimeout(function(){zTree.editName(treeNode)}, 10);
            return false;
        }
        return true;
    }

    function showRemoveBtn(treeId, treeNode) {
        return !treeNode.isFirstNode;
    }
    function showRenameBtn(treeId, treeNode) {
        return !treeNode.isLastNode;
    }

    function addHoverDom(treeId, treeNode) {
        var sObj = $("#" + treeNode.tId + "_span");
        if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
        var addStr = "<span class='fa fa-plus add' id='addBtn_" + treeNode.tId
            + "' title='添加' onfocus='this.blur();'></span>";
        sObj.after(addStr);
        var btn = $("#addBtn_"+treeNode.tId);
        if (btn) btn.bind("click", function(){

            $('#addNewRoleWin').find('input[name="parentId"]').val(treeNode.id);
            $('#addNewRoleWin').find('.form-horizontal').find('input[name="name"]').val("");
            $("#addNewRoleWin").find('.form-horizontal').find('input[name="id"]').val("");
            $("#addNewRoleWin").find('.form-horizontal').find('select[name="type"]').val("0");

            cbms.getDialog("添加角色", $('#addNewRoleWin').html());

            return false;
        });
    }

    function removeHoverDom(treeId, treeNode) {
        $("#addBtn_"+treeNode.tId).unbind().remove();
    };

    $(document).ready(function(){
        reload()

        $(document).on("click", "#btnSaveRole",function () {
            save();
        });

        $(document).on("click", "#btnClose",function () {
            cbms.closeDialog();
        });

        $(document).on("click", "#btnAdd",function () {
            $('#addNewRoleWin').find('input[name="parentId"]').val(0);
            $('#addNewRoleWin').find('.form-horizontal').find('input[name="name"]').val("");
            $("#addNewRoleWin").find('.form-horizontal').find('select[name="type"]').val("0");
            $("#addNewRoleWin").find('.form-horizontal').find('input[name="id"]').val("");

            cbms.getDialog("添加角色", $('#addNewRoleWin').html());
        });
    });

    function reload(){
        $.ajax({
            type:'post',
            url:Context.PATH + "/role/list.html",
            data:{},
            success:function(res){
                if(res.length>0) {
                    $.fn.zTree.init($("#roleTree"), setting, res);
                } else {
                    $("#btnAdd").show();
                }
            },
            error:function(s){
            }
        });
    }

    function save() {
        if (setlistensSave("#form-horizontal")) {
            var name = $("#dialogContBox").find('.form-horizontal').find('input[name="name"]').val();
            var parentId = $("#dialogContBox").find('.form-horizontal').find('input[name="parentId"]').val();
            var id = $("#dialogContBox").find('.form-horizontal').find('input[name="id"]').val();
            var type = $("#dialogContBox").find('.form-horizontal').find('select[name="type"]').val();
            var roleType = $("#dialogContBox").find('.form-horizontal').find('select[name="roleType"]').val();
            var code = $("#dialogContBox").find('.form-horizontal').find('input[name="code"]').val();
            
            if(utils.isEmpty(name)) {
                cbms.alert("名称为必填项");
                return;
            }
            if (utils.isEmpty(parentId)) {
                cbms.alert("节点有误");
                return;
            }

            if(utils.isEmpty(id)) {
                add(name, parentId, type,roleType);
            } else {
                edit(id, name, type,roleType);
            }
        }
    }

    function add(name, parentId, type, roleType) {

        $.ajax({
            type: "POST",
            url: Context.PATH + '/role/add.html',
            data: {
                parentId: parentId,
                name: name,
                code: name,
                type:type,
                roleType:roleType
            },
            dataType: "json",
            success: function (response, textStatus, xhr) {
                if (response.success) {
                    reload();
                    cbms.closeDialog();
                } else {
                    var index = parseInt(response.data);
                    cbms.alert( MESSAGE.ROLE.ADD[index] );
                }
            }
        });
    }

    function edit(id, name, type, roleType) {
        $.ajax({
            type: "POST",
            url: Context.PATH + '/role/edit.html',
            data: {
                id: id,
                name: name,
                code: name,
                type:type,
                roleType:roleType
            },
            dataType: "json",
            success: function (response, textStatus, xhr) {
                if (response.success) {
                    reload();
                    cbms.closeDialog();
                } else {
                    var index = parseInt(response.data);
                    cbms.alert( MESSAGE.ROLE.UPDATE[index] );
                }
            }
        });
    }

    function del (id) {
        $.ajax({
            type: "POST",
            url: Context.PATH + '/role/del.html',
            data: {
                id: id
            },
            dataType: "json",
            success: function (response, textStatus, xhr) {
                if (response.success) {
                    reload();
                }
            }
        });
    }
});

