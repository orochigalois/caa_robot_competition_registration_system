//超时登陆
$(function(){ 
    //.ajaxError事件定位到document对象，文档内所有元素发生ajax请求异常，都将冒泡到document对象的ajaxError事件执行处理
    $(document).ajaxError(
        //所有ajax请求异常的统一处理函数，处理
        function(event,xhr,options,exc ){
            if(xhr.status == 'undefined'){
                return;
            }
            switch(xhr.status){

                case 451:
                	alertMsg("1","登录超时！请重新登录","fail"); 
                	setTimeout('window.open("../login.html","_self")',2000)
                    break;
                default:
                    
                	console.log(xhr.responseText);
                	break;
            }
        }
    );
});

var tempobj="";

function alertMsg(cover,text,icon){
	var htmls="";
	if(cover=="1"){
		htmls+='<div class="cover1"></div><div class="msgWin1">'
	}else if(cover=="99"){
		htmls+='<div class="cover1"></div><div class="msgWin1">'
		htmls+='<img class="closeWin" src="../images/guanbi.png" onclick="closeWin()">'
	    		+'<img src="images/'+icon+'.png" class="face">'
	    		+'<h1 class="wTitle">'+text+'</h1>'
	    		+'<a class="known" onclick="closeWin()">知道了</a></div>';
	    $("body").append(htmls);
	    return;
	}else{
		htmls+='<div class="cover2"></div><div class="msgWin2">'
	}
	htmls+='<img class="closeWin" src="../images/guanbi.png" onclick="closeWin()">'
    		+'<img src="../images/'+icon+'.png" class="face">'
    		+'<h1 class="wTitle">'+text+'</h1>'
    		+'<a class="known" onclick="closeWin()">知道了</a></div>';
    $("body").append(htmls);
}

function closeWin(){
	$(".msgWin1").remove();
	$(".msgWin2").remove();
	$(".cover1").remove();
	$(".cover2").remove();
}

function alertConfirm(cover,text,fun,obj){
	tempobj=obj;
	var htmls="";
	if(cover=="1"){
		htmls+='<div class="cover1"></div><div class="msgWin1">'
	}else{
		htmls+='<div class="cover2"></div><div class="msgWin2">'
	}
	htmls+='<img class="closeWin" src="../images/guanbi.png" onclick="closeWin()">'
    		+'<h1 class="wTitle">提示</h1>'
    		+'<div class="question">'+text+'</div>'
    		+'<div class="confirmBtn"><a class="conBtn1" onclick="'+fun+'">确定</a>'
    		+'<a class="conBtn2" onclick="closeWin()">取消</a></div></div>';
    $("body").append(htmls);
}

function alertConfirm2(cover,text,fun){
	tempobj=obj;
	var htmls="";
	if(cover=="1"){
		htmls+='<div class="cover1"></div><div class="msgWin1">'
	}else{
		htmls+='<div class="cover2"></div><div class="msgWin2">'
	}
	htmls+='<img class="closeWin" src="../images/guanbi.png" onclick="closeWin()">'
    		+'<h1 class="wTitle">提示</h1>'
    		+'<div class="question">'+text+'</div>'
    		+'<div class="confirmBtn"><a class="conBtn1" onclick="'+fun+'">确定</a>'
    		+'<a class="conBtn2" onclick="closeWin()">取消</a></div></div>';
    $("body").append(htmls);
}

$(function(){
	//回显顶部信息
	findUserHead();
	
	//退出登录
	logout();
	
})

var uid = sessionStorage.getItem("uid");
//回显顶部信息
function findUserHead(){
	
	$.ajax({
		type: "GET",
        url: "../findLoginUser",
        dataType: "JSON",
        async:false,
        data: {"uid":uid},
        success:function(data){
        	$("#avatar").attr("src",data.user.picurl);
        	$("#username").text(data.user.uname);
        }
	})
}

//退出登录
function logout(){
	$(".logout").click(function(){
		$.ajax({
			type: "GET",
			url: "../logout",
			dataType: "JSON",
			async:false,
			data:{},
			success:function(data){
	        	window.location.href = "../login.html";
	        	$(".navright").addClass("hidenavright");
	        	if(($("#autologin").prop("checked"))){
	        		$("#uname").val("");
	        		$("#pwd").val("");
	        	}
	        	sessionStorage.clear()
	        },
	        error:function(data){
	        	      	
	        }
		})
	})
}

