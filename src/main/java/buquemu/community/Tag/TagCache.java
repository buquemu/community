package buquemu.community.Tag;

import buquemu.community.dto.TagDTO;
import org.thymeleaf.util.StringUtils;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagCache {
    public static List<TagDTO> tag(){
        List<TagDTO> tagDTOS = new ArrayList<>();
        TagDTO pinpai = new TagDTO();
        pinpai.setSort("品牌");
//        将字符串转换为list
        pinpai.setTags(Arrays.asList("杜卡迪","川崎","雅马哈","贝纳利","阿普利亚","铃木"));
        tagDTOS.add(pinpai);

        TagDTO yongtu = new TagDTO();
        yongtu.setSort("用途");
        yongtu.setTags(Arrays.asList("跑车","街车","摩旅","越野","复古","巡航"));
        tagDTOS.add(yongtu);

        return tagDTOS;
    }


//    判断标签是否属于标签库库
    public static String haveTag(String tags){
        String[] split = StringUtils.split(tags, "，");
        List<TagDTO> tagDTOS = tag();

        List<String> collect = tagDTOS.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
//        返回一个出错的集合
        String invalid = Arrays.stream(split).filter(t -> !collect.contains(t)).collect(Collectors.joining(","));
//返回非法标签
        return invalid;
    }
}
