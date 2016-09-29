/**
 * Create by Rabbit
 */
var highestPercent;
var leastPercent;
var ischanged;
var _warehouseData;
var _factoryData;
var _factory;
$().ready(function () {
    $(".check").verifyForm();
    highestPercent = $("#highestPercent").val();
    leastPercent = $("#leastPercent").val();

    loadFactoryByCategory();
    loadAllFactory();
    renderTable(_quotationOrderId);
    bindEvent();
    getWarehouseData();
});

function getWarehouseData(){
    $.ajax({
        type: "POST",
        url: Context.PATH + "/smartmatch/warehouse/getAllWarehouseForPuzzyMatch.html",
        dataType: "json",
        success: function (response) {
            if (response.success) {
                _warehouseData = response.data;
            } else {
                cbms.alert(response.data);
            }
        },
        error: function (xhr, textStatus, errorThrown) {
        }
    });
}
function loadAllFactory(){
    $.ajax({
        type : "POST",
        url : Context.PATH + "/smartmatch/factory/getAllFactory.html",
        data : {},
        dataType : "json",
        async : false,
        success : function(response) {
            if (response.success) {
                _factory = response.data;
            } else {
                cbms.alert(response.data);
            }
        },
        error : function(xhr, textStatus, errorThrown) {}
    });
}
/**
 * 根据品名uuid加载钢厂数据
 */
