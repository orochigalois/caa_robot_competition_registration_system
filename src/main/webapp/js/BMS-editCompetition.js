$(function(){
	$(document).click(function(){
		$(".emulate").hide();
	})
	
	$(".selinput").click(function(e){
    	e.stopPropagation();
    	var me=this;
    	$(me).next().show();
    	$(me).next().children().click(function(){
    		$(me).val($(this).text());
    		$("#islog").attr("altvalue",$(this).attr("altvalue"));
    		if($(this).attr("altvalue")=='1'){
    			$("#firstli").removeAttr("style");
    			$("#sndli").removeAttr("style");
    			$("#thridli").removeAttr("style");
    		}else if($(this).attr("altvalue")=='0'){
    			$("#firstli").attr("style","display:none");
    			$("#sndli").attr("style","display:none");
    			$("#thridli").attr("style","display:none");
    		}
    		$(me).next().hide();
    	})
    })
    
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
        		$("#frname").val(info.frname);
        		$("#rname").val(info.rname);
        		$("#startdate").val(info.startdate);
        		$("#enddate").val(info.enddate);
        		$("#introduce").val(info.introduce);
        		editor.insertHtml(info.description);
        		$("#description").val(info.description);
        		$("#rules").val(info.rules);
        		$("#islog").val($("#islog").next().find("[altvalue="+info.islog+"]").text());
        		if(info.islog=="1"){
        			$("#islog").attr("altvalue","1");
        			$("#firstli").removeAttr("style");
        			$("#sndli").removeAttr("style");
        			$("#thridli").removeAttr("style"); 			
        		}else{
        			$("#islog").attr("altvalue","0");
        			$("#firstli").attr("style","display:none");
        			$("#sndli").attr("style","display:none");
        			$("#thridli").attr("style","display:none");
        		}
        		$("#stend").val(info.stend);
        		$("#ndend").val(info.ndend);
				$("#rdend").val(info.rdend);
				$("#techmax").val(info.techmax);
				$("#stumax").val(info.stumax);
        		if(info.attachurl!=""){
        			var urlarr=info.attachurl.split(",");
        			var htmls="";
        			$.each(urlarr,function(i,fileurl){
        				htmls+='<div class="enclosureitem"><a class="filename" href="'+fileurl+'">'+fileurl.slice(fileurl.indexOf("_")+1)
								+'</a><form class="editform" enctype="multipart/form-data" method="post">'
								+'<a class="editfile">更改<input type="file" class="hidfile" onchange="editFile(this)" name="files"></a>'
								+'<input hidden name="savetype" value="01">'
								+'<input hidden name="filename" value="'+fileurl+'">'
								+'</form><a class="delfile" '
								+'onclick="alertConfirm(\'2\',\'确定删除该附件吗？\',\'delFile()\',this)">&times;</a></div>';
        			})
        			$("#myform1").before(htmls);
        		}
        	}else if(data.status == 1){
        		alertMsg("2",data.errmsg,"fail")
        	}
        },
        error:function(data){
        	
        }
	})
}

