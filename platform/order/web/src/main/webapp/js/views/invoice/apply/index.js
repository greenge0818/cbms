var type = "buyer", dt;

$(document).ready(function(){
    initOrganization();
    $(".amount-invoice").click(function() {
        var $ele = $(this).closest("tr");
        var tit = $ele.find(".enterName").html(),
            unopen = $ele.find(".unopen-amount").html(),
            open = $ele.find(".open-amount").html(),
            sec = $ele.find(".second-amount").attr("data-amount"),
            orgId = $ele.find(".second-amount").attr("data-orgId"),
            orgName = $ele.find(".second-amount").attr("data-orgName"),
            buyerId = $ele.find(".second-amount").attr("data-buyerId"),
            buyerName = $ele.find(".second-amount").attr("data-buyerName"),
            ownerId = $ele.find(".second-amount").attr("data-ownerId"),
            ownerName = $ele.find(".second-amount").attr("data-ownerName");

        if(open==="0") {
            cbms.alert("该公司开票金额上限为零，不可开票");

            return false;
        }

        var e = '<form id="checkForm"><div class="amount-box"><ul>';
        e += '<li>未申请开票金额（元）：     ' + unopen + '</li>';
        e += '<li>可开票金额上限（元）：     ' + open + '</li>';
        e += '<li>录入开票金额（元）：     <input class="c-text" type="text" value="" must="1" verify="numeric" name="amount" /></li>';
        e += '<li><label class="pos-rel"><input type="checkbox" name="isIndependent" class="ace"><span class="lbl red">单独开票，不与其他交易员与该公司产生的订单合并开票</span></label></li>';
        if (parseInt(sec) > 0){
            e += '<li class="warning-info"><span >该买家欠结算款：' + sec + '元<br>如需开票请I联系对接核算会计。</span></li>';
        }
        e += '<li class="btn-bar text-center"><button id="setAmount" type="button" class="btn btn-sm btn-primary">申请开票</button>&nbsp;<button type="button" id="cancel" class="btn btn-sm btn-default">取消</button></li>';
        e += '<input type="hidden" name="orgId" value="'+orgId+'">';
        e += '<input type="hidden" name="orgName" value="'+orgName+'">';
        e += '<input type="hidden" name="buyerId" value="'+buyerId+'">';
        e += '<input type="hidden" name="buyerName" value="'+buyerName+'">';
        e += '<input type="hidden" name="ownerId" value="'+ownerId+'">';
        e += '<input type="hidden" name="ownerName" value="'+ownerName+'">';
        e += '</ul></div></form>';
        cbms.getDialog(tit, e);
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
        url: Context.PATH + '/user/init/organization.html',
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
    var url = Context.PATH + "/invoice/apply/index.html?action=search",
        orgId = $("#hidOrgId").val(),
        buyerName = $("#iptBuyerName").val();
    if(!utils.isEmpty(buyerName)) {
        url += '&buyerName='+buyerName;
    }
    if(!utils.isEmpty(orgId) && orgId != "0") {
        url += '&orgId='+orgId;
    }
    window.location.href = url;

}

function add() {
    var amount = $("#checkForm").find('input[name="amount"]').val();

    if(utils.isEmpty(amount)) {
        cbms.alert("金额为必填项");
        return;
    }

    var orgId = $("#checkForm").find('input[name="orgId"]').val(),
    orgName = $("#checkForm").find('input[name="orgName"]').val(),
    buyerId = $("#checkForm").find('input[name="buyerId"]').val(),
    buyerName = $("#checkForm").find('input[name="buyerName"]').val(),
    ownerId = $("#checkForm").find('input[name="ownerId"]').val(),
    ownerName = $("#checkForm").find('input[name="ownerName"]').val(),
    isIndependent = $("#checkForm").find('input[name="isIndependent"]').is(":checked");

    $.ajax({
        type: "POST",
        url: Context.PATH + '/invoice/apply/add.html',
        data: {
            ownerId:ownerId,
            ownerName:ownerName,
            orgId:orgId,
            orgName:orgName,
            buyerId:buyerId,
            buyerName:buyerName,
            applyAmount: amount,
            isIndependent:isIndependent
        },
        dataType: "json",
        success: function (response, textStatus, xhr) {
            if (response.success) {
                window.location.href = Context.PATH + "/invoice/apply/index.html";
                cbms.closeDialog();
            } else {
                cbms.alert("添加失败！");
            }
        }
    });

}

