package cn.showclear.www.dao.base.table.impl;

import cn.showclear.utils.DBConnectUtil;
import cn.showclear.utils.FileConnectUtil;
import cn.showclear.www.common.constant.CommonConstant;
import cn.showclear.www.dao.base.table.TableDao;
import cn.showclear.www.pojo.base.ColumnDo;
import cn.showclear.www.pojo.base.TableDo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * @author Wang Junbo
 * @description
 * @date 2019/6/18
 */
@Repository
public class TableDaoImpl implements TableDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(TableDaoImpl.class);

    @Autowired
    private DBConnectUtil dbConnectUtil;

    /**
     * 获取指定数据库的表信息
     * @param databaseName
     * @return
     * @throws SQLException
     */
    @Override
    public List<TableDo> getDBTableInfo(String databaseName) throws SQLException {
        List<TableDo> tableDoList = new ArrayList<TableDo>();
        String sql = "SELECT TABLE_NAME, AUTO_INCREMENT FROM TABLES WHERE TABLE_SCHEMA = ?";
        //获得information_schema数据库的连接，information_schema数据库存放了表结构和列信息
        Connection conn = dbConnectUtil.getConnecttion(CommonConstant.IS_DB_DATASOURCE_NAME);
        PreparedStatement prepStmt = conn.prepareStatement(sql);
        prepStmt.setString(1, databaseName);
        ResultSet rs = prepStmt.executeQuery();
        while (rs.next()) {
            TableDo tableDo = new TableDo();
            tableDo.setTableName(rs.getString("TABLE_NAME"));
            tableDo.setAutoIncrement(rs.getInt("AUTO_INCREMENT"));
            if (tableDo.getAutoIncrement() == null) {
                LOGGER.error("自增字段为空！");
            }
            tableDoList.add(tableDo);
        }
        dbConnectUtil.releaseConnect(conn, prepStmt, rs);
        return tableDoList;
    }

    /**
     * 根据数据库名和表名从数据库查询列信息
     * @param databaseName
     * @return
     * @throws SQLException
     */
    @Override
    public List<ColumnDo> getDBColumnInfo(String databaseName, String tableName) throws SQLException {
        List<ColumnDo> columnDoList = new ArrayList<ColumnDo>();
        String sql = "SELECT COLUMN_NAME, ORDINAL_POSITION, COLUMN_DEFAULT, IS_NULLABLE, " +
                "COLUMN_TYPE, COLUMN_KEY, EXTRA, COLUMN_COMMENT FROM COLUMNS WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?";
        //获得information_schema数据库的连接，information_schema数据库存放了表结构和列信息
        Connection conn = dbConnectUtil.getConnecttion(CommonConstant.IS_DB_DATASOURCE_NAME);
        PreparedStatement prepStmt = conn.prepareStatement(sql);
        prepStmt.setString(1, databaseName);
        prepStmt.setString(2, tableName);
        ResultSet rs = prepStmt.executeQuery();
        while (rs.next()) {
            ColumnDo columnDo = new ColumnDo();
            columnDo.setTableName(tableName);
            columnDo.setColumnName(rs.getString("COLUMN_NAME"));
            columnDo.setOrdinalPosition(rs.getLong("ORDINAL_POSITION"));
            columnDo.setColumnDefault(rs.getString("COLUMN_DEFAULT"));
            columnDo.setIsNullable(rs.getString("IS_NULLABLE"));
            columnDo.setColumnType(rs.getString("COLUMN_TYPE"));
            columnDo.setColumnKey(rs.getString("COLUMN_KEY"));
            columnDo.setExtra(rs.getString("EXTRA"));
            columnDo.setColumnComment(rs.getString("COLUMN_COMMENT"));
            columnDoList.add(columnDo);
        }
        dbConnectUtil.releaseConnect(conn, prepStmt, rs);
        return columnDoList;
    }

    /**
     * 获取备份文件中数据库表信息
     * @return
     */
    @Override
    public List<TableDo> getBackupFileTableInfo() throws IOException {
        Properties properties = new Properties();
        InputStream fileIn = FileConnectUtil.getInputStream();
        properties.load(fileIn);
        return this.arrangeProp(properties);
    }

    /**
     * 添加或修改多个数据库表属性
     * @param tables
     * @return
     */
    @Override
    public boolean updateDBProperties(List<TableDo> tables) {
        if (tables == null || tables.size() == 0) {
            return true;
        }
        boolean updateResult = true;
        Properties properties = new Properties();
        InputStream fileIn = null;
        OutputStream fileOut = null;
        try {
            fileIn = FileConnectUtil.getInputStream();
            //载入配置文件数据到输入流中
            properties.load(fileIn);
            fileOut = FileConnectUtil.getOutputStream();
        } catch (IOException e) {
            LOGGER.error("加载配置文件失败！");
            updateResult = false;
        }
        for (int i = 0; i < tables.size(); i++) {
            this.setProperties(tables.get(i), properties);
        }
        try {
            properties.store(fileOut, "");
        } catch (IOException e) {
            LOGGER.error("property写入输出流失败！");
            updateResult = false;
        } finally {
            try {
                fileIn.close();
                fileOut.close();
            } catch (IOException e) {
                LOGGER.error("流关闭失败！");
                updateResult = false;
            }
        }
        return updateResult;
    }

    /**
     * 添加或修改单个数据库表属性
     * @param table
     * @return
     */
    @Override
    public boolean updateDBProperty(TableDo table) {
        if (table == null) {
            return true;
        }
        if (!this.validateArgu(table)) {
            return false;
        }
        boolean updateResult = true;
        Properties properties = new Properties();
        InputStream fileIn = null;
        OutputStream fileOut = null;
        try {
            fileIn = FileConnectUtil.getInputStream();
            //载入配置文件数据到输入流中
            properties.load(fileIn);
            fileOut = FileConnectUtil.getOutputStream();
        } catch (IOException e) {
            LOGGER.error("加载配置文件失败！");
            updateResult = false;
        }
        try {
            setProperties(table, properties);
            properties.store(fileOut, "");
        } catch (IOException e) {
            LOGGER.error("property写入输出流失败！");
            updateResult = false;
        } finally {
            try {
                fileIn.close();
                fileOut.close();
            } catch (IOException e) {
                LOGGER.error("流关闭失败！");
            }
        }
        return updateResult;
    }

    private void setProperties(TableDo table, Properties properties) {
        String key = "";
        String value = "";
        StringBuilder sb = new StringBuilder();
        //存表名
        sb.delete(0, sb.length());
        key = sb.append(table.getTableName()).append(".").append("tableName").toString();
        value = table.getTableName();
        properties.setProperty(key, value);
        //存表创建时间
        sb.delete(0, sb.length());
        key = sb.append(table.getTableName()).append(".").append("createTime").toString();
        value = String.valueOf(table.getCreateTime());
        properties.setProperty(key, value);
        //存表修改时间
        sb.delete(0, sb.length());
        key = sb.append(table.getTableName()).append(".").append("updateTime").toString();
        value = String.valueOf(table.getUpdateTime());
        properties.setProperty(key, value);
    }

    /**
     * 检查参数
     * @param table
     * @return
     */
    private boolean validateArgu(TableDo table) {
        if (StringUtils.isEmpty(table.getTableName()) || table.getCreateTime() == null || table.getUpdateTime() == null) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 封装备份文件中数据库表信息，并返回
     * @param properties
     * @return
     */
    private List<TableDo> arrangeProp(Properties properties) {
        Set<Object> propSet = properties.keySet();
        List<TableDo> tables = new ArrayList<TableDo>();
        for (Object prop : propSet) {
            if (prop.toString().contains("tableName")) {
                TableDo tableDo = new TableDo();
                String tableName = properties.getProperty(prop.toString());
                tableDo.setTableName(tableName);
                String createTime = properties.getProperty(tableName + ".createTime");
                tableDo.setCreateTime(Long.parseLong(createTime));
                String updateTime = properties.getProperty(tableName + ".updateTime");
                tableDo.setUpdateTime(Long.parseLong(updateTime));
                tables.add(tableDo);
            }
        }
        return tables;
    }

}
