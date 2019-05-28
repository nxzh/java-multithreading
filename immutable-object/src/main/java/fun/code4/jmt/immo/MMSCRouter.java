package fun.code4.jmt.immo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class MMSCRouter {

    // volatile 保证 setInstance 修改之后立马可见
    private static volatile MMSCRouter instance = new MMSCRouter();
    private final Map<String, MMSCInfo> routeMap;

    public MMSCRouter() {
        this.routeMap = mockRouteMapFromDB();
    }

    private Map<String, MMSCInfo> mockRouteMapFromDB() {
        return new HashMap<String, MMSCInfo>(){{
            put("123456", new MMSCInfo("1QAX2WSX", "http://fake.url", 100));
            put("234567", new MMSCInfo("0OKM9IJN", "http://fake.url", 99));
        }};
    }

    public static MMSCRouter getInstance() {
        return instance;
    }

    public MMSCInfo getMMSC(String msisdnPrefix) {
        return routeMap.get(msisdnPrefix);
    }

    public static void setInstance(MMSCRouter newInstance) {
        instance = newInstance;
    }

    // 确保 getRouteMap 里 get 后不会被改动.
    private static Map<String, MMSCInfo> deepCopy(Map<String, MMSCInfo> m) {
        Map<String, MMSCInfo> result = new HashMap<String, MMSCInfo>();
        for (String key : m.keySet()) {
            result.put(key, new MMSCInfo(m.get(key)));
        }
        return result;
    }

    // unmodifiableMap 保证了集合中不能添加/删除元素
    // getRouteMap
    public Map<String, MMSCInfo> getRouteMap() {
        return Collections.unmodifiableMap(deepCopy(routeMap));
    }
}
