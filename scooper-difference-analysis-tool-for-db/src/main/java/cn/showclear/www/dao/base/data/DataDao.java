package cn.showclear.www.dao.base.data;

import cn.showclear.www.pojo.base.ColumnDo;

import java.sql.SQLException;
import java.util.List;

public interface DataDao {
    /**
     * 根据列信息获得所有数据
     * @param columns
     * @return
     */
    List<String[]> getDataList(List<ColumnDo> columns, String dataSourceName) throws SQLException;

}
