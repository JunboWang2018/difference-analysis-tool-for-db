package cn.showclear.www.service.generate.impl;

import cn.showclear.www.common.constant.CommonConstant;
import cn.showclear.www.dao.base.count.CountDao;
import cn.showclear.www.service.generate.GenerateFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Wang Junbo
 * @description
 * @date 2019/6/26
 */
@Service
public class GenerateFileServiceImpl implements GenerateFileService {

    @Autowired
    private CountDao countDao;

    /**
     * 生成sql文件，并返回生成路径，不含SQL文件名
     * @param path
     * @param sql
     */
    @Override
    public String generateSQLFile(String path, String sql) throws IOException {
        String targetPath = this.createFolder(path);
        Path filePath = this.createSQLFile(targetPath);
        String filePathStr = filePath.toString();
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePathStr));
        bufferedWriter.write(sql, 0, sql.length());
        bufferedWriter.flush();
        bufferedWriter.close();
        return filePath.toString();
    }


    /**
     * 创建本次导出的目录
     * @param path
     * @return
     * @throws IOException
     */
    @Override
    public String createFolder(String path) throws IOException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String targetPathStr = path + File.separator + CommonConstant.PARENT_FILE_NAME_START + "_" + df.format(new Date());
        Path targetPath = Paths.get(targetPathStr);
        if (Files.notExists(targetPath)) {
            targetPath = Files.createDirectories(targetPath);
        }
        return targetPath.toString();
    }


    /**
     * 创建SQL文件
     * @param path
     * @return
     * @throws IOException
     */
    private Path createSQLFile(String path) throws IOException {
        int count = countDao.getCount();
        String fileName = CommonConstant.SQL_FILE_NAME_START + "_" + count + "_" + System.currentTimeMillis() + CommonConstant.SQL_FILE_NAME_END;
        String filePath = path + File.separator + fileName;
        Path path1 = Paths.get(filePath);
        if (Files.notExists(path1)) {
            path1 = Files.createFile(path1);
        }
        return path1;
    }

    /**
     * 创建ZIP文件
     * @param parentPath
     * @return
     * @throws IOException
     */
    @Override
    public String generateZipFilePath(String parentPath) throws IOException {
        int count = countDao.getCount();
        String targetPath = this.createFolder(parentPath);
        String fileName = CommonConstant.ZIP_FILE_NAME_START + "_" + count + "_" + System.currentTimeMillis() + CommonConstant.ZIP_FILE_NAME_END;
        String filePath = targetPath + File.separator + fileName;
        return filePath;
    }
}
