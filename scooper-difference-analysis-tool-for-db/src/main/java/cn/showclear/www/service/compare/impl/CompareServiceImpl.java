package cn.showclear.www.service.compare.impl;

import cn.com.scooper.common.exception.BusinessException;
import cn.showclear.www.common.constant.CommonConstant;
import cn.showclear.www.dao.base.data.DataDao;
import cn.showclear.www.dao.base.file.FileDao;
import cn.showclear.www.dao.base.table.TableDao;
import cn.showclear.www.pojo.base.ColumnDo;
import cn.showclear.www.pojo.base.TableDo;
import cn.showclear.www.service.compare.CompareService;
import cn.showclear.www.service.generate.GenerateFileService;
import cn.showclear.www.service.generate.GenerateSQLService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Wang Junbo
 * @description
 * @date 2019/6/20
 */
@Service
public class CompareServiceImpl implements CompareService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompareServiceImpl.class);

    @Autowired
    private TableDao tableDao;  //数据库表信息

    @Autowired
    private DataDao dataDao;   //数据库数据信息

    @Autowired
    private FileDao fileDao;   //文件信息

    @Autowired
    private PropertiesFactoryBean propFactory; //配置文件信息

    @Autowired
    private GenerateSQLService generateSQLService;

    @Autowired
    private GenerateFileService generateFileService;

    private StringBuilder updateSQL = new StringBuilder();

    /**
     * 比较数据库信息
     * @return
     */
    @Override
    public String compareDBInfo(String generatePath) throws BusinessException {
        String sql = "";
        this.compareTableInfo();
        sql = updateSQL.toString();
        if (updateSQL.toString().contains("\\")) {
            sql = sql.replaceAll("\\\\", "\\\\\\\\");
        }
        if (StringUtils.isEmpty(sql)) {
            return CommonConstant.NO_UPDATE;
        } else {
            //生成sql
            String filePath = null;
            try {
                filePath = generateFileService.generateSQLFile(generatePath, sql);
            } catch (IOException e) {
                LOGGER.error("生成SQL文件失败！");
            }
            updateSQL.delete(0, updateSQL.length());
            return filePath;
        }
    }

    /**
     * 比较文件信息
     * 1. 获取要扫描的文件路径和备份文件中保存的上次导出的时间
     * 2. 检查文件是否有更新（递归）。
     * 3. 若有更新，执行以下步骤：
     *    3.1 创建生成文件目录，创建压缩文件对象
     *    3.2 检查扫描文件路径下的更新时间
     *    3.3 将符合条件的文件信息添加到压缩路径，保持文件结构一致。
     * 4. 压缩文件创建成功则更新备份文件时间信息
     * @param generatePath  生成压缩包的路径
     * @return
     * @throws BusinessException
     *
     */
    @Override
    public String compareFileInfo(String generatePath) throws BusinessException {
        //取配置文件中扫描路径配置，并检查文件是否存在
        Properties configProp = null;
        try {
            configProp = propFactory.getObject();
        } catch (IOException e) {
            String excepMsg = "读取配置文件信息失败！";
            LOGGER.error(excepMsg);
            throw new BusinessException(CommonConstant.FAILED_CODE, excepMsg);
        }
        String scanningPath = configProp.getProperty("scanning.file.path");
        if (StringUtils.isEmpty(scanningPath)) {
            String excepMsg = "没有读取到需要扫描的文件路径信息！";
            LOGGER.error(excepMsg);
            throw new BusinessException(CommonConstant.FAILED_CODE, excepMsg);
        }
        File scanningFile = new File(scanningPath);
        if (!scanningFile.exists()) {
            String excepMsg = "需要扫描的文件路径不存在！";
            LOGGER.error(excepMsg);
            throw new BusinessException(CommonConstant.FAILED_CODE, excepMsg);
        }
        //取备份文件中上一次导出时间信息
        Long lastUpdateTime = null;
        try {
            lastUpdateTime = fileDao.getFileTimeProperty();
        } catch (IOException e) {
            String excepMsg = "读取备份文件信息失败！";
            LOGGER.error(excepMsg);
            throw new BusinessException(CommonConstant.FAILED_CODE, excepMsg);
        }
        //检查更新
        if (isUpdated(scanningFile, lastUpdateTime)) {
            String zipPath = null;
            OutputStream outputStream = null;
            try {
                //有更新
                zipPath = generateFileService.generateZipFilePath(generatePath);
                outputStream = new FileOutputStream(zipPath);
            } catch (FileNotFoundException e) {
                String excepMsg = "生成文件的路径不存在！";
                LOGGER.error(excepMsg);
                throw new BusinessException(CommonConstant.FAILED_CODE, excepMsg);
            } catch (IOException e) {
                String excepMsg = "生成文件的路径创建失败！";
                LOGGER.error(excepMsg);
                throw new BusinessException(CommonConstant.FAILED_CODE, excepMsg);
            }
            ZipOutputStream zipOut = new ZipOutputStream(outputStream);
            try {
                this.compareFile(scanningFile, zipOut, scanningFile.getName(), lastUpdateTime);
            } catch (IOException e) {
                String excepMsg = "复制新增的文件时读取异常！";
                LOGGER.error(excepMsg);
                throw new BusinessException(CommonConstant.FAILED_CODE, excepMsg);
            } finally {
                if(zipOut != null){
                    try {
                        zipOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            //压缩文件创建成功则更新备份文件时间信息
            //检查文件是否存在
            if (new File(zipPath).exists()) {
                fileDao.updateFileTimeProperty(new Date().getTime());
            }
            return zipPath;
        }
        return CommonConstant.NO_UPDATE;
    }

    /**
     * 检查文件是否有更新
     * @param file
     * @param lastUpdateTime
     * @return
     */
    private boolean isUpdated(File file, Long lastUpdateTime) {
        boolean isUpdated = false;
        if (!file.isFile()) {
            //处理文件夹
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                if (file.lastModified() > lastUpdateTime) {
                    isUpdated = true;
                } else {
                    isUpdated = false;
                }
            } else {
                for (File file1 : files) {
                    if (isUpdated(file1, lastUpdateTime)) {
                        isUpdated = true;
                    }
                }
            }
        } else {
            if (file.lastModified() > lastUpdateTime) {
                isUpdated = true;
            } else {
                isUpdated = false;
            }
        }
        return isUpdated;
    }

    /**
     * 将有更新的文件复制到压缩文件中
     * @param scanningFile
     * @param zipOut
     * @param name
     * @param lastUpdateTime
     * @throws IOException
     */
    private void compareFile(File scanningFile, ZipOutputStream zipOut, String name, Long lastUpdateTime) throws IOException {
        byte[] buf = new byte[2 * 1024];
        if (!scanningFile.isFile()) {
            //处理文件夹。
            File[] files = scanningFile.listFiles();
            if(files == null || files.length == 0){
                if (scanningFile.lastModified() > lastUpdateTime) {
                    // 新增的空文件夹处理
                    zipOut.putNextEntry(new ZipEntry(scanningFile.getName() + "\\"));
                    // 没有文件，不需要文件的copy
                    zipOut.closeEntry();
                }
            }
            for (File file : files) {
                this.compareFile(file, zipOut, name + "\\" + file.getName(), lastUpdateTime);
            }
        } else {
            //处理文件
            if (scanningFile.lastModified() > lastUpdateTime) {
                // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字。注： getName()只返回文件或目录名，而不是全路径名
                zipOut.putNextEntry(new ZipEntry(name));
                //copy文件
                FileInputStream fileIn = new FileInputStream(scanningFile);
                int len;
                while ((len = fileIn.read(buf)) != -1) {
                    zipOut.write(buf, 0, len);
                }
                zipOut.closeEntry();
                fileIn.close();
            }
        }
    }

    /**
     * 比较表信息
     *
     *  1. 检查表
     *  2. 在有更新的表中检查是否有更新字段。若有，添加SQL。
     *  3. 比较数据信息
     * @throws SQLException
     * @throws IOException
     * @throws BusinessException
     */
    private void compareTableInfo() throws BusinessException {
        List<String> modifiedTables = new ArrayList<String>();  //用来存放有更新的表的表名
        //从mainDB中获取表信息
        Properties properties = null;
        try {
            properties = propFactory.getObject();
        } catch (IOException e) {
            String excepMsg = "读取数据库配置信息失败！";
            LOGGER.error(excepMsg);
            throw new BusinessException(CommonConstant.FAILED_CODE, excepMsg);
        }
        String mainDBName = properties.getProperty("mainDB.dbname");
        List<TableDo> mainDBTables = null;
        try {
            mainDBTables = tableDao.getDBTableInfo(mainDBName);
            //从备份文件中获取表信息
//            List<TableDo> backupFileDBTables = tableDao.getBackupFileTableInfo();
        } catch (SQLException e) {
            String excepMsg = "读取数据库数据失败！";
            LOGGER.error(excepMsg);
            throw new BusinessException(CommonConstant.FAILED_CODE, excepMsg);
        }
        //对比数据信息
        for (int i = 0; i < mainDBTables.size(); i++) {
            modifiedTables.add(mainDBTables.get(i).getTableName());
            /*for (int j = 0; j < backupFileDBTables.size(); j++) {
                if (mainDBTables.get(i).getTableName().equals(backupFileDBTables.get(j).getTableName())) {
                    //相同表比较创建时间、修改时间和自增字段
                    if ((mainDBTables.get(i).getCreateTime().intValue() == backupFileDBTables.get(j).getCreateTime().intValue())
                            && mainDBTables.get(i).getUpdateTime().intValue() == backupFileDBTables.get(j).getUpdateTime().intValue()) {
                        if (mainDBTables.get(i).getAutoIncrement().intValue() != backupFileDBTables.get(j).getAutoIncrement().intValue()) {
                            //自增字段有变化，该表有更新
                            modifiedTables.add(mainDBTables.get(i).getTableName());
                        }
                        //创建时间、修改时间和自增字段均无变化，该表没有更新，继续循环
                    } else {
                        //创建时间或修改时间有变化，该表有更新
                        modifiedTables.add(mainDBTables.get(i).getTableName());
                    }
                }
            }*/
        }
        //比较表结构信息，检查是否有新增列，若有，返回SQL并添加
        try {
            updateSQL.append(this.compareColumnInfo(modifiedTables));
            //比较表数据信息
            updateSQL.append(this.compareDataInfo(modifiedTables));
        } catch (IOException e) {
            String excepMsg = "读取数据库配置信息失败！";
            LOGGER.error(excepMsg);
            throw new BusinessException(CommonConstant.FAILED_CODE, excepMsg);
        } catch (SQLException e) {
            String excepMsg = "读取数据库数据失败！";
            LOGGER.error(excepMsg);
            throw new BusinessException(CommonConstant.FAILED_CODE, excepMsg);
        }

    }

    /**
     * 在表中检查是否有新增字段。若有，返回新增字段的SQL
     * @param tables
     */
    private String compareColumnInfo(List<String> tables) throws IOException, SQLException {
        StringBuilder addColsSQL = new StringBuilder();
        Properties properties = propFactory.getObject();
        String mainDBName = properties.getProperty("mainDB.dbname");
        String supDBName = properties.getProperty("supDB.dbname");
        for (int i = 0; i < tables.size(); i++) {
            List<ColumnDo> mainDBCols = tableDao.getDBColumnInfo(mainDBName, tables.get(i));
            List<ColumnDo> supDBCols = tableDao.getDBColumnInfo(supDBName, tables.get(i));
            for (ColumnDo mainDBCol : mainDBCols) {
                boolean flag = false;
                for (ColumnDo supDBCol : supDBCols) {
                    if (mainDBCol.getColumnName().equals(supDBCol.getColumnName())) {
                        flag = true;
                    }
                }
                if (!flag) {
                    //没有该字段，生成SQL
                    String addColSQL = generateSQLService.generateAddColumnSQL(mainDBCol);
                    addColsSQL.append(addColSQL);
                }
            }
        }
        return addColsSQL.toString();
    }

    /**
     * 比较有更新表中的数据差异
     * @param modifiedTables
     * @return
     * @throws IOException
     * @throws SQLException
     */
    private String compareDataInfo(List<String> modifiedTables) throws IOException, SQLException {
        StringBuilder sqlSB = new StringBuilder();
        Properties properties = propFactory.getObject();
        String mainDBName = properties.getProperty("mainDB.dbname");
        String supDBName = properties.getProperty("supDB.dbname");
        for (int i = 0; i < modifiedTables.size(); i++) {
            //获取列信息
            List<ColumnDo> mainDBCols = tableDao.getDBColumnInfo(mainDBName, modifiedTables.get(i));
            List<ColumnDo> supDBCols = tableDao.getDBColumnInfo(supDBName, modifiedTables.get(i));
            List<String[]> mainDBDatas = dataDao.getDataList(mainDBCols, CommonConstant.MAIN_DB_DATASOURCE_NAME);
            List<String[]> supDBDatas = dataDao.getDataList(supDBCols, CommonConstant.SUP_DB_DATASOURCE_NAME);
            sqlSB.append(this.compareData(modifiedTables.get(i), mainDBCols, mainDBDatas, supDBCols, supDBDatas));
        }
        return sqlSB.toString();
    }

    /**
     * 比较数据
     *
     * 查找主键位置，初始化辅助标记数组，查询sup数据库中是否有该主键。
     *  1. 若存在主键，检查列是否相同
     *   1.1 不相同，有新增列，则生成update语句
     *   1.2 相同，检查该条数据是否相同
     *    1.2.1 若相同，则在两个列表中均逻辑删除（mainDBFlag或supDBFlag置为0）
     *    1.2.2 若不相同，则生成update语句，再执行逻辑删除
     *  2. 若不存在，则生成insert语句
     *  3. sup列表中剩余数据生成delete语句
     * @param tableName
     * @param mainDBCols
     * @param mainDBDatas
     * @param supDBCols
     * @param supDBDatas
     * @return
     */
    private String compareData(String tableName, List<ColumnDo> mainDBCols, List<String[]> mainDBDatas, List<ColumnDo> supDBCols, List<String[]> supDBDatas) {
        StringBuilder sqlSB = new StringBuilder();
        //查找主键位置
        int priIndex = Integer.MAX_VALUE;
        for(int i = 0; i < mainDBCols.size(); i++) {
            if (mainDBCols.get(i).getColumnKey().equalsIgnoreCase("PRI")) {
                priIndex = mainDBCols.get(i).getOrdinalPosition().intValue();
                break;
            }
        }
        if (priIndex == Integer.MAX_VALUE) {
            LOGGER.error("没有找到主键！");
            throw new BusinessException(CommonConstant.FAILED_CODE, "没有找到主键！");
        }
        //辅助标记数组
        int[] mainDBFlag = new int[mainDBDatas.size()];
        for (int i = 0; i < mainDBFlag.length; i++) {
            mainDBFlag[i] = 1;
        }
        int[] supDBFlag = new int[supDBDatas.size()];
        for (int i = 0; i < supDBFlag.length; i++) {
            supDBFlag[i] = 1;
        }
        //开始比较
        for (int mainDBIndex = 0; mainDBIndex < mainDBDatas.size(); mainDBIndex++) {
            //获得主键值
            String[] mainData = mainDBDatas.get(mainDBIndex);
            String priValue = mainData[priIndex];
            //检查supDB是否存在该主键
            boolean priFlag = false;
            String[] supData = null;
            int supDBIndex = 0;
            for (int i = 0; i < supDBDatas.size(); i++) {
                supData = supDBDatas.get(i);
                if (priValue.equals(supData[priIndex])) {
                    priFlag = true;
                    supDBIndex = i;
                    break;
                }
            }
            //若存在，检查数据
            if (priFlag) {
                //检查是否有新增列，有新增列直接生成update语句
                if (mainDBCols.size() > supDBCols.size()) {
                    sqlSB.append(generateSQLService.generateUpdateRecordSQL(tableName, mainDBCols, mainData));
                } else {
                    //列相同，检查数据，数据不同则生成更新语句并逻辑删除，相同则直接逻辑删除
                    for (int j = 1; j < mainData.length; j++) {
                        /*if(mainData.length>=12 && j == 12 && mainData[12].indexOf("\\") >= 0) {
                            System.out.println(mainData[12]);
                        }*/
                        if (!mainData[j].equals(supData[j])) {
                            sqlSB.append(generateSQLService.generateUpdateRecordSQL(tableName, mainDBCols, mainData));
                        }
                    }
                    //逻辑删除
                    mainDBFlag[mainDBIndex] = 0;
                    supDBFlag[supDBIndex] = 0;
                }
            } else {
                //不存在主键，执行生成insert语句
                sqlSB.append(generateSQLService.generateAddRecordSQL(tableName, mainDBCols, mainData));
            }
        }
        //sup中“剩余”数据执行生成delete语句
        for (int i = 0; i < supDBDatas.size(); i++) {
            if (supDBFlag[i] == 1) {
                sqlSB.append(generateSQLService.generateDeleteRecordSQL(tableName, mainDBCols, supDBDatas.get(i)));
            }
        }
        return sqlSB.toString();
    }


}
