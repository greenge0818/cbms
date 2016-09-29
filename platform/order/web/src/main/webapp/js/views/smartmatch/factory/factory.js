var _data;
var _condition = {};
var _isAdd;
var _factoryId;

jQuery(function ($) {
    initTable();

    onSearchClick();

    //添加钢厂
    $("#addFactory").on(ace.click_event,function(){
    	enableSave();
    	cbms.getDialog("新增",$("#hidden-form").html());
    	bindRegionData($("#province"), $("#city"),$("#district"));
    	_isAdd = true;
    	setlistensSave("#form-horizontal");
    });

    //编辑钢厂
    $(document).on("click","#editInfoBtn",function(){
    	var tr = $(this).closest('tr'), data = _data.row(tr).data();
    	editFactory(data.id);
    });

  //删除钢厂
    $(document).on("click","#deleteInfoBtn",function(){
    	var tr = $(this).closest('tr'), data = _data.row(tr).data();
    	cbms.confirm("确定要删除吗？","",function(){
    		deleteFactory(data.id);
    	});
    });

    //保存按钮
    $(document).on("click","#saveInfoBtn",function(){
    	saveAddFunc();
    });
    //取消按钮
    $(document).on("click","#cancel",function(){
        cbms.closeDialog();
        enableSave();
    });
    
    //清空按钮
    $(document).on("click","#cleanSearch",function(){
    	resetForm($("form.form-inline"));
    });

});

function initTable() {
    var url = Context.PATH + "/smartmatch/factory/search.html";
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
                	factoryName: $("#factoryName").val(),
                	factoryCity: $("#factoryCity").val(),
                    factoryBusiness: $("#factoryBusiness").val()
                });
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'name'},
            {data: 'alias'},
            {data: 'city'},
            {data: 'addr'},
            {data: 'contact'}
            , {data: 'mobile'}
            , {data: 'business'}
            , {data: 'longitude'}
            , {data: 'latitude'}
            , {data: 'output', "sClass": "text-right"}
            , {defaultContent: ''}
        ],

        columnDefs: [
             {
                sDefaultContent: '', //解决请求参数未知的异常
                aTargets: ['_all']

            }
        ] ,

        fnRowCallback: function (nRow, aData, iDataIndex) {
            $('td:eq(10)', nRow).html(generateOptHtml($.trim(aData.id)));
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

function enableSave(){
    $("#saveInfoBtn").removeAttr("disabled");
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
    optHtml += "<a style='cursor:pointer' id='editInfoBtn' target='_blank' title='编辑'>";
    optHtml += "<i class=\"fa fa-edit fa-2x\"></i></a>";
   	optHtml += " <a id='deleteInfoBtn' style='cursor:pointer' target='_blank' title='删除'><i class=\"fa fa-trash-o fa-fw fa-2x\"></i></a>";
    optHtml += '</div>';
    return optHtml;
}

function saveAddFunc (){
	_condition["name"] = $("#name").val();
	_condition["alias"] = $("#alias").val().replace(/ /ig,'');
	_condition["provinceId"] = $("#province").val();
	_condition["cityId"] = $("#city").val();
	_condition["districtId"] = $("#district").val();
	_condition["addr"] = $("#addr").val();
	_condition["contact"] = $("#contact").val();
	_condition["mobile"] = $("#mobile").val();
	_condition["business"] = $("#business").val();
	_condition["longitude"] = $("#longitude").val();
	_condition["latitude"] = $("#latitude").val();
	_condition["output"] = $("#output").val();
	_condition["id"] = $("#id").val();

	if(_condition["alias"].indexOf("，")>=0){
		_condition["alias"]=_condition["alias"].replace(/，/ig,',');
	}
	//过滤空值
	var alias=$.map(_condition["alias"].split(","),function(v){
		if(!utils.isEmpty(v)){
			return v;
		}
	});
	
	_condition["alias"]=unique(alias).join(",");
	
	for(var i in alias){
		if(alias[i] == _condition["name"]){
			cbms.alert("钢厂名称和别名不能相同！",enableSave);
			return false;
		}
	}

    if(setlistensSave("#form-horizontal")){
    	$("#saveInfoBtn").attr("disabled","disabled");
    	//新增
        if(_isAdd){
        	saveFactory(_condition);
        }else{
            _condition["id"] = _factoryId;
            saveFactory(_condition);
        }
    }

}

function saveFactory(data) {
    $.ajax({
        type : "POST",
        url : Context.PATH + "/smartmatch/factory/save.html",
        data :data,
        dataType : "json",
        success : function(response) {
            if (response.success) {
                cbms.alert(response.data,enableSave);
                $("#factoryName").val("");
            	$("#factoryCity").val("");
                $("#factoryBusiness").val("");
                cbms.closeDialog();
                searchData();
            } else {
                cbms.alert(response.data,enableSave);
            }
        },
        error : function(xhr, textStatus, errorThrown) {}
    });
}

//编辑
function editFactory(id){
	enableSave();
    $.ajax({
        type : "POST",
        url : Context.PATH + "/smartmatch/factory/"+id+"/edit.html",
        dataType : "json",
        success : function(response) {
            if (response.success) {
                var data = response.data;
                $("#name").val(data.name);
                $("#alias").val(data.alias);
                bindRegionData($("#province"), $("#city"),$("#district"),data.provinceId,data.cityId,data.districtId);
            	$("#addr").val(data.addr);
            	$("#contact").val(data.contact);
            	$("#mobile").val(data.mobile);
            	$("#business").val(data.business);
            	$("#longitude").val(data.longitude);
            	$("#latitude").val(data.latitude);
            	$("#output").val(data.output);
                _isAdd = false;
                _factoryId = id;
                setlistensSave("#form-horizontal");
            }
        },
        error : function(xhr, textStatus, errorThrown) {}
    });
    cbms.getDialog("编辑",$("#hidden-form").html());
}

//删除
function deleteFactory(id){
    $.ajax({
        type : "POST",
        url : Context.PATH + "/smartmatch/factory/"+id+"/delete.html",
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

