$(function(){
	$("#cancel").click(function(){
		$('#cover,#selImg').hide()
	})
	$("#ensure").click(function(){
		$("#imgwrap").html($(".photobox").html())
		$('#cover,#selImg').hide()
	})
	$("#imgwrap").click(function(){
		$("#cover,#selImg").show()
	})
	$("#gender").click(function(){
		$(this).next().show();
		
		$("#genderli li").click(function(){
			// $("#ifinvoiveli").hide();
			$("#gender").val($(this).text());
			$("#gender").attr("altvalue",$(this).attr("altvalue"));
			$(this).parent().hide();
		})
	})

	$("#ifinvoice").click(function(){
		$(this).next().show();
		$("#ifinvoiceli li").click(function(){
			// $("#genderli").hide();
			$("#ifinvoice").val($(this).text());
			$("#ifinvoice").attr("altvalue",$(this).attr("altvalue"));

			if($(this).attr("altvalue")=='01'){
    			$("#invoicenamediv").removeAttr("style");
    			$("#banknumberdiv").removeAttr("style");
    			$("#addressphonediv").removeAttr("style");
    			$("#taxpayernumberdiv").removeAttr("style");
    		}else if($(this).attr("altvalue")=='00'){
    			$("#invoicenamediv").attr("style","display:none");
    			$("#banknumberdiv").attr("style","display:none");
    			$("#addressphonediv").attr("style","display:none");
    			$("#taxpayernumberdiv").attr("style","display:none");
			}
			
			$(this).parent().hide();
		})
	})
})
var baseurl="http://"+window.location.host+'/robot/jsp/';
var picurl="";
//获取验证码
function getCheckCode(){
	 var email=$("#mail").val();
	 var reg=/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/
	 if(reg.test(email)){
		 $.ajax({
	         type: "GET",
	         url: "../sendCheckCodeToEmail",
	         async:false,
	         data: {"email":email,"registerflg":"00"},
	         dataType: "json",
	         success: function(data){
	                    if(data.status==0){
	                    	sessionStorage.setItem("email",email)
	                    	window.open("register-second.html","_self")
	                    }else if(data.status==1){
	                    	alertMsg("1",data.errmsg,"fail")
	                    }
	                  }
	     }); 
	 }else{
		 alertMsg("1","邮箱格式错误！","fail")
	 }
}

//校验密码格式
function checkPwd(){
	var pwd1=$("#pwd1").val();
	var reg=/^[a-zA-Z\d_]{8,}$/;
	if(pwd1.trim()!=""){
		if(!reg.test(pwd1.trim())){
			alertMsg("1","密码格式不符合要求！","fail")
			return false;
		}else{
			return true;
		}
	}
}

//校验是否相同
function isPwdEqual(){
	var pwd1=$("#pwd1").val();
	var pwd2=$("#pwd2").val();
	if(pwd1.trim()!=""&&pwd2.trim()!=""){
		if(!(pwd1==pwd2)){
			alertMsg("1","两次密码不相同！","fail")
			return false;
		}else{
			return true;
		}
	}
}

//校验验证码
function checkCode(){
	var pwd1=$("#pwd1").val();
	var pwd2=$("#pwd2").val();
	var code=$("#code").val();
	if(pwd1.trim()==""){
		alertMsg("1","请填写密码！","fail")
		return;
	}else{
		if(!checkPwd()){
			return;
		}
	}
	if(pwd2.trim()==""){
		alertMsg("1","请确认密码！","fail")
		return;
	}else{
		if(!isPwdEqual()){
			return;
		}
	}
	if(code.trim()==""){
		alertMsg("1","请填写验证码！","fail")
		return;
	}
	var email=sessionStorage.getItem("email");
	$.ajax({
        type: "GET",
        url: "../verifyCheckCode",
        async:false,
        data: {
        	"email":email,
        	"code":code
        },
        dataType: "json",
        success: function(data){
                   if(data.status==0){
                   	sessionStorage.setItem("code",code)
                   	sessionStorage.setItem("password",pwd1)
                   	window.open("register-third.html","_self")
                   }
                 }
    }); 
	
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
					/*alertMsg("1","上传成功！","success")*/
					picurl=data.urls;
				}else if(data.status==1){
					alertMsg("1",data.errmsg,"fail")
				}
			},
			error:function(){
				alertMsg("1","上传失败！","fail")
			}
	    })
	}else{
		alertMsg("1","文件格式不正确！","fail")
		return
	}
    
    //$(f).remove()
}

