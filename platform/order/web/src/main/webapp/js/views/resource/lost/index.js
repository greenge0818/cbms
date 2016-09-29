/**
*
*缺失资源页面js
*/

/**
 * 缺失资源全局变量
 */
var _lostResourceAttr={
		dt:null        //datatables
}
$(document).ready(function() {
	//清空按钮
	$("#cleanSearch").on("click", function () {
		resetForm($("form.form-inline"));
		$("#sourceType").find("option[text='不限']").attr("selected",true);
	});
	
	if(!utils.isEmpty(tabIndex)){
		var index = parseInt(tabIndex);
		$("#myTab4 li").removeClass("active");
		$("#myTab4").find("li:eq("+index+")").addClass("active").find("a").attr("href","javascript:;;");
	}
	 var url = Context.PATH + "/resource/lost/search.html";
	 _lostResourceAttr.dt = jQuery("#dynamic-table").DataTable({
	        "processing" : false,
	        "serverSide" : true,
	        "searching" : false,
	        "ordering" : false,
	        "paging" : true,
	        //"bInfo" : false,
	        "autoWidth": false,
			"iDisplayLength" : 50,
			"aLengthMenu" : [10,30,50,100],//定义每页显示数据数量
	        "ajax" : {
	            "url" : url,
	            "type" : "POST",
	            data : function(d) {
	                return $.extend({}, d, {
	                	sourceType:$("#sourceType").val(),
	                	areaId:$("#area").val(),
	                	tStart:$("#tStart").val(),
	                	tEnd:$("#tEnd").val()
	                });
	            }
	        },
	        columns : [
	            {data : 'no'},
	            {data : 'purchaseDate'},
	            {data : 'purchaseCityNames',"mRender":function(e,t,f){if(utils.isEmpty(e)){return '全国';}else{return e;}}},
	            {data : 'categoryName'},
	            {data : 'purchaseMaterialNames'},
	            {data : 'purchaseSpec'},
	            {data : 'purchaseFactoryNames'},
	            {data : 'sourceType'}
	        ]
    });
	 
	 //搜索
	 $("#searchList").on(ace.click_event, function() {
		 cbms.loading();
		 _lostResourceAttr.dt.ajax.reload(function(){
			 cbms.closeLoading();
		 });
	 });
});