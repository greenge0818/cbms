var _buyerDt;
var _isDestroy = true;
var _orderIds;           // 发票ID集合
var _deptIds; //买家部门id集合

$().ready(function () {
	_buyerDt = $('#list-table').dataTable({
    	bAutoWidth: false,
        searching:false,
        bPaginate: false,
        paging: false,
        ordering : false
    });
	
	$("#createForm").verifyForm();
	
	//搜索
	$(document).on("click", "#btnSearch", function(){
		/*
		if (!setlistensSave())return;
		if($('#buyer').attr("accountid") == 0) {
			cbms.alert("您输入的公司名字可能错误或不存在，请重输！");
			return;
		}
		*/
		_orderIds = null;
        _deptIds = null;
		if(_isDestroy) {
			_isDestroy = false;
			_buyerDt.fnDestroy();
			searchTable();
		}else {
			if(!isNotNull()){
        		cbms.alert("必须有一个查询条件不为空！");
        		return;
        	}
        	if(_buyerDt && _buyerDt.ajax){
        		_buyerDt.ajax.reload();
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

        if(_deptIds == null || _deptIds.length == 0){
            cbms.alert("订单必须有部门");
            return;
        }

        //同一家公司 不允许多个部门
        if(_deptIds && _deptIds.length > 1){
            cbms.alert("不能选择多个部门");
            return;
        }

    	allowanceNext();
    });
    
    $(document).on("click", "#searchAccountList li a", function () {
    	if($('#buyer').attr("accountid") == 0) {
			return;
		}
    	if(_isDestroy) {
			_isDestroy = false;
			_buyerDt.fnDestroy();
			searchTable();
		}else {
			if(!isNotNull()){
        		cbms.alert("必须有一个查询条件不为空！");
        		return;
        	}
        	if(_buyerDt && _buyerDt.ajax){
        		_buyerDt.ajax.reload();
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
	
	_buyerDt = $('#list-table').DataTable({
		bAutoWidth: false,
        searching:false,
        bPaginate: false,
        paging: false,
        ordering : false,
        ajax: {
            url: Context.PATH + '/allowance/loadbuyerorderdata.html',
            type: 'POST',
            data: function (temp) {
            	temp.sellerId = $('#seller').attr("accountid");
            	temp.contractCode = $('#contractCode').val();
            	temp.buyerId = $('#buyer').attr("accountid");
            	temp.orderCode = $('#orderCode').val();
            	temp.startTime = $('#startTime').val();
            	temp.endTime = $('#endTime').val();
            	temp.allowanceType = 'buyer';
            }
        },
        fnRowCallback: function (nRow, aData, iDataIndex) {
        	var inputHtml = "<input type='checkbox' name='check' value='" + aData.orderId + "' deptid='"+aData.buyerDepartmentId+"'>";
        	
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
    _deptIds = [];
	var checked = $("input[name='check']:checked").not("#allCheck");
	$(checked).each(function () {
		var id = $(this).val();
		_orderIds.push(id);

        //如果不存在此部门则添加  历史数据如果没有部门折让会有问题
        var deptid = $(this).attr("deptid");
        if(deptid && deptid != 'null' && $.inArray(deptid,_deptIds) < 0){
            _deptIds.push(deptid);
        }
	});
	var checkedCount = checked.length;
	$('#checkCount').text(checkedCount);
}

//下一步
function allowanceNext() {
	
	var rowInfo = $('.row-info');
	
	window.location.href = Context.PATH + 
			"/allowance/addbuyer.html?orderIds="+_orderIds+
			"&allowanceType=buyer"+
			"&buyerId="+rowInfo.attr("buyerId")+"&buyerDeptId="+_deptIds[0] || '' ;
}

function isNotNull(){
	var array = [];
	array.push($("#buyer").val());
	array.push($("#contractCode").val());
	array.push($("#orderCode").val());
	
	if($("#buyer").val()){
		return true;
	}
	
	if($("#contractCode").val() || $("#orderCode").val()){
		return true;
	}
	
	return false;
}

