/**
 * Created by dengxiyan on 2015/8/27.
 * 订单全部tab页
 */

var tradeTable;
jQuery(function ($) {
    initTable();
    initClickEvent();
});

function initTable() {
    var url = Context.PATH + "/order/query/loadOrderData.html";
    tradeTable = jQuery("#dynamic-table").DataTable({
        "sScrollY": "400px",
        "bScrollCollapse": true,
        "iDisplayLength": 50,
        "ajax": {
            "url": url
            , "type": "POST"
            , data: function (d) {
                d.code = $("#code").val();
                d.accountName = $("#accountName").val();
                d.createdStartTime = $("#startTime").val();
                d.createdEndTime = $("#endTime").val();
                d.ownerName = $.trim($("#ownerId").val());//交易员 唯一匹配
                d.sellerName = $("#sellerName").val();
                //支付类型
                var payType = $("#select_pay_style").val();
                if(payType) d.isPayedByAcceptDraft = payType;
                
                //交易单状态：
                var orderStatusList = null;
                $("#show_options label").each(function(){
                	if(!orderStatusList) 
                		orderStatusList = [];
            		orderStatusList.push($(this).children("input[type='hidden']").val());
                })
                if(orderStatusList) d.orderStatus = orderStatusList.toString();
                
                
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'code'},
            {data: 'createdStr'},
            {data: 'accountName'},
            {data: 'ownerName'}
            , {data: 'sellerName'}
            , {data: 'totalQuantity', "sClass": "text-right"}
            , {data: 'totalWeight', "sClass": "text-right"}
            , {data: 'pickupTotalWeight', "sClass": "text-right"}
            , {data: 'totalAmount', "sClass": "text-right"}
            , {data: 'pickupTotalAmount', "sClass": "text-right"}
            , {data: 'status'}
        ]
        , columnDefs: [
            {
                "targets": 0, //第几列 从0开始
                "data": "code",
                "render": renderCode
            }
            , {
                "targets": 4, //第几列 从0开始
                "data": "sellerName",
                "render": renderSeller
            }
            , {
                "targets": 6, //第几列 从0开始
                "data": "totalWeight",
                "render": renderWeight
            }, {
                "targets": 7, //第几列 从0开始
                "data": "pickupTotalWeight",
                "render": renderWeight
            }
            , {
                "targets": 8, //第几列 从0开始
                "data": "totalAmount",
                "render": renderAmount
            }, {
                "targets": 9, //第几列 从0开始
                "data": "pickupTotalAmount",
                "render": renderPickupAmount
            }
            ,
            {
                "targets": 10, //第几列 从0开始
                "data": "status",
                "render": function (data, type, full, meta) {

                    //订单状态显示
                    if (full.status == '1' || full.status == '2' || full.status == '10') {
                        return renderStatus(full.status);//1:待审核 2:待关联合同 10:交易完成
                    }
                    if (full.status == '4' || full.status == '6') {//已关联合同/待二次结算
                        var text = "</br>" + renderStatus(full.status);//待出库、待二次结算
                        if (full.payStatus == 'APPLY') {
                            return renderStatus("3") + text;
                        }
                        if (full.payStatus == 'REQUESTED') {
                            return renderStatus("7") + text;//已申请付款
                        }
                        if (full.payStatus == 'APPROVED') {
                            return renderStatus("8") + text;//已通过付款申请
                        }
                        if(full.payStatus == 'APPLYPRINTED'){
                            return renderStatus("11") + text;//已打印付款申请
                        }
                        if (full.payStatus == 'PAYED') {
                            return "已确认付款" + text;
                        }
                    }
                    if (full.status == '7') {
                        return "待开票申请";
                    }
                    if (full.status == '8') {
                        return "待开票";
                    }
                    if (full.status == '3' || full.status == '5' || full.status == '-5' || full.status == '-6') {
                        return renderStatus("9");//已申请关闭
                    }
                    if (full.status == '-1' || full.status == '-2' || full.status == '-3' || full.status == '-4' || full.status == '-7'
                        || full.status == '-8') {
                        return renderStatus("0");//交易关闭
                    }
                    return "";
                }
            }
        ]
        //生成footer
        , "footerCallback": function (row, data, start, end, display) {
            var api = this.api(), total;

            //当前页汇总
            total = pageTotalOrder(api);

            // Update footer
            var ele = '<div class=" recordbar col-xs-12">' +
                '<span class="bolder">当前页汇总</span>' +
                '<span>总数量：<span class="bolder">' + total.pageTotalQty + '</span></span>' +
                '<span>总重量：<span class="bolder">' + total.pageTotalWeight.toFixed(6) + '</span>吨</span>' +
                '<span>实提总重量：<span class="bolder">' + total.pageTotalPickupWeight.toFixed(6) + '</span>吨</span>' +
                '<span>总金额：<span class="bolder">' + formatMoney(total.pageTotalAmount, 2) + '</span>元</span>' +
                '<span>实提总金额：<span class="bolder">' + formatMoney(total.pageTotalPickupAmount, 2) + '</span>元</span>' +
                '</div>';
            var footerColumnIndex = 10;//状态列索引
            $(api.column(footerColumnIndex).footer()).html(
                ele
            );

        }
    });
}


/**
 * 统计当前页需要显示总和的列
 * @param api
 * @returns {{pageTotalQty: *, pageTotalWeight: *, pageTotalPickupWeight: *, pageTotalAmount: *, pageTotalPickupAmount: *}}
 */
function pageTotalOrder(api) {
    //列索引:数量、总重量、实提总重量、总金额、实提总金额
    var qtyColIndex = 5, weightColIndex = 6, pickupWeightColIndex = 7, amountColIndex = 8, pickupAmountColIndex = 9;
    var total = {
        pageTotalQty: pageTotalColumn(api, qtyColIndex),
        pageTotalWeight: pageTotalColumn(api, weightColIndex),
        pageTotalPickupWeight: pageTotalColumn(api, pickupWeightColIndex),
        pageTotalAmount: pageTotalColumn(api, amountColIndex),
        pageTotalPickupAmount: pageTotalColumn(api, pickupAmountColIndex)
    }
    return total;
}

function initClickEvent() {
    //搜索事件
    $("#queryBtn").click(search);
    
    //交易状态单，点击选择，弹出选项框
    $("#orderStatusBtn").click(showSelectOptionsBox);
    
    //交易状态单，选中checkbox， 出现在一个已经选中的label, 
    selectOptions();
    
} 


function search() {
    tradeTable.ajax.reload();
}

function showSelectOptionsBox(){
	var optionbox = $("#orderStatusList");
	if(optionbox.css("display") == "none"){
		optionbox.show();
		$(document).on("mouseleave","#order_status_options", function(){
			optionbox.hide();
		});
	}else{
		optionbox.hide();
	}
}

function selectOptions(){
	var options = $("#orderStatusList").find("li").each(function(){
		//$this = li
		$(this).find("input[type='checkbox']").change(function(){
			//$this = checkbox
			var checkbox = $(this);
			var index  = checkbox.siblings("input[name='index']").val();
			if(checkbox.prop('checked')){
				var label =  checkbox.siblings("span").text();
				var code = checkbox.val();
				$("#show_options").append("<label class=\"option_item_"+index+"\">&nbsp;&nbsp;"+label+"&nbsp;&nbsp;<input type='hidden' name='code' value='"+code+"'/></label>");
			}else{
				$("#show_options").children(".option_item_"+index).remove();
			}
		})
		
	});
}

