/**
*
*基价管理
* created by afeng on 2016-06-20
*/

//加载所有城市
$(document).ready(function () {
	$.ajax({
		type: "POST",
		url: Context.PATH+ '/common/allcity.html',
		dataType: "json",
		success: function (response, textStatus, xhr) {
			Context.CITYDATA=response;
		}
	});
});

//地区提示
$(document).on('input propertychange',"input[search='city']", function (e) {
	var input = this;
	showPYMatchList($(input),Context.CITYDATA,"name","name",null);
	cbms.stopF(e);
});

$(document).ready(function() {
	$(document).on("click", "#add_base_price", function ()  { 
		var html = $('#determine').html();
		cbms.getDialog("添加基价", html);
		$("#region").val($("#city").val());
		
		$(document).on("click", "#btnClose",function () {
	       cbms.closeDialog();
	    });
		 
		$(document).off('click', '#btncommit');
			
		$(document).on('click', '#btncommit', function() {
			$(this).attr("disabled", true);
			var forms = setlistensSave("#form-horizontal");
			if (!forms)return;
			$.ajax({
				type: 'post',
				url: Context.PATH + '/smartmatch/quote/savebaseprice.html',
				data: {
					"city": $("#region").val(),//地区
					"basePriceName": $("#baseName").val(),//基价名称
					"basePriceType": $("#typeName").val(),//基价类别
					"category": $("#category").val(),//品名
					"material": $("#material").val(),//材质
					"spec": $("#spec_dia").val(),//规格
					"factory": $("#factory").val(),//厂家
					"price": $("#curdayPrice").val(),//最新报价
					"remark": $("#edit_txtremark").val() //备注
				},
				success: function (result) {
					$("#btncommit").attr("disabled", false);
					if (result && result.success) {
						cbms.closeDialog();
	                	cbms.alert("提交成功！", function() {
							dt.ajax.reload();
	        			});
	                } else {
	                    cbms.alert(result.data);
	                }
				},
				error: function(){
					$("#btncommit").attr("disabled", false);
				}
		 	});
		});
	});

	$(document).on("click", "#releaseBtn", function ()  {
		var html = Context.PATH + '/smartmatch/quote/release.html';
		cbms.getDialog("发布报价", html, null, fillDataTable, 1);

		$(document).on("click", "#releaseClose",function () {
	       cbms.closeDialog();
	    });

		$(document).off('click', '#releaseCommit');

		$(document).on('click', '#releaseCommit', function() {
			cbms.loading();
			//发布报价：
			var forms = setlistensSave("#form-horizontal");
			if (!forms){
				cbms.closeLoading();
				return;
			}



			//统计选中的信息
			var basePriceIds = []; //基价ID集合
			var checked = $("input[name='check']:checked").not("#allCheck");
			var releaseDateList= [];
			$(checked).each(function () {
				var id = "";
				var price = "";
				var releaseDate = {
					id : $(this).val(),
					price : $(this).closest("tr").find(".release_base").val()
				}
				releaseDateList.push(releaseDate);
				var basePriceId = $(this).val();
				basePriceIds.push(basePriceId);
			});
			if (basePriceIds == null || basePriceIds.length == 0) {
				cbms.closeLoading();
	            cbms.alert("请选择要发布的信息");
	            return;
	        }
			$.ajax({
		        url: Context.PATH + '/smartmatch/quote/releasequote.html',
		        type: "POST",
		        data : {
					"releaseDateList" : JSON.stringify(releaseDateList)
				},
		        success: function (result) {
		    		if (result.success) {
		    			cbms.alert("发布成功！", function() {
		    				location.reload();
		    			});
		    		} else {
		    			cbms.alert(result.data);
		    		}
					cbms.closeLoading();
		        }
		    });
		});
	});
	
	// 全选/全不选
	$(document).on('click', '#allCheck', function() {
		var checked = $(this).is(':checked');
        // 取消全选
        if (!checked) {
            $("input[name='check']").removeAttr("checked");
            $(this).removeAttr("checked");
            $("input[name='check']").closest("tr").find(".release_base").removeAttr("verify");
            $("input[name='check']").closest("tr").find(".release_base").removeAttr("must");
        }
        else {
            $("input[name='check']").prop('checked', true);
            $("input[name='check']").closest("tr").find(".release_base").attr("verify","rmb");
            $("input[name='check']").closest("tr").find(".release_base").attr("must","1");
            $("input[name='check']").prop('checked', true);
        }

        //basePriceTotal();
	});
	
	// 单选
    $("body").on("click", "input[name='check']", function () {
        var checked = $(this).is(':checked');
        if (!checked) {
            $(this).removeAttr("checked");
            $("#allCheck").removeAttr("checked");   // 取消全选
            $(this).closest("tr").find(".release_base").removeAttr("verify");
            $(this).closest("tr").find(".release_base").removeAttr("must");
        }
        else {
            $(this).prop('checked', true);
            $(this).closest("tr").find(".release_base").attr("verify","rmb");
            $(this).closest("tr").find(".release_base").attr("must","1");
        }
        
        //basePriceTotal();
    });
	
	var basePriceIds; //基价ID集合
	//统计选中的基价信息
	function basePriceTotal() {
		basePriceIds = [];
		var checked = $("input[name='check']:checked").not("#allCheck");
		$(checked).each(function () {
			var id = $(this).val();
			basePriceIds.push(id);
		});
	}
});

var pub_dt = "";
function fillDataTable() {
	pub_dt = $('#release-table').DataTable({
		ajax: {
			url: Context.PATH + '/smartmatch/quote/getbaseprice.html',
			type: "POST",
			data: function (d) {
				return $.extend({}, d, {
					city:$("#cityName").find("option:selected").text() == "全部" ? null : $("#cityName").find("option:selected").text(),
					basePriceName:$("#basePriceName").val(),
					basePriceType:$("#typeName").val(),
					release:"true"
				});
			}
		},
		serverSide: true, //服务模式
		processing: true,//显示数据加载进度
		searching: false, //是否启用搜索
		ordering: false, //是否启用排序
		lengthChange: false, //不显示pageSize的下拉框
		oLanguage: {sUrl: Context.PATH + "/js/DT_zh.txt"}, //自定义语言包
		bFilter: false,
		bInfo: false,
		iDisplayLength: 15,
		bLengthChange: false, //不显示每页长度的选择条
		bPaginate: false,  //不显示分页器
		fnRowCallback: function (nRow, aData, iDataIndex) {
			$('td:eq(0)', nRow).html("<input type='checkbox' name='check' value='" + aData.id + "'>");
			var link = "<input id='basePrice' class='release_base' type='text'  basePriceId ="+aData.id+"/>";
			if (parseFloat(aData.lowestPrice) > parseFloat(0)) {
				//如果是整数则直接放数字
				var price_value = "";
				if(parseInt(aData.lowestPrice)==aData.lowestPrice){
					price_value = aData.lowestPrice;
				}else{
					price_value = aData.lowestPrice.toFixed(2);
				}

				link = "<input id='basePrice' class='release_base' type='text' value="+ price_value +" basePriceId ="+aData.id+"/>";
			}
			$('td:eq(-1)', nRow).html(link);
			return nRow;
		},
		columns: [
			{data: 'id'},
			{data: 'basePriceName'},
			{data: 'city'},
			{data: 'category'},
			{defaultContent: ''}
		],
		columnDefs: [

		]
	});
}
