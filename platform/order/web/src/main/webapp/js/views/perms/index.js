jQuery(function($){

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
            url: Context.PATH + '/perms/query.html',
            data: {
                id: treeNode.id
            },
            dataType: "json",
            success: function (response, textStatus, xhr) {
                if (response.success) {

                    var data = response.data;

                    cbms.getDialog("编辑", $('#editForm').html());

                    $("#dialogContBox").find('.form-horizontal').find('input[name="id"]').val(data.id);
                    $("#dialogContBox").find('.form-horizontal').find('input[name="name"]').val(data.name);
                    $("#dialogContBox").find('.form-horizontal').find('input[name="code"]').val(data.code);
                    $("#dialogContBox").find('.form-horizontal').find('input[name="url"]').val(data.url);
                    $("#dialogContBox").find('.form-horizontal').find('input[name="parentId"]').val(data.parentId);
                }
            }
        });
    }

    function beforeRemove(treeId, treeNode) {
        className = (className === "dark" ? "":"dark");

        var zTree = $.fn.zTree.getZTreeObj("roleTree");
        zTree.selectNode(treeNode);

        //cbms.confirm("确认删除此节点吗?", treeNode.id, del);
        cbms.alert("您不可以删除权限数据?");

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

            $('#editForm').find('input[name="parentId"]').val(treeNode.id);
            $('#editForm').find('.form-horizontal').find('input[name="name"]').val("");
            $('#editForm').find('.form-horizontal').find('input[name="code"]').val("");
            $('#editForm').find('.form-horizontal').find('input[name="url"]').val("");
            $("#editForm").find('.form-horizontal').find('input[name="id"]').val("");

            cbms.getDialog("添加权限", $('#editForm').html());

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
            $('#editForm').find('input[name="parentId"]').val(0);
            $('#editForm').find('.form-horizontal').find('input[name="name"]').val("");
            $('#editForm').find('.form-horizontal').find('input[name="code"]').val("");
            $('#editForm').find('.form-horizontal').find('input[name="url"]').val("");
            $("#editForm").find('.form-horizontal').find('input[name="id"]').val("");

            cbms.getDialog("添加权限", $('#editForm').html());
        });
    });

    function reload(){
        $.ajax({
            type:'post',
            url:Context.PATH + "/perms/list.html",
            data:{},
            success:function(res){
                if(res.length>0) {
                    $.fn.zTree.init($("#roleTree"), setting, res);
                }
                $("#btnAdd").show();
            },
            error:function(s){
            }
        });
    }

    function save() {
        if (setlistensSave("#form-horizontal")) {
            var name = $("#dialogContBox").find('.form-horizontal').find('input[name="name"]').val();
            var code = $("#dialogContBox").find('.form-horizontal').find('input[name="code"]').val();
            var url = $("#dialogContBox").find('.form-horizontal').find('input[name="url"]').val();
            var parentId = $("#dialogContBox").find('.form-horizontal').find('input[name="parentId"]').val();
            var id = $("#dialogContBox").find('.form-horizontal').find('input[name="id"]').val();

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
                add(name, code, url, parentId);
            } else {
                if(code!=name) {
                    edit(id, name, code, url, parentId);
                } else {
                    cbms.closeDialog();
                }
            }
        }
    }

    function add(name, code, url, parentId) {

        $.ajax({
            type: "POST",
            url: Context.PATH + '/perms/add.html',
            data: {
                parentId: parentId,
                name: name,
                code: code,
                url:url
            },
            dataType: "json",
            success: function (response, textStatus, xhr) {
                if (response.success) {
                    reload();
                    cbms.closeDialog();
                } else {
                    cbms.alert(response.data);
                }
            }
        });
    }

    function edit(id, name, code, url, parentId) {
        $.ajax({
            type: "POST",
            url: Context.PATH + '/perms/edit.html',
            data: {
                id: id,
                parentId:parentId,
                name: name,
                code: code,
                url:url
            },
            dataType: "json",
            success: function (response, textStatus, xhr) {
                if (response.success) {
                    reload();
                    cbms.closeDialog();
                } else {
                    var index = parseInt(response.data);
                    cbms.alert(response.data);
                }
            }
        });
    }

    //function del (id) {
    //    $.ajax({
    //        type: "POST",
    //        url: Context.PATH + '/perms/del.html',
    //        data: {
    //            id: id
    //        },
    //        dataType: "json",
    //        success: function (response, textStatus, xhr) {
    //            if (response.success) {
    //                reload();
    //            }
    //        }
    //    });
    //}
});

