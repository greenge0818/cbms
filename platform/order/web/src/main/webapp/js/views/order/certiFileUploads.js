//批量凭证上传
//code by tuxianming

//凭证图片上传url
var _uploadfilesURL = Context.PATH + "/order/query/uploadcredentialimgs.html";
//凭证图片与上传的图片绑定信息提交的url
var _updateCredentialImgURL = Context.PATH + "/order/certificate/updateCredentialImages.html";

var browseFileBtn = ".browse-files"; //浏览文件按钮

//这个是浏览文件按钮的属性
var credentialCode = "";  	//指定的凭证号上传凭证图片的时候，将会有值 
var credentialId = "";  	//指定的凭证号上传凭证图片的时候，将会有值 
var require = "";  //是不是必须输入

var uploadedFiles = {}; //图片上传成功返回的图片对象信息， 当点击关闭图标时，这里面相关的objcet会被移除， removeObjs里面会加上这个删除的id
var updateObjs = {}; // {attachmentId, url, credentialCode, pageNum}, 当输入框的内容发生改变时，这里面会记录这张图片要绑定的相关信息
var removeObjs = []; // attachmentId 

//这个是弹窗的模版html
var dialogHtml ='<div class="myteam g_columnbox clearfix">'+
				'	<div class="myteamlist_wrap clearfix" id="myteamlistWrap">'+
				'		<div id="myteamlist" class="myteamlist outerBox clearfix">'+
				'			<ul id="myteamlistC">'+
				'			</ul>'+
				'		</div>'+
				'		<a class="prev" href="javascript:void(0);"></a><a class="next no_next" href="javascript:void(0);"></a>'+
				'		<div class="scrollpage clearfix"></div>'+
				'	</div>'+
				'</div>'+
				'<form>'+
				'	<div class="con-bar" style="margin-top:50px;">'+
				'		<ul></ul>'+
				'	</div>'+
				'	<div class="btn-bar text-center">'+
				'		<button type="button" id="save-info" class="btn btn-sm btn-primary">确定</button>'+
				'		<button type="button" id="close-dlg" class="btn btn-sm btn-default">取消</button>'+
				'	</div>'+
				'</form>';

$(document).ready(function(){
	
	//当点击浏览文件时，获取这个按钮上的一些属性，构建文件上传的form表单，再打开文件浏览器
    $(document.body).on("click", browseFileBtn, function(){
    	var _this = $(this);
    	credentialId = _this.attr("credentialId") || "";
    	credentialCode = _this.attr("credentialCode") || "";
    	require = _this.attr("require") || "";
    	//添加文件上传组件
    	addUploadForm();
    	
    });
	
	//处理文件上传，当在文件浏览器里面选中文件 以后，点击确定后，会自动上传文件 。
    //当文件上传成功后，根据 返回的url，弹出处理图片的窗口。
	$(document.body).on("change", "#filesInput", function(){
		updateObjs = {}; 
		removeObjs = [];
		doUpload();
	});
    
	//弹窗图片切换.item a
	$(document.body).on("click", "#myteamlistC .item a", function(){
		switchImage(this);
	});
	
	//处理上传文件的文件， 在弹窗中，选中某个图片时，当右侧输入相关信息的时候，会把相关的信息保存的在updateObjs
	$(document.body).on("propertychange input", ".con-bar input", function(){
		changeValue(this);
	});
	
	//删除图片， 在弹窗中点击删除图片按钮时，将该张图片从dom中称除，并将该图片的id放到removeObjs里面， 
	//同时移除： updateObjs, 和uploadedFiles里面的相关对象 ，
	$(document.body).on("click", "#myteamlistC .deleteImg", function(){
		var item = $(this);
		var parent = item.closest("li");
		var id = parent.attr("iid");
		delete updateObjs[id];
		var i = 0;
		for(; i<uploadedFiles.length; i++){
			if(uploadedFiles[i].id == id){
				break;
			}
		}
		removeObjs.push(id);
		
		parent.remove();
		var index = $("#myteamlistC li").index(parent);
		$(".con-bar li").eq(index).remove();
		
	});
	
	//提交处理过的图片信息 
	//当图片信息输入完成时，点击提交的时候，这里更新提交的信息
	$(document.body).on("click", "#save-info", function(){
		updateInfo();

	});
	
	//关闭弹窗
	$(document.body).on("click", "#close-dlg", function(){
		cbms.closeDialog();
		removeImages();
		$("#upfiles-form").remove();

		//恢复对话框右上角×号
		$("#dialogClose").html("×");
		$("#dialogClose").attr("class","d-close");
	});

	//动态获取凭证页码
	$(document).on("blur",".con-bar input[name='credentialCode']",function(){
		var _this = $(this);
		getCredentialPageNumber(_this);
	})
	
	$(document).on("click", "#unReplaceOldBtn", function(){
		updateInfo(false);
		cbms.closeDialog();
	});
	
	$(document).on("click", "#replaceOldBtn", function(){
		updateInfo(true);
		cbms.closeDialog();
	});
	
});

