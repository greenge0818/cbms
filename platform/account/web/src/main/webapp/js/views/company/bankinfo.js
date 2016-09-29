var dt;
var _imgFlag = false;
jQuery(function ($) {
    //initiate dataTables plugin
    dt = $('#dynamic-table')
        .DataTable({
            "dom": 'lrTt<"bottom"p>i<"clear">',
            paging: false, //禁用分页功能
            bLengthChange: false, //选择分页的页数功能禁用
            info: false, // 总记录数显示的禁用
            "bScrollCollapse": true,
            searching: false,//搜索功能
            autoWidth: true,
            "serverSide": true,
            "ordering": false,
            ajax: {
                url: Context.PATH + "/company/loadbankinfo.html",
                type: "POST",
                data: function (d) {
                    d.accountId = $("#accountId").val();
                }
            },
            "columnDefs": [{
                "targets": 0,
                "data": null,
                "defaultContent": '<label class="pos-rel"><input name="check" type="radio"><span class="lbl"></span></label>'
            }],
            "aoColumns": [{
                "bSortable": false,
                "sWidth": "60px",
                "sTitle": "默认银行"
            }, {
                "sTitle": "开户行",
                "data": "bankName"
            }, {
                "sTitle": "支行"
                , "data": "bankNameBranch"
            }, {
                "sTitle": "开户银行所在城市"
                , "data": "bankProvinceName"
                , "render": renderCity
            }, {
                "sTitle": "行号"
                , "data": "bankCode"
            }, {
                "sTitle": "银行账号"
                , "data": "bankAccountCode"
            }, {
                "sTitle": "打款资料"
                , "data": "url"
                , "render": renderImg
            }, {
                "sTitle": "状态"
                , "data": "bankDataStatus"
                , "render": renderStatus
            }, {
                "sTitle": "操作"
                , defaultContent: ''
                , "render": renderOpt
            }
            ],
            "aaSorting": [],
            "fnRowCallback": function (nRow, aData, iDataIndex) {
                //默认银行列 默认选择默认银行
                var radio = "";
                if (aData.isDefault == '1') {//默认银行
                    radio = '<label class="pos-rel"><input id="setChecked" name="check" type="radio" value="' + aData.id + '" bank-data-status="' + aData.bankDataStatus + '"><span class="lbl"></span></label>';
                    $("td:eq(0)", nRow).html(radio);
                    setTimeout(function () {
                        $("#setChecked").prop("checked", true);
                    }, 300);
                } else {
                    radio = '<label class="pos-rel"><input name="check" type="radio" value="' + aData.id + '" bank-data-status="' + aData.bankDataStatus + '"><span class="lbl"></span></label>';
                    $("td:eq(0)", nRow).html(radio);
                }
                return nRow;
            }
            , "fnInitComplete": function (settings) {
            }
        });

    //添加银行信息弹框
    $(document).on('click', '.tianjia span', function () {
        cbms.getDialog("添加银行信息", "#tanktianjia");
        bindRegionData($("#bankProvinceId"), $("#bankCityId"), null);
        $("#bankForm").verifyForm();
        
        var imgSrc = $("#turnImg").attr("src");
        if ( imgSrc == "") {
        	$("#turnImg").css({'width':'0','height':'0'});
        	$("span.may label.marginbottom").addClass('marginleft0');
        } else{
        	$("#turnImg").css({'width':'50','height':'50'});
        	$("span.may label.marginbottom").removeClass('marginleft0');
        }
    });

    //编辑银行信息弹框
    $(document).on('click', '.bank-edit', function () {
        var bankId = $(this).attr("bankid");
        var url = Context.PATH + '/company/' + bankId + '/getbankinfo.html';
        $.get(url, function (result) {
            cbms.getDialog("编辑银行信息", "#tanktianjia");
            $("#bankForm").verifyForm();

            if (result) {
                if (result.success) {
                    var data = result.data;
                    bindRegionData($("#bankProvinceId"), $("#bankCityId"), null, data.bankProvinceId || '', data.bankCityId || '177', null);
                    $("#bankId").val(bankId);
                    $("#bankName").val(data.bankName);
                    $("#bankNameBranch").val(data.bankNameBranch);
                    $("#bankCode").val(data.bankCode);
                    $("#bankAccountCode").val(data.bankAccountCode);

                    if (data.url && data.url.length > 0) {
                        _imgFlag = true;
                        $("#turnImg").attr("src", Context.PATH + "/common/getfile.html?key=" + data.url);
                        $("#turnImg").css({'width':'50','height':'50'});
        				$("span.may label.marginbottom").removeClass('marginleft0');
                        $("#fileInfo").text("");
                        $("#oldImgUrl").val(data.url);
                    }
                }
            }
        });
    });

    //删除银行信息弹框
    $(document).on('click', '.bank-delete', function () {
        var bankId = $(this).attr("bankid");
        var accountId = $("#accountId").val();
        var url = Context.PATH + '/company/'+ accountId +'/' + bankId + '/deletelogicallybankinfo.html';
        cbms.confirm("确定删除本条银行账号信息吗？", null, function () {
            $.get(url, function (result) {
                if (result) {
                    if (result.success) {
                        cbms.gritter("删除成功！", true, function () {
                            dt.ajax.reload();
                        });
                    } else {
                        cbms.gritter(result.data, false);
                    }
                }
            });
        });
    });

    //we put a container before our table and append TableTools element to it
    $('.dataTables_length').after($('.toolsbar'));

    //单击显示大图片
    $(document).on('click', '.imgbigger', function () {
        var con = '<img src="' + $(this).attr("src") + '" width="400" height="200"/>';
        cbms.getDialog(false, con);
    });

    //设置默认银行
    $(document).on("click", "input[name='check']", function () {
        var $radio = $(this);
        var status = $radio.attr("bank-data-status");
        //审核通过的才可以设置默认银行
        if (status == 'Approved') { //mouseDown
            //请求
            var url = Context.PATH + '/company/setdefaultbank.html';
            $.post(url, {
                bankId: $radio.val(),
                accountId: $("#accountId").val()
            }, function (result) {
                if (result) {
                    if (result.success) {
                        cbms.gritter("客户默认银行设置成功！", true, function () {
                            dt.ajax.reload();
                        });
                    } else {
                        cbms.gritter(result.data, false, function () {
                            dt.ajax.reload();
                        });
                    }
                }
            });
        } else {
            //不是审核通过的，不让选中，事件取消
            return false;
        }
    });

    //保存银行信息
    $(document).on("click", "#saveBankBtn", function () {
        //check data
        if (!CheckData()) {
            return;
        }
        ;
        var btn = $(this);
        btn.attr("disabled", "true");
        var options = {
            type: 'POST',
            data: {
                "id": $("#bankId").val(),
                "bankName": $("#bankName").val(),
                "bankNameBranch": $("#bankNameBranch").val(),
                "bankProvinceId": $("#bankProvinceId").val(),
                "bankProvinceName": $("#bankProvinceId").children(":selected").text(),
                "bankCityId": $("#bankCityId").val(),
                "bankCityName": $("#bankCityId").children(":selected").text(),
                "bankCode": $("#bankCode").val(),
                "bankAccountCode": $("#bankAccountCode").val(),
                "accountId": $("#accountId").val(),
                "url": $("#oldImgUrl").val()
            },
            error: function (s) {
                btn.removeAttr("disabled");
            },
            success: function (result) {
                btn.removeAttr("disabled");
                if (result) {
                    if (result.success) {
                        cbms.gritter("保存成功", true, function () {
                            dt.ajax.reload();
                            cbms.closeDialog();
                        });
                    } else {
                        cbms.alert(result.data);
                    }
                } else {
                    cbms.gritter("保存失败", false);
                }
            }
        };
        $("#bankForm").ajaxSubmit(options);
    });


    //图片改变事件
    $(document).on("change", "#tianjiaz", function () {
        if (this.files.length > 0) {
            if (!CheckExt(this.files[0]) || !CheckProperty(this.files[0])) {
                _imgFlag = false;
                $("#fileInfo").text("未选择任何文件");
            } else {
                _imgFlag = true;
                $("#fileInfo").text("");
                $("#turnImg").css({'width':'50','height':'50'});
        		$("span.may label.marginbottom").removeClass('marginleft0');
            }
        }

    });

});

