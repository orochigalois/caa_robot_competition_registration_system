$(function(){
	getMemInfo()
})

var tmid=sessionStorage.getItem("tmid");

function getMemInfo(){
	$.ajax({
		type: "GET",
        url: "../findMemberInfoByTmid",
        dataType: "JSON",
        async:false,
        data: {
        	"tmid":tmid,
        	},
        success: function(data){
        	if(data.status == 0){
        		var info=data.info;
        		$("#tmname").html(info.tmname);
        		if(info.didtype=="00"){$("#didtype").html('身份证');}
        		else if(info.didtype=="01"){$("#didtype").html('护照');}
        		else{$("#didtype").html('港澳台往来大陆通行证');}
        		$("#tmcode").html(info.tmcode);
        		if(info.diningtype=="01"){$("#diningtype").html('普通');}
        		else if(info.diningtype=="02"){$("#diningtype").html('清真');}
        		else{$("#didtype").html('素食');}
        		if(info.roleflg=="01"){$("#roleflg").html('指导教师');}
        		else if(info.roleflg=="02"){$("#roleflg").html('学生');}
        		$("#did").html(info.did);
        		$("#birthday").html(info.birthday);
        		if(info.sex=="01"){$("#sex").html('男');}
        		else if(info.sex=="02"){$("#sex").html('女');}
        		$("#email").html(info.email);
        		$("#school").html(info.school);
        		$.each(folkname,function(i,nation){
        			if(info.folk==nation.folkid){
        				$("#nation").html(nation.folk);
        			}
        		})
        		$("#phone").html(info.phone);
        		$("#departname").html(info.departname);
        		$("#memhead").attr("src",info.picurl);
        	}else if(data.status == 1){
        		alertMsg("1",data.errmsg,"fail")
        	}
        },
	})
}
