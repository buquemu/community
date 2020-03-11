package buquemu.community.exception;

public enum CustomErrorCode{
    QUESTION_NOT_FOUND(100,"你找的这个问题不在了！哭哭"),
    QUESTION_NOT_CHOOSE(101,"没有选中任何问题或评论"),
    NO_LOGIN(102,"未登录不能评论，请先登录"),
    TYPE_NOT_FOUND_OR_WRONG(103,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(104,"回复的评论不存在"),
    SYS_ERRO(201,"奥里给!服务器坏了!"),
    CONTENT_IS_EMPTY(105,"输入内容不能为空"),
    DIANZAN_IS_NULL(106,"你给谁点赞呢"),
    NOTICE_IS_NOTYOUR(107,"该通知不是你的"),
    NOTICE_TYPE_WRONG(108,"大哥别再地址栏瞎实验了"),
    NOTICE_OUTERID_NOTFOUND(109,"肯定是你在地址栏瞎打id了")

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
