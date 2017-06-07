package yh.espide;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.text.Document;
import java.util.logging.Logger;

/**
 * Created by yozora on 2017/1/12.
 */
public class TerminalHandler {

    Logger logger = Logger.getLogger(TerminalHandler.class.getName());

    RSyntaxTextArea rSyntaxTextArea = new RSyntaxTextArea();

    public TerminalHandler() {
        rSyntaxTextArea.setEditable(false);
        rSyntaxTextArea.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        rSyntaxTextArea.setColumns(20);
        rSyntaxTextArea.setTabSize(4);
        rSyntaxTextArea.setToolTipText("");
        rSyntaxTextArea.setWrapStyleWord(false);
        rSyntaxTextArea.setBracketMatchingEnabled(false);
        rSyntaxTextArea.setCloseCurlyBraces(false);
        rSyntaxTextArea.setCloseMarkupTags(false);
        rSyntaxTextArea.setDragEnabled(false);
        rSyntaxTextArea.setFadeCurrentLineHighlight(true);
        rSyntaxTextArea.setHighlightSecondaryLanguages(false);
        rSyntaxTextArea.setMaximumSize(new java.awt.Dimension(100, 100));
        rSyntaxTextArea.setMinimumSize(new java.awt.Dimension(100, 100));
        rSyntaxTextArea.setName(""); // NOI18N
        rSyntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LUA);

    }

    public RSyntaxTextArea getRSyntaxTextArea() {
        return rSyntaxTextArea;
    }

    public void add(String rc) {
        Document doc = rSyntaxTextArea.getDocument();
        if (doc.getLength() > Config.ins.getTermnal_max_text_size() * 1024) {
            try {
                doc.remove(0, 1024);
            } catch (Exception e) {
            }
        }
        try {
            doc.insertString(doc.getLength(), rc, null);
        } catch (Exception e) {
            logger.info(e.toString());
        }
        if (Regedit.getBoolean(Regedit.AUTO_SCROLL, true)) {
            try {
                rSyntaxTextArea.setCaretPosition(doc.getLength());
            } catch (Exception e) {
                logger.info(e.toString());
            }
        }
    }


    public void echo(String s, boolean end) {
        String pre = "#--";
        if (FirmwareType.current.eq(FirmwareType.NodeMCU)) {
            pre = "---";
        }
        add("\r\n" + pre + s);
        if (end) {
            add("\r\n");
        }
    }

}
