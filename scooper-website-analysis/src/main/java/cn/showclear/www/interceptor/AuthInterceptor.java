/**
 *
 * Project Name: scooper-difference-analysis-tool-for-db
 * File Name: AuthInterceptor.java
 * Package Name: cn.showclear.www.interceptor
 * Description: 
 * Copyright: Copyright (c) 2017
 * Company: 杭州叙简科技股份有限公司
 * @version 1.0.1 
 * @author zhengkai
 * @date 2017年1月8日下午7:46:21
 */
package cn.showclear.www.interceptor;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;

import cn.com.scooper.common.resp.APIRespJson;
import cn.com.scooper.common.resp.ResultCode;

import cn.showclear.www.common.constant.CommonConstant;


/**
 * 权限认证业务
 * @Reason: TODO ADD REASON(可选). <br/>
 * @date: 2017年1月8日下午7:46:21 <br/>
 *
 * @author ZHENGKAI
 * @version 1.4.0
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {

	protected static final Logger LOGGER = LoggerFactory.getLogger(AuthInterceptor.class);

	// 是否需要进行权限验证
    @Value("#{configProperties['auth.enabled']}")
    protected boolean enableAuth;

    @Autowired
    protected ServletContext servletContext;
    
	/** 业务定义 */

    //配置文件业务
    @Resource(name = "configProperties")
    protected Properties configProperties;
    
    
    // 
    private String contextPath;
    
    @PostConstruct
    private void startup() {
        contextPath = servletContext.getContextPath();
    }

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//权限认证业务操作
        if (enableAuth) {
		
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("开始权限认证");
			}

            // 若参数中携带了 token，则使用参数中的；否则使用 HTTP-Header 中的；其次，使用 Cookie 中的
            String token = request.getParameter("token");
            if (StringUtils.isEmpty(token)) {
                token = request.getHeader("Token");
            }
            if (StringUtils.isEmpty(token)) {
                token = getCookieValue(request, "token");
            }
            if (StringUtils.isEmpty(token)) {
                LOGGER.info("need parameter : 'token'");
                return failNotLogin(request, response);
            }
            
            try {
                // TODO 验证 token 有效性

            	// 验证成功，若未设置 cookie 则设置之
            	// addCookieToken(response, token);
            	
            } catch (Exception ex) {
                LOGGER.info(ex.getMessage() + " : " + token);
                return failNotLogin(request, response);
            }
			
        }
        
		return true;
	}

    // 请求失败处理；数据接口返回JSON，视图则重定向到登陆界面；
	protected boolean failNotLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String actionPath = getActionPath(request);
        // 统一定义以“/data/”开头的为 RESP API 接口
        if (actionPath.startsWith(CommonConstant.REST_API_BASE)) {
            // 返回JSON格式失败信息
            response.setContentType("application/json");
            APIRespJson resp = new APIRespJson(ResultCode.TOKEN_NOEXIST, "need login");
            response.getWriter().append(JSONObject.toJSONString(resp));
        } else {
            // 跳转到登陆页，并携带 redirectFrom 参数（以便跳回该页）
            String path = request.getContextPath()
                    + "/view/login?redirectFrom="
                    + URLEncoder.encode(request.getRequestURI(), "UTF-8");
            response.sendRedirect(path);
        }
        return false;
    }
    
    // 获取相对 contextPath 的请求路径
    protected String getActionPath(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (request.getContextPath().length() > 2) {
            path = path.substring(request.getContextPath().length());
        }
        return path;
    }
    
    // 获取cookie中的指定值
    protected String getCookieValue(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        //
        for (Cookie c : cookies) {
            if (key.equals(c.getName())) {
                return c.getValue();
            }
        }
        return null;
    }
    
    // 在 cookie 中添加 token （安全考虑，仅通过后台设置 cookie 值，并设置为 HttpOnly）
    protected void addCookieToken(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath(contextPath);
        cookie.setMaxAge(-1);
        response.addCookie(cookie);
    }
    
}
