jQuery(function ($) {
	initOrg();
    $("#form").verifyForm();
    $(document).on("click", "#return", function () {
        location.href = Context.PATH + "/acceptdraft/list.html";
    });

    $(document).on("click", "#save", function () {
        if (setlistensSave("#form")) {
            $("input[name='operation']").val("save");
            submitForm("save");
        }
    });

    $(document).on("click", "#submit", function () {
        if (setlistensSave("#form")) {
            $("input[name='operation']").val("submit");
            submitForm("submit");
        }
    });
    $(document).on("click", ".fa-close", function () {
        ids = ids+$(this).attr("id") + "|";
        $(this).closest("span").remove();
        picAmount--;
    });
    //$(document).on("change", "#amountShow", function(){
    //    $(this).val($(this).val().replace(/,/gm,""));
    //})
});

function submitForm(sbt) {
    if(!checkUploadpics()){
        return false;
    }
    if(ids != ''){
        $("#imgsId").val(ids.substring(0,ids.length-1));
    }
    $("#"+sbt).prop("disabled",true);


    $("input[name='amount']").val($("#amountShow").val().replace(/,/gm,""));
    $('#form').ajaxSubmit({
        success: function (data) {
            cbms.alert(data.data, function(){
                if(data.success){
                    location.href = Context.PATH + "/acceptdraft/list.html";
                }else{
                    $("#"+sbt).prop("disabled",false);
                }
            });
        }
    });
}
//校验最多能上传10张照片 add by wangxj 20151209 迭代8
function checkUploadpics(){
    var url = getFileUrl("pic");
    //var picAmount =  $("#pic_amount").val();
    if(picAmount >0){
        if(url && (parseInt(url.length,10) + picAmount > 10 || (parseInt(url.length,10) + picAmount) == 0)){
            cbms.alert("必需上传图片，且上传图片不能超过10张！");
            return false;
        }
    }else{
        if(url && (url.length > 10 || url.length == 0)){
            cbms.alert("必需上传图片，且上传图片不能超过10张！");
            return false;
        }
    }
    return true;
}

function initOrg(){
	 $.ajax({
	        type: "POST",
	        url: Context.PATH + '/acceptdraft/queryDraftedOrg.html',
	        data: {},
	        dataType: "json",
	        success: function (response, textStatus, xhr) {
	            if (response.success) {
	                var datas = response.data;
	                $("#orgId").empty();
	                $("#orgId").append('<option value ="">请选择</option>');
	                for (var i in datas) {
	                    $("#orgId").append('<option value ="' + datas[i].id + '">' + datas[i].name + '</option>');
	                }
	                var selectedOrgId = $('#orgId').attr('value');
	                $('#orgId option[value="'+selectedOrgId+'"]').attr("selected",true);
	            }
	        },
	        error: function (xhr, textStatus, errorThrown) {
	        }
	    });
}