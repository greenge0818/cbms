jQuery(function($) {
	function huazhuan() {
		var managerExIds = "", nameExs = "", type = "";
		$("#duallist option:selected").each(function() {
			if (managerExIds == "") {
				managerExIds = $(this).attr("uid");// 前任ID
				nameExs = $(this).attr("nameex");// 前任名字
				type = $(this).attr("type");// 客户类型
			} else {
				managerExIds += "," + $(this).attr("uid");
				nameExs += "," + $(this).attr("nameex");
				type += "," + $(this).attr("type");
			}
		});
		var ids = $("#duallist").val().toString();//客户ID
		var uid = $("#userId").val(); // 现任ID
		var nameNext = $("#userId option:selected").attr("nameNext");// 现任名字
		var nameExStr = [];
		nameExStr = nameExs.split(",");
		for (var int = 0; int < nameExStr.length; int++) {
			var nameEx = nameExStr[int];
			if(nameEx==nameNext){
				cbms.alert("不能划转给自己！");
				return;
			}
		}
		function refreash(uid, nameNext) {
			$("#userId option").each(function() {
				// alert(uid + '==' + nameNext);
				// $("#nameex").val(nameNext);
				$(this).val(nameNext);
			});
		}

		$.ajax({
			type : "POST",
			url : Context.PATH + "/account/buyer/updaccountssign.html",
			data : {
				"ids" : ids,
				"uid" : uid,
				"managerExIds" : managerExIds,
				"nameExs" : nameExs,
				"nameNext" : nameNext,
				"type" : type
			},
			success : function(result) {
				cbms.alert(result);
				//cbms.alert(this, result);
				setTimeout(function() {
					location.reload(true);
				}, 1500);
			},
			error : function(xhr, textStatus, errorThrown) {
			}
		});
	}

	$("#trans").click(function() {
		huazhuan();
	})
})
