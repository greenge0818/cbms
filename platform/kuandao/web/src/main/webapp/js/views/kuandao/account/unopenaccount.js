/**
 * Created by dengxiyan on 2015/8/27.
 * 未开户页
 */
var kuandaoAccount = {};
//客户性质用于显示   加工、运输、仓库亮色暂时不做
kuandaoAccount._accountTagArray = [{code:1,name:'买',title:"买家客户",class:'buy'}
                        ,{code:6,name:'临',title:"卖家客户",class:'lin'}
                        ,{code:10,name:'代',title:"卖家代运营客户",class:'dai'}
                      /*  ,{code:16,name:'仓库',title:"仓库客户"}
                        ,{code:32,name:'运输',title:"运输客户"}
                        ,{code:64,name:'加工',title:"加工客户"}*/
                        ];
var tradeTable;
jQuery(function ($) {
    initTable();
    initClickEvent();
});

function initTable() {
    var url = Context.PATH + "/kuandao/account/queryUnOpenedAccount.html";
    tradeTable = jQuery("#dynamic-table").DataTable({
        "sScrollY": "400px",
        "bScrollCollapse": true,
        "iDisplayLength": 50,
        "ajax": {
            "url": url
            , "type": "POST"
            , data: function (d) {
                d.memeberName = $("#memeberName").val();
                d.custType = $("#custType").children("option:selected").val();
                d.status =  $("#status").children("option:selected").val();
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'acctId'},
            {data: 'memeberName'},
            {data: 'custType'},
            {data: 'orgName'},
            {data: 'idNo'}
            , {data: 'mobile'}
            , {data: 'memeberCode'}
            , {data: 'custStatus'}
            , {data: 'operate', "sClass": "text-center"}
        ]
        , columnDefs: [
			{
			    "targets": 0, //第几列 从0开始
			    "data": "acctId",
			    "render": function (data, type, full, meta) {
			    	if(full.mobile && full.idNo)
			    	return '<input type="checkbox"  class="checkchild"  value="' + full.acctId + '" />';
			    }
			},
            {
                "targets": 2, //第几列 从0开始
                "data": "custType",
                "render": renderAccountTag
            },
            {
                "targets": 7, //第几列 从0开始
                "data": "custStatus",
                "render": function (data, type, full, meta) {
                	var text = '';
                	if(!full.mobile || !full.idNo){
                		text = '<span>有空值</span>';
                	}else if(full.custStatus == 0){
            			text = '<span>锁定</span>';
            		}else{
            			text = '<span>正常</span>';
            		}
                	 return text;
                }
            },
            {
                "targets": 8, //第几列 从0开始
                "data": "operate",
                "render": function (data, type, full, meta) {
                	var btn = '';
                	var idNo = full.idNo;
                	var mobile = full.mobile;
                    //电话号码、组织机构代码为空的不能开户
                	if(mobile && idNo){
                    	btn = btn + '<a href="javascript:void(0);" onclick="openAccount('+full.acctId+')">开户</a>&nbsp;&nbsp;&nbsp;';
                	}
                	if(!idNo)	idNo = '';
                	if(!mobile) mobile = '';
                	btn = btn + '<a href="javascript:void(0);" onclick="modify('+full.acctId+',\''+full.memeberName+'\',\''+idNo+'\',\''+mobile+'\','+full.custType+')">修改</a>';
                	return btn;
                }
            },
            {
            	'sDefaultContent': '',
            	 'targets': [ '_all' ]
            }
        ]
    });
}

/**
 * 显示所有的客户性质 如果是某个性质就点亮（）
 * @param data
 * @param type
 * @param full
 * @param meta
 * @returns {string}
 */
