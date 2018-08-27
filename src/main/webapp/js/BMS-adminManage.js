$(function(){
	bmsgetcurmatchname();
	var jsonobj=JSON.parse(sessionStorage.getItem("adminlistJson"));
	var num="";
	if(jsonobj!=undefined){
		num=jsonobj.pageNo;
		$("#searchsex").attr("altvalue",jsonobj.sex);
		if(jsonobj.sex!=""){
			$("#searchsex").val($("#searchsex").next().find("[altvalue="+jsonobj.sex+"]").text());
		}
		$("#str").val(jsonobj.str);
	}else{
		num=0
	}
	getAllMem(num);
	if(sessionStorage.getItem("uid")=="admin"){
		$(".addAdmin").show();
		$(".addAdmin").attr("onclick","addAdmin()")
	}
	 $(".selinput").click(function(e){
     	e.stopPropagation();
     	var me=this;
     	$(me).next().show();
     	$(me).next().children().click(function(){
     		$(me).val($(this).text());
     		$(me).attr("altvalue",$(this).attr("altvalue"));
     		$(me).next().hide();
     	})
     })
     $(document).click(function(){
    		$(".emulate").hide();
    	})
})
var temp;
var picurl="";
function getAllMem(num){
	var sex=$("#searchsex").attr("altvalue");
	var str=$("#str").val();
	var pageSize=10;
	$("#page").unbind().data("pagination", null).empty();	
	$("#page").pagination({
		pageIndex: num,
		pageSize : pageSize,
		showFirstLastBtn : true,
		showPrevious: true,
	    showNext: true,
	    autoHidePrevious: true,
	    autoHideNext: true,
	    showJump: true,
	    jumpBtnText:'跳转',
		remote : {
			"url" : "../findAllregistered",
			pageParams : function(data) {
				var param={};
				param.pageNo=data.pageIndex;
				param.sex=sex;
				param.str=str;
				sessionStorage.setItem("adminlistJson", JSON.stringify(param));
				return {
					iDisplayStart : data.pageIndex * data.pageSize,
					iDisplayLength : data.pageSize,
					"sex":sex,
					"str":str,
					"roleflg":"04"
				};
			}, // [Function]:自定义请求参数
			"success" : function(data, status) {
				console.log(data)
				if (data.status == 0) {
					$("tbody").empty();
					
					
					var htmls="";
					$.each(data.list,function(i,mem){
						htmls+='<tr><td><img class="memberhead" src="'+mem.picurl+'">'
				    			+'<span>'+mem.uname+'</span></td><td>管理员</td>'
				    	
				    	htmls+='<td><img src="../images/'+(mem.sex=="01"?'male.png':'female.png')
				    			+'"></td>'
						
								+'<td>'+mem.email+'</td>'
								+'<td>'+mem.phone+'</td>';
								
						if(sessionStorage.getItem("uid")=="admin"){
							htmls+='<td>'
								+'<a uid="'+mem.uid+'" class="delete1" '
								+'onclick="alertConfirm(\'2\',\'确定删除该管理员吗？\',\'delAdmin()\',this)">删除</a>'
		    					+'<span class="split">|</span>'
		    					+'<a class="modify1" detail=\''+JSON.stringify(mem)+'\' onclick="editUser(this)">修改</a>'
						}
						else{
							htmls+='<td>'
								+'<a class="delete2">删除</a>'
		    					+'<span class="split">|</span>'
		    					+'<a class="modify2" >修改</a>'
						}
		    			htmls+='</td></tr>'
								
					})
					$("tbody").append(htmls)
				} else if(data.status == 1){
	        		alertMsg("2",data.errmsg,"fail")
	        	}
			},
			"error" : function(data) {
				alertMsg("2","后台获取数据出错!","fail")
			},
			beforeSend : null, // [Function]:请求之前回调函数（同jQuery）
			complete : null, // [Function]:请求完成回调函数（同jQuery）
			pageIndexName : 'pageIndex', // (已过时）[String]:自定义请求参数的当前页名称
			pageSizeName : 'iDisplayLength', // (已过时）[String]:自定义请求参数的每页数量名称
			totalName : 'iTotalRecords', // [String]:自定义返回的总页数名称，对象属性可写成'data.total'
			traditional : false
			// [Boolean]:参数序列化方式（同jQuery）
		},
	});
}

function submitResult(flg,uid){
	$.ajax({
		type: "GET",
        url: "../updateLoginUser",
        dataType: "JSON",
        async:false,
        data: {
        	
        	"uid":uid,
        	
        	"status":flg,
        	},
        success: function(data){
        	if(data.status == 0){
        		alert("审批成功");
        	}else if(data.status == 1){
        		alert(data.errmsg);
        	}
        },
	})
}

