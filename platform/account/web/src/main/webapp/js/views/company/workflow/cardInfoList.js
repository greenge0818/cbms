var dt;
$(document).ready(function () {
    dt = $('#dynamic-table').DataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "bLengthChange": false,
        "ajax": {
            url: Context.PATH + '/flow/cardinfo/loadlist.html',
            type: 'POST',
            data: function (d) {
                d.ids = $("#ids").val();
                d.queryStatus = $("#queryStatus").val();
                d.companyName = $("#companyName").val();
                d.orgId = $("#orgId").val();
            }
        },
	    "fnRowCallback": function (nRow, aData, iDataIndex) {
	        $('td:eq(1)', nRow).html(generateOptHtml(aData.id));
	    },
	    columns: [
	        {data: 'name'},
	        {data: 'name'}
	    ]

    });
});

function doSearch(){
	dt.ajax.reload();
}

function generateOptHtml(accountId) {
	var queryStatus = $("#queryStatus").val();
	var link = "";
	if(queryStatus ==10000){
		 link = "<a href='" +Context.PATH + "/flow/cardinfo/"+accountId+"/toauditdoc.html'>审核证件资料</a>";
	}
	if(queryStatus == 10001){
		link = "<a href='" +Context.PATH + "/flow/invoice/"+accountId+"/toauditinvoice.html'>审核开票资料</a>";
	}
	if(queryStatus == 10002){
		link = "<a href='" +Context.PATH + "/flow/bank/"+accountId+"/toauditbank.html'>审核打款资料</a>";
	}
	if(queryStatus == 10003){
		link = "<a href='" +Context.PATH + "/flow/annual/"+accountId+"/10003/toauditpurchaseagree.html'>一审年度采购协议</a>";
	}
	if(queryStatus == 10004){
		link = "<a href='" +Context.PATH + "/flow/annual/"+accountId+"/10004/toauditpurchaseagree.html'>二审年度采购协议</a>";
	}
	if(queryStatus == 10005){
		link = "<a href='" +Context.PATH + "/flow/annual/"+accountId+"/10005/toauditpurchaseagree.html'>审核年度采购协议</a>";
	}
	if(queryStatus == 10006 ){
		link = "<a href='" +Context.PATH + "/flow/consignagreement/"+accountId+"/10006/toauditconsignagree.html'>一审代运营协议</a>";
	}
	if(queryStatus == 10007){
		link = "<a href='" +Context.PATH + "/flow/consignagreement/"+accountId+"/10007/toauditconsignagree.html'>二审代运营协议</a>";
	}
	if(queryStatus == 10008){
		link = "<a href='" +Context.PATH + "/flow/consignagreement/"+accountId+"/10008/toauditconsignagree.html'>审核代运营协议</a>";
	}
	return link
}


