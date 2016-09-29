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
	accountCacheData:[],
	//基价
	basePriceCacheData:[],
	//品名
	categoryCacheData:[],
	//钢厂
	factoryCacheData:[],
	
};

var accountInfo=[];
var accountSearchTable=null;
/**
 * 获取一些通用的全局数据：所有卖家
 */
(function getCommonData(){
	$.ajax({
        type: 'post',
        url: Context.PATH + "/smartmatch/basePriceRelation/getCommonData.html",
        success: function (result) {
        	if(result.success){
        		_resAttr.accountCacheData=result.data.accountList;
        		_resAttr.basePriceCacheData=result.data.basePriceList;
        		_resAttr.categoryCacheData=result.data.categoryList;
        		_resAttr.factoryCacheData=result.data.factoryList;
        		
        	}
        }
    });
})();
// 卖家联想
$(document).on('input propertychange','#accountName_form', function () {
    showPYMatchList($(this),_resAttr.accountCacheData,"id","name",checkBasePrice);
    $("#dropdown").css("z-index",9999);
});

//查询卖家联想
$(document).on('input propertychange','#s_accountName', function () {
    showPYMatchList($(this),_resAttr.accountCacheData,"id","name",null);
    $("#dropdown").css("z-index",9999);
});

//查询基价联想
$(document).on('input propertychange','#s_basepriceName', function () {
    showPYMatchList($(this),_resAttr.basePriceCacheData,"id","basePriceName",null);
    $("#dropdown").css("z-index",9999);
});

//查询品名联想
$(document).on('input propertychange','#s_categoryName', function () {
    showPYMatchList($(this),_resAttr.categoryCacheData,"id","name",null);
    $("#dropdown").css("z-index",9999);
});

//查询工厂联想
$(document).on('input propertychange','#s_factoryName', function () {
    showPYMatchList($(this),_resAttr.factoryCacheData,"id","name",null);
    $("#dropdown").css("z-index",9999);
});

var baseTable;
//查询
function doSearchList(){
	baseTable.fnDraw();
}

//清空
$(document).on("click", "#cleanList",function () {
	$(this).closest(".form-inline").find('input').val('');
	$("#s_cityName").val(-1);
});

$(document).ready(function () {
	loadTable();
});

//加载表格
function loadTable(){
	var beginIndex;
	baseTable = $('#dynamic-table').dataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "bLengthChange": false,
        "iDisplayLength" : 30,
        "aLengthMenu" : [10,30,50,100],//定义每页显示数据数量
        "ajax": {
            url: Context.PATH + '/smartmatch/basePriceRelation/searchbasepricerelation.html',
            type: 'POST',
            data: function (d) {
        	   	d.accountName = $("#s_accountName").val();
            	d.basePriceName = $("#s_basepriceName").val();
            	if($("#s_cityName").val() == "-1"){
            		d.cityName = "";
            	}else{
            		d.cityName = $("#s_cityName").find("option:selected").text();
            	}
            	d.categoryName = $("#s_categoryName").val();
            	d.factoryName = $("#s_factoryName").val();
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
        	$('td:eq(0)', nRow).html(beginIndex+iDataIndex);
        	$('td:eq(7)', nRow).html((aData.isEnable == 1?"启用":"禁用"));
        	$('td:eq(8)', nRow).html(generatelinkHtml(aData));

        },
        "fnPreDrawCallback": function (nTable) {
        	beginIndex = 1;
        	beginIndex += nTable._iDisplayStart;
        },
        columns: [
            {data: 'accountName'},
            {data: 'accountName'},
            {data: 'basePriceName'},
            {data: 'cityName'},
            {data: 'categoryName'},
            {data: 'factoryName'},
            {data: 'baseRelationDetailCount'},
            {data: 'isEnable'},
            {data: 'accountName'}
        ]
    });
}
//单表格操作列内容
function generatelinkHtml(aData){
	var content ='<a href="javascript:void(0)" id="modifyBasePrice" val="'+aData.id+'" class="update_resource" >' + '修改'+'</a>';
	content+='&nbsp;&nbsp<a href="javascript:void(0)" id="deleteBasePrice" val="'+aData.id+'"   class="update_resource" >' + '删除'+'</a>';;
	return content;
}

