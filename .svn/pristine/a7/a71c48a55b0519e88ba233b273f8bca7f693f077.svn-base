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
	sessionStorage.setItem("mid",$(obj).attr("mid"));
	window.open("matchList.html" ,"_self")
}