/**
 * create by dengxiyan
 * 交易单管理-交易单查询：部分tab页查询公用js
 */


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

