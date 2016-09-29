/**
 * 合同模板基础页面js
 *
 */

$(document).ready(function() {
	initList($(".ck li.acti a").attr("val"));

	//部门li点击切换
	$(document).on("click",".ck li a",function(){
		$(".ck li").removeClass("acti");
		$(this).closest('li').addClass("acti");
		var deptId=$(this).closest('li a').attr("val");
		$("div[id*='deptTemp_']").addClass("none");
		$("#deptTemp_"+deptId).removeClass("none");
		initList(deptId);
	});

	//设置默认合同模板
	$(document).on("change", "input[contract='check']", function() {
		var self = this;
		if($(self).closest(".row").find("p").attr("status") == "审核通过"){
			cbms.confirm("确定要将“"+$(self).closest(".row").find("p").text()+"”设置为默认合同模板吗？",null,function(){
				setDefaultContractTemplate($(self).val(),$(self).closest(".backgr").attr("id"),$(".ck li.acti a").attr("val"));
			},function(){
				initList($(".ck li.acti a").attr("val"));
			});
		}else{
			cbms.alert("审核通过的合同模板才能设为默认合同模板");
			initList($(".ck li.acti a").attr("val"));
		}
	});

	$("#contracttemplate").addClass("active").find("a").attr("href","javascript:void(0);");

	$(document).on("click",".doTemplateOprt",function(){
		doTemplateOprt(this);
	});

});

/**
 * 模版处理操作
 * @param el      元素
 * @param action  动作：添加 add，查看 view，编辑 edit
 */
function doTemplateOprt(el){
	if(!el){
		return;
	}
	//操作类型
	var action=$(el).attr("action");
	if(!action){
		cbms.alert("操作类型错误，请尝试刷新处理!");
		return;
	}
	//模板id
	var templateId=$(el).closest("div .row").attr("id")||-1;
	var templateName=$(el).closest("div .row").find("p.marginbottom").text();
	if(templateName.indexOf("(")>=0){
		templateName=templateName.substr(0,templateName.indexOf("("));
	}


	//公司id
	var companyId=$("#companyId").val();
	//公司名称
	var companyName=$("#companyName").val();
	//删除操作
	if("del"==action){
		cbms.confirm("确定要删除 "+templateName+" 模板吗?",null,function(){
			doDelTemplate(templateId,companyId);
		});
	}else{//其他操作:查看，编辑，添加

		//账户id（部门id）
		var accountId=$(".ck li.acti a").attr("val");
		if(!accountId){
			cbms.alert("部门错误，请尝试刷新处理!");
			return;
		}
		//模版类型
		var type=$(el).closest("div .row").attr("type");
		if(!type){
			cbms.alert("模版类型错误，请尝试刷新处理!");
			return;
		}

		var url=Context.PATH + "/contracttemplate/dotemplate.html?";
		var param="accountId="+accountId+"&companyId="+companyId+
				"&action="+action+"&type="+type+"&id="+templateId+"&name="+templateName+"&companyName="+companyName;

		//参数作简单编码处理
		param=encodeURIComponent(param);

		//构建form提交
		var form = $("<form>");
		form.attr('style', 'display:none');
//	    form.attr('target', '_blank');
		form.attr('method', 'get');
		form.attr('action', Context.PATH + "/contracttemplate/dotemplate.html");

		form.append($('<input>').attr('type', 'hidden').attr('name', 'param').attr('value', param));

		$('body').append(form);

		form.submit();
		form.remove();
	}


}

/**
 * 删除合同模板
 * @param templateId
 */
function doDelTemplate(templateId,companyId){
	$.ajax({
		type : "POST",
		url : Context.PATH + "/contracttemplate/del.html",
		data : {
			"id" : templateId
		},
		success : function(result) {
			cbms.closeLoading();
			if (result) {
				if (result.success) {
					cbms.alert("操作成功！",function(){
						location.reload();
					});
				}
			}
		},
		error : function(xhr, textStatus, errorThrown) {
			cbms.closeLoading();
		}
	});
}
/**
 * 获取所有合同模板
 * @param accountId
 */
