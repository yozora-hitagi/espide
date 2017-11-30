package yh.espide;

import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.rsyntaxtextarea.Theme;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import java.awt.Component;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
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

    public static CompletionProvider create2(FirmwareType type) {
        DefaultCompletionProvider provider = new DefaultCompletionProvider();
        try {
            String name = null;
            if (FirmwareType.MicroPython.eq(type)) {
                name = "/resources/python.autocomplete";
            } else {
                name = "/resources/nodemcu.autocomplete";
            }

            List<String> list = new ArrayList();

            if (null != name) {
                InputStream is = Context.class.getResourceAsStream(name);
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line = null;
                while ((line = br.readLine()) != null) {
                    list.add(line);
                }
                br.close();
                is.close();
            }

            list.forEach(s -> provider.addCompletion(new BasicCompletion(provider, s)));

        } catch (Exception e) {
            e.printStackTrace();
        }

//        provider.addCompletion(new ShorthandCompletion(provider, "sysout",
//                "System.out.println(", "System.out.println("));
//        provider.addCompletion(new ShorthandCompletion(provider, "syserr",
//                "System.err.println(", "System.err.println("));

        return provider;

    }

    public static JMenu createM1(String text) {
        JMenu item = new JMenu(text);
        item.setFont(new Font("????", Font.PLAIN, 12));
        return item;
    }

    public static File[] ShowFileDialog(Component parent, String title, File select, boolean multi, boolean isopen, boolean filter) {

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
        if (filter) {
            chooser.setFileFilter(FirmwareType.GetFileFilter());
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
