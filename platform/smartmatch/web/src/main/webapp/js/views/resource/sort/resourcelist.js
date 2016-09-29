/**
 * create by peanut on 15-12-3
 */
//资源全局变量
var _resAttr={
	//卖家数据
	accountCacheData:[],
	//品名数据
	nsortCacheData:[],
	//厂家数据
	factoryCacheData:[],
	//仓库数据
	warehouseCacheData:[]
	
};

/**
 * 获取一些通用的全局数据：所有卖家、钢厂、仓库、品名数据
 */
(function getCommonData(){
	$.ajax({
        type: 'post',
        url: Context.PATH + "/resource/getCommonData.html",
        success: function (result) {
        	if(result.success){
        		_resAttr.accountCacheData=result.data.accountList;
        		_resAttr.factoryCacheData=result.data.factoryList;
        		_resAttr.warehouseCacheData=result.data.warehouseList;
        		_resAttr.nsortCacheData=result.data.nsortList;
        		loadSortName(_resAttr.nsortCacheData);
        	}
        }
    });
})();

(function() {

	$('.seller-ipt').inputFocus();
	$('.nsortName').inputFocus();
	$('.material').inputFocus();
	$('.factory').inputFocus();
	$('.warehouse').inputFocus();
	$('.norms').inputFocus();
	
	 /**
     * 点击弹层以外的地方会使弹层消失
     */
    $("body").click(function (event) {
        var t = event.target;
        if ($(t).closest('.form-item').length > 0) {
            return;
        }
        $(".show-layer").hide();
    });
	var setFoucsFirst = function(o) {
		if ($(o).closest(".m-search").find('.nsortName').val() == "") {
			$(o).closest(".m-search").find(".nsortName").focus();
			$(o).closest(".show-layer").hide();
			return true;
		}
	};
	// 卖家输入框点击事件
	$(document).on('input propertychange','#account', function () {
        showPYMatchList($(this),_resAttr.accountCacheData,"id","name",null);
        $("#dropdown").css("z-index",9999);
    });
	// 厂家输入框点击事件
	$(document).on('input propertychange','#facotory', function () {
		showPYMatchList($(this),_resAttr.factoryCacheData,"id","name",null);
		$("#dropdown").css("z-index",9999);
	});
	// 仓库输入框点击事件
	$(document).on('input propertychange','#warehouse', function () {
		showPYMatchList($(this),_resAttr.warehouseCacheData,"id","name",null);
		$("#dropdown").css("z-index",9999);
	});
	
	/*品名输入框获取焦点 */
	$(document).on("focus", ".nsortName", function() {
		 setLayerPosition($(this), $("#showLayer_nsort"));
		 controlLayerShow($("#showLayer_nsort"));
		 $("#showLayer_nsort").css("z-index",9999);
		 //隐藏其他层级
		 $("#showLayer_material").hide();
		 $("#showLayer_spec3").hide();
	});
	/*材质输入框获取焦点 */
	$(document).on("focus", ".material", function() {
		if (setFoucsFirst(this))
			return;
		 loadMaterial($(this).closest(".m-search").find(".nsortName"));
		 loadNorms($(this).closest(".m-search").find(".nsortName"));
		 
		 setLayerPosition($(this), $("#showLayer_material"));
		$("#showLayer_material").css("z-index",9999);
		$("#showLayer_material").show();
		//隐藏其他层级
		 $("#showLayer_nsort").hide();
		 $("#showLayer_spec3").hide();
	});
	/*规格输入框获取焦点 */
	$(document).on("focus", ".inputspec3", function() {
		if (setFoucsFirst(this))
			return;
		loadNorms($(this).closest(".m-search").find(".nsortName"));
		setLayerPosition($(this), $("#showLayer_spec3"));
		$("#showLayer_spec3").css("z-index",9999);
		$("#showLayer_spec3").show();
		//隐藏其他层级
		 $("#showLayer_nsort").hide();
		 $("#showLayer_material").hide();
		 
		 
		
	});
	// 绑定选择品名单击事件
    $(document).on("click", "#showLayer_nsort"+ " span a", function () {
    	//规格数据
    	var normsData=$(this).attr("spec");
    	$(".nsortName").attr("spec",normsData)
        var nsortId = $(this).attr("nsortid");
        var nsortName = $(this).text();
        $(".nsortName").attr("nsortid", nsortId);
        $(".nsortName").val(nsortName);
        $("#showLayer_nsort").hide();
        loadPriceLimit(nsortId,$("#price"));

        //联动材质，规格
        resetMaterial(".material");
        resetSpec(".inputspec3");
        
        loadMaterial($(".nsortName"));
        loadNorms($(".nsortName"));
        
        setLayerPosition($(".material"), $("#showLayer_material"));
        setLayerPosition($(".inputspec3"), $("#showLayer_spec3"));
    });

	//鼠标滑过大类，显示大类下的小类
	var t;
	$(document).on("mouseover", ".product-t-li", function() {
		var tab = this;
		t = setTimeout(function() {
			$(tab).parent().find('li').removeClass('hover');
			$(tab).addClass('hover');
			$(tab).closest(".show-layer").find('div[name="nsort"]').hide();
			$(tab).closest(".show-layer").find('div[name="nsort"]:eq(' + ($(tab).index()) + ')').show();
			return false;
		}, 200);
	});
	
	$(document).on("mouseout", ".product-t-li", function() {
		clearTimeout(t);
	});

	//选择材质
	$(document).on("click", ".caizhi a", function(event) {
		$(this).closest(".show-layer").find("a").removeClass("hover");
		$(this).addClass("hover");
		var v = $(this).text();
		var materialId = $(this).attr("materialId");
		$(".material").val(v);
		$(".material").attr("materialId",materialId);
		$(".show-layer").hide();
		$('.material').inputFocus("选择材质");
		$('.material').blur();
		cbms.stopF(event);
		//规格显示
		setLayerPosition($(".inputspec3"), $("#showLayer_spec3"));
		$("#showLayer_spec3").show();
		$("#showLayer_spec3").css("z-index",9999);
	});

	//点击规格按钮
	$(document).on("click", "#showLayer_spec3 button.confirm-btn", function(event) {
		if (setlistensSave("#showLayer_spec3 form")) {
			var ipt = $("#showLayer_spec3 .guiIpt-bar").find('input[type="text"]'), v = "";
			ipt.each(function(i) {
				if (i > 0) {
					v += "*" + ipt.eq(i).val();
				} else {
					v += ipt.eq(i).val();
				}
			});
			$("#inputspec3").val(v);
			$("#showLayer_spec3").hide();
			return false;
		}
		return false;
	});
	
	/***** 资源保存****/
	$(document).on("click", "#saveBtn", function(event) {
		if (setlistensSave("#resourceForm")) {
			var spec=$("#inputspec3").val();
			var bol=false;
			$(spec.split("*")).each(function(i,e){
				if(utils.isEmpty(e)){
					bol=true;
				}
			});
			if(bol){
				cbms.alert("请输入正确的规格数据!");
				return false;
			}
			var status=$("#myTab4 li.active input").val();
			
			//非异常tab做校验
			if(status!=='-1'){
				if(utils.isEmpty($("#account").attr("val"))
						||utils.isEmpty($("#warehouse").attr("val"))
						||utils.isEmpty($("#facotory").attr("val"))){
					cbms.alert("卖家、厂家或仓库未正确选择!");
					return false;
				}
			}
			
//			if(Number($("#weight").val())<1
//					||Number($("#quantity").val())<1
//					||Number($("#price").val())<1){
//				cbms.alert("仓库、件数或价格不能小于 1");
//				return false;
//			}
			var data={
				id:$.trim($("#resourceId").val()),
				accountId:$("#account").attr("val"),
				categoryUuid:$("#nsortName").attr("nsortid"),
				materialUuid:$("#material").attr("materialid"),
				sourceType:$("#sourceType").val(),
				spec:spec,
				factoryId:$("#facotory").attr("val"),
				factoryName:$("#facotory").val(),
				warehouseId:$("#warehouse").attr("val"),
				warehouseName:$("#warehouse").val(),
				weightConcept:$("#weightConcept").val(),
				weight:$("#weight").val(),
				quantity:$("#quantity").val(),
				price:utils.isEmpty($("#price").val())?99999:$("#price").val(),
				remark:$("#remark").val(),
				status:utils.isEmpty($("#myTab4 li.active input").val())?"0":$("#myTab4 li.active input").val()
			};

			var pricemin=$("#price").attr("min");
			var pricemax=$("#price").attr("max");
			if (pricemin == '' || pricemax == '' || isNaN(pricemin) || isNaN(pricemax)) {
				cbms.alert("未查询到参考价范围");
				return false;
			}
			else if(parseFloat(data.price)<parseFloat(pricemin)||parseFloat(data.price)>parseFloat(pricemax)){
				if(!cbms.confirm("修改后的价格不在该品种参考价范围内，确定要保存吗?",data,doSave)){
					return false;
				}
			}
			doSave(data);

		}
		return false
	});
})();

