package cn.showclear.www.service.download.impl;

import cn.com.scooper.common.exception.BusinessException;
import cn.showclear.www.common.constant.CommonConstant;
import cn.showclear.www.dao.base.file.FileDao;
import cn.showclear.www.service.download.DownloadService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * @author Wang Junbo
 * @description
 * @date 2019/6/28
 */
@Service
public class DownloadServiceImpl implements DownloadService {
    /**
     * 根据文件路径获取输入流
     * @param filePath
     * @return
     * @throws BusinessException
     */
    @Override
    public InputStream getFileInputStream(String filePath) throws BusinessException {
        if (StringUtils.isEmpty(filePath)) {
            throw new BusinessException(CommonConstant.FAILED_CODE, "文件路径不存在！");
        }
        File file = new File(filePath);
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new BusinessException(CommonConstant.FAILED_CODE, file.getName() + "文件不存在！");
        }
        return inputStream;
    }
}
