/**
 * Created by lcw on 2015/7/16.
 * 参数说明：
 * province        ：省份
 * city            : 城市
 * district        ：地区
 * provinceVal     : 省份值
 * cityVal         ：城市值
 * districtVal     : 地区值
 * provinceId      ：省份ID
 * cityId          : 城市ID
 *
 * 示例：
 * $().ready(function () {
        // 省市联动
        bindRegionData($("#sel_province"),$("#sel_city"));

        // 省市区联动
        bindRegionData($("#sel_province"),$("#sel_city"),$("#sel_district"));

        // 省市联动以及设置省份默认值（省份ID或省份名称）
        bindRegionData($("#sel_province"),$("#sel_city"),null,18);

        // 省市联动以及设置省份默认值和设置城市默认值（省份ID或省份名称，城市ID或城市名称）
        bindRegionData($("#sel_province"),$("#sel_city"),$("#sel_district"),18,"长沙市");

        // 市县联动
        bindCityData($("#sel_city"),$("#sel_district"));

        // 获取城市以及设置城市默认值（城市ID或城市名称），区县以此类推
        bindCityData($("#sel_city")，$("#sel_district"),177);
   });
 */

// 查询所有省份
function getAllProvince() {
    var result = null;
    if (allProvinceCache == undefined || allProvinceCache == null || allProvinceCache == "") {
        $.ajax({
            type: 'post',
            url: Context.PATH + "/common/privincelist.html",
            data: {},
            async: false,
            error: function (s) {
            }
            , success: function (data) {
                if (data) {
                    result = data;
                }
            }
        });
    }
    else {
        result = allProvinceCache;
    }
    return result;
}

// 查询所有城市
function getAllCity() {
    var result = null;
    if (allCityCache == undefined || allCityCache == null || allCityCache == "") {
        $.ajax({
            type: 'post',
            url: Context.PATH + "/common/citylist.html",
            data: {},
            async: false,
            error: function (s) {
            }
            , success: function (data) {
                if (data) {
                    result = data;
                }
            }
        });
    }
    else {
        result = allCityCache;
    }
    return result;
}

var allProvinceCache = getAllProvince();    // 缓存所有省份数据
var allCityCache = getAllCity();            // 缓存所有城市数据

function bindRegionData(province, city, district, provinceVal, cityVal, districtVal) {
    var result = getAllProvince();
    if (result != null) {
        var provinceOptions = "";
        provinceOptions += "<option value=''>请选择</option>";
        for (var i = 0; i < result.length; i++) {
            provinceOptions += "<option value='" + result[i].id + "'>" + result[i].name + "</option>";
        }
        $(province).html(provinceOptions);

        // 设置省份
        var provinceValType = typeof provinceVal;
        if (provinceValType == "number") {    // 省份ID
            $(province).val(provinceVal);
        }
        else if (provinceValType == "string") {    // 省份名称
            $(province).children("option").each(function () {
                if ($(this).text() == provinceVal)
                    $(province).val($(this).val());
            });
        }

        // 如果没有选中任何项，就默认选中第一项
        if ($(province).val() == null) {
            $(province).get(0).selectedIndex = 0;
        }

        // 获取该省所有的城市
        if (city != null && $(province).val() > 0) {
            bindCityData(city, district, $(province).val(), cityVal, districtVal);
        }
    }

    // 绑定省市联动事件
    if (typeof city == "object") {
        $(province).bind("change", function () {
            var provinceId = $(this).val();
            bindCityData(city, district, provinceId, cityVal, districtVal);
        });
    }
}

function bindCityData(city, district, provinceId, cityVal, districtVal) {
    var result = getAllCity();
    if (result != null) {
        var cityOptions = "";
        cityOptions += "<option value=''>请选择</option>";
        for (var i = 0; i < allCityCache.length; i++) {
            if (allCityCache[i].provinceId == provinceId) {
                cityOptions += "<option value='" + result[i].id + "'>" + result[i].name + "</option>";
            }
        }
        $(city).html(cityOptions);

        // 设置城市
        var cityValType = typeof cityVal;
        if (cityValType == "number") {    // 城市ID
            $(city).val(cityVal);
        }
        else if (cityValType == "string") {    // 城市名称
            $(city).children("option").each(function () {
                if ($(this).text() == cityVal) {
                    $(city).val($(this).val());
                }
            });
        }

        if ($(city).val() == null) {
            $(city).get(0).selectedIndex = 0;
        }

        // 获取该城市所有的区县
        if (district != null && $(city).val() > 0) {
            bindDistrictData(district, $(city).val(), districtVal)
        }else{
        	$(district).empty();
        }
    }

    // 绑定市县联动事件
    if (typeof district == "object") {
        $(city).bind("change", function () {
            var cityId = $(this).val();
            bindDistrictData(district, cityId, districtVal)
        });
    }
}

function bindDistrictData(district, cityId, districtVal) {
    var url = Context.PATH + "/common/districtlist.html";
    $.getJSON(url, {cityId: cityId}, function (result) {
            var districtOptions = "";
            districtOptions += "<option value=''>请选择</option>";
            for (var i = 0; i < result.length; i++) {
                districtOptions += "<option value='" + result[i].id + "'>" + result[i].name + "</option>";
            }
            $(district).html(districtOptions);

            // 设置区县
            var districtValType = typeof districtVal;
            if (districtValType == undefined) return;
            else if (districtValType == "number") {    // 区县ID
                $(district).val(districtVal);
            }
            else if (districtValType == "string") {    // 区县名称
                $(district).children("option").each(function () {
                    if ($(this).text() == districtVal)
                        $(district).val($(this).val());
                });
            }

            if ($(district).val() == null) {
                $(district).get(0).selectedIndex = 0;
            }
        }
    )
    ;
}

// 根据城市ID/名称获取城市对象
function getCity(cityVal) {
    var allCity = allCityCache;
    if (allCity != null) {
        var cityValType = typeof cityVal;
        if (cityValType == undefined) return null;
        else if (cityValType == "number") {    // 城市ID
            for (var i = 0; i < allCity.length; i++) {
                if (cityVal == allCity[i].id) {
                    return allCity[i];
                }
            }
        }
        else if (cityValType == "string") {
            for (var i = 0; i < allCity.length; i++) {
                if (cityVal == allCity[i].name) {
                    return allCity[i];
                }
            }
        }
    }
}
