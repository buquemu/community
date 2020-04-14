package buquemu.community.service;

import buquemu.community.dto.QuestionDTO;
import buquemu.community.mapper.UserMapper;
import buquemu.community.model.Question;
import buquemu.community.model.User;
import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ESService {
    @Autowired
    private TransportClient client;
    @Autowired
    private UserMapper userMapper;

    public List<QuestionDTO> search(String text, Integer page, Integer rows) {
//        使用tranSprotClient封装query对象
        MatchQueryBuilder query =
                QueryBuilders.matchQuery("title", text);
//        prepare获取request对象
        SearchRequestBuilder request = client.prepareSearch("community");
//
//        SearchResponse response =
//                request.setQuery(query).setFrom((page - 1) * rows).setSize(rows).get();
        SearchResponse response =
              request.setQuery(query).get();
        List<QuestionDTO> pList = new ArrayList<>();
// 获取hits 循环解析到plist
        SearchHit[] hits = response.getHits().getHits();
        for (SearchHit hit:hits
             ) {
            String string = hit.getSourceAsString();
//            拿到question对象 对其进行封装
            Question question = JSON.parseObject(string, Question.class);
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            pList.add(questionDTO);
        }

        return pList;
    }
}
