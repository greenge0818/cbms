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
            url: Context.PATH + '/report/business/loadcontractlist.html',
            type: 'POST',
            data: function (d) {
            	buildParam(d);
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
            var amount = parseFloat(aData.totalAmount);
            $('td:eq(6)', nRow).html(formatMoney((amount), 2)).addClass("text-right");
        },
        columns: [
            {data: 'type'},
            {data: 'contractCode'},
            {data: 'accountName'},
            {data: 'orderOrgName'},
            {data: 'ownerName'},
            {data: 'createdTime'},
            {data: 'totalAmount'},
			{data: 'custLabel'},
            {data: "note"}
        ],
        "fnDrawCallback": function (row, data, start, end, display) {
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
	var contractType = $("#contractType").val();
	if(contractType)
		d.contractType = contractType;
	var buyerName = $("#accountName").val();
	if(buyerName)
		d.buyerName = buyerName;
	var contractNo = $("#contractNo").val();
	if(contractNo)
		d.contractNo = contractNo;
    
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
    form.attr('action', Context.PATH + "/report/business/loadcontractlistexport.html");

    for(var i in d){
    	var input = $('<input>').attr('type', 'hidden')
	 		.attr('name', i).attr('value', d[i]);
    	form.append(input);
    }
   
    $('body').append(form);

    form.submit();
    form.remove();
}