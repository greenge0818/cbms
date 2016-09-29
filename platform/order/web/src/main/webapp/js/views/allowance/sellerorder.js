var _sellerDt;
var _isDestroy = true;
var _orderIds;           // 发票ID集合

var _sellerId; //卖家ID
var _buyerAccountList;

$().ready(function () {
    if($("#allowanceId").val()){
        searchTable();
    }else{
    	if(isNotNull){
    		_sellerDt = $('#list-table').dataTable({
                bAutoWidth: false,
                searching:false,
                bPaginate: false,
                paging: false,
                ordering : false
            });
    	}else{
    		cbms.alert("必须有一个查询条件不为空！");
    	}
        
    }

    $("#createForm").verifyForm();

    //搜索
    $(document).on("click", "#btnSearch", function(){
    	/*
        if (!setlistensSave())return;
        if($('#seller').attr("accountid") == 0) {
            cbms.alert("您输入的公司名字可能错误或不存在，请重输！");
            return;
        }
        if(!$("#department").val()){
            cbms.alert("部门不能为空！");
            return;
        }
        */
        _orderIds = null;
        if(_isDestroy) {
            _isDestroy = false;
            _sellerDt.fnDestroy();
            searchTable();
        }else {
        	if(!isNotNull()){
        		cbms.alert("必须有一个查询条件不为空！");
        		return;
        	}
        	if(_sellerDt && _sellerDt.ajax){
            	_sellerDt.ajax.reload();
            }else{
            	searchTable();
            }
        }
    });

    // 全选/全不选
    $("#allCheck").click(function () {
        var checked = $(this).is(':checked');
        // 取消全选
        if (!checked) {
            $("input[name='check']").removeAttr("checked");
            $(this).removeAttr("checked");
        }
        else {
            $("input[name='check']").prop('checked', true);
            $(this).prop('checked', true);
        }
        orderTotal();
    });

    // 单选
    $("body").on("click", "input[name='check']", function () {
        var checked = $(this).is(':checked');
        if (!checked) {
            $(this).removeAttr("checked");
            $("#allCheck").removeAttr("checked");   // 取消全选
        }
        else {
            $(this).prop('checked', true);
        }

        // 如果全部选中，那么全选checkbox选中
        var checkCount = $("input[name='check']").length;
        var checkedCount = $("input[name='check']:checked").length;
        if (checkCount == checkedCount) {
            $("#allCheck").prop('checked', true);
        }
        orderTotal();
    });

    // 下一步
    $("#allowanceNext").click(function () {
        if (_orderIds == null || _orderIds.length == 0) {
            cbms.alert("请选择订单！");
            return;
        }

        /*
        if(!$("#department").val()){
            cbms.alert("请选择部门！");
            return;
        }
         */
        allowanceNext();
    });

    //从重新关联订单进来 设置卖家输入框禁用
    if($("#allowanceId").val()){
        $("#seller").prop('readonly', true);
    }

    // 绑定输入框值改变事件
    $(document).on("input", "input[search='buyer']", function (e) {
        $(this).attr("accountid", 0);
        $(this).attr("consigntype", "");
        $(this).removeClass("temp-lin");
        searchBuyerBySellerId($(this));
    });

    // 绑定下拉列表键盘事件(需要整合)
    $(document).on("keydown", "#buyer", function (e) {
        var event = e || window.event;
        if (event.keyCode == 38 || event.keyCode == 40 || event.keyCode == 13) {
            SelectOptions(event.keyCode, $("#searchBuyerList"));
        }
        else {
            currentLine = -1;
        }
    });

    // 绑定下拉选择单击事件
    $(document).on("click", "#searchBuyerList li a", function () {
        var input = "input[inputid='" + $("#searchBuyerList").attr("inputid") + "']";
        var accountId = $(this).attr("accountid");
        var consignType = $(this).attr("consigntype");
        var accountName = $(this).attr("accountname");
        if (consignType == "temp") {
            $(input).addClass("temp-lin");
        }
        $(input).attr("accountid", accountId);
        $(input).attr("consigntype", consignType);
        $(input).val(accountName);
        $("#searchBuyerList").hide();


        if(!$("#department").val()){
            cbms.alert("部门不能为空！");
            return;
        }

        if(_isDestroy) {
            _isDestroy = false;
            _sellerDt.fnDestroy();
            searchTable();
        }else {
        	if(!isNotNull()){
        		cbms.alert("必须有一个查询条件不为空！");
        		return;
        	}
            if(_sellerDt && _sellerDt.ajax){
            	_sellerDt.ajax.reload();
            }else{
            	searchTable();
            }
        }
    });

    // 点击页面其他地方隐藏下拉div
    $(document).mouseup(function (e) {
        var event = e || window.event;
        var targetId = $(event.target).attr("id");
        var searchAccountId = "searchBuyerList";
        if (targetId != searchAccountId) {
            $("#" + searchAccountId).hide();
        }
    });

    //选中卖家以后，查询卖家对应的部门及订单相关的买家
    $(document).on("click", "#searchAccountList li a", function () {
        var sellerId = $('#seller').attr("accountid");
        if(sellerId != null && sellerId != 0) {
            getDepartment(sellerId);//获取客户的部门
            searchBuyerBySellerId($("#buyer"));
        }
    });

    //卖家部门切换查询数据
    $(document).on("change","#department",function(){
        if(_isDestroy) {
            _isDestroy = false;
            _sellerDt.fnDestroy();
            searchTable();
        }else {
        	if(!isNotNull()){
        		cbms.alert("必须有一个查询条件不为空！");
        		return;
        	}
        	if(_sellerDt && _sellerDt.ajax){
            	_sellerDt.ajax.reload();
            }else{
            	searchTable();
            }
        }
    });
});

