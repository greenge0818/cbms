/**
 * Created by dengxiyan on 2015/8/27.
 * 订单全部tab页
 */

var tradeTable;
jQuery(function ($) {
    initTable();
    initClickEvent();
});

function initTable() {
    var url = Context.PATH + "/kuandao/account/queryOpenedAccount.html";
    tradeTable = jQuery("#dynamic-table").DataTable({
        "sScrollY": "400px",
        "bScrollCollapse": true,
        "iDisplayLength": 50,
        "ajax": {
            "url": url
            , "type": "POST"
            , data: function (d) {
                d.memeberName = $("#memeberName").val();
                d.memeberCode = $("#memeberCode").val();
                d.virAcctNo = $("#virAcctNo").val();
                d.status = $("#status").children('option:selected').val();
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'memeberName'},
            {data: 'memeberCode'},
            {data: 'virAcctNo'}
            ,{data: 'acctNo'}
            , {data: 'idNo'}
            , {data: 'mobile'}
            ,{data: 'status'}
            , {data: 'operate', "sClass": "text-center"}
        ]
        , columnDefs: [
			{
			    "targets": 0, //第几列 从0开始
			    "data": "memeberName",
			    "render": function (data, type, full, meta) {
			    	var text = '';
			    	if(full.modificationStatus == 1 || full.modificationStatus == 3 || full.modificationStatus ==5 || full.modificationStatus ==7){
			    		text = '<span class="text-danger">'+ data +'</span>';
			    	}else{
			    		text = '<span>'+ data +'</span>';
			    	}
			    	return text;
			    }
			},
			{
			    "targets": 4, 
			    "data": "idNo",
			    "render": function (data, type, full, meta) {
			    	var text = '';
			    	if(full.modificationStatus == 4 || full.modificationStatus == 5 || full.modificationStatus == 6 || full.modificationStatus ==7){
			    		text = '<span class="text-danger">'+ data +'</span>';
			    	}else{
			    		text = '<span>'+ data +'</span>';
			    	}
			    	return text;
			    }
			},
			{
			    "targets": 5, 
			    "data": "mobile",
			    "render": function (data, type, full, meta) {
			    	var text = '';
		    		if(full.modificationStatus == 2 || full.modificationStatus == 3 || full.modificationStatus == 6 || full.modificationStatus ==7){
		    			text = '<span class="text-danger">'+ data +'</span>';
			    	}else{
			    		text = '<span>'+ data +'</span>';
			    	}
			    	return text;
			    }
			},
			{
			    "targets": 6, //第几列 从0开始
			    "data": "status",
			    "render": function (data, type, full, meta) {
			    	var text = '';
			        //客户状态  已开户、已绑定、异常
			    	if(full.modificationStatus == 1){
			    		text = '<span>客户名称被修改</span>';
			    	}else if(full.modificationStatus == 2){
			    		text = '<span>款道电话被修改</span>';
			    	}else if(full.status == 3){
		    			text = '<span>客户名称、款道电话被修改</span>';
		        	}else if(full.modificationStatus == 4){
			    		text = '<span>组织机构代码被修改</span>';
			    	}else if(full.modificationStatus ==5){
			    		text = '<span>客户名称、组织机构代码被修改</span>';
			    	}else if(full.modificationStatus == 6){
			    		text = '<span>款道电话、客户名称被修改</span>';
			    	}else if(full.modificationStatus ==7){
			    		text = '<span>客户名称、款道电话、组织机构代码被修改</span>';
			    	}else if(full.status == 0){
		    			text = '<span>已绑定</span>';
		        	}else{
		        		text = '<span>已开户</span>';
		        	}
			    	return text;
			    }
			},
            {
                "targets": 7, //第几列 从0开始
                "data": "operate",
                "render": function (data, type, full, meta) {
                	var btn = '';
                    //根据修改状态modificationStatus展示‘同步’和‘删除’按钮
                	if(full.id){
                    	btn = btn + '<a href="javascript:void(0);" onclick="synchronize('+full.acctId+')">同步</a>';
                    	btn = btn + '&nbsp;&nbsp;&nbsp;<a href="javascript:void(0);" onclick="closeAccount('+full.acctId+')">删除</a>';
                	}
                	return btn;
                }
            }
        ]
    });
}



function initClickEvent() {
    //搜索事件
    $("#queryBtn").click(search);
    //会员查询
    $("#queryAllBtn").click(queryAll);
    //同步所有
    $("#synchronizeAllBtn").click(synchronizeAll);
} 


function search() {
    tradeTable.ajax.reload();
}

function synchronize(acctId){
	if(acctId){
		cbms.confirm('您确定要同步该客户吗？',acctId,function(acctId){
			cbms.loading();
			$.ajax({
	            type: 'post',
	            url: Context.PATH + "/kuandao/account/synchronizeAccount.html",
	            data: {
	            	acctId: acctId
	            },
	            error: function (s) {
	            	cbms.closeLoading();
	            }
	            , success: function (result) {
	            	cbms.closeLoading();
	                if (result && result.success) {
	                    cbms.alert("客户信息同步成功");
	                    search();
	                } else {
	                    cbms.alert(result.data);
	                }
	            }

	        });
		});
	}else{
		cbms.alert('请选择客户！');
	}
	
}

function synchronizeAll(){
		cbms.confirm('您确定要同步所有客户吗？','',function(){
			cbms.loading();
			$.ajax({
	            type: 'post',
	            url: Context.PATH + "/kuandao/account/synchronizeAllToSpdb.html",
	            error: function (s) {
	            	cbms.closeLoading();
	            }
	            , success: function (result) {
	            	cbms.closeLoading();
	                if (result && result.success) {
	                    cbms.alert("客户信息同步完成，详情请看同步日志");
	                    search();
	                } else {
	                    cbms.alert(result.data);
	                }
	            }

	        });
		});
	
}


function queryAll(){
	cbms.confirm('您确定要查询所有已开户的客户吗？','',function(){
		cbms.loading();
		$.ajax({
            type: 'post',
            url: Context.PATH + "/kuandao/account/synchronizeAllToLocal.html",
            error: function (s) {
            	cbms.closeLoading();
            }
            , success: function (result) {
            	cbms.closeLoading();
                if (result && result.success) {
                    cbms.alert("客户信息查询完成");
                    search();
                } else {
                    cbms.alert(result.data);
                }
            }

        });
	});
}


function closeAccount(acctId){
	cbms.confirm('您确定要删除该客户吗？','',function(){
		cbms.loading();
		$.ajax({
            type: 'post',
            url: Context.PATH + "/kuandao/account/closeAccount.html",
            data: {
            	acctId: acctId
            },
            error: function (s) {
            	cbms.closeLoading();
            }
            , success: function (result) {
                if (result && result.success) {
                    cbms.alert("删除客户成功",function(){
                    	window.location.href=Context.PATH + "/kuandao/account/unopenaccount.html";
                    });
                } else {
                    cbms.alert(result.data);
                }
                cbms.closeLoading();
            }

        });
	});
}