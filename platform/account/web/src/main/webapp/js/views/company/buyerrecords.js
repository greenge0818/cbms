/**
 * 销售记录
 * Created by zhoucai@prcsteel.com on 2016/3/27.
 */
var dt;

jQuery(function ($) {
    $("#checkData").verifyForm();
    $("#checkData1").verifyForm();
    var isadmin = $("#isAdmin").val();
    var isShow = $("#isShow").val();
    generateCheckBox();
	$("#buyerRecords").addClass("active").find("a").attr("href","javascript:void(0);");
    countTradeFlowData();
    initTable(isadmin,isShow);
    $("#searchBtn").on("click", function () {

        countTradeFlowData();
        dt.ajax.reload();
    });
    if(isadmin>0){
        queryAllprojectList();

        $("#mangerProject").on("click", function () {
            cbms.getDialog("项目管理","#projectManegerDiv");
            $("#checkData").verifyForm();
        });
        $(document).on("click", "#createProject", function(){

            if(setlistensSave("#checkData")){
                createProject();
            }
        });
        $("#moveProjiect").on("click", function () {
            cbms.getDialog("移动到项目",$("#moveProjectDiv").html());
        });
        $(document).on("click", "#confirmBtn", function(){
            moveOrderToProject();
        });

        $(document).on("click", "#cancelBtn1", function(){
            cbms.closeDialog();
        });

        $(document).on("click", "#confirmBtn1", function(){
            editStatus();
        });

        $(document).on("click", "#cancelBtn", function(){
            cbms.closeDialog();
        });
        $('#projectList').change(function(){
            searchData();
        });
    }
    
});
//状态改变重新加载数据
function searchData() {
    dt.ajax.reload();
    countTradeFlowData();
}
//获取状态值列表
function getStatusValuesById(id){
    var checkedBoxes = $("#"+id).find(".mulsel-content-list ul").find("input[type='checkbox']:checked");
    var array = [];
    checkedBoxes.each(function (){
        array.push($(this).val());
    });
    return array.join(",");
}
//查询列表
function initTable(isAdmin,isShow) {
    if(isAdmin==0){
        $("#showManagerTable").hide();
        if(isShow>0){
            $("#showNormalTable").hide();
            dt = jQuery("#dynamic-table2").DataTable({
                "pageLength": 50, //每页记录数
                "processing": true,//显示数据加载进度
                "serverSide": true, //服务模式
                "searching": false, //是否启用搜索
                "ordering": false, //是否启用排序
                "ajax": {
                    "url": Context.PATH + "/company/loadsellertradelist.html"
                    , "type": "POST"
                    ,
                    data: function(d){
                        d.accountId = $("#accountId").val();
                        d.departId = $("#departmentId").val();
                        d.strStartTime = $("#startTime").val();
                        d.strEndTime = $("#endTime").val();
                        d.projectId = $("#projectList").val();
                        d.statusList = getStatusValuesById("tradetatus");
                        d.sellerName = $("#buyerCust").val()
                    }
                }
                ,"fnRowCallback":function(nRow, aData, iDataIndex){
                    var orderUrl =$("#orderUrl").val();
                    $('td:eq(11)', nRow).html(renderStatus(aData.status));
                    $('td:eq(8)', nRow).html(formatMoney(aData.sumAmount,2));
                    $('td:eq(9)', nRow).html(formatMoney(aData.amount,2));
                    $('td:eq(6)',nRow).html((aData.sumWeight).toFixed(6));
                    $('td:eq(7)',nRow).html((aData.weight).toFixed(6));
                    var tempText2 = '<button  class="button btn-xs btn-link confirm">'+aData.accountName+'</button>';
                    var tempText1 = '<a href="'+orderUrl+'/order/query/detail.html?orderid='+aData.id+'&menu=tradecomplete"  class="button btn-xs btn-link confirm">'+aData.orderId+'</button>';
                    $('td:eq(1)',nRow).html(tempText1);
                },
                columns: [
                    { data: 'orderId' },
                    { data: 'creatTime' },
                    { data: 'sellerName' },
                    { data: 'userName' },
                    { data: 'accountName' },
                    { data: 'quantity' },
                    { data: 'sumWeight' },
                    { data: 'weight' },
                    { data: 'sumAmount' },
                    { data: 'amount' },
                    { data: 'projectName' },
                    { data: 'status' }
                ]
            } );

        }else{
            $("#showjiaoyiYuanTable").hide();
            dt = jQuery("#dynamic-table").DataTable({
                "pageLength": 50, //每页记录数
                "processing": true,//显示数据加载进度
                "serverSide": true, //服务模式
                "searching": false, //是否启用搜索
                "ordering": false, //是否启用排序
                "ajax": {
                    "url": Context.PATH + "/company/loadsellertradelist.html"
                    , "type": "POST"
                    ,
                    data: function(d){
                        d.accountId = $("#accountId").val();
                        d.departId = $("#departmentId").val();
                        d.strStartTime = $("#startTime").val();
                        d.strEndTime = $("#endTime").val();
                        d.projectId = $("#projectList").val();
                        d.statusList = getStatusValuesById("tradetatus");
                        d.sellerName = $("#buyerCust").val()
                    }
                }
                ,"fnRowCallback":function(nRow, aData, iDataIndex){
                    var orderUrl =$("#orderUrl").val();
                    $('td:eq(10)', nRow).html(renderStatus(aData.status));
                    $('td:eq(8)', nRow).html(formatMoney(aData.sumAmount,2));
                    $('td:eq(9)', nRow).html(formatMoney(aData.amount,2));
                    $('td:eq(6)',nRow).html((aData.sumWeight).toFixed(6));
                    $('td:eq(7)',nRow).html((aData.weight).toFixed(6));
                    var tempText2 = '<button  class="button btn-xs btn-link confirm">'+aData.sellerName+'</button>';
                    var tempText1 = '<a href="'+orderUrl+'/order/query/detail.html?orderid='+aData.id+'&menu=tradecomplete"  class="button btn-xs btn-link confirm">'+aData.orderId+'</button>';
                    $('td:eq(0)',nRow).html(tempText1);
                    $('td:eq(4)',nRow).html(tempText2);

                },
                columns: [
                    { data: 'orderId' },
                    { data: 'creatTime' },
                    { data: 'accountName' },
                    { data: 'userName' },
                    { data: 'sellerName' },
                    { data: 'quantity' },
                    { data: 'sumWeight' },
                    { data: 'weight' },
                    { data: 'sumAmount' },
                    { data: 'amount' },
                    /* { data: 'sellerDepartName' },*/
                    { data: 'status' }
                ]
            } );
        }
    }else{
        $("#showjiaoyiYuanTable").hide();
        $("#showNormalTable").hide();
        dt = jQuery("#dynamic-table1").DataTable({
            "pageLength": 50, //每页记录数
            "processing": true,//显示数据加载进度
            "serverSide": true, //服务模式
            "searching": false, //是否启用搜索
            "ordering": false, //是否启用排序
            "ajax": {
                "url": Context.PATH + "/company/loadsellertradelist.html"
                , "type": "POST"
                ,
                data: function(d){
                    d.accountId = $("#accountId").val();
                    d.departId = $("#departmentId").val();
                    d.strStartTime = $("#startTime").val();
                    d.strEndTime = $("#endTime").val();
                    d.projectId = $("#projectList").val();
                    d.statusList = getStatusValuesById("tradetatus");
                    d.sellerName = $("#buyerCust").val()
                }
            }
            ,"fnRowCallback":function(nRow, aData, iDataIndex){
                var orderUrl =$("#orderUrl").val();
                $('td:eq(12)', nRow).html(renderStatus(aData.status));
                $('td:eq(9)', nRow).html(formatMoney(aData.sumAmount,2));
                $('td:eq(10)', nRow).html(formatMoney(aData.amount,2));
                $('td:eq(7)',nRow).html((aData.sumWeight).toFixed(6));
                $('td:eq(8)',nRow).html((aData.weight).toFixed(6));
                var tempText2 = '<button  class="button btn-xs btn-link confirm">'+aData.accountName+'</button>';
                var tempText1 = '<a href="'+orderUrl+'/order/query/detail.html?orderid='+aData.id+'&menu=tradecomplete"  class="button btn-xs btn-link confirm">'+aData.orderId+'</button>';
                var tempText3 = '<input type="checkbox" name="projectBox" value='+aData.orderId+'>';
                $('td:eq(1)',nRow).html(tempText1);
                //$('td:eq(2)',nRow).html(tempText2);
                $('td:eq(0)',nRow).html(tempText3);
            },
            columns: [
                { data: 'orderId' },
                { data: 'orderId' },
                { data: 'creatTime' },
                { data: 'sellerName' },
                { data: 'userName' },
                { data: 'accountName' },
                { data: 'quantity' },
                { data: 'sumWeight' },
                { data: 'weight' },
                { data: 'sumAmount' },
                { data: 'amount' },
               /* { data: 'sellerDepartName' },*/
                { data: 'projectName' },
                { data: 'status' }
            ]
        } );

    }

}
//汇总交易信息 
function countTradeFlowData(){
	//情况下拉框
    document.getElementById("salesCount").innerText= "";
    document.getElementById("totalMass").innerText= '';
    document.getElementById("factTotalMass").innerText= '';
    document.getElementById("totalAmt").innerText = '';
    document.getElementById("factTotlAmt").innerText= '';
	$.ajax({
		url : Context.PATH + "/company/countsellertradeflow.html",
		type : "POST",
        data:{
            departId: $("#departmentId").val(),
            accountId : $("#accountId").val(),
            sellerId : '',
            accountName : '',
            strStartTime : $("#startTime").val(),
            strEndTime : $("#endTime").val(),
            statusList : getStatusValuesById("tradetatus"),
            projectId : $("#projectList").val(),
            sellerName : $("#buyerCust").val()
            },
	    success: function(result) {

            if(result.orderId==0){
                return;
            }else{
                document.getElementById("salesCount").innerText= result.orderId;
                if(result.sumWeight!=null){
                    document.getElementById("totalMass").innerText= (result.sumWeight).toFixed(6);
                }
                if(result.weight!=null){
                    document.getElementById("factTotalMass").innerText= (result.weight).toFixed(6);
                }
                if(result.sumAmount!=null){
                    document.getElementById("totalAmt").innerText = formatMoney(result.sumAmount,2);
                }
                if(result.amount!=null){
                    document.getElementById("factTotlAmt").innerText= formatMoney(result.amount,2);
                }


            }

	    }
	});
		
	
}
//生成状态checkbox
 function generateCheckBox() {
     $("#tradetatus").selectCheckBox(Context.PATH+"/company/getbuyerconsignorderstatus.html", {},searchData);
}
//获取状态checkbox选中的值
function getStatusValuesById(id){
    var checkedBoxes = $("#"+id).find(".mulsel-content-list ul").find("input[type='checkbox']:checked");
    var array = [];
    checkedBoxes.each(function (){
        array.push($(this).val());
    });
    return array.join(",");
}
//翻译状态
function renderStatus(data){

    if(data == '1'){
        return "待审核";
    }if(data == '-1'){
        return "订单关闭";
    }if(data == '2'){
        return "待关联";
    }if(data == '3'){
        return "交易关闭待审核";
    }if(data == '-2'){
        return "订单关闭";
    }if(data == '4'){
        return "待出库";
    }if(data == '5'){
        return "交易关闭待审核";
    }if(data == '6'){
        return "待二次结算";
    }if(data == '7'){
        return "待开票申请";
    }if(data == '8'){
        return "待开票";
    }if(data == '10'){
        return "交易完成";
    }if(data == '-3'){
        return "订单关闭";
    }if(data == '-4'){
        return "订单关闭";
    }if(data == '-5'){
        return "交易关闭待审核";
    }if(data == '-6'){
        return "交易关闭待审核";
    }if(data == '-7'){
        return "订单关闭";
    }if(data == '-8'){
        return "订单关闭";
    }
    return '-';
}

