$(function(){
	var htmls="";
	$.each(regions,function(i,value){
		htmls+='<li>'+value+'</li>'
	})
	$("#region").next().append(htmls);
	htmls="";
	$.each(orgtypes,function(i,value){
		htmls+='<li>'+value+'</li>'
	})
	$("#orgtype").next().append(htmls);
	$(document).click(function(){
		$(".emulate").hide();
	})
   /* $(".selinput").click(function(e){
    	e.stopPropagation();
    	var me=this;
    	$(me).next().show();
    	$(me).next().children().click(function(){
    		$(me).val($(this).text());
    		$(me).next().hide();
    	})
    })*/
	getTeamDetail()
})

var tid=sessionStorage.getItem("tid");
var rid=sessionStorage.getItem("rid");
var mid=sessionStorage.getItem("currentmid");

function getTeamDetail(){
	var mid=sessionStorage.getItem("currentmid");
	$.ajax({
		type: "GET",
        url: "../findRaceAllMembersByRidTid",
        dataType: "JSON",
        async:false,
        data: {
        	"tid":tid,
        	"rid":rid,
        	"mid":mid
        	},
        success: function(data){
        	if(data.status == 0){
        		$("#rname").html(data.list[0].rname);
        		$("#tname").val(data.list[0].tname);
        		$("#tschool").val(data.list[0].tmschool);
        		$("#tdepartname").val(data.list[0].tmdepartname);
        		$("#orgtype").val(data.list[0].orgtype);
        		$("#region").val(data.list[0].region);
        		sessionStorage.setItem("currentmid",data.list[0].mid);
        		var memList=data.list;
        		
        		$.each(memList,function(i,mem){
        			var htmls=""; 
        			htmls+='<tr tmid="'+mem.tmid+'"><td><img class="headicon" src="'+mem.picurl+'">'
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
					htmls+='<td><a class="editbtn" onclick="editMember(this)" detail=\''+JSON.stringify(mem)+'\'></a>'
						+'<a class="delbtn" onclick="alertConfirm(\'1\',\'确定删除该成员吗？\',\'delMem()\',this)"></a>'
					/*	+'<a onclick="moveUp(this)" class="upBtn">上移</a><a class="downBtn" onclick="moveDown(this)">下移</a></td>'*/
					/*htmls+='<td>'*/
						+'<a onclick="moveUp(this)" class="upBtn"></a><a class="downBtn" onclick="moveDown(this)"></a></td>'
					htmls+='</tr>'
						if(mem.roleflg=="01"){
							$(".teaList tbody").append(htmls)
						}
						else{$(".stuList tbody").append(htmls)}
        		})
        		canMove();
        	}else if(data.status == 1){
        		alertMsg("1",data.errmsg,"fail");
        	}
        },
	})
}

function saveTeam(){
	adjustOrder();
	var tname=$("#tname").val();
	var school=$("#tschool").val();
	var departname=$("#tdepartname").val();
	var orgtype=$("#orgtype").val();
	var region=$("#region").val();
	
	if(tname.trim()==""){
		alertMsg("1","请输入队伍名称！","fail");
		return;
	}else if(tname.indexOf(",")!=-1){
		alertMsg("1","队伍名称不允许出现非法字符！","fail");
		return;
	}
	if(orgtype.trim()==""){
		alertMsg("1","请选择机构类别！","fail");
		return;
	}
	if(region.trim()==""){
		alertMsg("1","请选择所属地区！","fail");
		return;
	}
	if($(".teaList tbody tr").length==0){
		alertMsg("1","请添加指导教师！","fail");
		return;
	}
	if($(".stuList tbody tr").length==0){
		alertMsg("1","请添加队员！","fail");
		return;
	}
	
	$.ajax({
		type: "GET",
        url: "../saveTeamInfo",
        dataType: "JSON",
        async:false,
        data: {
        	"tid":tid,
        	"tname":tname,
        	"school":school,
        	"departname":departname,
        	"mid":mid
        	},
        success: function(data){
        	if(data.status == 0){
        		alertMsg("1","保存成功！","success");
        		setTimeout("location.reload()",2000)
        	}else if(data.status == 1){
        		alertMsg("1",data.errmsg,"fail");
        	}
        },
	})
}

