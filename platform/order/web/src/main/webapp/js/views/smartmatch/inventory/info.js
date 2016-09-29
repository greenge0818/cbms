$(document).ready(function () {
    $("#btnSearch").click(function(){
        var dt = $("#dt").val(),
            areaId= $("#area").val();

        window.location.href = Context.PATH + "/smartmatch/inventory/info.html?areaId="+areaId+"&dt="+dt;
    });

    $("#cityName").html($("#area").find("option:selected").text());
    
    //卖家统计跳转
    $("#sellerResourceStatisticBtn").click(function(){
        window.location.href = Context.PATH + "/smartmatch/inventory/seller/statistic/index.html";
    });
})