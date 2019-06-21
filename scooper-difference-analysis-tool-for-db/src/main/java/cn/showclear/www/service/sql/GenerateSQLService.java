package cn.showclear.www.service.sql;

import cn.showclear.www.pojo.base.ColumnDo;

public interface GenerateSQLService {
    /**
     * 生成新增字段SQL语句
     * @param columnDo
     * @return
     */
    String generateAddColumnSQL(ColumnDo columnDo);


    String generateAddRecordSQL();
}
