
//规格
$(document).on('input propertychange',"input[name='spec_dia']", function (e) {
	var input = this;
	var spec = $.trim($(input).closest(".m-search").find('input[name="spec_dia"]').val());
	if(spec != ''){
		$.ajax({
			type: "POST",
			url: Context.PATH+ '/smartmatch/quote/matchresource.html',
			dataType: "json",
			data:{conditionName:'spec',spec:spec},
			success: function (response, textStatus, xhr) {
				if(response.length > 0){
					showPYMatchList($(input),response,"spec","spec",null);
					cbms.stopF(e);
				}
			}
		});
	}
});

//品名
$(document).on('input propertychange',"input[name='category_dia']", function (e) {
	var input = this;
	//var spec = $.trim($(input).closest(".m-search").find('input[name="spec_dia"]').val());
	//var cityName = $.trim($(input).closest(".m-search").find('input[name="citys"]').val());
	$.ajax({
		type: "POST",
		url: Context.PATH+ '/smartmatch/quote/matchresource.html',
		dataType: "json",
		async:false,//同步操作
		data:{conditionName:'category'},
		success: function (response, textStatus, xhr) {
			if(response.length > 0){
				showPYMatchList($(input),response,"categoryUuid","categoryName",loadPrice);
				cbms.stopF(e);
			}
		}
	});
	
});

function loadPrice(id){
	loadPriceLimit(id,$("#price"));
}

//材质联想显示
$(document).on('input propertychange',"input[name='material_dia']", function (e) {
	var input = this;
	//var spec = $.trim($(input).closest(".m-search").find('input[name="spec_dia"]').val());
	//var categoryName = $.trim($(input).closest(".m-search").find('input[name="category_dia"]').val());
	//var cityName = $.trim($(input).closest(".m-search").find('input[name="citys"]').val());
	$.ajax({
		type: "POST",
		url: Context.PATH+ '/smartmatch/quote/matchresource.html',
		dataType: "json",
		data:{conditionName:'material'},
		success: function (response, textStatus, xhr) {
			if(response.length > 0){
				showPYMatchList($(input),response,"materialUuid","materialName",null);
				cbms.stopF(e);
			}
		}
	});
});

//钢厂联想
$(document).on('input propertychange',"input[name='factory_dia']", function (e) {
	var input = this;
	//var spec = $.trim($(input).closest(".m-search").find('input[name="spec_dia"]').val());
	//var categoryName = $.trim($(input).closest(".m-search").find('input[name="category_dia"]').val());
	//var materialName = $.trim($(input).closest(".m-search").find('input[name="material_dia"]').val());
	//var cityName = $.trim($(input).closest(".m-search").find('input[name="citys"]').val());
	$.ajax({
		type: "POST",
		url: Context.PATH+ '/smartmatch/quote/matchresource.html',
		dataType: "json",
		data:{conditionName:'factory'},
		success: function (response, textStatus, xhr) {
			if(response.length > 0){
				showPYMatchList($(input),response,"factoryId","factoryName",null);
				cbms.stopF(e);
			}
		}
	});
});
