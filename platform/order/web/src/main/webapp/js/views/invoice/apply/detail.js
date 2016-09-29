var _weightPrecision = 6;
var _amountPrecision = 2;
$(document).ready(function(){
    var table = $(".table-apply");
    // 展开
    var slideFlag = true;
    table.on("click", "a[name='slide']", function () {
        var currentTable = $(this).closest("table");
        var index = 0;
        currentTable.find(".table-apply-tbody > tr").each(function(){
            var row = $(this);
            index++;
            if(index>=5) {
                if(slideFlag == true) {
                    row.show();
                }else if(slideFlag == false) {
                    row.hide();
                }
            }
        });
        if(slideFlag == true) {
            $(this).closest("tfoot").find("td:eq(0)").find("a").text("收起");
            slideFlag = false;
        }else if(slideFlag == false) {
            $(this).closest("tfoot").find("td:eq(0)").find("a").text("展开");
            slideFlag = true;
        }

    });

    // 查看
    table.on("click", "a[name='view']", function () {
        var currentRow = $(this).closest("tr");
        if($("tbody[name=mergedData] tr",currentRow).size() == 0){
            // 合并数据并设置到tfoot中去
            $(".itemOrder",currentRow).each(function(){
                var dataList = {};
                var mergeDataCot = $("tbody[name=mergedData]",this);
                $(".itemsList-tbody tr",this).each(function(){
                    var row = $(this);
                    var orderdetailid = row.attr("orderdetailid");
                    var itemObj = dataList[orderdetailid];
                    if(!itemObj){
                        var cloneRow = row.clone();
                        itemObj = {
                            trRow : cloneRow,
                            weightTd : cloneRow.find("td:eq(4)"),
                            amountTd : cloneRow.find("td:eq(8)"),
                            invoiceWeight : 0,
                            invoiceAmount : 0
                        };
                        dataList[orderdetailid] = itemObj;
                        mergeDataCot.append(itemObj.trRow);
                    }
                    var cw = parseFloat(cbms.convertFloat($("td:eq(4)",row).text(),_weightPrecision));
                    var ca = parseFloat(cbms.convertFloat($("td:eq(8)",row).text(),_amountPrecision));
                    itemObj.invoiceWeight += cw;
                    itemObj.invoiceAmount += ca;
                    itemObj.weightTd.text(itemObj.invoiceWeight.toFixed(_weightPrecision));
                    itemObj.amountTd.text(itemObj.invoiceAmount.toFixed(_amountPrecision));
                });
            });
        }
        var itemsList = currentRow.find(".itemsList").html();
        cbms.getDialog(currentRow.find("td:eq(0)").text(),itemsList);
    });

    // 录入开票资料
    table.on("click", "a[name='add']", function () {
        var currentRow = $(this).closest("tr");
        var buyerId = currentRow.attr("buyerid");
        var accountDomain=$("#accountDomain").val();
        window.open(accountDomain + '/company/' + buyerId + '/credentialsinfo.html');
    });

    // 提交
    $(document).on("click", "#submit-btn", function(){
        cbms.confirm("确认待申请开票提交？", null, function () {
            applySubmit();
        });
    });

    // 返回
    $(document).on("click", "#items-btn", function(){
        cbms.closeDialog();
    });

    // 详情搜索
    $(document).on("click","#btnSearch",function(){
        var infoBar = $(this).closest(".info-bar");

        var paramOrderNo = infoBar.find("#seachConsignOrderCode").val();
        var paramStartTime = infoBar.find("#startTime").val();
        var paramEndTime = infoBar.find("#endTime").val();
        var items = infoBar.find(".itemOrder");
        items.show();
        var orderNo,created,createdDate;
        items.each(function(i,e){
            orderNo = $(e).find("label:eq(1) span").text();
            created = $(e).find("label:eq(2) span").text();
            createdDate = created.substr(0,10);
            if($.trim(paramOrderNo)!=""&&orderNo.indexOf(paramOrderNo)==-1){
                $(e).hide();
                return;
            }
            if($.trim(paramStartTime)!=""&&(paramStartTime>createdDate)){
                $(e).hide();
                return;
            }
            if($.trim(paramEndTime)!=""&&(paramEndTime<createdDate)){
                $(e).hide();
                return;
            }
        });
    });
});

