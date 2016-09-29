/**
 * Created by lcw on 2015/8/3.
 */

$(document).ready(function () {
    var table = null;
    var uuid = "";

    function getData() {
        if (table == null) {
            table = $('#dynamic-table').dataTable({
                "processing": true,
                "serverSide": true,
                "searching": false,
                "ordering": false,
                "bLengthChange": false,
                "ajax": {
                    url: Context.PATH + '/invoice/in/searchbynsort.html',
                    type: 'POST',
                    data: function (d) {
                        d.uuid = uuid
                    }
                },
                "fnRowCallback": function (nRow, aData, iDataIndex) {
                    var amount = aData.totalAmount.toFixed(2);
                    var weight = aData.totalWeight.toFixed(6);
                    $('td:eq(3)', nRow).html(weight).addClass("text-right");
                    $('td:eq(4)', nRow).html(amount).addClass("text-right");
                    var link = Context.PATH + "/invoice/in/detailbynsort.html?"
                        + "nsortName=" + encodeURI(encodeURI(aData.nsortName))
                        + "&material=" + encodeURI(encodeURI(aData.material))
                        + "&spec=" + aData.spec
                        + "&uuid=" + uuid;
                    $('td:eq(5)', nRow).html(generateOptHtml(link));
                },
                columns: [
                    {data: 'nsortName'},
                    {data: 'material'},
                    {data: 'spec'},
                    {data: 'totalAmount'},
                    {data: 'totalWeight'},
                    {defaultContent: ''}
                ]
            });
        }
        else {
            table.fnDraw();
        }
    }

    $("#sortList li a").click(function () {
        uuid = $(this).attr("uuid");
        $("#sortList li").removeClass("active");
        $(this).parent().addClass("active");
        getData();
    });

    // 默认选中第一个
    $("#sortList li a").eq(0).click();
});

function generateOptHtml(link) {
    var optHtml = '<div class="hidden-sm hidden-xs action-buttons">';
    optHtml += "<a href='" + link + "' title='查看详情'>";
    optHtml += "<i class='ace-icon fa fa-search-plus bigger-130 blue'></i></a>";
    optHtml += '</div>';
    optHtml += '<div class="hidden-md hidden-lg">';
    optHtml += '<div class="inline pos-rel">';
    optHtml += '<button class="btn btn-minier btn-yellow dropdown-toggle" data-toggle="dropdown"';
    optHtml += 'data-position="auto">';
    optHtml += '<i class="ace-icon fa fa-caret-down icon-only bigger-120"></i></button>';
    optHtml += '<ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">';
    optHtml += '<li>';
    optHtml += "<a href='" + link + "' title='查看详情'><i class='ace-icon fa fa-search-plus bigger-130 blue'></i></a>";
    optHtml += '</li>';
    optHtml += '</ul>';
    optHtml += '</div>';
    optHtml += '</div>';
    return optHtml;
}