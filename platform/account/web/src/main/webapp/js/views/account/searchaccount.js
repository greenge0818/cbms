/**
 * 根据账号名模糊查询有效账户
 * 输入框添加 search='account' 属性即可
 * 如果需要限定用户角色，添加属性 accounttype="seller" 或  accounttype="buyer"
 * 添加： accounttype='department', 则返回具体部门列表
 * 示例：
 * <input type='text' search='account' />
 * Created by lcw on 2015/7/20.
 */

$().ready(function () {
    // 绑定输入框值改变事件
    $(document).on("input", "input[search='account']", function (e) {
        $(this).attr("accountid", 0);
        $(this).attr("consigntype", "");
        $(this).removeClass("temp-lin");
        searchAccountByName($(this));
        onBlur($(this));
    });

    // 绑定下拉列表键盘事件(需要整合)
    $(document).on("keydown", "input[search='account']", function (e) {
        var event = e || window.event;
        if (event.keyCode == 38 || event.keyCode == 40 || event.keyCode == 13) {
            SelectOptions(event.keyCode, $("#searchAccountList"));
        }
        else {
            currentLine = -1;
        }
    });

    // 绑定下拉选择单击事件
    $(document).on("click", "#searchAccountList li a", function () {
        var input = "input[inputid='" + $("#searchAccountList").attr("inputid") + "']";
        var accountId = $(this).attr("accountid");
        var consignType = $(this).attr("consigntype");
        var accountName = $(this).attr("accountname");
        if (consignType == "temp") {
            $(input).addClass("temp-lin");
        }
        $(input).attr("accountid", accountId);
        $(input).attr("consigntype", consignType);
        $(input).val(accountName);
        $("#searchAccountList").hide();
        
        /*这里添加一个回调方法, tuxianming*/
        if(window.doProcessSearchAccountSelected){
        	doProcessSearchAccountSelected(accountId, consignType, accountName);
        }
        
    });

    // 点击页面其他地方隐藏下拉div
    $(document).mouseup(function (e) {
        var event = e || window.event;
        var targetId = $(event.target).attr("id");
        var searchAccountId = "searchAccountList";
        if (targetId != searchAccountId) {
            $("#" + searchAccountId).hide();
        }
    });
});

function onBlur(input){
	$(input).unbind("blur").blur(function(){
		var inputVal=$.trim($(input).val());
		if(inputVal!==""){
			$("#searchAccountList li a").each(function(){
				if($(this).text()==inputVal){
					$(this).click();
				}
			})
		}else{
			$(input).removeAttr("accountid");
	        $(input).removeAttr("consigntype");
	        $(input).val("");
		}
	});
}

function selectResult(input){
	//键盘选择模糊匹配结果
	$(input).keydown(function (event) {
		if ($("#searchAccountList li").length > 0) {
			var index;
			if (event.keyCode === 38) {
				index = $("#searchAccountList li").has("a.hover").index();
				$("#searchAccountList li:eq(" + index + ") a").removeClass("hover");
				if (index === -1 || index === 0) {
					index = $("#searchAccountList li").length - 1;
				} else {
					index--;
				}
				$("#searchAccountList li:eq(" + index + ") a").addClass("hover");
			} else if (event.keyCode === 40) {
				index = $("#searchAccountList li").has("a.hover").index();
				$("#searchAccountList li:eq(" + index + ") a").removeClass("hover");
				if (index === -1 || index === $("#searchAccountList li").length - 1) {
					index = 0;
				} else {
					index++;
				}
				$("#searchAccountList li:eq(" + index + ") a").addClass("hover");
			} else if (event.keyCode === 13) {
				index = $("#searchAccountList li").has("a.hover").index();
				if (index > -1) {
					$("#searchAccountList li:eq(" + index + ") a").click();
					
					return false;
				}
			}
		}
	});
}

function searchAccountByName(inputBox) {
    var name = $.trim($(inputBox).val());
    if (name == "") return;
    var accountType = $(inputBox).attr("accounttype");
    if (accountType == null || accountType == undefined || accountType == "")
        accountType = "";

    $.ajax({
        type: 'post',
        url: Context.PATH + "/common/searchaccount.html",
        data: {
            accountName: name,
            type: accountType
        },
        error: function (s) {
        }
        , success: function (result) {
            if (result) {
                $("#searchAccountList").remove();   // 先清除该元素，防止出现多个
                if ($(result.data) != null && $(result.data).length > 0) {
                    var inputId = "account" + new Date().getTime();
                    $(inputBox).attr("inputid", inputId);
                    var searchHtml = "<div id='searchAccountList' class='product-complete' inputid='" + inputId + "'>";
                    searchHtml += "<ul>";
                    var flagIndex = -1;
                    for (var i = 0; i < $(result.data).length; i++) {
                        // 如果相等，就默认选中第一个
                        if (result.data[i].name == name) {
                            flagIndex = i;
                        }
                        searchHtml += "<li><a href='javascript:;' ";
                        searchHtml += " accountid='" + result.data[i].id + "'";
                        searchHtml += " accountname='" + result.data[i].name + "'";
                        searchHtml += " consigntype='" + result.data[i].consignType + "'";
                        searchHtml += " provinceid='" + result.data[i].provinceId + "'";
                        searchHtml += " cityid='" + result.data[i].cityId + "'";
                        searchHtml += " >";
                        if (result.data[i].consignType == "temp") {
                            searchHtml += "<em class='label-orange white'>临</em>";
                        }
                        searchHtml += processStr(name, result.data[i].name);
                        searchHtml += "</a></li>";
                    }
                    searchHtml += "</ul>";
                    searchHtml += "</div>";
                    $("body").append($(searchHtml));
                    setLayerPosition($(inputBox), $("#searchAccountList"));
                    controlLayerShow($("#searchAccountList"));
                    if (flagIndex > -1) {
                        $("#searchAccountList a").eq(flagIndex).click();
                    }
                }
            } else {
                cbms.alert("查询失败");
            }
        }

    });
}