function initList(accountId) {
	$.ajax({
		type : "POST",
		url : Context.PATH + "/contracttemplate/search.html",
		data :{accountId:accountId},
		dataType : "json",
		success : function(response) {
			if (response.success) {
				accountContractTemplates=response.data;
				var div = $("#deptTemp_"+accountId);
				$(div).find("div[name='addDiv']").empty();
				for(type in accountContractTemplates){
					var input=null;
					$(accountContractTemplates[type]).each(function(i,e){
						if(i == 0){
							$(div).find("#"+type+"").find("div[name='addDiv']").append($(createSysContractTemplate(e)));
						}else{
							var isLast = accountContractTemplates[type].length-i==1?true:false;
							$(div).find("#"+type+"").find("div[name='addDiv']").append($(createCustomContractTemplate(e,isLast)));

							if(e.enabled == 1){
								input = $("#"+e.id).find("input");
							}
						}
					});
					if(input){
						$("input[name="+type+"]").removeAttr("checked");
						$(input).prop("checked",true);
					}
				}

			} else {
				cbms.alert(response.data);
			}
		},
		error : function(xhr, textStatus, errorThrown) {}
	});
}

/**
 * 系统模板
 */
function createSysContractTemplate(contractTemplate){
	var html = "<div class='row borderbottomTwo' id="+contractTemplate.id+" type='"+contractTemplate.type+"'><div class='col-md-4 height-b'><p class='font14 marginbottom' status="+contractTemplate.status+">"+contractTemplate.name+"</p></div><div class='col-md-6 height-b'>" +
			"<input type='radio' checked='true' value="+contractTemplate.id+" contract='check' name='"+contractTemplate.type+"' class='margin-top10'/>设为默认</div><div class='col-md-2 height-b'><a href='#' class='left13 doTemplateOprt' action='view' >查看</a></div></div>" ;
	return html;
}

/**
 * 自定义模板
 */
function createCustomContractTemplate(contractTemplate,isLast){
	var html = "";
	if(contractTemplate.status == "待审核"){
		html = "<div class='row "+(isLast?"":"borderbottomTwo")+" height-b ' id="+contractTemplate.id+" type='"+contractTemplate.type+"'><div class='col-md-4'><p class='font14 marginbottom'  status="+contractTemplate.status+">"+contractTemplate.name+"<span class='colorc81623'>("+contractTemplate.status+")</span></p></div><div class='col-md-6'>" +
				"</div><div class='col-md-2'><a href='#' class='left13 doTemplateOprt' action='view'>查看</a></div></div>";
	}else if(contractTemplate.status == "审核通过"){
		//合同模板设为默认不可编辑
		var editHtml = contractTemplate.enabled==0 ? "<a href='#' class='left13 doTemplateOprt' action='edit'>编辑</a>" : "";
		html = "<div class='row "+(isLast?"":"borderbottomTwo")+" height-b ' id="+contractTemplate.id+" type='"+contractTemplate.type+"'><div class='col-md-4'><p class='font14 marginbottom'  status="+contractTemplate.status+">"+contractTemplate.name+"<span class='colorc81623'>("+contractTemplate.status+")</span></p></div><div class='col-md-6'>" +
				"<input type='radio' value="+contractTemplate.id+" contract='check' name="+contractTemplate.type+" class='margin-top10'/>设为默认</div><div class='col-md-2'><a href='#' class='left13 doTemplateOprt' action='view'>查看</a>" + editHtml +
				"</div></div>";
	}else if(contractTemplate.status == "审核未通过"){
		html = "<div class='row "+(isLast?"":"borderbottomTwo")+" height-b ' id="+contractTemplate.id+" type='"+contractTemplate.type+"'><div class='col-md-4'><p class='font14 marginbottom'  status="+contractTemplate.status+">"+contractTemplate.name+"<span class='colorc81623'>("+contractTemplate.status+"，理由："+contractTemplate.statusDeclineReason+")</span></p></div><div class='col-md-6'>" +
				"</div><div class='col-md-2'><a href='#' class='left13 doTemplateOprt' action='view'>查看</a><a href='#' class='left13 doTemplateOprt' action='edit'>编辑</a>" +
				"<a href='#' class='left13 doTemplateOprt' action='del'>删除</a></div></div>";
	}
	return html;
}

/**
 * 设置默认合同模板
 * @param contractTemplateId
 * @param contractTemplateType
 * @param accountId
 * @param self
 */
function setDefaultContractTemplate(contractTemplateId,contractTemplateType,accountId){
	$.ajax({
		type : "POST",
		url : Context.PATH + "/contracttemplate/setdefault.html",
		data :{contractTemplateId : contractTemplateId,
			contractTemplateType : contractTemplateType,
			accountId : accountId},
		dataType : "json",
		success : function(response) {
			if (response.success) {
				cbms.gritter("默认合同模板设置成功！",true);
				setTimeout(function() {
					location.reload(true);
				}, 1500);
			} else {
				cbms.gritter(response.data,false);
			}
		},
		error : function(xhr, textStatus, errorThrown) {}
	});
}
