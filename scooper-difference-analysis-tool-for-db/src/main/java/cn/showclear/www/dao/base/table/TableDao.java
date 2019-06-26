package cn.showclear.www.dao.base.table;

import cn.com.scooper.common.exception.BusinessException;
import cn.showclear.www.pojo.base.ColumnDo;
import cn.showclear.www.pojo.base.TableDo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface TableDao {
    /**
     * 获取指定数据库的表信息
     * @param databaseName
     * @return
     * @throws SQLException
     */
    List<TableDo> getDBTableInfo(String databaseName) throws SQLException, BusinessException;

    /**
     * 根据数据库名和表名获取数据库中列信息
     * @return
     */
    List<ColumnDo> getDBColumnInfo(String databaseName, String tableName) throws SQLException;

    /**
     * 获取备份文件中数据库表信息
     * @return
     */
    List<TableDo> getBackupFileTableInfo() throws IOException;

    /**
     * 添加或修改多个数据库表属性
     * @param tables
     * @return
     */
    boolean updateDBProperties(List<TableDo> tables);

    /**
     * 添加或修改单个数据库表属性
     * @param table
     * @return
     */
    boolean updateDBProperty(TableDo table);
}
