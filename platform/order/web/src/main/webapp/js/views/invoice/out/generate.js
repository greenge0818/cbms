/**
 * 销项票开单。。。
 * Created by lcw on 2015/9/15.
 */

var tobeconfirmedTable = null;
var nowMonth = new Date().getMonth();
var mlist = [];
$().ready(function () {
    $("#invoiceForm").verifyForm();

    var invoiceInTable = $("#invoiceInTable");            // 已确认未认证的进项票
    var suspendTable = $("#suspendTable");                // 已选择暂缓认证的发票
    var waitTable = $("#waitTable");                      // 已选择待开的销项票
    var waitTbody = $(waitTable).find("tbody")[0];
    var tobeOutTable = $("#tobeOutTable");                // 待开销项票
    var selectedNotOutTable = $("#selectedNotOutTable");  // 已选择不开的销项票

    /**
     * 移动到暂缓认证的发票
     */
    invoiceInTable.on("click", "a[name='shift']", function () {
        var currentRow = $(this).closest("tr");

        var currentDate = new Date($(currentRow).find("span[name='date']").text());
        var currentMonth = currentDate.getMonth();
        if (nowMonth > currentMonth) {
            cbms.alert("上月的票的必须要认证，不能暂缓认证！");
            return;
        }
        var invoiceInId = $(currentRow).attr("invoiceinid");

        // 默认不设置排序值，统一由updateIndex方法设置
        var firstTdHtml = "<td><span name='index'></span></td>";
        currentRow.prepend(firstTdHtml);
        var lastTdHtml = '<a href="javascript:" name="billing" invoiceinid="' + invoiceInId + '">开票</a>';
        lastTdHtml += '&nbsp;<a href="javascript:" name="cancel" invoiceinid="' + invoiceInId + '">取消</a>';
        $(currentRow).find("td").last().html(lastTdHtml);
        $(suspendTable).append(currentRow);

        getoutitemdetails();
        updateIndex();
    });

    /**
     * 取消暂缓，返回到已确认未认证的进项票
     */
    suspendTable.on("click", "a[name='cancel']", function () {
        var currentRow = $(this).closest("tr");
        var index = $(currentRow).find("td").first().text();
        var lastTdHtml = '<a href="javascript:" name="shift">→</a>';
        $(currentRow).find("td").first().remove();
        $(currentRow).find("td").last().html(lastTdHtml);
        $(invoiceInTable).append(currentRow);

        // 更新 已选择待开的销项票
        $(waitTbody).find("tr").each(function () {
            var thisIndex = $(this).find("td").first().text();
            if (index == thisIndex) {
                $(this).remove();
            }
        });

        if ($(waitTbody).find("tr").length == 0) {
            $("#waitRegion").hide();
        }
        getoutitemdetails();
        updateIndex();
    });

    /**
     * 暂缓区开票
     */
    suspendTable.on("click", "a[name='billing']", function () {
        if ($(this).attr("opened") == "true") {
            return;
        }
        $(this).attr("opened", true);
        $(this).text("已开");
        $("#waitRegion").show();
        var invoiceInId = $(this).attr("invoiceinid");

        var currentRow = $(this).closest("tr");
        var thisIndex = $(currentRow).find("td").first().text();

        cbms.loading();
        $.ajax({
            type: 'post',
            url: Context.PATH + "/invoice/out/applyitemdetails.html",
            data: {invoiceInId: invoiceInId, ids: $("#applyIds").val()},
            error: function (s) {
                cbms.closeLoading();
            }
            , success: function (result) {
                cbms.closeLoading();
                if (result) {
                    if (result.success) {
                        generateInDetails(result.data, thisIndex, invoiceInId);
                    } else {
                        cbms.alert(result.data);
                    }
                } else {
                    cbms.alert("获取数据失败");
                }
            }
        });
    });

    /**
     * 修改值触发价税合计
     */
    waitTable.on("input", "input[name='amount']", function () {
        var maxAmount = parseFloat($(this).attr("maxamount"));
        var amount = parseFloat($.trim($(this).val()));
        if (!isNaN(amount) && amount > maxAmount) {
            alert("不能大于默认申请金额！");
            $(this).val(maxAmount.toFixed(2));
        }
        // 计算开票重量
        var row = $(this).closest("tr");
        var price = parseFloat(parseFloat($(row).attr("price")).toFixed(2));
        var weight = parseFloat(parseFloat($(row).find("span[name='weight']").text()).toFixed(6));
        var actualWeight = (amount.div(price)).toFixed(6);
        $(row).find("span[name='weight']").text(actualWeight);

        calculation();
    });

    /**
     * 切换步数选择
     */
    $("#secTab li a").click(function () {
        var parentLi = $(this).closest("li");
        if ($(parentLi).hasClass("active")) {
            return;
        }
        var index = $(this).closest("li").index();
        if (index == 1) {
            if (!setlistensSave())return;
            getoutitemdetails();
        }
        $(parentLi).addClass("active").siblings().removeClass("active");
        $("#step1Layer,#step2Layer").toggle();
    });

    /**
     * 移动到已选择不开的销项票
     */
    tobeOutTable.on("click", "a[name='shift']", function () {
        var currentRow = $(this).closest("tr");
        var outDetailId = $(currentRow).attr("outdetailid");
        var weightHtml = "<td><span name='weight'>" + $(currentRow).attr("weight") + "</span></td>";
        $(weightHtml).insertAfter(currentRow.find("td").eq(3));
        var lastTdHtml = '<a href="javascript:" name="cancel" outdetailid="' + outDetailId + '">取消</a>';
        $(currentRow).find("td").last().html(lastTdHtml);
        $(currentRow).attr("isdefer",'yes');
        $(selectedNotOutTable).append(currentRow);

        calculation();
    });

    /**
     * 取消选择不开，返回到待开销项票
     */
    selectedNotOutTable.on("click", "a[name='cancel']", function () {
        var currentRow = $(this).closest("tr");
        var lastTdHtml = '<a href="javascript:" name="shift">→</a>';
        $(currentRow).find("td").eq(4).remove();
        $(currentRow).find("td").last().html(lastTdHtml);
        $(tobeOutTable).append(currentRow);

        calculation();
        updateIndex();
    });

    /**
     * 导出excel
     */
    $("#importExcel").click(function () {
        var invoicesJson = {
            invoiceInArray: invoiceInIdArray(invoiceInTable),
            suspendArray: invoiceInIdArray(suspendTable),
            waitArray: waitArray()
        };
        downloadExcel(invoicesJson);
    });

    /**
     * 下一步
     */
    $("#nextStep").click(function () {
        $("#secTab li a").eq(1).click();
    });

    /**
     * 返回，返回到上一步
     */
    $("#previous").click(function () {
        $("#secTab li a").eq(0).click();
    });

    /**
     * 取消，返回到代开票列表
     */
    $("#cancel").click(function () {
        location.href = $("#back").attr("href");
    });

    /**
     * 打开 查看待确认的进项票弹窗
     */
    $("#tobeInvoiceInView").click(function () {
        cbms.getDialog("已申请开销项票相应未确认的进项票", Context.PATH + "/invoice/out/tobeconfirmedinvin.html");
    });

    /**
     * 关闭 查看待确认的进项票弹窗
     */
    $(document).on("click", "#closeInDialog", function () {
        cbms.closeDialog();
    });

    /**
     * 提交，生成开票清单并进项票提交认证
     */
    $(".submit-save").click(function () {
        if (!setlistensSave())return;
        var applyIds = $("#applyIds").val().split(','); // 已确认未认证的进项票
        var invoiceInIds = [];                          // 已确认未认证的进项票
        var suspendIds = [];                            // 已选择不开的销项票ID集合
        var tobeOutIdIds = [];                          // 待开销项ID集合
        var selectedNotOutIds = [];                     // 已选择不开的销项票ID集合
        
        var sendstatus = $(this).attr("sendstatus");
        
        $(invoiceInTable).find("tbody > tr").each(function () {
            invoiceInIds.push($(this).attr("invoiceinid"));
        });
        $(suspendTable).find("tbody > tr").each(function () {
            suspendIds.push($(this).attr("invoiceinid"));
        });
        
        //二结欠款的最大限额
        var InvoiceOutApplySecond = $("input[name='InvoiceOutApplySecond']").val();
        
        $(tobeOutTable).find("tbody > tr").each(function () {
        	
        	//modify by tuxming 20150518
        	var row = $(this);
        	//二结欠款金额
        	var balanceSecondSettlement = row.find("input[name='balanceSecondSettlement']").val();	
        	//凭证审核状态
        	var credentialStatus = row.find("input[name='credentialStatus']").val();
        	
        	if(sendstatus=='SEND'){
        		if(balanceSecondSettlement/1+InvoiceOutApplySecond/1 >=0 && credentialStatus=='1'){
        			tobeOutIdIds.push($(this).attr("outdetailid"));
        		}
        	}else if(sendstatus=='UNSEND'){
        		if(balanceSecondSettlement/1 + InvoiceOutApplySecond/1 < 0 || credentialStatus=='0'){
        			tobeOutIdIds.push($(this).attr("outdetailid"));
        		}
        	}else{
        		tobeOutIdIds.push($(this).attr("outdetailid"));
        	}
            //end
        });
        $(selectedNotOutTable).find("tbody > tr").each(function () {
            selectedNotOutIds.push($(this).attr("outdetailid"));
        });
        
        //add by tuxming 20150518
        if(tobeOutIdIds.length == 0){
        	cbms.alert("没有符合条件的待开销项票!");
        	return ;
        }//end
        
        if (applyIds.length == 0) {
            cbms.alert("没有申请单！");
            return;
        }
        var waitArr = waitArray();
        if (waitArr.length == 0 && tobeOutIdIds.length == 0) {
            cbms.alert("没有需要生成的清单数据！");
            return;
        }
        var invoicesJson = {
            applyIds: applyIds,
            invoiceInIds: invoiceInIds,
            suspendIds: suspendIds,
            waitArray: waitArr,
            tobeOutIdIds: tobeOutIdIds,
            selectedNotOutIds: selectedNotOutIds,
            modifyNum: JSON.parse(_modifyNumStr)
        };

        //add by tuxianming 20160516
        invoicesJson.sendStatus=sendstatus;
        
        cbms.confirm("确定要生成开票清单吗？", null, function () {
            cbms.loading();
            $("#submit").prop("disabled", true);
            $.ajax({
                type: 'post',
                url: Context.PATH + "/invoice/out/generatechecklist.html",
                data: {
                    invoicesJson: JSON.stringify(invoicesJson)
                },
                error: function (s) {
                    $("#submit").prop("disabled", false);
                    cbms.closeLoading();
                }
                , success: function (result) {
                    cbms.closeLoading();
                    if (result) {
                        if (result.success) {
                            cbms.alert("生成开票清单成功！", function () {
                                $("#cancel").click();
                            });
                        }
                        else {
                            $("#submit").prop("disabled", false);
                            var msg = result.data;
                            cbms.alert(msg, function () {
                                if (msg.indexOf("该申请已处理") > -1) {
                                    $("#cancel").click();
                                }
                            });
                        }
                    } else {
                        $("#submit").prop("disabled", false);
                        cbms.alert("生成开票清单失败");
                    }
                }
            });
        });
    });

    $("#autoSelect").click(function () {
        var rows = $(invoiceInTable).find("tbody > tr");
        if (rows.length > 0) {
            var recommendAmount = parseFloat(parseFloat($("#recommendLeaveAmount").text()).toFixed(2));
            var curlist = [];
            var curObj = {};
            $(rows).each(function () {
                curObj = {
                    id: parseInt($(this).attr("invoiceinid")),
                    cost: parseFloat(parseFloat($(this).find("input[name='amount']").val()).toFixed(2))
                }
                curlist.push(curObj);
            });
            var curtotal = invoiceInSum();
            getclosest(curlist, curtotal, recommendAmount);
            if (mlist.length > 0) {
                for (var i = 0; i < mlist; i++) {
                    $(rows).each(function () {
                        if (parseInt($(this).attr("invoiceinid")) == mlist[i]) {
                            $(this).find("a[name='shift']").click();
                        }
                    });
                }
            }
        }
    });
    /**
     * 合计 已选择不开的销项票
     */
    function selectedNotOutSum() {
        var sum = sumAmount($(selectedNotOutTable).find("input[name='amount']"));
        $("#selectedNotOutAmount").val(sum);
        $("#selectedNotOutAmountDisplay").text(formatMoney(sum, 2));
        return sum;
    }

    /**
     * 计算
     */
    function calculation() {
        var openedAmount = parseFloat($("#openedAmount").val());                // 本月已开
        var approvedMonthAmount = parseFloat($("#approvedMonthAmount").val());  // 本月已认证
        var inAmount = parseFloat($("#inAmount").val());                        // 进项总额
        var actualRax = parseFloat($("#actualRax").val());                      // 实际税负
        var actualRaxAmount = parseFloat($("#actualRaxAmount").val());          // 实际税额
        var approvedAmount = parseFloat($("#approvedAmount").val());            // 认证总额
        var actualOutAmount = parseFloat($("#actualOutAmount").val());          // 实开销项
        var applyTotalAmount = parseFloat($("#applyTotalAmount").val());        // 销项票申请总额
        var tobeInAmount = 0;													// 当前申请中进项票还未确认的金额
        if ($("#tobeInAmount").val()) {
            tobeInAmount = parseFloat($("#tobeInAmount").val());
        }

        invoiceInSum();
        var suspendSumAmount = suspendSum();
        waitSum();
        tobeOutSum();
        var selectedNotOutAmount = selectedNotOutSum();

        // 实开销项（元）= 本月已开总额（所有服务中心）+ 销项票申请总额（所有服务中心）
        // - 已选择不开的销项票（所有服务中心）- 当前申请中已选择不开的销项票
        actualOutAmount = openedAmount.add(applyTotalAmount).sub(selectedNotOutAmount);
        $("#actualOutAmount").val(actualOutAmount);
        $("#actualOutAmountDisplay").text(formatMoney(actualOutAmount, 2));

        // 认证总额（元）= 本月已认证进项票（所有服务中心）+ 已到未认证进项票（所有服务中心）
        // - 暂缓认证的进项票（所有服务中心）- 当前申请中暂缓认证的进项票
        var invoiceInSumAmount = parseFloat($("#invoiceInSumAmount").val()); // 已到未认证进项票（所有服务中心）
        approvedAmount = approvedMonthAmount.add(invoiceInSumAmount).sub(suspendSumAmount);
        $("#approvedAmount").val(approvedAmount);
        $("#approvedAmountDisplay").text(formatMoney(approvedAmount, 2));

        // 实际税额 = (实开销项-认证总额)/1.17*0.17
        actualRaxAmount = (actualOutAmount.sub(approvedAmount)).div(_taxRate.add(1)).mul(_taxRate);

        // 实际税负 = ((实开销项-认证总额)/1.17*0.17）/ 实开销项
        actualRax = actualRaxAmount.div(actualOutAmount);

        actualRaxAmount = actualRaxAmount < 0 ? 0 : actualRaxAmount;
        $("#actualRaxAmount").val(actualRaxAmount);
        $("#actualRaxAmountDisplay").text(formatMoney(actualRaxAmount, 2));

        actualRax = actualRax < 0 ? 0 : actualRax;
        $("#actualRax").val(actualRax);
        $("#actualRaxDisplay").text(formatMoney(actualRax.mul(1000), 2) + "‰");

        //推荐预留金额 = 实开销项 - 预计认证金额(进项总额 - 预计进项与销项差值(实开销项 *1.17/0.17*预期税负))
        var taxRate = parseFloat(parseFloat($("#taxRate").val()).toFixed(6));
        var taxAmount = parseFloat(parseFloat($("#taxAmount").val()).toFixed(6));
        if (taxRate && taxRate > 0) {
            // 预计进项与销项差值 = 本月预计应开销项 * 1.17/0.17*0.0005
            var differenceAmount = actualOutAmount.mul(1.17.div(0.17).mul(parseFloat(taxRate.div(1000).toFixed(6))));
            taxAmount = actualOutAmount - (inAmount - differenceAmount);
        }
        if (taxAmount > actualRaxAmount) {
            $("#recommendLeaveAmount").text((taxAmount).toFixed(2));
            $("#autoSelect").removeClass("none");
        } else {
            $("#recommendLeaveAmount").text("0.00");
            $("#autoSelect").addClass("none");
        }
    }

    /**
     * 已确认未认证的进项票/已选择暂缓认证的发票 的详情集合
     */
    function invoiceInIdArray(table) {
        var result = [];
        $(table).find("tbody > tr").each(function () {
            var item = {
                id: $(this).attr("invoiceinid"),
                index: $(this).find("span[name='index']").length > 0 ? $(this).find("span[name='index']").text() : "",
                invoiceDate: $(this).find("span[name='date']").text(),
                code: $(this).find("span[name='code']").text(),
                orgName: $(this).find("span[name='org']").text(),
                sellerName: $(this).find("span[name='seller']").text(),
                amount: $(this).find("input[name='amount']").val()
            };
            result.push(item);
        });
        return result;
    }

    /**
     * 生成进项票详情信息
     * @param data 数据
     * @param index 已选择暂缓认证的发票的排序值
     */
    function generateInDetails(data, index, invoiceInId) {
        if (data == null) {
            return;
        }
        var row = '';
        var usefulAmount = 0, actualAmount = 0, usefulWeight = 0, actualWeight = 0;
        for (var i = 0; i < data.length; i++) {
            row = '<tr detailid="' + data[i].id + '" invoiceinid="' + invoiceInId + '" price="' + data[i].price + '" orgId="' + data[i].orgId + '">';
            row += '<td><span name="index">' + index + '</span></td>';
            row += '<td><span name="buyername">' + data[i].buyerName + '</span></td>';
            row += '<td><span name="nsortname">' + data[i].nsortName + '</span></td>';
            row += '<td><span name="material">' + data[i].material + '</span></td>';
            row += '<td><span name="spec">' + data[i].spec + '</span></td>';
            actualWeight = data[i].actualWeight != null ? data[i].actualWeight : 0;
            usefulWeight = data[i].weight.sub(actualWeight);
            row += '<td><span name="weight">' + usefulWeight.toFixed(6) + '</span></td>';
            actualAmount = data[i].actualAmount != null ? data[i].actualAmount : 0;
            usefulAmount = data[i].amount.sub(actualAmount);
            row += '<td><input type="text" name="amount" maxamount="' + usefulAmount + '" class="c-text text-right" must="1" verify="rmb" value="' + usefulAmount.toFixed(2) + '" /></td>';
            row += '</tr>';
            $(waitTbody).append(row);
        }
        calculation();
    }

    /**
     * 更新table的序列值
     */
    function updateIndex() {
        var rows = $(suspendTable).find("tbody > tr");
        var rowSize = $(rows).size();
        var row, invId, tempInvId;

        // 更新已选择暂缓认证的发票序列值
        for (var i = 0; i < rowSize; i++) {
            row = $(rows).eq(i);
            $(row).find("span[name='index']").text(i + 1);
            invId = $(row).attr("invoiceinid");

            // 更新已选择待开的销项票序列值
            $(waitTbody).find("tr").each(function () {
                tempInvId = $(this).attr("invoiceinid");
                if (invId == tempInvId) {
                    $(this).find("span[name='index']").text(i + 1);
                }
            });
        }
    }

    /**
     * 合计金额
     * @param data 数据集合
     * @returns {number} 合计结果
     */
    function sumAmount(data) {
        var total = 0;
        var tempValue = 0;
        $(data).each(function () {
            var type = $(this).attr("type");
            if (type != undefined && type.length > 0) {
                tempValue = parseFloat(parseFloat($(this).val()).toFixed(2));
            }
            else {
                tempValue = parseFloat(parseFloat($(this).text()).toFixed(2));
            }
            total = total.add(tempValue);
        });
        return total;
    }

    /**
     * 合计 已确认未认证的进项票
     */
    function invoiceInSum() {
        var sum = sumAmount($(invoiceInTable).find("input[name='amount']"));
        $("#invoiceInAmount").val(sum);
        $("#invoiceInAmountDisplay").text(formatMoney(sum, 2));
        return sum;
    }

    /**
     * 合计 已选择暂缓认证的发票
     */
    function suspendSum() {
        var sum = sumAmount($(suspendTable).find("input[name='amount']"));
        $("#suspendAmount").val(sum);
        $("#suspendAmountDisplay").text(formatMoney(sum, 2));
        return sum;
    }

    /**
     * 合计 已选择待开的销项票
     */
    function waitSum() {
        var sum = sumAmount($(waitTbody).find("input[name='amount']"));
        $("#selectedOutAmount").val(sum);
        $("#selectedOutAmountDisplay").text(formatMoney(sum, 2));
        return sum;
    }

    /**
     * 合计 待开销项票
     */
    function tobeOutSum() {
        var sum = sumAmount($(tobeOutTable).find("input[name='amount']"));
        $("#tobeOutAmount").val(sum);
        $("#tobeOutAmountDisplay").text(formatMoney(sum, 2));
        return sum;
    }

    /**
     * 已选择待开的销项票的详情集合
     */
    function waitArray() {
        var result = [];
        $(waitTable).find("tbody > tr").each(function () {
            var item = {
                id: $(this).attr("detailid"),
                orgId: $(this).attr("orgId"),
                index: $(this).find("span[name='index']").text(),
                buyerName: $(this).find("span[name='buyername']").text(),
                nsortName: $(this).find("span[name='nsortname']").text(),
                material: $(this).find("span[name='material']").text(),
                spec: $(this).find("span[name='spec']").text(),
                weight: $(this).find("span[name='weight']").text(),
                amount: $.trim($(this).find("input[name='amount']").val())
            };
            result.push(item);
        });
        return result;
    }

    function getoutitemdetails() {
        var applyIds = $("#applyIds").val().split(','); // 申请id集合
        var suspendIds = [];                            // 已选择暂缓认证的发票
        $(suspendTable).find("tbody > tr").each(function () {
            suspendIds.push($(this).attr("invoiceinid"));
        });
        var invoicesJson = {
            applyIds: applyIds,
            notInvoiceInIds: suspendIds
        };

        cbms.loading();
        $.ajax({
            type: 'post',
            url: Context.PATH + "/invoice/out/getoutitemdetails.html",
            data: {
                invoicesJson: JSON.stringify(invoicesJson)
            },
            error: function (s) {
                $("#submit").prop("disabled", false);
                cbms.closeLoading();
            }
            , success: function (result) {
                cbms.closeLoading();
                if (result) {
                    if (result.success) {
                    	
                    	var InvoiceOutApplySecond = $("input[name='InvoiceOutApplySecond']").val();
                    	
                        $(tobeOutTable).find("tbody").empty();
                        $(selectedNotOutTable).find("tbody > tr[isdefer='yes']").empty();
                        for (var i = 0; i < result.data.length; i++) {
                            var row = result.data[i];
                            var usefulAmount = row.amount;
                            if (row.actualAmount != null) {
                                usefulAmount = row.amount.sub(row.actualAmount);
                            }
                            var tr = '<tr outdetailid="' + row.id + '" weight="' + row.weight + '">';
                            tr += '<td><span name="buyer">' + row.buyerName + '</span></td>';
                            tr += '<td><span name="nsortname">' + row.nsortName + '</span></td>';
                            tr += '<td><span name="material">' + row.material + '</span></td>';
                            tr += '<td><span name="spec">' + row.spec + '</span></td>';
                            tr += '<td class="text-right"><input type="hidden" name="amount" value="' + usefulAmount + '"/><span name="amount">' + formatMoney(usefulAmount, 2) + '</span></td>';
                            
                            //modify by  tuxianming 20160525
                            var style = "";
                            var bs = row.balanceSecondSettlement;
                            if((bs/1)+(InvoiceOutApplySecond/1) < 0 && Math.abs(bs) > InvoiceOutApplySecond){
                            	style = "color:red;";
                            }
                            
                            if(bs/1 >0){
                            	bs = 0;
                            }else{
                            	 bs = Math.abs(bs);
                            }
                            
                            tr += '<td><input type="hidden" name="balanceSecondSettlement" value="'+row.balanceSecondSettlement+'"/><span style='+style+'>' + bs + '</span></td>';
                            
                            var style1 = "";
                            var csvalue = "1";
                            if(row.credentialStatus==false){
                            	style1 = "color:red;";
                            	csvalue = "0";
                            }else{
                            	csvalue = "1";
                            }
                            //end modify by  tuxianming 20160525
                            
                            tr += '<td><input type="hidden" name="credentialStatus" value="'+csvalue+'"/><span style="'+style1+'">'+row.credentialStatusStr+'</span<</td>';
                            tr += '<td><a href="javascript:" name="shift">→</a></td>';
                            tr += "</tr>";
                            $(tobeOutTable).find("tbody").append(tr);
                        }

                        calculation();
                    }
                    else {
                        cbms.alert(result.data);
                    }
                } else {
                    cbms.alert("获取待开销项票失败");
                }
            }
        });
    }

    tobeOutSum();
    getoutitemdetails();
});


