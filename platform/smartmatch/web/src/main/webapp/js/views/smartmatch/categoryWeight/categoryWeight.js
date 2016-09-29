var _data;
var _isAdd;
var _categoryWeightId;
var _factoryData=[];
var _categoryData=[];

jQuery(function ($) {
    initTable();

    onSearchClick();

    //添加物资单件重量
    $("#addInfoBtn").on(ace.click_event,function(){
    	enableSave();
    	cbms.getDialog("新增",$("#hidden-form").html());
    	_isAdd = true;
    	setlistensSave("#form-horizontal");

    });

    //编辑物资单件重量
    $(document).on("click","#editInfoBtn",function(){
    	var tr = $(this).closest('tr'), data = _data.row(tr).data();
    	enableSave();
    	editCategoryWeight(data.id);
    });

  //删除物资单件重量
    $(document).on("click","#deleteInfoBtn",function(){
    	var tr = $(this).closest('tr'), data = _data.row(tr).data();
    	cbms.confirm("确定要删除吗？","",function(){
        	deleteCategoryWeight(data.id);
    	})
    });

    //保存按钮
    $(document).on("click","#saveInfoBtn",function(){
    	saveAddFunc();
    });

    //取消按钮
    $(document).on("click","#cancel",function(){
        cbms.closeDialog();
        enableSave();
    })
    
    //清空按钮
    $(document).on("click","#cleanSearch",function(){
    	resetForm($("form.form-inline"));
    });

    $(document).on('input propertychange','#factoryName', function () {
    	showPYMatchList($("#factoryName"),_factoryData,"id","name",null);
    	$("#dropdown").css("z-index",999999);
    });

    $(document).on('input propertychange','#categoryName', function () {
    	showPYMatchList($("#categoryName"),_categoryData,"uuid","name",lodeMeterial);
    	$("#dropdown").css("z-index",999999);

    });

});

function lodeMeterial(){
	bindMaterialsData($("#materialName"),$("#categoryName").attr("val"));
}

//获取材质
function getMaterials(categoryUuid) {
    var result = null;
    $.ajax({
        type: 'post',
        url: Context.PATH + "/smartmatch/categoryweight/getMaterials.html",
        data: {
        	categoryUuid:categoryUuid
        },
        async: false,
        error: function (s) {
        }
        , success: function (data) {
            if (data) {
                result = data;
            }
        }
    });
    return result;
}

/*
 * 根据品名获取材质
 */
function bindMaterialsData(materialName, categoryUuid) {
		var result = getMaterials(categoryUuid);
        var materialOptions = "";
        materialOptions += "<option value=''>==请选择==</option>";
        for (var i = 0; i < result.length; i++) {
            materialOptions += "<option id='" + result[i].uuid + "' value='" + result[i].uuid + "'>" + result[i].name + "</option>";
        }
        $(materialName).html(materialOptions);

}

(function getData(){
	if(_factoryData.length==0){
		$.ajax({
	        type : "POST",
	        url : Context.PATH + "/smartmatch/factory/getAllFactory.html",
	        dataType : "json",
	        success : function(response) {
	            if (response.success) {
	            	_factoryData = response.data;
	            } else {
	                cbms.alert(response.data);
	            }
	        },
	        error : function(xhr, textStatus, errorThrown) {}
	    });
	}

	if(_categoryData.length==0){
		$.ajax({
	        type : "POST",
	        url : Context.PATH + "/category/getAllCategory.html",
	        data : {},
	        dataType : "json",
	        success : function(response) {
	            if (response.success) {
	            	_categoryData = response.data;
	            } else {
	                cbms.alert(response.data);
	            }
	        },
	        error : function(xhr, textStatus, errorThrown) {}
	    });
	}

})();

function initTable() {
    var url = Context.PATH + "/smartmatch/categoryweight/search.html";
    _data = jQuery("#dynamic-table").DataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "autoWidth": false,
        "iDisplayLength":50,
        "aLengthMenu": [10,30,50,100],//定义每页显示数据数量
        "ajax": {
            "url": url
            , "type": "POST"
            , data: function (d) {
                return $.extend({}, d, {
                	factory: $("#factory").val(),
                	category: $("#category").val(),
                	norms: $("#norms").val()
                });
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'factoryName'}
            ,{data: 'categoryName'}
            , {data: 'norms_name'}
            , {data: 'single_weight', "sClass": "text-right"}
            , {defaultContent: ''}
        ],

        columnDefs: [
             {
                sDefaultContent: '', //解决请求参数未知的异常
                aTargets: ['_all']

            }
        ] ,

        fnRowCallback: function (nRow, aData, iDataIndex) {
            $('td:eq(4)', nRow).html(generateOptHtml($.trim(aData.id)));
            return nRow;
        }

    });

}

