/**
 *
 * Project Name: scooper-difference-analysis-tool-for-db
 * File Name: InstantiationTracingBeanPostProcessor.java
 * Package Name: cn.showclear.www.config
 * Description: 
 * Copyright: Copyright (c) 2017
 * Company: 杭州叙简科技股份有限公司
 * @version 1.4.0
 * @author ZHENGKAI
 * @date 2017年1月12日下午5:25:53
 */
package cn.showclear.www.config;

import cn.showclear.www.service.init.InitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * spring容器初始化完成后业务操作
 * @Reason: TODO ADD REASON(可选). <br/>
 * @date: 2017年1月12日下午5:25:53 <br/>
 *
 * @author ZHENGKAI
 * @version 1.4.0
 */
public class InstantiationTracingBeanPostProcessor implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(InstantiationTracingBeanPostProcessor.class);

	@Autowired
	private InitService initService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		//需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("spring容器初始化完成");
		}
		
		if(event.getApplicationContext().getParent() == null) {
			//初始化备份文件信息
			initService.init();
        } 
	}
}
