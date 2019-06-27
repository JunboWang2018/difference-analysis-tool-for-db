package cn.showclear.www.service.generate;

import java.io.IOException;

/**
 * 生成文件
 */
public interface GenerateFileService {
    /**
     * 生成sql文件，并返回生成路径，不含SQL文件名
     * @param path
     * @param sql
     * @return
     * @throws IOException
     */
    String generateSQLFile(String path, String sql) throws IOException;

    /**
     * 创建本次导出的目录
     * @param path
     * @return
     * @throws IOException
     */
    String createFolder(String path) throws IOException;

    /**
     * 创建ZIP文件
     * @param parentPath
     * @return
     */
    String generateZipFilePath(String parentPath) throws IOException;
}