function renderAccountTag(data, type, full, meta){
    var v="";
    var lights = getAccountTagsByCode(data);
    $.each(kuandaoAccount._accountTagArray,function(index,item){
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
    $.each(kuandaoAccount._accountTagArray,function(index,item){
        if((item.code & code) === item.code){
            array.push(item.code);
        }
    });
    return array;
}


function initClickEvent() {
    //搜索事件
    $("#queryBtn").click(search);
   
    $("#openAccountBtn").click(batchOpenAccount);
    
    $("#checkAll").change(function () {
        var check = $(this).prop("checked");
        $(".checkchild").prop("checked", check);
    });
    
} 


function search() {
    tradeTable.ajax.reload();
}

function openAccount(acctId){
	if(!acctId){
		cbms.alert('请选择记录');
		return;
	}
	cbms.confirm('您确定为选中的客户开户吗？',acctId,function(acctId){
		cbms.loading();
		$.ajax({
	        type: "POST",
	        url: Context.PATH + '/kuandao/account/openAccount.html',
	        data: {
	        	acctId: acctId
	        },
	        dataType: "json",
	        success: function (response, textStatus, xhr) {
	            if (response.success) {
	            	cbms.alert('客户开户成功',function(){
	            		window.location.href=Context.PATH + "/kuandao/account/index.html";
	            	});
	            } else {
	                cbms.alert(response.data);
	            }
	            cbms.closeLoading();
	        },
	        error: function (xhr, textStatus, errorThrown) {
	        	cbms.closeLoading();
	        }
	    });
	});
	
}

function batchOpenAccount(){
	var id = '';
	$(".checkchild:checked").each(function(i,checkbox){
		id = id + $(checkbox).val() + ',';
	});
	var len = id.length;
	if(len == 0){
		cbms.alert('请选择记录');
		return;
	}
	id = id.substring(0,len - 1);
	cbms.confirm('您确定为选中的客户开户吗？',id,function(id){
		cbms.loading();
		$.ajax({
            type: 'post',
            url: Context.PATH + "/kuandao/account/batchOpenAccount.html",
            data: {
	        	acctId: id
	        },
            error: function (s) {
            	cbms.closeLoading();
            }
            , success: function (result) {
                if (result && result.success) {
                    cbms.alert("选中客户开户完成",function(){
	            		window.location.href=Context.PATH + "/kuandao/account/index.html";
	            	});
                } else {
                    cbms.alert(result.data);
                }
               cbms.closeLoading();
            }

        });
	});
}

function modify(acctId,memeberName,idNo,mobile,custType){
	

    var ele = '<div class="dialog-m" id="modifyDialog">' +
        '<p><label for="memeberName">公&nbsp;司&nbsp;名&nbsp;称&nbsp;&nbsp;：&nbsp;&nbsp;&nbsp;</label><input type="text" name="memeberName" id="memeberName" value="'+memeberName+'" size="40"/></p>' +
        '<p><label for="orgCode">组织机构代码：</label><input type="text" name="orgCode" id="orgCode"  value="'+idNo+'" size="40"/></p>' +
        '<p><label for="mobile">款&nbsp;道&nbsp;手&nbsp;机&nbsp;&nbsp;：&nbsp;&nbsp;&nbsp;</label><input type="text" name="mobile" id="mobile"  value="'+mobile+'" size="40"/></p>' +
        '<div class="btn-bar text-center"><button id="commit" type="button" class="btn btn-primary btn-sm">确定</button>&nbsp;<button type="button" class="btn btn-default btn-sm" id="cancel">取消</button></div></div>';
    var dia = cbms.getDialog("客户信息修改", ele);


    $("#modifyDialog").on("click", "#cancel", function () {
        cbms.closeDialog();
    });

    $("#modifyDialog").on("click", "#commit", function () {
        var inMemeberName = $("#memeberName").val();
        var orgCode = $("#orgCode").val();
        var inMobile = $("#mobile").val();
        if(!inMemeberName || !inMemeberName.trim()){
        	cbms.closeDialog();
        	cbms.alert("请输入客户名称");
        	return;
        }
        if(getLength(inMemeberName) > 80){
        	cbms.closeDialog();
        	cbms.alert("客户名称不能超过80个字符（一个汉字算两个字符 ）");
        	return;
        }
        if(!orgCode || !orgCode.trim()){
        	cbms.closeDialog();
        	cbms.alert("请输入组织机构代码证号");
        	return;
        }
        if(getLength(orgCode) > 20){
        	cbms.closeDialog();
        	cbms.alert("组织机构代码证号不能超过20个字符（一个汉字算两个字符 ）");
        	return;
        }
        if(!utils.isValid(Regexs.mobile,inMobile)){
        	cbms.closeDialog();
        	cbms.alert("请输入正确的手机号");
        	return;
        }
        cbms.closeDialog();
        cbms.loading();
        $.ajax({
            type: "POST",
            url: Context.PATH + '/kuandao/account/modifyCustAccount.html',
            data: {
            	acctId: acctId,
            	memeberName: inMemeberName,
            	idNo:orgCode,
            	mobile:inMobile
            },
            dataType: "json",
            success: function (response, textStatus, xhr) {
                if (response.success) {
                	cbms.alert(response.data,function(){
                		tradeTable.ajax.reload();
                	});
                } else {
                    cbms.alert(response.data);
                }
                cbms.closeLoading();
            },
            error: function (xhr, textStatus, errorThrown) {
            	cbms.alert("请刷新重试");
            	cbms.closeLoading();
            }
        });
    });
        
}

function initOrg(){
	 $.ajax({
	        type: "POST",
	        url: Context.PATH + '/kuandao/account/queryAllBusinessOrg.html',
	        data: {},
	        dataType: "json",
	        success: function (response, textStatus, xhr) {
	            if (response.success) {
	                var datas = response.data;
	                $("#org").empty();
	                $("#org").append('<option value ="">全部</option>');
	                for (var i in datas) {
	                    $("#org").append('<option value ="' + datas[i].id + '">' + datas[i].name + '</option>');
	                }
	                var selectedOrgId = $('#org').attr('value');
	                $('#org option[value="'+selectedOrgId+'"]').attr("selected",true);
	            }
	        },
	        error: function (xhr, textStatus, errorThrown) {
	        }
	    });
}

function getLength(str)   
{  
    return str.replace(/[^\x00-\xff]/g,"aa").length;  
};
