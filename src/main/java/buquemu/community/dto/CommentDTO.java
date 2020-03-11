package buquemu.community.dto;

import buquemu.community.model.User;
import lombok.Data;

import java.util.List;


@Data
public class CommentDTO {
    private Integer id;
    private Integer parentId;
    private Integer type;
    private String content;
    private Integer likeCount;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer commentator;
    private Integer commentCount;
    private User user;


}
