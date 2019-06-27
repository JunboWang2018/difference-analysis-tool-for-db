/**
 * 业务层，该层下以业务建包，如service.dispatch
 * 
 * 业务层整体结构如下：
 * service
 * 		-{业务名1}
 * 			-impl：接口实现层:需要自启动的请再实现IBaseManager，并在Managers里配置
 * 			接口定义
 * 		-{业务名2}
 * 		-IBaseManager：基础管理器
 * 		-Managers：业务管理类
 * 		-BaseService：基础接口方法（抽象类）
 */
package cn.showclear.www.service;