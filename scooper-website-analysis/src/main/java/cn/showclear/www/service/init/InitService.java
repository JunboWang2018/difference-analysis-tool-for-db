package cn.showclear.www.service.init;

import cn.com.scooper.common.exception.BusinessException;

/**
 * 初始化工具
 */
public interface InitService {
    /**
     * 初始化工具
     * @return
     */
    void init() throws BusinessException;
}
