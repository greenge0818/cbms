$(document).ready(function(){
	$("#saveInfoBtn").click(function(){
		save()
	});
});

function save(){
	cbms.loading();
	$.ajax({
		type: "post",
		url: Context.PATH + "/account/contracttemplate/save.html",
		data: {
			id:$("#tid").val(),
			content:$("textarea.clause").val()
		},
		success: function(res){
			cbms.closeLoading();
			if (result.success) {
				cbms.alert(msg+"³É¹¦");
			} else {
				cbms.alert(result.data);
			}
		},
		error: function(s){
			cbms.closeLoading();
			cbms.alert(s);
		}
	});
}