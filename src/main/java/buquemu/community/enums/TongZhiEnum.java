package buquemu.community.enums;

public enum TongZhiEnum {
//    数据库中保存较少的值 根据type 拿后面的文字
    REPLAY_QUESTION(1,"回复了问题"),
    REPLAY_COMMENT(2,"回复了评论"),
    REPLAY_DIANZAN(3,"点赞了评论"),
    ;

    private int type;
    private String name;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    TongZhiEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String describe(int type){
        for (TongZhiEnum tongZhiEnum:TongZhiEnum.values()){
            if (tongZhiEnum.getType() == type){
                return tongZhiEnum.getName();
            }
        }return "";
    }

    public static boolean inClude(Integer type) {
        for (TongZhiEnum c:TongZhiEnum.values()) {
            if (c.getType() == type) {
                return true;
            }
        }
        return false;
    }

}
