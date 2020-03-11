package buquemu.community.controller;

import buquemu.community.dto.CommentDTO;
import buquemu.community.dto.CommentPageDTO;
import buquemu.community.dto.LikeCount;
import buquemu.community.dto.ResultDTO;
import buquemu.community.enums.CommentTypeEnum;
import buquemu.community.exception.CustomErrorCode;
import buquemu.community.model.Comment;
import buquemu.community.model.User;
import buquemu.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

//评论
    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentPageDTO commentPageDTO,
                       HttpServletRequest request
    ) {
        User user = (User) request.getSession().getAttribute("githubuser");
        if (user == null) {
//            自定义的错误码 给前端去判断 返回一个json
            return ResultDTO.errorOf(CustomErrorCode.NO_LOGIN);
        }
//        评论内容为空
        if (commentPageDTO.getContent() == null || commentPageDTO.getContent() == "") {
            return ResultDTO.errorOf(CustomErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        comment.setParentId(commentPageDTO.getParentId());
        comment.setContent(commentPageDTO.getContent());
        comment.setType(commentPageDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0);
        comment.setCommentCount(0);
        commentService.insert(comment);
//        返回一个带有唯一标识的code码
        return ResultDTO.okOf();
    }

    @ResponseBody
    @GetMapping("/comment/{parentId}")
    public ResultDTO comments(@PathVariable(name = "parentId") Integer id) {
//        获取目标id  二级评论
//        返回一个list 列表
        List<CommentDTO> twocomment  = commentService.ListByTargetId(id, CommentTypeEnum.COMMENT.getType());
        return ResultDTO.okOf(twocomment);
    }


    @ResponseBody
    @PostMapping("/comment/dianzan")
    public ResultDTO LikeCount(@RequestBody LikeCount likeCountId,
                               HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("githubuser");
        if (user == null) {
            return ResultDTO.errorOf(CustomErrorCode.NO_LOGIN);
        }
        if (likeCountId == null) {
            return ResultDTO.errorOf(CustomErrorCode.DIANZAN_IS_NULL);
        }
        int likeCountNumber = commentService.LikeCount(likeCountId, user);
        return ResultDTO.okOf(likeCountNumber);
    }


}
