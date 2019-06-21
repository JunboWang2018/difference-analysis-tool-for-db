package cn.showclear.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * @author Wang Junbo
 * @description
 * @date 2019/6/18
 */
public class FileConnectUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileConnectUtil.class);
    private static File file = new File(System.getProperties().getProperty("user.conf") + "/backup.properties");
/*    private static File filePath = new File("F:/backup");*/

    /**
     * 初始化时间存储文件
     * @return
     */
    public static boolean initFile() {
        boolean initResult = true;
       /* if (!filePath.exists()) {
            initResult = filePath.mkdir();
        }*/
        if (!file.exists()) {
            try {
                initResult = file.createNewFile();
            } catch (IOException e) {
                LOGGER.error("创建时间备份文件失败！");
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

}
