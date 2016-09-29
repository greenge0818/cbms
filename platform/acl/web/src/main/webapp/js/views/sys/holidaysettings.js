/**
 * Created by lixiang on 2016/9/18.
 */

var dt = "";

function initTable() {
    var url = Context.PATH + "/sys/get/holidaySettings.html";
    dt = $("#dynamic-table").DataTable({
        ajax: {
            url: url,
            type: "POST",
            data: function (d) {
            	 d.startTime = $("#startTime").val();
            	 d.endTime = $("#endTime").val();
            }
        },
        serverSide: true, //服务模式
        processing: true,//显示数据加载进度
        searching: false, //是否启用搜索
        ordering: false, //是否启用排序
        lengthChange: false, //不显示pageSize的下拉框
        oLanguage: {sUrl: Context.PATH + "/js/DT_zh.txt"}, //自定义语言包
        bFilter: false,
        bInfo: false,
        iDisplayLength: 50,
        columns: [
	        {data: 'title'},  //客户数
	        {data: 'holidayDate'}, //状态
	        {defaultContent: ''} //操作,
        ],
        columnDefs: [
             {
                 "targets": 1, //第几列 从0开始
                 "data": "holidayDate",
                 "render": formatDay
             }        
        ],
        fnRowCallback: function (nRow, aData, iDataIndex) {
        	var link = "<a href='javascript:void(0)' holidayId='"+aData.id+"' class='editHoliday'>修改</a>";
        	$('td:eq(-1)', nRow).html(link);
        }
    });
}


	
	
$(document).on("click", ".editHoliday", function ()  {
	var holidayId = $(this).attr("holidayId");//假期id
	$.ajax({
        url: Context.PATH + '/sys/getHoliday.html',
        type: "POST", 
        data : {
			"holidayId" : holidayId
		},
        success: function (result) {
    		cbms.getDialog("修改假期", '#determine');
    		
        	$("#title").val(result.data.title);
        	$("#holidayDate").val(result.data.holidayDate);
 
    		$(document).on("click", "#btnClose",function () {
    	       cbms.closeDialog();
    	    });
    		 
    		$(document).off('click', '#btncommit');
    		
    		$(document).on('click', '#btncommit', function() {
    			var title = $("#title  option:selected").text();
    			var holidayDate = $("#holidayDate").val();
    			var forms = setlistensSave("#form-horizontal");
    			if (!forms)return;
    			saveHoliday(holidayId, title, holidayDate);
    		});
        }
    });
});

function saveHoliday(holidayId, title, holidayDate) {
	$.ajax({
		type: 'post',
		url: Context.PATH + '/sys/saveHoliday.html',
		data: {
			"id" : holidayId,
			"title": title,
			"holidayDate": holidayDate
		},
		success: function (result) {
			if (result && result.success) {
				cbms.closeDialog();
            	cbms.alert("提交成功！", function() {
            		dt.ajax.reload();
    			});
            } else {
                cbms.alert(result.data);
            }
		}
 	});
}

$(document).on("click", "#addHoliday", function ()  {
	cbms.getDialog("新增假期", '#determine');
	 
	$(document).on("click", "#btnClose",function () {
	   cbms.closeDialog();
	});
	 
	$(document).off('click', '#btncommit');
	
	$(document).on('click', '#btncommit', function() {
		var forms = setlistensSave("#form-horizontal");
		if (!forms)return;
		var title = $("#title  option:selected").text();
		var holidayDate = $("#holidayDate").val();
		saveHoliday(null, title, holidayDate);
	});
});

function formatDay(data) {
	var dt = new Date(data);
	var time = dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate() + " " + 
	((dt.getHours()/1)<10?("0"+dt.getHours()):dt.getHours()) + ":" + ((dt.getMinutes()/1)<10?("0"+dt.getMinutes()):dt.getMinutes()) + ":" 
	+ ((dt.getSeconds()/1)<10?("0"+dt.getSeconds()):dt.getSeconds());
	return time;
}


$("#searchForm").on("click","#btn", function() {
    dt.ajax.reload();
});

jQuery(function($) {
	 initTable();
});