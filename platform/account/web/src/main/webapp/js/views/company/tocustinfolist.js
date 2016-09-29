var dt;
jQuery(function($) {

    initMultiSelect($);

    initTable($);

    //we put a container before our table and append TableTools element to it
    $('.dataTables_length').after($('.toolsbar'));

    //搜索
    $("#searchBtn").on(ace.click_event, function() {
        searchData();
    });

    $(".po-rel").hover(function(){
        $(".po-abso").toggle();
    });


    //TODO 年度采购协议撤销通过 回到待打印
    $(document).on("click",".btn-purchase-cancle",function(){
        //cust_account id 客户id
        var accountid = $(this).attr("accountid");
        cbms.confirm("撤消通过后，状态将变更为初始待打印状态，确定该操作吗？",null,function(){
        	var data = {"id":accountid,"purchaseAgreementStatus":'ToPrint'};
        	doCancelAudit(data);
        });
    });

    //TODO 卖家代运营协议撤销通过 回到待打印
    $(document).on("click",".btn-consign-cancle",function(){
        //cust_account id 客户id
        var accountid = $(this).attr("accountid");
        cbms.confirm("撤消通过后，状态将变更为初始待打印状态，确定该操作吗？",null,function(){
        	var data = {"id":accountid,"consignAgreementStatus":'ToPrint'};
        	doCancelAudit(data);
        });
    });

    //TODO 证件资料撤销通过 回到待审核状态
    $(document).on("click",".btn-cardinfo-cancle",function(){
        //cust_account id 客户id
        var accountid = $(this).attr("accountid");
        cbms.confirm("撤消通过后，状态将变更为资料待审核状态，确定该操作吗？",null,function(){
        	var data = {"id":accountid,"cardInfoStatus":'Requested'};
        	doCancelAudit(data);
        });
    });
    //TODO 开票资料撤销通过 回到待审核状态
    $(document).on("click",".btn-invoice-cancle",function(){
        //cust_account id 客户id
        var accountid = $(this).attr("accountid");
        cbms.confirm("撤消通过后，状态将变更为资料待审核状态，确定该操作吗？",null,function(){
        	var data = {"id":accountid,"invoiceDataStatus":'2'};
        	doCancelAudit(data);
        });
    });

});

function doCancelAudit(data){
	$.ajax({
		url: Context.PATH + "/flow/cardinfo/docancelaudit.html",
		type:"POST",
		data:data,
		success: function (result) {
        	if (result.success) {
        		dt.ajax.reload();
        	}
		}
	});
}

function initTable($){
    //initiate dataTables plugin
    var url= Context.PATH +"/company/loadcustinfolist.html";
    dt = $('#dynamic-table')
        .DataTable(
        {
            "dom" : 'lrTt<"bottom"p>i<"clear">',
            "aLengthMenu": [50, 100],
            "bScrollCollapse" : true,
            searching : false,
            "bSort":true,
            ajax: {
                url: url,
                type: "POST",
                data: function (d) {
                    d.companyName = $.trim($("#accountName").val());
                    d.orgId = $("#orgId").val();
                    d.purchaseStatusList = getStatusValuesById("purchaseStatus");
                    d.consignStatusList = getStatusValuesById("consignStatus");
                    d.invoiceStatusList = getStatusValuesById("invoiceStatus");
                    d.cardStatusList = getStatusValuesById("cardStatus");
                    d.payStatusList = getStatusValuesById("payStatus");

                    //d.order 单击排序时会发送的排序相关的请求参数
                    if(d.order.length > 0){
                        //获取当前排序的列索引
                        var columnIndex = d.order[0]["column"] ;
                        //获取排序的规则 如:desc
                        d.order = d.order[0]["dir"] ;
                        d.orderBy = d.columns[columnIndex].name;
                    }else{
                        //默认排序使用注册时间
                        d.orderBy = "t2.reg_time";
                        d.order = "asc";
                    }
                }
            },
            serverSide: true, //服务模式
            processing: true,//显示数据加载进度
            "columnDefs" : [{
                sDefaultContent: '-', //解决请求参数未知的异常
                aTargets: ['_all']
            }],
            "aoColumns" : [{
                "data":'name',
                "sTitle" : "客户全称"
                ,"className":"text-left"
                ,"name":"t1.name"//用于排序
            }, {
                "data":'purchaseAgreementStatus',
                "sTitle" : "年度采购协议",
                "render":renderPurchaseStatus
                ,"name":"t2.annual_purchase_agreement_status"
                ,"className":"text-left"
            }, {
                "data":'consignAgreementStatus',
                "sTitle" : "卖家代运营协议",
                "render":renderConsignStatus
                ,"name":"t2.seller_consign_agreement_status"
                ,"className":"text-left"
            }, {
                "data":'cardInfoStatus',
                "sTitle" : "证件资料",
                "render":renderCardInfoStatus
                ,"name":"t2.card_info_status"
                ,"className":"text-left"
            }, {
                "data":'invoiceDataStatus',
                "sTitle" : "开票资料",
                "render":renderInvoiceDataStatus
                ,"name":"t2.invoice_data_status"
                ,"className":"text-left"
            }, {
                "data":'bankDataStatus',
                "sTitle" : "打款资料",
                "render":renderBankDataStatus
                ,"name":"t2.bank_data_status"
                ,"className":"text-left"
            }
            ],
            "aaSorting" : []
        });
}


function searchData() {
    dt.ajax.reload();
}


