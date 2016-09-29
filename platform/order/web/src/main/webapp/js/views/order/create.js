/**
 * Created by lcw on 2015/7/18.
 */

var currentLine = -1;   // 当前下拉选择的Index值
var acceptDraftData = null;    // 银票数据

var isShowAcceptDraft = $("#isShowAcceptDraft").val();
$().ready(function () {
    //从报价单生成开单数据后做统计。
    if(!utils.isEmpty(CreateOrderPage.purchaseOrderId)){
        orderTotal();
    }

    setSelectValue();
    bindCity();
    $("#createForm").verifyForm();


    // 选择交易员列表
    $("#userList").change(function () {
        var checkText = $(this).find("option:selected").text();
        $("#user").val(checkText).attr("userid", $(this).val());
        $("select[name='acceptDraft']").empty();
    });

    // 重新开单，存在交易员
    if ($("#owner").size() > 0) {
        $("#userList").val($("#owner").attr("userid"));
    }

    // 默认选中第一个
    $("#userList").change();

    // 触发公司名称下拉列表
    $(document).on("input", "#buyer", function (e) {
        var event = e || window.event;
        if (event.keyCode == 38 || event.keyCode == 40 || event.keyCode == 13) {
            return;
        }
        var buyerName = $.trim($("#buyer").val());
        var userId = parseInt($("#user").attr("userid"));
        if (userId == 0) {
            alert("交易员输入错误！");
            $("#user").focus();
            return;
        }
        $("select[name='acceptDraft']").empty();
        getBuyer(userId, buyerName);
    });

    // 联系人名称下拉列表
    $(document).on("input", "#contact", function (e) {
        var event = e || window.event;
        if (event.keyCode == 38 || event.keyCode == 40 || event.keyCode == 13) {
            return;
        }
        $("#contactMsg").hide();
        if(CreateOrderPage.purchaseOrderId == "") {
            $("#contactTel").val("");
        }
        var departmentId = parseInt($("#department").val());
        if (departmentId > 0) {
            getContact(departmentId, $.trim($(this).val()));
        }
    });

    // 绑定下拉列表
    $("input[dropdown='dropdown']").keydown(function (e) {
        var event = e || window.event;
        keyboardSelect($(this), event);
    });

    // 选择交易员
    $("body").on("click", "#userDropdown ul li a", function () {
        $("#user").val($(this).text());
        $("#user").attr("userid", $(this).attr("userid"));
        $("#userDropdown").hide();
    });

    // 选择买家公司
    $("body").on("click", "#buyerDropdown ul li a", function () {
        var accountid = $(this).attr("accountid");
        $("#buyer").val($(this).attr("accountname"));
        $("#buyer").attr("accountid", accountid);
        $("#buyerDropdown").hide();
        getDepartment(accountid,$("#department"),true);
        //getContact(accountid, $(this).text());

        var balance = parseFloat($(this).attr("balance"));
        if (isNaN(balance) == false) {
            showBalance(balance);
        }
        if (isShowAcceptDraft == "true") {
            getAcceptDraft(accountid);
        }
    });

    // 选择买家部门
    $("#department").change(function(){
        var accountId = parseInt($(this).val());
        getContact(accountId, "");
    });

    // 选择联系人
    $("body").on("click", "#contactDropdown ul li a", function () {
        $("#contact").val($(this).text());
        $("#contact").attr("contactid", $(this).attr("contactid"));
        $("#contactTel").val($(this).attr("tel"));
        $("#contactDropdown,#contactMsg").hide();
    });

    // 复制资源
    $("#table_resource").on("click", "a[copy='copy']", function () {
        var currentRow = $(this).parents('tr:first');
        // 当前行
        if (isEmptyRow(currentRow)) {
            cbms.alert("请先输入当前行数据！");
            return;
        }
        var cloneRow = $(currentRow).clone();       // 复制当前行

        // 部门赋值
        var departmentId = $(currentRow).find("[name='department']").val();
        $(cloneRow).find("[name='department']").val(departmentId);
        getSellerContact(departmentId,$(cloneRow).find("[name='department']"));

        // 获取省市value
        var provinceVal = 0, cityVal = 0;
        var temp_provinceVal = $(currentRow).find("[name='province']").val();
        if (temp_provinceVal != "") {
            provinceVal = parseInt(temp_provinceVal);
        }
        var temp_cityVal = $(currentRow).find("[name='city']").val();
        if (temp_cityVal != "") {
            cityVal = parseInt(temp_cityVal);
        }

        // 修改赋值
        var weightConceptVal = $(currentRow).find("[name='weightConcept']").val();
        $(cloneRow).find("[name='weightConcept']").val(weightConceptVal);

        var acceptDraftVal = $(currentRow).find("[name='acceptDraft']").val();
        $(cloneRow).find("[name='acceptDraft']").val("");
        // 追加在当前行后面
        $(currentRow).after($(cloneRow));

        // 绑定省市联动
        bindRegionData($(cloneRow).find("[name='province']"), $(cloneRow).find("[name='city']"), null, provinceVal, cityVal);

        updateIndex();
        orderTotal();
    });

    // 删除资源
    $("#table_resource").on("click", "a[delete='delete']", function () {
        var rowsSize = $("#table_resource tbody tr").size();
        var currentRow = $(this).parents('tr:first');
        // 只有一行数据时不能被删除
        if (rowsSize > 1) {
            // 空行直接删除
            if (isEmptyRow(currentRow)) {
                $(currentRow).remove();
                updateIndex();
                orderTotal();
            }
            else {
                cbms.confirm("确认删除本条资源？", null, function () {
                    $(currentRow).remove();
                    updateIndex();
                    orderTotal();
                });
            }
        }
        else {
            cbms.alert("不能全部删除！");
        }
    });

    // 选择卖家公司
    $(document).on("click", "#searchAccountList li a", function () {
        var accountId = $(this).attr("accountid");
        var input = "input[inputid='" + $("#searchAccountList").attr("inputid") + "']";
        var department = $(input).closest("tr").find("select[name='department']");
        getDepartment(accountId,$(department),false);
    });

    // 选择卖家部门
    $(document).on("change", "select[name='department']", function () {
        var accountId = parseInt($(this).val());
        getSellerContact(accountId, $(this));
    });

    // 触发统计
    $("#table_resource").on("input", "input[name='costPrice'],input[name='weight'],input[name='quantity']", function () {
        var currentRow = $(this).parents('tr:first');     // 获取当前行
        calculationAmount(currentRow);
        orderTotal();
    });

    // 销售单价、采购单价最多输入小数点后两位
    $("#table_resource").on("input", "input[name='costPrice'],input[name='dealPrice']", function () {
        //TwoNumAfterDotInputOnly($(this).val());
        var tmpVal = $(this).val();
        if(tmpVal.length>0) {
            var code = /^0{1}([.]\d{1,2})?$|^[1-9]\d*([.]{1}[0-9]{1,2})?$/;
            if (!code.test(tmpVal)) {
                var lastLength = tmpVal.substr(tmpVal.length - 1, tmpVal.length);
                if (lastLength != ".") {
                    $(this).val(tmpVal.replace(tmpVal, tmpVal.substr(0, tmpVal.length - 1)));
                }
            }
        }
    });

    // 销售价同步到采购价
    $("#table_resource").on("blur", "input[name='dealPrice']", function () {
        var currentRow = $(this).parents('tr:first');     // 获取当前行
        var dealPrice = $(this).val();
        var costPriceInput = $(currentRow).find("input[name = 'costPrice']");
        if ($.trim($(costPriceInput).val()) == "") {
            $(costPriceInput).val(dealPrice);
        }
        calculationAmount(currentRow);
        orderTotal();
    });
 

    //手动添加
    $("#manualAdd").click(function () {
        var isEmpty = false;
        $("#table_resource tbody tr").each(function () {
            var currentRow = $(this);
            if (isEmptyRow(currentRow)) {
                cbms.alert("请先输入空行的数据！");
                isEmpty = true;
                return false;
            }
        });
        if (!isEmpty) {
            addEmptyRow();
        }
    });

    //搜索添加
    $("#searchAdd").click(function () {
        cbms.getDialog("搜索资源", Context.PATH + "/order/searchresource.html");
    });

    // 运杂费
    $(document).on("input", "#shipFee", function (e) {
        orderTotal();
    });

    // 提交审核
    $("#submit").click(function () {
        if (!setlistensSave())return;
        var sellerId;
        var seller;
        var strSellerName= $.trim($("#table_resource tbody tr").find("input[name='sellerName']").val());
        $("#table_resource tbody tr").each(function () {
            seller = $(this).find("input[name='sellerName']");
            sellerId = $(seller).attr("accountid");
        });
        //当前开单重量不能超过卖家客户本身或风控设置的重量
        var isPass=    confineorder(sellerId,$.trim($("#totalWeight").text()));
        if (isPass == false) {
            return;
        }
        var estimate=newOrderHint(strSellerName);
        //风控交易量和交易笔数的控制
        if(estimate[0]==false){
        	cbms.alert(estimate[1]);
            return false;
        }else{
            if(estimate[1] != '')
                cbms.alert(estimate[1],createOrder);
            else
                createOrder();
        }

    });
    function createOrder(){
        var secType = $('input:radio[name="paymentType"]:checked').val();//给卖家付款方式
        var delayDays = $('#delayNum').val();//延后天数
        if(secType == "1" && (delayDays <= 1 || delayDays >= 100)){
            cbms.alert("当给卖家付款选择提货后延期付款，提货后付款延期的天数应该大于1小于100的正整数！");
            isPass = false;
            return false;
        }
        var buyer = $("#buyer");
        var user = $("#user");
        var contact = $("#contact");
        var buyAccountId = $(buyer).attr("accountid");
        var orderId = 0;
        var sellerConsignType = "consign";
        // 取消修改操作
        //var tempOrderId = $("#orderId").val();
        //if (tempOrderId != "" && tempOrderId != "0") {
        //    orderId = parseInt(tempOrderId);
        //}
        var isPass = true;
        var sellerDepartmentArray = [];  // 卖家部门ID集合
        var arrayResource = []; // 资源对象集合
        var seller;
        var sellerId = "", sellerName = "";
        var tempArr = [];//存放卖家名称
        $("#table_resource tbody tr").each(function () {
            seller = $(this).find("input[name='sellerName']");
            sellerId = $(seller).attr("accountid");
            sellerName = $.trim($(seller).val());
            // 卖家账号必须存在
            if (sellerId == null || sellerId == "" || sellerId == "0") {
                cbms.alert("未找到对应的代运营客户，请重新输入！");
                $(this).find("input[name='sellerName']").focus();
                isPass = false;
                return false;
            }
            // 买卖双方不能相同
            if (sellerId == buyAccountId) {
                cbms.alert("买家与卖家不能相同！");
                isPass = false;
                return false;
            }

            var departmentId = $(this).find("select[name='department']").find("option:selected").val();
            if ($.inArray(departmentId, sellerDepartmentArray) == -1) {
                sellerDepartmentArray.push(departmentId);
            }
            // edit by rabbit 只能添加一个卖家且只能有一个部门
            if (sellerDepartmentArray.length > 1) {
                cbms.alert("一笔交易单只能添加一个卖家一个部门的资源。");

                isPass = false;
                return false;
            }
            //add by wangxianjun 20151215，当付款方式选择延时，添加资源时必须是同一个卖家
            if (secType == "1" && $.inArray(sellerName, tempArr) == -1) {
                tempArr.push(sellerName);
                if(tempArr.length > 1) {
                    cbms.alert("当给卖家付款选择提货后延期付款，添加资源时必须是同一个卖家！");
                    //$('input:radio[name="paymentType"]:checked').val("0");
                    document.getElementsByName("paymentType")[0].checked = true;
                    clickPaymentType();
                    isPass = false;
                    return false;
                }
            }
            // 城市必选
            var city = $(this).find("select[name='city']").val();
            if (city == null || city == "") {
                cbms.alert("请选择城市！");
                isPass = false;
                return false;
            }
            // 数量、重量、价格不能为0
            var quantity = parseInt($(this).find("input[name='quantity']").val());
            var weight = parseFloat($(this).find("input[name='weight']").val());
            var dealPrice = parseFloat($(this).find("input[name='dealPrice']").val());
            var costPrice = parseFloat($(this).find("input[name='costPrice']").val());
            if (quantity <= 0 || weight <= 0 || dealPrice <= 0 || costPrice <= 0) {
                cbms.alert("数量、重量、价格不能为0！");
                isPass = false;
                return false;
            }
            
            // 出现了卖家公司名称不同，ID一致的问题
            for (var i = 0; i < arrayResource.length; i++) {
                if ((sellerId == arrayResource[i].sellerId && sellerName != arrayResource[i].sellerName)
                    || (sellerId != arrayResource[i].sellerId && sellerName == arrayResource[i].sellerName)) {
                    cbms.alert("卖家公司名称输入错误！");
                    isPass = false;
                    return false;
                }
            }
           //isPass=    confineorder(sellerId,$.trim($("#totalWeight").text()));
            var isFlag = isRepeat($(this));
            if (isFlag) {
                isPass = false;
                return false;
            }
            arrayResource.push(createResource($(this)));
        });

        if (isPass == false) {
            return;
        }

        var orderData = {
            id: orderId,
            accountId: buyAccountId,
            departmentId: $.trim($("#department").find("option:selected").val()),
            departmentName: $.trim($("#department").find("option:selected").text()),
            consignType: sellerConsignType,
            accountName: $.trim($(buyer).val()),
            ownerId: $(user).attr("userid"),
            ownerName: $.trim($(user).val()),
            contactId: $(contact).attr("contactid"),
            contactName: $.trim($(contact).val()),
            contactTel: $.trim($("#contactTel").val()),
            deliveryType: $("#deliveryType").val(),
            deliveryEndDate: $.trim($("#endTime").val()),
            feeTaker: $("#feeTaker").val(),
            shipFee: $.trim($("#shipFee").val()),
            outboundTaker: $("#outboundTaker").val(),
            outboundFee: $.trim($("#outboundFee").val()),
            totalQuantity: $.trim($("#totalQuantity").text()),
            totalWeight: $.trim($("#totalWeight").text()),
            totalAmount: $.trim($("#totalAmount").val()),
            contractAddress: $.trim($("#contractAddress").val()),
            resourceItems: arrayResource,
            paymentType: secType,
            delayNum:delayDays,
            paymentSort:$("#paymentSort").val(),
            requirementCode: $("#requirementCode").val(), //需求单号
            transArea: $("#transArea").val(), //交货地
            origin: $("input[name='origin']").val()  //订单来源： 空， SMART_MATCH(找货)，
        };

        $(this).prop("disabled", true);
        cbms.loading();
        var quotationUrl = $("#smartmatchDomain").val()+ '/smartmatch/quotation/info/'+ $("#quotationOrderId").val() +'.html';
        $.ajax({
            type: 'post',
            url: Context.PATH + "/order/saveorder.html",
            data: {
                purchaseOrderId: utils.isEmpty(CreateOrderPage.purchaseOrderId) ? "" : CreateOrderPage.purchaseOrderId,
                quotationIds: $("#quotationIds").val(),
                orderJson: JSON.stringify(orderData)
            },
            error: function (s) {
                $("#submit").prop("disabled", false);
                cbms.closeLoading();
            }
            , success: function (result) {
                cbms.closeLoading();
                if (result) {
                    if (result.success) {
                        if (orderId > 0) {
                            cbms.alert("修改交易单成功，请等待审核！");
                        }
                        else {

                            if(result.data == undefined || result.data == ""){
                                cbms.alert("提交订单成功，请等待办事处总经理审核订单！");
                                setTimeout('location.href="' + Context.PATH + '/order/query/approval.html"', 3000);
                            }else{
                                cbms.alert("提交订单成功！");
                                setTimeout('location.href="' + quotationUrl + '"', 3000);
                            }

                        }
                    }
                    else {
                        $("#submit").prop("disabled", false);
                        cbms.alert(result.data);
                    }
                } else {
                    $("#submit").prop("disabled", false);
                    cbms.alert("提交审核失败");
                }
            }
        });
    }


    // 选择多条资源添加到订单资源表
    $(document).on("click", "#addBuy", function () {
        var isClose = true;
        var checkbox, columns, resourceId;
        $("input[name='resourcecheck']:checked").each(function () {
            checkbox = $(this);
            columns = $(checkbox).parents('tr:first').find("td");
            resourceId = $(checkbox).val();
            if (isExist(resourceId) == false) {
                var resource = {};
                resource.resourceId = resourceId;
                resource.sellerId = $(checkbox).attr("sellerid");
                resource.sellerName = $(columns).eq(1).text();
                resource.nsortName = $(columns).eq(2).text();
                resource.material = $(columns).eq(3).text();
                resource.spec = $(columns).eq(4).text();
                resource.factory = $(columns).eq(5).text();
                resource.warehouse = $(columns).eq(6).text();
                resource.weight = $(columns).eq(7).text();
                resource.weightConcept = $(columns).eq(8).text();
                resource.costPrice = $(columns).eq(9).text();
                resource.dealPrice = $(columns).eq(9).text();
                resource.city = $(checkbox).attr("city");
                resource.quantity = $(checkbox).attr("quantity");
                resource.consigntype = $(checkbox).attr("consigntype");
                addResourceToTable(resource);
            }
            else {
                cbms.alert("相同资源不能重复添加！");
                isClose = false;
                return false;
            }
        });
        if (isClose) {
            cbms.closeDialog();
        }
    });

    // 点击页面其他地方隐藏下拉div
    $(document).mousedown(function () {
        $(".product-complete").hide();
    });

    // 阻止事件继续冒泡
    $(document).on("mousedown", ".product-complete", function (e) {
        var event = e || window.event;
        event.stopPropagation();
    });

    // 绑定卖家的下拉单击事件，根据卖家账号所在地选中省市
    $(document).on("click", "#searchAccountList li a", function () {
        var input = "input[inputid='" + $("#searchAccountList").attr("inputid") + "']";
        var provinceId = $(this).attr("provinceid");
        var cityId = $(this).attr("cityid");
        if (provinceId != "null" && cityId != "null") {
            var currentRow = $(input).closest("tr");
            if ($("[name='province']", currentRow).length > 0 && $("[name='city']", currentRow).length > 0) {
                bindRegionData($("[name='province']", currentRow), $("[name='city']", currentRow), null,parseInt(provinceId), parseInt(cityId));
            }
        }
    });
});
function confineorder(sellerId,totalWeight){
	var isPass = true;
$.ajax({
   type: 'post',
   url: Context.PATH + "/sys/confineorder.html",
   async : false,
   data: {
	   userId :$("#user").attr("userid"),
       accountId: sellerId,
   totalWeight: totalWeight
   },
   error: function (s) {
   }
   , success: function (result) {
       if(result.success){
       }else{
           cbms.alert(result.data);
           isPass = false;
           return isPass;
       }

   }
});
  return isPass;
}


