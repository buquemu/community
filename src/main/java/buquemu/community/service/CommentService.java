package buquemu.community.service;

import buquemu.community.enums.CommentTypeEnum;
import buquemu.community.exception.CustomErrorCode;
import buquemu.community.exception.CustomException;
import buquemu.community.mapper.CommentMapper;
import buquemu.community.mapper.QuestionMapper;
import buquemu.community.model.Comment;
import buquemu.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    public void insert(Comment comment) {
//        没选中问题
        if(comment.getParentId()==null||comment.getParentId()==0){
            throw new CustomException(CustomErrorCode.QUESTION_NOT_CHOOSE);
        }
        if (comment.getType()==null||!CommentTypeEnum.inClude(comment.getType())){
            throw new CustomException(CustomErrorCode.QUESTION_NOT_CHOOSE);
        }
        if (comment.getType()==CommentTypeEnum.COMMENT.getType()){
//            回复评论
            Comment huifu = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(huifu == null){
                throw new CustomException(CustomErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
        }else {
//            回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question==null){
                throw new CustomException(CustomErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setViewCount(1);
            questionMapper.addComment(question);

        }
    }
}
