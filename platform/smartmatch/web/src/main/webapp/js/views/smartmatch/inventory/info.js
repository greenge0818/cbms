$(document).ready(function () {
    $("#btnSearch").click(function(){
        var dt = $("#dt").val(),
            areaId= $("#area").val();

        window.location.href = Context.PATH + "/smartmatch/inventory/info.html?tabIndex="+tabIndex+"&areaId="+areaId+"&dt="+dt;
    });
    
    if(!utils.isEmpty(tabIndex)){
		var index = parseInt(tabIndex);
		$("#myTab4 li").removeClass("active");
		$("#myTab4").find("li:eq("+index+")").addClass("active").find("a").attr("href","javascript:;;");
	};

    $("#cityName").html($("#area").find("option:selected").text());
    
    //卖家统计跳转
    $("#sellerResourceStatisticBtn").click(function(){
        window.location.href = Context.PATH + "/smartmatch/inventory/seller/statistic/index.html";
    });
})