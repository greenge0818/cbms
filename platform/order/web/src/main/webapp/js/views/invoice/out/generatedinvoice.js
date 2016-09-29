var dt;

$(document).ready(function(){
    // 列表
    initTable();
    // 操作
    //initClickEvent();
    /**
     * 点击：选择报表中心，服务中心选择框
     */
    $("#orgSelectBtn").click(showSelectOptionsBox);

    /**
     * 选中：所有服务中心时，取消其它服务中心选项框, 选中其它服务中心时，取消：所有服务中心
     */
    clickSelectAll();

    $("#search_btn").click(function(){
    	dt.ajax.reload();
    });
});

// 列表
function initTable(){
    dt = $('#generated-table').DataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "ajax": {
            url: Context.PATH + '/invoice/out/querygeneratedinvoice.html',
            type: 'POST',
            data: function(d){
            	d.status = '';
            	
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
            	if(orgIds && orgIds.length>0){
            		if(d) d.orgIds = orgIds.toString();
            	}
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
            if($('#allowViewChecklist').val()=="true") {
                renderOperation(nRow, aData, iDataIndex);
            }
            var created = new Date(aData.created);
            $('td:eq(1)', nRow).html(created.Format("yyyy-MM-dd"));
            var amount = parseFloat(aData.amount);
            $('td:eq(3)', nRow).addClass("text-right").html(formatMoney((amount), 2)).addClass("text-right");
        },
        columns: [
            {data: 'id'},
            {data: 'created'},
            {data: 'orgName'},
            {data: 'amount'},
            {data: 'statusName'},
            {defaultContent: ''}
        ],
        columnDefs: [
            {
                sDefaultContent: '', //解决请求参数未知的异常
                aTargets: ['_all']
            }
        ]
    });
}

/**
* 点击：选择报表中心，服务中心选择框
*/
function showSelectOptionsBox(){
	var optionbox = $("#orgSelect");
	if(optionbox.css("display") == "none"){
		optionbox.show();
		$(document).on("mouseleave","#org_options", function(){
			optionbox.hide();
		});
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
 * 查看开票清单
 * @param nRow
 * @param aData
 * @param iDataIndex
 */
function renderOperation(nRow, aData, iDataIndex){

    var url = Context.PATH + "/invoice/out/checklist/"+aData.id+"/index.html", //查看开票清单连接
        html = '<div class="hiddenh-sm hidden-xs action-buttons">';

    html += '<a href="' + url + '" class="btn btn-xs btn-info">查看开票清单</a>';
    html += '</div>';
    $('td:eq(-1)', nRow).html(html);
}
