package api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IPList {
    private List<String> ipList;
    public IPList(){
        this.ipList = new ArrayList<>();
    }
    public IPList(List<String> ipList) {
        this.ipList = ipList;
    }

    public List<String> getIpList() {
        return ipList;
    }

    public static IPList CreateIPs() {
        IPList ipList = new IPList(
                Arrays.asList(
                        "46.226.227.20",
                        "77.88.55.55",
                        "185.5.136.5",
                        "83.222.0.1",
                        "188.134.0.1"
                )
        );
        return ipList;
    }
}
