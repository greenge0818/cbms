/**
 * Created by chenchen on 2015/8/3.
 */


    jQuery(function ($) {
        //绑定确认方法
    $('#sure').click(function () {
        cbms.loading();
        $('#savaReward').ajaxSubmit({
            success: function (data) {
                cbms.closeLoading();
                cbms.alert(data.success ? "设置提成比例成功，下月生效" : "设置提成比例失败");
            }
        })
    })
    $(document).on("click", ".delectRatio",function (e){
            var a=$(this);
            cbms.confirm("是否确定删除？？", null, function () {
                delectRatio(a)
            });
        }
        );
    $(document).on("blur", ".ratio-region:odd",function (e){
            var a=parseFloat($(this).val());
            var b=$(".ratio-region").index(this);
            if(b>2){
             var c= parseFloat($(".ratio-region").eq(b-1).val());
            if(a<c){
                cbms.alert("请检查数值区间是否有误！")
                return
            }
            }
            $(".ratio-region").eq(b+1).val(a);

        }
    );

    $("#addRatio").click(function () {
        var  minLimit= $("[name='minLimit']:last").val()
        var  maxLimit= $("[name='maxLimit']:last").val()
        var  buyRadio= $("[name='buyRadio']:last").val()

        if(minLimit==null||minLimit==""){
         cbms.alert("请输入合法的最低值！")
        return}
        if(maxLimit==null||maxLimit==""){
        cbms.alert("请输入合法的最高值!")
            return}
        if(buyRadio==null||buyRadio==""){
        cbms.alert("请输入合法的买家采购量系数!")
            return}


        var html ="<tr class='bolder-b-dashed lineRatio' >"+
        "<td><input type='text'value='"+maxLimit+"' class='c-text ratio-region' name='minLimit' /></td>"+
            "<td><input type='text'value='' class='c-text ratio-region' name='maxLimit'/></td>"+
            "<td><input type='text'value='' class='c-text'name='buyRadio' /></td>"+
            "<td>&nbsp;&nbsp;<a  href='javascript:;' class='delectRatio'>删除</a></td>"+"</tr>";
        $("#lineRatio").append(html);
    });
});

function delectRatio(a){
    a.parent().parent().remove();
}

