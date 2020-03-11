package buquemu.community.dto;

import lombok.Data;

@Data
public class CommentPageDTO {
    private Integer parentId;
    private String content;
    private Integer type;
}
