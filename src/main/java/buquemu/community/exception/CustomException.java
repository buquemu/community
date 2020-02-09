package buquemu.community.exception;
//自定义 运行时异常
public class CustomException extends RuntimeException {
    private String message;
    private Integer code;

    public CustomException(CustomErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public Integer getCode() {
        return code;
    }

    //    可以在外层拿到message
    @Override
    public String getMessage() {
        return message;
    }
}
