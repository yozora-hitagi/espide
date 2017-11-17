package yh.espide;

import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.JPopupMenu;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Created by yozora on 2017/1/12.
 */
public class TerminalHandler extends RTextScrollPane {

    Logger logger = Logger.getLogger(TerminalHandler.class.getName());

    RSyntaxTextArea rSyntaxTextArea;


    AtomicInteger location = new AtomicInteger(0);
    AtomicBoolean control = new AtomicBoolean(true);

    CommandListener listener;

    public TerminalHandler(CommandListener listener) {
        this.listener = listener;

        rSyntaxTextArea = new RSyntaxTextArea(new RSyntaxDocument(SyntaxConstants.SYNTAX_STYLE_LUA) {
            @Override
            public void remove(int offs, int len) throws BadLocationException {
                if (control.get()) {
                    if (offs < location.get()) {
                        offs = location.get();
                    }
                }
                super.remove(offs, len);
            }

            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                /**
                 *
                 */
                if (super.getLength() > Config.ins.getTermnal_max_text_size() * 1024) {
                    try {
                        super.remove(0, str.length());
                    } catch (Exception e) {
                    }
                }

                if (control.get()) {
                    if (offs < location.get()) {
                        offs = rSyntaxTextArea.getText().length();
                        rSyntaxTextArea.setCaretPosition(offs);
                    }
                }
                super.insertString(offs, str, a);
            }


        }) {
            @Override
            public void undoLastAction() {
                // super.undoLastAction();
            }
        };

        // rSyntaxTextArea.setEditable(false);
        rSyntaxTextArea.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        rSyntaxTextArea.setColumns(20);
        rSyntaxTextArea.setTabSize(4);
        rSyntaxTextArea.setWrapStyleWord(false);
        rSyntaxTextArea.setBracketMatchingEnabled(false);
        rSyntaxTextArea.setCloseCurlyBraces(false);
        rSyntaxTextArea.setCloseMarkupTags(false);
        rSyntaxTextArea.setDragEnabled(false);
        rSyntaxTextArea.setFadeCurrentLineHighlight(true);
        rSyntaxTextArea.setHighlightSecondaryLanguages(false);
        rSyntaxTextArea.setMaximumSize(new java.awt.Dimension(100, 100));
        rSyntaxTextArea.setMinimumSize(new java.awt.Dimension(100, 100));
        rSyntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LUA);

        rSyntaxTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {


                int length = rSyntaxTextArea.getText().length();
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    if (null != listener) {
                        rSyntaxTextArea.setCaretPosition(length);
                        String command = null;
                        try {
                            command = rSyntaxTextArea.getText(location.get(), length - location.get());
                        } catch (BadLocationException e1) {
                            command = rSyntaxTextArea.getText().substring(location.get());
                        }
                        listener.listen(command);
                    }
                }


            }

        });


        setMaximumSize(new java.awt.Dimension(100, 100));
        setMinimumSize(new java.awt.Dimension(100, 100));
        setPreferredSize(new java.awt.Dimension(100, 100));

        setViewportView(rSyntaxTextArea);
        rSyntaxTextArea.getAccessibleContext().setAccessibleParent(this);


    }


    /**
     * clean terminal text
     */
    public void clean() {
        rSyntaxTextArea.setText("");
    }


    public void setEOLMarkersVisible(boolean b) {
        rSyntaxTextArea.setEOLMarkersVisible(b);
    }


    public void setPopupMenu(JPopupMenu popup) {
        rSyntaxTextArea.setPopupMenu(popup);
    }


    public void setSyntaxEditingStyle(FirmwareType type) {
        switch (type) {
            case MicroPython:
                rSyntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);
                break;
            default:
                rSyntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LUA);
                break;

        }
    }


    public void setEditable(boolean b) {
        rSyntaxTextArea.setEditable(b);
    }


    public boolean hasSelected() {
        return rSyntaxTextArea.getSelectedText().length() > 0;
    }

    public void copy() {
        rSyntaxTextArea.copy();
    }


    public void setFontSize(int size) {
        rSyntaxTextArea.setFont(rSyntaxTextArea.getFont().deriveFont(size));
    }


    public RSyntaxTextArea getRSyntaxTextArea() {
        return rSyntaxTextArea;
    }

    public void add(String rc) {
        Document doc = rSyntaxTextArea.getDocument();

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

        try {
            location.set(rSyntaxTextArea.getLineEndOffset(rSyntaxTextArea.getLineCount() - 1));
        } catch (BadLocationException e) {
            location.set(rSyntaxTextArea.getText().length());
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

    public static interface CommandListener {
        void listen(String command);
    }

}