// 获取买家公司
function getBuyer(userId, name) {
    var buyerDropdown = "buyerDropdown";
    $("#" + buyerDropdown).remove();
    resetData("buyer");
    name = $.trim(name);
    if (name == "") return;

    $.ajax({
        type: 'post',
        url: Context.PATH + "/order/getbuyer.html",
        data: {
            userId: userId,
            name: name
        },
        error: function (s) {
        }
        , success: function (result) {
            if (result) {
                if (result.success) {
                    if ($(result.data) != null && $(result.data).length > 0) {
                        var buyerDropdownHtml = "<div id='" + buyerDropdown + "' class='product-complete'>";
                        buyerDropdownHtml += "<ul>";
                        for (var i = 0; i < $(result.data).length; i++) {
                            // 如果刚好匹配就直接选中，不出现下拉列表
                            if (name == result.data[i].name) {
                                $("#buyer").val(result.data[i].name);
                                $("#buyer").attr("accountid", result.data[i].id);
                                getDepartment(result.data[i].id,$("#department"),true);

                                var balance = parseFloat(result.data[i].balance);
                                if (isNaN(balance) == false) {
                                    showBalance(balance);
                                }
                                if (isShowAcceptDraft == "true") {
                                    getAcceptDraft(result.data[i].id);
                                }
                                return;
                            }

                            buyerDropdownHtml += "<li><a href='javascript:;' accountid='" + result.data[i].id + "' accountname='" + result.data[i].name
                            + "' balance='" + result.data[i].balanceSecondSettlement + "' >"
                            + processStr(name, result.data[i].name) + "</a></li>";
                        }
                        buyerDropdownHtml += "</ul>";
                        buyerDropdownHtml += "</div>";
                        $("body").append(buyerDropdownHtml);
                        setLayerPosition($("#buyer"), $("#" + buyerDropdown));
                        controlLayerShow($("#" + buyerDropdown));
				 } else {//如果买家不存在 表示新增的买家，给出默认部门
                        //重新输入买家时，清空部门，联系人，联系电话
                        $("#department").empty();
                        $("#contact").empty();
                        $("#contactTel").empty();
                        $("#department").append("<option value=''>钢材部</option>");//赋值
                    }
                }
            } else {
                cbms.alert("获取数据失败");
            }
        }

    });
}

