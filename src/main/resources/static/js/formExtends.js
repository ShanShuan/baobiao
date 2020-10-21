/**
 * 成功提交返回后执行用户输入方法
 * @param formId
 * @param callback 方法返回CallbackForm对象
 * @return
 */
function CallbackForm(formId,callback) {
	this.formId = formId;
	this.callBack = callback;
	this.init = function() {
		var checkForm = $("#" + formId);
		checkForm._form = this;
		checkForm.validate({   
			 submitHandler: function(form){
				  $(form).find(":submit").attr("disabled", true);
				  var index = layer.load(0, {shade: false});  //加载进度条
			      $(form).ajaxSubmit(function(res){
			    	    layer.close(index); 
	    				if (res.success == true) {
	                        layer.msg("操作成功!");
	                        if(checkForm._form.callBack){
	                        	checkForm._form.callBack(checkForm._form);
	                        }
	                    } else {
	                    	layer.msg(res.message);
	                    }
	    				$(form).find(":submit").attr("disabled", false);
			      });     
			 },
			 errorPlacement: function(error, element) {
				 element.addClass("Validform_error");
				 element.parent("div").next("div").html(error);
				 element.parent("div").next("div").find(".error").addClass("Validform_checktip Validform_wrong");
			 }
		 });
	}
	this.init();
}
function Validform(obj,callback) {
	this.init = function() {
		obj.validate({   
			submitHandler: function(form){
				$(form).find(":submit").attr("disabled", true);
				var index = layer.load(0, {shade: false});  //加载进度条
				$(form).ajaxSubmit(function(res){
					layer.close(index); 
					if (res.success == true) {
						layer.msg("操作成功!");
						if(callback){
							callback(form);
                        }
					} else {
						layer.msg(res.message);
					}
					$(form).find(":submit").attr("disabled", false);
				});     
			},
			errorPlacement: function(error, element) {
				element.addClass("Validform_error");
				element.parent("div").next("div").html(error);
				element.parent("div").next("div").find(".error").addClass("Validform_checktip Validform_wrong");
			}
		});
	}
	this.init();
}
//callBefor 检证结束后提交前调用
//formId form的Id
//actionName 操作说明
//callBack 	成功后的返回方法
function AdvancedForm(formOption) {
	this.callBefor = formOption.callBefor;
	this.formId = formOption.formId;
	this.actionName = formOption.actionName;
	this.result = {};
	this.callBack =formOption.callBack;
	this.init = function() {
		var checkForm = $("#" + this.formId);
		checkForm._form = this;
		checkForm.validate( {ignore:"",
			submitHandler : function(form) {
				if(checkForm._form.callBefor){
					if(!checkForm._form.callBefor())
						return;
				}
				if (confirm("确定" + checkForm._form.actionName + "?")) {
					var options = {};
					options.success = function(rInfo) {
						var obj=eval(rInfo);
						checkForm._form.result = obj;
						alert(obj.message[0]);
						if (obj.flag == "success") {
							checkForm._form.callBack(checkForm._form);
						}
					};
					options.beforeSend=function(XMLHttpRequest){disabledForm(checkForm._form.formId);WaitDialog.show("正在提交...")};
					options.complete=function(XMLHttpRequest){enabledForm(checkForm._form.formId);WaitDialog.hide();};
					options.dataType = "text";
					var ajaxForm = $(form);
					ajaxForm.ajaxSubmit(options);
				}
			}
		});
	}
	this.init();
}


/**
 * 使表单所有元素失效
 * @param id
 * @return
 */
function disabledForm(id){
	$("#"+id).find("textarea").attr("disabled",true);
	$("#"+id).find("input").attr("disabled",true);
}
/**
 * 使表单所有元素有效
 * @param id
 * @return
 */
function enabledForm(id){
	$("#"+id).find("textarea").attr("disabled",false);
	$("#"+id).find("input").attr("disabled",false);
} 

/**
 * 弹出关闭
 * @param formId
 * @param actionName
 * @return
 */
function GeneralFormDialog(formId, actionName) {
	this.formId = formId;
	this.actionName = actionName;
	this.result = {};
	this.init = function() {
		var checkForm = $("#" + formId);
		checkForm._form = this;
		checkForm.validate({
					ignore : "",
					submitHandler : function(form) {
						if (confirm("确定" + actionName + "?")) {
							var options = {};
							options.success = function(rInfo) {
								var obj = eval(rInfo);
								checkForm._form.result = obj;
								alert(obj.message[0]);
								if (obj.flag == "success") {
									dialog.closeMe();
								}
							};
							options.beforeSend = function(XMLHttpRequest) {
								disabledForm(formId);
								WaitDialog.show("正在提交...")
							};
							options.complete = function(XMLHttpRequest) {
								enabledForm(formId);
								WaitDialog.hide();
							};
							options.dataType = "text";
							var ajaxForm = $(form);
							ajaxForm.ajaxSubmit(options);
						}
					}
				});
	}
	this.init();
}

/**
 * 弹出等条
 */
var WaitDialog= new function (){
   var rootPath=getRootPath()
	this.div;
	this.show = function(message){
		var style="background:#ffffff;position:absolute;width:300px;top:50%;left:50%;margin-left:-150px;margin-top:-150px;text-align:center;padding:7px 0 0 0;font:bold 11px Arial, Helvetica, sans-serif;";
		var html="<div id=\"loading\" style=\""+style+"\">"+message+"..<img src=\""+rootPath+"/style/m/images/waiting.gif\" alt=\"wait...\" /></div>";
		this.div =$(html);
		$("body").append(this.div);
	}
	this.hide=function(){
		this.div.remove();
	}
}
function getRootPath(){
var strFullPath=window.document.location.href;
var strPath=window.document.location.pathname;
var pos=strFullPath.indexOf(strPath);
var prePath=strFullPath.substring(0,pos);
var postPath=strPath.substring(0,strPath.substr(1).indexOf('/')+1);
return(prePath+postPath);
}


