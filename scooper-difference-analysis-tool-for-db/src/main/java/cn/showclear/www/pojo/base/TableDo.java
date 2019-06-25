package cn.showclear.www.pojo.base;

/**
 * @author Wang Junbo
 * @description 表
 * @date 2019/6/18
 */
public class TableDo {
    private String tableName;
    private Integer autoIncrement;
    private Long createTime;
    private Long updateTime;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(Integer autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "TableDo{" +
                "tableName='" + tableName + '\'' +
                ", autoIncrement=" + autoIncrement +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
