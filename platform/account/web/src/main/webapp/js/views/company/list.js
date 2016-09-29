var dt;
//客户性质用于显示   加工、运输、仓库亮色暂时不做
var _accountTagArray = [{code:1,name:'买',title:"买家客户",class:'buy'}
                        ,{code:6,name:'临',title:"卖家客户",class:'lin'}
                        ,{code:10,name:'代',title:"卖家代运营客户",class:'dai'}
                      /*  ,{code:16,name:'仓库',title:"仓库客户"}
                        ,{code:32,name:'运输',title:"运输客户"}
                        ,{code:64,name:'加工',title:"加工客户"}*/
                        ];
jQuery(function($) {

    initTable($);

    initMultiSelect($);

    //we put a container before our table and append TableTools element to it
    $('.dataTables_length').after($('.toolsbar'));

    //搜索
    $("#searchBtn").on(ace.click_event, function() {
       searchData();
    });

    //添加用户
    $("#addUser").on(ace.click_event, function() {
        location.href = Context.PATH+"/account/create.html";
    });

    //列表多选框、全选反选
    $(".dataTables_wrapper").dataTableSelected();

    //批量锁定用户
    $("#lockUser").click(function() {
        var ids = getCheckedValues();
        if(ids.length == 0){
            cbms.alert("请选择需要锁定的客户");
            return;
        }
        if(validateStatus("1")){
           lockOrUnLockCompany({ids:ids,status:0},"确定要锁定选定的客户吗？","锁定");
        }
    });

    //批量解锁用户
    $("#unlockUser").click(function() {
        var ids = getCheckedValues();
        if (ids.length == 0) {
            cbms.alert("请选择需要解锁的客户");
            return;
        }
        if(validateStatus("0")) {
           lockOrUnLockCompany({ids: ids, status: 1}, "确定要解锁选定的客户吗？", "解锁");
        }
    });

    //锁定/解锁用户
    $(document).on("click", ".table-lock", function () {
        var id = $(this).attr("id");
        var status = $(this).attr("status");
        var text = (status == '1' ? '锁定' : '解锁');
        var updateStatus = (status == '1' ? '0' : '1');
        lockOrUnLockCompany({ids:id,status:updateStatus}, "确定要"+text+"该客户？", text);
    });

    //显示隐藏列
    $('.toggle-vis').on('change', function (e) {
        e.preventDefault();
        var column = dt.column($(this).attr('data-column'));
        column.visible(!column.visible());
    });

    $(".po-rel").hover(function(){
        $(".po-abso").toggle();
    });

});



function initTable($){
    //initiate dataTables plugin
    var url= Context.PATH +"/company/loadCompanyList.html";
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
                    d.accountTag = $("#accountTag").val();
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
                        d.order = "desc";
                    }
                }
            },
            serverSide: true, //服务模式
            processing: true,//显示数据加载进度
            "columnDefs" : [{
                "targets" : 0, //第一列隐藏
                "data" : null,
                "defaultContent" : '<label class="pos-rel"><input name="check" type="checkbox" class="ace"><span class="lbl"></span></label>'
            },{
                sDefaultContent: '-', //解决请求参数未知的异常
                aTargets: ['_all']
            }],
            "aoColumns" : [{
                "sWidth": "20px",
                "sTitle" : '<label class="pos-rel"><input id="allCheck" type="checkbox" class="ace"><span class="lbl"></span></label>',
                "bSortable" : false
            }, {
                "data":'name',
                "sTitle" : "公司名称",
                "render":renderCompanyName
                ,"name":"t1.name"//用于排序
            }, {
                "data":'accountTag',
                "sTitle" : "客户性质",
                "render":renderAccountTag
                ,"name":"t1.account_tag"
            }, {
                "data":'purchaseAgreementStatus',
                "sTitle" : "年度采购协议",
                "render":renderPurchaseStatus
                ,"name":"t2.annual_purchase_agreement_status"
            }, {
                "data":'consignAgreementStatus',
                "sTitle" : "卖家代运营协议",
                "render":renderConsignStatus
                ,"name":"t2.seller_consign_agreement_status"
            }, {
                "data":'cardInfoStatus',
                "sTitle" : "证件资料",
                "render":renderCardInfoStatus
                ,"name":"t2.card_info_status"
            }, {
                "data":'invoiceDataStatus',
                "sTitle" : "开票资料",
                "render":renderInvoiceDataStatus
                ,"name":"t2.invoice_data_status"
            }, {
                "data":'bankDataStatus',
                "sTitle" : "打款资料",
                "render":renderBankDataStatus
                ,"name":"t2.bank_data_status"
            }, {
                "data":'orgName',
                "sTitle" : "服务中心"
                ,"name":"t1.org_name"
            }, {
                "data":'regTime',
                "sTitle" : "注册时间"
                ,"name":"t2.reg_time"
            }, {
                "data":'status',
                "sTitle" : "状态",
                "render":renderStatus
                ,"name":"t1.status"
            }, {
                "sTitle" : "操作",
                "render":renderOpt,
                "bSortable":false
            }
            ],
            "aaSorting" : [],
            "createdRow": function ( row, data, index ) {
                //set value for checkbox
                $('td:eq(0)',row).find("input[name='check']").val(data.id);
                $('td:eq(0)',row).find("input[name='check']").attr("status",data.status);
            },
            "fnRowCallback": function (nRow, aData, iDataIndex) {
            }
            ,"fnDrawCallback":function(setting){

            }
        });
}
	
