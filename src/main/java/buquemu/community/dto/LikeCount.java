package buquemu.community.dto;

import lombok.Data;

@Data
public class LikeCount {
    private Integer commentId;
//    根据type判断是点赞还是扯赞
    private Integer type;
}
