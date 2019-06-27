package cn.showclear.www.pojo.base;

/**
 * @author Wang Junbo
 * @description 历史记录
 * @date 2019/6/27
 */
public class HistoryQo {
    private Integer id;
    private String date;
    private String path;
    private String fileName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "HistoryQo{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", path='" + path + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