// 下载excel文件
function downloadExcel(invoicesJson) {
    var form = $("<form>");
    form.attr('style', 'display:none');
    form.attr('target', '');
    form.attr('method', 'post');
    form.attr('action', Context.PATH + "/invoice/out/importtoexcel.html");

    var input1 = $('<input>');
    input1.attr('type', 'hidden');
    input1.attr('name', 'invoicesJson');
    input1.attr('value', encodeURI(JSON.stringify(invoicesJson)));

    $('body').append(form);
    form.append(input1);

    form.submit();
    form.remove();
}


/**
 * 自动选择最接近的数组
 * @param curlist   当前所有对象列表
 * @param curtotal  当前合计
 * @param target    目标值
 * @returns {Array} 最优值对应的id列表
 */
function getclosest(curlist, curtotal, target) {
    if (curtotal < target) {
        return curtotal;
    }
    var min = curtotal;
    for (var i = 0; i < curlist.length; i++) {
        var result = 0
        if (curtotal - curlist[i].cost < target) {
            result = curtotal;
            if (result <= min) {
                if (result < min || curlist.length < mlist.length || mlist.length == 0) {
                    mlist.length = 0;
                    for (var x = 0; x < curlist.length; x++) {
                        mlist.push(curlist[x].id);
                    }
                }
                min = result;
            }
            continue;
        }
        else {
            var sublist = curlist.slice(0);
            sublist.splice(i, 1);
            result = getclosest(sublist, curtotal - curlist[i].cost, target);
            if (result <= min) {
                if (result < min || sublist.length < mlist.length || mlist.length == 0) {
                    mlist.length = 0;
                    for (var x = 0; x < sublist.length; x++) {
                        mlist.push(sublist[x].id);
                    }
                }
                min = result;
            }
        }
    }
    return min;
}