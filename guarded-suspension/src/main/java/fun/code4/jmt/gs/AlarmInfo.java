package fun.code4.jmt.gs;

public class AlarmInfo {
    private String message;

    public AlarmInfo(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "AlarmInfo{" +
                "message='" + message + '\'' +
                '}';
    }
}
