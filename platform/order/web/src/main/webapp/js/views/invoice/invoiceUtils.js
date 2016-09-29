var invoiceIds;           // 发票ID集合
//统计选中的发票
function invoiceTotal() {
	invoiceIds = [];
	var checked = $("input[name='check']:checked").not("#allCheck");
	$(checked).each(function () {
		var id = $(this).val();
		invoiceIds.push(id);
	});

	var checkedCount = checked.length;
	$('#checkCount').text(checkedCount);
}
// 时间转换成字符串
function dateToString(date) {
    var dt = new Date(date);
	var time = dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate() + " " +
			((dt.getHours()/1)<10?("0"+dt.getHours()):dt.getHours()) + ":" + ((dt.getMinutes()/1)<10?("0"+dt.getMinutes()):dt.getMinutes()) + ":" + ((dt.getSeconds()/1)<10?("0"+dt.getSeconds()):dt.getSeconds());
	return time;
}
//时间转换成字符串
function dateToStringTODay(date) {
    var dt = new Date(date);
    var time = dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate();
    return time;
}