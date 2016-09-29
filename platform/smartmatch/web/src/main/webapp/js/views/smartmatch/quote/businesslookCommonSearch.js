var _resourceData = {
	selectedIds:[] //数据行选中id记录集
}

//加载所有城市
$(document).ready(function () {
	crateCommonSearchDiv();
});

function addCommonSearch(){
	var html = $('#commonSearchFormDiv').html();
	cbms.getDialog("新增常用搜索", html);
	
	$(document).off("click","#cancel_btn");
	
	$(document).on("click", "#cancel_btn", function(){
		cbms.closeDialog();

	});
	
}

function delCommonSearch(id){
	cbms.confirm("您确定删除该条信息吗?",null,function(){
		if(!utils.isEmpty(id)){
			$.ajax({
		        type: 'post',
		        url: Context.PATH + "/smartmatch/quote/delHotSearch.html",
		        data : {"id":id},
		        success: function (result) {
		        	if(result.success){
		        		cbms.alert('删除成功！');
		        		refsearchData(result);
		        	}else{
		        		cbms.alert(result.data);
		        	}
		        }
		    });
		}else{
			cbms.alert('请先选择一条数据!');
		}
	});
}

//规格
$(document).on('input propertychange',"input[search='spec_from']", function (e) {
	var input = this;
	//var spec = $.trim($(input).closest(".m-search").find("input[id='spec_from']").val());
	var spec = $.trim($(input).val());
	if(spec != ''){
		$.ajax({
			type: "POST",
			url: Context.PATH+ '/smartmatch/quote/matchresource.html',
			dataType: "json",
			data:{conditionName:'spec',spec:spec},
			success: function (response, textStatus, xhr) {
				if(response.length > 0){
					showPYMatchList($(input),response,"spec","spec",null);
					cbms.stopF(e);
				}
			}
		});
	}
});

//品名联想显示
$(document).on('input propertychange',"input[search='category_form']", function (e) {
	var input = this;
	var spec = $.trim($("input[search='spec_from']").val());
	var cityName = $.trim($("input[search='city_from']").val());
	//if(spec != ''){
		$.ajax({
			type: "POST",
			url: Context.PATH+ '/smartmatch/quote/matchresource.html',
			dataType: "json",
			data:{conditionName:'category',spec:spec,cityName:cityName},
			success: function (response, textStatus, xhr) {
				if(response.length > 0){
					showPYMatchList($(input),response,"categoryUuid","categoryName",addCategoryId);
					cbms.stopF(e);
				}
			}
		});
	//}
});

//材质联想显示
$(document).on('input propertychange',"input[search='material_form']", function (e) {
	var input = this;
	//var spec = $.trim($('input[search="spec_form"]').val());
	var spec = $.trim($("input[search='spec_from']").val());
	var categoryName = $.trim($('input[search="category_form"]').val());
	var cityName = $.trim($("input[search='city_from']").val());
	//if(spec != '' && categoryName !=''){
		$.ajax({
			type: "POST",
			url: Context.PATH+ '/smartmatch/quote/matchresource.html',
			dataType: "json",
			data:{conditionName:'material',spec:spec,categoryName:categoryName,cityName:cityName},
			success: function (response, textStatus, xhr) {
				if(response.length > 0){
					showPYMatchList($(input),response,"materialUuid","materialName",addMaterialId);
					cbms.stopF(e);
				}
			}
		});
	//}
});

//钢厂联想
$(document).on('input propertychange',"input[search='factory_form']", function (e) {
	var input = this;
	var spec = $.trim($("input[search='spec_from']").val());
	var categoryName = $.trim($("input[search='category_form']").val());
	var materialName = $.trim($("input[id='material_form']").val());
	var cityName = $.trim($("input[search='city_from']").val());
	//if(spec != '' && categoryName !='' && materialName != null){
		$.ajax({
			type: "POST",
			url: Context.PATH+ '/smartmatch/quote/matchresource.html',
			dataType: "json",
			data:{conditionName:'factory',spec:spec,categoryName:categoryName,materialName:materialName,cityName:cityName},
			success: function (response, textStatus, xhr) {
				if(response.length > 0){
					showPYMatchList($(input),response,"factoryName","factoryName",null);
					cbms.stopF(e);
				}
			}
		});
	//}
});

//交货地提示
$(document).on('input propertychange',"input[search='city_form']", function (e) {
	var input = this;
	showPYMatchList($(input),Context.CITYDATA,"name","name",null);
	cbms.stopF(e);
});

//提交按钮
$(document).on("click", "#commit_btn", function(event) {
	//获取提交的表单数据
	var data={
		spc:$.trim($("input[search='spec_from']").val()),
		categoryUuid:$.trim($("input[id='categoryId_form']").val()),
		categoryName:$.trim($("input[id='category_form']").val()),
		materialUuid:$.trim($("input[id='materialId_form']").val()),
		materialName:$.trim($("input[id='material_form']").val()),
		//factoryId:$.trim($("input[id='factory_form']").attr("val")),
		factoryName:$.trim($("input[id='factory_form']").val()),
		//cityId:$.trim($("input[id='city_form']").attr("val")),
		cityName:$.trim($("input[id='city_form']").val())
		
	};
	if(data.spc==''&&data.categoryName==''&&data.materialName==''&&data.factoryName==''&&data.cityName==''){
		cbms.alert('至少必须输入一个条件！');
		return;
	}
	if(data.spc==splitStr||data.categoryName==splitStr||data.materialName==splitStr||data.factoryName==splitStr||data.cityName==splitStr){
		cbms.alert("不能输入字符'"+splitStr+"'!");
		return;
	}
	var url = Context.PATH + "/smartmatch/quote/saveHotSearch.html";
	//提交按钮不可用，防止重复提交
	$("#commit_btn").attr("disabled","disabled");
	//提交
	$("#commonSearch_form").ajaxSubmit({  
        type: 'post',  
        url: url, 
        data:data,
        dataType:"json",
        success: function(data){
			if(data.success){
				cbms.alert('保存成功！');
				//刷新数据
				refsearchData(data);
			}else{
        		cbms.alert(data.data);
        	}
			cbms.closeDialog();
			//提交按钮重新可用
			$("#commit_btn").removeAttr("disabled");
        } 
    });
	return false
});


