package yh.espide;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.PopupMenuEvent;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;


public class EspIDE extends javax.swing.JFrame implements TerminalHandler.CommandListener, FileList.ItemClickedListener, TextEditTabbed.SelectChangedListener {

    public static final Logger LOGGER = Logger.getLogger(EspIDE.class.getName());

    private static final int portMask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS;
    public static SerialPort serialPort;
    public static ArrayList<String> sendBuf;
    public static boolean pasteMode = true; // for MicroPython only
    public static int j = 0;
    public static int pyLevel = 0;
    public static boolean sendPending = false;
    public static String s[];
    public static String rcvBuf = "";
    public static String rx_data = "";
    public static String tx_data = "";
    public static byte[] rx_byte;
    public static byte[] tx_byte;
    public static String DownloadCommand;
    public static boolean busyIcon = false;
    private static pyFiler pyFiler = new pyFiler();
    public final int SendPacketSize = 250;
    public ActionListener taskPerformer;
    public ActionListener watchDog;
    public Timer timer;
    public Timer timeout;
    // downloader
    public int packets = 0;
    public ArrayList<String> rcvPackets;
    public ArrayList<byte[]> sendPackets;
    public ArrayList<Boolean> sendPacketsCRC;
    public ArrayList<String> PacketsData;
    public ArrayList<Integer> PacketsSize;
    public ArrayList<Integer> PacketsCRC;
    public ArrayList<Integer> PacketsNum;

    public FileList fileList = new FileList();

    SerialPortStatus serialPortStatus = SerialPortStatus.CLOSED;

    JButton firmware_type_label;
    String DownloadedFileName = "";

    private TerminalHandler thandler = new TerminalHandler(this);
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBoxMenuItem AlwaysOnTop;
    private javax.swing.JCheckBox AutoScroll;
    private javax.swing.JLabel Busy;
    private javax.swing.JButton ButtonCopy;
    private javax.swing.JButton ButtonCut;
    private javax.swing.JButton ButtonFileClose;
    private javax.swing.JButton ButtonFileNew;
    private javax.swing.JButton ButtonFileOpen;
    private javax.swing.JButton ButtonFileReload;
    private javax.swing.JButton ButtonFileSave;
    private javax.swing.JButton ButtonPaste;
    private javax.swing.JButton ButtonRedo;
    private javax.swing.JButton ButtonSendLine;
    private javax.swing.JButton ButtonSendSelected;
    private javax.swing.JButton ButtonUndo;
    private javax.swing.JCheckBox CR;
    private javax.swing.JPopupMenu ContextMenuTerminal;
    private javax.swing.JCheckBox EOL;
    private javax.swing.JButton FileFormat;

    private javax.swing.JButton FileListReload;
    private javax.swing.JScrollPane FileManagerScrollPane;
    private javax.swing.JLayeredPane FileManagersLayer;
    private javax.swing.JLabel FilePathLabel;
    private javax.swing.JTextField FileRename;
    private javax.swing.JLabel FileRenameLabel;
    private javax.swing.JLayeredPane FileRenamePanel;
    private javax.swing.JToggleButton FileSaveESP;
    private javax.swing.JToggleButton FileSendESP;
    private javax.swing.JButton FileSystemInfo;
    private TextEditTabbed FilesTabbedPane;
    private javax.swing.JToolBar FilesToolBar;
    private javax.swing.JButton FilesUpload;
    private javax.swing.JSplitPane HorizontSplit;
    private javax.swing.JLayeredPane LEDPanel;
    private javax.swing.JCheckBox LF;
    private javax.swing.JLayeredPane LeftBasePane;
    private javax.swing.JLayeredPane LeftMainButtons;
    private javax.swing.JMenuItem MenuItemEditCopy;
    private javax.swing.JMenuItem MenuItemEditCut;
    private javax.swing.JMenuItem MenuItemEditPaste;
    private javax.swing.JMenuItem MenuItemEditRedo;
    private javax.swing.JMenuItem MenuItemEditSendLine;
    private javax.swing.JMenuItem MenuItemEditSendSelected;
    private javax.swing.JMenuItem MenuItemEditUndo;
    private javax.swing.JMenuItem MenuItemFileClose;
    private javax.swing.JMenuItem MenuItemFileNew;
    private javax.swing.JMenuItem MenuItemFileOpen;
    private javax.swing.JMenuItem MenuItemFileReload;
    private javax.swing.JMenuItem MenuItemFileSave;
    private javax.swing.JMenuItem MenuItemFileSaveESP;
    private javax.swing.JMenuItem MenuItemFileSendESP;
    private javax.swing.JMenuItem MenuItemTerminalClear;
    private javax.swing.JMenuItem MenuItemTerminalCopy;
    private javax.swing.JMenuItem MenuItemViewClearTerminal;
    private javax.swing.JCheckBoxMenuItem MenuItemViewEditorOnly;
    private javax.swing.JCheckBoxMenuItem MenuItemViewFileManager;
    private javax.swing.JCheckBoxMenuItem MenuItemViewTerminalOnly;
    private javax.swing.JCheckBoxMenuItem MenuItemViewToolbar;
    private javax.swing.JLayeredPane NodeFileMgrPane;
    private javax.swing.JPanel NodeMCU;
    private javax.swing.JToggleButton Open;
    private javax.swing.JComboBox Port;
    private javax.swing.JLabel PortCTS;
    private javax.swing.JToggleButton PortDTR;
    private javax.swing.JLabel PortOpenLabel;
    private javax.swing.JToggleButton PortRTS;
    private javax.swing.JLayeredPane PyFileMgrPane;
    private javax.swing.JButton PyListDir;
    private javax.swing.JButton ReScan;
    private javax.swing.JLayeredPane RightBasePane;
    private javax.swing.JSplitPane RightTerminalSplitPane;
    private javax.swing.JComboBox Speed;
    private javax.swing.JLayeredPane SriptsTab;


    private org.jdesktop.beansbinding.BindingGroup bindingGroup;

    private ArrayList<File> upload_file_list;
    private int upload_file_index = -1;

    private ArrayList<javax.swing.JButton> PyFileAsButton;
    // private int iTab = 0; // tab index

    private String UploadFileName = "";
    private long startTime = System.currentTimeMillis();


    public EspIDE() {
        setTitle(Version.title());
        initComponents();
        FinalInit();
    }

    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        ContextMenuTerminal = new javax.swing.JPopupMenu();
        MenuItemTerminalClear = new javax.swing.JMenuItem();
        MenuItemTerminalCopy = new javax.swing.JMenuItem();

        HorizontSplit = new javax.swing.JSplitPane();
        LeftBasePane = new javax.swing.JLayeredPane();

        NodeMCU = new javax.swing.JPanel();

        SriptsTab = new javax.swing.JLayeredPane();
        FilesToolBar = new javax.swing.JToolBar();
        FilesTabbedPane = new TextEditTabbed(this);


        Busy = new javax.swing.JLabel();
        FilePathLabel = new javax.swing.JLabel();
        LeftMainButtons = new javax.swing.JLayeredPane();
        FileSaveESP = new javax.swing.JToggleButton();
        FileSendESP = new javax.swing.JToggleButton();

        FilesUpload = new javax.swing.JButton();


        RightBasePane = new javax.swing.JLayeredPane();
        LEDPanel = new javax.swing.JLayeredPane();
        PortOpenLabel = new javax.swing.JLabel();
        PortCTS = new javax.swing.JLabel();
        PortDTR = new javax.swing.JToggleButton();
        PortRTS = new javax.swing.JToggleButton();
        Open = new javax.swing.JToggleButton();
        Speed = new javax.swing.JComboBox();
        ReScan = new javax.swing.JButton();
        AutoScroll = new javax.swing.JCheckBox();
        Port = new javax.swing.JComboBox();
        EOL = new javax.swing.JCheckBox();
        CR = new javax.swing.JCheckBox();
        LF = new javax.swing.JCheckBox();
        RightTerminalSplitPane = new javax.swing.JSplitPane();

        FileManagerScrollPane = new javax.swing.JScrollPane();
        FileManagersLayer = new javax.swing.JLayeredPane();
        firmware_type_label = new JButton(FirmwareType.current.toString());
        NodeFileMgrPane = new javax.swing.JLayeredPane();
        FileFormat = new javax.swing.JButton();
        FileSystemInfo = new javax.swing.JButton();
        FileListReload = new javax.swing.JButton();
        FileRenamePanel = new javax.swing.JLayeredPane();
        FileRenameLabel = new javax.swing.JLabel();
        FileRename = new javax.swing.JTextField();
        PyFileMgrPane = new javax.swing.JLayeredPane();
        PyListDir = new javax.swing.JButton();

        MenuItemFileNew = new javax.swing.JMenuItem();
        MenuItemFileOpen = new javax.swing.JMenuItem();
        MenuItemFileReload = new javax.swing.JMenuItem();
        MenuItemFileSave = new javax.swing.JMenuItem();
        MenuItemFileClose = new javax.swing.JMenuItem();

        MenuItemFileSaveESP = new javax.swing.JMenuItem();
        MenuItemFileSendESP = new javax.swing.JMenuItem();


        MenuItemEditUndo = new javax.swing.JMenuItem();
        MenuItemEditRedo = new javax.swing.JMenuItem();

        MenuItemEditCut = new javax.swing.JMenuItem();
        MenuItemEditCopy = new javax.swing.JMenuItem();
        MenuItemEditPaste = new javax.swing.JMenuItem();

        MenuItemEditSendSelected = new javax.swing.JMenuItem();
        MenuItemEditSendLine = new javax.swing.JMenuItem();


        AlwaysOnTop = new javax.swing.JCheckBoxMenuItem();
        MenuItemViewClearTerminal = new javax.swing.JMenuItem();

        MenuItemViewTerminalOnly = new javax.swing.JCheckBoxMenuItem();
        MenuItemViewEditorOnly = new javax.swing.JCheckBoxMenuItem();