// 获取部门
function getDepartment(accountId,departmentElement,isBuyer) {
    if (isBuyer) {
        resetData("department");
    }
    else{
        var contactElement = $(departmentElement).closest("tr").find("select[name='contact']");
        $(contactElement).empty();
    }
    $.ajax({
        type: 'post',
        url: Context.PATH + "/order/getdepartment.html",
        data: {
            accountId: accountId
        },
        error: function (s) {
        }
        , success: function (result) {
            if (result && result.success) {
                $(departmentElement).empty();
                if (result.data != null && $(result.data).length > 0) {
                    for (var i = 0; i < $(result.data).length; i++) {
                        var option = "<option value='" + result.data[i].id + "'>" + result.data[i].name + "</option>";
                        $(departmentElement).append(option);
                    }
                    if (isBuyer) {
                        getContact(result.data[0].id, "");
                    }
                    else {
                        getSellerContact(result.data[0].id, departmentElement);
                    }
                }
            } else {
                cbms.alert("获取部门失败");
            }
        }

    });
}

// 获取买家联系人
function getContact(departmentId, name) {
    var contactDropdown = "contactDropdown";
    $("#" + contactDropdown).remove();
    resetData("contact");

    $.ajax({
        type: 'post',
        url: Context.PATH + "/order/getbuyercontact.html",
        data: {
            departmentId: departmentId,
            userId:$("#user").attr("userid")
        },
        error: function (s) {
        }
        , success: function (result) {
            if (result && result.success) {
                if (result.data != null && $(result.data).length > 0) {
                    var contactDropdownHtml = "<div id='" + contactDropdown + "' class='product-complete'>";
                    contactDropdownHtml += "<ul>";
                    for (var i = 0; i < $(result.data).length; i++) {
                        // 如果刚好匹配就直接选中，不出现下拉列表
                        if (name == result.data[i].name) {
                            $("#contact").val(result.data[i].name);
                            $("#contact").attr("contactid", result.data[i].id);
                            $("#contactTel").val(result.data[i].tel);
                            $("#contactMsg").hide();
                            return;
                        }

                        contactDropdownHtml += "<li><a href='javascript:;' tel='" + result.data[i].tel + "' contactid='" + result.data[i].id + "'>" + result.data[i].name + "</a></li>";
                    }
                    contactDropdownHtml += "</ul>";
                    contactDropdownHtml += "</div>";
                    $("body").append(contactDropdownHtml);
                    setLayerPosition(contact, $("#" + contactDropdown));
                    controlLayerShow($("#" + contactDropdown));
                }
            } else {
                cbms.alert("获取买家联系人失败");
            }
        }

    });
}