function loadFactoryByCategory() {
    $.ajax({
        type: 'post',
        url: Context.PATH + "/smartmatch/purchaseorder/loadFactoryByCategory.html",
        data: {
            purchaseOrderId:$("#purchaseOrderId").val()
        },
        dataType: "json",
        async: false,
        error: function (s) {
        },
        success: function (data) {
            if(data.success){
                _factoryData = data.data;
            }
        }
    });
}
function bindEvent(){
    //厂家Tab点击事件
    $(document).on("click",".factory",function(){
        $(".show-layer").hide();
        $(this).closest(".factoryDiv").find("#showLayerFactory").attr("style","display: block;");
    });

    //厂家内确定按钮点击事件
    $(document).on("click", ".factory_div button.confirm-button", function () {
        $(".factory_div").hide();
        if(!$(this).closest('div.factoryDiv').find("input[name=otherFactoryId]").val()){
            $(this).closest(".factoryDiv").find(".factory").val($(this).closest('div.factoryDiv').find(".factory_div a.hover").html());
            $(this).closest(".factoryDiv").find(".factory").attr("factoryId",$(this).closest('div.factoryDiv').find(".factory_div a.hover").attr("value"));
        }
        $(this).closest("tr").find(".id").attr("ischanged", true);
    });

    //厂家点击事件
    $(document).on("click", ".factory_div .textures-con span a", function () {
        if ($(this).hasClass("hover")) {
            $(this).removeClass("hover");
        } else {
            $(this).closest("div.factoryDiv").find("input[name=otherFactoryId]").attr("val", "");
            $(this).closest("div.factoryDiv").find("input[name=otherFactoryId]").val("");
            $(this).closest("div.factoryDiv").find(".factory_div a.hover").removeClass("hover");
            $(this).addClass("hover");
        }
    });

    //厂家内清除按钮点击事件
    $(document).on("click", ".factory_div button.clear-btn", function () {
        $(this).closest("div.factoryDiv").find(".factory_div a").removeClass("hover");
        //$(this).closest("div.factory").find("input[name=otherFactory]").prop("checked",false);
        $(this).closest("div.factory_div ").find("input[name=otherFactoryId]").attr("val", "");
        $(this).closest("div.factory_div ").find("input[name=otherFactoryId]").val("");
    });


    //厂家内其他输入框输入事件
    $(document).on("input", "input[name=otherFactoryId]", function () {
        var input = $(this);
        $(this).closest("div.factoryDiv").find(".factory_div a.hover").removeClass("hover");
        $(this).attr("val", "");
        var factoryDiv = $(this).closest("div.factoryDiv");
        //去掉界面上选中的那些仓库
        var temp = _factory.slice(0);
        var existsIds = $(factoryDiv).find(".factory_div a");
        if(existsIds.length>0){
            $(existsIds).each(function () {
                for (var i in temp) {
                    if (temp[i].id == $(this).attr("factoryid")) {
                        temp.splice(i, 1);
                    }
                }
            });
        }
        showPYMatchList(input, temp, 'id', 'name', function(){
            var factoryId = $(input).closest('div.factoryDiv').find("input[name=otherFactoryId]").attr("val")?$(input).closest('div.factoryDiv').find("input[name=otherFactoryId]").attr("val"):""
            var factoryName = $(input).closest('div.factoryDiv').find("input[name=otherFactoryId]").val()?$(input).closest('div.factoryDiv').find("input[name=otherFactoryId]").val():""
            $(input).closest(".factoryDiv").find(".factory").val(factoryName);
            $(input).closest(".factoryDiv").find(".factory").attr("factoryId",factoryId);
        });
        $("#dropdown").css("z-index", 9999);
        return false;
    });

    $(document).on('input',"input.warehouse", function () {
        var target = $(this);
        showPYMatchList(target, _warehouseData,"id","name");
        $("#dropdown").css("z-index",999999);
    });

    $(document).on("click", ".option li a", function () {
        $(".option li").removeClass("active");
        $(this).closest("li").addClass("active");
        renderTable();
    });

    $(document).on("input", ".weight", function () {
        var tr = $(this).closest('tr');
        $(tr).find('.totalAmount').html(cbms.convertFloat($(tr).find('.costPrice').val()*$(this).val(), 2));
    });

    $(document).on("input", ".costPrice", function () {
        var tr = $(this).closest('tr');
        $(tr).find('.totalAmount').html(cbms.convertFloat($(tr).find('.weight').val()*$(this).val(), 2));
    });

    /**
     * 销售价格修改之后，重新计算销售总价和利润
     */
    $(document).on("input", ".dealPrice", function () {
        if (!setlistensSave(".check")) {return;}
        statistics();
    });

    $(document).on("click", ".update", function () {
        $("tr .id").removeAttr("ischanged");   //把所有item先设置成未改
        cbms.getDialog("", $("div#" + $(this).attr("itemsId")).formhtml());
        $("#dialogContBox").css("height", $("#dialogContBox table").height() + 150);
    });

    $(document).on("input", ".d-border input", function () {
        if(!$(this).hasClass("weight")) {
            $(this).closest("tr").find(".id").attr("ischanged", true);  //如果改了采购重量之外的重量，把item设置成已修改
        }
    });

    $(document).on("click", "#cancel", function () {
        cbms.closeDialog();
    });

    $(document).on("click", "#confirm", function () {
        if (!setlistensSave(".check")) {
            return;
        }
        var tableId = $(this).closest("table").attr("id");
        $("#hiddenTableDiv div#" + tableId).empty().append($("#dialogContBox").formhtml());
        //如果用户修改了销售重量，需要把销售重量，单价等信息同步到主页面上
        if (checkInput(tableId)) {
            cbms.closeDialog();
            statistics();
            updateInquiryOrder($(this));
        }
    });

    $(document).on("click", "#submit", function () {
        if (!setlistensSave(".check")) {return;}
        var items = [];

        var purchaseOrderId = $("#purchaseOrderId").val();
        var remark = $("#remark").val();
        $("#hiddenTableDiv table").each(function(){
            $(this).find("input[type='checkbox']:checked").each(function(){
                var row = $(this).closest("tr");
                var specList = $(row).find(".spec").html().split("*");
                var spec1="", spec2="", spec3="";
                if(specList.length > 0){
                    spec1 = specList[0];
                }
                if(specList.length > 1){
                    spec2 = specList[1];
                }
                if(specList.length > 2){
                    spec3 = specList[2];
                }
                var dealPrice = $("#dynamicTable tr[itemsId=" + $(row).closest("div").attr("id") + "]").find(".dealPrice").val();
                var warehouseId = $(row).find(".warehouse").attr("val");
                var item = {
                    inquiryOrderItemsId: $(row).find(".id").val(),
                    sellerId: $(row).find(".sellerId").val(),
                    categoryUuid: $(row).find(".categoryUuid").val(),
                    materialUuid: $(row).find(".materialUuid").val(),
                    spec1: spec1,
                    spec2: spec2,
                    spec3: spec3,
                    weightConcept: $(row).find(".weightConcept").html(),
                    factoryId: $(row).find(".factory").attr("factoryid"),
                    warehouseId: (warehouseId == "" ? -1 : warehouseId),
                    abnormalWarehouse: (warehouseId == "" ? $(row).find(".warehouse").val() : ""),
                    quantity: $(row).find(".quantity").val(),
                    weight: $(row).find(".weight").val(),
                    costPrice: $(row).find(".costPrice").val(),
                    dealPrice: dealPrice,
                    totalAmount: dealPrice * $(row).find(".weight").val()
                };
                items.push(item);
            });
        });
        $.ajax({
            type: "POST",
            url: Context.PATH + "/smartmatch/inquiryorder/saveQuotationOrder.html",
            data: {
                id: (_quotationOrderId == "" ? 0 : _quotationOrderId),
                purchaseOrderId: purchaseOrderId,
                lastSaveTab: $(".option li.active a").attr("option"),
                remark: remark,
                quotationOrderItems: JSON.stringify(items)
            },
            success: function (result) {
                if(result.success){
                    location.href = Context.PATH + '/smartmatch/quotation/index/' + result.data + '.html';
                }else{
                    cbms.alert(result.data);
                }
            }
        });
    });
}

