var settingsname;

function editPaymentSort(trpse){
	 settingsname=  $(trpse).attr("settingsname");
	var settingsid=  $(trpse).attr("settingsid");
	var html="<div><div><table id='edit_voucher'  class='contable border1'   style='width: 500px'><tbody class='text-center'><tr><td>支付名称</td><td style='width: 400px'>操作</td></tr>"+
		"<tr><td><input type='text'   value='"+settingsname+"' must='1' class='ipt-text  ' id='settingsid'  size='30'/></td> <td><a href='#' settingsid='"+settingsid+"' class='delPaymentSort' >删除</a></td></tr>"+
		"</table></div><div class='btn-bar _text-center'><button type='button'  class='btn btn-default btn-sm' id='window_close'>取消</button>&nbsp;"+
		"<button type='button'  class='btn btn-info btn-sm' id='edit_voucher_shut'>确定</button></div></div><br> ";
	cbms.getDialog("编辑支付方式",html);

}
$(document).on("click",".delPaymentSort",function(){
	var settingsid=  $(this).attr("settingsid");
	cbms.confirm("确定删除『"+settingsname+"』该支付方式么？",'',function(){
		delPaymentSort(settingsid)
	});
});

function addPaymentSort(){
	var htm="<div class='well'> <form id='addPaymentSort'><label style='width: 100px'>支付名称</label><input must='1' verify='chinese' type='text' value='' id='paymentSortName' "+
	"style='width: 200px'/><br/></form></div> <p align='center'><a class='button btn-info btn-lg' id='submit'>提交</a></p>";
	cbms.getDialog("新增支付方式",htm);
}
function delPaymentSort(settingsid){
	  $.ajax({
		  type: 'POST',
		  url: Context.PATH + "/sys/delpaymentsort.html",
		  data: {
			  id:settingsid
		  }, error: function(s){

		  }, success: function (data) {
			  //cbms.alert(data.success ? "删除成功！" : "删除失败!"+data.data);
			  cbms.closeDialog();
			  if(data.success){
				  cbms.alert("删除成功！");
				  window.setTimeout(function(){
					  window.location.reload();
				  },1000);
			  }else{
				  cbms.alert( "删除失败!"+data.data);
			  }
		  }
	  });

}
$(document).on("click","#window_close",function(){
	cbms.closeDialog();
})

$(document).on("click","#submit",function(){
	var paymentSortName=  $("#paymentSortName").val();
	$("#addPaymentSort").verifyForm();
	savePaymentSort(paymentSortName)
});

function savePaymentSort(paymentSortName){
	$.ajax({
		type: 'POST',
		url: Context.PATH + "/sys/savepaymentsort.html",
		data: {
			name:paymentSortName
		}, error: function(s){
		}, success: function (data) {
			if(data.success){
				cbms.closeDialog();
				cbms.alert("保存成功！");
				window.setTimeout(function(){
					window.location.reload();
				},1000);
			}else{
				cbms.alert(data.data);
			}
		}
	});

}

//添加支付类型
function addPaymentType(){
	var htm="<div class='well'> <form id='addPaymentType'><label style='width: 100px'>支付类型</label><input must='1' verify='chinese' type='text' value='' id='paymentTypeName' "+
	"style='width: 200px'/><br/></form></div> <p align='center'><a class='button btn-info btn-lg' id='addPayType'>提交</a></p>";
	cbms.getDialog("新增支付类型",htm);
}

$(document).on("click","#addPayType",function(){
	var paymentTypeName=  $("#paymentTypeName").val();
	$("#addPaymentType").verifyForm();
	savePaymentType(paymentTypeName)
});

function savePaymentType(paymentTypeName){
	$.ajax({
		type: 'POST',
		url: Context.PATH + "/sys/savepaymenttype.html",
		data: {
			name:paymentTypeName
		}, error: function(s){
		}, success: function (data) {
			if(data.success){
				cbms.closeDialog();
				cbms.alert("保存成功！");
				window.setTimeout(function(){
					window.location.reload();
				},1000);
			}else{
				cbms.alert(data.data);
			}
		}
	});
}

function editPaymentType(){
	settingsname=  $(trpse).attr("settingsname");
	$(document).on("click",".editPaymentType",function(){
		var settingsid=  $(this).attr("settingsid");
		cbms.confirm("确定要禁用『"+settingsname+"』该支付类型么？",'',function(){
			
		});
	});	
}





