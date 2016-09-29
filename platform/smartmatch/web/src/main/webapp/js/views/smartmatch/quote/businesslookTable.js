var singleTable;
var multiTable;
var _condition_data=null;//查询的参数
var limitPrices=[];

$(document).ready(function () {
	loadSingleTable();
});

$(document).on('input keypress',"input[name='specs']", function (event) {
    if(event.keyCode == "13"){
    	serachList();
    }
});

//加载查询条件一列,单一表格
function loadSingleTable(){
	$("#singleTable").css('display','');
	$("#multiTable").css('display','none').find("tbody").empty();
	var beginIndex;
	singleTable = $('#dynamic-table-single').dataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "bLengthChange": false,
        "iDisplayLength" : 50,
        "aLengthMenu" : [10,30,50,100],//定义每页显示数据数量
        "ajax": {
            url: Context.PATH + '/smartmatch/quote/searchsinglebusinessresource.html',
            type: 'POST',
            data: function (d) {
            	if(_condition_data==null){
	            	d.spec = $.trim($($("div[id='m-searchDiv']").find('input[id="spec"]')[0]).val());
	            	d.categoryName = $.trim($($("div[id='m-searchDiv']").find('input[id="category"]')[0]).val());
	            	d.materialName = $.trim($($("div[id='m-searchDiv']").find('input[id="material"]')[0]).val());
	            	d.factoryName = $.trim($($("div[id='m-searchDiv']").find('input[id="factory"]')[0]).val());
	            	d.cityName = $.trim($($("div[id='m-searchDiv']").find('input[id="city"]')[0]).val());
            	}else{
            	   	d.spec = _condition_data.spc;
	            	d.categoryName = _condition_data.categoryName;
	            	d.materialName = _condition_data.materialName;
	            	d.factoryName = _condition_data.factoryName;
	            	d.cityName = _condition_data.cityName;
          
            	}
            	d.isTodayPrice = $("[id='todayPrice']")[0].checked;
            	d.accountName = $("#account").val();
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
        	$('td:eq(0)', nRow).html('<label class="pos-rel"><input name="single_checkbox" type="checkbox" class="ace" val="'+aData.id+'"/><span class="lbl"></span></label>');
        	$('td:eq(1)', nRow).html(generateAccountHtml(aData)).addClass("showphonelist");
        	$('td:eq(8)', nRow).html(generateInquiryByHtml(aData));
			var single_weight = aData.singleWeight == null ? '0' : aData.singleWeight;
			//数量
			$('td:eq(10)', nRow).html("<input single_weight='" + single_weight + "' runit='" + aData.id + "' style='width:50px'/>");
			//件数
			$('td:eq(11)', nRow).html("<input single_weight='" + single_weight + "' rweight='" + aData.id + "' style='width:80px'/>");

        	$('td:eq(12)', nRow).html(generatePriceHtml(aData));
        	$('td:eq(13)', nRow).html(generatelinkHtml(aData));
        	
        	//每一列设置id,编辑保存后修改数据
        	$('td:eq(2)', nRow).attr("id","td_categoryName");
        	$('td:eq(3)', nRow).attr("id","td_materialName");
        	$('td:eq(4)', nRow).attr("id","td_spec");
        	$('td:eq(5)', nRow).attr("id","td_factoryName");
        	$('td:eq(6)', nRow).attr("id","td_cityName");
        	$('td:eq(7)', nRow).attr("id","td_warehouseName");
        },
        "drawCallback": function(e,t) {
        	$("#dynamic-table-single th:eq(8)").hide();
			$("#dynamic-table-single tr").each(function(){
				$(this).find("td:eq(8)").hide();
			});
        },
        columns: [
            {data: 'accountName'},
            {data: 'accountName'},
            {data: 'categoryName'},
            {data: 'materialName'},
            {data: 'spec'},
            {data: 'factoryName'},
            {data: 'cityName'},
            {data: 'warehouseName'},
            {data: 'lastUpdatedBy'},
			{data: 'singleWeight',render:function(data){
				return "<span singleweight>" + (data == null ? "" : data) + "</span>";
			}},
			{defaultContent: ''},
			{defaultContent: ''},
            {data: 'price'},
            {data: 'accountName'},
        ]
    });
}


