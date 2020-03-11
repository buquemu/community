package buquemu.community.enums;

public enum  TongZhiStatusEnum {
    DEFAULT(0),READ(1)
    ;
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    TongZhiStatusEnum(int status) {
        this.status = status;
    }
}
