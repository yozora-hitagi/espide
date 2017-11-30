package yh.espide;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Created by yozora on 2017/1/9.
 */
public enum FirmwareType {

    Unknown, NodeMCU, MicroPython, AT;

    private static final FileNameExtensionFilter FILTER_LUA = new FileNameExtensionFilter("LUA files (*.lua, *.lc)", "lua", "lc");
    private static final FileNameExtensionFilter FILTER_PYTHON = new FileNameExtensionFilter("Python files (*.py, *.pyc)", "py", "pyc");
    public static FirmwareType current = Unknown;

    public static FileFilter GetFileFilter() {
        switch (current) {
            case MicroPython:
                return FILTER_PYTHON;

            case NodeMCU:
                return FILTER_LUA;
            default:
                return null;
        }

    }

    public boolean eq(FirmwareType type) {
        return this.ordinal() == type.ordinal() && this.toString().equals(type.toString());

    }

}