function checkSearchConditions(){
	var result = true;
	$("div[id='m-searchDiv']").each(function(i){
		var specInput = $(this).find('input[id="spec"]')[0];
		var categoryInput = $(this).find('input[id="category"]')[0];
		var materialInput = $(this).find('input[id="material"]')[0];
		var factoryInput = $(this).find('input[id="factory"]')[0];
		var cityInput = $(this).find('input[id="city"]')[0];
		if($.trim($(specInput).val()) == "" && $.trim($(categoryInput).val()) == "" && $.trim($(materialInput).val()) == "" 
			&& $.trim($(factoryInput).val()) == "" && $.trim($(cityInput).val()) == "" && $.trim($(account).val()) == ""){
			cbms.gritter("查询条件不能为空");
			$(specInput).focus();
			result = false;
			return ;
		}
		if(!checkedSpec($(specInput).val())){
			result = false;
			return ;
		}
	});
	return result;
}

/**
 * 规格驶入区间是否合法
 * @param spec
 * @returns {Boolean}
 */
function checkedSpec(spec){
	var index = -1;
	if(spec.indexOf("-") != -1){
		var sepcs = spec.split("*");
		for(i=0;i<sepcs.length;i++){
			index = sepcs[i].indexOf("-");
			if(index != -1){
				prefixSpec = sepcs[i].substring(0,index);
				suffixSpec = sepcs[i].substring(index+1,sepcs[i].length);
				if(prefixSpec != "" && suffixSpec !=""){
					if(parseFloat(prefixSpec) > parseFloat(suffixSpec)){
						cbms.gritter(prefixSpec +"-" + suffixSpec +"规格条件输入错误");
						return false;
					}
				}
			}
		}
	}
	return true;
}

//加载查询条件多列,嵌套表格
function loadMultiTable(){
	if(!checkSearchConditions()){
		return;
	}
	$("#multiTable").css('display','');
	$("#singleTable").css('display','none').find("tbody").empty();
	var beginIndex;
	multiTable = $('#dynamic-table-multi').dataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "bLengthChange": false,
        "iDisplayLength" : 50,
        "aLengthMenu" : [10,30,50,100],//定义每页显示数据数量
        "ajax": {
            url: Context.PATH + '/smartmatch/quote/searchtmultibusinessresource.html',
            type: 'POST',
            data: function (d) {
            	var specs = "";
            	var materials = "";
            	var categorys = "";
            	var factorys = "";
            	var citys = "";
            	$("div[id='m-searchDiv']").find('input[id="spec"]').each(function(i){
            		specs+= $.trim($(this).val())+",";
            	});
            	$("div[id='m-searchDiv']").find('input[id="category"]').each(function(i){
            		categorys+= $.trim($(this).val())+",";
            	});
            	$("div[id='m-searchDiv']").find('input[id="material"]').each(function(i){
        		materials+= $.trim($(this).val())+",";
        	    });
            	$("div[id='m-searchDiv']").find('input[id="factory"]').each(function(i){
            		factorys+= $.trim($(this).val())+",";
            	});
            	$("div[id='m-searchDiv']").find('input[id="city"]').each(function(i){
            		citys+= $.trim($(this).val())+",";
            	});
            	d.specs = specs;
            	d.materials = materials;
            	d.categorys = categorys;
            	d.factorys = factorys;
            	d.citys = citys;
            	d.isTodayPrice = $("[id='todayPrice']")[0].checked;
            	d.accountName = $("#account").val();
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
        	$('td:eq(0)', nRow).html("");
        	$('td:eq(1)', nRow).html(generateAccountHtml(aData));
        	if(aData.totalWeight != ''){
        		$('td:eq(3)', nRow).html(aData.totalWeight.toFixed(3));
        	}
        	$('td:eq(4)', nRow).html(generateInquiryByHtml(aData));
        	$('td:eq(5)', nRow).html('');
        	$('td:eq(6)', nRow).html('');
        	$(nRow).attr("accountId",aData.accountId);
        },

        "fnPreDrawCallback": function (nTable) {
        	beginIndex = 1;
        	beginIndex += nTable._iDisplayStart;
        },
        fnDrawCallback: function (aaData) {
        	$("#dynamic-table-multi th:eq(4)").hide();
			$("#dynamic-table-multi tr").each(function(){
				$(this).find("td:eq(4)").hide();
			});
            $("#databody").find("tr[accountId]").each(function (index, obj) {
                var aData = aaData.aoData[index]._aData;
                var childTable = $("#subTable").clone().removeClass("none").removeAttr("id");//移除ID并显示
                loadChildTable(aData,childTable);
                var totalcolumns = aaData.aoColumns.length;
                var html = "<tr accountId=sub_"+aData.accountId+" ";
                if(index > 0){
                	html+=" class='none'>";
                }else{
                	html+=" class=''>";
                }
                html+="<td colspan='" + totalcolumns + "' class='childtables' >"+childTable.css("width","").prop("outerHTML")+"</td></tr>"
            	$(obj).after(html);
            });
        },
        columns: [
            {data: 'accountName'},
            {data: 'accountName'},
            {data: 'orgName'},
            {data: 'totalWeight'},
            {data: 'lastUpdatedBy'},
            {data: 'lastUpdatedBy'},
            {data: 'lastUpdatedBy'},
        ]
    });
}

