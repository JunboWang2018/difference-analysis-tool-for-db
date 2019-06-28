package cn.showclear.www.dao.base.file;

import cn.com.scooper.common.exception.BusinessException;

import java.io.IOException;

public interface FileDao {
    /**
     * 添加或修改文件导出时间
     * @param time
     * @return
     */
    boolean updateFileTimeProperty(Long time);

    /**
     * 从备份文件中获取文件导出时间
     * @return
     */
    Long getFileTimeProperty() throws IOException;
}
