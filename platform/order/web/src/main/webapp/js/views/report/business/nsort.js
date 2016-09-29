/**
 * Created by dengxiyan on 2015/8/20.
 * 品类交易报表
 */
jQuery(function ($) {
    //搜索事件
    $("#queryBtn").click(function () {
        submitForm();
    });
});


function submitForm(){
    $("#init").val(false);
    $("#queryForm").submit();
}