var temp;
var picurl="";
function editMember(obj){
	temp=obj;
	var info=JSON.parse($(obj).attr("detail"));
	var htmls="";
	picurl=info.picurl;
	htmls+='<div id="shade"></div><div id="changeteainfo">'
			+'<img class="guanbi" src="../images/guanbi.png" onclick="closeInfoWin()">'
			+'<div id="form">';
	if(info.roleflg=="01"){htmls+='<h1>修改指导教师</h1>'}
	else{htmls+='<h1>修改队员</h1>'}
    htmls+='<div class="changeinfo clearfix"><div class="div-l">'
			+'<div class="perinfodiv"><span>姓名</span><input type="text" id="tmname" value="'+info.tmname+'"><span class="editspan-end">*</span></div>'
			+'<div class="perinfodiv"><span>民族</span><input type="text" id="folk" readonly altvalue="'+info.folk
			+'" class="selinput" value="';
    $.each(folkname,function(i,nation){
		if(info.folk==nation.folkid){
			htmls+=nation.folk
		}
	})
	htmls+=	'"><ul class="emulate"></ul><span class="editspan-end">*</span></div>'
			+'<div class="perinfodiv"><span>证件类型</span><input id="didtype" type="text" name="" readonly="readonly"'
			+' altvalue="'+info.didtype+'" class="selinput" value="';
    if(info.didtype=="00"){htmls+='身份证'}
	else if(info.didtype=="01"){htmls+='护照'}
	else{htmls+='港澳台通行证'}
    htmls+='"><ul class="emulate"><li altvalue="00">身份证</li><li altvalue="01">护照</li>'
			+'<li altvalue="02">港澳台通行证</li></ul><span class="editspan-end">*</span></div>'
			+'<div class="perinfodiv"><span>证件号</span><input type="text" id="did"'
			+' value="'+info.did+'" onblur="autofillbirthday()"><span class="editspan-end">*</span></div>'
			+'<div class="perinfodiv"><span>学校</span><input type="text" id="school"'
			+' value="'+info.school+'"><span class="editspan-end">*</span></div>'
			+'</div><div class="div-c">'
			+'<div class="perinfodiv" id="sex"><span>性别</span>'
	if(info.sex=="01"){
		htmls+='<input value="01" type="radio" name="sex" checked>男'
			+'<input value="02" type="radio" name="sex" id="female">女</div>'
	}else{
		htmls+='<input value="01" type="radio" name="sex">男'
			+'<input value="02" type="radio" name="sex" id="female" checked>女</div>'
	}
			
	htmls+='<div class="perinfodiv"><span>生日</span><input id="birthday" type="text" readonly '
			+'value="'+info.birthday+'" onclick="laydate({ elem:\'#birthday\', format:\'YYYY-MM-DD\'} )"><span class="editspan-end">*</span></div>'
			+'<div class="perinfodiv"><span>邮箱</span><input type="text" id="email"'
			+' value="'+info.email+'"><span class="editspan-end">*</span></div>'
			
			+'<div class="perinfodiv"><span>手机</span><input type="text" id="phone"'
			+' value="'+info.phone+'"><span class="editspan-end">*</span></div>'
			+'<div class="perinfodiv"><span>院系</span><input type="text" id="departname"'
			+' value="'+info.departname+'"></div>'
			+'</div><div class="div-r">'
			+'<span id="zhaopian">照片</span><form id="myform" enctype="multipart/form-data" method="post">'
			+'<img src="'+info.picurl+'" id="portrait">'
			+'<input type="file" class="imgfile" id="file" name="files" onchange="uploadImg()"><input hidden name="savetype" value="00"></form>'
			+'<span id="imgformat">413*626px,不超过1000kb</span>'
			+'<div class="perinfodiv"><span style="vertical-align: middle;">用餐类型</span>'
			+'<input id="diningtype" type="text" readonly="readonly" altvalue="'+info.diningtype
			+'" class="selinput" value="'
	if(info.diningtype=="01"){htmls+='普通'}
	else if(info.diningtype=="02"){htmls+='清真'}
	else{htmls+='素食'}		
	htmls+='"><ul class="emulate"><li altvalue="01">普通</li><li altvalue="02">清真</li><li altvalue="03">素食</li></ul><span class="editspan-end">*</span></div>'
			+'</div></div><div class="memsave"><a id="savebtn" onclick="editSaveInfo()">保存</a></div></div></div>';
    $("body").append(htmls);
    var html2=""
        $.each(folkname,function(i,nation){
    		html2+='<li altvalue="'+nation.folkid+'">'+nation.folk+'</li>'
    	})
    	$("#folk").next().append(html2)
    	$(document).click(function(){
    		$(".emulate").hide();
    	})
        $(".selinput").click(function(e){
        	e.stopPropagation();
        	var me=this;
        	$(me).next().show();
        	$(me).next().children().click(function(){
        		$(me).val($(this).text());
        		$(me).attr("altvalue",$(this).attr("altvalue"));
        		$(me).next().hide();
        	})
        })
}


