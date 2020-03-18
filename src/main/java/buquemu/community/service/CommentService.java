package buquemu.community.service;

import buquemu.community.dto.CommentDTO;
import buquemu.community.dto.LikeCount;
import buquemu.community.enums.CommentTypeEnum;
import buquemu.community.enums.TongZhiEnum;
import buquemu.community.enums.TongZhiStatusEnum;
import buquemu.community.exception.CustomErrorCode;
import buquemu.community.exception.CustomException;
import buquemu.community.mapper.*;
import buquemu.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private PraiseMapper praiseMapper;

    @Transactional
    public void insert(Comment comment) {
//        没选中问题
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomException(CustomErrorCode.QUESTION_NOT_CHOOSE);
        }
        if (comment.getType() == null || !CommentTypeEnum.inClude(comment.getType())) {
            throw new CustomException(CustomErrorCode.QUESTION_NOT_CHOOSE);
        }

        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
//            回复评论
            Comment huifu = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (huifu == null) {
                throw new CustomException(CustomErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
            huifu.setCommentCount(1);
            commentMapper.addCommentCount(huifu);


                //添加通知
                Notice notice = new Notice();
                notice.setGmtCreate(System.currentTimeMillis());
                notice.setType(TongZhiEnum.REPLAY_COMMENT.getType());
                notice.setOuterid(comment.getParentId());
//  接收者       评论的创建人
                notice.setNotofier(comment.getCommentator());
                notice.setStatus(TongZhiStatusEnum.DEFAULT.getStatus());
//  发送者        回复的创建人
                notice.setReceiver(huifu.getCommentator());
                // 先根据2几评论找到1级评论 再根据1级评论找到问题
                Question question = questionMapper.selectByPrimaryKey(huifu.getParentId());
                notice.setTitle(question.getTitle());
                if (huifu.getCommentator()==comment.getCommentator()){
                    return;
                }
                noticeMapper.insert(notice);
        } else {
//            回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomException(CustomErrorCode.QUESTION_NOT_FOUND);
            }
//            添加事务一起成功或一起失败
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionMapper.addComment(question);
            //添加通知
            Notice notice = new Notice();
            notice.setTitle(question.getTitle());
            notice.setGmtCreate(System.currentTimeMillis());
            notice.setType(TongZhiEnum.REPLAY_QUESTION.getType());
            notice.setOuterid(comment.getParentId());
// 接受者      当前问题的创建人
            notice.setNotofier(question.getCreator());
            notice.setStatus(TongZhiStatusEnum.DEFAULT.getStatus());
// 发送者  当前问题的评论人
            notice.setReceiver(comment.getCommentator());
            if(question.getCreator()==comment.getCommentator()){
                return;
            }
            noticeMapper.insert(notice);

        }
    }

    public List<CommentDTO> ListByTargetId(Integer id, Integer type) {
        CommentExample example = new CommentExample();
//        不但要id相同 还得是一级评论
        example.createCriteria().andParentIdEqualTo(id)
// 根据type 判断 1 2 级
                .andTypeEqualTo(type);
        example.setOrderByClause("gmt_create desc");

        List<Comment> comments = commentMapper.selectByExample(example);
//拿到 commentator对应的user信息  防止重复获取 jdk1.8语法
        if (comments.size() == 0) {
            return new ArrayList<>();
        }
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (Comment comment: comments) {
            CommentDTO commentDTO = new CommentDTO();
            User user = userMapper.selectByPrimaryKey(comment.getCommentator());
                commentDTO.setUser(user);
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTOList.add(commentDTO);
        }

        return commentDTOList;

    }

    public int LikeCount(LikeCount likeCountId,User user) {
//        获取点赞的评论
        Comment comment = commentMapper.selectByPrimaryKey(likeCountId.getCommentId());
        if(1 == likeCountId.getType()) {
            //        增加点赞数
            comment.setLikeCount(1);
            commentMapper.addLikeCount(comment);

//            添加点赞到数据库
            PraiseExample example = new PraiseExample();
            example.createCriteria().andCommentidEqualTo(comment.getId()).andUserEqualTo(user.getId());
            List<Praise> praises = praiseMapper.selectByExample(example);
//            没有增加
            if (praises.size()==0){
            Praise praise = new Praise();
            praise.setType(1);
            praise.setUser(user.getId());
            praise.setCommentid(comment.getId());
            praiseMapper.insert(praise);
            }else {
                //有就修改  1待变点过赞了 0代表没点过赞
                praise(comment,user,1);
            }

//  调用通知方法
            addTongzhi(comment,user,likeCountId);
            Comment Returncomment = commentMapper.selectByPrimaryKey(likeCountId.getCommentId());
            return Returncomment.getLikeCount();
        }
        else{
//     前端没有弄好  点赞变成了负数 点的时候就给他+1
                //     减少点赞数
                comment.setLikeCount(1);
                commentMapper.reduceLikeCount(comment);
//                评论id相同 UserId也要相同

//              修改点赞表的status；
                praise(comment,user,0);

                Comment Returncomment = commentMapper.selectByPrimaryKey(likeCountId.getCommentId());
                return Returncomment.getLikeCount();

        }
    }

//    将点赞通知封装
    public void addTongzhi(Comment comment,User user,LikeCount likeCountId){
        //   判断数据库是否有 有就不添加通知了 防止重复添加
        NoticeExample example = new NoticeExample();
        example.createCriteria().andOuteridEqualTo(likeCountId.getCommentId()).andTypeEqualTo(3);
        List<Notice> notices = noticeMapper.selectByExample(example);
        if(notices.size() == 0) {
            //        添加通知
            Notice notice = new Notice();
            notice.setGmtCreate(System.currentTimeMillis());
            notice.setType(TongZhiEnum.REPLAY_DIANZAN.getType());
            notice.setOuterid(comment.getId());
// 发送者       谁点的赞
            notice.setReceiver(user.getId());
//  接收者       评论的创建人
            notice.setNotofier(comment.getCommentator());
            notice.setStatus(TongZhiStatusEnum.DEFAULT.getStatus());
            // 先根据2几评论找到1级评论 再根据1级评论找到问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            notice.setTitle(question.getTitle());
//            自己点赞自己不通知
            if (user.getId()==comment.getCommentator()){
                return;
            }
            noticeMapper.insert(notice);

        }
    }

//    封装一下点赞功能
        public void praise( Comment comment,User user,int i){
        //                修该点赞表 不仅Userid相同 而且必须CommentId也要相同
            Praise record = new Praise();
            record.setType(i);
            PraiseExample example = new PraiseExample();
            example.createCriteria().andCommentidEqualTo(comment.getId()).andUserEqualTo(user.getId());
            praiseMapper.updateByExampleSelective(record, example);
        }


}
