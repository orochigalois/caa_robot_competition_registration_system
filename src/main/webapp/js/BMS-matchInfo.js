$(function(){
	if(sessionStorage.getItem("roleflg")=="02"){
		$(".sidebar-ul").find("li:not(:eq(6))").hide();
	}
	if(sessionStorage.getItem("roleflg")!="00"){
		$(".create").hide();
	}
	getMatchList();
})
var raceobj="";
function getMatchList(){
	var pageSize = 10;
	var uid=sessionStorage.getItem("uid");
	var roleflg=sessionStorage.getItem("roleflg")
	$("#page").pagination({
		pageSize : pageSize,
		showFirstLastBtn : true,
		showPrevious: true,
	    showNext: true,
	    autoHidePrevious: true,
	    autoHideNext: true,
		remote : {
			"url" : "../findMatchListByPage",
			pageParams : function(data) {
				return {
					iDisplayStart : data.pageIndex * data.pageSize,
					iDisplayLength : data.pageSize,
					"uid":uid,
					"roleflg":roleflg
				};
			}, // [Function]:自定义请求参数
			"success" : function(data, status) {
				console.log(data)
				if (data.status == 0) {
					$("tbody").empty();
					var htmls="";
					$.each(data.list,function(i,match){
						htmls+='<tr><td mid="'+match.mid+'"><a class="matchname" onclick="viewEvent(this)">'+match.mname+'</a></td>'
							  	+'<td>'+match.year+'</td>'
							  	+'<td>'+match.organizer+'</td>'
							  	+'<td>'+match.manager+'</td>'
							  	+'<td>'+match.address+'</td>'
							  	+'<td style="text-align:center">'+match.startdate+"至"+'<br/>'+match.enddate+'</td>'
							  	+'<td style="text-align:center">'+match.signstartdate+"至"+'<br/>'+match.signenddate+'</td>'
							  //	+'<td>'+match.delflg+'</td>';
						if(match.delflg=="01"){
							htmls+='<td>已删除</td>';
							if(match.mstatus=="01"){
								htmls+='<td mid="'+match.mid+'"><img src="../images/zhunbei.png" class="changestatus"></td>'
							}else if(match.mstatus=="00"){
								htmls+='<td mid="'+match.mid+'"><img src="../images/kaiqi.png" class="changestatus"></td>'
							}else if(match.mstatus=="02"){
								htmls+='<td mid="'+match.mid+'"><img src="../images/yiguanbi.png" class="changestatus" mstatus="02"></td>'
							}
							if(match.islog=="1"){
								htmls+='<td>是</td>'
							}else if(match.islog=="0"){
								htmls+='<td>否</td>'
							}else{
								htmls+='<td></td>'
							}
							htmls+='<td><a class="modify" style="color:#999">修改</a>'+
								 '<span class="split">|</span><a class="delete" style="color:#999">删除</a>'
								 +'<span class="split">|</span><a class="delete"'
								 +' onclick="setDefaultMatch(\''+match.mid+'\')">设置为当前系统默认赛事</a>';
							if(roleflg=="00"){
								htmls+='<span class="split">|</span><a class="delete"'
									 +' onclick="showadminList(this)">分配管理员</a>';
							}
							htmls+='</td></tr>'
						}else if(match.delflg=="00"){
							htmls+='<td>未删除</td>';
							if(match.mstatus=="01"){
								htmls+='<td mid="'+match.mid+'"><img src="../images/zhunbei.png" class="changestatus"'
									+' mstatus="01" onclick="alertConfirm(\'2\',\'确定开启该赛事吗？\',\'changestatus()\',this)"></td>'
							}else if(match.mstatus=="00"){
								htmls+='<td mid="'+match.mid+'"><img src="../images/kaiqi.png" class="changestatus"'
									+' mstatus="00" onclick="alertConfirm(\'2\',\'确定关闭该赛事吗？\',\'changestatus()\',this)"></td>'
							}else if(match.mstatus=="02"){
								htmls+='<td mid="'+match.mid+'"><img src="../images/yiguanbi.png" class="changestatus" mstatus="02"></td>'
							}
							if(match.islog=="1"){
								htmls+='<td>是</td>'
							}else if(match.islog=="0"){
								htmls+='<td>否</td>'
							}else{
								htmls+='<td></td>'
							}
							htmls+='<td mid="'+match.mid+'"><a class="modify" onclick="editEvent(this)">修改</a>'+
									'<span class="split">|</span><a class="delete" '
									+'onclick="alertConfirm(\'2\',\'确定删除该赛事吗？\',\'delEvent()\',this)">删除</a>'
									+'<span class="split">|</span><a class="delete" '
									+'onclick="setDefaultMatch(\''+match.mid+'\')">设置为当前系统默认赛事</a>'
							if(roleflg=="00"){
								htmls+='<span class="split">|</span><a class="delete"'
									 +' onclick="showadminList(this)">分配管理员</a>';
							}
							htmls+='</td></tr>'
						}
						
					})
					$("tbody").append(htmls)
				} else {
					alert(data.msg);
				}
			},
			"error" : function(data) {
				alert("后台获取数据出错");
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


function editEvent(obj){
	var mid=$(obj).parent().attr("mid");
	sessionStorage.setItem("mid",mid);
	window.open('BMS-editEvent.html',"_self");
}

function viewEvent(obj){
	var mid=$(obj).parent().attr("mid");
	sessionStorage.setItem("mid",mid);
	window.open('BMS-eventDetail.html',"_self");
}

function delEvent(){
	closeWin();
	var mid=$(tempobj).parent().attr("mid");
	$.ajax({
		type: "GET",
        url: "../updateDelflgMatchByMid",
        dataType: "JSON",
        async:false,
        data: {
        		"mid":mid
        	},
        success: function(data){
        	if(data.status == 0){
        		alertMsg("2","删除成功！","success")
        		/*$(obj).parent().parent().remove();*/
        		setTimeout('location.reload()',2000)
        	}else if(data.status == 1){
        		alertMsg("2",data.errmsg,"fail");
        	}
        },
	})
}

function changestatus(){
	var mid=$(tempobj).parent().attr("mid");
	var mstatus=$(tempobj).attr("mstatus");
	$.ajax({
		type: "GET",
        url: "../onOrOffMatchByMid",
        dataType: "JSON",
        async:false,
        data: {
        		"mid":mid
        	},
        success: function(data){
        	if(data.status == 0){
        		if(mstatus=="01"){
        			$(tempobj).attr("src","../images/kaiqi.png")
        			alertMsg("2","成功开启该赛事！","success")
        		}
        		if(mstatus=="00"){
        			$(tempobj).attr("src","../images/yiguanbi.png")
        			alertMsg("2","成功关闭该赛事！","success")
        		}
        		setTimeout('window.location.reload()',2000);
        	}else if(data.status == 1){
        		alertMsg("2",data.errmsg,"fail")
        	}
        },
	})
}

function showadminList(obj){
	raceobj=obj;
	var mid=$(obj).parent().attr("mid");
	var htmls="";
	htmls+='<div class="shade"></div><div class="adminwrap"><h1 class="wTitle">分配管理员</h1>'
    	 +'<img class="closeWin" src="../images/guanbi.png" onclick="closeadminwrap()">'
    	 +'<div class="infoneck"><div class="neckleft"><span style="float:left;">赛事名称：</span>'
    	 +'<span class="racename">'+$(obj).parent().parent().find("td:eq(0)").text()+'</span>'
    	 +'</div><span class="neckright" onclick="getRestadmin(\''+mid+'\')">分配管理员</span>'
    	 +'</div>';
	$.ajax({
		type: "GET",
        url: "../findMatchUserByMid",
        dataType: "JSON",
        async:false,
        data: {
        		"mid":mid,
        	},
        success: function(data){
        	if(data.status == 0){
        		console.log(data);
        		if(data.list.length==0){
        			htmls+='<h1 class="wTitle" style="margin-top:20px;font-weight:normal">该赛事暂未分配管理员！</h1>'
        		}else{
        			htmls+='<div class="tablewrap"><table><thead><tr>'
        				 +'<td style="width:70%">管理员姓名</td><td>操作</td>'
        				 +'</tr></thead><tbody>'
        			$.each(data.list,function(i,item){
        				htmls+='<tr><td>'+item.uname+'</td>'
    					     +'<td uid="'+item.uid+'" mid="'+mid+'">'
    					     +'<a class="delete" onclick="alertConfirm(\'2\',\'确定删除该管理员吗？\',\'delRaceadmin()\',this)">删除</a></td>'
    				         +'</tr>'
        			})
        			htmls+='</tbody></table></div>'
        		}
        	}else if(data.status == 1){
        		alertMsg("2",data.errmsg,"fail")
        	}
        },
	})
	htmls+='</div>'
	$("body").append(htmls)
}

function getRestadmin(mid){
	var htmls="";
	htmls+='<div class="cover2"></div><div class="adminList">'
    	 +'<h1 class="wTitle">分配管理员</h1>'
    	 +'<img class="closeWin" src="../images/guanbi.png" onclick="closeadminList()">'
	$.ajax({
		type: "GET",
        url: "../findNotMatchUserByMid",
        dataType: "JSON",
        async:false,
        data: {
        		"mid":mid,
        	},
        success: function(data){
        	if(data.status == 0){
        		console.log(data);
        		if(data.list.length==0){
        			htmls+='<h1 class="wTitle" style="margin-top:20px;font-weight:normal">暂无可分配的管理员,请添加！</h1>'
        			htmls+='<div class="btnwrap">'
               			 +'<span style="color:#333;border:1px solid #ccc;" onclick="closeadminList()">知道了</span></div></div>';
        		}else{
        			htmls+='<div class="tablewrap">'
        	    	 +'<table><thead><tr><td style="width:20%"></td>'
        	    	 +'<td style="text-align:center;padding-right:0">管理员姓名</td>'
        	    	 +'</tr></thead><tbody>';
        			$.each(data.list,function(i,item){
            			htmls+='<tr><td><input type="checkbox" uid="'+item.uid+'" class="selTc"></td>'
        					 +'<td style="text-align:center">'+item.uname+'</td></tr>';
            		})
            		htmls+='</tbody></table></div><div class="btnwrap">'
            			 +'<span style="background:#e21230;margin-right:20px" onclick="addadminForRace(\''+mid+'\')">确定</span>'
            			 +'<span style="color:#333;border:1px solid #ccc;" onclick="closeadminList()">取消</span></div></div>';
        		}
        		
        	}else if(data.status == 1){
        		alertMsg("2",data.errmsg,"fail")
        	}
        },
	})
	
	$("body").append(htmls)
}
function addadminForRace(mid){
	if($(".selTc:checked").length==0){
		alertMsg("2","请选择管理员！","fail");
		return;
	}
	var uid=sessionStorage.getItem("uid");
	var uidList=[];
	$(".selTc:checked").each(function(){
		var info={};
		info.uid=$(this).attr("uid");
		uidList.push(info);
	})
	$.ajax({
		type: "GET",
        url: "../addUserByMid",
        dataType: "JSON",
        async:false,
        data: {
        		"uidList":JSON.stringify(uidList),
        		"uid":uid,
        		"mid":mid
        	},
        success: function(data){
        	if(data.status == 0){
        		closeadminList();
        		closeadminwrap();
        		alertMsg("2","操作成功","success");
        		showadminList(raceobj)
        	}else if(data.status == 1){
        		alertMsg("2",data.errmsg,"fail")
        	}
        },
	})
}

function closeadminList(){
	$(".cover2").remove();
	$(".adminList").remove();
}
function closeadminwrap(){
	$(".shade").remove();
	$(".adminwrap").remove();
}

function delRaceadmin(){
	closeWin();
	var uid=$(tempobj).parent().attr("uid");
	var rid=$(tempobj).parent().attr("rid");
	var mid=sessionStorage.getItem("currentmid");
	$.ajax({
		type: "GET",
        url: "../deleteMatchUserByUid",
        dataType: "JSON",
        async:false,
        data: {
        		"uid":uid,
        		"mid":mid
        	},
        success: function(data){
        	if(data.status == 0){
        		closeadminwrap();
        		alertMsg("2","删除成功！","success")
        		showadminList(raceobj);
        	}else if(data.status == 1){
        		alertMsg("2",data.errmsg,"fail")
        	}
        },
	})
}