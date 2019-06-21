/**
 *
 * Project Name: scooper-difference-analysis-tool-for-db
 * File Name: BaseService.java
 * Package Name: cn.showclear.www.service
 * Description: 
 * Copyright: Copyright (c) 2017
 * Company: 杭州叙简科技股份有限公司
 * @version 1.4.0 
 * @author ZHENGKAI
 * @date 2017年4月17日上午9:52:40
 */
package cn.showclear.www.service;

import java.util.List;

/**
 * 基础接口方法
 * @Reason: ADD REASON(可选). <br/>
 * @date: 2017年4月17日上午9:52:40 <br/>
 *
 * @author ZHENGKAI
 * @version 1.4.0
 */
public abstract class BaseService<T> {
	
	/**
	 * 数据字典翻译
	 */
	protected void translate(List<T> list) {
		if (list != null) {
			for (T obj : list) {
				translate(obj);
			}
		}
	}

	/**
	 * 数据字典翻译
	 */
	protected abstract void translate(T obj);
}
