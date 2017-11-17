package yh.espide;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.Font;

/**
 * Created by yozora on 2017/6/9.
 */
public class EditButton extends JButton {

    public EditButton(String text, String img, String tip) {
        setFont(new Font("雅黑", 0, 10)); // NOI18N
        setIcon(new ImageIcon(getClass().getResource(img))); // NOI18N
        setText(text);
        setToolTipText(tip);

        setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        setMaximumSize(new Dimension(40, 40));
        setMinimumSize(new Dimension(40, 40));
        setPreferredSize(new Dimension(40, 40));
        setVerticalTextPosition(SwingConstants.BOTTOM);
    }

}