function lockOrUnLockCompany(data,info,text){
    cbms.confirm(info, null, function () {
        $.ajax({
            type: "POST",
            url: Context.PATH + "/company/lockAndUnlockCompany.html",
            data: data,
            dataType: "json",
            success: function (response, textStatus, xhr) {
                if (response.success) {
                    cbms.gritter(text + "成功！",true,function () {
                        //查询数据
                        searchData();
                    });
                } else {
                    cbms.gritter(text + "失败！",false);
                }
            },
            error: function (xhr, textStatus, errorThrown) {
            }
        });
    })
}

function searchData() {
    dt.ajax.reload();
}

/**
 * 获得选择的checkbox的values
 */
function getCheckedValues(){
    var checkedBoxes = $('#dynamic-table').find("tbody").find("input[name='check']:checked");
    var ids = [];
    checkedBoxes.each(function(){
        ids.push($(this).val());
    });
    return ids.join(",");
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

//点击跳入企业基本信息页 企业账户信息 TODO
function renderCompanyName(data, type, full, meta){
    //Context.PATH
    var href = getCardInfoUrl(full.id);
    return '<span><a href="'+ href +'" class="blue">' + data +'</a></span>';
}

function renderOpt(data, type, full, meta){
    var status = full.status;
    var text = (status == '1' ? '锁定' : '解锁');
    return  '<a class="table-lock blue" href="javascript:;" id="' + full.id + '" status="' + status + '">' + text + '</a>';
}

function renderStatus(data, type, full, meta){
    return (data == '1' ? '正常' : '<em class="red bolder">锁定</em>');
}

/**
 * 显示所有的客户性质 如果是某个性质就点亮（）
 * @param data
 * @param type
 * @param full
 * @param meta
 * @returns {string}
 */
function renderAccountTag(data, type, full, meta){
    var v="";
    var lights = getAccountTagsByCode(data);
    $.each(_accountTagArray,function(index,item){
        var className = $.inArray(item.code, lights) >= 0 ? item.class : '';
        v += "<span title='"+item.title+"' class='taci "+ className +"'>"+item.name+"</span>";
    });
    return v;
}

/**
 * 通过code获得所有包含的客户性质数组
 * @param code
 * @returns {Array}
 */
function getAccountTagsByCode(code){
    var array = [];
    $.each(_accountTagArray,function(index,item){
        if((item.code & code) === item.code){
            array.push(item.code);
        }
    });
    return array;
}

/**
 * 年度采购协议渲染
 * @param data
 * @param type
 * @param full
 * @param meta
 */
function renderPurchaseStatus(data, type, full, meta){
    var defaultUrl = "javascrit:;";
    if(data == 'ToPrint'){
        return createAele(getCardInfoUrl(full.id),"blue","待打印","");
    }
    if(data == 'Requested'){
        return createAele(defaultUrl,"blue","待审核","");
    }
    if(data == 'FirstApproved'){
        return createAele(defaultUrl,"blue","一审通过","") ;
    }
    if(data == 'SecondApproved'){
        return createAele(defaultUrl,"blue","二审通过","");
    }
    if(data == 'Uploaded'){
        return createAele(defaultUrl,"blue","已上传待审核","");
    }
    if(data == 'Approved'){
        return createAele(defaultUrl,"green","审核通过","");
    }
    if(data == 'Declined' || data == 'FirstDeclined' || data == 'SecondDeclined'){
        return createAele(getCardInfoUrl(full.id),"red","审核未通过",full.purchaseAgreementReason || '');
    }
    return '-';
}

/**
 * 卖家代运营协议渲染
 * @param data
 * @param type
 * @param full
 * @param meta
 */
function renderConsignStatus(data, type, full, meta){
    var defaultUrl = "javascrit:;";
    if(data == 'ToPrint'){
        return createAele(getCardInfoUrl(full.id),"blue","待打印","");
    }
    if(data == 'Requested'){
        return createAele(defaultUrl,"blue","待审核","");
    }
    if(data == 'FirstApproved'){
        return createAele(defaultUrl,"blue","一审通过","") ;
    }
    if(data == 'SecondApproved'){
        return createAele(defaultUrl,"blue","二审通过","");
    }
    if(data == 'Uploaded'){
        return createAele(defaultUrl,"blue","已上传待审核","");
    }
    if(data == 'Approved'){
        return createAele(defaultUrl,"green","审核通过","");
    }
    if(data == 'Declined' || data == 'FirstDeclined' || data == 'SecondDeclined'){
        return createAele(getCardInfoUrl(full.id),"red","审核未通过",full.consignAgreementReason || '');
    }
    if(data == 'Terminate'){
        return createAele(defaultUrl,"red","已终止",'');
    }

    return '-';
}

/**
 * 开票资料状态渲染
 * @param data
 * @param type
 * @param full
 * @param meta
 */
function renderInvoiceDataStatus(data, type, full, meta){
    var defaultUrl = "javascrit:;";
    if(data == '2'){
        return createAele(defaultUrl,"blue","待审核","");
    }
    if(data == '3'){
        return createAele(getCardInfoUrl(full.id),"blue","未上传","");
    }
    if(data == '1'){
        return createAele(defaultUrl,"green","通过","");
    }
    if(data == '4'){
        return createAele(getCardInfoUrl(full.id),"red","审核未通过","点击查看");
    }
    return '-';
}

/**
 * 打款资料状态渲染
 * @param data
 * @param type
 * @param full
 * @param meta
 */
function renderBankDataStatus(data, type, full, meta){
    var defaultUrl = "javascrit:;";
    if(data == 'Requested'){
        return createAele(defaultUrl,"blue","待审核","");
    }
    if(data == 'Insufficient'){
        return createAele(getCardInfoUrl(full.id),"blue","未上传","");
    }
    if(data == 'Approved'){
        return createAele(defaultUrl,"green","通过","");
    }
    if(data == 'Declined'){
        return createAele(getCardInfoUrl(full.id),"red","审核未通过","点击查看");
    }
    return '-';
}

/**
 * 证件资料状态渲染
 * @param data
 * @param type
 * @param full
 * @param meta
 */
function renderCardInfoStatus(data, type, full, meta){
    var defaultUrl = "javascrit:;";
    if(data == 'Requested'){
        return createAele(defaultUrl,"blue","待审核","");
    }
    if(data == 'Insufficient'){
        return createAele(getCardInfoUrl(full.id),"blue","未上传","");
    }
    if(data == 'Approved'){
        return createAele(defaultUrl,"green","通过","");
    }
    if(data == 'Declined'){
        return createAele(getCardInfoUrl(full.id),"red","审核未通过","点击查看");
    }
    return '-';
}

/**
 * 创建协议和状态的公用超链接
 * @param url
 * @param className
 * @param text
 * @param title
 * @returns {string}
 */
function createAele(url, className,text,title) {
    return "<a href='" + url + "' class='"+className+"' title='"+title+"'>" + text + "</a>";
}

/**
 * 账户信息页面URL
 * @param id 客户id
 * @returns {string}
 */
function getCardInfoUrl(id){
    return Context.PATH+"/accountinfo/"+id+"/accountinfo.html";
}

/**
 *  验证与勾选的列表中是否包含参数status一样的值
 * @param status
 * @return true 包含 false 不包含
 */
function validateStatus(status){
    var checkedBoxes = $('#dynamic-table').find("tbody").find("input[name='check']:checked");
    var flag = false;
    checkedBoxes.each(function(){
        if($(this).attr("status") == status){
            flag = true;
            return;
        }
    });
    return flag;
}