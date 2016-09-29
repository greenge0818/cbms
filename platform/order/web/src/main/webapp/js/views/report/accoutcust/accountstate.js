/**
 * Created by lixiang on 2015/8/22.
 */

var dt = "";
function fillDataTable() {
		var type = $("#type").val();//类型，买家or卖家
		var dateStart = $("#startTime").val();
		var dateEnd = $("#endTime").val();
	    dt = $('#dynamic-table').DataTable({
        ajax: {
            url: Context.PATH + '/report/accoutcust/state/data.html',
            type: "POST", 
            data: function (d) {
                return $.extend({}, d, {
                	dateStartStr:$("#startTime").val(),//起始时间
                	dateEndStr:$("#endTime").val(),//终止时间
                	accountName:$("#account_name").val(),//买家全称
                	orgName:$("#sorganization").val() == "无"?"":$("#sorganization").val(),//服务中心
                	type:type//类型，买家or卖家
                });
            }
        },
        searching: false,
        "processing": true,
        "serverSide": true,
        "bLengthChange":false,
        fnRowCallback: function (nRow, aData, iDataIndex) {
        	 var emptyAmount ="0.00";
        	 var balance = aData.balance == null ? emptyAmount:formatMoney(aData.balance,2);
        	 var balanceSecondSettlement = aData.balanceSecondSettlement == null ? emptyAmount:formatMoney(aData.balanceSecondSettlement,2);
        	 var bringAmount = aData.bringAmount == null ? emptyAmount:formatMoney(aData.bringAmount,2);
        	 var amount = aData.amount == null ? emptyAmount:formatMoney(Math.abs(aData.amount),2);
        	 var totalAmount = aData.totalAmount == null ? emptyAmount:formatMoney(aData.totalAmount,2);
        	 if(type == 'buyer'){
        		 var link = '<a href="javascript:void(0);" buyeraccountid='+aData.accountId+'>'+aData.cName+'</a>';
        	 }else{
        		 var link = '<a href="javascript:void(0);" selleraccountid='+aData.accountId+'>'+aData.cName+'</a>';
        	 }
        	 
        	 $('td:eq(0)', nRow).html(link);
        	 $('td:eq(1)', nRow).html(totalAmount).addClass("text-right");
             $('td:eq(2)', nRow).html(amount).addClass("text-right");
             $('td:eq(3)', nRow).html(bringAmount).addClass("text-right");
             $('td:eq(4)', nRow).html(balance).addClass("text-right");
             $('td:eq(5)', nRow).html(balanceSecondSettlement).addClass("text-right");
            
             return nRow;
        },
        columns: [
            {defaultContent: ''},   //买家全称
            {defaultContent: ''},  //合同金额
            {defaultContent: ''}, //付款总金额
            {defaultContent: ''},  //实提总金额
            {defaultContent: ''}, //资金账户余额
            {defaultContent: ''} //结算余额
        ],
        "oLanguage": {                          //汉化
            "sLengthMenu": "每页显示 _MENU_ 条记录",
            "sZeroRecords": "没有检索到数据",
            "sInfo": "当前数据为从第 _START_ 到第 _END_ 条数据；总共有 _TOTAL_ 条记录",
            "sInfoEmtpy": "没有数据",
            "sProcessing": "正在加载数据...",
            "oPaginate": {
                "sFirst": "首页",
		        "sPrevious": "上一页",
		        "sNext": "下一页",
                "sLast": "尾页"
            }
        },
        columnDefs: [
			
        ]
    });
    
}


jQuery(function($) {
    fillDataTable();
    $("#searchForm").on("click","#queryBtn", function() {
    	if($("#startTime").val()==''){
    		cbms.alert("请选择起始日期！");
    		return;
    	}
    	if($("#endTime").val()==''){
    		cbms.alert("请选择终止日期！");
    		return;
    	}
        dt.ajax.reload();
    });
    
        $("#dynamic-table").on("click","a",function(){
    	var accountid=$(this).attr("buyeraccountid");
    	var url=Context.PATH + '/report/accoutcust/buyerstatedetails.html';
    	if(typeof(accountid)=='undefined'){
    		accountid=$(this).attr("selleraccountid");
    		url=Context.PATH + '/report/accoutcust/sellerstatedetails.html';
    	}
    	var dateStartStr=$("#startTime").val();
    	var dateEndStr=$("#endTime").val();
    	location.href=url+"?id="+accountid+"&dateStartStr="+dateStartStr+"&dateEndStr="+dateEndStr;
    	//$.StandardPost(url,{id:accountid,dateStartStr:dateStartStr,dateEndStr:dateEndStr});
    });
    
});

$.extend({
    StandardPost:function(url,args){
        var body = $(document.body),
            form = $("<form method='post'></form>"),
            input;
        form.attr({"action":url});
        $.each(args,function(key,value){
            input = $("<input type='hidden'>");
            input.attr({"name":key});
            input.val(value);
            form.append(input);
        });

        form.appendTo(document.body);
        form.submit();
        document.body.removeChild(form[0]);
    }
});