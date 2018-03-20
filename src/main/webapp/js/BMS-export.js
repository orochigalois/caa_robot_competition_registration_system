$(function(){
	//getAllMatch()
	bmsgetcurmatchname();
	getAllFrname();
	var htmls="";
	$.each(regions,function(i,value){
		htmls+='<li>'+value+'</li>'
	})
	$(".region").next().append(htmls);
	htmls="";
	$.each(orgtypes,function(i,value){
		htmls+='<li>'+value+'</li>'
	})
	$(".orgtype").next().append(htmls);
	$(".selinput").click(function(e){
     	e.stopPropagation();
     	$(".emulate").hide();
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

function getAllMatch(){
	$.ajax({
		type: "GET",
        url: "../findAllMatch",
        dataType: "JSON",
        async:false,
        data: {
        	
        	},
        success: function(data){
        	if(data.status == 0){
        		var htmls="";
        		$.each(data.list,function(i,team){
        			htmls+='<li mid="'+team.mid+'">'+team.mname+'</li>'
        		})
        		$(".frname").next().append(htmls)
        	}else if(data.status == 1){
           		alertMsg("2",data.errmsg,"fail")
        	}
        },
	})
}

function getAllMatch(){
	$.ajax({
		type: "GET",
        url: "../findAllMatch",
        dataType: "JSON",
        async:false,
        data: {
        	
        	},
        success: function(data){
        	if(data.status == 0){
        		var htmls="";
        		$.each(data.list,function(i,team){
        			htmls+='<li altvalue="'+team.mid+'">'+team.mname+'</li>'
        		})
        		$(".mname").next().append(htmls)
        	}else if(data.status == 1){
           		alertMsg("2",data.errmsg,"fail")
        	}
        },
	})
}

function getAllFrname(){
	var mid=sessionStorage.getItem("currentmid");
	$.ajax({
		type: "GET",
        url: "../findRaceFrnameBymid",
        dataType: "JSON",
        async:false,
        data: {
        	"mid":mid,
        	},
        success: function(data){
        	if(data.status == 0){
        		$(".frname").next().empty();
        		var htmls="";
        		$.each(data.list,function(i,match){
        			htmls+='<li onclick="getAllRname(this)">'+match.frname+'</li>'
        		})
        		$(".frname").next().append(htmls)
        	}else if(data.status == 1){
           		alertMsg("2",data.errmsg,"fail")
        	}
        },
	})
}

function getAllRname(obj){
	var mid=sessionStorage.getItem("currentmid");
	var frname=$(obj).text();
	$.ajax({
		type: "GET",
        url: "../findRaceRnameBymidFrname",
        dataType: "JSON",
        async:false,
        data: {
        	"mid":mid,
        	"frname":frname
        	},
        success: function(data){
        	if(data.status == 0){
        		console.log(data)
        		$(".rname").next().empty();
        		var htmls="";
        		$.each(data.list,function(i,match){
        			htmls+='<li altvalue="'+match.rid+'">'+match.rname+'</li>'
        		})
        		$(".rname").next().append(htmls)
        	}else if(data.status == 1){
           		alertMsg("2",data.errmsg,"fail")
        	}
        },
	})
}

function exportPlayer(){
	var mname=$(".currentmatch").text();
	var mid=sessionStorage.getItem("currentmid");
	var tmname=$("#searchMem .tmname").val();
	var sex=$("#searchMem .sex").attr("altvalue");
	var didtype=$("#searchMem .didtype").attr("altvalue");
	var did=$("#searchMem .did").val();
	var roleflg=$("#searchMem .roleflg").attr("altvalue");
	var diningtype=$("#searchMem .diningtype").attr("altvalue");
	var param={};
	param.mname=mname;
	param.mid=mid;
	param.tmname=tmname;
	param.sex=sex;
	param.didtype=didtype;
	param.roleflg=roleflg;
	param.diningtype=diningtype;
	param.did=did;
	var form=$("<form>");//定义一个form表单
	form.attr("style","display:none");
	form.attr("target","");
	form.attr("method","post");
	//form.attr("action","../memberDerivedByMid");
	$("body").append(form);//将表单放置在web中
	$.each(param,function(key,value){
		var input1=$("<input>");
		input1.attr("type","hidden");
		input1.attr("name",key);
		input1.attr("value",value);
		form.append(input1);
	})
	form.ajaxSubmit({
			url : "../memberDerivedByMid",
			dataType : 'json',
			async : false,
			success : function(data) {
				if(data.status==0){
					console.log(data)
					//location.href="../downFileByPath?path="+data.path
					//window.location=data.fileurl; 
					setTimeout(function() {
						window.location=data.fileurl; 
					}, 2000)					
					
				}else if(data.status == 1){
	        		alertMsg("2",data.errmsg,"fail")
	        	}
			},
			error:function(){
				alertMsg("2","导出失败！","fail")
			}
	    })
	//form.submit();//表单提交 
	form.remove();
	/*$.ajax({
		type: "GET",
        url: "../memberDerivedByMid",
        dataType: "JSON",
        async:false,
        data: {
        	"mid":mid,
        	"tmname":tmname,
        	"sex":sex,
        	"didtype":didtype,
        	"did":did,
        	"roleflg":roleflg,
        	"diningtype":diningtype,
        	},
        success: function(data){
        	if(data.status == 0){
        		
        	}else if(data.status == 1){
           		alertMsg("2",data.errmsg,"fail")
        	}
        },
	})*/
}

function exportTeam(){
	var mname=$(".currentmatch").text();
	var mid=sessionStorage.getItem("currentmid");
	var tname=$("#searchteam .tname").val();
	var tcode=$("#searchteam .tcode").val();
	var tschool=$("#searchteam .tschool").val();
	var orgtype=$("#searchteam .orgtype").val();
	var region=$("#searchteam .region").val();
	var uname=$("#searchteam .uname").val();
	var email=$("#searchteam .email").val();
	
	var param={};
	param.mname=mname;
	param.mid=mid;
	param.tname=tname;
	param.tcode=tcode;
	param.tschool=tschool;
	param.orgtype=orgtype;
	param.region=region;
	param.uname=uname;
	param.email=email;
	
	var form=$("<form>");//定义一个form表单
	form.attr("style","display:none");
	form.attr("target","");
	form.attr("method","post");
	//form.attr("action","../teamDerivedByMid");
	$("body").append(form);//将表单放置在web中
	$.each(param,function(key,value){
		var input1=$("<input>");
		input1.attr("type","hidden");
		input1.attr("name",key);
		input1.attr("value",value);
		form.append(input1);
	})
	form.ajaxSubmit({
			url : "../teamDerivedByMid",
			dataType : 'json',
			async : false,
			success : function(data) {
				if(data.status==0){
					console.log(data)
					//location.href="../downFileByPath?path="+data.path
					//window.location=data.fileurl;
					setTimeout(function() {
						window.location=data.fileurl; 
					}, 2000)
				}else if(data.status == 1){
	        		alertMsg("2",data.errmsg,"fail")
	        	}
			},
			error:function(){
				alertMsg("2","导出失败！","fail")
			}
	    })
	//form.submit();//表单提交 
	form.remove();
	/*$.ajax({
		type: "GET",
        url: "../teamDerivedByMid",
        dataType: "JSON",
        async:false,
        data: {
        	"mid":mid,
        	"tname":tname,
        	"tcode":tcode,
        	"tschool":tschool,
        	"orgtype":orgtype,
        	"region":region,
        	"uname":uname,
        	"email":email,
        	},
        success: function(data){
        	if(data.status == 0){
        		
        	}else if(data.status == 1){
           		alertMsg("2",data.errmsg,"fail")
        	}
        },
	})*/
}

function exportRaceteam(){
	var mname=$(".currentmatch").text();
	var mid=sessionStorage.getItem("currentmid");
	var frname=$("#raceTeam .frname").val();
	var rname=$("#raceTeam .rname").val();
	var tname=$("#raceTeam .tname").val();
	var tcode=$("#raceTeam .tcode").val();
	var tschool=$("#raceTeam .tschool").val();
	var orgtype=$("#raceTeam .orgtype").val();
	var region=$("#raceTeam .region").val();
	var uname=$("#raceTeam .uname").val();
	var email=$("#raceTeam .email").val();
	var infostatus=$("#raceTeam .infostatus").attr("altvalue");
	var ckstatus=$("#raceTeam .ckstatus").attr("altvalue");
	
	var param={};
	param.mname=mname;
	param.mid=mid;
	param.frname=frname;
	param.rname=rname;
	param.tname=tname;
	param.tcode=tcode;
	param.tschool=tschool;
	param.orgtype=orgtype;
	param.region=region;
	param.uname=uname;
	param.email=email;
	param.infostatus=infostatus;
	param.ckstatus=ckstatus;
	
	
	var form=$("<form>");//定义一个form表单
	form.attr("style","display:none");
	form.attr("target","");
	form.attr("method","post");
	//form.attr("action","../teamDerivedByMid");
	$("body").append(form);//将表单放置在web中
	$.each(param,function(key,value){
		var input1=$("<input>");
		input1.attr("type","hidden");
		input1.attr("name",key);
		input1.attr("value",value);
		form.append(input1);
	})
	form.ajaxSubmit({
			url : "../teamRaceDerivedByMid",
			dataType : 'json',
			async : false,
			success : function(data) {
				if(data.status==0){
					console.log(data)
					//location.href="../downFileByPath?path="+data.path
					//window.location=data.fileurl;
					setTimeout(function() {
						window.location=data.fileurl; 
					}, 2000)
				}else if(data.status == 1){
	        		alertMsg("2",data.errmsg,"fail")
	        	}
			},
			error:function(){
				alertMsg("2","导出失败！","fail")
			}
	    })
	//form.submit();//表单提交 
	form.remove();
	/*$.ajax({
		type: "GET",
        url: "../teamDerivedByMid",
        dataType: "JSON",
        async:false,
        data: {
        	"mid":mid,
        	"frname":frname,
        	"rname":rname,
        	"tname":tname,
        	"tcode":tcode,
        	"tschool":tschool,
        	"orgtype":orgtype,
        	"region":region,
        	"uname":uname,
        	"email":email,
        	"infostatus":infostatus,
        	},
        success: function(data){
        	if(data.status == 0){
        		
        	}else if(data.status == 1){
           		alertMsg("2",data.errmsg,"fail")
        	}
        },
	})*/
}
function exportTeamaward(){
	var mname=$(".currentmatch").text();
	var mid=sessionStorage.getItem("currentmid");
	var param={};
	param.mname=mname;
	param.mid=mid;
	var form=$("<form>");//定义一个form表单
	form.attr("style","display:none");
	form.attr("target","");
	form.attr("method","post");
	//form.attr("action","../teamDerivedByMid");
	$("body").append(form);//将表单放置在web中
	$.each(param,function(key,value){
		var input1=$("<input>");
		input1.attr("type","hidden");
		input1.attr("name",key);
		input1.attr("value",value);
		form.append(input1);
	})
	form.ajaxSubmit({
			url : "../teamMemberScoreByMid",
			dataType : 'json',
			async : false,
			success : function(data) {
				if(data.status==0){
					console.log(data)
					//location.href="../downFileByPath?path="+data.path
					//window.location=data.fileurl;
					setTimeout(function() {
						window.location=data.fileurl; 
					}, 2000)
				}else if(data.status == 1){
	        		alertMsg("2",data.errmsg,"fail")
	        	}
			},
			error:function(){
				alertMsg("2","导出失败！","fail")
			}
	    })
	//form.submit();//表单提交 
	form.remove();
}

function exportPersonaward(){
	var mname=$(".currentmatch").text();
	var mid=sessionStorage.getItem("currentmid");
	var param={};
	param.mname=mname;
	param.mid=mid;
	var form=$("<form>");//定义一个form表单
	form.attr("style","display:none");
	form.attr("target","");
	form.attr("method","post");
	//form.attr("action","../teamDerivedByMid");
	$("body").append(form);//将表单放置在web中
	$.each(param,function(key,value){
		var input1=$("<input>");
		input1.attr("type","hidden");
		input1.attr("name",key);
		input1.attr("value",value);
		form.append(input1);
	})
	form.ajaxSubmit({
			url : "../teamScoreByMid",
			dataType : 'json',
			async : false,
			success : function(data) {
				if(data.status==0){
					console.log(data)
					//location.href="../downFileByPath?path="+data.path
					//window.location=data.fileurl;
					setTimeout(function() {
						window.location=data.fileurl; 
					}, 2000)
				}else if(data.status == 1){
	        		alertMsg("2",data.errmsg,"fail")
	        	}
			},
			error:function(){
				alertMsg("2","导出失败！","fail")
			}
	    })
	//form.submit();//表单提交 
	form.remove();
}
function clearSearch(){
	$(".searchul input").each(function(){
		$(this).val("");
		if($(this).attr("altvalue")!=null){
			$(this).attr("altvalue","")
		}
	})
}