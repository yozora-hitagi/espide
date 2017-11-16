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
