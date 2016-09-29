var dt;
$(document).ready(function () {

	$("#payStatus").selectCheckBox(Context.PATH+"/flow/contracttemplate/getAccountContractTemplateType.html", {},doSearch);

    dt = $('#dynamic-table').DataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "bLengthChange": false,
        "ajax": {
            url: Context.PATH + '/flow/contracttemplate/loadlist.html',
            type: 'POST',
            data: function (d) {
                d.ids = $("#ids").val();
                d.companyName = $("#companyName").val();
                d.orgId = $("#orgId").val();
				d.contractTemplateTypeList = getStatusValuesById("payStatus");
            }
        },
	    "fnRowCallback": function (nRow, aData, iDataIndex) {
			$('td:eq(0)', nRow).html(aData.companyName + "(" + aData.accountName + ")");
			$('td:eq(1)', nRow).html(generateContractTypeHtml(aData.type));
			var name = encodeURIComponent(encodeURIComponent(aData.name));
			var urlParam = "?id=" + aData.id + "&name=" + name + "&accountId=" + aData.accountId + "&companyId=" + aData.companyId + "&type=" + aData.type + "&companyName=" + aData.companyName;
			$('td:eq(3)', nRow).html("<a href='" +Context.PATH + "/flow/contracttemplate/toauditcontracttemplate.html" + urlParam + "'>审核合同模板</a>");
		},
	    columns: [
	        {data: 'accountName'},
			{data: 'type'},
			{data: 'name'},
	        {data: 'name'}
	    ]

    });
});

function doSearch(){
	dt.ajax.reload();
}

function generateContractTypeHtml(type) {
	if(type == "buyer"){
		return "买家合同";
	}
	if(type == "seller"){
		return "卖家合同";
	}
	if(type == "frame"){
		return "框架合同";
	}
	if(type == "channel"){
		return "款道合同";
	}
	return "";
}

function getStatusValuesById(id){
	var checkedBoxes = $("#"+id).find(".mulsel-content-list ul").find("input[type='checkbox']:checked");
	var array = [];
	checkedBoxes.each(function (){
		array.push($(this).val());
	});
	return array.join(",");
}

