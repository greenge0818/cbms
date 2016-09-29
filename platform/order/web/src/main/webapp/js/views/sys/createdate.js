/**
 * 系统设置项设置
 * Created by lx 
 */
$().ready(function() {
	$("#confirm").click(function() {
		if(!setlistensSave("#checkForm")){
			return;		
		}
		var id = $("#sysId").val();
		var settingValue = $("#times").val();
		var reportOrgDay = $("#isdefault").val();
		$.ajax({
			type : "POST",
			url : Context.PATH + "/sys/update/date.html",
			data : {
				"id" : id,
				"settingValue" : settingValue,
				"reportOrgDay":reportOrgDay
			},
			success : function(result) {
				if (result && result.success) {
					cbms.alert(result.data);
				} else {
					cbms.alert(result.data);
				}
			},
			error : function(xhr, textStatus, errorThrown) {
				cbms.alert("设置失败！");
			}
		});
	});
});