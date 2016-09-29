var _templetAttr={
	table:null,
	correctData:[],//正确的excel数据
	data:[],
	submitLimit:100   //单次上传资源总数
};
	_templetAttr.table = $('#dynamic-table').dataTable({
		'language': {
			"emptyTable":"没有数据",
		},
	    "bLengthChange": false,
	    "scrollY": "400px",
	    "scrollCollapse": true,
	    "paging": false,
	    "bPaginate": false, //翻页功能
	    "bLengthChange": false, //改变每页显示数据数量
	    "bFilter": false, //过滤功能
	    "bSort": false, //排序功能
	    "bInfo": false,//页脚信息
	    "bAutoWidth": false,//自动宽度
	    "data":_templetAttr.data,
	    columns: [
	        {data:"errorMsg",
	    	"mRender":function(data,type,full){//通过回调函数添加html
	    		if(!utils.isEmpty(data)){
	    			return "<span style='cursor:pointer;' class='icon err-batch-icon' title='"+data+"'></span>";
	    		}
	        }},
	        {data: 'accountName'},
	        {data: 'categoryName'},
	        {data: 'materialName'},
	        {data: 'normName',
	        "mRender":function(data,type,full){
        		return $.makeArray(data).join("*");
            }},
	        {data: 'factoryName'},
	        {data: 'warehouseName'},
	        {data: 'weightConcept'},
	        {data: 'quantity'},
	        {data: 'weight'},
	        {data: 'price'},
	        {data: 'remark'}
	    ],
	    columnDefs: [
	         {
	            sDefaultContent: '', //解决请求参数未知的异常
	            aTargets: ['_all']
	        }
	    ] 
	});
    $("#uploadFile").change(function(){
        var v = $(this).val();
        $("#copyFile").val(v);
    });
    
    $(document).on("click",".standard-model",function(){
    	cbms.confirm("您确定下载标准模板吗?",null,function(){
    		window.open("download.html");
    	});
    });
    //清空解析数据
    $(document).on("click",".clearBtn",function(){
    	if(!utils.isEmpty($("#dynamic-table").dataTable().fnGetNodes())){
			cbms.confirm("您确定要清空当前已解析的数据么？",null,function(){
				_templetAttr.table.fnClearTable();
				_templetAttr.correctData.length=0;
				$(".redF").text(0);
				$(".upload em").text(0);
	    	});
    	}else{
    		cbms.alert("暂无数据,请先添加资源!");
    	}
    });
    
    //返回
    $(document).on("click",".returnBtn",function(){
    	location.href=Context.PATH + "/resource/sort/index.html";
    });
    
    //导入资源并不挂牌
    $(document).on("click",".importAndunListed",function(){
    	doImportData("0");
    });
    
    //导入资源并挂牌
    $(document).on("click",".importAndListed",function(){
    	doImportData("1")
    });
    
    $(document).on("click","#saveFile",function(){
    	if(utils.isEmpty($("#uploadFile").val())){
    		cbms.alert("请先选择要上传的资源文件!");
    		return false;
    	}
    	cbms.loading();
    	//数据清空
    	_templetAttr.correctData.length=0;
    	$(".redF").text(0);
    	$(".upload em").text(0);
    	$("#fileForm").ajaxSubmit({  
            type: 'post',  
            url: "upload.html",  
            dataType:"json",
            success: function(data){
				_templetAttr.table.fnClearTable();
				cbms.closeLoading();
				if(data.success){ 
					_templetAttr.table.fnAddData(data.data, false);
					_templetAttr.table.fnDraw();
					//遍历出没有错误的数据
					_templetAttr.correctData=$.grep(data.data,function(e,i){return utils.isEmpty(e.errorMsg)});
					$(".redF").text(_templetAttr.correctData.length);
				}else{
					cbms.alert(data.data);
				}

            } 
        });
    	return false
    });
    /**
     * 操作列图片组装
     * @param id
     * @returns {String}
     */
	function generateOptHtml(id) {
	    var optHtml = '<div class="fa-hover">';
	    optHtml += "<a href='javascript:goToEdit("+id+")' target='_blank' title='编辑'>";
	    optHtml += "<i class=\"fa fa-edit fa-2x\"></i></a>";
	   	optHtml += " <a href='javascript:goToDel("+id+")' target='_blank' title='删除'><i class=\"fa fa-trash-o fa-fw fa-2x\"></i></a>";
	    optHtml += '</div>';
	    return optHtml;
	}
	/**
	 * 导入资源数据
	 * @param status  资源状态  1:挂牌,0:未挂牌
	 */
	function doImportData(status){
		if(_templetAttr.correctData.length>0){
    		cbms.confirm("您确定要导入当前已解析的数据么？",null,function(){
				$.each(_templetAttr.correctData,function(i,e){ e.status=status;})
				cbms.loading();
				var data=_templetAttr.correctData.slice(0);
				doBatchImport(data);
    		});
    	}else{
    		cbms.alert("暂无数据可导入，请先做解析!");
    	}
	}
	/**
	 * 
	 */
	function remove(rest){
		var j =0;
		$("#dynamic-table tbody tr").each(function(i,e){
			var title = $(e).find("td:first").find("span").attr("title");
			if(!title){
				j++;
				$(e).remove();
			}
			if(j==_templetAttr.submitLimit){
				return false;
			}
		});
		var restTrLength = $("#dynamic-table tbody tr").length;
		if(restTrLength<13){
			$(".dataTables_scrollBody").css("height",(restTrLength*30)+"px");
		}
		
		var percent=(_templetAttr.correctData.length-rest)/_templetAttr.correctData.length;
		percent=Math.round(percent*100)
		$(".upload em").text(percent);
	}
	/**
	 * 以_templetAttr.submitLimit条数据为批次导入
	 * 
	 * @param data  数据源
	 */
	function doBatchImport(data){
		var len=data.length;
		var ndata=new Array();
		if(len>0){
			var divide= len/_templetAttr.submitLimit;
			if(divide>=1){
				//源数据截取_templetAttr.submitLimit条数据
				ndata=data.slice(0,_templetAttr.submitLimit);
				//源数据删除_templetAttr.submitLimit条
				data.splice(0,_templetAttr.submitLimit);
			}else{
				ndata=data.slice(0);
				data.length=0;
			}
			$.ajax({
		        type : "POST",
		        url : Context.PATH + "/resource/templet/importResource.html",
		        data : JSON.stringify(ndata),
		        contentType:"application/json",
		        dataType : "json",
		        success : function(response) {
		            if (response.success) {
		            	remove(data.length);
		            	if(data.length==0){
			            	cbms.closeLoading();
			            	//数据清空
			            	_templetAttr.correctData.length=0;
			            	$("#copyFile").val("");
			            	$("#uploadFile").val("");
			            }
		            	doBatchImport(data);
		            }else{
		            	cbms.closeLoading();
		            	cbms.alert("操作失败!");
		            }
		        }
	        });
		}
		
	}
