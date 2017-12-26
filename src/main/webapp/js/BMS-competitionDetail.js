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
        		if(info.attachurl!=""){
        			var urlarr=info.attachurl.split(",");
        			var htmls="";
        			$.each(urlarr,function(i,fileurl){
        				htmls+='<a href="'+fileurl+'" class="filename">'+fileurl.slice(fileurl.indexOf("_")+1)
        						+'</a>';
        			})
        			$("#attachfile").append(htmls);
        		}
        	}else if(data.status == 1){
        		alertMsg("2",data.errmsg,"fail")
        	}
        },
        error:function(data){
        	
        }
	})
}