	
	/**
	* 关闭窗口
	*/
	$("#cancelBtn").on("click",function(){
		cbms.closeDialog();
	});
	/**
	 * 保存窗口
	 */
	$("#saveBtn").on("click",formSubmit);
	
	/**
	 * 检查属性名
	 */
	$('#attrName').on("change",function(){
		checkName($("#attrForm").find('input[name="id"]').val(),$(this).val());
	});	
	
	function checkName(id,val){
		var bol=false;
		$.ajax({
            type: "POST",
            async:false,
            url: Context.PATH + '/resource/attribute/edit/checkAttrName.html',
            data: {
            	 id:id,
                 name: val
            },
            dataType: "json",
            success: function (response, textStatus, xhr) {
                if (response.success) {
                    cbms.alert(response.data,function(){
                    	$("#saveBtn").removeAttr("disabled");
                    	$('#attrName').attr("sel","1");
                    });
                    bol=true;
                }else{
                	$('#attrName').attr("sel","0");
                }
            }
        });
		return bol;
	}
	
	/**
	 * 移除按钮disabled属性
	 */
	function removeBtnAttr(){
		$("#saveBtn").removeAttr("disabled");
	}
	/**
	*属性添加提交表单
	*/
	function formSubmit(){
	 	if (setlistensSave("#attrForm")) {
	 		
	 		var id=$("#attrForm").find('input[name="id"]').val();
			//属性名称
			var name=$("#attrForm").find('input[name="name"]').val();
			//属性类型
			var type=$("#attrForm").find("select option:selected").val();
			//属性值
			var value=$("#attrForm").find('input[name="options"]').val();
			if($.trim(type)!="input"){
				if(utils.isEmpty(value)){
					cbms.alert("属性值不能为空!",removeBtnAttr);
					return false;
				}
				if(value.indexOf("，")>=0){
					value=value.replace(/，/ig,',');
				}
				//过滤空值
				value=$.map(value.split(","),function(v){
					if(!utils.isEmpty(v)){
						return v;
					}
				}).join(",");
				
				if(utils.isEmpty(value)){
					cbms.alert("属性值输入有误,请核实!",removeBtnAttr);
					return false;
				}
			} 
			//属性名是否已存在
			if(checkName(id,name)){
				return;
			}
			
			if(utils.isEmpty(id)) {
                add(name, type, value);
            } else {
                edit(id,name, type, value);
            }
		}
	}
	
	/**
	 *  编辑区域
	 * @param id             属性主健id
	 * @param name           属性名称
	 * @param type   		 属性类型
	 * @param value     	 属性值
	 */
	function edit(id,name, type, value){
		$.ajax({
            type: "POST",
            url: Context.PATH + '/resource/attribute/edit/modify.html',
            data: {
            	id:id,
                name: name,
                type: type,
                options:value
            },
            dataType: "json",
            success: function (response, textStatus, xhr) {
                if (response.success) {
                	cbms.closeDialog();
                	_attrTable.fnDraw();
                } else {
                    cbms.alert(response.data );
                }
            }
        });
	}
	
	/**
	 *  增加属性
	 * @param name           属性名称
	 * @param type   		 属性类型
	 * @param value     	 属性值
	 */
	function add(name, type, value) {

        $.ajax({
            type: "POST",
            url: Context.PATH + '/resource/attribute/edit/add.html',
            data: {
                name: name,
                type: type,
                options:value
            },
            dataType: "json",
            success: function (response, textStatus, xhr) {
                if (response.success) {
                	cbms.closeDialog();
                	_attrTable.fnDraw();
                } else {
                	 cbms.alert(response.data );
                }
            }
        });
    }
