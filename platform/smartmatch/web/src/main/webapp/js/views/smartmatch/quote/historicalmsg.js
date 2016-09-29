/**
 * Created by lixiang on 2016/06/24.
 */

var dt = "";
function fillDataTable() {
    dt = $('#dynamic-table').DataTable({
	    ajax: {
	        url: Context.PATH + '/smartmatch/quote/get/historicalmsg.html',
	        type: "POST", 
	        data: function (d) {
	            return $.extend({}, d, {
	            	startDate : $("#startTime").val(),//起始时间
	            	endDate : $("#endTime").val(),//终止时间
	            	mobile : $("#tel").val()//电话
	            });
	        }
	    },
	    searching: false,
	    "processing": true,
	    "serverSide": true,
	    "bLengthChange":false,
	    "iDisplayLength" : 50,
	    fnRowCallback: function (nRow, aData, iDataIndex) {
	    	$('td:eq(0)', nRow).html((iDataIndex + 1));
	         return nRow;
	    },
	    columns: [
	        {defaultContent: '',sWidth:'30px'},
			{data: 'created'},
			{data: 'accountName'},
			{data: 'contactName'},
			{data: 'tel'},   
	        {data: 'msgContent'},
	        {data: 'status'}
	    ],
	   
	    columnDefs: [
	        {	
	        	"targets": 1, // 第几列 从0开始
	 			"data": "created",
				"render": formatDay
			},
			{
				"targets": 6, // 第几列 从0开始
	 			"data": "status",
				"render": getStatus
			}
			
	    ]
	});
}

function getStatus(data) {
	var status = "";
	if (data == "sent") {
		status = "已发送";
	}
	return status;
}

function formatDay(data) {
	var dt = new Date(data);
	var time = dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate() + " " + 
	((dt.getHours()/1)<10?("0"+dt.getHours()):dt.getHours()) + ":" + ((dt.getMinutes()/1)<10?("0"+dt.getMinutes()):dt.getMinutes()) + ":" 
	+ ((dt.getSeconds()/1)<10?("0"+dt.getSeconds()):dt.getSeconds());
	return time;
}

jQuery(function($) {
    fillDataTable();
    $("#dateBtn").click(function () {
        dt.ajax.reload();
    });
});

