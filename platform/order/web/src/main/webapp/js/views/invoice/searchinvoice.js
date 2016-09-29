/**
 * 根据发票名模糊查询有效发票
 * 输入框添加 invoiceSearch='invoice' 属性即可
 * 如果没有配置该属性，默认查询进项票 如果需要限定用户角色，添加属性 invoiceType="in" 或  invoiceType="out"
 * 示例：
 * <input type='text' invoiceSearch='invoice'  />
 * Created by zhoucai on 2016/3/24.
 */

$().ready(function () {
    // 绑定输入框值改变事件
    $(document).on("input", "input[invoiceSearch='invoice']", function (e) {
        $(this).attr("invoiceId", 0);
        $(this).removeClass("temp-lin");
        searchInvoiceByCode($(this));
        onBlur1($(this));
    });

    // 绑定下拉列表键盘事件(需要整合)
    $(document).on("keydown", "input[invoiceSearch='invoice']", function (e) {
        var event = e || window.event;
        if (event.keyCode == 38 || event.keyCode == 40 || event.keyCode == 13) {
            SelectOptions(event.keyCode, $("#searchInvoiceCodeList"));
        }
        else {
            currentLine = -1;
        }
    });

    // 绑定下拉选择单击事件
    $(document).on("click", "#searchInvoiceCodeList li a", function () {    	
        var input = "input[inputid='" + $("#searchInvoiceCodeList").attr("inputid") + "']";
        var invoiceId = $(this).attr("invoiceId");
        $(input).attr("invoiceId", invoiceId);
        $(input).val(invoiceId);
        $("#searchInvoiceCodeList").hide();
    });

    // 点击页面其他地方隐藏下拉div
    $(document).mouseup(function (e) {
        var event = e || window.event;
        var targetId = $(event.target).attr("id");
        var searchAccountId = "searchInvoiceCodeList";
        if (targetId != searchAccountId) {
            $("#" + searchAccountId).hide();
        }
    });
});
function onBlur1(input){
	$(input).unbind("blur").blur(function(){
		var inputVal=$.trim($(input).val());
		if(inputVal!==""){
			$("#searchInvoiceCodeList li a").each(function(){
				if($(this).text()==inputVal){
					$(this).click();
				}
			})
		}else{
			$(input).removeAttr("invoiceId");
	        $(input).val("");
		}
	});
}



function searchInvoiceByCode(inputBox) {
    var invoiceCode = $.trim($(inputBox).val());
    if (invoiceCode == "") return;
    var invoiceType = $(inputBox).attr("invoiceType");
    if (invoiceType == null || invoiceType == undefined || invoiceType == "")
    	invoiceType = "";

    $.ajax({
        type: 'post',
        url: Context.PATH + "/invoice/in/searchinvoicecodelist.html",
        data: {
        	code: invoiceCode,
            type: invoiceType
        },
        error: function (s) {
        }
        , success: function (result) {
            if (result) {
                $("#searchInvoiceCodeList").remove();   // 先清除该元素，防止出现多个
                if ($(result.data) != null && $(result.data).length > 0) {
                    var inputId = "invoice" + new Date().getTime();
                    $(inputBox).attr("inputid", inputId);
                    var searchHtml = "<div id='searchInvoiceCodeList' class='product-complete' inputid='" + inputId + "'>";
                    searchHtml += "<ul>";
                    var flagIndex = -1;
                    for (var i = 0; i < $(result.data).length; i++) {
                        // 如果相等，就默认选中第一个
                        if (result.data[i].name == name) {
                            flagIndex = i;
                        }
                        searchHtml += "<li><a href='javascript:;' ";
                        searchHtml += " invoiceId='" + result.data[i].code + "'";
                        searchHtml += " >";
                        searchHtml += processStr(invoiceCode, result.data[i].code);
                        searchHtml += "</a></li>";
                    }
                    searchHtml += "</ul>";
                    searchHtml += "</div>";
                    $("body").append($(searchHtml));
                    setLayerPosition($(inputBox), $("#searchInvoiceCodeList"));
                    controlLayerShow($("#searchInvoiceCodeList"));
                    if (flagIndex > -1) {
                        $("#searchInvoiceCodeList a").eq(flagIndex).click();
                    }
                }
            } else {
                cbms.alert("查询失败");
            }
        }

    });
}