//嵌套表格点击,隐藏,显示列
$(document).on("click", "#dynamic-table-multi tr[accountId]", function () {
    var accountId = $(this).attr("accountId");
    var subtr = $("#databody tr[accountId=sub_" + accountId + "]");//将属性一致的隐藏
    if (subtr.hasClass('none')) {
        subtr.removeClass('none');
    }
    else {
        subtr.addClass('none');
    }
});
$(document).on("mouseenter", ".showphonelist", function (e) {
    var elm = e.target,elmLeft = $(elm).position().left, 
        input = $(elm).closest("td").find('input[id="accountId"]'),
        pInput = $(elm).closest("td").find('.phonelist p');
    $(elm).closest("td").find(".phonelist").removeClass("none");
    //没有加载过联系人,提交请求到后台加载数据
    if(pInput.length <=0){
        var accountId = $(input[0]).val();
        $.ajax({
            url: Context.PATH + '/smartmatch/quote/serchcontact.html',
            type: "Get", 
            data : {
    			"accountId" : accountId
    		},
            success: function (result) {
                var i = 0, content = "";
                for(i=0;i<result.data.length;i++){
                	content += '<p>';
                	content += result.data[i].deptName;
            		content += "&nbsp"+ result.data[i].name+':';
            		content += result.data[i].tel;
            		content += "&nbsp"+ "QQ:";
            		content += (result.data[i].qq == null ? "" :result.data[i].qq);
            		content += "&nbsp"+ (result.data[i].traderName == null ? "" :result.data[i].traderName);
            		content += "&nbsp"+ (result.data[i].note == null ? "" :result.data[i].note);
                	content += '</p>';
            	}
            	$(elm).closest("td").find(".phonelist").empty().html(content);
            }
        });
    }

});
$(document).on("mouseout", ".showphonelist", function (e) {
    var elm = e.target;

    $(elm).closest("td").find(".phonelist").addClass("none");
});

