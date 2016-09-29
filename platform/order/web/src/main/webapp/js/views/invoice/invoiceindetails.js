/**
 * Created by dengxiyan on 2015/11/5.
 */
var _weightFixedLength = 6;
var _amountFixedLength = 2;
var _relateTableColumns = {
    getTd: function (row, column) {
        return $(column.filter, row);
    },
    orderDateTime: {
        index: 0,
        filter: 'td:eq(0)'
    },
    orderNumber: {
        index: 1,
        filter: 'td:eq(1)'
    },
    contractCode: {
        index: 2,
        filter: 'td:eq(2)'
    },
    buyerName: {
        index: 3,
        filter: 'td:eq(3)'
    },
    nsortName: {
        index: 4,
        filter: 'td:eq(4)'
    },
    materials: {
        index: 5,
        filter: 'td:eq(5)'
    },
    spec: {
        index: 6,
        filter: 'td:eq(6)'
    },
    unInvoiceWeight: {
        index: 7,
        filter: 'td:eq(7)'
    },
    unInvoiceAmount: {
        index: 8,
        filter: 'td:eq(8)'
    },
    increaseWeight: {
        index: 9,
        filter: 'td:eq(9)'
    },
    increaseAmount: {
        index: 10,
        filter: 'td:eq(10)'
    }
};

var _detailTableColumns = {
    getTd: function (row, column) {
        return $(column.filter, row);
    },
    assign_cargoName: {
        index: 0,
        filter: 'td:eq(0)'
    },
    assign_cargoSpec: {
        index: 1,
        filter: 'td:eq(1)'
    },
    assign_cargoWeight: {
        index: 3,
        filter: 'td:eq(3)'
    },
    assign_priceTaxAmount: {
        index: 8,
        filter: 'td:eq(8)'
    }
};

jQuery(function ($) {
    $("#dynamic-table").on("click", "button[name='viewRelate']", function () {
        var currentRow = $(this).closest("tr");

        cbms.getDialog("查看关联", "#assignDialog");

        copyRowToRelateContainer(currentRow);

        bindAssignOrderItems($(this).attr("detailId"));


    })
    
    $("#dynamic-table").on("click", "button[name='allowanceViewRelate']", function () {
        cbms.getDialog("查看关联", "#allowanceDialog");
    })
    
});


function bindAssignOrderItems(invoiceDetailId) {
    $('#relateTable').DataTable({
        ajax: {
            url: Context.PATH + "/invoice/in/assign.html",
            type: "POST",
            data: function (d) {

                //满足后台检查，invoiceDetailId
                d.sellerAccountId = $("#sellerId").val();
                d.nsortName = '';
                d.materials = '';
                d.spec = '';
                d.weight = parseFloat(convertTextToFloat($("#assign_cargoWeight").text()).toFixed(_weightFixedLength));
                d.priceAndTaxAmount = parseFloat(convertTextToFloat($("#assign_priceTaxAmount").text()).toFixed(_amountFixedLength));
                d.invoiceDetailId = invoiceDetailId;

                delete d.columns;
                delete d.order;
                delete d.search;
            }
        },
        columns: [
            {
                data: 'orderDateTime',
                render: function (data, type, full, meta) {
                    return new Date(data).Format("yyyy-MM-dd");
                }
            },
            {data: 'orderNumber'},
            {data: 'contractCode'},
            {data: 'buyerName'},
            {data: 'nsortName'},
            {data: 'materials'},
            {data: 'spec'},
            {
                data: 'unInvoiceWeight', defaultContent: '0', render: function (data, type, full, meta) {
                var value = data || 0;
                return value.toFixed(_weightFixedLength);
            }
            },
            {
                data: 'unInvoiceAmount', defaultContent: '0', render: function (data, type, full, meta) {
                var value = data || 0;
                if(full.allowanceAmount){
                	value += full.allowanceAmount;
                }
                return value.toFixed(_amountFixedLength);
            }
            },
            {
                data: 'increaseWeight', defaultContent: '0', render: function (data, type, full, meta) {
                return renderRedColor(data, _weightFixedLength)
            }
            },
            {
                data: 'increaseAmount', defaultContent: '0', render: function (data, type, full, meta) {
                	return renderRedColor(data, _amountFixedLength)
                }
            },
            {
                data: 'allowanceAmount', defaultContent: '0', render: function (data, type, full, meta) {
                	return '<input name="allowanceAmount" value="'+data+'"/>'; 
                },
                sClass: "none"
            }
        ],
        fnRowCallback: function (nRow, aData, iDataIndex) {

        },
        drawCallback: function (settings) {
            computeRemain();
        },
        bAutoWidth: false,
        searching: false,
        bPaginate: false,
        paging: false,
        ordering: false
    });
}

function copyRowToRelateContainer(currentRow) {
    $("#assign_invoiceDate").text($("#detail_invoiceDate").text());
    $("#assign_sellerName").text($("#sellerName").text());
    $("#assign_cargoName").text(_detailTableColumns.getTd(currentRow, _detailTableColumns.assign_cargoName).text());
    $("#assign_cargoSpec").text(_detailTableColumns.getTd(currentRow, _detailTableColumns.assign_cargoSpec).text());
    $("#assign_cargoWeight").text(parseFloat(_detailTableColumns.getTd(currentRow, _detailTableColumns.assign_cargoWeight).text()).toFixed(_weightFixedLength));
    $("#assign_priceTaxAmount").text(parseFloat(_detailTableColumns.getTd(currentRow, _detailTableColumns.assign_priceTaxAmount).text()).toFixed(_amountFixedLength));
}

//计算剩余量
function computeRemain() {
    // 获取发票重量和价税合计
    var gotWeight = parseFloat(convertTextToFloat($("#assign_cargoWeight").text()).toFixed(_weightFixedLength));
    var gotAmount = parseFloat(convertTextToFloat($("#assign_priceTaxAmount").text()).toFixed(_amountFixedLength));
    var increaseWeightTotal = 0;
    var increaseAmountTotal = 0;
    // 计算所有行的到票量之和
    $("#relateTableBody tr").each(function () {
        var currentRow = $(this);
        var increaseWeight = _relateTableColumns.getTd(currentRow, _relateTableColumns.increaseWeight);
        var increaseAmount = _relateTableColumns.getTd(currentRow, _relateTableColumns.increaseAmount);
        
        var allwoanceAmount = convertTextToFloat(currentRow.find('input[name="allowanceAmount"]').val());
        
        var iwt = convertTextToFloat(increaseWeight.text()).toFixed(_weightFixedLength);
        var iat = convertTextToFloat(increaseAmount.text()).toFixed(_amountFixedLength);
        increaseWeightTotal += parseFloat(iwt);
        increaseAmountTotal += parseFloat(iat) - allwoanceAmount;
    });
    // 设置当前剩余量
    var rmWeight = parseFloat((gotWeight - increaseWeightTotal).toFixed(_weightFixedLength));
    var rmAmount = parseFloat((gotAmount - increaseAmountTotal).toFixed(_amountFixedLength));
    $("#assign_remainWeight").text(rmWeight.toFixed(_weightFixedLength));
    $("#assign_remainAmount").text(rmAmount.toFixed(_amountFixedLength));
}

function convertTextToFloat(txt) {
    if (!txt) {
        return 0;
    }
    return parseFloat($.trim(txt).replace(/,/g, ''));
}

function renderRedColor(data, scale) {
    var value = data || 0;
    return '<em class="red">' + (value.toFixed(scale)) + '</em>';
}

