/**
 * Created by dq on 2016/01/15.
 * modify by zhoucai@prcsteel.com 2016-3-16
 * 
 * 客户划转
 */
var _dt;
//客户性质用于显示   加工、运输、仓库亮色暂时不做
var _accountTagArray = [{code:1,name:'买',title:"买家客户",class:'buy'}
                        ,{code:6,name:'临',title:"卖家客户",class:'lin'}
                        ,{code:10,name:'代',title:"卖家代运营客户",class:'dai'}
                      /*  ,{code:16,name:'仓库',title:"仓库客户"}
                        ,{code:32,name:'运输',title:"运输客户"}
                        ,{code:64,name:'加工',title:"加工客户"}*/
                        ];
jQuery(function($) {	
	
	queryIsAdmin();	     
	
	$("#queryBtn").click(function () {
	        _dt.ajax.reload();
	  });
	$("#transferTable").on("click", "button[name='transferOrg']", function () {
		 initManager(this.value);
        var currentRow = $(this).closest("tr");
       
        $("#accountId").val(currentRow.attr("accountId"));
        $("#tempName").html(currentRow.find("td:eq(1)").text());
        cbms.getDialog("客户划转",$("#transfer").html());
    });
	$("#transferTable").on("click", "button[name='companyName']", function () {
		initManager(this.value);
        var currentRow = $(this).closest("tr");        
        $("#accountId").val(currentRow.attr("accountId"));
        $("#tempName").html(currentRow.find("td:eq(1)").text());
        cbms.getDialog("客户划转",$("#transfer").html());
    });
    $(document).on("click", "#confirmBtn", function(){
    	transfer();
    });
    
    $(document).on("click", "#cancelBtn", function(){
    	cbms.closeDialog();
    });
    
    $("#transferBtn").click(function () {
    	window.location.href = Context.PATH + "/accountAssign/viewaccountassignlog.html";
    });
   
   

});
//判断是否为管理员 add by zhucai@prcsteel.com 2016-3-16

function queryIsAdmin(){
	//情况下拉框	
	
	$("#org").empty();
	$.ajax({
		url : Context.PATH + "/transfer/queryisadmin.html",
		type : "POST",
		data: {
			
        },
	    success: function (result) {
	    	var isAdmin=result.recordsTotal;	    		
	    	queryManagerLists(isAdmin);	    	
	    }
	});
		
	
}
//查询服务中心列表，并根据角色显示 add by zhucai@prcsteel.com 2016-3-16