function CheckData() {
    if (!_imgFlag) {
        cbms.alert("没有上传附件或附件不符合规定！");
        return false;
    }
    if (!setlistensSave())return false;

    if($.trim($("#bankAccountCode").val()).length < 4 ){
        cbms.alert("请输入正确的银行卡号");
        return false;
    }
    return true;
}

function renderStatus(data, type, row, meta) {
    if (data == 'Requested') {
        return "待审核";
    }
    if (data == 'Approved') {
        return "审核通过";
    }
    if (data == 'Declined') {
        return "<em class='red'>审核未通过</em>";
    }
    if (data == 'Insufficient') {
        return "未上传";
    }
    return '-';
}

function renderOpt(data, type, row, meta) {
    return '<a href="javascript:;" class="bank-edit dkk" bankid="' + row.id + '">编辑</a><a href="javascript:;" class="bank-delete" bankid="' + row.id + '">删除</a>';
}

function renderImg(data, type, row, meta) {
    if (row.url) {
        var reqestUrl = Context.PATH + "/common/getfile.html?key=" + row.url;
        return '<img class="imgbigger lik" src="' + reqestUrl + '" />';
    }
    return "";
}

function renderCity(data, type, row, meta) {
    var provinceName = data || '';
    var cityName = row.bankCityName || '';
    return provinceName + "&nbsp;" + cityName;
}


