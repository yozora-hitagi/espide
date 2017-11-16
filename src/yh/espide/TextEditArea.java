package yh.espide;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.GroupLayout;
import javax.swing.JLayeredPane;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by yozora on 2017/11/16.
 */
public class TextEditArea {

    public static final ArrayList<TextEditArea> TEXT_EDIT_AREAS = new ArrayList<>();




    public JLayeredPane pane;
    public RSyntaxTextArea rSyntaxTextArea;
    public File file;
    public boolean filechanged;
    public AutoCompletion autoCompletion;

    public TextEditArea(FirmwareType type){
        pane=new JLayeredPane();

        rSyntaxTextArea = Context.create1(type);


        file=new File("");
        filechanged=false;

        autoCompletion=new AutoCompletion(Context.create2(type));
        autoCompletion.install(rSyntaxTextArea);


        RTextScrollPane scrollPane = new RTextScrollPane();
        scrollPane.setViewportView(rSyntaxTextArea);
        scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setIconRowHeaderEnabled(false);
        scrollPane.setLineNumbersEnabled(true);
        scrollPane.setFoldIndicatorEnabled(true);

        GroupLayout layout = new javax.swing.GroupLayout(pane);
        pane.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
        );

        pane.setLayer(scrollPane, javax.swing.JLayeredPane.DEFAULT_LAYER);
    }


}