function queryManagerLists(isAdmin){
	//情况下拉框	
	$("#org").empty();
	//获取当前客户所属的服务中心
	var currentOrgId=$("#currentOrgId").val();
	var currentOrgName=$("#currentOrgName").val();
	//如果为管理员 isadmin为1,，则展示全部的服务中心，否则只展现当前客户所属服务中心
	if(isAdmin==1){		
		$.ajax({
			url : Context.PATH + "/transfer/querymanagerlists.html",
			type : "POST",
			data: {
				'orgId' : ''
	        },
		    success: function (result) {
		    	if(result.success){
		    		var users = result.data;
		    		if(users!=null &&users.length>0) {	
		    				$("#org").append("<option value=''>"+'-----全部-----'+"</option>");
		    			for (var i = 0; i < users.length; i++) {
		    				$("#org").append("<option value='"+users[i].id+"'>"+users[i].name+"</option>"); 
		    			}		    			
		    		}
		    	}
		    	 
		    }
	        
		});
		
	}else{
		$("#org").append("<option value='"+currentOrgId+"'>"+currentOrgName+"</option>"); 		
	}	
	initTable();
}
//查询待划转服务中心列表，并过滤当前客户归属的服务中心 orgid add by zhucai@prcsteel.com 2016-3-16
function initManager(orgid) {
	$.ajax({
		url : Context.PATH + "/transfer/querymanagerlists.html",
		type : "POST",
		data: {
			'orgId' : orgid
        },
	    success: function (result) {	    	
			var users = result.data;	    	
			if(users!=null &&users.length>0) {
				for (var i = 0; i < users.length; i++) {
					$("#transferOrg").append("<option value='"+users[i].id+"'> "+users[i].name+"</option>"); 
				}
			}
	    	
	    }
	});
}
//add by zhoucai@prcsteel.com 查询划转列表
function initTable() {	
    _dt = $("#transferTable").DataTable({
    	"pageLength": 50, //每页记录数
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "oLanguage": {sUrl: Context.PATH + "/js/DT_zh.txt"}, //自定义语言包
        "ajax": {
            "url": Context.PATH + "/transfer/customertransferdata.html"
            , "type": "POST"
            , data: function (d) {
            	var orglength=$("#org").length;
            	var orgid=$("#org").val();
            	if (orglength > 0) {
            		d.orgId = orgid;
                }
            	//客户划转为
            	d.accountTag=2;
            	
            	if ($("#accountName").val().length > 0) {
                    d.accountName = $("#accountName").val();
                }
            }
        },   
        "columnDefs" : [{
            "targets" : 0, //第一列隐藏
            "data" : null,
            "defaultContent" : ''
        },{
            sDefaultContent: '-', //解决请求参数未知的异常
            aTargets: ['_all']
        }],       
        "aoColumns" : [{
            "sTitle" : '序号',
        }, {
            "sTitle" : "客户名称",
        }, {
            "data":'accountTag',
            "sTitle" : "客户类型",
            "render":renderAccountTag
        }, {
            "data":'orgName',
            "sTitle" : "归属服务中心",
        }, {
            "data":'createdStr',
            "sTitle" : "注册时间",
        }, {
            "data":'statusStr',
            "sTitle" : "状态",
        }, {
            "sTitle" : "操作",
        }
        ],
        
        "fnRowCallback": function (nRow, aData, iDataIndex) {
        	nRow.setAttribute('accountId',aData.id); 
        	var orgId=aData.orgId;
	        $('td:first', nRow).html(iDataIndex + 1);
        	var tempText = '<button name="transferOrg" value="'+orgId+'" class="button btn-xs btn-link confirm">划转服务中心</button>';
        	var companyNameText = '<button name="companyName" value="'+orgId+'" class="button btn-xs btn-link confirm">'+aData.name+'</button>';
        	 $('td:last', nRow).html(tempText);
        	 $('td:eq(1)', nRow).html(companyNameText);
        }
       
    });
}
/**
 * 显示所有的客户性质 如果是某个性质就点亮
 * @param data
 * @param type
 * @param full
 * @param meta
 * @returns {string}
 */
function renderAccountTag(data, type, full, meta){
    var v="";
    var lights = getAccountTagsByCode(data);
    $.each(_accountTagArray,function(index,item){
        var className = $.inArray(item.code, lights) >= 0 ? item.class : '';
        v += "<span title='"+item.title+"' class='taci "+ className +"'>"+item.name+"</span>";
    });
    return v;
}

/**
 * 通过code获得所有包含的客户性质数组
 * @param code
 * @returns {Array}
 */
function getAccountTagsByCode(code){
    var array = [];
    $.each(_accountTagArray,function(index,item){
        if((item.code & code) === item.code){
            array.push(item.code);
        }
    });
    return array;
}
//add descrption  zhoucai@prcsteel.com 执行划转。
function transfer() {
    $.ajax({
        type: 'POST',
        dataType:"JSON",
        url: Context.PATH + "/transfer/customertransferaction.html",
        data: {
    		'orgId' : $("#transferOrg").val(),
    		'orgName' : $("#transferOrg option:selected").text(),
            'accountId' : $("#accountId").val()
        },
        error: function (s) {
            cbms.closeDialog();
        },
        success: function (result) {
            cbms.closeDialog();
            if (result) {
                if (result.success) {
                	cbms.alert("划转成功", function () {
                		_dt.ajax.reload();
                    });
                } else {
                	cbms.alert(result.data);
                }
            } else {
                    cbms.alert("划转失败");
            }
        }
    });
}
