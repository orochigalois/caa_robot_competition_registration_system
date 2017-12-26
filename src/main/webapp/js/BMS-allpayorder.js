$(function(){
	bmsgetcurmatchname();
	var jsonobj=JSON.parse(sessionStorage.getItem("gradelistJson"));
	var num="";
	if(jsonobj!=undefined){
		num=jsonobj.pageNo;
		$("#orderid").val(jsonobj.orderid);
		$("#txntype").attr("altvalue",jsonobj.txntype);
		if(jsonobj.txntype!=""){
			$("#txntype").val($("#txntype").next().find("[altvalue="+jsonobj.txntype+"]").text());
		}
		var txnstatus=$("#txnstatus").attr("altvalue",jsonobj.txnstatus);
		if(jsonobj.txnstatus!=""){
			$("#txnstatus").val($("#txnstatus").next().find("[altvalue="+jsonobj.txnstatus+"]").text());
		}
	}else{
		num=0
	}
	getallorder('',num);
	$(".selinput").click(function(e){
     	e.stopPropagation();
     	$(".emulate").hide();
     	var me=this;
     	$(me).next().show();
     	$(me).next().children().click(function(){
     		$(me).val($(this).text());
     		$(me).attr("altvalue",$(this).attr("altvalue"));
     		$(me).next().hide();
     	})
     })
    $(document).click(function(){
    	$(".emulate").hide();
    })
})

