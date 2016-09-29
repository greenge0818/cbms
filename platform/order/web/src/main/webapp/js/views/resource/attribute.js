
var _attribute={
	 condition :{},
	 isAdd:true,
	 data:[]
}
jQuery(function ($) {
    initTable();

    onSearchClick();

    //添加属性
    $("#addButton").on(ace.click_event,function(){
    	getNsortData();
    	getAttrData();
    	cbms.getDialog("添加属性", $("#hideDiv").html());
    	_attribute.isAdd = true;
    	setlistensSave("#dialog");
    	removeDisabled();
    });

    //编辑属性
    $(document).on("click","#editInfoBtn",function(){
    	var tr = $(this).closest('tr') ,data = _attribute.dt.row(tr).data();
    	editAttribute(data.categoryUuid, data.categoryName);
    });

    //删除属性
    $(document).on("click","#deleteInfoBtn",function(){
    	var tr = $(this).closest('tr'),data = _attribute.dt.row(tr).data();
    	deleteAttribute(data.categoryUuid);
    });

    //保存按钮
    $(document).on("click","#submitBtn",function(){
    	saveAddFunc();
    });
    //取消按钮
    $(document).on("click","#cancelBtn",function(){
    	cbms.closeDialog();
    });
    
    //清空按钮
    $(document).on("click","#cleanSearch",function(){
    	resetForm($("form.form-inline"));
    });

    $(document).on('input propertychange','#nsort', function () {
    	showPYMatchList($("#nsort"),_attribute.data,"uuid","name",callBackFunc);
    	$("#dropdown").css("z-index",999999);
    });

});
function removeDisabled(){
	$("#submitBtn").removeAttr("disabled");
}
function initTable() {
    var url = Context.PATH + "/resource/attribute/search.html";
    _attribute.dt = jQuery("#dynamic-table").DataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "autoWidth": false,
        "ordering": false,
        //"bLengthChange": false, //显示pageSize的下拉框
		"iDisplayLength" : 50,
		"aLengthMenu" : [10,30,50,100],//定义每页显示数据数量
        "ajax": {
            "url": url,
            "type": "POST",
            data: function (d) {
            	d.attrName=$("select option:selected").val();
            	d.groupName=$("#sortName").val();
            	d.categoryName=$("#nsortName").val();
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'groupName'},
            {data: 'categoryName'},
            {data: 'attribute1Name'},
            {data: 'attribute1Options',
            "mRender":function(data,type,full){//通过回调函数添加html
				return renderOptions(full.attribute1Type,full.attribute1Options,full.categoryName,full.attribute1Name);
            }
            },
            {data: 'attribute2Name'},
            {data: 'attribute2Options',
        	"mRender":function(data,type,full){//通过回调函数添加html
        		return renderOptions(full.attribute2Type,full.attribute2Options,full.categoryName,full.attribute2Name);
             }},
            {data: 'attribute3Name'},
            {data: 'attribute3Options',
        	"mRender":function(data,type,full){//通过回调函数添加html
        		return renderOptions(full.attribute3Type,full.attribute3Options,full.categoryName,full.attribute3Name);
            }},
            {defaultContent: ''}
        ],

        fnRowCallback: function (nRow, aData, iDataIndex) {
            $('td:eq(8)', nRow).html(renderInput(aData.categoryUuid));
        }
    });
}
/**
 * 品名选择后的回调
 */
function callBackFunc(){
	var categoryUuid=$("#nsort").attr("val");
	if(!utils.isEmpty(categoryUuid)){
		$.ajax({
	        type : "POST",
	        url : Context.PATH + "/resource/attribute/getByUuid.html",
	        data : {"categoryUuid": categoryUuid},
	        dataType : "json",
	        success : function(response) {
	            if (response.data.length>0) {
	            	cbms.alert("该品名已被选择!",removeDisabled);
	            	$("#nsort").attr("sel","1");
	            }else{
	            	$("#nsort").attr("sel","0");
	            }
	        }
	        });
	}
}
/**
 * 列显示html
 * @param type       类型：input,select,radio,checkbox
 * @param options    数据值
 * @returns {String}
 */
