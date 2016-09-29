/**
 * Created by lcw on 2015-08-08.
 */
$(document).ready(function () {
    $("#prints").click(function () {
    	var pc = parseInt($("#printcount").text(),10);
    	$("#dialogCount").text(pc);
    	var printIp = $("#print_ip").text();
    	var printName = $("#print_name").text();
		var requestId = $("#requestId").val();
		if (printIp=="" || printName ==""){ //如果已经打印过一次，而此时页面没有刷新，则去请求查询对应的ip地址和打印人，打印时间
			if (pc > 0){
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
		var html = $('#addreason').html();
		if (pc > 0) {
			cbms.getDialog("提示信息", html);
		} else {
			doPrint();
		}
		$(document).on("click", "#btnClose",function () {
	        cbms.closeDialog();
	    });
    });
})

function doPrint(){
	var url = Context.PATH + '/payment/updateprintstatus.html';
    var id = $("#requestId").val();
    var count = parseInt($("#printcount").text());
    if (isNaN(count))
        count = 0;
    $.post(url, {id: id}, function (re) {
        if (re.success) {
        	$("#printeare").print();
            $("#printcount").text(++count);
            cbms.closeDialog();
        }else{
        	if (parseInt($("#printcount").text()) > 0) {
				cbms.closeDialog();
			}
			cbms.alert(re.data, function() {
				location.href = Context.PATH + "/order/query/withdrawconfirm2.html";
			});
		}
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
