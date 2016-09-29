/**
 * Created by Rabbit Mao on 2015/7/20.
 */

var dt;
function fillDataTable() {
  dt = $('#dynamicTable').DataTable({
    ajax: {
      url: Context.PATH + '/invoice/out/list.html',
      type: "POST",
      data: function (d) {
        return $.extend({}, d, {
          sbuyerName: $("#sbuyerName").val(),
          sexpressName: $("#sexpressName").val(),
          stimeFrom: $("#stimefrom").val(),
          stimeTo: $("#stimeto").val()
        });
      }
    },
    searching: false,
    processing: true,
    serverSide: true,
    ordering: false,
    bLengthChange: false,
    columns: [
      {data: 'invoiceOut.code'},   //销项发票号
      {data: 'invoiceOut.buyerName'},  //买家全称
      {data: 'invoiceOut.amount'},  //发票金额
      {data: 'invoiceOut.inputUserName'},  //录入人员
      {data: 'invoiceOut.inputUserMobil'},  //联系电话
      {data: 'express.sendTime'},    //寄出时间
      {data: 'express.expressName'},   //快递单号
      {data: 'invoiceOut.status'},    //发票状态
      {defaultContent: '--'}   //操作
    ],
    "oLanguage": {                          //汉化
      "sLengthMenu": "每页显示 _MENU_ 条记录",
      "sZeroRecords": "没有检索到数据",
      "sInfo": "当前数据为从第 _START_ 到第 _END_ 条数据；总共有 _TOTAL_ 条记录",
      "sInfoEmtpy": "没有数据",
      "sProcessing": "正在加载数据...",
      "oPaginate": {
        "sFirst": "首页",
        "sPrevious": "上一页",
        "sNext": "下一页",
        "sLast": "尾页"
      }
    },
    columnDefs: [
      {
        "targets": 2, //第几列 从0开始
        "data": "invoiceOut.amount",
        "render": renderAmount
      },
      {
        "targets": 5, //第几列 从0开始
        "data": "express.sendTime",
        "render": renderTime
      },
      {
        "targets": 7, //第几列 从0开始
        "data": "invoiceOut.status",
        "render": getBankTransactionType
      },
      {
        "targets": 8, //第几列 从0开始
        "data": "invoiceOut.id",
        "render": renderInput
      }
    ]
  });
}

function renderInput(data, type, full, meta){
  return  '<button class="button btn-xs btn-link confirm" rel="'+ data +'">确认</button>';
  //'<button class="button btn-xs btn-link edit" rel="'+ data +'">修改</button>' +

};

function getBankTransactionType(data, type, full, meta){
  var status = '--';
  if(data == '2') status='<span>待确认</span>';
  return status;
};


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
  var mytime=year+"-"+(month<10?"0":"")+month+"-"+(mydate<10?"0":"")+mydate+" "+(hour<10?"0":"")+hour+":"+(minute<10?"0":"")+minute;
  return mytime;
}


function confirm(invoiceOutCode, invoiceOutAmount){
  $.ajax({
    type: "POST",
    url: Context.PATH + '/invoice/out/confirm.html',
    data: {
      invoiceOutCode: invoiceOutCode,
      invoiceOutAmount: invoiceOutAmount
    },
    dataType: "json",
    success: function (response, textStatus, xhr) {
      if(response.success){
        dt.ajax.reload();
        cbms.closeDialog();
        cbms.alert("确认发票成功");
      }else{
        cbms.alert(response.data);
      }
    }
  });
}

jQuery(function($) {

  fillDataTable();

  $("#searchForm").on("click","#search", function() {
    dt.ajax.reload();
  });

  $(document).on("click", "#print", function(){
        $( "#dynamicTable" ).print();
  });

  $(document).on("click","#commit", function() {
    if (!setlistensSave("#check")) {
      return false;
    }
    var invoiceOutCodeHidden = $("#invoiceOutCodeHidden").val();
    var invoiceOutAmountHidden = $("#invoiceOutAmountHidden").val();
    var invoiceOutCode = $("#invoiceOutCode").val();
    var invoiceOutAmount = $("#invoiceOutAmount").val();
    if(!((invoiceOutCodeHidden === invoiceOutCode) && (invoiceOutAmountHidden === invoiceOutAmount))){
      alert("输入的发票号或者金额有误，请联系财务确认。");
      return false;
    }
    confirm(invoiceOutCode, invoiceOutAmount);
  });

  $(document).on("click","#fix", function() {   //暂时不知道规则，先不做
  });

  $(document).on("click","#cancel", function() {
    cbms.closeDialog();
  });


  $(document).on("click", ".confirm", function() {
    var invoiceOutId = $(this).attr("rel");
    $.ajax({
      type: "POST",
      url: Context.PATH + '/invoice/out/selectById.html',
      data: {
        id: invoiceOutId
      },
      dataType: "json",
      success: function (response, textStatus, xhr) {
        if(response.success){
          html = '<div align="center"><form id="check"><input type="hidden" id="invoiceOutCodeHidden"/><input type="hidden" id="invoiceOutAmountHidden"/>' +
          '<input type="text" id="invoiceOutCode" must="1" placeholder="请输入发票号"/><input type="text" id="invoiceOutAmount" placeholder="请输入发票金额" must="1" verify="rmb"/><br/>' +
          '<button class="btn btn-info btn-sm" id="commit">保存</button>&nbsp;<button class="btn btn-info btn-sm" id="cancel">取消</button></from></div>';
          cbms.getDialog("输入销项发票信息", html);
          var data = response.data;
          $("#invoiceOutCodeHidden").val(data.invoiceOut.code);
          $("#invoiceOutAmountHidden").val(data.invoiceOut.amount);
          $("#check").verifyForm();
        }else{
          cbms.alert(response.data);
        }
      }
    });
  });

  $(document).on("click", ".edit", function() {
    var invoiceOutId = $(this).attr("rel");
    $.ajax({
      type: "POST",
      url: Context.PATH + '/invoice/out/selectById.html',
      data: {
        id: invoiceOutId
      },
      dataType: "json",
      success: function (response, textStatus, xhr) {
        if(response.success){
          html = '<div align="center"><input type="text" id="invoiceOutCode" placeholder="请输入发票号"/><input type="text" id="invoiceOutAmount" placeholder="请输入发票金额"/><br/>' +
          '<button class="btn btn-info btn-sm" id="fix">修改</button>&nbsp;<button class="btn btn-info btn-sm" id="cancel">取消</button></div>';
          cbms.getDialog("修改销项发票信息",html);
          var data = response.data;
          $("#idHidden").val(invoiceOutId);
          $("#invoiceOutCode").val(data.invoiceOut.code);
          $("#invoiceOutAmount").val(data.invoiceOut.amount);
        }else{
          cbms.alert(response.data);
        }
      }
    });
  });
});