function editSaveInfo(){
	var obj = temp
	var htmls="";
	var roleflg=JSON.parse($(obj).attr("detail")).roleflg;
	var tmid=JSON.parse($(obj).attr("detail")).tmid;
	var tmname=$("#tmname").val();
	var folk=$("#folk").attr("altvalue");
	var didtype=$("#didtype").attr("altvalue");
	var email=$("#email").val();
	var school=$("#school").val();
	var sex=$("#sex").find("input:checked").val();
	var birthday=$("#birthday").val();
	var did=$("#did").val();
	var phone=$("#phone").val();
	var departname=$("#departname").val();
	var diningtype=$("#diningtype").attr("altvalue");
	var mid=JSON.parse($(obj).attr("detail")).mid;
	if(tmname.trim()==""){
		alertMsg("1","请填写姓名！","fail");
		return;
	}else if(tmname.indexOf(",")!=-1){
		alertMsg("1","姓名不允许出现非法字符！","fail");
		return;
	}
	if(folk==""){
		alertMsg("1","请选择民族！","fail");
		return;
	}
	if(didtype==""){
		alertMsg("1","请选择证件类型！","fail");
		return;
	}
	if(did.trim()==""){
		alertMsg("1","请填写证件号！","fail");
		return;
	}else{
		var reg=/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
		if(didtype=="00"&&!IdentityCodeValid.test(did)){
			alertMsg("1","证件号格式不正确！","fail");
			return;
		}
	}
	if(school==""){
		alertMsg("1","请填写学校！","fail");
		return;
	}
	if(sex==""||sex==undefined){
		alertMsg("1","请选择性别！","fail");
		return;
	}
	if(birthday=="--"){
		alertMsg("1","请填写生日！","fail");
		return;
	}
	if(email.trim()==""){
		alertMsg("1","请填写邮箱！","fail");
		return;
	}else{
		var reg=/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
		if(!reg.test(email)){
			alertMsg("1","邮箱格式不正确！","fail");
			return;
		}
	}
	if(phone.trim()==""){
		alertMsg("1","请填写手机号！","fail");
		return;
	}else{
		var reg=/^1[34578]\d{9}$/; 
		if(!reg.test(phone)){
			alertMsg("1","手机号格式不正确！","fail");
			return;
		}
	}
	if(picurl==""){
		alertMsg("1","请上传头像！","fail");
		return;
	}
	if(diningtype==""){
		alertMsg("1","请选择用餐类型！","fail");
		return;
	}
	
	//验证队员年龄
	if(roleflg=="02"){
		if(!checktmAge(birthday)){
			alertMsg("1","该队员年龄不符合，不可参加此赛项！","fail");
			return;
		}
	}

	var info={};
	info.tmid=tmid;
	info.roleflg=roleflg;
	info.tmname=tmname;
	info.folk=folk;
	info.didtype=didtype;
	info.email=email;
	info.school=school;
	info.sex=sex;
	info.birthday=birthday;
	info.did=did;
	info.phone=phone;
	info.departname=departname;
	info.diningtype=diningtype;
	info.picurl=picurl;
	info.mid=mid;
	htmls+='<td><img class="headicon" src="'+$("#portrait").attr("src")+'">'
	    	+'<span>'+tmname+'</span></td><td><img src="../images/'+(sex=="01"?'male.png':'female.png')
	    	+'"></td>';
	$.each(folkname,function(i,nation){
		if(folk==nation.folkid){
			htmls+='<td>'+nation.folk+'</td>'
		}
	})
    htmls+='<td>'+birthday+'</td>';
	if(didtype=="00"){htmls+='<td>身份证</td>'}
	else if(didtype=="01"){htmls+='<td>护照</td>'}
	else{htmls+='<td>港澳台通行证</td>'}
	htmls+='<td>'+did+'</td>'
			+'<td>'+email+'</td>'
			+'<td>'+phone+'</td>'
			+'<td>'+school+'</td>'
			+'<td>'+departname+'</td>';
	if(diningtype=="01"){htmls+='<td>普通</td>'}
	else if(diningtype=="02"){htmls+='<td>清真</td>'}
	else{htmls+='<td>素食</td>'}
	htmls+='<td><a class="editbtn" onclick="editMember(this)" detail=\''+JSON.stringify(info)+'\'></a>'
		+'<a class="delbtn" onclick="alertConfirm(\'1\',\'确定删除该成员吗？\',\'delMem()\',this)"></a></td>'
	$.ajax({
		type: "GET",
        url: "../updateMember",
        dataType: "JSON",
        async:false,
        data: info,
        success: function(data){
        	if(data.status == 0){
        		alertMsg("1","修改成功！","success");
        		setTimeout('location.reload()',2000);
        	}else if(data.status == 1){
        		alertMsg("1",data.errmsg,"fail");
        	}
        },
	})
}

function uploadImg(){
	var filetype=$("#file").val().slice($("#file").val().lastIndexOf(".")+1).toUpperCase()
	if (filetype == 'JPG'||filetype == 'PNG'||filetype == 'JPEG'){
		$("#myform").ajaxSubmit({
			url : "../uploadFiles",
			dataType : 'json',
			async : false,
			success : function(data) {
				if(data.status==0){
					readAsDataURL();
					picurl=data.urls;
				}else if(data.status==1){
					alertMsg("1",data.errmsg,"fail");
				}
			},
			error:function(){
				alertMsg("1","上传失败！","fail");
			}
	    })
	}else{
		alertMsg("1","文件格式不正确！","fail");
		return
	}
  
}

