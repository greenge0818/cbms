/**
 * Created by dengxiyan on 2015/8/19.
 * 代运营卖家交易报表
 */

var dt;
var vStatus = '';//交易状态
jQuery(function ($) {
	
	 $("#table-box").css({
	    	"width":"100%",
	    	"overflow":"auto",
	    	"height": ($(window).height()-240)+"px"
	    });
	
    $("#secStartTime").val('');
    $("#secEndTime").val('');
    initTable();
    //搜索事件
    $("#queryBtn").click(function () {
        if ($("#accountid").val() == '') {
            $("#accountid").removeAttr("accountid");
        }
        dt.ajax.reload();
    });

    $("#exportexcel").click(function () {
        exportToExcel();
    });
    //交易状态单，点击选择，弹出选项框
    $("#orderStatusBtn").click(showSelectOptionsBox);

    //交易状态单，选中checkbox， 出现在一个已经选中的label,
    selectOptions();
});

function initTable() {
    var url = Context.PATH + "/report/business/getdetailsdata.html";
    dt = $("#dynamic-table").DataTable({
        ajax: {
            url: url,
            type: "POST",
            data: function (d) {
                if ($("#sorganizationHidden").length > 0) {
                    d.orgid = $("#sorganizationHidden").val();
                }
                if ($("#traderName").length > 0) {
                    d.uid = $("#traderName").attr("userid");
                }
                d.buyerid = $("#accountid").attr("accountid");
                d.sellId = $("#sellid").attr("accountid");
                d.secsdate = $("#secStartTime").val();
                d.secedate = $("#secEndTime").val();
                d.sdate = $("#startTime").val();
                d.edate = $("#endTime").val();
                //交易状态：
                d.status =  getStatus(orderStatusList);
            }
        },
        serverSide: true, //服务模式
        processing: true,//显示数据加载进度
        searching: false, //是否启用搜索
        ordering: false, //是否启用排序
        lengthChange: false, //不显示pageSize的下拉框
        oLanguage: {sUrl: Context.PATH + "/js/DT_zh.txt"}, //自定义语言包
        bFilter: false,
        iDisplayLength: 15,
        bLengthChange: false, //不显示每页长度的选择条
        bPaginate: true,
        columns: [{defaultContent: ''}],
        columnDefs: [{
            sDefaultContent: '', //解决请求参数未知的异常
            aTargets: ['_all']
        }],
        fnRowCallback: function (nRow, aData, iDataIndex) {
            var childTable = $("#order_items").clone().removeClass("none").removeAttr("id");//移除ID并显示
            InitChildBlock(aData, childTable);
            $('td:eq(0)', nRow).html(childTable);
        },
        footerCallback: function (row, data, start, end, display) {
            var api = this.api();
            //var weightIndex = 9, amountIndex = 10;
            var ele = '<div class="recordbar col-xs-12">' +
                '<span class="bolder">汇总</span>' +
                '<span>总重量(吨)：<span class="bolder">' + getTotalPickWeight(data).toFixed(6) + '</span></span>' +
                '<span>销售总金额(元)：<span class="bolder">' + getTotalPickDealAmount(data).toFixed(2) + '</span></span>' +
                '<span>采购总金额(元)：<span class="bolder">' + getTotalPickCostAmount(data).toFixed(2) + '</span></span></div>';
            $(api.column(0).footer()).html(ele);
        }
    });
}
function getStatus(orderStatusList){
    var orderStatusList = null;
    $("#show_options label").each(function(){
        if(!orderStatusList)
            orderStatusList = [];
        orderStatusList.push($(this).children("input[type='hidden']").val());
    });
    var statusStr = '';
    if(orderStatusList){
        for(var i=0;i<orderStatusList.length;i++){
            if(orderStatusList[i].indexOf("PAY_") != -1 || orderStatusList[i].indexOf("FillUP_") != -1){
                if(statusStr =='' ){
                    statusStr +=  orderStatusList[i] + '|';
                }
                else{
                    if(statusStr.charAt(statusStr.length - 1) == '|')
                        statusStr +=  orderStatusList[i] + '|';
                    else
                        statusStr += '|' + orderStatusList[i] + '|';
                }
            }else{
                statusStr += orderStatusList[i] + ',';
            }
        }
        statusStr = statusStr.substring(0,statusStr.length - 1);
    }
    return statusStr;
}
function InitChildBlock(jsonData, itemsBlock) {
    var ordertime = new Date(jsonData.created);
    $(itemsBlock).find(".ordertime").html(dateFormat(ordertime, "yyyy-MM-dd hh:mm:ss"));
    $(itemsBlock).find(".orderid").html(jsonData.code);
    $(itemsBlock).find(".contractcode").html(jsonData.contractCode);
    var buyerName = jsonData.accountName;
    if (jsonData.departmentCount > 1) {
        buyerName += '【' + jsonData.departmentName + '】';
    }
    $(itemsBlock).find(".orderbuyercompany").html(buyerName);
    $(itemsBlock).find(".orderbuyer").html(jsonData.ownerName);
    var orderstatustext = $(itemsBlock).find(".orderstatus");
    //if ($("#order_status")[0].selectedIndex != 0) {
    //    orderstatustext.html($("#order_status").find("option:selected").text());
    //}
    //else {
    //    orderstatustext.html(jsonData.status + " " + jsonData.payStatus);
    //}
    orderstatustext.html(jsonData.status + " " + jsonData.payStatus);
    $(itemsBlock).find(".table").DataTable({
        aaData: jsonData.consignOrderItems,
        columns: [
            {data: 'departmentCount'},
            {data: 'supplierLabel'},
            {data: 'contractCodeAuto'},
            {data: 'sellerTraderName'},
            {data: 'nsortName'},
            {data: 'spec'},
            {data: 'material'},
            {data: 'factory'},
            {data: 'warehouse'},
            {data: 'dealPrice'},
            {data: 'costPrice'},
            {data: 'quantity'},
            {data: 'weight'},
            {data: 'amount'},
            {defaultContent: ''},
            {data: 'actualPickWeightServer'},
            {defaultContent: ''},
            {defaultContent: ''},
            {defaultContent: ''},  //买家折扣金额
            {defaultContent: ''},  //卖家折扣金额
            {defaultContent: ''},  //销售结算金额
            {defaultContent: ''},  //采购结算金额
            {defaultContent: ''},  //毛利
            {data: 'acceptDraft'},  //银票支付
            {data: 'settlementTime'},  //结算时间
            {data: 'invoiceTime'},	  //开票时间
            {data: 'financeOrder'} //是否为融资订单
        ],
        columnDefs: [
            {
                "targets": 0, //第几列 从0开始
                "data": "departmentCount",
                "render": function (data, type, full, meta) {
                    if (data > 1) {
                        return full.sellerName + '【' + full.departmentName + '】';
                    } else {
                        return full.sellerName;
                    }
                }
            },
            {
                "targets": 2, //第几列 从0开始
                "data": "contractCodeAuto",
                "render": function (data, type, full, meta) {
                    var endContractCode=data.toString().substr(data.length-8,8);
                    return "<span title='"+data+"'>"+endContractCode+"</span>";
                }
            },
            {
                "targets": 9,
                "data": "dealPrice",
                "render": function (data, type, full, meta) {
                    return data.toFixed(2);
                }
            },
            {
                "targets": 10,
                "data": "costPrice",
                "render": function (data, type, full, meta) {
                    return data.toFixed(2);
                }
            },
            {
                "targets": 12,
                "data": "weight",
                "render": function (data, type, full, meta) {
                    return data.toFixed(6);
                }
            },
            {
                "targets": 13,
                "data": "amount",   // 销售金额
                "render": function (data, type, full, meta) {
                    return data.toFixed(2);
                }
            },
            {
                "targets": 14,
                "data": "amount",   // 采购金额
                "render": function (data, type, full, meta) {
                    return (full.weight * full.costPrice).toFixed(2);
                }
            },
            {
                "targets": 15,
                "data": "actualPickWeightServer",
                "render": function (data, type, full, meta) {
                    return data.toFixed(6);
                }
            },
            {
                "targets": 16,
                "data": "code",     // 销售实提金额
                "render": function (data, type, full, meta) {
                    return (full.actualPickWeightServer * full.dealPrice).toFixed(2);
                }
            },
            {
                "targets": 17,
                "data": "code",     // 采购实提金额
                "render": function (data, type, full, meta) {
                    return (full.actualPickWeightServer * full.costPrice).toFixed(2);
                }
            },
            {
                "targets": 18,
                "data": "",     // 折扣金额（买家）
                "render": function (data, type, full, meta) {
                    return full.allowanceBuyerAmount.toFixed(2);
                }
            },
            {
            	"targets": 19,
            	"data": "",     // 折扣金额（卖家）
            	"render": function (data, type, full, meta) {
            		return full.allowanceAmount.toFixed(2);
            	}
            },
            {
            	"targets": 20,
            	"data": "",     // 销售结算金额（买家）
            	"render": function (data, type, full, meta) {
            		return (full.actualPickWeightServer * full.dealPrice + full.allowanceBuyerAmount).toFixed(2);
            	}
            },
            {
                "targets": 21,
                "data": "",     // 采购结算金额(元)（卖家）
                "render": function (data, type, full, meta) {
                    return (full.actualPickWeightServer * full.costPrice + full.allowanceAmount).toFixed(2);
                }
            },
            {
                "targets": 22,
                "data": "",     // 毛利(元)
                "render": function (data, type, full, meta) {
                    return ((full.actualPickWeightServer * full.dealPrice + full.allowanceBuyerAmount) - (full.actualPickWeightServer * full.costPrice + full.allowanceAmount)).toFixed(2);
                }
            },
            {
                "targets": 26,
                "data": "",     // 融资订单
                "render": function (data, type, full, meta) {
                    if( full.financeOrder == "1"){
                        return "是";
                    }else{
                        return "否";
                    }

                }
            }
        ],
        bPaginate: false,  //不显示分页器
        bLengthChange: false, //不显示每页长度的选择条
        bProcessing: false, //不显示"正在处理"
        bPaginate: false,
        bInfo: false,
        bFilter: false,
        bSort: false
    });
}