function renderOptions(type,options,category,name){
	var contentHtml="";
	if(!utils.isEmpty(options)){
		switch(type){
			case "input":
				contentHtml+=options;
				break;
			case "radio":
				if(options.indexOf(",")>=0){
					var oArrays=options.split(",");
					for(var i=0;i<oArrays.length;i++){
						if(!utils.isEmpty(oArrays[i])){
							contentHtml+="<label class='pos-rel'>"
							contentHtml+="<input type='radio' name='"+category+name+"' value='"+oArrays[i]+"' class='ace'>"
							contentHtml+="<span class='lbl' style='margin:5px;'></span>"+oArrays[i]
							contentHtml+="</label>";
						}
					}
				}
				break;
			case "select":
				if(options.indexOf(",")>=0){
					contentHtml+='<select>';
					var oArrays=options.split(",");
					for(var i=0;i<oArrays.length;i++){
						if(!utils.isEmpty(oArrays[i])){
							contentHtml+='<option >'+oArrays[i]+'</option>';
						}
					}
				}
				contentHtml+='</select>';
				break;
			case "checkbox":
				if(options.indexOf(",")>=0){
					var oArrays=options.split(",");
					for(var i=0;i<oArrays.length;i++){
						if(!utils.isEmpty(oArrays[i])){
							contentHtml+="<label class='pos-rel'>"
							contentHtml+="<input type='checkbox' name='check' value='"+oArrays[i]+"' class='ace'>"
							contentHtml+="<span class='lbl' style='margin:5px;'></span>"+oArrays[i]
							contentHtml+="</label>";
						}
					}
				}
				break;
		}
	}
	return contentHtml;
}
function searchData(isNewSearch) {
	if(isNewSearch){
		_attribute.dt.ajax.reload();
	}else{
		_attribute.dt.ajax.reload(null, false);
	}
}

function onSearchClick() {
    jQuery("#searchButton").on(ace.click_event, function () {
        searchData(true);
    });
}
/**
 * 操作html
 * @param uuid
 * @returns {String}
 */
function renderInput(id){
	var optHtml = '<div class="fa-hover">';
	    optHtml += "<a href='javascript:void(0)' id='editInfoBtn' target='_blank' title='编辑'>";
	    optHtml += "<i class=\"fa fa-edit fa-2x\"></i></a>";
	   	optHtml += " <a href='javascript:void(0)' id='deleteInfoBtn' target='_blank' title='删除'>";
	   	optHtml += "<i class=\"fa fa-trash-o fa-fw fa-2x\"></i></a>";
	    optHtml += '</div>';
    return optHtml;
}
/**
 * 检查品名名称是否与数据源中的品名名称对应
 * @returns {Boolean}
 */
function checkCategoryName(){
	var categoryName=$("#nsort").val();
	var categoryUuid= $("#nsort").attr("val");
	var d=_attribute.data;
	if(!utils.isEmpty(d) && !utils.isEmpty(categoryName)){
		for(var i=0;i<d.length;i++){
			if(d[i].name==categoryName){
				$("#nsort").attr("val",d[i].uuid);
				callBackFunc();
				return true;
			}else{
				$("#nsort").attr("val","");
			}
		}
	}
	return false
}
/**
 * 点击保存
 */
