$(document).ready(function(){
	function loadHistory() {

	};

	$("#btn_analysis").click(function(){
		$("#span_analysis_success").hide();
		$("#span_analysis_failed").hide();
		$("#span_analysis_info").text("正在分析，请耐心等待...");
		$("#span_analysis_info").show();
		$.ajax({
			type:"get",
			url:"/scooper_website_analysis/data/compare",
			async:true,
			dataType: "json",
			success: function(data) {
				$("#span_analysis_info").hide();
				if (data.code == 0) {
					$("#span_analysis_success").text("分析成功！结果如下");
					$("#span_analysis_success").show();
					$("#input_db_result").attr("value", data.data.sqlPath);
					$("#input_resource_result").attr("value", data.data.filePath);
				} else {
					var message = data.message;
					$("#span_analysis_success").text("分析失败！原因是：" + message);
					$("#span_analysis_success").show();
					$("#input_db_result").attr("value", "");
					$("#input_resource_result").attr("value", "");
				}
			},
			error: function () {
				$("#span_analysis_info").hide();
				$("#span_analysis_failed").text("请求失败！");
				$("#span_analysis_failed").show();
			}
		});
	});

	$("#btn_sql_download").click(function () {
		var sqlPath = $("#input_db_result").val();
		download(sqlPath);
	});
	
	$("#btn_resource_download").click(function () {
		var resourcePath = $("#input_resource_result").val();
		download(resourcePath);
	});
});

function download(path) {
	$.ajax({
		type : "post",
		url : "",
		async : "",
		dataType : "",
		data : {
			'path' : path
		},
		success : function () {
			
		},
		error : function () {
			
		}
	});
}