$(function(){
	getTeamDetail();
	getAllRace();
})
var tid=sessionStorage.getItem("tid");

function getTeamDetail(){
	$.ajax({
		type: "GET",
        url: "../findAllMembersByTid",
        dataType: "JSON",
        async:false,
        data: {
        		"tid":tid
        	},
        success: function(data){
        	if(data.status == 0){
        		$(".personul").empty();
        		$("#tname").html(data.list[0].tname);
        		//$("#tcode").html(data.list[0].tcode); remove by grace at 20180109
        		$("#tschool").html(data.list[0].tmschool);
        		$("#tdepartname").html(data.list[0].tmdepartname);
        		$("#orgtype").html(data.list[0].orgtype);
        		$("#region").html(data.list[0].region);
        		$("#invoicename").html(data.list[0].invoicename);
        		$("#taxpayernumber").html(data.list[0].taxpayernumber);
        		$("#banknumber").html(data.list[0].banknumber);
        		$("#addressphone").html(data.list[0].addressphone);
        		$("#receiveaddress").html(data.list[0].receiveaddress);
        		if(data.list[0].paymenturl==""){
        			$("#paymenturl").html("未上传")
        		}else{
        			var str='<a class="view" onclick="viewFile(this)"'
        				+' paymenturl="'+data.list[0].paymenturl+'">查看文件</a>';
        			$("#paymenturl").html(str);
        		}
        		var memList=data.list;
        		
        		$.each(memList,function(i,mem){
        			var htmls=""; 
        			htmls+='<ul class="personul"><li><span class="infoname">姓名：</span>'+mem.tmname+'</li>'
    						+'<li><span class="infoname">生日：</span>'+mem.birthday+'</li>'
    						+'<li><span class="infoname">性别：</span>';
        			if(mem.sex=="01"){htmls+='男'}
        			else{htmls+='女'}
        			htmls+='</li><li><span class="infoname">证件类型：</span>';
        			if(mem.didtype=="00"){htmls+='身份证'}
					else if(mem.didtype=="01"){htmls+='护照'}
					else{htmls+='港澳台通行证'}
        			htmls+='</li><li><span class="infoname">证件号：</span>'+mem.did+'</li>'
    						+'<li><span class="infoname">邮箱：</span>'+mem.email+'</li>'
    						+'<li><span class="infoname">手机：</span>'+mem.phone+'</li>'
    						+'<li><span class="infoname">学校：</span>'+mem.school+'</li>'
    						+'<li><span class="infoname">院系：</span>'+mem.departname+'</li>'
    						+'<li><span class="infoname">免责声明：</span>';
        			if(mem.disclaimerurl==""){
        				htmls+="未上传";
        			}else{
        				htmls+='<a class="view" href="'+mem.disclaimerurl+'">查看文件</a>';
        			}
        			htmls+='</li></ul>';
    				if(mem.roleflg=="01"){
    					$(".teaList").append(htmls)
    				}else{
    					$(".stuList").append(htmls)
    				}
        		})
        		
        	}else if(data.status == 1){
        		alertMsg("2",data.errmsg,"fail")
        	}
        },
	})
}

function getAllRace(){
	$.ajax({
		type: "GET",
        url: "../findAllTeamsRace",
        dataType: "JSON",
        async:false,
        data: {
        		"tid":tid
        	},
        success: function(data){
        	if(data.status == 0){
        		$(".raceul").empty();
        		var memList=data.list;
        		var htmls="";
        		$.each(memList,function(i,mem){
        			var tcode = $("#tcode").html(); //add by grace 20180109
        			if(tcode.length){
        				$("#tcode").html(tcode+","+mem.tcode);
        			}else{
        				$("#tcode").html(mem.tcode);
        			}
        			htmls+='<li rid="'+mem.rid+'"><span class="racename">'+mem.rname+'</span>';
        			if(mem.infostatus=="01"){
        				/*htmls+='<a class="apprbtn" flg="00" onclick="alertConfirm(\'2\',\'确定审批通过该队伍？\','
						+'\'approveTeam()\',this)">通过</a>'
						+'<a class="apprbtn" flg="02" onclick="alertConfirm(\'2\',\'确定审批拒绝该队伍？\','
						+'\'approveTeam()\',this)">不通过</a>'	 */
        				htmls+='<a class="apprbtn" flg="00" onclick="getMemPrice(this)">通过</a>'
    						+'<a class="apprbtn" flg="02" onclick="alertConfirm(\'2\',\'确定审批拒绝该队伍？\','
    						+'\'approveTeam()\',this)">不通过</a>'
        			}else if(mem.infostatus=="00"){
        				htmls+='<span class="infoname">审核状态：</span><span class="teamstatus1">已通过</span>'
        			}else if(mem.infostatus=="02"){
        				htmls+='<span class="infoname">审核状态：</span><span class="teamstatus3">已拒绝</span>'
        			}
        			+'</li>'
        		})
        		$(".raceul").append(htmls)
        	}else if(data.status == 1){
        		alertMsg("1",data.errmsg,"fail")
        	}
        },
	})
}

