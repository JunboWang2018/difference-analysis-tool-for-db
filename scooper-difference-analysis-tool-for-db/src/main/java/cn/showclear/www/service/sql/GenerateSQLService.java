package cn.showclear.www.service.sql;

import cn.com.scooper.common.exception.BusinessException;
import cn.showclear.www.pojo.base.ColumnDo;

import java.util.List;

public interface GenerateSQLService {
    /**
     * 生成新增字段SQL语句
     * @param columnDo
     * @return
     */
    String generateAddColumnSQL(ColumnDo columnDo);

    /**
     * 生成插入数据SQL语句
     * @param tableName
     * @param data
     * @return
     */
    String generateAddRecordSQL(String tableName, List<ColumnDo> columns, String[] data);

    /**
     * 生成修改数据SQL语句
     * @param tableName
     * @param data
     * @return
     */
    String generateUpdateRecordSQL(String tableName, List<ColumnDo> columns, String[] data);

    /**
     * 生成删除数据SQL语句
     * @param tableName
     * @param data
     * @return
     */
    String generateDeleteRecordSQL(String tableName, List<ColumnDo> columns, String[] data) throws BusinessException;

}
