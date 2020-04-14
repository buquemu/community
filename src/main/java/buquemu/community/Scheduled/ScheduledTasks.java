package buquemu.community.Scheduled;

import buquemu.community.controller.ESCreateIndex;
import buquemu.community.dto.HotTagDTO;
import buquemu.community.mapper.QuestionMapper;
import buquemu.community.model.Question;
import buquemu.community.model.QuestionExample;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

@Component
//定时器
public class ScheduledTasks {
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    ESCreateIndex esCreateIndex;
//    @Scheduled(fixedRate = 10000)
   @Scheduled(cron = "0 0 1 * * *")
   public void ESIndex() throws JsonProcessingException{
       esCreateIndex.createIndex();
   }


    @Scheduled(cron = "0 0 1 * * *")
    public List<HotTagDTO> hotTags() {


        //            写着为了防止map put覆盖
        Map<String,Double> tagWeight = new HashMap<>();

//        循环所有问题 找到里面的tag

        List<Question> questions = questionMapper.selectByExample(new QuestionExample());

        for (Question question:questions){
            String tag = question.getTag();
//            分tag
            String[] split = StringUtils.split(tag, "，");
            for (String s:split) {
                if (tagWeight.containsKey(s)){
                    Double a = tagWeight.get(s);
//保留2位小数
                    double d = a+10+(question.getCommentCount()*1)+(question.getViewCount()*0.1);
                    BigDecimal b = new BigDecimal(d);
                    d = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    tagWeight.put(s,d);
                }else {
                    double d = 10+(question.getCommentCount()*1)+(question.getViewCount()*0.1);
                    BigDecimal b = new BigDecimal(d);
                    d = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    tagWeight.put(s,d);
                }
            }
        }


        List<Map.Entry<String, Double>> list = new ArrayList<>(tagWeight.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>()
        {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2)
            {
                //按照value值，重小到大排序
//                return o1.getValue() - o2.getValue();

                //按照value值，从大到小排序
                int value2 = new Double(o2.getValue()).intValue();
                int value1 = new Double(o1.getValue()).intValue();
                return value2 - value1;

                //按照value值，用compareTo()方法默认是从小到大排序
  //                 return o1.getValue().compareTo(o2.getValue());
            }
        });


        List<HotTagDTO> hotTagDTOS = new ArrayList<>();
//拿到前4个热门标签
        for (int i=0;i<5;i++)
        {
            HotTagDTO hotTagDTO = new HotTagDTO();
           hotTagDTO.setHotTags((list.get(i).getKey()));
           hotTagDTO.setRedu(list.get(i).getValue());

           hotTagDTOS.add(hotTagDTO);
        }

//        tagWeight.forEach(
//                (k,v)->{
//                    System.out.print(k);
//                    System.out.print(":");
//                    System.out.print(v);
//                    System.out.println();
//                }
//        );


        return hotTagDTOS;
    }
}
