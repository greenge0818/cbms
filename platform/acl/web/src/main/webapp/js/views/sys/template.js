/**
 * 合同模板设置页面js
 */

var _preEnabled;

$(document).ready(function() {	
	_preEnabled = $("input[name='check']:checked").val();
	
    //设置买家银票支付方式
    $(document).on("click", "input[name='check']", function() {
    	var self = this;
    	if($(self).val() != _preEnabled){
    		var enabled = $(self).val() == "开启"?true:false;
        	var id = $(self).closest(".buyerspaydrafts").attr("id");
        	cbms.confirm("确定要将买家银票支付方式设置成“"+$(self).val()+"”？",null,function(){
        		//请求
                var url = Context.PATH + '/sys/template/enabled.html';
                $.post(url, {
                	enabled : enabled,
                    id : id
                }, function(result) {
                    if (result) {
                        if (result.success) {
                            cbms.gritter("买家银票支付方式设置成功！",true);
                            _preEnabled = $("input[name='check']:checked").val();
                        } else {
                            cbms.gritter(result.data,false);
                        }
                    }
                });
        	},function(){
        		 $("input[name='check']:not(:checked)").prop("checked", true);
        	}); 
    	}else{
    		return false;
    	}
    });
	
    //发布系统默认合同模板
	$(document).on("click", "#btnRelease", function() {
		var id = $(this).attr("val");
        var type = $(this).attr("type");
		cbms.confirm("确定要发布该合同模板吗？",null,function(){
			release(id, type);
		});
	});

    //不发布修改的系统默认合同模板
    $(document).on("click", "#btnNotRelease", function() {
        var id = $(this).attr("val");
        cbms.loading();
        $.ajax({
            type : "POST",
            url : Context.PATH + "/sys/template/doNotRelease.html",
            data : {
                "id" : id
            },
            success : function(result) {
                cbms.closeLoading();
                if (result) {
                    if (result.success) {
                        location.href=Context.PATH + "/sys/template.html";
                    } else {
                        cbms.alert(result.data);s
                    }
                }
            },
            error : function(xhr, textStatus, errorThrown) {
                cbms.closeLoading();
            }
        });
    });
	
	$(document).on("click", "#btnPublish", function() {
		location.href=Context.PATH + "/sys/template/detail.html?id=" + $(this).attr("templateId") + "&type=" + $(this).attr("templateType") + "&action=release";
	});

    //弹出页面查看图片
    $(document).on("click", ".img-box", function () {
        var $img = $(this).next("img"), tit = $img.attr("alt");
        var src =  $(this).find("img").attr("src");
        renderImg(encodeURI(src));
    });

});

function release(id, type){
	cbms.loading();
        $.ajax({
            type : "POST",
            url : Context.PATH + "/sys/template/release.html",
            data : {
                "id" : id ,
                "type" : type
            },
            success : function(result) {
                cbms.closeLoading();
                if (result) {
                    if (result.success) {
                    	location.href=Context.PATH + "/sys/template.html";
                    } else {
                        cbms.alert(result.data);
                    }
                }
            },
            error : function(xhr, textStatus, errorThrown) {
                cbms.closeLoading();
            }
        });
}

function save(){
    cbms.loading();
    $.ajax({
        type : "POST",
        url : Context.PATH + "/sys/template/save.html",
        data : {
            "id" : TEMPLATE_DETAIL.id,
            "preContent" : $("textarea").val(),
			"sysTemplateStatus":0
        },
        success : function(result) {
            cbms.closeLoading();
            if (result) {
                if (result.success) {
                    cbms.alert("操作成功！",function(){
                    	location.href=Context.PATH + "/sys/template.html";
                    });
                } else {
                    cbms.alert(result.data);
                }
            }
        },
        error : function(xhr, textStatus, errorThrown) {
            cbms.closeLoading();
        }
    });
}


