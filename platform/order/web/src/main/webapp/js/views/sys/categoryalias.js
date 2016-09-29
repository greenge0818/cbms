/**
 * 系统设置  -- 进项票品名映射设置
 * Created by tuxianming
 */

var _categories=null;  //在加载数据成功后，会初始化这个值，保存，更新，删除时，会更新这个值
var _updateObjs=null;  //当修改原有数据时，这里面会有值，每次打开弹窗的时候会重新初始化
var index; //当弹窗打开时，这个index会被重置，这个index为_categories的下标 

$(document).ready(function() {
	//加载数据
	$.get(Context.PATH + "/category/loadcategoryalias.html", {rnd: new Date().getTime()}, function(data){
		console.log(data);
		
		if(data.success){
			//初始化表格
			var tablebody = $("#dynamic-table tbody");
			_categories =  data.data;
			
			if(_categories){
				for(var i=0; i<_categories.length; i++){
					var category = _categories[i];
					var row = "<tr><td>"+category.categoryName+"</td><td>{aliaes}</td><td><a class='editBtn' categoryName='"+category.categoryName+"' index='"+i+"' href='javascript:void(0);'>编辑</a></td></tr>";
										
					row = row.replace("\{aliaes\}", buildAlias(category.alias));
					tablebody.append($(row));
				}
			}
			
		}
	});
	
	//点击编辑出现弹窗
	$("#dynamic-table").on("click", "a.editBtn", function(){
		var item = $(this);
		index = item.attr("index")/1;
		
		var tbody = $("#editTable tbody");
		tbody.empty();
		
		_updateObjs = new Object();
		var category = _categories[index];
		
		var aliases = category.alias;
		for(var j=0; j<aliases.length; j++){
			
			var as = aliases[j];
			var aliasName = as.aliasName;
			
			var aliasId =  as.id;
			if(aliasId){
				aliasId = " aliasId='"+aliasId+"' ";
			}else{
				aliasId="";
			}
			
			if(aliasName){
				var row = "<tr><td><input type='text' class='readyUpdate' name='aliasName' value='"+aliasName+"' /><input type='hidden' name='index' value='"+index+"'/><input type='hidden' name='aliasIndex' value='"+j+"'/></td><td> <a class='deleteAlias' "+aliasId+" href='javascript:void(0);'>删除</a></td></tr>";
				tbody.append(row);
			}
		}
		
		var addRow = "<tr><td colspan='2'><a id='addRowBtn' href='javascript:void(0);'>添加</a></td></tr>";
		tbody.append(addRow);
		
		cbms.getDialog("编辑品名&nbsp;&nbsp;&nbsp;&nbsp;<span>" + item.attr("categoryName") + "</span>", "#editDialog");
		
	});
	
	$(document).on("change", ".readyUpdate", function(){
		var _this = $(this);
		var index = _this.siblings("input[name='index']").val();
		var aliasIndex = _this.siblings("input[name='aliasIndex']").val();
		
		var alias = _updateObjs[index+"-"+aliasIndex];
		if(alias){
			alias.aliasName = _this.val();
		}else{
			var as = _categories[index].alias[aliasIndex];
			_updateObjs[index+"-"+aliasIndex]={aliasIndex: aliasIndex, id: as.id, categoryId: as.categoryId, categoryName: as.categoryName, aliasName: _this.val()};
		}
		
	})
	
	//点击取消，关闭弹窗
	$(document).on("click","#cancelBtn", function(){
		$("#dynamic-table tbody tr").eq(index).children().eq(1).html(buildAlias(_categories[index].alias));
		cbms.closeDialog();
	});
	
	$(document).on("click", "#addRowBtn", function(){
		var row = "<tr><td><input type='text' name='aliasName' value='' /><input type='hidden' name='index' value='"+index+"'/></td><td> <a class='deleteAlias' href='javascript:void(0);'>删除</a></td></tr>";
		$(this).parent().parent().before(row); // tbody > tr > td > a
	});
	
	//点击保存，如果成功关闭弹窗，并提示保存成功, 不关闭弹窗，提示保存失败
	$(document).on("click","#saveBtn", function(){
		
		//得到需要提交的数据， 并提交，如果成功则将数据存到本地，并重新显示数据
		var categoryId = _categories[index].categoryId, //分类id
			categoryName =  _categories[index].categoryName, //分类名
			aliasNames=[]; //别名数组
		
		var rows = $("#editTable tbody tr");
		for(var i=0; i<rows.length-1; i++){
			var row = rows.eq(i);
			var categorysIndex = row.find("input[name='aliasIndex']").val();
			if(!categorysIndex){ //没有aliasIndex， 说明在_categories中不存在，也就是说还没有保存到数据库中
				
				var aliasName = row.find("input[name='aliasName']").val();
				//判断添加的品名是否存在过
				var exist = function() {
					for (var i=0; i<_categories.length; i++) {
						var aliases = _categories[i].alias;
						if (aliases && aliases.length>0) {
							for (var j=0; j<aliases.length; j++) {
								var tmpName = aliases[j].aliasName;
								if (tmpName == aliasName) {
									return true;
								}
							}
						}
					}
					return false;
				}();
				if (!exist) {
					if(aliasName && $.trim(aliasName).length>0){
						aliasNames.push($.trim(aliasName));
					}
				} else {
					cbms.alert("该品名（"+aliasName+"）已存在，请重新输入！");
					return;
				}
			}
		}
		
		var size = function(obj){
			var count = 0;
			if(obj && typeof(obj)=='object'){
				for(var i in obj){
					count++;
				}
			}
			return count;
		}(_updateObjs);
		
		if(aliasNames.length>0 || size>0){
			
			var params = {};
			
			if(aliasNames.length>0){
				var aliasesStr = aliasNames.toString();
				
				params.categoryId = categoryId;
				params.categoryName=categoryName;
				params.aliasNames =aliasesStr;
				
			}
			
			var j=0;
			for(var i in _updateObjs){
				var prefix = 'alias['+(j++)+'].';
				params[prefix+'aliasName'] = _updateObjs[i].aliasName;
				params[prefix+'id'] = _updateObjs[i].id;
				
			}
			
			$.post(
					Context.PATH + "/category/addcategoryalias.html", 
					params,
					function(data){
						if(data.success){
							//保存成功
							cbms.alert("保存成功！");
							//存到本地
							var ids = data.data;
							
							if(ids){
								for(var i=0; i<ids.length; i++){
									//更新本地数组 aliases 
									_categories[index].alias.push({id: ids[i], categoryId: categoryId, categoryName: categoryName, aliasName:aliasNames[i]});
								}
							}
							$("#dynamic-table tbody tr").eq(index).children().eq(1).html(buildAlias(_categories[index].alias));
							cbms.closeDialog();
						}else{
							cbms.alert(data.data);
						}
					}
			);
		}else{
			//没有任何数据更新，直接 关闭？
			$("#dynamic-table tbody tr").eq(index).children().eq(1).html(buildAlias(_categories[index].alias));
			cbms.closeDialog();
		}
		
		
	});
	
	//点击删除，干掉这一行，并把删除的提交到服务器
	$(document).on("click", ".deleteAlias", function(){
		var _this = $(this);
		var aliasId = _this.attr("aliasId");
		if(aliasId){
			
			cbms.confirm("确定删除吗？", null, function (){
				//提交到服务器
				$.get(Context.PATH + "/category/deletecategoryalias.html", {id:aliasId, rnd: new Date().getTime()},function(data){
					if(data.success){
						cbms.alert("删除成功！");
						//删除本地数据
						var parent = _this.parent().parent(); // tbody > tr > td > a
						var index = parent.find("input[name='index']").val();
						var aliasIndex = parent.find("input[name='aliasIndex']").val();
						_categories[index].alias.splice(aliasIndex,1);
						
						delete _updateObjs[index+"-"+aliasIndex];
						parent.remove();
					}
				});
			});
		}else{
			//删除这一行
			_this.parent().parent().remove(); // tbody > tr > td > a
		}
	});
	
	
	function buildAlias(aliases){
		var aliasesStr = "";
		if(aliases){
			for(var j=0; j<aliases.length; j++){
				
				var aliasName = aliases[j].aliasName;
				if(aliasName){
					aliasesStr +=aliasName;
					if(j+1<aliases.length){
						aliasesStr+="、 ";
					}
				}
			}
		}
		return aliasesStr;
	}
});