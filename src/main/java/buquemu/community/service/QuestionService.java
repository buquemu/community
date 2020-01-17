package buquemu.community.service;

import buquemu.community.Model.Question;
import buquemu.community.Model.User;
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

        pageDTO.setPagination(totalCount,totalPage,page);
        return pageDTO;

    }
}
