var globalEl = null;
var identifying=null;
var globalDas = null;
var dealEvidence = null;
var defaultValue='';

function editVoucher(trpse){
	identifying=$(".identifying")[0].innerText;
	globalEl = $(trpse).parent().prev();
	var strs=$(trpse).parent().prev().text().split("、"); //字符分割
	var marking=$(trpse).closest("table").find(".marking").text();
	if(marking=='凭证名称'){
		globalDas='凭证名称';
		dealEvidence=$(trpse).parent().parent().find(".dealEvidence").text();
	}else{
			globalDas='';
		dealEvidence='';
	}
	var td="";
	var defaultTable='';
	var defailtNameStr=''
	if(marking=='标示名称'){
		 defaultValue='<td><label><input type="radio" name="defaultValue" class="defaultCheck" value="1" >默认</label></td>';
		 defaultTable='<td>新增客户时默认值</td>';
		 defailtNameStr=$(trpse).parent().parent().find("input[name='defaultValue']")[0].value;
	}else{
		 defaultValue='';
		 defaultTable='';
	}
	
	for (i=0;i<strs.length ;i++ )
	{
		var checkedstr=''
		if(marking=='标示名称'){
			if(strs[i]==defailtNameStr){
				 checkedstr='<td><label><input type="radio" name="defaultValue" class="defaultCheck" value="1" checked="true">默认</label></td>';
			}else{
				checkedstr='<td><label><input type="radio" name="defaultValue" class="defaultCheck" value="1" >默认</label></td>';
			}
		}
		td+="<tr>"+checkedstr+"<td class='voucherTxt'><input type='text' value='"+strs[i]+"' must='1' class='ipt-text version ' id='' name='version' size='30' readOnly/></td><td><a  href='javascript:;' class='delectVoucher'>修改</a></td></tr>"	;
	}
	var html="<div><div><table id='edit_voucher'  class='contable border1'   style='width: 500px'><tbody class='text-center'><tr>"+defaultTable+"<td>"+marking+"</td><td>操作</td></tr>"+td+
		"<tr>"+defaultValue+"<td><input type='text'   value='' must='1' class='ipt-text  ' id='' name='version' size='30'/></td> <td><a href='#' id='addVoucherName' >添加</a></td></tr>"+
		"</table></div><div class='btn-bar _text-center'><button type='button'  class='btn btn-default btn-sm' id='window_close'>取消</button>&nbsp;"+
		"<button type='button'  class='btn btn-info btn-sm' id='edit_voucher_shut'>确定</button></div></div> ";
	cbms.getDialog("编辑凭证",html);
	//alert(treeId);
}
$(document).on("click","#window_close",function(){
	cbms.closeDialog();
})
$(document).on("click","#addVoucherError",function(){
	cbms.alert("单次只能添加一个凭证名称！");
})
$(document).on("click","#edit_voucher_shut",function(){
	var a=$(".version");
	var htm="";
	
	for(var i=0;i< a.length;i++){
		var s=	a[i].value;
		if( s==null||s==""){
			cbms.alert("凭证名称不能为空！");
            return
		}else{
			htm+=  a[i].value+"、";}
	}
	var newstr=htm.substring(0,htm.length-1);
	var marking=$(globalEl).closest("tbody").find(".voucher");
	if(globalDas=="凭证名称"){
		if(dealEvidence=="卖家凭证"){
		marking.each(function(index){
			if((index+1)%2==1){
			$(this).text("").text(newstr);
			$(this).prev().find("input").val(newstr);
			}
		})}else{
			marking.each(function(index){
				if((index+1)%2==0){
				$(this).text("").text(newstr);
				$(this).prev().find("input").val(newstr);
				}
			})
			
		}
		cbms.closeDialog();
		initIdentifying();
	}else{
	globalEl.text("").text(newstr);
	var defaultIndex=''
		$(".defaultCheck").each(function(index){
		if($(this)[0].checked){
			defaultIndex=index
		}
	});
	var defauStr=newstr.split("、")[defaultIndex]; 
	if(typeof(defauStr) != "undefined"||defauStr!=null){
	globalEl.parent().find("input[name='defaultValue']")[0].value=defauStr;
	}
	globalEl.prev().find("input").val(newstr);
	cbms.closeDialog();
	initIdentifying();
	}
})
function initIdentifying(){
	var aba=$(".identifying")[0].innerText;
	if(aba==identifying){
		return null;
	}else{
		initAllTable(aba);
	}
}
$(document).on("click",".delectVoucher",function(){
	var single=$(this).parent().prev().find("input[name='version']");
	if($(this).text()=="修改"){
		$(this).text("").text("确定")
		single.removeAttr("readOnly")}else{
		$(this).text("").text("修改")
		single.attr("readOnly","readOnly")
	}
	//var txt=	$(this).parent().prev().text();
	//var tt="<input type='text'   value='"+txt+"' must='1' class='ipt-text version' id='' name='' size='30'/>";
	//$(this).parent().prev().text("").append(tt);
	//$(this).parent().parent().remove();
})
$(document).on("click","#addVoucherName",function(){

	var txt=$(this).parent().prev().children().val().trim();
	var judge=false;
	if(txt==null||txt==''){
		cbms.alert("凭证名称不能为空！");
		judge=true;
	}
	$(".version").each(function(){
		var text=	$(this).val().trim();
		if (text==null||text==""){
			judge=true;
		}
	})
	if(judge){
		return
	}
	var txt=$(this).parent().prev().find("input[name='version']").val();
	$(this).parent().parent().remove();
	var html ="<tr>"+defaultValue+"<td class='voucherTxt'><input type='text'   value='"+txt+"' must='1' class='ipt-text version' id='' name='' size='30' readOnly/></td><td>&nbsp;&nbsp;<a  href='javascript:;' class='delectVoucher'>修改</a></td>"+"</tr>"
		+"<tr>"+defaultValue+"<td><input type='text'   value='' must='1' class='ipt-text' id='' name='' size='30'/></td> <td><a href='#' disabled = 'true'id='addVoucherError'>添加</a></td></tr>";
	$("#edit_voucher").append(html);
})
$('#submit').click(function () {
	//cbms.loading();
	$('#savaRisk').ajaxSubmit({
		success: function (data) {
			cbms.closeLoading();
			if(data.success){
				cbms.alert("保存成功！");
				window.setTimeout(function(){
					window.location.reload();
				},1000);
			}else{
				cbms.alert( "保存失败!"+data.data);
			}

		}
	})
})
$("#subimtb").click(function(){
	$.ajax({
		type : "POST",
		url : Context.PATH + "/sys/savaRisk.html",
		data : {
			"message" : $(".message").val(),
			"exceedtime" : $("#exceedtime").val(),
			"clientNature" : $(".clientNatureDB").val(),
			"dealProof" : $(".dealProof").val(),
			"proofName" : $(".proofName").val(),
			"clientIdentifying" : $(".clientIdentifying").val(),
			"identifyingName" : $(".identifyingName").val(),
			"sellerBuyer" : $(".sellerBuyer").val(),
			"whetherName" : $(".whetherName").val(),
			"messageType" : $(".messageType").val(),
			"messageName" : $(".messageName").val(),
			"whetherStart" : $(".whetherStart").val(),
			"messageNameEng" : $(".messageNameEng").val()
		},
		success: function(data){
			cbms.closeLoading();
			cbms.alert(data.success ? "保存成功！" : "保存失败!"+data.data);
		}
	});
})
//$(document).on("click",".checked",function(){
//	if($(this).attr("checked")){
//		$(this).parent().parent().next().val("1")
//	}else{
//		$(this).parent().parent().next().val("0")
//	}
//})
$(".checked").click(function(){
	if($(this).is(":checked")){
		$(this).val("1");
		$(this).parent().parent().prev().find("input[name='whetherStart']").val("1");
	}else{
		$(this).val("0");
		$(this).parent().parent().prev().find("input[name='whetherStart']").val("0");
	}

})