// 获取卖家联系人
function getSellerContact(departmentId,departmentElement) {
    var contactElement = $(departmentElement).closest("tr").find("select[name='contact']");
    $(contactElement).empty();
    $.ajax({
        type: 'post',
        url: Context.PATH + "/order/getsellercontact.html",
        data: {
            departmentId: departmentId
        },
        error: function (s) {
        }
        , success: function (result) {
            if (result && result.success) {
                if (result.data != null && $(result.data).length > 0) {
                    for (var i = 0; i < $(result.data).length; i++) {
                        var option = "<option value='" + result.data[i].id + "'>" + result.data[i].name + "</option>";
                        $(contactElement).append(option);
                    }
                }
            } else {
                cbms.alert("获取卖家联系人失败");
            }
        }

    });
}

// 获取买家银票
function getAcceptDraft(accountId) {
    $.ajax({
        type: 'post',
        url: Context.PATH + "/order/getacceptdraft.html",
        data: {
            accountId: accountId
        },
        error: function (s) {
        }
        , success: function (result) {
            if (result) {
                var adOptions = "<option value='0' selected='selected'>无</option>";
                adOptions += "<option value='-1'>银票支付</option>";
                if (result.data != null && $(result.data).length > 0) {
                    if ($(result.data).length > 0) {
                        acceptDraftData = $(result.data);
                        for (var i = 0; i < acceptDraftData.length; i++) {
                            adOptions += "<option value='" + acceptDraftData[i].id + "'>" + acceptDraftData[i].code + "</option>";
                        }
                    }
                }
                $("select[name='isPayedByAcceptDraft']").html(adOptions);
            } else {
                cbms.alert("获取数据失败");
            }
        }

    });
}