function editUser(obj){
	temp=obj;
	var info=JSON.parse($(obj).attr("detail"))
	var htmls="";
	picurl=info.picurl;
	htmls+='<div class="cover2"></div><div id="addAdminbox">'
   			+'<img class="closeWin" src="../images/guanbi.png" onclick="closeInfoWin()">'
   			+'<h1 class="wTitle">修改用户信息</h1><div class="infobox">'
   			+'<ul class="infoboxul"><li><span class="leftspan">姓名</span>'
   			+'<input type="text" class="infoinput" id="uname" value="'+info.uname+'"></li>'
   			+'<li><span class="leftspan">电话</span><input type="text" class="infoinput" id="phone" value="'+info.phone+'"></li>'
   			/*+'<li><span class="leftspan">邮箱</span>'
   			+'<input type="text" class="infoinput" id="email" value="'+info.email+'"></li>'*/
   			+'</ul>'
   			+'<ul class="infoboxul"><li id="sex"><span>性别</span>'
   			if(info.sex=="01"){
   				htmls+='<input type="radio" value="01" name="gender" checked style="vertical-align:sub;margin-left:10px;">男'
   		   			+'<input type="radio" value="02" name="gender" style="vertical-align:sub;margin-left:10px;">女'
   			}else{
   				htmls+='<input type="radio" value="01" name="gender" style="vertical-align:sub;margin-left:10px;">男'
   		   			+'<input type="radio" value="02" name="gender" style="vertical-align:sub;margin-left:10px;" checked>女'
   			}
   			
	htmls+='</li>'
   			
   			+'</ul><div class="iconwrap">'
   			+'<span id="zhaopian">照片</span><form id="myform" enctype="multipart/form-data" method="post">'
			+'<img src="'+info.picurl+'" id="portrait" >'
			+'<input type="file" class="imgfile" id="file" name="files" onchange="uploadImg()"><input hidden name="savetype" value="00"></form>'
			+'<p>413*626px,不超过1000kb</p></div></div><a class="ensure" onclick="editSaveInfo(\''+info.uid+'\')">保存</a></div>'
	$("body").append(htmls);
}
function editSaveInfo(uid){
	
	var uname=$("#uname").val();
	//var email=$("#email").val();
	var sex=$("#sex").find("input:checked").val();
	var phone=$("#phone").val();
	if(uname.trim()==""){
		alertMsg("2","请填写姓名！","fail");
		return;
	}else if(uname.indexOf(",")!=-1){
		alertMsg("2","姓名不允许出现非法字符！","fail");
		return;
	}
	/*if(email.trim()==""){
		alertMsg("2","请填写邮箱！","fail");
		return;
	}else{
		var reg=/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
		if(!reg.test(email)){
			alertMsg("2","邮箱格式不正确！","fail");
			return;
		}
	}*/
	if(sex==""||sex==undefined){
		alertMsg("2","请选择性别！","fail");
		return;
	}
	if(phone.trim()==""){
		alertMsg("2","请填写手机号！","fail");
		return;
	}else{
		var reg=/^1[34578]\d{9}$/; 
		if(!reg.test(phone)){
			alertMsg("2","手机号格式不正确！","fail");
			return;
		}
	}
	$.ajax({
		type: "GET",
        url: "../updateLoginUser",
        dataType: "JSON",
        async:false,
        data: {
        	"uid":uid,
        	"uname":uname,
        	//"email":email,
        	"sex":sex,
        	"phone":phone,
        	"roleflg":"00",
        	"picurl":picurl,
        },
        success: function(data){
        	if(data.status == 0){
        		closeInfoWin();
        		alertMsg("2","修改成功！","success")
        		setTimeout('location.reload()',2000);
        	}else if(data.status == 1){
        		closeInfoWin();
        		alertMsg("2",data.errmsg,"fail")
        	}
        },
	})
}

function closeInfoWin(){
	$(".cover2").remove()
	$("#addAdminbox").remove()
}
function uploadImg(){
	var filetype=$("#file").val().slice($("#file").val().lastIndexOf(".")+1).toUpperCase()
	if (filetype == 'JPG'||filetype == 'PNG'||filetype == 'JPEG'){
		$("#myform").ajaxSubmit({
			url : "../uploadFiles",
			dataType : 'json',
			async : false,
			success : function(data) {
				if(data.status==0){
					readAsDataURL();
					picurl=data.urls;
				}else if(data.status==1){
					alertMsg("2",data.errmsg,"fail")
				}
			},
			error:function(){
				alertMsg("2","上传失败！","fail")
			}
	    })
	}else{
		alertMsg("2","文件格式不正确！","fail")
		return
	}
  
}

