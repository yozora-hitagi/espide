package yh.espide;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

/**
 * Created by yozora on 2017/1/10.
 */
public class Startup {


    public static EspIDE ide;

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if (info.getClassName().equals("javax.swing.plaf.nimbus.NimbusLookAndFeel")) {
                    UIManager.setLookAndFeel(info.getClassName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            java.awt.EventQueue.invokeLater(() -> {
                ide = new EspIDE();
                ide.setVisible(true);
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
