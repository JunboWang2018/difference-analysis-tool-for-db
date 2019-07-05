# scooper-website-analysis
## 概述

### 安装/升级说明
#### 全新安装
1. 将发布包中的 `/config/scooper-website-analysis` 文件夹放入 `/icooper/config/` 目录。
2. 在config.properties文件中根据不同系统环境配置资源文件扫描路径和导出文件生成路径，如在linux环境下需要配置linux.scanning.file.path和linux.generate.file.path两项。
3. 在db.properties文件中配置需要分析的数据库，mainDB为测试修改的数据库，supDB为辅助备份数据库，isDB为 MySQL 自带的信息数据库，三个数据库必须为同一连接（host一致），具体配置步骤见下方用户配置。 
4. 将发布包中的 `scooper-website-analysis.war` 放入 `{tomcat}/webapps/` 目录。
5. 访问地址：http://IP:8080/项目名/。本项目即为：http://IP:8080/scooper-website-analysis/

#### 升级安装
1. 将发布包中的 `scooper-website-analysis.war` 覆盖到 `{tomcat}/webapps/` 目录中。

### 使用说明
1. 使用前确保本地镜像的数据库和辅助备份数据库与公司正式环境数据库完全一致，包括自增长的字段，并本地镜像的数据库和辅助备份数据库在同一连接上。
2. 保证公司网站的镜像项目中资源文件与正式环境中一致。
3. 点击“开始分析”按钮，即可进行数据库和资源文件的差异分析。分析完成后导出的文件路径会显示在下方框内，可以按需下载。
4. 会保留最近十次有生成文件的分析（数据库或资源文件有差异）的记录，可以下载最近十次分析导出的文件。

###使用说明解释
1. 本项目用到了MySQL自带的information_schema数据库，提供了一个对该数据库的配置信息，为了确保能够正确的查询到需要比较的表信息和字段信息，需要在同一个连接中（同一个MySQL数据库）。
2. 由于数据库中一些表只有自增长字段能唯一标识一条记录，所以为了正确更新对应的记录，所以需要保证两个数据库中对应的自增字段的值一致。
3. 项目初始化完成后，不能删除用于记录信息的backup.properties文件。若不慎删除，需要删除所有生成的文件，并保证镜像的数据库和辅助备份数据库与公司官网数据库一致，保证静态资源文件的一致，再重新启动项目，此时所有历史记录会清空。
4. 项目首次初始化的时候会记录当前时间并保存到backup.properties文件中，用于在资源文件对比的时候与文件的最后修改时间进行比较，对于所有大于记录时间的文件，系统会判定其为有更新的文件，并复制到压缩文件中。所以在项目初始化之前需要保证正式项目和镜像的资源文件内容一致。
5. 项目部署完成后，若遇到需要重新启动，启动初始化时会检查backup.properties中备份信息是否已存在，若已存在则不会覆盖。

## 版本更新记录

详见：[{project}/docs/CHANGELOG.md](CHANGELOG.md)

## REST API 定义

详见：[{project}/docs/RESTAPI.md](RESTAPI.md)


## war包结构
在WAR包中必须包含当前发布的版本号，放在WAR包根目录的version.txt文件中。WAR包文件结构如下：
scooper-website-analysis.war
  ├─ META-INF   
  ├─ WEB-INF/
  │   └─ web.xml
  ├─ css
  ├─ js
  ├─ index.jsp
  └─ version.txt
- README.md    - 提供该应用的描述，配置说明，注意事项等文字内容；如果该应用有其它依存关系也在这里进行说明；如果项目的上述内容变动则需更新该文件。
- CHANGELOG.md - 各个版本的版本升级描述，每次版本发布时都须更新该文件，说明该版本改变的功能点、修复的BUG等信息；如果该版本有配置项的增减、数据库结构的改动，也需要在此说明。
- css          - 样式目录
- js           - js文件
- index.jsp    - 项目主页
- version.txt  - 版本文件
其中version.txt文件及其内容，在应用打包的时候使用Maven/ANT脚本自动生成。


