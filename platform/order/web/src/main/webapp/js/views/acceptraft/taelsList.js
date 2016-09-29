var dt;
jQuery(function () {
    initTable();


    $(document).on("click", "#search", function () {
        dt.ajax.reload();
    });

    $("#exportBtn").on("click", function () {
        exportAccepTraft();
    });
    $("#exportPut").on("click", function () {
        var file = $("#acceptDraftExcel")
        file.after(file.clone().val(""));
        file.remove();
        uploading();
    });


    $(window).resize(function(){
            init();
    });


});
function init(){
    var w = $(".main-content-inner").width();
    $("#table-bar").width(w-30);
}

function uploading(){
    var form = $("<form>");
    form.attr('style', 'display:none');
    form.attr('target', '');
    form.attr('method', 'post');
    form.attr('action', Context.PATH + "/acceptdraft/uploading.html");

    var input1 = $('<input>');
    input1.attr('type', 'file');
    input1.attr('name', 'file');
    input1.attr('value', $("#file").val());


    $('body').append(form);
    form.append(input1);


    form.submit();
    form.remove();

}

//上传EXCEL
function uploading() {
    $("#acceptDraftExcel").click();
}

function uploadingOk(){
      $("#searchForm").ajaxSubmit({
      type: 'POST',
      url: Context.PATH + "/acceptdraft/uploadingExcel.html",
      dataType:"JSON",
      success: function(data){
          if (data) {
              cbms.closeLoading();
              if (data.success) {
                  cbms.alert("上传成功！");
                  location.reload();
              } else {
                  cbms.alert(data.data);
              }
          }
      }
      });

}


function uploadingExcel(){
    var exeStr=$("#acceptDraftExcel").val();
  	if(exeStr == ""){
        cbms.alert("请先选择要上传的EXCEL文件!");
        return false;
    }
    var exeEx=exeStr.substring(exeStr.lastIndexOf("."));
    if (!/\.(xls|xlsx|xlsm|XLS|XLSX|XLSM)$/.test(exeEx)) {
        cbms.alert("请先选择正确的EXCEL文件!！！");
        return false;
    }
    cbms.loading();

  	$("#searchForm").ajaxSubmit({
        type: 'POST',
        url: Context.PATH + "/acceptdraft/uploading.html",
        dataType:"JSON",
        success: function(data){
            if (data) {
                cbms.closeLoading();
                if (data.success) {
                    if(data.data>0){
                        cbms.confirm("有"+data.data+"条数据已存在，是否覆盖？",null,uploadingOk);
                    }else{
                        cbms.alert("上传成功");
                        location.reload();
                    }
                } else {
                    cbms.alert(data.data);
                }
            }
        }
    });
}
function exportAccepTraft(){
    var draftListCheck = $(".draftcheck");
    var rows=new Array();
    for(var i=0;i<draftListCheck.length;i++){
        if(draftListCheck.eq(i).is(":checked")){
            rows[i]=1;
        }else{
            rows[i]=0;
        }
    }
    var codeIds = getCodeIds();//
    var msg = "";
    if(codeIds == '' || codeIds == undefined){
        msg = "全部";
    }else{
        msg = "选中";
    }
    cbms.confirm("确认导出" + msg + "数据？", null, function () {
        exportExcel(codeIds,rows)
    });
}

// 导出EXCEL
function exportExcel(codeIds,rows){
    var form = $("<form>");
    form.attr('style', 'display:none');
    form.attr('target', '');
    form.attr('method', 'post');
    form.attr('action', Context.PATH + "/acceptdraft/exportexcel.html");
    // code
    var input1 = $('<input>');
    input1.attr('type', 'hidden');
    input1.attr('name', 'code');
    input1.attr('value', $("#code").val());
    // 开始时间
    var input2 = $('<input>');
    input2.attr('type', 'hidden');
    input2.attr('name', 'startTime');
    input2.attr('value', $("#startTime").val());
    // 结束时间
    var input3 = $('<input>');
    input3.attr('type', 'hidden');
    input3.attr('name', 'endTime');
    input3.attr('value', $("#endTime").val());

    var input4 = $('<input>');
    input4.attr('type', 'hidden');
    input4.attr('name', 'codeIds');
    input4.attr('value', codeIds);

    var rowstr= rows.join("-");
    var input5 = $('<input>');
    input5.attr('type', 'hidden');
    input5.attr('name', 'rowstr');
    input5.attr('value', rowstr);


    $('body').append(form);
    form.append(input1);
    form.append(input2);
    form.append(input3);
    form.append(input4);
    form.append(input5);

    form.submit();
    form.remove();
}