function getTotalPickWeight(data) {
    var total = 0;
    for (var i = 0; i < data.length; i++) {
        if (data[i].consignOrderItems) {
            for (var j = 0; j < data[i].consignOrderItems.length; j++) {
                if (!isNaN(data[i].consignOrderItems[j]["actualPickWeightServer"])) {
                    total += parseFloat(parseFloat(data[i].consignOrderItems[j]["actualPickWeightServer"]).toFixed(6));
                }
            }
        }
    }
    return total;
}
function getTotalPickDealAmount(data) {
    var total = 0;
    for (var i = 0; i < data.length; i++) {
        if (data[i].consignOrderItems) {
            for (var j = 0; j < data[i].consignOrderItems.length; j++) {
                if (!isNaN(data[i].consignOrderItems[j]["actualPickWeightServer"])) {
                    total += parseFloat(parseFloat(parseFloat(data[i].consignOrderItems[j]["actualPickWeightServer"]).toFixed(6)) * parseFloat(parseFloat(data[i].consignOrderItems[j]["dealPrice"]).toFixed(2)).toFixed(2));
                }
            }
        }
    }
    return total;
}
function getTotalPickCostAmount(data) {
    var total = 0;
    for (var i = 0; i < data.length; i++) {
        if (data[i].consignOrderItems) {
            for (var j = 0; j < data[i].consignOrderItems.length; j++) {
                if (!isNaN(data[i].consignOrderItems[j]["actualPickWeightServer"])) {
                    total += parseFloat(parseFloat(parseFloat(data[i].consignOrderItems[j]["actualPickWeightServer"]).toFixed(6)) * parseFloat(parseFloat(data[i].consignOrderItems[j]["costPrice"]).toFixed(2)).toFixed(2));
                }
            }
        }
    }
    return total;
}

