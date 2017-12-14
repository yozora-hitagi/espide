package yh.espide;

import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yozora on 2017/12/14.
 */
public class FileType {

    public static final Map<String, FileFilter> FILTER_MAP = new HashMap();
    public static final Map<String, String> SYNTAX_MAP = new HashMap();
    public static final Map<String, String> AUTO_COMP_MAP = new HashMap<>();

    static {
        FILTER_MAP.put("lua", new FileNameExtensionFilter("LUA files (*.lua)", "lua"));
        FILTER_MAP.put("py", new FileNameExtensionFilter("Python files (*.py)", "py"));
        FILTER_MAP.put("lc", new FileNameExtensionFilter("LUA files ( *.lc)", "lc"));
        FILTER_MAP.put("pyc", new FileNameExtensionFilter("Python files (*.pyc)", "pyc"));
    }

    static {
        SYNTAX_MAP.put("lua", SyntaxConstants.SYNTAX_STYLE_LUA);
        SYNTAX_MAP.put("py", SyntaxConstants.SYNTAX_STYLE_PYTHON);
    }

    static {
        AUTO_COMP_MAP.put("lua", "/resources/nodemcu.autocomplete");
        AUTO_COMP_MAP.put("py", "/resources/python.autocomplete");
    }

    public static String Get_suffix(File file) {
        String name = file.getName();
        return name.substring(name.lastIndexOf('.') + 1);
    }

    public static CompletionProvider create(String key) {
        DefaultCompletionProvider provider = new DefaultCompletionProvider();
        try {
            String name = AUTO_COMP_MAP.get(key);
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
}
