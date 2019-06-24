package cn.showclear.www.service.compare.impl;

import cn.com.scooper.common.exception.BusinessException;
import cn.showclear.www.common.constant.CommonConstant;
import cn.showclear.www.dao.base.data.DataDao;
import cn.showclear.www.dao.base.file.FileDao;
import cn.showclear.www.dao.base.table.TableDao;
import cn.showclear.www.pojo.base.ColumnDo;
import cn.showclear.www.pojo.base.TableDo;
import cn.showclear.www.pojo.common.Message;
import cn.showclear.www.service.compare.CompareService;
import cn.showclear.www.service.sql.GenerateSQLService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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

    private StringBuilder updateSQL = new StringBuilder();

    /**
     * 比较数据库信息
     * @return
     */
    @Override
    public Message compareDBInfo() {
        try {
            List<String> modifiedTables = this.compareTableInfo();
            this.compareDataInfo(modifiedTables);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(updateSQL);
        return new Message(CommonConstant.SUCCESS_CODE, "成功");
    }

    /**
     * 比较文件信息
     * @return
     */
    @Override
    public Message compareFileInfo() {
        return null;
    }

    /**
     * 比较表信息，在有更新的表中检查是否有更新字段。若有，添加SQL，并返回有更新的表
     * @return
     * @throws SQLException
     * @throws IOException
     */
    private List<String> compareTableInfo() throws SQLException, IOException {
        List<String> modifiedTables = new ArrayList<String>();  //用来存放有更新的表的表名
        //从主数据库中获取表信息
        Properties properties = null;
        try {
            properties = propFactory.getObject();
        } catch (IOException e) {
            LOGGER.error("读取数据库配置信息失败！");
        }
        String backupDBName = properties.getProperty("mainDB.dbname");
        List<TableDo> dbTables = tableDao.getDBTableInfo(backupDBName);
        //从备份文件中获取表信息
       /* List<TableDo> fileTables = tableDao.getBackupFileTableInfo();
        for (int i = 0; i < dbTables.size(); i++) {
            for (int j = 0; j < fileTables.size(); j++) {
                //找表名相同的元素
                if (dbTables.get(i).getTableName().equals(fileTables.get(j).getTableName())) {
                    //检查创建时间和修改时间是否一致，不一致的加入modifiedTables
                    if (dbTables.get(i).getCreateTime().longValue() == fileTables.get(j).getCreateTime().longValue()
                            && dbTables.get(i).getUpdateTime().longValue() == fileTables.get(j).getUpdateTime().longValue()) {
                        break;
                    } else {
                        modifiedTables.add(dbTables.get(i).getTableName());
                    }
                }
            }
        }*/
        for (int i = 0; i < dbTables.size(); i++) {
            modifiedTables.add(dbTables.get(i).getTableName());
        }
        //在有更新的表中检查是否有新增字段
        String columnSQL = this.compareColumnInfo(modifiedTables);
        updateSQL.append(columnSQL);
        return modifiedTables;
    }

    /**
     * 在有更新的表中检查是否有新增字段。若有，返回新增字段的SQL
     * @param modifiedTables
     */
    private String compareColumnInfo(List<String> modifiedTables) throws IOException, SQLException {
        StringBuilder addColsSQL = new StringBuilder();
        Properties properties = propFactory.getObject();
        String mainDBName = properties.getProperty("mainDB.dbname");
        String supDBName = properties.getProperty("supDB.dbname");
        for (int i = 0; i < modifiedTables.size(); i++) {
            List<ColumnDo> mainDBCols = tableDao.getDBColumnInfo(mainDBName, modifiedTables.get(i));
            List<ColumnDo> supDBCols = tableDao.getDBColumnInfo(supDBName, modifiedTables.get(i));
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
     */
    private void compareDataInfo(List<String> modifiedTables) throws IOException, SQLException {
        Properties properties = propFactory.getObject();
        String mainDBName = properties.getProperty("mainDB.dbname");
        String supDBName = properties.getProperty("supDB.dbname");
        for (int i = 0; i < modifiedTables.size(); i++) {
            //获取列信息
            List<ColumnDo> mainDBCols = tableDao.getDBColumnInfo(mainDBName, modifiedTables.get(i));
            List<ColumnDo> supDBCols = tableDao.getDBColumnInfo(supDBName, modifiedTables.get(i));
            List<String[]> mainDBDatas = dataDao.getDataList(mainDBCols, CommonConstant.MAIN_DB_DATASOURCE_NAME);
            List<String[]> supDBDatas = dataDao.getDataList(supDBCols, CommonConstant.SUP_DB_DATASOURCE_NAME);
            //若该表在两个数据库中都没有数据，则此表没有数据更新
            if (mainDBDatas.size() == 0 && supDBDatas.size() == 0) {
                continue;
            }
            updateSQL.append(this.compareData(modifiedTables.get(i), mainDBCols, mainDBDatas, supDBCols, supDBDatas));
        }
    }

    /**
     * 比较数据
     *
     * 查找主键位置，初始化辅助标记数组，查询sup数据库中是否有该主键。
     *  1. 若存在主键，检查列是否相同
     *   1.1 不相同，有新增列，则生成update语句
     *   1.2 相同，检查该条数据是否相同
     *    1.2.1 若相同，则在两个列表中均逻辑删除（mainDBFlag或supDBFlag置为0）
     *    1.2.2 若不相同，则生成update语句
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
                    //列相同，检查数据
                    boolean dataFlag = true;
                    for (int j = 1; j < mainData.length; j++) {
                        if (mainData[j].equals(supData[j])) {
                            dataFlag = false;
                            sqlSB.append(generateSQLService.generateUpdateRecordSQL(tableName, mainDBCols, mainData));
                        }
                    }
                    //数据相同 逻辑删除
                    if (dataFlag) {
                        mainDBFlag[mainDBIndex] = 0;
                        supDBFlag[supDBIndex] = 0;
                    }
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
        System.out.println(sqlSB.toString());
        return sqlSB.toString();
    }


}
