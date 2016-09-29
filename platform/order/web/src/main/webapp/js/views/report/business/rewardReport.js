	
    jQuery(function ($) {

		//搜索事件
		$("#queryBtn").click(function () {
			submitForm();

		});
		$("#exportBtn").click(function () {
			exportExcel();

	 });
		$("#exportBtn2").click(function () {
			exportExcel("collect");

		});
	});


	function submitForm(){
		$("#init").val(false);
		$("#searchForm").submit();
	}

	function exportExcel(a){
		var form = $("<form>");
		form.attr('style', 'display:none');
		form.attr('target', '');
		form.attr('method', 'post');

		if(a==null)
			form.attr('action', Context.PATH + "/report/reward/exportexcel.html");
		else
			form.attr('action', Context.PATH + "/report/reward/collectexcel.html");
		var input1 = $('<input>');
		input1.attr('type', 'hidden');
		input1.attr('name', 'orgName');
		input1.attr('value', $("#sorganization").val());

		var input2 = $('<input>');
		input2.attr('type', 'hidden');
		input2.attr('name', 'month');
		input2.attr('value', $("#month").val());

		$('body').append(form);
		form.append(input1);
		form.append(input2);

		form.submit();
		form.remove();
	}