//动态获取凭证页码，并显示到页面上。
function getCredentialPageNumber(credentialCodeInput){
	
	var iTotalPageLabel =credentialCodeInput.parent().next();
	var iTotalPage = iTotalPageLabel.find(".iTotalPage");
	
	var code=credentialCodeInput.val();	
	
	if(code){
		$.ajax({
			type: 'post',
			url: Context.PATH + "/order/certificate/get/"+code+".html",
			async : false,
			data: {},
			error: function (s) {
			}
			, success: function (result) {
				if(result.success){
					iTotalPage.html(result.data.credentialNum);
					iTotalPageLabel.find("input[name='uploadStatus']").val(result.data.uploadStatus);
				}
			}
		});
	}
}

/**
 * 更新图片信息到后台，
 */
var called = false;
function updateInfo(replaceDuplicate){
	
	//防止多次提交 
	if(called){
		return;
	}
	called == true;
	
	
	//检测是不是为空，如果是必须, 则检测页码，如果页码为空，跳转到当前图片，并结束提交 
	//if(require){
		var items = $(".con-bar li");
		for(var i=0; i<items.length; i++){
			
			var item = items.eq(i);
			var code = item.find("input[name='credentialCode']").val();
			if(code){
				if (!setlistensSave(".con-bar li:eq("+i+") label:last")){
					items.hide();
					item.show();
					return ;
				} 
			}
			
			var pageNum = item.find("input[name='pageNum']").val();
			var total = $.trim(item.find(".iTotalPage").text()) || 0;
			var uploadStatus = item.find("input[name='uploadStatus']").val();
			
			if(uploadStatus == 'OLD_DATA'){
				continue;
			}
			
			if(code){
				if( pageNum/1 <=0){
					cbms.alert("凭证页码必须大于0");
					items.hide();
					item.show();
					return ;
				}
				
				if(pageNum/1 > total/1 ){
					cbms.alert("凭证页码必须小于等于总页码");
					items.hide();
					item.show();
					return ;
				}
			}
		}
	//}
	
		
	//检测页码是不是存在重复
	var checkResults = {};
	//将页码按code存储起来
	for(var i in updateObjs){
		if(updateObjs[i].credentialCode){
			var pageNums = checkResults[updateObjs[i].credentialCode];
			if(!pageNums){
				checkResults[updateObjs[i].credentialCode] = pageNums = [];
			}
			pageNums.push(updateObjs[i].pageNum);
		}
	}

	var duplicateCodes = [];
	//将有重复页码的code取出来
	for(var code in checkResults){
		var pageNums = checkResults[code];
		var temp = {};
		for(var j=0; j<pageNums.length; j++){
			if(!temp[pageNums[j]]){
				for(var k=0; k<pageNums.length; k++){
					if(k!=j && pageNums[j] == pageNums[k]){
						duplicateCodes.push(code);
						temp[pageNums[j]] = true;
						break;
					}
				}
			}
		}
	}
	//如果code为重复则提示，并结束当次提交 
	if(duplicateCodes.length>0){
		cbms.alert("凭证号“"+duplicateCodes.toString()+"”输入凭证页码重复，请确认后再提交");
		return;
	}
	
	//构建函数
	var params = {};
	var j=0;
	for(var i in updateObjs){
		
		if(updateObjs[i].credentialCode && updateObjs[i].pageNum){ 
			
			var prefix = 'updates['+(j++)+'].';
			params[prefix+'id'] = i;  //就是图片id
			params[prefix+'credentialCode'] = updateObjs[i].credentialCode;
			params[prefix+'pageNum'] = updateObjs[i].pageNum;
			
		}
	}
	
	if(removeObjs.length>0){
		params.removes = removeObjs.toString();
	}
	
	if(typeof(replaceDuplicate) != 'undefined'){
		params.replace = replaceDuplicate;
	}
	
	//提交到后台。
	$.post(
			_updateCredentialImgURL, 
			params,
			function(result){
				if(result.success){
					//保存成功
					called = false;
					cbms.alert("保存成功！");
					
					cbms.closeDialog();
					$("#upfiles-form").remove();
					updateObjs = {};
					removeObjs = {};
					
					//这里提供一个回调函数 ，比如可以刷新当页面的表格，或者把提交成功的图片显示在某个位置 
					//uploadedFilesc对象存储了图片相关信息
					if(window.refreshTable){
						window.refreshTable(uploadedFiles);
					}

					//恢复对话框右上角×号
					$("#dialogClose").html("×");
					$("#dialogClose").attr("class","d-close");
					
				}else{
					//这里会从后台返回一个  “1||msg”这样格式的字符串，
					//如果不是这种格式，则是错误信息，直接显示 
					//如是是这个种格式，则弹出确认框 
					var rss = result.data.split("||");
					called = false;
					
					if(rss.length==1){
						cbms.alert(result.data);
					}else{
						//cbms.confirm(rss[1], true, updateInfo, notReplaceUpdateInfo)
						
						var confirm =  '<div style="min-width: 250px; padding: 20px;"><div style="padding-bottom:20px;">'+rss[1]+'</div><div style="text-align:right;"><button class="btn btn-sm btn-default" id="unReplaceOldBtn">不替换</button>&nbsp;&nbsp;<button class="btn btn-sm btn-primary" id="replaceOldBtn">确定</button></div></div>';
						cbms.getDialog('', confirm);
						
					}
				}
			}
	);
}