//加载嵌套表格中的子表格
function loadChildTable(jsonData,childTable) {
	childTable.dataTable({
        bPaginate: false,  //不显示分页器
        bLengthChange: false, //不显示每页长度的选择条
        bProcessing: false, //不显示"正在处理"
        bInfo: false,
        bFilter: false,
        bSort: false,
        aaData: jsonData.childList,
        "fnRowCallback": function (nRow, aData, iDataIndex) {
        	if(aData.categoryName != null && aData.categoryName !=''){
                if(aData.showIndex ===''){
                     $('td:eq(0)', nRow).css("padding-left","30px").html('<input name="multi_checkbox" type="checkbox" val="'+aData.id+ '"/>'); 
                }else{
                   $('td:eq(0)', nRow).css({"position":"relative","padding-left":"30px"}).html('<span class="eq-i left0 top0 pos-abs bgNumber width30 height30 numIndex">'+aData.showIndex+'</span><input name="multi_checkbox" type="checkbox" val="'+aData.id+ '"/>');
                }
            	$('td:eq(11)', nRow).html(generatePriceHtml(aData));
            	$('td:eq(12)', nRow).html(generatelinkHtml_multi(aData));

				var single_weight = aData.singleWeight == null ? '0' : aData.singleWeight;
				//数量
				$('td:eq(9)', nRow).html("<input single_weight='" + single_weight + "' runit='" + aData.id + "' style='width:50px' />");
				//件数
				$('td:eq(10)', nRow).html("<input single_weight='" + single_weight + "' rweight='" + aData.id + "' style='width:80px' />");
        	}else{
        		$('td:eq(0)', nRow).css({"position":"relative","padding-left":"30px"}).html('<span class="eq-i left0 top0 pos-abs bgNumber width30 height30 numIndex">'+aData.showIndex+'</span>');
        		$('td:eq(12)', nRow).html('<a href="javascript:void(0)" class="add_resource" resourceId ='+aData.id+' account_id = '+aData.accountId+'>' + '添加'+'</a>');
        	}
        	//每一列设置id,编辑保存后修改数据
        	$('td:eq(1)', nRow).attr("id","td_categoryName");
        	$('td:eq(2)', nRow).attr("id","td_materialName");
        	$('td:eq(3)', nRow).attr("id","td_spec");
        	$('td:eq(4)', nRow).attr("id","td_factoryName");
        	$('td:eq(5)', nRow).attr("id","td_cityName");
        	$('td:eq(6)', nRow).attr("id","td_warehouseName");
        	$('td:eq(7)', nRow).attr("id","td_weightConcept");

        },       
        columns: [
            {data: 'categoryName'},
            {data: 'categoryName'},
            {data: 'materialName'},
            {data: 'spec'},
            {data: 'factoryName'},
            {data: 'cityName'},
            {data: 'warehouseName'},
            {data: 'weightConcept'},
			{data: 'singleWeight',render:function(data){
				return "<span singleweight>" + (data == null ? "" : data) + "</span>";
			}},
			{defaultContent: ''},
			{defaultContent: ''},
            {data: 'price'},
            {data: 'categoryName'},
        ]
    });
}

//搜索按钮
function serachList(){
	_condition_data=null;
	if(!checkSearchConditions()){
		return;
	}
	if ($("div[id='m-searchDiv']").length > 1) {
		$("#multiTable").css('display','');
		$("#singleTable").css('display','none');
		if(multiTable == null){
			loadMultiTable();
		}else{
			multiTable.fnDraw();
		}
	}else{
		$("#singleTable").css('display','');
		$("#multiTable").css('display','none');
		singleTable.fnDraw();
	}
}

//客户列显示内容
function generateAccountHtml(aData){
	var content = '<input type="hidden" id="accountId" value="';
	content+=aData.accountId +'"/>';
	if(1 ==aData.supplierLabel){
		content+=  '<span class=\"font-bai\">白</span>';
	}
	if(1==aData.payMentLaybel){
		content+=  '<span class=\"font-yu\">预</span>';
	}
	content+= '&nbsp; <i class="showphonelist">'+ aData.accountName +'</i>';
	content+= '&nbsp; <i class=\"fa fa-phone-square showphonelist fa-1x red\">&nbsp;</i>';
    content+= '<div class="phonelist"></div>'
	return content;
}

//上次询价列显示内容
function generateInquiryByHtml(aData){
	if(aData.lastUpdatedBy != null && aData.lastUpdatedBy !=''){
		var minutes = parseInt(aData.timeStr);
		if(minutes > 0){
			var time = parseInt(minutes/60);
			if(time > 0){
				var day = parseInt(time/24);
				if(day > 0){
					return aData.lastUpdatedBy + day + "天前询价"
				}
				return aData.lastUpdatedBy + time + "小时前询价"
			}
			return aData.lastUpdatedBy + minutes + "分钟前询价"
		}
	}
	return "";
}

