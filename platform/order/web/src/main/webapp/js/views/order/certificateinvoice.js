/**
 * 补齐买，卖家凭证开票订单
 * @author peanut
 * @date 2016/04/13
 */


jQuery(function ($) {
    initEvent();
    initTable();
});

//列表
function initTable() {
    //_globalAttr 全局属性对象已在vm定义
    _globalAttr.table= $("#dynamic-table").DataTable({
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "lengthChange": true, //显示pageSize的下拉框 50 100 150
        "pageLength": 50, //每页记录数
        "ajax": {
            "url": Context.PATH + "/order/certificate/searchCertificateInvoice.html",
            "type": "POST",
            data : function (d) {
                d.status=$("#status").val();
                d.credentialCode=$("#credentialCode").val();
                d.code=$("#code").val();
                d.sellerName=$("#sellerName").val();
                d.buyerName=$("#buyerName").val();
                d.ownerName=$("#ownerName").val();
                d.startTime=$("#startTime").val();
                d.endTime=$("#endTime").val();
                d.credentialStartTime=$("#credentialStartTime").val();
                d.credentialEndTime=$("#credentialEndTime").val();
                d.type=_globalAttr.accountType;
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns:[
//                   {defaultContent: ''},
            {data: 'credentialCode',"mRender":function(e,t,f){return !e||e==''?"-":e}},   	//凭证号
            {data: 'printDate',"mRender":function(e,t,f){return !e||e==''?"-":e}},   			//创建凭证时间
            {data: 'code'},   				//交易单号
            {data: 'openOrderDate'},  		//开单时间
            {data: 'buyerName'},   			//买家全称
            {data: 'ownerName'},   			//交易员
            {data: 'sellerName'},  			//卖家全称
            {data: 'quantity'},  			//件数
            {data: 'totalWeight'},			//总重量
            {data: 'actualPickTotalWeight'},	//实提总重量
            {data: 'totalAmount'},			//总金额
            {data: 'actualPickTotalAmount'},	//实提总重量
            {data: 'status'},			//打印状态
            {data: 'durationDay'},			//距离开单时间已有
            {data: 'expiryDay'},			//回收截止日期
        ]
        , columnDefs: [
            {
                sDefaultContent: '', //解决请求参数未知的异常
                aTargets: ['_all']

            }
        ]
    });
}

function initEvent(){

    $("#queryBtn").on("click",function(){
        _globalAttr.table.ajax.reload();
    });
}