//注册信息
function completeSign(){
	/*if(picurl==""){
		alertMsg("1","请先上传头像！","fail")
		return;
	}*/
	var name=$("#name").val();
	var sex=$("#gender").attr("altvalue");
	var phone=$("#phone").val();
	var school=$("#school").val();
	var department=$("#department").val();
	var invoicename=$("#invoicename").val();
	var taxpayernumber=$("#taxpayernumber").val();
	var banknumber=$("#banknumber").val();
	var addressphone=$("#addressphone").val();
	var receiveaddress=$("#receiveaddress").val();
	var code=sessionStorage.getItem("code");
	var password=sessionStorage.getItem("password");
	var email=sessionStorage.getItem("email");
	
	if(name.trim()==""){
		alertMsg("1","请填写姓名！","fail")
		return;
	}else if(name.indexOf(",")!=-1){
		alertMsg("1","姓名不允许出现非法字符！","fail");
		return;
	}
	if(sex.trim()==""){
		alertMsg("1","请选择性别！","fail")
		return;
	}
	if(phone.trim()==""){
		alertMsg("1","请填写电话！","fail")
		return;
	}else if(!isPhoneNo(phone)){
		alertMsg("1","电话格式错误！","fail")
		return;
	}
	if(school.trim()==""){
		alertMsg("1","请填写学校！","fail")
		return;
	}

	var ifinvoice=$("#ifinvoice").attr("altvalue");
	var ifinvoiceval = $('#ifinvoice').val();

	if(ifinvoiceval.trim()==""){
		alertMsg("1","请选择开具发票！","fail")
		return;
	}else{
		if(ifinvoice=='01'){
			if(invoicename.trim()==""){
				alertMsg("1","请输入发票名称！","fail")
				return;
			}
			if(taxpayernumber.trim()==""){
				alertMsg("1","请填写纳税人识别号！","fail")
				return;
			}
		}else if(ifinvoice=='00'){
			$("#invoicename").val("");
			$("#banknumber").val("");
			$("#addressphone").val("");
			$("#taxpayernumber").val("");
		}
	}

	/*if(invoicename.trim()==""){
		alertMsg("1","请填写发票名称！","fail")
		return;
	}
	if(banknumber.trim()==""){
		alertMsg("1","请填写开户行及账号！","fail")
		return;
	}
	if(addressphone.trim()==""){
		alertMsg("1","请填写地址及电话！","fail")
		return;
	}*/
	if(receiveaddress.trim()==""){
		alertMsg("1","请填写收发票地址！","fail")
		return;
	}
	$.ajax({
        type: "GET",
        url: "../signUser",
        async:false,
        data: {
        	"email":email,
        	"code":code,
        	"password":password,
        	"uname":name,
        	"sex":sex,
        	"phone":phone,
        	"school":school,
        	"department":department,
        	"picurl":picurl,
        	"invoicename":invoicename,
        	"taxpayernumber":taxpayernumber,
        	"banknumber":banknumber,
        	"addressphone":addressphone,
			"receiveaddress":receiveaddress,
			"ifinvoice":ifinvoice
        },
        dataType: "json",
        success: function(data){
                   if(data.status==0){
                	alertMsg("1","注册成功!","success")
                   	setTimeout('window.open("../login.html","_self")',2000)
                   }else if(data.status==1){ 
                	   alertMsg("1",data.errmsg,"fail")
                   }
                 }
    }); 
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

//验证手机号
function isPhoneNo(phone) { 
var pattern = /^1[34578]\d{9}$/; 
return pattern.test(phone); 
}