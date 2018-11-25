$(function(){
	var ordernum=timeFormat("02")+randomNum(5);
	var curTime=timeFormat("01");
	$("#ordernum").text(ordernum);
	$("#curTime").text(curTime);
})
function randomNum(n){ 
	var t=''; 
	for(var i=0;i<n;i++){ 
		t+=Math.floor(Math.random()*10); 
	} 
	return t; 
} 

function timeFormat(flg){
	var myDate = new Date();
	var Y =myDate.getFullYear();  //获取完整的年份(4位,1970-????)
	var M =myDate.getMonth()+1;    //获取当前月份(0-11,0代表1月)
	var D =myDate.getDate();    //获取当前日(1-31)   
	var h =myDate.getHours();    //获取当前小时数(0-23)
	var m =myDate.getMinutes();   //获取当前分钟数(0-59)
	var s =myDate.getSeconds();
	if(flg=="01"){
		var str=''+Y+'-'+(M<10?'0'+M:M)+'-'+(D<10?'0'+D:D)
		   +' '+(h<10?'0'+h:h)+':'+(m<10?'0'+m:m);
	}else if(flg=="02"){
		var str=''+Y+(M<10?'0'+M:M)+(D<10?'0'+D:D)
		   +(h<10?'0'+h:h)+(m<10?'0'+m:m)+(s<10?'0'+s:s);
	}
	return str;
}

function payexpense(flg){
	var htmls="";
	htmls+='<div class="shade"></div><div class="payorder"><h1 class="wTitle">支付订单</h1>'
		 +'<ul class="orderinfo clearfix"><li>订单号：<span id="ordernum">'+timeFormat("02")+randomNum(5)+'</span></li>'
		 +'<li>订单时间：<span id="curTime">'+timeFormat("01")+'</span></li>';
	if(flg=="01"||flg=="02"||flg=='03'){
		htmls+='<li>交易金额：<input type="text" id="txnAmt"></li>'
	}
	htmls+=	'</ul>';
	if(flg=="03"){
		htmls+='<div class="photowall"><ul class="photoul clearfix"></ul>'
			 +'<form id="myform" enctype="multipart/form-data" method="post">'
			 +'<a class="addbtn">添加凭证<input type="file" id="file" onchange="uploadImg()" name="files"></a>'
			 +'<input hidden name="savetype" value="01"></form></div>'
	}
	htmls+='<div class="btnwrap">'
		 +'<span style="background:#e21230;margin-right:20px" onclick="';
	if(flg=="01"){
		htmls+='payonline()'
	}else if(flg=="02"){
		htmls+='paydownline()'
	}else if(flg=="03"){
		htmls+='paybyvoucher()'
	}
	htmls+='">确定</span>'
		 +'<span style="color:#333;border:1px solid #ccc;" onclick="closepayexpense()">取消</span>'
		 +'</div></div>';
	$("body").append(htmls);		
}

function closepayexpense(){
	$(".shade").remove();
	$(".payorder").remove();
}

function payonline(){
	var orderId=$("#ordernum").text();
	var txnTime=$("#curTime").text();
	var txnAmt=$("#txnAmt").val();
	var signuid=sessionStorage.getItem("uid");
	var mid=sessionStorage.getItem("currentmid");
	if(txnAmt.trim()==""){
		alertMsg("1","请填写订单金额！","fail");
		return;
	}
	var param={};
	param.orderId=orderId;
	param.txnTime=orderId.slice(0,14);
	param.txnAmt=parseInt(txnAmt*100);
	param.signuid=signuid;
	param.mid=mid;
	var form=$("<form>");//定义一个form表单
	form.attr("style","display:none");
	form.attr("target","");
	form.attr("method","post");
	form.attr("action","../unionPayConsume");
	$("body").append(form);//将表单放置在web中
	$.each(param,function(key,value){
		var input1=$("<input>");
		input1.attr("type","hidden");
		input1.attr("name",key);
		input1.attr("value",value);
		form.append(input1);
	})
	form.submit();//表单提交 
	form.remove();
	/*$.ajax({
		type: "GET",
        url: "../unionPayConsume",
        dataType: "JSON",
        async:false,
        data: {
        	"orderId":orderId,
        	"txnTime":txnTime,
        	"txnAmt":txnAmt,
        	},
        success: function(data){
        	if(data.status == 0){
        		alertMsg("2","保存成功！","success");
        		setTimeout("location.reload()",2000)
        	}else if(data.status == 1){
        		alertMsg("2",data.errmsg,"fail")
        	}
        },
        error:function(data){
        	console.log(data)
        }
	})*/
}

