# REST API 定义
1. “http://IP:8080/scooper-website-analysis/”
    视图跳转：返回首页index.jsp
2. “http://IP:8080/scooper-website-analysis/data/index/history”
    参数列表：无
    返回格式：
        code:操作结果代码
        message:操作结果提示
        systemTime:操作时间
        data：历史记录列表
3. “http://IP:8080/scooper-website-analysis/data/index/compare”
    参数列表：无
    返回格式：
        code:操作结果代码
        message:操作结果提示
        systemTime:操作时间
        data：SQL文件路径或没有更新的提示信息，静态资源压缩文件路径或没有更新的提示信息。
4. “http://IP:8080/scooper-website-analysis/data/index/download”
    参数列表：
        filePath:需要下载的文件的路径
    返回格式：
        code:操作结果代码
        message:操作结果提示
        systemTime:操作时间
        data：文件。       
    