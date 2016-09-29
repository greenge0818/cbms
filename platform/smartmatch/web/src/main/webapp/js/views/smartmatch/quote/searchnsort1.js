/**
 * 查询品名选择框
 * 输入框添加 search='nsort' 属性即可
 * 示例：
 * <input type='text' search='nsort' />
 * Created by lcw on 2015/7/22.
 */

var nsortCacheData = null;
var _nsort = null;
var _factory = null;
var _account = null;
var _specificSeller = [];
var nsortLayerId = "showLayer_nsortName";
var materialLayerId = "showLayer_material";
//$().ready(function () {
//   
//});

function loadAllFactory(){
    $.ajax({
        type : "POST",
        url : Context.PATH + "/smartmatch/factory/getAllFactory.html",
        data : {},
        dataType : "json",
        async : false,
        success : function(response) {
            if (response.success) {
                _factory = response.data;
            } else {
                cbms.alert(response.data);
            }
        },
        error : function(xhr, textStatus, errorThrown) {}
    });
}

function loadAllNsort(){
	//cbms.loading();
    $.ajax({
        type : "POST",
        url : Context.PATH + "/category/getAllCategory.html",
        data : {},
        dataType : "json",
        async : false,
        success : function(response) {
        	//cbms.closeLoading();
            if (response.success) {
                _nsort = response.data;
            } else {
                cbms.alert(response.data);
            }
        },
        error : function(xhr, textStatus, errorThrown) {}
    });
}

function loadAllAccount(){
    $.ajax({
        type : "POST",
        url : Context.PATH + "/smartmatch/purchaseorder/getAllAccount.html",
        data : {},
        dataType : "json",
        async : false,
        success : function(response) {
            if (response.success) {
                _account = response.data;
                for(var i in response.data){
                    if(response.data[i].uuid != null){
                        _specificSeller.push(response.data[i]);
                    }
                }
            } else {
                cbms.alert(response.data);
            }
        },
        error : function(xhr, textStatus, errorThrown) {}
    });
}

function enableNsortEvent(){

    $(document).on("input", "input[search='nsort']", function () {
        $(".show-layer").hide();
        var input = $(this);
        showPYMatchList(input, _nsort, 'uuid', 'name', function(){
            $(_nsort).each(function (i, e) {
                if(e.name == $(input).val()) {
                    $(input).attr("nsortid", e.uuid);
                }
            });
            loadData(input);
        });
    });

    // 绑定输入框focus事件
    $(document).on("click", "input[search='nsort']", function () {
        getNsort($(this));
    });

    // 绑定选择品名单击事件
    $(document).on("click", "#" + nsortLayerId + " span a", function () {
        var showLayer = $("#" + nsortLayerId);
        var input = "input[inputid='" + $(showLayer).attr("inputid") + "']";
        var nsortId = $(this).attr("nsortid");
        var nsortName = $(this).text();
        $(input).attr("nsortid", nsortId);
        $(input).val(nsortName);
        $(showLayer).remove();

        loadData($(input));
    });
    function loadData(input){
        //联动材质，规格，属性，厂家
        resetMaterial($(input));
        resetSpec($(input));
        resetFactory($(input));
        resetSingleWeight($(input));

        showSpecList($(input));
        loadAttribute($(input));
        loadMaterial($(input));
        loadFactory($(input));

    }

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

    // 阻止事件继续冒泡
    $(document).on("mousedown", "#" + nsortLayerId, function (e) {
        var event = e || window.event;
        event.stopPropagation();
    });
}

