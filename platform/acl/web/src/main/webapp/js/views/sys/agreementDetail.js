var _editor;
jQuery(function ($) {
    KindEditor.ready(function (K) {
        _editor = K.create('textarea[name="preContent"]', {
            cssPath: Context.PATH + '/theme/default/js/kindeditor-4.1.9/plugins/code/prettify.css',
            uploadJson: Context.PATH + '/theme/default/js/kindeditor-4.1.9/jsp/upload_json.jsp',
            fileManagerJson : Context.PATH+'/theme/default/js/kindeditor-4.1.9/jsp/file_manager_json.jsp',
            allowFileManager: true,
            filterMode: false,//是否开启过滤模式
            afterBlur: function () {
                this.sync();
                //启用提交按钮
                $("#btnSubmit").removeAttr("disabled");
            }
        });
        prettyPrint();
    });

    //表单提交(submit表单提交 编辑器的内容和textarea内容同步)
    $('#contracttemplateForm').submit(function () {
        if (setlistensSave("#contracttemplateForm")) {
            $(this).ajaxSubmit({
                success: function (result) {
                    if (result.success) {
                        cbms.gritter("提交成功", true, function () {
                            location.href = Context.PATH + "/sys/template.html";
                        });
                    } else {
                        cbms.gritter(result.data, false);
                    }
                }
            });
        } else {

        }
        return false;
    });

    //发布系统默认协议模板
    $(document).on("click", "#btnRelease", function() {
        cbms.confirm("确定要发布该协议模板吗？",null,function(){
            cbms.loading();
            $.ajax({
                type : "POST",
                url : Context.PATH + "/sys/template/release.html",
                data : {
                    "id" : $("#id").val(),
                    "type" : $("#type").val()
                },
                success : function(result) {
                    cbms.closeLoading();
                    if (result) {
                        if (result.success) {
                            cbms.gritter("发布成功", true, function () {
                                location.href = Context.PATH + "/sys/template.html";
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
        });

    });

    //不发布修改的系统默认合同模板
    $(document).on("click", "#btnNotRelease", function() {
        cbms.confirm("确定要不发布该协议模板吗？",null,function(){
            cbms.loading();
            $.ajax({
                type : "POST",
                url : Context.PATH + "/sys/template/doNotRelease.html",
                data : {
                    "id" : $("#id").val()
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
        });
    });

});
