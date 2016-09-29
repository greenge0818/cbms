/**
 * Created by dengxiyan on 2015/8/27.
 * 订单全部tab页
 */
$(document).ready(function(){
	$('#takeNumber').css({'font-size':'30px','font-family':"微软雅黑",'font-weight':'bold','color':'#000','display':'block'});
	$('#takeNumberStyle').css({'height':'45px','width':'135','background-color':'#0099cd','text-align':'center'});
});
function takeNumber(){
	$.ajax({
        type: 'post',
        url: Context.PATH + "/kuandao/payment/kuandaoTakeNumber.html",
        data: {
        	
        },
        success: function (result) {
            showPrcsteelAccount(result.memeberName,result.bankName,result.virAcctNo,result.paymentOrderCode);
        }

    });
}

function showPrcsteelAccount(memeberName,bankName,virAcctNo,paymentOrderCode){
	var ele = '<div class="dialog-m" id="takeNumberDialog">'+
				'<div><p class="recordbar text-left bolder row" style="margin-top:-10px">&nbsp;&nbsp;钢为网款道收款说明</p></div>'+
				'<div id="copy">'+
				'<p>名&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称:&nbsp;&nbsp;'+memeberName+'\r\n</p>'+
				'<p>开&nbsp;&nbsp;&nbsp;户&nbsp;&nbsp;行:&nbsp;&nbsp;'+bankName+'\r\n</p>'+
				'<p>账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号:&nbsp;&nbsp;'+virAcctNo+'\r\n</p>'+
				'<p>备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注:&nbsp;&nbsp;'+paymentOrderCode+'\r\n</p>'+
				'</div>'+
				'<div class="btn-bar text-center">'+
				'<button id="commit" type="button" class="btn btn-primary btn-sm">复制</button>'+
				'</div></div>';
    var dia = cbms.getDialog(false, ele);
    $('#takeNumberDialog').css('width','350');
    if(window.clipboardData){
    	$("#takeNumberDialog").on("click", "#commit", function () {
    		window.clipboardData.setData("Text", $('#copy').text());  
        	cbms.closeDialog();
        	cbms.alert("复制成功！");
        });
    }else{
    	init();
    }
}

function init() {
	var clip = new ZeroClipboard.Client(); // 新建一个对象
	clip.setHandCursor( true );
	clip.setText($('#copy').text()); // 设置要复制的文本。
	clip.glue("commit"); 
	clip.addEventListener( "mouseUp", function(client) {
		clip.hide();
		cbms.closeDialog();
		cbms.alert("复制成功！");
	});
}

