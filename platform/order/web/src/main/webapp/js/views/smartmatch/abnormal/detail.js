var _Detail = new function() {
    this.dt;
}
$(document).ready(function(){
    searchClick();
    var url = Context.PATH + "/smartmatch/abnormal/search.html";
    _Detail.dt=jQuery("#dynamic-table").DataTable({
        "processing" : false,
        "serverSide" : false,
        "searching" : false,
        "ordering" : false,
        "paging" : true,
        "bInfo" : false,
        "autoWidth": true,
        "iDisplayLength": 50,
        "aLengthMenu": [10, 30, 50, 100],//定义每页显示数据数量
        "ajax" : {
            "url" : url,
            "type" : "POST",
            data : function(d) {
                return $.extend({}, d, {
                    reportResourceInventoryId : DetailPage.ID,
                    sellerName : $("#sellerName").val(),
                    categoryName : $("#category").val(),
                    factory : $("#factory").val(),
                    warehouse : $("#warehouse").val()
                });
            }
        },
        columns : [
            {data : 'categoryName'},
            {data : 'materialName'},
            {data : 'spec'},
            {data : 'factory'},
            {data : 'warehouse'},
            {data : 'price'},
            {data : 'sellerName'},
        ]
    });

    //清空按钮
    $(document).on("click", "#cleanSearch", function () {
        resetForm($("form.form-inline"));
    });
});

function searchClick(){
    jQuery("#searchList").on(ace.click_event, function() {
        searchData(true);
    });
}

function searchData(isNewSearch) {
	if(isNewSearch){
		_Detail.dt.ajax.reload();
	}else{
		_Detail.dt.ajax.reload(null, false);
	}
}