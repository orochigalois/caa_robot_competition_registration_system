$(function(){
	getStartEvent();
})

function getStartEvent(){
	$.ajax({
		type: "GET",
        url: "../findstartMatch",
        dataType: "JSON",
        async:false,
        data: {
        		
        	},
        success: function(data){
        	if(data.status == 0){
        		var htmls="";
        		$.each(data.list,function(i,match){
        			htmls+='<li><div class="matchrel"><h1>'+match.mname+'</h1>'
	    				 +'<div class="holdTandP"><div>报名时间：<p>'+match.signstartdate.slice(0,10).split("-").join("/")
	    				 +'-'+match.signenddate.slice(0,10).split("-").join("/")+'</p></div>'
	    				 +'<div>举办时间：<p>'+match.startdate.slice(0,10).split("-").join("/")
	    				 +'-'+match.enddate.slice(0,10).split("-").join("/")+'</p></div>'
	    				 +'<div>举办地点：<p>'+match.address+'</p></div>'
	    				 +'</div>';
        			startdate=match.signstartdate;
        			enddate=match.signenddate;
        			if(!compareTime()){
        				htmls+='<a class="enroll" mid="'+match.mid+'" onclick="skippage(this)">立即报名</a>'
        			}
	    			htmls+='</div>'
	    				 +'<div class="matchdetail">'+match.description
	    				 +'</div></li>'
        		})
        		$(".eventlist").append(htmls);
        	}else if(data.status == 1){
        		alertMsg("1",data.errmsg,"fail")
        	}
        },
	})
}
var enddate="";
var startdate="";
function compareTime(){
	  var oDate1 = new Date();
      var oDate2 = new Date(enddate);
      var oDate3 = new Date(startdate);
      if(oDate1.getTime() > oDate2.getTime()){
    	  return true;
      }else if(oDate1.getTime() < oDate3.getTime()){
    	  return true;
      }else {
    	  return false;
      } 
}

function skippage(obj){
	var uid = sessionStorage.getItem("uid");
	var j = 1;
	//用户信息不全 则无法报名
	$.ajax({
		type: "GET",
        url: "../findLoginUser",
        dataType: "JSON",
        async:false,
        data: {"uid":uid},
        success:function(data){
        	
			//提醒用户补全基本信息
			if(data.user.uname==""||data.user.email==""||data.user.school==""||data.user.phone==""||data.user.receiveaddress==""){
				alertMsg("1","请补全您的基本信息，否则将无法报名！","fail");
				j=0;
				return;
			}
			
        	if(data.user.ifinvoice=="01"){
				if(data.user.invoicename==""||data.user.taxpayernumber==""){
					alertMsg("1","请补全您的基本信息，否则将无法报名","fail");
					j=0;
					return;
				}
			}
        }
	})

	if(j==1){
		sessionStorage.setItem("mid",$(obj).attr("mid"));
		window.open("matchList.html" ,"_self")
	}
}