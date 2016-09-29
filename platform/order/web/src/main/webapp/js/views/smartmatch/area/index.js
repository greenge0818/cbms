 var _areaTable;
$(document).ready(function () {
	_areaTable = $('#dynamic-table').dataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "autoWidth": false,
        "iDisplayLength" : 50,
        "aLengthMenu" : [10,30,50,100],//定义每页显示数据数量
        "ajax": {
            url: Context.PATH + '/smartmatch/area/searchArea.html',
            type: 'POST',
            data: function (d) {
                d.name = $("#areaName").val();
                d.centerCityName = $("#centerCity").val();
                d.centerCityId=$("#centerCity").attr("cityid");
                d.refsCityName = $("#refsCity").val();
                d.refsCityId=$("#refsCity").attr("cityid");
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
            // operation button
            $('td:last', nRow).html(generateOptHtml($.trim(aData.areaId)));
            if(aData.checkTotalAmount && aData.checkTotalAmount > 0){
            	$('td:eq(3)', nRow).html(formatMoney(aData.checkTotalAmount,2));
            }
        },
        columns: [
            {data: 'areaId'},
            {data: 'areaName'},
            {data: 'centerCityName'},
            {data: 'refCityNames'},
            {defaultContent:""}
        ]
    });

    $("#queryBtn").on("click", function () {
    	checkCityName("centerCity");
    	checkCityName("refsCity");
    	_areaTable.fnDraw();
    });
    
    //清空按钮
    $("#cleanSearch").on("click", function () {
    	resetForm($("form.form-inline"));
    });
});
/**
 * 检验城市中文名称是否匹配
 * @param cityInput
 */
function checkCityName(cityInput){
	var cityName= $("#"+cityInput).val();
	if(!utils.isEmpty(cityName)){
		$(Context.CITYDATA).each(function (i, e) {
			if( e.name==cityName){
				$("#"+cityInput).attr("cityid",e.id);
				 	return false;
			 	}else{
			 		$("#"+cityInput).attr("cityid",-99999);
			 	}
		});
	}else{
		$("#"+cityInput).attr("cityid","");
	}
}
	/**
	 * 编辑
	 * @param id
	 */
    function goToEdit(id) {
    	cbms.getDialog("编辑区域", Context.PATH + "/smartmatch/area/"+id+"/detail.html");
    }
	/**
	 * 删除
	 * @param id
	 */
    function goToDel (id) {
    	cbms.confirm("确定删除这条记录?",null,function(){
    		$.ajax({
                type: "POST",
                url: Context.PATH + '/smartmatch/area/'+id+'/del.html',
                dataType: "json",
                success: function (response, textStatus, xhr) {
                    if (response.success) {
                    	_areaTable.fnDraw();
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
	$("#cityAdd").click(function () {
	    cbms.getDialog("新增区域", Context.PATH + "/smartmatch/area/create.html");
	});