function initAllTable(adages){
	var adget =adages.split("、");
	var identifyi=identifying.split("、");
	if(adget.length>identifyi.length){
		var dyour=	adget.pop();
		amendAllTable(adget);
		addAllTable(dyour);
	}else{
		amendAllTable(adget);
	}


}

function addAllTable(param){
	var	index=$("#clientTd tr:last").index()+1;

	var clientTd='<tr class="bolder-b-dashed"><td class="clientTypeName">'+param+'</td><td class="dealEvidence">卖家凭证</td><input type="hidden" name="clientNature" class="clientTypeNameDB" value="'+param+'">'+
		'<input type="hidden" name="dealProof" value="卖家凭证"><td colspan="2"><label><input type="radio" name="yesPass'+(index+1)+'" class="check" value="1">需要审核通过</label>'+
		'<label><input type="radio" name="yesPass'+(index+1)+'" class="check" value="0" checked="true">不需要审核通过</label></td><td style="display: none">' +
		'<input type="hidden" name="proofName" value="销售单、放货确认函"></td><td colspan="2" class="voucher">销售单、放货确认函</td><td><a href="javascript:void(0);" onclick="editVoucher(this)">编辑</a></td></tr>'+
		'<tr class="bolder-b-dashed"><td class="clientTypeName">'+param+'</td><td class="dealEvidence" >买家凭证</td><input type="hidden" name="clientNature" class="clientTypeNameDB" value="'+param+'">'+
		'<input type="hidden" name="dealProof" value="买家凭证"><td colspan="2"><label><input type="radio" name="yesPass'+(index+2)+'" class="check" value="1">需要审核通过</label>'+
		'<label><input type="radio" name="yesPass'+(index+2)+'" class="check" value="0" checked="true">不需要审核通过</label></td><td style="display: none">' +
		'<input type="hidden" name="proofName" value="收货确认函"></td><td colspan="2" class="voucher">收货确认函</td><td><a href="javascript:void(0);" onclick="editVoucher(this)">编辑</a></td></tr>';
	$("#clientTd").append(clientTd);

	
	
     var whetherTd='<tr><td class="whetherName">'+param+'</td><input type="hidden" name="whetherName" class="whetherNameDB" value="'+param+'">'+
    '<td colspan="2"><label><input type="radio" name="" class="check" value="1">能</label>'+
     '<label><input type="radio" name="whether4" class="check" value="0" checked="">不能</label></td></tr>';
	$("#whetherTd").append(whetherTd);
	var index=$("#whetherTd tr:last").index();
	$("#whetherTd td:last label input").attr("name","whether"+(index+1));
	$("#whetherTd tr:first td:eq(0)").attr("rowspan",index+1);
	//
	
    
	var dealParameterTdTd='<tr><td class="parameterName">'+param+'</td><input type="hidden" name="parameterName" class="parameterNameDB" value="'+param+'"><td colspan="2">'+
	'<label><input type="radio" name="" value="1">是</label><label><input type="radio" name="" value="0" checked="">否</label></td>'+
    '<td><input type="text" value="0" class="ipt-text" id="" name="tonnage" size="14"></td>'+
    '<td><input type="text" value="0" class="ipt-text" id="" name="count" size="14"></td>'+
    
    '<td><input type="text"   value="0" class="ipt-text" id="" name="singleTradeWeight" size="14"/></td>'+
	'<td><input type="text"   value="0" class="ipt-text" id="" name="allTradeQuality" size="14"/></td>'+
	'<td><input type="text"   value="0" class="ipt-text" id="" name="allTradeWeight" size="14"/></td>'+
    
    '<td><input type="text" value="0" class="ipt-text" id="" name="percent" size="14"></td>'+
	'<input type="hidden" name="orgId" value="$!{deal.orgId}"/><input type="hidden" name="orgName" value="$!{deal.orgName}"/></tr>';
	$(".dealParameterTd").each(function(){
		var	index1=$(this).find(" tr:last").index();
		
		$(this).append(dealParameterTdTd);
		$(this).find("tr td:eq(0)").attr("rowspan",index1+2);
		$(this).find("tr td:eq(1)").attr("rowspan",index1+2);
		
		var orgId=$(this).find(":input[name='orgId']")[0].value;
		var orgName=$(this).find(":input[name='orgName']")[0].value
		
		$(this).find(":input[name='orgId']")[index1+1].value=orgId;
		$(this).find(":input[name='orgName']")[index1+1].value=orgName;
		
		$(this).find("tr:last td:eq(1) label input").attr("name",orgId+"yesParameter"+(index1+2));
		//$("#dealParameterTd tr:last td:eq(1) label input").attr("name","yesParameter"+(index+1));
//		$("#dealParameterTd tr:first td:eq(0)").attr("rowspan",index+1);
	});
//	append(dealParameterTdTd);
//	index=$("#dealParameterTd tr:last").index();
//	$("#dealParameterTd tr:last td:eq(1) label input").attr("name","yesParameter"+(index+1));
//	$("#dealParameterTd tr:first td:eq(0)").attr("rowspan",index+1);

	
	
	var newOrder=$("#newOrderTd tr:last").clone();
	$("#newOrderTd").append(newOrder);
	$("#newOrderTd tr:last input:last")[0].value=param;
	$("#newOrderTd tr:last  span:eq(0)")[0].innerHTML=param;
	var i=$("#newOrderTd tr:last ").index();
	$("#newOrderTd tr:first td:eq(0)").attr("rowspan",i+1);

	var auditOrderTd=$("#auditOrderTd tr:last").clone();
	$("#auditOrderTd").append(auditOrderTd);
	$("#auditOrderTd tr:last input:last")[0].value=param;
	$("#auditOrderTd tr:last  span:eq(0)")[0].innerHTML=param;
	var i=$("#auditOrderTd tr:last ").index();
	$("#auditOrderTd tr:first td:eq(0)").attr("rowspan",i+1);

	var applyPaymentTd=$("#applyPaymentTd tr:last").clone();
	$("#applyPaymentTd").append(applyPaymentTd);
	$("#applyPaymentTd tr:last input:last")[0].value=param;
	$("#applyPaymentTd tr:last  span:eq(0)")[0].innerHTML=param;
	var i=$("#applyPaymentTd tr:last ").index();
	$("#applyPaymentTd tr:first td:eq(0)").attr("rowspan",i+1);

	var pendingPaymentTd=$("#pendingPaymentTd tr:last").clone();
	$("#pendingPaymentTd").append(pendingPaymentTd);
	$("#pendingPaymentTd tr:last input:last")[0].value=param;
	$("#pendingPaymentTd tr:last  span:eq(0)")[0].innerHTML=param;
	var i=$("#pendingPaymentTd tr:last ").index();
	$("#pendingPaymentTd tr:first td:eq(0)").attr("rowspan",i+1);


	var printFloatLayerTd=$("#printFloatLayerTd tr:last").clone();
	$("#printFloatLayerTd").append(printFloatLayerTd);
	$("#printFloatLayerTd tr:last input:last")[0].value=param;
	$("#printFloatLayerTd tr:last  span:eq(0)")[0].innerHTML=param;
	var i=$("#printFloatLayerTd tr:last ").index();
	$("#printFloatLayerTd tr:first td:eq(0)").attr("rowspan",i+1);

	var printTransInterfaceTd=$("#printTransInterfaceTd tr:last").clone();
	$("#printTransInterfaceTd").append(printTransInterfaceTd);
	$("#printTransInterfaceTd tr:last input:last")[0].value=param;
	$("#printTransInterfaceTd tr:last  span:eq(0)")[0].innerHTML=param;
	var i=$("#printTransInterfaceTd tr:last ").index();
	$("#printTransInterfaceTd tr:first td:eq(0)").attr("rowspan",i+1);

}
function amendAllTable(adget){
	//var adget =adages.split("、");
	var clientType=  $(".clientTypeName");
	var clientTypeNameDB=$(".clientTypeNameDB");
	for(var i=0;i<clientType.length;i++){
		var str=adget[Math.ceil((i+1)/2)-1];
		clientType[i].innerHTML=str;
		clientTypeNameDB[i].value=str;
	}
	var whetherName=$(".whetherName");var whetherNameDB=$(".whetherNameDB");
	var   parameterName=$(".parameterName");var   parameterNameDB=$(".parameterNameDB");
	var newOrder=$(".newOrder");var newOrderDB=$(".newOrderDB");
	var auditOrder=$(".auditOrder");var auditOrderDB=$(".auditOrderDB");
	var applyPayment=$(".applyPayment");var applyPaymentDB=$(".applyPaymentDB");
	var pendingPayment=$(".pendingPayment");var pendingPaymentDB=$(".pendingPaymentDB");
	var printFloatLayer=$(".printFloatLayer");var printFloatLayerDB=$(".printFloatLayerDB");
	var printTransInterface=$(".printTransInterface");var printTransInterfaceDB=$(".printTransInterfaceDB");
	for(var i=0;i<whetherName.length;i++){
		var str=adget[i];
		whetherName[i].innerHTML=parameterName[i].innerHTML=newOrder[i].innerHTML=auditOrder[i].innerHTML=
			applyPayment[i].innerHTML=pendingPayment[i].innerHTML=printFloatLayer[i].innerHTML=printTransInterface[i].innerHTML=str;
		whetherNameDB[i].value=parameterNameDB[i].value=newOrderDB[i].value=auditOrderDB[i].value=
			applyPaymentDB[i].value=pendingPaymentDB[i].value=printFloatLayerDB[i].value=printTransInterfaceDB[i].value=str;
	}
	var index=0;
	for(var i=0;i<parameterName.length;i++){
		var str=adget[index];
		index++;
		parameterName[i].innerHTML=parameterNameDB[i].value=str;
		if(index==adget.length){
			index=0;
		}
	}
	
}


