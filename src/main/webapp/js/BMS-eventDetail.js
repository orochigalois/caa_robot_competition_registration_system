$(function(){
	getEventInfo();
})
function filldata(){
	showmap();
	var map = new BMap.Map("allmap");
	var gps=$("#gps").html()
	var longitude=parseFloat(gps.slice(1,gps.indexOf(",")));
	var latitude=parseFloat(gps.slice(gps.indexOf(",")+1,gps.indexOf(")")));
	var gpsdescription=gps.slice(gps.indexOf(")")+1);
	var point = new BMap.Point(longitude,latitude);
	var marker = new BMap.Marker(point); // 创建点	
	var circle = new BMap.Circle(point,20000,{strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5}); //创建圆	
	map.addOverlay(marker);            //增加点
	map.addOverlay(circle); 
	map.centerAndZoom(point,15);
	map.enableScrollWheelZoom(true);
	$("#longitude").val(longitude);
	$("#latitude").val(latitude);
	$("#gpsdescription").val(gpsdescription)
}
var mid=sessionStorage.getItem("mid");
function getEventInfo(){
	$.ajax({
		type: "GET",
        url: "../findMatchInfoByMid",
        dataType: "JSON",
        async:false,
        data: {
        		"mid":mid
        	},
        success: function(data){
        	if(data.status == 0){
        		console.log(data)
        		var info=data.info;
        		$("#mname").html(info.mname);
        		$("#year").html(info.year);
        		$("#manager").html(info.manager);
        		$("#organizer").html(info.organizer);
        		$("#organizertel").html(info.organizertel);
        		$("#address").html(info.address);
        		$("#startdate").html(info.startdate);
        		$("#enddate").html(info.enddate);
        		$("#maxcnt").html(info.maxcnt);
        		$("#signstartdate").html(info.signstartdate);
        		$("#signenddate").html(info.signenddate);
        		$("#mincnt").html(info.mincnt);
        		$("#unitprice").html(info.unitprice/100+"元");
        		if(info.islog=="1"){
        			$("#islog").html("是")
        			$("#firstli").removeAttr("style");
        			$("#sndli").removeAttr("style");
        			$("#thridli").removeAttr("style"); 			
        		}else if(info.islog=="0"){
        			$("#islog").html("否")
        			$("#firstli").attr("style","display:none");
        			$("#sndli").attr("style","display:none");
        			$("#thridli").attr("style","display:none");
        		}
        		$("#firstsublogend").html(info.firstsublogend);
        		$("#sndsublogend").html(info.sndsublogend);
        		$("#thirdsublogend").html(info.thirdsublogend);
        		$("#introduce").html(info.introduce);
        		$("#description").html(info.description);
        		$("#gps").html(info.gps);
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
        		alert("查询失败！");
        	}
        },
	})
}
