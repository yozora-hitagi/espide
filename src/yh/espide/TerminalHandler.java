package yh.espide;

import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yozora on 2017/1/12.
 */
public class TerminalHandler extends RTextScrollPane {

    Logger logger = Logger.getLogger(TerminalHandler.class.getName());

    RSyntaxTextArea rSyntaxTextArea;

    JPopupMenu popup;


    AtomicInteger location = new AtomicInteger(0);
    AtomicBoolean control = new AtomicBoolean(true);

    AtomicBoolean add = new AtomicBoolean(false);

    ArrayList<String> history = new ArrayList();
    int history_loc = 0;

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
                if (super.getLength() > Config.ins.getTermnal_max_text_size() * 1024) {
                    try {
                        super.remove(0, str.length());
                    } catch (Exception e) {
                    }
                }

                if (control.get()) {
                    if (offs < location.get()) {
                        offs = getLength();
                        rSyntaxTextArea.setCaretPosition(offs);

                    }
                }


                super.insertString(offs, str, a);


            }


        }) {
//            @Override
//            public void undoLastAction() {
//                // super.undoLastAction();
//            }

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

        //rSyntaxTextArea.setMaximumSize(new java.awt.Dimension(100, 100));
        rSyntaxTextArea.setMinimumSize(new java.awt.Dimension(100, 100));
        rSyntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LUA);


        rSyntaxTextArea.setLineWrap(true);

        rSyntaxTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {


                int length = rSyntaxTextArea.getText().length();
                int key = e.getKeyCode();

                if (key == KeyEvent.VK_BACK_SPACE) {
                    if (rSyntaxTextArea.getCaretPosition() < location.get()) {
                        rSyntaxTextArea.setCaretPosition(length);
                    }
                }

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

                        if (null != command && !command.isEmpty()) {
                            history.add(command);
                            history_loc = history.size();
                        }
                    }
                }


                if (e.isControlDown() && key == KeyEvent.VK_MINUS && history_loc > 0) {
                    //上翻历史
                    history_loc--;
                    try {
                        rSyntaxTextArea.getDocument().remove(location.get(), length - location.get());
                    } catch (BadLocationException e1) {
                        rSyntaxTextArea.setText(rSyntaxTextArea.getText().substring(0, location.get() + 1));
                    }
                    rSyntaxTextArea.append(history.get(history_loc));

                } else if (e.isControlDown() && key == KeyEvent.VK_EQUALS && history_loc < history.size() - 1) {
                    //下翻历史
                    history_loc++;
                    try {
                        rSyntaxTextArea.getDocument().remove(location.get(), length - location.get());
                    } catch (BadLocationException e1) {
                        rSyntaxTextArea.setText(rSyntaxTextArea.getText().substring(0, location.get() + 1));
                    }
                    rSyntaxTextArea.append(history.get(history_loc));

                }

                if (e.isControlDown() && (key == KeyEvent.VK_MINUS || key == KeyEvent.VK_EQUALS)) {
                    rSyntaxTextArea.setCaretPosition(rSyntaxTextArea.getText().length());
                }


            }

        });


        setMaximumSize(new java.awt.Dimension(100, 100));
        setMinimumSize(new java.awt.Dimension(100, 100));
        setPreferredSize(new java.awt.Dimension(100, 100));

        setViewportView(rSyntaxTextArea);
        rSyntaxTextArea.getAccessibleContext().setAccessibleParent(this);


        List rl = new ArrayList();
        rl.add(rSyntaxTextArea.getInputMap().get(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK, false)));
        //rl.add(rSyntaxTextArea.getInputMap().get(KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.KEY_LOCATION_UNKNOWN, false)));
        ActionMap am = rSyntaxTextArea.getActionMap();
        while (null != am) {
            for (Object b : rl)
                am.remove(b);
            am = am.getParent();
        }


        popup = new JPopupMenu();
        JMenuItem MenuItemTerminalClear = new JMenuItem();
        MenuItemTerminalClear.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemTerminalClear.setIcon(Icon.TERMINAL_CLEAR);
        MenuItemTerminalClear.setText(Context.BUNDLE.getString("Clear"));
        MenuItemTerminalClear.addActionListener(evt -> clean());
        popup.add(MenuItemTerminalClear);

        JMenuItem MenuItemTerminalCopy = new JMenuItem();
        MenuItemTerminalCopy.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemTerminalCopy.setIcon(Icon.COPY);
        MenuItemTerminalCopy.setText(Context.BUNDLE.getString("Copy"));
        MenuItemTerminalCopy.setToolTipText("Copy selected text to system clipboard");
        MenuItemTerminalCopy.setEnabled(false);
        MenuItemTerminalCopy.addActionListener(evt -> copy());
        popup.add(MenuItemTerminalCopy);

        JCheckBoxMenuItem AutoScroll = new JCheckBoxMenuItem();
        AutoScroll.setSelected(Config.ins.getAutoScroll());
        AutoScroll.setText(Context.BUNDLE.getString("AutoScroll"));
        AutoScroll.setToolTipText("terminalArea AutoScroll Enable/Disable");
        AutoScroll.addActionListener(evt -> Config.ins.setAutoScroll(AutoScroll.isSelected()));
        popup.add(AutoScroll);

        JCheckBoxMenuItem EOL = new JCheckBoxMenuItem();
        EOL.setSelected(Config.ins.getShowEOL());
        EOL.setText("EOL");
        EOL.setToolTipText("EOL visible Enable/Disable");
        EOL.addItemListener(evt -> setEOLMarkersVisible(EOL.isSelected()));
        popup.add(EOL);

        popup.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                try {
                    MenuItemTerminalCopy.setEnabled(hasSelected());
                } catch (Exception exp) {
                    MenuItemTerminalCopy.setEnabled(false);
                }
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
        });
        rSyntaxTextArea.setPopupMenu(popup);
    }


    /**
     * clean terminal text
     */
    public void clean() {
        control.set(false);
        rSyntaxTextArea.setText("");
        location.set(0);
        control.set(true);
    }


    public void setEOLMarkersVisible(boolean b) {
        rSyntaxTextArea.setEOLMarkersVisible(b);
    }


    public void updateTheme() {
        Theme theme = Context.GetTheme();
        if (null != theme) {
            setTheme(theme);
            setFontSize(Config.ins.getTerminal_font_size());
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


    public void setTheme(Theme theme) {
        theme.apply(rSyntaxTextArea);
    }


    public synchronized void add(String rc) {
        add.set(true);
        rSyntaxTextArea.append(rc);

        if (add.get() && Config.ins.getAutoScroll()) {
            int loca = rSyntaxTextArea.getText().length();
            try {
                rSyntaxTextArea.setCaretPosition(loca);
            } catch (Exception e) {
                logger.log(Level.WARNING, "SCROLL ERROR : " + e.getMessage(), e);
            }
            location.set(loca);
        }

        add.set(false);
    }


    public void comment(String s, boolean end) {
        String pre = "---";

        add("\r\n" + pre + s);
        if (end) {
            add("\r\n");
        }

    }

    public static interface CommandListener {
        void listen(String command);
    }

}