//图片预览
function readAsDataURL(){
  var simpleFile = document.getElementById("file").files[0];
  var reader = new FileReader();
  // 将文件以Data URL形式进行读入页面
  reader.readAsDataURL(simpleFile);
  reader.onload = function(e){
      $("#portrait").attr("src",this.result);
  }
}

function closeInfoWin(){
	$("#shade").remove()
	$("#changeteainfo").remove()
	$("#didinfo").remove();
}

function addNewMember(roleflg,didtype,did){
	picurl="";
	var htmls="";
	htmls+='<div id="shade"></div><div id="changeteainfo">'
			+'<img class="guanbi" src="../images/guanbi.png" onclick="closeInfoWin()">'
			+'<div id="form" action="">';
	if(roleflg=="01"){htmls+='<h1>添加指导教师</h1>'}
	else{htmls+='<h1>添加队员</h1>'}
    htmls+='<div class="changeinfo clearfix"><div class="div-l">'
			+'<div class="perinfodiv"><span>姓名</span><input type="text" id="tmname"><span class="editspan-end">*</span></div>'
			+'<div class="perinfodiv"><span>民族</span><input type="text" id="folk" altvalue="" class="selinput"><ul class="emulate"></ul><span class="editspan-end">*</span></div>'
			+'<div class="perinfodiv"><span>证件类型</span><input id="didtype" type="text" name="" disabled="disabled" altvalue="'
			+didtype+'" class="selinput" value="';
    if(didtype=="00"){htmls+='身份证'}
	else if(didtype=="01"){htmls+='护照'}
	else{htmls+='港澳台通行证'}		
    	htmls+=	'">'
			+'<ul class="emulate"><li altvalue="00">身份证</li><li altvalue="01">护照</li>'
			+'<li altvalue="02">港澳台通行证</li></ul><span class="editspan-end">*</span></div>'
			+'<div class="perinfodiv"><span>证件号</span><input type="text" id="did" disabled="disabled" value="'
			+did+'"><span class="editspan-end">*</span></div>'
			+'<div class="perinfodiv"><span>学校</span><input type="text" id="school"><span class="editspan-end">*</span></div>'
			+'</div><div class="div-c">'
			+'<div class="perinfodiv" id="sex"><span>性别</span>'
			var sexnum=parseInt(did.slice(-2,-1));
    		if(sexnum%2==0){
    			htmls+='<input value="01" type="radio" name="sex">男'
    				+'<input value="02" type="radio" name="sex" checked id="female">女</div>'
    		}else{
    			htmls+='<input value="01" type="radio" checked name="sex">男'
    				+'<input value="02" type="radio" name="sex" id="female">女</div>'
    		}
    		var birthnum=did.slice(6,14);
    	htmls+='<div class="perinfodiv"><span>生日</span><input id="birthday" type="text" onclick="laydate({ elem:\'#birthday\', format:\'YYYY-MM-DD\'} )"'
    		+' value="'+birthnum.slice(0,4)+'-'+birthnum.slice(4,6)+'-'+birthnum.slice(6)+'"><span class="editspan-end">*</span></div>'
			+'<div class="perinfodiv"><span>邮箱</span><input type="text" id="email"><span class="editspan-end">*</span></div>'
			+'<div class="perinfodiv"><span>手机</span><input type="text" id="phone"><span class="editspan-end">*</span></div>'
			+'<div class="perinfodiv"><span>院系</span><input type="text" id="departname"></div>'
			+'</div><div class="div-r">'
			+'<span id="zhaopian">照片</span><form id="myform" enctype="multipart/form-data" method="post"><img src="" id="portrait">'
			+'<input type="file" class="imgfile" id="file" name="files" onchange="uploadImg()"><input hidden name="savetype" value="00"></form>'
			+'<span id="imgformat">413*626px,不超过1000kb</span>'
			+'<div class="perinfodiv"><span style="vertical-align: middle;">用餐类型</span><input id="diningtype" type="text" readonly="readonly" altvalue="" class="selinput">'
			+'<ul class="emulate"><li altvalue="01">普通</li><li altvalue="02">清真</li><li altvalue="03">素食</li></ul><span class="editspan-end">*</span></div>'
			+'</div></div><div class="memsave"><a id="savebtn" onclick="saveInfo(\''+roleflg+'\')">添加</a></div></div></div>';
	$("body").append(htmls);
	//只有证件类型为身份证，生日只读
	if(didtype=="00"){
		$('#birthday').attr("disabled","disabled")
		$('#birthday').attr("style","background:#CCCCCC");
	}
	$('#didtype').attr("style","background:#CCCCCC");
	$('#did').attr("style","background:#CCCCCC");

    var html2=""
    $.each(folkname,function(i,nation){
		html2+='<li altvalue="'+nation.folkid+'">'+nation.folk+'</li>'
	})
	$("#folk").next().append(html2)
	$(document).click(function(){
		$(".emulate").hide();
	})
    $(".selinput").click(function(e){
    	e.stopPropagation();
    	var me=this;
    	$(me).next().show();
    	$(me).next().children().click(function(){
    		$(me).val($(this).text());
    		$(me).attr("altvalue",$(this).attr("altvalue"));
    		$(me).next().hide();
    	})
    })
}
function addOldMember(roleflg,info){
	var htmls="";
	picurl=info.picurl;
	htmls+='<div id="shade"></div><div id="changeteainfo">'
			+'<img class="guanbi" src="../images/guanbi.png" onclick="closeInfoWin()">'
			+'<div id="form" action="">';
	if(roleflg=="01"){htmls+='<h1>添加指导教师</h1>'}
	else{htmls+='<h1>添加队员</h1>'}
    htmls+='<div class="changeinfo clearfix"><div class="div-l">'
			+'<div class="perinfodiv"><span>姓名</span><input type="text" id="tmname" value="'+info.tmname+'"><span class="editspan-end">*</span></div>'
			+'<div class="perinfodiv"><span>民族</span><input type="text" id="folk" readonly altvalue="'+info.folk
			+'" class="selinput" value="';
    $.each(folkname,function(i,nation){
		if(info.folk==nation.folkid){
			htmls+=nation.folk
		}
	})
	htmls+=	'"><ul class="emulate"></ul><span class="editspan-end">*</span></div>'
			+'<div class="perinfodiv"><span>证件类型</span><input id="didtype" type="text" name="" disabled="disabled"'
			+' altvalue="'+info.didtype+'" class="selinput" value="';
    if(info.didtype=="00"){htmls+='身份证'}
	else if(info.didtype=="01"){htmls+='护照'}
	else{htmls+='港澳台通行证'}
    htmls+='"><ul class="emulate"><li altvalue="00">身份证</li><li altvalue="01">护照</li>'
			+'<li altvalue="02">港澳台通行证</li></ul><span class="editspan-end">*</span></div>'
			+'<div class="perinfodiv"><span>证件号</span><input type="text" id="did"'
			+' value="'+info.did+'" disabled="disabled"><span class="editspan-end">*</span></div>'
			+'<div class="perinfodiv"><span>学校</span><input type="text" id="school"'
			+' value="'+info.school+'"><span class="editspan-end">*</span></div>'
			+'</div><div class="div-c">'
			+'<div class="perinfodiv" id="sex"><span>性别</span>'
	if(info.sex=="01"){
		htmls+='<input value="01" type="radio" name="sex" checked>男'
			+'<input value="02" type="radio" name="sex" id="female">女</div>'
	}else{
		htmls+='<input value="01" type="radio" name="sex">男'
			+'<input value="02" type="radio" name="sex" id="female" checked>女</div>'
	}
			
	htmls+='<div class="perinfodiv"><span>生日</span><input id="birthday" type="text" '
			+'value="'+info.birthday+'" onclick="laydate({ elem:\'#birthday\', format:\'YYYY-MM-DD\'} )"><span class="editspan-end">*</span></div>'
			+'<div class="perinfodiv"><span>邮箱</span><input type="text" id="email"'
			+' value="'+info.email+'"><span class="editspan-end">*</span></div>'
			+'<div class="perinfodiv"><span>手机</span><input type="text" id="phone"'
			+' value="'+info.phone+'"><span class="editspan-end">*</span></div>'
			+'<div class="perinfodiv"><span>院系</span><input type="text" id="departname"'
			+' value="'+info.departname+'"></div>'
			+'</div><div class="div-r">'
			+'<span id="zhaopian">照片</span><form id="myform" enctype="multipart/form-data" method="post">'
			+'<img src="'+picurl+'" id="portrait">'
			+'<input type="file" class="imgfile" id="file" name="files" onchange="uploadImg()"><input hidden name="savetype" value="00"></form>'
			+'<span id="imgformat">413*626px,不超过1000kb</span>'
			+'<div class="perinfodiv"><span style="vertical-align: middle;">用餐类型</span>'
			+'<input id="diningtype" type="text" readonly="readonly" altvalue="'+info.diningtype
			+'" class="selinput" value="'
	if(info.diningtype=="01"){htmls+='普通'}
	else if(info.diningtype=="02"){htmls+='清真'}
	else{htmls+='素食'}		
	htmls+='"><ul class="emulate"><li altvalue="01">普通</li><li altvalue="02">清真</li><li altvalue="03">素食</li></ul><span class="editspan-end">*</span></div>'
			+'</div></div><div class="memsave"><a id="savebtn" onclick="saveInfo(\''+roleflg+'\')">添加</a></div></div></div>';
	$("body").append(htmls);
	
	//只有证件类型为身份证，生日只读
	if(info.didtype=="00"){
		$('#birthday').attr("disabled","disabled")
		$('#birthday').attr("style","background:#CCCCCC");
	}
	$('#did').attr("style","background:#CCCCCC");
	$('#didtype').attr("style","background:#CCCCCC");
	

    var html2=""
        $.each(folkname,function(i,nation){
    		html2+='<li altvalue="'+nation.folkid+'">'+nation.folk+'</li>'
    	})
    	$("#folk").next().append(html2)
    	$(document).click(function(){
    		$(".emulate").hide();
    	})
        $(".selinput").click(function(e){
        	e.stopPropagation();
        	var me=this;
        	$(me).next().show();
        	$(me).next().children().click(function(){
        		$(me).val($(this).text());
        		$(me).attr("altvalue",$(this).attr("altvalue"));
        		$(me).next().hide();
        	})
        })
}
function saveInfo(flg){
	if(!distinctMem()){
		alertMsg("1","该成员已存在！","fail");
		return;
	}
	var htmls="";
	var roleflg=flg;
	var tmname=$("#tmname").val();
	var folk=$("#folk").attr("altvalue");
	var didtype=$("#didtype").attr("altvalue");
	var email=$("#email").val();
	var school=$("#school").val();
	var sex=$("#sex").find("input:checked").val();
	var birthday=$("#birthday").val();
	var did=$("#did").val();
	var phone=$("#phone").val();
	var departname=$("#departname").val();
	var diningtype=$("#diningtype").attr("altvalue");
	var mid=sessionStorage.getItem("currentmid");
	var picurl_t=$("#portrait").attr("src");
	if(tmname.trim()==""){
		alertMsg("1","请填写姓名！","fail");
		return;
	}else if(tmname.indexOf(",")!=-1){
		alertMsg("1","姓名不允许出现非法字符！","fail");
		return;
	}
	if(folk==""){
		alertMsg("1","请选择民族！","fail");
		return;
	}
	if(didtype==""){
		alertMsg("1","请选择证件类型！","fail");
		return;
	}
	if(did.trim()==""){
		alertMsg("1","请填写证件号！","fail");
		return;
	}else{
		var reg=/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
		if(didtype=="00"&&!IdentityCodeValid(did)){
			alertMsg("1","证件号格式不正确！","fail");
			return;
		}
	}
	if(school==""){
		alertMsg("1","请填写学校！","fail");
		return;
	}
	if(sex==""||sex==undefined){
		alertMsg("1","请选择性别！","fail");
		return;
	}
	if(birthday=="--"){
		alertMsg("1","请选择生日！","fail");
		return;
	}
	if(email.trim()==""){
		alertMsg("1","请填写邮箱！","fail");
		return;
	}else{
		var reg=/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
		if(!reg.test(email)){
			alertMsg("1","邮箱格式不正确！","fail");
			return;
		}
	}
	if(phone.trim()==""){
		alertMsg("1","请填写手机号！","fail");
		return;
	}else{
		var reg=/^1[34578]\d{9}$/; 
		if(!reg.test(phone)){
			alertMsg("1","手机号格式不正确！","fail");
			return;
		}
	}
	if(picurl_t==""){
		alertMsg("1","请上传头像！","fail");
		return;
	}
	if(diningtype==""){
		alertMsg("1","请选择用餐类型！","fail");
		return;
	}

	//验证队员年龄
	if(roleflg=="02"){
		if(!checktmAge(birthday)){
			alertMsg("1","该队员年龄不符合，不可参加此赛项！","fail");
			return;
		}
	}

	var info={};
	info.tid=tid;
	info.roleflg=roleflg;
	info.tmname=tmname;
	info.folk=folk;
	info.didtype=didtype;
	info.email=email;
	info.school=school;
	info.sex=sex;
	info.birthday=birthday;
	info.did=did;
	info.phone=phone;
	info.departname=departname;
	info.diningtype=diningtype;
	info.picurl=picurl;
	info.mid=mid;
	if(roleflg=="01"){
		info.serialnum=$(".teaList tbody tr").length+1;
	}else{
		info.serialnum=$(".stuList tbody tr").length+1;
	}
	$.ajax({
		type: "GET",
        url: "../addMemeberToTeam",
        dataType: "JSON",
        async:false,
        data: info,
        success: function(data){
        	if(data.status == 0){
        		alertMsg("1","添加成功！","success");
        		setTimeout('location.reload()',2000);
        	}else if(data.status == 1){
        		alertMsg("1",data.errmsg,"fail");
        	}
        },
	})
	/*htmls+='<tr><td><img class="headicon" src="'+$("#portrait").attr("src")+'">'
	    	+'<span>'+tmname+'</span></td><td><img src="../images/'+(sex=="01"?'male.png':'female.png')
	    	+'"></td>';
	$.each(folkname,function(i,nation){
		if(folk==nation.folkid){
			htmls+='<td>'+nation.folk+'</td>'
		}
	})
    htmls+='<td>'+birthday+'</td>';
	if(didtype=="00"){htmls+='<td>身份证</td>'}
	else if(didtype=="01"){htmls+='<td>护照</td>'}
	else{htmls+='<td>港澳台往来大陆通行证</td>'}
	htmls+='<td>'+did+'</td>'
			+'<td>'+email+'</td>'
			+'<td>'+phone+'</td>'
			+'<td>'+school+'</td>'
			+'<td>'+departname+'</td>';
	if(diningtype=="01"){htmls+='<td>普通</td>'}
	else if(diningtype=="02"){htmls+='<td>清真</td>'}
	else{htmls+='<td>素食</td>'}
	htmls+='<td><a class="editbtn" onclick="editMember(this)" detail=\''+JSON.stringify(info)+'\'></a><a class="delbtn"></a></td></tr>'
	if(flg=="01"){
		$(".teaList tbody").append(htmls)
	}
	else{$(".stuList tbody").append(htmls)}*/
}

