$(function(){
	
	$(document).click(function(){
		$(".emulate").hide();
	})
	
	$(".selinput").click(function(e){
    	e.stopPropagation();
    	var me=this;
    	$(me).next().show();
    	$(me).next().children().click(function(){
    		$(me).val($(this).text());
    		$("#editifinvoice").attr("altvalue",$(this).attr("altvalue"));
    		if($(this).attr("altvalue")=='1'){
    			$("#editinvoicenameli").removeAttr("style");
    			$("#edittaxpayernumberli").removeAttr("style");
    			$("#editbanknumberli").removeAttr("style");
    			$("#editaddressphoneli").removeAttr("style");
    		}else if($(this).attr("altvalue")=='0'){
    			$("#editinvoicenameli").attr("style","display:none");
    			$("#edittaxpayernumberli").attr("style","display:none");
    			$("#editbanknumberli").attr("style","display:none");
    			$("#editaddressphoneli").attr("style","display:none");
    		}
    		$(me).next().hide();
    	})
    })
    
    
	//回显用户信息
	findLoginUser();
	//编辑用户信息并保存
	updateLoginUser();
	$("#editinfo").click(function(){
		$("#userinfo").addClass("hideInfo");
		$("#editInfo").removeClass("hideInfo");
		editFindLoginUser();
	});
	$("#cancel").click(function(){
		window.location.reload();
	})
	$("#abolish").click(function(){
		$('#cover,#selImg').hide()
	})
	$("#editphoto").click(function(){
		$("#cover,#selImg").show();
	})
	$("#ensure").click(function(){
		$("#editphoto").attr("src",$(".photobox").children().attr("src"));
		$('#cover,#selImg').hide()
		uploadImg();
	})
	
	//修改密码
	$("#editcode").click(function(){
		$(".contentbox>div").addClass("hideInfo");
		$("#changepwd").removeClass("hideInfo");
		pwdFindLoginUser();
	})
})
var uid = sessionStorage.getItem("uid");
var picurl="";
//回显用户信息
function findLoginUser(){
	$.ajax({
		type: "GET",
        url: "../findLoginUser",
        dataType: "JSON",
        async:false,
        data: {"uid":uid},
        success:function(data){
        	$("#userphoto").attr("src",data.user.picurl);
        	$("#infoname").text(data.user.uname);
        	if(data.user.sex == "01"){
        		$("#infosex").text("男");
        	}else if(data.user.sex == "02"){
        		$("#infosex").text("女");
        	}
        	$("#infoemail").text(data.user.email);
        	$("#infophone").text(data.user.phone);
        	$("#infoschool").text(data.user.school);
        	$("#infocollege").text(data.user.department);
        	$("#infoinvoicename").text(data.user.invoicename);
        	$("#infotaxpayernumber").text(data.user.taxpayernumber);
        	$("#infobanknumber").text(data.user.banknumber);
        	$("#infoaddressphone").text(data.user.addressphone);
        	$("#inforeceiveaddress").text(data.user.receiveaddress);
        	if(data.user.status == "00"){
        		$("#infostatus").text("正常");
        	}else if(data.user.status == "01"){
        		$("#infostatus").text("未审批");
        	}else if(data.user.status == "02"){
        		$("#infostatus").text("禁用");
        	}
        	
        }
	})
}


//编辑用户页面的回显
function editFindLoginUser(){
	$.ajax({
		type: "GET",
        url: "../findLoginUser",
        dataType: "JSON",
        async:false,
        data: {"uid":uid},
        success:function(data){
        	picurl=data.user.picurl;
        	$("#editphoto").attr("src",data.user.picurl);
        	$("#editname").val(data.user.uname);
        	if(data.user.sex == "01"){
        		$("#male").attr("checked",true);
        	}else if(data.user.sex == "02"){
        		$("#famale").attr("checked",true);
        	}
        	$("#editemail").val(data.user.email);
        	$("#editphone").val(data.user.phone);
        	$("#editschool").val(data.user.school);
        	$("#editdepartment").val(data.user.department);
        	$("#editinvoicename").val(data.user.invoicename);
        	$("#edittaxpayernumber").val(data.user.taxpayernumber);
        	$("#editbanknumber").val(data.user.banknumber);
        	$("#editaddressphone").val(data.user.addressphone);
        	$("#editreceiveaddress").val(data.user.receiveaddress);
        }
	})
}

