$(document).ready(function () {
	//加载服务中心
    $.post(Context.PATH + '/common/allOrganizationList.html','',function(result){
        $("#serviceArea").empty();
        $("#serviceArea").append("<option value=''>-选择服务中心-</option>");
        if(result.success){
            for (var i = 0; i < result.data.length; i++) {
                $("#serviceArea").append("<option value='" + result.data[i].id + "'>" + result.data[i].name + "</option>");
            }
        }
    });
	
	var table;
	var beginIndex;
	table = $('#dynamic-table').dataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "bLengthChange": true,
        "iDisplayLength" : 100,
        "aLengthMenu" : [10,30,50,100],//定义每页显示数据数量
        "ajax": {
            url: Context.PATH + '/resource/searchforstatisres.html',
            type: 'POST',
            data: function (d) {
            	d.cityName=$("#cityName").val()
            	d.userId = $("#userId").val();
                d.sourceType = $("#sourceType").val();
                d.beginDate = $("#beginDate").val();
                d.endDate = $("#endDate").val();
                d.orgId=$("#serviceArea").val();
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
        	$('td:eq(0)', nRow).html(beginIndex+iDataIndex);
        	$('td:eq(5)', nRow).html(aData.dailyCount+aData.inquiryCount+aData.historyCount);
        },
        "fnPreDrawCallback": function (nTable) {
        	beginIndex = 1;
        	beginIndex += nTable._iDisplayStart;
        },
        "drawCallback": function(e,t) {
        	//隐藏列
        	var sourceType = $("#sourceType").val();
        	var showIndex = 0;
        	var hideIndex;
        	var hideIndex1;
        	if(sourceType =='daily_common'){
        		showIndex = 2;
        		hideIndex = 3;
        		hideIndex1 =4;
        	}
        	if(sourceType =='inquiry'){
        		showIndex = 3;
        		hideIndex = 2;
        		hideIndex1 =4;
        	}
        	if(sourceType =='history_transaction'){
        		showIndex = 4;
        		hideIndex = 2;
        		hideIndex1 =3;
        	}
        	if(showIndex != 0){
        		$("#dynamic-table th:eq("+showIndex +")").show();
        		$("#dynamic-table th:eq("+hideIndex +")").hide();
        		$("#dynamic-table th:eq("+hideIndex1 +")").hide();
    			$("#dynamic-table tr").each(function(){
    				$(this).find("td:eq("+showIndex +")").show();
    				$(this).find("td:eq("+hideIndex +")").hide();
    				$(this).find("td:eq("+hideIndex1 +")").hide();
    			});
        	}else{
        		$("#dynamic-table th:eq(2)").show();
        		$("#dynamic-table th:eq(3)").show();
        		$("#dynamic-table th:eq(4)").show();
    			$("#dynamic-table tr").each(function(){
    				$(this).find("td:eq(2)").show();
    				$(this).find("td:eq(3)").show();
    				$(this).find("td:eq(4)").show();
    			});
        	}
        },
        "fnFooterCallback": function( nFoot, aData, iStart, iEnd, aiDisplay ){
        	//统计合计项
        	var i=0;
        	var totalDaily = 0;
        	var totalInquiry = 0;
        	var totalHistory = 0;
        	for(;i<aData.length;i++){
        		totalDaily+= aData[i].dailyCount;
        		totalInquiry+= aData[i].inquiryCount
        		totalHistory+= aData[i].historyCount;
        	}
        	$("#totalDaily").html(totalDaily);
        	$("#totalInquiry").html(totalInquiry); 
        	$("#totalHistory").html(totalHistory);
        	$("#totalAll").html(totalDaily+totalInquiry+totalHistory);
        	//统计项汇总
        	var count = 0;
        	if(aData.length > 0){
        		$("#totalLbl").text(aData[0].statisTotal.total);
        		$("#dailyTotalLbl").text(aData[0].statisTotal.dailyTotalCount);
        		$("#inquiryTotalLbl").text(aData[0].statisTotal.inquiryTotalCount);
        		$("#historyTotalLbl").text(aData[0].statisTotal.historyTotalCount);
        	}else{
        		$("#totalLbl").text(count);
        		$("#dailyTotalLbl").text(count);
        		$("#inquiryTotalLbl").text(count);
        		$("#historyTotalLbl").text(count);
        	}
        },
        columns: [
            {data: 'accountId'},
            {data: 'name'},
            {data: 'dailyCount'},
            {data: 'inquiryCount'},
            {data: 'historyCount'},
            {data: 'accountId'},
        ]
    });

    $("#searchList").on("click", function () {
    	table.fnDraw();
    });
    
    //清空按钮
    $("#cleanSearch").on("click", function () {
    	resetForm($("form.form-inline"));
    });
});
