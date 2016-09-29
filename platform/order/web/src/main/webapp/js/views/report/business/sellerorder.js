/**
 * Created by dengxiyan on 2015/8/19.
 * 代运营卖家交易报表
 */

var dt;
jQuery(function ($) {
    initTable();
    //搜索事件
    $("#queryBtn").click(function () {
        dt.ajax.reload();
    });
});

function initTable() {
    dt = $("#dynamic-table").DataTable({
        "pageLength": 15, //每页记录数
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "lengthChange": false, //不显示pageSize的下拉框
        "oLanguage": {sUrl: Context.PATH + "/js/DT_zh.txt"}, //自定义语言包
        "ajax": {
            "url": Context.PATH + "/report/business/loadsellertradedata.html"
            , "type": "POST"
            , data: function (d) {
                d.sellerName = $("#sellerName").val();
                d.orgId = $("#sorganizationHidden").val();
                d.strStartTime = $("#startTime").val();
                d.strEndTime = $("#endTime").val();
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'sellerName'},
            {data: 'consignType'},
            {data: 'monthWeight', "sClass": "text-right"},
            {data: 'monthAllSellerWeight', "sClass": "text-right"},
            {data: 'monthOrderCount', "sClass": "text-right"},
            {data: 'monthAmount', "sClass": "text-right"},
            {data: 'sumWeight', "sClass": "text-right"}
        ]
        , columnDefs: [
            {
                "targets": 1,
                "data": "consignType",
                "render": function (data, type, full, meta) {
                    return renderType(data);
                }
            }
            , {
                "targets": 2,
                "data": "monthWeight",
                "render": function (data, type, full, meta) {
                    return renderWeight(data);
                }
            }, {
                "targets": 3,
                "data": "monthAllSellerWeight",
                "render": function (data, type, full, meta) {
                    return renderPercent(full.monthWeight, data);
                }
            }
            , {
                "targets": 5,
                "data": "monthAmount",
                "render": function (data, type, full, meta) {
                    if (data) {
                        return formatMoney(data, 2);
                    }
                    return "";
                }
            }, {
                "targets": 6, //第几列 从0开始
                "data": "sumWeight",
                "render": function (data, type, full, meta) {
                    if (data) {
                        return formatMoney(data, 4);
                    }
                    return "";
                }
            }
        ]
    });
}

