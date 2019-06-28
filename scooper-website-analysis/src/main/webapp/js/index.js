$(document).ready(function () {
	var url = getProjectName() + "/data/index/history";
	$.ajax({
		type : "get",
		url : url,
		async : "true",
		dataType : "json",
		success : function (result) {
			if (result.code == 0) {
				data = result.data;
				var historyHtml = fullHistory(data);
				$("#tbody_histories").html(historyHtml);
			} else {
				var message = result.message;
				$("#tbody_histories").html(message);
			}
		},
		error : function () {
			$("#tbody_histories").html("未获取到历史记录！");
		}
	});
});

$(document).ready(function(){
	$("#btn_analysis").click(function(){
		//清空文本内容，隐藏标签
		$("#input_db_result").attr("value", "");
		$("#input_resource_result").attr("value", "");
		$("#span_analysis_success").hide();
		$("#span_analysis_failed").hide();
		$("#span_db_download_result").hide();
		$("#span_resource_download_result").hide();
		$("#span_analysis_info").text("正在分析，请耐心等待...");
		$("#span_analysis_info").show();
		var url = getProjectName() + "/data/index/compare";
		$.ajax({
			type : "get",
			url : url,
			async : true,
			dataType : "json",
			success : function(result) {
				$("#span_analysis_info").hide();
				if (result.code == 0) {
					$("#span_analysis_success").text("分析成功！结果如下");
					$("#span_analysis_success").show();
					$("#input_db_result").attr("value", result.data.sqlPath);
					$("#input_resource_result").attr("value", result.data.filePath);
				} else {
					var message = result.message;
					$("#span_analysis_success").text("分析失败！原因是：" + message);
					$("#span_analysis_success").show();
					$("#input_db_result").attr("value", "");
					$("#input_resource_result").attr("value", "");
				}
			},
			error : function () {
				$("#span_analysis_info").hide();
				$("#span_analysis_failed").text("请求失败！");
				$("#span_analysis_failed").show();
			}
		});
	});

	var PLEASE_CLICK_ANALYSIS_FIRST = "请先点击开始分析！";
	var DB_NOT_UPDATE = "数据库没有更新！";
	var RESOURCE_NOT_UPDATE = "资源文件没有更新！";

	$("#btn_db_download").click(function () {
		var sqlPath = $("#input_db_result").val();
		if (isEmpty(sqlPath)) {
			$("#span_db_download_result").text(PLEASE_CLICK_ANALYSIS_FIRST);
			$("#span_db_download_result").show();
			return;
		}
		if (sqlPath == "没有更新") {
			$("#span_db_download_result").text(DB_NOT_UPDATE);
			$("#span_db_download_result").show();
			return;
		}
		download(sqlPath);
	});
	
	$("#btn_resource_download").click(function () {
		var resourcePath = $("#input_resource_result").val();
		if (isEmpty(resourcePath)) {
			$("#span_resource_download_result").show();
			return;
		}
		if (resourcePath == "没有更新") {
			$("#span_resource_download_result").text(RESOURCE_NOT_UPDATE);
			$("#span_resource_download_result").show();
			return;
		}
		download(resourcePath);
	});

});

function download(path) {
	var downloadForm = $("<form method='post' style='display: none'>");
	var url = getProjectName() + "/data/index/download";
	downloadForm.attr("action", url);
	var input = $("<input>");
	input.attr("name", "filePath");
	input.attr("type", "hidden");
	input.attr("value", path);
	downloadForm.append(input);
	downloadForm.append("</form>");
	$(document.body).append(downloadForm);
	downloadForm.submit();
	downloadForm.remove();
}

function fullHistory(data) {
	var historyHtml = "";
	var total = data.total;
	var list = data.list;
	for (var i = 0; i < total && i < 10; i++) {
		historyHtml += "<tr>";
		historyHtml += "<td style='text-align: center;'>" + (i + 1) + "</td>";
		if (list[i].zip != undefined) {
			historyHtml += "<td style='text-align: center;'>" + list[i].zip.date + "</td>";
			historyHtml += "<td style='text-align: center; '>" + list[i].zip.path + "</td>";

		} else {
			historyHtml += "<td style='text-align: center;'>" + list[i].sql.date + "</td>";
			historyHtml += "<td style='text-align: center; '>" + list[i].sql.path + "</td>";
		}
		historyHtml += "<td style='text-align: center; '>";
		if (list[i].zip != undefined) {
            var filePath = list[i].zip.path + "/" +list[i].zip.fileName;
			historyHtml += "<button class='btn btn-default' style='margin-right: 5px' type='button' onclick=\"download('" + filePath + "')\">资源下载</button>";

		}
		if (list[i].sql != undefined) {
		    var filePath = list[i].sql.path + "/" +list[i].sql.fileName;
			historyHtml += "<button class='btn btn-default' type='button' onclick=\"download('" + filePath + "')\">SQL下载</button>";
		}
		historyHtml += "</td>";
		historyHtml += "</tr>";
	}
	return historyHtml;
}

//判断字符是否有效的方法
function isEmpty(obj){
	if(obj == undefined || obj == null || obj == ""){
		return true;
	}else{
		return false;
	}
}

function getRootPath() {
	var localhostPath = getIPAddress();
	var projectName = getProjectName();
	return localhostPath + projectName;
}

function getIPAddress() {
	//获取当前url
	var curWwwPath = window.document.location.href;
	//获取主机地址之后的目录
	var pathName = window.document.location.pathname;
	var position = curWwwPath.indexOf(pathName);
	//截取主机地址
	var localhostPath = curWwwPath.substring(0, position);
	return localhostPath;
}

function getProjectName() {
	//获取主机地址之后的目录
	var pathName = window.document.location.pathname;
	//截取项目名
	var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
	return projectName;
}
