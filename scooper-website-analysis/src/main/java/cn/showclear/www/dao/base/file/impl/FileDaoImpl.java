package cn.showclear.www.dao.base.file.impl;

import cn.showclear.utils.FileConnectUtil;
import cn.showclear.www.dao.base.file.FileDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * @author Wang Junbo
 * @description
 * @date 2019/6/20
 */
@Repository
public class FileDaoImpl implements FileDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileDaoImpl.class);

    /**
     * 添加或修改文件导出时间
     * @param time
     * @return
     */
    @Override
    public boolean updateFileTimeProperty(Long time) {
        boolean updateResult = true;
        String key = "file.update.time";
        String value = String.valueOf(time);
        Properties properties = new Properties();
        InputStream fileIn = null;
        OutputStream fileOut = null;
        try {
            fileIn = FileConnectUtil.getInputStream();
            //载入配置文件数据到输入流中
            properties.load(fileIn);
            fileOut = FileConnectUtil.getOutputStream();
        } catch (IOException e) {
            LOGGER.error("加载配置文件失败！", e);
            updateResult = false;
        }
        properties.setProperty(key, value);
        try {
            properties.store(fileOut, "backup info");
        } catch (IOException e) {
            LOGGER.error("property写入输出流失败！", e);
            updateResult = false;
        } finally {
            try {
                fileIn.close();
                fileOut.close();
            } catch (IOException e) {
                LOGGER.error("流关闭失败！", e);
            }
        }
        return updateResult;
    }

    @Override
    public Long getFileTimeProperty() throws IOException {
        Properties properties = new Properties();
        InputStream fileIn = FileConnectUtil.getInputStream();
        properties.load(fileIn);
        String time = properties.getProperty("file.update.time");
        if (StringUtils.isEmpty(time)) {
            return 0L;
        }
        return Long.parseLong(time);
    }
}