## 应用版本发布包结构
scooper-website-analysis_r1.0.0.2_20190701.tgz
  ├─ README.md
  ├─ CHANGELOG.md
  ├─ scooper-website-analysis.war
  ├─ sql/
  │    └─ init.sql
  └─ config/
       └─ scooper-website-analysis/
            ├─ db.properties
            └─ config.properties
- README.md - 提供该应用的描述，配置说明，注意事项等文字内容；如果该应用有其它依存关系也在这里进行说明；如果项目的上述内容变动则需更新该文件。
- CHANGELOG.md - 各个版本的版本升级描述，每次版本发布时都须更新该文件，说明该版本改变的功能点、修复的BUG等信息；如果该版本有配置项的增减、数据库结构的改动，也需要在此说明。
- sql/init.sql - 本项目无需自建数据库，所以该文件内容为空。
- config/ - 配置文件模板存放目录；由安装程序将该目录下的文件复制到/icooper/config/scooper-website-analysis目录下，并替换模板文件内对应的配置项为正确的配置值
- scooper-website-analysis-rest.war - 发布版本的WAR包。

## 配置文件
本项目中的配置文件分为两部分：程序默认配置，用户配置。

### 程序默认配置--生产环境禁止修改
主要为应用程序配置项的默认值。

程序的默认配置放置在 `{project}/src/main/resource/default.properties` 文件中。

在编译打包后为 `{app}/WEB-INF/classes/default.properties` 文件。

### 用户配置
主要为项目部署后的独立配置信息（如：扫描文件地址配置、数据库配置等）。

用户配置的文件模板在： `{tgz包}/config/scooper-website-analysis`目录中。

需要将配置文件复制到对应的目录中（见下面两条——Windows,Linux），并修改配置内容为正确的配置。

#### Windows：
将配置文件放置到 `{user-home}\scooper\{app-name}` 目录。
（这里的 {app-name} 默认为 `scooper_website_analysis`）

如：`C:\Users\yykfbwe\scooper\scooper_website_analysis`

并修改配置文件内容。

#### Linux：
将配置文件放置到 `/icooper/config/{app-name}` 目录。
（这里的 {app-name} 默认为 `scooper-website-analysis`）

如：`/icooper/config/scooper-website-analysis`

并修改配置文件内容。
    config.properties包含两项配置信息，并区分linux环境和windows环境，scanning.file.path为需要扫描的静态资源文件目录，generate.file.path为配置生成的文件存放目录。
    用户根据不同环境配置相应的路径，如：
        在windows环境下，扫描路径为F:/upload，导出文件存放路径为F:/update，需要如下配置
            windows.scanning.file.path=F:/upload
            windows.generate.file.path=F:/update
        在linux环境下，扫描路径为/icooper/appdata/scooper-websit/zb_users/upload，导出文件存放路径为/icooper/appdata/scooper-website-analysis，需要如下配置
            linux.scanning.file.path=/icooper/appdata/scooper-websit/zb_users/upload
            linux.generate.file.path=/icooper/appdata/scooper-website-analysis
    db.properties包含三个同一连接的数据库的配置，mainDB为本地镜像数据库，supDB为辅助备份数据库，isDB为information_schema数据库（MySQL自带），配置内容如下：
        mainDB.host=192.168.100.216:3306        主机 
        mainDB.dbname=bdm100006363_db           数据库名
        mainDB.driver=com.mysql.jdbc.Driver     数据库加载驱动名
        mainDB.user=root                        数据库的用户名
        mainDB.password=123456                  数据库的密码
        mainDB.url=jdbc:mysql://${mainDB.host}/${mainDB.dbname}?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&zeroDateTimeBehavior=convertToNull   数据库url
        mainDB.initialSize=5                    数据库连接池初始大小
        mainDB.maxActive=10                     数据库连接池最大活跃连接数
        mainDB.minIdle=2                        最小空闲数，始终保留在池中的最小连接数
        mainDB.maxIdle=10                       最大空闲数，始终保留在池中的最大连接数
        mainDB.maxWait=1000                     最大等待毫秒数, 单位为 ms, 超过时间会出错误信息
    其它数据库的配置信息同上。