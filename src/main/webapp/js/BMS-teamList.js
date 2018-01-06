$(function(){
	bmsgetcurmatchname();
	getAllRace();
	var jsonobj=JSON.parse(sessionStorage.getItem("teamlistJson"));
	var num="";
	if(jsonobj!=undefined){
		num=jsonobj.pageNo;
		$("#tname").val(jsonobj.tname);
		$("#infostatus").attr("altvalue",jsonobj.infostatus);
		if(jsonobj.infostatus!=""){
			$("#infostatus").val($("#infostatus").next().find("[altvalue="+jsonobj.infostatus+"]").text());
		}
		$("#school").val(jsonobj.school);
		$("#feestatus").attr("altvalue",jsonobj.feestatus);
		if(jsonobj.feestatus!=""){
			$("#feestatus").val($("#feestatus").next().find("[altvalue="+jsonobj.feestatus+"]").text());
		}	
		$("#rname").attr("altvalue",jsonobj.rid);
		$("#orgtype").val(jsonobj.orgtype);
		$("#region").val(jsonobj.region);
	}else{
		num=0
	}
	getAllTeam(num);
	var htmls="";
	$.each(regions,function(i,value){
		htmls+='<li>'+value+'</li>'
	})
	$("#region").next().append(htmls);
	htmls="";
	$.each(orgtypes,function(i,value){
		htmls+='<li>'+value+'</li>'
	})
	$("#orgtype").next().append(htmls);
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

function getAllTeam(num){
	var pageSize=10;
	var mid=sessionStorage.getItem("currentmid");
	var tname=$("#tname").val();
	var infostatus=$("#infostatus").attr("altvalue");
	var school=$("#school").val();
	var feestatus=$("#feestatus").attr("altvalue");
	var rid=$("#rname").attr("altvalue");
	var orgtype=$("#orgtype").val();
	var region=$("#region").val();
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
			"url" : "../findAllTeamsBySignuidPages",
			pageParams : function(data) {
				var param={};
				param.pageNo=data.pageIndex;
				param.tname=tname;
				param.infostatus=infostatus;
				param.school=school;
				param.feestatus=feestatus;
				param.orgtype=orgtype;
				param.region=region;
				param.rid=rid;
				sessionStorage.setItem("teamlistJson", JSON.stringify(param));
				return {
					iDisplayStart : data.pageIndex * data.pageSize,
					iDisplayLength : data.pageSize,
					"name":tname,
					"infostatus":infostatus,
					"school":school,
					"feestatus":feestatus,
					"orgtype":orgtype,
					"region":region,
					"rid":rid,
					"mid":mid
				};
			}, // [Function]:自定义请求参数
			"success" : function(data, status) {
				console.log(data)
				if (data.status == 0) {
					$("tbody").empty();
					var htmls="";
					$(".current").html(data.list[0].mname);
					$.each(data.list,function(i,team){
						htmls+='<tr><td>'+(team.rtcode==null?"":team.rtcode)+'</td>'
								+'<td tid="'+team.tid+'"><a class="teamname" onclick="viewTeam(this)">'+team.tname+'</td>'
								+'<td>'+team.rname+'</td>'
								+'<td>'+team.school+'</td>';
						/*if(team.paymenturl==""){
							htmls+='<td>未上传</td>'
						}else{
							htmls+='<td tid="'+team.tid+'" paymenturl="'+team.paymenturl
								+'" rid="'+team.rid+'">'
								+'<a class="view" onclick="viewFile(this)">查看文件</a></td>'
						}*/
    					htmls+='<td>';
						if(team.infostatus=="00"){htmls+='<span class="teamstatus1">已通过</span></td>'}
						else if(team.infostatus=="01"){htmls+='<span class="teamstatus2">待审核</span></td>'}
						else{htmls+='<span class="teamstatus3">已拒绝</span></td>'}
						/*if(team.infostatus!="01"){
							htmls+='<td><a class="passBtn2">通过</a><a class="refuseBtn2">拒绝</a></td>'
						}else{
							htmls+='<td tid="'+team.tid+'" rid="'+team.rid+'"><a class="passBtn1" '
							+'flg="00" onclick="alertConfirm(\'2\',\'确定审批通过该队伍？\','
							+'\'approveTeam()\',this)">通过</a>'
							+'<a class="refuseBtn1" flg="02" onclick="alertConfirm(\'2\',\'确定审批拒绝该队伍？\','
							+'\'approveTeam()\',this)">拒绝</a></td>'
						}*/
						if(team.feestatus=="00"){
							htmls+='<td><span class="paystatus1">已缴费</span></td>'
						}else if(team.feestatus=="01"){
							htmls+='<td><span class="paystatus2">未缴费</span></td>'
						}else if(team.feestatus=="02"){
							htmls+='<td><span class="paystatus3">部分缴费</span></td>'
						}
						if(team.ckstatus=="00"){
							htmls+='<td><span class="paystatus1">已签到</span></td>'
						}else if(team.ckstatus=="01"){
							htmls+='<td><span class="paystatus2">未签到</span></td>'
						}
						htmls+='<td tid="'+team.tid+'" rid="'+team.rid+'">'
    							+'<a class="modify" onclick="editTeam(this)">修改</a>'
    							+'<span class="split">|</span>'
    							+'<a class="delete" '
    							+'onclick="alertConfirm(\'2\',\'确定取消该队伍报名吗？\',\'cancelEnroll()\',this)">取消报名</a>'
    							+'<span class="split">|</span>'
    							+'<a class="delete" '
    							+'onclick="alertConfirm(\'2\',\'删除队伍将取消所有该队伍的报名！\',\'delTeam()\',this)">队伍删除</a>'
    							+'</td></tr>'
					})
					$("tbody").append(htmls)
				} else if(data.status==1){
					alertMsg("2",data.errmsg,"fail")
				}
			},
			"error" : function(data) {
				alertMsg("2","后台获取数据出错","fail")
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

function approveTeam(){
	closeWin();
	var flg=$(tempobj).attr("flg");
	var tid=$(tempobj).parent().attr("tid");
	var rid=$(tempobj).parent().attr("rid");
	var htmls="";
	htmls+='<div class="cover2"></div><div class="approvebox"><h1 class="approvetitle">'
	if(flg=="00"){htmls+='【通过】'}
	else{htmls+='【拒绝】'}
	htmls+='该条队伍信息并通知</h1><textarea id="feedback">'
	if(flg=="00"){htmls+='您提交的队伍信息已通过审核！'}
	else{htmls+='对不起,您提交的队伍信息没有通过审核！'}
	htmls+='</textarea>'
		+'<div class="btnwrap"><a class="approbtn1" '
		+'onclick="submitResult(\''+flg+'\',\''+tid+'\',\''+rid+'\')">确定</a>'
		+'<a class="approbtn2" onclick="closeAppro()">取消</a></div></div>';
	$("body").append(htmls)
}

function submitResult(flg,tid,rid){
	closeAppro();
	var feedback=$("#feedback").val()
	$.ajax({
		type: "GET",
        url: "../approveTeam",
        dataType: "JSON",
        async:false,
        data: {
        	"tid":tid,
        	"rid":rid,
        	"feedback":feedback,
        	"infostatus":flg,
        	},
        success: function(data){
        	if(data.status == 0){
        		alertMsg("2","审批成功！","success")
        		setTimeout("location.reload()",2000)
        	}else if(data.status == 1){
        		alertMsg("2",data.errmsg,"fail")
        	}
        },
	})
}

function closeAppro(){
	$(".cover2").remove();
	$(".approvebox").remove();
}

function editTeam(obj){
	var tid=$(obj).parent().attr("tid");
	var rid=$(obj).parent().attr("rid");
	sessionStorage.setItem("tid",tid);
	sessionStorage.setItem("rid",rid);
	
	window.open("BMS-editTeam.html","_self");
}

function viewTeam(obj){
	var tid=$(obj).parent().attr("tid");
	sessionStorage.setItem("tid",tid);
	window.open("BMS-teamDetail.html","_self");
}

function cancelEnroll(obj){
	closeWin();
	var tid=$(tempobj).parent().attr("tid");
	var rid=$(tempobj).parent().attr("rid");
	var mid=sessionStorage.getItem("currentmid");
	$.ajax({
		type: "GET",
        url: "../delRaceTeamByTid",
        dataType: "JSON",
        async:false,
        data: {
        	"tid":tid,
        	"rid":rid,
        	"mid":mid
        	},
        success: function(data){
        	if(data.status == 0){
        		alertMsg("2","操作成功！","success")
        		setTimeout("location.reload()",2000)
        	}else if(data.status == 1){
        		alertMsg("2",data.errmsg,"fail")
        	}
        },
	})
}

function delTeam(obj){
	closeWin();
	var tid=$(tempobj).parent().attr("tid");
	var mid=sessionStorage.getItem("currentmid");
	$.ajax({
		type: "GET",
        url: "../delTeamByTid",
        dataType: "JSON",
        async:false,
        data: {
        	"tid":tid,
        	"mid":mid
        	},
        success: function(data){
        	if(data.status == 0){
        		alertMsg("2","操作成功！","success")
        		setTimeout("location.reload()",2000)
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
var enddate="";
function getEnddate(){
	$.ajax({
		type: "GET",
        url: "../findstartMatch",
        dataType: "JSON",
        async:false,
        data: {
        	
        },
        success: function(data){
        	if(data.status == 0){
        		enddate=data.info.signenddate
        	}else if(data.status == 1){
        		alertMsg("2",data.errmsg,"fail")
        	}
        },
	})
}

function compareTime(){
	  var oDate1 = new Date();
      var oDate2 = new Date(enddate);
      if(oDate1.getTime() > oDate2.getTime()){
    	  return true;
      }else {
    	  return false;
      } 
}

function viewFile(obj){
	var paymenturl=$(obj).parent().attr("paymenturl");
	var tid=$(obj).parent().attr("tid");
	var rid=$(obj).parent().attr("rid");
	var htmls="";
	htmls+='<div id="shade"></div><div class="photowall">'
			+'<img class="closeWin" src="../images/guanbi.png" onclick="closeInfoWin()">'
			+'<ul class="photoul clearfix">';
	$(paymenturl.split(",")).each(function(i,value){
		htmls+='<li><img src="'+value+'"></li>';
	})
	htmls+='</ul><div><span>更改缴费状态：</span><input class="searchinput selinput" type="text"'
			+' placeholder="请选择" readonly altvalue="">'
			+'<ul class="emulate"><li altvalue="02" onclick="updateFeestatus(\'02\',\''
			+tid+'\',\''+rid+'\''
			+')">部分缴费</li><li altvalue="00" onclick="updateFeestatus(\'00\',\''
			+tid+'\',\''+rid+'\')">已缴费</li>'
			+'</ul></div></div>';
	$("body").append(htmls);
	$(".photowall .selinput").click(function(e){
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
    $(".photoul li img").click(function(){
		$("#popup img").attr("src",$(this)[0].src);
		$("#popup").css("display","block");
	});
	$("#popup").click(function(){
		$("#popup").css("display","none");
	});
}

function updateFeestatus(feestatus,tid,rid){
	$.ajax({
		type: "GET",
        url: "../updateFeestatusByTeamRace",
        dataType: "JSON",
        async:false,
        data: {
        	"tid":tid,
        	"rid":rid,
        	"feestatus":feestatus
        },
        success: function(data){
        	if(data.status == 0){
        		alertMsg("2","操作成功！","success")
        		setTimeout("location.reload()",2000)
        	}else if(data.status == 1){
        		alertMsg("2",data.errmsg,"fail")
        	}
        },
	})
}

function closeInfoWin(){
	$("#shade").remove();
	$(".photowall").remove();
}

function getAllRace(){
	var mid=sessionStorage.getItem("currentmid");
	$.ajax({
		type: "GET",
        url: "../findAllRaceRnameBymid",
        dataType: "JSON",
        async:false,
        data: {
        	"mid":mid
        },
        success: function(data){
        	if(data.status == 0){
        		var htmls="";
        		$.each(data.list,function(i,race){
        			htmls+='<li altvalue="'+race.rid+'">'+race.rname+'</li>'
        		})
        		$("#rname").next().append(htmls)
        	}else if(data.status == 1){
        		alertMsg("2",data.errmsg,"fail")
        	}
        },
	})
}