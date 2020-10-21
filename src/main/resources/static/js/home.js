/*加载显示区域的内容*/
function LoadAjaxContent(url,parameter,method){
    // 移除style属性，显示第二个 > 和 三级目录标题
    $('#home_third_menu').removeAttr('style');
    $('.c-gray').eq(1).removeAttr('style');
    window.location.hash = url;
    $('#home_third_menu').html(getCookie("third_menu"));
    $('#home_second_menu').html(getCookie("second_menu"));
    $('.loading').show();
    loadHome($.ajaxGetHtml(url,parameter,method));
}
/**
 * 请求html信息
 *
 * */
$.ajaxGetHtml = function (url, parameter, method) {
    var param = parameter || {};
    param["v_timestamp"] = new Date().getTime();
    var html,data={};
    method = method || "get";
    if(method === "get"){
        for (var item in param) {
            //console.log(parameter[item]);
            url = changeURLPar(url, item, param[item]);
        }
    }else{
        data=parameter;
    }
    $.ajax({
        type: method,
        url: url,
        dataType: "html",
        data: data,
        async: false,
        global: true,
        success: function (data, textStatus) {
            if(isJSON(data)){
                var dataObj = JSON.parse(data)
                if(dataObj.status!=="200"){
                    html = '<p style="text-align: center">'+dataObj.message+'</p>';
                }
            } else{
                html = data;
            }

        },
        error: function (HttpRequest, textStatus, errorThrown) {
            $(this).ajaxError(HttpRequest, textStatus, errorThrown);
        }
    });
    return html;
};

//改变地址栏参数
var changeURLPar = function (destiny, par, par_value) {
    var pattern = par + '=([^&]*)';
    var replaceText = par + '=' + par_value;
    if (destiny.match(pattern)) {
        var tmp = '/\\' + par + '=[^&]*/';
        tmp = tmp.replace(/\\/, "");
        tmp = destiny.replace(eval(tmp), replaceText);
        return (tmp);
    }
    else {
        if (destiny.match('[\?]')) {
            return destiny + '&' + replaceText;
        }
        else {
            return destiny + '?' + replaceText;
        }
    }
    return destiny + '\n' + par + '\n' + par_value;
}
/**
 *  判断是否为json字符串
 * @param str
 * @returns {boolean}
 */
function isJSON(str) {
    if (typeof str == 'string') {
        try {
            JSON.parse(str);
            return true;
        } catch (e) {
            return false;
        }
    }
}
/*设置cookie*/
function setCookie(name, value, Days){
    if(Days == null || Days == ''){
        Days = 300;
    }
    var exp  = new Date();
    exp.setTime(exp.getTime() + Days*24*60*60*1000);
    document.cookie = name + "="+ escape (value) + "; path=/;expires=" + exp.toGMTString();
}
/*获取cookie*/
function getCookie(name) {
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
    if(arr=document.cookie.match(reg))
        return unescape(arr[2]);
    else
        return null;
}
//把内容加载到中央显示区域
function loadHome(content){
    var maindiv = $('#ajax-main-content');
    maindiv.html(content);
    maindiv.parseHtmlEvent();
    maindiv.find("[data-content]").each(function(){
        var ops=$(this).data("options")||"";
        $.ajaxcontent.methods[$(this).data("content")].call(this,$(this),$.parseQuoteOptions(ops));
    });
    //初始化layer组件
    maindiv.find("[data-layer]").each(function () {
        var ops = $(this).data("options") || "";
        //沿用layerOpen初始方式
        $.layerOpen.methods[$(this).data("layer")].call(this, $(this),
            $.parseQuoteOptions(ops));
    });
}


