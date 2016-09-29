/**
 * Created by Administrator on 2016/7/14.
 */
var marketResourceDt = null;
$(document).ready(function () {
	/**
	 * 超市资源移除资源类型查询条件 功能 #11877  added by yjx  2.16.8.22
	 */
	$("#lab_sourceType").addClass("none");
	
    if (!utils.isEmpty(tabIndex)) {
        var index = parseInt(tabIndex);
        $("#myTab4 li").removeClass("active");
        $("#myTab4").find("li:eq(" + index + ")").addClass("active").find("a").attr("href", "javascript:;;");
    }

    hideAll();
    $("#lab_serviceArea").hide();
    $("#lastupdateBy_panel").hide();
    $("#update_type_panel").hide()
    $("#seller_panel").hide();

    marketResourceDt = jQuery("#dynamic-table").DataTable({
        "processing": false,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "paging": true,
        //"bInfo" : false,
        "autoWidth": false,
        "iDisplayLength": 50,
        "aLengthMenu": [10, 30, 50, 100],//定义每页显示数据数量
        "ajax": {
            "url": Context.PATH + "/resource/market/search.html",
            "type": "POST",
            data: function (d) {
                var upStart=$("#upStart").val();
                //写死查询起始时间，取项目开始时间:2015年11月1日
                if(utils.isEmpty(upStart)){
                    upStart='2015-11-1 00:00';
                }
                var pricemin = '', pricemax = '';
                if (!isNaN($("#pricemin").val())) {
                    pricemin = $("#pricemin").val();
                }
                if (!isNaN($("#pricemax").val())) {
                    pricemax = $("#pricemax").val();
                }
                return $.extend({}, d, {
                    warehouseNames: $.trim($("#warehouseName").val()),
                    categoryName: $.trim($("#cagetoryName").val()),
                    materialNames: $.trim($("#materialName").val()),
                    spec: $.trim($("#spec").val()),
                    factoryNames: $.trim($("#factoryName").val()),
                    specificCityName: $.trim($("#areaName").val()),
                    updateType: $("#updateType").val(),
                    startDate: upStart,
                    endDate: $("#upEnd").val(),
                    priceMin:pricemin,
                    priceMax:pricemax
                });
            }
        },
        columns: [
            {data: 'categoryName'},
            {data: 'materialName'},
            {data: 'spec'},
            {data: 'factoryName'},
            {data: 'cityName'},
            {data: 'warehouseName'},
            {data: 'weightConcept'},
            {data: 'price'},
            {
                data: 'lastUpdated',
                render: function (data, type, full, meta) {
                    return dateFormat(new Date(data), "yyyy-MM-dd hh:mm:ss");
                }
            }
        ]
    });

    //搜索
    $("#searchList").on(ace.click_event, function () {
        cbms.loading();
        marketResourceDt.ajax.reload(function () {
            cbms.closeLoading();
        });
    });

    //清空按钮
    $("#cleanSearch").on("click", function () {
        resetForm($("form.form-inline"));
    });

});


//隐藏所有按钮
function hideAll() {
    //    添加
    $("#addResource").addClass("none");
    //  批量导入
    $("#excelBatch").addClass("none");
    //批量调价
    $("#bulkPricing").addClass("none");
    //批量改库存
    $("#changeCont").addClass("none");
    //选中挂牌
    $("#selPutUp").addClass("none");
    //一键挂牌
    $("#putUp").addClass("none");
    //选中撤牌
    $("#selPutDown").addClass("none");
    //一键撤牌
    $("#putDown").addClass("none");
    //选中删除
    $("#selDel").addClass("none");
    //刷新异常
    $("#refresh").addClass("none");
    //资源导出
    $("#exportRes").addClass("none");
    //资源更新统计
    $("#statisRes").addClass("none");
    //全选
    $("#allSelect").closest("label").addClass("none");
}


function dateFormat(date, fmt) {
    var o = {
        "M+": date.getMonth() + 1,                 //月份
        "d+": date.getDate(),                    //日
        "h+": date.getHours(),                   //小时
        "m+": date.getMinutes(),                 //分
        "s+": date.getSeconds(),                 //秒
        "q+": Math.floor((date.getMonth() + 3) / 3), //季度
        "S": date.getMilliseconds()             //毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}