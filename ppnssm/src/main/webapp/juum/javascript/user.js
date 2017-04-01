



var gUserColumns = [[{ field: "username", title:'用户名',width:"100",sortable:true},
                    { field: "password", title:'密码',width:"100",sortable:true},
                    { field: "gender", title:'性别',width:"100",formatter: function(val,row,index){
        				if (val){
        					return val;
        				} else {
        					return val;
        				}
        			}},
                    { field: "idcard", title:'身份证号码',width:"100",sortable:true},
                    { field: "email", title:'邮箱',width:"100",sortable:true},
                    { field: "qq", title:'QQ',width:"100",sortable:true},
                    { field: "weixin", title:'微信',width:"100",sortable:true},
                    { field: "regtime", title:'入职时间',width:"100",sortable:true}
                   ]];


function pageInit(){
	$('#datagrid').datagrid({
	    url:"../user/datagrid.action", 
	    toolbar:"#toolbar",
	    fit:true,
	    pagination:true,
	    singleSelect:true,
	    fitColumns:true,
	    striped:true,
	    border:false,
	    nowrap:false,
	    rownumbers:true,
	    columns:gUserColumns
	});
}
		
	var path="..";	
		
		//添加用户信息
	function addUser(){
		$('#dlg').dialog('open').dialog('setTitle','新增用户');
		$('#fm').form('clear');
		url = path+"/user/addUser";
		mesTitle = '新增用户成功';
	}
	
	//编辑用户信息
 	function editUser(){
	 	var row = $('#datagrid').datagrid('getSelected');
	 	if (row){
	 		var id = row.id;
		 	$('#dlg').dialog('open').dialog('setTitle','编辑用户');
		 	$('#fm').form('load',row);//这句话有问题，第一次加载时正确的，第二次就出错了，还保持第一次的数据
		 	url = path+"/user/editUser?id="+id;
		 	mesTitle = '编辑用户成功';
	 	}else{
	 		$.messager.alert('提示', '请选择要编辑的记录！', 'error');
	 	}
	}
 	
	//删除用信息
 	function deleteUser(){
	 	var row = $('#datagrid').datagrid('getSelected');
	 	if (row){
		 	$('#dlg_delete').dialog('open').dialog('setTitle','删除用户');
		 	$('#fm').form('load',row);//这句话有问题，第一次加载时正确的，第二次就出错了，还保持第一次的数据
		 	url = path+"/user/deleteUser?id="+row.id;
		 	mesTitle = '删除用户成功';
	 	}else{
	 		$.messager.alert('提示', '请选择要删除的记录！', 'error');
	 	}
	}
 	
	//保存添加、修改内容
	function saveUser(){
	 	$('#fm').form('submit',{
		 	url: url,
		 	onSubmit: function(){
		 		return $(this).form('validate');
		 	},
			success: function(result){
				/* console.info(result); */
				var result = eval('('+result+')');
				if (result.success){
				 	$('#dlg').dialog('close'); 
				 	$('#datagrid').datagrid('reload'); 
				} else {
					 mesTitle = '新增用户失败';
				}
				$.messager.show({ 
					 title: mesTitle,
					 msg: result.msg
				});
			}
	 	});
	}	
	
	//提交删除内容
	function saveUser_del(){
	 	$('#fm').form('submit',{
		 	url: url,
		 	onSubmit: function(){
		 	},
			success: function(result){
				var result = eval('('+result+')');
				if (result.success){
				 	$('#dlg_delete').dialog('close'); 
				 	$('#datagrid').datagrid('reload'); 
				} else {
					 mesTitle = '删除用户失败';
				}
				$.messager.show({ 
					 title: mesTitle,
					 msg: result.msg
				});
			}
	 	});
	}

	//快速查询
 	function searchUserQ(){
 		var params={username:$("#search_username").val()};
	 	$.ajax({
	        url : "../user/datagrid",// 后台处理方法
	        dataType : "json",
	        type:"POST",
	        headers : {
	            "DataType" : "json"
	        },
	        data:params,
	        success : function(result) {
	        	$('#dlgsearch').dialog('close'); 
	        	$("#datagrid").datagrid('loadData',result);
	        },
	    });
	}
	
	//高级查询
 	function searchUser(){
		$('#dlgsearch').dialog('open').dialog('setTitle','查询');
		$('#fmsearch').form('clear');
		url = path+"/user/datagrid";
		mesTitle = '查询用户成功';
	}
 	
 	function serach_User(){
	 	var params={username:$("#username").val(),
	 			idcard:$("#idcard").val()
 		};
 	 	$.ajax({
 	        url : url,// 后台处理方法
 	        dataType : "json",
 	        type:"POST",
 	        headers : {
 	            "DataType" : "json"
 	        },
 	        data:params,
 	        success : function(result) {
 	        	$('#dlgsearch').dialog('close'); 
 	        	$("#datagrid").datagrid('loadData',result);
 	        },
 	    });
	}
	
	//刷新
	function reload(){
		$('#datagrid').datagrid('reload'); 
	}