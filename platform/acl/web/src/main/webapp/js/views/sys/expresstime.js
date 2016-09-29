/**
 * 服务中心快递时间表
 * Created by lcw on 2016/01/21.
 */

jQuery(function ($) {


    //add by wangxianjun 20160128 查看服务中心快递时间
    $(".modifyOrgDeliver").click(function(){
        var orgid =  $(this).parent().parent().attr("orgid");
        queryOrgDeliverShow(orgid);
    });


});


function queryOrgDeliverShow(orgid){
    var deliverurl = Context.PATH + "/sys/queryorgdeliver.html";
    $.ajax({
        type: 'post',
        url: deliverurl,
        data: {
            orgId: orgid
            },
        error: function(s)
    {
    }
,
    success: function (result) {
        var htmlStr = '';
         htmlStr = '<div class="table-bar"><table class="table table-striped table-bordered table-hover orderItemTable">'
            + '<thead><tr><th>服务中心</th>';
        if (result && result.success) {
            for (var i = 0; i < result.data.length; i++){
                htmlStr += '<th>' + result.data[i].name + '</th>';
        }
    }
    htmlStr += '</tr></thead><tbody class="itemsdatabody">';
    if (result && result.success) {
        htmlStr += "<tr><td><input type='hidden' id='orgId' value='"+result.data[0].orgId+"'/>" + result.data[0].orgName + "</td>";
        for (var i = 0; i < result.data.length; i++) {
            htmlStr += "<td><input class='orgDeliId' type='text' verify='number'  must='1' delid ='"+result.data[i].deliverId+"' name='deldays' value='"+result.data[i].deliverDays+"'/>"  + "</td>";
        }
    }
    htmlStr += '</tr></tbody></table><div class="btn-bar text-center"><button id="saveOrg" class="btn btn-info btn-sm"><i class="fa fa-print"></i>保存</button></div></div>';

   $("#orgDeliverView").html(htmlStr);
     cbms.getDialog("修改快递时间", "#orgDeliverView");
    $("#orgDeliverView").html('');
    }
    });
}
//保存事件
$(document).off("click","#saveOrg");
$(document).on("click","#saveOrg",function () {
    updateOrgDeliver();
});

//更新返利金额和订单中新的买家联系人
function updateOrgDeliver(){
    var deliverIds = "";
    var delId = '';
    var days  = '';
    $(".orgDeliId").each(function(i){
        delId =  $(this).attr("delid");
        days  =  $(this).val();
        deliverIds += delId + ',' + days +'|'
    });
    deliverIds = deliverIds.substring(0,deliverIds.length - 1);
    $.ajax({
        type: 'post',
        url: Context.PATH + "/sys/updateorgdeliver.html",
        data: {
            orgId: $("#orgId").val(),
            deliDaysId: deliverIds
        },
        error: function (s) {
        }
        , success: function (result) {
            cbms.alert(result.data,function(){
                if (result.success) {
                    location.href = Context.PATH + "/sys/expresstime.html";
                }
            });

        }

    });
}
