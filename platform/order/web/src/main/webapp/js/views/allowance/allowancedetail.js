$().ready(function () {
    $(".img-box").click(function () {
		var src = $("#turnImg").attr("src");
		renderImg(src);
	});
});