package buquemu.community.service;

import buquemu.community.dto.CommentDTO;
import buquemu.community.exception.CustomErrorCode;
import buquemu.community.exception.CustomException;
import buquemu.community.model.Question;
import buquemu.community.model.QuestionExample;
import buquemu.community.model.User;
import buquemu.community.dto.PageDTO;
import buquemu.community.dto.QuestionDTO;
import buquemu.community.mapper.QuestionMapper;
import buquemu.community.mapper.UserMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    UserMapper userMapper;
    public PageDTO list(Integer page, Integer size,String tag) {

//        根据tag是否为空 判断是否市热门标签
        if(tag.length()>1){
            QuestionExample example = new QuestionExample();
            example.createCriteria().andTagLike("%"+tag+"%");
            Integer totalCount = (int)questionMapper.countByExample(example);
            Integer totalPage = (totalCount % size == 0) ? totalCount / size : totalCount / size + 1;
            //数据校验放置page越界
            if(page<1){
                page=1;
            }
            if(page>totalPage){
                page=totalPage;
            }
//        数据库查询页数
            Integer yeshu = size*(page-1);
            //分页 。。方法            limit 0 5
            QuestionExample example1 = new QuestionExample();
            example1.createCriteria().andTagLike("%"+tag+"%");
            example1.setOrderByClause("gmt_create desc");
            List<Question> questions = questionMapper.selectByExampleWithRowbounds(example1,new RowBounds(yeshu,size));

            List<QuestionDTO> questionDTOList = new ArrayList<>();
            PageDTO pageDTO = new PageDTO();
            for (Question question : questions) {
                User user = userMapper.selectByPrimaryKey(question.getCreator());
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(question,questionDTO);
                questionDTO.setUser(user);
                questionDTOList.add(questionDTO);
            }
            pageDTO.setData(questionDTOList);

            pageDTO.setPagination(totalPage,page);
            return pageDTO;
        }else {
            Integer totalCount = (int) questionMapper.countByExample(new QuestionExample());
            Integer totalPage = (totalCount % size == 0) ? totalCount / size : totalCount / size + 1;
            //数据校验放置page越界
            if (page < 1) {
                page = 1;
            }
            if (page > totalPage) {
                page = totalPage;
            }
//        数据库查询页数
            Integer yeshu = size * (page - 1);
            //分页 。。方法            limit 0 5
            QuestionExample example = new QuestionExample();
            example.setOrderByClause("gmt_create desc");
            List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(yeshu, size));
            List<QuestionDTO> questionDTOList = new ArrayList<>();
            PageDTO pageDTO = new PageDTO();
            for (Question question : questions) {
                User user = userMapper.selectByPrimaryKey(question.getCreator());
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(question, questionDTO);
                questionDTO.setUser(user);
                questionDTOList.add(questionDTO);
            }
            pageDTO.setData(questionDTOList);

            pageDTO.setPagination(totalPage, page);
            return pageDTO;
        }
    }


    public PageDTO find(Integer name, Integer page, Integer size) {
//    一个用户问问题总数

        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(name);
        Integer totalCount = (int)questionMapper.countByExample(questionExample);
        Integer totalPage = (totalCount % size == 0) ? totalCount / size : totalCount / size + 1;
        //数据校验放置page越界
        if(page<1){
            page=1;
        }
        if(page>totalPage){
            page=totalPage;
        }
//        数据库查询页数
        Integer yeshu = size*(page-1);

        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(name);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example,new RowBounds(yeshu,size));

        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PageDTO pageDTO = new PageDTO();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        pageDTO.setData(questionDTOList);

        pageDTO.setPagination(totalPage,page);
        return pageDTO;
    }

//    根据id查询 question user 封装到DTO
    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if(question == null){
            throw new CustomException(CustomErrorCode.QUESTION_NOT_FOUND);
        }

        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    //    问题 创建和更新
    public void createOrUpdate(Question question) {
        if(question.getId()==null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setCommentCount(0);
            question.setLikeCount(0);
            questionMapper.insert(question);
        }else {
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTag(question.getTag());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());

            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            int i = questionMapper.updateByExampleSelective(updateQuestion, example);
//            判断更新时问题是否 还在 另一个页面删除
            if (i!=1){
                throw new CustomException(CustomErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }
//阅读数
    public void addView(Integer id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionMapper.addView(question);
    }
//相关问题推荐
    public List<Question> RelevantQuestion(QuestionDTO questionDTO) {
        String[] split = questionDTO.getTag().split(",");
        String tag = StringUtils.join(split, "|");

        Question question = new Question();
        question.setId(questionDTO.getId());
        question.setTag(tag);

        List<Question> questions = questionMapper.relevantTag(question);

        return questions;
    }
}
