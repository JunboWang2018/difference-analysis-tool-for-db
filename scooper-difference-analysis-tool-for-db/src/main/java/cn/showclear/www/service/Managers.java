/**
 *
 * Project Name: scooper-difference-analysis-tool-for-db
 * File Name: Managers.java
 * Package Name: cn.showclear.www.service
 * Description: 
 * Copyright: Copyright (c) 2017
 * Company: 杭州叙简科技股份有限公司
 * @version 1.4.0 
 * @author ZHENGKAI
 * @date 2017年4月17日上午9:52:40
 */
package cn.showclear.www.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 业务管理类
 * @Reason: ADD REASON(可选). <br/>
 * @date: 2017年4月12日下午2:51:14 <br/>
 *
 * @author ZHENGKAI
 * @version 1.4.0
 */
@Component
public final class Managers implements IBaseManager {
	private static final Logger log = LoggerFactory.getLogger(Managers.class);

	/** 业务定义 */
	//TODO
	
	/**
	 * 启动
	 */
	@Override
	@PostConstruct
	public void startup() {
		IBaseManager[] items = new IBaseManager[] {};
		for (IBaseManager item : items) {
			if (item == null) continue;
			try {
				item.startup();
				log.info("starup success: "+item.getClass().getCanonicalName());
			} catch (Throwable e) {
				log.error("startup failed : " + item.getClass().getCanonicalName());
			}
		}
	}

	/**
	 * 关闭
	 */
	@Override
	@PreDestroy
	public void shutdown() {
		IBaseManager[] items = new IBaseManager[] {};
		for (IBaseManager item : items) {
			if (item == null) continue;
			try {
				item.shutdown();
			} catch (Throwable e) {
				log.error("shutdown failed : " + item.getClass().getCanonicalName());
			}
		}
	}
}