function saveAddFunc (){
	
	if(_attribute.isAdd && !checkCategoryName()){
		 cbms.alert("请先选择品名!",removeDisabled);
		 return;
	 }
	 if($("#nsort").attr("sel")==1){
		 cbms.alert("该品名已被选择!",removeDisabled);
		 return;
	 }
	 var categoryUuid= $("#nsort").attr("val");
	 var array=new Array();
	 $("#dialog").find("input[name='check']").each(function(){
		 if($(this)[0].checked){
			 array.push($(this).val());
		 }
	 });
	if(array.length<=0){
		cbms.alert("至少选择一条属性!",removeDisabled);
		return;
	}
	 _attribute.condition["categoryUuid"]=categoryUuid;
	 _attribute.condition["attributeUuid"]=array;

	 if(setlistensSave("#dialog")){
    	saveAttribute(_attribute.condition);
	 }
}
/**
 * 执行保存
 * @param data
 */
function saveAttribute(data) {
    $.ajax({
        type : "POST",
        url : Context.PATH + "/resource/attribute/save.html",
        data :data,
        dataType : "json",
        success : function(response) {
            if (response.success) {
                cbms.alert(response.data);
                cbms.closeDialog();
                searchData();
            } else {
                cbms.alert(response.data);
            }
        },
        error : function(xhr, textStatus, errorThrown) {}
    });
}

//点击编辑
function editAttribute(id, categoryName){
    getNsortData();
	getAttrData();
	cbms.getDialog("编辑属性", $("#hideDiv").html());
	_attribute.isAdd = false;
	setlistensSave("#dialog");
    $.ajax({
        type : "POST",
        url : Context.PATH + "/resource/attribute/getByUuid.html",
        data : {"categoryUuid": id},
        dataType : "json",
        success : function(response) {
            if (response.success) {
                var data = response.data;
                $("#nsort").val(categoryName);
                $("#nsort").attr("val",id);
                $("#nsort").attr("readOnly","readOnly");
                var arrays=$("#dialog .pos-rel input[type='checkbox']");
                for(var i=0;i<arrays.length;i++){
                	var t=$(arrays[i]).val();
                	for(var j in data){
						if (t == data[j].attributeUuid) {
							$(arrays[i]).attr("checked", "checked");
						}
                	}
                }
                setlistensSave("#dialog");
            }
        },
        error : function(xhr, textStatus, errorThrown) {}
    });
    removeDisabled();
}

//删除
function deleteAttribute(id){
	cbms.confirm("确定删除此记录?",null,function(){
		$.ajax({
	        type : "POST",
	        url : Context.PATH + "/resource/attribute/delete.html",
	        data : {"categoryUuid": id},
	        dataType : "json",
	        success : function(response) {
	            if (response.success) {
	                cbms.alert(response.data);
	                searchData();
	            } else {
	                cbms.alert(response.data);
	            }
	        },
	        error : function(xhr, textStatus, errorThrown) {}
	    });
	});
}

function getNsortData(){
	if(_attribute.data.length==0){
		$.ajax({
	        type : "POST",
	        url : Context.PATH + "/category/getAllCategory.html",
	        data : {},
	        dataType : "json",
	        success : function(response) {
	            if (response.success) {
	            	_attribute.data = response.data;
	            } else {
	                cbms.alert(response.data);
	            }
	        },
	        error : function(xhr, textStatus, errorThrown) {}
	    });
	}
}
/**
 * 取得所有属性组装checkbox
 */
function getAttrData(){
	$.ajax({
		type : "POST",
		url : Context.PATH + "/resource/getAllAttribute.html",
		data : {},
		dataType : "json",
		success : function(response) {
			if (response.success) {
//				_attrData = response.data;
				var res=response.data;
				if(res.length>0){
					var contentHtml="";
					for(var k=0;k<res.length;k++){
						contentHtml+="<label class='pos-rel'>"
	        			contentHtml+="<input type='checkbox' name='check' value='"+res[k].uuid+"' class='ace'>"
	    				contentHtml+="<span class='lbl' style='margin:5px;'></span>"+res[k].name
	    				contentHtml+="</label>";
					}
					$("#dialog").append(contentHtml);
				}
			} else {
				cbms.alert(response.data);
			}
		},
		error : function(xhr, textStatus, errorThrown) {}
	});
}