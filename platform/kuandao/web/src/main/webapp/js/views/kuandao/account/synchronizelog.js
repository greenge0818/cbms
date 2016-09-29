/**
 * Created by dengxiyan on 2015/8/27.
 * 订单全部tab页
 */

var tradeTable;
jQuery(function ($) {
    initTable();
    initClickEvent();
});

function initTable() {
    var url = Context.PATH + "/kuandao/account/querySynchronizeLog.html";
    tradeTable = jQuery("#dynamic-table").DataTable({
        "sScrollY": "400px",
        "bScrollCollapse": true,
        "iDisplayLength": 50,
        "ajax": {
            "url": url
            , "type": "POST"
            , data: function (d) {
                d.memeberName = $("#memeberName").val();
                d.result = $("#result").children("option:selected").val();
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'createDateTime'},
            {data: 'memeberName'},
            {data: 'memeberCode'},
            {data: 'description'},
            {data: 'result'}
            , {data: 'type'}
            , {data: 'errMsg'}
        ]
        , columnDefs: [
			{
			    "targets": 4, //第几列 从0开始
			    "data": "result",
			    "render": function (data, type, full, meta) {
			        var text = '';
			        //同步结果 0：失败 1：成功
			        if(data == 0){
			        	text = '同步失败';
			        }else if(data == 1){
			        	text = '同步成功';
			        }
			        return text;
			    }
			},
            {
                "targets": 5, //第几列 从0开始
                "data": "type",
                "render": function (data, type, full, meta) {
                	var text = '';
                    //同步方式 0:手动 1:自动 
            		if(data == 0){
            			text = '手动';
                	}else if(data == 1){
            			text = '自动';
                	}
                	return text;
                }
            }
        ]
    });
}



function initClickEvent() {
    //搜索事件
    $("#queryBtn").click(search);
   
} 


function search() {
    tradeTable.ajax.reload();
}


function initOrg(){
	 $.ajax({
	        type: "POST",
	        url: Context.PATH + '/kuandao/account/queryAllBusinessOrg.html',
	        data: {},
	        dataType: "json",
	        success: function (response, textStatus, xhr) {
	            if (response.success) {
	                var datas = response.data;
	                $("#org").empty();
	                $("#org").append('<option value ="">全部</option>');
	                for (var i in datas) {
	                    $("#org").append('<option value ="' + datas[i].id + '">' + datas[i].name + '</option>');
	                }
	                var selectedOrgId = $('#org').attr('value');
	                $('#org option[value="'+selectedOrgId+'"]').attr("selected",true);
	            }
	        },
	        error: function (xhr, textStatus, errorThrown) {
	        }
	    });
}
