/**
 * Created by lixiang on 2015/7/31.
 */

var dt = "";
function fillDataTable() {
	    dt = $('#dynamic-table').DataTable({
        ajax: {
            url: Context.PATH + '/order/secondapplyforpaymentonly.html',
            type: "POST", 
            data: function (d) {
                return $.extend({}, d, {
                	gatheringBar:$("#selected").val(),//类型
                	accountName:$("#buyer_name").val(),//单位全称 
                	orgId:$("#org").val(),//服务中心
                	userName:$("#owner").val()//交易员
                });
            }
        },
        searching: false,
        "processing": true,
        "serverSide": true,
        "bLengthChange":false,
        fnRowCallback: function (nRow, aData, iDataIndex) {
        	if(aData.balanceSecondSettlement < 0){
        		var receivablePay ="<input type='hidden' name='ys' value="+aData.balanceSecondSettlement+">"+formatMoney(Math.abs(aData.balanceSecondSettlement),2);//应收款
        	}else{
        		var handlePay = "<input type='hidden' name='yf' value="+aData.balanceSecondSettlement+">"+formatMoney(aData.balanceSecondSettlement,2);//应付款
        	}
        	
             $('td:eq(3)', nRow).html(receivablePay).addClass("text-right");
             $('td:eq(4)', nRow).html(handlePay).addClass("text-right");
             var link = '<a href="' + Context.PATH + '/order/secondapplyforpaymentorder.html?id='+aData.departmentId+'">查看详情</a>';
             $('td:eq(-1)', nRow).html(link);
             return nRow;
        },
            drawCallback: function(){
                setTotalRow();
            },
        columns: [
            {data: 'accountName'},   //单位全称
            {data: 'departmentName'}, //部门
            {data: 'managerName'},  //交易员
            {defaultContent: ''}, //应收金额
            {defaultContent: ''},  //应付金额
            {defaultContent: ''}
        ],
        "oLanguage": {                          //汉化
            "sLengthMenu": "每页显示 _MENU_ 条记录",
            "sZeroRecords": "没有检索到数据",
            "sInfo": "当前数据为从第 _START_ 到第 _END_ 条数据；总共有 _TOTAL_ 条记录",
            "sInfoEmtpy": "没有数据",
            "sProcessing": "正在加载数据...",
            "oPaginate": {
                "sFirst": "首页",
                "sPrevious": "前页",
                "sNext": "后页",
                "sLast": "尾页"
            }
        },
        columnDefs: [
			
        ]
    });
    
}


jQuery(function($) {
	
    fillDataTable();
    window.setTimeout(setTotalRow,500);
    $("#searchForm").on("click","#btn", function() {
        dt.ajax.reload();
        window.setTimeout(setTotalRow,500);
    });
    
    $(document).on("click","#export_btn", function() {
    	exportToExcel();
    });
    
});


/**
 * 导出报表
 */
function exportToExcel() {
    var form = $("<form>");
    form.attr('style', 'display:none');
    form.attr('target', '');
    form.attr('method', 'post');
    form.attr('action', Context.PATH + "/order/exportsecondapplyforpaymentonly.html");
    form.attr("enctype","multipart/form-data");//防止提交数据乱码

    var gatheringBar =  $("#selected").val();
    if(gatheringBar)
    	form.append("<input type='hidden' name='gatheringBar' value='"+gatheringBar+"'/>");
    
    var buyerName = $("#buyer_name").val() || '';
	form.append("<input type='hidden' name='buyerName' value='"+buyerName+"'/>");
    	
    var orgName = $("#sorganization").val();
    if(orgName)	
    	form.append("<input type='hidden' name='orgName' value='"+orgName+"'/>");

    var userName = $("#owner").val();
    if(userName)
    	form.append("<input type='hidden' name='userName' value='"+userName+"'/>");
    
    $('body').append(form);

    form.submit();
    form.remove();
}
