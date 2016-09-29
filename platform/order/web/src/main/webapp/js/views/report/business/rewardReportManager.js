jQuery(function ($) {
    //搜索事件
    $("#queryBtn").click(function () {
        submitForm();
    });
});


function submitForm(){
    $("#init").val(false);
    $("#searchForm").submit();
}