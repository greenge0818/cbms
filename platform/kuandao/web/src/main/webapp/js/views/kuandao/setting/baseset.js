jQuery(function($){

	$("#addForwarder").click(function () {
			var ele = '<li><label><input  type="text" name="email" value="" /></label>&nbsp;'+
			'<a href="javascript:;" title="添加" id="delForwarder"><i class="ace-icon glyphicon glyphicon-minus"></i></a></li>';
			$(this).closest("ul").find("li").last("li").after(ele);

	});

	$(document).on("click", "#delForwarder", function () {

			var v = $(this).closest("li").find(".fowarder-name").val();
			//if(v.length > 0){}
			$(this).closest("li").remove();

	})
	$("#addForwarder1").click(function () {
		var ele = '<li><label><input  type="text" name="phonenumber" value="" /></label>&nbsp;'+
		'<a href="javascript:;" title="添加" id="delForwarder"><i class="ace-icon glyphicon glyphicon-minus"></i></a></li>';
		$(this).closest("ul").find("li").last("li").after(ele);
	});
	$("#addForwarder2").click(function () {
		var ele = '<li><label><input  type="text" name="limitBankName" value="" placeholder="请输入名称"/>&nbsp;&nbsp;<input type="text" name="limitBankId" value="" placeholder="请输入ID号"></label>&nbsp;'+
		'<a href="javascript:;" title="添加" id="delForwarder"><i class="ace-icon glyphicon glyphicon-minus"></i></a></li>';
		$(this).closest("ul").find("li").last("li").after(ele);
	});
	
	
	$("#submitBtn").click(function () {
		
		cbms.confirm('您确定要修改参数吗？','',function(){
			cbms.loading();
			$.ajax({
	            type: 'post',
	            url: Context.PATH + "/kuandao/setting/modifyBaseset.html",
	            data: $("#basesetForm").serialize(),
	            error: function (s) {
	            	cbms.closeLoading();
	            }
	            , success: function (result) {
	            	cbms.closeLoading();
	                if (result && result.success) {
	                    cbms.alert("参数信息保存成功",function(){
	                    	window.location.href = Context.PATH + "/kuandao/setting/baseset.html";
	                    });
	                } else {
	                    cbms.alert(result.data);
	                }
	            }

	        });
		});
	})

});
