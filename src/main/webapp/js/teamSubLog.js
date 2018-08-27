$(function(){
//	getcurmatchname();
//	var jsonobj=JSON.parse(sessionStorage.getItem("userteamlogJson"));
//	var num="";
//	if(jsonobj!=undefined){
//		num=jsonobj.pageNo
//	}else{
//		num=0
//	}
	getAllRCJTeamInfo();
	compareTime();
})

var stsublogdate="";
var ndsublogdate="";
var rdsublogdate="";

var mid=sessionStorage.getItem("currentmid");
var tid=sessionStorage.getItem("tid");
var rid=sessionStorage.getItem("rid");
var mname="";
var rname="";

function getAllRCJTeamInfo(){
	$.ajax({
		type: "Post",
        url: "../findTeamsinfoToRCJ",
        dataType: "JSON",
        async:false,
        data: {
//        	"signuid":signuid,
        	"mid":mid,
        	"rid":rid,
        	"tid":tid
        	},
        success: function(data){
        	console.log(data)
        	if(data.status == 0){
        		var info=data.list[0];
        		$("#tname").html(info.tname);
        		$("#rname").html(info.rname);
        		$("#tcode").html(info.rtcode);
        		$("#school").html(info.school);
        		if(info.departname != null && info.departname != ""){
        			$("#departname").html(info.departname);
        		}else{
        			$("#departname").html("无");
        		}
        		
        		mname=info.mname;
        		rname=info.rname;
        			
        		if(info.infostatus=="00"){
        			$("#infostatus").html("已通过");
        		}else if(info.infostatus=="01"){
        			$("#infostatus").html("待审核");
        		}else{
        			$("#infostatus").html("已拒绝");
        		}
        		
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
        		
        		stsublogdate=info.stend;
    			ndsublogdate=info.ndend;
    			rdsublogdate=info.rdend;
    			
    			if(info.stlogurl!=""&&info.stlogurl!=undefined){
        			var htmls="";        			
        			htmls+='<div class="enclosureitem"><a class="filename" href="'+info.stlogurl+'">'
        				+info.stlogurl.slice(info.stlogurl.indexOf("_")+1)
						+'</a><form class="editform" enctype="multipart/form-data" method="post">'
						+'<a class="editfile" id="stsublog">更改<input type="file" class="hidfile" onchange="editModel(this)" name="files"></a>'
						+'<input hidden name="filenewname" value="">'
						+'<input hidden name="savetype" value="03">'
						+'<input hidden class="modelname"  id="stmode" value="'+info.stlogurl+'">'
						+'<input hidden id="mname" name="mname" value="'+info.mname+'">'
						+'<input hidden id="rname" name="rname" value="'+info.rname+'">'
						+'</form></div>';
        			$(".stsublog").html(htmls);
        		}else{
        			$("#mname_1").val(info.mname);
        			$("#rname_1").val(info.rname);
        		}
        			
    			if(info.ndlogurl!=""&&info.ndlogurl!=undefined){
        			var htmls="";
        			htmls+='<div class="enclosureitem"><a class="filename" href="'+info.ndlogurl+'">'
    				+info.ndlogurl.slice(info.ndlogurl.indexOf("_")+1)
					+'</a><form class="editform" enctype="multipart/form-data" method="post">'
					+'<a class="editfile" id="ndsublog">更改<input type="file" class="hidfile" onchange="editModel(this)" name="files"></a>'
					+'<input hidden name="filenewname" value="">'
					+'<input hidden name="savetype" value="03">'
					+'<input hidden class="modelname"  id="ndmode" value="'+info.ndlogurl+'">'
					+'<input hidden id="mname" name="mname" value="'+info.mname+'">'
					+'<input hidden id="rname" name="rname" value="'+info.rname+'">'
					+'</form></div>';
        			$(".ndsublog").html(htmls);
        		}else{
        			$("#mname_2").val(info.mname);
        			$("#rname_2").val(info.rname);
        		}
    			if(info.rdlogurl!=""&&info.rdlogurl!=undefined){
        			var htmls="";
        			htmls+='<div class="enclosureitem"><a class="filename" href="'+info.rdlogurl+'">'
    				+info.rdlogurl.slice(info.rdlogurl.indexOf("_")+1)
					+'</a><form class="editform" enctype="multipart/form-data" method="post">'
					+'<a class="editfile" id="rdsublog">更改<input type="file" class="hidfile" onchange="editModel(this)" name="files"></a>'
					+'<input hidden name="filenewname" value="">'
					+'<input hidden name="savetype" value="03">'
					+'<input hidden class="modelname"  id="rdmode" value="'+info.rdlogurl+'">'
					+'<input hidden id="mname" name="mname" value="'+info.mname+'">'
					+'<input hidden id="rname" name="rname" value="'+info.rname+'">'
					+'</form></div>';
        			$(".rdsublog").html(htmls);
        		}else{
        			$("#mname_3").val(info.mname);
        			$("#rname_3").val(info.rname);
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

function compareTime(){
	  var oDate1 = new Date();
      var oDate2 = new Date(stsublogdate);
      var oDate3 = new Date(ndsublogdate);
      var oDate4 = new Date(rdsublogdate);
      var flg = "1";
      
      if(oDate1.getTime() > oDate2.getTime()){
    	  $("#stsublog").attr("style","background:url(../images/jujue.png) no-repeat left center;");
    	  $("#stsublog").html("已过期");
    	  $('#stsublog').removeAttr("class");
    	  $('#stsublog').attr("class","addEnclosure");
//    	  flg = "1";
//    	  if(flg=="1"){
//    		  $("#stsublog").attr("style","background:url(../images/jujue.png) no-repeat left center; margin-left:10px;");
//    	  }
      }else{
    	  flg = "2";
      }
      
      if(oDate1.getTime() >= oDate3.getTime()){
    	  $("#ndsublog").attr("style","background:url(../images/jujue.png) no-repeat left center;");
    	  $("#ndsublog").html("已过期");
    	  $('#ndsublog').removeAttr("class");
    	  $('#ndsublog').attr("class","addEnclosure");
//    	  flg = "1";
//    	  if(flg=="1"){
//    		  $("#stsublog").attr("style","background:url(../images/jujue.png) no-repeat left center; margin-left:10px;");
//    	  }
      }else{
    	  if(flg=="2"){
        	  $("#ndsublog").attr("style","background:url(../images/jujue.png) no-repeat left center;");
        	  $("#ndsublog").html("未开放");
    	  }
    	  flg = "2";
      }
      
      if(oDate1.getTime() > oDate4.getTime()){
    	  $("#rdsublog").attr("style","background:url(../images/jujue.png) no-repeat left center;");
    	  $("#rdsublog").html("已过期");
    	  $('#rdsublog').removeAttr("class");
    	  $('#rdsublog').attr("class","addEnclosure");
//    	  flg = "1";
//    	  if(flg=="1"){
//    		  $("#stsublog").attr("style","background:url(../images/jujue.png) no-repeat left center; margin-left:10px;");
//    	  }
      }else{
    	  if(flg=="2"){
        	  $("#rdsublog").attr("style","background:url(../images/jujue.png) no-repeat left center;");
        	  $("#rdsublog").html("未开放");
    	  }
      }
      
}

function uploadModel(obj){
	var namestr=$(obj).val();	
	var filetype=$(obj).val().slice($(obj).val().lastIndexOf(".")+1).toUpperCase();
	var filename=namestr.split("\\")[namestr.split("\\").length-1];
	var filenewname="";
	
	//生成文件名
	var logtimes=$(obj).parent().attr("id");
	var editid="";
	var modelid="";
	if(logtimes=="stsublog"){
		filenewname="阶段一日志";
		editid="stsublog";
		modelid="stmode";
	}else if(logtimes=="ndsublog"){
		filenewname="阶段二日志";
		editid="ndsublog";
		modelid="ndmode";
	}else if(logtimes=="rdsublog"){
		filenewname="阶段三日志";
		editid="rdsublog";
		modelid="rdmode";
	}else{
		return;
	}
	//项目名称
	var rname = $("#rname").html();
	filenewname = filenewname + "_" + rname;
	
	//队伍编号
	var tcode = $("#tcode").html();
	filenewname = filenewname + "_" + tcode + ".pdf";
	
	$(obj).parent().next().val(filenewname);
	
	var htmls="";
	if(namestr!=""){
		if(filetype=="PDF"){
			$(obj).parent().parent().ajaxSubmit({
				url : "../uploadFiles_path",
				dataType : 'json',
				async : false,
				success : function(data) {
					if(data.status==0){
						htmls+='<div class="enclosureitem"><a class="filename">'+filename
	    						+'</a><form class="editform" enctype="multipart/form-data" method="post">'
	    						+'<a class="editfile" id="'+ editid
	    						+'">更改<input type="file" class="hidfile" onchange="editModel(this)" name="files"></a>'
	    						+'<input hidden name="filenewname" value="">'
	    						+'<input hidden name="savetype" value="03">'
	    						+'<input hidden class="modelname" id="'+ modelid +'" value="'+data.urls+'">'
	    						+'<input hidden id="mname" name="mname" value="'+mname+'">'
	    						+'<input hidden id="rname" name="rname" value="'+rname+'">'
	    						+'</form></div>';
						$(obj).parent().parent().parent().html(htmls);
						alertMsg("1","上传成功！","success")
					}else if(data.status==1){
						alertMsg("1",data.errmsg,"fail")
					}
				},
				error:function(){
					alertMsg("1","上传失败！","fail")
				}
		    })
		}else{
			alertMsg("1","日志文件应为pdf文件！","fail")
		}	
	}	
}

function editModel(obj){
	var namestr=$(obj).val();
	var filetype=$(obj).val().slice($(obj).val().lastIndexOf(".")+1).toUpperCase();
	var filename=namestr.split("\\")[namestr.split("\\").length-1];
	var filenewname="";
	
	//生成文件名
	var logtimes=$(obj).parent().attr("id");
	var editid="";
	var modelid="";
	if(logtimes=="stsublog"){
		filenewname="阶段一日志";
		editid="stsublog";
		modelid="stmode";
	}else if(logtimes=="ndsublog"){
		filenewname="阶段二日志";
		editid="ndsublog";
		modelid="ndmode";
	}else if(logtimes=="rdsublog"){
		filenewname="阶段三日志";
		editid="rdsublog";
		modelid="rdmode";
	}else{
		return;
	}
	//项目名称
	var rname = $("#rname").html();
	filenewname = filenewname + "_" + rname;
	
	//队伍编号
	var tcode = $("#tcode").html();
	filenewname = filenewname + "_" + tcode + ".pdf";
	
	$(obj).parent().next().val(filenewname);
	
	var htmls="";
	if(namestr!=""){
		if(filetype=="PDF"){
			$(obj).parent().parent().ajaxSubmit({
				url : "../uploadFiles_path",
				dataType : 'json',
				async : false,
				success : function(data) {
					if(data.status==0){
						htmls+='<a class="filename">'+filename
	    						+'</a><form class="editform" enctype="multipart/form-data" method="post">'
	    						+'<a class="editfile" id="'+editid
	    						+'">更改<input type="file" class="hidfile" onchange="editModel(this)" name="files"></a>'
	    						+'<input hidden name="filenewname" value="">'
	    						+'<input hidden name="savetype" value="03">'
	    						+'<input hidden class="modelname" id="'+ modelid +'" value="'+data.urls+'">'
	    						+'<input hidden id="mname" name="mname" value="'+mname+'">'
	    						+'<input hidden id="rname" name="rname" value="'+rname+'">'
	    						+'</form>';
						$(obj).parent().parent().parent().html(htmls);
						alertMsg("1","修改成功！","success")
					}else if(data.status==1){
						alertMsg("1",data.errmsg,"fail")
					}
				},
				error:function(){
					alertMsg("1","上传失败！","fail")
				}
		    })
		}else{
			alertMsg("1","日志文件应为pdf文件！","fail")
		}
	}	
}

function savelog(){
	
	var stlogurl=$('#stmode').val();
	var ndlogurl=$('#ndmode').val();
	var rdlogurl=$('#rdmode').val();
	
	if(stlogurl==undefined && ndlogurl==undefined && rdlogurl==undefined){
		alertMsg("1","请添加日志","fail")
		return;
	}
	
	$.ajax({
		type: "Post",
        url: "../savelog",
        dataType: "JSON",
        async:false,
        data: {
        	"mid":mid,
        	"rid":rid,
        	"tid":tid,
        	"stlogurl":stlogurl,
        	"ndlogurl":ndlogurl,
        	"rdlogurl":rdlogurl
        	},
    	success: function(data){
        	if(data.status == 0){
        		alertMsg("1","保存成功","success")
        		setTimeout('location.reload()',2000);
        	}else if(data.status == 1){
        		alertMsg("1",data.errmsg,"fail")
        	}
        },
	})
}
