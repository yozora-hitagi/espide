package yh.espide;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Created by yozora on 2017/1/12.
 */
public abstract class Regedit {
    private static Preferences prefs = Preferences.userRoot().node("/yh/espide/config");

    public static final String TERMINAL_MAX_SIZE = "terminal_text_max_size";
    public static final String SERIAL_PORT = "serial_port";
    public static final String SERIAL_BAUD_RATE = "serial_baud_rate";


    public static final String PATH = "path";
    public static final String FILE_AUTO_SAVE_DISK = "file_auto_save_disk";
    public static final String FILE_AUTO_SAVE_ESP = "file_auto_save_esp";
    public static final String FILE_AUTO_RUN = "file_auto_run";
    public static final String COLOR_THEME = "color_theme";
    public static final String DELAY = "delay";
    public static final String TIMEOUT = "timeout";
    public static final String DUMB_MODE = "dumb_mode";
    public static final String TURBO_MODE = "turbo_mode";
    public static final String LINE_DELAY = "line_delay";
    public static final String TERMINAL_FONT_SIZE = "terminal_font_size";
    public static final String EDITOR_FONT_SIZE = "editor_font_size";
    public static final String LOG_FONT_SIZE = "log_font_size";

    // v0.2
    public static final String AUTO_SCROLL = "auto_scroll";
    public static final String SHOW_TOOLBAR = "show_toolbar";
    public static final String SHOW_FM_RIGHT = "show_fm_right";
    public static final String USE_CUSTOM_PORT = "use_custom_port";
    public static final String CUSTOM_PORT_NAME = "custom_port_name";
    public static final String FM_DIV = "fm_div";
    public static final String PORT_RTS = "port_rts";
    public static final String PORT_DTR = "port_dtr";
    public static final String USE_EXT_EDITOR = "use_ext_editor";
    public static final String SHOW_EOL = "show_eol";
    public static final String WIN_X = "win_x";
    public static final String WIN_Y = "win_y";
    public static final String WIN_H = "win_h";
    public static final String WIN_W = "win_w";
    public static final String COMMAND_ECHO = "command_echo";

    private static void save() {
        try {
            prefs.flush();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }

    public static int getInt(String key, int def) {
        return prefs.getInt(key, def);
    }

    public static void setInt(String key, int v) {
        prefs.putInt(key, v);
        save();
    }

    public static String getString(String key, String def) {
        return prefs.get(key, def);
    }

    public static void setString(String key, String v) {
        prefs.put(key, v);
        save();
    }


    public static boolean getBoolean(String key, boolean def) {
        return prefs.getBoolean(key, def);
    }

    public static void setBoolean(String key, boolean v) {
        prefs.putBoolean(key, v);
        save();
    }

    public static float getFloat(String key, float def) {
        return prefs.getFloat(key, def);
    }

    public static void setFloat(String key, float v) {
        prefs.putFloat(key, v);
        save();
    }
}