function getPrice(input) {
	var closesttr = $(input).closest("tr");
	closesttr.find(".price_text").attr("verify","rmb");
	closesttr.find(".price_text").attr("must","1");
	var forms = setlistensSave("#search_form");
	if (!forms)return;
	var hisPrice = parseFloat($("input[name='price_']").val());
	var cuPrice = parseFloat($(input).val());
	if (hisPrice == cuPrice) {
		return;
	} else {
		var resourceDateList = {
				"id" : closesttr.find("#id").val(), 
				"spec" : closesttr.find("#spec_s").val(),
				"price" : cuPrice//金额
		 }
		$.ajax({
			type: 'post',
			url: Context.PATH + '/smartmatch/quote/saveresource.html',
			data: {
				"resourceDateList" : JSON.stringify(resourceDateList)
				
			},
			success: function (result) {
				if (result && result.success) {
        			closesttr.find('input[id="id"]').eq(0).val(result.data.id);
        			closesttr.find('a[id="doModify"]').eq(0).attr('resourceId',result.data.id);
                } else {
                    cbms.alert(result.data);
                }
			}
	 	});
	}
}

//单价列显示内容
function generatePriceHtml(aData){
	var content ="";
	var price = aData.price == null ? "" : aData.price;
	//modify by zhoucai@prcsteel.com ,2016-8-24 如果价格为99999，则显示为议价
	if(price==99999){
		price='议价';
	}
	var content = '<input type="text" id="priceText" class="price_text" name="price_ext_" onblur="getPrice(this);" value="';
	content+=price +'"/>';
	content+='<input type="hidden" id="price" name="price_" value="';
	content+=price +'"/>';
	content+='<input type="hidden" id="id"  value="';
	content+=aData.id +'"/>';
	content+='<input type="hidden" id="spec_s"  value="';
	content+=aData.spec +'"/>';
	content+='<input id="categoryUuid" type="hidden" value="';
	content+=aData.categoryUuid +'"/>';
	return content;
}


//单表格操作列内容
function generatelinkHtml(aData){
	var content ='<a id="doModify" href="javascript:void(0)" class="update_resource" resourceId ='+aData.id+' warehouseName = '+aData.warehouseName+' account_id = '+aData.accountId+'>' + '修改'+'</a>';
	content+='<a href="javascript:void(0);" option="createorder" url="'+_orderDomain +'/order/'+aData.id+'/businessquotation/create.html" rid="'+aData.id+'">' + '开单'+'</a>';
	return content;
}

//多表格操作列内容
function generatelinkHtml_multi(aData){
	var content ='<a href="javascript:void(0)" class="add_resource" resourceId ='+aData.id+' account_id = '+aData.accountId+' warehouseName = '+aData.warehouseName+'>' + '添加'+'</a>&nbsp;&nbsp;&nbsp;&nbsp;';
	content +='<a id="doModify" href="javascript:void(0)" class="update_resource" resourceId ='+aData.id+' warehouseName = '+aData.warehouseName+' account_id = '+aData.accountId+'>' + '修改'+'</a>';
	return content;
}

function loadPriceLimit(uuid,input){
	for(var i=0;i<limitPrices.length;i++){
		if(limitPrices[i].uuid==uuid){
			input.attr("min", limitPrices[i].priceMin).attr("max", limitPrices[i].priceMax);
			return;
		}
	}
	$.ajax({
		type: 'post',
		url: Context.PATH + "/resource/getpricelimit.html",
		data: {uuid: uuid},
		success: function (result) {
			if (result.success) {
				limitPrices.push(result.data);
				if (input != null) {
					input.attr("min", result.data.priceMin).attr("max", result.data.priceMax);
				}
			}
		}
	});
}


