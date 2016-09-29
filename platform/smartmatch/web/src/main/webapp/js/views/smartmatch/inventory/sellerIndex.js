/**
*
*卖家资源统计js
* created by peanut on 2016-02-17
*/

//全局变量属性
var _sellerStatisticAttr={
		dt:null             //datatables 表格
}

$(document).ready(function() {
	
	 var url = Context.PATH + "/smartmatch/inventory/seller/statistic/search.html";
	 _sellerStatisticAttr.dt = jQuery("#dynamic-table").DataTable({
        "processing" : false,
        "serverSide" : true,
        "searching" : false,
//        "ordering" : false,
        "paging" : false,
//        "autoWidth": false,
//		"iDisplayLength" : 50,
//		"aLengthMenu" : [10,30,50,100],//定义每页显示数据数量
        "ajax" : {
            "url" : url,
            "type" : "POST",
            data : function(d) {
                return $.extend({}, d, {
                	  dt : $("#dt").val(),
                     areaId:$("#area").val(),
                     rType:$("#rType").val()
                });
            }
        },
        columns : [
            {data : 'no'},
            {data : 'accountName'},
            {data : 'resourceCount'},
            {data : 'dailyNormalUpdateCount'},
            {data : 'dailyInqueryUpdateCount'},
        ]
    });
	 
	 $("#btnSearch").on(ace.click_event, function() {
		 cbms.loading();
		 _sellerStatisticAttr.dt.ajax.reload(function(){
			 cbms.closeLoading();
		 });
	    });
});