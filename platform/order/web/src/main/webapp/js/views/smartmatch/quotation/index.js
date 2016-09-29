var _dtQutotionOrderItems;

$(document).ready(function() {
	var url = Context.PATH + "/smartmatch/quotation/search.html";
	_dtQutotionOrderItems = jQuery("#dynamic-table").DataTable({
		"processing" : false,
		"serverSide" : false,
		"searching" : false,
		"ordering" : false,
		"paging" : false,
		"bInfo" : false,
		"autoWidth": true,
		"ajax" : {
			"url" : url,
			"type" : "POST",
			data : function(d) {
				return $.extend({}, d, {
					 id:QuotationPage.ID
				});
			}
		},
		columns : [
		 {data : 'categoryName'},
		 {data : 'materialName'},
		 {data : 'spec'},
		 {data : 'factoryName'},
		 {data : 'warehouseName'},

		 {data : 'weight'},
		 {data : 'dealPrice'},
		 {data : 'totalAmount'},
		 {data : 'remark'}
		],
		footerCallback: function (row, data, start, end, display) {
			$("#dynamic-table th").css("background-color","#E87716").css("color","#000000");
            var api = this.api(), total;
            //当前页汇总
            total = pageTotal(api);
            $(".weight").html(total.pageTotalWeight.toFixed(2));
            $(".price").html(total.pageTotalAmount.toFixed(2));
        }
	});
});


function pageTotal(api) {
    var weight = 5,  amount= 7;
    var total = {
        pageTotalWeight: pageTotalColumn(api, weight),
        pageTotalAmount: pageTotalColumn(api, amount)
    }
    return total;
}

$(document).on("click", ".confirm", function(){
	$.ajax({
		type: "POST",
		url: Context.PATH + "/smartmatch/quotation/confirm.html",
		data: {
			id : QuotationPage.ID
		},
		dataType: "json",
		success: function (response) {
			if(response.success){
				location.href = Context.PATH + "/smartmatch/purchaseorder/list.html";
			}else{
				utils.showMsg(response.data, null, "error", 2000);
			}
		}
	});
});