//修改操作
$(document).on("click", ".update_resource", function ()  { 
	var resourceId = $(this).attr("resourceId");
	var accountId = $(this).attr("account_id");
	var warehouseName = $(this).attr("warehouseName");
	var selectTr = $(this).closest("tr");
	$.ajax({
        url: Context.PATH + '/smartmatch/quote/querybyresourceId.html',
        type: "POST", 
        data : {
			"resourceId" : resourceId
		},
        success: function (result) {
        	var html = $('#determine2').html();
    		cbms.getDialog("修改", html);
        	$("#category").val(result.data.categoryName);
        	$("#material").val(result.data.materialName);
        	$("#spec_dia").val(result.data.spec);
        	$("#factory").val(result.data.factoryName);
        	$("#city").val(result.data.cityName);
        	$("#weightConcept").val(result.data.weightConcept);
        	$("#price").val(result.data.price);
        	$("#warehouse").val(result.data.warehouseName);
        	loadPriceLimit(result.data.categoryUuid,$("#price"));
    		
    		$(document).on("click", "#btnClose",function () {
    	       cbms.closeDialog();
    	    });
    		 
    		$(document).off('click', '#btncommit');
    		
    		$(document).on('click', '#btncommit', function() {
    			var forms = setlistensSave("#form-horizontal");
    			if (!forms)return;
    			var resourceDateList = {
    					"id" : resourceId, 
    					"accountId" : accountId,
    					"categoryUuid" : $("#category").attr("val"),
    					"categoryName": $("#category").val(),//品名
    					"materialUuid" : $("#material").attr("val"),
    					"materialName": $("#material").val(),//材质
    					"spec" : $("#spec_dia").val(),
    					"factoryName": $("#factory").val(),//厂家
    					"cityName" : $("#city").val(),//地区
    					"weightConcept" : $("#weightConcept").val(),//计重方式
    					"price" : $("#price").val(),//金额
    					"factoryId" : $("#factory").attr("val"),
    					"warehouseName":$("#warehouse").val()
    			}
    			var pricemin=$("#price").attr("min");
    			var pricemax=$("#price").attr("max");
    			if (pricemin == '' || pricemax == '' || isNaN(pricemin) || isNaN(pricemax)) {
    				cbms.alert("未查询到参考价范围");
    				return false;
    			} else if(parseFloat(resourceDateList.price)<parseFloat(pricemin)||parseFloat(resourceDateList.price)>parseFloat(pricemax)){
    				if(!cbms.alert("修改后的价格不在该品种参考价范围内!")){
    					return false;
    				}
    			}
    			$.ajax({
    				type: 'post',
    				url: Context.PATH + '/smartmatch/quote/saveresource.html',
    				data: {
    					"resourceDateList" : JSON.stringify(resourceDateList)
    					
    				},
    				success: function (result) {
    					if (result && result.success) {
        					cbms.closeDialog();
	            			selectTr.find('td[id="td_categoryName"]').eq(0).html(resourceDateList.categoryName);
	            			selectTr.find('td[id="td_materialName"]').eq(0).html(resourceDateList.materialName);
	            			selectTr.find('td[id="td_spec"]').eq(0).html(resourceDateList.spec);
	            			selectTr.find('td[id="td_factoryName"]').eq(0).html(resourceDateList.factoryName);
	            			selectTr.find('td[id="td_cityName"]').eq(0).html(resourceDateList.cityName);
	            			selectTr.find('td[id="td_warehouseName"]').eq(0).html(resourceDateList.warehouseName);
	            			selectTr.find('td[id="td_weightConcept"]').eq(0).html(resourceDateList.weightConcept);
							selectTr.find("span[singleweight]").html(result.data.singleWeight);
							selectTr.find("input[runit]").attr("single_weight",result.data.singleWeight);
							selectTr.find("input[rweight]").attr("single_weight",result.data.singleWeight);
	            			selectTr.find('input[id="priceText"]').eq(0).val(resourceDateList.price);
	            			selectTr.find('input[id="id"]').eq(0).val(result.data.id);
	            			selectTr.find('a[id="doModify"]').eq(0).attr('resourceId',result.data.id);
    	                } else {
    	                    cbms.alert(result.data);
    	                }
    				}
    		 	});
    		});
        }
    });
});


