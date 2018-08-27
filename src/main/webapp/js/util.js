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
                	alert("djkadjajhd");                
                    break;
                default:
                    layer.alert("系统出错,请联系管理员", {offset:['200px']});
                	console.log(xhr.responseText);
                	break;
            }
        }
    );
});