function resetData(id) {
    if(CreateOrderPage.purchaseOrderId == "") {
        if (id == "user") {
            $("#user").attr("userid", 0);
            $("#buyer").val("");
        }
        if (id == "user" || id == "buyer") {
            $("#buyer").attr("accountid", 0);
            showBalance(0);
        }
        var contact = $("#contact");
        if (id != "contact") {
            $(contact).val("");
        }
        var contactTel = $("#contactTel");
        $(contact).attr("contactid", 0);
        $(contactTel).val("");
    }
}

// 显示二次结算金额
function showBalance(balance) {
    if (balance < 0) {
        $("#notSettleAmount").text(formatMoney(parseFloat(-balance), 2));
        $("#balanceSettle").show();
    }
    else {
        $("#notSettleAmount").text(0);
        $("#balanceSettle").hide();
    }
}

// 更新table的序列值
function updateIndex() {
    var rows = $("#table_resource tbody tr");
    var rowSize = $(rows).size();
    for (var i = 0; i < rowSize; i++) {
        $(rows).eq(i).find("td").eq(0).text(i + 1);
    }
}

// 计算单条资源总金额
function calculationAmount(row) {
    var dealPrice = 0, costPrice = 0, weight = 0, amount = 0, costAmount = 0;

    var temp_dealPrice = row.find("input[name='dealPrice']").val();
    var temp_costPrice = row.find("input[name='costPrice']").val();
    var temp_weight = row.find("input[name='weight']").val();

    dealPrice = parseFloat(cbms.subStr(temp_dealPrice,2));
    costPrice = parseFloat(cbms.subStr(temp_costPrice, 2));
    weight = parseFloat(cbms.convertFloat(temp_weight));
    amount = parseFloat(cbms.convertFloat((dealPrice.mul(weight)), 2));
    costAmount = parseFloat(cbms.convertFloat((costPrice.mul(weight)), 2));
    if (!isNaN(amount)) {
        $(row).find("input[name='amount']").val(amount);
        $(row).find("span[name='amounttext']").text(formatMoney(amount, 2));
    }
    if (!isNaN(costAmount)) {
        $(row).find("input[name='costamount']").val(costAmount);
        $(row).find("span[name='costamounttext']").text(formatMoney(costAmount, 2));
    }
}

