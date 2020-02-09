package buquemu.community.exception;

public enum CustomErrorCode{
    QUESTION_NOT_FOUND(100,"你找的这个问题不在了！哭哭"),
    QUESTION_NOT_CHOOSE(101,"没有进行任何选择"),
    NO_LOGIN(102,"未登录不能评论"),
    SYS_ERRO(200,"奥里给!服务器坏了!"),
    TYPE_NOT_FOUND_OR_WRONG(103,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(104,"回复的评论不存在")

    ;
    private String message;
    private Integer code;

    CustomErrorCode(Integer code,String message) {
        this.message = message;
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public Integer getCode() {
        return code;
    }
}
