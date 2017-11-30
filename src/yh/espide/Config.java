package yh.espide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yozora on 2017/6/7.
 */
public class Config {
    public static final Config ins = new Config();

    private static final String TERMINAL_MAX_SIZE = "terminal_text_max_size";
    private static final String SERIAL_PORT = "serial_port";
    private static final String SERIAL_BAUD_RATE = "serial_baud_rate";
    private static final String PATH = "path";
    private static final String FILE_AUTO_SAVE_DISK = "file_auto_save_disk";
    private static final String FILE_AUTO_SAVE_ESP = "file_auto_save_esp";
    private static final String FILE_AUTO_RUN = "file_auto_run";
    private static final String COLOR_THEME = "color_theme";
    private static final String DELAY_AFTER_ANSWER = "delay_after_answer";
    private static final String ANSWER_TIMEOUT = "answer_timeout";
    private static final String DUMB_MODE = "dumb_mode";
    private static final String TURBO_MODE = "turbo_mode";
    private static final String LINE_DELAY = "line_delay";
    private static final String TERMINAL_FONT_SIZE = "terminal_font_size";
    private static final String EDITOR_FONT_SIZE = "editor_font_size";
    private static final String LOG_FONT_SIZE = "log_font_size";
    private static final String AUTO_SCROLL = "auto_scroll";
    private static final String SHOW_TOOLBAR = "show_toolbar";
    private static final String SHOW_FM_RIGHT = "show_fm_right";
    private static final String USE_CUSTOM_PORT = "use_custom_port";
    private static final String CUSTOM_PORT_NAME = "custom_port_name";
    private static final String FM_DIV_DIVIDERLOCATION = "fm_div_dividerlocation";
    private static final String PORT_RTS = "port_rts";
    private static final String PORT_DTR = "port_dtr";
    private static final String USE_EXT_EDITOR = "use_ext_editor";
    private static final String SHOW_EOL = "show_eol";
    private static final String WIN_X = "win_x";
    private static final String WIN_Y = "win_y";
    private static final String WIN_H = "win_h";
    private static final String WIN_W = "win_w";
    private static final String COMMAND_ECHO = "command_echo";

    Logger logger = Logger.getLogger(Config.class.getName());


    //kb
    private int default_termnal_max_text_size = 100;
    private boolean default_dumb_mode = false;
    private boolean default_turbo_mode = false;
    private boolean default_file_auto_save_esp = true;
    private boolean default_file_auto_save_disk = true;
    private boolean default_file_auto_run = false;
    private int default_color_theme = 1;
    private int default_delay_after_answer = 0;
    private int default_line_delay_for_dumb = 200;
    private int default_answer_timeout = 3;
    private int default_terminal_font_size = 16;
    private int default_editor_font_size = 13;
    private String default_serial_port = "";
    private int default_win_x = 0;
    private int default_win_y = 0;
    private int default_win_h = 768;
    private int default_win_w = 1024;
    private String default_path = ".";


    private Properties properties = new Properties();

    private Config() {
        try {
            properties.load(new FileInputStream("espide.properties"));
        } catch (FileNotFoundException e) {
            try {
                new File("espide.properties").createNewFile();
            } catch (IOException e1) {
                logger.log(Level.WARNING, "", e1);
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "", e);
        }

    }


    public void setWinX(int x) {
        set(WIN_X, x);
    }

    public void setWinY(int y) {
        set(WIN_Y, y);
    }

    public void setWinH(int h) {
        set(WIN_H, h);
    }

    public void setWinW(int w) {
        set(WIN_W, w);
    }

    public boolean getAutoScroll() {
        return getBoolean(AUTO_SCROLL, true);
    }

    public void setAutoScroll(boolean auto) {
        set(AUTO_SCROLL, auto);
    }

    public void setFm_div_dividerlocation(int loc) {
        set(FM_DIV_DIVIDERLOCATION, loc);
    }

    public String getPath() {
        return getString(PATH, default_path);
    }

    public void setPath(String path) {
        set(PATH, path);
    }

    public int getTerminal_font_size() {
        return getInt(TERMINAL_FONT_SIZE, default_terminal_font_size);
    }

    public void setTerminal_font_size(int terminal_font_size) {
        set(TERMINAL_FONT_SIZE, terminal_font_size);
    }