// 订单统计
function orderTotal() {
    var totalQuantity = 0, totalWeight = 0, totalResourceAmount = 0, shipFee = 0, totalAmount = 0;

    var trSize = $("#table_resource tbody tr").size();
    for (var i = 0; i < trSize; i++) {
        var quantity = 0, weight = 0, dealPrice = 0, amount = 0;

        var current = $("#table_resource tbody tr").eq(i);
        var temp_quantity = $.trim(current.find("input[name='quantity']").val());
        var temp_weight = $.trim(current.find("input[name='weight']").val());
        var temp_price = $.trim(current.find("input[name='dealPrice']").val());

        if (temp_quantity != "") {
            quantity = parseInt(temp_quantity);
        }
        if (temp_weight != "") {
            weight = parseFloat(cbms.convertFloat(temp_weight));
        }
        if (temp_price != "") {
            dealPrice = parseFloat(cbms.convertFloat(temp_price, 2));
        }
        //修改生产问题 ，订单总金额计算错误
        amount = parseFloat(cbms.convertFloat((dealPrice.mul(weight)), 2));
        totalQuantity += quantity;
        totalWeight = totalWeight.add(weight);
        totalResourceAmount = totalResourceAmount.add(amount);
    }
    var temp_shipFee = $("#shipFee").val();
    shipFee = parseFloat(cbms.convertFloat(temp_shipFee, 2));

    if (isNaN(totalQuantity))
        totalQuantity = 0;
    if (isNaN(totalWeight))
        totalWeight = 0;
    if (isNaN(totalResourceAmount))
        totalResourceAmount = 0;
    if (isNaN(shipFee))
        shipFee = 0;

    $('#totalQuantity').text(totalQuantity);
    $("#totalWeight").text(cbms.convertFloat(totalWeight));
    $("#totalResourceAmount").text(formatMoney(totalResourceAmount, 2));

    totalAmount = parseFloat(cbms.convertFloat((totalResourceAmount.add(shipFee)), 2));
    $("#totalAmount").val(totalAmount);

}