function distinctMem(){
	var hash={};
	$(".editbtn").each(function(){
		var info=JSON.parse($(this).attr("detail"));
		var idstr=info.didtype+info.did;
		hash[idstr]=idstr;
	})
	var inchecked=$("#didtype").attr("altvalue")+$("#did").val();
	if(hash[inchecked]!=null){
		return false;
	}else{
		return true;
	}
}

//根据生日获取年龄
function getAge(dateString) {
    var today = new Date();
	var birthDate = new Date(dateString);
    var age = today.getFullYear() - birthDate.getFullYear();
    var m = today.getMonth() - birthDate.getMonth();
    if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
        age--;
    }
    return age;
}

function checktmAge(birthday){
	//获取当前年龄
	var age=getAge(birthday);
	alert(age);

	var stuoldmin;
	var stuoldmax;
	//获取年龄限制
	$.ajax({
		type: "GET",
        url: "../findRaceInfoByRid",
        dataType: "JSON",
        async:false,
        data: {
        	"rid":rid
        	},
        success: function(data){
        	console.log(data)
        	if(data.status == 0){
				var info=data.info;
				stuoldmin = info.stuoldmin;
				stuoldmax = info.stuoldmax;
        	}else if(data.status == 1){
        		alertMsg("1",data.errmsg,"fail")
        	}
        },
        error:function(data){
        	alertMsg("1","后台获取数据错误！","fail")
        }
	})

	//校验年龄限制
	if(age<stuoldmin || age >stuoldmax){
		return false;
	}else{
		return true;
	}
}