// 查找订单信息
function searchTable(){
	
	if(!isNotNull()){
		cbms.alert("必须有一个查询条件不为空！");
		return;
	}
	
    _sellerDt = $('#list-table').DataTable({
        bAutoWidth: false,
        searching:false,
        bPaginate: false,
        paging: false,
        ordering : false,
        ajax: {
            url: Context.PATH + '/allowance/loadsellerorderdata.html',
            type: 'POST',
            data: function (temp) {
                temp.sellerId = $('#seller').attr("accountid");
                temp.contractCode = $('#contractCode').val();
                temp.buyerId = $('#buyer').attr("accountid");
                temp.orderCode = $('#orderCode').val();
                temp.startTime = $('#startTime').val();
                temp.endTime = $('#endTime').val();
                temp.allowanceType = 'seller';
                temp.sellerDeptId=$("#department").val();
            }
        },
        fnRowCallback: function (nRow, aData, iDataIndex) {
            var inputHtml = "<input type='checkbox' name='check' value='" + aData.orderId + "'>";
            
            //other data
            inputHtml += "<input type='hidden' name='row-info' class='row-info' value='' sellerId = '"+aData.sellerId+"' sellerDepartmentId='"+aData.sellerDepartmentId+"' buyerId='"+aData.buyerId+"' buyerDepartmentId='"+aData.buyerDepartmentId+"'/>";
            
            $('td:eq(0)', nRow).html(inputHtml);
            var dt = new Date(aData.orderTime);
            var time = dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate();
            $('td:eq(1)', nRow).html(time);
            $('td:eq(6)', nRow).html(aData.totalWeight.toFixed(6));
            $('td:eq(7)', nRow).html(aData.totalActualWeight.toFixed(6));
            $('td:eq(8)', nRow).html(formatMoney(aData.totalAmount,2));
            $('td:eq(9)', nRow).html(formatMoney(aData.totalActualAmount,2));

            //买家全称显示逻辑： 如果有多个部门则显示部门否则不显示部门(部门不为空)
            if(aData.isShowBuyerDept && aData.buyerDepartmentName){
                $('td:eq(4)', nRow).html(aData.buyerName + "<br/>【" + aData.buyerDepartmentName +"】");
            }
        },
        columns: [
            {data: ''},
            {data: 'orderTime'},
            {data: 'contractCode'},
            {data: 'orderCode'},
            {data: 'buyerName'},
            {data: 'totalQuantity'},
            {data: 'totalWeight'},
            {data: 'totalActualWeight'},
            {data: 'totalAmount'},
            {data: 'totalActualAmount'}
        ],
        columnDefs: [
            {
                sDefaultContent: '', //解决请求参数未知的异常
                aTargets: ['_all']
            }
        ]
    });
}

