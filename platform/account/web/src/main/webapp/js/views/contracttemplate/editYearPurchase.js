var _editor;
jQuery(function($) {
    $("#btnSubmit").attr('disabled', true);
    
    KindEditor.ready(function(K) {
        _editor = K.create('textarea[name="content"]', {
            cssPath : Context.PATH+'/theme/default/js/kindeditor-4.1.9/plugins/code/prettify.css',
            uploadJson : Context.PATH+'/theme/default/js/kindeditor-4.1.9/jsp/upload_json.jsp',
            fileManagerJson : Context.PATH+'/theme/default/js/kindeditor-4.1.9/jsp/file_manager_json.jsp',
            allowFileManager : true,
            filterMode: false,//是否开启过滤模式
            afterBlur: function(){
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
        });
        prettyPrint();
    });
    
    //提交模板
    $(document).on("click","#btnSubmit",function(){
    	 $('#yearPurchaseForm').submit(function() {
 	        if (setlistensSave("#yearPurchaseForm")) {
 	            $(this).ajaxSubmit({
 	                success : function(data) {
 	                    cbms.alert(data.data,function(){
 	                        if(data.success){
 	                        	location.href = Context.PATH + "/company/" + $("#accountId").val() + "/credentialsinfo.html";
 	                        }
 	                    });
 	                }
 	            });
 	        } else {
 	
 	        }
 	        return false;
 	    });
    });
    
    //恢复系统默认模板
    $(document).on("click","#btnRecover",function(){
    	 cbms.confirm("确定要恢复到系统默认的模板吗？", null, function () {
    		 $(this).ajaxSubmit({
			 	url :  Context.PATH + "/agreementTemplate/recoveryearpurchase.html?accountId="+$("#accountId").val(),
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
    
    $("#renew").click(function(){
		var checked=$(this).prop('checked');
		$("#renewAfterExpriration").val(checked ? "1" : "0");
	})
    
});