//增加项目管理分组
function createProject() {
    $.ajax({
        type: 'POST',
        dataType:"JSON",
        url: Context.PATH + "/company/creatproject.html",
        data: {
            'projectName' : $("#projectNameInput").val(),
            'companyId' :   $("#accountId").val(),
            'status' : '0'
        },
        error: function (s) {
            cbms.closeDialog();
        },
        success: function (result) {
            cbms.closeDialog();
            if (result) {
                if (result.success) {
                    cbms.alert("新增项目成功", function () {
                        queryAllprojectList();
                    });
                } else {
                    cbms.alert(result.data);
                }
            } else {
                cbms.alert("新增项目失败");
            }
        }
    });
}
function getRadio() {
    var radionum = $("input[name=projectStatus]");
    var radiaoId ="";
    for (var i = 0; i < radionum.length; i++) {
        var id = radionum[i].value;
        if (radionum[i].checked) {
            radiaoId = radionum[i].value;
            return radiaoId;
        }
    }

}
//查询 所有项目列表
function queryAllprojectList(){
    $("#moveOrderProjectList").empty();
    $("#projectList").empty();
    $("#projectList").append("<option value=''>-----全部-----</option>");
    aa = jQuery("#project-table").DataTable({
        "dom": 'lrTt<"bottom"p>i<"clear">',
        paging: false, //禁用分页功能
        bLengthChange: false, //选择分页的页数功能禁用
        info: false, // 总记录数显示的禁用
        "bScrollCollapse": true,
        searching: false,//搜索功能
        autoWidth: true,
        "bDestroy":true,
        "serverSide": true,
        "ordering": false,
        "ajax": {
            "url": Context.PATH + "/company/queryallprojectlist.html"
            , "type": "POST"
            ,
            data: function(d){
                d.companyId = $("#accountId").val()

            }
        }
        ,"fnRowCallback":function(nRow, aData, iDataIndex){
            var projectId=aData.projectId;
            var  projectName= aData.projectName;
            var status = aData.status;
            if(status=='0'){
                $("input[name='projectStatus']").eq(1).attr("checked","checked");
                $("input[name='projectStatus']").eq(0).removeAttr("checked");
                $("input[name='projectStatus']").eq(1).click();
            }else{
                $("input[name='projectStatus']").eq(0).attr("checked","checked");
                $("input[name='projectStatus']").eq(1).removeAttr("checked");
                $("input[name='projectStatus']").eq(0).click();
            }
            var tempText1 = '<input value="'+projectName+'" type="text" maxlength="12" must="1" verify="stri" >';
            var tempText2 = '<a href="#" onclick="changeProjectName('+projectId+','+iDataIndex+')">[编辑]</a><a href="#" onclick="deleteProjectName('+projectId+')">[删除]</a>';
            $('td:eq(1)',nRow).html(tempText2);
            $('td:eq(0)',nRow).html(tempText1);
            $("#moveOrderProjectList").append("<option value="+projectId+">"+projectName+"</option>");
            $("#projectList").append("<option value="+projectId+">"+projectName+"</option>");

        },
        columns: [
            { data: 'projectId' },
            { data: 'projectName' }
        ]
    } );

}
//将订单关联项目
function moveOrderToProject(){
    var orderList =getCheckedValues();
    if(orderList==''){
        return;
    }
    $.ajax({
        type: 'POST',
        dataType:"JSON",
        url: Context.PATH + "/company/changeorderproject.html",
        data: {
            'projectId' : $("#moveOrderProjectList").val(),
            'projectName' : $("#moveOrderProjectList").find("option:selected").text(),
            'orderList' : orderList
        },
        error: function (s) {
            cbms.closeDialog();
        },
        success: function (result) {
            cbms.closeDialog();
            if (result) {
                if (result.success) {
                    cbms.alert("移动项目成功", function () {
                        queryAllprojectList();
                        searchData();
                    });
                } else {
                    cbms.alert(result.data);
                }
            } else {
                cbms.alert("移动项目失败");
            }
        }
    });
}