function approveTeam(){
	closeWin();
	var flg=$(tempobj).attr("flg");
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
	var feedback=$("#feedback").val();
	var mid=sessionStorage.getItem("currentmid");
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
        	"mid":mid
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

function viewFile(obj){
	var paymenturl=$(obj).attr("paymenturl");
	var htmls="";
	htmls+='<div id="shade"></div><div class="photowall">'
			+'<img class="closeWin" src="../images/guanbi.png" onclick="closeInfoWin()">'
			+'<ul class="photoul clearfix">';
	$(paymenturl.split(",")).each(function(i,value){
		htmls+='<li><img src="'+value+'"></li>';
	})
	htmls+='</ul></div>';
	$("body").append(htmls);
    $(".photoul li img").click(function(){
		$("#popup img").attr("src",$(this)[0].src);
		$("#popup").css("display","block");
	});
	$("#popup").click(function(){
		$("#popup").css("display","none");
	});
}

function closeInfoWin(){
	$("#shade").remove();
	$(".photowall").remove();
	$(".memlist").remove();
}

function getMemPrice(obj){
	var rid=$(obj).parent().attr("rid");
	var htmls="";
	htmls+='<div id="shade"></div><div class="memlist"><h1 class="wTitle">成员单价</h1>'
  	  	 +'<div class="tablewrap"><table><thead><tr><td style="width:120px">姓名</td>'
  	  	 +'<td style="width:120px">曾参赛？</td><td>单价(人/项)</td>'
  	  	 +'</tr></thead><tbody>';
	$.ajax({
		type: "GET",
        url: "../findUnitpriceByTid",
        dataType: "JSON",
        async:false,
        data: {
        	"tid":tid,
        	"rid":rid,
        	},
        success: function(data){
        	if(data.status == 0){
        		console.log(data);
        		$.each(data.list,function(i,item){
        			htmls+='<tr tmid="'+item.tmid+'"><td>'+item.tmname+'</td>'
	    				 +'<td>'+(item.alreadyflg=="00"?'是':'否')+'</td>'
	    				 +'<td><input type="text" class="price" value="'+item.unitprice/100
	    				 +'"></td></tr>'
        		})
        	}else if(data.status == 1){
        		alertMsg("2",data.errmsg,"fail")
        	}
        },
	})
	htmls+='</tbody></table></div><div class="btnwrap">'
		 +'<span style="background:#e21230;margin-right:20px" onclick="updateMemPrice(\''+rid+'\')">确定</span>'
		 +'<span style="color:#333;border:1px solid #ccc;" onclick="closeInfoWin()">取消</span>'
		 +'</div></div>'
	$("body").append(htmls);
	/*
	    		
	    			
	    			
	    		*/
}

function updateMemPrice(rid){
	var memberList=[];
	var onswitch=true;
	$(".memlist .tablewrap tbody tr").each(function(){
		if($(this).find(".price").val().trim()==""){
			onswitch=false;
			return false;
		}else if(!checkMoneyFormat($(this).find(".price").val())){
			onswitch=false;
			return false;
		}else{
			var info={};
			info.tmid=$(this).attr("tmid");
			info.unitprice=parseInt($(this).find(".price").val()*100);
			memberList.push(info);
		}
	})
	if(!onswitch){
		alertMsg("2","请填写单价","fail")
		return;
	}
	$.ajax({
		type: "GET",
        url: "../updateUnitprice",
        dataType: "JSON",
        async:false,
        data: {
        	"tid":tid,
        	"memberList":JSON.stringify(memberList),
        	},
        success: function(data){
        	if(data.status == 0){
        		closeInfoWin();
        		var mid=sessionStorage.getItem("currentmid");
        		$.ajax({
        			type: "GET",
        	        url: "../approveTeam",
        	        dataType: "JSON",
        	        async:false,
        	        data: {
        	        	"tid":tid,
        	        	"rid":rid,
        	        	"feedback":"",
        	        	"infostatus":"00",
        	        	"mid":mid
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
        		
        	}else if(data.status == 1){
        		alertMsg("2",data.errmsg,"fail")
        	}
        },
	})
}

function checkMoneyFormat(val){
    var reg = /^(([1-9]\d*)|(([1-9][0-9]*\.[0-9]{1,2})|([0]\.[0-9]{1,2})|0))$/;
    var isMoneyFormatRight = reg.test(val);
    return isMoneyFormatRight;
}