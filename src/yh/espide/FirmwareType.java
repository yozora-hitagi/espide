package yh.espide;

/**
 * Created by yozora on 2017/1/9.
 */
public enum FirmwareType {

    Unknown, NodeMCU, MicroPython, AT;

    public boolean eq(FirmwareType type) {
        return this.ordinal() == type.ordinal() && this.toString().equals(type.toString());

    }

    public static FirmwareType current = Unknown;
}
