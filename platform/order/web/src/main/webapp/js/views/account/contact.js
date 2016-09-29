var dt;
var accountType = $("#type").val();
var contactId;
var totalIsMain;
var condition = {};
var isAdd;
var htmlStr;

//联系人信息弹出页面
function initShowHtml(){
    htmlStr = "<div class='well ' style='margin-top:25px; width:500px'><form id='form-horizontal' class='form-horizontal' role='form'>";
    htmlStr += "<div class='form-group'><label class='col-sm-3 control-label no-padding-right' for='name'>姓名</label>";
    htmlStr += "<div class='col-sm-9'><input type='text' id='name' placeholder='请输入姓名' must='1' class='col-xs-10 col-sm-5' /><span class='red'>*</span></div></div>";
    htmlStr += "<div class='form-group'><label class='col-sm-3 control-label no-padding-right' for='tel'>手机号</label>";
    htmlStr += "<div class='col-sm-9'><input type='text' id='tel' placeholder='请输入手机号'  must='1' verify='mobile' class='col-xs-10 col-sm-5' /><span class='red'>*</span></div></div>";
    htmlStr += "<div class='form-group'><label class='col-sm-3 control-label no-padding-right' for='deptName'>部门</label>";
    htmlStr += "<div class='col-sm-9'><input type='text' id='deptName' placeholder='请输入部门' class='col-xs-10 col-sm-5' /></div></div>";
    htmlStr += "<div class='form-group'><label class='col-sm-3 control-label no-padding-right' for='qq'>QQ</label>";
    htmlStr += "<div class='col-sm-9'><input type='text' id='qq' placeholder='请输入QQ' verify='qq' class='col-xs-10 col-sm-5' /></div></div>";
    htmlStr += "<div class='form-group'><label class='col-sm-3 control-label no-padding-right' for='email'>邮箱</label>" ;
    htmlStr += "<div class='col-sm-9'><input type='text' id='email' placeholder='请输入邮箱' verify='email' class='col-xs-10 col-sm-5' /></div></div>";
    htmlStr += "<div class='form-group'><label class='col-sm-3 control-label no-padding-right' for='isMain'>主联系人</label>";
    htmlStr += "<div class='col-sm-9'>";
    if(accountType == "seller")
        htmlStr += "<input type='radio' name='isMain' value='1' checked='checked'/>是";
    else
        htmlStr += "<input type='radio' name='isMain' value='1' />是<input type='radio' name='isMain' value='0' checked='checked' />否";
    htmlStr += "</div></div>";
    htmlStr += "<div class='form-group'><label class='col-sm-3 control-label no-padding-right' for='status'>状态</label>";
    htmlStr += "<div class='col-sm-9'><span class='vendor-edit'><select id='status' name='status'><option value ='1'>正常</option><option value ='0'>锁定</option></select></span></div></div>";
    htmlStr += "<div class='form-group'><label class='col-sm-3 control-label no-padding-right' for='note'>备注</label>";
    htmlStr += "<div class='col-sm-9'><input type='text' id='note' placeholder='请输入备注' class='col-xs-10 col-sm-5' /></div></div>";
    htmlStr += "</form><div class='modal-footer'><button data-bb-handler='success' type='button' id='saveContent' class='btn btn-sm btn-primary'><i class='ace-icon fa fa-check'></i> 保存</button>";
    htmlStr += "<button data-bb-handler='button' type='button' id='cancel' class='btn btn-sm'>取消</button></div></div>";
}

//编辑页面
function editContact(id){
    cbms.getDialog("编辑联系人",htmlStr);
    $.ajax({
        type : "POST",
        url : Context.PATH + "/account/contact/edit.html",
        data : {"id": id},
        dataType : "json",
        success : function(response, textStatus, xhr) {
            if (response.success) {
                var data = response.data;
                $("#name").val(data.name);
                $("#tel").val(data.tel);
                $("#deptName").val(data.deptName);
                $("#qq").val(data.qq);
                $("#email").val(data.email);
                $("input[name='isMain'][value='" + data.isMain + "']").attr("checked",true);
                $("#status").val(data.status);
                $("#note").val(data.note);
                isAdd = false;
                contactId = id;
                setlistensSave("#form-horizontal");
            }
        },
        error : function(xhr, textStatus, errorThrown) {}
    });
}

