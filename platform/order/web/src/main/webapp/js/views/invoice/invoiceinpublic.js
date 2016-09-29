/**
 * Created by lcw on 8/5/2015.
 */

/**
 * 字符串转换成 Float 类型
 * @param str 字符串
 * @returns {Float} Float类型，失败为0
 */
function convertFloat(str) {
    var value = 0;
    var tempVal = $.trim(str);
    if (tempVal != "") {
        value = parseFloat(tempVal);
        if (isNaN(value))
            value = 0;
    }
    return value;
}

// 成功返回 Int 值，失败返回0
function convertInt(str) {
    var value = 0;
    var tempVal = $.trim(str);
    if (tempVal != "") {
        value = parseInt(tempVal);
        if (isNaN(value))
            value = 0;
    }
    return value;
}
