$(function(){
	//getEnddate();
	getAllCompt();
})
var enddate="";
var startdate="";
var mid=sessionStorage.getItem("mid");
function getAllCompt(){
	var pageSize=10;
	$("#page").unbind().data("pagination", null).empty();	
	$("#page").pagination({
		pageSize : pageSize,
		showFirstLastBtn : true,
		showPrevious: true,
	    showNext: true,
	    autoHidePrevious: true,
	    autoHideNext: true,
		remote : {
			"url" : "../findRaceListByPage",
			pageParams : function(data) {
				return {
					iDisplayStart : data.pageIndex * data.pageSize,
					iDisplayLength : data.pageSize,
					mid:mid
				};
			}, // [Function]:自定义请求参数
			"success" : function(data, status) {
				console.log(data)
				if (data.status == 0) {
					$("tbody").empty();
					var htmls="";
					$.each(data.list,function(i,com){
						htmls+='<tr><td style="padding-left:40px">'+com.rcode+'</td><td rid="'+com.rid+'">'
								+'<a class="matchname" onclick="viewCompt(this)">'+com.rname+'</a></td>'
							  	+'<td>'+com.startdate+'</td>'
							  	+'<td>'+com.enddate+'</td>'
							  	+'<td rid="'+com.rid+'">';
						if(compareTime()){
							htmls+='<a class="operate1">报名</a></td></tr>'
						}
						else{
							htmls+='<a class="operate2" onclick="showEnroll(this)">报名</a></td></tr>'
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

function viewCompt(obj){
	var rid=$(obj).parent().attr("rid");
	sessionStorage.setItem("rid",rid);
	window.open('matchDetail.html',"_self");
}

function showEnroll(obj){
	var rid=$(obj).parent().attr("rid");
	sessionStorage.setItem("rid",rid);
	$("#cover,.enrollWin").show();
}
function closeEnroll(){
	$("#cover,.enrollWin").hide();
}

function closeSel(){
	$("#cover,.selWin").hide();
}
function showSel(){
	closeEnroll();
	$("#cover,.selWin").show();
	$.ajax({
		type: "GET",
        url: "../findTeamsBySignuid",
        dataType: "JSON",
        async:false,
        data: {
        	"mid":mid
        },
        success: function(data){
        	if(data.status == 0){
        		$(".optul").empty();
        		var htmls="";
        		$.each(data.list,function(i,team){
        			htmls+='<li><input type="radio" value="'+team.tid+'" name="team">'
        					+team.tname+'</li>'
        		})
        		$(".optul").append(htmls)
        	}else if(data.status == 1){
        		alertMsg("1",data.errmsg,"fail")
        	}
        },
	})
}

function chose2enroll(){
	closeSel()
	if($("input:checked").length==0){
		alertMsg("1","请选择队伍！","fail")
		return;
	}
	var tid=$("input:checked").val();
	var rid=sessionStorage.getItem("rid");
	$.ajax({
		type: "GET",
        url: "../addAvailableTeam",
        dataType: "JSON",
        async:false,
        data: {
        	"tid":tid,
        	"rid":rid,
        	"mid":mid,
        },
        success: function(data){
        	if(data.status == 0){
        		alertMsg("1","报名成功！","success")
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
        		startdate=data.info.signstartdate
        	}else if(data.status == 1){
        		alertMsg("1",data.errmsg,"fail")
        	}
        },
	})
}

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