//新增操作
$(document).on("click", ".add_resource", function ()  { 
	var resourceId = $(this).attr("resourceId");
	var accountId = $(this).attr("account_id");
	var accountName_add = $("tr[accountid='"+accountId+"']").find(".showphonelist").html();
	var warehouseName = $(this).attr("warehouseName");

	var html = $('#determine').html();
	var currentTr = $(this).closest("tr");
	cbms.getDialog("新增", html);
	$(document).on("click", "#btnClose",function () {
       cbms.closeDialog();
    });
	 
	$(document).off('click', '#btncommit');
	
	$(document).on('click', '#btncommit', function() {
		var forms = setlistensSave("#form-horizontal");
		if (!forms)return;
		
		var resourceDateList = {
				"id" : resourceId, 
				"accountId" : accountId,
				"accountName":accountName_add,
				"categoryUuid" : $("#category").attr("val") == "null" ? "" : $("#category").attr("val"),
				"categoryName": $("#category").val(),//品名
				"materialUuid" : $("#material").attr("val") == "null" ? "" : $("#material").attr("val"),
				"materialName": $("#material").val(),//材质
				"spec" : $("#spec_dia").val(),
				"factoryName": $("#factory").val(),//厂家
				"cityName" : $("#city").val(),//地区
				"weightConcept" : $("#weightConcept").val(),//计重方式
				"price" : $("#price").val(),//金额
				"factoryId" : $("#factory").attr("val"),
				"warehouseName":$("#warehouse").val()
		}
		var pricemin=$("#price").attr("min");
		var pricemax=$("#price").attr("max");
		if (pricemin == '' || pricemax == '' || isNaN(pricemin) || isNaN(pricemax)) {
			cbms.alert("未查询到参考价范围");
			return false;
		} else if(parseFloat(resourceDateList.price)<parseFloat(pricemin)||parseFloat(resourceDateList.price)>parseFloat(pricemax)){
			if(!cbms.alert("修改后的价格不在该品种参考价范围内!")){
				return false;
			}
		}
		$.ajax({
			type: 'post',
			url: Context.PATH + '/smartmatch/quote/saveresource.html',
			data: {
				"resourceDateList" : JSON.stringify(resourceDateList)
			},
			success: function (result) {
				if (result && result.success) {
					cbms.closeDialog();
                	cbms.alert("提交成功！", function() {
                		var content ='<tr role="row" class="even">'
                			content+='<td style="padding-left: 30px;">';
                			content+='<input name="multi_checkbox" type="checkbox" val="'+result.data.id+'"></td>';
                			content+='<td id="td_categoryName">'+resourceDateList.categoryName+'</td><td id="td_materialName">'+resourceDateList.materialName+'</td>';
                			content+='<td id="td_spec">'+resourceDateList.spec+'</td>';
                			content+='<td id="td_factoryName">'+resourceDateList.factoryName+'</td>';
                			content+='<td id="td_cityName">'+resourceDateList.cityName+'</td>';
                			content+='<td id="td_warehouseName">'+resourceDateList.warehouseName+'</td>';
                			content+='<td id="td_weightConcept">'+resourceDateList.weightConcept+'</td>';
							content+='<td><span singleweight>' + (result.data.singleWeight==null?'':result.data.singleWeight) + '</span></td>';
							content+='<td><input single_weight="' + result.data.singleWeight + '" runit="' + result.data.id + '" style="width:50px"/></td>';
							content+='<td><input single_weight="' + result.data.singleWeight + '" rweight="' + result.data.id + '" style="width:80px"/></td>';
                			content+='<td><input type="text" id="priceText" class="price_text" name="price_ext_" onblur="getPrice(this);" value="'+resourceDateList.price+'">';
                			content+='<input type="hidden" id="price" name="price_" value="'+resourceDateList.price+'">';
                			content+='<input type="hidden" id="id" value="'+result.data.id+'">';
                			content+='<input type="hidden" id="spec_s" value="16*12"></td>';
                			content+='<td><a href="javascript:void(0)" class="add_resource" resourceid="'+result.data.id+'" account_id="'+result.data.accountId+'" warehousename="'+result.data.warehouseName+'">添加</a>';
                			content+='&nbsp;&nbsp;&nbsp;&nbsp;<a id="doModify" href="javascript:void(0)" class="update_resource" resourceid="'+result.data.id+'" warehousename="'+result.data.warehouseName+'" account_id="'+result.data.accountId+'">修改</a> ';
                			content+='</td></tr>';
                			currentTr.after(content);
        			});
                } else {
                    cbms.alert(result.data);
                }
			}
	 	});
	});
});