// 创建资源对象
function createResource(row) {
    var resource = {};
    resource.sellerId = $(row).find("input[name='sellerName']").attr("accountid");
    resource.sellerName = $.trim($(row).find("input[name='sellerName']").val());
    resource.departmentId = $(row).find("select[name='department']").val();
    resource.departmentName = $(row).find("select[name='department']").find("option:selected").text();
    resource.contactId = $(row).find("select[name='contact']").val();
    resource.contactName = $(row).find("select[name='contact']").find("option:selected").text();
    resource.nsortName = $.trim($(row).find("input[name='nsortName']").val());
    resource.material = $.trim($(row).find("input[name='material']").val());
    resource.spec = $.trim($(row).find("input[name='spec']").val());
    resource.factory = $.trim($(row).find("input[name='factory']").val());
    resource.city = $(row).find("select[name='city']").find("option:selected").text();
    resource.warehouse = $.trim($(row).find("input[name='warehouse']").val());
    resource.quantity = parseInt($.trim($(row).find("input[name='quantity']").val()));
    resource.weight = cbms.convertFloat($(row).find("input[name='weight']").val());
    resource.weightConcept = $(row).find("select[name='weightConcept']").val();
    resource.costPrice = cbms.subStr($(row).find("input[name='costPrice']").val(), 2);
    resource.dealPrice = cbms.subStr($(row).find("input[name='dealPrice']").val(), 2);
    resource.amount = cbms.convertFloat(($(row).find("input[name='amount']").val()), 2);
    resource.strappingNum = $.trim($(row).find("input[name='strappingNum']").val()); //捆包号
    var isPayedByAcceptDraft = $(row).find("select[name='isPayedByAcceptDraft']").val(); //是否使用银票
    var acceptDraftId = 0;  //银票票号id
    var acceptDraftCode = "";  //银票票号
    if(isPayedByAcceptDraft == undefined){
        isPayedByAcceptDraft = 0;
    }
    // 若没有选择银票支付或者票号则为0，若选择了则为1
    if(isPayedByAcceptDraft != 0){
        if(isPayedByAcceptDraft > 0){
            acceptDraftId = $(row).find("select[name='isPayedByAcceptDraft']").val();  //银票票号id
            acceptDraftCode = $(row).find("select[name='isPayedByAcceptDraft']").find("option:selected").text();  //银票票号
        }
        isPayedByAcceptDraft = 1;  //是否使用银票0否1是
    }
    resource.isPayedByAcceptDraft = isPayedByAcceptDraft;
    resource.acceptDraftId = acceptDraftId;
    resource.acceptDraftCode = acceptDraftCode;
    return resource;
}

// 添加空行
function addEmptyRow() {
    var lastRow = $("#table_resource tbody tr").last();        // 获取最后一行
    var cloneRow = $(lastRow).clone();                         // 复制最后一行
    // 重置输入框等
    $(cloneRow).find("input").val("");
    var sellerName = $(cloneRow).find("input[name='sellerName']");
    $(sellerName).attr("resourceId", "0");
    $(sellerName).attr("accountid", "0");
    $(sellerName).attr("consigntype", "");
    $(sellerName).removeClass("temp-lin");
    $(cloneRow).find("select").get(0).selectedIndex = 0;
    $(cloneRow).find("[name='department']").empty();
    $(cloneRow).find("[name='contact']").empty();
    $(cloneRow).find("[name='city']").empty();
    $(cloneRow).find("[name='amounttext']").text("");
    $(cloneRow).find("[name='costamounttext']").text("");
    //追加在最后一行后面
    $(lastRow).after($(cloneRow));
    // 绑定省市联动
    bindRegionData($(cloneRow).find("[name='province']"), $(cloneRow).find("[name='city']"));
    updateIndex();
}

// 添加数据到订单资源table
function addResourceToTable(resource) {
    addEmptyRow();
    var lastRow = $("#table_resource tbody tr").last();        // 获取最后一行
    var sellerName = $(lastRow).find("input[name='sellerName']");
    $(sellerName).attr("resourceid", resource.resourceId);
    $(sellerName).attr("accountid", resource.sellerId);
    $(sellerName).attr("consigntype", resource.consigntype);
    if (resource.consigntype == "temp") {
        $(sellerName).addClass("temp-lin");
    }
    $(sellerName).val(resource.sellerName);
    getDepartment(resource.sellerId,$(lastRow).find("[name='department']"),false);
    $(lastRow).find("input[name='nsortName']").val(resource.nsortName);
    $(lastRow).find("input[name='material']").val(resource.material);
    $(lastRow).find("input[name='spec']").val(resource.spec);
    $(lastRow).find("input[name='factory']").val(resource.factory);
    var city = getCity(resource.city);
    if (city != null) {
        // 绑定省市联动数据
        bindRegionData($(lastRow).find("[name='province']"), $(lastRow).find("[name='city']"), null, city.provinceId, city.id);
    }
    $(lastRow).find("input[name='warehouse']").val(resource.warehouse);
    $(lastRow).find("input[name='quantity']").val(resource.quantity);
    $(lastRow).find("input[name='weight']").val(resource.weight);
    $(lastRow).find("select[name='weightConcept'] option:contains('" + resource.weightConcept + "')").prop("selected", true);
    $(lastRow).find("input[name='costPrice']").val(resource.costPrice);
    $(lastRow).find("input[name='dealPrice']").val(resource.dealPrice);
    $(lastRow).find("select[name='acceptDraft'] option:contains('" + resource.acceptDraftId + "')").prop("selected", true);
    // 删除空行
    $("#table_resource tbody tr").each(function () {
        var currentRow = $(this);
        if (isEmptyRow(currentRow)) {
            $(currentRow).remove();
        }
    });
    updateIndex();
    calculationAmount($(lastRow));
    orderTotal();
}

