<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title>公司网站数据库差异分析工具</title>
        <script type="text/javascript" src="js/jquery-3.3.1.min.js" ></script>
        <link rel="stylesheet" href="css/bootstrap.min.css" />
        <script type="text/javascript" src="js/bootstrap.min.js" ></script>
        <link rel="stylesheet" href="css/index.css" />
        <script type="text/javascript" src="js/index.js" ></script>
    </head>
    <body class="bodyFlow">
        <div class="indexW">
            <table class="table">
                <caption style="text-align: center;"><h3>公司网站数据库差异分析工具</h3></caption>
                <tbody>
                    <tr>
                        <td >
                            <button id="btn_analysis" class="btn btn-default" style="margin-right: 30px">开始分析</button>
                            <span id="span_analysis_info" style="display: none;" class="label label-info"></span>
                            <span id="span_analysis_success" style="display: none;" class="label label-success"></span>
                            <span id="span_analysis_failed" style="display: none;" class="label label-danger"></span>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <h4>数据库 : </h4>
                            <div class="col-lg-6">
                                <div class="input-group" style="width: 110%;">
                                    <input id="input_db_result" readonly="readonly" type="text" class="form-control">
                                    <span class="input-group-btn">
                                        <button id="btn_db_download" class="btn btn-default" type="button">下载</button>
                                    </span>
                                </div>
                                <span id="span_db_download_result" style="display: none; " class="label label-danger" >请先点击开始分析！</span>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td style="border-top:0px;padding-bottom: 30px;">
                            <h4>资源文件 : </h4>
                            <div class="col-lg-6">
                                <div class="input-group" style="width: 110%;">
                                    <input id="input_resource_result" readonly="readonly" type="text" class="form-control">
                                    <span class="input-group-btn">
                                        <button id="btn_resource_download" class="btn btn-default" type="button">下载</button>
                                    </span>
                                </div>
                                <span id="span_resource_download_result" style="display: none;" class="label label-danger" >请先点击开始分析！</span>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <h4>历史记录（保留最近10条）</h4>
                            <div class="historyTable">
                                <table class="table table-hover">
                                    <thead>
                                    <tr>
                                        <th style="text-align: center; width: 5%;">序号</th>
                                        <th style="text-align: center; width: 15%;">日期</th>
                                        <th style="text-align: center; width: 65%;">路径</th>
                                        <th style="text-align: center; width: 15%;">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody id="tbody_histories"></tbody>
                                </table>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </body>
</html>
