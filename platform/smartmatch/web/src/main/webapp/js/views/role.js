/* role2.html xuefei20150715  fuelux tree*/
var tree_data ="";

//Tree数据加载 xuefei20150715 END

function initiateDemoData() {
    var dataSource1 = function (options, callback) {
        var $data = null;

        if (!("text" in options) && !("type" in options)) {
            $data = tree_data;//the root tree
            callback({data: $data});
            return;
        }
        else if ("type" in options && options.type == "folder") {
            if ("additionalParameters" in options && "children" in options.additionalParameters)
                $data = options.additionalParameters.children || {};
            else $data = {}//no data
        }

        if ($data != null)//this setTimeout is only for mimicking some random delay
            setTimeout(function () {
                callback({data: $data});
            }, parseInt(Math.random() * 500) + 200);

        //we have used static data here
        //but you can retrieve your data dynamically from a server using ajax call
        //checkout examples/treeview.html and examples/treeview.js for more info
    }
    return {'dataSource1': dataSource1}
}

jQuery(function ($) {
    $(document).on("click", ".addTree", function () {
        var pid = $(this).parent().prev().attr("data-id");
        var bt = {
            title : "新增角色",
            message : "<form id='form-check' class='form-horizontal' role='form'><div class='well ' style='margin-top:25px;'>"+
            "<div class='form-group'><label class='col-sm-3 control-label no-padding-right' for='txtitemcode'>code</label><div class='col-sm-9'><input type='text' must='1' id='txtitemcode' placeholder='请输入角色code'  /></div></div><div class='form-group'><label class='col-sm-3 control-label no-padding-right' for='txtitemname'>角色名</label><div class='col-sm-9'><input type='text' must='1' id='txtitemname' placeholder='请输入角色名'  /></div>" +
            "</div><div class='red text-right' id='addrole_errInfo'></div>"+
            "</div><div class='modal-footer'><button data-bb-handler='success' type='button' pid='"+pid+"' class='ok-btn btn btn-sm btn-primary' id='btnAddRole_Save'><i class='ace-icon fa fa-check'></i> 保存</button>"+
            "<button data-bb-handler='button' type='button' class='btn cancel-btn btn-sm'>取消</button></div></form>"
        };

        cbms.getDialog(bt.title, bt.message);


        //$("#form-check").verifyForm();
        cbms.setFocus ();//初始化input焦点

        if (window.event) {// IE
            cancelBubble = true;
        } else {
            event.stopPropagation();
        }
    });
    $(document).on("click","#btnAddRole_Save",function(){
        var vv = setlistensSave();
        if(vv){
            var pid = $(this).attr("pid");
            var itemcode=$("#txtitemcode").val();
            var itemname = $("#txtitemname").val();
            $.post(Context.PATH + "/role/addrole.html", {parentid: pid, name: itemname,code:itemcode}, function (re) {
                if (!re.success) {
                    $("#addrole_errInfo").text(re.data);
                }
                else {
                    $("#addrole_errInfo").text(re.data);
                    location.reload();
                }
            })
        };

    });
    $(document).on("click", ".delTree", function () {
        cbms.confirm(this, delTreeNode);
    });
    $(document).on("click", ".cancel-btn", function(){
        cbms.closeDialog();
    });
    refreshTree();
});

function delTreeNode(obj)
{
    var pid = $(obj).parent().prev().attr("data-id");
    $.post(Context.PATH + "/role/delrole.html", {id: pid}, function (re) {
        if (!re.success) {
            cbms.alert(re.data);
        }
        else {
            location.reload();
        }
    })
}
function refreshTree() {
    $.get(Context.PATH + "/role/getrolesdata.html", function (data) {
        tree_data = eval(data);
        var sampleData = initiateDemoData();//see below
        $('#tree1').ace_tree({
            dataSource: sampleData['dataSource1'],
            multiSelect: true,
            cacheItems: true,
            'open-icon': 'ace-icon fa fa-users',
            'close-icon': 'ace-icon fa fa-users',
            'selectable': true,
            'selected-icon': 'ace-icon fa fa-check glyphicon glyphicon-user',
            'unselected-icon': 'ace-icon glyphicon glyphicon-user',
            loadingHTML: '<div class="tree-loading"><i class="ace-icon fa fa-refresh fa-spin blue"></i></div>'
        });
    })
}
