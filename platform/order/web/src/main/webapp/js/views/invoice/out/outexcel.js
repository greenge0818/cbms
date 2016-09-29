jQuery(function($) {
	function excel() {
		var form = $("<form>");
	    form.attr('style', 'display:none');
	    form.attr('target', '');
	    form.attr('method', 'post');
	    form.attr('action', Context.PATH + "/invoice/out/excel.html");
	    $('body').append(form);
	   
	    form.submit();
	    form.remove();
		
	}

	$("#excelBtn").click(function() {
		excel();
	})
})
