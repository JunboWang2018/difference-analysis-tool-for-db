package cn.showclear.www.service.download;

import cn.com.scooper.common.exception.BusinessException;

import java.io.InputStream;

public interface DownloadService {
    /**
     * 根据文件路径获取输入流
     * @param filePath
     * @return
     * @throws BusinessException
     */
    InputStream getFileInputStream(String filePath) throws BusinessException;
}
