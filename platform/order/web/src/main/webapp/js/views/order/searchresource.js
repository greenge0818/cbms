/**
 * Created by lcw on 2015/7/21.
 */
$(document).ready(function () {
    //移除绑定事件，防止重复绑定
    $('#searchResource').unbind();
    $('#allCheck').unbind();
    $("input[name='resourcecheck']").unbind();
    var table = null;
    // 搜索超市资源
    $("#searchResource").bind("click", function () {
        var sellerId = $("#sellerCompany").attr("accountid");
        if (sellerId == "" || sellerId == "0") {
            cbms.alert("请输入有效的公司名称！");
            return;
        }
        var consignType = $("#sellerCompany").attr("consigntype");
        $('#resourceTable tbody').empty();
        if (table == null) {
            table = $('#resourceTable').dataTable({
                "processing": true,
                "serverSide": true,
                "searching": false,
                "ordering": false,
                "bLengthChange": false,
                "ajax": {
                    url: Context.PATH + '/order/getresource.html',
                    type: 'POST',
                    data: function (d) {
                        d.sellerCompany = $.trim($("#sellerCompany").val());
                        d.nsortName = $.trim($("#nsortName").val());
                        d.material = $.trim($("#material").val());
                        d.spec = $.trim($("#spec").val());
                        d.factory = $.trim($("#factory").val())
                    }
                },
                "fnRowCallback": function (nRow, aData, iDataIndex) {
                    var checkHtml = "<input type='checkbox' name='resourcecheck' value='" + aData.id + "' city='" + aData.city
                        + "' sellerid='" + sellerId + "' consigntype='" + consignType + "' quantity='" + aData.quantity + "' />";
                    var weightConcept = aData.weightConcept;
                    if (weightConcept == "过磅") {
                        weightConcept = "磅计";
                    }
                    $('td:eq(0)', nRow).html(checkHtml);
                    $('td:eq(8)', nRow).html(weightConcept);
                },
                "fnDrawCallback": function () {
                    $("#allCheck").removeAttr("checked");   // 取消全选
                },
                columns: [
                    {data: 'id'},
                    {data: 'sellerName'},
                    {data: 'nsortName'},
                    {data: 'material'},
                    {data: 'spec'},
                    {data: 'factory'},
                    {data: 'warehouse'},
                    {data: 'weight'},
                    {data: 'weightConcept'},
                    {data: 'costPrice'}
                ]
            });
        }
        else {
            table.fnDraw();
        }
    });

    // 全选/全不选
    $("#allCheck").bind("click", function () {
        var checked = $(this).is(':checked');
        // 取消全选
        if (!checked) {
            $("input[name='resourcecheck']").removeAttr("checked");
            $(this).removeAttr("checked");
        }
        else {
            $("input[name='resourcecheck']").prop('checked', true);
            $(this).prop('checked', true);
        }
    });

    // 单选
    $("input[name='resourcecheck']").bind("click", function () {
        var checked = $(this).is(':checked');
        if (!checked) {
            $(this).removeAttr("checked");
            $("#allCheck").removeAttr("checked");   // 取消全选
        }
        else {
            $(this).prop('checked', true);
        }

        // 如果全部选中，那么全选checkbox选中
        var checkCount = $("input[name='resourcecheck']").length;
        var checkedCount = $("input[name='resourcecheck']:checked").length;
        if (checkCount == checkedCount) {
            $("#allCheck").prop('checked', true);
        }
    });
});