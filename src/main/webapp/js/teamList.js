$(function(){
	getcurmatchname();
	var jsonobj=JSON.parse(sessionStorage.getItem("userteamlistJson"));
	var num="";
	if(jsonobj!=undefined){
		num=jsonobj.pageNo
	}else{
		num=0
	}
	getAllTeam(num);
})
var enddate="";
function getAllTeam(num){
	var mid=sessionStorage.getItem("currentmid");
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
			"url" : "../findTeamsBySignuidPages",
			pageParams : function(data) {
				var param={};
				param.pageNo=data.pageIndex;
				sessionStorage.setItem("userteamlistJson", JSON.stringify(param));
				return {
					iDisplayStart : data.pageIndex * data.pageSize,
					iDisplayLength : data.pageSize,
					"mid":mid
				};
			}, // [Function]:自定义请求参数
			"success" : function(data, status) {
				console.log(data)
				if (data.status == 0) {
					$("tbody").empty();
					var htmls="";
					$.each(data.list,function(i,team){
						enddate=team.signenddate;
						sessionStorage.setItem("enddate",team.signenddate);
						htmls+='<tr><td tid="'+team.tid+'" rid="'+team.rid+'" style="padding-left:20px">'
								+'<a class="teamname" onclick="viewTeam(this)">'+team.tname+'</td>'
								+'<td rid="'+team.rid+'"><a class="matchname" onclick="viewMatch(this)">'+team.rname+'</a></td>'
    							+'<td>'+(team.rtcode==null?"":team.rtcode)+'</td>'
    							+'<td>'+team.school+'</td>'
    							+'<td>'+team.departname+'</td>'
    							+'<td>';
						if(team.infostatus=="00"){htmls+='<span class="teamstatus1">已通过</span>'}
						else if(team.infostatus=="01"){htmls+='<span class="teamstatus2">待审核</span>'}
						else{htmls+='<span class="teamstatus3">已拒绝</span>'}
						if(team.feedback==undefined){
							team.feedback="";
						}
						htmls+='<td>'+team.feedback+'</td>'
						if(compareTime()){
							htmls+='</td><td>'
							+'<a class="manageteam1">管理</a>'
							+'<span class="split">|</span>'
							+'<a class="delteam1">取消报名</a>'
							+'<span class="split">|</span>'
							+'<a class="delteam1">队伍删除</a>'
							+'</td></tr>'
						}
						else{
							htmls+='</td><td tid="'+team.tid+'" rid="'+team.rid+'">'
							+'<a class="manageteam2" onclick="editTeam(this)">管理</a>'
							/*+'<span class="split">|</span>'
							+'<a class="delteam1">取消报名</a>'
							+'<span class="split">|</span>'
							+'<a class="delteam1">队伍删除</a>'*/
							+'<span class="split">|</span>'
							+'<a class="delteam2" onclick="alertConfirm(\'1\',\'确定取消报名吗？\',\'cancelEnroll()\',this)">取消报名</a>'
							+'<span class="split">|</span>'
							+'<a class="delteam2" onclick="alertConfirm(\'1\',\'删除队伍将取消所有该队伍的报名！\',\'delTeam()\',this)">队伍删除</a>'
							+'</td></tr>'
						}
					})
					$("tbody").append(htmls)
				} else if(data.status==1){
					alertMsg("1",data.errmsg,"fail")
				}
			},
			"error" : function(data) {
				alertMsg("1","后台获取数据出错","fail")
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

function viewTeam(obj){
	var tid=$(obj).parent().attr("tid");
	var rid=$(obj).parent().attr("rid");
	sessionStorage.setItem("tid",tid);
	sessionStorage.setItem("rid",rid); //add by grace at 20180108
	window.open("teamDetail.html","_self");
}

function editTeam(obj){
	var tid=$(obj).parent().attr("tid");
	var rid=$(obj).parent().attr("rid");
	sessionStorage.setItem("tid",tid);
	sessionStorage.setItem("rid",rid); //add by grace at 20180108
	
	window.open("editTeam.html","_self");
}

function viewMatch(obj){
	var rid=$(obj).parent().attr("rid");
	sessionStorage.setItem("rid",rid);
	window.open("matchDetail.html","_self");
}

function cancelEnroll(){
	closeWin();
	var mid=sessionStorage.getItem("currentmid");
	var tid=$(tempobj).parent().attr("tid");
	var rid=$(tempobj).parent().attr("rid");
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
        		alertMsg("1","操作成功!","success");
        		setTimeout('location.reload()',2000)
        	}else if(data.status == 1){
        		alertMsg("1",data.errmsg,"fail")
        	}
        },
	})
}

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
        		alertMsg("1",data.errmsg,"fail")
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


function delTeam(obj){
	closeWin();
	var mid=sessionStorage.getItem("currentmid");
	var tid=$(tempobj).parent().attr("tid");
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
        		alertMsg("1","操作成功！","success")
        		setTimeout("location.reload()",2000)
        	}else if(data.status == 1){
        		alertMsg("1",data.errmsg,"fail")
        	}
        },
	})
}

function setcurmatch(){
	var mid=$("[name=match]:checked").val();
	sessionStorage.setItem("currentmid",mid);
	$(".shade,.mymatchlist").remove();
	getcurmatchname();
	getAllTeam();
}