package buquemu.community.controller;

import buquemu.community.dto.LikeCount;
import buquemu.community.dto.ResultDTO;
import buquemu.community.exception.CustomErrorCode;
import buquemu.community.model.User;
import buquemu.community.service.PraiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PraiseController {
    @Autowired
    PraiseService praiseService;
    @ResponseBody
    @PostMapping("/praise/dianzan")
    public ResultDTO LikeCount(@RequestBody LikeCount likeCountId,
                               HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("githubuser");
        if (user == null) {
            return ResultDTO.errorOf(CustomErrorCode.NO_LOGIN);
        }
        if (likeCountId == null) {
            return ResultDTO.errorOf(CustomErrorCode.DIANZAN_IS_NULL);
        }
//        根据评论id去找
        int status = praiseService.findStatus(likeCountId.getCommentId(), user);
        return ResultDTO.praise(status);
    }
}
