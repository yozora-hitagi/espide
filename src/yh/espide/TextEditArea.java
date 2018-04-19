package yh.espide;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
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
    // public boolean filechanged;
    public AutoCompletion autoCompletion;
    JTabbedPane parent;
    Logger logger = Logger.getLogger(TextEditArea.class.getName());
    TextChangedListener listener;
    private File file;
    private String title = NewFile;

    public TextEditArea(JTabbedPane parent, File file) {
        this.parent = parent;

        this.file = file;

        String suffix = FileType.Get_suffix(file);
        this.setTitle(file.getName());

        rSyntaxTextArea = new RSyntaxTextArea(new RSyntaxDocument(FileType.SYNTAX_MAP.get(suffix)) {
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

        rSyntaxTextArea.setSyntaxEditingStyle(FileType.SYNTAX_MAP.get(suffix));

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


        autoCompletion = new AutoCompletion(FileType.create(suffix));
        autoCompletion.install(rSyntaxTextArea);


        RTextScrollPane scrollPane = new RTextScrollPane();
        scrollPane.setViewportView(rSyntaxTextArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setIconRowHeaderEnabled(false);
        scrollPane.setLineNumbersEnabled(true);
        scrollPane.setFoldIndicatorEnabled(true);

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
        );

        setLayer(scrollPane, JLayeredPane.DEFAULT_LAYER);


        parent.addTab(title, this);
        parent.setSelectedIndex(parent.getTabCount() - 1);
    }

    public void setTextChangedListener(TextChangedListener listener) {
        this.listener = listener;
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

    public File getFile() {
        return this.file;
    }

    public boolean isChanged() {
        return rSyntaxTextArea.canUndo();
    }

    public String getTitle() {
        return this.title;
    }

    private void setTitle(String title) {
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
