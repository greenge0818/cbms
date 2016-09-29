$(document).on("click", "#saveInfoBtn", function () {
	if (!setlistensSave("#form1"))return;
	var configJSON = [];
	$("#dynamic-table tbody tr").each(function () {
		var config = {};
		config.id=$(this).find("input[name='id']").val();
		config.orgId=$(this).find("input[name='orgId']").val();
		config.orgName=$(this).find("input[name='orgName']").val();
		config.userId=$(this).find("input[name='userId']").val();
		config.userName=$(this).find("input[name='userName']").val();
		config.type=$(this).find("input[name='type']").val();
		config.orderStatusCode=$(this).find("input[name='orderStatusCode']").val();
		config.orderStatusName=$(this).find("input[name='orderStatusName']").val();
		config.operatorMobile=$(this).find("input[name='operatorMobile']").val();
		config.operatorId=$(this).find("input[name='operatorId']").val();
		config.operatorName=$(this).find("input[name='operatorName']").val();
		config.operatorMobile=$(this).find("input[name='operatorMobile']").val();
		config.operatorRoleName=$(this).find("input[name='operatorRoleName']").val();
		configJSON.push(config);
	});
	var str = JSON.stringify(configJSON);
	cbms.loading();
	$.ajax({
        type: 'post',
        url: Context.PATH + "/sys/busiprocess/save.html",
        data: {
        	configJSON: str
        },
        error: function (s) {
            $("#saveInfoBtn").prop("disabled", false);
            cbms.closeLoading();
        }
        , success: function (result) {
            cbms.closeLoading();
            if (result) {
                if (result.success) {
                   cbms.alert(result.data,function(){
                   	location.reload();
                   });
                }else {
                    $("#saveInfoBtn").prop("disabled", false);
                    cbms.alert(result.data);
                }
            } else {
                $("#saveInfoBtn").prop("disabled", false);
                cbms.alert("提交失败");
            }
        }
    });
});