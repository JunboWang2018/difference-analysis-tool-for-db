/**
 * Project Name: scooper-website-analysis-dsapi
 * File Name: PageBean.java
 * Package Name: cn.showclear.www.pojo.common
 * Copyright (c) 2017 杭州叙简科技股份有限公司
 *
 * $Revision: 1.4.0 $
 * $Author: ZHENGKAI $
 * $Date: 2017-01-04 $
 */
package cn.showclear.www.pojo.common;

import java.io.Serializable;

/**
 * @version 1.4.0
 * @author ZHENGKAI
 * @data 2015年7月19日下午10:21:04
 * @描述: 
 * @deprecated use {@link cn.com.scooper.common.resp.APIPage}
 */
@Deprecated
public class PageBean implements Serializable {

	private static final long serialVersionUID = 5181434984797267537L;

	/** 当前页码 */
	private Integer currentPage;
	
	/** 每页条数 */
	private Integer pageSize;
	
	/**
	 * 是否分页
	 */
	public boolean isPaging() {
		return currentPage > 0 && pageSize > 0;
	}

	/**
	 * 获取起始行
	 */
	public int getBeginRowNum() {
		return pageSize * (currentPage - 1);
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}
