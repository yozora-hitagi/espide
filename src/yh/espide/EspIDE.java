package yh.espide;

import jssc.*;
import org.jdesktop.beansbinding.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;


public class EspIDE extends JFrame implements TerminalHandler.CommandListener, FileList.ItemClickedListener, TextEditTabbed.SelectChangedListener {

    public static final Logger LOGGER = Logger.getLogger(EspIDE.class.getName());


    public static ArrayList<String> sendBuf;


    public static int j = 0;
    public static boolean sendPending = false;


    public static String rx_data = "";


    public static boolean busyIcon = false;


    public ActionListener taskPerformer;

    public Timer timer;
    public WatchDog watchDog = new WatchDog(this);


    public ArrayList<byte[]> sendPackets;
    public ArrayList<Boolean> sendPacketsCRC;


    public FileList fileList = new FileList();


    public TerminalHandler thandler = new TerminalHandler(this);
    private JCheckBoxMenuItem AlwaysOnTop;
    private JLabel Busy;
    private JButton ButtonCopy;
    private JButton ButtonCut;
    private JButton ButtonFileClose;
    private JButton ButtonFileNew;
    private JButton ButtonFileOpen;
    private JButton ButtonFileReload;
    private JButton ButtonFileSave;
    private JButton ButtonPaste;
    private JButton ButtonRedo;
    private JButton ButtonSendLine;
    private JButton ButtonSendSelected;
    private JButton ButtonUndo;


    private JButton FileFormat;

    private JButton FileListReload;
    private JScrollPane FileManagerScrollPane;
    private JLayeredPane FileManagersLayer;
    private JLabel FilePathLabel;
    private JTextField FileRename;
    private JLabel FileRenameLabel;
    private JLayeredPane FileRenamePanel;
    private JToggleButton FileSaveESP;
    private JToggleButton FileSendESP;
    private JButton FileSystemInfo;
    private TextEditTabbed FilesTabbedPane;
    private JToolBar FilesToolBar;
    private JButton FilesUpload;
    private JSplitPane HorizontSplit;
    private JLayeredPane LEDPanel;

    private JLayeredPane LeftBasePane;
    private JLayeredPane LeftMainButtons;
    private JMenuItem MenuItemEditCopy;
    private JMenuItem MenuItemEditCut;
    private JMenuItem MenuItemEditPaste;
    private JMenuItem MenuItemEditRedo;
    private JMenuItem MenuItemEditSendLine;
    private JMenuItem MenuItemEditSendSelected;
    private JMenuItem MenuItemEditUndo;
    private JMenuItem MenuItemFileClose;
    private JMenuItem MenuItemFileNew;
    private JMenuItem MenuItemFileOpen;
    private JMenuItem MenuItemFileReload;
    private JMenuItem MenuItemFileSave;


    private JCheckBoxMenuItem MenuItemViewEditorOnly;
    private JCheckBoxMenuItem MenuItemViewFileManager;
    private JCheckBoxMenuItem MenuItemViewTerminalOnly;
    private JCheckBoxMenuItem MenuItemViewToolbar;
    private JLayeredPane NodeFileMgrPane;
    private JPanel NodeMCU;
    private JToggleButton Open;
    private JComboBox Port;
    private JLabel PortCTS;
    private JToggleButton PortDTR;
    private JLabel PortOpenLabel;
    private JToggleButton PortRTS;


    private JButton ReScan;
    private JLayeredPane RightBasePane;
    private JSplitPane RightTerminalSplitPane;
    private JComboBox Speed;
    private JLayeredPane SriptsTab;


    private BindingGroup bindingGroup;

    private ArrayList<File> upload_file_list;
    private int upload_file_index = -1;


    // private int iTab = 0; // tab index

    private String UploadFileName = "";
    private long startTime = System.currentTimeMillis();


    public EspIDE() {
        setTitle(Version.title());
        initComponents();
        FinalInit();
    }

