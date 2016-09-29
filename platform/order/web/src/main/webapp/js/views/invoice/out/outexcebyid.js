jQuery(function($) {
	
	var id = $("#invoicoutMainId").val();
	
	function exceldate() {
		var dateStart = $("#startTime").val();
		var dateEnd = $("#endTime").val();
		if(dateStart==''){
			cbms.alert("请选择起始日期！");
			return;
		}
		if(dateEnd==''){
			cbms.alert("请选择终止日期！");
			return;
		}
		var form = $("<form>");
	    form.attr('style', 'display:none');
	    form.attr('target', '');
	    form.attr('method', 'post');
	    form.attr('action', Context.PATH + "/invoice/out/excelbyid.html");
	    
	    var input1 = $('<input>');
	    input1.attr('type', 'hidden');
	    input1.attr('name', 'id');
	    input1.attr('value', id);
	    
	    var input2 = $('<input>');
	    input2.attr('type', 'hidden');
	    input2.attr('name', 'dateStart');
	    input2.attr('value', dateStart);
	    
	    var input3 = $('<input>');
	    input3.attr('type', 'hidden');
	    input3.attr('name', 'dateEnd');
	    input3.attr('value', dateEnd);

	    $('body').append(form);
	    form.append(input1);
	    form.append(input2);
	    form.append(input3);
	    
	    form.submit();
	    form.remove();
		
	}

	function excelById() {
		var form = $("<form>");
	    form.attr('style', 'display:none');
	    form.attr('target', '');
	    form.attr('method', 'post');
	    form.attr('action', Context.PATH + "/invoice/out/excelbyid.html");
	    
	    var input1 = $('<input>');
	    input1.attr('type', 'hidden');
	    input1.attr('name', 'id');
	    input1.attr('value', id);
	    
	    $('body').append(form);
	    form.append(input1);

	    form.submit();
	    form.remove();
		
	}
	
	$("#excelBtn").click(function() {
		excelById();
	})
	
	$("#download").click(function() {
		$.ajax({
			type : "POST",
			url : Context.PATH + "/invoice/out/getbylist.html",
			data : {
				"id" : id,
				"dateStart":$("#startTime").val(),
				"dateEnd":$("#endTime").val()	
			
			},
			success : function(result) {
				if(!result.success){
					cbms.alert("该时间段没有数据，请重新选择！");
					return;
				}else{				
						exceldate();
				}
			},
			error : function(xhr, textStatus, errorThrown) {
				
			}
		});			
	})
	
})
