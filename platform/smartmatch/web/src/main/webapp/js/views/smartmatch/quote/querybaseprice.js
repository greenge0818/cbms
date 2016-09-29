/**
 * Created by lixiang on 2016/06/21.
 */

var dt = "";
var has_delete_base_btn = true;
function InitBasePriceTable() {
    dt = $('#dynamic-table').DataTable({
        ajax: {
            url: Context.PATH + '/smartmatch/quote/getbaseprice.html',
            type: "POST", 
            data: function (d) {
                return $.extend({}, d, {
                	city : $("#cityName").find("option:selected").text() == "全部" ? null : $("#cityName").find("option:selected").text(),
                	basePriceName : $("#basePriceName").val(),
                	basePriceType : $("#typeName").val()
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
			var html = "<a href='javascript:void(0)'  class='update_base' basePriceId =" + aData.id + ">修改</a>";
			if(has_delete_base_btn){
				html += "&nbsp;&nbsp;<a href='javascript:void(0)' class='delete_base' basePriceId =" + aData.id + ">删除</a>";
			}
			$('td:eq(-1)', nRow).html(html);
			if (aData.basePriceType != null)
				addNewTypename(aData.basePriceType);
			return nRow;
        },
        columns: [
			{data: 'basePriceName'},   
			{data: 'city'},
			{data: 'basePriceType'},
            {data: 'category'},  
            {data: 'material', "sClass": "none"},
            {data: 'spec', "sClass": "none"},
            {data: 'factory'},   
            {data : 'lowestPrice', "sClass": "text-right","mRender":function(e,t,f){
	            	if(e!=null&&e!=undefined&&e!='null'){
	            		if(parseFloat(e) == parseFloat(0)){//如果等于0，返回空值
	            			return "";
	            		}
	            		if(parseInt(e)==e){//如果是整数，返回当前整数
	            			return e;
	            		}
	            		
	            		return e.toFixed(2);
	            	}else{
	            		return "";	
	            	}
            	}
        	},
			{data: 'remark'},
            {defaultContent: ''}
        ],
        columnDefs: [

        ]
    });
}

jQuery(function($) {
	InitBasePriceTable();
    $("#dateBtn").click(function () {
        dt.ajax.reload();
    });
});


function deleteBasePrice(o) {
	$.ajax({
        url: Context.PATH + '/smartmatch/quote/deletebaseprice.html',
        type: "POST", 
        data : {
			"basePriceId" : o.basePriceId
		},
        success: function (result) {
    		if (result.success) {
    			cbms.alert("删除成功！", function() {
    				location.reload();
    			});
    		} else {
    			cbms.alert(result.data);
    		}
        }
    });
}

//删除操作
$(document).on("click", ".delete_base", function ()  { 
	var basePriceId = $(this).attr("basePriceId");
	var o = {"basePriceId":basePriceId}
	cbms.confirm("确定删除？", o,deleteBasePrice);
	
});

//getBasePriceTypeByCityId
//类别下拉选根据城市联动
$(document).on("change", "#cityName", function ()  {
	//获取当前的cityId
	var cityId = $("#cityName").val();
	$.ajax({
        url: Context.PATH + '/smartmatch/quote/getBasePriceTypeByCityId.html',
        type: "POST", 
        data : {
			"cityId" : cityId
		},
        success: function (result) {
    		if (result.success) {
    			var types = result.data;
    			$("#typeName").html("");//去掉原来的option
    			$("#typeName").append("<option value=''>全部</option>");  
    			if(types.length > 0){
    				for(var i=0;i<types.length;i++){
    					$("#typeName").append("<option value='"+types[i]+"'>"+types[i]+"</option>");  
    				}
    			}
    			
    		} else {
    			cbms.alert("获取基价类别失败！");
    		}
        }
    });
	
});


//修改操作
$(document).on("click", ".update_base", function ()  {
	var basePriceId = $(this).attr("basePriceId");
	$.ajax({
        url: Context.PATH + '/smartmatch/quote/getbaseprice.html',
        type: "POST", 
        data : {
			"id" : basePriceId
		},
        success: function (result) {
        	var html = $('#determine').html();
    		cbms.getDialog("修改基价", html);
    		
        	$("#basePriceId").val(result.data[0].id);
        	$(".region").val(result.data[0].city);
        	$(".baseName").val(result.data[0].basePriceName);
        	$(".typeName").val(result.data[0].basePriceType);
        	$("#category").val(result.data[0].category);
        	$("#category").attr("readonly","readonly");
        	$("#material").val(result.data[0].material);
        	$("#spec_dia").val(result.data[0].spec);
        	$("#factory").val(result.data[0].factory);
        	if(result.data[0].lowestPrice!=0){
        		$("#curdayPrice").val(result.data[0].lowestPrice);
        	}
			$("#edit_txtremark").val(result.data[0].remark);
        	
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
    					"id" : $("#basePriceId").val(),
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
        }
    });
});

function addNewTypename(typename){
	if ($("#typeName option[value='" + typename + "']").length == 0) {
		$("#typeName").append("<option value='" + typename + "'>" + typename + "</option>");
	}
}


