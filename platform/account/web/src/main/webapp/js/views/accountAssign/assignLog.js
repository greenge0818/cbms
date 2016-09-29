var dt;

$(document).ready(function() {
   
	queryIsAdmin();	 
    $("#queryBtn").on("click", function () {
        dt.ajax.reload();
    });
} );

function formatDay(data) {
    var dt = new Date(data);
    var time = dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate() + " " +
        ((dt.getHours()/1)<10?("0"+dt.getHours()):dt.getHours()) + ":" + ((dt.getMinutes()/1)<10?("0"+dt.getMinutes()):dt.getMinutes()) + ":" + ((dt.getSeconds()/1)<10?("0"+dt.getSeconds()):dt.getSeconds());
    return time;
}
//判断是否为管理员 add by zhoucai@prcsteel.com 2016-3-16

function queryIsAdmin(){
	//情况下拉框	
	
	$("#org").empty();
	$.ajax({
		url : Context.PATH + "/transfer/queryisadmin.html",
		type : "POST",
		data: {
			
        },
	    success: function (result) {
	    	
	    	var isAdmin=result.recordsTotal;	    		
	    	queryManagerLists(isAdmin);	    	
	    }
	});
		
	
}
//查询服务中心列表，并根据角色显示 add by zhucai@prcsteel.com 2016-3-16

function queryManagerLists(isAdmin){
	//情况下拉框	
	$("#org").empty();
	var currentOrgId=$("#currentOrgId").val();
	var currentOrgName=$("#currentOrgName").val();
	if(isAdmin==1){		
		$.ajax({
			url : Context.PATH + "/transfer/querymanagerlists.html",
			type : "POST",
			data: {
				'orgId' : ''
	        },
		    success: function (result) {
		    	if(result.success){
		    		var users = result.data;
		    		if(users!=null &&users.length>0) {	
		    				$("#org").append("<option value=''>"+'-----全部-----'+"</option>");
		    			for (var i = 0; i < users.length; i++) {
		    				$("#org").append("<option value='"+users[i].id+"'>"+users[i].name+"</option>"); 
		    			}
		    		}
		    	}
		    }
		});
		
	}else{
		$("#org").append("<option value='"+currentOrgId+"' selected=>"+currentOrgName+"</option>"); 
	}	
	initTable();
}
//modify by zhoucai@prcsteel.com 注解control 命名为全小写 2016-3-18
function initTable(){
	
	 dt = $('#dynamic-table').DataTable( {
	        "pageLength": 50, //每页记录数
	        "processing": true,//显示数据加载进度
	        "serverSide": true, //服务模式
	        "searching": false, //是否启用搜索
	        "ordering": false, //是否启用排序
	        "oLanguage": {sUrl: Context.PATH + "/js/DT_zh.txt"}, //自定义语言包
	        "ajax": {
	            url: Context.PATH + '/accountAssign/loadaccountassignlogdata.html',
	            type: 'POST',
	            data: function(d){
	               //客户名称 服务中心
	                d.accountName = $.trim($("#accountName").val());
	                d.orgId = $("#org").val();
	            }
	        },
	        "fnRowCallback":function(nRow, aData, iDataIndex){
	            $('td:first', nRow).html(iDataIndex + 1);
	            $('td:eq(4)', nRow).html(formatDay(aData.created));
	        },
	        columns: [
	            {defaultContent: ''},
	            { data: 'accountName' },
	            { data: 'orgNextName' },
	            { data: 'orgExName' },
	            { data: 'created' },
	            { data: 'assignName' }
	        ]
	    } );

	
}