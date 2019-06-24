package cn.showclear.www.service.sql.impl;

import cn.com.scooper.common.exception.BusinessException;
import cn.showclear.www.common.constant.CommonConstant;
import cn.showclear.www.pojo.base.ColumnDo;
import cn.showclear.www.service.sql.GenerateSQLService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * @author Wang Junbo
 * @description
 * @date 2019/6/20
 */
@Service
public class GenerateSQLServiceImpl implements GenerateSQLService {

    /**
     * 生成新增字段SQL语句
     * @param columnDo
     * @return
     */
    @Override
    public String generateAddColumnSQL(ColumnDo columnDo) {
        if (!isLegal(columnDo)) {
            return "";
        }
        StringBuilder addColSQL = new StringBuilder();
        addColSQL.append("ALTER TABLE " + columnDo.getTableName() + " ADD COLUMN " + columnDo.getColumnName() + " " + columnDo.getColumnType());
        if (CommonConstant.NOT_NULLABLE.equals(columnDo.getIsNullable())) {
            addColSQL.append(" NOT NULL");
        } else {
            addColSQL.append(" NULL");
        }
        if (!StringUtils.isEmpty(columnDo.getColumnDefault())) {
            addColSQL.append(" DEFAULT '" + columnDo.getColumnDefault() + "'");
        }
        if (!StringUtils.isEmpty(columnDo.getExtra())) {
            addColSQL.append(" " + columnDo.getExtra());
        }
        if (!StringUtils.isEmpty(columnDo.getColumnComment())) {
            addColSQL.append(" COMMENT '" + columnDo.getColumnComment() + "'");
        }
        addColSQL.append(";");
        return addColSQL.toString();
    }

    /**
     * 检查新增列参数是否合法
     * @param columnDo
     * @return
     */
    private boolean isLegal(ColumnDo columnDo) {
        if (columnDo == null) {
            return false;
        }
        if (StringUtils.isEmpty(columnDo.getTableName()) || StringUtils.isEmpty(columnDo.getColumnName())
                || StringUtils.isEmpty(columnDo.getColumnType())) {
            return false;
        }
        return true;
    }

    /**
     * 生成插入数据SQL语句
     * @param tableName
     * @param columns
     * @param data
     * @return
     */
    @Override
    public String generateAddRecordSQL(String tableName, List<ColumnDo> columns, String[] data) {
        StringBuilder sbSQL = new StringBuilder();
        //按ordinal position排序，使之与data数组数据顺序对应
        columns.sort(new Comparator<ColumnDo>() {
            @Override
            public int compare(ColumnDo o1, ColumnDo o2) {
                return o1.getOrdinalPosition().intValue() - o1.getOrdinalPosition().intValue();
            }
        });
        sbSQL.append("INSERT INTO " + tableName + "(");
        for (int i = 0; i < columns.size(); i++) {
            ColumnDo column = columns.get(i);
            if (i != columns.size() - 1) {
                sbSQL.append(column.getColumnName()).append(",");
            } else {
                sbSQL.append(column.getColumnName());
            }
        }
        sbSQL.append(") VALUES(");
        for (int i = 0; i < data.length; i++) {
            //字符串类型和时间类型加引号
            if (columns.get(i).getColumnType().toLowerCase().contains("char") || columns.get(i).getColumnType().toLowerCase().contains("text")
                    || columns.get(i).getColumnType().toLowerCase().contains("blob") || columns.get(i).getColumnType().toLowerCase().contains("enum")
                    || columns.get(i).getColumnType().toLowerCase().contains("set") || columns.get(i).getColumnType().toLowerCase().contains("time")) {
                if (i != data.length - 1) {
                    sbSQL.append("'").append(data[i]).append("'").append(",");
                } else {
                    sbSQL.append("'").append(data[i]).append("'");
                }

            }
            //整形和浮点型不加引号
            if (columns.get(i).getColumnType().toLowerCase().contains("int") || columns.get(i).getColumnType().toLowerCase().equals("long")
                    || columns.get(i).getColumnType().toLowerCase().contains("float") || columns.get(i).getColumnType().toLowerCase().contains("double")) {
                if (i != data.length - 1) {
                    sbSQL.append(data[i]).append(",");
                } else {
                    sbSQL.append(data[i]);
                }
            }

        }
        sbSQL.append(");");
        return sbSQL.toString();
    }

    /**
     * 生成修改数据SQL语句
     * @param tableName
     * @param columns
     * @param data
     * @return
     */
    @Override
    public String generateUpdateRecordSQL(String tableName, List<ColumnDo> columns, String[] data) {
        //寻找主键字段和位置
        int priColIndex = Integer.MAX_VALUE;
        String priColName = null;
        StringBuilder sbSQL = new StringBuilder();
        for (ColumnDo column : columns) {
            if (column.getColumnKey().equalsIgnoreCase("PRI")) {
                priColIndex = column.getOrdinalPosition().intValue();
                priColName = column.getColumnName();
            }
        }
        sbSQL.append("UPDATE ").append(tableName).append(" SET ");
        for (int i = 0; i < columns.size(); i++) {
            if (!columns.get(i).getColumnKey().equalsIgnoreCase("PRI")) {
                if (i != columns.size() - 1) {
                    sbSQL.append(columns.get(i).getColumnName()).append(" = ").append(data[columns.get(i).getOrdinalPosition().intValue()]).append(",");
                } else {
                    sbSQL.append(columns.get(i).getColumnName()).append(" = ").append(data[columns.get(i).getOrdinalPosition().intValue()]);
                }
            }
        }
        sbSQL.append(" WHERE ").append(priColName).append(" = '").append(data[priColIndex]).append("';");
        return sbSQL.toString();
    }

    /**
     * 生成删除数据SQL语句
     *  1. 从column信息中查询主键位置
     *  2. 取出主键数据
     *  3. 删除数据
     * @param tableName
     * @param columns
     * @param data
     * @return
     */
    @Override
    public String generateDeleteRecordSQL(String tableName, List<ColumnDo> columns, String[] data) throws BusinessException {
        //寻找主键字段和位置
        int priColIndex = Integer.MAX_VALUE;
        String priColName = null;
        StringBuilder sbSQL = new StringBuilder();
        for (ColumnDo column : columns) {
            if (column.getColumnKey().equalsIgnoreCase("PRI")) {
                priColIndex = column.getOrdinalPosition().intValue();
                priColName = column.getColumnName();
            }
        }
        if (priColIndex == Integer.MAX_VALUE || StringUtils.isEmpty(priColName)) {
            throw new BusinessException(CommonConstant.FAILED_CODE, "没有找到" + tableName + "表的主键！");
        }
        sbSQL.append("DELETE FROM ").append(tableName).append(" WHERE ").append(priColName).append(" = '").append(data[priColIndex]).append("';");
        return sbSQL.toString();
    }


}
