/**
 * 
 * Project Name: scooper-difference-analysis-tool-for-db
 * File Name: BaseController.java
 * Package Name: cn.showclear.www.controller
 * Description: 
 * Copyright: Copyright (c) 2017
 * Company: 杭州叙简科技股份有限公司
 * @version 1.4.0
 * @author ZHENGKAI
 * @date 2017年4月17日下午4:09:03
 */
package cn.showclear.www.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import cn.com.scooper.common.util.PropertiesUtil;

/**
 * 基础controller类
 * @Reason: ADD REASON(可选). <br/>
 * @date: 2017年4月17日下午4:09:03 <br/>
 *
 * @author ZHENGKAI
 * @version 1.4.0
 */
public class BaseController {
	
	/** 业务定义 */
    
	//配置文件业务
    @Resource(name = "configProperties")
    protected Properties configProperties;

    // i18n 国际化资源
    @Autowired
    protected MessageSource messageSource;
    
	/**
	 * 加载初始化配置项
	 * 
	 * @param type 配置项前缀
	 * @return
	 */
	protected Map<String, Object> getAppConfig(String type) {
		Map<String, Object> configCache = new HashMap<String, Object>();
		//常规配置项
		configCache.put("dev_mode", PropertiesUtil.getValue(configProperties, "devMode", "false"));
		configCache.put("main.title", PropertiesUtil.getValue(configProperties, "main.title",""));
		configCache.putAll(PropertiesUtil.getByPrefix(configProperties, "video"));
		// 根据网页类型加载相应的配置项
		if(!(type==null||type.length()==0)){
			configCache.putAll(PropertiesUtil.getByPrefix(configProperties, type));
		}
		// other configurations
		prepareAppConfig(configCache);
		//
		return configCache;
	}
	
	/**
	 * 添加其他配置项
	 * @param config
	 */
	protected void prepareAppConfig(Map<String, Object> config) {
		// Ignore. fill in sub-class
	}
}
