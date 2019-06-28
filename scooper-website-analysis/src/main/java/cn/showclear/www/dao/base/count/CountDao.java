package cn.showclear.www.dao.base.count;

import cn.com.scooper.common.exception.BusinessException;

public interface CountDao {
    /**
     * 添加或修改计数
     * @param count
     */
    void updateCount(int count) throws BusinessException;

    /**
     * 获得计数
     * @return
     */
    int getCount() throws BusinessException;
}
