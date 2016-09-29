var type = "buyer", dt;

$(document).ready(function(){
    initOrganization();
    $("#btnShowAddExpressFrom").click(function() {
        var e = '<form id="checkForm" ><div class="amount-box"><ul >';
        e += '<li style="line-height: 25px;padding-top: 5px;">快递公司：<select class="text_sketch"><option selected="" value="顺丰">顺丰</option><option value="EMS">EMS</option><option value="其他">其他</option></select></li>';
        e += '<li style="line-height: 25px;padding-top: 5px;">快递单号：<input class="c-text" type="text" value="" must="1" name="expressName" /></li>';
        e += '<li class="btn-bar text-center"><button id="setAmount" type="button" class="btn btn-sm btn-primary">提交</button>&nbsp;<button type="button" id="cancel" class="btn btn-sm btn-default">取消</button></li>';
        e += '</ul></div></form>';

        cbms.getDialog("请认真填写快递信息", e);
    });

    $(document).on("click","#cancel",function(){
        cbms.closeDialog();
    });

    $(document).on("click","#setAmount",function(){
        if(setlistensSave("#checkForm")){
            add();
        }
    });

    $(document).on("click", "#btnSearch", function(){
        search();
    });

    $("#checkAll").click(function(){
        $('input:checkbox').not(this).prop('checked', this.checked);
    });
});

var setting = {
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
        beforeClick: beforeClick,
        onClick: onClick
    }
};
function beforeClick(treeId, treeNode) {
}
function onClick(e, treeId, treeNode) {
    var zTree = $.fn.zTree.getZTreeObj("orgTree"),
        nodes = zTree.getSelectedNodes(),
        v = "",vid = "";
    nodes.sort(function compare(a,b){return a.id-b.id;});
    for (var i=0, l=nodes.length; i<l; i++) {
        v += nodes[i].name + ",";
        vid+= nodes[i].id ;
    }
    if (v.length > 0 ) v = v.substring(0, v.length-1);
    var cityObj = $("#sorganization");
    cityObj.attr("value", v);
    $("#hidOrgId").attr("value",vid);

    if(vid.length > 0){

    }
}
function initOrganization() {
    $.ajax({
        type: "POST",
        url: Context.PATH + '/invoice/express/initOrganization.html',
        data: {},
        dataType: "json",
        success: function (response, textStatus, xhr) {
            if (response.success) {
                $.fn.zTree.init($("#orgTree"), setting, response.data);
            }
        }
    });
}

function showMenu() {
    var cityObj = $("#sorganization");
    var cityOffset = $("#sorganization").offset();
    $("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

    $("body").bind("mousedown", onBodyDown);
}
function hideMenu() {
    $("#menuContent").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDown);
}
function onBodyDown(event) {
    if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
        hideMenu();
    }
}

function search() {
    var url = Context.PATH + "/invoice/express/index.html?action=search",
        orgId = $("#hidOrgId").val(),
        buyerName = $("#iptBuyerName").val(),
        from = $("#startTime").val(),
        to = $("#endTime").val();
    if(!utils.isEmpty(buyerName)) {
        url += '&buyerName='+buyerName;
    }
    if(!utils.isEmpty(orgId) && orgId != "0") {
        url += '&orgId='+orgId;
    }
    if(!utils.isEmpty(from)) {
        url += '&from='+from;
    }

    if(!utils.isEmpty(to)) {
        url += '&to='+to;
    }

    window.location.href = url;

}

function add() {
    var expressName = $("#checkForm").find('input[name="expressName"]').val();

    var p = [];
    $('input.express:checked').each(function () {
        p.push($(this).val());
    });

    if (p.length==0) {
        cbms.alert("您没有待寄出的发票");
        return;
    }

    if(utils.isEmpty(expressName)) {
        cbms.alert("快递单号必填项");
        return;
    }

    $.ajax({
        type: "POST",
        url: Context.PATH + '/invoice/express/add.html',
        data: {
            p:p,
            company:"顺丰",
            expressName:expressName
        },
        dataType: "json",
        success: function (response, textStatus, xhr) {
            if (response.success) {
                window.location.href = Context.PATH + "/invoice/express/index.html";
                cbms.closeDialog();
            } else {
                cbms.alert("添加失败！");
            }
        }
    });

}