    private void initComponents() {
        bindingGroup = new BindingGroup();

        HorizontSplit = new JSplitPane();
        LeftBasePane = new JLayeredPane();

        NodeMCU = new JPanel();

        SriptsTab = new JLayeredPane();
        FilesToolBar = new JToolBar();
        FilesTabbedPane = new TextEditTabbed(this);


        Busy = new JLabel();
        FilePathLabel = new JLabel();
        LeftMainButtons = new JLayeredPane();
        FileSaveESP = new JToggleButton();
        FileSendESP = new JToggleButton();

        FilesUpload = new JButton();


        RightBasePane = new JLayeredPane();
        LEDPanel = new JLayeredPane();
        PortOpenLabel = new JLabel();
        PortCTS = new JLabel();
        PortDTR = new JToggleButton();
        PortRTS = new JToggleButton();
        Open = new JToggleButton();
        Speed = new JComboBox();
        ReScan = new JButton();
        Port = new JComboBox();


        RightTerminalSplitPane = new JSplitPane();

        FileManagerScrollPane = new JScrollPane();
        FileManagersLayer = new JLayeredPane();


        NodeFileMgrPane = new JLayeredPane();
        FileFormat = new JButton();
        FileSystemInfo = new JButton();
        FileListReload = new JButton();
        FileRenamePanel = new JLayeredPane();
        FileRenameLabel = new JLabel();
        FileRename = new JTextField();


        MenuItemFileNew = new JMenuItem();
        MenuItemFileOpen = new JMenuItem();
        MenuItemFileReload = new JMenuItem();
        MenuItemFileSave = new JMenuItem();
        MenuItemFileClose = new JMenuItem();


        MenuItemEditUndo = new JMenuItem();
        MenuItemEditRedo = new JMenuItem();

        MenuItemEditCut = new JMenuItem();
        MenuItemEditCopy = new JMenuItem();
        MenuItemEditPaste = new JMenuItem();

        MenuItemEditSendSelected = new JMenuItem();
        MenuItemEditSendLine = new JMenuItem();


        AlwaysOnTop = new JCheckBoxMenuItem();

        MenuItemViewTerminalOnly = new JCheckBoxMenuItem();
        MenuItemViewEditorOnly = new JCheckBoxMenuItem();

        MenuItemViewToolbar = new JCheckBoxMenuItem();
        MenuItemViewFileManager = new JCheckBoxMenuItem();


        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setAutoRequestFocus(false);
        setBounds(new Rectangle(0, 0, 0, 0));
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        setFocusCycleRoot(false);
        setLocationByPlatform(true);
        setMinimumSize(new Dimension(100, 100));
        setPreferredSize(new Dimension(1024, 768));
        addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                formComponentResized(evt);
            }
        });


        addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent evt) {
            }

            public void windowClosing(WindowEvent evt) {
                formWindowClosing(evt);
            }

            public void windowOpened(WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        HorizontSplit.setDividerLocation(550);
        HorizontSplit.setMinimumSize(new Dimension(100, 100));
        HorizontSplit.setPreferredSize(new Dimension(768, 567));
        HorizontSplit.addPropertyChangeListener(evt -> reLocationRightSplit());

        LeftBasePane.setOpaque(true);


        NodeMCU.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        NodeMCU.setMinimumSize(new Dimension(100, 100));
        NodeMCU.setPreferredSize(new Dimension(461, 537));
        NodeMCU.addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent evt) {
                NodeMCUComponentShown(evt);
            }
        });


        SriptsTab.setMinimumSize(new Dimension(460, 350));
        SriptsTab.setOpaque(true);

        FilesToolBar.setFloatable(false);
        FilesToolBar.setRollover(true);
        FilesToolBar.setAlignmentY(0.5F);
        FilesToolBar.setMaximumSize(new Dimension(1000, 40));
        FilesToolBar.setMinimumSize(new Dimension(321, 40));
        FilesToolBar.setPreferredSize(new Dimension(321, 40));


        ButtonFileNew = new EditButton(Context.BUNDLE.getString("New"), Icon.DOCUMENT, "New file");
        Binding binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, MenuItemFileNew, ELProperty.create("${enabled}"), ButtonFileNew, BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);
        ButtonFileNew.addActionListener(evt -> MenuItemFileNew.doClick());
        FilesToolBar.add(ButtonFileNew);

        ButtonFileOpen = new EditButton(Context.BUNDLE.getString("Open"), Icon.FOLDER_OPEN, "AddTab file from disk");
        ButtonFileOpen.addActionListener(evt -> MenuItemFileOpen.doClick());
        FilesToolBar.add(ButtonFileOpen);

        ButtonFileReload = new EditButton(Context.BUNDLE.getString("Reload"), Icon.REFRESH, "Reload file from disk (for use with external editor)");
        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, MenuItemFileReload, ELProperty.create("${enabled}"), ButtonFileReload, BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);
        ButtonFileReload.addActionListener(evt -> MenuItemFileReload.doClick());
        FilesToolBar.add(ButtonFileReload);

        ButtonFileSave = new EditButton(Context.BUNDLE.getString("Save"), Icon.SAVE, "Save file to disk");
        ButtonFileSave.addActionListener(evt -> MenuItemFileSave.doClick());
        FilesToolBar.add(ButtonFileSave);

        ButtonFileClose = new EditButton(Context.BUNDLE.getString("Close"), Icon.FOLDER_CLOSED, "Close file");
        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, MenuItemFileClose, ELProperty.create("${enabled}"), ButtonFileClose, BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);
        ButtonFileClose.addActionListener(evt -> MenuItemFileClose.doClick());
        FilesToolBar.add(ButtonFileClose);
        FilesToolBar.add(new JSeparator());

        ButtonUndo = new EditButton(Context.BUNDLE.getString("Undo"), Icon.UNDO1, "Undo last action");
        ButtonUndo.setEnabled(false);
        ButtonUndo.setFocusable(false);
        ButtonUndo.addActionListener(evt -> MenuItemEditUndo.doClick());
        FilesToolBar.add(ButtonUndo);

        ButtonRedo = new EditButton(Context.BUNDLE.getString("Redo"), Icon.REDO1, "Redo last action");
        ButtonRedo.setEnabled(false);
        ButtonRedo.setFocusable(false);
        ButtonRedo.addActionListener(evt -> MenuItemEditRedo.doClick());
        FilesToolBar.add(ButtonRedo);
        FilesToolBar.add(new JSeparator());

        ButtonCut = new EditButton(Context.BUNDLE.getString("Cut"), Icon.CUT, "Cut");
        ButtonCut.setEnabled(false);
        ButtonCut.addActionListener(evt -> MenuItemEditCut.doClick());
        FilesToolBar.add(ButtonCut);

        ButtonCopy = new EditButton(Context.BUNDLE.getString("Copy"), Icon.COPY, "Copy");
        ButtonCopy.setEnabled(false);
        ButtonCopy.addActionListener(evt -> MenuItemEditCopy.doClick());
        FilesToolBar.add(ButtonCopy);

        ButtonPaste = new EditButton(Context.BUNDLE.getString("Paste"), Icon.PASTE, "Paste");
        ButtonPaste.setEnabled(false);
        ButtonPaste.addActionListener(evt -> MenuItemEditPaste.doClick());
        FilesToolBar.add(ButtonPaste);
        FilesToolBar.add(new JSeparator());

        ButtonSendSelected = new EditButton("Block", Icon.SEND_SELECTED, "Send selected block to ESP");
        ButtonSendSelected.addActionListener(evt -> MenuItemEditSendSelected.doClick());
        FilesToolBar.add(ButtonSendSelected);

        ButtonSendLine = new EditButton("Line", Icon.RUN_LINE, "Send current line to ESP");
        ButtonSendLine.setFocusable(false);
        ButtonSendLine.addActionListener(evt -> MenuItemEditSendLine.doClick());
        FilesToolBar.add(ButtonSendLine);


        Busy.setBackground(new Color(0, 153, 0));
        Busy.setForeground(new Color(255, 255, 255));
        Busy.setHorizontalAlignment(SwingConstants.CENTER);
        Busy.setIcon(Icon.LED_GREY);
        Busy.setText("IDLE");
        Busy.setOpaque(true);


        LeftMainButtons.setOpaque(true);
        LeftMainButtons.setLayout(new FlowLayout());

        FileSaveESP.setIcon(Icon.MOVE);
        FileSaveESP.setText("Save to ESP");
        FileSaveESP.setToolTipText("Send file to ESP and save into flash memory");
        FileSaveESP.setHorizontalAlignment(SwingConstants.LEFT);
        FileSaveESP.setIconTextGap(8);
        FileSaveESP.setMargin(new Insets(2, 2, 2, 2));
        FileSaveESP.setMaximumSize(new Dimension(127, 30));
        FileSaveESP.setMinimumSize(new Dimension(127, 30));
        FileSaveESP.setPreferredSize(new Dimension(127, 30));
        FileSaveESP.addActionListener(evt -> FileSaveESPActionPerformed(evt));
        LeftMainButtons.add(FileSaveESP);


        FileSendESP.setIcon(Icon.PLAY);
        FileSendESP.setText("Exec on ESP");
        FileSendESP.setToolTipText("Send file to ESP and run  \"line by line\"");
        FileSendESP.setHorizontalAlignment(SwingConstants.LEFT);
        FileSendESP.setIconTextGap(8);
        FileSendESP.setMargin(new Insets(2, 2, 2, 2));
        FileSendESP.setMaximumSize(new Dimension(127, 30));
        FileSendESP.setMinimumSize(new Dimension(127, 30));
        FileSendESP.setPreferredSize(new Dimension(127, 30));
        FileSendESP.addActionListener(evt -> FileSendESPActionPerformed(evt));
        LeftMainButtons.add(FileSendESP);


        FilesUpload.setIcon(Icon.UPLOADLUA);
        FilesUpload.setText("Upload ...");
        FilesUpload.setToolTipText("Upload file from disk to ESP flash memory");
        FilesUpload.setHorizontalAlignment(SwingConstants.LEFT);
        FilesUpload.setIconTextGap(8);
        FilesUpload.setMargin(new Insets(2, 2, 2, 2));
        FilesUpload.setMaximumSize(new Dimension(127, 30));
        FilesUpload.setMinimumSize(new Dimension(127, 30));
        FilesUpload.setPreferredSize(new Dimension(127, 30));
        FilesUpload.addActionListener(evt -> UploadFiles());
        LeftMainButtons.add(FilesUpload);

        SriptsTab.setLayer(FilesToolBar, JLayeredPane.PALETTE_LAYER);
        SriptsTab.setLayer(FilesTabbedPane, JLayeredPane.DEFAULT_LAYER);

        SriptsTab.setLayer(Busy, JLayeredPane.DEFAULT_LAYER);
        SriptsTab.setLayer(FilePathLabel, JLayeredPane.DEFAULT_LAYER);
        SriptsTab.setLayer(Progress.ins, JLayeredPane.DEFAULT_LAYER);
        SriptsTab.setLayer(LeftMainButtons, JLayeredPane.DEFAULT_LAYER);

        GroupLayout SriptsTabLayout = new GroupLayout(SriptsTab);
        SriptsTab.setLayout(SriptsTabLayout);
        SriptsTabLayout.setHorizontalGroup(
                SriptsTabLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(FilesTabbedPane, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(FilesToolBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(SriptsTabLayout.createSequentialGroup()
                                .addGroup(SriptsTabLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(Progress.ins, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(GroupLayout.Alignment.LEADING, SriptsTabLayout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addComponent(Busy, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(FilePathLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))

                                        .addComponent(LeftMainButtons, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 531, GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        SriptsTabLayout.setVerticalGroup(
                SriptsTabLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(SriptsTabLayout.createSequentialGroup()
                                .addComponent(FilesToolBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(FilesTabbedPane)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Progress.ins, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(SriptsTabLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(Busy)
                                        .addComponent(FilePathLabel))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)

                                .addComponent(LeftMainButtons, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );


        SriptsTab.getAccessibleContext().setAccessibleName("Files");


        GroupLayout NodeMCULayout = new GroupLayout(NodeMCU);
        NodeMCU.setLayout(NodeMCULayout);
        NodeMCULayout.setHorizontalGroup(
                NodeMCULayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(SriptsTab, GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
        );
        NodeMCULayout.setVerticalGroup(
                NodeMCULayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(SriptsTab, GroupLayout.DEFAULT_SIZE, 712, Short.MAX_VALUE)
        );


        LeftBasePane.setLayer(NodeMCU, JLayeredPane.DEFAULT_LAYER);

        GroupLayout LeftBasePaneLayout = new GroupLayout(LeftBasePane);
        LeftBasePane.setLayout(LeftBasePaneLayout);
        LeftBasePaneLayout.setHorizontalGroup(
                LeftBasePaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(NodeMCU, GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
        );
        LeftBasePaneLayout.setVerticalGroup(
                LeftBasePaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(NodeMCU, GroupLayout.DEFAULT_SIZE, 744, Short.MAX_VALUE)
        );


        HorizontSplit.setLeftComponent(LeftBasePane);

        RightBasePane.setOpaque(true);

        LEDPanel.setMaximumSize(new Dimension(392, 25));
        LEDPanel.setMinimumSize(new Dimension(392, 25));
        LEDPanel.setOpaque(true);

        PortOpenLabel.setHorizontalAlignment(SwingConstants.CENTER);
        PortOpenLabel.setIcon(Icon.LED_GREY);
        PortOpenLabel.setText("Open");
        PortOpenLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        PortOpenLabel.setMaximumSize(new Dimension(50, 25));
        PortOpenLabel.setMinimumSize(new Dimension(50, 25));
        PortOpenLabel.setPreferredSize(new Dimension(50, 25));
        PortOpenLabel.setVerticalTextPosition(SwingConstants.BOTTOM);

        PortCTS.setHorizontalAlignment(SwingConstants.CENTER);
        PortCTS.setIcon(Icon.LED_GREY);
        PortCTS.setText("CTS");
        PortCTS.setHorizontalTextPosition(SwingConstants.CENTER);
        PortCTS.setMaximumSize(new Dimension(50, 25));
        PortCTS.setMinimumSize(new Dimension(50, 25));
        PortCTS.setPreferredSize(new Dimension(50, 25));
        PortCTS.setVerticalTextPosition(SwingConstants.BOTTOM);

        PortDTR.setIcon(Icon.LED_GREY);
        PortDTR.setText("DTR");
        PortDTR.setHorizontalTextPosition(SwingConstants.CENTER);
        PortDTR.setVerticalAlignment(SwingConstants.TOP);
        PortDTR.setVerticalTextPosition(SwingConstants.BOTTOM);
        PortDTR.addActionListener(evt -> PortDTRActionPerformed(evt));

        PortRTS.setIcon(Icon.LED_GREY);
        PortRTS.setText("RTS");
        PortRTS.setHorizontalTextPosition(SwingConstants.CENTER);
        PortRTS.setVerticalAlignment(SwingConstants.TOP);
        PortRTS.setVerticalTextPosition(SwingConstants.BOTTOM);
        PortRTS.addActionListener(evt -> PortRTSActionPerformed(evt));

        Open.setFont(Context.FONT_10);
        Open.setIcon(Icon.CONNECT1);
        Open.setText("Open");
        Open.setToolTipText("AddTab/Close selected serial port");
        Open.setIconTextGap(2);
        Open.setMargin(new Insets(1, 1, 1, 1));
        Open.setMaximumSize(new Dimension(100, 25));
        Open.setMinimumSize(new Dimension(85, 25));
        Open.setPreferredSize(new Dimension(80, 25));
        Open.addActionListener(evt -> OpenActionPerformed(evt));

        Speed.setFont(Context.FONT_12);
        Speed.setModel(new DefaultComboBoxModel(new Integer[]{1200, 2400, 4800, 9600, 19200, 38400, 57600, 74880, 115200, 230400, 460800, 921600}));
        Speed.setToolTipText("Select baud rate");
        Speed.setMaximumSize(new Dimension(80, 25));
        Speed.setMinimumSize(new Dimension(80, 25));
        Speed.setPreferredSize(new Dimension(80, 25));

        ReScan.setIcon(Icon.REFRESH3);
        ReScan.setMaximumSize(new Dimension(80, 25));
        ReScan.setMinimumSize(new Dimension(80, 25));
        ReScan.setPreferredSize(new Dimension(80, 25));
        ReScan.addActionListener(evt -> PortFinder());


        Port.setFont(Context.FONT_10);
        Port.setMaximumRowCount(20);
        Port.setModel(new DefaultComboBoxModel(new String[]{}));
        Port.setToolTipText("Serial port chooser");
        Port.setMaximumSize(new Dimension(150, 25));
        Port.setMinimumSize(new Dimension(150, 25));
        Port.setPreferredSize(new Dimension(150, 25));

        Port.setEditable(true);


        LEDPanel.setLayer(PortOpenLabel, JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(PortCTS, JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(PortDTR, JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(PortRTS, JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(Open, JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(Speed, JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(ReScan, JLayeredPane.DEFAULT_LAYER);

        LEDPanel.setLayer(Port, JLayeredPane.DEFAULT_LAYER);


        GroupLayout LEDPanelLayout = new GroupLayout(LEDPanel);
        LEDPanel.setLayout(LEDPanelLayout);
        LEDPanelLayout.setHorizontalGroup(
                LEDPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(LEDPanelLayout.createSequentialGroup()
                                .addGroup(LEDPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(LEDPanelLayout.createSequentialGroup()
                                                .addGroup(LEDPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(LEDPanelLayout.createSequentialGroup()
                                                                .addComponent(PortDTR)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(PortRTS))
                                                        .addGroup(LEDPanelLayout.createSequentialGroup()
                                                                .addComponent(PortOpenLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(PortCTS, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(Open, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(LEDPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(LEDPanelLayout.createSequentialGroup()
                                                                .addComponent(ReScan, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                                        )
                                                        .addGroup(LEDPanelLayout.createSequentialGroup()
                                                                .addComponent(Speed, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))))
                                        .addComponent(Port, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        LEDPanelLayout.setVerticalGroup(
                LEDPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(LEDPanelLayout.createSequentialGroup()
                                .addComponent(Port, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(LEDPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(GroupLayout.Alignment.TRAILING, LEDPanelLayout.createSequentialGroup()
                                                .addGroup(LEDPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(ReScan, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                )
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(LEDPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(Speed, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                ))
                                        .addGroup(GroupLayout.Alignment.TRAILING, LEDPanelLayout.createSequentialGroup()
                                                .addGroup(LEDPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(PortOpenLabel, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(PortCTS, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(LEDPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(PortDTR)
                                                        .addComponent(PortRTS)))
                                        .addComponent(Open, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );


        RightTerminalSplitPane.setAutoscrolls(true);
        RightTerminalSplitPane.addPropertyChangeListener(evt -> RightFilesSplitPanePropertyChange(evt));


        RightTerminalSplitPane.setLeftComponent(thandler);

        FileManagerScrollPane.setBorder(null);
        FileManagerScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


        NodeFileMgrPane.setMaximumSize(new Dimension(145, 145));
        NodeFileMgrPane.setPreferredSize(new Dimension(145, 145));
        FlowLayout flowLayout1 = new FlowLayout(FlowLayout.LEADING, 2, 2);
        flowLayout1.setAlignOnBaseline(true);
        NodeFileMgrPane.setLayout(flowLayout1);

        FileFormat.setIcon(Icon.FILE_MANAGER_DELETE);
        FileFormat.setText(Context.BUNDLE.getString("FS Format"));
        FileFormat.setToolTipText("Format (erase) NodeMCU file system. All files will be removed!");
        FileFormat.setHorizontalAlignment(SwingConstants.LEFT);
        FileFormat.setMargin(new Insets(2, 4, 2, 4));
        FileFormat.setMaximumSize(new Dimension(130, 25));
        FileFormat.setMinimumSize(new Dimension(130, 25));
        FileFormat.setPreferredSize(new Dimension(130, 25));
        FileFormat.addActionListener(evt -> MenuItemESPFormatActionPerformed(evt));
        NodeFileMgrPane.add(FileFormat);

        FileSystemInfo.setIcon(Icon.FILE_MANAGER);
        FileSystemInfo.setText(Context.BUNDLE.getString("FS Info"));
        FileSystemInfo.setToolTipText("Execute command file.fsinfo() and show total, used and remainig space on the ESP filesystem");
        FileSystemInfo.setAlignmentX(0.5F);
        FileSystemInfo.setHorizontalAlignment(SwingConstants.LEFT);
        FileSystemInfo.setMargin(new Insets(2, 2, 2, 2));
        FileSystemInfo.setMaximumSize(new Dimension(130, 25));
        FileSystemInfo.setPreferredSize(new Dimension(130, 25));
        FileSystemInfo.addActionListener(evt -> NodeFileSystemInfo());
        NodeFileMgrPane.add(FileSystemInfo);

        FileListReload.setFont(Context.FONT_12);
        FileListReload.setIcon(Icon.REFRESH3);
        FileListReload.setText(Context.BUNDLE.getString("FS Reload"));
        FileListReload.setAlignmentX(0.5F);
        FileListReload.setHorizontalAlignment(SwingConstants.LEFT);
        FileListReload.setMargin(new Insets(2, 2, 2, 2));
        FileListReload.setMaximumSize(new Dimension(130, 25));
        FileListReload.setPreferredSize(new Dimension(130, 25));
        FileListReload.addActionListener(evt -> NodeListFiles());
        NodeFileMgrPane.add(FileListReload);

        FileRenamePanel.setMaximumSize(new Dimension(130, 45));
        FileRenamePanel.setMinimumSize(new Dimension(130, 45));

        FileRenameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        FileRenameLabel.setText("Old file name");
        FileRenameLabel.setToolTipText("Input new file name and hit Enter to completed or press Reload for cancel");
        FileRenameLabel.setMaximumSize(new Dimension(130, 14));
        FileRenameLabel.setMinimumSize(new Dimension(130, 14));
        FileRenameLabel.setPreferredSize(new Dimension(130, 14));

        FileRename.setText("NewFileName");
        FileRename.setToolTipText("Input new file name and hit Enter to completed or press Reload for cancel");
        FileRename.setMaximumSize(new Dimension(130, 25));
        FileRename.setMinimumSize(new Dimension(130, 25));
        FileRename.setPreferredSize(new Dimension(130, 25));
        FileRename.addActionListener(evt -> FileRenameActionPerformed(evt));

        FileRenamePanel.setLayer(FileRenameLabel, JLayeredPane.DEFAULT_LAYER);
        FileRenamePanel.setLayer(FileRename, JLayeredPane.DEFAULT_LAYER);

        GroupLayout FileRenamePanelLayout = new GroupLayout(FileRenamePanel);
        FileRenamePanel.setLayout(FileRenamePanelLayout);
        FileRenamePanelLayout.setHorizontalGroup(
                FileRenamePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(FileRenamePanelLayout.createSequentialGroup()
                                .addGroup(FileRenamePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(FileRename, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(FileRenameLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        FileRenamePanelLayout.setVerticalGroup(
                FileRenamePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(FileRenamePanelLayout.createSequentialGroup()
                                .addComponent(FileRenameLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(FileRename, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        NodeFileMgrPane.add(FileRenamePanel);


        // fileList.setMaximumSize(new Dimension(500, 155));
        fileList.setPreferredSize(new Dimension(150, 400));
        fileList.setItemclicked(this);


        FileManagersLayer.setLayer(NodeFileMgrPane, JLayeredPane.DEFAULT_LAYER);


        NodeFileMgrPane.setVisible(true);


        GroupLayout FileManagersLayerLayout = new GroupLayout(FileManagersLayer);
        FileManagersLayer.setLayout(FileManagersLayerLayout);
        FileManagersLayerLayout.setHorizontalGroup(
                FileManagersLayerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(FileManagersLayerLayout.createSequentialGroup()
                                .addGroup(FileManagersLayerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(NodeFileMgrPane, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 433, Short.MAX_VALUE))
        );
        FileManagersLayerLayout.setVerticalGroup(
                FileManagersLayerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(FileManagersLayerLayout.createSequentialGroup()
                                .addComponent(NodeFileMgrPane, GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                        )
        );

        FileManagerScrollPane.setViewportView(FileManagersLayer);

        RightTerminalSplitPane.setRightComponent(FileManagerScrollPane);


        RightBasePane.setLayer(LEDPanel, JLayeredPane.DEFAULT_LAYER);
        RightBasePane.setLayer(RightTerminalSplitPane, JLayeredPane.DEFAULT_LAYER);

        GroupLayout RightBasePaneLayout = new GroupLayout(RightBasePane);
        RightBasePane.setLayout(RightBasePaneLayout);
        RightBasePaneLayout.setHorizontalGroup(
                RightBasePaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(RightBasePaneLayout.createSequentialGroup()
                                .addGroup(RightBasePaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(RightTerminalSplitPane)
                                        .addGroup(RightBasePaneLayout.createSequentialGroup()
                                        )
                                        .addGroup(RightBasePaneLayout.createSequentialGroup()
                                                .addGroup(RightBasePaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)

                                                        .addComponent(LEDPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        RightBasePaneLayout.setVerticalGroup(
                RightBasePaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(RightBasePaneLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(LEDPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(RightTerminalSplitPane)
                                .addGap(5, 5, 5)

                                .addGroup(RightBasePaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                ))
        );


        HorizontSplit.setRightComponent(RightBasePane);

        JMenuBar menu = new JMenuBar();

        JMenu MenuFile = Context.createM1(Context.BUNDLE.getString("File"));

        JMenuItem settings = new JMenuItem(Context.BUNDLE.getString("Setting"));
        settings.setIcon(Icon.SETTINGS2);
        settings.addActionListener(evt -> {
            new SettingsFrame(this).setVisible(true);
        });
        MenuFile.add(settings);

        MenuItemFileNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        MenuItemFileNew.setIcon(Icon.DOCUMENT);
        MenuItemFileNew.setText(Context.BUNDLE.getString("New"));
        MenuItemFileNew.setToolTipText("File New");
        MenuItemFileNew.addActionListener(e -> {
            File[] file = Context.ShowFileDialog(null, Context.BUNDLE.getString("Create new file"), null, false, false);
            if (null == file) {
                return;
            }
            if (file[0].exists()) {
                Context.Dialog("File " + file[0] + " existed!!!", JOptionPane.YES_OPTION);
            } else {
                try {
                    if (file[0].createNewFile()) {
                        OpenFile(file[0]);
                        return;
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                Context.Dialog("File " + file[0] + " create fail!!!", JOptionPane.YES_OPTION);
            }
        });
        MenuFile.add(MenuItemFileNew);

        MenuItemFileOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        MenuItemFileOpen.setIcon(Icon.FOLDER_OPEN);
        MenuItemFileOpen.setText(Context.BUNDLE.getString("Open"));
        MenuItemFileOpen.addActionListener(evt -> {
            new Thread() {
                @Override
                public void run() {
                    OpenFile();
                }
            }.start();

        });
        MenuFile.add(MenuItemFileOpen);

        MenuItemFileReload.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
        MenuItemFileReload.setIcon(Icon.REFRESH);
        MenuItemFileReload.setText(Context.BUNDLE.getString("Reload"));
        MenuItemFileReload.setToolTipText("Reload file from disk, if you use external editor");
        MenuItemFileReload.addActionListener(evt -> {
            new Thread() {
                @Override
                public void run() {
                    ReloadFile();
                }
            }.start();

        });
        MenuFile.add(MenuItemFileReload);

        MenuItemFileSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        MenuItemFileSave.setIcon(Icon.SAVE);
        MenuItemFileSave.setText(Context.BUNDLE.getString("Save"));
        MenuItemFileSave.addActionListener(evt -> SaveFile());
        MenuFile.add(MenuItemFileSave);


        MenuItemFileClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.CTRL_MASK));
        MenuItemFileClose.setIcon(Icon.FOLDER_CLOSED);
        MenuItemFileClose.setText(Context.BUNDLE.getString("Close"));
        MenuItemFileClose.addActionListener(evt -> MenuItemFileCloseActionPerformed(evt));
        MenuFile.add(MenuItemFileClose);
        MenuFile.add(new JPopupMenu.Separator());


        JMenuItem MenuItemFileExit = new JMenuItem();
        MenuItemFileExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
        MenuItemFileExit.setText(Context.BUNDLE.getString("Exit"));
        MenuItemFileExit.addActionListener(evt -> AppClose());
        MenuFile.add(MenuItemFileExit);

        menu.add(MenuFile);

        JMenu MenuEdit = Context.createM1(Context.BUNDLE.getString("Edit"));

        MenuItemEditUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
        MenuItemEditUndo.setIcon(Icon.UNDO1);
        MenuItemEditUndo.setText(Context.BUNDLE.getString("Undo"));
        MenuItemEditUndo.setEnabled(false);
        MenuItemEditUndo.addActionListener(evt -> MenuItemEditUndoActionPerformed(evt));
        MenuEdit.add(MenuItemEditUndo);

        MenuItemEditRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK));
        MenuItemEditRedo.setIcon(Icon.REDO1);
        MenuItemEditRedo.setText(Context.BUNDLE.getString("Redo"));
        MenuItemEditRedo.setEnabled(false);
        MenuItemEditRedo.addActionListener(evt -> MenuItemEditRedoActionPerformed(evt));
        MenuEdit.add(MenuItemEditRedo);
        MenuEdit.add(new JPopupMenu.Separator());

        MenuItemEditCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        MenuItemEditCut.setIcon(Icon.CUT);
        MenuItemEditCut.setText(Context.BUNDLE.getString("Cut"));
        MenuItemEditCut.setEnabled(false);
        MenuItemEditCut.addActionListener(evt -> getCurrentEdit().cut());
        MenuEdit.add(MenuItemEditCut);

        MenuItemEditCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        MenuItemEditCopy.setIcon(Icon.COPY);
        MenuItemEditCopy.setText(Context.BUNDLE.getString("Copy"));
        MenuItemEditCopy.setEnabled(false);
        MenuItemEditCopy.addActionListener(evt -> getCurrentEdit().copy());
        MenuEdit.add(MenuItemEditCopy);

        MenuItemEditPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
        MenuItemEditPaste.setIcon(Icon.PASTE);
        MenuItemEditPaste.setText(Context.BUNDLE.getString("Paste"));
        MenuItemEditPaste.setEnabled(false);
        MenuItemEditPaste.addActionListener(evt -> getCurrentEdit().paste());
        MenuEdit.add(MenuItemEditPaste);
        MenuEdit.add(new JPopupMenu.Separator());

        MenuItemEditSendSelected.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.ALT_MASK));
        MenuItemEditSendSelected.setIcon(Icon.SEND_SELECTED);
        MenuItemEditSendSelected.setText("<html>Send selected <u>B</u>lock to ESP");
        MenuItemEditSendSelected.setToolTipText("Send selected block to ESP");


        MenuItemEditSendSelected.addActionListener(evt -> MenuItemEditSendSelectedActionPerformed(evt));
        MenuEdit.add(MenuItemEditSendSelected);

        MenuItemEditSendLine.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.ALT_MASK));
        MenuItemEditSendLine.setIcon(Icon.RUN_LINE);
        MenuItemEditSendLine.setText("<html>Send current <u>L</u>ine to ESP");
        MenuItemEditSendLine.setToolTipText("Send current line from code editor window to ESP");
        MenuItemEditSendLine.addActionListener(evt -> MenuItemEditSendLineActionPerformed(evt));
        MenuEdit.add(MenuItemEditSendLine);

        menu.add(MenuEdit);


        JMenu MenuView = Context.createM1(Context.BUNDLE.getString("View"));

        AlwaysOnTop.setText("Always On Top");
        AlwaysOnTop.setIcon(Icon.ALWAYSONTOP);
        AlwaysOnTop.addItemListener(evt -> AlwaysOnTopItemStateChanged(evt));
        MenuView.add(AlwaysOnTop);


        MenuView.add(new JPopupMenu.Separator());

        MenuItemViewTerminalOnly.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, InputEvent.ALT_MASK));
        MenuItemViewTerminalOnly.setText("Show terminalArea only (Left panel show/hide)");
        MenuItemViewTerminalOnly.setToolTipText("Enable/disable left panel");
        MenuItemViewTerminalOnly.addItemListener(evt -> LeftRightOnlyShowStateChanged(evt));
        MenuView.add(MenuItemViewTerminalOnly);

        MenuItemViewEditorOnly.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, InputEvent.ALT_MASK));
        MenuItemViewEditorOnly.setText("Show Editor only (Right panel show/hide)");
        MenuItemViewEditorOnly.setToolTipText("Enable/disable left panel");
        MenuItemViewEditorOnly.addItemListener(evt -> LeftRightOnlyShowStateChanged(evt));
        MenuView.add(MenuItemViewEditorOnly);
        MenuView.add(new JPopupMenu.Separator());


        MenuItemViewToolbar.setText("Show toolbar at top left");
        MenuItemViewToolbar.setToolTipText("Enable/disable files toolbar at top left");
        MenuItemViewToolbar.addItemListener(evt -> MenuItemViewToolbarItemStateChanged(evt));
        MenuView.add(MenuItemViewToolbar);


        MenuItemViewFileManager.setSelected(true);
        MenuItemViewFileManager.setText("Show FileManager panel at right");
        MenuItemViewFileManager.setToolTipText("Enable/disable FileManager panel at right");
        MenuItemViewFileManager.addItemListener(evt -> MenuItemViewFileManagerItemStateChanged(evt));
        MenuView.add(MenuItemViewFileManager);


        menu.add(MenuView);


        setJMenuBar(menu);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(HorizontSplit, GroupLayout.DEFAULT_SIZE, 1024, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(HorizontSplit, GroupLayout.DEFAULT_SIZE, 746, Short.MAX_VALUE)
        );

        getAccessibleContext().setAccessibleDescription("");

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void OpenActionPerformed(ActionEvent evt) {
        if (Open.isSelected()) {
            Open.setSelected(portOpen());
            if (Open.isSelected()) {
                String port = Port.getSelectedItem().toString().trim();
                Config.ins.setSerial_port(port);
                Config.ins.setSerial_baud_rate(getBaudRate());
            }
        } else {
            portClose();
        }
        UpdateButtons();
    }

    public void UpdateButtons() {
        if (Open.isSelected() && SerialObject.ins.getSerialPortStatus().isOpened()) {
            Port.setEnabled(false);
            ReScan.setEnabled(false);
        } else {
            Port.setEnabled(true);
            ReScan.setEnabled(true);
        }
        UpdateLED();

    }

    private void formWindowOpened(WindowEvent evt) {
        PortFinder();
        Progress.ins.setVisible(false);

        FilesToolBar.setVisible(Config.ins.getShowToolbar());

        this.setBounds(Config.ins.getWin_x(), Config.ins.getWin_y(), Config.ins.getWin_w(), Config.ins.getWin_h());
        isFileManagerShow();
    }


    private void isFileManagerShow() {
        int div;
        final int w = 160;
        if (MenuItemViewFileManager.isSelected()) {
            FileManagerScrollPane.setEnabled(true);
            FileManagerScrollPane.setVisible(true);
            //div = prefs.getInt( FM_DIV, RightTerminalSplitPane.getWidth()-w );
            //if ( div > RightTerminalSplitPane.getWidth()-w ) {
            div = RightTerminalSplitPane.getWidth() - w;
            //}
            RightTerminalSplitPane.setDividerLocation(div);
        } else {
            FileManagerScrollPane.setEnabled(false);
            FileManagerScrollPane.setVisible(false);
            RightTerminalSplitPane.setDividerLocation(RightTerminalSplitPane.getWidth() - RightTerminalSplitPane.getDividerSize());
        }
    }


    boolean SaveFile() {
        TextEditArea area = getCurrentEdit();
        try {
            try {
                LOGGER.info("Try to saving file " + area.getFile().getName() + " ...");
                area.save_file();
                String filename = area.getFile().getName();
                LOGGER.info("Save file " + filename + ": Success.");
                return true;
            } catch (IOException ex) {
                LOGGER.log(Level.WARNING, "Save file " + area.getFile().getName() + ": FAIL.", ex);
                JOptionPane.showMessageDialog(null, "Error, file not saved!");
                return false;
            }

        } finally {
            UpdateEditorButtons(area);
        }
    }


    private void OpenFile() {
        File[] files = Context.ShowFileDialog(LeftBasePane, null, null, true, true);
        if (null != files) {
            for (File file : files) {
                //文件已打开
                if (FilesTabbedPane.hashOpen(file)) {
                    UpdateEditorButtons(getCurrentEdit());
                    String filename = file.getName();
                    LOGGER.info("File " + filename + " already open, select tab to file " + filename);
                    JOptionPane.showMessageDialog(null, Context.FormatString("SF_HasOpened", filename));
                    continue;
                }

                OpenFile(file);

            }
        }
        UpdateEditorButtons(getCurrentEdit());
    }

    public void OpenFile(File file) {
        TextEditArea area = FilesTabbedPane.AddTab(file);
        try {
            Busy();
            Progress.ins.start("AddTab " + area.getFile().getName(), area::reload_file);
            SelectChanged(area);
            Idle();
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Loading " + area.getFile() + ": FAIL.", ex);
            JOptionPane.showMessageDialog(null, "Error, file not load!");
        }
        LOGGER.info("AddTab \"" + area.getFile().getName() + "\": Success.");
    }


    public void UpdateEditorButtons(TextEditArea area) {
        if (area == null) {
            return;
        }
        if (area.isChanged()) {
            MenuItemFileSave.setEnabled(true);
            ButtonFileSave.setEnabled(true);
        } else {
            MenuItemFileSave.setEnabled(false);
            ButtonFileSave.setEnabled(false);
        }

        // CanUndo
        if (area.rSyntaxTextArea.canUndo()) {
            MenuItemEditUndo.setEnabled(true);
            ButtonUndo.setEnabled(true);
        } else {
            MenuItemEditUndo.setEnabled(false);
            ButtonUndo.setEnabled(false);
        }
        // CanRedo
        if (area.rSyntaxTextArea.canRedo()) {
            MenuItemEditRedo.setEnabled(true);
            ButtonRedo.setEnabled(true);
        } else {
            MenuItemEditRedo.setEnabled(false);
            ButtonRedo.setEnabled(false);
        }

        ///////////
        if (area.rSyntaxTextArea.getSelectedText() == null) {
            MenuItemEditCut.setEnabled(false);
            MenuItemEditCopy.setEnabled(false);
            ButtonCut.setEnabled(false);
            ButtonCopy.setEnabled(false);
            MenuItemEditSendSelected.setEnabled(false);
            ButtonSendSelected.setEnabled(false);
        } else {
            MenuItemEditCut.setEnabled(true);
            MenuItemEditCopy.setEnabled(true);
            ButtonCut.setEnabled(true);
            ButtonCopy.setEnabled(true);
            MenuItemEditSendSelected.setEnabled(Open.isSelected());
            ButtonSendSelected.setEnabled(Open.isSelected());
        }
        try {
            if (Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null) == null) { // clipboard empty
                MenuItemEditPaste.setEnabled(false);
                ButtonPaste.setEnabled(false);
            } else {
                MenuItemEditPaste.setEnabled(true);
                ButtonPaste.setEnabled(true);
            }
        } catch (Exception e) {
        }

    }

    private void formFocusGained(FocusEvent evt) {
        UpdateEditorButtons(getCurrentEdit());
        UpdateButtons();
    }

    private void MenuItemFileCloseActionPerformed(ActionEvent evt) {
        CloseFile();
    }


    private void AlwaysOnTopItemStateChanged(ItemEvent evt) {
        this.setAlwaysOnTop(AlwaysOnTop.isSelected());
    }

    private void MenuItemEditUndoActionPerformed(ActionEvent evt) {
        if (getCurrentEdit().rSyntaxTextArea.canUndo()) {
            getCurrentEdit().rSyntaxTextArea.undoLastAction();
        }
    }

    private void MenuItemEditRedoActionPerformed(ActionEvent evt) {
        if (getCurrentEdit().rSyntaxTextArea.canRedo()) {
            getCurrentEdit().rSyntaxTextArea.redoLastAction();
        }
    }

    private void NodeListFiles() {
        if (!SerialObject.ins.getSerialPortStatus().isOpened()) {
            LOGGER.info("ERROR: Communication with MCU not yet established.");
            return;
        }
        String cmd = Context.GetLua("/yh/espide/file_list.lua");
        try {
            SerialObject.ins.removeEventListener();
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }
        try {
            SerialObject.ins.addEventListener(new PortNodeFilesReader());
            LOGGER.info("FileManager: Add EventListener: Success.");
        } catch (SerialPortException e) {
            LOGGER.info("FileManager: Add EventListener Error. Canceled.");
            return;
        }
        ClearNodeFileManager();
        rx_data = "";

        sendBuf = cmdPrep(cmd);
        LOGGER.info("FileManager: Starting...");
        SendLock();
        int delay = 10;
        j0();
        taskPerformer = evt -> {
            if (j < sendBuf.size()) {
                send(addCR(sendBuf.get(j)), false);
                sendPending = false;
            }
        };
        timer = new Timer(delay, taskPerformer);
        timer.setRepeats(false);
        timer.setInitialDelay(delay);
        watchDog.start();
        timer.start();
    }

    private void ClearNodeFileManager() {
        if (!MenuItemViewFileManager.isSelected()) {
            return;
        }
        NodeFileMgrPane.removeAll();
        NodeFileMgrPane.add(FileFormat);
        NodeFileMgrPane.add(FileSystemInfo);
        NodeFileMgrPane.add(FileListReload);
        NodeFileMgrPane.add(FileRenamePanel);


        fileList.removeall();
        NodeFileMgrPane.add(fileList);

        FileRenamePanel.setVisible(false);
        FileRenamePanel.setEnabled(false);
        NodeFileMgrPane.repaint();
    }

    private void FileDownload(String param, boolean open) {
        if (!SerialObject.ins.getSerialPortStatus().isOpened()) {
            LOGGER.info("Downloader: Communication with MCU not yet established.");
            return;
        }
        // param  init.luaSize:123
        String[] args = param.split("Size:");

        LOGGER.info("Downloader: Starting...");
        startTime = System.currentTimeMillis();
        SendLock();
        rx_data = "";


        try {
            SerialObject.ins.removeEventListener();
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }
        try {
            PortFileDownloader downloader = new PortFileDownloader(this, args[0], open, Integer.parseInt(args[1]));
            SerialObject.ins.addEventListener(downloader);
            downloader.start();
            LOGGER.info("Downloader: Add EventListener: Success.");
        } catch (SerialPortException e) {
            LOGGER.info("Downloader: Add EventListener Error. Canceled.");
            return;
        }
    }

    public void FileDownloadFinisher(Buffer buffer, String filename, boolean open) {
        try {
            SerialObject.ins.removeEventListener();
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }
        try {
            SerialObject.ins.addEventListener(new PortReader(this));
        } catch (SerialPortException e) {
            LOGGER.info("Downloader: Can't Add OldEventListener.");
        }
        //SendUnLock();
        StopSend();
        if (null != buffer) {
            thandler.comment("Success.", true);
            File file = SaveDownloadedFile(filename, buffer.toByteArray());
            if (open && null != file) {
                OpenFile(file);
            }
        } else {
            thandler.comment("FAIL.", true);
        }

    }

    public int CRC(byte[] s) {
        int cs = 0;
        int x;
        try {
            for (int i = 0; i < s.length; i++) {
                x = s[i] & 0xFF;
                //log( Integer.toHexString(x) );
                cs = cs + (x * 20) % 19;
            }
            //log("\r\nCRC size= " + Integer.toString(s.length)+ ", CRC="+Integer.toString(cs));
        } catch (Exception e) {
            LOGGER.info(e.toString());
            LOGGER.info(e.getStackTrace().toString());
            LOGGER.info("size=" + Integer.toString(s.length));
        }
        return cs;
    }

    private void HexDump(String FileName) {
        String cmd = Context.GetLua("/yh/espide/file_hexdump.lua", FileName);
        SendToESP(cmdPrep(cmd));
    }

    private ArrayList<String> cmdPrep(String cmd) {
        String[] str = cmd.split("\n");
        ArrayList<String> list = new ArrayList<>();
        int i = 0;
        list.add("");
        for (String subs : str) {
            if ((list.get(i).length() + subs.trim().length()) <= 250) {
                list.set(i, list.get(i) + " " + subs.trim());
            } else {
                list.set(i, list.get(i) + "\r");
                list.add(subs.trim());
                i++;
            }
        }
        return list;
    }

    public void UpdateLedCTS() {
        try {
            if (SerialObject.ins.isCTS()) {
                PortCTS.setIcon(Icon.LED_GREEN);
            } else {
                PortCTS.setIcon(Icon.LED_GREY);
            }
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }
    }

    private void MenuItemEditSendSelectedActionPerformed(ActionEvent evt) {
        int l = 0;

        try {
            l = getCurrentEdit().rSyntaxTextArea.getSelectedText().length();
        } catch (Exception e) {
            LOGGER.info("Can't send: nothing selected.");
            return;
        }
        if (l > 0) {
            SendToESP(getCurrentEdit().rSyntaxTextArea.getSelectedText());
        }

    }


    private void removeFileFromESP(String FileName) {
        btnSend("file.remove(\"" + FileName + "\")");
        try {
            Thread.sleep(200L);
        } catch (Exception e) {
        }
        FileListReload.doClick();
    }

    private void MenuItemFileSaveESPActionPerformed(ActionEvent evt) {
        if (!FileSaveESP.isSelected()) {
            FileSaveESP.doClick();
        }
    }

    private void MenuItemEditSendLineActionPerformed(ActionEvent evt) {
        int nLine;

        nLine = getCurrentEdit().rSyntaxTextArea.getCaretLineNumber();
        String cmd = getCurrentEdit().rSyntaxTextArea.getText().split("\r?\n")[nLine];
        btnSend(cmd);


    }

    private void MenuItemESPFormatActionPerformed(ActionEvent evt) {

        int isFormat = Context.Dialog("Format ESP flash data area and remove ALL files. Are you sure?", JOptionPane.YES_NO_OPTION);
        if (isFormat == JOptionPane.YES_OPTION) {
            btnSend("file.format()");
        }

    }

    private void RightFilesSplitPanePropertyChange(java.beans.PropertyChangeEvent evt) {
        if ("dividerLocation".equals(evt.getPropertyName()) && MenuItemViewFileManager.isSelected()) {
            Config.ins.setFm_div_dividerlocation(RightTerminalSplitPane.getDividerLocation());
        }
    }


    private void formComponentResized(ComponentEvent evt) {
        isFileManagerShow();
    }


    private void PortDTRActionPerformed(ActionEvent evt) {
        Config.ins.setPortDtr(PortDTR.isSelected());
        try {
            SerialObject.ins.setDTR(PortDTR.isSelected());
            if (PortDTR.isSelected()) {
                LOGGER.info("DTR set to ON");
            } else {
                LOGGER.info("DTR set to OFF");
            }
        } catch (Exception e) {
            PortDTR.setSelected(false);
            LOGGER.info(e.toString());
            LOGGER.info("Can't change DTR state");
        }
        UpdateLED();
    }

    private void PortRTSActionPerformed(ActionEvent evt) {
        Config.ins.setPortRts(PortRTS.isSelected());
        try {
            SerialObject.ins.setRTS(PortRTS.isSelected());
            if (PortRTS.isSelected()) {
                LOGGER.info("RTS set to ON");
            } else {
                LOGGER.info("RTS set to OFF");
            }
        } catch (Exception e) {
            PortRTS.setSelected(false);
            LOGGER.info(e.toString());
            LOGGER.info("Can't change RTS state");
        }
        UpdateLED();
    }

    private void MenuItemViewToolbarItemStateChanged(ItemEvent evt) {
        Config.ins.setShowToolbar(MenuItemViewToolbar.isSelected());

        FilesToolBar.setVisible(Config.ins.getShowToolbar());
    }

    private void MenuItemViewFileManagerItemStateChanged(ItemEvent evt) {
        Config.ins.setFm_right_show(MenuItemViewFileManager.isSelected());
        isFileManagerShow();
    }

    private void FileRenameActionPerformed(ActionEvent evt) {
        btnSend("file.rename(\"" + FileRenameLabel.getText() + "\",\"" + FileRename.getText().trim() + "\")");
        try {
            Thread.sleep(200L);
        } catch (Exception e) {
        }
        FileListReload.doClick();
    }

    private void formWindowClosing(WindowEvent evt) {
        AppClose();
    }

    private void AppClose() {
        Rectangle r = this.getBounds();
        Config.ins.setWinX(r.x);
        Config.ins.setWinY(r.y);
        Config.ins.setWinH(r.height);
        Config.ins.setWinW(r.width);
        while (FilesTabbedPane.getTabCount() > 0) {
            if (CloseFile() == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }
        this.setVisible(false);
        System.exit(0);
    }


    private void NodeMCUComponentShown(ComponentEvent evt) {
        UpdateEditorButtons(getCurrentEdit());
        UpdateButtons();
    }

    private void FileSendESPActionPerformed(ActionEvent evt) {
        if (FileSendESP.isSelected()) {
            if (getCurrentEdit().rSyntaxTextArea.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "File empty.");
                FileSendESP.setSelected(false);
                return;
            }
            SendToESP(getCurrentEdit().rSyntaxTextArea.getText());
        } else {
            StopSend();
        }
    }

    private void FileSaveESPActionPerformed(ActionEvent evt) {
        TextEditArea area = getCurrentEdit();
        if (area == null || !FileSaveESP.isSelected()) {
            StopSend();
            return;
        }
        if (area.rSyntaxTextArea.getText().length() == 0) {
            FileSaveESP.setSelected(false);
            JOptionPane.showMessageDialog(null, "File empty.");
            return;
        }
        String fName = area.getFile().getName();
        if (fName.length() == 0) {
            FileSaveESP.setSelected(false);
            String msg = " Can't save file to ESP without name.";
            LOGGER.info("FileSaveESP: FAIL. " + msg);
            JOptionPane.showMessageDialog(null, msg);
            return;
        }
        if (!Open.isSelected() || !SerialObject.ins.getSerialPortStatus().isOpened()) {
            LOGGER.info("FileSaveESP: Serial port not open. Operation canceled.");
            FileSaveESP.setSelected(false);
            return;
        }

        SaveToESP(fName);

    }


    private void reLocationRightSplit() {
        RightTerminalSplitPane.setDividerLocation(RightTerminalSplitPane.getWidth() - 180);
    }

    private void LeftRightOnlyShowStateChanged(ItemEvent evt) {
        if (evt.getSource() == MenuItemViewEditorOnly && MenuItemViewEditorOnly.isSelected()) {
            MenuItemViewTerminalOnly.setSelected(false);
            LeftBasePane.setVisible(true);
            RightBasePane.setVisible(false);
        } else if (evt.getSource() == MenuItemViewTerminalOnly && MenuItemViewTerminalOnly.isSelected()) {
            MenuItemViewEditorOnly.setSelected(false);
            LeftBasePane.setVisible(false);
            RightBasePane.setVisible(true);
        } else {
            LeftBasePane.setVisible(true);
            RightBasePane.setVisible(true);
            HorizontSplit.setDividerLocation(550);
        }
        reLocationRightSplit();
    }


    private void NodeFileSystemInfo() {
        String cmd = Context.GetLua("file_info.lua");
        send(addCRLF(cmd), true);
    }


    public void j0() {
        j = 0;
    }

    public void PortFinder() {
        Speed.setSelectedItem(Config.ins.getSerial_baud_rate());
        Port.removeAllItems();

        String[] portNames = SerialPortList.getPortNames();
        for (String p : portNames) {
            Port.addItem(p);
        }
        String lastPort = Config.ins.getSerialPort();
        Port.setSelectedItem(lastPort != null ? lastPort : portNames[0]);
    }

    public String GetSerialPortName() {
        return Port.getSelectedItem().toString();
    }

    public int getBaudRate() {
        return Integer.parseInt(String.valueOf(Speed.getSelectedItem()));
    }


    public boolean portOpen() {
        String portName = GetSerialPortName();
        LOGGER.info("Try to open port " + portName + ", baud " + getBaudRate() + ", 8N1");
        SerialObject.ins = new SerialObject(portName);
        try {
            if (!SerialObject.ins.openPort()) {
                LOGGER.info("ERROR opening serial port " + portName);
                return false;
            }

            try {
                if (!SerialObject.ins.setParams(getBaudRate(),
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE,
                        PortRTS.isSelected(),
                        PortDTR.isSelected())) {
                    LOGGER.info("ERROR setting port " + portName + " parameters.");
                }
            } catch (Exception e) {
                LOGGER.info(e.toString());
            }
            UpdateLED();
            SerialObject.ins.addEventListener(new PortReader(this));
        } catch (SerialPortException ex) {
            LOGGER.info(ex.toString());
        }

        LOGGER.info("AddTab port " + portName + " - Success.");
        thandler.comment("PORT OPEN " + getBaudRate(), true);
        btnSend("\r\n");
        return true;

    }

    public void portClose() {
        try {
            if (SerialObject.ins.closePort()) {
                thandler.comment("PORT CLOSED - Success.", false);
            } else {
                thandler.comment("PORT CLOSED - Fail.", false);
            }
        } catch (SerialPortException ex) {
            LOGGER.info(ex.toString());
        }

        UpdateLED();
        ClearNodeFileManager();
    }

    public String addCRLF(String cmd) {
        cmd += (char) 13;
        cmd += (char) 10;
        return cmd;
    }

    public String addCR(String s) {
        return s + (char) 13;
    }

    public void btnSend(String s) {
        send(addCRLF(s), true);
    }

    private void FinalInit() {
        setIconImage(Icon.ESP8299_64X64.getImage());
        setLocationRelativeTo(null); // window centered

        FilesTabbedPane.removeAll();
        FilesTabbedPane.setSelectChangedListener(this);
        LoadPrefs();
        FileRenamePanel.setVisible(false);
        updateTheme();
    }

    private void LoadPrefs() {
        MenuItemViewToolbar.setSelected(Config.ins.getShowToolbar());
        MenuItemViewFileManager.setSelected(Config.ins.getFm_right_show());
        isFileManagerShow();
        PortDTR.setSelected(Config.ins.getPortDtr());
        PortRTS.setSelected(Config.ins.getPortRts());
        LOGGER.info("Load saved settings: DONE.");
    }

    private void AddNodeFileButton(String FileName, int size) {
        JPopupMenu popup = new JPopupMenu();
        if (FileName.endsWith(".lua")) {
            AddMenuItemRun(popup, FileName);
            AddMenuItemCompile(popup, FileName);
            AddMenuItemView(popup, FileName);
            AddMenuItemEdit(popup, FileName, size);
        } else if (FileName.endsWith(".lc")) {
            AddMenuItemRun(popup, FileName);
        } else {
            AddMenuItemView(popup, FileName);
            AddMenuItemEdit(popup, FileName, size);
        }
        popup.add(new JPopupMenu.Separator());
        AddMenuItemRename(popup, FileName);
        AddMenuItemDump(popup, FileName);
        AddMenuItemDownload(popup, FileName, size);
        AddMenuItemRemove(popup, FileName);

        FileList.Item item = new FileList.Item();
        item.setText(FileName);
        item.setComponentPopupMenu(popup);
        fileList.addItem(item);


    }


    private void AddMenuItemEdit(JPopupMenu popup, String FileName, int size) {
        JMenuItem menu = new JMenuItem();
        menu.setIcon(Icon.EDIT);
        menu.setText("Edit " + FileName);
        menu.setToolTipText("Download file from ESP and open in new editor window");
        menu.setActionCommand(FileName + "Size:" + Integer.toString(size));
        menu.addActionListener(evt -> {
            FileDownload(evt.getActionCommand(), true);
        });
        popup.add(menu);
    }

    private void AddMenuItemDownload(JPopupMenu popup, String FileName, int size) {
        JMenuItem menu = new JMenuItem();
        menu.setIcon(Icon.DOWNLOAD);
        menu.setText("Download " + FileName);
        menu.setToolTipText("Download file from ESP and save to disk");
        menu.setActionCommand(FileName + "Size:" + Integer.toString(size));
        menu.addActionListener(evt -> {
            FileDownload(evt.getActionCommand(), false);
        });
        popup.add(menu);
    }

    private void AddMenuItemRun(JPopupMenu popup, String FileName) {
        JMenuItem menu = new JMenuItem();
        menu.setIcon(Icon.PLAY);
        menu.setText("Run " + FileName);
        menu.setToolTipText("Execute command dofile(\"" + FileName + "\") for run this file");
        menu.setActionCommand(FileName);
        menu.addActionListener(evt -> btnSend("dofile(\"" + evt.getActionCommand() + "\")"));
        popup.add(menu);
    }

    private void AddMenuItemCompile(JPopupMenu popup, String FileName) {
        JMenuItem menu = new JMenuItem();
        menu.setIcon(Icon.WIZARD);
        menu.setText("Compile " + FileName + " to .lc");
        menu.setToolTipText("Execute command node.compile(\"" + FileName + "\")");
        menu.setActionCommand(FileName);
        menu.addActionListener(evt -> {
            btnSend("node.compile(\"" + evt.getActionCommand() + "\")");
            try {
                Thread.sleep(500L);
            } catch (Exception e) {
            }
            FileListReload.doClick();
        });
        popup.add(menu);
    }

    private void AddMenuItemRename(JPopupMenu popup, String FileName) {
        JMenuItem menu = new JMenuItem();
        menu.setIcon(Icon.RENAME);
        menu.setText("Rename " + FileName);
        menu.setToolTipText("Execute command file.rename(\"" + FileName + "\",\"NewName\")");
        menu.setActionCommand(FileName);
        menu.addActionListener(evt -> {
            FileRename.setText(evt.getActionCommand());
            FileRenameLabel.setText(evt.getActionCommand());
            FileRenamePanel.setEnabled(true);
            FileRenamePanel.setVisible(true);
            FileRename.grabFocus();
        });
        popup.add(menu);
    }

    private void AddMenuItemRemove(JPopupMenu popup, String FileName) {
        JMenuItem menu = new JMenuItem();
        menu.setIcon(Icon.TRASH);
        menu.setText("Remove " + FileName);
        menu.setToolTipText("Execute command file.remove(\"" + FileName + "\") and delete file from NodeMCU filesystem");
        menu.setActionCommand(FileName);
        menu.addActionListener(evt -> removeFileFromESP(evt.getActionCommand()));
        popup.add(menu);
    }

    private void AddMenuItemView(JPopupMenu popup, String FileName) {
        JMenuItem menu = new JMenuItem();
        menu.setIcon(Icon.VIEW);
        menu.setText("View " + FileName);
        menu.setToolTipText("View content of file " + FileName + " on terminalArea");
        menu.setActionCommand(FileName);
        menu.addActionListener(evt -> ViewFile(evt.getActionCommand()));
        popup.add(menu);
    }

    private void AddMenuItemDump(JPopupMenu popup, String FileName) {
        JMenuItem menu = new JMenuItem();
        menu.setIcon(Icon.DUMP);
        menu.setText("HexDump " + FileName);
        menu.setToolTipText("View HexDump " + FileName + "in terminalArea");
        menu.setActionCommand(FileName);
        menu.addActionListener(evt -> HexDump(evt.getActionCommand()));
        popup.add(menu);
    }

    private TextEditArea getCurrentEdit() {
        return (TextEditArea) FilesTabbedPane.getSelectedComponent();
    }


    private int CloseFile() {
        TextEditArea area = getCurrentEdit();
        if (area != null && area.isChanged()) {
            LOGGER.info("File changed. Ask before closing.");
            int returnVal = Context.Dialog("Save file \"" + area.getFile() + "\" before closing?", JOptionPane.YES_NO_CANCEL_OPTION);
            if (returnVal == JOptionPane.YES_OPTION) {
                if (!SaveFile()) {
                    LOGGER.info("File close: FAIL (file not saved, closing aborted)");
                    return JOptionPane.CANCEL_OPTION;
                }
            } else if (returnVal == JOptionPane.CANCEL_OPTION) {
                LOGGER.info("User select: Continue editing.");
                return JOptionPane.CANCEL_OPTION;
            } else {
                LOGGER.info("User select: Close anyway.");
            }
        }
        FilesTabbedPane.RemoveTab();
        LOGGER.info("File close: Success.");
        return JOptionPane.YES_OPTION;
    }

    private void ReloadFile() {
        TextEditArea area = getCurrentEdit();
        if (area.isChanged()) {
            LOGGER.info("File reload: File changed. Ask before reloading.");
            int returnVal = Context.Dialog("Discard any changes and reload file from disk?", JOptionPane.YES_NO_OPTION);
            if (returnVal != JOptionPane.OK_OPTION) {
                LOGGER.info("File reload: FAIL (file not saved, reload cancelled by user choice)");
                return;
            } else {
                LOGGER.info("File reload: Reload anyway by user choice.");
            }
        }

        try {

            Busy();
            Progress.ins.start("Reload " + area.getFile().getAbsolutePath(), area::reload_file);
            SelectChanged(area);
            Idle();
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Loading " + area.getFile() + ": FAIL.", ex);
            JOptionPane.showMessageDialog(null, "Error, file not load!");
        }

        LOGGER.info("File reload: Success.");


    }

    public void StopSend() {
        try {
            SerialObject.ins.removeEventListener();
        } catch (Exception e) {
        }
        try {
            timer.stop();
        } catch (Exception e) {
        }
        try {
            SerialObject.ins.addEventListener(new PortReader(this));
        } catch (SerialPortException e) {
        }
        SendUnLock();
        long duration = System.currentTimeMillis() - startTime;
        LOGGER.info("Operation done. Duration = " + Long.toString(duration) + " ms");
    }

    private boolean SendToESP(String str) {
        String[] s = str.split("\r?\n");
        return SendToESP(Arrays.asList(s));
    }

    private boolean SendToESP(java.util.List<String> buf) {
        boolean success = false;
        if (!Open.isSelected() || !SerialObject.ins.getSerialPortStatus().isOpened()) {
            LOGGER.info("SendESP: Serial port not open. Cancel.");
            return success;
        }
        sendBuf = new ArrayList<>();
        sendBuf.addAll(buf);

        success = SendTimerStart();
        LOGGER.info("SendToESP: Starting...");
        return success;
    }


    private boolean SaveToESP(String ft) {
        boolean success = false;
        LOGGER.info("FileSaveESP: Try to save file to ESP...");
        sendBuf = new ArrayList<>();
        if (Config.ins.isTurbo_mode()) {
            return nodeSaveFileESPTurbo(ft);
        }
        sendBuf.add("file.remove(\"" + ft + "\");");
        sendBuf.add("file.open(\"" + ft + "\",\"w+\");");
        sendBuf.add("w = file.writeline\r\n");
        String[] s = getCurrentEdit().rSyntaxTextArea.getText().split("\r?\n");
        for (String subs : s) {
            sendBuf.add("w([==[" + subs + "]==]);");
        }
        sendBuf.add("file.close();");
        // data ready
        success = SendTimerStart();
        LOGGER.info("FileSaveESP: Starting...");
        return success;
    }

    private boolean nodeSaveFileESPTurbo(String ft) {
        LOGGER.info("FileSaveESP-Turbo: Try to save file to ESP in Turbo Mode...");
        sendBuf.add("local FILE=\"" + ft + "\" file.remove(FILE) file.open(FILE,\"w+\") uart.setup(0," + getBaudRate() + ",8,0,1,0)");
        sendBuf.add("ESP_Receiver=function(rcvBuf) if string.match(rcvBuf,\"^ESP_cmd_close\")==nil then file.write(string.gsub(rcvBuf, \'\\r\', \'\')) uart.write(0, \"> \") else uart.on(\"data\") ");
        sendBuf.add("file.flush() file.close() FILE=nil rcvBuf=nil ESP_Receiver=nil uart.setup(0," + getBaudRate() + ",8,0,1,1) str=\"\\r\\n--Done--\\r\\n> \" print(str) str=nil collectgarbage() end end uart.on(\"data\",'\\r',ESP_Receiver,0)");
        int pos1 = 0;
        int pos2 = 0;
        int size = 254;

        TextEditArea area = getCurrentEdit();

        int l = area.rSyntaxTextArea.getText().length();
        String fragment;
        while (pos1 <= l) {
            pos2 = pos1 + size;
            if (pos2 > l) {
                pos2 = l;
            }
            fragment = area.rSyntaxTextArea.getText().substring(pos1, pos2);
            sendBuf.add(fragment);
            pos1 += size;
        }
        sendBuf.add("ESP_cmd_close");
        sendBuf.add("\r\n");

        boolean success = SendTurboTimerStart();
        LOGGER.info("FileSaveESP-Turbo: Starting...");
        return success;
    }

    public boolean SendTurboTimerStart() {
        startTime = System.currentTimeMillis();
        SendLock();

        try {
            SerialObject.ins.removeEventListener();
        } catch (Exception e) {
        }
        try {
            SerialObject.ins.addEventListener(new PortTurboReader());
        } catch (SerialPortException e) {
            LOGGER.info("DataTurboSender: Add EventListener Error. Canceled.");
            return false;
        }
        int delay = 0;
        j0();
        delay = Config.ins.getDelay_after_answer();
        if (delay == 0) {
            delay = 10;
        }
        taskPerformer = evt -> {
            if (j < sendBuf.size()) {
                send(addCR(sendBuf.get(j)), false);
                sendPending = false;
            }
        };
        timer = new Timer(delay, taskPerformer);
        timer.setRepeats(false);
        LOGGER.info("DataTurboSender: start \"Smart Mode\"");
        timer.setInitialDelay(delay);
        watchDog.start();
        timer.start();
        return true;
    }


    public boolean SendTimerStart() {
        startTime = System.currentTimeMillis();
        SendLock();

        try {
            SerialObject.ins.removeEventListener();
        } catch (Exception e) {
        }
        try {
            if (Config.ins.isDumb_mode()) {
                SerialObject.ins.addEventListener(new PortReader(this));
            } else {
                SerialObject.ins.addEventListener(new PortExtraReader());
            }
        } catch (SerialPortException e) {
            LOGGER.info("DataSender: Add EventListener Error. Canceled.");
            return false;
        }
        int delay;
        j0();
        if (Config.ins.isDumb_mode()) { // DumbMode
            delay = Config.ins.getLine_delay_for_dumb();

            taskPerformer = evt -> {
                if (j < sendBuf.size()) {
                    send(addCRLF(sendBuf.get(j).trim()), false);
                    j++;
                    int div = sendBuf.size() - 1;
                    if (div == 0) {
                        div = 1; // for non-zero divide
                    }
                    Progress.ins.setValue((j * 100) / div);
                    if (j > sendBuf.size() - 1) {
                        timer.stop();
                        StopSend();
                    }
                }
            };

            timer = new Timer(delay, taskPerformer);
            timer.setRepeats(true);
            timer.setInitialDelay(delay);
            timer.start();
            LOGGER.info("DataSender: start \"Dumb Mode\"");
        } else { // SmartMode
            delay = Config.ins.getDelay_after_answer();
            if (delay == 0) {
                delay = 10;
            }
            taskPerformer = evt -> {
                if (j < sendBuf.size()) {
                    LOGGER.info(Integer.toString(j));
                    send(addCRLF(sendBuf.get(j).trim()), false);
                    sendPending = false;
                }
            };
            timer = new Timer(delay, taskPerformer);
            timer.setRepeats(false);
            timer.setInitialDelay(delay);
            timer.start();
            LOGGER.info("DataSender: start \"Smart Mode\"");
            watchDog.start();
        }
        return true;
    }

    public void send(String s, boolean simple) {
        if (!Open.isSelected()) {
            LOGGER.info("DataSender: Serial port not open, operation FAILED.");
            return;
        }
        if (busyIcon) {
            Busy.setIcon(Icon.LED_BLUE);
        } else {
            Busy.setIcon(Icon.LED_RED);

        }
        busyIcon = !busyIcon;
        try {
            LOGGER.info("sending:" + s.replace("\r\n", "<CR><LF>"));
            SerialObject.ins.writeString(s);
        } catch (SerialPortException ex) {
            LOGGER.info("send FAIL:" + s.replace("\r\n", "<CR><LF>"));
        }
        if (!Config.ins.isDumb_mode() && !simple) {
            try {
                watchDog.feed();
            } catch (Exception e) {
            }
        }
        if (simple) {
            Busy.setIcon(Icon.LED_GREY);

        }
    }


    public void Busy() {
        Busy.setText("BUSY");
        Busy.setBackground(new Color(153, 0, 0)); // LED_RED


        Progress.ins.setValue(0);
        Progress.ins.setVisible(true);

    }

    public void SendLock() {
        Busy();
        FileSaveESP.setText("Cancel");
        FileSaveESP.setIcon(Icon.ABORT);
        FileSaveESP.setSelected(true);
    }

    public void Idle() {

        Progress.ins.setVisible(false);

        Busy.setText("IDLE");
        Busy.setBackground(new Color(0, 153, 0)); // LED_GREEN
        Busy.setIcon(Icon.LED_GREY);


        //FileSendESP.setSelected(true);
        UpdateButtons();
        UpdateEditorButtons(getCurrentEdit());
    }

    public void SendUnLock() {
        Idle();
        FileSaveESP.setText("Save to ESP");
        FileSaveESP.setIcon(Icon.MOVE);
        FileSaveESP.setSelected(false);
        FileSendESP.setSelected(false);
    }

    public void UpdateLED() {
        if (!Open.isSelected()) {
            PortDTR.setIcon(Icon.LED_GREY);
            PortRTS.setIcon(Icon.LED_GREY);
            PortCTS.setIcon(Icon.LED_GREY);
            Open.setText("Open");
            Open.setIcon(Icon.CONNECT1);
        } else {
            Open.setText("Close");
            Open.setIcon(Icon.DISCONNECT1);

            UpdateLedCTS();
            if (PortDTR.isSelected()) {
                PortDTR.setIcon(Icon.LED_GREEN);
            } else {
                PortDTR.setIcon(Icon.LED_GREY);
            }
            if (PortRTS.isSelected()) {
                PortRTS.setIcon(Icon.LED_GREEN);
            } else {
                PortRTS.setIcon(Icon.LED_GREY);
            }
        }

        if (SerialObject.ins.getSerialPortStatus().isClosed()) {
            PortOpenLabel.setIcon(Icon.LED_GREY);
        } else if (SerialObject.ins.getSerialPortStatus().isConnected()) {
            PortOpenLabel.setIcon(Icon.LED_RED);
        } else if (SerialObject.ins.getSerialPortStatus().isOpened()) {
            PortOpenLabel.setIcon(Icon.LED_GREEN);
        }
    }


    File SaveDownloadedFile(String filename, byte[] data) {
        LOGGER.info("Saving downloaded file...");
        File f = new File(filename);
        File[] files = Context.ShowFileDialog(null, "Save downloaded from ESP file \"" + filename + "\" As...", f, false, false);
        if (null == files) {
            LOGGER.info("Saving abort by user.");
            return null;
        }
        f = files[0];
        filename = f.getName();

        if (f.exists()) {
            LOGGER.info("File " + filename + " already exist, waiting user choice");
            int shouldWrite = Context.Dialog("File " + filename + " already exist. Overwrite?", JOptionPane.YES_NO_OPTION);
            if (shouldWrite != JOptionPane.YES_OPTION) {
                LOGGER.info("Saving canceled by user, because file " + filename + " already exist");
                return null;
            } else {
                LOGGER.info("File " + filename + " will be overwriten by user choice");
            }
        } else { // we saving file, when open
            LOGGER.info("We saving new file " + filename);
        }
        try {
            Context.Save(f, data);
            LOGGER.info("Save file " + filename + ": Success, size:" + Long.toString(f.length()));
        } catch (Exception e) {
            LOGGER.info("Save file " + filename + ": FAIL.");
            LOGGER.info(e.toString());
            JOptionPane.showMessageDialog(null, "Error, file " + filename + " not saved!");
        }
        return f;
    }

    private void UploadFiles() {
        if (!Open.isSelected()) {
            thandler.comment("Uploader: Serial port not open, operation FAILED.", true);
            return;
        }
        if (!SerialObject.ins.getSerialPortStatus().isOpened()) {
            thandler.comment("Uploader: Communication with MCU not yet established.", true);
            return;
        }

        File[] files = Context.ShowFileDialog(LeftBasePane, "Select file to upload to ESP", null, true, true);
        upload_file_list = new ArrayList<>();
        LOGGER.info("Uploader: chooser selected file:" + files.length);
        if (upload_file_list.addAll(Arrays.asList(files))) {
//        if ( upload_file_list.add(chooser.getSelectedFile()) ) {
            upload_file_index = 0;
        } else {
            upload_file_index = -1;
            LOGGER.info("Uploader: no file selected");
            return;
        }
//        chooser.setMultiSelectionEnabled(false);
        if (null == files) {
            LOGGER.info("Uploader: canceled by user");
            return;
        }
        UploadFilesStart();
    }

    private void UploadFilesStart() {
        UploadFileName = upload_file_list.get(upload_file_index).getName();
        sendBuf = new ArrayList<>();


        sendPacketsCRC = new ArrayList<>();

        sendPackets = Context.LoadBinaryFile(upload_file_list.get(upload_file_index));
        if (sendPackets.isEmpty()) {
            LOGGER.info("Uploader: loaded fail!");
            return;
        }

        LOGGER.info("sendPackets=" + Integer.toString(sendPackets.size()));
        String cmd = "_up=function(n,l,ll)\n"
                + "     local cs = 0\n"
                + "     local i = 0\n"
                + "     print(\">\"..\" \")\n"
                + "     uart.on(\"data\", l, function(b) \n"
                + "          i = i + 1\n"
                + "          file.open(\"" + UploadFileName + "\",'a+')\n"
                + "          file.write(b)\n"
                + "          file.close()\n"
                + "          cs=0\n"
                + "          for j=1, l do\n"
                + "               cs = cs + (b:byte(j)*20)%19\n"
                + "          end\n"
                + "          uart.write(0,\"~~~CRC-\"..\"START~~~\"..cs..\"~~~CRC-\"..\"END~~~\")\n"
                + "          if i == n then\n"
                + "               uart.on(\"data\")\n"
                + "          end\n"
                + "          if i == n-1 and ll>0 then\n"
                + "               _up(1,ll,ll)\n"
                + "          end\n"
                + "          end,0)\n"
                + "end\n"
                + "file.remove(\"" + UploadFileName + "\")\n";
        sendBuf = cmdPrep(cmd);
        sendBuf.add("_up(" + sendPackets.size() + "," + sendPackets.get(0).length + "," + sendPackets.get(sendPackets.size() - 1).length + ")");
        LOGGER.info("Uploader: Starting...");
        startTime = System.currentTimeMillis();
        SendLock();
        rx_data = "";
        try {
            SerialObject.ins.removeEventListener();
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }
        try {
            SerialObject.ins.addEventListener(new PortFilesUploader());
            LOGGER.info("Uploader: Add EventListener: Success.");
        } catch (SerialPortException e) {
            LOGGER.info("Uploader: Add EventListener Error. Canceled.");
            SendUnLock();
            return;
        }
        int delay = 10;
        j0();
        taskPerformer = evt -> {
            //log("send j="+Integer.toString(j));
            if (j < sendBuf.size()) {
                send(addCR(sendBuf.get(j)), false);
                sendPending = false;
            } else if ((j - sendBuf.size()) < sendPackets.size()) {
                sendBytes(sendPackets.get(j - sendBuf.size()));
                sendPending = false;
            } else {
                LOGGER.info("Sorry, bug found: j overflow");
            }
        };
        timer = new Timer(delay, taskPerformer);
        timer.setRepeats(false);
        LOGGER.info("Uploader: Start");
        thandler.comment("Uploading to ESP file " + UploadFileName + "...", true);
        timer.setInitialDelay(delay);
        watchDog.start();
        timer.start();
    }


    public void sendBytes(byte[] b) {
        if (!Open.isSelected()) {
            LOGGER.info("BytesSender: Serial port not open, operation FAILED.");
            return;
        }
        if (busyIcon) {
            Busy.setIcon(Icon.LED_BLUE);

        } else {
            Busy.setIcon(Icon.LED_RED);

        }
        busyIcon = !busyIcon;
        try {
            //log("BytesSender sending:" + b.toString().replace("\r\n", "<CR><LF>"));
            SerialObject.ins.writeBytes(b);
        } catch (SerialPortException ex) {
            LOGGER.info("BytesSender send FAIL:" + b.toString().replace("\r\n", "<CR><LF>"));

        }
    }

    private void ViewFile(String fn) {
        String c = Context.GetLua("/yh/espide/file_view.lua", fn);
        SendToESP(cmdPrep(c));
    }


    @Override
    public void listen(String command) {
        btnSend(command);
    }

    @Override
    public void ItemClicked(MouseEvent e, FileList.Item item) {
        if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
            String fn = item.getText();
            if (fn.endsWith(".lua") || fn.endsWith(".lc")) {
                String cmd = "dofile(\"" + fn + "\")";
                btnSend(cmd);
            } else if (fn.endsWith(".bin") || fn.endsWith(".dat")) {
                HexDump(fn);
            } else {
                ViewFile(fn);
            }
        }
    }

    @Override
    public void SelectChanged(TextEditArea area) {
        if (null == area) {
            FilePathLabel.setText("");
        } else {
            FilePathLabel.setText(area.getFile().getPath());
        }
        UpdateEditorButtons(area);
    }

    public void updateTheme() {
        FilesTabbedPane.updateTheme();
        thandler.updateTheme();
    }

    private class PortNodeFilesReader implements SerialPortEventListener {

        private String sendResp = "";

        public void serialEvent(SerialPortEvent event) {
            String data;
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    data = SerialObject.ins.readString(event.getEventValue());
                    sendResp = sendResp + data;
                    rx_data = rx_data + data;
                } catch (Exception e) {
                    data = "";
                    LOGGER.info(e.toString());
                }
                if (sendResp.contains("> ")) {
                    try {
                        watchDog.feed();
                    } catch (Exception e) {
                        LOGGER.info(e.toString());
                    }
                    sendResp = "";
                    if (j < sendBuf.size() - 1) {
                        if (timer.isRunning() || sendPending) {
                            //
                        } else {
                            j++;
                            sendPending = true;
                            timer.start();
                        }
                    } else { // send done
                        try {
                            timer.stop();
                        } catch (Exception e) {
                        }
                    }
                }
                try {
                    if (rx_data.contains("~~~File list END~~~")) {
                        try {
                            watchDog.stop();
                        } catch (Exception e) {
                            LOGGER.info(e.toString());
                        }
                        Progress.ins.setValue(100);
                        LOGGER.info("FileManager: File list found! Do parsing...");
                        try {
                            // parsing answer
                            int start = rx_data.indexOf("~~~File list START~~~");
                            rx_data = rx_data.substring(start + 23, rx_data.indexOf("~~~File list END~~~"));
                            //log(rx_data.replaceAll("\r?\n", "<CR+LF>\r\n"));
                            String[] s = rx_data.split("\r?\n");
                            Arrays.sort(s);
//                            TerminalAdd("\r\n" + rx_data + "\r\n> ");
                            int usedSpace = 0;
                            thandler.comment("----------------------------", false);
                            for (String subs : s) {
                                thandler.comment(subs, false);
                                String[] parts = subs.split(":");
                                if (parts[0].trim().length() > 0) {
                                    int size = Integer.parseInt(parts[1].trim().split(" ")[0]);
                                    AddNodeFileButton(parts[0].trim(), size);
                                    usedSpace += size;
                                    LOGGER.info("FileManager found file " + parts[0].trim());
                                }
                            }
                            if (fileList.listsize() == 0) {
                                thandler.comment("No files found.", true);
                            } else {
                                thandler.comment("Total file(s)   : " + Integer.toString(s.length), false);
                                thandler.comment("Total size      : " + Integer.toString(usedSpace) + " bytes", true);
                            }
                            NodeFileMgrPane.invalidate();
                            NodeFileMgrPane.doLayout();
                            NodeFileMgrPane.repaint();
                            NodeFileMgrPane.requestFocusInWindow();
                            LOGGER.info("FileManager: File list parsing done, found " + fileList.listsize() + " file(s).");
                        } catch (Exception e) {
                            LOGGER.info(e.toString());
                        }
                        try {
                            SerialObject.ins.removeEventListener();
                        } catch (Exception e) {
                        }
                        SerialObject.ins.addEventListener(new PortReader(EspIDE.this));
                        SendUnLock();
                    }
                } catch (SerialPortException ex) {
                    LOGGER.info(ex.toString());
                }
            } else if (event.isCTS()) {
                UpdateLedCTS();
            } else if (event.isERR()) {
                LOGGER.info("FileManager: Unknown serial port error received.");
            }
        }
    }


    private class PortExtraReader implements SerialPortEventListener {

        private String sendResp = "";

        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                String data = "";
                try {
                    data = SerialObject.ins.readString(event.getEventValue());
                } catch (SerialPortException ex) {
                    LOGGER.info(ex.toString());
                }
                data = data.replace(">> ", "");
                data = data.replace(">>", "");
                data = data.replace("\r\n> ", "");
                data = data.replace("\r\n\r\n", "\r\n");

                sendResp = sendResp + data;
                LOGGER.info("recv:" + data.replace("\r\n", "<CR><LF>"));
                thandler.add(data);
                if (sendResp.contains(sendBuf.get(j).trim())) {
                    // first, reset watchdog timer
                    try {
                        watchDog.stop();
                    } catch (Exception e) {
                    }
                    /*
                    if (rcvBuf.contains("stdin:")) {
                        String msg[] = {"Interpreter error detected!", rcvBuf, "Click OK to continue."};
                        JOptionPane.showMessageDialog(null, msg);
                    }
                     */
                    sendResp = "";
                    if (j < sendBuf.size() - 1) {
                        if (timer.isRunning() || sendPending) {
                            // waiting
                        } else {
                            j++;
                            sendPending = true;
                            int div = sendBuf.size() - 1;
                            if (div == 0) {
                                div = 1;
                            }
                            Progress.ins.setValue((j * 100) / div);
                            timer.start();
                        }
                    } else {  // send done
                        StopSend();
                    }
                }
                if (sendResp.contains("powered by Lua 5.")) {
                    StopSend();
                    String msg[] = {"ESP module reboot detected!", "Event: internal NodeMCU exception or power fail.", "Please, try again."};
                    JOptionPane.showMessageDialog(null, msg);
                }
            } else if (event.isCTS()) {
                UpdateLedCTS();
            } else if (event.isERR()) {
                LOGGER.info("FileManager: Unknown serial port error received.");
            }
        }
    }

    private class PortTurboReader implements SerialPortEventListener {

        private String sendResp = "";

        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                String data = "";
                try {
                    data = SerialObject.ins.readString(event.getEventValue());
                } catch (SerialPortException ex) {
                    LOGGER.info(ex.toString());
                }
                sendResp = sendResp + data;
                String l = data.replace("\r", "<CR>");
                l = l.replace("\n", "<LF>");
                l = l.replace("`", "<OK>");
                LOGGER.info("recv:" + l);
                thandler.add(data);
                if (sendResp.contains("> ")) {
                    try {
                        watchDog.stop();
                    } catch (Exception e) {
                    }
                    sendResp = "";
                    if (j < sendBuf.size() - 1) {
                        if (timer.isRunning() || sendPending) {
                            // waiting
                        } else {
                            j++;
                            sendPending = true;
                            int div = sendBuf.size() - 1;
                            if (div == 0) {
                                div = 1;
                            }
                            Progress.ins.setValue((j * 100) / div);
                            timer.start();
                        }
                    } else { // send done
                        StopSend();
                    }
                }
            } else if (event.isCTS()) {
                UpdateLedCTS();
            } else if (event.isERR()) {
                LOGGER.info("FileManager: Unknown serial port error received.");
            }
        }
    }

    private class PortFilesUploader implements SerialPortEventListener {
        private String sendResp = "";

        public void serialEvent(SerialPortEvent event) {
            String data, crc_parsed;
            boolean gotProperAnswer = false;
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    data = SerialObject.ins.readString(event.getEventValue());
                    sendResp = sendResp + data;
                    rx_data = rx_data + data;
                } catch (Exception e) {
                    data = "";
                    LOGGER.info(e.toString());
                }
                if (sendResp.contains("> ") && j < sendBuf.size()) {
                    //log("got intepreter answer, j="+Integer.toString(j));
                    sendResp = "";
                    gotProperAnswer = true;
                }
                if (rx_data.contains("~~~CRC-END~~~")) {
                    gotProperAnswer = true;
                    //log("Uploader: receiving packet checksum " + Integer.toString( j-sendBuf.size()  +1) + "/"
                    //                                           + Integer.toString( sendPackets.size() ) );
                    // parsing answer
                    int start = rx_data.indexOf("~~~CRC-START~~~");
                    //log("Before CRC parsing:"+rx_data);
                    crc_parsed = rx_data.substring(start + 15, rx_data.indexOf("~~~CRC-END~~~"));
                    rx_data = rx_data.substring(rx_data.indexOf("~~~CRC-END~~~") + 13);
                    //log("After  CRC parsing:"+crc_parsed);
                    int crc_received = Integer.parseInt(crc_parsed);
                    int crc_expected = CRC(sendPackets.get(j - sendBuf.size()));
                    if (crc_expected == crc_received) {
                        LOGGER.info("Uploader: receiving checksum " + Integer.toString(j - sendBuf.size() + 1) + "/"
                                + Integer.toString(sendPackets.size())
                                + " check: Success");
                        sendPacketsCRC.add(true);
                    } else {
                        LOGGER.info("Uploader: receiving checksum " + Integer.toString(j - sendBuf.size() + 1) + "/"
                                + Integer.toString(sendPackets.size())
                                + " check: Fail. Expected: " + Integer.toString(crc_expected)
                                + ", but received: " + Integer.toString(crc_received));
                        sendPacketsCRC.add(false);
                    }
                }
                if (gotProperAnswer) {
                    try {
                        watchDog.feed();
                    } catch (Exception e) {
                        LOGGER.info(e.toString());
                    }
                    Progress.ins.setValue(j * 100 / (sendBuf.size() + sendPackets.size() - 1));
                    if (j < (sendBuf.size() + sendPackets.size())) {
                        if (timer.isRunning() || sendPending) {
                            //
                        } else {
                            j++;
                            sendPending = true;
                            timer.start();
                        }
                    } else {
                        try {
                            timer.stop();
                        } catch (Exception e) {
                        }
                    }
                }
                if (j >= (sendBuf.size() + sendPackets.size())) {
                    send(addCR("_up=nil"), false);
                    try {
                        timer.stop();
                    } catch (Exception e) {
                    }
                    try {
                        watchDog.stop();
                    } catch (Exception e) {
                        LOGGER.info(e.toString());
                    }
                    //log("Uploader: send all data, finishing...");
                    boolean success = true;
                    for (int i = 0; i < sendPacketsCRC.size(); i++) {
                        if (!sendPacketsCRC.get(i)) {
                            success = false;
                        }
                    }
                    if (success && (sendPacketsCRC.size() == sendPackets.size())) {
                        thandler.comment("Success", true);
                        LOGGER.info("Uploader: Success");
                    } else {
                        thandler.comment("Fail", true);
                        LOGGER.info("Uploader: Fail");
                    }
                    try {
                        SerialObject.ins.removeEventListener();
                    } catch (Exception e) {
                        LOGGER.info(e.toString());
                    }
                    try {
                        SerialObject.ins.addEventListener(new PortReader(EspIDE.this));
                    } catch (Exception e) {
                        LOGGER.info(e.toString());
                    }
                    StopSend();
                    if (upload_file_index != -1 && (++upload_file_index) < upload_file_list.size()) {
                        UploadFilesStart();
                    }
                }
            } else if (event.isCTS()) {
                UpdateLedCTS();
            } else if (event.isERR()) {
                LOGGER.info("FileManager: Unknown serial port error received.");
            }
        }
    }

} // EspIDE
