jQuery(function ($) {
	renderTable();
	
	//点击返回按钮
	$("#return").on(ace.click_event, function () {
        location = Context.PATH + "/smartmatch/purchaseorder/list.html";
    });
    
});

function renderTable() {
    $.ajax({
        type: "POST",
        url: Context.PATH + "/smartmatch/purchaseorder/getdata.html",
        data: {
            purchaseOrderId: $("#purchaseOrderId").val(),
        },
        success: function (result) {
            var html;
            if (result) {
                if (result.success) {
                    var purchaseOrderItems = result.data.purchaseOrderItems;
                    var quotationOrderList = result.data.quotationOrders;
                    
                    //展现采购单明细
                    $("#purchaseItemsTable tbody").empty();
                    for (var i in purchaseOrderItems) {
                        html = '<tr>' +
                        '<td class="category">' + purchaseOrderItems[i].categoryName + '</td>' +
                        '<td class="material">' + purchaseOrderItems[i].purchaseMaterialNames + '</td><td>' + purchaseOrderItems[i].purchaseSpec + '</td><td class="factory">' + purchaseOrderItems[i].purchaseFactoryNames + '</td>' +
                        '<td class="result_quantity">' + purchaseOrderItems[i].resultQuantity + '</td>' +
                        '<td class="requestWeight">' + cbms.convertFloat(purchaseOrderItems[i].resultWeight, 6) + '</td>' +
                        '</tr>';
                        $("#purchaseItemsTable tbody").append(html);
                    }
                    
                    //展现报价单明细
                    $("#quotation").empty();
                    for (var k in quotationOrderList){
            			var quotation = $(createQuotation(k,quotationOrderList[k].quotationItems));
            			for(var q in quotationOrderList[k].quotationItems){
            				var quotationItem = $(createQuotationItems(quotationOrderList[k].quotationItems[q]));  
            				$(quotation).find("#quotationTable tbody").append(quotationItem);
            			}
            			$("#quotation").append(quotation);
            		}
                    
                } else {
                    cbms.alert(result.data);
                }
            }
        },
        error: function (xhr, textStatus, errorThrown) {
            cbms.closeLoading();
        }
    });
};

//创建报价单
function createQuotation(index,quotationOrder){ 
	var htmlStr="<div><span>报价单"+ ++index +":</span><span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>" +
			"<span>时间："+ date2String(new Date(quotationOrder[0].quotationOrderUpdateTime)) +"</span>" +
			"<table id='quotationTable' class='table table-bordered table-hover check'>" +
			"<thead><tr><th style='width: 232px'>公司名称</th><th style='width: 125px'>品名</th><th style='width: 122px'>材质</th><th style='width: 157px'>规格</th>" +
			"<th style='width: 97px'>厂家</th><th style='width: 73px'>数量</th><th style='width: 78px'>重量</th><th style='width: 136px'>仓库</th>" +
			"<th style='width: 125px'>价格</th></tr></thead> <tbody></tbody></table></div>";
	return htmlStr;
}

//创建报价单明细
function createQuotationItems(quotationOrderItems){
	var htmlStr = "<tr>" +
    "<td class='sellerName'>" + quotationOrderItems.sellerName + "</td>" +
    "<td class='category'>" + quotationOrderItems.categoryName + "</td>" +
    "<td class='material'>" + quotationOrderItems.materialName + "</td><td>" + quotationOrderItems.spec + "</td>"+
    "<td class='factory'>" + quotationOrderItems.factoryName + "</td>" +
    "<td class='quantity'>" + quotationOrderItems.quantity + "</td>" +
    "<td class='weight'>" + cbms.convertFloat(quotationOrderItems.weight, 6) + "</td>" +
    "<td class='warehouseName'>" + quotationOrderItems.warehouseName + "</td>" +
    "<td class='totalAmount'>" + cbms.convertFloat(quotationOrderItems.totalAmount, 2) + "</td>" +
    "</tr>";
	return htmlStr;
}

//js将Date类型转换为String类型：
function date2String(aDate){
    var year=aDate.getFullYear();
    var month=aDate.getMonth();
    month++;
    var mydate=aDate.getDate();
    var hour=aDate.getHours();
    var minute=aDate.getMinutes();
    var mytime=year+"-"+(month<10?"0":"")+month+"-"+(mydate<10?"0":"")+mydate+" "+(hour<10?"0":"")+hour+":"+(minute<10?"0":"")+minute;
    return mytime;
}

