/**
 * tuxianming on 20160701
 * 入口用户管理
 */
var table, 		//dataTable 对象
	_tableHeight = null;  //列表高度

var data = []; //列表原始数据缓存

var assigned = {};		//已经关联的公司，和页面添加关联，但还没有保存的， 缓存数据
var removeAssigned= [];	//待删除的关联公司

jQuery(function ($) {
	_tableHeight = ($(window).height()-(300)<300?300:$(window).height()-(300))+"px"
	initTable();
    initEvent();
    
});

function initTable() {
	table = $("#dynamic-table").DataTable({
        "pageLength": 50, //每页记录数
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "aLengthMenu": [50,100,150], //这个为选择每页的条目数default [10, 25, 50, 100]
        "lengthChange": true, //是否显示pageSize的下拉框
        "oLanguage": {sUrl: Context.PATH + "/js/DT_zh.txt"},
        "bAutoWidth": false, //是否自动计算表格各列宽度
        "scrollY": _tableHeight,
        "scrollX": true,
        "ajax": {
            "url": Context.PATH + "/accountinfo/potentialcustomer/load.html"
            , "type": "POST"
            , data: function (d) {
            	buildParams(d);
            }
        	//操作服务器返回的数据
	        , "dataSrc": function (result) {
	        	data = result.data;
	            return result.data;//返回data
	        }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'contactId'},
            {data: 'mobile'},
            {data: 'name'},
            {data: 'company'},
            {data: 'regdate'},
            {data: 'origin'},
            {data: 'status'},
            {data: ''}
        ],
        columnDefs: [
				{
				   "targets": 5, //第几列 从0开始
				    "data": "origin",
				    "render": function (data, type, full, meta){
				    	if(full.origin == 'APP')
				    		return '手机';
				    	else if(full.origin == 'MARKET')
				    		return '超市';
				    	else if(full.origin == 'PICK')
				    		return '分检';
				    	else 
				    		return full.origin;

				    }
				},
               {
            	   "targets": 6, //第几列 从0开始
                   "data": "status",
                   "render": function (data, type, full, meta){
                	   return full.status=='1'? "已关联":"未关联"; 
                   }
               },
               {
                   "targets": 7, //第几列 从0开始
                   "data": "",
                   "render": function (data, type, full, meta){
                	   
                	   var edit = "<a href='javascript:;' class='rowEdit' index='"+meta.row+"'>编辑</a>"
                	   var assign = "<a href='javascript:;' class='rowAssign' index='"+meta.row+"'>关联</a>"
                	   
                	   return edit +"&nbsp;&nbsp;&nbsp;&nbsp;"+ assign
                	   
                   }
               }
          ]
        /*
        , fnRowCallback: function (nRow, aData, iDataIndex) {
        	
        }*/
    });
}

function initEvent() {
	
    $("#queryBtn").click(function () {
		if(table)
			table.ajax.reload();	
		else
			initTable();
    });
    
    //点击编辑按钮
    $(document.body).on("click", ".rowEdit", function(){
    	popEdit($(this));
    });
    
    //编辑弹窗关闭
    $(document.body).on("click", ".edit-dlg-close", function(){
    	cbms.closeDialog();
    });
    
    //编进弹窗确认
    $(document.body).on("click", ".edit-dlg-submit", function(){
    	updateEdit($(this));
    });
    
    //打开弹窗
    $(document.body).on("click", ".rowAssign", function(){
    	assigned = {};
    	removeAssigned = [];
    	popAssignDlg($(this));
    });
    
    //删除关联
    $(document.body).on("click",  ".assign-wrapper a", function(){
    	deleteAssign($(this));
    });
    
    //更新关联 
    $(document.body).on("click", ".assign-submit-btn", function(){
    	updateAssign();
    });
    
    //关闭关联弹窗
    $(document.body).on("click", ".assign-close-btn", function(){
    	cbms.closeDialog();
    });
    
}

function popEdit(ele){
	
	var index = ele.attr("index");
	
	cbms.getDialog("编辑", "#edit-dialog", null, function(){
		
		//初始化编辑弹窗显示数据， 添加timeout是因为： 回调函数执行过早，导致数据没有正常显示
		setTimeout(function(){
			var d = data[(index*1)];
			
			var origin = '';
			if(d.origin == 'APP')
				origin = '手机';
	    	else if(d.origin == 'MARKET')
	    		origin = '超市';
	    	else if(d.origin == 'PICK')
	    		origin = '分检';
	    	else 
	    		origin = d.origin;
			
			$(".edit-tel").val(d.mobile);
			$(".edit-name").val(d.name);
			$(".edit-company").text(d.company);
			$(".edit-origin").text(origin);
			$(".edit-regdate").text(d.regdate);
			$(".edit-lastupdted").text(d.lastUpdated);
			$(".edit-dlg-submit").attr("index", index);
		}, 1000);
	}, true);
	
}

