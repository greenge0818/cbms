/**
 * 系统设置项设置
 * Created by tuxianming on 2015/12/10.
 */
$(document).ready(function () {
	
    var table;
    table = $('#dynamic-table').dataTable({
    	"processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "ajax": {
            url: Context.PATH + '/sys/reportmaillist.html',
            type: 'POST'
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
        	
        	$('td:eq(3)', nRow).css("width","120px");
        	$('td:eq(0)', nRow).css("width","80px");
        	$('td:eq(1)', nRow).css("width","80px");
        	$('td:eq(5)', nRow).css("width","140px");
        	
        	 var editLink = "&nbsp;&nbsp;<a href='javascript:;' class='editBtn' name='edit' eid='" + aData.id + "'>编辑</a>";
        	 editLink += "&nbsp;&nbsp;<a href='javascript:;' class='deleteBtn' name='delete' eid='" + aData.id + "'>删除</a>"
             $('td:eq(6)', nRow).html(editLink).css("width","90px");
        },
        columns: [
            {data: 'title'},
            {data: 'sendTime'},
            {data: 'receiver'},
            {data: 'period'},
            {data: 'createdBy'},
            {data: 'createTimeStr'},
            {defaultContent: ''}
        ]
    });

    /**
     * 添加按钮 
     */
    $('#addBtn').click(function(){
        cbms.getDialog("添加邮件设置", Context.PATH + "/sys/editreportmail.html");
    });

    /**
     * 编辑按钮
     */
    $(document).on("click",".editBtn",function(){
        cbms.getDialog("编辑邮件设置", Context.PATH + "/sys/editreportmail.html?id="+$(this).attr("eid")+"&");
    });
    
    /**
     * 删除
     */
    $(document).on("click",".deleteBtn",function(){
    	var _this = this;
    	cbms.confirm("确定删除吗？",'',function(){
    		$.ajax({
                type: "GET",
                url: Context.PATH + '/sys/deletereportmail.html',
                data:{id:$(_this).attr("eid")},
                dataType: "json",
                success: function (response, textStatus, xhr) {
                    if (response.success) {
                        cbms.alert("删除成功");
                        table.fnDraw();
                    } else {
                        $("#submitBtn").prop("disabled", false);
                        cbms.alert(response.data);
                    }
                    cbms.closeLoading();
                },
                error: function (s) {
                    $("#submitBtn").prop("disabled", false);
                    cbms.closeLoading();
                }
            });
    	});
    });
    
    /**
     * 关闭弹窗 
     */
    $(document).on("click","#exitBtn",function(){
    	cbms.closeDialog();
    });
    
    /**
     * 提交编辑，或者增加，然后关闭弹窗 
     */
    $(document).on("click","#submitBtn",function(){
    	if (!setlistensSave())return;
    	
    	//get var
    	var vid = $("input[name='id']").val();
    	var vreceiver = $("#report-mail-receiver").val();
    	
    	//验证邮件
    	if(vreceiver){
    		
    		vreceiver = $.trim(vreceiver);
    		if(vreceiver.length>1){
    			var speator = vreceiver.substring(vreceiver.length-1);
    			if(speator==';')
    				vreceiver = vreceiver.substring(0, vreceiver.length-1);
    		}
    		var receivers = vreceiver.split(";");
    		
    		var mailRegExp = /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/;
    		for(var i=0; i<receivers.length; i++){
    			
    			var receiver = $.trim(receivers[i]);
    			if(!mailRegExp.test(receiver)){
    				cbms.alert("第"+(i+1)+"个邮件格式不对！");
    				return;
    			}
    		}
    	}
    	
    	
    	var vtitle = $("#report-mail-title").val();
    	var vattachment = $("#report-mail-attachment").val();
    	
    	//时间：hh:mm
    	var vhour = $("#report-mail-hour").val();
    	var vminute = $("#report-mail-minute").val();
    	var vtime = vhour+":"+vminute;
    	var vcontent = $("#report-mail-content").val();
    	
    	var params = {};
    	if(vid) params.id = vid;
    	params.receiver =  vreceiver;
    	params.title = vtitle;
    	params.content = vcontent;
    	params.sendTime = vtime;
    	params.attachment = vattachment;
    	
    	//定期模式
    	//如果选则 按周：取如下值
    	var periodType = $("input[name='period_type']:checked").val(); 
    	if(periodType == "WEEK"){
    		params.periodType = periodType;
    		
    		var allweeks = $("#report-mail-weeks input[type='checkbox']");
    		var selectWeeks = [];
    		allweeks.each(function(){
    			var weekItem = $(this);
    			if(weekItem.prop("checked")){
    				selectWeeks.push(weekItem.val());
    			}
    		});
    		if(selectWeeks.length==0){
    			cbms.alert("请选择是星期几发送");
    			return ;
    		}
    		params.weeks=selectWeeks.toString();
    		params.monthDuration=0;
    		params.day=0;
    		params.weekDuration=0;
    	}else if(periodType=="MONTH"){  //按月
    		
    		var subPeriodType = $(".period_tab2 input[name='period_type1']:checked").val();
    		params.periodType = subPeriodType;
    		if(subPeriodType == "MONTH1"){
    			params.monthDuration = $("#month-duration-1").val();
    			params.day = $("#day-duration").val();
    			
    			params.weeks='';
        		params.weekDuration=0;
    			
    			if(!params.monthDuration){
        			cbms.alert("请输入月数");
        			return ;
        		}
    			if(!params.day){
        			cbms.alert("请输入天数");
        			return ;
        		}
    			
    		}else if(subPeriodType == "MONTH2"){
    			params.monthDuration = $("#month-duration-2").val();
    			params.weekDuration = $("select.week-duration").val();
    			params.weeks = $("select.week").val();
    			
        		params.day=0;
    			if(!params.monthDuration){
        			cbms.alert("请输入月数");
        			return ;
        		}
    		}else{
    			cbms.alert("请选择一个类型");
    			return ;
    		} 
    	}else{
    		cbms.alert("请选择一个类型");
    		return ;
    	}
    	
    	//submit
    	$.ajax({
            type: "POST",
            url: Context.PATH + '/sys/updatereportmail.html',
            data:params,
            dataType: "json",
            success: function (response, textStatus, xhr) {
                if (response.success) {
                    cbms.alert("更新设置成功");
                    cbms.closeDialog();
                    table.fnDraw();
                } else {
                    $("#submitBtn").prop("disabled", false);
                    cbms.alert(response.data);
                }
                cbms.closeLoading();
            },
            error: function (s) {
                $("#submitBtn").prop("disabled", false);
                cbms.closeLoading();
            }
        });
    	
    });

    /**
     * 添加事件
     */
    $(document).on("change","input[name='period_type']",function(){
    	var radio = $(this);
    	if(radio.prop("checked")){
    		if(radio.val()=="WEEK"){
    			$(".period_tab1").show();
    			$(".period_tab2").hide();
    		}else{
    			$(".period_tab2").show();
    			$(".period_tab1").hide();
    		}
    	}
    });
    
    
});