//保存询价单
function updateInquiryOrder(o) {
    var items = [];
    $(o).closest("table").find("tbody tr").each(function () {
        if($(this).find(".id").attr("ischanged") == "true") {
            var item = {
                id: $(this).find(".id").val(),
                sellerId: $(this).find(".sellerId").val(),
                categoryUuid: $(this).find(".categoryUuid").val(),
                materialUuid: $(this).find(".materialUuid").val(),
                normsName: $(this).find(".spec").html(),
                factoryId: $(this).find(".factory").attr("factoryid"),
                factoryName: $(this).find(".factory").val(),
                warehouseId: $(this).find(".warehouse").attr("val"),
                warehouseName: $(this).find(".warehouse").val(),
                weightConcept: $(this).find(".weightConcept").html(),
                weight: $(this).find(".stock").val(),
                quantity: $(this).find(".quantity").val(),
                resultWeight: $(this).find(".stock").val(),
                resultQuantity: $(this).find(".quantity").val(),
                price: $(this).find(".costPrice").val(),
                option: 'save'
            };
            items.push(item);
        }
    });
    if(items.length > 0) {
        $.ajax({
            type: "POST",
            url: Context.PATH + "/smartmatch/inquiryorder/updateInquiryOrderItems.html",
            data: {
                inquiryOrderItems: JSON.stringify(items)
            },
            success: function (result) {
                if (result.success) {
                    utils.showMsg("修改成功", null, null, 2000);
                } else {
                    utils.showMsg(result.data, null, "error", 2000);
                }
            }
        });
    }
}

