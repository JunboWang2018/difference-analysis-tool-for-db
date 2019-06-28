/**
 *
 * Project Name: scooper-website-analysis
 * File Name: PropertyConfig.java
 * Package Name: cn.showclear.utils
 * Description: 
 * Copyright: Copyright (c) 2017
 * Company: 杭州叙简科技股份有限公司
 * @version 1.0.1 
 * @author ZHENGKAI
 * @date 2017年1月12日下午5:25:53
 */
package cn.showclear.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 配置加载、管理类；
 * 
 * <pre>
 * # 需加载的配置文件（Linux在 /icooper/config/{app_name}/ 目录下,Windows在 {UserHome}/scooper/{app_name}/ 目录下）
 * </pre>
 * @Reason: ADD REASON(可选). <br/>
 * @date: 2017年1月12日下午5:25:53 <br/>
 *
 * @author ZHENGKAI
 * @version 1.4.0
 */
@Deprecated
public class PropertyConfig extends PropertyPlaceholderConfigurer {
	
	protected Map<String, String> config = new HashMap<String, String>();
	
	
	@Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String value = props.getProperty(keyStr);
            config.put(keyStr, value);
        }
    }
	
	/**
	 * 已加载的配置文件中，是否包含该配置项
	 * @param key
	 * @return
	 */
	public boolean has(String key) {
		return config.containsKey(key);
	}
	
	public void put(String key, String value) {
		config.put(key, value);
	}
	
	public String get(String key) {
		return has(key) ? config.get(key) : null;
	}
	
	public String get(String key, String defaultValue) {
		String val = get(key);
		return val == null ? defaultValue : val;
	}
	
	public int getInt(String key) {
		return getInt(key, 0);
	}
	
	public int getInt(String key, int defaultValue) {
		String val = get(key);
		try {
			if (val != null) {
				return Integer.parseInt(val);
			}
		} catch (Exception e) {
			// Ignore.
		}
		return defaultValue;
	}
	
	public long getLong(String key) {
		return getLong(key, 0);
	}
	
	public long getLong(String key, long defaultValue) {
		String val = get(key);
		try {
			if (val != null) {
				return Long.parseLong(val);
			}
		} catch (Exception e) {
			// Ignore.
		}
		return defaultValue;
	}
	
	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}
	
	public boolean getBoolean(String key, boolean defaultValue) {
		String val = get(key);
		try {
			if (val != null) {
				val = val.toLowerCase();
				return "true".equals(val) || "yes".equals(val);
			}
		} catch (Exception e) {
			// Ignore.
		}
		return defaultValue;
	}
	
	public Map<String, String> getByPrefix(String prefix) {
		Map<String, String> map = new HashMap<String, String>();
		//
		for (Map.Entry<String, String> ent : config.entrySet()) {
			if (ent.getKey() == null) continue;
			if (ent.getKey().startsWith(prefix)) {
				map.put(ent.getKey(), ent.getValue());
			}
		}
		//
		return map;
	}
	
}
