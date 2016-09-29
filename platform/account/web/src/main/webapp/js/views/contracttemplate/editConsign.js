var _editor;
jQuery(function ($) {
    //禁用提交按钮
    if ($("#isReload").val() == 'false') {
        $("#btnSubmit").attr('disabled', true);
    }

    KindEditor.ready(function (K) {
        _editor = K.create('textarea[name="content"]', {
            cssPath: Context.PATH + '/theme/default/js/kindeditor-4.1.9/plugins/code/prettify.css',
            uploadJson: Context.PATH + '/theme/default/js/kindeditor-4.1.9/jsp/upload_json.jsp',
            fileManagerJson : Context.PATH+'/theme/default/js/kindeditor-4.1.9/jsp/file_manager_json.jsp',
            allowFileManager: true,
            filterMode: false,//是否开启过滤模式
            afterBlur: function () {
                this.sync();
                //启用提交按钮
                $("#btnSubmit").removeAttr("disabled");
            },
            items:[
                'source', '|',
                'undo', 'redo', '|',
                'justifyleft', 'justifycenter', 'justifyright','removeformat','|',
                'fontname', 'fontsize', '|',
                'forecolor', 'hilitecolor', 'bold', 'italic', '|',
                'table', 'hr',  'pagebreak', 'anchor', 'link', 'unlink', '|',
                'about'
                //'image','multiimage', 'flash','media','preview', 'print','code', 'template','cut', 'copy', 'paste',
                //'plainpaste', 'wordpaste','insertorderedlist', 'justifyfull',  'insertunorderedlist', 'indent', 'outdent',
                // 'subscript', 'superscript','formatblock','underline','strikethrough','lineheight',
                // 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
                //'insertfile',   'emoticons', 'baidumap',
            ]
            /*,
            //监听编辑器内容发生改变的事件(有问题)
            afterChange: function () {
                /!*!//判断是否输入了内容
                 var count = this.count; //获取编辑器输入内容总数
                 if(count > 0)
                 {
                 alert("您尚未输入内容!");
                 return;
                 }
                 $("#btnSubmit").removeAttr("disabled");
                 *!/
            }*/

        });
        prettyPrint();
    });

    //恢复到系统默认模板
    $(document).on("click", "#btnRecover", function () {
    	cbms.confirm("确定要恢复到系统默认的模板吗？", null, function () {
   		 $(this).ajaxSubmit({
			 	url :  Context.PATH + "/agreementTemplate/recoverconsign.html?accountId="+$("#accountId").val(),
               success : function(data) {
                   cbms.alert(data.data,function(){
                       if(data.success){
                       	location.href = Context.PATH + "/company/" + $("#accountId").val() + "/credentialsinfo.html";
                       }
                   });
               }
           });
        });

    });

    //表单提交(submit表单提交 编辑器的内容和textarea内容同步)
    $('#contracttemplateForm').submit(function () {
        if (setlistensSave("#contracttemplateForm")) {
            $(this).ajaxSubmit({
                success: function (result) {
                    if (result.success) {
                        cbms.gritter("提交成功", true, function () {
                            location.href = Context.PATH + "/company/" + $("#accountId").val() + "/credentialsinfo.html";
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





});
