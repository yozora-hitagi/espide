package yh.espide;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.GroupLayout;
import javax.swing.JLayeredPane;
import javax.swing.JTabbedPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yozora on 2017/11/16.
 */
public class TextEditArea extends JLayeredPane {
    private static final String NewFile = "(*)";

    public RSyntaxTextArea rSyntaxTextArea;
    public File file;
    // public boolean filechanged;
    public AutoCompletion autoCompletion;
    JTabbedPane parent;
    Logger logger = Logger.getLogger(TextEditArea.class.getName());
    TextChangedListener listener;
    private String title = NewFile;

    public TextEditArea(JTabbedPane parent, FirmwareType type) {
        this.parent = parent;


        rSyntaxTextArea = new RSyntaxTextArea(new RSyntaxDocument(SyntaxConstants.SYNTAX_STYLE_LUA) {
            @Override
            public void remove(int offs, int len) throws BadLocationException {
                super.remove(offs, len);
                if (null != listener) {
                    listener.TextChanged(TextEditArea.this);
                }
            }


            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                super.insertString(offs, str, a);
                if (null != listener) {
                    listener.TextChanged(TextEditArea.this);
                }
            }
        });

        if (FirmwareType.MicroPython.eq(type)) {
            rSyntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);
        } else {
            rSyntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LUA);
        }
        rSyntaxTextArea.setColumns(20);
        rSyntaxTextArea.setRows(5);
        rSyntaxTextArea.setDragEnabled(false);
        rSyntaxTextArea.setFadeCurrentLineHighlight(true);
        rSyntaxTextArea.setPaintMarkOccurrencesBorder(true);
        rSyntaxTextArea.setPaintMatchedBracketPair(true);
        rSyntaxTextArea.setCodeFoldingEnabled(false);
        rSyntaxTextArea.setAntiAliasingEnabled(true);
        rSyntaxTextArea.setTabsEmulated(true);
        rSyntaxTextArea.setBracketMatchingEnabled(true);
        rSyntaxTextArea.setTabSize(4);

        rSyntaxTextArea.setPopupMenu(null);


        file = new File("");
        //filechanged = false;

        autoCompletion = new AutoCompletion(Context.create2(type));
        autoCompletion.install(rSyntaxTextArea);


        RTextScrollPane scrollPane = new RTextScrollPane();
        scrollPane.setViewportView(rSyntaxTextArea);
        scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setIconRowHeaderEnabled(false);
        scrollPane.setLineNumbersEnabled(true);
        scrollPane.setFoldIndicatorEnabled(true);

        GroupLayout layout = new javax.swing.GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
        );

        setLayer(scrollPane, javax.swing.JLayeredPane.DEFAULT_LAYER);


        parent.addTab(title, this);
        parent.setSelectedIndex(parent.getTabCount() - 1);
    }

    public void setTextChangedListener(TextChangedListener listener) {
        this.listener = listener;
    }

    public boolean isNew() {
        return NewFile.equals(title);
    }

    public void copy() {
        rSyntaxTextArea.copy();
    }

    public void cut() {
        rSyntaxTextArea.cut();
        //filechanged=true;
    }

    public void paste() {
        rSyntaxTextArea.paste();
        //filechanged=true;
    }


    public boolean isChanged() {
        return rSyntaxTextArea.canUndo();
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
        if (null != parent) {
            for (int i = 0; i < parent.getTabCount(); i++) {
                if (this.equals(parent.getComponentAt(i))) {
                    parent.setTitleAt(i, title);
                    return;
                }
            }
        }
    }

    public void reload_file(ProgressV v) {
        try {
            FileReader fr = new FileReader(file);

            long length = file.length();
            logger.info("Load file " + file + " ->length:" + length);
            long count = 0;

            try {
                rSyntaxTextArea.getDocument().remove(0, rSyntaxTextArea.getDocument().getLength());
            } catch (BadLocationException e) {
                //这个卡主了。 可能是 remove 和insertstring 重写有listener的调用 效率不行
                rSyntaxTextArea.setText("");
            }

            char[] buf = new char[40 * 1024];
            int len = -1;
            while ((len = fr.read(buf)) > 0 && !v.needstop()) {
                count += len;
                rSyntaxTextArea.append(new String(buf, 0, len));
                double p = count * 100.0 / length;
                logger.info("Load file " + file + " ->progress:" + p);
                v.setV((int) p);
            }
            fr.close();

            rSyntaxTextArea.discardAllEdits();
        } catch (IOException e) {
            logger.log(Level.WARNING, "", e);
        }
    }


    public void save_file() throws IOException {
        Context.Save(file, rSyntaxTextArea.getText().getBytes());
        rSyntaxTextArea.discardAllEdits();
    }


    public static interface TextChangedListener {
        void TextChanged(TextEditArea area);
    }

}