// 解析页面组件，并给其添加事件,如datagrid、form
$.fn.parseHtmlEvent = function (data) {
    var html = $(this);

    if (html.find('#login-sign').length > 0) {
        parent.location.href = "/admin/login";
    }

    //初始化Datagrid
    html.find(".datagrid").each(function (i, item) {
        if ($(this).data("options")) {
            $(this).grid($.parseDateOptions($(this).data("options")));
        } else {
            $(this).grid();
        }
    });

    //初始化日历
    html.find(".date-widget").click(function () {
        var opt = {};
        if ($(this).data("options")) {
            opt = $.parseDateOptions($(this).data("options"));
            WdatePicker(opt);
        }
    });
//	    html.find(".date-widget").each(function (i, item) {
//	    	$(this).click(function(){
//	    		var opt = {};
//	    		if($(this).data("options"))	//dateFmt:'yyyy-MM'   日期格式
//	    			opt = $.parseDateOptions($(this).data("options"));
//	    		WdatePicker(opt);
//	    	});
//	    });

    //一般的form提交
    html.find(".general-from").each(function () {
        //e.preventDefault();
        $(this).validate({
            submitHandler: function (form) {
                $(form).ajaxSubmit(function (res) {
                    console.log(res);
                    if (res.url !== "") {
                        if (res.success) {
                            // window.location.href=window.location.href.split("#")[0]+"#"+res.url;
                            LoadAjaxContent(res.url,res.data,res.method);
                        } else {
                            layer.msg(res.message);
                        }
                    } else {
                        loadHome(res);
                    }

                });
            }
        });
    });
    //表单验证	errorpop 弹出类型 msg:提示 open:弹出框提示
    html.find(".validform").each(function () {
        var ops = {
            errorpop: 'msg',  //显示类型 msg:提示   confirm:询问框  open:捕获页
            width: '420px',
            height: '240px'
        };
        if ($(this).data("options")) {
            var opt = $.parseDateOptions($(this).data("options"));
            ops = $.extend({}, ops, opt || {});
        }
        //给必填项加星
        $(this).find('[required]').each(function () {
            var parent;
            if ($(this).is("select")){
                parent = $(this).parent("span").parent("div").next("div")
            }else{
                parent = $(this).parent("div").next("div");
            }
            if(parent.find('.Validform_checktip').length <= 0){
                parent.append('<label class="Validform_checktip validform_star">*</label>');
            }
        });
        $(this).validate({
            submitHandler: function (form) {
                $(form).find(":submit").attr("disabled", true);
                var index = layer.load(0, {shade: false});  //加载进度条
                $(form).ajaxSubmit(function (res) {
                    layer.close(index);
                    if (res.success) {
                        layer.msg("保存成功", $.layer('close'));
                        if (typeof(data) !== "undefined" && typeof(data.callback)!== "undefined") //界面关闭时回调
                        {
                            data.callback();
                        }
                        //用户redis过期,ajax请求gate网关后,返回的是/admin/login对应的页面源码
                    }else if(res.success == undefined && res.indexOf("<!DOCTYPE") != -1) {
                        location.href="/admin/login";
                    }else {
                        if (ops.errorpop === 'msg') {
                            layer.msg(res.message);
                        } else if (ops.errorpop === 'open') {
                            layer.open({
                                type: 1,
                                skin: 'layui-layer-rim', //加上边框
                                area: [ops.width, ops.height], //宽高
                                content: res.message
                            });
                        }
                        $(form).find(":submit").attr("disabled", false);
                    }
                });
            },
            errorPlacement: function (error, element) {
                element.addClass("Validform_error");
                element.parent("div").next("div").html(error);
                element.parent("div").next("div").find(".error").addClass(
                    "Validform_checktip Validform_wrong");
            }
        })
    });

    //倒计时
    var time = $("#timeCountDown");
    if (time.length) {
        var t = time.data("time"), date = new Date(t);
        var d = Date.UTC(date.getFullYear(), date.getMonth(), date.getDate(),
            date.getHours(), date.getMinutes(), date.getSeconds()),
            obj = {
                sec: time.find("em:last"),
                mini: time.find("em:first")
            };
        fnTimeCountDown(d, obj);
    }

    /*弹出框关闭*/
    html.find(".layer-close").on('click', function (e) {
        $(".layer").hide();
        // 寻找外层包裹上的id，传入close方法
        var layerWrap = $(this).parents('.layui-layer-page');
        var index = +layerWrap.attr('times');
        layer.close(index);
    });

    html.find(".upload").each(function(){
        var ops=$(this).data("options");
        $(this).powerWebUpload($.parseQuoteOptions(ops));
    });

    //页面弹出框
   html.find("[data-toggle='dialog']").click(function(){
       var ops=$(this).data("options")||"";
       $.layer('open', {'title':ops.title, 'url':ops.url, 'width':600, 'height':400, 'callback': function () {
           ops.callback();
       }});
   });
};