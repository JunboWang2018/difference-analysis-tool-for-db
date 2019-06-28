package cn.showclear.www.service.compare;

import cn.com.scooper.common.exception.BusinessException;

public interface CompareService {

    /**
     * 获取导出路径
     * @return
     */
    String getGeneratePath();
    /**
     * 比较数据库信息
     * @return
     */
    String compareDBInfo(String generatePath) throws BusinessException;

    /**
     * 比较文件信息
     * @return
     */
    String compareFileInfo(String generatePath) throws BusinessException;
}