        MenuItemViewToolbar = new javax.swing.JCheckBoxMenuItem();
        MenuItemViewFileManager = new javax.swing.JCheckBoxMenuItem();


        ContextMenuTerminal.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }

            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }

            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                ContextMenuTerminalPopupMenuWillBecomeVisible(evt);
            }
        });

        MenuItemTerminalClear.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemTerminalClear.setIcon(Icon.TERMINAL_CLEAR);
        MenuItemTerminalClear.setText(Context.BUNDLE.getString("Clear"));
        MenuItemTerminalClear.addActionListener(evt -> thandler.clean());
        ContextMenuTerminal.add(MenuItemTerminalClear);

        MenuItemTerminalCopy.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemTerminalCopy.setIcon(Icon.COPY);
        MenuItemTerminalCopy.setText(Context.BUNDLE.getString("Copy"));
        MenuItemTerminalCopy.setToolTipText("Copy selected text to system clipboard");
        MenuItemTerminalCopy.setEnabled(false);
        MenuItemTerminalCopy.addActionListener(evt -> thandler.copy());
        ContextMenuTerminal.add(MenuItemTerminalCopy);


        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setAutoRequestFocus(false);
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFocusCycleRoot(false);
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(100, 100));
        setPreferredSize(new java.awt.Dimension(1024, 768));
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        addPropertyChangeListener(this::formPropertyChange);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
            }

            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }

            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        HorizontSplit.setDividerLocation(550);
        HorizontSplit.setMinimumSize(new java.awt.Dimension(100, 100));
        HorizontSplit.setPreferredSize(new java.awt.Dimension(768, 567));
        HorizontSplit.addPropertyChangeListener(evt -> reLocationRightSplit());

        LeftBasePane.setOpaque(true);


        NodeMCU.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        NodeMCU.setMinimumSize(new java.awt.Dimension(100, 100));
        NodeMCU.setPreferredSize(new java.awt.Dimension(461, 537));
        NodeMCU.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                NodeMCUComponentShown(evt);
            }
        });


        SriptsTab.setMinimumSize(new java.awt.Dimension(460, 350));
        SriptsTab.setOpaque(true);

        FilesToolBar.setFloatable(false);
        FilesToolBar.setRollover(true);
        FilesToolBar.setAlignmentY(0.5F);
        FilesToolBar.setMaximumSize(new java.awt.Dimension(1000, 40));
        FilesToolBar.setMinimumSize(new java.awt.Dimension(321, 40));
        FilesToolBar.setPreferredSize(new java.awt.Dimension(321, 40));


        ButtonFileNew = new EditButton(Context.BUNDLE.getString("New"), Icon.DOCUMENT, "New file");
        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, MenuItemFileNew, org.jdesktop.beansbinding.ELProperty.create("${enabled}"), ButtonFileNew, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);
        ButtonFileNew.addActionListener(evt -> MenuItemFileNew.doClick());
        FilesToolBar.add(ButtonFileNew);

        ButtonFileOpen = new EditButton(Context.BUNDLE.getString("Open"), Icon.FOLDER_OPEN, "AddTab file from disk");
        ButtonFileOpen.addActionListener(evt -> MenuItemFileOpen.doClick());
        FilesToolBar.add(ButtonFileOpen);

        ButtonFileReload = new EditButton(Context.BUNDLE.getString("Reload"), Icon.REFRESH, "Reload file from disk (for use with external editor)");
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, MenuItemFileReload, org.jdesktop.beansbinding.ELProperty.create("${enabled}"), ButtonFileReload, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);
        ButtonFileReload.addActionListener(evt -> MenuItemFileReload.doClick());
        FilesToolBar.add(ButtonFileReload);

        ButtonFileSave = new EditButton(Context.BUNDLE.getString("Save"), Icon.SAVE, "Save file to disk");
        ButtonFileSave.addActionListener(evt -> MenuItemFileSave.doClick());
        FilesToolBar.add(ButtonFileSave);

        ButtonFileClose = new EditButton(Context.BUNDLE.getString("Close"), Icon.FOLDER_CLOSED, "Close file");
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, MenuItemFileClose, org.jdesktop.beansbinding.ELProperty.create("${enabled}"), ButtonFileClose, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
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


        Busy.setBackground(new java.awt.Color(0, 153, 0));
        Busy.setForeground(new java.awt.Color(255, 255, 255));
        Busy.setHorizontalAlignment(SwingConstants.CENTER);
        Busy.setIcon(Icon.LED_GREY);
        Busy.setText("IDLE");
        Busy.setOpaque(true);


        LeftMainButtons.setOpaque(true);
        LeftMainButtons.setLayout(new java.awt.FlowLayout());

        FileSaveESP.setIcon(Icon.MOVE);
        FileSaveESP.setText("<html><u>S</u>ave to ESP");
        FileSaveESP.setToolTipText("Send file to ESP and save into flash memory");
        FileSaveESP.setHorizontalAlignment(SwingConstants.LEFT);
        FileSaveESP.setIconTextGap(8);
        FileSaveESP.setMargin(new java.awt.Insets(2, 2, 2, 2));
        FileSaveESP.setMaximumSize(new java.awt.Dimension(127, 30));
        FileSaveESP.setMinimumSize(new java.awt.Dimension(127, 30));
        FileSaveESP.setPreferredSize(new java.awt.Dimension(127, 30));
        FileSaveESP.addActionListener(evt -> FileSaveESPActionPerformed(evt));
        LeftMainButtons.add(FileSaveESP);

        FileSendESP.setIcon(Icon.PLAY);
        FileSendESP.setText("<html>S<u>e</u>nd to ESP");
        FileSendESP.setToolTipText("Send file to ESP and run  \"line by line\"");
        FileSendESP.setHorizontalAlignment(SwingConstants.LEFT);
        FileSendESP.setIconTextGap(8);
        FileSendESP.setMargin(new java.awt.Insets(2, 2, 2, 2));
        FileSendESP.setMaximumSize(new java.awt.Dimension(127, 30));
        FileSendESP.setMinimumSize(new java.awt.Dimension(127, 30));
        FileSendESP.setPreferredSize(new java.awt.Dimension(127, 30));
        FileSendESP.addActionListener(evt -> FileSendESPActionPerformed(evt));
        LeftMainButtons.add(FileSendESP);


        FilesUpload.setIcon(Icon.UPLOADLUA);
        FilesUpload.setText("Upload ...");
        FilesUpload.setToolTipText("Upload file from disk to ESP flash memory");
        FilesUpload.setHorizontalAlignment(SwingConstants.LEFT);
        FilesUpload.setIconTextGap(8);
        FilesUpload.setMargin(new java.awt.Insets(2, 2, 2, 2));
        FilesUpload.setMaximumSize(new java.awt.Dimension(127, 30));
        FilesUpload.setMinimumSize(new java.awt.Dimension(127, 30));
        FilesUpload.setPreferredSize(new java.awt.Dimension(127, 30));
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

        LEDPanel.setMaximumSize(new java.awt.Dimension(392, 25));
        LEDPanel.setMinimumSize(new java.awt.Dimension(392, 25));
        LEDPanel.setOpaque(true);

        PortOpenLabel.setHorizontalAlignment(SwingConstants.CENTER);
        PortOpenLabel.setIcon(Icon.LED_GREY);
        PortOpenLabel.setText("Open");
        PortOpenLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        PortOpenLabel.setMaximumSize(new java.awt.Dimension(50, 25));
        PortOpenLabel.setMinimumSize(new java.awt.Dimension(50, 25));
        PortOpenLabel.setPreferredSize(new java.awt.Dimension(50, 25));
        PortOpenLabel.setVerticalTextPosition(SwingConstants.BOTTOM);

        PortCTS.setHorizontalAlignment(SwingConstants.CENTER);
        PortCTS.setIcon(Icon.LED_GREY);
        PortCTS.setText("CTS");
        PortCTS.setHorizontalTextPosition(SwingConstants.CENTER);
        PortCTS.setMaximumSize(new java.awt.Dimension(50, 25));
        PortCTS.setMinimumSize(new java.awt.Dimension(50, 25));
        PortCTS.setPreferredSize(new java.awt.Dimension(50, 25));
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
        Open.setMargin(new java.awt.Insets(1, 1, 1, 1));
        Open.setMaximumSize(new java.awt.Dimension(100, 25));
        Open.setMinimumSize(new java.awt.Dimension(85, 25));
        Open.setPreferredSize(new java.awt.Dimension(80, 25));
        Open.addActionListener(evt -> OpenActionPerformed(evt));

        Speed.setFont(Context.FONT_12);
        Speed.setModel(new DefaultComboBoxModel(new Integer[]{1200, 2400, 4800, 9600, 19200, 38400, 57600, 74880, 115200, 230400, 460800, 921600}));
        Speed.setToolTipText("Select baud rate");
        Speed.setMaximumSize(new java.awt.Dimension(80, 25));
        Speed.setMinimumSize(new java.awt.Dimension(80, 25));
        Speed.setPreferredSize(new java.awt.Dimension(80, 25));

        ReScan.setIcon(Icon.REFRESH3);
        ReScan.setMaximumSize(new java.awt.Dimension(25, 25));
        ReScan.setMinimumSize(new java.awt.Dimension(25, 25));
        ReScan.setPreferredSize(new java.awt.Dimension(25, 25));
        ReScan.addActionListener(evt -> PortFinder());

        AutoScroll.setFont(Context.FONT_8);
        AutoScroll.setSelected(true);
        AutoScroll.setText(Context.BUNDLE.getString("AutoScroll"));
        AutoScroll.setToolTipText("terminalArea AutoScroll Enable/Disable");
        AutoScroll.setMinimumSize(new java.awt.Dimension(70, 25));
        AutoScroll.setPreferredSize(new java.awt.Dimension(60, 25));
        AutoScroll.addActionListener(evt -> Config.ins.setAutoScroll(AutoScroll.isSelected()));

        Port.setFont(Context.FONT_10);
        Port.setMaximumRowCount(20);
        Port.setModel(new DefaultComboBoxModel(new String[]{}));
        Port.setToolTipText("Serial port chooser");
        Port.setMaximumSize(new java.awt.Dimension(150, 25));
        Port.setMinimumSize(new java.awt.Dimension(150, 25));
        Port.setPreferredSize(new java.awt.Dimension(150, 25));

        Port.setEditable(true);

        EOL.setFont(Context.FONT_8);
        EOL.setText("EOL");
        EOL.setToolTipText("EOL visible Enable/Disable");
        EOL.setMinimumSize(new java.awt.Dimension(70, 25));
        EOL.setPreferredSize(new java.awt.Dimension(60, 25));
        EOL.addItemListener(evt -> thandler.setEOLMarkersVisible(EOL.isSelected()));

        CR.setFont(CR.getFont().deriveFont(CR.getFont().getSize() - 4f));
        CR.setSelected(true);
        CR.setText("CR");
        CR.setToolTipText("Add CR at end of line");
        CR.setAlignmentY(0.0F);
        CR.setEnabled(false);
        CR.setIconTextGap(0);
        CR.setMargin(new java.awt.Insets(0, 0, 0, 0));
        CR.setName("");
