package cn.showclear.www.dao.base.count.impl;

import cn.com.scooper.common.exception.BusinessException;
import cn.showclear.utils.FileConnectUtil;
import cn.showclear.www.common.constant.CommonConstant;
import cn.showclear.www.dao.base.count.CountDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * @author Wang Junbo
 * @description 记录导出次数
 * @date 2019/6/27
 */
@Repository
public class CountDaoImpl implements CountDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(CountDaoImpl.class);
    /**
     * 添加或修改计数
     * @param count
     */
    @Override
    public void updateCount(int count) throws BusinessException {
        String key = "export.count";
        Properties properties = new Properties();
        InputStream fileIn = null;
        OutputStream fileOut = null;
        try {
            fileIn = FileConnectUtil.getInputStream();
            //载入配置文件数据到输入流中
            properties.load(fileIn);
            fileOut = FileConnectUtil.getOutputStream();
        } catch (IOException e) {
            String excepMsg = "加载备份文件失败！";
            LOGGER.error(excepMsg);
            throw new BusinessException(CommonConstant.FAILED_CODE, excepMsg);
        }
        properties.setProperty(key, String.valueOf(count));
        try {
            properties.store(fileOut, "backup info");
        } catch (IOException e) {
            String excepMsg = "property写入输出流失败！";
            LOGGER.error(excepMsg);
            throw new BusinessException(CommonConstant.FAILED_CODE, excepMsg);
        }
        if (count == 0) {
            LOGGER.info("初始化计数信息成功！");
        } else {
            LOGGER.info("更新计数信息成功！");
        }

    }


    /**
     * 获得计数
     * @return
     */
    @Override
    public int getCount() {
        Properties properties = new Properties();
        InputStream fileIn = null;
        try {
            fileIn = FileConnectUtil.getInputStream();
            properties.load(fileIn);
        } catch (FileNotFoundException e) {
            String excepMsg = "未找到备份文件！";
            LOGGER.error(excepMsg);
            throw new BusinessException(CommonConstant.FAILED_CODE, excepMsg);
        } catch (IOException e) {
            String excepMsg = "读取property失败！";
            LOGGER.error(excepMsg);
            throw new BusinessException(CommonConstant.FAILED_CODE, excepMsg);
        }
        String countStr = properties.getProperty("export.count");
        if (StringUtils.isEmpty(countStr)) {
            return 0;
        }
        return Integer.parseInt(countStr);
    }
}
