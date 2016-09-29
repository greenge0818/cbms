//全局变量
var _resourcePage = {
	selectedIds:[], //数据行选中id记录集
    dt:null,        //datatables
    oneKeyOprtObj:null //一键操作查询对象
}

$(document).ready(function() {
	//加载服务中心
    $.post(Context.PATH + '/common/allOrganizationList.html','',function(result){
        $("#serviceArea").empty();
        $("#serviceArea").append("<option value=''>-选择服务中心-</option>");
        if(result.success){
            for (var i = 0; i < result.data.length; i++) {
                $("#serviceArea").append("<option value='" + result.data[i].id + "'>" + result.data[i].name + "</option>");
            }
        }
    });	
	
	if(!utils.isEmpty(tabIndex)){
		var index = parseInt(tabIndex);
		$("#myTab4 li").removeClass("active");
		$("#myTab4").find("li:eq("+index+")").addClass("active");
	}
	reloadQueryObj();
	
    var url = Context.PATH + "/resource/base/search.html";
    _resourcePage.dt = jQuery("#dynamic-table").DataTable({
        "processing" : false,
        "serverSide" : true,
        "searching" : false,
        "ordering" : false,
        "paging" : true,
        //"bInfo" : false,
        "autoWidth": false,
		"iDisplayLength" : 50,
		"aLengthMenu" : [10,30,50,100],//定义每页显示数据数量
        "ajax" : {
            "url" : url,
            "type" : "POST",
            data : function(d) {
                return $.extend({}, d, _resourcePage.oneKeyOprtObj);
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
         
        },
        columns : [
         
            {data : 'accountName'},
            {data : 'categoryName'},
            {data : 'materialName',"mRender":function(e,t,f){return exceptionRender(e,f.materialUuid);}},
            {data : 'spec',"mRender":function(e,t,f){return exceptionRender(e,f.spec);}},
            {data : 'factoryName',"mRender":function(e,t,f){return exceptionRender(e,f.factoryId);}},
          
            {data : 'warehouseName',"mRender":function(e,t,f){return exceptionRender(e,f.warehouseId);}},
            {data : 'weightConcept'},
            {data : 'quantity'},
            {data : 'weight',"mRender":function(e,t,f){
            	if(e!=null){
            		return e.toFixed(3);
            	}else{
            		return e;
            	}
        	}},
            {data : 'price', "sClass": "text-right","mRender":function(e,t,f){
	            	if(e!=null){
	            		if(e==99999){
	                		return "电话议价"
	                	}else{
	                		return e.toFixed(2)
	                	}	
	            	}else{
	            		return '';
	            	}
            	
            	}
        	},
           
            {data : 'mgtLastUpdated',    "mRender":function(e,t,f){
            	return dateFormat(new Date(f.mgtLastUpdated), "yyyy-MM-dd hh:mm:ss");
            	}
            },
            {data : 'sourceType',"mRender":function(e){
            	if(e=='daily_common'){ return '日常资源';}
            	else if(e=='inquiry'){ return '询价资源';}
            	else if(e=='history_transaction'){ return '历史资源';}
            }}
            
            
        ],

        columnDefs: [
						{
						    "targets": 9, //第几列 从0开始
						    "data": "price",
						    "render": renderAmount
						},
		                {
		                    sDefaultContent: '', //解决请求参数未知的异常
		                    aTargets: ['_all']

		                }
		         ] ,

        "drawCallback": function(e,t) {
        	var status=$("#myTab4 li.active input").val();
        	if(status==2){   //历史成交tab隐藏多选
        		$("#dynamic-table th:eq(0)").hide();
        		$("#dynamic-table tr").each(function(){
        			$(this).find("td:first").hide();
        		})
        	}else{
        		$("#dynamic-table th:eq(0)").show();
        	}
        	//modify by caosulin@prcsteel.com 待审核显示字段 所在城市和跌涨
        	if(status=='0'){
        		$("#dynamic-table th:eq(6)").show();
        		$("#dynamic-table th:eq(12)").show();
        		$("#dynamic-table th:eq(14)").show();
        	}else {
        		$("#dynamic-table th:eq(6)").hide();
        		$("#dynamic-table tr").each(function(){
        			$(this).find("td:eq(6)").hide();
        		});
        		$("#dynamic-table th:eq(12)").hide();
        		$("#dynamic-table tr").each(function(){
        			$(this).find("td:eq(12)").hide();
        		});
        		$("#dynamic-table th:eq(14)").hide();
        		$("#dynamic-table tr").each(function(){
        			$(this).find("td:eq(14)").hide();
        		});
        	}
            updateCountByStatus();
        }
    });

   
	//清空按钮
	$("#cleanSearch").on("click", function () {
		resetForm($("form.form-inline"));
	});

	
	
	
	jQuery("#searchList").on(ace.click_event, function() {
		cbms.loading();
		//重构查询对象
		reloadQueryObj();
        searchData(true,true);
        
    });
	
	
	
});


function sourceType(){
	
}


/**
 * 更新tab数量值
 * 资源审核总数量，需要修改
 */
function updateCountByStatus(){
	$.ajax({
        type: 'post',
        url: Context.PATH + "/resource/getCountByStatus.html",
        success: function (result) {
        	if(result.success){
        		var allCount=result.data.allCount|0;
        		var listedCount=result.data.listedCount|0;
        		var unListedCount=result.data.unListedCount|0;
        		var exceptionCount=result.data.exceptionCount|0;
        		var historyTransactionCount=result.data.historyTransactionCount|0;
        		var inqueryCount = result.data.inqueryCount|0;
        		$("#myTab4 li span")[5].innerText="（"+allCount+"）";//全部资源
        		$("#myTab4 li span")[1].innerText="（"+listedCount+"）";//日常资源
        		$("#myTab4 li span")[0].innerText="（"+unListedCount+"）";//未审核资源
        		$("#myTab4 li span")[3].innerText="（"+historyTransactionCount+"）";//历史成交
        		$("#myTab4 li span")[4].innerText="（"+exceptionCount+"）";//异常资源
        		$("#myTab4 li span")[2].innerText="（"+inqueryCount+"）";//询价资源
        	}
        }
    });
}
/**
 * 异常数据加红渲染
 * @param t  内容
 * @param id 校验id
 * @returns {String}
 */
function exceptionRender(t,id){
	var res="";
	if(utils.isEmpty(id)){
		res="<span class='green'>"+t+"</span>";
	}else{
		res=t;
	}
	return res;
}
/***
 * 重新加载查询对象
 */
function reloadQueryObj(){
	var upStart=$("#upStart").val();
	//写死查询起始时间，取项目开始时间:2015年11月1日
	if(utils.isEmpty(upStart)){
		upStart='2015-11-1 00:00';
	}
	_resourcePage.oneKeyOprtObj={
			 accountName:$.trim($("#name").val()),
		     warehouseName:$.trim($("#warehouseName").val()),
		     lastUpdatedBy:$.trim($("#lastUpdatedBy").val()),
		     categoryName:$.trim($("#cagetoryName").val()),
		     materialName:$.trim($("#materialName").val()),
		     spec:$.trim($("#spec").val()),
		     factoryName:$.trim($("#factoryName").val()),
		     area:$.trim($("#areaName").val()),
		     sourceType:$("#sourceTypes").val(),
		     upStart:upStart,
		     upEnd:$("#upEnd").val(),
		     statusNum:$("#myTab4 li.active input").val(),
		     orgId:$("#serviceArea").val()
		}
}

function dateFormat(date, fmt) {
	var o = {
		"M+": date.getMonth() + 1,                 //月份
		"d+": date.getDate(),                    //日
		"h+": date.getHours(),                   //小时
		"m+": date.getMinutes(),                 //分
		"s+": date.getSeconds(),                 //秒
		"q+": Math.floor((date.getMonth() + 3) / 3), //季度
		"S": date.getMilliseconds()             //毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
	for (var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}
function searchData(isNewSearch,isAddFlower) {
	if(isNewSearch){
		_resourcePage.dt.ajax.reload(function(){
			closeLoadding(isAddFlower);
		});
	}else{
		_resourcePage.dt.ajax.reload(function(){
			closeLoadding(isAddFlower);
		},false);
	}
}

function closeLoadding(isAddFlower){
	if(isAddFlower){
		cbms.closeLoading();
	}
}