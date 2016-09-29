 var _attrTable;
$(document).ready(function () {
	_attrTable = $('#dynamic-table').dataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "autoWidth": false,
        "iDisplayLength" : 50,
		"aLengthMenu" : [10,30,50,100],//定义每页显示数据数量
        "ajax": {
            url: Context.PATH + '/resource/attribute/edit/searchAttr.html',
            type: 'POST',
            data: function (d) {
                d.attrName = $("#attrName").val();
                d.attrType = $("select option:selected").val();
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {

            // operation button
            $('td:last', nRow).html(generateOptHtml($.trim(aData.id)));
        },
        columns: [
            {data: 'name'},
            {data: 'type'},
            {data: 'options'},
            {defaultContent:""}
        ]
    });

    $("#queryBtn").on("click", function () {
    	_attrTable.fnDraw();
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
    	cbms.getDialog("编辑属性", Context.PATH + "/resource/attribute/edit/"+id+"/detail.html");
    }
	/**
	 * 删除
	 * @param id 
	 */
    function goToDel (id) {
    	cbms.confirm("确定删除这条记录?",null,function(){
    		$.ajax({
                type: "POST",
                url: Context.PATH + '/resource/attribute/edit/'+id+'/del.html',
                dataType: "json",
                success: function (response, textStatus, xhr) {
                    if (response.success) {
                    	_attrTable.fnDraw();
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
	function generateOptHtml(id) {
	    var optHtml = '<div class="fa-hover">';
	    optHtml += "<a href='javascript:goToEdit("+id+")' target='_blank' title='编辑'>";
	    optHtml += "<i class=\"fa fa-edit fa-2x\"></i></a>";
	   	optHtml += " <a href='javascript:goToDel("+id+")' target='_blank' title='删除'><i class=\"fa fa-trash-o fa-fw fa-2x\"></i></a>";
	    optHtml += '</div>';
	    return optHtml;
	}
	//添加
	$("#attrAdd").click(function () {
	    cbms.getDialog("新增属性", Context.PATH + "/resource/attribute/edit/create.html");
	});
