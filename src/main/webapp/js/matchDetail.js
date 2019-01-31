$(function(){
	getCompt();
})
var rid=sessionStorage.getItem("rid");
function getCompt(){
	$.ajax({
		type: "GET",
        url: "../findRaceInfoByRid",
        dataType: "JSON",
        async:false,
        data: {
        	"rid":rid,
        	},
        success: function(data){
        	console.log(data)
        	if(data.status == 0){
        		var info=data.info;
        		$("#frname").html(info.frname);
        		$("#rname").html(info.rname);
        		$("#startdate").html(info.startdate);
        		$("#enddate").html(info.enddate);
        		$("#introduce").html(info.introduce);
        		$("#description").html(info.description);
        		$("#rules").html(info.rules);
        		if(info.islog=="1"){
        			$("#islog").html("是")
        			$("#islog").attr("altvalue","1");
        			$("#firstli").removeAttr("style");
        			$("#sndli").removeAttr("style");
        			$("#thridli").removeAttr("style"); 			
        		}else{
        			$("#islog").html("否")
        			$("#islog").attr("altvalue","0");
        			$("#firstli").attr("style","display:none");
        			$("#sndli").attr("style","display:none");
        			$("#thridli").attr("style","display:none");
        		}
        		$("#stend").html(info.stend);
        		$("#ndend").html(info.ndend);
				$("#rdend").html(info.rdend);
				$("#techmax").html(info.techmax);
				$("#stumax").html(info.stumax);
				$("#stuoldmin").html(info.stuoldmin);
				$("#stuoldmax").html(info.stuoldmax);
        		if(info.attachurl!=""){
        			var urlarr=info.attachurl.split(",");
        			var htmls="";
        			$.each(urlarr,function(i,fileurl){
        				htmls+='<a href="'+fileurl+'" class="matchEnclosure">'+fileurl.slice(fileurl.indexOf("_")+1)
        						+'</a>';
        			})
        			$("#attachfile").append(htmls);
        		}
        	}else if(data.status == 1){
        		alertMsg("1",data.errmsg,"fail")
        	}
        },
        error:function(data){
        	alertMsg("1","后台获取数据错误！","fail")
        }
	})
}