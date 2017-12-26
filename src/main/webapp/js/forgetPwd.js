//获取验证码
function getCheckCode(){
	 var email=$("#mail").val();
	 var reg=/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/
	 if(reg.test(email)){
		 $.ajax({
	         type: "GET",
	         url: "../sendCheckCodeToEmail",
	         async:false,
	         data: {"email":email},
	         dataType: "json",
	         success: function(data){
	                    if(data.status==0){
	                    	sessionStorage.setItem("email",email)
	                    	setTimeout('window.open("forgetPwd-second.html","_self")',2000)
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
        url: "../verifyCheckCodeAndUpdatePsw",
        async:false,
        data: {
        	"email":email,
        	"code":code,
        	"psw":pwd1
        },
        dataType: "json",
        success: function(data){
                   if(data.status==0){
                	alertMsg("1","设置成功!","success")   
                   	setTimeout('window.open("../login.html","_self")',2000)
                   }else if(data.status==1){
                	   alertMsg("1",data.errmsg,"fail")   
                   }
             	}
    }); 
	
}