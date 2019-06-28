# scooper-website-analysis


## 概述


### 依赖项目
本项目依赖如下项目：
- `scooper-core` - 使用scooper-core的账号系统，通讯录数据
- `dispatch-web` - 如果需要拨打电话，则依赖 dispatch-web
- `scooper-video` - 如果需要使用视频监控，则依赖 scooper-video

### 安装/升级说明
#### 全新安装
1. 将发布包中的 `/config/scooper-website-analysis/resources/conf_template` 目录放入 `/icooper/config/` 目录。
2. 在config.properties文件中根据不同系统环境配置资源文件扫描路径和导出文件生成路径，如在linux环境下需要配置linux.scanning.file.path和linux.generate.file.path两项。
3. 在db.properties文件中配置数据库。  
4. 将发布包中的 `scooper-website-analysis.war` 放入 `{tomcat}/webapps/` 目录。

#### 升级安装
1. 将发布包中的 `scooper-website-analysis.war` 覆盖到 `{tomcat}/webapps/` 目录中。

### 使用说明
1. 点击“开始分析”按钮，即可进行数据库和资源文件的差异分析。分析完成后导出的文件路径会显示在下方框内，可以按需下载。
2. 会保留最近十次有生成文件的分析（数据库或资源文件有差异）的记录，可以下载最近十次分析导出的文件。


## 版本更新记录

详见：[{project}/docs/CHANGELOG.md](CHANGELOG.md)

## REST API 定义

详见：[{project}/docs/RESTAPI.md](RESTAPI.md)


## war包结构
在WAR包中必须包含当前发布的版本号，放在WAR包根目录的version.txt文件中。WAR包文件结构示例：
scooper-website-analysis.war
  ├─ WEB-INF/
  │   └─ web.xml
  ├─ index.html
  └─ version.txt

version.txt文件内容示例：（版本命名参照《版本管理制度》）
```
r1.4.0.32\_20170315\_police
```

其中version.txt文件及其内容，在应用打包的时候使用Maven/ANT脚本自动生成。


## 应用版本发布包结构

### 示例
scooper-website-analysis_r1.4.0.32_20170315_police.tgz
  ├─ README.md
  ├─ CHANGELOG.md
  ├─ scooper-website-analysis.war
  ├─ sql/
  │    ├─ init.sql
  │    ├─ update_r1.4.0.30.sql
  │    └─ update_r1.4.0.32_20170315_police.sql
  ├─ config/
  │    └─ scooper-website-analysis/
  │         ├─ db.properties
  │         └─ config.properties
  └─ thirdpart/
       ├─ something.zip
       └─ others.zip
- README.md - 提供该应用的描述，配置说明，注意事项等文字内容；如果该应用有其它依存关系也在这里进行说明；如果项目的上述内容变动则需更新该文件。
- CHANGELOG.md - 各个版本的版本升级描述，每次版本发布时都须更新该文件，说明该版本改变的功能点、修复的BUG等信息；如果该版本有配置项的增减、数据库结构的改动，也需要在此说明。
- scooper-website-analysis-rest.war - 发布版本的WAR包。
- sql/ - 在该目录下放置程序的数据库建库（建表）脚本、初始数据插入脚本；如果是平台版本增量更新涉及到数据库改动，则还需要有增量更新脚本。
init.sql - 平台（项目）统一版本r1.4.0时该应用的建库（建表）脚本，以及初始数据导入脚本。
update_r1.4.0.30.sql - 在版本r1.4.0.30发布时相对平台（项目）版本r1.4.0初始时的增量更新脚本。
update_r1.4.0.32_20170315_police.sql - 在版本r1.4.0.32_20170315_police发布时相对于r1.4.0.30的增量更新脚本。
- config/ - 配置文件模板存放目录；由安装程序将该目录下的文件复制到/icooper/config/scooper-website-analysis目录下，并替换模板文件内对应的配置项为正确的配置值。
- thirdpart/ - 该目录中放置应用依赖的第三方应用包。（仅首次发布包中需包含）如，在r1.4.0.30时加入该依赖，则仅该版本发布时需包含，后续一直到下次平台（项目）统一发布时再次包含进来（或者纳入到平台基本包中）。


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
例如：

将 db.properties 中的 `${db.base.host}` 修改为 `192.168.101.28:3306`， `${db.base.dbname}` 修改为 对应的数据库名，`${db.base.user}`及`${db.base.password}`修改为对应的用户名和密码，例如：

```
db.base.host=192.168.101.28:3306
db.base.dbname=DB_SC_CORE
db.base.driver=com.mysql.jdbc.Driver
db.base.user=showclear
db.base.password=showclear
db.base.url=jdbc:mysql://${db.base.host}/${db.base.dbname}?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&zeroDateTimeBehavior=convertToNull
db.base.initialSize=5
db.base.maxActive=10
db.base.minIdle=2
db.base.maxIdle=10
db.base.maxWait=1000
```

#### Windows：
将配置文件放置到 `{user-home}\scooper\{app-name}` 目录。
（这里的 {app-name} 默认为 `scooper_website_analysis`）

如：`C:\Users\yykfbwe\scooper\scooper_website_analysis`

并修改配置文件内容。

#### Linux：
将配置文件放置到 `/icooper/config/{app-name}` 目录。
（这里的 {app-name} 默认为 `scooper_website_analysis`）

如：`/icooper/config/scooper_website_analysis`

并修改配置文件内容。


## 程序发布

### 项目发布
1. 在Eclipse项目上点鼠标右键 -> Run As -> Maven Build；
2. 在弹出的对话框的 Goals 中输入 “verify -DskipTests”；
3. 然后点击 Run 。

运行完成后，程序打包并输出到共享目录：
```
\\192.168.103.154\132rd\Projects\scooper-website-analysis\publish
```

## 第三方软件安装配置


#### 持续更新中...