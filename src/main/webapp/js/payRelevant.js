$(function(){
	getcurmatchname();
	getAllFeestatus();
	getAllMemFeestatus();
	getAllorder()
})
var signuid=sessionStorage.getItem("uid");
function getAllFeestatus(){
	var mid=sessionStorage.getItem("currentmid");
	$.ajax({
		type: "GET",
        url: "../findTotalCostByMidSiginuid",
        dataType: "JSON",
        async:false,
        data: {
        	"signuid":signuid,
        	"mid":mid,
        	},
        success: function(data){
        	if(data.status == 0){
        		console.log(data);
        		$("#yingjje").text(data.yingjje/100);
        		$("#yizhifuconfirmje").text(data.yizhifuconfirmje/100);
        		$("#yizhifunotconfirmje").text(data.yizhifunotconfirmje/100);
        		$("#daijje").text(data.daijje/100);
        		if(data.yingjje==0){
        			$(".pay").hide();
        		}
        	}else if(data.status == 1){
        		alertMsg("1",data.errmsg,"fail")
        	}
        },
	})
}

function getAllMemFeestatus(){
	var mid=sessionStorage.getItem("currentmid");
	$.ajax({
		type: "GET",
        url: "../findMemberRaceListByMidSiginuid",
        dataType: "JSON",
        async:false,
        data: {
        	"signuid":signuid,
        	"mid":mid,
        	},
        success: function(data){
        	if(data.status == 0){
        		$(".memfeestatus tbody").empty();
        		var htmls="";
        		$.each(data.list,function(i,item){
        			htmls+='<tr><td>'+item.tmname+'</td>';
        			if(item.roleflg=="01"){
        				htmls+='<td>教师</td>'
        			}else{
        				htmls+='<td>学生</td>'
        			}
        			htmls+='<td>'+item.unitprice/100+'</td>'
        				 +'<td>'+item.frequency+'</td></tr>'
        		})
        		$(".memfeestatus tbody").append(htmls)
        		/*alertMsg("2","创建成功！","success")
        		setTimeout('history.back()',2000)*/
        	}else if(data.status == 1){
        		alertMsg("1",data.errmsg,"fail")
        	}
        },
	})
}

function skipPage(){
	window.open("paymentway.html","_self")
}

function getAllorder(){
	var mid=sessionStorage.getItem("currentmid");
	$.ajax({
		type: "GET",
        url: "../findOrderListByMidSiginuid",
        dataType: "JSON",
        async:false,
        data: {
        	"signuid":signuid,
        	"mid":mid,
        	},
        success: function(data){
        	if(data.status == 0){
        		$(".orderlist tbody").empty();
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
        					 +' paymenturl="'+item.paymenturl+'" '
        					 +' onclick="viewFile(this)">查看缴费凭证</a></td>'
        			}
        			htmls+='<td>'+item.txnamt/100+'</td>'
        			if(item.txnstatus=="00"){
        				htmls+='<td>已支付确认</td>'
        			}else if(item.txnstatus=="01"){
        				htmls+='<td>待支付确认</td>'
        			}else if(item.txnstatus=="02"){
        				htmls+='<td>废除</td>'
        			}else if(item.txnstatus=="03"){
        				htmls+='<td>已开发票</td>'
        			}else{
        				htmls+='<td></td>'
        			}
        			htmls+='</tr>'
        		})
        		$(".orderlist tbody").append(htmls)
        	}else if(data.status == 1){
        		alertMsg("1",data.errmsg,"fail")
        	}
        },
	})
}

function viewFile(obj){
	var paymenturl=$(obj).attr("paymenturl");
	$("#popup img").attr("src",paymenturl);
	$("#popup").css("display","block");
	$("#popup").click(function(){
		$("#popup").css("display","none");
	});
}
function setcurmatch(){
	var mid=$("[name=match]:checked").val();
	sessionStorage.setItem("currentmid",mid);
	$(".shade,.mymatchlist").remove();
	getcurmatchname();
	getAllFeestatus();
	getAllMemFeestatus();
	getAllorder()
}
