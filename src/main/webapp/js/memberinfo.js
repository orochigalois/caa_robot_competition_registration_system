$(function(){
	getcurmatchname();

	enddate=sessionStorage.getItem("enddate");
	var jsonobj=JSON.parse(sessionStorage.getItem("usermemlistJson"));
	var num="";
	if(jsonobj!=undefined){
		num=jsonobj.pageNo
	}else{
		num=0
	}
	getAllMem(num);
})

var mname;

var enddate="";
function getAllMem(num){
	var mid=sessionStorage.getItem("currentmid");
	var pageSize=10;
	$("#page").unbind().data("pagination", null).empty();	
	$("#page").pagination({
		pageIndex: num,
		pageSize : pageSize,
		showFirstLastBtn : true,
		showPrevious: true,
	    showNext: true,
	    autoHidePrevious: true,
	    autoHideNext: true,
	    showJump: true,
	    jumpBtnText:'跳转',
		remote : {
			"url" : "../findAllMembersBySignuidAndPage",
			pageParams : function(data) {
				var param={};
				param.pageNo=data.pageIndex;
				sessionStorage.setItem("usermemlistJson", JSON.stringify(param));
				return {
					iDisplayStart : data.pageIndex * data.pageSize,
					iDisplayLength : data.pageSize,
					"mid":mid
				};
			}, // [Function]:自定义请求参数
			"success" : function(data, status) {
				console.log(data)
				if (data.status == 0) {
					$("tbody").empty();
					sessionStorage.setItem("currentmid",data.list[0].mid)
					var htmls="";
					$.each(data.list,function(i,mem){
						htmls+='<tr><td><img class="memberhead" src="'+mem.picurl+'">'
				    			+'<span>'+mem.tmname+'</span></td><td>'+mem.tmcode+'</td>'
				    	if(mem.roleflg=="01"){
							htmls+='<td>指导教师</td>'
						}
						else{htmls+='<td>队员</td>'}
				    	htmls+='<td><img src="../images/'+(mem.sex=="01"?'male.png':'female.png')
				    			+'"></td>';
						$.each(folkname,function(i,nation){
							if(mem.folk==nation.folkid){
								htmls+='<td>'+nation.folk+'</td>'
							}
						})
					    htmls+='<td>'+mem.birthday+'</td>'
								+'<td>'+mem.email+'</td>'
								+'<td>'+mem.phone+'</td>'
								+'<td>'+mem.school+'</td>'
						if(mem.diningtype=="01"){htmls+='<td>普通</td>'}
						else if(mem.diningtype=="02"){htmls+='<td>清真</td>'}
						else{htmls+='<td>素食</td>'}
						if(mem.disclaimerurl==""){
							if(!compareTime()){
								htmls+='<td tmid="'+mem.tmid+'"><form enctype="multipart/form-data" method="post">'
								+'<a class="upload">上传PDF免责'
								+'<input type="file" class="pdffile"  name="files" onchange="getPDFUrl(this)">'
								+'<input hidden name="savetype" value="01"></a></form></td>'
							}else{
								htmls+='<td><div class="upload1">上传PDF免责</div></td>'
							}
							
						}else{
							htmls+='<td tmid="'+mem.tmid+'"><a class="view" href="'+mem.disclaimerurl+'">查看文件</a>'
							if(!compareTime()){
								htmls+='<form enctype="multipart/form-data" method="post">'
									+'<a class="upload">重新上传'
									+'<input type="file" class="pdffile"  name="files" onchange="getPDFUrl(this)">'
									+'<input hidden name="savetype" value="01"></a></form>'
									+'</td>'
							}else{
								htmls+='<div class="upload1">重新上传</div></td>'
							}
								
						}
						htmls+='<td detail=\''+JSON.stringify(mem)+'\'>'
								+'<img class="xiangqing" src="../images/xiangqing.png" onclick="viewMem(this)">';
						if(!compareTime()){
								htmls+='<img class="change" src="../images/change.png" onclick="editMember(this)">'				
						}
						htmls+=	'<img class="shanchu" src="../images/saixiang2.png" '
								+'onclick="getMyRace(this)">'
								+'</td></tr>'
					})
					$("tbody").append(htmls)
				} else if(data.status==1){
					alertMsg("1",data.errmsg,"fail")
				}
			},
			"error" : function(data) {
				alertMsg("1","后台获取数据出错！","fail")
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

function viewMem(obj){
	var tmid=JSON.parse($(obj).parent().attr("detail")).tmid;
	sessionStorage.setItem("tmid",tmid);
	window.open("memberDetail.html","_self");
}

var temp;
var picurl="";
function editMember(obj){
	temp=obj;
	var info=JSON.parse($(obj).parent().attr("detail"));
	var htmls="";
	picurl=info.picurl;
	htmls+='<div id="shade"></div><div id="changeteainfo">'
			+'<img class="guanbi" src="../images/guanbi.png" onclick="closeInfoWin()">'
			+'<div id="form">';
	if(info.roleflg=="01"){htmls+='<h1>修改指导教师</h1>'}
	else{htmls+='<h1>修改队员</h1>'}
    htmls+='<div class="changeinfo clearfix"><div class="div-l">'
			+'<div class="perinfodiv"><span>姓名</span><input type="text" id="tmname" value="'+info.tmname+'" onchange="warningimg()"></div>'
			+'<div class="perinfodiv"><span>民族</span><input type="text" id="folk" readonly altvalue="'+info.folk
			+'" class="selinput" value="';
    $.each(folkname,function(i,nation){
		if(info.folk==nation.folkid){
			htmls+=nation.folk
		}
	})
	htmls+=	'"><ul class="emulate"></ul></div>'
			+'<div class="perinfodiv"><span>证件类型</span><input id="didtype" type="text" name="" readonly="readonly"'
			+' altvalue="'+info.didtype+'" class="selinput" value="';
    if(info.didtype=="00"){htmls+='身份证'}
	else if(info.didtype=="01"){htmls+='护照'}
	else{htmls+='港澳台通行证'}
    htmls+='"><ul class="emulate"><li altvalue="00">身份证</li><li altvalue="01">护照</li>'
			+'<li altvalue="02">港澳台通行证</li></ul></div>'
			+'<div class="perinfodiv"><span>证件号</span><input type="text" id="did"'
			+' value="'+info.did+'" onchange="warningimg()" onblur="autofillbirthday()"></div>'
			+'<div class="perinfodiv"><span>学校</span><input type="text" id="school"'
			+' value="'+info.school+'"></div>'
			+'</div><div class="div-c">'
			+'<div class="perinfodiv" id="sex"><span>性别</span>'
	if(info.sex=="01"){
		htmls+='<input value="01" type="radio" name="sex" checked>男'
			+'<input value="02" type="radio" name="sex" id="female">女</div>'
	}else{
		htmls+='<input value="01" type="radio" name="sex">男'
			+'<input value="02" type="radio" name="sex" id="female" checked>女</div>'
	}
	
	mname=$(".currentmatch").text();

	htmls+='<div class="perinfodiv"><span>生日</span><input id="birthday" type="text" readonly '
			+'value="'+info.birthday+'" onclick="laydate({ elem:\'#birthday\', format:\'YYYY-MM-DD\'} )"></div>'
			
			+'<div class="perinfodiv"><span>邮箱</span><input type="text" id="email"'
			+' value="'+info.email+'"></div>'
			+'<div class="perinfodiv"><span>手机</span><input type="text" id="phone"'
			+' value="'+info.phone+'"></div>'
			+'<div class="perinfodiv"><span>院系</span><input type="text" id="departname"'
			+' value="'+info.departname+'"></div>'
			+'</div><div class="div-r">'
			+'<span id="zhaopian">照片</span><form id="myform" enctype="multipart/form-data" method="post">'
			+'<img src="'+info.picurl+'" id="portrait">'
			+'<input type="file" class="imgfile" id="file" name="files" onchange="uploadImg()"><input hidden name="savetype" value="00">'
			+'<input hidden name="mname" value="'+mname+'">'
			+'<input hidden name="tmname" value="" id="form_tmname">'
			+'<input hidden name="strdid" value="" id="form_strdid"></form>'
			+'<span id="imgformat">413*626px,不超过1000kb</span>'
			+'<div class="perinfodiv"><span style="vertical-align: middle;">用餐类型</span>'
			+'<input id="diningtype" type="text" readonly="readonly" altvalue="'+info.diningtype
			+'" class="selinput" value="'
	if(info.diningtype=="01"){htmls+='普通'}
	else if(info.diningtype=="02"){htmls+='清真'}
	else{htmls+='素食'}		
	htmls+='"><ul class="emulate"><li altvalue="01">普通</li><li altvalue="02">清真</li><li altvalue="03">素食</li></ul></div>'
			+'</div></div><div class="memsave"><a id="savebtn" onclick="editSaveInfo()">保存</a></div></div></div>';
	$("body").append(htmls);
	
	//只有证件类型为身份证，生日只读
	if(info.didtype=="00"){
		$('#birthday').attr("disabled","disabled")
		$('#birthday').attr("style","background:#CCCCCC");
	}

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

function warningimg(){
	var src=$('#portrait').attr("src");
	if(src!=""){
		alertMsg("1","修改姓名或证件号码后，请重新上传头像！","fail");
		$('#portrait').attr("src","");
		return;
	}
}


function editSaveInfo(){
	var obj = temp
	var htmls="";
	var roleflg=JSON.parse($(obj).parent().attr("detail")).roleflg;
	var tmid=JSON.parse($(obj).parent().attr("detail")).tmid;
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
	var mid=JSON.parse($(obj).parent().attr("detail")).mid;
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
	if(sex==""||sex==undefined){
		alertMsg("1","请选择性别！","fail");
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
	if(diningtype==""){
		alertMsg("1","请选择用餐类型！","fail");
		return;
	}
	if(picurl_t==""){
		alertMsg("1","请上传头像！","fail");
		return;
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
		+'<a class="delbtn" onclick="delMem(this)"></a></td>'
	$.ajax({
		type: "GET",
        url: "../updateMember",
        dataType: "JSON",
        async:false,
        data: info,
        success: function(data){
        	if(data.status == 0){
        		alertMsg("1","修改成功","success")
        		setTimeout('location.reload()',2000);
        	}else if(data.status == 1){
        		alertMsg("1",data.errmsg,"fail")
        	}
        },
	})
}
function addMember(roleflg){
	closeEnrollWin();
	picurl="";
	var htmls="";
	htmls+='<div id="shade"></div><div id="changeteainfo">'
			+'<img class="guanbi" src="../images/guanbi.png" onclick="closeInfoWin()">'
			+'<div id="form" action="">';
	if(roleflg=="01"){htmls+='<h1>添加指导教师</h1>'}
	else{htmls+='<h1>添加队员</h1>'}
    htmls+='<div class="changeinfo clearfix"><div class="div-l">'
			+'<div class="perinfodiv"><span>姓名</span><input type="text" id="tmname"></div>'
			+'<div class="perinfodiv"><span>民族</span><input type="text" readonly id="folk" altvalue="" class="selinput"><ul class="emulate"></ul></div>'
			+'<div class="perinfodiv"><span>证件类型</span><input id="didtype" type="text" name="" readonly="readonly" altvalue="" class="selinput">'
			+'<ul class="emulate"><li altvalue="00">身份证</li><li altvalue="01">护照</li>'
			+'<li altvalue="02">港澳台通行证</li></ul></div>'
			+'<div class="perinfodiv"><span>证件号</span><input type="text" id="did"></div>'
			+'<div class="perinfodiv"><span>学校</span><input type="text" id="school"></div>'
			+'</div><div class="div-c">'
			+'<div class="perinfodiv" id="sex"><span>性别</span><input value="01" type="radio" name="sex">男'
			+'<input value="02" type="radio" name="sex" id="female">女</div>'
			+'<div class="perinfodiv"><span>生日</span><input id="birthday" type="text" readonly onclick="laydate({ elem:\'#birthday\', format:\'YYYY-MM-DD\'} )"></div>'
			+'<div class="perinfodiv"><span>邮箱</span><input type="text" id="email"></div>'
			
			+'<div class="perinfodiv"><span>手机</span><input type="text" id="phone"></div>'
			+'<div class="perinfodiv"><span>院系</span><input type="text" id="departname"></div>'
			+'</div><div class="div-r">'
			+'<span id="zhaopian">照片</span><form id="myform" enctype="multipart/form-data" method="post"><img src="" id="portrait">'
			+'<input type="file" class="imgfile" id="file" name="files" onchange="uploadImg()"><input hidden name="savetype" value="00"></form>'
			+'<span id="imgformat">413*626px,不超过1000kb</span>'
			+'<div class="perinfodiv"><span style="vertical-align: middle;">用餐类型</span><input id="diningtype" type="text" readonly="readonly" altvalue="" class="selinput">'
			+'<ul class="emulate"><li altvalue="01">普通</li><li altvalue="02">清真</li><li altvalue="03">素食</li></ul></div>'
			+'</div></div><div class="memsave"><a id="savebtn" onclick="saveInfo(\''+roleflg+'\')">添加</a></div></div></div>';
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

function saveInfo(flg){
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
	if(sex==""||sex==undefined){
		alertMsg("1","请选择性别！","fail");
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
	if(diningtype==""){
		alertMsg("1","请选择用餐类型！","fail");
		return;
	}
	if(picurl_t==""){
		alertMsg("1","请上传头像！","fail");
		return;
	}
	var info={};
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
	$.ajax({
		type: "GET",
        url: "../addMember",
        dataType: "JSON",
        async:false,
        data: info,
        success: function(data){
        	if(data.status == 0){
        		alertMsg("1","添加成功!","success")
        		setTimeout('location.reload()',2000);
        	}else if(data.status == 1){
        		alertMsg("1",data.errmsg,"fail")
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
function closeInfoWin(){
	$("#shade").remove()
	$("#changeteainfo").remove();
	$("#myRace").remove();
}

function showEnrollWin(){
	$("#cover,.enrollWin").show();
}
function closeEnrollWin(){
	$("#cover,.enrollWin").hide();
}

function uploadImg(){
	//因为图片命名为证件号码后6位_姓名.jpg
	var didtype=$("#didtype").attr("altvalue");
	var did=$("#did").val();
	var strdid;

	//00身份证 01护照 02 港澳台
	if(didtype=="00"){
		strdid="S"
	}else if(didtype=="01"){
		strdid="H"
	}else if(didtype=="02"){
		strdid="G"
	}else{
		strdid="N"
	}

	if(didtype!="00"){
		if(did.length<6){
			did="000000"+did;
		}
	}
	//截取证件号码的后六位
	strdid += did.slice(-6);
	$("#form_strdid").val(strdid);
	//=========组织照片名称 end===================


	//上传图片
	var tmname=$("#tmname").val();
	if(tmname.trim()==""){
		alertMsg("1","上传头像之前，请填写姓名！","fail");
		return;
	}
	//取修改后的名称,赋值给form
	$("#form_tmname").val(tmname);

		
	var filetype=$("#file").val().slice($("#file").val().lastIndexOf(".")+1).toUpperCase()
	if (filetype == 'JPG'||filetype == 'PNG'||filetype == 'JPEG'){
		$("#myform").ajaxSubmit({
			url : "../uploadFiles_path",
			dataType : 'json',
			async : false,
			success : function(data) {
				if(data.status==0){
					readAsDataURL();
					picurl=data.urls;
				}else if(data.status==1){
					alertMsg("1",data.errmsg,"fail")
				}
			},
			error:function(){
				alertMsg("1","上传失败！","fail")
			}
	    })
	}else{
		alertMsg("1","文件格式不正确！","fail")
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

function delMem(){
	closeWin();
	var tmid=JSON.parse($(tempobj).parent().attr("detail")).tmid;
	$.ajax({
		type: "GET",
        url: "../updateDelflgMemberByTmid",
        dataType: "JSON",
        async:false,
        data: {
        	"tmid":tmid
        },
        success: function(data){
        	if(data.status == 0){
        		alertMsg("1","删除成功!","success")
        		setTimeout('location.reload()',2000);
        	}else if(data.status == 1){
        		alertMsg("1",data.errmsg,"fail")
        	}
        },
	})
}

function getMyRace(obj){
	var tmid=JSON.parse($(obj).parent().attr("detail")).tmid;
	$.ajax({
		type: "GET",
        url: "../findJoinRaceByTmid",
        dataType: "JSON",
        async:false,
        data: {
        	"tmid":tmid,
        },
        success: function(data){
        	if(data.status == 0){
        		console.log(data)
        		var htmls="";
        		htmls+='<div id="shade"></div><div id="myRace">'
        			+'<img class="guanbi" src="../images/guanbi.png" onclick="closeInfoWin()">'
        			+'<h1>已报名赛项</h1><table><thead><tr><td>赛项名称</td><td>所在队伍</td></tr></thead>'
        			+'<tbody>';
	        	$.each(data.list,function(i,item){
	        		htmls+='<tr><td>'+item.rname+'</td><td>'+item.tnames+'</td></tr>'
	        	})
	        	htmls+='</tbody></table>';
	        	$("body").append(htmls)
        	}else if(data.status == 1){
        		alertMsg("2",data.errmsg,"fail")
        	}
        },
	})		
}

function getPDFUrl(obj){
	var tmid=$(obj).parent().parent().parent().attr("tmid")
	if ($(obj).val().slice($(obj).val().lastIndexOf(".")+1).toUpperCase() == 'PDF'){
		$(obj).parent().parent().ajaxSubmit({
			url : "../uploadFiles",
			dataType : 'json',
			async : false,
			success : function(data) {
				if(data.status==0){
					uploadPDF(tmid,data.urls)
				}else if(data.status==1){
					alertMsg("1",data.errmsg,"fail")
				}
			}
	    })
	}else{
		alertMsg("1","文件格式不正确！","fail")
		return
	}
  
}

function uploadPDF(tmid,disclaimerurl){
	$.ajax({
		type: "GET",
        url: "../updateMember",
        dataType: "JSON",
        async:false,
        data: {
        	"tmid":tmid,
        	"disclaimerurl":disclaimerurl
        },
        success: function(data){
        	if(data.status == 0){
        		alertMsg("1","上传成功!","success")
        		setTimeout('location.reload()',2000);
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

function setcurmatch(){
	var mid=$("[name=match]:checked").val();
	sessionStorage.setItem("currentmid",mid);
	$(".shade,.mymatchlist").remove();
	getcurmatchname();
	getAllMem();
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
	var birthday=birthnum.slice(0,4)+'-'+birthnum.slice(4,6)+'-'+birthnum.slice(6);
	$("#birthday").val(birthday);
}