function initTable() {
    var url = Context.PATH + "/acceptdraft/getTaelsData.html";
    dt = jQuery("#forShow").on('page.dt',
        function() {
            setTimeout(getName,50);
        }).DataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "pageLength": 100,
        "ajax": {
            "url": url
            , "type": "POST"
            , data: function (d) {
                return $.extend({}, d, {
                    dateStartStr: $("#startTime").val(),
                    dateEndStr: $("#endTime").val(),
                    code: $("#code").val()
                });
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'code'},
            {data: 'amount', sClass: "text-right"},
            {data: 'acceptanceDate'},
            {data: 'endDate'},
            {data: 'acceptanceBankCode'},
            {data: 'acceptanceBankFullName'},
            {data: 'drawerName'},
            {data: 'drawerAccountCode'},
            {data: 'drawerBankCode'},
            {data: 'drawerBankFullName'},
            {data: 'receiverName'},
            {data: 'receiverAccountCode' },
            {data: 'receiverBankFullName'},
            {data: 'adjustDateCount'},
            {data: 'readTimes'},
            {data: 'acceptanceAgreementNumber'},
            {data: 'isDifferentBank'},
            {defaultContent: ''}
        ]


        , fnRowCallback: function (nRow, aData, iDataIndex) {
            var inputHtml = "<label><input type='checkbox' class='ace check-box' name='check'  value='" + aData.id + "'><span class='lbl'></span></label>";
            $('td:eq(0)', nRow).html(inputHtml);
            $('td:eq(1)', nRow).html(aData.code);
            var amountFormat = parseFloat(aData.amount);
            $('td:eq(2)', nRow).addClass("text-right").html(formatMoney((amountFormat), 2)).addClass("text-right");
            $('td:eq(3)', nRow).html(renderTime(aData.acceptanceDate));
            $('td:eq(4)', nRow).html(renderTime(aData.endDate));
            $('td:eq(5)', nRow).html(aData.acceptanceBankCode);
            $('td:eq(6)', nRow).html(aData.acceptanceBankFullName);
            $('td:eq(7)', nRow).html(aData.drawerName);
            $('td:eq(8)', nRow).html(aData.drawerAccountCode);
            $('td:eq(9)', nRow).html(aData.drawerBankCode);
            $('td:eq(10)', nRow).html(aData.drawerBankFullName);
            $('td:eq(11)', nRow).html(aData.receiverName);
            $('td:eq(12)', nRow).html(aData.receiverAccountCode);
            $('td:eq(13)', nRow).html(aData.receiverBankFullName);
            $('td:eq(14)', nRow).html(aData.adjustDateCount);
            $('td:eq(15)', nRow).html(aData.readTimes);
          $('td:eq(16)', nRow).html(aData.acceptanceAgreementNumber);
            var isDifferentBankName = aData.isDifferentBank;
            if(isDifferentBankName == null || isDifferentBankName == "") {
                isDifferentBankName = "否";
            }
            $('td:eq(17)', nRow).html(isDifferentBankName);
            return nRow;
        }
        , footerCallback: function(row, data){
            $("#count").html(data.length);
            $("#amount").html(pageTotalColumn(data, 'amount'));
        }
    });
}

/** Tools  **/
function renderPercent(data){
    return formatMoney(data, 6);
}

function getName(){
    var draftListCheck = $(".draftcheck");//draftcheck
    for(var i=0;i<draftListCheck.length;i++){
        if(draftListCheck.eq(i).is(":checked")){
          var num = draftListCheck.eq(i).attr("id");
          setOutCol(document.getElementById('forShow'),parseInt(draftListCheck.eq(i).attr("id")));
        }else{
          setHiddenCol(document.getElementById('forShow'),parseInt(draftListCheck.eq(i).attr("id")));
        }
    }
}

// 没选中的隐藏
function setHiddenCol(oTable,iCol) {
    for (i=0;i < oTable.rows.length ; i++){
       $(oTable.rows[i].cells[iCol]).hide();
    }
}

// 选中的显示出来
function setOutCol(oTable,iCol) {
    for (i=0;i < oTable.rows.length ; i++){
        $(oTable.rows[i].cells[iCol]).show();
    }
}

$(".draftcheck").click(function(){
    if($(this).attr("checked")=="checked"){
        $(this).removeAttr("checked");
    }else{
        $(this).attr("checked","checked");
    }
})

// 全选
function jselectAll(){
    var isf = true;
    $(".draftcheck").each(function(i){
        if($(".draftcheck").eq(i).prop("checked")==false){
            isf = false;
        }
    })
    if (isf == true) {
        $(".draftcheck").removeAttr("checked");
    }else{
        $(".draftcheck").prop("checked",true);
    }
}

function renderTime(data, type, full, meta){
    return date2String(new Date(data));
}

//js将Date类型转换为String类型：
function date2String(aDate){
    var year=aDate.getFullYear();
    var month=aDate.getMonth();
    month++;
    var mydate=aDate.getDate();
    var hour=aDate.getHours();
    var minute=aDate.getMinutes();
    var mytime=year+"-"+(month<10?"0":"")+month+"-"+(mydate<10?"0":"")+mydate;
    return mytime;
}

function renderOperation(data){
    var operation = '';
    return operation;
}

function pageTotalColumn(data, columnName) {
    var total = 0;
    var temp;
    for (var i = 0; i < data.length; i++) {
        temp = parseFloat(data[i][columnName]);
        if (!isNaN(temp)) {
            total += parseFloat(temp.toFixed(2));
        }
    }
    return formatMoney(total, 2);
}

// 统计银票
function invoiceTotal() {
    var checkedCount = selectIds.length;
    $('#checkCount').text(checkedCount);
}

// 全选/全不选
$("#allCheck").click(function () {
    var checked = $(this).is(':checked');
    // 取消全选
    var checkebox = $("input[name='check']");
    if(!checked) {
        checkebox.removeAttr("checked");
        $(this).removeAttr("checked");
    }else {
        checkebox.prop('checked', true);
        $(this).prop('checked', true);
    }
    invoiceTotal();
});

// 单选
$("body").on("click", "input[name='check']", function () {
    var checked = $(this).is(':checked');
    if(!checked) {
        $(this).removeAttr("checked");
        $("#allCheck").removeAttr("checked");   // 取消全选
    }else {
        $(this).prop('checked', true);
    }
    // 如果全部选中，那么全选checkbox选中
    var checkCount = $("input[name='check']").length;
    var checkedCount = $("input[name='check']:checked").length;
    if (checkCount == checkedCount) {
        $("#allCheck").prop('checked', true);
    }
    invoiceTotal();
});

function getCodeIds(){
    var codeIds = "";
    $("input[name='check']:checkbox:checked").each(function(){
        codeIds += $(this).val() + ","
    });
    codeIds = codeIds.substr(0, codeIds.length - 1);
    return codeIds;
}
