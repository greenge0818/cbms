/**
 * create by lixiang
 * 选择银行
 */

$().ready(function(){
	
$("#bankList").change(function(){
	var option = $("#bankList").find("option:selected");
//	var bnBranch = $(option).attr("bnBranch");
	var bCode = $(option).attr("bCode");
//	$("#bnBranch").val(bnBranch);
	$("#bCode").val(bCode);
});


//默认选择第一个
$("#bankList").change();


});