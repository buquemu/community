package buquemu.community.dto;

import lombok.Data;

@Data
public class QQUserDTO {
    private String nickname; //昵称
    private String openId; //唯一id
    private String figureurl_1; //头像
    private String gender;//性别。 如果获取不到则默认返回"男"
    private String province;
    private String city;
    private String year;
    private String constellation;
}
