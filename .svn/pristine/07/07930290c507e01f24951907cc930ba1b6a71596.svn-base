$(function(){
	//getEnddate();
	getTeamDetail();
	getAllRace();
	if(compareTime()){
		$(".editteam").hide();
	}
})
var tid=sessionStorage.getItem("tid");
var enddate=sessionStorage.getItem("enddate");
function getTeamDetail(){
	var mid=sessionStorage.getItem("mid");
	$.ajax({
		type: "GET",
        url: "../findAllMembersByTid",
        dataType: "JSON",
        async:false,
        data: {
        		"tid":tid,
        		"mid":mid
        	},
        success: function(data){
        	if(data.status == 0){
        		$("#tname").html(data.list[0].tname);
        		$("#school").html(data.list[0].tmschool);
        		$("#departname").html(data.list[0].tmdepartname);
        		$("#orgtype").html(data.list[0].orgtype);
        		$("#region").html(data.list[0].region);
        		var memList=data.list;
        		$("tbody").empty();
        		$.each(memList,function(i,mem){
        			var htmls=""; 
        			htmls+='<tr><td>';
        			if(mem.roleflg=="01"){
        				htmls+=$(".teaList tbody tr").length+1
        			}else{
        				htmls+=$(".stuList tbody tr").length+1
        			}
        			htmls+='</td><td><img class="headicon" src="'+mem.picurl+'">'

					    	+'<span>'+mem.tmname+'</span></td><td><img src="../images/'+(mem.sex=="01"?'male.png':'female.png')
					    	+'"></td>';
					$.each(folkname,function(i,nation){
						if(mem.folk==nation.folkid){
							htmls+='<td>'+nation.folk+'</td>'
						}
					})
				    htmls+='<td>'+mem.birthday+'</td>';
					if(mem.didtype=="00"){htmls+='<td>身份证</td>'}
					else if(mem.didtype=="01"){htmls+='<td>护照</td>'}
					else{htmls+='<td>港澳台通行证</td>'}
					htmls+='<td>'+mem.did+'</td>'
							+'<td>'+mem.email+'</td>'
							+'<td>'+mem.phone+'</td>'
							+'<td>'+mem.school+'</td>'
							+'<td>'+mem.departname+'</td>';
					if(mem.diningtype=="01"){htmls+='<td>普通</td>'}
					else if(mem.diningtype=="02"){htmls+='<td>清真</td>'}
					else{htmls+='<td>素食</td>'}
					htmls+='</tr>'	
					if(mem.roleflg=="01"){
						$(".teaList tbody").append(htmls)
					}
					else{$(".stuList tbody").append(htmls)}
        		})
        		
        	}else if(data.status == 1){
        		alertMsg("1",data.errmsg,"fail")
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
        			htmls+='<li>'+mem.rname+'</li>'
        		})
        		$(".raceul").append(htmls)
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