function delMem(){
	closeWin();
	if($(tempobj).parent().parent().siblings().length==0){
		alertMsg("1","至少需要一名指导教师或成员！","fail");
		return;
	}
	$(tempobj).parent().parent().remove();
	adjustOrder();
	var tmid=JSON.parse($(tempobj).siblings().attr("detail")).tmid;
	var mid=sessionStorage.getItem("currentmid");
	$.ajax({
		type: "GET",
        url: "../removeMemeberFromTeam",
        dataType: "JSON",
        async:false,
        data: {
        	"tid":tid,
        	"tmid":tmid,
        	"mid":mid
        },
        success: function(data){
        	if(data.status == 0){
        		alertMsg("1","删除成功","success")
        		setTimeout('location.reload()',2000);
        	}else if(data.status == 1){
        		alertMsg("1",data.errmsg,"fail")
        	}
        },
	})
}

function searchBydid(roleflg){
	//校验教师，学生数量
	var stumax;
	var techmax;
	$.ajax({
		type: "GET",
        url: "../findRaceTechStuMax",
        dataType: "JSON",
        async:false,
        data: {
        	"mid":mid,
        	"rid":rid
        	},
        success: function(data){
        	if(data.status == 0){
				stumax= data.list[0].stumax;
				techmax= data.list[0].techmax;
        	}else if(data.status == 1){
        		alertMsg("2",data.errmsg,"fail")
        	}
        },
	})

	if(roleflg=="01"){
		if($(".teaList tbody tr").length>=techmax){
			alertMsg("1","指导老师只能添加"+techmax+"位","fail");
			return;
		}
	}else{
		if($(".stuList tbody tr").length>=stumax){
			alertMsg("1","队员只能添加"+stumax+"位","fail");
			return;
		}
	}

	var htmls="";
	htmls+='<div id="shade"></div><div id="didinfo">'
		+'<img class="guanbi" src="../images/guanbi.png" onclick="closeInfoWin()">';
	if(roleflg=="01"){htmls+='<h1>添加指导教师</h1>'}
	else{htmls+='<h1>添加队员</h1>'}
	htmls+='<div class="diditem"><span>证件类型：</span><input type="text" readonly altvalue="" id="IDtype">'
			+'<ul class="emulate"><li altvalue="00">身份证</li><li altvalue="01">护照</li>'
			+'<li altvalue="02">港澳台通行证</li></ul></div>'
			+'<div class="diditem"><span>证件号：</span><input type="text" id="IDnumber"></div>'
			+'<a class="nextstep" onclick="searchMem(\''+roleflg+'\')">下一步</a></div>';
	$("body").append(htmls)
	$(document).click(function(){
		$(".emulate").hide();
	})
    $("#IDtype").click(function(e){
    	e.stopPropagation();
    	$("#IDtype").next().show();
    	$("#IDtype").next().children().click(function(){
    		$("#IDtype").val($(this).text());
    		$("#IDtype").attr("altvalue",$(this).attr("altvalue"));
    		$("#IDtype").next().hide();
    	})
    })
}