function checkBasePrice(){
	addBasePrice(null);
}
//材质联想显示
var _materialsCache = new Array();
$(document).on('input propertychange',"input[search='material_form']", function (e) {
	//先判断3个必填项是否已经填了
	var accountId = $("#accountName_form").attr("val");
	var cityId = $("#cityName").val();
	var basePriceId = $("#basepriceName_form").attr("val");
    var flag = checkInput(accountId,cityId,basePriceId);
    if(!flag){
    	return;
    }
	
	var input = this;
	var categoryUuid = $.trim($("#category_name_view").attr("val"));
	
	//查看是否有缓存数据
	var data = null;
	for(var i = 0;i<_materialsCache.length;i++){
		if(categoryUuid == _materialsCache[i].key){
			data = _materialsCache[i].data;
		}
	}
	if(data != null){
		showPYMatchList($(input),data,"uuid","name",addMaterialId);
		cbms.stopF(e);
		return;
	}
	
	$.ajax({
		type: "POST",
		url: Context.PATH+ '/smartmatch/basePriceRelation/queryMaterials.html',
		dataType: "json",
		data:{categoryUuid:categoryUuid},
		success: function (response, textStatus, xhr) {
			if(response.data.length > 0){
				var _data = {
						key:categoryUuid,
						data:response.data
				};
				_materialsCache.push(_data);
				showPYMatchList($(input),response.data,"uuid","name",addMaterialId);
				cbms.stopF(e);
			}
		}
	});
});

//基价联想显示
var _basepriceCache = new Array();
$(document).on('input propertychange',"input[id='basepriceName_form']", function (e) {
	//先判断2个必填项是否已经填了
	var cityId = $("#cityName").val();
	if(cityId == 'null' || cityId == ''){
		cbms.alert('请先选择一个地区！'); 
		return;
	}
	
	var input = this;
	var data = null;
	//查看是否有缓存数据
	for(var i = 0;i<_basepriceCache.length;i++){
		if(cityId == _basepriceCache[i].cityId){
			data = _basepriceCache[i].data;
		}
	}
	
	if(data != null){
		showPYMatchList($(input),data,"id","basePriceName",addBasePrice);
		cbms.stopF(e);
		return;
	}
	//查询后台
	$.ajax({
		type: "POST",
		url: Context.PATH+ '/smartmatch/basePriceRelation/queryBasePrice.html',
		dataType: "json",
		data:{cityId:cityId},
		success: function (response, textStatus, xhr) {
			if(response.data.length > 0){
				var _basepriceData = {
						cityId:cityId,
						data:response.data
				};
				_basepriceCache.push(_basepriceData);
				showPYMatchList($(input),response.data,"id","basePriceName",addBasePrice);
				cbms.stopF(e);
			}
		}
	});
});



function addBasePrice(selectInput){
	if(selectInput != null){
		var categoryName = $(selectInput).attr("category");
		var categoryUuid = $(selectInput).attr("categoryUuid");
		var	factoryId = $(selectInput).attr("factoryId");
		var factoryName = $(selectInput).attr("factory");
		
		var oldCategoryName = $("#category_name_view").attr("val");
		if(oldCategoryName != categoryUuid){ //如果基价的品名变化了，则材质清空
			$("input[id=material_form]").val("");
		}
		
		$("#category_name").val(categoryName);
		$("#category_name_view").html(categoryName);
		$("#category_name_view").attr("val",categoryUuid);
		$("#factory_name_view").html(factoryName);
		$("#factory_name_view").attr("val",factoryId);
	}
	
	//获取卖家，地区，基价名称,是否启用
	var accountId = $("#accountName_form").attr("val");
	var accountName =  $("#accountName_form").val();
	var cityId = $("#cityName").val();
	var cityName = $("#cityName").find("option:selected").text();
	var baseprice =  $("#basepriceName_form").attr("val");

	if(typeof(accountId) == "undefined" || accountId==""){
		return ;
	}
	if(cityId == 'null' || cityId == ''){
		return ;
	}
	if(typeof(baseprice) == "undefined" || baseprice == 'null' || baseprice == ''){
		return ;
	}
	
	$.ajax({
		type: "POST",
		url: Context.PATH+ '/smartmatch/basePriceRelation/checkBasePrice.html',
		dataType: "json",
		data:{cityId:cityId,accountId:accountId,basePriceId:baseprice},
        success: function(data){
			if(data.success){
				cbms.alert("当前卖家与基价已经设置！");
			}
			
        } 
	});
}

