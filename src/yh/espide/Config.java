package yh.espide;

import static yh.espide.Regedit.*;

/**
 * Created by yozora on 2017/6/7.
 */
public class Config {
    public static final Config ins = new Config();


    private boolean command_echo =true;

    //kb
    private int termnal_max_text_size = 100;

    private boolean dumb_mode=false;
    private boolean turbo_mode=false;

    private boolean file_auto_save_esp=true;
    private  boolean file_auto_save_disk=true;
    private boolean file_auto_run=false;

    private int color_theme=1;

    private int delay_after_answer=0;
    private int line_delay_for_dumb=200;
    private int answer_timeout=   3;

    private Config() {
        command_echo = getBoolean(COMMAND_ECHO, command_echo);
        termnal_max_text_size=getInt(TERMINAL_MAX_SIZE,termnal_max_text_size);
        dumb_mode=getBoolean(DUMB_MODE, dumb_mode);
        turbo_mode=getBoolean(TURBO_MODE, turbo_mode);

        file_auto_save_esp = getBoolean(FILE_AUTO_SAVE_ESP,file_auto_save_esp);
        file_auto_save_disk = getBoolean(FILE_AUTO_SAVE_DISK,file_auto_save_disk);
        file_auto_run=getBoolean(FILE_AUTO_RUN,file_auto_run);

        color_theme=getInt(COLOR_THEME,color_theme);

        delay_after_answer = getInt(DELAY,delay_after_answer);
        line_delay_for_dumb=getInt(LINE_DELAY,line_delay_for_dumb);
        answer_timeout=getInt(TIMEOUT,answer_timeout);

    }

    public int getAnswer_timeout() {
        return answer_timeout;
    }

    public void setAnswer_timeout(int answer_timeout) {
        setInt(TIMEOUT,answer_timeout);
        this.answer_timeout = answer_timeout;
    }

    public int getLine_delay_for_dumb() {
        return line_delay_for_dumb;
    }

    public void setLine_delay_for_dumb(int line_delay_for_dumb) {
        setInt(LINE_DELAY,line_delay_for_dumb);
        this.line_delay_for_dumb = line_delay_for_dumb;
    }

    public int getDelay_after_answer() {
        return delay_after_answer;
    }

    public void setDelay_after_answer(int delay_after_answer) {
        setInt(DELAY,delay_after_answer);
        this.delay_after_answer = delay_after_answer;
    }

    public boolean isFile_auto_save_disk() {
        return file_auto_save_disk;
    }

    public void setFile_auto_save_disk(boolean file_auto_save_disk) {
        setBoolean(FILE_AUTO_SAVE_DISK,file_auto_save_disk);
        this.file_auto_save_disk = file_auto_save_disk;
    }

    public boolean isCommand_echo() {
        return command_echo;
    }

    public void setCommand_echo(boolean command_echo) {
        setBoolean(COMMAND_ECHO, command_echo);
        this.command_echo = command_echo;
    }

    public int getTermnal_max_text_size() {
        return termnal_max_text_size;
    }

    public void setTermnal_max_text_size(int termnal_max_text_size) {
        setInt(TERMINAL_MAX_SIZE,termnal_max_text_size);
        this.termnal_max_text_size = termnal_max_text_size;
    }

    public boolean isDumb_mode() {
        return dumb_mode;
    }

    public void setDumb_mode(boolean dumb_mode) {
        setBoolean(DUMB_MODE,dumb_mode);
        this.dumb_mode = dumb_mode;
    }

    public boolean isFile_auto_save_esp() {
        return file_auto_save_esp;
    }

    public void setFile_auto_save_esp(boolean file_auto_save_esp) {
        setBoolean(FILE_AUTO_SAVE_ESP,file_auto_save_esp);
        this.file_auto_save_esp = file_auto_save_esp;
    }

    public boolean isFile_auto_run() {
        return file_auto_run;
    }

    public void setFile_auto_run(boolean file_auto_run) {
        setBoolean(FILE_AUTO_RUN,file_auto_run);
        this.file_auto_run = file_auto_run;
    }

    public boolean isTurbo_mode() {
        return turbo_mode;
    }

    public void setTurbo_mode(boolean turbo_mode) {
        setBoolean(TURBO_MODE,turbo_mode);
        this.turbo_mode = turbo_mode;
    }

    public int getColor_theme() {
        return color_theme;
    }

    public void setColor_theme(int color_theme) {
        setInt(COLOR_THEME,color_theme);
        this.color_theme = color_theme;
    }
}
