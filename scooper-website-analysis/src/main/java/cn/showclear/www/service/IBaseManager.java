/**
 *
 * Project Name: scooper-difference-analysis-tool-for-db
 * File Name: IBaseManager.java
 * Package Name: cn.showclear.www.service
 * Description: 
 * Copyright: Copyright (c) 2017
 * Company: 杭州叙简科技股份有限公司
 * @version 1.4.0 
 * @author ZHENGKAI
 * @date 2017年4月17日上午9:52:40
 */
package cn.showclear.www.service;

/**
 * 基础管理器
 * @Reason: ADD REASON(可选). <br/>
 * @date: 2017年4月17日上午9:52:40 <br/>
 *
 * @author ZHENGKAI
 * @version 1.4.0
 */
public interface IBaseManager {
	
	/**
	 * 启动
	 */
	public void startup();
	
	/**
	 * 关闭
	 */
	public void shutdown();
}
