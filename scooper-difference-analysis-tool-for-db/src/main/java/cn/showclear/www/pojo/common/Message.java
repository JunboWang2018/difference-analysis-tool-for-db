package cn.showclear.www.pojo.common;

/**
 * @author Wang Junbo
 * @description
 * @date 2019/6/18
 */
public class Message {
    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Message(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Message(int code) {
        this.code = code;
        this.message = "";
    }
}
