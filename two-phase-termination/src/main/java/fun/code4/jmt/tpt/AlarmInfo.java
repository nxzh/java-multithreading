package fun.code4.jmt.tpt;

public class AlarmInfo {
    private String id;
    private AlarmType type;

    private String extraInfo;

    public AlarmInfo(String id, AlarmType type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public AlarmType getType() {
        return type;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    @Override
    public String toString() {
        return "AlarmInfo{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", extraInfo='" + extraInfo + '\'' +
                '}';
    }
}
