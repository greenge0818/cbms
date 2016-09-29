/**
 * 销售记录
 * Created by zhoucai@prcsteel.com on 2016/3/27.
 */
var dt;
jQuery(function ($) {
    generateCheckBox();
	$("#salesRecords").addClass("active").find("a").attr("href","javascript:void(0);");
    initTable();
    countTradeFlowData();
    $("#searchBtn").on("click", function () {

        countTradeFlowData();
        dt.ajax.reload();
    });
    
});
//状态改变重新加载数据
function searchData() {
    dt.ajax.reload();
    countTradeFlowData();
}
//查询列表
function initTable() {
    dt = jQuery("#dynamic-table").DataTable({
        "pageLength": 50, //每页记录数
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "ajax": {
            "url": Context.PATH + "/company/loadsellertradelist.html"
            , "type": "POST"
            ,
            data: function(d){
                d.sellerId = $("#sellerId").val();
                d.departId = $("#departmentId").val();
                d.strStartTime = $("#startTime").val();
                d.projectId ='',
                d.strEndTime = $("#endTime").val();
                d.statusList = getStatusValuesById("tradetatus");
                d.accountName = $("#buyerCust").val()
            }
        }
        ,"fnRowCallback":function(nRow, aData, iDataIndex){
            $('td:eq(10)', nRow).html(renderStatus(aData.status));
            $('td:eq(8)', nRow).html(formatMoney(aData.sumAmount,2));
            $('td:eq(9)', nRow).html(formatMoney(aData.amount,2));
            $('td:eq(6)',nRow).html((aData.sumWeight).toFixed(6));
            $('td:eq(7)',nRow).html((aData.weight).toFixed(6));
            var orderUrl =$("#orderUrl").val();
            var tempText2 = '<button  class="button btn-xs btn-link confirm">'+aData.sellerName+'</button>';
            var tempText1 = '<a href="'+orderUrl+'/order/query/detail.html?orderid='+aData.id+'&menu=tradecomplete"  class="button btn-xs btn-link confirm">'+aData.orderId+'</button>';
            $('td:eq(0)',nRow).html(tempText1);
            $('td:eq(4)',nRow).html(tempText2);

        },
        columns: [
            { data: 'orderId' },
            { data: 'creatTime' },
            { data: 'accountName' },
            { data: 'userName' },
            { data: 'sellerName' },
            { data: 'quantity' },
            { data: 'sumWeight' },
            { data: 'weight' },
            { data: 'sumAmount' },
            { data: 'amount' },
            /*{ data: 'departName' },*/
            { data: 'status' }
        ]
    } ); 
}
//汇总交易信息 
function countTradeFlowData(){
	//情况下拉框
    document.getElementById("salesCount").innerText= "";
    document.getElementById("totalMass").innerText= '';
    document.getElementById("factTotalMass").innerText= '';
    document.getElementById("totalAmt").innerText = '';
    document.getElementById("factTotlAmt").innerText= '';

	$.ajax({
		url : Context.PATH + "/company/countsellertradeflow.html",
		type : "POST",
        data:{
            departId: $("#departmentId").val(),
            sellerId : $("#sellerId").val(),
            accountId : '',
            projectId : '',
            sellerName : '',
            strStartTime : $("#startTime").val(),
            strEndTime : $("#endTime").val(),
            statusList : getStatusValuesById("tradetatus"),
            accountName : $("#buyerCust").val()
            },
	    success: function(result) {

            if(result.orderId==0){
                return;
            }else{
                document.getElementById("salesCount").innerText= result.orderId;
                if(result.sumWeight!=null){
                    document.getElementById("totalMass").innerText= (result.sumWeight).toFixed(6);
                }
                if(result.weight!=null){
                    document.getElementById("factTotalMass").innerText= (result.weight).toFixed(6);
                }
                if(result.sumAmount!=null){
                    document.getElementById("totalAmt").innerText = formatMoney(result.sumAmount,2);
                }
                if(result.amount!=null){
                    document.getElementById("factTotlAmt").innerText= formatMoney(result.amount,2);
                }


            }

	    }
	});
		
	
}
//生成状态checkbox
 function generateCheckBox() {
     $("#tradetatus").selectCheckBox(Context.PATH+"/company/getsellerconsignorderstatus.html", {},searchData);
}
//获取状态checkbox选中的值
function getStatusValuesById(id){
    var checkedBoxes = $("#"+id).find(".mulsel-content-list ul").find("input[type='checkbox']:checked");
    var array = [];
    checkedBoxes.each(function (){
        array.push($(this).val());
    });
    return array.join(",");
}
//翻译状态
function renderStatus(data){

    if(data == '1'){
        return "待审核";
    }if(data == '-1'){
        return "订单关闭";
    }if(data == '2'){
        return "待关联";
    }if(data == '3'){
        return "交易关闭待审核";
    }if(data == '-2'){
        return "订单关闭";
    }if(data == '4'){
        return "待出库";
    }if(data == '5'){
        return "交易关闭待审核";
    }if(data == '6'){
        return "待二次结算";
    }if(data == '7'){
        return "待开票申请";
    }if(data == '8'){
        return "待开票";
    }if(data == '10'){
        return "交易完成";
    }if(data == '-3'){
        return "订单关闭";
    }if(data == '-4'){
        return "订单关闭";
    }if(data == '-5'){
        return "交易关闭待审核";
    }if(data == '-6'){
        return "交易关闭待审核";
    }if(data == '-7'){
        return "订单关闭";
    }if(data == '-8'){
        return "订单关闭";
    }
    return '-';
}