/**
 * 输入框值发生变化，则将数据保存在updateObjs里面。
 * @param _this
 */
function changeValue(_this){
	var input = $(_this);
	var parent = input.closest("li");
	var id = parent.attr("iid");

	//先检测这个值 是不是在updateObjs里面已经存在，如果存在，则在原来基础上修改，如果不存在，则new一个新对象， 并存入到updateObjs
	var updateObj = updateObjs[id];
	if(!updateObj){
		updateObjs[id] = updateObj = {};
	}
	
	parent.find("input[type='text']").each(function(index, item){
		var _input = $(this);
		updateObj[_input.attr("name")] = _input.val();
	});
	
	
	var index = $(".con-bar li").index(parent);
	
	//这两个值如果有一个为空，则将图片边框设置成红色，反之设置成绿色
	if(updateObj.credentialCode && updateObj.pageNum){
		var style = "4px solid rgba(64, 187, 101, 1)";
		$("#myteamlistC li").eq(index).find("img").css("border", style);
	}else{
		var style = "4px solid rgba(203, 45, 57, 1)";
		$("#myteamlistC li").eq(index).find("img").css("border", style);
	}
}


/**
 * 上传图片
 */
function doUpload(){
	//console.log("do upload...");
	
	var options = {
		type : "POST",
		success: function(result){
			cbms.closeLoading();
			if(result.success){
				uploadedFiles = result.data;  //	[{},{}]     {}=> {fileUrl, id}
				cbms.getDialog("处理上传图片", dialogHtml);
				
				setTimeout(function () { //加载已经上传的文件到弹窗
					buildSlidebar(uploadedFiles);  //构建html， 默认显示第一张图片
					getCredentialPageNumber($(".con-bar li:eq(0) input[name='credentialCode']"));  //获取第一张图片的凭证号。
					$("#dialogClose").empty();
					$("#dialogClose").removeAttr("class");

                }, 250);
				
			}else{
				cbms.alert(result.data);
			}
			
		} //end success callback
	};//end options
	
	cbms.loading();
	$("#upfiles-form").ajaxSubmit(options);
	
}


