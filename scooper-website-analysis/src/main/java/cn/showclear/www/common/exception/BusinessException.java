/**
 *
 * Project Name: scooper-website-analysis
 * File Name: BusinessException.java
 * Package Name: cn.showclear.www.common.exception
 * Description: 
 * Copyright: Copyright (c) 2017
 * Company: 杭州叙简科技股份有限公司
 * @version 1.4.0
 * @author zhengkai
 * @date 2017年1月7日下午1:29:20
 */
package cn.showclear.www.common.exception;

import cn.com.scooper.common.resp.ResultCode;


/**
 * 自定义异常类：统一业务处理异常
 * @Reason: ADD REASON(可选). <br/>
 * @date: 2017年1月7日下午1:29:20 <br/>
 *
 * @author ZHENGKAI
 * @version 1.4.0
 * 
 * @deprecated {@link cn.com.scooper.common.exception.BusinessException}
 */
@Deprecated
public class BusinessException extends Exception {

	private static final long serialVersionUID = -5819070357576514051L;

	// 返回状态码
	private int code = ResultCode.FAIL;
	
	/**
	 * BusinessException
	 * 默认构造方法
	 */
	public BusinessException(){
		super();
	}
	
	/**
	 * BusinessException
	 * @param message
	 */
	public BusinessException(String message){
		super(message);
	}
	
	/**
	 * BusinessException
	 * @param code
	 * @param message
	 */
	public BusinessException(String message, int code){
		super(message);
		this.code = code;
	}
    
	/**
	 * BusinessException
	 * @param message
	 * @param cause
	 */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
	
    /**
     * BusinessException
     * @param message
     * @param code
     * @param cause
     */
	public BusinessException(String message, int code, Throwable cause) {
	    super(message, cause);
	    this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
