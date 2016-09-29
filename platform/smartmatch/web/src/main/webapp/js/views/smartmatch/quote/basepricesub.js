/**
*买家订阅js
*/
//全局变量
var _dataPage = {
	selectedIds:[], //数据行选中id记录集
    dt:null        //datatables
}
//资源全局变量
var _resAttr={
	//卖家数据
	accountCacheData:[]
	
};

var accountInfo=[];
var accountSearchTable=null;
/**
 * 获取一些通用的全局数据：所有卖家
 */
(function getCommonData(){
	$.ajax({
        type: 'post',
        url: Context.PATH + "/smartmatch/quote/getCommonData.html",
        success: function (result) {
        	if(result.success){
        		_resAttr.accountCacheData=result.data.accountList;
        		
        	}
        }
    });
})();

$(document).ready(function() {
	
	$('#accountName_form').inputFocus();

	initAccountSearch();
	 /**
     * 点击弹层以外的地方会使弹层消失
     */
    $("body").click(function (event) {
        var t = event.target;
        if ($(t).closest('.form-item').length > 0) {
            return;
        }
        $(".show-layer").hide();
    });
	// 卖家输入框点击事件
	$(document).on('input propertychange','#accountName_form', function () {
        showPYMatchList($(this),_resAttr.accountCacheData,"id","name",generateTelInfo);
        $("#dropdown").css("z-index",9999);
    });
	// 地区移开输入框
	$(document).on('blur','#cityName_form', generateBasePriceInfo);
	
    var url = Context.PATH + "/smartmatch/quote/searchbasepricesub.html";
    _dataPage.dt = jQuery("#dynamic-table").DataTable({
        "processing" : false,
        "serverSide" : true,
        "searching" : false,
        "ordering" : false,
        "paging" : true,
        //"bInfo" : false,
        "autoWidth": false,
		"iDisplayLength" : 50,
		"aLengthMenu" : [10,30,50,100],//定义每页显示数据数量
        "ajax" : {
            "url" : url,
            "type" : "POST",
            data : function(d) {
            	 return $.extend({}, d, {
                     accountName:$("#accountName").val(),
                     ownerName : $("#ownerName").val(),
                     cityName : $("#cityName").val()
                 });
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
            // operation button
            $('td:last', nRow).html(generateOptHtml(aData));
        },
        columns : [
            {data : 'id'},
            {data : 'accountName'},
            {data : 'tel'},
            {data : 'cityName'},
            {data : 'subBasePriceName'},
            {data : 'traderTel'},
            {defaultContent:""}
        ],
        columnDefs: [/*
		{
			"targets": 9, //第几列 从0开始
			"data": "price",
			"render": renderAmount
		},
		{
			sDefaultContent: '', //解决请求参数未知的异常
			aTargets: ['_all']
		},
		{
			"targets": 13, //第几列 从0开始
			"render": function (data, type, full, meta) {
				var status=$("#myTab4 li.active input").val();
				if (status == 8) {
					return dateFormat(new Date(full.lastUpdated), "yyyy-MM-dd hh:mm:ss");
				}else{
					return data;
				}
			}
		}
	 	*/],
        "drawCallback": function(e,t) {
        	
        }
    });
	
	
	$(document).on("click", "#addSub", function ()  { 
		var html = $('#determine').html();
		cbms.getDialog("添加订阅", html);
		//初始化交易员，地区等输入框
		$("#ownerId_form").val(userName);
		$("#cityName_form").val(cityName);
		$("#actionType").val("ADD");
		if(cityName != null&& cityName != ''){
			generateBasePriceInfo();
		}
		
		$(document).on("click", "#btnClose",function () {
	       cbms.closeDialog();
	    });
		 
		$(document).off('click', '#btncommit');
			
		$(document).on('click', '#btncommit', function() {
			
		});
	});
	
	$(document).on("click", "#searchList", function ()  { 
		searchData(true);
	});
	
	//删除
	$(document).on("click", ".fa-trash-o", function(){
		var tr = $(this).closest('tr'), data = _dataPage.dt.row(tr).data();
		/*if(data.statusNum==1){
			cbms.alert("该条资源已挂牌，请撤牌后再做删除!");
			return false;
		}*/
		cbms.confirm("您确定删除该条信息吗?",null,function(){
			delResource(data.id);
			accountSearchTable.ajax.reload();//重新加载买家查找数据
		});

	});
	//删除
	$(document).on("click", ".fa-edit", function(){
		var tr = $(this).closest('tr'), data = _dataPage.dt.row(tr).data();
		var html = $('#determine').html();
		cbms.getDialog("修改订阅", html);
		//初始化交易员，地区等输入框
		$("#accountName_form").attr("val",data.accountId);
		$("#accountName_form").val(data.accountName);
		$("#ownerId_form").val(data.ownerName);
		$("#cityName_form").val(data.cityName);
		cityId = data.cityId;
		$("#subId").val(data.id);
		$("#actionType").val("EDIT");
		if(cityName != null&& cityName != ''){
			generateBasePriceInfo(data.subBasePriceIds);
			
		}
		if(data.accountId != null&& data.accountId != ''){
			generateTelInfo(data.subBasePriceContactIds,data.subBasePriceTraderIds);
		}
		$(document).on("click", "#btnClose",function () {
	       cbms.closeDialog();
	    });
		 
		$(document).off('click', '#btncommit');
			
		$(document).on('click', '#btncommit', function() {
			
		});
	});
	
	//取消
	$(document).on("click", "#cancel_btn", function(){
		cbms.closeDialog();

	});
	
	
	/***** 资源保存****/
	$(document).on("click", "#commit_btn", function(event) {
		
		//获取选中的联系人,名称,电话
		var chk_contact_value =[];
		var chk_contact_name = [];
		var chk_contact_tel =[];
		$('input[name="contact_checkbox"]:checked').each(function(){ 
			chk_contact_value.push($(this).val());
			chk_contact_name.push($(this).attr("contactName"));
			chk_contact_tel.push($(this).attr("contactTel"));
			}); 
		if(chk_contact_value.length==0){
			cbms.alert('你还没有选择任何联系人手机！'); 
			return;
		}

		//获取选中的交易员
		var chk_trader_value=[];
		$("#subtrader input[name='trader_checkbox']:checked").each(function(){
			chk_trader_value.push($(this).val());
		});

		//获取选中的基价,联系人,联系人电话
		var chk_bprice_value =[];
		$('input[name="basePrice_checkbox"]:checked').each(function(){
			chk_bprice_value.push($(this).val()); 
		  });
		if(chk_bprice_value.length==0){
			cbms.alert('你还没有选择任何订阅基价！'); 
			return;
		}
	
		var data={
			cityId:cityId,
			cityName:cityName,
			id:$("#subId").val(),
			actionType:$("#actionType").val(),
			accountId:$("#accountName_form").attr("val"),
			ownerId:userId,
			subBasePriceIds:chk_bprice_value.join(','),
			subBasePriceContactIds:chk_contact_value.join(','),
			subBasePriceContactNames:chk_contact_name.join(','),
			subBasePriceContactTels:chk_contact_tel.join(','),
			subBasePriceTraderIds:chk_trader_value.join(',')
		};
		var url = Context.PATH + "/smartmatch/quote/saveBasePriceSub.html";
		//提交按钮不可用，防止重复提交
		$("#commit_btn").attr("disabled","disabled");
		//提交
    	$("#basepricesub_form").ajaxSubmit({  
            type: 'post',  
            url: url, 
            data:data,
            dataType:"json",
            success: function(data){
				if(data.success){
					searchData(false);
					accountSearchTable.ajax.reload();//重新加载买家查找数据
					cbms.closeDialog();
				}
				cbms.alert(data.data);
				//提交按钮重新可用
				$("#commit_btn").removeAttr("disabled");
				//updateCountByStatus();
				
            } 
        });
		return false;
	});

	$(document).on("click","#customer_search",function(){
		$("#customer_search_panel").removeClass("none");
	});
	//选择买家取消
	$(document).on("click","#cancel_buyerselect",function(){
		$("#customer_search_panel").addClass("none");
	});
	//选择买家确定
	$(document).on("click","#buyerselected",function(){
		var cbbuyer=$("#customer_list_table input[type=radio][name=account_select]:checked");
		if(cbbuyer.length>0){
			var companyName=cbbuyer.closest("tr").find("td").eq(1).text();
			var account_id=cbbuyer.attr("aid");
			$("#accountName_form").attr("val",account_id).val(companyName);
			generateTelInfo();
			$("#customer_search_panel").addClass("none");
		}
	});

	//选择买家确定
	$(document).on("click","#customer_list_table tr",function(){
		var cbradio = $(this).find("input[type=radio][name=account_select]");
		if (cbradio.length > 0) {
			cbradio.prop("checked",true)
		}
	});

});

function searchData(isNewSearch) {
	if(isNewSearch){
		_dataPage.dt.ajax.reload();
	}else{
		_dataPage.dt.ajax.reload(null, false);
	}
}
/**
 * 删除
 * @param ids  id集
 */
function delResource(ids){
	if(!utils.isEmpty(ids)){
		$.ajax({
	        type: 'post',
	        url: Context.PATH + "/smartmatch/quote/delBasePriceSub.html",
	        data : {"ids":ids},
	        success: function (result) {
	        	if(result.success){
	        		cbms.alert(result.data);
	        		_dataPage.dt.ajax.reload();
	        	}
	        }
	    });
	}
}

/**
 * 操作列图片组装
 * @param id
 * @returns {String}
 */
function generateOptHtml(aData) {
	var optHtml = '<div class="fa-hover">';
	optHtml += "<a href='javascript:void(0);' title='编辑'>";
	optHtml += "<i class=\"fa fa-edit fa-2x\"></i></a>";
	optHtml += "<a href='javascript:void(0);' title='删除'><i class=\"fa fa-trash-o fa-fw fa-2x\"></i></a>";
    optHtml += '</div>';
    return optHtml;
}

function generateTelInfo(_telinfo,traderinfo){
	var select_value_array;
	if(_telinfo!=null&&typeof(_telinfo) != "undefined"&&typeof(_telinfo) == "string"){
		select_value_array = _telinfo.split(",");
	}else{
		select_value_array=[];
	}
	var traderList=[];
	if(traderinfo!=null&&typeof(traderinfo) != "undefined"&&typeof(traderinfo) == "string"){
		traderList = traderinfo.split(",");
	}

	var accountIdVal = $("#accountName_form").attr("val");
    if(accountIdVal ==''){
    	return;
    }
	$.ajax({
        type: 'post',
        url: Context.PATH + "/smartmatch/quote/getContactData.html",
        data : {"accountId":accountIdVal},
        success: function (result) {
        	if(result.success){
				var contactData = result.data.contactInfo;
				t = "<table border='0'>";
				$.each(contactData, function (i, item) {
					if (item != null) {
						var flag = true;
						if (i % 2 == 0) {
							flag = false;
						}
						var contactName = item.name == null ? "-1" :item.name;
						var contactTel = item.tel == null ? "-1" :item.tel
						if (!flag)
							t += "<tr>";
						t += '<td><label><input name="contact_checkbox" type="checkbox" ';
						if (contains(select_value_array, item.id)) {
							t += ' checked="checked" ';
						}
						t += ' value="' + item.id + '"'+  'contactName="'+contactName + '" contactTel="'+ contactTel+ '" />' + item.name + ' ' + item.tel + '</label></td>';

						if (flag || i == (result.data.length - 1))
							t += "</tr>";
					}
				})
				t += "</table>";
				$("#subtel").html(t);
				
				var traderData = result.data.traderInfo;
				var traderhtml='';
				$.each(traderData, function (i, item) {
					if (item != null) {
						var flag = true;
						if (i % 2 == 0) {
							flag = false;
						}
						if (!flag)
							traderhtml += "<tr>";
						traderhtml += '<td><label><input name="trader_checkbox" type="checkbox" ';
						if (jQuery.inArray(item.id.toString(),traderList)>=0) {
							traderhtml += ' checked="checked" ';
						}
						traderhtml += ' value="' + item.id + '" />' + item.name + ' ' + item.tel + '</label></td>';

						if (flag || i == (result.data.length - 1))
							traderhtml += "</tr>";
					}
				})
				$("#subtrader").html(traderhtml);
    		
        	}
        }
    });
}

function generateBasePriceInfo(_priceinfo) {
	var select_value_array;
	if(_priceinfo!=null&&typeof(_priceinfo) != "undefined"&&typeof(_priceinfo) == "string"){
		select_value_array = _priceinfo.split(",");
	}else{
		select_value_array=[];
	}
	$.ajax({
        type: 'post',
        url: Context.PATH + "/smartmatch/quote/getBasePriceData.html",
        data : {"cityName":$("#cityName_form").val()},
        success: function (result) {
        	if(result.success){
				data = result.data;
				t = "<table border='0'>";
				var rowCount = 4;
				$.each(data, function (i, item) {
					cityId = item.cityId;
					if (i % rowCount == 0)
						t += "<tr>";
					t += '<td><label><input name="basePrice_checkbox" type="checkbox"';
					if (contains(select_value_array, item.id)) {
						t += ' checked="checked" ';
					}
					t += ' value="' + item.id + '" />' + item.basePriceName + '</label></td>';
					if (i % rowCount == rowCount - 1 || i == (result.data.length - 1))
						t += "</tr>";
				})
				t += "</table>";
				$("#subbaseprice").html(t);
        	}
        }
    });
}
function contains(array, obj) {
	var value = obj+'';
	var i = array.length;
	while (i--) {
		if (array[i] === value) {
			return true;
		}
	}
	return false;
}

//初始化买家查找div
function initAccountSearch(){
	var url = Context.PATH + "/smartmatch/quote/querycustsubscribinfo.html";
	accountSearchTable = jQuery("#customer_list_table").DataTable({
		"processing": false,
		"serverSide": true,
		"searching": false,
		"ordering": false,
		"paging": false,
		"bInfo" : false,
		"ajax": {
			type: 'post',
			url: url
		},
		columns: [
			{data: "accountId"},
			{data: "accountName"},
			{data: "subscribed"}
		],
		columnDefs: [
			{
				"targets": 0,
				"render": function (data, type, full, meta) {
					if(full.subscribed==1){
						return "";
					}
					else{
						return "<input type='radio' name='account_select' aid='" + data + "'/>";
					}
				}
			},
			{
				"targets": 2,
				"render": function (data, type, full, meta) {
					return data == 1 ? "已订阅" : "未订阅";
				}
			}
		]
	});
}