//修改企业联系人
function updateContact(data) {
    $.ajax({
        type : "POST",
        url : Context.PATH + "/account/contact/update.html",
        data : data,
        dataType : "json",
        success : function(response, textStatus, xhr) {
            if (response.success) {
                cbms.alert(response.data);
                cbms.closeDialog();
                searchData();
            } else {
                cbms.alert(response.data);
            }
        },
        error : function(xhr, textStatus, errorThrown) {}
    });
}

//锁定和解锁
function lockAndUnLock(id, status,msg){
    cbms.confirm(msg,null,function(){
        $.ajax({
            type : "POST",
            url : Context.PATH + "/account/contact/lockAndUnLock.html",
            data : {"id": id,
                "status" : status
            },
            dataType : "json",
            success : function(response, textStatus, xhr) {
                if (response.success) {
                    if (response.success) {
                        cbms.alert("成功！");
                        searchData();
                    } else {
                        cbms.alert("失败！");
                    }
                }
            },
            error : function(xhr, textStatus, errorThrown) {}
        });
    })


}

function searchData(){
    dt.ajax.reload();
}

function initTable(){
    dt = jQuery("#dynamic-table").DataTable( {
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "bLengthChange": true, //显示pageSize的下拉框
        "ajax": {
            url: Context.PATH + '/account/ajaxaccountcontact.html',
            type: 'POST',
            data: {"accountId" : $("#accountId").val(),
                "type" : accountType
            }
        },
        fnRowCallback: renderOperation,
        columns: [
            {data: 'name'},
            {data: 'tel'},
            {data: 'deptName'},
            {data: 'qq'},
            {data: 'email'},
            {data: 'isMain'},
            {data: 'status'},
            {data: 'managerName'},
            {data: 'note'},
            {defaultContent: ''}
        ],
        columnDefs: [
            {
                sDefaultContent: '', //解决请求参数未知的异常
                aTargets: ['_all']
            }
        ]
    });
}

function renderOperation(nRow, aData, iDataIndex){
    $('td:eq(5)', nRow).html(aData.isMain == 0 ? '<span>否</span>' :'<span class="red">是</span>');
    $('td:eq(6)', nRow).html(aData.status == 0 ? '<span class="red">锁定</span>' :'<span>正常</span>');

    var manager = aData.manager;    //买家交易员
    var canSeeIds=[];
    if($.trim($("#canSeeIds").val())!=""){
    	canSeeIds = $("#canSeeIds").val().split(",");
    }
    //var userId = $("#userId").val();    //当前登录人员
    var html = '<div class="hiddenh-sm hidden-xs action-buttons">';
    
    if($.inArray(""+manager,canSeeIds)==-1&&canSeeIds.length>0){
        $('td:eq(1)', nRow).html('***');  //手机
        $('td:eq(3)', nRow).html('***');    //QQ
        $('td:eq(4)', nRow).html('***');    //邮箱
        html += '<a class="blue assign" title="划转记录" href="javascript:;">';
        html += '<i class="ace-icon fa  fa-eye bigger-130"></i>';
        html += '</a>';
    }else{
        html += '<a class="green edit" title="编辑" href="javascript:;">';
        html += '<i class="ace-icon fa fa-pencil bigger-130"></i>';
        html += '</a>';
        //正常可以锁定
        if(aData.status == 1){
            html += '<a class="red lock" title="锁定" href="javascript:;">';
            html += '<i class="ace-icon fa fa-lock bigger-130"></i>';
            html += '</a>';
        }else if(aData.status == 0){  //锁定可以解锁
            html += '<a class="red unlock" title="解锁" href="javascript:;">';
            html += '<i class="ace-icon fa fa-unlock bigger-130"></i>';
            html += '</a>';
        }
        if(accountType == 'buyer'){
            html += '<a class="blue authority" title="划转" href="javascript:;">';
            html += '<i class="ace-icon fa  fa-exchange bigger-130"></i>';
            html += '</a>';
        }
        html += '<a class="blue assign" title="客户划转记录" href="javascript:;">';
        html += '<i class="ace-icon fa  fa-eye bigger-130"></i>';
        html += '</a>';
    }
    html += '</div>';
    $('td:eq(-1)', nRow).html(html);
}