function updateEdit(ele){
	
	var d = {};
	
	//这个是表格每一行的index，同时对应data的index。
	//所以可以通过index到data里面获取数据。
	var index = ele.attr("index");
	var row = data[index];
	d.contactId = row.contactId;
	d.ecId = row.ecUserId;
	d.phone = $(".edit-tel").val();
	d.name = $(".edit-name").val();
	
	$.post(Context.PATH + "/accountinfo/potentialcustomer/edit.html", 
		d,
		function(result){
			if(result.success){
				cbms.closeDialog();
				cbms.alert("更新成功！");
				row.mobile = d.phone;
				row.name = d.name;
				$("#dynamic-table tbody tr:eq("+index+") td:eq(1)").html(d.phone);
				$("#dynamic-table tbody tr:eq("+index+") td:eq(2)").html(d.name);
				
			}else{
				cbms.alert("更新失败！");
			}
	   	});
}

function buildParams(d) {
	if(d==null) 
		d = {};
    var companyName = $("#companyName").val();
    if(companyName){
    	d.companyName = companyName;
    }
    
	var name = $("input[name='name']").val();
    if(name){
    	d.name = name;
    }
    
    var mobile = $("input[name='mobile']").val();
    if(mobile){
    	d.tel = mobile;
    }
    
    var status = $("#status").val();
    if(status || status == '0'){
    	d.status = status;
    }
    
    return d;
}


/**关联相关代码**/

function popAssignDlg(ele){
	var index = ele.attr("index");
	$(".assign-wrapper").html("");
	
	
	cbms.getDialog("编辑", "#assign-dialog", null, function(){
		
		setTimeout(function(){
			var d = data[(index*1)];
			$("input[name='assign-contactid']").val(d.contactId);
			$("input[name='assign-index']").val(index);
			$(".assign-show-name").text(d.name);
			$(".assign-show-tel").text(d.mobile);
			$(".assign-show-company").text(d.company);
			
			///load assigned 加载已经关联的。
			$.get(Context.PATH + "/accountinfo/potentialcustomer/assign/load.html", 
					{"id": d.contactId},
					function(result){
						if(result.success && result.data){
							var datas = result.data;
							for(var i=0; i<datas.length; i++){
								var data = datas[i];
								assigned[data.accountId+""] = {"assignId": data.id,"accountId": data.accountId, "accountName": data.accountName, "contactId": data.contactId}; 
								$(".assign-wrapper").append($('<div class="clearfix">'+data.accountName+'【'+data.departmentName+'】'+'<a href="javascript:;" accountId='+data.accountId+' class="pull-right fa fa-close fa-lg red" >&nbsp;</a></div>'));
							}
						}
				   	});
			
		}, 1000);
	}, true);
}

//searchaccount.js的回调函数
function doProcessSearchAccountSelected(accountId, accountType, accountName){
	//通过搜索框显示的公司名称，点击确认后，自动添加到下面已关联列表里面去。
	if(accountId && accountId > 0){
		var existedObj = assigned[accountId+""];
		if(!existedObj){
			assigned[accountId+""] = {"accountId": accountId, "accountType": accountType, "accountName": accountName, "contactId": $("input[name='assign-contactid']").val()}; 
			$(".assign-wrapper").append($('<div class="clearfix">'+accountName+'<a href="javascript:;" accountId='+accountId+' class="pull-right fa fa-close fa-lg red" >&nbsp;</a></div>'));
		}
	}
}

//删除关联
function deleteAssign(_this){
	var accountId = _this.attr("accountId");
	
	var existedObj = assigned[accountId+""];
	if(existedObj && existedObj.assignId){  //assignId
		removeAssigned.push(existedObj.assignId) ;
	}
	delete assigned[accountId+""];
	_this.closest("div").remove();
}

//更新关联
function updateAssign(){
	
	var params = {};
	if(!isEmptyObject(assigned)){
		var j=0;
		
		var salsemanId =  $("#userId").val();
		
		for(var accountId in assigned){
			var assign = assigned[accountId+""];
			if(!assign.assignId){ //assignId存在，说明已经关联过的，不需要再次提交
				
				if(!salsemanId){
					cbms.alert("请选择服务中心和交易员！");
					return;
				}
				
				var prefix = 'updates['+(j++)+'].';
				params[prefix+'contactId'] = assign.contactId;
				params[prefix+'accountId'] = assign.accountId;
				params[prefix+'manager'] = salsemanId;
			}	
		}
	}
	
	if(!isEmptyObject(removeAssigned)){
		params.removes = removeAssigned.toString();
	}

	if(isEmptyObject(params)){
		cbms.alert("没有要更新的数据！");
		return;
	}
	
	$.post(Context.PATH + "/accountinfo/potentialcustomer/assign.html", 
			params,
			function(result){
				if(result.success){
					cbms.alert("更新成功！");
					cbms.closeDialog();
					var index = $("input[name='assign-index']").val();
					$("#dynamic-table tbody tr:eq("+index+") td:eq(6)").html(isEmptyObject(assigned)?"未关联":"已关联");
				}else{
					cbms.alert("更新失败！");
				}
		   	});
}

//判断javascript object有没有属性
function isEmptyObject(e) {  
    var t;  
    for (t in e)  
        return !1;  
    return !0  
}  

