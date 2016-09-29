/**
 * create by dengxiyan
 * 交易单管理-交易单查询：部分tab页查询公用js
 */

// Remove the formatting to get integer data for summation
function numberVal(i) {
    return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
}

/**
 * 统计当前页中某一列的总和，默认为0
 * @param api
 * @param index 列索引
 * @returns {*}
 */
function pageTotalColumn(api, index) {
    return api.column(index, {page: 'current'}).data().reduce(function (a, b) {
        return numberVal(a).add(numberVal(b));
    }, 0);
}

/**
 * 返回订单详情的URL
 * @param orderid
 */
function getDetailURL(orderid) {
    return Context.PATH + "/order/query/detail.html?orderid=" + orderid + "&menu=" + $("#pageTab").val();
}

/**
 * 返回打印合同URL
 * @param orderid
 */
function getPrintContractURL(orderid) {
    return Context.PATH + "/order/contract.html?id=" + orderid;
}

/**
 * 渲染交易单号
 * @param data
 * @param type
 * @param full
 * @param meta
 * @returns {string}
 */
function renderCode(data, type, full, meta) {
    return createAele(getDetailURL(full.id), data);
}

function renderAmount(data, type, full, meta) {
    if (data) {
        return "<span class='bolder'>" + formatMoney(data, 2) + "</span>";
    }
    return "";
}

function renderPickupAmount(data, type, full, meta) {
    if (data) {
        return formatMoney(data, 2);
    }
    if (full.status === '6') { //待二次结算 为空显示--
        return "--"
    }
    return "0.00";
}

function renderWeight(data, type, full, meta) {
    var temp = data;
    if (temp) {
        return cbms.convertFloat(parseFloat((temp + "").replace(/[^\d\.-]/g, ""))) + "";
    }
    if (full.status === '6') { //待二次结算 为空显示--
        return "--"
    }
    return "0.0000";
}

function renderStatus(flag) {
    var statusShow;
    //Tab页显示列表中的状态
    switch (flag) {
        case '0' :
            statusShow = '交易关闭';
            break;
        case '1' :
            statusShow = '待审核';
            break;
        case '2' :
            statusShow = '待关联合同';
            break;
        case '3' :
            statusShow = '已关联合同';
            break;
        case '4' :
            statusShow = '待出库';
            break;
        case '5' :
            statusShow = '放货单已全打印';
            break;
        case '6' :
            statusShow = '待二次结算';
            break;
        case '7' :
            statusShow = '已申请付款';
            break;
        case '8' :
            statusShow = '已通过付款申请';
            break;
        case '9' :
            statusShow = '已申请关闭';
            break;
        case '10' :
            statusShow = '交易完成';
            break;
        case '11' :
            statusShow = '已打印付款申请';
            break;
        default:
            statusShow = "";
    }
    ;
    return statusShow;
}

function renderChangeStatus(f) {
    var statusShow;
    if (f == 'CHANGED') {
        return "变更待审核";
    } else if (f == 'DISAPPROVE') {
        return "变更审核失败";
    } else if (f == 'APPROVE') {
        return "变更成功";
    } else {
        return "";
    }
}

/**
 * 创建超链接
 * @param url href
 * @param text 显示文本
 */
function createAele(url, text) {
    return "<a href='" + url + "'>" + text + "</a>";
}


jQuery(function ($) {
    //settings default
    $.extend($.fn.dataTable.defaults, {
        "pageLength": 15, //每页记录数
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "lengthChange": false, //不显示pageSize的下拉框
        "oLanguage": {
        	sUrl: Context.PATH + "/js/DT_zh.txt",
        	"oAria":{
    		          /*
    		           * 默认值为activate to sort column ascending
    		           * 当一列被按照升序排序的时候添加到表头的ARIA标签，注意列头是这个字符串的前缀（？）
    		           */
    		              "sSortAscending": " - click/return to sort ascending",
    		          /*
    		           * 默认值为activate to sort column ascending
    		           * 当一列被按照升序降序的时候添加到表头的ARIA标签，注意列头是这个字符串的前缀（？）
    		           */
    		              "sSortDescending": " - click/return to sort descending"
    		         },
        } //自定义语言包
    });
});

/**
 * 统计所有页中某一列的总和，默认为0
 * @param api
 * @param index 列索引
 * @returns {*}
 */
function totalColumn(api, index) {
    return api.column(index).data().reduce(function (a, b) {
        return numberVal(a).add(numberVal(b));
    }, 0);
}

function renderSeller(data, type, full, meta) {
    //临采
    if (full.consignType == "temp") {
        return "<span><em class='label-orange white'>临</em>" + data + "</span>";
    }
    return data;
}