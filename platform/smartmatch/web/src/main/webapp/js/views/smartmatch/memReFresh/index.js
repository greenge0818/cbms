$(document).ready(function () {
    $("#freshBtn").on("click", function () {
    	$.ajax({
            type: 'post',
            url: Context.PATH + "/smartmatch/memReFresh/refresh.html",
            data: {
            	memKey:$("#memKey").val()
            },
            success: function (result) {
            	if(result.success){
            		cbms.alert("刷新成功!");
            	}else{
            		cbms.alert(result.data);
            	}
            }
        });
    	
    });
});