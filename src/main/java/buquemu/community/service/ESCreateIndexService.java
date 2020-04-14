package buquemu.community.service;

import buquemu.community.mapper.QuestionMapper;
import buquemu.community.model.Question;
import buquemu.community.model.QuestionExample;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ESCreateIndexService {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private TransportClient client;
    public void createIndex() throws JsonProcessingException {
        /* 读源数据
        *   List<Question>
        整理成Document 调用TranSportClient写入
        *  创建索引 for 循环list 挨个写入 json格式写入
         */
        List<Question> questions = questionMapper.selectByExample(new QuestionExample());
        //创建索引
        client.admin().indices().prepareExists("community").get();
//for循环添加Doc
        for (Question q: questions) {
//            把q 序列化为Json
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(q);
            IndexRequestBuilder request =
                    client.prepareIndex("community", "question", q.getId().toString());
//添加request参数json
            request.setSource(json).get();

        }
    }
}
