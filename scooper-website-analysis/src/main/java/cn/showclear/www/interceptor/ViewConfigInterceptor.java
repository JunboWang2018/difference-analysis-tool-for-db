/**
 *
 * Project Name: scooper-website-analysis
 * File Name: ViewConfigInterceptor.java
 * Package Name: cn.showclear.www.interceptor
 * Description: 
 * Copyright: Copyright (c) 2017
 * Company: 杭州叙简科技股份有限公司
 * @version 1.4.0
 * @author ZHENGKAI
 * @date 2017年4月17日下午3:58:41
 */
package cn.showclear.www.interceptor;

import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 为 view 设置 appConfig 属性
 * @Reason: ADD REASON(可选). <br/>
 * @date: 2017年4月17日下午3:58:41 <br/>
 *
 * @author ZHENGKAI
 * @version 1.4.0
 */
public class ViewConfigInterceptor extends cn.com.scooper.common.spring.interceptor.ViewConfigInterceptor {
	
	/** 业务定义 */

    //配置文件业务
    @Resource(name = "configProperties")
    protected Properties configProperties;
    
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//设置scooper-res的路径
		request.setAttribute("resPath", configProperties.get("config_res"));
		
		//
		return super.preHandle(request, response, handler);
	}

}
