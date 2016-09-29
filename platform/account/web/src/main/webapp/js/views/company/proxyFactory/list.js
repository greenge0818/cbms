
$().ready(function () {
    $("#addFactoryDtoFrom").verifyForm();
});

/**
 * 增加代理钢厂
 */
function doAdd(){
	$("#addFactoryInfoDiv").css("display","");//显示编辑表格
}

/**
 * 编辑代理钢厂
 * @param id
 */

function doEdit(id){
	$("#tr_"+id).find("td").each(function(i){
		var value = $(this).text();
		if(i == 0){
			$(this).html('<input id="factory_'+id+'" type="text" class="c-text" value="'+value+'" must="1"/>');
		}
		if(i == 1){
			$(this).html('<input id="nsortName_'+id+'" type="text" class="c-text" search="nsort" readonly="readonly" value="'+value+'" must="1"/>');
		}
		if(i == 2){
			var content = '<select id="qualification_'+id+'">';
				content+= '<option value="一级代理">一级代理</option>';
				content+= '<option value="二级代理">二级代理</option>';
				content+= '<option value="三级代理">三级代理</option>';
				content+= '<option value="其他">其他</option></select>';
			$(this).html(content);
			var name = '#qualification_'+id +' option[value="'+value+'"]';
			$(name).attr("selected", true);
		}
		if(i == 3){
			var content = '<select id="quantityUnit_'+id+'">';
			content+= '<option value="一年">一年</option>';
			content+= '<option value="一月">一月</option></select>';
			value =  $(this).attr("quantity");
			content+= '&nbsp;&nbsp<input id="quantity_'+id+'" type="text" class="c-text" verify="weight" value="'+value+'" must="1"/>'
			$(this).html(content);
			value =  $(this).attr("quantityUnit");
			var name = '#quantityUnit_'+id +' option[value="'+value+'"]';
			$(name).attr("selected", true);
			
		}
		if(i == 4){
			$(this).html('<input id="stock_'+id+'" type="text" class="c-text" verify="weight" value="'+value+'" must="1"/>');
		}
		if(i == 5){
			$(this).html('<input type="button" onclick="doEditSave('+id+')" value="确定"/>');
			$("#editFactoryDtoFrom").verifyForm();
		}
	});
}

/**
 * 删除代理钢厂
 * @param id
 */
function doDelete(id){
	cbms.confirm("确定删除该条代理钢厂信息吗？",null,function(){
		$.ajax({
			url: Context.PATH + "/proxyfactory/deleteproxyfactory.html",
			type:"POST",
			data:{"id":id},
			success: function (result) {
				if (result.success) {
					$("#tr_"+id).remove();
				}else{
					cbms.gritter("删除钢厂失败,请联系管理员");
				}
			}
		});
	});
}

/**
 * 新增保存
 */
function doAddSave(id){
	if(verifyForm(id)){
		var data = $("#addFactoryDtoFrom").serialize();
		doSave(id,data,"add");
	}
}
/**
 * 编辑保存
 */
function doEditSave(id){
	if(verifyForm(id)){
		var data = {
					"id":id,
					"factory": $("#factory_"+id).val(),
					"nsortName": $("#nsortName_"+id).val(),
					"qualification": $("#qualification_"+id).val(),
					"quantityUnit": $("#quantityUnit_"+id).val(),
					"quantity": $("#quantity_"+id).val(),
					"stock": $("#stock_"+id).val(),
					};
		doSave(id,data,"edit");
	}
}
/**
 * 保存代理钢厂
 */
function doSave(id,data,op){
	$.ajax({
		url: Context.PATH + "/proxyfactory/saveproxyfactory.html",
		type:"POST",
		data:data,
		success: function (result) {
			if (result.success && result.data.id > 0) {
				var rData = result.data;
				var tdContent = generateTdContent(rData);
				//新增刷新页面
				if(op == "add"){
					//界面添加一行数据
					var content ='<tr id="tr_'+rData.id+'">';
					content+=tdContent;
		            content+='</tr>';
		            var currentRows = $("#factoryInfoTable").find("tr");
		            if(currentRows.length > 1){
		            	$(currentRows[1]).before(content);
		            }else{
		            	$("#appendFlag").append(content);
		            }
		            doCancel();
				}else{
					//编辑刷新页面
					$("#tr_"+rData.id).html(tdContent);
				}
			}else{
				cbms.gritter("保存钢厂失败,请联系管理员");
			}
		}
	});
}

/**
 * 取消新增编辑
 */
function doCancel(){
    $("#AddTr").find('input[type=text]').each(function(){
    	$(this).val("");
    });
	$("#addFactoryInfoDiv").css("display","none");
}
/**
 * 校验文本框输入值
 * @param id
 * @returns {Boolean}
 */
function verifyForm(id){
	var factoryLbl ="#factory";
	var nsortNameLbl ="#nsortName";
	var quantityLbl ="#quantity";
	var stockLbl ="#stock";
	if(id != -1){
		factoryLbl+="_"+id;
		nsortNameLbl+="_"+id;
		quantityLbl+="_"+id;
		stockLbl+="_"+id;
	}
	var factory = $.trim($(factoryLbl).val());
	if(!factory){
		$(factoryLbl).focus();
		$(factoryLbl).click();
		return false;
	}
	var nsortName = $.trim($(nsortNameLbl).val());
	if(!nsortName){
		$(nsortNameLbl).focus();
		$(nsortNameLbl).click();
		return false;
	}
	var quantity = $.trim($(quantityLbl).val());
	var pattern = /^\d+(\.\d{1,6})?$/;
	if(!pattern.test(quantity)){
		$(quantityLbl).focus();
		$(quantityLbl).click();
		return false;
	}
	var stock = $.trim($(stockLbl).val());
	if(!pattern.test(stock)){
		$(stockLbl).focus();
		$(stockLbl).click();
		return false;
	}
	return true;
}

/**
 * 保存后Table td内容
 * @param rData
 * @returns
 */
function generateTdContent(rData){
	var content ='<td>'+rData.factory+'</td>';
	content+='<td>'+rData.nsortName+'</td>';
	content+='<td>'+rData.qualification+'</td>';
    content+='<td quantityUnit='+rData.quantityUnit+' quantity='+rData.quantity+'>'+ rData.quantityUnit+'&nbsp;'+ rData.quantity+'</td>';
    content+='<td>'+rData.stock+'</td>';
    content+='<td><a href="javascript:void(0)" onclick="doEdit('+rData.id+')">编辑</a>&nbsp';
    content+='<a href="javascript:void(0)" onclick="doDelete('+rData.id+')">删除</a></td>>';
    return content;
}

/**
 * 数字补位
 * @param number
 * @param fixedNum
 * @returns
 */
function convertDecimal(number,fixedNum)
{
	return number.toFixed(fixedNum);
}