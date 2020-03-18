package buquemu.community.controller;

import buquemu.community.enums.TongZhiEnum;
import buquemu.community.exception.CustomErrorCode;
import buquemu.community.exception.CustomException;
import buquemu.community.mapper.CommentMapper;
import buquemu.community.mapper.NoticeMapper;
import buquemu.community.mapper.QuestionMapper;
import buquemu.community.model.*;
import buquemu.community.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class NoticeController {

    @Autowired
    CommentMapper commentMapper;
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    NoticeMapper noticeMapper;
    @Autowired
    NoticeService noticeService;

    @GetMapping("/notice/{id}/{type}/{zhujian}")
    public String title(@PathVariable("id") int id,
                        @PathVariable("type") int type,
                        @PathVariable("zhujian") int zhujian,
                        HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("githubuser");
        if (user == null) {
            return "redirect:/";
        }

//        这一块都是get校验
//        判断这个通知是不是自己的
        NoticeExample noticeExample = new NoticeExample();
        noticeExample.createCriteria().andOuteridEqualTo(id);
        List<Notice> notices = noticeMapper.selectByExample(noticeExample);
        //   地址栏先输入了不存在的outerid
        if(notices.size() == 0){
            throw new CustomException(CustomErrorCode.NOTICE_OUTERID_NOTFOUND);
        }

        //判断get提交的type
        if(!TongZhiEnum.inClude(type)){
            throw new CustomException(CustomErrorCode.NOTICE_TYPE_WRONG);
        }
//
//
//        // 他有并且不是当前用户的 抛异常 outerId 和type都正确
//        for (Notice notice : notices) {
//               if( notices.size()!=0 && !notice.getNotofier().equals(user.getId())){
//                   throw new CustomException(CustomErrorCode.NOTICE_IS_NOTYOUR);
//               }
//        }

        //        点击跳转将未读变成已读
        noticeService.read(id,type,zhujian);



//  可以直接根据outerId跳转到对应question
        if (TongZhiEnum.REPLAY_QUESTION.getType() == type) {
            return "redirect:/question/" + id;
        }


//        找到评论
        Comment comment = commentMapper.selectByPrimaryKey(id);

//        找到问题
        Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
        return "redirect:/question/"+ question.getId() ;
    }
}