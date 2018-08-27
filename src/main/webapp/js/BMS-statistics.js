$(function(){
	bmsgetcurmatchname();
	$(".tabletitle").html($(".currentmatch").text()+"信息统计");
	//getStartEvent();
	getStatis();
})

function getStatis(){
	var mid=sessionStorage.getItem("currentmid");
	$.ajax({
		type: "GET",
        url: "../statisticsMemberByMid",
        dataType: "JSON",
        async:false,
        data: {
        	"mid":mid
        },
        success: function(data){
        	if(data.status == 0){
        		$("tbody").empty()
        		var htmls="";
        		$.each(data.list,function(i,value){
        			var item=value[0];
        			htmls+='<tr><td>'+item["组织机构"]+'</td><td>'
        				+item["teamnum"]+'</td><td>'
        				+item["参赛队员男"]+'</td><td>'+item["参赛队员女"]
        				+'</td><td>'+item["参赛队员合计"]+'</td><td>'
        				+item["指导教师男"]+'</td><td>'+item["指导教师女"]
        				+'</td><td>'+item["指导教师合计"]+'</td></tr>'
        		})
        		$("tbody").append(htmls)
        	}else if(data.status == 1){
        		alertMsg("2",data.errmsg,"fail")
        	}
        },
	})
}

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
        		var match=data.info;
        		$(".tabletitle").html(match.mname+"信息统计");
        	}else if(data.status == 1){
        		alertMsg("1",data.errmsg,"fail")
        	}
        },
	})
}