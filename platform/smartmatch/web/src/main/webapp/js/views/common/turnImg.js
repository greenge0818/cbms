/**
 * Created by Rabbit on 2015-11-12 20:48:41
 */

var trsn = 0;
$(".revolveImg").click(function(){
  trsn -= 90;
  var img = $("#turnImg").get(0);
  img.style.transform = "rotate(" + trsn + "deg)";

});
$("#showEx").click(function(){
  cbms.getDialog('银票示例图','<img src="' + Context.PATH + '/img/upload/u238.png" />');
});
/**银票多张图片一张一张浏览**/
$(".img-sliders-nav").on("click","a",function(){
  $(".img-sliders-nav img").removeAttr("style");
  $(this).find("img").css({"border":"2px solid #337ab7"});
  var r = $(this).find("img").attr("src");
  $("#turnImg").attr("src", r);
  $("#turnImg").css("transform","rotate(0deg)");
  var idx = $(".img-sliders-nav img").index($(this).find("img"));
  $("#turnImg").attr("rel",idx);

});
$(".sliders-prve").click(function(){
  $("#turnImg").css("transform","rotate(0deg)");
  $(".img-sliders-nav img").removeAttr("style");
  var rel = parseInt($("#turnImg").attr("rel")),
      rel = rel < 0?$(".img-sliders-nav img").length:rel;
  url = $(".img-sliders-nav img").eq(rel-1).attr("src");
  $(".img-sliders-nav img").eq(rel-1).css({"border":"2px solid #337ab7"});
  $("#turnImg").attr("rel",rel-1);
  $("#turnImg").attr("src",url);
});
$(".sliders-next").click(function(){
  $("#turnImg").css("transform","rotate(0deg)");
  $(".img-sliders-nav img").removeAttr("style");
  var rel = parseInt($("#turnImg").attr("rel")),
      rel = rel >= $(".img-sliders-nav img").length-1? -1 : rel;
  url = $(".img-sliders-nav img").eq(rel+1).attr("src");
  $(".img-sliders-nav img").eq(rel+1).css({"border":"2px solid #337ab7"});
  $("#turnImg").attr("rel",rel+1);
  $("#turnImg").attr("src",url);
});