function initClickEvent(){
    //编辑
    $(".table").on("click", ".edit", function(){
        var tr = $(this).closest('tr'), data = dt.row(tr).data();
        editContact(data.id);
    });

    //锁定
    $(".table").on("click", ".lock", function(){
        var tr = $(this).closest('tr'), data = dt.row(tr).data();
        lockAndUnLock(data.id, '0',"确定要锁定该用户？");
    });

    //解锁
    $(".table").on("click", ".unlock", function(){
        var tr = $(this).closest('tr'), data = dt.row(tr).data();
        lockAndUnLock(data.id, '1',"确定要解锁该用户？");
    });

    //划转
    $(".table").on("click", ".authority", function(){
        location = Context.PATH + "/account/contact/accountcontact.html";
    });

    //客户划转记录
    $(".table").on("click", ".assign", function(){
        location = Context.PATH + "/account/buyer/" + $("#accountId").val() + "/assignlog.html";
    });
}

jQuery(function($) {
    //弹层页面
    initShowHtml();
    //列表
    initTable();
    //操作
    initClickEvent();

    function saveAddFunc (){
        condition["accountId"] = $("#accountId").val();
        condition["name"] = $("#name").val();
        condition["tel"] = $("#tel").val();
        condition["deptName"] = $("#deptName").val();
        condition["qq"] = $("#qq").val();
        condition["email"] = $("#email").val();
        condition["isMain"] = $("input[name='isMain']:checked").val();
        condition["status"] = $("#status").val();
        condition["note"] = $("#note").val();
        condition["type"] = accountType;
        if(setlistensSave("#form-horizontal")){
            //新增
            if(isAdd){
                saveContact(condition);
            }else{
                condition["id"] = contactId;
                updateContact(condition);
            }
        }
    }

    //保存企业联系人
    function saveContact(data) {
        $.ajax({
            type : "POST",
            url : Context.PATH + "/account/contact/save.html",
            data : data,
            dataType : "json",
            success : function(response, textStatus, xhr) {
                if (response.success) {
                    cbms.alert(response.data);
                    cbms.closeDialog();
                    searchData();
                } else {
                    cbms.alert(response.data);
                }
            },
            error : function(xhr, textStatus, errorThrown) {}
        });
    }

    //添加企业联系人
    $("#addContact").on(ace.click_event,function(){
        $.ajax({
                type : "POST",
                url : Context.PATH + "/account/contact/querycount.html",
                data : {
                    "accountId" : $("#accountId").val()
                },
                dataType : "json",
                success : function(response, textStatus, xhr) {
                    if (response.success) {
                        totalIsMain = response.data;
                    } else {
                        totalIsMain = 0;
                    }
                    //如果是卖家
                    if('seller' == accountType){
                        //如果卖家存在一个联系人信息就不能再新添加
                        if(totalIsMain > 0){
                            cbms.alert('卖家联系人只能有一个！');
                            return;
                        }
                    }
                    cbms.getDialog("添加联系人",htmlStr);
                    isAdd = true;
                    setlistensSave("#form-horizontal");
                },
                error : function(xhr, textStatus, errorThrown) {}
            });
    });
    //保存按钮
    $(document).on("click","#saveContent",function(){
        saveAddFunc();
    });
    //取消按钮
    $(document).on("click","#cancel",function(){
        cbms.closeDialog();
    })
})