/**
 * Created by caochao on 2016/9/14.
 */

$(document).ready(function () {

    $("#releaseBtn").addClass("none");  //隐藏发布按钮
    $("#btn_BatchUpdate").removeClass("none");   //显示批量更新功能
    $("#add_base_price").addClass("none"); //隐藏添加按钮

    $(document).on("click", "#dynamic-table .update_base", function () { //修改基价
        var edit_dialog = $("#determine");
        edit_dialog.find("#region").attr("readonly", "readonly"); //地区不可修改
        edit_dialog.find("#baseName").attr("readonly", "readonly"); //基价名称不可修改
        edit_dialog.find("#typeName").attr("readonly", "readonly"); //类别不可修改
        edit_dialog.find("#category").attr("readonly", "readonly"); //品名不可修改
        edit_dialog.find("#factory").attr("readonly", "readonly"); //钢厂不可修改
        edit_dialog.find("#edit_txtremark").attr("readonly", "readonly"); //备注不可修改
    });

    $(document).on("click", "#btn_BatchUpdate", function () {
        var html = Context.PATH + '/smartmatch/quote/release.html';
        cbms.getDialog("批量更新", html, null, dialogReady, 1);

        $(document).on("click", "#releaseClose", function () {
            cbms.closeDialog();
        });

        $(document).off('click', '#releaseCommit');

        $(document).on('click', '#releaseCommit', function () {
            cbms.loading();
            //发布报价：
            var forms = setlistensSave("#form-horizontal");
            if (!forms) {
                cbms.closeLoading();
                return;
            }
            //统计选中的信息
            var basePriceIds = []; //基价ID集合
            var checked = $("input[name='check']:checked").not("#allCheck");
            var releaseDateList = [];
            $(checked).each(function () {
                var id = "";
                var price = "";
                var releaseDate = {
                    id: $(this).val(),
                    price: $(this).closest("tr").find(".release_base").val()
                }
                releaseDateList.push(releaseDate);
                var basePriceId = $(this).val();
                basePriceIds.push(basePriceId);
            });
            if (basePriceIds == null || basePriceIds.length == 0) {
                cbms.closeLoading();
                cbms.alert("请选择要更新的信息");
                return;
            }
            $.ajax({
                url: Context.PATH + '/smartmatch/basePriceRelation/refreshquote.html',
                type: "POST",
                data: {
                    "releaseDateList": JSON.stringify(releaseDateList)
                },
                success: function (result) {
                    if (result.success) {
                        cbms.alert("更新成功！", function () {
                            location.reload();
                        });
                    } else {
                        cbms.alert(result.data);
                    }
                    cbms.closeLoading();
                }
            });
        });
    });

    has_delete_base_btn = false; //移除删除功能
});

function dialogReady() {
    $("#releaseCommit").text("更新");
    fillDataTable();
}


