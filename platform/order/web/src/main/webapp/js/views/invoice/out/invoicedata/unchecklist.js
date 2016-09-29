$.ajax({
    type: "POST",
    url: Context.PATH + '/common/organizationList.html',
    data: {},
    dataType: "json",
    success: function (response, textStatus, xhr) {
        if (response.success) {
            var data = response.data;
            $.fn.zTree.init($("#orgTree"), setting, data);
            
        }
    },
    error: function (xhr, textStatus, errorThrown) {
    }
});

function showMenu() {
    var cityObj = $("#sorganization");
    var cityOffset = $("#sorganization").offset();
    $("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

    $("body").bind("mousedown", onBodyDown);
}
function hideMenu() {
    $("#menuContent").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDown);
}
function onBodyDown(event) {
    if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
        hideMenu();
    }
}
var setting = {
    view: {
        dblClickExpand: false
    },
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "parentId",
            rootPId: 0
        }
    },
    callback: {
        beforeClick: beforeClick,
        onClick: onClick
    }
};

function beforeClick(treeId, treeNode) {
}

function onClick(e, treeId, treeNode) {
    var zTree = $.fn.zTree.getZTreeObj("orgTree"),
        nodes = zTree.getSelectedNodes(),
        v = "",vid = "";
    nodes.sort(function compare(a,b){return a.id-b.id;});
    for (var i=0, l=nodes.length; i<l; i++) {
        v += nodes[i].name + ",";
        vid+= nodes[i].id ;
    }
    if (v.length > 0 ) v = v.substring(0, v.length-1);
    var cityObj = $("#sorganization");
    cityObj.attr("value", v);
    $("#sorganizationHidden").attr("value",vid);

    if(vid.length > 0){

    }
}

$(".btn-sm").click(function(){
	search();
});
$("#buyerName").keydown(function(event){
	if(event.keyCode==13){
		search();	
	}
});
function search(){
	$.post(Context.PATH+"/invoice/out/invoicedata/unchecklist/search.html",
			{orgId:$("#sorganizationHidden").val(),buyerName:$("#buyerName").val()},
			function(data){
				if(data.success){
					var html="";
					$(data.data).each(function(i,e){
						html+="<tr><td>"+e.name+"</td>";

                        if($('#allowChecked').val()=="true") {
                            html+="<td><a href='"+Context.PATH+"/invoice/out/invoicedata/account/"+e.id+"/show.html'>审核开票资料</a></td>";
                        } else {
                            html+="<td></td>";
                        }

                        html+="</tr>";
					});
					$("tbody").html(html);
				}
			});
}
search();