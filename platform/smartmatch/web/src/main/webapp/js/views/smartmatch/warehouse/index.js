var _warehouseDatatable;
var _warehouseCondition = {};
var _isAdd;
var _warehouseId;

jQuery(function ($) {
    //加载列表
    initTable();

    // 搜索
    searchClick();

    // 添加仓库
    $("#addWarehouse").on(ace.click_event, function () {
        enableSave();
        cbms.getDialog("新增", $("#addWare").html());
        bindRegionData($("#province"), $("#city"), $("#district"));
        _isAdd = true;
        setlistensSave("#form-horizontal");
    });

    //删除仓库
    $(document).on("click", "#deleteInfoBtn", function () {
        var tr = $(this).closest('tr'), data = _warehouseDatatable
            .row(tr).data();
        cbms.confirm("确定删除？", null, function () {
            deleteWarehouse(data.id);
        });
    });

    //编辑仓库
    $(document).on("click", "#editInfoBtn", function () {
        var tr = $(this).closest('tr'), data = _warehouseDatatable.row(tr).data();
        editWarehouse(data.id);
    });

    //保存按钮
    $(document).on("click", "#save", function () {
        addWare();
    });
    // 取消按钮
    $(document).on("click", "#cancel", function () {
        cbms.closeDialog();
        enableSave();
    });

    //清空按钮
    $(document).on("click", "#cleanSearch", function () {
        resetForm($("form.form-inline"));
    });

});

function searchClick() {
    jQuery("#searchlist").on(ace.click_event, function () {
        searchData(true);
    });
}

function searchData(isNewSearch) {
	if(isNewSearch){
		_warehouseDatatable.ajax.reload();
	}else{
		_warehouseDatatable.ajax.reload(null, false);
	}
}


function enableSave(){
    $("#save").removeAttr("disabled");
}


// 添加
function addWare() {
    enableSave();
    _warehouseCondition["name"] = $("#name").val();
    _warehouseCondition["alias"] = $("#alias").val().replace(/ /ig,'');
    _warehouseCondition["provinceId"] = $("#province").val();
    _warehouseCondition["cityId"] = $("#city").val();
    _warehouseCondition["districtId"] = $("#district").val();
    _warehouseCondition["addr"] = $("#addr").val();
    _warehouseCondition["longitude"] = $("#longitude").val();
    _warehouseCondition["latitude"] = $("#latitude").val();
    _warehouseCondition["contact"] = $("#contact").val();
    _warehouseCondition["mobile"] = $("#mobile").val();
    _warehouseCondition["exitFee"] = $("#exit_fee").val();
    _warehouseCondition["liftFee"] = $("#lift_fee").val();
    _warehouseCondition["remark"] = $("#remark").val();

    if(_warehouseCondition["alias"].indexOf("，")>=0){
        _warehouseCondition["alias"]=_warehouseCondition["alias"].replace(/，/ig,',');
    }

    //过滤空值
    _warehouseCondition["alias"]=$.map(unique(_warehouseCondition["alias"].split(",")),function(v){
        if(!utils.isEmpty(v)){
            return v;
        }
    }).join(",");
    if (setlistensSave("#form-horizontal")) {
        // 新增
        if (_isAdd) {
            _warehouseCondition["id"] = null;
            saveWarehouse(_warehouseCondition);
        } else {
            _warehouseCondition["id"] = _warehouseId;
            saveWarehouse(_warehouseCondition);
        }
    }
}
// 保存添加
function saveWarehouse(data) {
    $.ajax({
        type: "POST",
        url: Context.PATH + "/smartmatch/warehouse/save.html",
        data: data,
        dataType: "json",
        success: function (response) {
            if (response.success) {
                cbms.alert("保存成功！", function () {
                    enableSave();
                    $("#name").val("");
                    $("#city").val("");
                    searchData();
                });
                cbms.closeDialog();
                searchData();
            } else {
                cbms.alert(response.data, function () {
                    enableSave();
                });
            }
        },
        error: function (xhr, textStatus, errorThrown) {
        }
    });
}

// 删除
function deleteWarehouse(id) {
    $.ajax({
        type: "POST",
        url: Context.PATH + "/smartmatch/warehouse/del.html",
        data: {
            "id": id
        },
        dataType: "json",
        success: function (response) {
            if (response.success) {
                cbms.alert(response.data, function () {
                    enableSave();
                });
                searchData();
            } else {
                cbms.alert(response.data);
            }
        },
        error: function (xhr, textStatus, errorThrown) {
        }
    });
}

// 编辑
function editWarehouse(id) {
    enableSave();
    cbms.getDialog("编辑", $("#addWare").html());
    $.ajax({
        type: "POST",
        url: Context.PATH + "/smartmatch/warehouse/edit.html",
        data: {
            "id": id
        },
        dataType: "json",
        success: function (response) {
            if (response.success) {
                var data = response.data;
                $("#name").val(data.name);
                $("#alias").val(data.alias);
                bindRegionData($("#province"), $("#city"), $("#district"),
                    data.provinceId, data.cityId, data.districtId);
                $("#addr").val(data.addr);
                $("#contact").val(data.contact);
                $("#mobile").val(data.mobile);
                $("#longitude").val(data.longitude);
                $("#latitude").val(data.latitude);
                $("#exit_fee").val(data.exitFee);
                $("#lift_fee").val(data.liftFee);
                $("#remark").val(data.remark);
                _isAdd = false;
                _warehouseId = id;
                setlistensSave("#form-horizontal");
            }
        },
        error: function (xhr, textStatus, errorThrown) {
        }
    });

}

/**
 * 操作列图片
 * @param id
 * @returns {string}
 */
function generateOptHtml(id) {
    var optHtml = '<div class="fa-hover">';
    optHtml += "<a id='editInfoBtn' target='_blank' title='编辑'>";
    optHtml += "<i class=\"fa fa-edit fa-2x\"></i></a>";
    optHtml += " <a id='deleteInfoBtn' target='_blank' title='删除'><i class=\"fa fa-trash-o fa-fw fa-2x\"></i></a>";
    optHtml += '</div>';
    return optHtml;
}

function initTable() {
    var url = Context.PATH + "/smartmatch/warehouse/search.html";
    _warehouseDatatable = jQuery("#dynamic-table").DataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "bAutoWidth": false,
        "iDisplayLength": 50,
        "aLengthMenu": [10, 30, 50, 100],//定义每页显示数据数量
        "ajax": {
            "url": url,
            "type": "POST",
            data: function (d) {
                return $.extend({}, d, {
                    name: $("#name").val(),
                    city: $("#city").val()
                });
            }
        },
        // 数据源为数组时，定义数据列的对应
        columns: [
            {data: 'name'},
            {data: 'alias'},
            {data: 'city'},
            {data: 'addr'},
            {data: 'contact'},
            {data: 'mobile'},
            {data: 'exitFee',"sClass": "text-right"},
            {data: 'liftFee',"sClass": "text-right"},
            {data: 'type'},
            {data: 'remark'},
            {defaultContent: ''}
        ],
        columnDefs: [
            {
                "targets": 6, //第几列 从0开始
                "data": "exitFee",
                "render": renderAmount
            },
            {
                "targets": 7, //第几列 从0开始
                "data": "liftFee",
                "render": renderAmount
            },
            {
                sDefaultContent: '', //解决请求参数未知的异常
                aTargets: ['_all']

            }
        ],

        fnRowCallback: function (nRow, aData, iDataIndex) {
            $('td:eq(10)', nRow).html(generateOptHtml($.trim(aData.id)));
            return nRow;
        }
    });

}
