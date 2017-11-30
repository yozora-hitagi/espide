package yh.espide;

import javax.swing.JProgressBar;

interface ProgressV {
    void setV(int v);

    boolean needstop();
}


/**
 * Created by yozora on 2017/11/30.
 */
public class Progress extends JProgressBar implements ProgressV {

    public static final Progress ins = new Progress();


    private ProgressV v;

    private String text;

    private boolean needstop = false;

    public Progress() {
        setOpaque(true);
        setStringPainted(true);
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getString() {
        if (null == text) {
            return super.getString();
        } else {
            return String.format("%s %6s", text, super.getString());
        }
    }

    public void stop() {
        needstop = true;
    }

    public synchronized void start(String key, Do _do) {
        needstop = false;

        setVisible(true);

        setText(key);
        setValue(getMinimum());
        _do.Do(this);
        setText(null);

        setVisible(false);
    }

    public void setV(int v) {
        setValue(v);
    }

    public boolean needstop() {
        return needstop;
    }

    interface Do {
        void Do(ProgressV progress);
    }
}