function searchMem(roleflg){
	var didtype=$("#IDtype").attr("altvalue");
	var did=$("#IDnumber").val();
	var mid=sessionStorage.getItem("currentmid");
	if(didtype==""){
		alertMsg("1","请选择证件类型！","fail");
		return;
	}
	if(did.trim()==""){
		alertMsg("1","请填写证件号！","fail");
		return;
	}else{
		var reg=/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
		if(didtype=="00"&&!IdentityCodeValid(did)){
			alertMsg("1","证件号格式不正确！","fail");
			return;
		}
	}
	closeInfoWin();
	$.ajax({
		type: "GET",
        url: "../findMemberMapByDidMid",
        dataType: "JSON",
        async:false,
        data: {
        	"didtype":didtype,
        	"did":did,
        	"mid":mid
        	},
        success: function(data){
        	if(data.status == 0){
        		addOldMember(roleflg,data.info)
        	}else if(data.status == 1){
           		addNewMember(roleflg,didtype,did)
        	}
        },
	})
}

function moveUp(obj){
	var tr=$(obj).parent().parent().clone();
	$(obj).parent().parent().prev().before(tr);
	$(obj).parent().parent().remove();
	canMove();
} 

function moveDown(obj){
	var tr=$(obj).parent().parent().clone();
	$(obj).parent().parent().next().after(tr);
	$(obj).parent().parent().remove();
	canMove();
} 