function dateFormat(date, fmt) {
    var o = {
        "M+": date.getMonth() + 1,                 //月份
        "d+": date.getDate(),                    //日
        "h+": date.getHours(),                   //小时
        "m+": date.getMinutes(),                 //分
        "s+": date.getSeconds(),                 //秒
        "q+": Math.floor((date.getMonth() + 3) / 3), //季度
        "S": date.getMilliseconds()             //毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

function exportToExcel() {
    var form = $("<form>");
    form.attr('style', 'display:none');
    form.attr('target', '');
    form.attr('method', 'post');
    form.attr('action', Context.PATH + "/report/business/orderdetailtoexcel.html");

    var input1 = $('<input>');
    input1.attr('type', 'hidden');
    input1.attr('name', 'sdate');
    input1.attr('value', $("#startTime").val());

    var input2 = $('<input>');
    input2.attr('type', 'hidden');
    input2.attr('name', 'edate');
    input2.attr('value', $("#endTime").val());

    var input3 = $('<input>');
    input3.attr('type', 'hidden');
    input3.attr('name', 'orgid');
    input3.attr('value', $("#sorganizationHidden").val());

    var input4 = $('<input>');
    input4.attr('type', 'hidden');
    input4.attr('name', 'uid');
    input4.attr('value', $("#traderName").attr("userid"));

    var input5 = $('<input>');
    input5.attr('type', 'hidden');
    input5.attr('name', 'buyerid');
    input5.attr('value', $("#accountid").attr("accountid"));

    //交易状态：
    var input6 = $('<input>');
    input6.attr('type', 'hidden');
    input6.attr('name', 'status');
    input6.attr('value', getStatus());

    var input7 = $('<input>');
    input7.attr('type', 'hidden');
    input7.attr('name', 'sellId');
    input7.attr('value', $("#sellid").attr("accountid"));

    var input8 = $('<input>');
    input8.attr('type', 'hidden');
    input8.attr('name', 'secsdate');
    input8.attr('value', $("#secStartTime").val());

    var input9 = $('<input>');
    input9.attr('type', 'hidden');
    input9.attr('name', 'secedate');
    input9.attr('value', $("#secEndTime").val());
    $('body').append(form);
    form.append(input1);
    form.append(input2);
    form.append(input3);
    form.append(input4);
    form.append(input5);
    form.append(input6);
    form.append(input7);
    form.append(input8);
    form.append(input9);
    form.submit();
    form.remove();
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
                    var code = checkbox.val();
                    $("#show_options").append("<label class=\"option_item_"+index+"\"><input type='hidden' name='code' value='"+code+"'/></label>");
                }else{
                    $("#show_options").children(".option_item_"+index).remove();
                }

        })

    });
}

