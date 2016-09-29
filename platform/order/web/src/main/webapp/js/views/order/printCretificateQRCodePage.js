

function printCretificateRQCodePage(){
	
	var qrcodeImg = $(".qrcode-img");
	var totalPageNumber = $(".credentialNum").val();
	var code = $(".certificatecode").eq(0).text();
	
	var itemsHtml = "";
	for(var i = 0; i<totalPageNumber; i++){
		
		var itemHtml = "<div style='display:inline-block; text-align:center; font-size:12px; '>"+
			"<p style='margin:0px; padding: 0px;' class='text-center'>"+qrcodeImg.prop("outerHTML")+"</p>"+
			"<p style='margin:0px;margin-top: -5px; padding: 0px; font-weight: bold;' class='text-center'>&nbsp;&nbsp;&nbsp;"+code+"&nbsp;&nbsp;&nbsp;</p>"+
			"<p style='margin:0px; margin-bottom: 10px; padding: 0px;' class='text-center'>第"+(i+1)+"页，共"+totalPageNumber+"页</p>"+
			"</div>";
		itemsHtml += itemHtml;
	}
	
	var html = '<div id="qrcode-page" style="width: 760px; background: white;">'+itemsHtml+'</div>';
	
	$(html).print();
	
}