package buquemu.community.dto;

import lombok.Data;

import java.util.List;

@Data
public class TagDTO {
//    分类
    private String sort;
    private List<String> tags;

}
