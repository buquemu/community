package buquemu.community.controller;

import buquemu.community.dto.CommentDTO;
import buquemu.community.dto.ResultDTO;
import buquemu.community.exception.CustomErrorCode;
import buquemu.community.model.Comment;
import buquemu.community.model.User;
import buquemu.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;


    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request
    ){
        User user = (User) request.getSession().getAttribute("githubuser");
        if(user==null){
//            自定义的错误码 给前端去判断 返回一个json
            return ResultDTO.errorOf(CustomErrorCode.NO_LOGIN);
        }
        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(1L);
        commentService.insert(comment);
//        返回一个带有唯一标识的code码
        return ResultDTO.okOf();
    }
}