function canMove(){
	$(".upBtn,.downBtn").show();
	$(".stuList tbody tr").each(function(){
		if($(".stuList tbody tr").length==1){
			$(this).find(".upBtn").hide();
			$(this).find(".downBtn").hide();
		}else{
			if($(this).index()==0){
				$(this).find(".upBtn").hide()
			}else if($(this).index()==$(".stuList tbody tr").length-1){
				$(this).find(".downBtn").hide()
			}else{
				$(this).find(".upBtn").show();
				$(this).find(".downBtn").show();
			}
		}
	})
	$(".teaList tbody tr").each(function(){
		if($(".teaList tbody tr").length==1){
			$(this).find(".upBtn").hide();
			$(this).find(".downBtn").hide();
		}else{
			if($(this).index()==0){
				$(this).find(".upBtn").hide()
			}else if($(this).index()==$(".teaList tbody tr").length-1){
				$(this).find(".downBtn").hide()
			}else{
				$(this).find(".upBtn").show();
				$(this).find(".downBtn").show();
			}
		}
	})
} 


function adjustOrder(){
	var memList=[];
	$(".stuList tbody tr").each(function(i){
		var obj={};
		obj.tmid=$(this).attr("tmid");
		obj.serialnum=(i+1)
		memList.push(obj)
	})
	$(".teaList tbody tr").each(function(i){
		var obj={};
		obj.tmid=$(this).attr("tmid");
		obj.serialnum=(i+1);
		memList.push(obj)
	})
	$.ajax({
		type: "GET",
        url: "../updateSerialnumByTid",
        dataType: "JSON",
        async:false,
        data: {
        	"memList":JSON.stringify(memList),
        	"tid":tid,
        	"rid":rid
        	},
        success: function(data){
        	if(data.status == 0){
        		
        	}else if(data.status == 1){
        		alertMsg("1",data.errmsg,"fail");
        		return;
        	}
        },
	})
}

function autofillbirthday(){
	var didtype=$("#didtype").attr("altvalue");
	if(didtype!="00"){
		$('#birthday').removeAttr("disabled");
		$('#birthday').removeAttr("style");
		return;
	}

	var did=$("#did").val();
	var reg=/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
		if(didtype=="00"&&!IdentityCodeValid(did)){
			alertMsg("1","证件号格式不正确！","fail");
			return;
		}

	$('#birthday').attr("disabled","disabled");
	$('#birthday').attr("style","background:#CCCCCC");

	var birthnum=did.slice(6,14);
	// alert(didtype);
	// alert(birthnum);
	var birthday=birthnum.slice(0,4)+'-'+birthnum.slice(4,6)+'-'+birthnum.slice(6);
	// alert(birthday);
	$("#birthday").val(birthday);
}