// 获取品名数据
function getNsort(inputBox) {
    if (nsortCacheData == null || nsortCacheData == "") {
        $.ajax({
            type: 'post',
            url: Context.PATH + "/smartmatch/purchaseorder/GetSortAndNsort.html",
            data: {},
            dataType: "json",
            async : false,
            error: function (s) {
            }
            , success: function (result) {
                if (result.statusCode == 0) {
                    nsortCacheData = result;
                    loadSortName(nsortCacheData);
                    showNsortLayer(inputBox);
                    $(inputBox).closest("#m-search").find("#nsortloadok").val("done").change();
                } else {
                    cbms.alert("查询失败");
                }
            }
        });
    }
    else {
        loadSortName(nsortCacheData);
        showNsortLayer(inputBox);
        $(inputBox).closest("#m-search").find("#nsortloadok").val("done").change();
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

    if (data.statusCode == 0) {
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

//加载材质
function loadMaterial(inputBox) {
    var showPlace = $(inputBox).closest('#m-search').find('.material').find('#' + materialLayerId);
    $.ajax({
        type: 'post',
        url: Context.PATH + "/smartmatch/purchaseorder/GetMaterial.html",
        data: {
            nosrtUUID: $(inputBox).attr("nsortid")
        },
        dataType: "json",
        async : false,
        error: function (s) {
        },
        success: function (data) {
            if (data.statusCode === 0) {
                $(showPlace).find(".textures-con").empty();
                var rowSize = 4, content = "";
                $(data.data).each(function (i, e) {
                    if (i % rowSize === 0) {
                        content += '<div class="textures-con-bar-list bder-b-dashed">';
                    }
                    content += '<span><a href="javascript:;" materialId ="' + e.material.uuid + '" materialName="' + e.material.name + '">' + e.material.name + '</a></span>';
                    if ((i + 1) % rowSize === 0 || (i + 1) === data.data.length) {
                        content += '</div>';
                    }
                });
                $(showPlace).find(".textures-con").append($(content));
                $(showPlace).show();
                $(inputBox).closest("#m-search").find("#materialloadok").val("done").change();

            } else {
                alert("加载材质发生错误:" + data.message);
            }
        }
    });
}

//加载属性
function loadAttribute(inputBox) {
    var showPlace = $(inputBox).closest('#m-search');
    $.ajax({
        type: 'post',
        url: Context.PATH + "/smartmatch/purchaseorder/GetAttribute.html",
        data: {
            nosrtUUID: $(inputBox).attr("nsortid")
        },
        dataType: "json",
        async : false,
        error: function (s) {
        },
        success: function (result) {
            var data = result.data;
            var html = '';
            for (var i in data) {
                html += renderAttribute(data[i], $(showPlace));
            }
            //显示
            $(showPlace).find("div.attrs").html("");
            $(showPlace).find("div.attrs").append(html);
            $(showPlace).find("#attrloadok").val("done").change();
        }
    });
}

//渲染属性控件,生成html代码
function renderAttribute(attribute, e) {
    //var index = $("span.attribute").length*1 + i*1 + 1*1;
    var html = ' <span class="search-radio-bar craft-bar">' + attribute.name + '：<input type="hidden" name="attribute" value="' + attribute.uuid + '" element="' + attribute.type + '"/>';
    var options;
    if (attribute.options != null) {
        options = attribute.options.split(',');
    }
    switch (attribute.type) {
        case "radio":
            for (var i in options) {
                html += '<label><input type="radio" name="'+attribute.uuid+"-"+$(".m-search").index($(e))+'" value="' + options[i] + '"/>' + options[i] + '</label>';
            }
            break;
        case "checkbox":
            for (var i in options) {
                html += '<label><input type="checkbox" name="'+attribute.uuid+'" value="' + options[i] + '"/>' + options[i] + '</label>';
            }
            break;
        case "select":
            html += '<select name="'+attribute.uuid+'">';
            for (var i in options) {
                html += '<option value="' + options[i] + '">' + options[i] + '</option>';
            }
            html += '</select>';
            break;
        case "input":
            html += '<input type="text" name="'+attribute.uuid+'" class="value" style="width: 105px"/>';
            break;
    }
    html += '</span>';
    return html;
}

/**
 *@date:2015-05-05
 *@Modify-lastdate :2015-05-13
 *@author :Green.Ge
 *@describe：选择品名后，显示该品名下的规格列表，如长宽高，但是不包括具体规格下的参数列表
 *@param: 品名ID
 *@return：null
 *
 */
function showSpecList(input) {
    var nsortId = $(input).attr("nsortid");
    var showPlace = $(input).closest('#m-search').find('.spec');
    $(showPlace).empty();
    var isReturn = false;
    $(nsortCacheData.data).each(function (i, e) {
        $(e.classInfo).each(function (j, f) {
            $(f.nsort).each(function (k, g) {
                if (g.nsortID == nsortId) {
                    var specNames = g.specName;
                    var mixLen = 3, len = utils.isEmpty(specNames) ? 0 : Object.getOwnPropertyNames(specNames).length;
                    var content='';
                    var _idx = 1;
                    $.each(specNames, function (key, value) {

                        if ($.trim(value) !== "" && value !== null) {
                            content +=  '<div class="product-ipt guige" type="spec" id="' + key + '">'
                                + (key === "spec1" ? "<span class='in-block f-fl'><em class='red'>*</em>规格：</span>" : "")
                                + '	   <div class="form-item">'
                                + '         <label style="color: rgb(169, 169, 169);" class="f-label spec">' + value + '</label>'
                                + '         <input type="text" id="input' + key + '" class="f-text spec" title="'+(key === "spec1" ? "可多选" : "单选")+'"'
                                + '                must="' + (key === "spec1" ? 1 : "") + '" readonly/>'
                                + '    </div>'
                                + (_idx!=3?'&nbsp;&nbsp;*':'')
                                + '</div>';
                        }
                        _idx++;
                    });

                    //当品规数少于3个时，自动填充一个input。
                    for(var i=0;i<(mixLen-len);i++){
                        content +=  '<div class="product-ipt">'
                            + '	   <div class="form-item">'
                            + '         <input type="text" class="f-text spec" placeholder="无" disabled/>'
                            + '    </div>'
                            + (i!=(mixLen-len-1)?'&nbsp;&nbsp;&nbsp;*':'')
                            + '</div>';
                    }

                    $(showPlace).append(content);
                    $(showPlace).find('#inputspec1').inputFocus();
                    $(showPlace).find('#inputspec2').inputFocus();
                    $(showPlace).find('#inputspec3').inputFocus();
                    isReturn = true;
                    return false;
                }
            });
            if (isReturn) {
                return false;
            }
        });
        if (isReturn) {
            return false;
        }
    });
}

//加载规格数据
function loadSpec(input, spec1, spec2, spec3){
    if($(input).attr("materialid") == ""){
        return false;
    }
    var showPlace = $(input).closest(".m-search");
    $.ajax({
        type: 'post',
        url: Context.PATH + "/smartmatch/purchaseorder/GetSpec.html",
        data: {
            materialUUID: $(input).attr("materialid"),
            nosrtUUID: $(showPlace).find('#nsortName').attr("nsortid")
        },
        dataType: "json",
        async : false,
        error: function (s) {
        },
        success: function (data) {
            if (data.statusCode === 0) {
                $(showPlace).find(".product-ipt[type='spec']").each(function (i, e) {
                    var spec = $(this).attr("id"),
                        specList = data.data[spec + 'List'],
                        rowSize = 5,
                        colSize = 8,
                        pageSize = rowSize * colSize,
                        page = 0;
                    if (specList.length === 0) {
                        page = 1;
                    } else {
                        page = Math.floor((specList.length - 1) / pageSize) + 1;
                    }
                    //spec1 多选处理
                    if (i === 0) {
                        setSpec1(specList, spec, page, pageSize, rowSize, $(showPlace), spec1);
                    } else {
                        //spec2,3 单选，区间输入处理
                        $(showPlace).find("#input" + spec).val("");
                        setSpec2Spec3(specList, rowSize, spec, $(showPlace), (spec == 'spec2' ? spec2 : spec3));
                    }
                });
                $(input).closest(".m-search").find("#showLayer_spec1").show();
                $(input).closest("#m-search").find("#specloadok").val("done").change();
            } else {
                alert("加载规格发生错误:" + data.message);
            }
        }
    });
}

/**
 *@date:2015-05-05
 *@Modify-lastdate :2015-05-13
 *@author :Green.Ge
 *@describe：设置规格1的参数列表，之所以不同规格要分开设置是因为规格1和2,3的处理方式是不一样的。
 *@param: specList:该规格下的明细数据，spec：规格弹层的名称参数，page：分几页（tab）显示, pageSize：一页显示多少数据，rowSize：一行显示多少数据
 *@return：null
 *
 */
function setSpec1(specList, spec, page, pageSize, rowSize, showPlace, origin) {
    var array = [];
    if(origin != ''){
        array = origin.split(",");
    }
    var content = '<!--规格数据层  S-->'
            + '<div class="show-layer textures-bar standard-bar none" id="showLayer_' + spec + '">'
            + '<div class="standard-t f-clr-r">'
            + '   <ul class="standard-t-ul">',
        specDetail = "";
    if(specList.length>0){
        var p;
        for (p = 0; p < page; p++) {
            var from = p * pageSize,
                to = (from + pageSize) > specList.length ? specList.length - 1 : (from + pageSize - 1);
            content += '<li class="standard-t-li ' + (p === 0 ? 'hover' : '') + '"><a href="javascript:;">' + specList[from].spec + '~' + specList[to].spec + '</a>'
            + '    <em class="icon down-redArr-icon"></em>'
            + '</li>';
            specDetail += '<div class="textures-con f-clrfix" type="specDetail">';
            var q;
            for (q = from; q <= to; q++) {

                if (q % rowSize === 0) {
                    specDetail += ' <div class="textures-con-bar-list bder-b-dashed">';
                    if (q + rowSize < to) {
                        specDetail += '<span class="textures-list-first bold">' + specList[q].spec + '~' + specList[q + rowSize - 1].spec + '</span>';
                    } else {
                        specDetail += '<span class="textures-list-first bold">' + specList[q].spec + '~' + specList[to].spec + '</span>';
                    }
                }
                specDetail += '<span><a href="javascript:;" class="' + (array.indexOf(specList[q].spec) >= 0 ? "hover" : '') +'">' + specList[q].spec + '</a></span>';
                if ((q + 1) % rowSize === 0 || (q + 1) === specList.length) {

                    specDetail += '</div>';
                }
            }
            specDetail += ' <div class="f-clr-l"></div>';
            specDetail += '</div>';
        }
    } else {
        specDetail = "你所选择的品类材质组合下没有资源";
    }

    content += '    </ul>'
    + '    <a class="layer-del f-fr"></a>'
    + '</div>';
    content += specDetail;
    content +=  ' <div class="btn-bar t-l">'
    + '     <span class="s-gray">其他：(不同规格中间用逗号分开)</span>'
    + '     <input class="range-ipt" type="text" value="" id="otherspec1" style="width: 100px"/>'
    + '     <button class="clear-btn">清除</button>'
    + '     <button class="confirm-btn">确认</button>'
    + ' </div>'
    + '<!--规格数据层  E--></div>';
    $(showPlace).find("#showLayer_" + spec).remove();
    $(showPlace).find("#" + spec + " .form-item").append(content);
    //只显示第一个规格tab，其他tab隐藏
    $("#" + spec + " .form-item div[type='specDetail']:gt(0)").hide();

}


/**
 *@date:2015-05-05
 *@Modify-lastdate :2015-05-13
 *@author :Green.Ge
 *@describe：设置规格2,3的参数列表，之所以不同规格要分开设置是因为规格1和2,3的处理方式是不一样的。
 *@param: specList:该规格下的明细数据，spec：规格弹层的名称参数，rowSize：一行显示多少数据
 *@return：null
 *
 */
function setSpec2Spec3(specList, rowSize, spec, showPlace, origin) {
    var from = '', to = '';
    if(origin != '' && origin.indexOf("-") != -1){
        var fromTo = origin.split("-");
        from = fromTo[0], to = fromTo[1];
    }
    var classes = "";
    if (spec === "spec3") {
        classes = "show-layer breadth-bar extent-bar  none";
    } else {
        classes = "show-layer textures-bar breadth-bar standard-bar none";
    }
    var content = '<div class="' + classes + '" id="showLayer_' + spec + '">'
        + '<div class="textures-con f-clrfix ">';
    if(specList.length > 0){
        $(specList).each(function (j, e) {
            if (j % rowSize === 0) {
                content += ' <div class="textures-con-bar-list bder-b-dashed">';
            }
            content += '<span><a href="javascript:;">' + specList[j].spec + '</a></span>';
            if ((j + 1) % rowSize === 0 || (j + 1) === specList.length) {
                content += '</div>';
            }
        });
    } else {
        content += "你所选择的品类材质组合下没有资源";
    }

    content += '<div class="f-clr-l"></div>'
    + ' </div>'
    + ' <div class="btn-bar t-l">'
    + '     <span class="s-gray">范围：</span>'
    + '     <input class="range-ipt" type="text" value="' + from + '" id="from_' + spec + '" /><em class="dash-line">—'
    + '       </em><input class="range-ipt" type="text" value="' + to + '" id="to_' + spec + '" />'
    + '     <button class="clear-btn">清除</button>'
    + '     <button class="confirm-btn">确认</button>'
    + ' </div>'
    + '</div>';
    $(showPlace).find(".spec #showLayer_" + spec).remove();
    $(showPlace).find(".spec #" + spec + " .form-item").append(content);
    if(from != '') {
        $(showPlace).find("#" + spec + " button.confirm-btn").click();
    }else{
        $(showPlace).find("#showLayer_" + spec + " .textures-con a").each(function (j, f) {
            if ($(f).text() === origin) {
                $(f).click();
            }
        });
    }
}

function loadFactory(input) {
    $.ajax({
        type: 'post',
        url: Context.PATH + "/smartmatch/purchaseorder/GetFactory.html",
        data: {
            nosrtUUID:  $(input).attr("nsortid")
        },
        dataType: "json",
        async: false,
        error: function (s) {
        },
        success: function (data) {
            var showPlace = $(input).closest('#m-search').find('.factory_layer .textures-con');
            if (data.statusCode === 0) {
                $(showPlace).empty();
                var content = "", rowSize = 5;
                if(data.data.length == 0){
                    content += '<div class="textures-con-bar-list bder-b-dashed">';
                    content += '你所选择的品类材质组合下没有厂家';
                    content += "</div>";
                }else {
                    $(data.data).each(function (i, e) {
                        if (i % rowSize === 0) {
                            content += '<div class="textures-con-bar-list bder-b-dashed">';
                        }
                        content += '<span><a href="javascript:;" factoryId="' + e.factory.uuid + '" class="">' + e.factory.name + '</a></span>';
                        if ((i + 1) % rowSize === 0 || (i + 1) === data.data.length) {
                            content += "</div>";
                        }
                    });
                }
                $(showPlace).append(content);
                $(input).closest('#m-search').find("#factroyloadok").val("done").change();
            } else {
                alert("加载厂家发生错误:" + data.message);
            }
        }
    });
}

/**
 *@date:2015-05-05
 *@Modify-lastdate :2015-05-13
 *@author :Green.Ge
 *@describe：设置规格1的事件。
 *@param: spec 规格代码 "spec2","spec3", 用户拼接一些组件的id如 "showLayer_spec2"
 *@return：null
 *
 */
function enableSpec1Event(){
    var spec = "spec1";
    //规格Tab点击事件
    //$(document).on("mouseover","#showLayer_" + spec + " .standard-t-li",function () {
    $(document).on("click", "input.spec,label.spec", function () {
        if(!checkNsortIsSelectd($(this))){
            return false;
        }
        $(".show-layer").hide();
        $(this).closest('.product-ipt').find('.show-layer').show();
    });

    //规格tab点击事件，显示当前tab内容
    $(document).on("mouseover","#showLayer_" + spec + " .standard-t-li",function () {
        $(this).siblings().removeClass("hover");
        $(this).addClass("hover");
        $(this).parents(".show-layer").find("div[type='specDetail']").hide();
        $(this).parents(".show-layer").find("div[type='specDetail']:eq(" + $(this).index() + ")").show();
        return false;
    });

    //小分类点击事件
    $(document).on("click","#showLayer_" + spec + " .textures-list-first",function () {
        if ($(this).attr("checked")) {
            $(this).removeAttr("checked");
            $(this).parent().find("a").removeClass("hover");
        } else {
            $(this).attr("checked", "checked");
            $(this).parent().find("a").addClass("hover");
        }
        return false;
    });
    //规格明细点击事件
    $(document).on("click","#showLayer_" + spec + " div[type='specDetail'] a",function () {
        if ($(this).hasClass("hover")) {
            $(this).removeClass("hover");
        } else {
            $(this).addClass("hover");
            if ($("#showLayer_" + spec + " div[type='specDetail'] a").length === 1) {
                $("#" + spec + " button.confirm-btn").click();
            }
        }
        return false;
    });
    //规格1确认按钮事件
    $(document).on("click","#" + spec + " button.confirm-btn",function () {
        var array = [];
        var select = $(this).closest('.product-ipt').find("div[type='specDetail'] a.hover");
        $(select).each(function (i, e) {
            array.push($(e).text());
        });
        var others = $(this).closest('.product-ipt').find("#otherspec1").val().split(",");
        var checksuccess = true;
        if(others != '') {
            $(others).each(function (i, e) {
                //验证是数字
                if (!(/^\d+(\.\d+)?$/).test(e)) {
                    utils.showMsg("规格必须是数值", null, "error", 2000);
                    checksuccess = false;
                    return false;
                }
                array.push(others[i]);
            });
        }
        if(checksuccess) {
            array = unique(array);  //数组去重
            var input = $(this).closest('.product-ipt').find("#input" + spec);
            $(input).val(array.join(','));
            $(input).blur();
            $(this).parents(".show-layer").hide();
            resetSingleWeight($(input));
        }
        return false;
    });

    //规格1清除按钮事件
    $(document).on("click","#" + spec + " button.clear-btn",function () {
        $(this).closest('.product-ipt').find("div[type='specDetail'] a").removeClass("hover");
        $(this).closest('.product-ipt').find("#otherspec1").val("");
        //$("#input" + spec).val("");
        //$("#input" + spec).blur();
        return false;
    });
}

/**
 *@date:2015-05-05
 *@Modify-lastdate :2015-05-13
 *@author :Green.Ge
 *@describe：设置规格2,3的事件。
 *@param: spec 规格代码 "spec2","spec3", 用户拼接一些组件的id如 "showLayer_spec2"
 *@return：null
 *
 */
function enableSpec2Spec3Event(spec){
    //规格明细点击事件
    $(document).on("click","#showLayer_" + spec + " a",function () {
        $(this).parents(".textures-con").find("a").removeClass("hover");
        $(this).addClass("hover");
        var input = $(this).closest('.product-ipt').find("#input" + spec);
        $(input).val($(this).text());
        $(this).closest(".show-layer").find(".btn-bar input").val("");
        $(input).blur();
        $(this).parents(".show-layer").hide();
        resetSingleWeight($(input));
        return false;
    });
    //规格2,3确认按钮事件
    $(document).on("click","#showLayer_" + spec + " button.confirm-btn",function () {
        var curDiv = $(this).closest('.product-ipt');
        var from = $(curDiv).find("#from_" + spec), to = $(curDiv).find("#to_" + spec);
        //if (prcsteel.checkInput(from, to)) {
            if ($.trim($(from).val()) === "" && $.trim($(to).val()) === "") {
                $(curDiv).find("#input" + spec).val("");
                $(curDiv).find("#showLayer_" + spec + " a").removeClass("hover");
                $("#showLayer_" + spec).hide();
            } else {
                var minVal , maxVal;
                if ($.trim($(from).val()) !== "" && $.trim($(to).val()) !== "") {//起始结束都不为空
                    minVal = $(from).val();
                    maxVal = $(to).val();
                    if(!$.isNumeric(minVal) || !$.isNumeric(maxVal)) {
                        utils.showMsg("范围必须为数字！", null, '', 'error', 2000);
                        return false;
                    }
                    if(parseInt(minVal) > parseInt(maxVal)) {
                        utils.showMsg("最大值不能小于最小值！", null, '', 'error', 2000);
                        return false;
                    }
                } else if ($.trim($(from).val()) === "") {// 起始值为空
                    minVal = "";
                    maxVal = $(to).val();
                } else {
                    minVal = $(from).val();
                    maxVal = "";
                }
                if(minVal == maxVal){
                    $(this).closest('.product-ipt').find("#input" + spec).val(minVal);
                }else{
                    $(this).closest('.product-ipt').find("#input" + spec).val(minVal + "-" + maxVal);
                }
                $(curDiv).find("#showLayer_" + spec + " a").removeClass("hover");
                if(minVal == "") { minVal = Number.MIN_VALUE;}
                if(maxVal == "") { maxVal = Number.MAX_VALUE;}
                $(curDiv).find("#showLayer_" + spec + " a").each(function () {
                    var val = $(this).text() * 1.0;
                    if (val >= minVal && val <= maxVal) {
                        $(this).addClass("hover");
                    }
                });
                resetSingleWeight($(this));
            }
            $(this).closest('.product-ipt').find("#input" + spec).blur();
            $(this).parents(".show-layer").hide();
        //}
        return false;
    });
    //规格2,3清除按钮事件
    $(document).on("click","#showLayer_" + spec + " button.clear-btn",function () {
        $(this).closest('.form-item').find("#showLayer_" + spec + " a").removeClass("hover");
        $(this).closest('.form-item').find("#input" + spec).val("");
        $(this).closest('.form-item').find("#from_" + spec).val("");
        $(this).closest('.form-item').find("#to_" + spec).val("");
        $("#input" + spec).blur();
        return false;
    });
}

function enableFactoryEvent(){
    //厂家Tab点击事件
    $(document).on("click", "input#factory", function () {
        if(!checkNsortIsSelectd($(this))){
            return false;
        }
        $(".show-layer").hide();
        $(this).closest('.m-search').find('.factory_layer').show();
    });

    //厂家点击事件
    $(document).on("click", ".factory_layer .textures-con span a", function () {
        if ($(this).hasClass("hover")) {
            $(this).removeClass("hover");
        } else {
        	//移除其他选中的
        	$(".textures-con span a").removeClass("hover");
        	//清除输入框的
        	$(this).closest("div.factory").find("input[name=otherFactoryIds]").val("");
            $(this).addClass("hover");
        }
        return false;
    });
    //厂家内确定按钮点击事件
    $(document).on("click", ".factory_layer button.confirm-btn", function () {
        $(".factory_layer").hide();
        totalFactory($(this));
        resetSingleWeight($(this));
        return false;
    });

    /**
     * 统计所有选择的钢厂
     * @param input
     */
    function totalFactory(input){
    	 var value = "", hiddenId = "";
         var factoryDiv =  $(input).closest('div.factory');
         $(factoryDiv).find(".factory_layer a.hover").each(function(){
             value += $(this).html();
             hiddenId += $(this).attr("factoryid");
         });
         var otherFactoryIds = $(factoryDiv).find("input[name=otherFactoryIds]");
         if ($(otherFactoryIds).attr("val") != "" && $(otherFactoryIds).attr("val") != undefined) {
             value += $(otherFactoryIds).val();
             hiddenId += $(otherFactoryIds).attr("val");
         }
         value = value.substring(0, value.length);
         hiddenId =  hiddenId.substring(0, hiddenId.length);
         $(factoryDiv).find('input#factory').val(value);
         $(factoryDiv).find('input#factory').attr("selectIds", hiddenId);
    }

    //厂家内清除按钮点击事件
    $(document).on("click", ".factory_layer button.clear-btn", function () {
        $(this).closest("div.factory").find(".factory_layer a").removeClass("hover");
        $(this).closest("div.factory").find("input[name=otherFactoryIds]").attr("val", "");
        $(this).closest("div.factory").find("input[name=otherFactoryIds]").val("");
        totalFactory($(this));
        return false;
    });

    //厂家内其他输入框输入事件
    $(document).on("input", "input[name=otherFactoryIds]", function () {
        var input = $(this);
        $(this).attr("val", "");
        var factoryDiv = $(this).closest("div.factory");
        //去掉界面上选中的那些仓库
        $(".textures-con span a").removeClass("hover");
        var temp = _factory.slice(0);
        var existsIds = $(factoryDiv).find(".factory_layer a");
        if(existsIds.length>0){
            $(existsIds).each(function () {
                for (var i in temp) {
                    if (temp[i].id == $(this).attr("factoryid")) {
                        temp.splice(i, 1);
                    }
                }
            });
        }
        showPYMatchList(input, temp, 'id', 'name', function(){
            totalFactory($(input));
        });
        $("#dropdown").css("z-index", 9999);
        return false;
    });
}



function enableMaterialEvent() {
    // 绑定输入框focus事件
    $(document).on("click", "input[search='material']", function () {
        if(!checkNsortIsSelectd($(this))){
            return false;
        }
        var input = $(this).closest('.m-search').find('#nsortName');
        if(input.val() != ''){
            $(this).closest('.product-ipt').find('.show-layer').show();
            return false;
        }
        loadMaterial(input);
    });

    //材质点击事件
    $(document).on("click", "#" + materialLayerId + " span a",function () {
        if ($(this).hasClass("hover")) {
            $(this).removeClass("hover");
        } else {
            //检查材质最多只能选10种
            if($(this).closest("#" + materialLayerId).find("span a.hover").length >= 10){
                cbms.alert("最多选择10种材质");
                return false;
            }
            $(this).addClass("hover");
        }
        return false;
    });

    //材质确认按钮事件
    $(document).on("click","#" + materialLayerId + " button.confirm-btn",function () {
        var nameArray = [], idArray = [];
        var select = $(this).closest('.show-layer').find("div.textures-con a.hover");
        $(select).each(function (i, e) {
            nameArray.push($(e).attr("materialname"));
            idArray.push($(e).attr("materialid"));
        });
        var input = $(this).closest('.product-ipt').find("#material");
        $(input).val(nameArray.join(','));
        $(input).attr("materialId", idArray.join(','));
        $(input).blur();
        $(this).parents(".show-layer").hide();
        resetSingleWeight($(input));

        var spec1 = $(this).closest(".m-search").find("#inputspec1").val();
        var spec2 = $(this).closest(".m-search").find("#inputspec2").val();
        var spec3 = $(this).closest(".m-search").find("#inputspec3").val();
        loadSpec(input, spec1, spec2, spec3);
        return false;
    });

    //材质清除按钮事件
    $(document).on("click","#" + materialLayerId + " button.clear-btn",function () {
        $(this).closest('.show-layer').find('div.textures-con a').removeClass("hover");
        $(this).closest('.btn-bar').find('button.confirm-btn').click();
        return false;
    });
}

function checkNsortIsSelectd(o) {
    if($(o).closest(".m-search").find('#nsortName').val() == ""){
        var input = $(o).closest("#m-search").find("input[search='nsort']");
        getNsort($(input));
        return false;
    }else if($(o).closest(".m-search").find('#material').val() == "") {
        var input = $(o).closest("#m-search").find("input[search='nsort']");
        loadMaterial($(input));
        return false;
    }else{
        return true;
    }
}

function enableWeightEvent(){
    $(document).on("input", "input[name='weight']", function(){
        var inputWeight = $(this);
        var singleWeight = $(this).attr("singleWeight");
        var div = $(inputWeight).closest(".m-search");
        if(singleWeight != undefined){
            $(div).find("input[name='quantity']").val(cbms.convertFloat($(inputWeight).val() / $(inputWeight).attr("singleWeight"), 2));
        }else{
            getSingleWeight(div, function(){
                $(div).find("input[name='quantity']").val(cbms.convertFloat($(inputWeight).val() / $(inputWeight).attr("singleWeight"), 2));
            });
        }
    });

    $(document).on("change", "input[name='weight']", function(){
        var quantity = $(this).closest(".m-search").find("input[name='quantity']");
        if($(this).attr("singleWeight") != undefined) {
            $(quantity).val(Math.round($(quantity).val()));
            $(this).val(cbms.convertFloat($(quantity).val() * $(this).attr("singleWeight"), 6));
        }
    });

    $(document).on("input", "input[name='quantity']", function(){
        var quantity = $(this);
        var weight = $(this).closest(".m-search").find("input[name='weight']");
        if($(weight).attr("singleWeight") != undefined) {
            $(this).val(Math.round($(this).val()));
            $(weight).val(cbms.convertFloat($(this).val() * $(weight).attr("singleWeight"), 6));
        }else{
            getSingleWeight($(this).closest(".m-search"), function(){
                $(quantity).val(Math.round($(quantity).val()));
                $(weight).val(cbms.convertFloat($(quantity).val() * $(weight).attr("singleWeight"), 6));
            });
        }
    });
}

function getSingleWeight(div, callback){
    var spec1 = $(div).find("#inputspec1").val();
    var spec2 = $(div).find("#inputspec2").val();
    var spec3 = $(div).find("#inputspec3").val();
    var factory = $(div).find("#factory").attr("selectids");
    if(spec1.indexOf(',') != -1 ||  spec1 == "" ||
        factory == undefined || factory.indexOf(',') != -1 ||  //品规1不能多选或不选，厂家不能多选或不选
        ($(div).find("#inputspec2").length == 1 && (spec2 == "" || spec2.indexOf('-') != -1)) ||  //品规2,3不能为空或者不能是范围
        ($(div).find("#inputspec3").length == 1 && (spec3 == "" || spec3.indexOf('-') != -1))){
        return false;
    }
    var spec = spec1 + ($(div).find("#inputspec2").length == 1 ? ("*" + spec2) : "") + ($(div).find("#inputspec3").length == 1 ? ("*" + spec3) : "");
    $.ajax({
        type: "POST",
        url: Context.PATH + "/smartmatch/categoryweight/selectSingleWeightByParamIds.html",
        data: {
            factoryId: factory,
            categoryUuid: $(div).find("#nsortName").attr("nsortid"),
            norms: spec
        },
        dataType: "json",
        success: function (response) {
            if (response.success) {
                $(div).find("input[name='weight']").attr("singleWeight", response.data);
                if (callback != undefined) {
                    callback();
                }
            }
        },
        error: function (xhr, textStatus, errorThrown) {
        }
    });
}

function resetSpec(o){
    $(o).closest(".m-search").find('.spec').val("");
}

function resetMaterial(o){
    $(o).closest(".m-search").find('#material').val("");
    $(o).closest(".m-search").find('#material').removeAttr("materialId");
}

function resetFactory(o){
    $(o).closest(".m-search").find('#factory').val("");
    $(o).closest(".m-search").find('#factory').removeAttr("selectids");
}

function resetSingleWeight(o){
    $(o).closest(".m-search").find('input[name="weight"]').removeAttr("singleWeight");
}

loadAllNsort();
loadAllFactory();
loadAllAccount();

enableWeightEvent();
enableNsortEvent();
enableMaterialEvent();
enableFactoryEvent();
enableSpec1Event();
enableSpec2Spec3Event("spec2");
enableSpec2Spec3Event("spec3");

//数组去重
function unique(array){
    var res = [];
    var json = {};
    for(var i = 0; i < array.length; i++){
        if(!json[array[i]]){
            res.push(array[i]);
            json[array[i]] = 1;
        }
    }
    return res;
}

function remove(array, val) {
    var index = array.indexOf(val);
    if (index > -1) {
        array.splice(index, 1);
    }
    //return array;
};
