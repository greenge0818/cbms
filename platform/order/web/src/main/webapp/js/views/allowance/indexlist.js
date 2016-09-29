var _listdataDt;

$().ready(function () {
    var type = $("#list_type").val();
    initTable(type);
    $("#queryBtn").click(function () {
        _listdataDt.ajax.reload();
    });

    $("body").on('click', "#listdatabody a[deleteallowance]", function () {
        var allowanceid = $(this).attr("deleteallowance");
        if (allowanceid) {
            cbms.confirm("确定要删除吗?",allowanceid,function(aid){
                $.ajax({
                    url: Context.PATH + '/allowance/deleteAllowance.html',
                    type: 'POST',
                    data: {
                        id: aid
                    },
                    success: function (re) {
                        if (re.success) {
                            cbms.alert("删除成功！", function () {
                                _listdataDt.ajax.reload();
                            });
                        }
                        else {
                            cbms.alert(re.data);
                        }
                    },
                    error: function () {

                    }
                });
            });
        }
    });
    $("body").on('click', "#listdatabody a[generateAllowance]", function () {
        var allowanceid = $(this).attr("generateAllowance");
        if (allowanceid) {
            cbms.confirm("确定要生成买家折让单吗?",allowanceid,function(aid){
                cbms.loading();
                $.ajax({
                    url: Context.PATH + '/allowance/savebuyerallowances.html',
                    type: 'POST',
                    data: {
                        allowanceId: aid
                    },
                    success: function (re) {
                        cbms.closeLoading();
                        if (re.success) {
                            cbms.alert("生成成功！", function () {
                                _listdataDt.ajax.reload();
                            });
                        }
                        else {
                            cbms.alert(re.data);
                        }
                    },
                    error: function () {
                        cbms.closeLoading();
                        cbms.alert("删除失败！");
                    }
                });
            });
        }
    });
});

// 查找订单信息
function initTable(type) {
    _listdataDt = $('#list-table').DataTable({
        ajax: {
            url: Context.PATH + '/allowance/listdata/' + type + '.html',
            type: 'POST',
            data: function (d) {
                d.allowanceCode = $("#allowance_code").val();
                d.accountId = $("#accountid").attr("accountid");
                d.status = $("#select_status").val();
                d.sdate = $("#startTime").val();
                d.edate = $("#endTime").val();
            }
        },
        columns: [
            {data: 'allowanceCode'},//折让单号
            {data: 'created'},//时间
            {data: 'accountName'},//卖/买家全称
            {data: 'totalQuantity'},//件数
            {data: 'totalWeight'},//总重量
            {data: 'actualTotalWeight'},//实提总重量
            {data: 'allowanceTotalWeight'},//折扣重量
            {defaultContent: ''},//折后重量
            {data: 'totalAmount'},//总金额
            {data: 'actualTotalAmount'},//实提总金额
            {data: 'allowanceTotalAmount'},//折扣金额
            {data: 'actualTotalWeight'},//折后金额
            {data: 'status'},//状态
            {defaultContent: ''}//操作
        ],
        columnDefs: [
            {
                "targets": 0, //第几列 从0开始，折让单号
                "data": "code",
                "render": function (data, type, full, meta) {
                    return "<a href='"+Context.PATH +"/allowance/detail/"+full.id+".html'>"+data+"</a>";
                }
            },
            {
                "targets": 2, //第几列 从0开始，时间
                "data": "code",
                "render": function (data, type, full, meta) {
                    return getDepartmentName(data,full);
                }
            },
            {
                "targets": 1, //第几列 从0开始，时间
                "data": "code",
                "render": function (data, type, full, meta) {
                    return dateFormat(new Date(data), "yyyy-MM-dd");
                }
            },
            {
                "targets": 4, //第几列 从0开始，总重量
                "data": "code",
                "render": function (data, type, full, meta) {
                    return data.toFixed(6);
                }
            },
            {
                "targets": 5, //第几列 从0开始，实提总重量
                "data": "code",
                "render": function (data, type, full, meta) {
                    return data.toFixed(6);
                }
            },
            {
                "targets": 8, //第几列 从0开始，总金额
                "data": "code",
                "render": function (data, type, full, meta) {
                    return formatMoney(data,2);
                }
            },
            {
                "targets": 9, //第几列 从0开始，实提总金额
                "data": "code",
                "render": function (data, type, full, meta) {
                    return formatMoney(data,2);
                }
            },
            {
                "targets": 10, //第几列 从0开始，折扣金额
                "data": "code",
                "render": function (data, type, full, meta) {
                    return formatMoney(data,2);
                }
            },
            {
                "targets": 11, //第几列 从0开始，折后金额
                "data": "code",
                "render": function (data, type, full, meta) {
                    return formatMoney(full.actualTotalAmount + full.allowanceTotalAmount);
                }
            },
            {
                "targets": 6, //第几列 从0开始，折扣重量
                "data": "code",
                "render": function (data, type, full, meta) {
                    return data.toFixed(6);
                }
            },
            {
                "targets": 7, //第几列 从0开始，折后重量
                "data": "code",
                "render": function (data, type, full, meta) {
                    return (full.actualTotalWeight + full.allowanceTotalWeight).toFixed(6);
                }
            },
            {
                "targets": 13, //第几列 从0开始，操作
                "data": "code",
                "render": function (data, type, full, meta) {
                    return renderOperation(data, full);
                }
            }
        ],
        serverSide: true, //服务模式
        processing: true,//显示数据加载进度
        searching: false, //是否启用搜索
        ordering: false, //是否启用排序
        lengthChange: false, //不显示pageSize的下拉框
        oLanguage: {
            sUrl: Context.PATH + "/js/DT_zh.txt"
        }, //自定义语言包
        bFilter: false,
        iDisplayLength: 15,
        bLengthChange: false
    });
}

