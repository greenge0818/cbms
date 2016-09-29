var _formData;
var _data=[];
var _categoryId;

jQuery(function ($) {
    initTable();
    
    onSearchClick();
    
    $(document).on("click","#editInfoBtn",function(){
    	getNsortData();
    	var tr = $(this).closest('tr'), data = _formData.row(tr).data();
    	editCategoryWeight(data.id);
    });
    
    $(document).on("click","#deleteInfoBtn",function(){
    	var tr = $(this).closest('tr'), data = _formData.row(tr).data();
    	
    	cbms.confirm("确定要删除吗？","",function(){
    		
        	deleteCategoryWeight(data.id);
    	})
    	
    });
    
    $(document).on("click","#saveInfoBtn",function(){
    	saveAddFunc();
    });
    
    $(document).on("click","#cancel",function(){
        cbms.closeDialog();
    });
    
    //清空按钮
    $(document).on("click","#cleanSearch",function(){
    	resetForm($("form.form-inline"));
    });
    
    $(document).on('input propertychange','#name', function () {
    	showPYMatchList($("#name"),_data,"uuid","name",null);
    	$("#dropdown").css("z-index",999999);
    });
    
});

function initTable() {
    var url = Context.PATH + "/category/searchCategoryList.html";
    _formData = jQuery("#dynamic-table").DataTable({
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
                	categoryName: $("#categoryName").val(),
                });
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'name'}
            ,{data: 'varietyQuantity'}
            ,{data: 'priceDeviation'}
            , {defaultContent: ''}
        ],
        
        columnDefs: [
                       {
                           "targets": 2, //第几列 从0开始
                           "data": "priceDeviation",
                           "render": function (data) {
                               return "<span>" + data.toFixed(2) + "%</span>";
                           }
                       }
                       ,
                       {
                           sDefaultContent: '', //解决请求参数未知的异常
                           aTargets: ['_all']

                       }
                   ] ,
        
        fnRowCallback: function (nRow, aData, iDataIndex) {
            $('td:eq(3)', nRow).html(generateOptHtml($.trim(aData.id)));
            return nRow;
        }

    });

}

function searchData(isNewSearch) {
	if(isNewSearch){
		_formData.ajax.reload();
	}else{
		_formData.ajax.reload(null, false);
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
    optHtml += "<a id='editInfoBtn' target='_blank' title='编辑'>";
    optHtml += "<i class=\"fa fa-edit fa-2x\"></i></a>";
    optHtml += '</div>';
    return optHtml;
}

function saveAddFunc (){
	
	var condition = {};
	if($("#name").attr('val') == null || $("#name").attr('val') == ""){
		cbms.alert("请选择品名列表中的品名",enabelSave);
		cbms.closeDialog();
	}else{
		condition["name"] = $("#name").val();
		condition["uuid"] = $("#name").attr('val');
		condition["varietyQuantity"] = $("#varietyQuantity").val();
		condition["priceDeviation"] = $("#priceDeviation").val();
		
	    if(setlistensSave("#form-horizontal")){
            condition["id"] = _categoryId;
            saveCategory(condition);
	    }
	}
}

function enabelSave(){
    $("#saveInfoBtn").removeAttr("disabled");
}

function saveCategory(data) {
    $.ajax({
        type : "POST",
        url : Context.PATH + "/category/save.html",
        data : data,
        dataType : "json",
        success : function(response) {
            if (response.success) {
                cbms.alert(response.data,enabelSave);             
                cbms.closeDialog();
                searchData();
            } else {
                cbms.alert(response.data);
            }
        },
        error : function(xhr, textStatus, errorThrown) {}
    });
}

//编辑
function editCategoryWeight(id){   
    $.ajax({
        type : "POST",
        url : Context.PATH + "/category/"+id+"/edit.html",
        data : {},
        dataType : "json",
        success : function(response) {
            if (response.success) {
                var data = response.data;
                $("#name").val(data.name);  
                $("#varietyQuantity").val(data.varietyQuantity);
                $("#priceDeviation").val(data.priceDeviation);
                $("#name").attr('val',data.uuid);    
                _categoryId = id;
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
        url : Context.PATH + "/category/"+id+"/delete.html",
        data : {},
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
}

function getNsortData(){
	if(_data.length==0){
		$.ajax({
	        type : "POST",
	        url : Context.PATH + "/category/getAllCategory.html",
	        data : {},
	        dataType : "json",
	        success : function(response) {
	            if (response.success) {
	                _data = response.data;
	            } else {
	                cbms.alert(response.data);
	            }
	        },
	        error : function(xhr, textStatus, errorThrown) {}
	    });
	}
}
