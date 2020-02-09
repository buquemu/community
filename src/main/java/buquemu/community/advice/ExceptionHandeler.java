package buquemu.community.advice;

import buquemu.community.dto.ResultDTO;
import buquemu.community.exception.CustomErrorCode;
import buquemu.community.exception.CustomException;
import com.alibaba.fastjson.JSON;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//通用异常处理
@ControllerAdvice
public class ExceptionHandeler {
    @ExceptionHandler(Exception.class)
//    不能用responsebody 因为既要判断是否返回json 也要判断是否返回页面
    Object handle(Throwable throwable, Model model,
                  HttpServletRequest request, HttpServletResponse response) {
        String contentType = request.getContentType();
//            返回json
        if ("application/json".equals(contentType)) {
            ResultDTO resultDTO;
            if (throwable instanceof CustomException) {
                resultDTO = ResultDTO.errorOf((CustomException) throwable);
            } else {
                resultDTO = ResultDTO.errorOf(CustomErrorCode.SYS_ERRO);
            }
//最笨的方式将resultDTO写到前端
            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        } else {
//        访问页面异常跳转 text/html
// throwable 是否为 自定义异常的 实例
            if (throwable instanceof CustomException) {
                model.addAttribute("message", throwable.getMessage());
            } else {
                model.addAttribute("message", CustomErrorCode.SYS_ERRO.getMessage());
            }
            return new ModelAndView("error");
        }
    }
}