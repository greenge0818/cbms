var InquiryOrderPage = new function() {
    this.dt;
}

//清空按钮
$(document).on("click","#cleanSearch",function(){
	resetForm($("form.form-inline"));
});

$(document).ready(function() {
    searchClick();
    var url = Context.PATH + "/smartmatch/inquiryorder/search.html";
    InquiryOrderPage.dt = jQuery("#dynamic-table").DataTable({
        "processing" : false,
        "serverSide" : false,
        "searching" : false,
        "ordering" : false,
        "paging" : true,
        "bInfo" : false,
        "autoWidth": true,
        "iDisplayLength":50,
        "aLengthMenu": [10,30,50,100],//定义每页显示数据数量
        "ajax" : {
            "url" : url,
            "type" : "POST",
            data : function(d) {
                return $.extend({}, d, {
                    code:$("#code").val(),
                    seller : $("#seller").val(),
                    city : $("#city").val(),
                    categoryName : $("#categoryName").val(),
                    materialName : $("#materialName").val(),
                    spec : $("#spec").val(),
                    warehouse : $("#warehouse").val(),
                    factory : $("#factory").val(),
                    startDate : $("#startDate").val(),
                    endDate : $("#endDate").val()
                });
            }
        },
        columns : [
            {data : 'code'},
            {data : 'createdTime'},
            {data : 'categoryName'},
            {data : 'materialName'},
            {data : 'spec'},
            {data : 'factory'},
            {data : 'warehouse'},
            {data : 'costPrice'},
            {data : 'seller'},
            {data : 'remark'}
        ]

    });
});
function searchClick(){
    jQuery("#searchList").on(ace.click_event, function() {
        searchData(true);
    });
}

function searchData(isNewSearch) {
	if(isNewSearch){
		InquiryOrderPage.dt.ajax.reload();
	}else{
		InquiryOrderPage.dt.ajax.reload(null, false);
	}
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
    var mytime=year+"-"+(month<10?"0":"")+month+"-"+(mydate<10?"0":"")+mydate+" "+(hour<10?"0":"")+hour+":"+(minute<10?"0":"")+minute;
    return mytime;
}