//修改密码的回显
function pwdFindLoginUser(){
	$.ajax({
		type: "GET",
        url: "../findLoginUser",
        dataType: "JSON",
        async:false,
        data: {"uid":uid},
        success:function(data){
        	$("#changepwd .userphoto").attr("src",data.user.picurl);
        	$("#pwd-uname").val(data.user.uname);
        	$("#pwd-email").val(data.user.email);
        }
	})
}
//编辑用户信息并保存
function updateLoginUser(){
	$("#editInfo .save").click(function(){
		if($("#editname").val().trim()==""){
			alertMsg("1","请输入姓名!","fail");
			return;
		}else if($("#editname").val().indexOf(",")!=-1){
			alertMsg("1","姓名不允许出现非法字符！","fail");
			return;
		}else if(!($("#male").prop("checked") == true || $("#famale").prop("checked") == true)){
			alertMsg("1","请选择性别!","fail");
			return;
		}
		else if($("#editemail").val().trim()==""){
			alertMsg("1","请输入邮箱!","fail");
			return;
		}
		else if($("#editphone").val().trim()==""){
			alertMsg("1","请输入电话!","fail");
			return;
		}
		else if($("#editschool").val().trim()==""){
			alertMsg("1","请输入学校/供职单位!","fail");
			return;
		}
		else if($("#editreceiveaddress").val().trim()==""){
			alertMsg("1","请输入收件地址!","fail");
			return;
		}
		
		var ifinvoice=$("#editifinvoice").attr("altvalue");
		var editifinvoice = $('#editifinvoice').val();
		var editinvoicename = $('#editinvoicename').val();
		var edittaxpayernumber = $('#edittaxpayernumber').val();
		
		if(editifinvoice.trim()==""){
			alertMsg("1","请选择开具发票！","fail")
			return;
		}else{
			if(ifinvoice=='1'){
				if(editinvoicename.trim()==""){
					alertMsg("1","请输入发票抬头！","fail")
					return;
				}
				if(edittaxpayernumber.trim()==""){
					alertMsg("1","请输入纳税人识别号！","fail")
					return;
				}
			}
		}

		/*else if($("#editinvoicename").val().trim()==""){
			alertMsg("1","请输入发票名称!","fail");
			return;
		}
		else if($("#editbanknumber").val().trim()==""){
			alertMsg("1","请输入开户行及账号!","fail");
			return;
		}
		else if($("#editaddressphone").val().trim()==""){
			alertMsg("1","请输入地址及电话!","fail");
			return;
		}
		else if($("#editreceiveaddress").val().trim()==""){
			alertMsg("1","请输入收发票地址!","fail");
			return;
		}*/
		$.ajax({
			type: "GET",
	        url: "../updateLoginUser",
	        dataType: "JSON",
	        async:false,
	        data: {
	        	"uid":uid,
	        	"picurl":picurl,
	        	"uname":$("#editname").val(),
	        	"phone":$("#editphone").val(),
	        	"school":$("#editschool").val(),
	        	"department":$("#editdepartment").val(),
	        	"sex":$("input:checked").val(),
	        	"invoicename":$("#editinvoicename").val(),
	        	"taxpayernumber":$("#edittaxpayernumber").val(),
	        	"banknumber":$("#editbanknumber").val(),
	        	"addressphone":$("#editaddressphone").val(),
	        	"receiveaddress":$("#editreceiveaddress").val(),
	        	},
	        success: function(data){
	        	console.log(data)
	        	if(data.status == 0){
	        		alertMsg("1","保存成功!","success");
	        		setTimeout('window.location.reload()',2000);
	        	}else if(data.status == 1){
	        		alertMsg("1",data.errmsg,"fail");
	        	}
	        },
		})
	})
}

//上传头像
function uploadImg(){
	var filetype=$("#file").val().slice($("#file").val().lastIndexOf(".")+1).toUpperCase()
	if (filetype == 'JPG'||filetype == 'PNG'||filetype == 'JPEG'){
		$("#myform").ajaxSubmit({
			url : "../uploadFiles",
			dataType : 'json',
			async : false,
			success : function(data) {
				if(data.status==0){
					console.log(data)
					alertMsg("1","上传成功!","success");
					picurl=data.urls;
				}else if(data.status==1){
					alertMsg("1",data.errmsg,"fail");
				}
			},
			error:function(){
				alertMsg("1","上传失败！","fail");
			}
	    })
	}else{
		alertMsg("1","文件格式不正确!","fail");
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
      $(".photobox").html('<img src="'+this.result+'" alt=""/>');
  }
}

//修改密码
function updatePwd(){
	var oldpwd=$("#oldpassword").val();
	var newpwd1=$("#newpassword1").val();
	var newpwd2=$("#newpassword2").val();
	if(oldpwd.trim()==""){
		alertMsg("1","请填写原密码！","fail");
		return;
	}else{
		if(!checkPwd($("#oldpassword"))){
			return;
		}
	}
	if(newpwd1.trim()==""){
		alertMsg("1","请填写新密码！","fail");
		return;
	}else{
		if(!checkPwd($("#newpassword1"))){
			return;
		}
	}
	if(newpwd2.trim()==""){
		alertMsg("1","请确认密码！","fail");
		return;
	}else{
		if(!isPwdEqual()){
			return;
		}
	}
	$.ajax({
		type: "GET",
        url: "../updatepassword",
        dataType: "JSON",
        async:false,
        data: {
        	"uid":uid,
        	"oldpassword":oldpwd,
        	"newpassword":newpwd1,
        	},
        success: function(data){
        	console.log(data)
        	if(data.status == 0){
        		alertMsg("1","修改成功！","success");
        		setTimeout('window.open("../login.html","_self")',2000)
        	}else if(data.status == 1){
        		alertMsg("1",data.errmsg,"fail");
        	}
        },
	})
}

//校验密码格式
function checkPwd(obj){
	var pwd=$(obj).val();
	var reg=/^[a-zA-Z\d_]{8,}$/;
	if(pwd.trim()!=""){
		if(!reg.test(pwd.trim())){
			alertMsg("1","密码格式不符合要求","fail");
			return false;
		}else{
			return true;
		}
	}
}

//校验是否相同
function isPwdEqual(){
	var pwd1=$("#newpassword1").val();
	var pwd2=$("#newpassword2").val();
	if(pwd1.trim()!=""&&pwd2.trim()!=""){
		if(!(pwd1==pwd2)){
			alertMsg("1","两次密码不相同！","fail");
			return false;
		}else{
			return true;
		}
	}
}