function checkInput(accountId,cityId,baseprice){
	if(typeof(accountId) == "undefined" || accountId==""){
		cbms.alert('请先选择一个卖家！'); 
		return false;
	}
	if(cityId == 'null' || cityId == ''){
		cbms.alert('请先选择一个地区！'); 
		return false;
	}
	if(typeof(baseprice) == "undefined" || baseprice == 'null' || baseprice == ''){
		cbms.alert('请先选择一个基价！'); 
		return false;
	}
	
	return true;
}
//选择城市
$(document).on('change',"#cityName", function (e) {
	if(accountSearchTable != null){
		accountSearchTable.ajax.reload();
	}
	
});

function addMaterialId(value){
	$("#materialId_form").val(value);
	var m_specs = new Array();
	var details = $(".d-dialog").find("#basepriceinfos").find("div[id='m-basepriceDiv']");
	for(var i=0;i<details.length;i++){
		var materialName = $(details[i]).find("#material_form").val();
		var spec = $(details[i]).find("input[search=spec_from]").val();

		var mater_spec = materialName+"_"+spec;
		for(var j=0;j<m_specs.length;j++){
			var m_spec = m_specs[j];
			if(m_spec == mater_spec){
				cbms.alert("同一材质 "+materialName+" 规格"+spec+"不能重复 !");
				return;
			}
		}
		m_specs.push(mater_spec);
	}
	
}

//规格判断重复
$(document).on('blur',"input[search='spec_from']", function (e) {
	 checkSpec($(this),0);
});

function checkSpec(input,type){
	var materalUuid = null;
	var inputData = null;
	if(type == 0){
		materalUuid = $(input).closest("#m-basepriceDiv").find("#material_form").attr("val");
		inputData = $(input).val();
	}else if((type == 1)){
		materalUuid = $(input).val();
		inputData = $(input).closest("#m-basepriceDiv").find("input[search=spec_from]").val();
	}
	
	var details = $("#basepriceinfos").find("div[id='m-basepriceDiv']");
	var count = 0;
	for(var i=0;i<details.length;i++){
		var uuid = $(details[i]).find("#material_form").attr("val");
		var spec = $(details[i]).find("input[search=spec_from]").val();
		if(inputData == spec && materalUuid == uuid){
			count++;
		}
	}
	if(count > 1){
		$(input).val("");
		cbms.alert("同一个材质规格不能重复！");
	}
}