function initMultiSelect($){
    //确定后直接搜索数据
    $("#purchaseStatus").selectCheckBox(Context.PATH+"/company/getAnnualPurchaseAgreementStatus.html", {},searchData);
    $("#payStatus").selectCheckBox(Context.PATH+"/company/getAccountBankDataStatus.html", {},searchData);
    $("#invoiceStatus").selectCheckBox(Context.PATH+"/company/getInvoiceDataStatus.html", {},searchData);
    $("#consignStatus").selectCheckBox(Context.PATH+"/company/getSellerConsignAgreementStatus.html", {},searchData);
    $("#cardStatus").selectCheckBox(Context.PATH+"/company/getCardInfoStatus.html", {},searchData);
}

function getStatusValuesById(id){
    var checkedBoxes = $("#"+id).find(".mulsel-content-list ul").find("input[type='checkbox']:checked");
    var array = [];
    checkedBoxes.each(function (){
        array.push($(this).val());
    });
    return array.join(",");
}

/**
 * 创建span
 * @param className
 * @param text
 * @returns {string}
 */
function createSpan(className,text) {
    return "<span class='"+className+"' >" + text + "</a>";
}

/**
 * 年度采购协议渲染
 * @param data
 * @param type
 * @param full
 * @param meta
 */
function renderPurchaseStatus(data, type, full, meta){
    if(data == 'ToPrint'){
        return createSpan("blue","待打印");
    }
    if(data == 'Requested'){
        return createSpan("blue","待审核");
    }
    if(data == 'FirstApproved'){
        return createSpan("blue","一审通过") ;
    }
    if(data == 'SecondApproved'){
        return createSpan("blue","二审通过");
    }
    if(data == 'Uploaded'){
        return createSpan("blue","已上传待审核");
    }
    if(data == 'Approved'){
    	var hasPermission = $("#hasPermission").val();
    	var content = '<div><em class="green">审核通过</em>';
    	if(hasPermission == 1){
    		content+='<button type="button" class="btn-purchase-cancle btn btn-xs btn-primary" accountid="'+full.id+'">撤销通过</button>';
    	}
    	content+='</div>';
        return content;
    }
    if(data == 'Declined' || data == 'FirstDeclined' || data == 'SecondDeclined'){
        return createSpan("red","审核未通过");
    }
    return '--';
}

/**
 * 卖家代运营协议渲染
 * @param data
 * @param type
 * @param full
 * @param meta
 */
function renderConsignStatus(data, type, full, meta){
    if(data == 'ToPrint'){
        return createSpan("blue","待打印");
    }
    if(data == 'Requested'){
        return createSpan("blue","待审核");
    }
    if(data == 'FirstApproved'){
        return createSpan("blue","一审通过") ;
    }
    if(data == 'SecondApproved'){
        return createSpan("blue","二审通过");
    }
    if(data == 'Uploaded'){
        return createSpan("blue","已上传待审核");
    }
    if(data == 'Approved'){
    	var hasPermission = $("#hasPermission").val();
    	var content = '<div><em class="green">审核通过</em>';
    	if(hasPermission == 1){
    		content+='<button type="button" class="btn-consign-cancle btn btn-xs btn-primary" accountid="'+full.id+'">撤销通过</button>';
    	}
    	content+='</div>';
        return content;
    }
    if(data == 'Declined' || data == 'FirstDeclined' || data == 'SecondDeclined'){
        return createSpan("red","审核未通过");
    }
    if(data == 'Terminate'){
        return createSpan("red","已终止");
    }

    return '--';
}

/**
 * 开票资料状态渲染
 * @param data
 * @param type
 * @param full
 * @param meta
 */
function renderInvoiceDataStatus(data, type, full, meta){
    if(data == '2'){
        return createSpan("blue","待审核");
    }
    if(data == '3'){
        return createSpan("blue","未上传");
    }
    if(data == '1'){
    	var hasPermission = $("#hasPermission").val();
    	var content = '<div><em class="green">审核通过</em>';
    	if(hasPermission == 1){
    		content+='<button type="button" class="btn-invoice-cancle btn btn-xs btn-primary" accountid="'+full.id+'">撤销通过</button>';
    	}
    	content+='</div>';
        return content;
    }
    if(data == '4'){
        return createSpan("red","审核未通过");
    }
    return '--';
}

/**
 * 打款资料状态渲染
 * @param data
 * @param type
 * @param full
 * @param meta
 */
function renderBankDataStatus(data, type, full, meta){
    if(data == 'Requested'){
        return createSpan("blue","待审核");
    }
    if(data == 'Insufficient'){
        return createSpan("blue","未上传");
    }
    if(data == 'Approved'){
        return createSpan("green","通过"); 
    }
    if(data == 'Declined'){
        return createSpan("red","审核未通过");
    }
    return '--';
}

/**
 * 证件资料状态渲染
 * @param data
 * @param type
 * @param full
 * @param meta
 */
function renderCardInfoStatus(data, type, full, meta){
    if(data == 'Requested'){
        return createSpan("blue","待审核");
    }
    if(data == 'Insufficient'){
        return createSpan("blue","未上传");
    }
    if(data == 'Approved'){
    	var hasPermission = $("#hasPermission").val();
    	var content = '<div><em class="green">审核通过</em>';
    	if(hasPermission == 1){
    		content+='<button type="button" class="btn-cardinfo-cancle btn btn-xs btn-primary" accountid="'+full.id+'">撤销通过</button>';
    	}
    	content+='</div>';
        return content;
    }
    if(data == 'Declined'){
        return createSpan("red","审核未通过");
    }
    return '--';
}


