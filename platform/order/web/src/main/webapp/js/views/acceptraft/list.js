var dt;
jQuery(function ($) {
    initTable();
    initOrg();
    $(document).on("click", "#search", function () {
        dt.ajax.reload();
    });

    $(document).on("click", "#new", function () {
        location.href = Context.PATH + "/acceptdraft/create.html";
    });

    $(document).on("click", ".edit", function () {
        location.href = Context.PATH + "/acceptdraft/create.html?id=" + $(this).attr("rel");
    });

    $(document).on("click", ".auditrecharge", function () {
        location.href = Context.PATH + "/acceptdraft/" + $(this).attr("rel") + "/auditrecharge.html";
    });

    $(document).on("click", ".check", function () {
        location.href = Context.PATH + "/acceptdraft/" + $(this).attr("rel") + "/check.html";
    });

    $(document).on("click", ".viewcharged", function () {
        location.href = Context.PATH + "/acceptdraft/" + $(this).attr("rel") + "/viewcharged.html";
    });

    $(document).on("click", ".cancelcharge", function () {
        location.href = Context.PATH + "/acceptdraft/" + $(this).attr("rel") + "/cancelcharge.html";
    });
});


function initTable() {
    var url = Context.PATH + "/acceptdraft/getData.html";
    dt = jQuery("#forShow").DataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "ajax": {
            "url": url
            , "type": "POST"
            , data: function (d) {
                return $.extend({}, d, {
                    status: $("#status").children('option:selected').val(),
                    dateStartStr: $("#stimefrom").val(),
                    dateEndStr: $("#stimeto").val(),
                    accountName: $("#accountName").val(),
                    orgId: $("#org").children("option:selected").val()
                });
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'code'},
            {data: 'accountName'},
            {data: 'acceptanceBankFullName'},
            {data: 'amount', sClass: "text-right"}
            , {data: 'endDate'}
            , {data: 'discountRate', sClass: "text-right"}
            , {data: 'discountRateBase', sClass: "text-right"}
            , {data: 'created'}
            , {data: 'orgName'}
            , {data: 'statusForShow'}
            , {defaultContent: ''}
        ]
        , fnRowCallback: function (nRow, aData, iDataIndex) {
            $('td:eq(3)', nRow).html(renderAmount(aData.amount));
            $('td:eq(4)', nRow).html(renderTime(aData.endDate));
            $('td:eq(5)', nRow).html(renderPercent(aData.discountRate));
            $('td:eq(6)', nRow).html(renderPercent(aData.discountRateBase));
            $('td:eq(7)', nRow).html(renderTime(aData.created));
            $('td:eq(-1)', nRow).html(renderOperation(aData));
            return nRow;
        }
        , footerCallback: function(row, data){
            $("#count").html(data.length);
            $("#amount").html(pageTotalColumn(data, 'amount'));
        }
    });
}

/** Tools  **/
function renderPercent(data){
    return formatMoney(data, 6);
}

function renderTime(data, type, full, meta){
    return date2String(new Date(data));
}
//js将Date类型转换为String类型：
function date2String(aDate){
    var year=aDate.getFullYear();
    var month=aDate.getMonth();
    month++;
    var mydate=aDate.getDate();
    var hour=aDate.getHours();
    var minute=aDate.getMinutes();
    var mytime=year+"-"+(month<10?"0":"")+month+"-"+(mydate<10?"0":"")+mydate;
    return mytime;
}

function renderAmount(data, type, full, meta) {
    if (data) {
        return "<span class='bolder'>" + formatMoney(data, 2) + "</span>";
    }
    return "";
}

function renderOperation(data){
    var operation = '';
    var opName = ($('#submit').val() == 'true' || $('#add').val() == 'true')?'处理':'查看';
    if(data.status == "NEW") {
        operation += '<a class="button btn-sm btn-info edit" rel="' + data.id + '">' + opName + '</a>';
    } else if (data.status == "SUBMITTED" && $('#withdraw').val()) {
        if($('#withdraw').val() == 'true') {
            operation += '<a class="button btn-sm btn-info auditrecharge" rel="' + data.id + '">申请撤销</a>';
        }
        if($('#check').val() == 'true') {
            if($('#withdraw').val() == 'true') {
                operation += '<br/>';
            }
            operation += '<a class="button btn-sm btn-info check" rel="' + data.id + '">审核</a>';
        }
    } else if (data.status == "CHARGED") {
        operation += '<a class="button btn-sm btn-info viewcharged" rel="' + data.id + '">查看</a>';
    } else if (data.status == "ROLLBACKREQUEST") {
        operation += '<a class="button btn-sm btn-info cancelcharge" rel="' + data.id + '">处理</a>';
    } else if (data.status == "RENEW") {
        operation += '<a class="button btn-sm btn-info edit" rel="' + data.id + '">' + opName + '</a>';
    }
    return operation;
}

function pageTotalColumn(data, columnName) {
    var total = 0;
    var temp;
    for (var i = 0; i < data.length; i++) {
        temp = parseFloat(data[i][columnName]);
        if (!isNaN(temp)) {
            total += parseFloat(temp.toFixed(2));
        }
    }
    return formatMoney(total, 2);
}

function initOrg(){
	 $.ajax({
	        type: "POST",
	        url: Context.PATH + '/acceptdraft/queryDraftedOrg.html',
	        data: {},
	        dataType: "json",
	        success: function (response, textStatus, xhr) {
	            if (response.success) {
	                var datas = response.data;
	                $("#org").empty();
	                $("#org").append('<option value ="">全部</option>');
	                for (var i in datas) {
	                    $("#org").append('<option value ="' + datas[i].id + '">' + datas[i].name + '</option>');
	                }
	                var selectedOrgId = $('#org').attr('value');
	                $('#org option[value="'+selectedOrgId+'"]').attr("selected",true);
	            }
	        },
	        error: function (xhr, textStatus, errorThrown) {
	        }
	    });
}