var isNotinFlag='_is_not_flag_';
var splitStr="_+_";
function refsearchData(result){
	  data = result.data;  
	  var t = "";
	  $.each(data, function (i, item) { 
		  var condition_value ='';//当前条件的类型是否在查询条件中，用于查询时，判断值
		  if(i == 0){
			t+='<div class="searchfirst">';
		  }else{
			t+='<div class="searchfirst mg-l-10">';
		  }
		  t+='<a href="javascript:void(0);" onclick="searchResource('+item.id+')">';
		  if(item.spc!=''){
			  t+=item.spc+'/'
			  condition_value+=item.spc+splitStr;
		  }else{
			  condition_value+=isNotinFlag+splitStr;
		  }
		  if(item.categoryName!=''){
			  t+=item.categoryName+'/'
			  condition_value+=item.categoryName+splitStr;
		  }else{
			  condition_value+=isNotinFlag+splitStr;
		  }
		  if(item.materialName!=''){
			  t+=item.materialName+'/'
			  condition_value+=item.materialName+splitStr;
		  }else{
			  condition_value+=isNotinFlag+splitStr;
		  }
		  if(item.factoryName!=''){
			  t+=item.factoryName+'/'
			  condition_value+=item.factoryName+splitStr;
		  }else{
			  condition_value+=isNotinFlag+splitStr;
		  }
		  if(item.cityName!=''){
			  t+=item.cityName+'/'
			  condition_value+=item.cityName+splitStr;
		  }else{
			  condition_value+=isNotinFlag+splitStr;
		  }
		  //去掉最后一个/
		  t=t.substring(0,t.length-1);
		  
		  t+='</a>&nbsp;&nbsp;&nbsp;&nbsp;';
		  t+='<input type="hidden" id="cvalue_'+item.id+'" value='+condition_value+'>';
		  t+='<a id="delhostsearch" href="javascript:void(0);" onclick="delCommonSearch('+item.id+')">X</a></div>';
		  
	  });
	  t+='<div style="margin-left:10px;float: left;"><a href="javascript:void(0);" class="newadd" onclick="addCommonSearch()">新增</a></div>'
	  $("#hotSearch_content").html(t);
}
function crateCommonSearchDiv(){
	$.ajax({
        type: 'post',
        url: Context.PATH + "/smartmatch/quote/searchCommonSearch.html",
       // data : {"cityName":$("#cityName_form").val()},
        success: function (result) {
        	if(result.success){
        		  data = result.data;  
        		  var t = "";
        		  refsearchData(result)
        	}
        }
    });
}

function addCategoryId(value){
	//alert(value);
	$("#categoryId_form").val(value);
}

function addMaterialId(value){
	//alert(value);
	$("#materialId_form").val(value);
}

function searchResource(id){
	var cvalue = "cvalue_"+id;
	var conditionStr = $("input[id='"+cvalue+"']").val();
	
	var conditions = conditionStr.split(splitStr);
	
	var data={
			spc:conditions[0]==isNotinFlag?null:conditions[0],
			categoryName:conditions[1]==isNotinFlag?null:conditions[1],
			materialName:conditions[2]==isNotinFlag?null:conditions[2],
			factoryName:conditions[3]==isNotinFlag?null:conditions[3],
			cityName:conditions[4]==isNotinFlag?null:conditions[4]
			
	};
	_condition_data=data;
	$("#singleTable").css('display','');
	$("#multiTable").css('display','none');
	singleTable.fnDraw();
}

/**
 * 检查数据行是否选中，并把资源id存入变量
 */
function _isSelectedRows(){
	var table = null;
	//多条件查询
	if ($("div[id='m-searchDiv']").length > 1) {
		table = multiTable;
	}else{//单条件查询
		table = singleTable;
	}
	//清空数组
	_resourceData.selectedIds.length=0;
	var rows=table.fnGetNodes();
	if(utils.isEmpty(rows)){
		cbms.alert("暂无数据,请先添加资源!");
		return false;
	}else{
		
		if(table == multiTable){
			var inps=$("input[type='checkbox'][name='multi_checkbox']");
			for(var k=0;k<inps.length;k++){
				var inp=inps[k];
				if(inp.checked){
					//var rid = $(rows[k]).find("input[id='resource']");
					var rid = $(inp).attr("val");
					_resourceData.selectedIds.push(Number(rid));
				}
			}
		}else{
			for(var k=0;k<rows.length;k++){
				var inp=$(rows[k]).find("input[type='checkbox']");
				if(inp[0].checked){
					//var rid = $(rows[k]).find("input[id='resource']");
					var rid = $(inp).attr("val");
					_resourceData.selectedIds.push(Number(rid));
				}
			}
			
		}
		
		if(utils.isEmpty(_resourceData.selectedIds)){
			cbms.alert("请先选中记录!");
			return false;
		}
	}
	return true;
}



//有价格选择
$(document).on("change", "#hasPrice", function(event) {
	//alert(11);
	
});

//有价格选择
$(document).on("change", "#todayPrice", function(event) {
	//alert(22);
	
});
