/**
 * 查询品名选择框
 * 输入框添加 search='nsort' 属性即可
 * 示例：
 * <input type='text' search='nsort' />
 * Created by lcw on 2015/7/22.
 */

var nsortCacheData = null;
var nsortLayerId = "showLayer_nsortName";
$().ready(function () {
    // 绑定输入框focus事件
    $(document).on("focus", "input[search='nsort']", function () {
        $(this).attr("nsortid", 0);
        getNsort($(this));
    });

    // 绑定选择单击事件
    $(document).on("click", "#" + nsortLayerId + " span a", function () {
        var showLayer = $("#" + nsortLayerId);
        var input = "input[inputid='" + $(showLayer).attr("inputid") + "']";
        var nsortId = $(this).attr("nsortid");
        var nsortName = $(this).text();
        $(input).attr("nsortid", nsortId);
        $(input).val(nsortName);
        $(showLayer).remove();
    });

    //鼠标滑过大类，显示大类下的小类
    var t;
    $(document).on("mouseover", ".product-t-li",
        function () {
            var tab = this;
            t = setTimeout(function () {
                $(tab).parent().find('li').removeClass('hover');
                $(tab).addClass('hover');
                $('div[name="nsort"]').hide();
                $('div[name="nsort"]:eq(' + ($(tab).index()) + ')').show();
                return false;
            }, 200);
        }
    );
    $(document).on("mouseout", ".product-t-li",
        function () {
            clearTimeout(t);
        }
    );

    // 点击页面其他地方隐藏该div
    $(document).mousedown(function (e) {
        var event = e || window.event;
        var targetId = $(event.target).attr("id");
        if (targetId != nsortLayerId) {
            $("#" + nsortLayerId).hide();
        }
    });

    // 阻止事件继续冒泡
    $(document).on("mousedown", "#" + nsortLayerId, function (e) {
        var event = e || window.event;
        event.stopPropagation();
    });

});

// 获取品名数据
function getNsort(inputBox) {
    if (nsortCacheData == null || nsortCacheData == "") {
        $.ajax({
            type: 'post',
            url: Context.PATH + "/common/getnsort.html",
            data: {},
            dataType: "json",
            error: function (s) {
            }
            , success: function (result) {
                if (result && result.success) {
                    nsortCacheData = result.data;
                    loadSortName(nsortCacheData);
                    showNsortLayer(inputBox);
                } else {
                    cbms.alert("查询失败");
                }
            }
        });
    }
    else {
        loadSortName(nsortCacheData);
        showNsortLayer(inputBox);
    }
}

// 显示品名弹层在对应的输入框下
function showNsortLayer(inputBox) {
    // 通过唯一属性 inputId 来关联
    var showLayer = $("#" + nsortLayerId);
    var inputId = "nsort" + new Date().getTime();
    $(inputBox).attr("inputid", inputId);
    $(showLayer).attr("inputid", inputId);

    setLayerPosition($(inputBox), $(showLayer));
    controlLayerShow($(showLayer));
}

//加载品名大类数据
function loadSortName(data) {
    $("#" + nsortLayerId).remove();
    // <!--品名数据层  S-->
    var layerHtml = "<div class='show-layer product-bar none' id='" + nsortLayerId + "'>";
    layerHtml += "<div class='product-t'>";
    layerHtml += "<ul class='product-t-ul'></ul>";
    layerHtml += "</div>";
    layerHtml += "</div>";
    $("body").append(layerHtml);
    // <!--品名数据层 E-->

    if (data.statusCode === 0) {
        var search_sorts = data.data;
        $(search_sorts).each(function (i, e) {
            //卷板，型材 横向列表
            var li = '<li class="product-t-li ' + (i === 0 ? 'hover' : '') + '"><a href="javascript:;" sortId="' + e.sortID + '">' + e.sortName + '</a>'
                + '<em class="icon down-redArr-icon"></em></li>';
            $("#" + nsortLayerId + " .product-t .product-t-ul").append(li);//'<a href="javascript:void(0)" sortId="'+e.sortID+'"><font>'+e.sortName+'</font></a>');
            //以下：品名明细，包括 热轧+品名
            //最大的包裹div
            var classDiv = $('<div name="nsort"/>');
            $(e.classInfo).each(function (j, f) {
                var nsort = $('<div class="product-con f-clrfix bder-b-dashed"/>');
                //热轧，冷轧
                var className = $('<div class="product-con-sort f-fl"><span class="bold">' + (f.className === "" ? "" : f.className + ":") + '</span></div>');

                if (f.nsort.length > 0) {
                    //品名列表最外层div
                    var nsortDiv = $('<div class="product-con-bar f-fl"/>'),
                        rowSize = 5,
                        nsortPkg = "";
                    $(f.nsort).each(function (k, g) {
                        //每N行换行div
                        if (k % rowSize === 0) {
                            nsortPkg += '<div class="product-con-bar-list">';
                        }
                        nsortPkg += '<span><a nsortId="' + g.nsortID + '" href="javascript:;">' + g.nsortName + '</a></span>';
                        if ((k + 1) % rowSize === 0 || (k + 1) === f.nsort.length) {
                            nsortPkg += '</div>';
                        }
                    });
                    $(nsortDiv).append($(nsortPkg));
                    $(nsort).append(className);
                    $(nsort).append(nsortDiv);
                }
                $(classDiv).append(nsort);
            });
            $("#" + nsortLayerId).append(classDiv);

        });

        $("div[name='nsort']:gt(0)").hide();
    } else {
        search_sorts = null;
        alert("加载品名大类发生错误:" + data.message);
    }
}