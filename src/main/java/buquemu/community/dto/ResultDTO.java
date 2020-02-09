package buquemu.community.dto;

import buquemu.community.exception.CustomErrorCode;
import buquemu.community.exception.CustomException;
import lombok.Data;

@Data
public class ResultDTO {
//    错误码
    private Integer code;
    private String message;

    public static ResultDTO errorOf(Integer code,String message){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }
    public static ResultDTO errorOf(CustomErrorCode customErrorCode) {
        return errorOf(customErrorCode.getCode(),customErrorCode.getMessage());
    }

    public static ResultDTO errorOf(CustomException throwable) {
        return errorOf(throwable.getCode(),throwable.getMessage());
    }

    public static ResultDTO okOf() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("登录成功");
        return resultDTO;
    }
}
