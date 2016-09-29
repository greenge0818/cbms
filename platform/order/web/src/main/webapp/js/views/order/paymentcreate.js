/**
 * Created by chengui on 2016/03/07.
 * 新增预付款
 */
$(document).ready(function () {
/*
 * 修改选择的卖家名称 触发修改
 */
    $(document).on("click", "#searchAccountList li a", function () {
		//选中卖家以后，查询卖家对应的部门及订单相关的买家 lixiang
		 var accountId = $(this).attr("accountid");
		 $("#search").find("option").remove();
		 $("#paymentDrafts").append("<option value=''>无</option>"); 
	     if(accountId != null && accountId != 0) {
	        getDepartment(accountId);//获取客户的部门
	     }
		 $.ajax({
			type: 'post',
			url: Context.PATH + '/order/paymentmanager/paymentCustQuery.html',
			data: {
				"custName" : $("#custName").val()
			},
			success: function (result) {
			    if (result && result.length > 0) {
			    	$("#custName").val(result[0].name);
			    	var bankAccountHtml = "";
			    	var bankName='';
			    	for(var i= 0; i < result.length; i++){
			    		if (result[i].bankStatus == "Approved" && result[i].isDeleted == "0") {
				    		if (result[i].isDefault == "1") {
				    				bankAccountHtml += '<option selected="selected" bankName="'+result[i].bankName+'" bankId="'+result[i].bankId+'" value="'+i+'">'+result[i].bankAccountCode+'</option>';
				    				bankName=result[i].bankName;
				    		} else {
				    			bankAccountHtml += '<option bankName="'+result[i].bankName+'" bankId="'+result[i].bankId+'" value="'+i+'">'+result[i].bankAccountCode+'</option>';			
				    		}
				    		if(bankName =="") {
				    			bankName=result[i].bankName;
				    		}
			    		}
			    		$("#bankName").html(bankName)
			    	}
			    	$("#bankAccount").append(bankAccountHtml);
			    	
			    	$("#status").html(toStatus(result[0].bankDataStatus));
			    	if("审核通过" != toStatus(result[0].bankDataStatus)){
			    		$("#status").attr("title", "打款资料未通过审核，无法提交预付款申请");
			    	}else{
			    		$("#status").attr("title", "");
			    	}
			    } 
			}
	 	});
    });
    
  //选择对应的银行账号改变开户银行的值 lixiang
	$("#bankAccount").change(function(){
		var option = $("#bankAccount").find("option:selected");
		var bankName = $(option).attr("bankName");
		$("#bankName").html(bankName);
	});
	//默认选择第一个
	$("#bankAccount").change();
		 
//获取部门 lixiang
 function getDepartment(accountId) {
     $("#department").empty();//清空
     $.ajax({
         type: 'post',
         url: Context.PATH + "/order/getdepartment.html",
         data: {
             accountId: accountId
         },
         error: function (s) {
         }
         , success: function (result) {
             if (result && result.success) {
                 if (result.data != null && $(result.data).length > 0) {
                     for (var i = 0; i < $(result.data).length; i++) {
                    	 var option = "<option value='" + result.data[i].id + "' balanceSecondSettlement='" + result.data[i].balanceSecondSettlement + "'>" + result.data[i].name + "</option>";
                    	 $("#custName").closest("tr").find("select[name='department']").append(option);//赋值
                         $("#balanceSecondSettlement").html(formatMoney(result.data[0].balanceSecondSettlement,2));
                     }
                 }
             } else {
                 cbms.alert("获取部门失败");
             }
         }

     });
 }
 	//选中部门后改变部门对应的二次结算余额
 	$(document).on("change", "select[name='department']", function (){
		var option = $("#custName").closest("tr").find("select[name='department']").find("option:selected");
		var balanceSecondSettlement = $(option).attr("balanceSecondSettlement");
		$("#balanceSecondSettlement").html(formatMoney(balanceSecondSettlement,2));
	});
 	
	/*
	 * 点击 提交审核
	 */
    $("body").on("click", "#submit", function () {
        var submitData = {
        	traderId: $("#trader").val(),
            trader: $("#trader").find("option:selected").text(),
            //增加部门id
            departmentId : $("#custName").closest("tr").find("select[name='department']").val(),
            paymentType: $("#type").find("option:selected").val(),
            name: $("#custName").val(),
            bankName: $("#bankName").html(),
            bankAccountCode: $("#bankAccount").find("option:selected").text(),
            payAmount: $("#payAmount").val(),
            bankId: $("#bankAccount").find("option:selected").attr("bankId"),
            paymentDrafts: $("#paymentDrafts").find("option:selected").text(),
            balanceSecondSettlement: $("#balanceSecondSettlement").val()
        }
        
        var forms = setlistensSave("#search");
		if (!forms)return;
        
        //参数验证不通过
        if(false == validateParam(submitData)){
        	return false;
        }
        
    	$.ajax({
            type: 'post',
            url: Context.PATH + "/order/paymentmanager/paymentNew.html",
            data: {
            	paymentCreateDto: JSON.stringify(submitData)
            },
            error: function (s) {
                cbms.closeLoading();
            }
            , success: function (result) {
                if (result && result.success) {
                	cbms.alert("提交成功！", function() {
                		 history.go(-1);
        			});
                } else {
                    cbms.alert(result.data,function(){
                    	history.go(-1);
                    });
                }
            }
        });
        
    });
    
    function validateParam(param){
    	//打款资料状态验证
    	var status = $("#status").text();
    	if("审核通过" != status){
    		cbms.alert("打款资料未通过审核，无法提交预付款申请");
    		return false;
    	}
    	
    	return true;
    }
    
    function toStatus(data) {
    	var dataUpper = data.toUpperCase();
    	var status ="";
    	if(dataUpper == "REQUESTED") {
    		status = "待审核";
    	} else if (dataUpper == "APPROVED") {
    		status = "审核通过";
    	} else if (dataUpper == "DECLINED") {
    		status = "审核不通过";
    	} else if (dataUpper == "INSUFFICIENT") {
    		status = "资料不足";
    	}
    	return status;
    }
    
});
