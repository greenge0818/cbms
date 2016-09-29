jQuery(function($) {
	KindEditor.ready(function(K) {
		var editor1 = K.create('textarea[name="content"]', {
			cssPath : Context.PATH+'/theme/default/js/kindeditor-4.1.9/plugins/code/prettify.css',
			uploadJson : Context.PATH+'/theme/default/js/kindeditor-4.1.9/jsp/upload_json.jsp',
			//fileManagerJson : Context.PATH+'/theme/default/js/kindeditor-4.1.9/jsp/file_manager_json.jsp',
			allowFileManager : true,
			afterBlur: function(){this.sync();},
			
			items:[
			        'source', '|', 'undo', 'redo', '|', 'preview', 'print',  'code', 'image', 'multiimage',//'template','cut', 'copy', 'paste',
			        'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
			        'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 
			        //'subscript',
			        //'superscript', 
			        'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
			        //'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
			        //'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 
			        
			        //'flash', 'media', 'insertfile', 'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
			        //'anchor', 'link', 'unlink', '|', 'about'
			]
		});
		prettyPrint();
	});
	
	$('#contracttemplateForm').submit(function() {
		if (setlistensSave("#contracttemplateForm")) {
			$(this).ajaxSubmit({
				success : function(data) {
					cbms.alert(data.data,function(){
						if(data.success){
							location.href=Context.PATH+"/account/"+$("#type").val()+"/"+$("#accountId").val()+"/contracttemplate.html"
						}else{
							
						}
					});
				}
			});
		} else {

		}
		return false;
	});
});
