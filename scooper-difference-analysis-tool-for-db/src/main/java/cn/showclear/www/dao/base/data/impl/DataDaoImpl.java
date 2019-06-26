package cn.showclear.www.dao.base.data.impl;

import cn.showclear.utils.DBConnectUtil;
import cn.showclear.www.dao.base.data.DataDao;
import cn.showclear.www.pojo.base.ColumnDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Wang Junbo
 * @description
 * @date 2019/6/20
 */
@Repository
public class DataDaoImpl implements DataDao {

    @Autowired
    private DBConnectUtil dbConnectUtil;

    /**
     * 根据列信息获得所有数据，数据按ordinalPosition序号来存入字符串数组中。
     * @param columns
     * @return
     */
    @Override
    public List<String[]> getDataList(List<ColumnDo> columns, String dataSourceName) throws SQLException {
        List<String[]> strsList = new ArrayList<String[]>();
        String querySQL = generateQuerySQL(columns);
        Connection conn = dbConnectUtil.getConnecttion(dataSourceName);
        PreparedStatement prepStmt = conn.prepareStatement(querySQL);
        ResultSet rs = prepStmt.executeQuery();
        int i = 0;
        while (rs.next()) {
            String[] strs = new String[columns.size() + 1]; //0不用
            i++;
            for (ColumnDo column : columns) {
                int index = column.getOrdinalPosition().intValue();
                strs[index] = rs.getString(column.getColumnName());
            }
            strsList.add(strs);
        }
        dbConnectUtil.releaseConnect(conn, prepStmt, rs);
        return strsList;
    }

    /**
     * 生成查询数据SQL
     * @return
     */
    private String generateQuerySQL(List<ColumnDo> columns) {
        StringBuilder sqlSB = new StringBuilder();
        sqlSB.append("SELECT ");
        for (int i = 0; i < columns.size(); i++) {
            ColumnDo column = columns.get(i);
            if (i != columns.size() - 1) {
                sqlSB.append(column.getColumnName() + ",");
            } else {
                sqlSB.append(column.getColumnName());
            }
            /*if ((i != columns.size() - 1) && (!CommonConstant.AUTO_INCREMENT.equals(column.getExtra()))) {
                sqlSB.append(column.getColumnName() + ",");
            }
            if ((i == columns.size() - 1) && (!CommonConstant.AUTO_INCREMENT.equals(column.getExtra()))) {
                sqlSB.append(column.getColumnName());
            }*/
        }
        sqlSB.append(" FROM " + columns.get(columns.size() - 1).getTableName());
        return sqlSB.toString();
    }

}