//开单
$(document).on("click", "#singleTable a[option=createorder],#multiTable a[option=createorder]", function () {
	var url = $(this).attr("url");
	var cur_row=$(this).closest("tr");
	var cur_unit=cur_row.find("input[runit]").val();
	var cur_weight=cur_row.find("input[rweight]").val();
	if(isNaN(cur_unit)) {
		cur_unit = '';
	}
	if (isNaN(cur_weight)) {
		cur_weight = '';
	}
	var obj={
		rid: $(this).attr("rid"),
		quality: cur_unit,
		weight: cur_weight
	}
	var list=[];
	list.push(obj);
	postAndRedirect(url, list);
});

//数量填写
$(document).on("input", "#singleTable input[runit],#multiTable input[runit]", function () {
	var single_weight = $(this).attr("single_weight");
	var unit = $(this).val();
	if (unit != '' && !isNaN(unit) && single_weight != '' && single_weight != 0 && !isNaN(single_weight)) {
		var weight_val = (parseFloat(unit) * parseFloat(single_weight)).toFixed(3);
		$(this).closest("tr").find("input[rweight]").val(weight_val);
	}
});
//重量填写
$(document).on("input", "#singleTable input[rweight],#multiTable input[rweight]", function () {
	var single_weight = $(this).attr("single_weight");
	var weight = $(this).val();
	if (weight != '' && !isNaN(weight) && single_weight != '' && single_weight != 0 && !isNaN(single_weight)) {
		var unit_val = Math.floor(parseFloat(weight) / parseFloat(single_weight));
		$(this).closest("tr").find("input[runit]").val(unit_val);
	}
});
//重量填写
$(document).on("blur", "#singleTable input[rweight],#multiTable input[rweight]", function () {
	var single_weight = $(this).attr("single_weight");
	var weight = $(this).val();
	if (weight != '' && !isNaN(weight) && single_weight != '' && single_weight != 0 && !isNaN(single_weight)) {
		var unit_val = Math.floor(parseFloat(weight) / parseFloat(single_weight));
		$(this).closest("tr").find("input[runit]").val(unit_val);
	}
	var unit = $(this).closest("tr").find("input[runit]").val();
	if (unit != '' && !isNaN(unit) && single_weight != '' && single_weight != 0 && !isNaN(single_weight)) {
		var weight_val = (parseFloat(unit) * parseFloat(single_weight)).toFixed(3);
		$(this).val(weight_val);
	}
});

//post跳转
function postAndRedirect(url, obj) {
	/*var body = $(document.body),
		form = $("<form method='post' type='hidden'></form>"),
		input;
	form.attr({"action": url});
	input = $("<input type='hidden' name='info'>");
	input.val(JSON.stringify(obj));
	form.append(input);

	form.appendTo(document.body);
	form.submit();
	document.body.removeChild(form[0]);*/
	//modify by caosulin 现在post方式跳转开单页面，第一次跳转时，会被shiro重定向到index页面，
	//所以先改成url后面直接跟参数的方式跳转。
	var infoStr = JSON.stringify(obj);
	window.location.href=url+"?info="+infoStr;
}

function openOrder(){
	//获取选中的资源的ID
	if(_isSelectedRows()){
		var ids = _resourceData.selectedIds.join("_");
		var url = _orderDomain + '/order/'+ids+'/businessquotation/create.html';

		var list=[];
		var temp_rid=0;
		//多条件查询
		var tablename = $("div[id='m-searchDiv']").length > 1 ? "multiTable" : "singleTable";
		var checkbox_name = (tablename == "multiTable") ? "multi_checkbox" : "single_checkbox";
		$("#" + tablename + " input[type='checkbox'][name='"+checkbox_name+"']:checked").each(function () {
			temp_rid = $(this).attr('val');
			var cur_unit = $(this).closest("tr").find("input[runit]").val();
			var cur_weight = $(this).closest("tr").find("input[rweight]").val();
			if (isNaN(cur_unit)) {
				cur_unit = '';
			}
			if (isNaN(cur_weight)) {
				cur_weight = '';
			}
			if (cur_unit == '' && cur_weight == '') {
				return true;
			}
			var obj = {
				rid: temp_rid,
				quality: cur_unit,
				weight: cur_weight
			}
			list.push(obj);
		});

		postAndRedirect(url, list);

		//清除当前的选中
		_resourceData.selectedIds=[];
	}
}
