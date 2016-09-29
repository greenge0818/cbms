var tradeTable;
jQuery(function ($) {
    initTable();
});
function initTable() {
    var url = Context.PATH + "/kuandao/setting/queryPrcsteelAccount.html";
    tradeTable = jQuery("#dynamic-table").DataTable({
        "sScrollY": "400px",
        "bScrollCollapse": true,
        "iDisplayLength": 50,
        "ajax": {
            "url": url
            , "type": "POST"
            , data: function (d) {
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'no'},
            {data: 'memeberName'},
            {data: 'memeberCode'}
            ,{data: 'bankName'}
            , {data: 'virAcctNo'}
            , {data: 'acctNo'}
            ,{data: 'idNo'}
            , {data: 'mobile'}
            , {data: 'isOpen'}
            
        ]
        , columnDefs: [
           			{
           			    "targets": 8, //第几列 从0开始
           			    "data": "isOpen",
           			    "render": function(data, type, full, meta){
           			    	var text = '是';
           			    	
           			    	return text;
           			    }
           			}
                    ,{
                    	'sDefaultContent': '',
                    	 'targets': [ '_all' ]
                    }
         ]
        
    });
    
    tradeTable.on( 'init.dt order.dt search.dt', function () {
    	tradeTable.column(0, {init:'applied',search:'applied', order:'applied'}).nodes().each( function (cell, i) {
    		cell.innerHTML = i+1;
    		} );
    	} ).draw();
}

