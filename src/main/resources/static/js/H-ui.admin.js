
//主题页面加载
$.ajaxcontent={
	addMethod:function(name, method){
		$.ajaxcontent.methods[ name ] = method;
	},
	methods:{
	}
	}

//日历插件
jQuery.ajaxcontent.addMethod("calendar",function(evl,ops){
	console.log(1);
});

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

/*获得皮肤缓存的值*/
function getskincookie(){
	var v = getCookie("Huiskin");
	if(v==null||v==""){
		v="default";
	}
	var skinlink = $("#skin").attr("href");
	$("#skin").attr("href", skinlink.replace(getSkin(), v));
}

/*获得当前皮肤*/
function getSkin(){
	$(window.frames.document).contents().find("#skin").attr("href");
	var start = $("#skin").attr("href").indexOf("skin") + 5;
	var end = $("#skin").attr("href").indexOf("skin.css") - 1;
	skin = $("#skin").attr("href").substring(start, end);
	return skin;
}

/*解析地址栏中的地址并加载*/
function parseAjaxUrl(){
	//解析当前地址栏的地址，使之可以加载
	var ajax_url = location.hash.replace(/^#/, '');
	if (ajax_url.length < 1) {
		ajax_url = 'admin/welcome';
	} 
	LoadAjaxContent(ajax_url);
}

jQuery.extend({
	//解析data-options的内容使之生成对象  将#号转换为双引号
	parseDateOptions:function(opt){
		var re = new RegExp("#", "g");
		opt = opt.replace(re, '"')
		if(opt.substring(0,1) != '{'){
			opt = '{' + opt + '}';
		}
		var opt = (new Function('return' + opt))();
		return $.extend({},opt);
	},
	//解析data-options的内容使之生成对象  将%号转换为单引号
	parseQuoteOptions:function(opt){
		var re = new RegExp("%", "g");
		opt = opt.replace(re, "'")
		if(opt.substring(0,1) != '{'){
			opt = '{' + opt + '}';
		}
		var opt = (new Function('return' + opt))();
		return $.extend({},opt);
	}
});

$(function(){
	if(window.location.hash){
		// meunList(window.location.hash);
	}else{
		// meunList("",0);
	}
	
	
	//导航
	 $("#menu").on("click","a",function(){
		 var index=$(this).parent().index();
		 // meunList("",index);
	 });
	 //$("#menu").find("a:first").click();
	//设置当前的皮肤
	getskincookie();
	//layer.config({extend: 'extend/layer.ext.js'});
	/*左侧菜单响应式*/
	if($(window).width()>=768){
		$(".Hui-aside").show()
	}
	
	/*左侧菜单*/
	//$.Huifold(".menu_dropdown dl dt",".menu_dropdown dl dd","fast",1,"click");
	
	/*选项卡导航*/
	$(".Hui-aside").on("click",".menu_dropdown a",function(){
		if($(this).attr('_href')){
			var bStop=false;
			var bStopIndex=0;
			var _href=$(this).attr('_href');
			var _titleName=$(this).attr("data-title");
			var show_navLi=$(document).find("#min_title_list li");
			show_navLi.each(function() {
				if($(this).find('span').attr("data-href")==_href){
					bStop=true;
					bStopIndex=show_navLi.index($(this));
					return false;
				}
			});
			
			if(bStop){
				show_navLi.removeClass("active").eq(bStopIndex).addClass("active");
			}
			var _href=$(this).attr('_href');
			setCookie("third_menu", $(this).attr("data-title"));
			setCookie("second_menu", $(this).parents("dd").attr("data-title"));
			if(window.location.hash=="#"+_href){
				LoadAjaxContent(_href);
			}else{
				window.location.hash = _href;
			}
			
			//LoadAjaxContent(_href);
		}
	});
	
	/*换肤*/
	$("#Hui-skin .dropDown-menu a").click(function(){
		var v = $(this).attr("data-val");
		setCookie("Huiskin", v);
		var skinlink = $("#skin").attr("href");
		$("#skin").attr("href", skinlink.replace(getSkin(), v));
		$(window.frames.document).contents().find("#skin").attr("href", $("#skin").attr("href"));
	});
	//解析当前的地址，使可以加载上一层或上几层
	parseAjaxUrl();
	
	/**
	 * 帐户切换
	 * 
	 */
	$('.account-change').click(function(){
		if($(this).data("options")){
			var opt = $.parseQuoteOptions($(this).data("options"));
			$.getJSON(opt.url, function (data) {
	            if (data.success) {
	                location.href = opt.redirect;
	            } else {
	            	layer.msg(data.message);
	            }
	        });
		}
	});
	

     $(window).on('hashchange', function () {
    		parseAjaxUrl();
    		// meunList(window.location.hash);
    });
	
});

// function meunList(hashUrl,index){
// 	$.ajax({
// 		url : '/admin/getMenu',
// 		type :"get",
// 		dataType : "json",
// 		cache : true,
// 		success : function(data){
// 			if(data.status==200){
// 				var datas=data.data,html="",sideHtml="",num=index;
// 				$.each(datas,function(i){
// 					html+='<li><a href="javascript:;">'+datas[i].funName+'</a></li>';
// 					if(index==i){
// 						var url=datas[index].list[0].list[0].funUrl;
// 						//setCookie("oldUrl",url);
// 						sidemeun(datas[index].list,url,0);
// 						if(url.split(":")[0]=="http"){
// 							window.location.hash = "/admin/welcome";
// 						}else{
// 							window.location.hash = url;
// 						}
//
// 					}else{
// 						$.each(datas[i].list,function(j){
// 							$.each(datas[i].list[j].list,function(k){
// 								if("#"+datas[i].list[j].list[k].funUrl==hashUrl.split("?")[0].split(".")[0]){
// 									setCookie("oldList",i);
// 									setCookie("oldUrl",datas[i].list[j].list[k].funUrl);
// 									setCookie("num",j);
// //									sidemeun(datas[i].list,datas[i].list[j].list[k].funUrl,j);
// //									num=i;
// 								}else if(hashUrl=="#ajax/welcome.html"){
// 									sidemeun(datas[0].list);
// 								}
// 							});
// 						});
// 						sidemeun(datas[getCookie("oldList")].list,getCookie("oldUrl"),getCookie("num"));
// 					}
// 				});
//
// 				$("#menu").html(html);
// 				$("#menu").find("li:eq("+getCookie("oldList")+")").addClass("current");
// 			}else if(data.status==400){
// 				window.location.href="/admin/login";
// 			}
// 		},
// 		error: function(err){
//             window.location.href="/admin/login";
// 		}
// 	});
// }

function sidemeun(menus,hashUrl,index){
	var html="";
	$.each(menus,function(i){
			html+='<dl>',className=i==index?"selected":"",isshow=i==index?"style='display: block;'":"";
			html+='<dt class="'+className+'"><i class="Hui-iconfont">'+menus[i].funIco+'</i>'+menus[i].funName+'<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i></dt>';
			if(menus[i].list){
				html+='<dd data-title="'+menus[i].funName+'" '+isshow+'><ul>';
				$.each(menus[i].list,function(j){
					var active="";
					if(menus[i].list[j].openType=="_blank"){
						html+='<li><a href="'+menus[i].list[j].funUrl+'" data-title="'+menus[i].list[j].funName+'" target="'+menus[i].list[j].openType+'">'+menus[i].list[j].funName+'</a></li></li>';
					}else{
						if(hashUrl==menus[i].list[j].funUrl){
							active="current";
							$('#home_third_menu').html(menus[i].list[j].funName);
							$('#home_second_menu').html(menus[i].funName);
						}
						html+='<li class="'+active+'"><a _href="'+menus[i].list[j].funUrl+'" data-title="'+menus[i].list[j].funName+'" href="javascript:void(0)">'+menus[i].list[j].funName+'</a></li></li>';
					}
					
				});
				html+='</ul></dd>';
			}
			html+='</dl>';
		});
		$("#asidenav").html(html);
		$.Huifold(".menu_dropdown dl dt",".menu_dropdown dl dd","fast",1,"click");
}
function _initUE(evl){
	var ue_attr = {
			autoHeightEnabled: true,
			autoFloatEnabled: false
			}
  
	if($(evl).attr("data-opt") ==="simple"){
		//自定义简洁版所需功能按钮
		ue_attr['toolbars'] = [[
	      	'anchor', //锚点
	        'undo', //撤销
	        'redo', //重做
	        'bold', //加粗
	        'italic', //斜体 'underline', //下划线
	        'strikethrough', //删除线
	        'subscript', //下标  
	        'time', //时间
	        'date', //日期
	        'cleardoc', //清空文档
	        'fontfamily', //字体
	        'fontsize', //字号
	        'simpleupload', //单图上传
	        'insertimage', //多图上传
	        'emotion', //表情
	        'justifyleft', //居左对齐
	        'justifyright', //居右对齐
	        'justifycenter', //居中对齐
	        'justifyjustify', //两端对齐
	        'indent', //首行缩进
	        'touppercase', //字母大写
	        'tolowercase', //字母小写
	        ]]
		}
	UE.delEditor($(evl).attr("id"))
	var ue = UE.getEditor($(evl).attr("id"),ue_attr);
}
function initWebUpload(evl){
	 var ops = $(evl).data("options") || "";
	 $(evl).powerWebUpload($.parseQuoteOptions(ops));
}