//        CR.setNextFocusableComponent(Command);

        LF.setFont(LF.getFont().deriveFont(LF.getFont().getSize() - 4f));
        LF.setSelected(true);
        LF.setText("LF");
        LF.setToolTipText("Add LF at end of line");
        LF.setAlignmentY(0.0F);
        LF.setEnabled(false);
        LF.setIconTextGap(0);
        LF.setMargin(new java.awt.Insets(0, 0, 0, 0));


        LEDPanel.setLayer(PortOpenLabel, JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(PortCTS, JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(PortDTR, JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(PortRTS, JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(Open, JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(Speed, JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(ReScan, JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(AutoScroll, JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(Port, JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(EOL, JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(CR, JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(LF, JLayeredPane.DEFAULT_LAYER);


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
                                                                .addComponent(ReScan, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(LEDPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(EOL, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(AutoScroll, GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(LEDPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addGroup(LEDPanelLayout.createSequentialGroup()
                                                                                .addComponent(LF))
                                                                        .addGroup(LEDPanelLayout.createSequentialGroup()
                                                                                .addComponent(CR))))
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
                                                        .addGroup(LEDPanelLayout.createSequentialGroup()
                                                                .addGroup(LEDPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(AutoScroll, GroupLayout.PREFERRED_SIZE, 19, Short.MAX_VALUE)
                                                                        .addComponent(CR)
                                                                )
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(LEDPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(EOL, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(LF))))
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

        Port.getAccessibleContext().setAccessibleName("");


        RightTerminalSplitPane.setAutoscrolls(true);
        RightTerminalSplitPane.addPropertyChangeListener(evt -> RightFilesSplitPanePropertyChange(evt));

        thandler.setPopupMenu(ContextMenuTerminal);

        RightTerminalSplitPane.setLeftComponent(thandler);

        FileManagerScrollPane.setBorder(null);
        FileManagerScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


        firmware_type_label.addActionListener(e -> {
            FirmwareType type = FirmwareType.NodeMCU;
            switch (FirmwareType.current) {
                case NodeMCU:
                    type = FirmwareType.MicroPython;
                    break;
                case MicroPython:
                    type = FirmwareType.AT;
                    break;
                case AT:
                    type = FirmwareType.NodeMCU;
                    break;
            }
            firmware_type_label.setText(type.toString());
            SetFirmwareType(type);
        });


        NodeFileMgrPane.setMaximumSize(new java.awt.Dimension(145, 145));
        NodeFileMgrPane.setPreferredSize(new java.awt.Dimension(145, 145));
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 2, 2);
        flowLayout1.setAlignOnBaseline(true);
        NodeFileMgrPane.setLayout(flowLayout1);

        FileFormat.setIcon(Icon.FILE_MANAGER_DELETE);
        FileFormat.setText(Context.BUNDLE.getString("FS Format"));
        FileFormat.setToolTipText("Format (erase) NodeMCU file system. All files will be removed!");
        FileFormat.setHorizontalAlignment(SwingConstants.LEFT);
        FileFormat.setMargin(new java.awt.Insets(2, 4, 2, 4));
        FileFormat.setMaximumSize(new java.awt.Dimension(130, 25));
        FileFormat.setMinimumSize(new java.awt.Dimension(130, 25));
        FileFormat.setPreferredSize(new java.awt.Dimension(130, 25));
        FileFormat.addActionListener(evt -> MenuItemESPFormatActionPerformed(evt));
        NodeFileMgrPane.add(FileFormat);

        FileSystemInfo.setIcon(Icon.FILE_MANAGER);
        FileSystemInfo.setText(Context.BUNDLE.getString("FS Info"));
        FileSystemInfo.setToolTipText("Execute command file.fsinfo() and show total, used and remainig space on the ESP filesystem");
        FileSystemInfo.setAlignmentX(0.5F);
        FileSystemInfo.setHorizontalAlignment(SwingConstants.LEFT);
        FileSystemInfo.setMargin(new java.awt.Insets(2, 2, 2, 2));
        FileSystemInfo.setMaximumSize(new java.awt.Dimension(130, 25));
        FileSystemInfo.setPreferredSize(new java.awt.Dimension(130, 25));
        FileSystemInfo.addActionListener(evt -> NodeFileSystemInfo());
        NodeFileMgrPane.add(FileSystemInfo);

        FileListReload.setFont(Context.FONT_12);
        FileListReload.setIcon(Icon.REFRESH3);
        FileListReload.setText(Context.BUNDLE.getString("FS Reload"));
        FileListReload.setAlignmentX(0.5F);
        FileListReload.setHorizontalAlignment(SwingConstants.LEFT);
        FileListReload.setMargin(new java.awt.Insets(2, 2, 2, 2));
        FileListReload.setMaximumSize(new java.awt.Dimension(130, 25));
        FileListReload.setPreferredSize(new java.awt.Dimension(130, 25));
        FileListReload.addActionListener(evt -> NodeListFiles());
        NodeFileMgrPane.add(FileListReload);

        FileRenamePanel.setMaximumSize(new java.awt.Dimension(130, 45));
        FileRenamePanel.setMinimumSize(new java.awt.Dimension(130, 45));

        FileRenameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        FileRenameLabel.setText("Old file name");
        FileRenameLabel.setToolTipText("Input new file name and hit Enter to completed or press Reload for cancel");
        FileRenameLabel.setMaximumSize(new java.awt.Dimension(130, 14));
        FileRenameLabel.setMinimumSize(new java.awt.Dimension(130, 14));
        FileRenameLabel.setPreferredSize(new java.awt.Dimension(130, 14));

        FileRename.setText("NewFileName");
        FileRename.setToolTipText("Input new file name and hit Enter to completed or press Reload for cancel");
        FileRename.setMaximumSize(new java.awt.Dimension(130, 25));
        FileRename.setMinimumSize(new java.awt.Dimension(130, 25));
        FileRename.setPreferredSize(new java.awt.Dimension(130, 25));
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


        // fileList.setMaximumSize(new java.awt.Dimension(500, 155));
        fileList.setPreferredSize(new java.awt.Dimension(150, 400));
        fileList.setItemclicked(this);

        PyFileMgrPane.setMaximumSize(new java.awt.Dimension(500, 155));
        PyFileMgrPane.setMinimumSize(new java.awt.Dimension(55, 55));
        PyFileMgrPane.setPreferredSize(new java.awt.Dimension(155, 155));
        PyFileMgrPane.setLayout(new java.awt.FlowLayout());

        PyListDir.setIcon(Icon.REFRESH3);
        PyListDir.setText("ListDir /");
        PyListDir.setToolTipText("Execute command listdir() and show files");
        PyListDir.setAlignmentX(0.5F);
        PyListDir.setHorizontalAlignment(SwingConstants.LEFT);
        PyListDir.setMargin(new java.awt.Insets(2, 2, 2, 2));
        PyListDir.setMaximumSize(new java.awt.Dimension(130, 25));
        PyListDir.setPreferredSize(new java.awt.Dimension(130, 25));
        PyListDir.addActionListener(evt -> PyListFiles());
        PyFileMgrPane.add(PyListDir);


        FileManagersLayer.setLayer(firmware_type_label, JLayeredPane.DEFAULT_LAYER);
        FileManagersLayer.setLayer(NodeFileMgrPane, JLayeredPane.DEFAULT_LAYER);
        FileManagersLayer.setLayer(PyFileMgrPane, JLayeredPane.DEFAULT_LAYER);

        NodeFileMgrPane.setVisible(false);
        PyFileMgrPane.setVisible(false);

        GroupLayout FileManagersLayerLayout = new GroupLayout(FileManagersLayer);
        FileManagersLayer.setLayout(FileManagersLayerLayout);
        FileManagersLayerLayout.setHorizontalGroup(
                FileManagersLayerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(FileManagersLayerLayout.createSequentialGroup()
                                .addGroup(FileManagersLayerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(firmware_type_label, GroupLayout.DEFAULT_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(PyFileMgrPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(NodeFileMgrPane, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 433, Short.MAX_VALUE))
        );
        FileManagersLayerLayout.setVerticalGroup(
                FileManagersLayerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(FileManagersLayerLayout.createSequentialGroup()
                                .addComponent(firmware_type_label, 30, 30, 30)
                                .addGap(6, 6, 6)
                                .addComponent(NodeFileMgrPane, GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                                .addGap(6, 6, 6)
                                .addComponent(PyFileMgrPane, GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE))
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

        MenuItemFileNew.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
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

        MenuItemFileOpen.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
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

        MenuItemFileReload.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
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

        MenuItemFileSave.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemFileSave.setIcon(Icon.SAVE);
        MenuItemFileSave.setText(Context.BUNDLE.getString("Save"));
        MenuItemFileSave.addActionListener(evt -> SaveFile());
        MenuFile.add(MenuItemFileSave);


        MenuItemFileClose.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemFileClose.setIcon(Icon.FOLDER_CLOSED);
        MenuItemFileClose.setText(Context.BUNDLE.getString("Close"));
        MenuItemFileClose.addActionListener(evt -> MenuItemFileCloseActionPerformed(evt));
        MenuFile.add(MenuItemFileClose);
        MenuFile.add(new JPopupMenu.Separator());

        MenuItemFileSaveESP.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK));
        MenuItemFileSaveESP.setIcon(Icon.MOVE);
        MenuItemFileSaveESP.setText("<html><u>S</u>ave to ESP");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, FileSaveESP, org.jdesktop.beansbinding.ELProperty.create("${enabled}"), MenuItemFileSaveESP, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        MenuItemFileSaveESP.addActionListener(evt -> MenuItemFileSaveESPActionPerformed(evt));
        MenuFile.add(MenuItemFileSaveESP);

        MenuItemFileSendESP.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.ALT_MASK));
        MenuItemFileSendESP.setIcon(Icon.SCRIPT_SEND);
        MenuItemFileSendESP.setText("<html>S<u>e</u>nd to ESP");
        MenuItemFileSendESP.addActionListener(evt -> FileSendESP.doClick());
        MenuFile.add(MenuItemFileSendESP);


        MenuFile.add(new JPopupMenu.Separator());

        JMenuItem MenuItemFileExit = new JMenuItem();
        MenuItemFileExit.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        MenuItemFileExit.setText(Context.BUNDLE.getString("Exit"));
        MenuItemFileExit.addActionListener(evt -> AppClose());
        MenuFile.add(MenuItemFileExit);

        menu.add(MenuFile);

        JMenu MenuEdit = Context.createM1(Context.BUNDLE.getString("Edit"));

        MenuItemEditUndo.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemEditUndo.setIcon(Icon.UNDO1);
        MenuItemEditUndo.setText(Context.BUNDLE.getString("Undo"));
        MenuItemEditUndo.setEnabled(false);
        MenuItemEditUndo.addActionListener(evt -> MenuItemEditUndoActionPerformed(evt));
        MenuEdit.add(MenuItemEditUndo);

        MenuItemEditRedo.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, 0));
        MenuItemEditRedo.setIcon(Icon.REDO1);
        MenuItemEditRedo.setText(Context.BUNDLE.getString("Redo"));
        MenuItemEditRedo.setEnabled(false);
        MenuItemEditRedo.addActionListener(evt -> MenuItemEditRedoActionPerformed(evt));
        MenuEdit.add(MenuItemEditRedo);
        MenuEdit.add(new JPopupMenu.Separator());

        MenuItemEditCut.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemEditCut.setIcon(Icon.CUT);
        MenuItemEditCut.setText(Context.BUNDLE.getString("Cut"));
        MenuItemEditCut.setEnabled(false);
        MenuItemEditCut.addActionListener(evt -> getCurrentEdit().cut());
        MenuEdit.add(MenuItemEditCut);

        MenuItemEditCopy.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemEditCopy.setIcon(Icon.COPY);
        MenuItemEditCopy.setText(Context.BUNDLE.getString("Copy"));
        MenuItemEditCopy.setEnabled(false);
        MenuItemEditCopy.addActionListener(evt -> getCurrentEdit().copy());
        MenuEdit.add(MenuItemEditCopy);

        MenuItemEditPaste.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemEditPaste.setIcon(Icon.PASTE);
        MenuItemEditPaste.setText(Context.BUNDLE.getString("Paste"));
        MenuItemEditPaste.setEnabled(false);
        MenuItemEditPaste.addActionListener(evt -> getCurrentEdit().paste());
        MenuEdit.add(MenuItemEditPaste);
        MenuEdit.add(new JPopupMenu.Separator());

        MenuItemEditSendSelected.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.ALT_MASK));
        MenuItemEditSendSelected.setIcon(Icon.SEND_SELECTED);
        MenuItemEditSendSelected.setText("<html>Send selected <u>B</u>lock to ESP");
        MenuItemEditSendSelected.setToolTipText("Send selected block to ESP");


        MenuItemEditSendSelected.addActionListener(evt -> MenuItemEditSendSelectedActionPerformed(evt));
        MenuEdit.add(MenuItemEditSendSelected);

        MenuItemEditSendLine.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.ALT_MASK));
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


        MenuItemViewClearTerminal.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemViewClearTerminal.setIcon(Icon.TERMINAL_CLEAR);
        MenuItemViewClearTerminal.setText("Clear terminal");
        MenuItemViewClearTerminal.setToolTipText("Clear terminal window");
        MenuItemViewClearTerminal.addActionListener(evt -> MenuItemTerminalClear.doClick());
        MenuView.add(MenuItemViewClearTerminal);
        MenuView.add(new JPopupMenu.Separator());

        MenuItemViewTerminalOnly.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, java.awt.event.InputEvent.ALT_MASK));
        MenuItemViewTerminalOnly.setText("Show terminalArea only (Left panel show/hide)");
        MenuItemViewTerminalOnly.setToolTipText("Enable/disable left panel");
        MenuItemViewTerminalOnly.addItemListener(evt -> LeftRightOnlyShowStateChanged(evt));
        MenuView.add(MenuItemViewTerminalOnly);

        MenuItemViewEditorOnly.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, java.awt.event.InputEvent.ALT_MASK));
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

    private void OpenActionPerformed(java.awt.event.ActionEvent evt) {
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

    private void UpdateButtons() {
        if (Open.isSelected() && serialPortStatus.isOpened()) {
            Port.setEnabled(false);
            //Speed.setEnabled(false);
            ReScan.setEnabled(false);

            thandler.setEnabled(true);

            CR.setEnabled(true);
            LF.setEnabled(true);
            // left panel
            FileSaveESP.setEnabled(true);
            MenuItemFileSaveESP.setEnabled(true);
            FileSendESP.setEnabled(true);
            MenuItemFileSendESP.setEnabled(true);


            MenuItemEditSendLine.setEnabled(true);
            ButtonSendLine.setEnabled(true);
        } else {
            Port.setEnabled(true);
            //Speed.setEnabled(true);
            ReScan.setEnabled(true);

            thandler.setEnabled(false);

            CR.setEnabled(false);
            LF.setEnabled(false);
            // left panel
            FileSaveESP.setEnabled(false);
            FileSaveESP.setSelected(false);
            MenuItemFileSaveESP.setEnabled(false);
            FileSendESP.setEnabled(false);
            FileSendESP.setSelected(false);
            MenuItemFileSendESP.setEnabled(false);


            MenuItemEditSendLine.setEnabled(false);
            ButtonSendLine.setEnabled(false);
        }
        UpdateLED();

    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {
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


    private void ContextMenuTerminalPopupMenuWillBecomeVisible(PopupMenuEvent evt) {
        try {
            MenuItemTerminalCopy.setEnabled(thandler.hasSelected());
        } catch (Exception e) {
            MenuItemTerminalCopy.setEnabled(false);
        }
    }


    boolean SaveFile() {
        TextEditArea area = getCurrentEdit();

        boolean success = false;

        try {
            try {
                LOGGER.info("Try to saving file " + area.getFile().getName() + " ...");
                area.save_file();
                String filename = area.getFile().getName();
                LOGGER.info("Save file " + filename + ": Success.");
                success = true;
            } catch (IOException ex) {
                LOGGER.log(Level.WARNING, "Save file " + area.getFile().getName() + ": FAIL.", ex);
                JOptionPane.showMessageDialog(null, "Error, file not saved!");
            }

        } finally {
            UpdateEditorButtons(area);
        }

        return success;
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

    private void formFocusGained(java.awt.event.FocusEvent evt) {
        UpdateEditorButtons(getCurrentEdit());
        UpdateButtons();
    }

    private void MenuItemFileCloseActionPerformed(java.awt.event.ActionEvent evt) {
        CloseFile();
    }


    private void AlwaysOnTopItemStateChanged(java.awt.event.ItemEvent evt) {
        this.setAlwaysOnTop(AlwaysOnTop.isSelected());
    }

    private void MenuItemEditUndoActionPerformed(java.awt.event.ActionEvent evt) {
        if (getCurrentEdit().rSyntaxTextArea.canUndo()) {
            getCurrentEdit().rSyntaxTextArea.undoLastAction();
        }
    }

    private void MenuItemEditRedoActionPerformed(java.awt.event.ActionEvent evt) {
        if (getCurrentEdit().rSyntaxTextArea.canRedo()) {
            getCurrentEdit().rSyntaxTextArea.redoLastAction();
        }
    }

    private void NodeListFiles() {
        if (!serialPortStatus.isOpened()) {
            LOGGER.info("ERROR: Communication with MCU not yet established.");
            return;
        }
        String cmd = Context.GetLua("/yh/espide/file_list.lua");
        try {
            serialPort.removeEventListener();
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }
        try {
            serialPort.addEventListener(new PortNodeFilesReader(), portMask);
            LOGGER.info("FileManager: Add EventListener: Success.");
        } catch (SerialPortException e) {
            LOGGER.info("FileManager: Add EventListener Error. Canceled.");
            return;
        }
        ClearNodeFileManager();
        rx_data = "";
        rcvBuf = "";
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
        WatchDog();
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

    private void FileDownload(String param) {
        if (!serialPortStatus.isOpened()) {
            LOGGER.info("Downloader: Communication with MCU not yet established.");
            return;
        }
        // param  init.luaSize:123
        DownloadedFileName = param.split("Size:")[0];
        int size = Integer.parseInt(param.split("Size:")[1]);
        packets = size / 1024;
        if (size % 1024 > 0) {
            packets++;
        }
        sendBuf = new ArrayList<>();
        rcvPackets = new ArrayList<>();
        PacketsData = new ArrayList<>();
        PacketsSize = new ArrayList<>();
        PacketsNum = new ArrayList<>();
        BufferCenter.RcvBuffer.reset();
        BufferCenter.DownBuffer.reset();
        rx_byte = new byte[0];
        PacketsCRC = new ArrayList<>();
        String cmd = Context.GetLua("/yh/espide/file_download.lua", DownloadedFileName);
        s = cmd.split("\r?\n");
        for (String subs : s) {
            sendBuf.add(subs);
        }
        LOGGER.info("Downloader: Starting...");
        startTime = System.currentTimeMillis();
        SendLock();
        rx_data = "";
        rcvBuf = "";
        rx_byte = new byte[0];
        try {
            serialPort.removeEventListener();
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }
        try {
            serialPort.addEventListener(new PortFileDownloader(), portMask);
            LOGGER.info("Downloader: Add EventListener: Success.");
        } catch (SerialPortException e) {
            LOGGER.info("Downloader: Add EventListener Error. Canceled.");
            return;
        }
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
        LOGGER.info("Downloader: Start");
        thandler.comment("Download file \"" + DownloadedFileName + "\"...", true);
        timer.setInitialDelay(delay);
        WatchDog();
        timer.start();
        return;
    }

    private void FileDownloadFinisher(boolean success) {
        try {
            serialPort.removeEventListener();
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }
        try {
            serialPort.addEventListener(new PortReader(), portMask);
        } catch (SerialPortException e) {
            LOGGER.info("Downloader: Can't Add OldEventListener.");
        }
        //SendUnLock();
        StopSend();
        if (success) {
            thandler.comment("Success.", true);
            if (DownloadCommand.startsWith("EDIT")) {
                File file = SaveDownloadedFile(DownloadedFileName, BufferCenter.RcvBuffer.toByteArray());
                OpenFile(file);
            } else if (DownloadCommand.startsWith("DOWNLOAD")) {
                SaveDownloadedFile(DownloadedFileName, BufferCenter.DownBuffer.toByteArray());
            } else {
                // nothing, reserved
            }
        } else {
            thandler.comment("FAIL.", true);
        }
    }

    private byte[] concatArray(byte[] a, byte[] b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        byte[] r = new byte[a.length + b.length];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }

    private byte[] copyPartArray(byte[] a, int start, int len) {
        if (a == null) {
            return null;
        }
        if (start > a.length) {
            return null;
        }
        byte[] r = new byte[len];
        try {
            System.arraycopy(a, start, r, 0, len);
        } catch (Exception e) {
            LOGGER.info(e.toString());
            LOGGER.info("copyPartArray exception");
            LOGGER.info("size a=" + Integer.toString(a.length));
            LOGGER.info("start =" + Integer.toString(start));
            LOGGER.info("len   =" + Integer.toString(len));
        }
        return r;
    }

    private int FindPacketID(int nPacket) {
        int i, j, n, ret = -1;
        boolean success;
        String s = "~~~DATA-START~~~";
        i = 0;
        n = 0;
        while (i < rx_byte.length - s.length()) {
            success = true;
            for (j = 0; j < s.length(); j++) {
                if (!(rx_byte[i + j] == s.charAt(j))) {
                    success = false;
                    break;
                }
            }
            if (success) {
                n++;
                //log("Downloader: n =" + Integer.toString(n));
                //log("Downloader: nPacket =" + Integer.toString(nPacket));
            }
            if (success && (n == nPacket)) {
                ret = i + s.length();
                break;
            } else {
            }
            i++;
        }
        //log("Downloader: FindPacketID=" + Integer.toString(ret));
        return ret;
    }

    private int CRC(byte[] s) {
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

    private void UpdateLedCTS() {
        try {
            if (serialPort.isCTS()) {
                PortCTS.setIcon(Icon.LED_GREEN);
            } else {
                PortCTS.setIcon(Icon.LED_GREY);
            }
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }
    }

    private void MenuItemEditSendSelectedActionPerformed(java.awt.event.ActionEvent evt) {
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

    private void MenuItemFileSaveESPActionPerformed(java.awt.event.ActionEvent evt) {
        if (!FileSaveESP.isSelected()) {
            FileSaveESP.doClick();
        }
    }

    private void MenuItemEditSendLineActionPerformed(java.awt.event.ActionEvent evt) {
        int nLine;

        nLine = getCurrentEdit().rSyntaxTextArea.getCaretLineNumber();
        String cmd = getCurrentEdit().rSyntaxTextArea.getText().split("\r?\n")[nLine];
        btnSend(cmd);


    }

    private void MenuItemESPFormatActionPerformed(java.awt.event.ActionEvent evt) {

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

    private void formPropertyChange(java.beans.PropertyChangeEvent evt) {
        //log(evt.getPropertyName());
    }

    private void formComponentResized(java.awt.event.ComponentEvent evt) {
        isFileManagerShow();
    }


    private void PortDTRActionPerformed(java.awt.event.ActionEvent evt) {
        Config.ins.setPortDtr(PortDTR.isSelected());
        try {
            serialPort.setDTR(PortDTR.isSelected());
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

    private void PortRTSActionPerformed(java.awt.event.ActionEvent evt) {
        Config.ins.setPortRts(PortRTS.isSelected());
        try {
            serialPort.setRTS(PortRTS.isSelected());
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

    private void MenuItemViewToolbarItemStateChanged(java.awt.event.ItemEvent evt) {
        Config.ins.setShowToolbar(MenuItemViewToolbar.isSelected());

        FilesToolBar.setVisible(Config.ins.getShowToolbar());
    }

    private void MenuItemViewFileManagerItemStateChanged(java.awt.event.ItemEvent evt) {
        Config.ins.setFm_right_show(MenuItemViewFileManager.isSelected());
        isFileManagerShow();
    }

    private void FileRenameActionPerformed(java.awt.event.ActionEvent evt) {
        btnSend("file.rename(\"" + FileRenameLabel.getText() + "\",\"" + FileRename.getText().trim() + "\")");
        try {
            Thread.sleep(200L);
        } catch (Exception e) {
        }
        FileListReload.doClick();
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
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


    private void NodeMCUComponentShown(java.awt.event.ComponentEvent evt) {
        UpdateEditorButtons(getCurrentEdit());
        UpdateButtons();
    }

    private void FileSendESPActionPerformed(java.awt.event.ActionEvent evt) {
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

    private void FileSaveESPActionPerformed(java.awt.event.ActionEvent evt) {
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
        if (!Open.isSelected() || !serialPortStatus.isOpened()) {
            LOGGER.info("FileSaveESP: Serial port not open. Operation canceled.");
            FileSaveESP.setSelected(false);
            return;
        }
        if (FirmwareType.current.eq(FirmwareType.MicroPython)) {
            pySaveFileESP(fName);
        } else if (FirmwareType.current.eq(FirmwareType.NodeMCU)) {
            nodeSaveFileESP(fName);
        } else {
            thandler.comment("detect firmware type : " + FirmwareType.current, true);
        }
    }


    private void reLocationRightSplit() {
        RightTerminalSplitPane.setDividerLocation(RightTerminalSplitPane.getWidth() - 180);
    }

    private void LeftRightOnlyShowStateChanged(java.awt.event.ItemEvent evt) {
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
        String cmd = "r,u,t=file.fsinfo() print(\"Total : \"..t..\" bytes\\r\\nUsed  : \"..u..\" bytes\\r\\nRemain: \"..r..\" bytes\\r\\n\") r=nil u=nil t=nil";
        send(addCRLF(cmd), true);
    }

    public void inc_j() {
        ++j;
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

    public boolean SetSerialPortParams() {
        boolean success = false;
        String portName = GetSerialPortName();
        try {
            success = serialPort.setParams(getBaudRate(),
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE,
                    PortRTS.isSelected(),
                    PortDTR.isSelected());
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }
        if (!success) {
            LOGGER.info("ERROR setting port " + portName + " parameters.");
        }
        UpdateLED();
        return success;
    }

    public boolean portOpen() {
        boolean success = false;
        String portName = GetSerialPortName();

        LOGGER.info("Try to open port " + portName + ", baud " + getBaudRate() + ", 8N1");

        serialPort = new SerialPort(portName);
        try {
            success = serialPort.openPort();
            if (!success) {
                LOGGER.info("ERROR opening serial port " + portName);
                return success;
            }
            SetSerialPortParams();
            serialPort.addEventListener(new PortReader(), portMask);
        } catch (SerialPortException ex) {
            LOGGER.info(ex.toString());
            return false;
        }

        if (success) {
            LOGGER.info("AddTab port " + portName + " - Success.");
            thandler.comment("PORT OPEN " + getBaudRate(), true);

            serialPortStatus = SerialPortStatus.CONNECTED;
            //
            btnSend("\r\n");
        }
        return success;

    }

    public void portClose() {
        try {
            if (serialPort.closePort()) {
                serialPortStatus = SerialPortStatus.CLOSED;
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
        if (CR.isSelected()) {
            cmd += (char) 13;
        }
        if (LF.isSelected()) {
            cmd += (char) 10;
        }
        return cmd;
    }

    public String addCR(String s) {
        String r = s;
        r += (char) 13;
        return r;
    }

    public void btnSend(String s) {
        send(addCRLF(s), true);
    }

    private void FinalInit() {
        setIconImage(Icon.ESP8299_64X64.getImage());
        setLocationRelativeTo(null); // window centered


        PyFileAsButton = new ArrayList<>();


        FilesTabbedPane.removeAll();
        FilesTabbedPane.setSelectChangedListener(this);

        LoadPrefs();

        FileRenamePanel.setVisible(false);

        updateTheme();
    }

    private void LoadPrefs() {
        // Settings - Firmware

        AutoScroll.setSelected(Config.ins.getAutoScroll());

        MenuItemViewToolbar.setSelected(Config.ins.getShowToolbar());

        MenuItemViewFileManager.setSelected(Config.ins.getFm_right_show());
        isFileManagerShow();


        PortDTR.setSelected(Config.ins.getPortDtr());
        PortRTS.setSelected(Config.ins.getPortRts());
        EOL.setSelected(Config.ins.getShowEOL());


        LOGGER.info("Load saved settings: DONE.");
    }

    private void AddNodeFileButton(String FileName, int size) {
        JPopupMenu popup = new JPopupMenu();
        if (FileName.endsWith(".lua")) {
            AddMenuItemRun(popup, FileName);
            AddMenuItemCompile(popup, FileName);
            popup.add(new JPopupMenu.Separator());
            AddMenuItemView(popup, FileName);
            AddMenuItemDump(popup, FileName);
            AddMenuItemEdit(popup, FileName, size);
            AddMenuItemDownload(popup, FileName, size);
            AddMenuItemRename(popup, FileName);
            popup.add(new JPopupMenu.Separator());
            AddMenuItemRemove(popup, FileName);
        } else if (FileName.endsWith(".lc")) {
            AddMenuItemRun(popup, FileName);
            popup.add(new JPopupMenu.Separator());
            AddMenuItemDump(popup, FileName);
            AddMenuItemDownload(popup, FileName, size);
            AddMenuItemRename(popup, FileName);
            popup.add(new JPopupMenu.Separator());
            AddMenuItemRemove(popup, FileName);
        } else {
            AddMenuItemView(popup, FileName);
            AddMenuItemDump(popup, FileName);
            AddMenuItemEdit(popup, FileName, size);
            AddMenuItemDownload(popup, FileName, size);
            AddMenuItemRename(popup, FileName);
            popup.add(new JPopupMenu.Separator());
            AddMenuItemRemove(popup, FileName);
        }
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
            DownloadCommand = "EDIT";
            FileDownload(evt.getActionCommand());
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
            DownloadCommand = "DOWNLOAD";
            FileDownload(evt.getActionCommand());
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

    private void StopSend() {
        try {
            serialPort.removeEventListener();
        } catch (Exception e) {
        }
        try {
            timer.stop();
        } catch (Exception e) {
        }
        try {
            timeout.stop();
        } catch (Exception e) {
        }
        try {
            serialPort.addEventListener(new PortReader(), portMask);
        } catch (SerialPortException e) {
        }
        SendUnLock();
        long duration = System.currentTimeMillis() - startTime;
        LOGGER.info("Operation done. Duration = " + Long.toString(duration) + " ms");
    }

    private boolean SendToESP(String str) {
        boolean success = false;
        if (!Open.isSelected() || !serialPortStatus.isOpened()) {
            LOGGER.info("SendESP: Serial port not open. Canceled.");
            return success;
        }
        sendBuf = new ArrayList<>();
        s = str.split("\r?\n");
        sendBuf.addAll(Arrays.asList(s));
        success = SendTimerStart();
        LOGGER.info("SendToESP: Starting...");
        return success;
    }

    private boolean SendToESP(ArrayList<String> buf) {
        boolean success = false;
        if (!Open.isSelected() || !serialPortStatus.isOpened()) {
            LOGGER.info("SendESP: Serial port not open. Cancel.");
            return success;
        }
        sendBuf = new ArrayList<>();
        sendBuf.addAll(buf);
        if (FirmwareType.current.eq(FirmwareType.MicroPython)) {
            sendBuf.add("");
            sendBuf.add("");
            sendBuf.add("");
        }
        success = SendTimerStart();
        LOGGER.info("SendToESP: Starting...");
        return success;
    }

    private void WatchDog() {
        if (Config.ins.isDumb_mode()) {
            return;
        }
        watchDog = evt -> {
            StopSend();
            Toolkit.getDefaultToolkit().beep();
            thandler.comment("Waiting answer from ESP - Timeout reached. Command aborted.", true);
            LOGGER.info("Waiting answer from ESP - Timeout reached. Command aborted.");
        };
        int delay = Config.ins.getAnswer_timeout() * 1000;
        if (delay == 0) {
            delay = 300;
        }
        timeout = new Timer(delay, watchDog);
        timeout.setRepeats(false);
        timeout.setInitialDelay(delay);
        timeout.start();
    }

    private boolean nodeSaveFileESP(String ft) {
        boolean success = false;
        LOGGER.info("FileSaveESP: Try to save file to ESP...");
        sendBuf = new ArrayList<>();
        if (Config.ins.isTurbo_mode()) {
            return nodeSaveFileESPTurbo(ft);
        }
        sendBuf.add("file.remove(\"" + ft + "\");");
        sendBuf.add("file.open(\"" + ft + "\",\"w+\");");
        sendBuf.add("w = file.writeline\r\n");
        s = getCurrentEdit().rSyntaxTextArea.getText().split("\r?\n");
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
        boolean success = false;
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

        success = SendTurboTimerStart();
        LOGGER.info("FileSaveESP-Turbo: Starting...");
        return success;
    }

    public boolean SendTurboTimerStart() {
        startTime = System.currentTimeMillis();
        SendLock();
        rcvBuf = "";
        try {
            serialPort.removeEventListener();
        } catch (Exception e) {
        }
        try {
            serialPort.addEventListener(new PortTurboReader(), portMask);
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
        WatchDog();
        timer.start();
        return true;
    }

    public boolean pasteMode() {
        return pasteMode;
    }

    public boolean pasteMode(boolean newMode) {
        pasteMode = newMode;
        return pasteMode;
    }

    public boolean SendTimerStart() {
        startTime = System.currentTimeMillis();
        SendLock();
        rcvBuf = "";
        try {
            serialPort.removeEventListener();
        } catch (Exception e) {
        }
        try {
            if (Config.ins.isDumb_mode()) {
                serialPort.addEventListener(new PortReader(), portMask);
            } else {
                serialPort.addEventListener(new PortExtraReader(), portMask);
            }
        } catch (SerialPortException e) {
            LOGGER.info("DataSender: Add EventListener Error. Canceled.");
            return false;
        }
        int delay = 0;
        j0();
        if (Config.ins.isDumb_mode()) { // DumbMode
            delay = Config.ins.getLine_delay_for_dumb();
            if (FirmwareType.current.eq(FirmwareType.NodeMCU)) {
                taskPerformer = evt -> {
                    if (j < sendBuf.size()) {
                        send(addCRLF(sendBuf.get(j).trim()), false);
                        inc_j();
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
            } else { // MicroPython
                taskPerformer = evt -> {
                    if (j < sendBuf.size()) {
                        if ((j == 0) && pasteMode()) {
                            sendStart();
                        }
                        send(addCRLF(sendBuf.get(j)), false);
                        inc_j();
                        if ((j == sendBuf.size()) && pasteMode()) {
                            sendEnd();
                        }
                        int div = sendBuf.size() - 1;
                        if (div == 0) {
                            div = 1; // for non-zero divide
                        }
                        Progress.ins.setValue((j * 100) / div);
                        if (j > sendBuf.size() - 1) {
                            timer.stop();
                            pasteMode(true);
                            StopSend();
                        }
                    }
                };
            }
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
            WatchDog();
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
            serialPort.writeString(s);
        } catch (SerialPortException ex) {
            LOGGER.info("send FAIL:" + s.replace("\r\n", "<CR><LF>"));
        }
        if (!Config.ins.isDumb_mode() && !simple) {
            try {
                timeout.restart();
            } catch (Exception e) {
            }
        }
        if (simple) {
            Busy.setIcon(Icon.LED_GREY);

        }
    }

    public void sendBin(byte data) {
        if (!Open.isSelected()) {
            LOGGER.info("DataSender: Serial port not open, operation FAILED.");
            return;
        }
        try {
            serialPort.writeByte(data);
        } catch (SerialPortException ex) {
            LOGGER.info("send FAIL:" + (char) data);
        }
    }

    public void sendStart() {
        byte data = 0x05;
        sendBin(data);
    }

    public void sendEnd() {
        byte data = 0x04;
        sendBin(data);
    }

    public void Busy() {
        Busy.setText("BUSY");
        Busy.setBackground(new java.awt.Color(153, 0, 0)); // LED_RED


        Progress.ins.setValue(0);
        Progress.ins.setVisible(true);

        FileSaveESP.setEnabled(false);
        FileSendESP.setEnabled(false);
        MenuItemFileSendESP.setEnabled(false);


        MenuItemEditSendSelected.setEnabled(false);
        ButtonSendSelected.setEnabled(false);
        MenuItemEditSendLine.setEnabled(false);
        ButtonSendLine.setEnabled(false);


    }

    public void SendLock() {
        Busy();
        FileSaveESP.setText("Cancel");
        FileSaveESP.setIcon(Icon.ABORT);
        FileSaveESP.setEnabled(true);
        FileSaveESP.setSelected(true);
    }

    public void Idle() {

        Progress.ins.setVisible(false);

        Busy.setText("IDLE");
        Busy.setBackground(new java.awt.Color(0, 153, 0)); // LED_GREEN
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

        if (serialPortStatus.isClosed()) {
            PortOpenLabel.setIcon(Icon.LED_GREY);
        } else if (serialPortStatus.isConnected()) {
            PortOpenLabel.setIcon(Icon.LED_RED);
        } else if (serialPortStatus.isOpened()) {
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
        if (!serialPortStatus.isOpened()) {
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
        PacketsData = new ArrayList<>();
        PacketsCRC = new ArrayList<>();
        PacketsSize = new ArrayList<>();
        PacketsNum = new ArrayList<>();
        sendPacketsCRC = new ArrayList<>();
        rcvBuf = "";
        BufferCenter.DownBuffer.reset();
        rx_byte = new byte[0];
        tx_byte = new byte[0];

        if (!LoadBinaryFile(upload_file_list.get(upload_file_index))) {
            LOGGER.info("Uploader: loaded fail!");
            return;
        }
        int lastPacketSize = SplitDataToPackets();
        if (lastPacketSize < 0) {
            LOGGER.info("Uploader: SplitDataToPackets fail!");
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
        int startPackets;
        if (packets == 1) { // small file
            startPackets = lastPacketSize;
        } else {
            startPackets = SendPacketSize;
        }
        sendBuf.add("_up(" + Integer.toString(packets) + "," + Integer.toString(startPackets) + "," + Integer.toString(lastPacketSize) + ")");
        LOGGER.info("Uploader: Starting...");
        startTime = System.currentTimeMillis();
        SendLock();
        rx_data = "";
        rcvBuf = "";
        try {
            serialPort.removeEventListener();
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }
        try {
            serialPort.addEventListener(new PortFilesUploader(), portMask);
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
        WatchDog();
        timer.start();
    }

    private boolean LoadBinaryFile(File f) {
        boolean success = false;
        try {
            LOGGER.info("BinaryFileLoader: Try to load file " + f.getName() + " ...");

            DataInputStream dis = new DataInputStream(new FileInputStream(f));
            tx_byte = new byte[dis.available()];
            int size = dis.read(tx_byte);
            dis.close();
            if (size == f.length()) {
                LOGGER.info("BinaryFileLoader: Load file " + f.getName() + ": Success, size:" + Long.toString(f.length()));
                success = true;
            } else {
                LOGGER.info("BinaryFileLoader: Load file " + f.getName() + ": Fail, file size:" + Long.toString(f.length()) + ", read:" + Integer.toString(size));
            }
        } catch (IOException e) {
            LOGGER.info("BinaryFileLoader: Load file " + f.getName() + ": FAIL.");
            LOGGER.info(e.toString());
            JOptionPane.showMessageDialog(null, "BinaryFileLoader: Error, file " + f.getName() + " can't be read!");
        }
        return success;
    }

    private int SplitDataToPackets() {
        sendPackets = new ArrayList<>();
        packets = tx_byte.length / SendPacketSize;
        LOGGER.info("1. packets = " + Integer.toString(packets));
        if ((tx_byte.length % SendPacketSize) > 0) {
            packets++;
        }
        LOGGER.info("2. packets = " + Integer.toString(packets));
        if (tx_byte.length < SendPacketSize) {
            packets = 1;
        }
        int remain = tx_byte.length;
        int lastPacketSize = -1;
        byte[] b;
        int pos = 0;
        for (int i = 0; i < packets; i++) {
            LOGGER.info("3. packet = " + Integer.toString(i));
            if (remain > SendPacketSize) {
                b = new byte[SendPacketSize]; // default value is 200
            } else {
                b = new byte[remain];
                lastPacketSize = remain;
            }
            System.arraycopy(tx_byte, pos, b, 0, b.length);
            sendPackets.add(b);
            LOGGER.info("BinaryFileLoader: Prepare next packet for send, len=" + Integer.toString(b.length));
            remain -= b.length;
            pos += b.length;
        }
        LOGGER.info("BinaryFileLoader: Total packets prepared=" + Integer.toString(sendPackets.size()));
        return lastPacketSize;
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
            serialPort.writeBytes(b);
        } catch (SerialPortException ex) {
            LOGGER.info("BytesSender send FAIL:" + b.toString().replace("\r\n", "<CR><LF>"));

        }
    }

    private void ViewFile(String fn) {
        String c = Context.GetLua("/yh/espide/file_view.lua", fn);
        SendToESP(cmdPrep(c));
    }

    private void SetFirmwareType(FirmwareType ftype) {
        FirmwareType.current = ftype;
        firmware_type_label.setText(ftype.toString());

        NodeFileMgrPane.setVisible(false);
        PyFileMgrPane.setVisible(false);
        switch (ftype) {
            case MicroPython:
                PyFileMgrPane.setVisible(true);
                break;
            case NodeMCU:
                NodeFileMgrPane.setVisible(true);
                break;
            case AT:
                break;
        }

        thandler.setSyntaxEditingStyle(ftype);

    }

    private boolean pySaveFileESP(String ft) {
        LOGGER.info("pyFileSaveESP: Starting...");
        String[] content = getCurrentEdit().rSyntaxTextArea.getText().split("\r?\n");
        if (pyFiler.Put(ft, content)) {
            pasteMode(false);
            return SendTimerStart();
        }
        return false;
    }

    private void AddPyFileButton(String FileName) {
        JButton f = new JButton(FileName);
        f.setAlignmentX(0.5F);
        f.setMargin(new java.awt.Insets(2, 2, 2, 2));
        f.setMaximumSize(new java.awt.Dimension(130, 25));
        f.setPreferredSize(new java.awt.Dimension(130, 25));
        f.setHorizontalAlignment(SwingConstants.LEFT);
        PyFileAsButton.add(f);

        PyFileMgrPane.add(f);

    } // AddPyFileButton

    private void ClearPyFileManager() {
        if (!MenuItemViewFileManager.isSelected()) {
            return;
        }
        PyFileMgrPane.removeAll();
        PyFileMgrPane.add(PyListDir);
        PyFileMgrPane.repaint();
        PyFileAsButton = new ArrayList<>();
    } // ClearPyFileManager

    private void PyListFiles() {
        if (!serialPortStatus.isOpened()) {
            LOGGER.info("ERROR: Communication with MCU not yet established.");
            return;
        }
        try {
            serialPort.removeEventListener();
        } catch (Exception e) {
            LOGGER.info(e.toString());
            return;
        }
        try {
            serialPort.addEventListener(new PortPyFilesReader(), portMask);
            LOGGER.info("pyFileManager: Add EventListener: Success.");
        } catch (SerialPortException e) {
            LOGGER.info("pyFileManager: Add EventListener Error. Canceled.");
            return;
        }
        ClearPyFileManager();
        rx_data = "";
        rcvBuf = "";
        LOGGER.info("pyFileManager: Starting...");
        String cmd = "import os;os.listdir('" + pyFiler.pwd() + "')";
        btnSend(cmd);
        WatchDogPyListDir();
    } // PyListFiles

    private void WatchDogPyListDir() {
        watchDog = evt -> {
            //StopSend();
            Toolkit.getDefaultToolkit().beep();
            thandler.comment("Waiting answer from ESP - Timeout reached. Command aborted.", true);
            LOGGER.info("Waiting answer from ESP - Timeout reached. Command aborted.");
            try {
                serialPort.removeEventListener();
                serialPort.addEventListener(new PortReader(), portMask);
            } catch (Exception e) {
                LOGGER.info(e.toString());
            }
            SendUnLock();
        };
        int delay = Config.ins.getAnswer_timeout() * 1000;
        if (delay == 0) {
            delay = 300;
        }

        delay = 3000;

        timeout = new Timer(delay, watchDog);
        timeout.setRepeats(false);
        timeout.setInitialDelay(delay);
        timeout.start();
    } // WatchDogPyListDir

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

        public void serialEvent(SerialPortEvent event) {
            String data;
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    data = serialPort.readString(event.getEventValue());
                    rcvBuf = rcvBuf + data;
                    rx_data = rx_data + data;
                } catch (Exception e) {
                    data = "";
                    LOGGER.info(e.toString());
                }
                if (rcvBuf.contains("> ")) {
                    try {
                        timeout.restart();
                    } catch (Exception e) {
                        LOGGER.info(e.toString());
                    }
                    rcvBuf = "";
                    if (j < sendBuf.size() - 1) {
                        if (timer.isRunning() || sendPending) {
                            //
                        } else {
                            inc_j();
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
                            timeout.stop();
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
                            s = rx_data.split("\r?\n");
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
                            serialPort.removeEventListener();
                        } catch (Exception e) {
                        }
                        serialPort.addEventListener(new PortReader(), portMask);
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

    private class PortFileDownloader implements SerialPortEventListener {

        public void serialEvent(SerialPortEvent event) {
            String data;
            byte[] b;
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    b = serialPort.readBytes();
                    rx_byte = concatArray(rx_byte, b);
                    data = new String(b);
                    rcvBuf = rcvBuf + data;
                    rx_data = rx_data + data;
                    //TerminalAdd(data);
                } catch (SerialPortException e) {
                    data = "";
                    LOGGER.info(e.toString());
                }
                if (rcvBuf.contains("> ")) {
                    try {
                        timeout.restart();
                    } catch (Exception e) {
                        LOGGER.info(e.toString());
                    }
                    rcvBuf = "";
                    if (j < sendBuf.size() - 1) {
                        if (timer.isRunning() || sendPending) {
                            //
                        } else {
                            inc_j();
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
                /*
                String l = data.replace("\r", "<CR>");
                l = l.replace("\n", "<LF>");
                l = l.replace("`", "<OK>");
                log("recv:" + l);
                 */
                if ((rx_data.lastIndexOf("~~~DATA-END") >= 0) && (rx_data.lastIndexOf("~~~DATA-START") >= 0)) {
                    // we got full packet
                    rcvPackets.add(rx_data.split("~~~DATA-END")[0]); // store RAW data
                    rx_data = rx_data.substring(rx_data.indexOf("~~~DATA-END") + 11); // and remove it from buf
                    if (packets > 0) { // exclude div by zero
                        Progress.ins.setValue(rcvPackets.size() * 100 / packets);
                    }
                    //  ~~~DATA-START~~~buf~~~DATA-LENGTH~~~string.len(buf)~~~DATA-N~~~i~~~DATA-CRC~~~CheckSum~~~DATA-END
                    //0        1                  2                               3            4                     5
                    // split packet & check crc
                    int i = rcvPackets.size() - 1;
                    String[] part = rcvPackets.get(i).split("~~~DATA-CRC~~~");
                    PacketsCRC.add(Integer.parseInt(part[1]));
                    String left = part[0];
                    part = left.split("~~~DATA-N~~~");
                    PacketsNum.add(Integer.parseInt(part[1]));
                    left = part[0];
                    part = left.split("~~~DATA-LENGTH~~~");
                    PacketsSize.add(Integer.parseInt(part[1]));
                    left = part[0];
                    part = left.split("~~~DATA-START~~~");
                    PacketsData.add(part[1]);
                    int startData = FindPacketID(i + 1);
                    byte[] x;
                    if ((startData > 0) && (rx_byte.length >= (startData + PacketsSize.get(i)))) {
                        x = copyPartArray(rx_byte, startData, PacketsSize.get(i));
                        //log("Downloader: data from packet #" + Integer.toString(i+1) + " found in raw data");
                    } else {
                        x = new byte[0];
                        //log("Downloader: data packet #" + Integer.toString(i+1) + " not found in raw data.");
                        //log("raw date length " + rx_byte.length +
                        //    "\r\nstartData " + Integer.toString(startData) );
                    }
                    //rx_byte = new byte[0];
                    if (PacketsCRC.get(i) == CRC(x)) {
                        try {
                            timeout.restart();
                        } catch (Exception e) {
                            LOGGER.info(e.toString());
                        }

                        try {
                            BufferCenter.RcvBuffer.write(PacketsData.get(i).getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            BufferCenter.DownBuffer.write(x);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        LOGGER.info("Downloader: Receive packet: " + Integer.toString(PacketsNum.get(i)) + "/" + Integer.toString(packets)
                                + ", size:" + Integer.toString(PacketsSize.get(i))
                                + ", CRC check: Success");
                    } else {
                        try {
                            timeout.stop();
                        } catch (Exception e) {
                            LOGGER.info(e.toString());
                        }
                        LOGGER.info("Downloader: Receive packets: " + Integer.toString(PacketsNum.get(i)) + "/" + Integer.toString(packets)
                                + ", size expected:" + Integer.toString(PacketsSize.get(i))
                                + ", size received:" + Integer.toString(BufferCenter.DownBuffer.size())
                                + "\r\n, CRC expected :" + Integer.toString(PacketsCRC.get(i))
                                + "  CRC received :" + Integer.toString(CRC(x)));
                        LOGGER.info("Downloader: FAIL.");
                        PacketsCRC.clear();
                        PacketsNum.clear();
                        PacketsSize.clear();
                        PacketsData.clear();
                        rcvPackets.clear();
                        BufferCenter.RcvBuffer.clean();
                        BufferCenter.DownBuffer.clean();
                        FileDownloadFinisher(false);
                    }
                } else if ((rx_data.lastIndexOf("~~~DATA-TOTAL-END~~~") >= 0) && (PacketsNum.size() == packets)) {
                    try {
                        timeout.stop();
                    } catch (Exception e) {
                        LOGGER.info(e.toString());
                    }
                    Progress.ins.setValue(100);
                    LOGGER.info("Downloader: Receive final sequense. File download: Success");
                    //log(rx_data);
                    FileDownloadFinisher(true);
                } else {
                    //log("rxbyte - " + Integer.toString( rx_byte.length ));
                }
            } else if (event.isCTS()) {
                UpdateLedCTS();
            } else if (event.isERR()) {
                LOGGER.info("Downloader: Unknown serial port error received.");
                FileDownloadFinisher(false);
            }
        }
    }

    private class PortReader implements SerialPortEventListener {

        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                String data = null;
                try {
                    data = serialPort.readString(event.getEventValue());
                } catch (SerialPortException ex) {
                    LOGGER.info(ex.toString());
                }
                if (null != data) {
                    thandler.add(data);
                    if (serialPortStatus.isConnected()) {
                        if (!data.trim().isEmpty()) {
                            if (data.contains("\r\n>>>")) {
                                thandler.comment("MicroPython firmware detected.", true);
                                btnSend("import sys; print(\"MicroPython ver:\",sys.version_info)");
                                SetFirmwareType(yh.espide.FirmwareType.MicroPython);
                            } else if (data.contains("\r\n>")) {
                                thandler.comment("NodeMCU firmware detected.", true);
                                btnSend("=node.info()");
                                SetFirmwareType(yh.espide.FirmwareType.NodeMCU);
                            } else if (data.contains("\r\nERR")) {
                                thandler.comment("AT-based firmware detected.", true);
                                btnSend("AT+GMR");
                                SetFirmwareType(yh.espide.FirmwareType.AT);
                            } else {
                                thandler.comment("Can't detect firmware, Choose by yourself.", true);
                            }

                            serialPortStatus = SerialPortStatus.OPENED;
                            UpdateButtons();
                        }
                    }
                }


            } else if (event.isCTS()) {
                UpdateLedCTS();
            } else if (event.isERR()) {
                LOGGER.info("FileManager: Unknown serial port error received.");
            }
        }
    }

    private class PortExtraReader implements SerialPortEventListener {

        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                String data = "";
                try {
                    data = serialPort.readString(event.getEventValue());
                } catch (SerialPortException ex) {
                    LOGGER.info(ex.toString());
                }
                data = data.replace(">> ", "");
                data = data.replace(">>", "");
                data = data.replace("\r\n> ", "");
                data = data.replace("\r\n\r\n", "\r\n");

                rcvBuf = rcvBuf + data;
                LOGGER.info("recv:" + data.replace("\r\n", "<CR><LF>"));
                thandler.add(data);
                if (rcvBuf.contains(sendBuf.get(j).trim())) {
                    // first, reset watchdog timer
                    try {
                        timeout.stop();
                    } catch (Exception e) {
                    }
                    /*
                    if (rcvBuf.contains("stdin:")) {
                        String msg[] = {"Interpreter error detected!", rcvBuf, "Click OK to continue."};
                        JOptionPane.showMessageDialog(null, msg);
                    }
                     */
                    rcvBuf = "";
                    if (j < sendBuf.size() - 1) {
                        if (timer.isRunning() || sendPending) {
                            // waiting
                        } else {
                            inc_j();
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
                if (rcvBuf.contains("powered by Lua 5.")) {
                    StopSend();
                    String msg[] = {"ESP module reboot detected!", "Event: internal NodeMCU exception or power fail.", "Please, try again."};
                    JOptionPane.showMessageDialog(null, msg);
                }
                if (rcvBuf.contains("Type \"help()")) {
                    StopSend();
                    String msg[] = {"ESP module reboot detected!", "Event: internal MicroPython exception or power fail.", "Please, try again."};
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

        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                String data = "";
                try {
                    data = serialPort.readString(event.getEventValue());
                } catch (SerialPortException ex) {
                    LOGGER.info(ex.toString());
                }
                rcvBuf = rcvBuf + data;
                String l = data.replace("\r", "<CR>");
                l = l.replace("\n", "<LF>");
                l = l.replace("`", "<OK>");
                LOGGER.info("recv:" + l);
                thandler.add(data);
                if (rcvBuf.contains("> ")) {
                    try {
                        timeout.stop(); // first, reset watchdog timer
                    } catch (Exception e) {
                    }
                    rcvBuf = "";
                    if (j < sendBuf.size() - 1) {
                        if (timer.isRunning() || sendPending) {
                            // waiting
                        } else {
                            inc_j();
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

        public void serialEvent(SerialPortEvent event) {
            String data, crc_parsed;
            boolean gotProperAnswer = false;
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    data = serialPort.readString(event.getEventValue());
                    rcvBuf = rcvBuf + data;
                    rx_data = rx_data + data;
                    //log("rcv:"+data);
                } catch (Exception e) {
                    data = "";
                    LOGGER.info(e.toString());
                }
                if (rcvBuf.contains("> ") && j < sendBuf.size()) {
                    //log("got intepreter answer, j="+Integer.toString(j));
                    rcvBuf = "";
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
                        timeout.restart();
                    } catch (Exception e) {
                        LOGGER.info(e.toString());
                    }
                    Progress.ins.setValue(j * 100 / (sendBuf.size() + sendPackets.size() - 1));
                    if (j < (sendBuf.size() + sendPackets.size())) {
                        if (timer.isRunning() || sendPending) {
                            //
                        } else {
                            inc_j();
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
                        timeout.stop();
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
                        serialPort.removeEventListener();
                    } catch (Exception e) {
                        LOGGER.info(e.toString());
                    }
                    try {
                        serialPort.addEventListener(new PortReader(), portMask);
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

    private class PortPyFilesReader implements SerialPortEventListener {

        public void serialEvent(SerialPortEvent event) {
            String data;
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    data = serialPort.readString(event.getEventValue());
                    rcvBuf = rcvBuf + data;
                    rx_data = rx_data + data;
                    thandler.add(data);
                } catch (Exception e) {
                    LOGGER.info(e.toString());
                }
                if (rx_data.contains("']\r\n>>>")) {
                    try {
                        timeout.stop();
                    } catch (Exception e) {
                        LOGGER.info(e.toString());
                    }
                    LOGGER.info("FileManager: File list found! Do parsing...");
                    try {
                        int start = rx_data.indexOf("[");
                        rx_data = rx_data.substring(start + 1, rx_data.indexOf("]"));
                        rx_data = rx_data.replace("'", "");
                        s = rx_data.split(", ");
                        Arrays.sort(s);
                        thandler.comment("----------------------------", false);
                        for (String subs : s) {
                            thandler.comment(subs, false);
                            if (subs.trim().length() > 0) {
                                AddPyFileButton(subs);
                                LOGGER.info("FileManager found file " + subs);
                            }
                        }
                        if (PyFileAsButton.size() == 0) {
                            thandler.comment("No files found.", false);
                        }
                        thandler.comment("----------------------------", true);
                        PyFileMgrPane.invalidate();
                        PyFileMgrPane.doLayout();
                        PyFileMgrPane.repaint();
                        PyFileMgrPane.requestFocusInWindow();
                        LOGGER.info("pyFileManager: File list parsing done, found " + PyFileAsButton.size() + " file(s).");
                    } catch (Exception e) {
                        LOGGER.info(e.toString());
                    }
                    try {
                        serialPort.removeEventListener();
                        serialPort.addEventListener(new PortReader(), portMask);
                    } catch (Exception e) {
                        LOGGER.info(e.toString());
                    }
//                    SendUnLock();
                }
            } else if (event.isCTS()) {
                UpdateLedCTS();
            } else if (event.isERR()) {
                LOGGER.info("FileManager: Unknown serial port error received.");
            }
        } // serialEvent
    } // PortPyFilesReader
} // EspIDE
