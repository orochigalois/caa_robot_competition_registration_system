$(function(){
	$("#pwd").focus(function(){
		
		$(this).prev().css("background-image","url(images/icon_land_password_b.png)");
		$(this).next().show()
	})
	$("#pwd").blur(function(){
		$(this).prev().css("background-image","url(images/icon_land_password.png)");
		$(this).next().hide()
	})
	$("#uname").focus(function(){
		$(this).prev().css("background-image","url(images/icon_land_username_b.png)");
		$(this).next().show()
	})
	$("#uname").blur(function(){
		$(this).prev().css("background-image","url(images/icon_land_username.png)");
		
	})
	$(".unamebox .inputbg").click(function(e){
		$(this).prev().val("")
	})
	$(".pwdbox .inputbg").hover(function(){
		$(this).prev().attr("type","text")
	},function(){
		$(this).prev().attr("type","password")
	})
})
var baseurl="http://"+window.location.host+'/robot/jsp/';
var uid = sessionStorage.getItem("uid");
function loginAcount(){
	/*if($.cookie("authId")!=null){
		alertMsg("99","您已登录其他用户！如更换账号请退出所有已登录的网页！","fail")
		
		setTimeout(function() {
			window.location.reload();
		}, 3000);
		return;
	}*/
	var email=$("#uname").val();
	var password=$("#pwd").val();
	if(email.trim()==""){
		showMsg("请输入账号！")
		//alertMsg("1","请输入账号！","fail")
		return;
	}
	if(password.trim()==""){
		showMsg("请输入密码！")
		//alertMsg("1","请输入密码！","fail")
		return;
	}
	$.ajax({
        type: "GET",
        url: "LogonByEmail",
        async:false,
        data: {
        		"email":email,
        		"password":password
        	},
        dataType: "json",
        success: function(data){
                   if(data.status==0){
	                	sessionStorage.setItem("uid",data.info.uid);
	                	sessionStorage.setItem("roleflg",data.info.roleflg);
	                	if(data.info.roleflg=="03"){
	                		if(data.info.status=="00"){
	                			window.open("jsp/main.html","_self");
	                		}else if(data.info.status=="01"){
	                			showMsg("该用户未审核,请联系管理员！")
	                		}else if(data.info.status=="03"){
	                			showMsg("该用户未通过审核,请联系管理员！")
	                		}
	                		
	                	}else if(data.info.roleflg=="00"||data.info.roleflg=="04"){
	                		window.open("jsp/BMS-competitionList.html","_self");
	                	}else if(data.info.roleflg=="02"){
	                		window.open("jsp/BMS-gradeManage.html","_self");
	                	}		
                   }else if(data.status==1){
                	   showMsg(data.errmsg)
                   }
            }
    }); 
}

function showMsg(text){
	$(".wTitle").html(text);
	$(".cover1,.msgWin1").show();
}

function closeIndexWin(){
	$(".cover1,.msgWin1").hide();
}