function getallorder(signuid,num){
	var pageSize=10;
	var mid=sessionStorage.getItem("currentmid");
	var orderid=$("#orderid").val();
	var txntype=$("#txntype").attr("altvalue");
	var txnstatus=$("#txnstatus").attr("altvalue");
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
			"url" : "../signuidPayListByMid",
			pageParams : function(data) {
				var param={};
				param.pageNo=data.pageIndex;
				param.orderid=orderid;
				param.txntype=txntype;
				param.txnstatus=txnstatus;
				sessionStorage.setItem("orderlistJson", JSON.stringify(param));
				return {
					iDisplayStart : data.pageIndex * data.pageSize,
					iDisplayLength : data.pageSize,
					"orderid":orderid,
					"txntype":txntype,
					"txnstatus":txnstatus,
					"signuid":signuid,
					"mid":mid
				};
			}, // [Function]:自定义请求参数
			"success" : function(data, status) {
				if (data.status == 0) {
					$("tbody").empty();
					var htmls="";
					$.each(data.list,function(i,item){
						htmls+='<tr><td>'+item.orderid+'</td>'
		       				 +'<td>'+item.txntime.slice(0,4)+'/'+item.txntime.slice(4,6)+'/'
		       				 +item.txntime.slice(6,8)+'/'+item.txntime.slice(8,10)+':'
		       				 +item.txntime.slice(10,12)+':'+item.txntime.slice(12)
		       				 +'</td>';
			       			 if(item.txntype=="00"){
			       				htmls+='<td>线下(现金)</td>'
			       			 }else if(item.txntype=="01"){
			       				htmls+='<td>线上(银联)</td>'
			       			 }else if(item.txntype=="02"){
			       				htmls+='<td>其它(凭证)<a class="viewfile" '
			       					+'paymenturl="'+item.paymenturl+'" '
			       					+'onclick="viewFile(this)">查看缴费凭证</a></td>'
			       			 }
			       			 htmls+='<td>'+item.txnamt/100+'</td>'
			       			 htmls+='<td>'+item.uname+'</td>'
			       			 htmls+='<td><p class="contactway">'+(item.email==null?'':item.email)+'</p>'
			       			 htmls+='<p class="contactway">'+(item.phone==null?'':item.phone)+'</p></td>'
			       			 /*htmls+='<td>'+(item.school==null?'':item.school)+'</td>'*/
			       			 if(item.txnstatus=="00"){
			       				htmls+='<td>已支付确认</td>'
			       			 }else if(item.txnstatus=="01"){
			       				htmls+='<td>待支付确认</td>'
			       			 }else if(item.txnstatus=="02"){
			       				htmls+='<td>废除</td>'
			       			 }
			       			 if(item.txnstatus=="01"){
			       				htmls+='<td signuid="'+item.signuid+'" orderid="'+item.orderid+'">'
								+'<a class="modify" onclick="getallorder(\''+item.signuid+'\',0)">查看此人所有订单</a>'
								+'<span class="split">|</span>'
								+'<a class="delete" '
								+'onclick="';
				       			if(item.txntype=="00"){
				       				htmls+='confirmdownline(this)'
				       			}else if(item.txntype=="02"){
				       				htmls+='vouchermoney(this)'
				       			}
				       			htmls+='">确认收款</a>'
								+'<span class="split">|</span>'
								+'<a class="delete" '
								+'onclick="alertConfirm(\'2\',\'确认废除该订单？\',\'';
				       			if(item.txntype=="00"){
				       				htmls+='confirmdownline()'
				       			}else if(item.txntype=="02"){
				       				htmls+='abolishvoucher()'
				       			}
				       			htmls+='\',this)">订单废除</a>'+'</td></tr>'
			       			 }else{
			       				htmls+='<td signuid="'+item.signuid+'" orderid="'+item.orderid+'">'
								+'<a class="modify" onclick="getallorder(\''+item.signuid+'\',0)">查看此人所有订单</a>'
								+'<span class="split">|</span>'
								+'<span class="delete2">确认收款</span>'
								+'<span class="split">|</span>'
								+'<span class="delete2">订单废除</span>'+'</td></tr>'
			       			 }
			       			
							
					})
					$("tbody").append(htmls)
				} else if(data.status==1){
					alertMsg("2",data.errmsg,"fail")
				}
			},
			"error" : function(data) {
				alertMsg("2","后台获取数据出错","fail")
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

function clearSearch(){
	$(".searchul input").each(function(){
		$(this).val("");
		if($(this).attr("altvalue")!=null){
			$(this).attr("altvalue","")
		}
	})
}
var enddate="";
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
        		alertMsg("2",data.errmsg,"fail")
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

function viewFile(obj){
	var paymenturl=$(obj).attr("paymenturl");
	$("#popup img").attr("src",paymenturl);
	$("#popup").css("display","block");
	$("#popup").click(function(){
		$("#popup").css("display","none");
	});
}


function closeInfoWin(){
	$(".shade").remove();
	$(".photowall").remove();
	$(".vouchermoney").remove();
}

function confirmdownline(obj){
	if(obj==undefined){
		obj=tempobj;
		var flg="02";
	}else{
		var flg="00"
	}
	var mid=sessionStorage.getItem("currentmid");
	var orderid=$(obj).parent().attr("orderid");
	var signuid=$(obj).parent().attr("signuid");
	$.ajax({
		type: "GET",
        url: "../cashPayRcv",
        dataType: "JSON",
        async:false,
        data: {
        	"mid":mid,
        	"orderId":orderid,
        	"signuid":signuid,
        	"txnstatus":flg
        },
        success: function(data){
        	if(data.status == 0){
        		alertMsg("2","操作成功！","success")
        		setTimeout("location.reload()",1000)
        	}else if(data.status == 1){
        		alertMsg("2",data.errmsg,"fail")
        	}
        },
	})
}
function vouchermoney(obj){
	var orderid=$(obj).parent().attr("orderid");
	var signuid=$(obj).parent().attr("signuid");
	var htmls='';
	htmls+='<div class="shade"></div><div class="vouchermoney">'
		 +'<h1 class="wTitle">凭证支付</h1>'
		 +'交易金额：<input type="text" id="txnamt">'
		 +'<div class="btnwrap">'
		 +'<span style="background:#e21230;margin-right:20px" '
		 +'onclick="confirmvoucher(\''+orderid+'\',\''+signuid+'\')">确认</span>'
		 +'<span style="color:#333;border:1px solid #ccc;" onclick="closeInfoWin()">取消</span>'
		 +'</div>'
		 +'</div>'
	$("body").append(htmls)
}
function confirmvoucher(orderid,signuid){
	var txnamt=$("#txnamt").val();
	if(txnAmt.trim()==""){
		alertMsg("2","请填写交易金额！","fail");
		return;
	}
	var mid=sessionStorage.getItem("currentmid");
	$.ajax({
		type: "GET",
        url: "../proofPayRcv",
        dataType: "JSON",
        async:false,
        data: {
        	"orderId":orderid,
        	"signuid":signuid,
        	"txnstatus":"00",
        	"txnamt":parseInt(txnamt*100),
        	"mid":mid,
        },
        success: function(data){
        	if(data.status == 0){
        		closeInfoWin();
        		alertMsg("2","操作成功！","success")
        		setTimeout("location.reload()",1000)
        	}else if(data.status == 1){
        		closeInfoWin();
        		alertMsg("2",data.errmsg,"fail")
        	}
        },
	})
}

function abolishvoucher(){
	var orderid=$(tempobj).parent().attr("orderid");
	var signuid=$(tempobj).parent().attr("signuid");
	var mid=sessionStorage.getItem("currentmid");
	$.ajax({
		type: "GET",
        url: "../cashPayRcv",
        dataType: "JSON",
        async:false,
        data: {
        	"orderId":orderid,
        	"signuid":signuid,
        	"txnstatus":"02",
        	"txnamt":0,
        	"mid":mid
        },
        success: function(data){
        	if(data.status == 0){
        		alertMsg("2","操作成功！","success")
        		setTimeout("location.reload()",1000)
        	}else if(data.status == 1){
        		alertMsg("2",data.errmsg,"fail")
        	}
        },
	})
}