/**
 * 构建表单用于图片上传  
 */
function addUploadForm(){
	var form = $("<form>");
	form.attr("id", "upfiles-form");
    form.attr('style', 'display:none');
    form.attr('target', '');
    form.attr('method', 'post');
    form.attr('enctype', 'multipart/form-data');
    form.attr('action', _uploadfilesURL);
    
    //<input id="pic" name="pic_cert" class="" type="file" multiple="multiple" value="" onchange="preImg();" />
    var fileinput = $("<input>").attr("type", "file").attr("multiple", "multiple").attr("id", "filesInput").attr("name", "credentialImg");
	/*
    if(credentialId){
    	var codeInput = $("<input>").attr("type", "hidden").attr("name", "credentialId").attr("value", credentialId);
        form.append(codeInput);
    }
    */
    form.append(fileinput);
    
    $("body").append(form);
    
    //打开文件浏览器
    $("#filesInput").click();
    
}

/**
 * 构建弹窗页面里元素
 */
function buildSlidebar(uploadedFiles){
	
//	$("#myteamlistC")
	var rootbase = Context.PATH + '/common/getfile.html?key='
	for(var i=0; i<uploadedFiles.length; i++){
		var thumbNode = ''+
			'<li iid="'+uploadedFiles[i].id+'">'+
			'	<div class="item">'+
			'		<span class="deleteImg"></span>'+
			'		<a href="javascript:;"><img width="105" height="105" src="'+rootbase+uploadedFiles[i].fileUrl+'" /></a>'+
			'	</div>'+
			'</li>';
		
		var none = "";
		if(i>0){
			none = "none"
		}
	
		var readonly = "";
		if(credentialCode)
			readonly = "readonly='readonly'";
		 
		var must =  " must='1' verify='numeric' ";
		//if(require){
		//	must = " must='1' verify='numeric' ";
		//}
		
		var detailNode = ''+
			'<li iid="'+uploadedFiles[i].id+'" class="clearfix '+none+'">'+
			'	<dl>'+
			'		<dt class="fl">'+
			'			<img src="'+rootbase+uploadedFiles[i].fileUrl+'" width="600" />'+
			'		</dt>'+
			'		<dd>'+
			'			<label>凭证号码：<input name="credentialCode" '+readonly+' type="text" value="'+credentialCode+'" /></label>'+
			'			<label>凭证总页码：<span class="iTotalPage">-</span><input type="hidden" name="uploadStatus" value=""/></label>'+
			'			<label>凭证当前页码：<input name="pageNum" '+must+' type="text" value="" /></label>'+
			'		</dd>'+
			'	</dl>'+
			'</li>';
		
		
		$("#myteamlistC").append(thumbNode);
		$(".con-bar > ul").append(detailNode);
		
	}
	
}

function switchImage(_this){
	//切换图片
	var idx = $(".item a").index(_this);
	$(".con-bar li").hide();
	var curr = $(".con-bar li").eq(idx)
	curr.show();
	getCredentialPageNumber(curr.find("input[name='credentialCode']"))
	
	
}

//chengui 从数据库删除当前的图片
function removeImages(){
	var params = {};
	var removeIds = removeObjs;
	if(removeObjs.length != uploadedFiles.length){
		for(var i=0; i<uploadedFiles.length; i++) {
			if(removeIds.indexOf(uploadedFiles[i].id) == -1){
				removeIds.push(uploadedFiles[i].id)
			}
		}
	}
	params.removes = removeIds.toString();
	$.post(
			_updateCredentialImgURL,
			params,
			function(result){
				if(!result.success){
					cbms.alert(result.data);
				}
			}
	);

	updateObjs = {};
	removeObjs = {};
}