    public int getEditor_font_size() {
        return getInt(EDITOR_FONT_SIZE, default_editor_font_size);
    }

    public void setEditor_font_size(int editor_font_size) {
        set(EDITOR_FONT_SIZE, editor_font_size);
    }

    public boolean getShowToolbar() {
        return getBoolean(SHOW_TOOLBAR, true);
    }

    public void setShowToolbar(boolean show) {
        set(SHOW_TOOLBAR, show);
    }

    public boolean getFm_right_show() {
        return getBoolean(SHOW_FM_RIGHT, true);
    }

    public void setFm_right_show(boolean show) {
        set(SHOW_FM_RIGHT, show);
    }

    public boolean getPortDtr() {
        return getBoolean(PORT_DTR, false);
    }

    public void setPortDtr(boolean dtr) {
        set(PORT_DTR, dtr);
    }

    public boolean getPortRts() {
        return getBoolean(PORT_RTS, false);
    }

    public void setPortRts(boolean rts) {
        set(PORT_RTS, rts);
    }

    public boolean getShowEOL() {
        return getBoolean(SHOW_EOL, false);
    }

    public void setSerial_port(String serial_port) {
        set(SERIAL_PORT, serial_port);
    }

    public int getSerial_baud_rate() {
        return getInt(SERIAL_BAUD_RATE, 115200);
    }

    public void setSerial_baud_rate(int rate) {
        set(SERIAL_BAUD_RATE, rate);
    }

    public String getSerialPort() {
        return getString(SERIAL_PORT, null);
    }

    public int getAnswer_timeout() {
        return getInt(ANSWER_TIMEOUT, default_answer_timeout);
    }

    public void setAnswer_timeout(int answer_timeout) {
        set(ANSWER_TIMEOUT, answer_timeout);
    }

    public int getWin_x() {
        return getInt(WIN_X, default_win_x);
    }

    public int getWin_y() {
        return getInt(WIN_Y, default_win_y);
    }

    public int getWin_h() {
        return getInt(WIN_H, default_win_h);
    }

    public int getWin_w() {
        return getInt(WIN_W, default_win_w);
    }

    public int getLine_delay_for_dumb() {
        return getInt(LINE_DELAY, default_line_delay_for_dumb);
    }

    public void setLine_delay_for_dumb(int line_delay_for_dumb) {
        set(LINE_DELAY, line_delay_for_dumb);
    }

    public int getDelay_after_answer() {
        return getInt(DELAY_AFTER_ANSWER, default_delay_after_answer);
    }

    public void setDelay_after_answer(int delay_after_answer) {
        set(DELAY_AFTER_ANSWER, delay_after_answer);
    }


    public int getTermnal_max_text_size() {
        return getInt(TERMINAL_MAX_SIZE, default_termnal_max_text_size);
    }

    public void setTermnal_max_text_size(int termnal_max_text_size) {
        set(TERMINAL_MAX_SIZE, termnal_max_text_size);
    }

    public boolean isDumb_mode() {
        return getBoolean(DUMB_MODE, default_dumb_mode);
    }

    public void setDumb_mode(boolean dumb_mode) {
        set(DUMB_MODE, dumb_mode);
    }


    public boolean isTurbo_mode() {
        return getBoolean(TURBO_MODE, default_turbo_mode);
    }

    public void setTurbo_mode(boolean turbo_mode) {
        set(TURBO_MODE, turbo_mode);
    }

    public int getColor_theme() {
        return getInt(COLOR_THEME, default_color_theme);
    }


    public void setColor_theme(int color_theme) {
        set(COLOR_THEME, color_theme);
    }

    //##################################
    //########
    private int getInt(String key, int def) {
        String v = properties.getProperty(key, String.valueOf(def));
        return Integer.parseInt(v);
    }

    private void set(String key, Object v) {
        properties.put(key, String.valueOf(v));
        save();
    }

    private String getString(String key, String def) {
        return properties.getProperty(key, def);
    }


    private boolean getBoolean(String key, boolean def) {
        String v = properties.getProperty(key, String.valueOf(def));
        return Boolean.parseBoolean(v);
    }


    private void save() {
        try {
            properties.store(new FileOutputStream("espide.properties"), "create by espide");
        } catch (IOException e) {
            logger.log(Level.WARNING, "", e);
        }
    }
}