function searchData(isNewSearch) {
	if(isNewSearch){
		_data.ajax.reload();
	}else{
		_data.ajax.reload(null, false);
	}
}

function onSearchClick() {
    jQuery("#searchVendorlist").on(ace.click_event, function () {
        searchData(true);
    });
}

/**
 * 操作列图片组装
 * @param id
 * @returns {String}
 */
function generateOptHtml(id) {
    var optHtml = '<div class="fa-hover">';
    optHtml += "<a id='editInfoBtn' style='cursor:pointer' target='_blank' title='编辑'>";
    optHtml += "<i class=\"fa fa-edit fa-2x\"></i></a>";
   	optHtml += " <a id='deleteInfoBtn' style='cursor:pointer' target='_blank' title='删除'><i class=\"fa fa-trash-o fa-fw fa-2x\"></i></a>";
    optHtml += '</div>';
    return optHtml;
}

function saveAddFunc (){
	var condition = {};
	condition["factoryId"] = $("#factoryName").attr('val');
	condition["categoryUuid"] = $("#categoryName").attr('val');
	condition["materialUuid"] = $("#materialName").val();
	condition["normsName"] = $("#norms_name").val();
	condition["singleWeight"] = $("#single_weight").val();

    if(setlistensSave("#form-horizontal")){
    	$("#saveInfoBtn").attr("disabled","disabled");
    	//新增
        if(_isAdd){
        	saveCategoryWeight(condition);
        }else{
            condition["id"] = _categoryWeightId;
            saveCategoryWeight(condition);
        }
    }
    
}

function enableSave(){
    $("#saveInfoBtn").removeAttr("disabled");
}

function saveCategoryWeight(data) {
	if($("#factoryName").attr('val') == null || $("#factoryName").attr('val') == ""){
		cbms.alert("请选择钢厂列表中的钢厂",enableSave);
	}else{
		$.ajax({
	        type : "POST",
	        url : Context.PATH + "/smartmatch/categoryweight/save.html",
	        data : data,
	        dataType : "json",
	        success : function(response) {
	            if (response.success) {
	                cbms.alert(response.data,enableSave);
	                $("#factory").val("");
	            	$("#category").val("");
	                $("#material").val("");
	                $("#norms").val("");
	                cbms.closeDialog();
	                searchData();
	            } else {
	                cbms.alert(response.data,enableSave);
	            }
	        },
	        error : function(xhr, textStatus, errorThrown) {}
	    });
	}
    
}

//编辑
function editCategoryWeight(id){
    $.ajax({
        type : "POST",
        url : Context.PATH + "/smartmatch/categoryweight/"+id+"/edit.html",
        data : {},
        dataType : "json",
        success : function(response) {
            if (response.success) {
                var data = response.data;
                var categoryOptions = "";
                var materialOptions = "";
                $("#factoryName").val(data.factoryName);
                $("#factoryName").attr('val',data.categoryWeight.factoryId);
                $("#categoryName").val(data.categoryName);
                $("#categoryName").attr('val',data.categoryWeight.categoryUuid);

                bindMaterialsData($("#materialName"),$("#categoryName").attr("val"));

                $("#materialName").val(data.categoryWeight.materialUuid);
            	$("#norms_name").val(data.categoryWeight.normsName);
            	$("#single_weight").val(data.categoryWeight.singleWeight);
                _isAdd = false;
                _categoryWeightId = id;
                setlistensSave("#form-horizontal");
            }
        },
        error : function(xhr, textStatus, errorThrown) {}
    });

    cbms.getDialog("编辑",$("#hidden-form").html());


}

//删除
function deleteCategoryWeight(id){
    $.ajax({
        type : "POST",
        url : Context.PATH + "/smartmatch/categoryweight/"+id+"/delete.html",
        data : {},
        dataType : "json",
        success : function(response) {
            if (response.success) {
                cbms.alert(response.data,enableSave);
                searchData();
            } else {
                cbms.alert(response.data,enableSave);
            }
        },
        error : function(xhr, textStatus, errorThrown) {}
    });
}