//图片预览
function readAsDataURL(){
  var simpleFile = document.getElementById("file").files[0];
  var reader = new FileReader();
  // 将文件以Data URL形式进行读入页面
  reader.readAsDataURL(simpleFile);
  reader.onload = function(e){
      $("#portrait").attr("src",this.result);
  }
}

function addAdmin(){
	var htmls="";
	picurl="";
	htmls+='<div class="cover2"></div><div id="addAdminbox">'
   			+'<img class="closeWin" src="../images/guanbi.png" onclick="closeInfoWin()">'
   			+'<h1 class="wTitle">添加管理员</h1><div class="infobox">'
   			+'<ul class="infoboxul"><li><span class="leftspan">姓名</span>'
   			+'<input type="text" class="infoinput" id="uname"></li>'
   			+'<li><span class="leftspan">邮箱</span>'
   			+'<input type="text" class="infoinput" id="email"></li>'
   			+'<li><span class="leftspan">密码</span>'
   			+'<input type="text" class="infoinput" id="password" placeholder="密码包含8位及以上数字、字母或下划线"></li></ul>'
   			+'<ul class="infoboxul"><li id="sex"><span>性别</span>'
   			+'<input type="radio" value="01" name="gender" style="vertical-align:sub;margin-left:10px;">男'
   		   	+'<input type="radio" value="02" name="gender" style="vertical-align:sub;margin-left:10px;">女'
   		   	+'</li>'
   			+'<li><span>电话</span><input type="text" class="infoinput" id="phone"></li>'
   			+'</ul><div class="iconwrap">'
   			+'<span id="zhaopian">照片</span><form id="myform" enctype="multipart/form-data" method="post">'
			+'<img src="" id="portrait" >'
			+'<input type="file" class="imgfile" id="file" name="files" onchange="uploadImg()"><input hidden name="savetype" value="00"></form>'
			+'<p>413*626px,不超过1000kb</p></div></div><a class="ensure" onclick="saveInfo()">保存</a></div>'
	$("body").append(htmls);
}

function saveInfo(){
	var uname=$("#uname").val();
	var email=$("#email").val();
	var sex=$("#sex").find("input:checked").val();
	var phone=$("#phone").val();
	var password=$("#password").val();
	if(uname.trim()==""){
		alertMsg("2","请填写姓名！","fail");
		return;
	}else if(uname.indexOf(",")!=-1){
		alertMsg("1","姓名不允许出现非法字符！","fail");
		return;
	}
	if(email.trim()==""){
		alertMsg("2","请填写邮箱！","fail");
		return;
	}else{
		var reg=/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
		if(!reg.test(email)){
			alertMsg("2","邮箱格式不正确！","fail");
			return;
		}
	}
	if(sex==""||sex==undefined){
		alertMsg("2","请选择性别！","fail");
		return;
	}
	if(phone.trim()==""){
		alertMsg("2","请填写手机号！","fail");
		return;
	}else{
		var reg=/^1[34578]\d{9}$/; 
		if(!reg.test(phone)){
			alertMsg("2","手机号格式不正确！","fail");
			return;
		}
	}
	if(password.trim()==""){
		alertMsg("2","请填写密码！","fail");
		return;
	}else{
		var reg=/^[a-zA-Z\d_]{8,}$/;
		if(!reg.test(password)){
			alertMsg("2","密码格式不正确！","fail");
			return;
		}
	}
	$.ajax({
		type: "GET",
        url: "../addUser",
        dataType: "JSON",
        async:false,
        data: {
        	"uname":uname,
        	"email":email,
        	"sex":sex,
        	"phone":phone,
        	"password":password,
        	"roleflg":"04",
        	"picurl":picurl,
        },
        success: function(data){
        	if(data.status == 0){
        		closeInfoWin();
        		alertMsg("2","添加成功！","success")
        		setTimeout('location.reload()',2000);
        	}else if(data.status == 1){
        		closeInfoWin();
        		alertMsg("2",data.errmsg,"fail")
        	}
        },
	})
}

function delAdmin(obj){
	closeWin();
	var uid=$(tempobj).attr("uid");
	$.ajax({
		type: "GET",
        url: "../updateLoginUser",
        dataType: "JSON",
        async:false,
        data: {
        	"uid":uid,
        	"delflg":"01"
        },
        success: function(data){
        	if(data.status == 0){
        		alertMsg("2","删除成功！","success")
        		setTimeout('location.reload()',2000);
        	}else if(data.status == 1){
        		alertMsg("2",data.errmsg,"fail")
        	}
        },
	})
}

function clearSearch(){
	$(".searchul input").each(function(){
		$(this).val("");
		if($(this).attr("altvalue")!=null){
			$(this).attr("altvalue","")
		}
	})
}