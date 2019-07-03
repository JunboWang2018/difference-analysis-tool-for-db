package cn.showclear.utils;

import cn.com.scooper.common.exception.BusinessException;
import cn.showclear.www.common.constant.CommonConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * @author Wang Junbo
 * @description
 * @date 2019/6/18
 */
public class FileConnectUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileConnectUtil.class);
    private static File file = null;

    static {
        Properties properties = new Properties();
        InputStream fileIn = null;
        try {
            fileIn = new FileInputStream(System.getProperty("user.conf") + File.separator + "config.properties");
            properties.load(fileIn);
        } catch (FileNotFoundException e) {
            String excepMsg = "没有找到配置文件！";
            LOGGER.error(excepMsg, e);
            throw new BusinessException(CommonConstant.FAILED_CODE, excepMsg);
        } catch (IOException e) {
            String excepMsg = "读取配置文件失败！";
            LOGGER.error(excepMsg, e);
            throw new BusinessException(CommonConstant.FAILED_CODE, excepMsg);
        }
        String generatePath = "";
        if (isLinux()) {
            generatePath = properties.getProperty("linux.generate.file.path");
        } else {
            generatePath = properties.getProperty("windows.generate.file.path");
        }

        if (StringUtils.isEmpty(generatePath)) {
            LOGGER.error("未配置导出文件目录！");
        }
        Path targetPath = Paths.get(generatePath);
        if (Files.notExists(targetPath)) {
            try {
                Files.createDirectories(targetPath);
            } catch (IOException e) {
                String excepMsg = "导出文件目录失败！";
                LOGGER.error(excepMsg, e);
                throw new BusinessException(CommonConstant.FAILED_CODE, excepMsg);
            }
        }
        file = new File(generatePath + File.separator + "backup.properties");
    }

    /**
     * 初始化时间存储文件
     * @return
     */
    public static boolean initFile() {
        boolean initResult = true;
        if (!file.exists()) {
            try {
                initResult = file.createNewFile();
            } catch (IOException e) {
                LOGGER.error("创建时间备份文件失败！", e);
                initResult = false;
            }
        }
        return initResult;
    }

    /**
     * 获得输入流
     * @return
     * @throws FileNotFoundException
     */
    public static InputStream getInputStream() throws FileNotFoundException {
        InputStream fileIn = new FileInputStream(file);
        return fileIn;
    }

    /**
     * 获得输出流
     * @return
     * @throws FileNotFoundException
     */
    public static OutputStream getOutputStream() throws FileNotFoundException {
        OutputStream fileOut = new FileOutputStream(file);
        return fileOut;
    }

    /**
     * 获得Properties实例
     *  1. 建立输入流
     *  2. 载入文件信息
     *  3. 返回实例
     * @return
     * @throws IOException
     */
    public static Properties getPropInstance() throws IOException {
        Properties properties = new Properties();
        InputStream fileIn = new FileInputStream(file);
        //载入配置文件数据到输入流中
        properties.load(fileIn);
        return properties;
    }

    /**
     * 判断是否是linux系统
     *
     * @return true：是linux系统|false：不是linux系统
     */
    private static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }


}