function updateCompetition(){
	var rid=sessionStorage.getItem("rid");
	var frname=$("#frname").val();
	var rname=$("#rname").val();	
	var startdate=$("#startdate").val();
	var enddate=$("#enddate").val();
	var introduce=$("#introduce").val();
	var description=$("#description").val();
	var islog=$("#islog").val();
	var islog_altvalue=$("#islog").attr("altvalue");
	var stend=$("#stend").val();
	var ndend=$("#ndend").val();
	var rdend=$("#rdend").val();
	var techmax=$("#techmax").val();
	var stumax=$("#stumax").val();
	
	var rules=$("#rules").val();
	var attachurl=[];
	if(frname.trim()==""){
		alertMsg("2","请填写大项名称！","fail")
		return;
	}else if(frname.indexOf(",")!=-1){
		alertMsg("2","大项名称不允许出现非法字符！","fail");
		return;
	}
	
	if(rname.trim()==""){
		alertMsg("2","请填写子项名称！","fail")
		return;
	}else if(rname.indexOf(",")!=-1){
		alertMsg("2","子项名称不允许出现非法字符！","fail");
		return;
	}
	
	if(startdate.trim()==""){
		alertMsg("2","请选择开始时间！","fail")
		return;
	}
	if(enddate.trim()==""){
		alertMsg("2","请选择结束时间！","fail")
		return;
	}
	if(islog.trim()==""){
		alertMsg("2","请选择提交日志！","fail")
		return;
	}else{
		if(islog_altvalue=='1'){
			if(stend.trim()==""){
				alertMsg("2","请选择阶段一结束日！","fail")
				return;
			}
			if(ndend.trim()==""){
				alertMsg("2","请选择阶段二结束日！","fail")
				return;
			}
			if(rdend.trim()==""){
				alertMsg("2","请选择阶段三结束日！","fail")
				return;
			}
		}else{
			stend = "";
			ndend = "";
			rdend = "";
		}
	}

	if(techmax.trim()==""){
		alertMsg("2","请填写教师人数上限！","fail")
		return;
	}else{
		var reg=/^[0-9]*$/;
		if(!reg.test(techmax.trim())){
			alertMsg("1","请输入数字教师人数上限！","fail");
			return;
		}
	}

	if(stumax.trim()==""){
		alertMsg("2","请填写队员人数上限！","fail")
		return;
	}else{
		var reg=/^[0-9]*$/;
		if(!reg.test(stumax.trim())){
			alertMsg("1","请输入数字队员人数上限！","fail");
			return;
		}
	}

	if(introduce.trim()==""){
		alertMsg("2","请填写赛项简介！","fail")
		return;
	}
	if(rules.trim()==""){
		alertMsg("2","请填写赛项规则！","fail")
		return;
	}
	if(description.trim()==""){
		alertMsg("2","请填写赛项描述！","fail")
		return;
	}
	$("[name=filename]").each(function(){
		attachurl.push($(this).val())
	})
	$.ajax({
		type: "GET",
        url: "../updateRace",
        dataType: "JSON",
        async:false,
        data: {
        	"rid":rid,
        	"frname":frname,
        	"rname":rname,
        	"startdate":startdate,
        	"enddate":enddate,
        	"introduce":introduce,
        	"description":description,
        	"rules":rules,
        	"attachurl":attachurl.join(","),
        	"islog":islog_altvalue,
        	"stend":stend,
        	"ndend":ndend,
			"rdend":rdend,
			"techmax":techmax,
			"stumax":stumax,
        	},
        success: function(data){
        	console.log(data)
        	if(data.status == 0){
        		alertMsg("2","修改成功！","success")
        		setTimeout('history.back()',2000)
        	}else if(data.status == 1){
        		alertMsg("2",data.errmsg,"fail");
        	}
        },
        error:function(data){
        	
        }
	})
}


function uploadFile(){
	var namestr=$("#file1").val();
	var filename=namestr.split("\\")[namestr.split("\\").length-1];
	var htmls="";
	if(namestr!=""){
		$("#myform1").ajaxSubmit({
			url : "../uploadFiles",
			dataType : 'json',
			async : false,
			success : function(data) {
				if(data.status==0){
					console.log(data)
					htmls+='<div class="enclosureitem"><a class="filename">'+filename
    						+'</a><form class="editform" enctype="multipart/form-data" method="post">'
    						+'<a class="editfile">更改<input type="file" class="hidfile" onchange="editFile(this)" name="files"></a>'
    						+'<input hidden name="savetype" value="01">'
    						+'<input hidden name="filename" value="'+data.urls+'">'
    						+'</form><a class="delfile" '
    						+'onclick="alertConfirm(\'2\',\'确定删除该附件吗？\',\'delFile()\',this)">&times;</a></div>';
					$("#myform1").before(htmls);
					alertMsg("2","上传成功！","success")
				}else if(data.status==1){
					alertMsg("2",data.errmsg,"fail")
				}
			},
			error:function(){
				alertMsg("2","上传失败！","fail")
			}
	    })
	}	
}

function editFile(obj){
var namestr=$(obj).val();
var filename=namestr.split("\\")[namestr.split("\\").length-1];
var htmls="";
if(namestr!=""){
	$(obj).parent().parent().ajaxSubmit({
		url : "../uploadFiles",
		dataType : 'json',
		async : false,
		success : function(data) {
			if(data.status==0){
				console.log(data)
				htmls+='<a class="filename">'+filename
						+'</a><form class="editform" enctype="multipart/form-data" method="post">'
						+'<a class="editfile">更改<input type="file" class="hidfile" onchange="editFile(this)" name="files"></a>'
						+'<input hidden name="savetype" value="01">'
						+'<input hidden name="filename" value="'+data.urls+'">'
						+'</form><a class="delfile" '
						+'onclick="alertConfirm(\'2\',\'确定删除该附件吗？\',\'delFile()\',this)">&times;</a>';
				$(obj).parent().parent().parent().html(htmls);
				alertMsg("2","修改成功！","success")
			}else if(data.status==1){
				alertMsg("2",data.errmsg,"fail")
			}
		},
		error:function(){
			alertMsg("2","上传失败！","fail")
		}
    })
}	
}

function delFile(obj){
	closeWin();
	$(tempobj).parent().remove();
}