function paydownline(){
	var orderId=$("#ordernum").text();
	var txnTime=$("#curTime").text();
	var txnAmt=$("#txnAmt").val();
	var signuid=sessionStorage.getItem("uid");
	var mid=sessionStorage.getItem("currentmid");
	if(txnAmt.trim()==""){
		alertMsg("1","请填写订单金额！","fail");
		return;
	}
	$.ajax({
		type: "GET",
        url: "../cashPayConsume",
        dataType: "JSON",
        async:false,
        data: {
        	"orderId":orderId,
        	"txnTime":orderId.slice(0,14),
        	"txnAmt":parseInt(txnAmt*100),
        	"signuid":signuid,
        	"mid":mid
        	},
        success: function(data){
        	if(data.status == 0){
        		alertMsg("1","操作成功！","success");
        		setTimeout("window.open('../jsp/payRelevant.html','_self')",1000)
        	}else if(data.status == 1){
        		alertMsg("1",data.errmsg,"fail")
        	}
        },
        error:function(data){
        	console.log(data)
        }
	})
}

function paybyvoucher(){
	var orderId=$("#ordernum").text();
	var txnTime=$("#curTime").text();
	var txnAmt=$("#txnAmt").val();
	var signuid=sessionStorage.getItem("uid");
	var paymenturl=$(".hideurl").val();
	var mid=sessionStorage.getItem("currentmid");
	if(txnAmt.trim()==""){
		alertMsg("1","请填写订单金额！","fail");
		return;
	}
	if(paymenturl==""){
		alertMsg("1","请上传缴费凭证！","fail");
		return;
	}
	$.ajax({
		type: "GET",
        url: "../proofPayConsume",
        dataType: "JSON",
        async:false,
        data: {
        	"orderId":orderId,
			"txnTime":orderId.slice(0,14),
			"txnAmt":parseInt(txnAmt*100),
        	"signuid":signuid,
        	"paymenturl":paymenturl,
        	"mid":mid        	
        	},
        success: function(data){
        	if(data.status == 0){
        		alertMsg("1","操作成功！","success");
        		setTimeout("window.open('../jsp/payRelevant.html','_self')",1000)
        	}else if(data.status == 1){
        		alertMsg("1",data.errmsg,"fail")
        	}
        },
        error:function(data){
        	console.log(data)
        }
	})
}
function uploadImg(){
	var filetype=$("#file").val().slice($("#file").val().lastIndexOf(".")+1).toUpperCase();
	if(filetype!=""){
		if (filetype == 'JPG'||filetype == 'PNG'||filetype == 'JPEG'){
			$("#myform").ajaxSubmit({
				url : "../uploadFiles",
				dataType : 'json',
				async : false,
				success : function(data) {
					if(data.status==0){
						readAsDataURL(data.urls);
					}else if(data.status==1){
						alertMsg("1",data.errmsg,"fail")
					}
				},
				error:function(){
					alertMsg("1","上传失败！","fail")
				}
		    })
		}else{
			alertMsg("1","文件格式应为JPG或PNG！","fail")
			return
		}
	}
}

//图片预览
function readAsDataURL(url){
  var simpleFile = document.getElementById("file").files[0];
  var reader = new FileReader();
  // 将文件以Data URL形式进行读入页面
  reader.readAsDataURL(simpleFile);
  reader.onload = function(e){
      var htmls="";
      htmls+='<li><img src="'+this.result+'">'
      		+'<a class="fork" onclick="delImg(this)">删除</a>'
      		+'<input hidden value="'+url+'" class="hideurl"></li>';
      $(".photoul").html(htmls);
  }
}

function delImg(obj){
	$(obj).parent().remove();
}