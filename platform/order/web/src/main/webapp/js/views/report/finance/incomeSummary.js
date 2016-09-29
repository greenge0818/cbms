//利润报表
//code by tuxianming

var _type = "seller-category-table";  //table id
var _seller_category,  	//按【卖家->品名】统计
	_category_seller,  	//按【品名->卖家】统计
	_buyer_category, 	//按【买家->品名】统计
	_category_buyer;	//按【品名->买家】统计

var _seller_category_url = "",
	_category_seller = "",
	_buyer_category = "",
	_cagtegory_buyer = "";

var _tableHeight = "";

var refs = [
	{
	    "targets": 3, //第几列 从0开始
	    "data": "weight",
	    "render": renderWeight
	},
	{
	    "targets": 4, //第几列 从0开始
	    "data": "amount",
	    "render": renderAmount
	},
	{
	    "targets": 5, //第几列 从0开始
	    "data": "saleAmount",
	    "render": renderAmount
	},
	{
	    "targets": 6, //第几列 从0开始
	    "data": "incomeAmount",
	    "render": renderAmount
	}
]

jQuery(function ($) {
	initEvent();
	_tableHeight = ($(window).height()-(300)<300?300:$(window).height()-(300))+"px";
	initSellerCategoryTable();
});

/**
 * 初始化表格或者重新加载表格数据
 * @param type
 * @param refresh
 */
function reloadTable(type, refresh){
	
	//如果table没有初始化则执行初始化方法，如果已经初始化，则看是不是需要强制刷新
	if(type=="seller-category-table"){
		if(_seller_category){
			if(refresh)
				_seller_category.ajax.reload();
			$("#"+type+"-content").show();
		}else{
			initSellerCategoryTable();
		}
	}else if(type=="category-seller-table"){
		if(_category_seller){
			if(refresh) _category_seller.ajax.reload();
			$("#"+type+"-content").show();
		}else{
			initCategorySellerTable();
		}
	}else if(type=="buyer-category-table"){
		if(_buyer_category){
			if(refresh) _buyer_category.ajax.reload();
			$("#"+type+"-content").show();
		}else{
			initBuyerCategoryTable();
		}
	}else if(type=="category-buyer-table"){
		if(_category_buyer){
			if(refresh) _category_buyer.ajax.reload();
			$("#"+type+"-content").show();
		}else{
			initCategoryBuyerTable();
		}
	}
}