// 判断要添加的资源是否已经添加 true：添加，false：未添加
function isExist(resourceId) {
    var result = false;
    $("#table_resource tbody input[name='sellerName']").each(function () {
        var addedId = $(this).attr("resourceid");
        if (addedId == resourceId) {
            result = true;
            return false;   //实现break功能
        }
    });
    return result;
}

/**
 * 不能提交相同的资源
 * @param currentRow    当前行
 * @returns {boolean} true：存在相同的资源，false：不存在相同的资源
 */
function isRepeat(currentRow) {
    // 暂时先取消限制
    return false;

    //var index = parseInt($(currentRow).find("td").eq(0).text());
    //var seller = $.trim($(currentRow).find("input[name='sellerName']").val());
    //var nsortName = $.trim($(currentRow).find("input[name='nsortName']").val());
    //var material = $.trim($(currentRow).find("input[name='material']").val());
    //var spec = $.trim($(currentRow).find("input[name='spec']").val());
    //var factory = $.trim($(currentRow).find("input[name='factory']").val());
    //var warehouse = $.trim($(currentRow).find("input[name='warehouse']").val());
    //var weightConcept = $(currentRow).find("select[name='weightConcept']").val();
    //var costPrice = parseFloat(cbms.convertFloat($(currentRow).find("input[name='costPrice']").val(), 2));
    //var dealPrice = parseFloat(cbms.convertFloat($(currentRow).find("input[name='dealPrice']").val(), 2));
    //var result = false;
    //var rIndex = 0;
    //var rSeller, rNsortName, rMaterial, rSpec, rFactory, rWarehouse, rWeightConcept;
    //var rCostPrice = 0;
    //var rDealPrice = 0;
    //$("#table_resource tbody tr").each(function () {
    //    rIndex = parseInt($(this).find("td").eq(0).text());
    //    if (index != rIndex) {
    //        rSeller = $.trim($(this).find("input[name='sellerName']").val());
    //        rNsortName = $.trim($(this).find("input[name='nsortName']").val());
    //        rMaterial = $.trim($(this).find("input[name='material']").val());
    //        rSpec = $.trim($(this).find("input[name='spec']").val());
    //        rFactory = $.trim($(this).find("input[name='factory']").val());
    //        rWarehouse = $.trim($(this).find("input[name='warehouse']").val());
    //        rWeightConcept = $(this).find("select[name='weightConcept']").val();
    //        rCostPrice = parseFloat(cbms.convertFloat($(this).find("input[name='costPrice']").val(), 2));
    //        rDealPrice = parseFloat(cbms.convertFloat($(this).find("input[name='dealPrice']").val(), 2));
    //        if (seller == rSeller && nsortName == rNsortName && material == rMaterial
    //            && spec == rSpec && factory == rFactory && warehouse == rWarehouse
    //            && weightConcept == rWeightConcept && costPrice == rCostPrice && dealPrice == rDealPrice) {
    //            cbms.alert("序号" + index + "、" + rIndex + "【卖家、品名、材质、规格、厂家、仓库、计重方式、销售价、采购价】相同，请修改。");
    //            result = true;
    //            return false;   //实现break功能
    //        }
    //    }
    //});
    //return result;
}

// 设置对应的选择值（select控件等）
function setSelectValue() {
    $("select[val]").each(function () {
        var value = $(this).attr("val");
        if (value != "") {
            $(this).val(value);
        }
    });
}

// 绑定省市数据
function bindCity() {
    var rows = $("#table_resource tbody tr");
    var rowSize = $(rows).size();
    for (var i = 0; i < rowSize; i++) {
        var row = rows[i];
        // 绑定省市数据
        var province = $(row).find("select[name='province']");
        var city = $(row).find("select[name='city']");
        var cityName = $(city).attr("city");
        if (cityName != undefined && cityName != "") {
            var cityInfo = getCity(cityName);
            if (cityInfo != null) {
                // 绑定省市联动数据
                bindRegionData(province, city, null, cityInfo.provinceId, cityInfo.id);
            }
        }
        else {
            bindRegionData(province, city);
        }
    }
}

/**
 *  判断是否为空行
 * @param row   需要判断的row
 * @returns {boolean} true:空行，false：非空
 */
function isEmptyRow(row) {
    var sellerName = $.trim($(row).find("input[name='sellerName']").val());
    var nsortName = $.trim($(row).find("input[name='nsortName']").val());
    var material = $.trim($(row).find("input[name='material']").val());
    var spec = $.trim($(row).find("input[name='spec']").val());
    var factory = $.trim($(row).find("input[name='factory']").val());
    if (sellerName == "" && nsortName == "" && material == "" && spec == "" && factory == "")
        return true;
    else
        return false;
}