// 提交
function applySubmit(){
    var invoiceApply = createApplyArray();
    if(null == invoiceApply || invoiceApply.length==0){
        cbms.alert("没有可以提供申请的数据,请确认开票资料已经通过审核、二次结算应收金额等于零且交易凭证通过审核!");
        return ;
    }

    var paramJson = JSON.stringify(invoiceApply);
    cbms.loading();
    $.ajax({
        type: 'POST',
        dataType:"JSON",
        url: Context.PATH + "/invoice/apply/submit.html",
        data: {
            "paramJson" : paramJson,
            "uuid":$("#uuid").val()
        },
        error: function (s) {
            cbms.closeLoading();
        },
        success: function (result) {
            cbms.closeLoading();
            if (result) {
                if (result.success) {
                    cbms.alert("待申请开票提交成功", function(){
                        window.location.href = Context.PATH + "/invoice/apply/index.html";
                    });
                } else {
                    cbms.alert(result.data, function(){
                        window.location.href = Context.PATH + "/invoice/apply/index.html";
                    });
                }
            } else {
                cbms.alert("待申请开票提交失败");
            }
        }
    });
}

// 创建申请数据
function createApplyArray() {
    // 获取销项票申请开票二次结算额度控制
    var secondBalance = getSecondBalance();
    if(secondBalance == "" || secondBalance == "undefined") secondBalance = 0;
    var invoiceApply = [];   //开票申请
    $(".invoice-apply").each(function () {
        var ownerDiv = $(this).find(".owner");  //交易员

        var orgId = ownerDiv.find(".orgId").text();    //服务中心ID
        var orgName = ownerDiv.find(".orgName").text();    //服务中心名称
        var ownerId = ownerDiv.find(".ownerId").text();    //交易员ID
        var ownerName = ownerDiv.find(".ownerName").text();    //交易员姓名
        var totalAmount = ownerDiv.find(".totalAmount").text().replace(/,/g,"");  //已申请开票金额总计
        var totalUnAmount = ownerDiv.find(".totalUnAmount").text().replace(/,/g,"");  //未开票金额总计

        var buyerList = [];   //买家列表
        $(this).find(".table-apply-tbody > tr").each(function () {
            var tr = $(this);
            
            //开票资料审核是否通过Id
            if(tr.attr("isokid") == '0'){
            	return true;
            }
            
            //如果二结余额控制开关开启，则进行判断
            if($("input[name='BalanceSecondSettlementSwitch']").val()=='1'){
                if(parseFloat(tr.find("td:eq(5)").text().replace(/,/g,"")) > secondBalance) {
                    return true;
                }
            }
            
            //检测凭证审核，如果审核不通则返回sellerCredential,buyerCredential
        	if($("input[name='CredentialCheckSwitch']").val()=='1'){
	        	if($.trim(tr.find(".sellerCredential").text())=='否' 
	        		
	            	|| $.trim(tr.find(".buyerCredential").text()) =='否'){
	            	return true;
	            }
        	}
            
            var buyerObj = {};


            buyerObj.buyerId=$(this).attr("buyerid"); //买家ID
            buyerObj.departmentId=$(this).attr("departmentid"); //部门ID
            buyerObj.isOkId = $(this).attr("isokid");   //开票资料审核是否通过Id
            buyerObj.isAllotId = $(this).attr("isallotid");  //状态Id
            var bn = $(this).find("td:eq(0)").text();
            buyerObj.buyerName = bn.indexOf("【")>=0?bn.substr(0,bn.indexOf("【")):bn;   //买家名称
            buyerObj.departmentName = $(this).find("td:eq(0) span").attr("departmentname");   //部门名称
            buyerObj.amount = $(this).find("td:eq(1)").text().replace(/,/g,"");   //已申请开票金额
            buyerObj.automaticAmount = $(this).find("td:eq(2)").text().replace(/,/g,""); //自动分配开票金额
            buyerObj.manualAmount = $(this).find("td:eq(3)").text().replace(/,/g,"");    //手动分配开票金额
            buyerObj.unAmount = $(this).find("td:eq(4)").text().replace(/,/g,""); //未开票金额
            buyerObj.balanceSecondSettlement = $(this).find("td:eq(5) .balanceSecondSettlement").text().replace(/,/g,""); //二次结算应收金额
            buyerObj.countMonth = $(this).find("td:eq(6)").text();  //未开发票超期月数
            buyerObj.isOkName = $(this).find("td:eq(7)").text();   //开票资料审核是否通过Value
            buyerObj.isAllotName = $(this).find("td:eq(8)").text();  //状态Value


            // 待申请开票详情弹层页面数据
            var itemOrderData = tr.find(".itemOrder");
            itemOrderList = buildItemOrderList(itemOrderData);
            buyerObj.itemOrder = itemOrderList;

            buyerList.push(buyerObj);
        });
        if(null == buyerList || "" == buyerList) {
            return null;
        }
        var ownerObj = {};
        ownerObj.orgId = orgId; //服务中心ID
        ownerObj.orgName = orgName; //服务中心名称
        ownerObj.ownerId = ownerId; //交易员ID
        ownerObj.ownerName = ownerName; //交易员姓名
        ownerObj.totalAmount = totalAmount;   //已申请开票金额总计
        ownerObj.totalUnAmount = totalUnAmount;   //未开票金额总计
        ownerObj.buyerList = buyerList;
        invoiceApply.push(ownerObj);
    });
    return invoiceApply;
}