function renderTable(quotationOrderId) {
    cbms.loading();
    $.ajax({
        type: "POST",
        url: Context.PATH + "/smartmatch/inquiryorder/getData.html",
        data: {
            purchaseOrderId: $("#purchaseOrderId").val(),
            option: $(".option li.active a").attr("option"),
            blockInquiryOrderSellerIds: $("#blockInquiryOrderSellerIds").val(),
            quotationOrderId: quotationOrderId
        },
        success: function (result) {
            cbms.closeLoading();
            var html;
            if (result) {
                if (result.success) {
                    var datas = result.data;
                    var count = 0;
                    $("#dynamicTable tbody").empty();
                    for (var i in datas) {
                        html = '<tr itemsId="' + datas[i].purchaseOrderItemsId + '"><td>' + ++count + '</td>' +
                        '<td class="category">' + datas[i].categoryName + '<br/><div class="message"></div></td>' +
                        '<td class="material">' + datas[i].materialName + '</td><td>' + datas[i].spec + '</td><td class="factory">' + datas[i].factory + '</td>' +
                        '<td class="seller"></td><td class="warehouse"></td>' +
                        '<td class="requestWeight">' + cbms.convertFloat(datas[i].requestWeight, 6) + '</td>' +
                        '<td class="quantity"></td><td class="weight"></td><td class="costPrice"></td>' +
                        '<td><input class="dealPrice" style="width: 70px" type="text" value="' + cbms.convertFloat(Math.ceil(datas[i].costPrice / 10) * 10, 2) + '" must="1" verify="rmb"/></td>' +
                        '<td><a class="update btn btn-sm btn-default" itemsId="' + datas[i].purchaseOrderItemsId + '">修改</a></td>' +
                        '</tr>';
                        $("#dynamicTable tbody").append(html);
                        var hidden = '';
                        var itemCount = 0;
                        var items = datas[i].items;
                        for(var j in items){
                           hidden += '<tr>' +
                           '<td><input type="checkbox"' + ((items[j].weight > 0)?"checked":"") + '/><input class="id" type="hidden" value="' + items[j].id + '"/></td>' +
                           '<td>' + ++itemCount + '<input type="hidden" class="categoryUuid" value="' + items[j].categoryUuid + '"/>' +
                           '<input type="hidden" class="materialUuid" value="' + items[j].materialUuid + '"/></td>' +
                           '<td class="material">' + items[j].materialName + '</td>' +
                           '<td class="spec">' + items[j].spec + '</td>' + buildFactoryHtml(items[j]) +
                           '<td><span class="seller">' + items[j].seller + '</span><span class="sellerName none">' + items[j].sellerName + '</span><span class="contact none">' + items[j].sellerContactName + '</span>' +
                           '<span class="tel none">' + items[j].sellerTel + '</span><input type="hidden" class="sellerId" value="' + items[j].sellerId + '"/></td>' +
                           '<td><input type="text" class="d-text warehouse" value="' + items[j].warehouse + '" val="' + (items[j].warehouseId == null ? "" : items[j].warehouseId) + '"/></td>' +
                           '<td><span class="weightConcept">' + items[j].weightConcept + '</span></td>' +
                           '<td><input type="text" class="d-text stock" value="' + items[j].stock + '" must="1" verify="weight"/></td>' +
                           '<td><input type="text" class="d-text weight" value="' + items[j].weight + '" must="1" verify="weight"/></td>' +
                           '<td><input type="text" class="d-text quantity" value="' + items[j].quantity + '" must="1" verify="number"/></td>' +
                           '<td><input type="text" class="d-text costPrice" value="' + items[j].costPrice + '" must="1" verify="rmb"/></td>' +
                           '<td class="totalAmount">' + cbms.convertFloat(items[j].weight * items[j].costPrice, 2) + '</td>' +
                           '</tr>';
                        }
                        $("#hiddenTableDiv div#" + datas[i].purchaseOrderItemsId).remove();
                        $("#hiddenTableDiv").append($("#baseTableDiv").html());
                        $("#hiddenTableDiv #orderItemsId").attr("id", datas[i].purchaseOrderItemsId);
                        $("#hiddenTableDiv #baseTable").attr("id", datas[i].purchaseOrderItemsId);
                        $("div#" + datas[i].purchaseOrderItemsId + " table tbody").append(hidden);
                        $("tr[itemsId='" + datas[i].purchaseOrderItemsId + "'] a.update").click();
                        $("#dialogContBox #confirm").click();
                    }
                    statistics();
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

function buildFactoryHtml(item){
    //钢厂如果指定的话显示指定钢厂，如果没有指定就根据品名Uuid查询钢厂数据
    var factoryStr = "<td>";
    var factorys;
    var factoryIdArr = [];
    var factoryNameArr = [];
    if(item.purchaseFactoryIds && item.purchaseFactoryIds != "null"){
        var factoryIds = item.purchaseFactoryIds;
        factoryIdArr = factoryIds.split(",");
        var factoryNames = item.purchaseFactoryNames;
        factoryNameArr = factoryNames.split(",");
    }else{
        factorys = _factoryData[item.categoryUuid];
        $(factorys).each(function(i,e){
            factoryIdArr.push(e.uuid);
            factoryNameArr.push(e.name);
        });
    }
    var content = "" , rowSize = 5;
    $(factoryIdArr).each(function (i, e) {
        if (i % rowSize === 0) {
            content += "<div class='textures-con-bar-list bder-b-dashed'>";
        }
        if(item.factoryId==e){
            content += '<span><a href="javascript:;" id="" value="' + e + '" class="hover">' + factoryNameArr[i] + '</a></span>';
        }else{
            content += '<span><a href="javascript:;" id="" value="' + e + '" class="">' + factoryNameArr[i] + '</a></span>';
        }
        if ((i + 1) % rowSize === 0 || (i + 1) === factoryIdArr.length) {
            content += "</div>";
        }
    });
    factoryStr += "<div class='form-item factoryDiv'><input type='text' class='f-text factory' must='1' factoryId='"+(item.factoryId?item.factoryId:"")+"' value='"+(item.factory?item.factory:"")+"' autocomplete='off' placeholder='单选' readonly>" +
    "<span class='icon down-arr-icon'></span><div class='show-layer textures-bar standard-bar factory_div none' id='showLayerFactory'><div class='textures-con'>"
    factoryStr +=content;
    factoryStr += "</div><div class='btn-bar f-clrfix "+(item.purchaseFactoryIds&&item.purchaseFactoryIds!="null"?"pull-right":"")+"'>"
    if(!item.purchaseFactoryIds || item.purchaseFactoryIds == "null"){
        factoryStr += "<div><span style='position: static'>其他：</span><input name='otherFactoryId' style='position: relative; width: 150px'/></div>"
    }
    factoryStr +="<button class='clear-btn'>清除</button> <button class='confirm-button'>确认</button></div></div></div>";
    factoryStr+="</td>";
    return factoryStr;
}

function statistics(){
    var sellerIds = [], warehouseNames = [], costAmount = 0, dealAmount = 0, allWeight = 0;
    $("#hiddenTableDiv table").each(function() {
        var tableId = $(this).attr("id");
        var tempSellerIds = [];   //用于统计这个品规有多少卖家
        $(this).find("tr input[type='checkbox']:checked").each(function () {
            var tr = $(this).closest("tr");
            var sellerId = $(tr).find(".sellerId").val();
            if (sellerIds.indexOf(sellerId) == -1) {
                sellerIds.push(sellerId);
            }
            if (tempSellerIds.indexOf(sellerId) == -1){
                tempSellerIds.push(sellerId);
            }
            var warehouseName = $(tr).find(".warehouse").val();
            if (warehouseNames.indexOf(warehouseName) == -1) {
                warehouseNames.push(warehouseName);
            }
            costAmount += ($(tr).find(".weight").val() * $(tr).find(".costPrice").val());
        });
        if(tempSellerIds.length > 1) {
            var messageDiv = $("#dynamicTable tr[itemsId=" + tableId + "]").find("div.message");
            $(messageDiv).find(".multiBuyerTag").remove();
            $(messageDiv).append('<H6 class="bg-yellow-style multiBuyerTag"><span class="red bolder">多个卖家供货</span></H6>');
        }
    });
    $("#seller_count").html(sellerIds.length);
    $("#warehouse_count").html(warehouseNames.length);

    $("#dynamicTable tbody tr").each(function(){
        var weight = $(this).find(".weight").html();
        var dealPrice = $(this).find(".dealPrice").val();
        allWeight += weight*1.0;
        dealAmount += weight * dealPrice;
    });
    $("#all_weight").html(cbms.convertFloat(allWeight, 6));
    $("#cost_amount").html(cbms.convertFloat(costAmount, 2));
    $("#deal_amount").html(cbms.convertFloat(dealAmount, 2));
    var profit = dealAmount - costAmount;
    $("#profit").html(cbms.convertFloat(profit, 2));
    $("#profit").removeClass();
    if(profit > 0){
        $("#profit").addClass("green");
    }else if(profit < 0){
        $("#profit").addClass("red");
    }
};

function checkInput(tableId){
    var totalWeight = 0, avgCostPrice, totalAmount = 0, totalQuantity = 0, result = true, totalStock = 0, sellerName = "", sellers = [], warehouses = [], factoryNames = [], materials = [];
    $("#dialogContBox").find("input[type='checkbox']:checked").closest('tr').each(function(){
        var weight = $(this).find('.weight').val()*1.0;
        var stock = $(this).find('.stock').val()*1.0;
        if(weight > stock){
            result = false;
        }
        totalWeight += weight;
        totalQuantity += $(this).find('.quantity').val()*1.0;
        totalAmount += ($(this).find('.costPrice').val()*$(this).find('.weight').val());
        var seller = {
            fullName:$(this).find('.seller').html(),
            name:$(this).find('.sellerName').html(),
            tel:$(this).find('.tel').html(),
            contact:$(this).find('.contact').html()
        };
        sellers.push(seller);
        var warehouse = $(this).find('.warehouse').val();
        if(warehouses.indexOf(warehouse) == -1){
            warehouses.push(warehouse);
        }
        var factoryName = $(this).find(".factory").val();
        if (factoryNames.indexOf(factoryName) == -1) {
            factoryNames.push(factoryName);
        }
        var material = $(this).find(".material").html();
        if (materials.indexOf(material) == -1) {
            materials.push(material);
        }
    });
    sellerUnique(sellers);  //去重
    if(sellers.length > 1){
        $(sellers).each(function(i, e){
            sellerName += (e.name + "<br/>");
        })
    }else if(sellers.length == 1){
        sellerName = sellers[0].fullName;
    }
    $("#dialogContBox").find("input[type='checkbox']").closest('tr').each(function() {
        var stock = $(this).find('.stock').val()*1.0;
        totalStock += stock;
    });
    if(result) {
        avgCostPrice = totalAmount / totalWeight;
        var tr = $("#dynamicTable tr[itemsId=" + tableId + "]");
        var requestWeight = $(tr).find(".requestWeight").html();
        $(tr).find(".weight").html(cbms.convertFloat(totalWeight, 6));
        $(tr).find(".costPrice").html(cbms.convertFloat(avgCostPrice, 2));
        avgCostPrice = (Math.ceil(avgCostPrice/10)) * 10;   //Math.ceil向上取整
        $(tr).find(".dealPrice").val(cbms.convertFloat(avgCostPrice, 2));
        $(tr).find(".seller").html(sellerName);
        $(tr).find(".warehouse").html(warehouses.join("<br/>"));
        $(tr).find(".factory").html(factoryNames.join("<br/>"));
        $(tr).find(".material").html(materials.join("<br/>"));
        $(tr).find(".quantity").html(totalQuantity);

        var messageDiv = $(tr).find(".category").closest("td").find("div.message");     //重新计算该条资源是否大于采购重量15%
        $(messageDiv).empty();
        if(totalStock < requestWeight){
            $(messageDiv).append('<H6 class="bg-yellow-style"><span class="red bolder">资源总量不足</span></H6>');
        }
        if(totalWeight > (cbms.convertFloat(requestWeight * highestPercent, 2))){
            $(messageDiv).append('<H6 class="bg-yellow-style"><span class="red bolder">采购重量大于求购重量的' + cbms.convertFloat(highestPercent*100, 2) + '%</span></H6>');
        }else if(totalWeight < (requestWeight * leastPercent)){
            $(messageDiv).append('<H6 class="bg-yellow-style"><span class="red bolder">采购重量小于求购重量的' + cbms.convertFloat(leastPercent*100, 2) + '%</span></H6>');
        }
    }else{
        utils.showMsg("采购重量不能大于买家求购重量", null, "error", 2000);
    }
    return result;
}

//数组去重
function sellerUnique(array){
    var res = [];
    var json = {};
    for(var i = 0; i < array.length; i++){
        if(!json[array[i].name]){
            res.push(array[i]);
            json[array[i].name] = 1;
        }
    }
    return res;
}