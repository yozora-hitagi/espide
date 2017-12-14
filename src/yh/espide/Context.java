package yh.espide;

import org.fife.ui.rsyntaxtextarea.Theme;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yozora on 2017/6/12.
 */
public class Context {

    public static final Font FONT_16 = new Font("雅黑", 0, 16);
    public static final Font FONT_14 = new Font("雅黑", 0, 14);
    public static final Font FONT_12 = new Font("雅黑", 0, 12);
    public static final Font FONT_10 = new Font("雅黑", 0, 10);
    public static final Font FONT_8 = new Font("雅黑", 0, 8);
    public static final ResourceBundle BUNDLE = ResourceBundle.getBundle("lang");
    private static Logger logger = Logger.getLogger(Context.class.getName());

    public static Theme GetTheme() {
        int t = Config.ins.getColor_theme();

        String res;
        if (t == 1) {
            res = "/org/fife/ui/rsyntaxtextarea/themes/dark.xml";
        } else if (t == 2) {
            res = "/org/fife/ui/rsyntaxtextarea/themes/eclipse.xml";
        } else if (t == 3) {
            res = "/org/fife/ui/rsyntaxtextarea/themes/idea.xml";
        } else if (t == 4) {
            res = "/org/fife/ui/rsyntaxtextarea/themes/vs.xml";
        } else if (t == 5) {
            res = "/org/fife/ui/rsyntaxtextarea/themes/default-alt.xml";
        } else {
            res = "/org/fife/ui/rsyntaxtextarea/themes/default.xml";
        }
        try {
            return Theme.load(Context.class.getClass().getResourceAsStream(res));
        } catch (IOException e) {
            logger.log(Level.WARNING, "", e);
            return null;
        }
    }


    public static void Save(File file, byte[] bytes) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes);
        fos.close();
    }

    public static String FormatString(String message, String... args) {
        return MessageFormat.format(BUNDLE.getString(message), args);
    }


    public static JMenu createM1(String text) {
        JMenu item = new JMenu(text);
        item.setFont(new Font("????", Font.PLAIN, 12));
        return item;
    }


    public static int Dialog(String msg, int btn) {
        Toolkit.getDefaultToolkit().beep();
        int returnVal = JOptionPane.showConfirmDialog(null, msg, Context.BUNDLE.getString("Attention"), btn, JOptionPane.WARNING_MESSAGE);
        return returnVal;
    }

    public static String GetLua(String file, String... args) {
        InputStream is = Context.class.getResourceAsStream(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len = 0;
        byte[] buf = new byte[1024];
        try {
            while ((len = is.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String str = new String(baos.toByteArray());
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < args.length; i++) {
            String key = "_espide_script_args" + i;
            str = str.replace(key, args[i]);
        }

        return str;
    }


    public static File[] ShowFileDialog(Component parent, String title, File select, boolean multi, boolean isopen) {

        JFileChooser chooser = new JFileChooser(Config.ins.getPath());
        if (null != title) {
            chooser.setDialogTitle(title);
        }
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (multi) {
            chooser.setMultiSelectionEnabled(multi);
        }

        if (null != select) {
            chooser.setSelectedFile(select);
        }

        for (FileFilter filter : FileType.FILTER_MAP.values()) {
            chooser.addChoosableFileFilter(filter);
        }

        int returnVal;
        if (isopen) {
            returnVal = chooser.showOpenDialog(parent);
        } else {
            returnVal = chooser.showSaveDialog(parent);
        }

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] files;
            if (multi) {
                files = chooser.getSelectedFiles();
            } else {
                files = new File[]{chooser.getSelectedFile()};
            }
            Config.ins.setPath(files[0].getParent());
            return files;
        }

        return null;

    }

}
