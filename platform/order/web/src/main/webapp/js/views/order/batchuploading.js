
/**
 * 批量上传凭证图片
 * @author  wangxiao
 * @date 2016/05/23
 */
var globalTable;

jQuery(function () {
	_tableHeight = ($(window).height()-(400)<400?400:$(window).height()-(400))+"px";
    initEvent();
    initTable();
	//弹出页面查看图片
	$(document).on("click", ".img-box", function () {
		var $img = $(this).next("img"), tit = $img.attr("alt");
		var src = $(this).find("img").attr("src");
		renderImg(src);
	});
    
});

//列表
function initTable() {
    //_globalAttr 全局属性对象已在vm定义
    globalTable= $("#dynamic-table").DataTable({
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "lengthChange": true, //显示pageSize的下拉框 50 100 150
        "pageLength": 50, //每页记录数
        "ajax": {
            "url": Context.PATH + "/order/certificate/getbatchuploading.html",
            "type": "POST",
            data : function (d) {
                d.status=$("#status").val();
                d.credentialCode=$("#credentialCode").val();
                d.code=$("#code").val();
                d.sellerName=$("#sellerName").val();
                d.buyerName=$("#buyerName").val();
                d.type=$("#type").val();
              
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns:[
            {data: ''},        
            {data: 'credentialCode',"mRender":function(e,t,f){return !e||e==''?"-":e}},   
            {data: 'pages'},   			
            {data: 'credentialNum'},   			
            {data: 'code'},  		
            {data: 'accountName'},   		
            {data: 'sellerName'},   		
            {data: 'type'},  			
            {data: 'uploadStatus'},  			
            {data: ''},			
          			
        ]  , fnRowCallback: function (nRow, aData, iDataIndex) {
           var imgHtml = "<img src='"+Context.PATH+"/common/getfile.html?key="+aData.fileUrl+"'  alt=''  width='50' height='50' />";
            $('td:eq(0)', nRow).html(imgHtml);
			if(aData.type=="buyer"){aData.type="买家凭证"}
			if(aData.type=="seller"){aData.type="卖家凭证"}
            $('td:eq(7)', nRow).html(aData.type);
            var status =""
            if(aData.uploadStatus=="PENDING_REVISION"){status="待校对"}
            if(aData.uploadStatus=="PENDING_COLLECT"){status="已校对待集齐"}
             if(aData.uploadStatus=="ALREADY_COLLECT"||aData.uploadStatus=="OLD_DATA"){status="已集齐"}
            $('td:eq(8)', nRow).html(status);
            var collect=""
            if(aData.uploadStatus=="ALREADY_COLLECT"||aData.uploadStatus=="OLD_DATA"){
            	collect='<br><a href="javascript:;" class="btn btn-link submit" codeId="'+aData.id+'" num="'+aData.credentialNum+'"  code="'+aData.credentialCode+'">提交审核</a>'
            }
            var operation='<a href="javascript:;" class="btn btn-link operation" codeId="'+aData.id+'" num="'+aData.credentialNum+'"  code="'+aData.credentialCode+'">编辑</a>'+collect;
            $('td:eq(9)', nRow).html(operation);

            return nRow;
        }
        , columnDefs: [
            {
            	
                sDefaultContent: '', //解决请求参数未知的异常
                aTargets: ['_all']

            }
        ]
        ,"scrollY": _tableHeight
    });
}


function initEvent(){

    $("#queryBtn").on("click",function(){
    	globalTable.ajax.reload();
    });
    $("#batchBtn").on("click",function(){
 
    })
  
}


$(document).on("click",".submit",function(){
	   $.ajax({
	        type: 'post',
	        url: Context.PATH + '/order/certificate/submitCert.html',
	        async : false,
	        data: {
	            status : 'PENDING_APPROVAL',
	            certid : $(this).attr("codeId"),
	        },
	        error: function (s) {
	        }
	        , success: function (result) {
	               if(result && result.success){
	            	   cbms.alert("提交风控人员审核成功！");
	            	   globalTable.ajax.reload();
	              }else{
	            	  cbms.alert(result.data);
	              }
	        }
	    });
	
})
$(document).on("click",".operation",function(){
	   var num=$(this).attr("num");  
	   var code=$(this).attr("code");
	   $.ajax({
	        type: 'post',
	        url: Context.PATH + "/order/certificate/getorderattachment.html",
	        async : false,
	        data: {
	        	id :$(this).attr("codeId"),
	        	code:$(this).attr("code")
	        },
	        error: function (s) {
	        }
	        , success: function (result) {
	               if(result && result.success){
	            	   var tdhtm=''
	            		
	            	   for (i=0;i<result.data.length ;i++ ){
	            		   tdhtm+= "<tr><td> <input type='hidden' class='certificateId'  value='"+result.data[i].id+"'/><a href='javascript:;' class='img-box'>" +
						   "<img src='"+Context.PATH+"/common/getfile.html?key="+result.data[i].fileUrl+"'  alt=''  width='50' height='50' /></a></td>"+
	   	           		"<td>"+code+"</td><td>"+num+"</td><td><input type='text'  class='pageNumber' value='"+result.data[i].pageNumber+"' must='1' class='ipt-text  ' id='' name='version' size='20'/></td> "+
		           		"<td><a href='javascript:;' imgId='"+result.data[i].id+"'  class='btn btn-link  del_batch_img' >删除</a></td></tr>";
	            	   }
	            	   var html="<div><div><table id='edit_voucher'  class='contable table'   style='width: 800px'><tbody class='text-center'><tr><td>凭证图片</td><td>凭证号码</td><td>凭证总页码</td><td>凭证当前页码</td><td>操作</td></tr>"+
	            	   tdhtm+
	           		"</table></div><div class='btn-bar _text-center'><button type='button' class='btn btn-info btn-sm'  id='edit_voucher_shut'>确定</button>&nbsp;"+
	           		"<button type='button' class='btn btn-default btn-sm'  id='window_close'>取消</button></div></div></tr><p></p>";
	           	cbms.getDialog("编辑凭证",html);
	              }else{
		             var html="<div><div><table id='edit_voucher'  class='contable table'   style='width: 800px'><tbody class='text-center'>" +
		             		"<tr><td>凭证图片</td><td>凭证号码</td><td>凭证总页码</td><td>凭证当前页码</td><td>操作</td></tr>"+
		             "<tr><td> <input type='hidden' name='certificateId'  value='"+result.data.id+"'/><a href='javascript:;' class='img-box'><img src='"+Context.PATH+"/common/getfile.html?key="+result.data.fileUrl+"'  alt=''  width='50' height='50' /></a></td>"+
		   	         "<td><input type='text'  value='' must='1' class='ipt-text certificate_code' id='' name='certificateCode' size='20'/></td><td>-</td><td><input type='text'   value='' must='1' class='ipt-text  ' id='' name='pageNumber' size='20'/></td> "+
			         "<td><a href='javascript:;' imgId='"+result.data.id+"'  class='btn btn-link del_batch_img' >删除</a></td></tr>"+
		            "</table></div><div class='btn-bar _text-center'><button type='button' class='btn btn-info btn-sm'  id='edit_voucher_mate'>确定</button>&nbsp;"+
		           	"<button type='button' class='btn btn-default btn-sm'  id='window_close'>取消</button></div></div></tr><p></p>";
		           	cbms.getDialog("编辑凭证",html);
	                 
	              }
	        }
	        

	    });
	
})

$(document).on("click","#edit_voucher_mate",function(){
	  var id=$("input[name='certificateId']").val();
	 var pageNumber=$("input[name='pageNumber']").val();
	 var code=$("input[name='certificateCode']").val();
	 if(id==null){
		 cbms.alert("没有数据！");
         return;
	 }
	   $.ajax({
		    type: 'post',
		    url: Context.PATH + "/order/certificate/matecertificate.html",
		    async : false,
		    data: {
		    	id :id,
		    	code :code,
		    	pageNumber :pageNumber
		    },
		    error: function (s) {
		    }
		    , success: function (result) {
		           if(result && result.success){
		        	   cbms.alert(result.data);
		        	   globalTable.ajax.reload();
		          }else{
		        	  cbms.alert(result.data);
		          }
		    }
		});
})
$(document).on("blur",".certificate_code",function(){
	 var nextTd=$(this).parent().next();
	  
		 var code=$("input[name='certificateCode']").val();
		 
		 if(code==""){
			 cbms.alert("凭证号码不能为空！");
	         return;
		 }
		   $.ajax({
			    type: 'post',
			    url: Context.PATH + "/order/certificate/blurcertificate.html",
			    async : false,
			    data: {
			    	code :code,
			    },
			    error: function (s) {
			    }
			    , success: function (result) {
			           if(result && result.success){
			        	   nextTd.html(result.data)  
			          }else{
			        	  cbms.alert(result.data);
			          }
			    }
			});
})
$(document).on("click","#edit_voucher_shut",function(){
	  var id="";
	 var pageNumber="";

	$(".certificateId").each(function(index){
		id+=$(this).val()+"、";
	})
	$(".pageNumber").each(function(index){
		pageNumber+=$(this).val()+"、";
	})
	 if(id==""){
		 cbms.alert("没有数据！");
         return;
	 }
	
editCertificate(id,pageNumber);
})
$(document).on("click","#window_close",function(){
	cbms.closeDialog();
	globalTable.ajax.reload();
})
$(document).on("click",".del_batch_img",function(){
	 var a=$(this);
	  cbms.confirm("是否确定删除？？", null, function () {
		  delectCertificate(a)
      });
})
function delectCertificate(a){
 
	   $.ajax({
	        type: 'post',
	        url: Context.PATH + "/order/certificate/delectcertificate.html",
	        async : false,
	        data: {
	        	id :$(a).attr("imgId"),
	        },
	        error: function (s) {
	        }
	        , success: function (result) {
	               if(result && result.success){
	            	   cbms.alert(result.data);
	            	   a.parent().parent().remove();
	              }else{
	            	  cbms.alert(result.data);
	              }
	        }
	    });
}
function editCertificate(id,pageNumber){
	 
	   $.ajax({
    type: 'post',
    url: Context.PATH + "/order/certificate/editcertificate.html",
    async : false,
    data: {
    	id :id,
    	pageNumber :pageNumber
    },
    error: function (s) {
    }
    , success: function (result) {
           if(result && result.success){
        	   cbms.alert(result.data);
        	   cbms.closeDialog();
        		globalTable.ajax.reload();
        		cbms.closeDialog();
          }else{
        	  cbms.alert(result.data);
          }
    }
});
}
 
function refreshTable(){
	 globalTable.ajax.reload();
}
	

