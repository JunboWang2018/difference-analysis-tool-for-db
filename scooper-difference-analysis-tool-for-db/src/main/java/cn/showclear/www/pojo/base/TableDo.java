package cn.showclear.www.pojo.base;

/**
 * @author Wang Junbo
 * @description 表
 * @date 2019/6/18
 */
public class TableDo {
    private String tableName;
    private Long createTime;
    private Long updateTime;
    private String engine;
    private String tableComment;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
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

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }


    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    @Override
    public String toString() {
        return "TableDo{" +
                "tableName='" + tableName + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", engine='" + engine + '\'' +
                ", tableComment='" + tableComment + '\'' +
                '}';
    }
}
