//加载所有城市
$(document).ready(function () {
	$.ajax({
		type: "POST",
		url: Context.PATH+ '/common/allcity.html',
		dataType: "json",
		success: function (response, textStatus, xhr) {
			Context.CITYDATA=response;
		}
	});
});
//资源全局变量
var _resAttr={
	//卖家数据
	accountCacheData:[],
	warehouseData:[]
};

/**
 * 获取一些通用的全局数据：所有卖家
 */
(function getCommonData(){
	$.ajax({
        type: 'post',
        url: Context.PATH + "/resource/getCommonData.html",
        success: function (result) {
        	if(result.success){
        		_resAttr.accountCacheData=result.data.accountList;
        		_resAttr.warehouseData=result.data.warehouseList;
        	}
        }
    });
})();

// 卖家输入框点击事件
$(document).on('input propertychange',"input[name='account']", function () {
    showPYMatchList($(this),_resAttr.accountCacheData,"id","name",null);
    $("#dropdown").css("z-index",9999);
});

//仓库
$(document).on('input propertychange',"input[name='warehouse']", function (e) {
	showPYMatchList($(this),_resAttr.warehouseData,"id","name",fillCity);
	cbms.stopF(e);
});

//仓库联想完以后自动填充城市
function fillCity(id){
	$.ajax({
		type : "POST",
		url : Context.PATH + "/smartmatch/inquiryorder/getCityByWarehouseId.html",
		data : {
			warehouseId:id
		},
		dataType : "json",
		success : function(response){
			$("input[fill='true']").eq(0).val(response.data);
		},
	});
	
}
//规格
//$(document).on('input propertychange',"input[name='specs']", function (e) {
//	var input = this;
//	var spec = $.trim($(input).closest(".m-search").find('input[name="specs"]').val());
//	if(spec != ''){
//		$.ajax({
//			type: "POST",
//			url: Context.PATH+ '/smartmatch/quote/matchresource.html',
//			dataType: "json",
//			data:{conditionName:'spec',spec:spec},
//			success: function (response, textStatus, xhr) {
//				if(response.length > 0){
//					showPYMatchList($(input),response,"spec","spec",null);
//					cbms.stopF(e);
//				}
//			}
//		});
//	}
//});

//品名联想显示
$(document).on('input propertychange',"input[name='categorys']", function (e) {
	var input = this;
	var spec = $.trim($(input).closest(".m-search").find('input[name="specs"]').val());
	var cityName = $.trim($(input).closest(".m-search").find('input[name="citys"]').val());
	$.ajax({
		type: "POST",
		url: Context.PATH+ '/smartmatch/quote/matchresource.html',
		dataType: "json",
		data:{conditionName:'category',spec:spec,cityName:cityName},
		success: function (response, textStatus, xhr) {
			if(response.length > 0){
				showPYMatchList($(input),response,"categoryUuid","categoryName",null);
				cbms.stopF(e);
			}
		}
	});
});

//材质联想显示
$(document).on('input propertychange',"input[name='materials']", function (e) {
	var input = this;
	var spec = $.trim($(input).closest(".m-search").find('input[name="specs"]').val());
	var categoryName = $.trim($(input).closest(".m-search").find('input[name="categorys"]').val());
	var cityName = $.trim($(input).closest(".m-search").find('input[name="citys"]').val());
	$.ajax({
		type: "POST",
		url: Context.PATH+ '/smartmatch/quote/matchresource.html',
		dataType: "json",
		data:{conditionName:'material',spec:spec,categoryName:categoryName,cityName:cityName},
		success: function (response, textStatus, xhr) {
			if(response.length > 0){
				showPYMatchList($(input),response,"materialUuid","materialName",null);
				cbms.stopF(e);
			}
		}
	});
});

//钢厂联想
$(document).on('input propertychange',"input[name='factorys']", function (e) {
	var input = this;
	var spec = $.trim($(input).closest(".m-search").find('input[name="specs"]').val());
	var categoryName = $.trim($(input).closest(".m-search").find('input[name="categorys"]').val());
	var materialName = $.trim($(input).closest(".m-search").find('input[name="materials"]').val());
	var cityName = $.trim($(input).closest(".m-search").find('input[name="citys"]').val());
	$.ajax({
		type: "POST",
		url: Context.PATH+ '/smartmatch/quote/matchresource.html',
		dataType: "json",
		data:{conditionName:'factory',spec:spec,categoryName:categoryName,materialName:materialName,cityName:cityName},
		success: function (response, textStatus, xhr) {
			if(response.length > 0){
				showPYMatchList($(input),response,"factoryName","factoryName",null);
				cbms.stopF(e);
			}
		}
	});
});

//交货地提示
$(document).on('input propertychange',"input[name='citys']", function (e) {
	var input = this;
	showPYMatchList($(input),Context.CITYDATA,"name","name",null);
	cbms.stopF(e);
});

//查询复制
$(document).on("click", ".clone-btn", function () {
	var c = $(this).closest(".m-search").clone();
	//复制后隐藏按钮
	$(c).closest(".m-search").find('button[id="searchList"]').css('display','none');
	$(c).closest(".m-search").find('span[id="cleanSearch"]').css('display','none');
	$(this).closest(".m-search").after(c);
	$(c).find(".spec .f-text").inputFocus();

	$(".show-layer").hide();
});

//删除复制
$(document).on("click", ".del-btn", function () {
	if ($("div[id='m-searchDiv']").length > 1) {
		$(this).closest(".m-search").remove();
		var input =  $(".m-search")[0];
		//删除的是第一行,显示按钮
		if($(input).closest(".m-search").find('button[id="searchList"]').css('display') == 'none'){
			$($(input).closest(".m-search").find('button[id="searchList"]').css('display',''));
			$($(input).closest(".m-search").find('span[id="cleanSearch"]').css('display',''));
		}	
	} else {
		cbms.alert("最少需要保留一条资源搜索条件");
	}
});

//清空按钮
$(document).on("click", ".otherbtn",function () {
	var input = this;
	var id = $(this).attr("id");
	if('cleanSearch' == id){
		$(input).closest(".m-search").find('input').val('');
	}
});
