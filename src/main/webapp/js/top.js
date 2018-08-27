/*$(function(){
	//回显顶部信息
	findUserHead();
	
	//退出登录
	logout();
})*/

var uid = sessionStorage.getItem("uid");
//回显顶部信息
function findUserHead(){
	$.ajax({
		type: "GET",
        url: "../findLoginUser",
        dataType: "JSON",
        async:false,
        data: {"uid":uid},
        success:function(data){
        	$("#avatar").attr("src",data.user.picurl);
        	$("#username").text(data.user.uname);
        }
	})
}