//卖家 -> 品名
function initSellerCategoryTable() {
	_seller_category = $("#seller-category-table").DataTable({
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "lengthChange": false, //显示pageSize的下拉框 50 100 150
        "info": false,
        "ajax": {
            "url": Context.PATH + "/report/finance/incomesummarybyseller.html",
            "type": "POST",
            data: function (d) {
            	d.groupType = "seller";
            	buildParam(d);  //拼装参数
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns:[
                   {data: 'name'},   		//服务中心
                   {defaultContent: ''},  	//卖家	
                   {data: 'nsortName'},   	//品名
                   {data: 'weight'},   		//重量
                   {data: 'amount'},  		//采购金额（元）
                   {data: 'saleAmount'},  	//销售金额（元）
                   {data: 'incomeAmount'}	//税前利润（元）
               ]
        , columnDefs:refs
        ,"fnRowCallback": function (nRow, aData, iDataIndex) {
        	renderRow(nRow);  //给小计，合计，总计这些行重新设置样式
        	if(iDataIndex == 0){ //给品名这第列设置宽度，一般只需要给这一列的第一行设置宽度就可以了
        		$('td:eq(2)', nRow).css("min-width", "90px");
        	}
        	var row = "1";
        	setColumnDefaultWidth(nRow, iDataIndex, row, aData); //设置后四列的默认宽度,设置是否显示部门
        	
        }
        , "fnDrawCallback": function (row, data, start, end, display) {
        	mergeColumn(this);  //合并服务中心相同行，合并第二列：卖家
        	
        	//隐藏table底部信息栏
        	var id = this.attr("id");
        	$("#"+id+"_wrapper > div:eq(2)").hide();
        }
        ,"scrollY": _tableHeight
        ,"scrollX": true
    });
}

//品名->卖家
function initCategorySellerTable() {
	_category_seller = $("#category-seller-table").DataTable({
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "lengthChange": false, //显示pageSize的下拉框 50 100 150
        "info": false,
        "ajax": {
            "url": Context.PATH + "/report/finance/incomesummarybyseller.html",
            "type": "POST",
            data: function (d) {
            	d.groupType = "category";
            	buildParam(d);
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns:[
					{data: 'name'},   		//服务中心
					{data: 'nsortName'},   	//品名
					{defaultContent: ''},  //卖家	
					{data: 'weight'},   	//重量
					{data: 'amount'},  		//采购金额（元）
					{data: 'saleAmount'},  	//销售金额（元）
					{data: 'incomeAmount'}	//税前利润（元）
               ]
        , columnDefs: refs
        ,"fnRowCallback": function (nRow, aData, iDataIndex) {
        	//重写
        	renderRow(nRow); //给小计，合计，总计这些行重新设置样式
        	if(iDataIndex == 0){ //给品名这第列设置宽度，一般只需要给这一列的第一行设置宽度就可以了
        		$('td:eq(1)', nRow).css("min-width", "90px");
        	}
        	var row = "";
        	setColumnDefaultWidth(nRow, iDataIndex, row, aData); //设置后四列的默认宽度,设置是否显示部门
        }
        , "fnDrawCallback": function (row, data, start, end, display) {
        	mergeColumn(this); //合并服务中心相同行，合并第二列：品名
        	
        	//隐藏table底部信息栏
        	var id = this.attr("id");
        	$("#"+id+"_wrapper > div:eq(2)").hide();
        }
        ,"scrollY": _tableHeight
        ,"scrollX": true
    });
}

//买家 -> 品名
function initBuyerCategoryTable() {
	_buyer_category = $("#buyer-category-table").DataTable({
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "lengthChange": false, //显示pageSize的下拉框 50 100 150
        "info": false,
        "ajax": {
            "url": Context.PATH + "/report/finance/incomesummarybybuyer.html",
            "type": "POST",
            data: function (d) {
            	d.groupType = "buyer";
            	buildParam(d);
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns:[
					{data: 'name'},   		//服务中心
					{defaultContent: ''},  //买家	
					{data: 'nsortName'},   	//品名
					{data: 'weight'},   	//重量
					{data: 'amount'},  		//采购金额（元）
					{data: 'saleAmount'},  	//销售金额（元）
					{data: 'incomeAmount'}	//税前利润（元）
               ]
        , columnDefs: refs
        ,"fnRowCallback": function (nRow, aData, iDataIndex) {
        	//重写
        	renderRow(nRow); //给小计，合计，总计这些行重新设置样式
        	if(iDataIndex == 0){//给品名这第列设置宽度，一般只需要给这一列的第一行设置宽度就可以了
        		$('td:eq(2)', nRow).css("min-width", "90px");
        	}
        	var row = "1";
        	setColumnDefaultWidth(nRow, iDataIndex, row, aData); //设置后四列的默认宽度,设置是否显示部门
        }
        , "fnDrawCallback": function (row, data, start, end, display) {
        	mergeColumn(this); //合并服务中心相同行，合并第二列：买家
        	
        	//隐藏table底部信息栏
        	var id = this.attr("id");
        	$("#"+id+"_wrapper  > div:eq(2)").hide();
        }
        ,"scrollY": _tableHeight
        ,"scrollX": true
    });
}

//品名 -> 买家
function initCategoryBuyerTable() {
	_category_buyer = $("#category-buyer-table").DataTable({
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "lengthChange": false, //显示pageSize的下拉框 50 100 150
        "info": false,
        "ajax": {
            "url": Context.PATH + "/report/finance/incomesummarybybuyer.html",
            "type": "POST",
            data: function (d) {
            	d.groupType = "category";
            	buildParam(d);
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns:[
                   	{data: 'name'},   		//服务中心
					{data: 'nsortName'},   	//品名
					{defaultContent: ''},  //买家	
					{data: 'weight'},   	//重量
					{data: 'amount'},  		//采购金额（元）
					{data: 'saleAmount'},  	//销售金额（元）
					{data: 'incomeAmount'}	//税前利润（元）
               ]
        , columnDefs:refs
        ,"fnRowCallback": function (nRow, aData, iDataIndex) {
        	//重写
        	renderRow(nRow); //给小计，合计，总计这些行重新设置样式
        	if(iDataIndex == 0){ //给品名这第列设置宽度，一般只需要给这一列的第一行设置宽度就可以了
        		$('td:eq(1)', nRow).css("min-width", "90px");
        	}
        	var row = "";
        	setColumnDefaultWidth(nRow, iDataIndex, row, aData); //设置后四列的默认宽度,设置是否显示部门
        }
        , "fnDrawCallback": function (row, data, start, end, display) {
        	mergeColumn(this); //合并服务中心相同行，合并第二列：品名
        	
        	//隐藏table底部信息栏
        	var id = this.attr("id");
        	$("#"+id+"_wrapper  > div:eq(2)").hide();
        }
        ,"scrollY": _tableHeight
        ,"scrollX": true
    });
}

/**
 * 合并服务中心相同行，合并第二列：买家， 卖家， 品名
 * @param table
 */
function mergeColumn(table){
	var rows = table.find("tbody tr");
	
	var itemPrevCell;  //sellerName, buyerName, categoryName
	var orgPrevCell;
	var orgCount = 0;
	var itemCount = 0;
	
	rows.each(function(index){
		var row = $(this);
		var cells =  row.find("td");
		var orgCell  = $(cells[0]).text();
		var itemCell = $(cells[1]).text();
		
		if(index==0){
			orgPrevCell  = $(cells[0]).text();
			itemPrevCell = $(cells[1]).text();
		}
		
		//合并服务中心
		if(orgPrevCell == orgCell){
			if(index==(rows.length-1)){
				//process merge
				if(orgCount>=1){
					var startIndex = index - orgCount;
					for(var i=startIndex+1; i<=index; i++){
						table.find("tbody tr:eq("+i+") td:eq(0)").remove(); //attr("rowspan", "5");
					}
					table.find("tbody tr:eq("+startIndex+") td:eq(0)").attr("rowspan", (orgCount+1));
				}
			}else{
				orgCount++;
			}
		}else{
			//process merge
			if(orgCount>1){
				var startIndex = index - orgCount;
				for(var i=startIndex+1; i<index; i++){
					table.find("tbody tr:eq("+i+") td:eq(0)").remove(); //attr("rowspan", "5");
				}
				table.find("tbody tr:eq("+startIndex+") td:eq(0)").attr("rowspan", orgCount);
			}
			orgPrevCell = orgCell;
			orgCount = 1;
		}
		
		//合并同一公司（买家，卖家），或者，品名
		if(itemPrevCell==itemCell){
			if(index==(rows.length-1)){
				//process merge
				if(orgCount>1){
					var startIndex = index - itemCount;
					for(var i=startIndex+1; i<=index; i++){
						table.find("tbody tr:eq("+i+") td:eq(1)").remove();
					}
					table.find("tbody tr:eq("+startIndex+") td:eq(1)").attr("rowspan", itemCount);
				}
				
			}else{
				itemCount++;
			}
		}else{
			//如果不是则合并，相同的行。
			if(itemCount>1){
				var startIndex = index - itemCount;
				for(var i=startIndex+1; i<index; i++){
					table.find("tbody tr:eq("+i+") td:eq(1)").remove();
				}
				//table.find("tr["+index+"]").attr("colspan", (orgCount+1));
				table.find("tbody tr:eq("+startIndex+") td:eq(1)").attr("rowspan", itemCount);
			}
			itemPrevCell = itemCell;
			itemCount = 1;
		}
		
	});
}

function initEvent(){
	
	/**
	 * 点击tab时，显示相应的tab页面，并初始化相应的表格
	 */
	$("#mytabbar").on("click", "a", function(){
		var _this = $(this);
		var li = _this.parent("li");
		if(li.hasClass("active")==false){
			$("#mytabbar").find("li").removeClass();
			li.addClass("active");
			
			$("#table-layout > div").hide();
//			$("#"+_type).hide();
			
			_type=_this.attr("vtype");
			reloadTable(_type);
			$("#"+_type+"-content").show();
		}
	});
	
	//导出报表 
	$("#exportexcel").click(exportToExcel);
	
	//点击搜索时，根据条件重新加载报表 
	$("#queryBtn").click(function(){
		reloadTable(_type, true);
	});
	
	/**
	 * 点击：选择报表中心，服务中心选择框
	 */
	$("#orgSelectBtn").click(showSelectOptionsBox);
	
	/**
	 * 选中：所有服务中心时，取消其它服务中心选项框, 选中其它服务中心时，取消：所有服务中心
	 */
	clickSelectAll();
}

/**
 * 构建查询参数
 * @param d
 */
function buildParam(d){
	
	var result = [];
	//查看哪些服务中心选中了，如果选中了: 全部服务中心 选项则不往后台传输数据
	var orgIds = [];
	$("#orgSelect li").each(function(){
		var li = $(this);
		var checkbox = li.find("input[type='checkbox']");
		if(checkbox.prop('checked')){
			var orgId = checkbox.val();
			if(orgId!='all'){
				orgIds.push(orgId);
			}
		}
	});
	if(orgIds && orgIds.length>0){
		if(d) d.orgIds = orgIds.toString();
		result.push({"key":"orgIds", "value": orgIds.toString()});
	}
	
	var showSubSummary = $("#showSubSummary").prop("checked");
	if(showSubSummary){
		if(d) d.showSubSummary = showSubSummary;
		result.push({"key":"showSubSummary", "value":showSubSummary});
	}
	
	var showNoneIncome = $("#showNoneIncome").prop("checked");
	if(d) d.showNoneIncome = showNoneIncome;
	result.push({"key":"showNoneIncome", "value":showNoneIncome});
	
	var accountName = $("#accountName").val();
	if(accountName){
//		if(_type=="seller-category-table" 
//			|| _type=="category-seller-table"){
//			d.seller = accountName;
//		}else{
//			d.buyer = accountName;
//		}
		if(d) d.accountName = accountName;
		result.push({"key":"accountName", "value":accountName});
	}	
	
	var category = $("#category").val();
	if(category){
		if(d) d.nsortName = category;
		result.push({"key":"nsortName", "value":category});
	}
	
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	
	if(d) d.startDate = startTime;
	result.push({"key":"startDate", "value":startTime});
	
	if(d) d.endDate = endTime;
	result.push({"key":"endDate", "value":endTime});
	
	return result;
}

/**
 * 导出报表
 */
function exportToExcel() {
    var form = $("<form>");
    form.attr('style', 'display:none');
    form.attr('target', '');
    form.attr('method', 'post');
    form.attr('action', Context.PATH + "/report/finance/exportincomesummary.html");
    form.attr("enctype","multipart/form-data");//防止提交数据乱码

    form.append("<input type='hidden' name='groupType' value='"+(_type.replace("-table", ""))+"'/>");
    
    var results = buildParam();
    for(var i=0; i<results.length; i++){
    	form.append("<input type='hidden' name='"+results[i].key+"' value='"+results[i].value+"'/>");
    }
    
    $('body').append(form);

    form.submit();
    form.remove();
}

/**
 * 点击：选择报表中心，服务中心选择框
 */
function showSelectOptionsBox(){
	var optionbox = $("#orgSelect");
	if(optionbox.css("display") == "none"){
		optionbox.show();
		$(document).on("mouseleave","#org_options", function(){
			optionbox.hide();
		});
	}else{
		optionbox.hide();
	}
}

/**
 * 选中：所有服务中心时，取消其它服务中心选项框, 选中其它服务中心时，取消：所有服务中心
 */
function clickSelectAll(){
	
	$("#selectAllOrg").click(function(){
		var checked=$(this).prop('checked');
		if(checked){
			$("#orgSelect li input[type='checkbox']").removeAttr("checked");
			$(this).prop("checked", "checked");
		}
	})
	
	$("#orgSelect li input[type='checkbox']").not("#selectAllOrg").click(function(){
		var selectAll = $("#selectAllOrg");
		if(selectAll.prop("checked")){
			//$(this).removeAttr("checked");
			selectAll.removeAttr("checked");
		}
	});
	
}

/**
 * 给合计，小计，总计重新设置样式
 * @param nRow
 */
function renderRow(nRow){
	
	if($('td:eq(2)', nRow).text().indexOf("小计")>=0
			|| $('td:eq(0)', nRow).text().indexOf("总计")>=0 ){
		var tds =  $("td", nRow);
		for(var i=0; i<tds.length; i++){
			$("td:eq("+i+")", nRow).css("font-weight", "bold")
		}
	}
	
	if($('td:eq(1)', nRow).text().indexOf("合计")>=0){
		var tds =  $("td", nRow);
		for(var i=0; i<tds.length; i++){
			$("td:eq("+i+")", nRow).css("font-weight", "bold").css("background-color", "#CCFFFF");
		}
	}	
}

/**
 * 格式化金额
 * @param data
 * @param type
 * @param full
 * @param meta
 * @returns
 */
function renderAmount(data, type, full, meta) {
    if (data) {
        return formatMoney(data, 2);
    }
    return "-";
}

/**
 * 格式化重量
 * @param data
 * @param type
 * @param full
 * @param meta
 * @returns
 */
function renderWeight(data, type, full, meta) {
    if (data) {
        return formatMoney(data, 4);
    }
    return "-";
}

function setColumnDefaultWidth(nRow, iDataIndex, row, aData){
	if(iDataIndex == 0){ //给品名这第列设置宽度，一般只需要给这一列的第一行设置宽度就可以了
		$('td:eq(3)', nRow).css("min-width", "140px");
		$('td:eq(4)', nRow).css("min-width", "180px");
		$('td:eq(5)', nRow).css("min-width", "180px");
		$('td:eq(6)', nRow).css("min-width", "180px");
	}
	var custName = aData.accountName;
	if (aData.departmentCount > 1) {
		custName = custName+"【"+aData.departmentName+"】";
	}
	if (row != "") {
    	$('td:eq(1)', nRow).html(custName);
	} else {
		$('td:eq(2)', nRow).html(custName);
	} 
}
