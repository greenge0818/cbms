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

    $(document).on("click","#btn_eidt_category",function(){
        if ($("#addCategory input[name=cateCheck]:checkbox:checked").length <= 0) {
            cbms.alert("请选择品名列表中的品名", enabelSave);
            return false;
        }
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
                    aliasName: $("#aliasName").val(),
                    ecDisplay: $("#ecDisplay").val()
                });
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'name'},
            {data: 'aliasName'},
            {data: 'isEcShow'},
            {defaultContent: ''},
            {data: 'varietyQuantity'},
            {data: 'priceDeviation'},
            {defaultContent: ''}
        ],
        columnDefs: [
            {
               "targets": 2, //第几列 从0开始
               "data": "isEcShow",
               "render": function (data) {
                   return data == "false" ? "不显示" : "显示";
               }
            },
            {
                "targets": 3, //第几列 从0开始
                "data": "priceMin",
                "render": function (data, t, full) {
                    return full.priceMin + "-" + full.priceMax;
                }
            },
            {
                "targets": 5, //第几列 从0开始
                "data": "priceDeviation",
                "render": function (data) {
                    return "<span>" + data.toFixed(2) + "%</span>";
                }
            },
            {
               sDefaultContent: '', //解决请求参数未知的异常
               aTargets: ['_all']

            }
        ],
        fnRowCallback: function (nRow, aData, iDataIndex) {
            $('td:eq(6)', nRow).html(generateOptHtml($.trim(aData.id)));
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
        if(parseFloat($("#minprice").val())>parseFloat($("#maxprice").val())){
            cbms.gritter("参考价填写不正确", enabelSave);
            return;
        }
        if ($("#minprice").val().length > 10 || $("#maxprice").val().length > 10) {
       	 cbms.gritter("参考价填写长度超过限制，请重新输入", enabelSave);
            return;
       }
		condition["name"] = $("#name").val();
		condition["uuid"] = $("#name").attr('val');
        condition["aliasName"] = $("#aliasName").val();
        condition["isEcShow"] = $("#form-horizontal input[type=radio][name='isEcShow']:checked").val();
		condition["varietyQuantity"] = $("#varietyQuantity").val();
		condition["priceDeviation"] = $("#priceDeviation").val();
        condition["priceMin"] = $("#minprice").val();
        condition["priceMax"] = $("#maxprice").val();
		
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
                cbms.alert(response.data, enabelSave);
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
                $("#aliasName").text(data.aliasName);
                if (data.isEcShow == "false") {
                    $("#ecshow_false").attr("checked", "checked");
                }else{
                    $("#ecshow_true").attr("checked", "checked");
                }
                $("#minprice").val(data.priceMin);
                $("#maxprice").val(data.priceMax);
                _categoryId = id;
                setlistensSave("#form-horizontal");
                enabelSave();
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
