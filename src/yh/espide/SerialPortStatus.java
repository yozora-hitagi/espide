package yh.espide;

/**
 * Created by yozora on 2017/12/1.
 */
public enum SerialPortStatus {

    CLOSED, CONNECTED, OPENED;

    public boolean isClosed() {
        return CLOSED.equals(this);
    }

    public boolean isConnected() {
        return CONNECTED.equals(this);
    }

    public boolean isOpened() {
        return OPENED.equals(this);
    }

}