//买家/卖家全称显示，如果有多个部门则显示部门，否则不显示部门
function getDepartmentName(data, full) {
	var custName = "";
	if (full.departmentCount > 1) {
		custName = data+"【"+full.departmentName+"】";
	} else {
		custName = data;
	}
	return custName;
}

function renderOperation(data, full) {
    var type = $("#list_type").val();
    var allowanceid = full.id;
    var html = "";
    var permission_generate = $("#permission_generate").val();
    var permission_approval = $("#permission_approval").val();
    var permission_cancel = $("#permission_cancel").val();
    var permission_delete = $("#permission_delete").val();
    var permission_edit = $("#permission_edit").val();
    if (!full.buyerGenerate && permission_generate == 'true') {
        html += "<a href='javascript:void(0);' generateAllowance='"+allowanceid+"'>生成买家折让单</a><br /> ";
    }
    if (full.status == '待提交' || full.status == '未通过') {
        if (permission_edit == 'true') {
            html += "<a href='" + Context.PATH + "/allowance/" + allowanceid + "/edit" + type + ".html'>编辑</a> ";
        }
        if (permission_delete == 'true') {
            html += "<a href='javascript:void(0)' deleteallowance='" + allowanceid + "'>删除</a> ";
        }
    }
    if (full.status == '待审核') {
        if (permission_approval == 'true') {
            html += "<a href='" + Context.PATH + "/allowance/auditrebate/"+ allowanceid +".html'>审核</a> ";
        }
    }
    if (full.status == '已审核') {
        if (permission_cancel == 'true') {
            html += "<a href='" + Context.PATH + "/allowance/auditrebate/"+ allowanceid +".html'>取消审核通过</a> ";
        }
    }
    if (full.status == '未通过') {

    }
    if (full.status == '已关闭') {

    }
    return html;
}


function dateFormat(date, fmt) {
    var o = {
        "M+": date.getMonth() + 1,                 //月份
        "d+": date.getDate(),                    //日
        "h+": date.getHours(),                   //小时
        "m+": date.getMinutes(),                 //分
        "s+": date.getSeconds(),                 //秒
        "q+": Math.floor((date.getMonth() + 3) / 3), //季度
        "S": date.getMilliseconds()             //毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