function getcurmatchname(){
	var uid=sessionStorage.getItem("uid");
	$.ajax({
		type: "GET",
        url: "../findMatchByUid",
        dataType: "JSON",
        async:false,
        data: {
        	"uid":uid,
        	},
        success: function(data){
        	if(data.status == 0){
        		if(sessionStorage.getItem("currentmid")==undefined){
        			$(".currentmatch").text(data.list[0].mname);
        			sessionStorage.setItem("currentmid",data.list[0].mid)
        			sessionStorage.setItem("enddate",data.list[0].signenddate)
        		}else{
        			$.each(data.list,function(i,match){
        				if(match.mid==sessionStorage.getItem("currentmid")){
        					$(".currentmatch").text(match.mname);
        					sessionStorage.setItem("enddate",match.signenddate)
        				}
        			})
        		}
        	}else if(data.status == 1){
        		alertMsg("1",data.errmsg,"fail")
        	}
        },
	})
}

function selManageMatch(){
	var htmls="";
	htmls+='<div class="shade"></div><div class="mymatchlist">'
		+'<h1 class="wTitle">选择赛事</h1><ul class="mymatchlistul">';
	var uid=sessionStorage.getItem("uid");
		$.ajax({
			type: "GET",
	        url: "../findMatchByUid",
	        dataType: "JSON",
	        async:false,
	        data: {
	        	"uid":uid,
	        	},
	        success: function(data){
	        	if(data.status == 0){
	        		$.each(data.list,function(i,match){
	        			htmls+='<li><input type="radio" name="match" value="'
	        			+match.mid+'" ';
	        			if(i==0){
	        				htmls+="checked";
	        			}
	        			htmls+='>'+match.mname+'</li>'
	        		})
	        	}else if(data.status == 1){
	        		alertMsg("1",data.errmsg,"fail")
	        	}
	        },
		})
		
	htmls+='</ul><div class="btnwrap">'
		 +'<span style="background:#e21230;margin-right:20px" onclick="setcurmatch()">确定</span>'
		 +'<span style="color:#333;border:1px solid #ccc;" onclick="closeselmanage()">取消</span>'
	     +'</div></div>'
	$("body").append(htmls)
}

function closeselmanage(){
	$(".shade,.mymatchlist").remove();
}

function bmsgetcurmatchname(){
	$.ajax({
		type: "GET",
        url: "../findAllMatchList",
        dataType: "JSON",
        async:false,
        data: {
        	
        	},
        success: function(data){
        	if(data.status == 0){
        		if(sessionStorage.getItem("currentmid")==undefined){
        			$(".currentmatch").text(data.list[0].mname);
        			sessionStorage.setItem("currentmid",data.list[0].mid)
        		}else{
        			$.each(data.list,function(i,match){
        				if(match.mid==sessionStorage.getItem("currentmid")){
        					$(".currentmatch").text(match.mname);
        					sessionStorage.setItem("enddate",match.signenddate)
        				}
        			})
        		}
        	}else if(data.status == 1){
        		alertMsg("1",data.errmsg,"fail")
        	}
        },
	})
}

function setDefaultMatch(mid){
	sessionStorage.setItem("currentmid",mid);
	alertMsg("2","操作成功","success")
}

function IdentityCodeValid(code) { 
    var city={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江 ",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北 ",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏 ",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外 "};
    var tip = "";
    var pass= true;
    
    if(!code || !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(code)){
        tip = "身份证号格式错误";
        pass = false;
    }
    
   else if(!city[code.substr(0,2)]){
        tip = "身份证号格式错误";//tip = "地址编码错误";
        pass = false;
    }
    else{
        //18位身份证需要验证最后一位校验位
        if(code.length == 18){
            code = code.split('');
            //∑(ai×Wi)(mod 11)
            //加权因子
            var factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ];
            //校验位
            var parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 ];
            var sum = 0;
            var ai = 0;
            var wi = 0;
            for (var i = 0; i < 17; i++)
            {
                ai = code[i];
                wi = factor[i];
                sum += ai * wi;
            }
            var last = parity[sum % 11];
            if(parity[sum % 11] != code[17]){
                tip = "身份证号格式错误";//tip = "校验位错误";
                pass =false;
            }
        }
    }
    return pass;
}
//以上是身份证验证代码
