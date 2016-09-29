	// 查询出所有城市
	(function(){
		$.ajax({
	        type: "POST",
	        url: Context.PATH+ '/common/allcity.html',
	        dataType: "json",
	        success: function (response, textStatus, xhr) {
	           Context.CITYDATA=response;
	        }
	    });
	}());
	$(document).on('input propertychange','.city', function () {
        showPYMatchList($(this),Context.CITYDATA,"id","name",null);
        $("#dropdown").css("z-index",9999);
    });
	$(document).on('input propertychange','#centerCity', function () {
		showPYMatchList($(this),Context.CITYDATA,"id","name",checkCenterCity);
		$("#dropdown").css("z-index",9999);
	});
	/**
	 * 移除按钮的disabled属性
	 * @param btn  
	 */
	function removeBtnDisabled(){
		$("#saveBtn").removeAttr("disabled");
	}
	/**
	*区域添加提交表单
	*/
	function formSubmit(e){
	 	if (setlistensSave("#areaForm")) {
	 		var id=$("#areaForm").find('input[name="id"]').val();
	 		//区域类别
	 		var zoneName=$("#areaForm").find('input[name="zoneName"]').val();
			//区域名称
			var name=$("#areaForm").find('input[name="name"]').val();
			//中心城市
			var centerCityName=$("#centerCity").val();
			var centerCityId=$("#centerCity").attr("val");
			//周边城市id集
			var refCityIds="";
			var refCity=$(".city");
			if(refCity.length>0){
				for(var i=0;i<refCity.length;i++){
					refCityIds+=","+$(refCity[i]).attr("val");
				}
			}
			refCityIds=refCityIds.substring(1);
			//停用启用按钮
			var enable = $('input[name="enable"]:checked').val();
			if(refCityIds.split(",").indexOf(centerCityId)>=0){
				cbms.alert("周边城市不能和中心城市相同",removeBtnDisabled);
			    return;
			}
			if(utils.isEmpty(zoneName)) {
			    cbms.alert("区域类别为必填项",removeBtnDisabled);
			    return;
			}
			if(utils.isEmpty(name)) {
			    cbms.alert("区域名称为必填项",removeBtnDisabled);
			    return;
			}
			if (utils.isEmpty(centerCityId)) {
			    cbms.alert("中心城市未正确选择",removeBtnDisabled);
			    return;
			}
			if (!isCityIds(refCityIds)) {
				cbms.alert("周边城市重复或未正确选择",removeBtnDisabled);
				return;
			}
			if($("#centerCity").attr("sel")==1){
				cbms.alert("中心城市已被选择",removeBtnDisabled);
				return;
			}
			if(utils.isEmpty(id)) {
                add(zoneName,name, centerCityId, refCityIds,enable);
            } else {
                edit(id,zoneName,name, centerCityId, refCityIds,enable);
            }
		}
	}
	/**
	 * 判断是否是城市id，以免随便输入未正确选择
	 * @param ids
	 * @returns {Boolean}
	 */
	function isCityIds(ids){
		var bol=true;
		if(!utils.isEmpty(ids) ){
			if(ids.indexOf(",")>=0){
				var idArray=ids.split(",").sort();
				$(idArray).each(function(i,e){ 
					if(e==idArray[i+1]||isNaN(idArray[i])){
						bol=false;
						return bol;
					}
				});
			}else{
				if(isNaN(ids)){
					bol=false;
				}
			}
		}
		return bol;
	}
	/**
	 * 检查中心城市是否已选
	 */
	function checkCenterCity(){
		var cityId=$("#centerCity").attr("val");
		var id=$("input[name='id']").attr("val")|null;
		if(cityId){
			$.ajax({
	            type: "POST",
	            url: Context.PATH + '/smartmatch/area/checkCenterCity.html',
	            data: {
	            	cityId:cityId,
	            	id:id
	            },
	            dataType: "json",
	            success: function (response, textStatus, xhr) {
	                if (response.success) {
	                	cbms.alert(response.data,function(){
	                		$("#saveBtn").removeAttr("disabled");
	                		$("#centerCity").attr("sel",1);
	                	});
	                }else{
	                	$("#centerCity").attr("sel",0);
	                	$("#saveBtn").removeAttr("disabled");
	                } 
	            }
	        });
		}
	}
	/**
	 *  编辑区域
	 * @param id             区域主健id
	 * @param zoneName       区域类别
	 * @param name           区域名称
	 * @param centerCityId   中心城市id
	 * @param refCityIds     周边城市id集
	 * @param enable         是否启用
	 */
	function edit(id,zoneName,name, centerCityId, refCityIds,enable){
		$.ajax({
            type: "POST",
            url: Context.PATH + '/smartmatch/area/edit.html',
            data: {
            	id:id,
            	zoneName:zoneName,
                name: name,
                centerCityId: centerCityId,
                refCityIds:refCityIds,
                isEnable:enable
            },
            dataType: "json",
            success: function (response, textStatus, xhr) {
                if (response.success) {
                	cbms.closeDialog();
                	_areaTable.fnDraw();
                } else {
                    var index = parseInt(response.data);
                    cbms.alert( MESSAGE.ROLE.ADD[index] );
                }
            }
        });
	}
	/**
	 *  增加区域
	 * @param zoneName       区域类别
	 * @param name           区域名称
	 * @param centerCityId   中心城市id
	 * @param refCityIds     周边城市id集
	 * @param enable         是否启用
	 */
	function add(zoneName,name, centerCityId, refCityIds,enable) {
        $.ajax({
            type: "POST",
            url: Context.PATH + '/smartmatch/area/add.html',
            data: {
            	zoneName:zoneName,
                name: name,
                centerCityId: centerCityId,
                refCityIds:refCityIds,
                isEnable:enable
            },
            dataType: "json",
            success: function (response, textStatus, xhr) {
                if (response.success) {
                	cbms.closeDialog();
                	_areaTable.fnDraw();
                } else {
                    var index = parseInt(response.data);
                    cbms.alert( MESSAGE.ROLE.ADD[index] );
                }
            }
        });
    }
	
	/**
	* 关闭窗口
	*/
	function closeForm(){
		cbms.closeDialog();
	}
	/**
	* 添加城市选择
	*/
	$("#addCity").click(function(){
		var length=$("#cityRegion").find("div").length+1;
		var content="<div class='form-group'>"+
					"<label class='col-sm-3 control-label no-padding-right'></label>&nbsp;"+
					"<input search='city' class='city' > <span class='fa fa-minus' onclick='minusCity(this)' style='cursor:pointer;'> </span>"+
	           		"</div>";
		$("#cityRegion").append(content);
	});
	
	/**
	* 减少城市选择
	*/
	function minusCity(e){
		$(e.parentNode).remove();
	}
