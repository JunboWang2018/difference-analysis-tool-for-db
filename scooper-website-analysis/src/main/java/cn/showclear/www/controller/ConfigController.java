/**
 * 
 * Project Name: scooper-website-analysis
 * File Name: ConfigController.java
 * Package Name: cn.showclear.www.controller
 * Description: 
 * Copyright: Copyright (c) 2017
 * Company: 杭州叙简科技股份有限公司
 * @version 1.4.0
 * @author ZHENGKAI
 * @date 2017年4月17日下午4:07:08
 */
package cn.showclear.www.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import cn.com.scooper.common.resp.APIObjectJson;
import cn.com.scooper.common.resp.ResultCode;

/**
 * 配置相关操作
 * @Reason: REASON(可选). <br/>
 * @date: 2017年4月17日下午4:07:08 <br/>
 *
 * @author ZHENGKAI
 * @version 1.4.0
 */
@Controller
@RequestMapping("/conf")
public class ConfigController extends BaseController {
	
	/**
	 * 加载配置项
	 * 
	 * @param type 配置项前缀
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/data")
	public APIObjectJson data(String type) {
		Map<String, Object> config = getAppConfig(type);
		
		APIObjectJson restResult = new APIObjectJson(ResultCode.SUCC);
		restResult.setData(config);
		return restResult;
	}
	
	/**
	 * requirejs相关
	 * 
	 * @param varName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/requireConfig")
	public ModelAndView requireConfig(String varName) {
		ModelAndView mav = new ModelAndView();
		if (StringUtils.isEmpty(varName)) {
			mav.addObject("useVar",false);
		} else {
			mav.addObject("varName",varName);
			mav.addObject("useVar",true);
		}
		mav.addObject("resPath", configProperties.get("config_res"));
		mav.setViewName("/js/djs/require-setup.djs");
		return mav;
	}
	
}
