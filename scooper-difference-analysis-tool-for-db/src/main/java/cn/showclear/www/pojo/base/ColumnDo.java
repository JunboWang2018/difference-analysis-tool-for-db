package cn.showclear.www.pojo.base;

/**
 * @author Wang Junbo
 * @description
 * @date 2019/6/18
 */
public class ColumnDo {
    private String tableName;
    private String columnName;
    private Long ordinalPosition;
    private String columnDefault;
    private String isNullable;
    private String columnType;
    private String columnKey;
    private String extra;
    private String columnComment;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Long getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(Long ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    public String getColumnDefault() {
        return columnDefault;
    }

    public void setColumnDefault(String columnDefault) {
        this.columnDefault = columnDefault;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    @Override
    public String toString() {
        return "ColumnDo{" +
                "tableName='" + tableName + '\'' +
                ", columnName='" + columnName + '\'' +
                ", ordinalPosition=" + ordinalPosition +
                ", columnDefault='" + columnDefault + '\'' +
                ", isNullable='" + isNullable + '\'' +
                ", columnType='" + columnType + '\'' +
                ", columnKey='" + columnKey + '\'' +
                ", extra='" + extra + '\'' +
                ", columnComment='" + columnComment + '\'' +
                '}';
    }
}