/**
 * 获得选择的checkbox的values
 */
function getCheckedValues(){
    var checkedBoxes = $('#dynamic-table1').find("tbody").find("input[name='projectBox']:checked");

    var ids = [];
    checkedBoxes.each(function(){
        ids.push($(this).val());
    });
    return ids.join(",");
}

function editStatus(){
    $.ajax({
        type: 'POST',
        dataType:"JSON",
        url: Context.PATH + "/company/editorderproject.html",
        data: {
            'companyId' :   $("#accountId").val(),
            'status': getRadio()
        },
        error: function (s) {
            cbms.closeDialog();
        },
        success: function (result) {
            cbms.closeDialog();
            if (result) {
                if (result.success) {
                    cbms.alert("项目状态修改成功", function () {
                        queryAllprojectList();
                        searchData();
                    });
                } else {
                    cbms.alert(result.data);
                }
            } else {
                cbms.alert("项目状态修改失败");
            }
        }
    });
}
//编辑项目名称
function changeProjectName(projectId,id){
    var projectName = $("#project-table").find("tbody tr").eq(id).find("td:first").find("input").val();
    alert(projectName);
    if(!setlistensSave("#checkData1")){
        return;
    }
    $.ajax({
        type: 'POST',
        dataType:"JSON",
        url: Context.PATH + "/company/editorderproject.html",
        data: {
            'companyId' :   $("#accountId").val(),
            'projectId': projectId,
            'beforeId': projectId,
            'projectName': projectName
        },
        error: function (s) {
            cbms.closeDialog();
        },
        success: function (result) {
            cbms.closeDialog();
            if (result) {
                if (result.success) {
                    cbms.alert("编辑项目成功", function () {
                        queryAllprojectList();
                        searchData();
                    });
                } else {
                    cbms.alert(result.data);
                }
            } else {
                cbms.alert("编辑项目失败");
            }
        }
    });
}

//删除项目名称
function deleteProjectName(projectId){
    $.ajax({
        type: 'POST',
        dataType:"JSON",
        url: Context.PATH + "/company/deleteorderproject.html",
        data: {
            'projectId': '',
            'beforeId': projectId,
            'projectName': ''
        },
        error: function (s) {
            cbms.closeDialog();
        },
        success: function (result) {
            cbms.closeDialog();
            if (result) {
                if (result.success) {
                    cbms.alert("删除项目成功", function () {
                        queryAllprojectList();
                        searchData();


                    });
                } else {
                    cbms.alert(result.data);
                }
            } else {
                cbms.alert("删除项目失败");
            }
        }
    });
}

