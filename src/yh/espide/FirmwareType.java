package yh.espide;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Created by yozora on 2017/1/9.
 */
public enum FirmwareType {

    Unknown, NodeMCU, MicroPython, AT;

    public static FirmwareType current = Unknown;


    public boolean eq(FirmwareType type) {
        return this.ordinal() == type.ordinal() && this.toString().equals(type.toString());

    }

}
