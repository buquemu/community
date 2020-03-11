package buquemu.community.dto;

import buquemu.community.exception.CustomErrorCode;
import buquemu.community.exception.CustomException;
import lombok.Data;

import java.util.List;

@Data
public class ResultDTO {
//    错误码
    private Integer code;
    private String message;
    private Integer likeCount;
    private List Data;

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

    public static ResultDTO okOf(List data) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setData(data);
        resultDTO.setCode(200);
        resultDTO.setMessage("登录成功");
        return resultDTO;
    }

    public static ResultDTO okOf(int likeCount) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("登录成功");
        resultDTO.setLikeCount(likeCount);
        return resultDTO;
    }
}