//统计选中的订单
function orderTotal() {
    _orderIds = [];
    var checked = $("input[name='check']:checked").not("#allCheck");
    $(checked).each(function () {
        var id = $(this).val();
        _orderIds.push(id);
    });
    var checkedCount = checked.length;
    $('#checkCount').text(checkedCount);
}

//下一步
function allowanceNext() {
    //从重新关联订单进来
	
	var rowInfo = $('.row-info');
	
    var id = $("#allowanceId").val();
    if($("#allowanceId").val()){
        location.replace(Context.PATH + "/allowance/"+ id +"/editseller.html?orderIds="+_orderIds+
        "&allowanceType=seller" + "&sellerId="+rowInfo.attr("sellerId"));
    }else{
        window.location.href = Context.PATH +
        "/allowance/addseller.html?orderIds="+_orderIds+
        "&allowanceType=seller"+
        "&sellerId="+rowInfo.attr("sellerId")+"&sellerDeptId="+rowInfo.attr("sellerDepartmentId");
    }

}

//获取部门
function getDepartment(accountId) {
    $("#department").empty();//清空
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
                if (result.data != null && result.data.length > 1) {
                    //添加显示部门下拉框
                    $("#department").removeClass("none");
                } else {
                    $("#department").addClass("none");
                }
                if (result.data != null && $(result.data).length > 0) {
                    for (var i = 0; i < $(result.data).length; i++) {
                        var option = "<option value='" + result.data[i].id + "'>" + result.data[i].name + "</option>";
                        $("#department").append(option);//赋值
                    }
                }
            } else {
                cbms.alert("获取部门失败");
            }
        }

    });
}

function searchBuyerBySellerId(inputBox) {
    var sellerId = $('#seller').attr("accountid");
    if (sellerId == "") return;
    if(sellerId != _sellerId) {
        _buyerAccountList = null;// 先清除该元素
        $.ajax({
            type: 'post',
            url: Context.PATH + "/allowance/searchbuyer.html",
            data: {
                sellerId: $('#seller').attr("accountid")

            },
            error: function (s) {
            }
            , success: function (result) {
                if (result) {
                    if ($(result.data) != null && $(result.data).length > 0) {
                        _buyerAccountList = result.data;
                        fittingHtml(inputBox);
                    }
                } else {
                    cbms.alert("查询失败");
                }
            }
        });
    }else {
        if (_buyerAccountList != null && _buyerAccountList.length > 0) {
            fittingHtml(inputBox);
        }
    }
    _sellerId = sellerId;
}

function fittingHtml(inputBox) {
    $("#searchBuyerList").remove();   // 先清除该元素，防止出现多个
    var name = $.trim($(inputBox).val());
    var inputId = "account" + new Date().getTime();
    $(inputBox).attr("inputid", inputId);
    var searchHtml = "<div id='searchBuyerList' class='product-complete' inputid='" + inputId + "'>";
    searchHtml += "<ul>";
    for (var i = 0; i < _buyerAccountList.length; i++) {
        var tempName = _buyerAccountList[i].name;
        if(name == null || name == "" || tempName.indexOf(name) >= 0) {
            searchHtml += "<li><a href='javascript:;' ";
            searchHtml += " accountid='" + _buyerAccountList[i].id + "'";
            searchHtml += " accountname='" + _buyerAccountList[i].name + "'";
            searchHtml += " consigntype='" + _buyerAccountList[i].consignType + "'";
            searchHtml += " >";

            if (_buyerAccountList[i].consignType == "temp") {
                searchHtml += "<em class='label-orange white'>临</em>";
            }
            searchHtml += processStr(name, _buyerAccountList[i].name);
            searchHtml += "</a></li>";
        }
    }
    searchHtml += "</ul>";
    searchHtml += "</div>";
    $("body").append($(searchHtml));
    setLayerPosition($(inputBox), $("#searchBuyerList"));
    controlLayerShow($("#searchBuyerList"));
}

function isNotNull(){
	var array = [];
	array.push($("#seller").val());
	array.push($("#contractCode").val());
	array.push($("#buyer").val());
	array.push($("#orderCode").val());
	
	if($("#seller").val() && $("#buyer").val()){
		return true;
	}
	
	if($("#contractCode").val() || $("#orderCode").val() ){
		return true;
	}
	
	return false;
}

