<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>公司网站数据库差异分析工具</title>
        <script type="text/javascript" src="js/jquery-3.3.1.min.js" ></script>
        <link rel="stylesheet" href="css/bootstrap.min.css" />
        <script type="text/javascript" src="js/bootstrap.min.js" ></script>
        <link rel="stylesheet" href="css/index.css" />
        <script type="text/javascript" src="js/index.js" ></script>
    </head>
    <body>
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
                                <div class="input-group" style="width: 120%;">
                                    <input id="input_db_result" type="text" class="form-control">
                                    <span class="input-group-btn">
                                        <button id="btn_db_download" class="btn btn-default" type="button">下载</button>
                                    </span>
                                </div>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td style="border-top:0px;padding-bottom: 30px;">
                            <h4>资源文件 : </h4>
                            <div class="col-lg-6">
                                <div class="input-group" style="width: 120%;">
                                    <input id="input_resource_result" type="text" class="form-control">
                                    <span class="input-group-btn">
                                        <button id="btn_resource_download" class="btn btn-default" type="button">下载</button>
                                    </span>
                                </div>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <h4>历史记录</h4>
                            <table class="table table-hover">
                                <thead>
                                <tr>
                                    <th style="text-align: center; width: 5%;">序号</th>
                                    <th style="text-align: center; width: 15%;">日期</th>
                                    <th style="text-align: center; width: 65%;">路径</th>
                                    <th style="text-align: center; width: 15%;">操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td style="text-align: center;">1</td>
                                    <td style="text-align: center;">27-Jun-2019 09:27:13.311</td>
                                    <td style="text-align: center; word-break:break-all">TanmayTanmayTanmayTanmayTanmayTanmayTanmayTanmayTanmayTanmayTanmayTanmayTanmayTanmayTanmayTanmayTanmayTanmayTanmayTanmayTanmayTanmayTanmayTanmayTanmayTanmay</td>
                                    <td style="text-align: center;">
                                        <button class="btn btn-default" type="button">SQL下载</button>
                                        <button class="btn btn-default" type="button">资源下载</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="text-align: center;">2</td>
                                    <td style="text-align: center;">27-Jun-2019 09:27:13.311</td>
                                    <td style="text-align: center; word-break:break-all">Tanmay</td>
                                    <td style="text-align: center;">
                                        <button class="btn btn-default" type="button">SQL下载</button>
                                        <button class="btn btn-default" type="button">资源下载</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="text-align: center;">3</td>
                                    <td style="text-align: center;">27-Jun-2019 09:27:13.311</td>
                                    <td style="text-align: center; word-break:break-all">Tanmay</td>
                                    <td style="text-align: center;">
                                        <button class="btn btn-default" type="button">SQL下载</button>
                                        <button class="btn btn-default" type="button">资源下载</button>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </body>
</html>