// 待申请开票详情弹层页面数据
function buildItemOrderList(itemOrderData) {
    var itemOrderList = [];   //订单列表
    itemOrderData.each(function () {
        var itemOrderObj = {};

        var orderCode = $(this).find(".orderCode").text();    //代运营交易单号
        var created = $(this).find(".created").text();    //开单时间
        var totalActualWeight = $(this).find(".totalActualWeight").text();    //实提总重量
        var totalContractAmount = $(this).find(".totalContractAmount").text();    //合同总金额
        var totalActualAmount = $(this).find(".totalActualAmount").text();    //实提总金额

        itemOrderObj.orderId = $(this).find(".orderId").text();    //代运营订单ID
        itemOrderObj.orderCode = orderCode;
        itemOrderObj.created = created;
        itemOrderObj.totalActualWeight = totalActualWeight;
        itemOrderObj.totalContractAmount = totalContractAmount;
        itemOrderObj.totalActualAmount = totalActualAmount;

        var itemOrderDetailArr = [];
        $(this).find(".itemsList-tbody > tr").each(function () {
            var itemOrderDetailObj = {};
            itemOrderDetailObj.orderDetailId=$(this).attr("orderdetailid"); // 订单详情ID
            itemOrderDetailObj.invoiceInId=$(this).attr("invoiceinid"); // 进项票ID
            itemOrderDetailObj.invoiceInDetailId=$(this).attr("invoiceindetailid");// 进项票详情ID
            itemOrderDetailObj.invoiceOrderitemId=$(this).attr("invoiceorderitemid");// 进项票详情与订单详情关联表ID
            itemOrderDetailObj.noTaxAmount=$(this).attr("notaxamount");// 不含税金额
            itemOrderDetailObj.taxAmount=$(this).attr("taxamount");// 税额

            itemOrderDetailObj.nsortName = $(this).find("td:eq(0)").text(); //品名
            itemOrderDetailObj.material = $(this).find("td:eq(1)").text();   //材质
            itemOrderDetailObj.spec = $(this).find("td:eq(2)").text();   //规格
            itemOrderDetailObj.actualWeight = $(this).find("td:eq(3)").text().replace(/,/g,""); //实提重量
            itemOrderDetailObj.weight = $(this).find("td:eq(4)").text().replace(/,/g,""); //开票重量
            itemOrderDetailObj.dealPrice = $(this).find("td:eq(5)").text().replace(/,/g,"");    //成交价
            itemOrderDetailObj.contractAmount = $(this).find("td:eq(6)").text().replace(/,/g,""); //合同金额
            itemOrderDetailObj.actualAmount = $(this).find("td:eq(7)").text().replace(/,/g,""); //实提金额
            itemOrderDetailObj.amount = $(this).find("td:eq(8)").text().replace(/,/g,""); //开票金额
            itemOrderDetailArr.push(itemOrderDetailObj);
        });
        itemOrderObj.itemOrderDetail = itemOrderDetailArr;
        itemOrderList.push(itemOrderObj);
    });
    return itemOrderList;
}

// 获取销项票申请开票二次结算额度控制
function getSecondBalance(){
    // 二次结算额度控制系统设置
    var secondBalance = 0;
    $.ajax({
        type: 'POST',
        dataType:"JSON",
        async: false,
        url: Context.PATH + "/invoice/apply/secondbalance.html",
        data: {},
        success: function (result) {
            if (result.success) {
                secondBalance = result.data;
            } else {
                cbms.alert(result.data);
            }
        }
    });
    return secondBalance;
}
