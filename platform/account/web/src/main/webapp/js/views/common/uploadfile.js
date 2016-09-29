/**
 * 上传图片
 * Created by lichaowei on 2016/1/20.
 * 示例：

 // 使用，在上传文件的控件上添加一个属性 upload='file'
 <input type='file' id='demo' upload='file' />

 * $().ready(function () {
        // 上传成功以后，获取当前图片的地址
        var url=$("#demo").attr("url");
   });
 */
var _allowExt = ".jpeg|.jpg|.png|.gif|.bmp|"; //允许上传的文件类型 ?为无限制 每个扩展名后边要加一个"|" 小写字母表示
var _allowImgFileSize = 2048;    //允许上传图片文件的大小 0为无限制 单位：KB

$().ready(function () {

    $(document).on("change", "input[type='file'][upload='file']", function () {
        uploadFile(this);
    });
});


// 检测图像属性
function checkProperty(file) {
    var ErrMsg = "";
    var ImgFileSize = Math.round(file.size / 1024 * 100) / 100;//取得图片文件的大小
    if (_allowImgFileSize != 0 && _allowImgFileSize < ImgFileSize) {
        ErrMsg = "请上传小于" + _allowImgFileSize + "KB的文件";
        gritter(ErrMsg);
        return false;
    }
    else
        return true;
}
function checkExt(file) {
    var ErrMsg = "";
    if (file.name == "") return false;
    var FileExt = file.name.substr(file.name.lastIndexOf(".")).toLowerCase();
    if (_allowExt != 0 && _allowExt.indexOf(FileExt + "|") == -1) //判断文件类型是否允许上传
    {
        ErrMsg = "不允许上传的文件类型" + FileExt;
        gritter(ErrMsg);
        return false;
    }
    else
        return true;
}

/**
 * 上传文件
 * @param fileElement 上传文件控件
 */
function uploadFile(fileElement) {
    var currentParent = $(fileElement).parent();
    if (fileElement.files.length > 0) {
        if (checkExt(fileElement.files[0]) && checkProperty(fileElement.files[0])) {

            var form = $("<form>");
            form.attr('style', 'display:none');
            form.attr('target', '');
            form.attr('method', 'post');
            form.attr('enctype', 'multipart/form-data');
            form.attr('action', Context.PATH + "/common/uploadfile.html");
            $(fileElement).attr('name', 'uploadFile');
            $('body').append(form);
            form.append(fileElement);

            var input1 = $('<input>');
            input1.attr('type', 'hidden');
            input1.attr('name', 'type');
            input1.attr('value', $(fileElement).attr("pictype"));
            form.append(input1);

            var options = {
                type: "POST",
                success: function (re) {
                    if (re) {
                        if (re.success) {
                            var imgUrl = Context.PATH + '/common/getfile.html?key=' + re.data;
                            $(currentParent).find(".imgbigger").attr("src",imgUrl);
                            $(currentParent).find(".imgbigger").closest("a").attr("href",imgUrl).show();
                            //var uploadImgId = $(fileElement).attr("id") + "Img";
                            //if ($("#" + uploadImgId).size() == 0) {
                            //    var uploadImg = "<img style='width:80px;height:80px;' id='" + uploadImgId + "' src='" + imgUrl + "' />";
                            //    $(currentTd).append(uploadImg);
                            //}
                            //else {
                            //    $("#" + uploadImgId).attr("src", imgUrl);
                            //}
                            $(fileElement).attr("url", re.data);
                        }
                        else {
                            gritter(re.data);
                        }
                    }
                    else {
                        gritter("上传失败，服务器异常");
                    }
                    $(currentParent).append(fileElement);
                    form.remove();
                },
                error: function (re) {
                    gritter("上传失败，服务器异常");
                    $(currentParent).append(fileElement);
                    form.remove();
                }
            };
            $(form).ajaxSubmit(options);
        }
    }
}

function gritter(msg) {
    $.gritter.add({
        title: '',
        text: msg,
        class_name: 'gritter-item-wrapper gritter-info gritter-center gritter-light'
    });
}