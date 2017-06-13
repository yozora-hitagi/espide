package yh.espide;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yozora on 2017/6/12.
 */
public class Context {

    public static RSyntaxTextArea create1(FirmwareType type) {
        RSyntaxTextArea textArea = new RSyntaxTextArea();

        if (FirmwareType.MicroPython.eq(type)) {
            textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);
        } else {
            textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LUA);
        }
        textArea.setColumns(20);
        textArea.setRows(5);
        textArea.setDragEnabled(false);
        textArea.setFadeCurrentLineHighlight(true);
        textArea.setPaintMarkOccurrencesBorder(true);
        textArea.setPaintMatchedBracketPair(true);
        textArea.setCodeFoldingEnabled(false);
        textArea.setAntiAliasingEnabled(true);
        textArea.setTabsEmulated(true);
        textArea.setBracketMatchingEnabled(true);
        textArea.setTabSize(4);

        return textArea;
    }

    public static CompletionProvider create2(FirmwareType type) {
        DefaultCompletionProvider provider = new DefaultCompletionProvider();
        try {
            String name = null;

            if (FirmwareType.NodeMCU.eq(type)) {
                name = "/resources/nodemcu.autocomplete";
            } else if (FirmwareType.MicroPython.eq(type)) {
                name = "/resources/python.autocomplete";
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

            list.forEach(s->provider.addCompletion(new BasicCompletion(provider, s)));

        } catch (Exception e) {
            e.printStackTrace();
        }

//        provider.addCompletion(new ShorthandCompletion(provider, "sysout",
//                "System.out.println(", "System.out.println("));
//        provider.addCompletion(new ShorthandCompletion(provider, "syserr",
//                "System.err.println(", "System.err.println("));

        return provider;

    }

    public static JMenu createM1(String text){
        JMenu item  = new JMenu(text);
        item.setFont(new Font("ºÚÌå", Font.PLAIN, 12));
        return item;
    }

}
