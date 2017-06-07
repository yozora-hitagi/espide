package yh.espide;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by yozora on 2017/1/10.
 */
public class CommandsSet {
    public static Set get(FirmwareType type) {
        Set set = new HashSet();
        switch (type) {
            case NodeMCU:
                set.add("=node.heap()");
                set.add("=node.chipid()");
                set.add("file.close()");
                set.add("file.remove(\"\")");
                set.add("dofile(\"\")");
                set.add("wifi.setmode(wifi.STATION)");
                set.add("wifi.setmode(wifi.SOFTAP)");
                set.add("wifi.setmode(wifi.STATIONAP)");
                set.add("=wifi.getmode()");
                set.add("wifi.sta.config(\"myssid\",\"mypassword\")");
                set.add("=wifi.sta.getip()");
                set.add("=wifi.ap.getip()");
                set.add("=wifi.sta.getmac()");
                set.add("=wifi.ap.getmac()");
                set.add("=wifi.sta.status()");
                set.add("=tmr.now()");
                break;

            case MicroPython:
                set.add("import sys; print(sys.version_info)");
                break;

            case AT:
                set.add("AT");
                set.add("AT+GMR");
                set.add("AT+RST");
                break;
        }
        return set;
    }

}
