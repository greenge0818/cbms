/**
 * 查看待确认的进项票
 * Created by lcw on 2015/9/17.
 */
$(document).ready(function () {
    tobeconfirmedTable = null;
        tobeconfirmedTable = $('#tobeconfirmedTable').dataTable({
            "processing": true,
            "serverSide": true,
            "searching": false,
            "ordering": false,
            "bLengthChange": false,
            "ajax": {
                url: Context.PATH + '/invoice/out/invoiceindata.html',
                type: 'POST',
                data: function (d) {
                    d.ids = $("#applyIds").val()
                }
            },
            "fnRowCallback": function (nRow, aData, iDataIndex) {
                var dt = new Date(aData.invoiceDate);
                var time = dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate();
                var linkHtml = "<a t href='" + Context.PATH + "/invoice/in/confirm/" + aData.sellerId + "/inputinvoice.html?invoiceId="
                    + aData.id + "' target='_blank'>确认</a>";
                $('td:eq(2)', nRow).html(formatMoney(aData.totalAmount, 2)).addClass("text-right");
                if(aData.checkTotalAmount && aData.checkTotalAmount>0){
                    $('td:eq(2)', nRow).html(formatMoney(aData.checkTotalAmount, 2)).addClass("text-right");
                }
                $('td:eq(5)', nRow).html(time);
                $('td:eq(8)', nRow).html(linkHtml);
            },
            columns: [
                {data: 'code'},
                {data: 'sellerName'},
                {data: 'totalAmount'},
                {data: 'inputUserName'},
                {data: 'inputUserMobil'},
                {data: 'invoiceDate'},
                {data: 'expressName'},
                {defaultContent: '待确认'},
                {defaultContent: '确认'}
            ]
        });
});