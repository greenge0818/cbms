$(document).ready(function () {
    var table;
    table = $('#dynamic-table').dataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "bLengthChange": false,
        "aLengthMenu":[50, 100, 150],
        "bLengthChange": true, //不显示每页长度的选择条
        "ajax": {
            url: Context.PATH + '/report/business/loadinvoiceoutchecklist.html',
            type: 'POST',
            data: function (d) {
            	buildParam(d);
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {

            var weight = parseFloat(aData.weight),
                amount = parseFloat(aData.amount);
            $('td:eq(1)', nRow).html((new Date(aData.applyTime).Format("yyyy-MM-dd")));
            $('td:eq(5)', nRow).html(weight.toFixed(6)).addClass("text-right");
            $('td:eq(6)', nRow).html(formatMoney((amount), 2)).addClass("text-right");
            var td3 = aData.nsortName;
            if(aData.material){
            	td3 += " " + aData.material;
            }
            $('td:eq(3)', nRow).html(td3);
          
//            if(aData.typeOfSpec && _specSignList[aData.typeOfSpec] && aData.typeOfSpec != "none"){
//            	$('td:eq(4)', nRow).html(_specSignList[aData.typeOfSpec] + " " + $('td:eq(4)', nRow).text());
//            }

            //console.log(aData);
//            if(aData.orgName){
//            	$('td:eq(7)', nRow).html(aData.orgName.replace("服务中心", ""));
//            }
            
        },
        columns: [
            {data: 'id'},
            {data: 'applyTime'},
            {data: 'buyerName'},
            {data: 'nsortName'},
            {data: 'spec'},
            {data: 'weight'},
            {data: 'amount'},
            {data: "orgName"},
            {data: "invoiceType"}
        ],
        "fnDrawCallback": function (row, data, start, end, display) {
        	
        	var d = {};
        	buildParam(d);
        	$.ajax({
        		type : "POST",
        		url : Context.PATH + '/report/business/loadinvoiceoutchecklisttotal.html',
        		data : d,
        	    success: function (result) {

        	    	var totalWeightNode = $("#pageTotalWeight");
    	        	var totalAmountNode = $("#pageTotalAmount");
        	    	if(result.success){
        	    		totalWeightNode.html(result.data.totalWeight.toFixed(6));
                		totalAmountNode.html(result.data.totalAmount.toFixed(2));
        	    	}else{
        	    		totalWeightNode.html("0.0000");
                		totalAmountNode.html("0.00");
        	    	}
        	    }
        	});
        	
        	//统计当前页面的
        	/*
        	var totalWeightNode = $("#pageTotalWeight");
        	var totalAmountNode = $("#pageTotalAmount");
        	
        	var rows = $("#dynamic-table tbody tr");
        	if(rows.length==1 && rows.eq(0).children().hasClass("dataTables_empty")){
        		totalWeightNode.html("0");
        		totalAmountNode.html("0");
        	}else{
        		var totalWeight = 0;
        		var totalAmount = 0;
        		for(var i=0; i<rows.length; i++){
        			var weight = (rows.eq(i).children().eq(5).html());
        			weight = weight.replace(",","");
        			if(weight=="-"){
        				weight = 0;
        			}
        			
        			totalWeight = totalWeight+parseFloat(weight);
        			
        			var amount = (rows.eq(i).children().eq(6).html());
        			amount = amount.replace(",","");
        			if(amount=="-"){
        				amount = 0;
        			}
        			totalAmount = totalAmount+parseFloat(amount);
        		}
        		
        		totalWeightNode.html(totalWeight.toFixed(4));
        		totalAmountNode.html(totalAmount.toFixed(2));
        		
        	}
        	*/
        }
    });

    /**
     * 搜索
     */
    $("#queryBtn").on("click", function () {
        table.fnDraw();
    });

    $(document).on("click", ".btn-export", function(){
        exportExcel();
    })

    
    /**
	 * 点击：选择报表中心，服务中心选择框
	 */
	$("#orgSelectBtn").click(showSelectOptionsBox);
    clickSelectAll();
	$(document).on("mouseleave","#org_options", function(){
		$("#orgSelect").hide();
	});
    
});

function buildParam(d){
	var id = $("#checkListId").val();;
	if(id){
		d.id = id;
	}
	var buyerName = $("#buyerName").val();
	if(buyerName)
		d.buyerName = buyerName;
    
	var beginTime =  $("#beginTime").val();
	if(beginTime)
		d.beginTime = beginTime;
	
	var endTime = $("#endTime").val();
	if(endTime)
		d.endTime = endTime;
    
    var orgIds = [];
	$("#orgSelect li").each(function(){
		var li = $(this);
		var checkbox = li.find("input[type='checkbox']");
		if(checkbox.prop('checked')){
			var orgId = checkbox.val();
			if(orgId!='all'){
				orgIds.push(orgId);
			}
		}
	});
    
	if(orgIds.length>0){
		for(var i=0; i<orgIds.length; i++){
			d["orgIds["+i+"]"] = orgIds[i];
		}
	}
}

/**
 * 点击：选择报表中心，服务中心选择框
 */
function showSelectOptionsBox(){
	var optionbox = $("#orgSelect");
	if(optionbox.css("display") == "none"){
		optionbox.show();
	}else{
		optionbox.hide();
	}
}

/**
 * 选中：所有服务中心时，取消其它服务中心选项框, 选中其它服务中心时，取消：所有服务中心
 */
function clickSelectAll(){
	$("#selectAllOrg").click(function(){
		var checked=$(this).prop('checked');
		if(checked){
			$("#orgSelect li input[type='checkbox']").removeAttr("checked");
			$(this).prop("checked", "checked");
		}
	})
	
	$("#orgSelect li input[type='checkbox']").not("#selectAllOrg").click(function(){
		var selectAll = $("#selectAllOrg");
		if(selectAll.prop("checked")){
			//$(this).removeAttr("checked");
			selectAll.removeAttr("checked");
		}
	});
	
}

/**
 * 导出excel文件
 */
function exportExcel() {
    var form = $("<form>");
    
    var d = {};
	buildParam(d);
	
    form.attr('style', 'display:none');
    form.attr('accept-charset', 'utf-8');
    form.attr('target', '');
    form.attr('method', 'post');
    form.attr('enctype', 'multipart/form-data');
    form.attr('action', Context.PATH + "/report/business/loadinvoiceoutchecklistexport.html");

    for(var i in d){
    	var input = $('<input>').attr('type', 'hidden')
	 		.attr('name', i).attr('value', d[i]);
    	form.append(input);
    }
   
    $('body').append(form);

    form.submit();
    form.remove();
}