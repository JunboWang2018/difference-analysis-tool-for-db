package cn.showclear.www.service.compare;

import cn.showclear.www.pojo.common.Message;

public interface CompareService {
    /**
     * 比较数据库信息
     * @return
     */
    Message compareDBInfo();

    /**
     * 比较文件信息
     * @return
     */
    Message compareFileInfo();
}