function doSave(data){
	var url = Context.PATH + "/resource/saveResource.html";

	//提交按钮不可用，防止重复提交
	$("#saveBtn").attr("disabled","disabled");

	$("#resourceForm").ajaxSubmit({
		type: 'post',
		url: url,
		data:data,
		dataType:"json",
		success: function(data){
			if(data.success){
				searchData(false);
			}
			cbms.alert(data.data);
			cbms.closeDialog();
			updateCountByStatus();
			//提交按钮重新可用
			$("#saveBtn").removeAttr("disabled");
		}
	});
}

/**
 * 重置规格
 * @param o
 */
function resetSpec(o){
    $(o).val("");
}
/**
 * 重置材质
 * @param o
 */
function resetMaterial(o){
    $(o).val("");
    $(o).removeAttr("materialId");
}
/**
 * 加载规格数据
 * @returns
 */
function loadNorms(inputBox){
	var showPlace=$("#showLayer_spec3 .textures-con");
	//编辑时规格的数据
	
	var value=$("#inputspec3").val().split("*");
	showPlace.empty();
	var htmlStr="";
	$.ajax({
        type: 'post',
        url: Context.PATH + "/resource/getNorms.html",
        data: {
         	"categoryUuid": $(inputBox).attr("nsortid")
        },
        dataType: "json",
        error: function (s) {
        },
        success: function (data) {
        	if(data.success){
        		$(data.data).each(function(i,e){
        			if(!utils.isEmpty(value[i])){
        				htmlStr+="<div class=\"guiIpt-bar\"><label>"+e.name+" : <input class=\"hou-ipt c-text\" type=\"text\" must=1 value='"+value[i]+"' verify='numeric'>&nbsp;"+e.unit+"</label></div>";
        			}else{
        				htmlStr+="<div class=\"guiIpt-bar\"><label>"+e.name+" : <input class=\"hou-ipt c-text\" type=\"text\" must=1 verify='numeric' >&nbsp;"+e.unit+"</label></div>";
        			}
        			
        		});
        	}
        	htmlStr+="<div class=\"clearfix\"></div></div><div class=\"btn-bar t-c\"><button class=\"confirm-btn\">确认</button></div></div>";
        	showPlace.append(htmlStr);
        }
    });
	
}
/**
 * 加载品名大类数据
 * @param data    品名数据
 */
