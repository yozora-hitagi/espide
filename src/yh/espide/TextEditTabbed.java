package yh.espide;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.File;

/**
 * Created by yozora on 2017/11/27.
 */
public class TextEditTabbed extends JTabbedPane {

    EspIDE ide;

    private SelectChangedListener selectchanged;

    public TextEditTabbed(EspIDE ide) {
        this.ide = ide;
        addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (null != selectchanged) {
                    selectchanged.SelectChanged((TextEditArea) getSelectedComponent());
                }
            }
        });
    }

    public void setSelectChangedListener(SelectChangedListener listener) {
        selectchanged = listener;
    }


    public void RemoveTab() {
        int index = getSelectedIndex();
        if (getTabCount() <= 1) {
            AddTab("");
        }
        if (index >= 0 && index < getTabCount()) {
            removeTabAt(index);
        }
    }


    public TextEditArea AddTab(File file) {
        TextEditArea area = (TextEditArea) getSelectedComponent();
        if (null == area || !area.isNew() || area.isChanged()) {
            area = AddTab("");
        }
        area.file = file;
        area.setTitle(file.getName());
        return area;
    }

    public void updateTheme() {
        Theme theme = Context.GetTheme();
        if (null != theme) {
            for (int i = 0; i < getTabCount(); i++) {
                RSyntaxTextArea area = ((TextEditArea) getComponentAt(i)).rSyntaxTextArea;
                theme.apply(area);
                area.setFont(area.getFont().deriveFont(Config.ins.getEditor_font_size()));
            }
        }
    }

    public TextEditArea AddTab(String content) {

        TextEditArea area = new TextEditArea(this, FirmwareType.current);


        area.rSyntaxTextArea.addCaretListener(evt ->
                ide.UpdateEditorButtons(area)
        );
        area.setTextChangedListener(new TextEditArea.TextChangedListener() {
            @Override
            public void TextChanged(TextEditArea area) {
                ide.UpdateEditorButtons(area);
            }
        });


        Theme theme = Context.GetTheme();
        if (null != theme) {
            theme.apply(area.rSyntaxTextArea);
            area.rSyntaxTextArea.setFont(area.rSyntaxTextArea.getFont().deriveFont(Config.ins.getEditor_font_size()));
        }

        if (null != selectchanged) {
            selectchanged.SelectChanged(area);
        }

        if (null != content && !content.isEmpty()) {
            area.rSyntaxTextArea.append(content);
        }

        return area;
    }

    public static interface SelectChangedListener {
        void SelectChanged(TextEditArea area);
    }

}
