package yh.espide;


import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JSlider;
import java.awt.Frame;
import java.awt.GridLayout;


/**
 * Created by yozora on 2017/6/7.
 */
public class SettingsFrame extends JDialog {


    public SettingsFrame(Frame owner) {
        super(owner);
        setTitle("Setting");
        setIconImage(new ImageIcon(getClass().getResource("/resources/settings2.png")).getImage());


        JLayeredPane NodeMCUSettings = new JLayeredPane();
        NodeMCUSettings.setAutoscrolls(true);
        NodeMCUSettings.setOpaque(true);

        JLayeredPane OptionsOther = OptionsOther();
        JLayeredPane OptionsFileSendMode = OptionsFileSendMode();

        NodeMCUSettings.setLayer(OptionsOther, javax.swing.JLayeredPane.DEFAULT_LAYER);
        NodeMCUSettings.setLayer(OptionsFileSendMode, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout NodeMCUSettingsLayout = new javax.swing.GroupLayout(NodeMCUSettings);
        NodeMCUSettings.setLayout(NodeMCUSettingsLayout);
        NodeMCUSettingsLayout.setHorizontalGroup(
                NodeMCUSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(NodeMCUSettingsLayout.createSequentialGroup()
                                .addGroup(NodeMCUSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(OptionsOther)
                                        .addComponent(OptionsFileSendMode, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        )
        );
        NodeMCUSettingsLayout.setVerticalGroup(
                NodeMCUSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(NodeMCUSettingsLayout.createSequentialGroup()
                                .addGroup(NodeMCUSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(NodeMCUSettingsLayout.createSequentialGroup()
                                                .addComponent(OptionsOther, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                )
                                .addComponent(OptionsFileSendMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 134, Short.MAX_VALUE))
        );


        add(NodeMCUSettings);
        setSize(300, 500);

        setLocation(owner.getLocation());
    }


    JLabel EditorThemeLabel = new JLabel();
    JComboBox EditorTheme = new JComboBox();

    private JLayeredPane OptionsOther() {




        EditorThemeLabel.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        EditorThemeLabel.setText("Editor color theme");

        EditorTheme.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        EditorTheme.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Default", "Dark", "Eclipse", "IDEA", "Visual Studio", "Default-alt"}));
        EditorTheme.addActionListener(evt -> EditorThemeActionPerformed(evt));


        JLayeredPane OptionsOther = new JLayeredPane();

        OptionsOther.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Other", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 10))); // NOI18N


        OptionsOther.setLayer(EditorThemeLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        OptionsOther.setLayer(EditorTheme, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout OptionsOtherLayout = new javax.swing.GroupLayout(OptionsOther);
        OptionsOther.setLayout(OptionsOtherLayout);
        OptionsOtherLayout.setHorizontalGroup(
                OptionsOtherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)

                        .addGroup(OptionsOtherLayout.createSequentialGroup()
                                .addComponent(EditorThemeLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(EditorTheme, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())


        );
        OptionsOtherLayout.setVerticalGroup(
                OptionsOtherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(OptionsOtherLayout.createSequentialGroup()

                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(OptionsOtherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(EditorThemeLabel)
                                        .addComponent(EditorTheme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
        );

        return OptionsOther;
    }

    JLabel DelayLabel = new JLabel();
    JSlider Delay = new javax.swing.JSlider();
    JLabel AnswerDelayLabel = new javax.swing.JLabel();
    JSlider AnswerDelay = new javax.swing.JSlider();
    JCheckBox DumbMode = new javax.swing.JCheckBox();
    JLabel LineDelayLabel = new javax.swing.JLabel();
    JSlider LineDelay = new javax.swing.JSlider();
    JCheckBox TurboMode = new javax.swing.JCheckBox();

    private JLayeredPane OptionsFileSendMode() {

        DelayLabel.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        DelayLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        DelayLabel.setText("Delay after answer = 0 ms");
        DelayLabel.setToolTipText("It's not \"line delay\", as you known. It's delay between answer from ESP and send new data");


        Delay.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        Delay.setMajorTickSpacing(500);
        Delay.setMaximum(1000);
        Delay.setMinorTickSpacing(100);
        Delay.setPaintLabels(true);
        Delay.setPaintTicks(true);
        Delay.setSnapToTicks(true);
        Delay.setToolTipText("Delay between answer from ESP and send new data");
        Delay.setValue(0);
        Delay.setAlignmentY(1.0F);
        Delay.addChangeListener(evt -> DelayStateChanged(evt));

        AnswerDelayLabel.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        AnswerDelayLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AnswerDelayLabel.setText("Answer timeout = 3 s");
        AnswerDelayLabel.setToolTipText("How many time we waiting answer from ESP8266");

        AnswerDelay.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        AnswerDelay.setMajorTickSpacing(5);
        AnswerDelay.setMaximum(10);
        AnswerDelay.setMinorTickSpacing(1);
        AnswerDelay.setPaintLabels(true);
        AnswerDelay.setPaintTicks(true);
        AnswerDelay.setSnapToTicks(true);
        AnswerDelay.setToolTipText("Maximum time for waiting firmware answer");
        AnswerDelay.setValue(3);
        AnswerDelay.addChangeListener(evt -> AnswerDelayStateChanged(evt));

        DumbMode.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        DumbMode.setText("\"Dumb Mode\", never check answers");
        DumbMode.addItemListener(evt -> DumbModeItemStateChanged(evt));

        LineDelayLabel.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        LineDelayLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LineDelayLabel.setText("Line delay for \"Dumb Mode\" = 200 ms");
        LineDelayLabel.setToolTipText("It's usual \"line delay\", as you known.");


        LineDelay.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        LineDelay.setMajorTickSpacing(500);
        LineDelay.setMaximum(1000);
        LineDelay.setMinorTickSpacing(100);
        LineDelay.setPaintLabels(true);
        LineDelay.setPaintTicks(true);
        LineDelay.setSnapToTicks(true);
        LineDelay.setToolTipText("Fixed delay between lines");
        LineDelay.setValue(200);
        LineDelay.setAlignmentY(1.0F);


        LineDelay.addChangeListener(evt -> LineDelayStateChanged(evt));

        TurboMode.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        TurboMode.setText("\"Turbo Mode\"");
        TurboMode.addActionListener(evt -> TurboModeActionPerformed(evt));


        JLayeredPane OptionsFileSendMode = new JLayeredPane();

        OptionsFileSendMode.setLayout(new GridLayout(8,1));

        OptionsFileSendMode.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Send", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 10))); // NOI18N
        OptionsFileSendMode.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        OptionsFileSendMode.add(TurboMode);
        OptionsFileSendMode.add(DelayLabel);
        OptionsFileSendMode.add(Delay);
        OptionsFileSendMode.add(AnswerDelayLabel);
        OptionsFileSendMode.add(AnswerDelay);
        OptionsFileSendMode.add(DumbMode);
        OptionsFileSendMode.add(LineDelayLabel);
        OptionsFileSendMode.add(LineDelay);

        return OptionsFileSendMode;
    }



    private void EditorThemeActionPerformed(java.awt.event.ActionEvent evt) {
        int n = EditorTheme.getSelectedIndex();
        Config.ins.setColor_theme(n);
        Startup.ide.updateTheme(true); // for all
    }

    private void DelayStateChanged(javax.swing.event.ChangeEvent evt) {
        DelayLabel.setText("Delay after answer = " + Integer.toString(Delay.getValue()) + " ms");
        Config.ins.setDelay_after_answer(Delay.getValue());
    }

    private void TurboModeActionPerformed(java.awt.event.ActionEvent evt) {
        if (TurboMode.isSelected()) {
            DumbMode.setEnabled(false);
            LineDelayLabel.setEnabled(false);
            LineDelay.setEnabled(false);
            DumbMode.setSelected(false);
        } else {
            DumbMode.setEnabled(true);
            LineDelayLabel.setEnabled(true);
            LineDelay.setEnabled(true);
        }
        Config.ins.setTurbo_mode(TurboMode.isSelected());
    }

    private void LineDelayStateChanged(javax.swing.event.ChangeEvent evt) {
        LineDelayLabel.setText("Line delay for \"Dumb Mode\" = " + Integer.toString(LineDelay.getValue()) + " ms");
        Config.ins.setLine_delay_for_dumb(LineDelay.getValue());
    }

    private void DumbModeItemStateChanged(java.awt.event.ItemEvent evt) {
        if (DumbMode.isSelected()) {
            DelayLabel.setEnabled(false);
            Delay.setEnabled(false);
            AnswerDelayLabel.setEnabled(false);
            AnswerDelay.setEnabled(false);
            TurboMode.setSelected(false);
            TurboMode.setEnabled(false);
        } else {
            DelayLabel.setEnabled(true);
            Delay.setEnabled(true);
            AnswerDelayLabel.setEnabled(true);
            AnswerDelay.setEnabled(true);
            TurboMode.setEnabled(true);
        }
        Config.ins.setDumb_mode(DumbMode.isSelected());
    }

    private void AnswerDelayStateChanged(javax.swing.event.ChangeEvent evt) {
        AnswerDelayLabel.setText("Answer timout = " + Integer.toString(AnswerDelay.getValue()) + " s");
        Config.ins.setAnswer_timeout(AnswerDelay.getValue());
    }


}
