package buquemu.community.service;

import buquemu.community.model.Question;
import buquemu.community.model.User;
import buquemu.community.dto.PageDTO;
import buquemu.community.dto.QuestionDTO;
import buquemu.community.mapper.QuestionMapper;
import buquemu.community.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    UserMapper userMapper;
    public PageDTO list(Integer page, Integer size) {

        Integer totalCount = questionMapper.count();
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

        List<Question> questions = questionMapper.List(yeshu,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PageDTO pageDTO = new PageDTO();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        pageDTO.setQuestion(questionDTOList);

        pageDTO.setPagination(totalPage,page);
        return pageDTO;

    }

    public PageDTO find(String name, Integer page, Integer size) {
//                                           一个用户问问题总数
        Integer totalCount = questionMapper.countBycreator(name);
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

        List<Question> questions = questionMapper.find(name,yeshu,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PageDTO pageDTO = new PageDTO();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        pageDTO.setQuestion(questionDTOList);

        pageDTO.setPagination(totalPage,page);
        return pageDTO;
    }

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.getById(id);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.findById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    //    问题 创建和更新
    public void createOrUpdate(Question question) {
        if(question.getId()==null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.create(question);
        }else {
            question.setGmtModified(System.currentTimeMillis());
            question.setTag(question.getTag());
            question.setTitle(question.getTitle());
            question.setDescription(question.getDescription());
            questionMapper.upDate(question);
        }
    }
}
