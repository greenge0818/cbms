/**
 * Created by prcsteel on 2015/12/25.
 */

$(function(){




    $("#eidtBtn").on("click",function(){
        var arrName=$("#errName").val();
        if(arrName.trim()==""){
            cbms.alert("错误客户名称不能为空！");
            return;
        }
        var correctName=$("#correctName").val();
        if(correctName.trim()==""){
            cbms.alert("正确客户名称不能为空！");
            return;
        }

        $("#searchForm").ajaxSubmit({
            type: 'POST',
            url: Context.PATH + "/sys/maintain/erredit.html",
            dataType:"JSON",
                success: function(data){
                    if (data.success) {
                        cbms.alert(data.data);
                    }else{
                        cbms.alert(data.data);
                    }
                }
            });
    });
    //$("#searchForm").attr("action", Context.PATH + "/sys/maintain/erredit.html");


    //$("#searchForm").ajaxSubmit({
    //    type: 'POST',
    //    url: Context.PATH + "/sys/maintain/erredit.html",
    //    dataType:"JSON",
    //    success: function(data){
    //        if (data.success) {
    //            cbms.alert(data.data);
    //        }else{
    //            cbms.alert(data.data);
    //        }
    //    }
    //});
});
