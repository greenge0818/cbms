/**
 * 材质管理页面首页 js
 * created by  peanut on 2016-1-13
 */

/****全局属性变量***/
var _materialMgtAttr={
	dt:null,   // datatables 对象
	categoryData:null   //品名数据
 };

$(document).ready(function () {
	_materialMgtAttr.dt = $('#dynamic-table').dataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "autoWidth": false,
        "iDisplayLength" : 50,
		"aLengthMenu" : [10,30,50,100],//定义每页显示数据数量
        "ajax": {
            url: Context.PATH + '/resource/materials/search.html',
            type: 'POST',
            data: function (d) {
                d.categoryName = $.trim($("#category").val());
                d.materialName = $.trim($("#material").val());
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
            // operation button
            $('td:last', nRow).html(generateOptHtml($.trim(aData.categoryMaterialId),$.trim(aData.materialId)));
        },
        columns: [
            {data: 'categoryName'},
            {data: 'materialName'},
            {data: 'remark'},
            {defaultContent:""}
        ]
    });

    $("#queryBtn").on("click", function () {
    	_materialMgtAttr.dt.fnDraw();
    });
    //清空按钮
    $("#cleanSearch").on("click", function () {
    	resetForm($("form.form-inline"));
    });
});
	/**
	 * 编辑
	 * @param id
	 */
    function goToEdit(id) {
    	cbms.getDialog("编辑属性", Context.PATH + "/resource/materials/edit/"+id+"/detail.html");
    }
	/**
	 * 删除
	 * @param id 
	 */
    function goToDel (id) {
    	cbms.confirm("您确定要删除与该材质关联的所有记录么?",null,function(){
    		$.ajax({
                type: "POST",
                url: Context.PATH + '/resource/materials/'+id+'/del.html',
                dataType: "json",
                success: function (response, textStatus, xhr) {
                    if (response.success) {
                    	_materialMgtAttr.dt.fnDraw();
                    }
                }
            });
    	});
    }
    
    /**
     * 操作列图片组装
     * @param id
     * @returns {String}
     */
	function generateOptHtml(categoryMaterialId,materialId) {
	    var optHtml = '<div class="fa-hover">';
	    optHtml += "<a href='javascript:goToEdit("+categoryMaterialId+")' target='_blank' title='编辑'>";
	    optHtml += "<i class=\"fa fa-edit fa-2x\"></i></a>";
	   	optHtml += " <a href='javascript:goToDel("+materialId+")' target='_blank' title='删除'><i class=\"fa fa-trash-o fa-fw fa-2x\"></i></a>";
	    optHtml += '</div>';
	    return optHtml;
	}
	
	//添加
	$("#materialsAdd").click(function () {
	    cbms.getDialog("新增材质", Context.PATH + "/resource/materials/create.html");
	});
