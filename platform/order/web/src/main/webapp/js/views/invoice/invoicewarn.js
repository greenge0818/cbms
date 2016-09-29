/**
 * Created by dengxiyan on 2015/8/19.
 * 代运营卖家交易报表
 */

var dt;
jQuery(function ($) {
    initTable();
    //搜索事件
    $("#queryBtn").click(function () {
        if ($("#accountid").val() == '') {
            $("#accountid").removeAttr("accountid");
        }
        dt.ajax.reload();
    });

    //交易状态单，点击选择，弹出选项框
    $("#sorganizationBtn").click(showSelectOptionsBox);

    //交易状态单，选中checkbox， 出现在一个已经选中的label,
    selectOptions();
});

function initTable() {
    var url = Context.PATH + "/invoice/in/getWarnReason.html";
    dt = $("#dynamic-table").DataTable({
            "processing": true,
            "serverSide": true,
            "searching": false,
            "ordering": false,
            iDisplayLength: 50,
            ajax: {
                url: url,
                type: "POST",
                data: function (d) {
                    d.buyerid = $("#accountid").attr("accountid");
                    d.invoiceDataStatus = $("#invoiceData").val();
                    //服务中心：
                    d.orgs =  getOrgs();
                }
            },
            //数据源为数组时，定义数据列的对应
            columns: [
            {data: 'accountName'},
            {data: 'buyerOrgName'},
            {data: 'invoiceDataStatus'},
            {data: 'status'},
            {data: 'balanceSecondSettlement', sClass: "text-right"},
            {data: 'applyAmount', sClass: "text-right"}
        ]
        ,
            fnRowCallback: function (nRow, aData, iDataIndex) {
            $('td:eq(4)', nRow).html(renderAmount(aData.balanceSecondSettlement));
            $('td:eq(5)', nRow).html(renderAmount(aData.applyAmount));
            return nRow;
        }
        ,
        footerCallback: function(row, data){
        }
    });
}
function renderAmount(data, type, full, meta) {
    if (data) {
        return "<span class='bolder'>" + formatMoney(data, 2) + "</span>";
    }
    return "<span class='bolder'>" + '0.00' + "</span>";
}

function getOrgs(){
    var orgList = null;
    $("#show_options label").each(function(){
        if(!orgList)
            orgList = [];
        orgList.push($(this).children("input[type='hidden']").val());
    });
    var statusStr = '';
    if(orgList){
        for(var i=0;i<orgList.length;i++){
            if(orgList[i]== '' || orgList[i] == null){
                statusStr = '';
                break;
            }
            else{
                statusStr += orgList[i] + ',';
            }
        }
        statusStr = statusStr.substring(0,statusStr.length - 1);
    }
    return statusStr;
}

function showSelectOptionsBox(){
    var optionbox = $("#sorganizationList");
    if(optionbox.css("display") == "none"){
        optionbox.show();
        $(document).on("mouseleave","#sorganization", function(){
            optionbox.hide();
        });
    }else{
        optionbox.hide();
    }
}

function selectOptions(){
    var options = $("#sorganizationList").find("li").each(function(){
        //$this = li
        $(this).find("input[type='checkbox']").change(function(){
            //$this = checkbox
            var checkbox = $(this);
            var index  = checkbox.siblings("input[name='index']").val();
            if(checkbox.prop('checked')){
                var code = checkbox.val();
                $("#show_options").append("<label class=\"option_item_"+index+"\"><input type='hidden' name='code' value='"+code+"'/></label>");
            }else{
                $("#show_options").children(".option_item_"+index).remove();
            }

        })

    });
}