$(document).ready(function() {
	
	$('#accountName_form').inputFocus();

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

	$(document).on("click", "#addSub", function ()  { 
		
		//updateBasepriceRelation(1);
		var html = $('#determine').html();
		cbms.getDialog("添加", html);
		
		$(document).on("click", "#btnClose",function () {
	       cbms.closeDialog();
	    });
		 
		$(document).off('click', '#btncommit');
			
		$(document).on('click', '#btncommit', function() {
			
		});
	});
	
	$(document).on("click", "#deleteBasePrice", function ()  { 
		var id = $(this).attr("val");
		cbms.confirm("您确定删除选中的数据吗?",null,function(){
			deleteBasePrice(id);
		});
		
	});
	
	$(document).on("click", "#modifyBasePrice", function ()  { 
		var id = $(this).attr("val");
		modifyBasePrice(id);
		
	});
	
	function deleteBasePrice(id){
		$.ajax({
	        type: 'post',
	        url: Context.PATH + "/smartmatch/basePriceRelation/deleteBasePriceRelation.html",
	        data : {id:id},
	        success: function (result) {
	        	if(result.success){
					cbms.alert('删除成功!');
					doSearchList();
	        	}
	        }
	    });
	}
	function modifyBasePrice(id){ 
		var html = $('#determine').html();
		cbms.getDialog("修改", html);
		initUpdateDialog(id);
		
		$(document).on("click", "#btnClose",function () {
	       cbms.closeDialog();
	    });
		 
		$(document).off('click', '#btncommit');
			
		$(document).on('click', '#btncommit', function() {
			
		});
	}
	
	function initUpdateDialog(id){
		$.ajax({
	        type: 'post',
	        url: Context.PATH + "/smartmatch/basePriceRelation/getBasePriceRelation.html",
	        data : {id:id},
	        success: function (result) {
	        	if(result.success){
					var data = result.data;
					generateDialog(data);
	        	}
	        }
	    });
	}
	
	function generateDialog(data){
		//卖家
		$("#accountName_form").attr("readonly","readonly");
		$("#accountName_form").val(data.accountName);
		$("#accountName_form").attr("val",data.accountId);
		$("#bpr_id").val(data.id);
		//地区
		$("#cityName").attr("disabled","disabled");
		$("#cityName").find("option[value='"+data.cityId+"']").attr("selected",true);
		
		//基价
		$("#basepriceName_form").attr("readonly","readonly");
		$("#basepriceName_form").val(data.basePriceName);
		$("#basepriceName_form").attr("val",data.basePriceId);
		
		//品名和钢厂
		$("#category_name_view").html(data.categoryName);
		$("#category_name_view").attr("val",data.categoryUuid);
		$("#factory_name_view").html(data.factoryName);
		$("#factory_name_view").attr("val",data.factoryId);
		//是否启用
		$("#enable_form").find("option[value='"+data.isEnable+"']").attr("selected",true);
		
		var firstRow= $(".d-dialog").find("div[id='m-basepriceDiv']");
		//初始化明细
		var items = data.details;
		if(jQuery.isArray(items)){
			var _row = null;
			for(var i = 0 ; i < items.length; i++){
				if(i == 0){//第一行
					initItems(firstRow,items[i]);
					_row = firstRow;
				}else{
					var c = $(_row).clone();
					initItems(c,items[i]);
					$(_row).after(c);
					_row = c;
				}
				
			}
		}		
	}
	
	function initItems(input,item){
		$(input).find("#material_form").val(item.materialName);
		$(input).find("#material_form").attr("val",item.materialUuid);
		$(input).find("input[search=spec_from]").val(item.spec);
		$(input).find("input[search=priceDeviation]").val(item.priceDeviation);
		$(input).find("input[id=detail_id]").val(item.id);
		
		var _div = $(input).find("label[id=isDeficiencyInventory]");
		var html = null;
		if(item.isDeficiencyInventory){
			html = '<input type="checkbox" id="isDeficiencyInventory" checked="checked"/>';
		}else{
			html = '<input type="checkbox" id="isDeficiencyInventory"/>';
		}
		_div.html(html);
	}	//删除
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
	
	function checkPrice(nubmer) {
	   var re = /^[\+\-]?\d+(\.\d+)?$/;//判断字符串是否为数值
	　 　if (!re.test(nubmer)) {
	　 　　　return false;
	　 　}
		return true;
	}

	/***** 资源保存****/
	$(document).on("click", "#commit_btn", function(event) {
		
		//获取卖家，地区，基价名称,是否启用
		var accountId = $("#accountName_form").attr("val");
		var accountName =  $("#accountName_form").val();
		var cityId = $("#cityName").val();
		var cityName = $("#cityName").find("option:selected").text();
		var baseprice =  $("#basepriceName_form").attr("val");
		var basepriceName =  $("#basepriceName_form").val();
		
		if(accountId == ''&&accountName != ''){
			cbms.alert("没有找到卖家的值，请重新选择卖家！");
			return ;
		}
		if(baseprice == ''&&basepriceName != ''){
			cbms.alert("没有找到基价的值，请重新选择基价！");
			return ;
		}
		//校验必填项
		var flag = checkInput(accountId,cityId,baseprice);
		if(!flag)return;
		
		var enable =  $("#enable_form").val();
		var categoryUuid = $("#category_name_view").attr("val");
		var categoryName = $("#category_name_view").html();
		var factoryId = $("#factory_name_view").attr("val");
		var factoryName = $("#factory_name_view").html();	
		//获取填入的所有的基价明细
		var items = [];
		var m_specs = new Array();
		var details = $(".d-dialog").find("#basepriceinfos").find("div[id='m-basepriceDiv']");
		for(var i=0;i<details.length;i++){
			var materialUuid = $(details[i]).find("#material_form").attr("val");
			var materialName = $(details[i]).find("#material_form").val();
			var spec = $(details[i]).find("input[search=spec_from]").val();
			var priceDeviation = $(details[i]).find("#priceDeviation").val();
			var isDeficiencyInventory = $(details[i]).find('input[id=isDeficiencyInventory]').is(':checked');
			
			//校验下列的输入框
			if(materialName == null || materialName == ''){
				cbms.alert("材质不能为空!");
				return;
			}
			
			if(spec == null || spec == ''){
				cbms.alert("规格不能为空!");
				return;
			}
			
			if(typeof(materialUuid) == "undefined" || materialUuid==""){
				cbms.alert("输入的材质: "+materialName+"不正确!");
				return;
			}
			
			if(!checkPrice(priceDeviation)){
				cbms.alert("输入的单价偏差:"+priceDeviation+"不正确 请输入数字!");
				return;
			}
			
			var mater_spec = materialName+"_"+spec;
			for(var j=0;j<m_specs.length;j++){
				var m_spec = m_specs[j];
				if(m_spec == mater_spec){
					cbms.alert("同一材质 "+materialName+" 规格"+spec+"不能重复 !");
					return;
				}
			}
			
			m_specs.push(mater_spec);
			var item = {
				materialName:materialName,
				materialUuid:materialUuid,
				spec:spec,
				priceDeviation:priceDeviation,
				isDeficiencyInventory:isDeficiencyInventory
			};
			items.push(item);
		
		}
			
		//判断新增还是修改
		var data = null;
		var _id = $("#bpr_id").val();
		if(_id == ''){//新增
			data={
					basePriceId:baseprice,
					basePriceName:basepriceName,
					accountId:accountId,
					accountName:accountName,
					cityId:cityId,
					cityName:cityName,
					categoryUuid:categoryUuid,
					categoryName:categoryName,
					factoryId:factoryId,
					factoryName:factoryName,
					isEnable:enable,
					items:JSON.stringify(items)
				};
		}else{//修改
			data={
				id:_id,
				isEnable:enable,
				items:JSON.stringify(items)
			};
		}

		var url = Context.PATH + "/smartmatch/basePriceRelation/saveBasePriceRelation.html";
		//提交按钮不可用，防止重复提交
		$("#commit_btn").attr("disabled","disabled");
		//提交
		$.ajax({  
            type: 'post',  
            url: url, 
            data:data,
            dataType:"json",
            success: function(data){
				if(data.success){
					cbms.closeDialog();
				}
				cbms.alert(data.data);
				//提交按钮重新可用
				$("#commit_btn").removeAttr("disabled");
				doSearchList();
				
            } 
        });
		return false;
	});

	$(document).on("click","#baseprice_search",function(){
		//修改时，禁用当前按钮
		var bprId = $("#bpr_id").val();
		if(bprId != ''){
			return ;
		}
		$("#table_baseprice").html("");//清空基价列表
		//创建一个新的基价表格
		var html = '<table id="baseprice_list_table" class="table table-striped table-bordered table-hover">';
		html += '<thead><tr>';
		html += '<th style="width:25px"></th>';
		html += '<th style="width:150px">基价名称</th>';
		html += '<th style="width:100px">品名</th>';
		//html += '<th style="width:100px;display:n">钢厂</th>';
		html += '</tr> </thead></table>';
    
		$("#table_baseprice").html(html);
		initBasepriceSearch();
		$("#baseprice_search_panel").removeClass("none");
	});
	//选择基价取消
	$(document).on("click","#cancel_buyerselect",function(){
		$("#baseprice_search_panel").addClass("none");
	});
	//选择基价确定
	$(document).on("click","#basepriceSelected",function(){
		var cbbuyer=$("#baseprice_list_table input[type=radio][name=baseprice_select]:checked");
		if(cbbuyer.length>0){
			var basepriceName=cbbuyer.closest("tr").find("td").eq(1).text();
			var baseprice_id=cbbuyer.attr("aid");
			$("#basepriceName_form").attr("val",baseprice_id).val(basepriceName);
			var categoryName = cbbuyer.closest("tr").find("td").eq(2).text();
			var categoryUuid =  cbbuyer.closest("tr").find("td").eq(2).find("input[name='categoryUuid']").val();
			var factoryName = cbbuyer.closest("tr").find("td").eq(2).find("input[name='factoryId']").attr("val");
			var factoryId = cbbuyer.closest("tr").find("td").eq(2).find("input[name='factoryId']").val();
			//判断当前基价是否已经与卖家关联了
			//获取卖家，地区，基价名称,是否启用
			var accountId = $("#accountName_form").attr("val");
			var accountName = $("#accountName_form").val();
			var cityId = $("#cityName").val();

			if(typeof(accountId) == "undefined" || accountId==""){
				if(accountName != ''){
					cbms.alert("没有找到卖家的值，请重新选择卖家！");
					return ;
				}
				cbms.alert("请先选择一个卖家！");
				return ;
			}
			$.ajax({
				type: "POST",
				url: Context.PATH+ '/smartmatch/basePriceRelation/checkBasePrice.html',
				dataType: "json",
				data:{cityId:cityId,accountId:accountId,basePriceId:baseprice_id},
		        success: function(data){
					if(data.success){
						cbms.alert("当前卖家与基价已经设置！");
						return;
					}
					
		        } 
			});
			
			$("#category_name").val(categoryName);
			$("#category_name_view").html(categoryName);
			$("#category_name_view").attr("val",categoryUuid);
			$("#factory_name_view").html(factoryName);
			$("#factory_name_view").attr("val",factoryId);
			//generateTelInfo();
			$("#baseprice_search_panel").addClass("none");
		}
	});

	//选择买家确定
	$(document).on("click","#baseprice_list_table tr",function(){
		var cbradio = $(this).find("input[type=radio][name=baseprice_select]");
		if (cbradio.length > 0) {
			cbradio.prop("checked",true)
		}
	});

});
//删除复制
$(document).on("click", ".del-btn", function () {
	if ($(".d-dialog").find("div[id='m-basepriceDiv']").length > 1) {
		$(this).closest(".m-search").remove();	
	} else {
		cbms.alert("最少需要保留一个明细");
	}
});
//查询复制
$(document).on("click", ".clone-btn", function () {
	var c = $(this).closest(".m-search").clone();
	//复制后隐藏按钮
	//$(c).closest(".m-search").find('button[id="searchList"]').css('display','none');
	//$(c).closest(".m-search").find('span[id="cleanSearch"]').css('display','none');
	$(c).find(".spec .f-text").inputFocus();
	$(c).find(".spec").val("");
	$(this).closest(".m-search").after(c);

	$(".show-layer").hide();
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
function initBasepriceSearch(){
	var url = Context.PATH + "/smartmatch/basePriceRelation/queryBasePrice.html";
	accountSearchTable = jQuery("#baseprice_list_table").DataTable({
		"processing": false,
		"serverSide": true,
		"searching": false,
		"ordering": false,
		"paging": false,
		"bInfo" : false,
		"ajax": {
			type: 'post',
			url: url,
			data:{
				cityId:function(d){
					var cityId = $("#cityName").val();
					return cityId;
				}
			}
		
		},
		columns: [
			{data: "id"},
			{data: "basePriceName"},
			{data: "category"}
		],
		columnDefs: [
			{
				"targets": 0,
				"render": function (data, type, full, meta) {
					if(full.subscribed==1){
						return "";
					}
					else{
						return "<input type='radio' name='baseprice_select' aid='" + data + "'/>";
					}
				}
			},
			{
				"targets": 2,
				"render": function (data, type, full, meta) {
					var html = data +"<input type='hidden' name='categoryUuid' value='" + full.categoryUuid + "'/>";
					html += "<input type='hidden' name='factoryId' val=" + full.factory + " value='" + full.factoryId + "'/>";
					return html;
					
				}
			}
			
		]
	});
}
