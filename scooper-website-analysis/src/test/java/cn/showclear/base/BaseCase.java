/**
 *
 * Project Name: scooper-difference-analysis-tool-for-db-ds
 * File Name: BaseCase.java
 * Package Name: cn.showclear.base
 * Description: 
 * Copyright: Copyright (c) 2017
 * Company: 杭州叙简科技股份有限公司
 * @version 1.0.1 
 * @author ZHENGKAI
 * @date 2017年1月12日下午5:25:53
 */
package cn.showclear.base;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * 基础测试方法，所有的单元测试类全部得继承该类
 * 
 * @Reason: REASON(可选). <br/>
 * @date: 2017年4月23日下午12:40:11 <br/>
 *
 * @author ZHENGKAI
 * @version 1.4.0
 */
@SuppressWarnings("deprecation")
@RunWith(JUnit4ClassRunner.class)
@ActiveProfiles("development")
@ContextConfiguration(locations = { "/config/applicationContext-test.xml" })
@Transactional
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
public abstract class BaseCase extends AbstractTransactionalJUnit4SpringContextTests {
  
}
