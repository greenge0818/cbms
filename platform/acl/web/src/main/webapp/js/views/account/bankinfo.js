/**
 * Created by lcw on 2015/7/15.
 */
$().ready(function () {
    $('#dynamic-table').dataTable({
        "bPaginate": false,
        "searching": false,
        "paging": false,
        "ordering": false,
        "processing": true,
        "serverSide": true,
        "ajax": {
            url: Context.PATH + '/account/bankinfo.html',
            type: 'POST',
            data: function (d) {
                    d.accountId = parseInt($("#accountId").val())
            }
        },
        columns: [
            {data: 'bankName'},
            {data: 'bankCode'},
            {data: 'bankAccountCode'},
            {defaultContent: ''}
        ],
        fnRowCallback: function (nRow, aData, iDataIndex) {
            var branch = aData.bankNameBranch==null?"":aData.bankNameBranch;
            var bankName = aData.bankName==null?"":aData.bankName;
            $('td:eq(0)', nRow).html(bankName + " " + branch);
            if (aData.isDefault == '1') {//默认银行
            	$('td:eq(3)', nRow).html('<input id="setChecked"  name="default" type="radio" account_id="'+aData.accountId+'" value="'+aData.id+'" />');
            	setTimeout(function(){$("#setChecked").prop("checked",true);},300);
            } else {
            	$('td:eq(3)', nRow).html('<input  name="default" type="radio" account_id="'+aData.accountId+'" value="'+aData.id+'" />');
            }
            return nRow;
        }
    });
    function getRadio() {
    	 var radionum = $("input[name='default']");
    	 var bankIds = [];
    	 var bankId ="";
    	 for (var i = 0; i < radionum.length; i++) {
    		 var id = radionum[i].value;
    		 if (radionum[i].checked) {
    			bankId = radionum[i].value;	
    		 }
    		 bankIds.push(id);
    	 }
    	 var accountId = $("input[name='default']").attr("account_id");
    	 var url = Context.PATH + '/account/update/bank/info.html';
    	 $.post(url, {
    		bankId : bankId,
    		accountId : accountId   		
    	 }, function(result) {
    		 if (result) {
					if (result.success) {
						cbms.alert("客户默认银行设置成功！");
					} else {
						cbms.alert(result.data);
					}
				}
    	 });
    }
   
    $(document).on("click", "input[name='default']", function() {
    	getRadio();
    });

});
