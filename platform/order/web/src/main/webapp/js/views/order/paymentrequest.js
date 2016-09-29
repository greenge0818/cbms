/**
 * Created by lixiang on 2015/7/18.
 */
$(document).ready(function() {
	$(document).on("click", "#prints", function() {
		var pc = parseInt($("#printcount").text(),10);
		if (pc > 0) {
			$(".accounting").text("核算会计(签名)：_________");
		}
		var printIp = $("#print_ip").text();
		var printName = $("#print_name").text();
		var requestId = $("#requestId").val();
		if (printIp=="" || printName =="") {//如果已经打印过一次，而此时页面没有刷新，则去请求查询对应的ip地址和打印人，打印时间
			if(pc > 0) {
				$.ajax({
		            url: Context.PATH + '/order/query/select/Ip.html',
		            type: "POST", 
		            data : {
						"requestId" : requestId
					},
		            success: function (result) {
	            		$("#dialog_ip").text(result.ip);
	            		$("#dialog_name").text(result.printName);
	            		$("#dialog_date").text(date2String(new Date(result.printDate)));
		            }
		        });
			}
		}
		$("#dialogCount").text(pc);
		var html = $('#addreason').html();
		$("#printTimes").css("display", "none");
		if (pc > 0) {
			cbms.getDialog("提示信息", html);
		} else {
			doPrint();
		}
		$(document).on("click", "#btnClose",function () {
		    $("#printTimes").css("display", "block");
	        cbms.closeDialog();
	    });
	});
	if ($("#closeorder").length > 0) {
		$("#closeorder").click(function () {
			cbms.confirm("确认订单关闭？", null, function () {
				var order_id = $("#order_Id").val();
				var payrequest_id = $("#requestId").val();
				var url = Context.PATH + '/account/payment/orderclose.html';
				cbms.loading();
				$.post(url, {orderid: order_id, payrequestid: payrequest_id}, function (re) {
					cbms.closeLoading();
					if (re.success) {
						cbms.alert("操作成功");
						location.replace(Context.PATH + "/order/query/confirmpayment.html");
					}
					else {
						cbms.alert(re.data);
					}
				});
			});
		});
	}
});
function doPrint() {
	var url = Context.PATH + '/account/payment/updateprintcounts.html';
	var id = $("#requestId").val();
	var count = parseInt($("#printcount").text());
	var orderId = $("#order_Id").val();
	$.post(url, {
		id : id,
		printTimes : count,
		orderId : orderId
	}, function(re) {
		if(parseInt($("#printcount").text()) > 0){
			cbms.closeDialog();
		}
		if (re.success) {
			var tableCount = $(".printTable").length;
			if(tableCount == 1) {
				$(".printTable").print();
			}else {
				$(".print-page").print();
			}
			var count = parseInt($("#printcount").text());
			$(".print_counts").text(++count);//页面打印次数+1，给其赋值
			
		}else{
			if (re.code == '1' || re.code == '2') {
				cbms.alert(re.data, function() {
					var url = Context.PATH + "/order/query/detail.html?orderid=" + orderId + "&menu=printpendingpayapply.html";
					location.replace(url);
				});
			} else {
				cbms.alert(re.data);
			}
		}
		$("#printTimes").css("display", "block");
	});
}

function date2String(aDate) {
    var year=aDate.getFullYear();
    var month=aDate.getMonth();
    month++;
    var mydate=aDate.getDate();
    var hour=aDate.getHours();
    var minute=aDate.getMinutes();
    var mytime=year+"-"+(month<10?"0":"")+month+"-"+(mydate<10?"0":"")+mydate+" "+(hour<10?"0":"")+hour+":"+(minute<10?"0":"")+minute;
    return mytime;
}
$(document).on('click', '#btncommit', function() {
	doPrint();
});
