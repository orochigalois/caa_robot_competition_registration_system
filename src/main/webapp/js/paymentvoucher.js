$(function(){
	getAllFile();
	$(document).on("click",".photoul li img",function(){
		$("#popup img").attr("src",$(this)[0].src);
		$("#popup").css("display","block");
	});
	$("#popup").click(function(){
		$("#popup").css("display","none");
	});
})
var uid=sessionStorage.getItem("uid");
function getAllFile(){
	$.ajax({
		type: "GET",
        url: "../findLoginUser",
        dataType: "JSON",
        async:false,
        data: {"uid":uid},
        success:function(data){
        	$(".photoul").empty();
        	var htmls="";
        	var paymenturl =data.user.paymenturl;
        	if(paymenturl!=""){
        		$(paymenturl.split(",")).each(function(i,value){
            		htmls+='<li><img src="'+value+'">'
              		+'<a class="fork" onclick="delImg(this)">删除</a>'
              		+'<input hidden value="'+value+'" class="hideurl"></li>';
            	})
            	$(".photoul").append(htmls);
        	}
        	
        }
	})
}
function uploadFile(){
	var paymenturl=[];
	$(".hideurl").each(function(){
		paymenturl.push($(this).val())
	})
	$.ajax({
		type: "GET",
        url: "../updateLoginUser",
        dataType: "JSON",
        async:false,
        data: {
        	"uid":uid,
        	"paymenturl":paymenturl.join(",")
        },
        success: function(data){
        	if(data.status == 0){
        		alertMsg("1","上传成功！","success")
        		setTimeout("location.reload()",2000)
        	}else if(data.status == 1){
        		alertMsg("1",data.errmsg,"fail")
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
      $(".photoul").append(htmls);
  }
}

function delImg(obj){
	$(obj).parent().remove();
}

function showfork(obj){
	$(obj).find(".fork").show();
}

function hidefork(obj){
	$(obj).find(".fork").hide();
}