
$(function() {
	var url = Context.PATH + "/sys/oplog/list.html";
	dt = $("#dynamic-table").DataTable({
		ajax : {
			url : url,
			type : "POST",
			data : function(d) {
				d.userName = $("#userName").val();
				d.opType = $("#opType").val();
				d.opLevel = $("#opLevel").val();
				d.beginDate = $("#beginDate").val();
				d.endDate = $("#endDate").val();
				if(d.order && d.order.length>0){
					var od = d.order[0];
					d.orderBy = d.columns[od.column].data;
					d.orderDirection = od.dir;
				}
				delete d.columns;
				delete d.order;
				delete d.search;
			}
		},
		serverSide : true, //服务模式
		processing : true,//显示数据加载进度
		searching : false, //是否启用搜索
		ordering : true, //是否启用排序
		lengthChange : false, //不显示pageSize的下拉框
		oLanguage : {
			sUrl : Context.PATH + "/js/DT_zh.txt"
		}, //自定义语言包
		bFilter : false,
		bLengthChange : false, //不显示每页长度的选择条
		bPaginate : true,
		order: [[ 0, "desc" ]],
		columns : [ {
			data : 'created'
		}, {
			data : 'operatorName',
			orderable : false
		}, {
			data : 'operationName',
			orderable : false
		}, {
			data : 'parameters',
			orderable : false
		}, {
			data : 'operationLevelDesc'
		} ],
		columnDefs : [ {
			"targets" : 0,
			"render" : function(data, type, full, meta) {
				return new Date(data).Format("yyyy-MM-dd hh:mm:ss");
			}
		}, {
			"targets" : 3,
			"render" : function(data, type, full, meta) {
				return "";
			}
		} ],
		fnRowCallback : function(nRow, aData, iDataIndex) {
			var cell = $('td:eq(3)', nRow);
			var link = cell.append("<a href='javascript:'>点击查看</a>");
			$("<input type='hidden'/>").val(aData.parameters).appendTo(cell);
			link.click(function() {
				var pasText = $(this).parent().find("input").val();
				cbms.alert(pasText);
			});
		}
	});
	$("#btn").click(function() {
		dt.ajax.reload();
	});
});