/*********图片相关 S**********/
var AllowExt = ".jpeg|.jpg|.png|.gif|.bmp|" //允许上传的文件类型 ?为无限制 每个扩展名后边要加一个"|" 小写字母表示
var AllowImgFileSize = 2048;    //允许上传图片文件的大小 0为无限制 单位：KB


/**
 * 从 file 域获取 本地图片 url
 */
function getFileUrl(sourceId) {
    var url;
    if (navigator.userAgent.indexOf("MSIE") >= 1) { // IE
        url = document.getElementById(sourceId).value;
    } else if (navigator.userAgent.indexOf("Firefox") > 0) { // Firefox
        url = window.URL.createObjectURL(document.getElementById(sourceId).files.item(0));
    } else if (navigator.userAgent.indexOf("Chrome") > 0) { // Chrome
        url = window.URL.createObjectURL(document.getElementById(sourceId).files.item(0));
    }
    return url;
}

/**
 * 将本地图片 显示到浏览器上
 */
function preImg(sourceId, targetId) {
    var url = getFileUrl(sourceId);
    var imgPre = document.getElementById(targetId);
    imgPre.src = url;
}

function CheckProperty(file)    //检测图像属性
{
    var ErrMsg = "";
    var ImgFileSize = Math.round(file.size / 1024 * 100) / 100;//取得图片文件的大小
    if (AllowImgFileSize != 0 && AllowImgFileSize < ImgFileSize) {
        ErrMsg = "请上传小于" + AllowImgFileSize + "KB的文件";
        cbms.alert(ErrMsg);
        return false;
    }
    else
        return true;
}
function CheckExt(file) {
    var ErrMsg = "";
    if (file.name == "") return false;
    var FileExt = file.name.substr(file.name.lastIndexOf(".")).toLowerCase();
    if (AllowExt != 0 && AllowExt.indexOf(FileExt + "|") == -1) //判断文件类型是否允许上传
    {
        ErrMsg = "不允许上传的文件类型" + FileExt;
        cbms.alert(ErrMsg);
        return false;
    }
    else
        return true;
}
/*********图片相关 E**********/

