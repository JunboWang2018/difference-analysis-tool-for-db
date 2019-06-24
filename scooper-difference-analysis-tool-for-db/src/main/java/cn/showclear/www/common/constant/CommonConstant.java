/**
 *
 * Project Name: scooper-difference-analysis-tool-for-db-common
 * File Name: CommonConstant.java
 * Package Name: cn.showclear.www.common.constant
 * Description: 
 * Copyright: Copyright (c) 2017
 * Company: 杭州叙简科技股份有限公司
 * @version 1.4.0 
 * @author ZHENGKAI
 * @date 2017年4月28日上午9:52:19
 */
package cn.showclear.www.common.constant;

/**
 * 项目中的常量定义
 * 
 * @Description 
 * @version 1.4.0
 * @author ZHENGKAI
 * @date 2017年4月28日上午9:52:19
 */
public final class CommonConstant {
    // private constructor
    private CommonConstant() { }
    
    public static final String REST_API_BASE = "/data/";
    
    // 下面开始自定义常量定义

    //数据源常量
    public static final String MAIN_DB_DATASOURCE_NAME = "mainDBDataSource";
    public static final String SUP_DB_DATASOURCE_NAME = "supDBDataSource";
    public static final String IS_DB_DATASOURCE_NAME = "ISDBDataSource";

    //方法返回结果码常量
    public static final int SUCCESS_CODE = 1;
    public static final int FAILED_CODE = 1000;

    //数据库常量
    public static final String NOT_NULLABLE = "NO";
    public static final String AUTO_INCREMENT = "auto_increment";
}
