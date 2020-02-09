package buquemu.community.enums;

public enum  CommentTypeEnum {
    QUESTION(1),
    COMMENT(2);
//    几级评论
    private Integer type;

    public static boolean inClude(Integer type) {
        for (CommentTypeEnum c:CommentTypeEnum.values()) {
            if (c.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public Integer getType() {
        return type;
    }

    CommentTypeEnum(Integer type) {
        this.type = type;
    }
}
