package buquemu.community.dto;

import buquemu.community.model.User;
import lombok.Data;

@Data
public class NoticeDTO {
//    数据库主键
    private int id;
    private int outerId;
    private Long gmtCreate;
//    是否读了
    private Integer status;
//    发送者
    private User receiver;
//   前端展示 名称
    private String title;
//   描述
    private String describe;
//   回复 内容 还是 问题
    private int type;

}
