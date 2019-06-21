package cn.showclear.utils;

import cn.showclear.www.common.constant.CommonConstant;
import org.springframework.jdbc.support.JdbcUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Wang Junbo
 * @description 数据库连接工具类
 * @date 2019/6/18
 */
public class DBConnectUtil {

    @Resource(name = "mainDBDataSource")
    private DataSource mainDBDataSource;

    @Resource(name = "supDBDataSource")
    private DataSource supDBDataSource;

    @Resource(name = "isDBDataSource")
    private DataSource isDBDataSource;

    /**
     * 根据dataSource id获取连接
     * @param dataSourceId
     * @return
     * @throws SQLException
     */
    public Connection getConnecttion(String dataSourceId) throws SQLException {
        if (CommonConstant.MAIN_DB_DATASOURCE_NAME.equals(dataSourceId)) {
            return mainDBDataSource.getConnection();
        } else if (CommonConstant.SUP_DB_DATASOURCE_NAME.equals(dataSourceId)) {
            return supDBDataSource.getConnection();
        } else if (CommonConstant.IS_DB_DATASOURCE_NAME.equals(dataSourceId)) {
            return isDBDataSource.getConnection();
        } else {
            return null;
        }
    }

    /**
     * 释放资源
     * @param connection
     * @param preparedStatement
     * @param resultSet
     */
    public void releaseConnect(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        JdbcUtils.closeResultSet(resultSet);
        JdbcUtils.closeStatement(preparedStatement);
        JdbcUtils.closeConnection(connection);
    }

    /**
     * 是否完成连接
     * @return
     */
    public boolean isConnected() {
        if (mainDBDataSource != null && supDBDataSource != null && isDBDataSource != null) {
            return true;
        } else {
            return false;
        }
    }

    public DataSource getMainDBDataSource() {
        return mainDBDataSource;
    }

    public void setMainDBDataSource(DataSource mainDBDataSource) {
        this.mainDBDataSource = mainDBDataSource;
    }

    public DataSource getSupDBDataSource() {
        return supDBDataSource;
    }

    public void setSupDBDataSource(DataSource supDBDataSource) {
        this.supDBDataSource = supDBDataSource;
    }

    public DataSource getIsDBDataSource() {
        return isDBDataSource;
    }

    public void setIsDBDataSource(DataSource isDBDataSource) {
        this.isDBDataSource = isDBDataSource;
    }
}
