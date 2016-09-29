/**
 * Created by lcw on 2015/8/2.
 */

var _weightFixedLength = 6;
var _amountFixedLength = 2;

$(document).ready(function () {
    var table;
    table = $('#dynamic-table').dataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "bLengthChange": false,
        "ajax": {
            url: Context.PATH + '/invoice/in/searchdetailbynsort.html',
            type: 'POST',
            data: function (d) {
                d.nsortName = $("#nsortName").text();
                d.material = $("#material").text();
                d.spec = $("#spec").text();
                d.uuid = $("#uuid").val();
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
            var amount = aData.totalAmount.toFixed(_amountFixedLength);
            var weight = aData.totalWeight.toFixed(_weightFixedLength);
            var displaName = aData.sellerName;
            if(aData.totalDepartment > 1 && aData.departmentName != null){
            	displaName = displaName + '【'+ aData.departmentName +'】';
            }
            $('td:eq(0)', nRow).html(displaName);
            $('td:eq(1)', nRow).html(weight).addClass("text-right");
            $('td:eq(2)', nRow).html(amount).addClass("text-right");
        },
        columns: [
            {data: 'sellerName'},
            {data: 'totalWeight'},
            {data: 'totalAmount'}
        ]
    });
});