function loadSortName(data) {
    $("#showLayer_nsort").remove();
    // <!--品名数据层  S-->
    var layerHtml = "<div class='show-layer product-bar none' id='showLayer_nsort'>";
    layerHtml += "<div class='product-t'>";
    layerHtml += "<ul class='product-t-ul'></ul>";
    layerHtml += "</div>";
    layerHtml += "</div>";
    $("body").append(layerHtml);
    // <!--品名数据层 E-->

    if (data != null) {
        var search_sorts = data;
        $(search_sorts).each(function (i, e) {
            //卷板，型材 横向列表
            var li = '<li class="product-t-li ' + (i === 0 ? 'hover' : '') + '"><a href="javascript:;" sortId="' + e.sortID + '">' + e.sortName + '</a>'
                + '<em class="icon down-redArr-icon"></em></li>';
            $("#showLayer_nsort .product-t .product-t-ul").append(li);//'<a href="javascript:void(0)" sortId="'+e.sortID+'"><font>'+e.sortName+'</font></a>');
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
                        rowSize = 6,
                        nsortPkg = "";
                    $(f.nsort).each(function (k, g) {
                        //每N行换行div
                        if (k % rowSize === 0) {
                            nsortPkg += '<div class="product-con-bar-list">';
                        }
                        nsortPkg += "<span><a nsortId='" + g.nsortID + "' href='javascript:;' spec='"+ JSON.stringify(g.specName)+"'>" + g.nsortName + '</a>'
                        +'</span>';
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
            $("#showLayer_nsort").append(classDiv);

        });

        $("div[name='nsort']:gt(0)").hide();
    } else {
        cbms.alert("加载品名大类发生错误:" + data.message);
    }
}

/**
 * 加载材质并显现
 * @param inputBox
 */
function loadMaterial(inputBox) {
    var showPlace = $("#showLayer_material");
    $.ajax({
        type: 'post',
        url: Context.PATH + "/smartmatch/categoryweight/getMaterials.html",
        data: {
        	categoryUuid: $(inputBox).attr("nsortid")
        },
        dataType: "json",
        error: function (s) {
        },
        success: function (data) {
            $(showPlace).find(".caizhi-con").empty();
            var rowSize = 6, content = "";
            $(data).each(function (i, e) {
                if (i % rowSize === 0) {
                    content += '<div class="textures-con-bar-list bder-b-dashed">';
                }
                content += '<span><a href="javascript:;" materialId ="' + e.uuid + '" materialName="' + e.name + '">' + e.name + '</a></span>';
                if ((i + 1) % rowSize === 0 || (i + 1) === data.length) {
                    content += '</div>';
                }
            });
            $(showPlace).find(".caizhi-con").append($(content));
            $(showPlace).show();
        }
    });
}
