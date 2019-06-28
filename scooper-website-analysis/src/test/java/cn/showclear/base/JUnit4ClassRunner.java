/**
 *
 * Project Name: scooper-website-analysis-ds
 * File Name: JUnit4ClassRunner.java
 * Package Name: cn.showclear.base
 * Description: 
 * Copyright: Copyright (c) 2017
 * Company: 杭州叙简科技股份有限公司
 * @version 1.4.0 
 * @author ZHENGKAI
 * @date 2017年4月23日下午12:49:37
 */
package cn.showclear.base;

import java.io.FileNotFoundException;

import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Log4jConfigurer;

/**
 * <p>Junit加载spring的runner（SpringJUnit4ClassRunner）要优先于spring加载log4j</p>
 * <p>因此采用普通方法，无法实现spring先加载log4j后被Junit加载。所以我们需要新建JUnit4ClassRunner类，修改SpringJUnit4ClassRunner加载log4j的策略。这样加载log4j就会优先于加载spring了</p>
 * @Reason: TODO ADD REASON(可选). <br/>
 * @date: 2017年4月23日下午12:49:37 <br/>
 *
 * @author ZHENGKAI
 * @version 1.4.0
 */
@SuppressWarnings("deprecation")
public class JUnit4ClassRunner extends SpringJUnit4ClassRunner {

	private static final String APP_NAME = "/scooper-website-analysis";
	private static final String CFG_BASE_LINUX = "/icooper/config";
	private static final String CFG_BASE_WINDOWS = System.getProperty("user.home") + "\\scooper";
	
	static {
        try {
            Log4jConfigurer.initLogging("classpath:config/log4j.properties");
        } catch (FileNotFoundException ex) {
            System.err.println("Cannot Initialize log4j");
        }
	}
	
	/**
	 * @param clazz
	 * @throws InitializationError
	 */
	public JUnit4ClassRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
		if(isLinux()) {
			System.setProperty("user.conf", CFG_BASE_LINUX + APP_NAME);
		} else {
			System.setProperty("user.conf", CFG_BASE_WINDOWS + APP_NAME);
		}
	}
	
	/**
	 * 判断是否是linux系统
	 * 
	 * @return true：是linux系统|false：不是linux系统
	 */
	protected boolean isLinux() {
		return System.getProperty("os.name").toLowerCase().contains("linux");
	}

}
