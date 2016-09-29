$(document).ready(function() {
    $('#example').dataTable( {
        "processing": true,
        "serverSide": true,
        //sAjaxSource:Context.PATH + '/js/views/data.txt',
        "ajax": {
            url: Context.PATH + '/ajaxdemo.html',
            type: 'POST',
            data:function(d){
                d.name = "myname";
                d.pwd = "mypwd";
            }
        },
        "oLanguage": {                          //汉化
            "sLengthMenu": "每页显示 _MENU_ 条记录",
            "sZeroRecords": "没有检索到数据",
            "sInfo": "当前数据为从第 _START_ 到第 _END_ 条数据；总共有 _TOTAL_ 条记录",
            "sInfoEmtpy": "没有数据",
            "sProcessing": "正在加载数据...",
            "oPaginate": {
                "sFirst": "首页",
		        "sPrevious": "上一页",
		        "sNext": "下一页",
                "sLast": "尾页"
            }
        },
        columns: [
            { data: 'id' },
            { data: 'name' },
            { data: 'pwd' },
        ]


    } );
} );