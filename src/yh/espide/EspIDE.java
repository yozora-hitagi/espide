
package yh.espide;

import jssc.*;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import static yh.espide.Regedit.*;

public class EspIDE extends javax.swing.JFrame {

    public static SerialPort serialPort;
    public static ArrayList<String> sendBuf;
    public static boolean pasteMode = true; // for MicroPython only

    boolean portJustOpen;

    private static pyFiler pyFiler = new pyFiler();

    private TerminalHandler thandler = new TerminalHandler();

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


        ContextMenuESPFileLUA = new javax.swing.JPopupMenu();
        MenuItemESPFileDo = new javax.swing.JMenuItem();
        TerminalSeparator3 = new javax.swing.JPopupMenu.Separator();
        MenuItemESPFileDelete = new javax.swing.JMenuItem();


        HorizontSplit = new javax.swing.JSplitPane();
        LeftBasePane = new javax.swing.JLayeredPane();

        NodeMCU = new javax.swing.JPanel();

        SriptsTab = new javax.swing.JLayeredPane();
        FilesToolBar = new javax.swing.JToolBar();
        FilesTabbedPane = new javax.swing.JTabbedPane();
        FileLayeredPane = new javax.swing.JLayeredPane();
        TextScroll = new org.fife.ui.rtextarea.RTextScrollPane();
        TextEditor = new org.fife.ui.rsyntaxtextarea.RSyntaxTextArea();

        Busy = new javax.swing.JLabel();
        FilePathLabel = new javax.swing.JLabel();
        ProgressBar = new javax.swing.JProgressBar();
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
        RightBottomPane = new javax.swing.JLayeredPane();
        Command = new javax.swing.JComboBox();
        RightFilesSplitPane = new javax.swing.JSplitPane();

        TerminalPane = new org.fife.ui.rtextarea.RTextScrollPane();
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


        SendCommand = new javax.swing.JButton();


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

        MenuItemTerminalClear.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemTerminalClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/terminal_clear.png"))); // NOI18N
        MenuItemTerminalClear.setText("Clear");
        MenuItemTerminalClear.setToolTipText("");
        MenuItemTerminalClear.addActionListener(evt -> thandler.getRSyntaxTextArea().setText(""));
        ContextMenuTerminal.add(MenuItemTerminalClear);

        MenuItemTerminalCopy.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemTerminalCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/copy.png"))); // NOI18N
        MenuItemTerminalCopy.setText("Copy");
        MenuItemTerminalCopy.setToolTipText("Copy selected text to system clipboard");
        MenuItemTerminalCopy.setEnabled(false);
        MenuItemTerminalCopy.addActionListener(evt -> thandler.getRSyntaxTextArea().copy());
        ContextMenuTerminal.add(MenuItemTerminalCopy);


        MenuItemESPFileDo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/play.png"))); // NOI18N
        MenuItemESPFileDo.setText("Do file");
        MenuItemESPFileDo.setToolTipText("");
        ContextMenuESPFileLUA.add(MenuItemESPFileDo);
        ContextMenuESPFileLUA.add(TerminalSeparator3);

        MenuItemESPFileDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/file_remove.png"))); // NOI18N
        MenuItemESPFileDelete.setText("Delete file");
        MenuItemESPFileDelete.setToolTipText("");
        ContextMenuESPFileLUA.add(MenuItemESPFileDelete);


        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
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

        LeftBasePane.setOpaque(true);


        NodeMCU.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        NodeMCU.setMinimumSize(new java.awt.Dimension(100, 100));
        NodeMCU.setPreferredSize(new java.awt.Dimension(461, 537));
        NodeMCU.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                NodeMCUComponentShown(evt);
            }
        });


        SriptsTab.setToolTipText("");
        SriptsTab.setMinimumSize(new java.awt.Dimension(460, 350));
        SriptsTab.setOpaque(true);

        FilesToolBar.setFloatable(false);
        FilesToolBar.setRollover(true);
        FilesToolBar.setAlignmentY(0.5F);
        FilesToolBar.setMaximumSize(new java.awt.Dimension(1000, 40));
        FilesToolBar.setMinimumSize(new java.awt.Dimension(321, 40));
        FilesToolBar.setPreferredSize(new java.awt.Dimension(321, 40));


        ButtonFileNew = new EditButton("新建", "/resources/document.png", "New file");
        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, MenuItemFileNew, org.jdesktop.beansbinding.ELProperty.create("${enabled}"), ButtonFileNew, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);
        ButtonFileNew.addActionListener(evt -> MenuItemFileNew.doClick());
        FilesToolBar.add(ButtonFileNew);

        ButtonFileOpen = new EditButton("打开", "/resources/folder open.png", "Open file from disk");
        ButtonFileOpen.addActionListener(evt -> MenuItemFileOpen.doClick());
        FilesToolBar.add(ButtonFileOpen);

        ButtonFileReload = new EditButton("刷新", "/resources/refresh.png", "Reload file from disk (for use with external editor)");
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, MenuItemFileReload, org.jdesktop.beansbinding.ELProperty.create("${enabled}"), ButtonFileReload, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);
        ButtonFileReload.addActionListener(evt -> MenuItemFileReload.doClick());
        FilesToolBar.add(ButtonFileReload);

        ButtonFileSave = new EditButton("保存", "/resources/save.png", "Save file to disk");
        ButtonFileSave.addActionListener(evt -> MenuItemFileSave.doClick());
        FilesToolBar.add(ButtonFileSave);

        ButtonFileClose = new EditButton("关闭", "/resources/folder closed.png", "Close file");
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, MenuItemFileClose, org.jdesktop.beansbinding.ELProperty.create("${enabled}"), ButtonFileClose, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);
        ButtonFileClose.addActionListener(evt -> MenuItemFileClose.doClick());
        FilesToolBar.add(ButtonFileClose);
        FilesToolBar.add(new JSeparator());

        ButtonUndo = new EditButton("撤销", "/resources/undo1.png", "Undo last action");
        ButtonUndo.setEnabled(false);
        ButtonUndo.setFocusable(false);
        ButtonUndo.addActionListener(evt -> MenuItemEditUndo.doClick());
        FilesToolBar.add(ButtonUndo);

        ButtonRedo = new EditButton("重做", "/resources/redo1.png", "Redo last action");
        ButtonRedo.setEnabled(false);
        ButtonRedo.setFocusable(false);
        ButtonRedo.addActionListener(evt -> MenuItemEditRedo.doClick());
        FilesToolBar.add(ButtonRedo);
        FilesToolBar.add(new JSeparator());

        ButtonCut = new EditButton("剪切", "/resources/cut.png", "Cut");
        ButtonCut.setEnabled(false);
        ButtonCut.addActionListener(evt -> MenuItemEditCut.doClick());
        FilesToolBar.add(ButtonCut);

        ButtonCopy = new EditButton("拷贝", "/resources/copy.png", "Copy");
        ButtonCopy.setEnabled(false);
        ButtonCopy.addActionListener(evt -> MenuItemEditCopy.doClick());
        FilesToolBar.add(ButtonCopy);

        ButtonPaste = new EditButton("粘贴", "/resources/paste.png", "Paste");
        ButtonPaste.setEnabled(false);
        ButtonPaste.addActionListener(evt -> MenuItemEditPaste.doClick());
        FilesToolBar.add(ButtonPaste);
        FilesToolBar.add(new JSeparator());

        ButtonSendSelected = new EditButton("Block", "/resources/send_selected.png", "Send selected block to ESP");
        ButtonSendSelected.addActionListener(evt -> MenuItemEditSendSelected.doClick());
        FilesToolBar.add(ButtonSendSelected);

        ButtonSendLine = new EditButton("Line", "/resources/run_line.png", "Send current line to ESP");
        ButtonSendLine.setFocusable(false);
        ButtonSendLine.addActionListener(evt -> MenuItemEditSendLine.doClick());
        FilesToolBar.add(ButtonSendLine);

        FilesTabbedPane.setOpaque(true);
        FilesTabbedPane.addChangeListener(evt -> FileLabelUpdate());

        TextScroll.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        TextScroll.setViewportBorder(javax.swing.BorderFactory.createEtchedBorder());
        TextScroll.setFoldIndicatorEnabled(true);
        TextScroll.setLineNumbersEnabled(true);
        TextScroll.setViewportView(TextEditor);

        TextEditor.setColumns(20);
        TextEditor.setRows(5);
        TextEditor.setTabSize(4);
        TextEditor.setCodeFoldingEnabled(true);
        TextEditor.setDragEnabled(false);
        TextEditor.setFadeCurrentLineHighlight(true);
        TextEditor.setPaintMarkOccurrencesBorder(true);
        TextEditor.setPaintMatchedBracketPair(true);
        TextEditor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LUA);
        TextEditor.addCaretListener(evt -> TextEditorCaretUpdate(evt));

        TextEditor.addActiveLineRangeListener(evt -> UpdateEditorButtons());
        TextEditor.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                TextEditorCaretPositionChanged(evt);
            }

            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        TextEditor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextEditorKeyTyped(evt);
            }
        });
        TextScroll.setViewportView(TextEditor);

        FileLayeredPane.setLayer(TextScroll, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout FileLayeredPaneLayout = new javax.swing.GroupLayout(FileLayeredPane);
        FileLayeredPane.setLayout(FileLayeredPaneLayout);
        FileLayeredPaneLayout.setHorizontalGroup(
                FileLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(TextScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE)
        );
        FileLayeredPaneLayout.setVerticalGroup(
                FileLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(TextScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
        );

        FilesTabbedPane.addTab("NewFile", FileLayeredPane);


        Busy.setBackground(new java.awt.Color(0, 153, 0));
        Busy.setForeground(new java.awt.Color(255, 255, 255));
        Busy.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Busy.setIcon(LED.GREY); // NOI18N
        Busy.setText("IDLE");
        Busy.setOpaque(true);

        ProgressBar.setToolTipText("");
        ProgressBar.setOpaque(true);
        ProgressBar.setStringPainted(true);

        LeftMainButtons.setOpaque(true);
        LeftMainButtons.setLayout(new java.awt.FlowLayout());

        FileSaveESP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/move.png"))); // NOI18N
        FileSaveESP.setText("<html><u>S</u>ave to ESP");
        FileSaveESP.setToolTipText("Send file to ESP and save into flash memory");
        FileSaveESP.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        FileSaveESP.setIconTextGap(8);
        FileSaveESP.setMargin(new java.awt.Insets(2, 2, 2, 2));
        FileSaveESP.setMaximumSize(new java.awt.Dimension(127, 30));
        FileSaveESP.setMinimumSize(new java.awt.Dimension(127, 30));
        FileSaveESP.setPreferredSize(new java.awt.Dimension(127, 30));
        FileSaveESP.addActionListener(evt -> FileSaveESPActionPerformed(evt));
        LeftMainButtons.add(FileSaveESP);

        FileSendESP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/play.png"))); // NOI18N
        FileSendESP.setText("<html>S<u>e</u>nd to ESP");
        FileSendESP.setToolTipText("Send file to ESP and run  \"line by line\"");
        FileSendESP.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        FileSendESP.setIconTextGap(8);
        FileSendESP.setMargin(new java.awt.Insets(2, 2, 2, 2));
        FileSendESP.setMaximumSize(new java.awt.Dimension(127, 30));
        FileSendESP.setMinimumSize(new java.awt.Dimension(127, 30));
        FileSendESP.setPreferredSize(new java.awt.Dimension(127, 30));
        FileSendESP.addActionListener(evt -> FileSendESPActionPerformed(evt));
        LeftMainButtons.add(FileSendESP);


        FilesUpload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/uploadLUA.png"))); // NOI18N
        FilesUpload.setText("Upload ...");
        FilesUpload.setToolTipText("Upload file from disk to ESP flash memory");
        FilesUpload.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        FilesUpload.setIconTextGap(8);
        FilesUpload.setMargin(new java.awt.Insets(2, 2, 2, 2));
        FilesUpload.setMaximumSize(new java.awt.Dimension(127, 30));
        FilesUpload.setMinimumSize(new java.awt.Dimension(127, 30));
        FilesUpload.setPreferredSize(new java.awt.Dimension(127, 30));
        FilesUpload.addActionListener(evt -> FilesUploadActionPerformed(evt));
        LeftMainButtons.add(FilesUpload);

        SriptsTab.setLayer(FilesToolBar, javax.swing.JLayeredPane.PALETTE_LAYER);
        SriptsTab.setLayer(FilesTabbedPane, javax.swing.JLayeredPane.DEFAULT_LAYER);

        SriptsTab.setLayer(Busy, javax.swing.JLayeredPane.DEFAULT_LAYER);
        SriptsTab.setLayer(FilePathLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        SriptsTab.setLayer(ProgressBar, javax.swing.JLayeredPane.DEFAULT_LAYER);
        SriptsTab.setLayer(LeftMainButtons, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout SriptsTabLayout = new javax.swing.GroupLayout(SriptsTab);
        SriptsTab.setLayout(SriptsTabLayout);
        SriptsTabLayout.setHorizontalGroup(
                SriptsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(FilesTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(FilesToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(SriptsTabLayout.createSequentialGroup()
                                .addGroup(SriptsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(ProgressBar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, SriptsTabLayout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addComponent(Busy, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(FilePathLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))

                                        .addComponent(LeftMainButtons, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 531, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        SriptsTabLayout.setVerticalGroup(
                SriptsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(SriptsTabLayout.createSequentialGroup()
                                .addComponent(FilesToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(FilesTabbedPane)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(SriptsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(Busy)
                                        .addComponent(FilePathLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)

                                .addComponent(LeftMainButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );


        SriptsTab.getAccessibleContext().setAccessibleName("Files");


        javax.swing.GroupLayout NodeMCULayout = new javax.swing.GroupLayout(NodeMCU);
        NodeMCU.setLayout(NodeMCULayout);
        NodeMCULayout.setHorizontalGroup(
                NodeMCULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(SriptsTab, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
        );
        NodeMCULayout.setVerticalGroup(
                NodeMCULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(SriptsTab, javax.swing.GroupLayout.DEFAULT_SIZE, 712, Short.MAX_VALUE)
        );


        LeftBasePane.setLayer(NodeMCU, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout LeftBasePaneLayout = new javax.swing.GroupLayout(LeftBasePane);
        LeftBasePane.setLayout(LeftBasePaneLayout);
        LeftBasePaneLayout.setHorizontalGroup(
                LeftBasePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(NodeMCU, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
        );
        LeftBasePaneLayout.setVerticalGroup(
                LeftBasePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(NodeMCU, javax.swing.GroupLayout.DEFAULT_SIZE, 744, Short.MAX_VALUE)
        );


        HorizontSplit.setLeftComponent(LeftBasePane);

        RightBasePane.setOpaque(true);

        LEDPanel.setMaximumSize(new java.awt.Dimension(392, 25));
        LEDPanel.setMinimumSize(new java.awt.Dimension(392, 25));
        LEDPanel.setOpaque(true);

        PortOpenLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PortOpenLabel.setIcon(LED.GREY); // NOI18N
        PortOpenLabel.setText("Open");
        PortOpenLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        PortOpenLabel.setMaximumSize(new java.awt.Dimension(50, 25));
        PortOpenLabel.setMinimumSize(new java.awt.Dimension(50, 25));
        PortOpenLabel.setPreferredSize(new java.awt.Dimension(50, 25));
        PortOpenLabel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        PortCTS.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PortCTS.setIcon(LED.GREY); // NOI18N
        PortCTS.setText("CTS");
        PortCTS.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        PortCTS.setMaximumSize(new java.awt.Dimension(50, 25));
        PortCTS.setMinimumSize(new java.awt.Dimension(50, 25));
        PortCTS.setPreferredSize(new java.awt.Dimension(50, 25));
        PortCTS.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        PortDTR.setIcon(LED.GREY); // NOI18N
        PortDTR.setText("DTR");
        PortDTR.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        PortDTR.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        PortDTR.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        PortDTR.addActionListener(evt -> PortDTRActionPerformed(evt));

        PortRTS.setIcon(LED.GREY); // NOI18N
        PortRTS.setText("RTS");
        PortRTS.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        PortRTS.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        PortRTS.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        PortRTS.addActionListener(evt -> PortRTSActionPerformed(evt));

        Open.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        Open.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/connect1.png"))); // NOI18N
        Open.setText("Open");
        Open.setToolTipText("Open/Close selected serial port");
        Open.setIconTextGap(2);
        Open.setMargin(new java.awt.Insets(1, 1, 1, 1));
        Open.setMaximumSize(new java.awt.Dimension(100, 25));
        Open.setMinimumSize(new java.awt.Dimension(85, 25));
        Open.setPreferredSize(new java.awt.Dimension(80, 25));
        Open.addActionListener(evt -> OpenActionPerformed(evt));

        Speed.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        Speed.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"1200", "2400", "4800", "9600", "19200", "38400", "57600", "74880", "115200", "230400", "460800", "921600"}));
        Speed.setToolTipText("Select baud rate");
        Speed.setMaximumSize(new java.awt.Dimension(80, 25));
        Speed.setMinimumSize(new java.awt.Dimension(80, 25));
        Speed.setPreferredSize(new java.awt.Dimension(80, 25));

        ReScan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/refresh3.png"))); // NOI18N
        ReScan.setMaximumSize(new java.awt.Dimension(25, 25));
        ReScan.setMinimumSize(new java.awt.Dimension(25, 25));
        ReScan.setPreferredSize(new java.awt.Dimension(25, 25));
        ReScan.addActionListener(evt -> PortFinder());

        AutoScroll.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        AutoScroll.setSelected(true);
        AutoScroll.setText("AutoScroll");
        AutoScroll.setToolTipText("terminalArea AutoScroll Enable/Disable");
        AutoScroll.setMinimumSize(new java.awt.Dimension(70, 25));
        AutoScroll.setPreferredSize(new java.awt.Dimension(60, 25));
        AutoScroll.addActionListener(evt -> AutoScrollActionPerformed(evt));

        Port.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        Port.setMaximumRowCount(20);
        Port.setModel(new javax.swing.DefaultComboBoxModel(new String[]{}));
        Port.setToolTipText("Serial port chooser");
        Port.setMaximumSize(new java.awt.Dimension(150, 25));
        Port.setMinimumSize(new java.awt.Dimension(150, 25));
        Port.setPreferredSize(new java.awt.Dimension(150, 25));

        Port.setEditable(true);

        EOL.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        EOL.setText("EOL");
        EOL.setToolTipText("EOL visible Enable/Disable");
        EOL.setMinimumSize(new java.awt.Dimension(70, 25));
        EOL.setPreferredSize(new java.awt.Dimension(60, 25));
        EOL.addItemListener(evt -> EOLItemStateChanged(evt));

        CR.setFont(CR.getFont().deriveFont(CR.getFont().getSize() - 4f));
        CR.setSelected(true);
        CR.setText("CR");
        CR.setToolTipText("Add CR at end of line");
        CR.setAlignmentY(0.0F);
        CR.setEnabled(false);
        CR.setIconTextGap(0);
        CR.setMargin(new java.awt.Insets(0, 0, 0, 0));
        CR.setName(""); // NOI18N
//        CR.setNextFocusableComponent(Command);

        LF.setFont(LF.getFont().deriveFont(LF.getFont().getSize() - 4f));
        LF.setSelected(true);
        LF.setText("LF");
        LF.setToolTipText("Add LF at end of line");
        LF.setAlignmentY(0.0F);
        LF.setEnabled(false);
        LF.setIconTextGap(0);
        LF.setMargin(new java.awt.Insets(0, 0, 0, 0));


        LEDPanel.setLayer(PortOpenLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(PortCTS, javax.swing.JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(PortDTR, javax.swing.JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(PortRTS, javax.swing.JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(Open, javax.swing.JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(Speed, javax.swing.JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(ReScan, javax.swing.JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(AutoScroll, javax.swing.JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(Port, javax.swing.JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(EOL, javax.swing.JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(CR, javax.swing.JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(LF, javax.swing.JLayeredPane.DEFAULT_LAYER);


        javax.swing.GroupLayout LEDPanelLayout = new javax.swing.GroupLayout(LEDPanel);
        LEDPanel.setLayout(LEDPanelLayout);
        LEDPanelLayout.setHorizontalGroup(
                LEDPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(LEDPanelLayout.createSequentialGroup()
                                .addGroup(LEDPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(LEDPanelLayout.createSequentialGroup()
                                                .addGroup(LEDPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(LEDPanelLayout.createSequentialGroup()
                                                                .addComponent(PortDTR)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(PortRTS))
                                                        .addGroup(LEDPanelLayout.createSequentialGroup()
                                                                .addComponent(PortOpenLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(PortCTS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(Open, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(LEDPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(LEDPanelLayout.createSequentialGroup()
                                                                .addComponent(ReScan, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(LEDPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(EOL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(AutoScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(LEDPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(LEDPanelLayout.createSequentialGroup()
                                                                                .addComponent(LF))
                                                                        .addGroup(LEDPanelLayout.createSequentialGroup()
                                                                                .addComponent(CR))))
                                                        .addGroup(LEDPanelLayout.createSequentialGroup()
                                                                .addComponent(Speed, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addComponent(Port, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        LEDPanelLayout.setVerticalGroup(
                LEDPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(LEDPanelLayout.createSequentialGroup()
                                .addComponent(Port, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(LEDPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LEDPanelLayout.createSequentialGroup()
                                                .addGroup(LEDPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(ReScan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(LEDPanelLayout.createSequentialGroup()
                                                                .addGroup(LEDPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(AutoScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 19, Short.MAX_VALUE)
                                                                        .addComponent(CR)
                                                                )
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(LEDPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(EOL, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(LF))))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(LEDPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(Speed, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                ))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LEDPanelLayout.createSequentialGroup()
                                                .addGroup(LEDPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(PortOpenLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(PortCTS, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(LEDPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(PortDTR)
                                                        .addComponent(PortRTS)))
                                        .addComponent(Open, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        Port.getAccessibleContext().setAccessibleName("");

        RightBottomPane.setAlignmentX(0.0F);
        RightBottomPane.setAlignmentY(0.0F);
        RightBottomPane.setOpaque(true);

        Command.setEditable(true);
        Command.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        Command.setMaximumRowCount(20);
        Command.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"AT", "AT+GMR", "AT+RST", ""}));
        Command.setToolTipText("Command to send");
        Command.setAlignmentX(0.0F);
        Command.setAlignmentY(0.0F);
        Command.setAutoscrolls(true);
        Command.setEnabled(false);
        Command.setName("Command"); // NOI18N
        Command.addActionListener(evt -> CommandActionPerformed(evt));


        RightBottomPane.setLayer(Command, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout RightBottomPaneLayout = new javax.swing.GroupLayout(RightBottomPane);
        RightBottomPane.setLayout(RightBottomPaneLayout);
        RightBottomPaneLayout.setHorizontalGroup(
                RightBottomPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Command, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        RightBottomPaneLayout.setVerticalGroup(
                RightBottomPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(Command, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        Command.getAccessibleContext().setAccessibleName("Command");


        RightFilesSplitPane.setDividerLocation(300);
        RightFilesSplitPane.setAutoscrolls(true);
        RightFilesSplitPane.addPropertyChangeListener(evt -> RightFilesSplitPanePropertyChange(evt));


        TerminalPane.setToolTipText("terminalArea window");
        TerminalPane.setMaximumSize(new java.awt.Dimension(100, 100));
        TerminalPane.setMinimumSize(new java.awt.Dimension(100, 100));
        TerminalPane.setName(""); // NOI18N
        TerminalPane.setPreferredSize(new java.awt.Dimension(100, 100));

        thandler.getRSyntaxTextArea().setPopupMenu(ContextMenuTerminal);
        TerminalPane.setViewportView(thandler.getRSyntaxTextArea());
        thandler.getRSyntaxTextArea().getAccessibleContext().setAccessibleParent(TerminalPane);


        RightFilesSplitPane.setLeftComponent(TerminalPane);

        FileManagerScrollPane.setBorder(null);
        FileManagerScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        FileManagersLayer.setMaximumSize(new java.awt.Dimension(145, 145));


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
        NodeFileMgrPane.setName(""); // NOI18N
        NodeFileMgrPane.setPreferredSize(new java.awt.Dimension(145, 145));
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 2, 2);
        flowLayout1.setAlignOnBaseline(true);
        NodeFileMgrPane.setLayout(flowLayout1);

        FileFormat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/file manager (delete).png"))); // NOI18N
        FileFormat.setText("Format");
        FileFormat.setToolTipText("Format (erase) NodeMCU file system. All files will be removed!");
        FileFormat.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        FileFormat.setMargin(new java.awt.Insets(2, 4, 2, 4));
        FileFormat.setMaximumSize(new java.awt.Dimension(130, 25));
        FileFormat.setMinimumSize(new java.awt.Dimension(130, 25));
        FileFormat.setPreferredSize(new java.awt.Dimension(130, 25));
        FileFormat.addActionListener(evt -> MenuItemESPFormatActionPerformed(evt));
        NodeFileMgrPane.add(FileFormat);

        FileSystemInfo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/file manager.png"))); // NOI18N
        FileSystemInfo.setText("FS Info");
        FileSystemInfo.setToolTipText("Execute command file.fsinfo() and show total, used and remainig space on the ESP filesystem");
        FileSystemInfo.setAlignmentX(0.5F);
        FileSystemInfo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        FileSystemInfo.setMargin(new java.awt.Insets(2, 2, 2, 2));
        FileSystemInfo.setMaximumSize(new java.awt.Dimension(130, 25));
        FileSystemInfo.setPreferredSize(new java.awt.Dimension(130, 25));
        FileSystemInfo.addActionListener(evt -> NodeFileSystemInfo());
        NodeFileMgrPane.add(FileSystemInfo);

        FileListReload.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        FileListReload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/refresh3.png"))); // NOI18N
        FileListReload.setText("Reload");
        FileListReload.setAlignmentX(0.5F);
        FileListReload.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        FileListReload.setMargin(new java.awt.Insets(2, 2, 2, 2));
        FileListReload.setMaximumSize(new java.awt.Dimension(130, 25));
        FileListReload.setPreferredSize(new java.awt.Dimension(130, 25));
        FileListReload.addActionListener(evt -> FileListReloadActionPerformed(evt));
        NodeFileMgrPane.add(FileListReload);

        FileRenamePanel.setMaximumSize(new java.awt.Dimension(130, 45));
        FileRenamePanel.setMinimumSize(new java.awt.Dimension(130, 45));

        FileRenameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
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

        FileRenamePanel.setLayer(FileRenameLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        FileRenamePanel.setLayer(FileRename, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout FileRenamePanelLayout = new javax.swing.GroupLayout(FileRenamePanel);
        FileRenamePanel.setLayout(FileRenamePanelLayout);
        FileRenamePanelLayout.setHorizontalGroup(
                FileRenamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(FileRenamePanelLayout.createSequentialGroup()
                                .addGroup(FileRenamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(FileRename, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(FileRenameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        FileRenamePanelLayout.setVerticalGroup(
                FileRenamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(FileRenamePanelLayout.createSequentialGroup()
                                .addComponent(FileRenameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(FileRename, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        NodeFileMgrPane.add(FileRenamePanel);

        PyFileMgrPane.setMaximumSize(new java.awt.Dimension(500, 155));
        PyFileMgrPane.setMinimumSize(new java.awt.Dimension(55, 55));
        PyFileMgrPane.setPreferredSize(new java.awt.Dimension(155, 155));
        PyFileMgrPane.setLayout(new java.awt.FlowLayout());

        PyListDir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/refresh3.png"))); // NOI18N
        PyListDir.setText("ListDir /");
        PyListDir.setToolTipText("Execute command listdir() and show files");
        PyListDir.setAlignmentX(0.5F);
        PyListDir.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        PyListDir.setMargin(new java.awt.Insets(2, 2, 2, 2));
        PyListDir.setMaximumSize(new java.awt.Dimension(130, 25));
        PyListDir.setPreferredSize(new java.awt.Dimension(130, 25));
        PyListDir.addActionListener(evt -> PyListDirActionPerformed(evt));
        PyFileMgrPane.add(PyListDir);


        FileManagersLayer.setLayer(firmware_type_label, JLayeredPane.DEFAULT_LAYER);
        FileManagersLayer.setLayer(NodeFileMgrPane, javax.swing.JLayeredPane.DEFAULT_LAYER);
        FileManagersLayer.setLayer(PyFileMgrPane, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout FileManagersLayerLayout = new javax.swing.GroupLayout(FileManagersLayer);
        FileManagersLayer.setLayout(FileManagersLayerLayout);
        FileManagersLayerLayout.setHorizontalGroup(
                FileManagersLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(FileManagersLayerLayout.createSequentialGroup()
                                .addGroup(FileManagersLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(firmware_type_label, javax.swing.GroupLayout.DEFAULT_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(PyFileMgrPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(NodeFileMgrPane, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 433, Short.MAX_VALUE))
        );
        FileManagersLayerLayout.setVerticalGroup(
                FileManagersLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(FileManagersLayerLayout.createSequentialGroup()
                                .addComponent(firmware_type_label, 30, 30, 30)
                                .addGap(6, 6, 6)
                                .addComponent(NodeFileMgrPane, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                                .addGap(6, 6, 6)
                                .addComponent(PyFileMgrPane, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE))
        );

        FileManagerScrollPane.setViewportView(FileManagersLayer);

        RightFilesSplitPane.setRightComponent(FileManagerScrollPane);

        SendCommand.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        SendCommand.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/left.png"))); // NOI18N
        SendCommand.setToolTipText("");
        SendCommand.setAlignmentY(0.0F);
        SendCommand.setEnabled(false);
        SendCommand.setText("Send");
        SendCommand.setMargin(new java.awt.Insets(0, 0, 0, 0));
        SendCommand.addActionListener(evt -> SendCommandActionPerformed(evt));

        RightBasePane.setLayer(LEDPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        RightBasePane.setLayer(RightBottomPane, javax.swing.JLayeredPane.DEFAULT_LAYER);
        RightBasePane.setLayer(RightFilesSplitPane, javax.swing.JLayeredPane.DEFAULT_LAYER);
        RightBasePane.setLayer(SendCommand, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout RightBasePaneLayout = new javax.swing.GroupLayout(RightBasePane);
        RightBasePane.setLayout(RightBasePaneLayout);
        RightBasePaneLayout.setHorizontalGroup(
                RightBasePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(RightBasePaneLayout.createSequentialGroup()
                                .addGroup(RightBasePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(RightFilesSplitPane)
                                        .addGroup(RightBasePaneLayout.createSequentialGroup()
                                                .addComponent(RightBottomPane)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(SendCommand, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(RightBasePaneLayout.createSequentialGroup()
                                                .addGroup(RightBasePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)

                                                        .addComponent(LEDPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        RightBasePaneLayout.setVerticalGroup(
                RightBasePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(RightBasePaneLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(LEDPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(RightFilesSplitPane)
                                .addGap(5, 5, 5)

                                .addGroup(RightBasePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(RightBottomPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(SendCommand, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        SendCommand.getAccessibleContext().setAccessibleName("");

        HorizontSplit.setRightComponent(RightBasePane);

        JMenuBar menu= new JMenuBar();

        JMenu MenuFile = Context.createM1("文件");

        JMenuItem settings = new JMenuItem("设置");
        settings.setIcon(new ImageIcon(getClass().getResource("/resources/settings2.png")));
        settings.addActionListener(evt -> {
            new SettingsFrame(this).setVisible(true);
        });
        MenuFile.add(settings);

        MenuItemFileNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemFileNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/document.png"))); // NOI18N
        MenuItemFileNew.setText("<html><u>N</u>ew");
        MenuItemFileNew.setToolTipText("File New");
        MenuItemFileNew.addActionListener(evt -> FileNew(""));
        MenuFile.add(MenuItemFileNew);

        MenuItemFileOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemFileOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/folder open.png"))); // NOI18N
        MenuItemFileOpen.setText("<html><u>O</u>pen from disk");
        MenuItemFileOpen.addActionListener(evt -> OpenFile());
        MenuFile.add(MenuItemFileOpen);

        MenuItemFileReload.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemFileReload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/refresh.png"))); // NOI18N
        MenuItemFileReload.setText("<html><u>R</u>eload from disk");
        MenuItemFileReload.setToolTipText("Reload file from disk, if you use external editor");
        MenuItemFileReload.setEnabled(false);
        MenuItemFileReload.addActionListener(evt -> ReloadFile());
        MenuFile.add(MenuItemFileReload);

        MenuItemFileSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemFileSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/save.png"))); // NOI18N
        MenuItemFileSave.setText("<html><u>S</u>ave to disk");
        MenuItemFileSave.addActionListener(evt -> MenuItemFileSaveActionPerformed(evt));
        MenuFile.add(MenuItemFileSave);


        MenuItemFileClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemFileClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/folder closed.png"))); // NOI18N
        MenuItemFileClose.setText("Close");
        MenuItemFileClose.addActionListener(evt -> MenuItemFileCloseActionPerformed(evt));
        MenuFile.add(MenuItemFileClose);
        MenuFile.add(new JPopupMenu.Separator());

        MenuItemFileSaveESP.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK));
        MenuItemFileSaveESP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/move.png"))); // NOI18N
        MenuItemFileSaveESP.setText("<html><u>S</u>ave to ESP");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, FileSaveESP, org.jdesktop.beansbinding.ELProperty.create("${enabled}"), MenuItemFileSaveESP, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        MenuItemFileSaveESP.addActionListener(evt -> MenuItemFileSaveESPActionPerformed(evt));
        MenuFile.add(MenuItemFileSaveESP);

        MenuItemFileSendESP.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.ALT_MASK));
        MenuItemFileSendESP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/script_send.png"))); // NOI18N
        MenuItemFileSendESP.setText("<html>S<u>e</u>nd to ESP");
        MenuItemFileSendESP.addActionListener(evt -> FileSendESP.doClick());
        MenuFile.add(MenuItemFileSendESP);


        MenuFile.add(new JPopupMenu.Separator());

        JMenuItem MenuItemFileExit = new JMenuItem();
        MenuItemFileExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        MenuItemFileExit.setText("退出");
        MenuItemFileExit.addActionListener(evt -> AppClose());
        MenuFile.add(MenuItemFileExit);

        menu.add(MenuFile);

        JMenu MenuEdit = Context.createM1("编辑");

        MenuItemEditUndo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemEditUndo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/undo1.png"))); // NOI18N
        MenuItemEditUndo.setText("Undo");
        MenuItemEditUndo.setEnabled(false);
        MenuItemEditUndo.addActionListener(evt -> MenuItemEditUndoActionPerformed(evt));
        MenuEdit.add(MenuItemEditUndo);

        MenuItemEditRedo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, 0));
        MenuItemEditRedo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/redo1.png"))); // NOI18N
        MenuItemEditRedo.setText("Redo");
        MenuItemEditRedo.setEnabled(false);
        MenuItemEditRedo.addActionListener(evt -> MenuItemEditRedoActionPerformed(evt));
        MenuEdit.add(MenuItemEditRedo);
        MenuEdit.add(new JPopupMenu.Separator());

        MenuItemEditCut.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemEditCut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/cut.png"))); // NOI18N
        MenuItemEditCut.setText("Cut");
        MenuItemEditCut.setEnabled(false);
        MenuItemEditCut.addActionListener(evt -> MenuItemEditCutActionPerformed(evt));
        MenuEdit.add(MenuItemEditCut);

        MenuItemEditCopy.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemEditCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/copy.png"))); // NOI18N
        MenuItemEditCopy.setText("Copy");
        MenuItemEditCopy.setEnabled(false);
        MenuItemEditCopy.addActionListener(evt -> MenuItemEditCopyActionPerformed(evt));
        MenuEdit.add(MenuItemEditCopy);

        MenuItemEditPaste.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemEditPaste.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/paste.png"))); // NOI18N
        MenuItemEditPaste.setText("Paste");
        MenuItemEditPaste.setToolTipText("");
        MenuItemEditPaste.setEnabled(false);
        MenuItemEditPaste.addActionListener(evt -> MenuItemEditPasteActionPerformed(evt));
        MenuEdit.add(MenuItemEditPaste);
        MenuEdit.add(new JPopupMenu.Separator());

        MenuItemEditSendSelected.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.ALT_MASK));
        MenuItemEditSendSelected.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/send_selected.png"))); // NOI18N
        MenuItemEditSendSelected.setText("<html>Send selected <u>B</u>lock to ESP");
        MenuItemEditSendSelected.setToolTipText("Send selected block to ESP");


        MenuItemEditSendSelected.addActionListener(evt -> MenuItemEditSendSelectedActionPerformed(evt));
        MenuEdit.add(MenuItemEditSendSelected);

        MenuItemEditSendLine.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.ALT_MASK));
        MenuItemEditSendLine.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/run_line.png"))); // NOI18N
        MenuItemEditSendLine.setText("<html>Send current <u>L</u>ine to ESP");
        MenuItemEditSendLine.setToolTipText("Send current line from code editor window to ESP");
        MenuItemEditSendLine.addActionListener(evt -> MenuItemEditSendLineActionPerformed(evt));
        MenuEdit.add(MenuItemEditSendLine);

        menu.add(MenuEdit);


        JMenu MenuView = Context.createM1("视图");

        AlwaysOnTop.setText("Always On Top");
        AlwaysOnTop.setToolTipText("");
        AlwaysOnTop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/AlwaysOnTop.png"))); // NOI18N
        AlwaysOnTop.addItemListener(evt -> AlwaysOnTopItemStateChanged(evt));
        MenuView.add(AlwaysOnTop);


        MenuItemViewClearTerminal.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemViewClearTerminal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/terminal_clear.png"))); // NOI18N
        MenuItemViewClearTerminal.setText("Clear terminal");
        MenuItemViewClearTerminal.setToolTipText("Clear terminal window");
        MenuItemViewClearTerminal.addActionListener(evt -> MenuItemTerminalClear.doClick());
        MenuView.add(MenuItemViewClearTerminal);
        MenuView.add(new JPopupMenu.Separator());

        MenuItemViewTerminalOnly.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, java.awt.event.InputEvent.ALT_MASK));
        MenuItemViewTerminalOnly.setText("Show terminalArea only (Left panel show/hide)");
        MenuItemViewTerminalOnly.setToolTipText("Enable/disable left panel");
        MenuItemViewTerminalOnly.addItemListener(evt -> MenuItemViewTerminalOnlyItemStateChanged(evt));
        MenuView.add(MenuItemViewTerminalOnly);

        MenuItemViewEditorOnly.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, java.awt.event.InputEvent.ALT_MASK));
        MenuItemViewEditorOnly.setText("Show Editor only (Right panel show/hide)");
        MenuItemViewEditorOnly.setToolTipText("Enable/disable left panel");
        MenuItemViewEditorOnly.addItemListener(evt -> MenuItemViewEditorOnlyItemStateChanged(evt));
        MenuView.add(MenuItemViewEditorOnly);
        MenuView.add(new JPopupMenu.Separator());

        MenuItemViewToolbar.setSelected(true);
        MenuItemViewToolbar.setText("Show toolbar at top left");
        MenuItemViewToolbar.setToolTipText("Enable/disable files toolbar at top left");
        MenuItemViewToolbar.addItemListener(evt -> MenuItemViewToolbarItemStateChanged(evt));
        MenuItemViewToolbar.addActionListener(evt -> MenuItemViewToolbarActionPerformed(evt));
        MenuView.add(MenuItemViewToolbar);


        MenuItemViewFileManager.setSelected(true);
        MenuItemViewFileManager.setText("Show FileManager panel at right");
        MenuItemViewFileManager.setToolTipText("Enable/disable FileManager panel at right");
        MenuItemViewFileManager.addItemListener(evt -> MenuItemViewFileManagerItemStateChanged(evt));
        MenuItemViewFileManager.addActionListener(evt -> MenuItemViewFileManagerActionPerformed(evt));
        MenuView.add(MenuItemViewFileManager);


        menu.add(MenuView);


        setJMenuBar(menu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(HorizontSplit, javax.swing.GroupLayout.DEFAULT_SIZE, 1024, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(HorizontSplit, javax.swing.GroupLayout.DEFAULT_SIZE, 746, Short.MAX_VALUE)
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
                Regedit.setString(Regedit.SERIAL_PORT, port);
                Regedit.setInt(Regedit.SERIAL_BAUD_RATE, getBaudRate());
            }
        } else {
            portClose();
        }
        UpdateButtons();
    }

    private void UpdateButtons() {
        if (Open.isSelected() && !portJustOpen) {
            Port.setEnabled(false);
            //Speed.setEnabled(false);
            ReScan.setEnabled(false);
            SendCommand.setEnabled(true);
            Command.setEnabled(true);
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
            SendCommand.setEnabled(false);
            Command.setEnabled(false);
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
        ProgressBar.setVisible(false);
        isToolbarShow();
        SetWindowSize();
        isFileManagerShow();
    }

    private void updateCommandsSet() {
        Command.removeAllItems();
        for (Object c : CommandsSet.get(FirmwareType.current)) {
            Command.addItem(c);
        }
    }


    private void SetWindowSize() {
        int x, y, h, w;
        x = Regedit.getInt(WIN_X, 0);
        y = Regedit.getInt(WIN_Y, 0);
        h = Regedit.getInt(WIN_H, 768);
        w = Regedit.getInt(WIN_W, 1024);
        this.setBounds(x, y, w, h);
    }

    private void isToolbarShow() {
        FilesToolBar.setVisible(MenuItemViewToolbar.isSelected());
    }


    private void isFileManagerShow() {
        int div;
        final int w = 160;
        if (MenuItemViewFileManager.isSelected()) {
            FileManagerScrollPane.setEnabled(true);
            FileManagerScrollPane.setVisible(true);
            //div = prefs.getInt( FM_DIV, RightFilesSplitPane.getWidth()-w );
            //if ( div > RightFilesSplitPane.getWidth()-w ) {
            div = RightFilesSplitPane.getWidth() - w;
            //}
            RightFilesSplitPane.setDividerLocation(div);
        } else {
            FileManagerScrollPane.setEnabled(false);
            FileManagerScrollPane.setVisible(false);
            RightFilesSplitPane.setDividerLocation(RightFilesSplitPane.getWidth() - RightFilesSplitPane.getDividerSize());
        }
    }


    private void SendCommandActionPerformed(java.awt.event.ActionEvent evt) {
        if (Config.ins.isCommand_echo()) {
            thandler.echo(Command.getSelectedItem().toString(), true);
        }

        if (!Open.isSelected() || portJustOpen) {
            LOGGER.info("Port not open, operation cancel.");
            return;
        }
        String cmd = Command.getSelectedItem().toString();
        //看着是删除重复的记录
        int count = Command.getItemCount();
        for (int i = 0; i < count; i++) {
            if (Command.getItemAt(i).equals(cmd)) {
                Command.removeItemAt(i);
                i--;
                count--;
            }
        }
        Command.insertItemAt(cmd, 0); // Add to History after last Position

        send(addCRLF(cmd), true);
        // History trim
        if (Command.getItemCount() > 20) {
            Command.removeItemAt(Command.getItemCount() - 1);
        }

        Command.setSelectedIndex(-1);
    }

    private void CommandActionPerformed(java.awt.event.ActionEvent evt) {
        //log("CommandActionPerformed " + evt.getActionCommand());
        if ("comboBoxEdited".equals(evt.getActionCommand())) { // Hit Enter
            SendCommand.doClick();
        }
    }


    private void ContextMenuTerminalPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
        try {
            MenuItemTerminalCopy.setEnabled(thandler.getRSyntaxTextArea().getSelectedText().length() > 0);
        } catch (Exception e) {
            MenuItemTerminalCopy.setEnabled(false);
        }
    }


    private void MenuItemFileSaveActionPerformed(java.awt.event.ActionEvent evt) {
        SaveFile();
        if (Config.ins.isFile_auto_save_esp() && !FileSaveESP.isSelected()) {
            FileSaveESP.doClick();
        }
    }

    boolean isFileNew() {
        try {
            if (FilesTabbedPane.getTitleAt(iTab).equals(NewFile)) {
                return true;
            }
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    boolean SaveFile() {
        boolean success = false;
        if (isFileNew()) { // we saving new file
            LOGGER.info("Saving new file...");
            FileCount++;
            String fileExt;
            if (FirmwareType.current.eq(FirmwareType.NodeMCU)) {
                fileExt = ".lua";
            } else { // MicroPython
                fileExt = ".py";
            }
            openedfiles.set(iTab, new File("script" + Integer.toString(FileCount) + fileExt));
            chooser.rescanCurrentDirectory();
            chooser.setSelectedFile(openedfiles.get(iTab));
            int returnVal = chooser.showSaveDialog(null);
            if (returnVal != JFileChooser.APPROVE_OPTION) {
                LOGGER.info("Saving abort by user.");
                UpdateEditorButtons();
                return false;
            }
            SavePath();
            openedfiles.set(iTab, chooser.getSelectedFile());
            if (openedfiles.get(iTab).exists()) {
                LOGGER.info("File " + openedfiles.get(iTab).getName() + " already exist, waiting user choice");
                int shouldWrite = Dialog("File " + openedfiles.get(iTab).getName() + " already exist. Overwrite?", JOptionPane.YES_NO_OPTION);
                if (shouldWrite != JOptionPane.YES_OPTION) {
                    UpdateEditorButtons();
                    return false;
                }
            }
        } else { // we saving file, when open
            LOGGER.info("We save known file " + openedfiles.get(iTab).getName());
        }
        try {
            LOGGER.info("Try to saving file " + openedfiles.get(iTab).getName() + " ...");
            fos = new FileOutputStream(openedfiles.get(iTab));
            osw = new OutputStreamWriter(fos, "UTF-8");
            bw = new BufferedWriter(osw);
            bw.write(TextEditorList.get(iTab).getText());
            bw.flush();
            osw.flush();
            fos.flush();
            String filename = openedfiles.get(iTab).getName();
            LOGGER.info("Save file " + filename + ": Success.");
            FilesTabbedPane.setTitleAt(iTab, filename);
            UpdateEditorButtons();
            success = true;
        } catch (IOException ex) {
            LOGGER.info("Save file " + openedfiles.get(iTab).getName() + ": FAIL.");
            LOGGER.info(ex.toString());
//            log(ex.getStackTrace().toString());
            JOptionPane.showMessageDialog(null, "Error, file not saved!");
        }
        try {
            if (bw != null) {
                bw.close();
            }
            if (osw != null) {
                osw.close();
            }
            if (fos != null) {
                fos.close();
            }
        } catch (IOException ex) {
            LOGGER.info(ex.toString());
//            log(ex.getStackTrace().toString());
        }
        TextEditorList.get(iTab).discardAllEdits();
        FileChanged.set(iTab, false);
        UpdateEditorButtons();
        return success;
    }

    private void FileNew(String s) {
        AddTab(NewFile, s);
        if (s.isEmpty()) {
            LOGGER.info("New empty file ready.");
        } else {
            LOGGER.info("New file ready, content load: Success.");
        }
    }

    private void MenuItemEditCutActionPerformed(java.awt.event.ActionEvent evt) {
        TextEditorList.get(iTab).cut();
        FileChanged.set(iTab, true);
    }

    private void MenuItemEditCopyActionPerformed(java.awt.event.ActionEvent evt) {
        TextEditorList.get(iTab).copy();
    }

    private void MenuItemEditPasteActionPerformed(java.awt.event.ActionEvent evt) {
        TextEditorList.get(iTab).paste();
        FileChanged.set(iTab, true);
    }

    private void SavePath() {
        workDir = chooser.getCurrentDirectory().toString();
        Regedit.setString(PATH, workDir);
    }

    private void OpenFile() {
        chooser.rescanCurrentDirectory();
        int success = chooser.showOpenDialog(LeftBasePane);
        if (success == JFileChooser.APPROVE_OPTION) {
            SavePath();
            int isOpen = -1;
            for (int i = 0; i < openedfiles.size(); i++) {
                if (chooser.getSelectedFile().getPath().equals(openedfiles.get(i).getPath())) {
                    iTab = i;
                    isOpen = i;
                    break;
                }
            }
            if (isOpen >= 0) {
                FilesTabbedPane.setSelectedIndex(iTab);
                UpdateEditorButtons();
                String filename = chooser.getSelectedFile().getName();
                LOGGER.info("File " + filename + " already open, select tab to file " + filename);
                JOptionPane.showMessageDialog(null, "File " + filename + " already open. You can use 'Reload' only.");
                return;
            }
            if (!isFileNew() || isChanged()) {
                AddTab(NewFile, "");
            }
            LOGGER.info("Try to open file " + chooser.getSelectedFile().getName());
            try {
                openedfiles.set(iTab, chooser.getSelectedFile());
                String filename = openedfiles.get(iTab).getName();
                LOGGER.info("File name: " + openedfiles.get(iTab).getPath());
                if (openedfiles.get(iTab).length() > 1024 * 1024) { // 1M
                    JOptionPane.showMessageDialog(null, "File " + filename + " too large.");
                    LOGGER.info("File too large. Size: " + Long.toString(openedfiles.get(iTab).length() / 1024 / 1024) + " Mb, file: " + openedfiles.get(iTab).getPath());
                    UpdateEditorButtons();
                    return;
                }
                FilesTabbedPane.setTitleAt(iTab, openedfiles.get(iTab).getName());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error, file is not open!");
                LOGGER.info(ex.toString());
                LOGGER.info("Open: FAIL.");
//                log(ex.getStackTrace().toString());
            }
            if (LoadFile()) {
                LOGGER.info("Open \"" + openedfiles.get(iTab).getName() + "\": Success.");
            }
        }
        UpdateEditorButtons();
    }

    private boolean LoadFile() {
        boolean success = false;
        if (isFileNew()) {
            UpdateEditorButtons();
            LOGGER.info("Internal error 101: FileTab is NewFile.");
            return false;
        }
        String filename = "";
        try {
            filename = openedfiles.get(iTab).getName();
            LOGGER.info("Try to load file " + filename);
        } catch (Exception e) {
            LOGGER.info("Internal error 102: no current file descriptor.");
            return false;
        }
        try {
            fis = new FileInputStream(openedfiles.get(iTab));
            isr = new InputStreamReader(fis, "UTF-8");
            br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
            TextEditorList.get(iTab).setText(sb.toString());
            success = true;
        } catch (Exception ex) {
            LOGGER.info(ex.toString());
//                log(ex.getStackTrace().toString());
            LOGGER.info("Loading " + filename + ": FAIL.");
            UpdateEditorButtons();
            JOptionPane.showMessageDialog(null, "Error, file not load!");
            return false;
        }
        try {
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        } catch (Exception ex) {
            LOGGER.info(ex.toString());
//                log(ex.getStackTrace().toString());
            LOGGER.info("Internal error 103: can't close stream.");
        }
        if (success) {
            TextEditorList.get(iTab).setCaretPosition(0);
            FileChanged.set(iTab, false);
            TextEditorList.get(iTab).discardAllEdits();
            UpdateEditorButtons();
            FileLabelUpdate();
            LOGGER.info("Loading " + filename + ": Success.");
        }
        return success;
    }


    private void CheckSelected() {
        if (TextEditorList.get(iTab).getSelectedText() == null) {
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

    private void UpdateEditorButtons() {
        iTab = FilesTabbedPane.getSelectedIndex();
        // isChanged
        if (isChanged()) {
            if (isFileNew()) {
                MenuItemFileSave.setEnabled(true);
                ButtonFileSave.setEnabled(true);
                MenuItemFileReload.setEnabled(false);
                ButtonFileReload.setEnabled(false);
            } else {
                MenuItemFileSave.setEnabled(true);
                ButtonFileSave.setEnabled(true);
                MenuItemFileReload.setEnabled(true);
                ButtonFileReload.setEnabled(true);
            }
        } else if (isFileNew()) {
            MenuItemFileSave.setEnabled(true);
            ButtonFileSave.setEnabled(true);
            MenuItemFileReload.setEnabled(false);
            ButtonFileReload.setEnabled(false);
        } else {
            MenuItemFileSave.setEnabled(false);
            ButtonFileSave.setEnabled(false);
            MenuItemFileReload.setEnabled(true);
            ButtonFileReload.setEnabled(true);
        }
        if (isFileNew() && (FilesTabbedPane.getTabCount() == 1)) {
            MenuItemFileClose.setEnabled(false);
            ButtonFileClose.setEnabled(false);
        } else {
            MenuItemFileClose.setEnabled(true);
            ButtonFileClose.setEnabled(true);
        }
        // CanUndo
        try {
            if (TextEditorList.isEmpty()) {
                return;
            }
        } catch (Exception e) {
            return;
        }
        if (TextEditorList.get(iTab).canUndo()) {
            MenuItemEditUndo.setEnabled(true);
            ButtonUndo.setEnabled(true);
        } else {
            MenuItemEditUndo.setEnabled(false);
            ButtonUndo.setEnabled(false);
        }
        // CanRedo
        if (TextEditorList.get(iTab).canRedo()) {
            MenuItemEditRedo.setEnabled(true);
            ButtonRedo.setEnabled(true);
        } else {
            MenuItemEditRedo.setEnabled(false);
            ButtonRedo.setEnabled(false);
        }
        CheckSelected();

        MenuItemFileNew.setEnabled(true);
        ButtonFileNew.setEnabled(true);

    }

    private void formFocusGained(java.awt.event.FocusEvent evt) {
        UpdateEditorButtons();
        UpdateButtons();
    }

    private void MenuItemFileCloseActionPerformed(java.awt.event.ActionEvent evt) {
        CloseFile();
    }

    private void MenuItemViewClearTerminalActionPerformed(java.awt.event.ActionEvent evt) {
        MenuItemTerminalClear.doClick();
    }


    private void AlwaysOnTopItemStateChanged(java.awt.event.ItemEvent evt) {
        this.setAlwaysOnTop(AlwaysOnTop.isSelected());
    }

    private void MenuItemEditUndoActionPerformed(java.awt.event.ActionEvent evt) {
        if (TextEditorList.get(iTab).canUndo()) {
            TextEditorList.get(iTab).undoLastAction();
        }
    }

    private void MenuItemEditRedoActionPerformed(java.awt.event.ActionEvent evt) {
        if (TextEditorList.get(iTab).canRedo()) {
            TextEditorList.get(iTab).redoLastAction();
        }
    }

    private void NodeListFiles() {
        if (portJustOpen) {
            LOGGER.info("ERROR: Communication with MCU not yet established.");
            return;
        }
        //String cmd = "print(\"~~~File \"..\"list START~~~\") for k,v in pairs(file.list()) do l = string.format(\"%-15s\",k) print(l..\" - \"..v..\" bytes\") end l=nil k=nil v=nil print(\"~~~File \"..\"list END~~~\")";
        String cmd = "_dir=function()\n"
                + "     local k,v,l\n"
                + "     print(\"~~~File \"..\"list START~~~\")\n"
                + "     for k,v in pairs(file.list()) do \n"
                + "          l = string.format(\"%-15s\",k) \n"
                + "          print(l..\" : \"..v..\" bytes\") \n"
                + "     end \n"
                + "     print(\"~~~File \"..\"list END~~~\")\n"
                + "end\n"
                + "_dir()\n"
                + "_dir=nil";
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
        FileRenamePanel.setVisible(false);
        FileRenamePanel.setEnabled(false);
        NodeFileMgrPane.repaint();
        FileAsButton = new ArrayList<>();
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
                        ProgressBar.setValue(100);
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
                            thandler.echo("----------------------------", false);
                            for (String subs : s) {
                                thandler.echo(subs, false);
                                String[] parts = subs.split(":");
                                if (parts[0].trim().length() > 0) {
                                    int size = Integer.parseInt(parts[1].trim().split(" ")[0]);
                                    AddNodeFileButton(parts[0].trim(), size);
                                    usedSpace += size;
                                    LOGGER.info("FileManager found file " + parts[0].trim());
                                }
                            }
                            if (FileAsButton.size() == 0) {
                                thandler.echo("No files found.", true);
                            } else {
                                thandler.echo("Total file(s)   : " + Integer.toString(s.length), false);
                                thandler.echo("Total size      : " + Integer.toString(usedSpace) + " bytes", true);
                            }
                            NodeFileMgrPane.invalidate();
                            NodeFileMgrPane.doLayout();
                            NodeFileMgrPane.repaint();
                            NodeFileMgrPane.requestFocusInWindow();
                            LOGGER.info("FileManager: File list parsing done, found " + FileAsButton.size() + " file(s).");
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

    private void FileDownload(String param) {
        if (portJustOpen) {
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
        rcvFile = "";
        PacketsByte = new byte[0];
        rx_byte = new byte[0];
        PacketsCRC = new ArrayList<>();
        String cmd = "_dl=function() "
                + "  file.open(\"" + DownloadedFileName + "\", \"r\")\n"
                + "  local buf "
                + "  local i=0 "
                + "  local checksum\n"
                + "  repeat "
                + "     buf = file.read(1024) "
                + "     if buf ~= nil then "
                + "          i = i + 1 "
                + "          checksum = 0 "
                + "          for j=1, string.len(buf) do\n"
                + "               checksum = checksum + (buf:byte(j)*20)%19 "
                + "          end "
                + "          buf='~~~'..'DATA-START~~~'..buf..'~~~'..'DATA-LENGTH~~~'..string.len(buf)..'~~~'..'DATA-N~~~'..i..'~~~'..'DATA-CRC~~~'..checksum..'~~~'..'DATA-END~~~'\n"
                + "          uart.write(0,buf) "
                + "     end "
                + "     tmr.wdclr() "
                + "  until(buf == nil) "
                + "  file.close()\n"
                + "  buf='~~~'..'DATA-TOTAL-START~~~'..i..'~~~'..'DATA-TOTAL-END~~~'\n"
                + "  uart.write(0,buf) "
                + "end "
                + "_dl() "
                + "_dl=nil\n";
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
        thandler.echo("Download file \"" + DownloadedFileName + "\"...", true);
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
            thandler.echo("Success.", true);
            if (DownloadCommand.startsWith("EDIT")) {
                FileNew(rcvFile);
            } else if (DownloadCommand.startsWith("DOWNLOAD")) {
                SaveDownloadedFile();
            } else {
                // nothing, reserved
            }
        } else {
            thandler.echo("FAIL.", true);
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
                        ProgressBar.setValue(rcvPackets.size() * 100 / packets);
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
                        rcvFile = rcvFile + PacketsData.get(i);
                        PacketsByte = concatArray(PacketsByte, x);
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
                                + ", size received:" + Integer.toString(PacketsByte.length)
                                + "\r\n, CRC expected :" + Integer.toString(PacketsCRC.get(i))
                                + "  CRC received :" + Integer.toString(CRC(x)));
                        LOGGER.info("Downloader: FAIL.");
                        PacketsCRC.clear();
                        PacketsNum.clear();
                        PacketsSize.clear();
                        PacketsData.clear();
                        rcvPackets.clear();
                        rcvFile = "";
                        PacketsByte = new byte[0];
                        FileDownloadFinisher(false);
                    }
                } else if ((rx_data.lastIndexOf("~~~DATA-TOTAL-END~~~") >= 0) && (PacketsNum.size() == packets)) {
                    try {
                        timeout.stop();
                    } catch (Exception e) {
                        LOGGER.info(e.toString());
                    }
                    ProgressBar.setValue(100);
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
        String cmd = "_dump=function()\n"
                + "  local buf\n"
                + "  local j=0\n"
                + "  if file.open(\"" + FileName + "\", \"r\") then\n"
                + "  print('--HexDump start')\n"
                + "  repeat\n"
                + "     buf=file.read(1024)\n"
                + "     if buf~=nil then\n"
                + "     local n \n"
                + "     if #buf==1024 then\n"
                + "        n=(#buf/16)*16\n"
                + "     else\n"
                + "        n=(#buf/16+1)*16\n"
                + "     end\n"
                + "     for i=1,n do\n"
                + "         j=j+1\n"
                + "         if (i-1)%16==0 then\n"
                + "            uart.write(0,string.format('%08X  ',j-1)) \n"
                + "         end\n"
                + "         uart.write(0,i>#buf and'   'or string.format('%02X ',buf:byte(i)))\n"
                + "         if i%8==0 then uart.write(0,' ')end\n"
                + "         if i%16==0 then uart.write(0,buf:sub(i-16+1, i):gsub('%c','.'),'\\n')end\n"
                + "         if i%128==0 then tmr.wdclr()end\n"
                + "     end\n"
                + "     end\n"
                + "  until(buf==nil)\n"
                + "  file.close()\n"
                + "  print(\"\\n--HexDump done.\")\n"
                + "  else\n"
                + "  print(\"\\n--HexDump error: can't open file\")\n"
                + "  end\n"
                + "end\n"
                + "_dump()\n"
                + "_dump=nil\n";
        SendToESP(cmdPrep(cmd));
    }

    private ArrayList<String> cmdPrep(String cmd) {
        String[] str = cmd.split("\n");
        ArrayList<String> s256 = new ArrayList<>();
        int i = 0;
        s256.add("");
        for (String subs : str) {
            if ((s256.get(i).length() + subs.trim().length()) <= 250) {
                s256.set(i, s256.get(i) + " " + subs.trim());
            } else {
                s256.set(i, s256.get(i) + "\r");
                s256.add(subs);
                i++;
            }
        }
        return s256;
    }

    private void UpdateLedCTS() {
        try {
            if (serialPort.isCTS()) {
                PortCTS.setIcon(LED.GREEN);
            } else {
                PortCTS.setIcon(LED.GREY);
            }
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }
    }


    private void MenuItemEditSendSelectedActionPerformed(java.awt.event.ActionEvent evt) {
        int l = 0;

        try {
            l = TextEditorList.get(iTab).getSelectedText().length();
        } catch (Exception e) {
            LOGGER.info("Can't send: nothing selected.");
            return;
        }
        if (l > 0) {
            SendToESP(TextEditorList.get(iTab).getSelectedText());
        }

    }

    private void MenuItemFileRemoveESPActionPerformed(java.awt.event.ActionEvent evt) {
        String ft = openedfiles.get(iTab).getName();
        if (ft.length() == 0) {
            LOGGER.info("removeFileFromESP: FAIL. Can't remove file from ESP without name.");
            JOptionPane.showMessageDialog(null, "Can't remove file from ESP without name.");
        }
        removeFileFromESP(ft);
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

        nLine = TextEditorList.get(iTab).getCaretLineNumber();
        String cmd = TextEditorList.get(iTab).getText().split("\r?\n")[nLine];
        btnSend(cmd);


    }


    private void MenuItemESPFormatActionPerformed(java.awt.event.ActionEvent evt) {

        int isFormat = Dialog("Format ESP flash data area and remove ALL files. Are you sure?", JOptionPane.YES_NO_OPTION);
        if (isFormat == JOptionPane.YES_OPTION) {
            btnSend("file.format()");
        }

    }


    private void RightFilesSplitPanePropertyChange(java.beans.PropertyChangeEvent evt) {
        if ("dividerLocation".equals(evt.getPropertyName()) && MenuItemViewFileManager.isSelected()) {
            Regedit.setInt(Regedit.FM_DIV, RightFilesSplitPane.getDividerLocation());
        }
    }


    private void formPropertyChange(java.beans.PropertyChangeEvent evt) {
        //log(evt.getPropertyName());
    }

    private void formComponentResized(java.awt.event.ComponentEvent evt) {
        isFileManagerShow();
    }

    private void FileListReloadActionPerformed(java.awt.event.ActionEvent evt) {
        NodeListFiles();
    }

    private void FileAsButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        String fn = evt.getActionCommand();
        if (fn.endsWith(".lua") || fn.endsWith(".lc")) {
            String cmd = "dofile(\"" + fn + "\")";
            btnSend(cmd);
        } else if (fn.endsWith(".bin") || fn.endsWith(".dat")) {
            HexDump(fn);
        } else {
            ViewFile(fn);
        }
    }


    private void MenuItemViewFileManagerActionPerformed(java.awt.event.ActionEvent evt) {
        Regedit.setBoolean(SHOW_FM_RIGHT, MenuItemViewFileManager.isSelected());
        isFileManagerShow();
    }


    private void MenuItemViewToolbarActionPerformed(java.awt.event.ActionEvent evt) {
        Regedit.setBoolean(SHOW_TOOLBAR, MenuItemViewToolbar.isSelected());
        isToolbarShow();
    }


    private void AutoScrollActionPerformed(java.awt.event.ActionEvent evt) {
        Regedit.setBoolean(AUTO_SCROLL, AutoScroll.isSelected());
    }

    private void PortDTRActionPerformed(java.awt.event.ActionEvent evt) {
        Regedit.setBoolean(PORT_DTR, PortDTR.isSelected());
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
        Regedit.setBoolean(PORT_RTS, PortRTS.isSelected());
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
        Regedit.setBoolean(SHOW_TOOLBAR, MenuItemViewToolbar.isSelected());
        isToolbarShow();
    }


    private void MenuItemViewFileManagerItemStateChanged(java.awt.event.ItemEvent evt) {
        Regedit.setBoolean(SHOW_FM_RIGHT, MenuItemViewFileManager.isSelected());
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
        Regedit.setInt(WIN_X, r.x);
        Regedit.setInt(WIN_Y, r.y);
        Regedit.setInt(WIN_H, r.height);
        Regedit.setInt(WIN_W, r.width);
        while (FilesTabbedPane.getTabCount() > 0) {
            if (CloseFile() == JOptionPane.CANCEL_OPTION) {
                return;
            }
            if ((FilesTabbedPane.getTabCount() == 1) && isFileNew()) {
                break;
            }
        }
        this.setVisible(false);
        System.exit(0);
    }


    private void EOLItemStateChanged(java.awt.event.ItemEvent evt) {
        thandler.getRSyntaxTextArea().setEOLMarkersVisible(EOL.isSelected());
    }


    private void MenuItemViewTerminalOnlyItemStateChanged(java.awt.event.ItemEvent evt) {
        if (MenuItemViewTerminalOnly.isSelected()) {
            MenuItemViewEditorOnly.setSelected(false);
            HorizontSplit.setDividerLocation(0);
        } else {
            HorizontSplit.setDividerLocation(550);
        }
    }


    private void NodeMCUComponentShown(java.awt.event.ComponentEvent evt) {
        UpdateEditorButtons();
        UpdateButtons();
    }


    private void FilesUploadActionPerformed(java.awt.event.ActionEvent evt) {
        //log(evt.paramString());
        UploadFiles();
    }


    private void FileSendESPActionPerformed(java.awt.event.ActionEvent evt) {
        if (FileSendESP.isSelected()) {
            if (TextEditorList.get(iTab).getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "File empty.");
                FileSendESP.setSelected(false);
                return;
            }
            SendToESP(TextEditorList.get(iTab).getText());
        } else {
            StopSend();
        }
    }

    private void FileSaveESPActionPerformed(java.awt.event.ActionEvent evt) {
        if (!FileSaveESP.isSelected()) {
            StopSend();
            return;
        }
        if (TextEditorList.get(iTab).getText().length() == 0) {
            FileSaveESP.setSelected(false);
            JOptionPane.showMessageDialog(null, "File empty.");
            return;
        }
        String fName = openedfiles.get(iTab).getName();
        if (fName.length() == 0) {
            FileSaveESP.setSelected(false);
            String msg = " Can't save file to ESP without name.";
            LOGGER.info("FileSaveESP: FAIL. " + msg);
            JOptionPane.showMessageDialog(null, msg);
            return;
        }
        if (Config.ins.isFile_auto_save_disk()) {
            if (!SaveFile()) { // first save file
                FileSaveESP.setSelected(false);
                return;
            }
        }
        if (!Open.isSelected() || portJustOpen) {
            LOGGER.info("FileSaveESP: Serial port not open. Operation canceled.");
            FileSaveESP.setSelected(false);
            return;
        }
        if (FirmwareType.current.eq(FirmwareType.MicroPython)) {
            pySaveFileESP(fName);
        } else if (FirmwareType.current.eq(FirmwareType.NodeMCU)) {
            nodeSaveFileESP(fName);
        } else {
            thandler.echo("不支持上传文件到 " + FirmwareType.current, true);
        }
    }


    private void TextEditorKeyTyped(java.awt.event.KeyEvent evt) {
        if (!isChanged()) {
            FileChanged.set(iTab, true);
            UpdateEditorButtons();
        }
    }


    private void TextEditorCaretPositionChanged(java.awt.event.InputMethodEvent evt) {
        UpdateEditorButtons();
    }


    private void TextEditorCaretUpdate(javax.swing.event.CaretEvent evt) {
        UpdateEditorButtons();
    }


    private void MenuItemViewEditorOnlyItemStateChanged(java.awt.event.ItemEvent evt) {
        if (MenuItemViewEditorOnly.isSelected()) {
            MenuItemViewTerminalOnly.setSelected(false);
            HorizontSplit.setDividerLocation(HorizontSplit.getWidth());
        } else {
            HorizontSplit.setDividerLocation(550);
        }
    }


    private void PyListDirActionPerformed(java.awt.event.ActionEvent evt) {
        PyListFiles();
    }


    private void NodeFileSystemInfo() {
        String cmd = "r,u,t=file.fsinfo() print(\"Total : \"..t..\" bytes\\r\\nUsed  : \"..u..\" bytes\\r\\nRemain: \"..r..\" bytes\\r\\n\") r=nil u=nil t=nil";
        send(addCRLF(cmd), true);
    }


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
    private javax.swing.JComboBox Command;

    private javax.swing.JPopupMenu ContextMenuESPFileLUA;
    private javax.swing.JPopupMenu ContextMenuTerminal;


    private javax.swing.JCheckBox EOL;


    private javax.swing.JButton FileFormat;
    private javax.swing.JLayeredPane FileLayeredPane;
    private javax.swing.JButton FileListReload;
    private javax.swing.JScrollPane FileManagerScrollPane;
    private javax.swing.JLayeredPane FileManagersLayer;

    JButton firmware_type_label;

    private javax.swing.JLabel FilePathLabel;
    private javax.swing.JTextField FileRename;
    private javax.swing.JLabel FileRenameLabel;
    private javax.swing.JLayeredPane FileRenamePanel;
    private javax.swing.JToggleButton FileSaveESP;
    private javax.swing.JToggleButton FileSendESP;
    private javax.swing.JButton FileSystemInfo;
    private javax.swing.JTabbedPane FilesTabbedPane;
    private javax.swing.JToolBar FilesToolBar;
    private javax.swing.JButton FilesUpload;
    private javax.swing.JSplitPane HorizontSplit;
    private javax.swing.JLayeredPane LEDPanel;
    private javax.swing.JCheckBox LF;
    private javax.swing.JLayeredPane LeftBasePane;
    private javax.swing.JLayeredPane LeftMainButtons;



    private javax.swing.JMenuItem MenuItemESPFileDelete;
    private javax.swing.JMenuItem MenuItemESPFileDo;
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
    private javax.swing.JProgressBar ProgressBar;
    private javax.swing.JLayeredPane PyFileMgrPane;
    private javax.swing.JButton PyListDir;
    private javax.swing.JButton ReScan;
    private javax.swing.JLayeredPane RightBasePane;
    private javax.swing.JLayeredPane RightBottomPane;
    private javax.swing.JSplitPane RightFilesSplitPane;
    private javax.swing.JButton SendCommand;

    private javax.swing.JComboBox Speed;
    private javax.swing.JLayeredPane SriptsTab;


    private org.fife.ui.rtextarea.RTextScrollPane TerminalPane;
    private javax.swing.JPopupMenu.Separator TerminalSeparator3;
    private org.fife.ui.rsyntaxtextarea.RSyntaxTextArea TextEditor;
    private org.fife.ui.rtextarea.RTextScrollPane TextScroll;


    private org.jdesktop.beansbinding.BindingGroup bindingGroup;

    private ArrayList<javax.swing.JLayeredPane> FilePaneList;
    private ArrayList<org.fife.ui.rsyntaxtextarea.RSyntaxTextArea> TextEditorList;
    private ArrayList<AutoCompletion> autoCompletions;
    private ArrayList<File> openedfiles = new ArrayList<>();

    private ArrayList<File> mFile; // for multifile op
    private ArrayList<Boolean> FileChanged;
    private ArrayList<javax.swing.JButton> FileAsButton;
    private ArrayList<javax.swing.JButton> PyFileAsButton;
    private ArrayList<javax.swing.JPopupMenu> FilePopupMenu;
    private ArrayList<javax.swing.JMenuItem> FilePopupMenuItem;
    private int iTab = 0; // tab index
    private int mFileIndex = -1; // multifile index
    private String UploadFileName = "";


    public static final Logger LOGGER = Logger.getLogger(EspIDE.class.getName());

    String DownloadedFileName = "";
    String NewFile = "New";
    int FileCount = 0;
    String workDir = "";
    JFileChooser chooser;
    static final FileNameExtensionFilter FILTER_LUA = new FileNameExtensionFilter("LUA files (*.lua, *.lc)", "lua", "lc");
    static final FileNameExtensionFilter FILTER_PYTHON = new FileNameExtensionFilter("Python files (*.py, *.pyc)", "py", "pyc");
    FileInputStream fis = null;
    FileOutputStream fos = null;
    InputStreamReader isr = null;
    OutputStreamWriter osw = null;
    BufferedReader br = null;
    BufferedWriter bw = null;
    public static int j = 0;
    public static int pyLevel = 0;
    public static boolean sendPending = false;
    public static String s[];
    public ActionListener taskPerformer;
    public ActionListener watchDog;
    public Timer timer;
    public Timer timeout;
    public Color color;
    public Color themeTextBackground;
    public static String rcvBuf = "";
    public static String rx_data = "";
    public static String tx_data = "";
    public static byte[] rx_byte;
    public static byte[] tx_byte;
    // downloader
    public int packets = 0;
    public String rcvFile = "";
    public ArrayList<String> rcvPackets;
    public ArrayList<byte[]> sendPackets;
    public ArrayList<Boolean> sendPacketsCRC;
    public ArrayList<String> PacketsData;
    public ArrayList<Integer> PacketsSize;
    public ArrayList<Integer> PacketsCRC;
    public ArrayList<Integer> PacketsNum;
    public byte[] PacketsByte;
    public final int SendPacketSize = 250;
    public static String DownloadCommand;
    public static boolean busyIcon = false;


    private long startTime = System.currentTimeMillis();

    private static final int portMask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS;


    public void inc_j() {
        ++j;
    }

    public void j0() {
        j = 0;
    }

    public void PortFinder() {
        Speed.setSelectedItem(String.valueOf(Regedit.getInt(Regedit.SERIAL_PORT, 115200)));
        Port.removeAllItems();

        String[] portNames = SerialPortList.getPortNames();
        for (String p : portNames) {
            Port.addItem(p);
        }
        String lastPort = Regedit.getString(Regedit.SERIAL_PORT, null);
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
            LOGGER.info("Open port " + portName + " - Success.");
            thandler.echo("PORT OPEN " + getBaudRate(), true);

            portJustOpen = true;
            //发送一个回车 触发串口返回，好判断固件类型
            btnSend("\r\n");
        }
        return success;

    }


    public void portClose() {
        try {
            if (serialPort.closePort()) {
                thandler.echo("PORT CLOSED - Success.", false);
            } else {
                thandler.echo("PORT CLOSED - Fail.", false);
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
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/ESP8266-64x64.png")));
        setLocationRelativeTo(null); // window centered

        FilePaneList = new ArrayList<>();
        TextEditorList = new ArrayList<>();
        autoCompletions = new ArrayList<>();

        FileChanged = new ArrayList<>();

        FileAsButton = new ArrayList<>();
        PyFileAsButton = new ArrayList<>();

        FilePopupMenu = new ArrayList<>();
        FilePopupMenuItem = new ArrayList<>();

        FilesTabbedPane.removeAll();
        LoadPrefs();

        FileRenamePanel.setVisible(false);

        AddTab(NewFile, "");

        updateTheme(true);
    }

    private void LoadPrefs() {
        // Settings - Firmware
        workDir = Regedit.getString(PATH, "");
        chooser = new JFileChooser(workDir);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        chooser.setCurrentDirectory(new File(workDir));

        AutoScroll.setSelected(Regedit.getBoolean(AUTO_SCROLL, true));

        MenuItemViewToolbar.setSelected(Regedit.getBoolean(SHOW_TOOLBAR, true));

        MenuItemViewFileManager.setSelected(Regedit.getBoolean(SHOW_FM_RIGHT, true));


        PortDTR.setSelected(Regedit.getBoolean(PORT_DTR, false));
        PortRTS.setSelected(Regedit.getBoolean(PORT_RTS, false));
        EOL.setSelected(Regedit.getBoolean(SHOW_EOL, false));


        LOGGER.info("Load saved settings: DONE.");
    }

    private void AddNodeFileButton(String FileName, int size) {
        FileAsButton.add(new javax.swing.JButton());
        int i = FileAsButton.size() - 1;
        FileAsButton.get(i).setText(FileName);
        //FileAsButton.get(i).setFont(new java.awt.Font("Tahoma", 0, 12));
        FileAsButton.get(i).setAlignmentX(0.5F);
        FileAsButton.get(i).setMargin(new java.awt.Insets(2, 2, 2, 2));
        FileAsButton.get(i).setMaximumSize(new java.awt.Dimension(130, 25));
        FileAsButton.get(i).setPreferredSize(new java.awt.Dimension(130, 25));
        FileAsButton.get(i).setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        //FileAsButton.get(i).setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        FileAsButton.get(i).addActionListener(evt -> FileAsButton1ActionPerformed(evt));
        // PopUp menu
        FilePopupMenu.add(new javax.swing.JPopupMenu());
        int x = FilePopupMenu.size() - 1;
        int y;
        // PopUp menu items
        if (FileName.endsWith(".lua")) {
            FileAsButton.get(i).setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/lua.png")));
            FileAsButton.get(i).setToolTipText(FileAsButton.get(i).getActionCommand() + ", LeftClick - Run, RightClick - Other actions");
            AddMenuItemRun(x, FileName);
            AddMenuItemCompile(x, FileName);
            AddMenuItemSeparator(x);
            AddMenuItemView(x, FileName);
            AddMenuItemDump(x, FileName);
            AddMenuItemEdit(x, FileName, size);
            AddMenuItemDownload(x, FileName, size);
            AddMenuItemRename(x, FileName);
            AddMenuItemSeparator(x);
            AddMenuItemRemove(x, FileName);
        } else if (FileName.endsWith(".lc")) {
            FileAsButton.get(i).setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/lc.png")));
            FileAsButton.get(i).setToolTipText(FileAsButton.get(i).getActionCommand() + ", LeftClick - Run, RightClick - Other actions");
            AddMenuItemRun(x, FileName);
            AddMenuItemSeparator(x);
            AddMenuItemDump(x, FileName);
            AddMenuItemDownload(x, FileName, size);
            AddMenuItemRename(x, FileName);
            AddMenuItemSeparator(x);
            AddMenuItemRemove(x, FileName);
        } else {
            FileAsButton.get(i).setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/file.png")));
            FileAsButton.get(i).setToolTipText(FileAsButton.get(i).getActionCommand() + ", LeftClick - View, RightClick - Other actions");
            AddMenuItemView(x, FileName);
            AddMenuItemDump(x, FileName);
            AddMenuItemEdit(x, FileName, size);
            AddMenuItemDownload(x, FileName, size);
            AddMenuItemRename(x, FileName);
            AddMenuItemSeparator(x);
            AddMenuItemRemove(x, FileName);
        }

        FileAsButton.get(i).setComponentPopupMenu(FilePopupMenu.get(x));
        NodeFileMgrPane.add(FileAsButton.get(i));
    }

    private void AddMenuItemSeparator(int x) {
        FilePopupMenu.get(x).add(new javax.swing.JPopupMenu.Separator());
    }

    private void AddMenuItemEdit(int x, String FileName, int size) {
        int y;
        FilePopupMenuItem.add(new javax.swing.JMenuItem());
        y = FilePopupMenuItem.size() - 1;
        FilePopupMenuItem.get(y).setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/edit.png")));
        FilePopupMenuItem.get(y).setText("Edit " + FileName);
        FilePopupMenuItem.get(y).setToolTipText("Download file from ESP and open in new editor window");
        FilePopupMenuItem.get(y).setActionCommand(FileName + "Size:" + Integer.toString(size));
        FilePopupMenuItem.get(y).addActionListener(evt -> {
            DownloadCommand = "EDIT";
            FileDownload(evt.getActionCommand());
        });
        FilePopupMenu.get(x).add(FilePopupMenuItem.get(y));
    }

    private void AddMenuItemDownload(int x, String FileName, int size) {
        int y;
        FilePopupMenuItem.add(new javax.swing.JMenuItem());
        y = FilePopupMenuItem.size() - 1;
        FilePopupMenuItem.get(y).setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/download.png")));
        FilePopupMenuItem.get(y).setText("Download " + FileName);
        FilePopupMenuItem.get(y).setToolTipText("Download file from ESP and save to disk");
        FilePopupMenuItem.get(y).setActionCommand(FileName + "Size:" + Integer.toString(size));
        FilePopupMenuItem.get(y).addActionListener(evt -> {
            DownloadCommand = "DOWNLOAD";
            FileDownload(evt.getActionCommand());
        });
        FilePopupMenu.get(x).add(FilePopupMenuItem.get(y));
    }

    private void AddMenuItemRun(int x, String FileName) {
        int y;
        FilePopupMenuItem.add(new javax.swing.JMenuItem());
        y = FilePopupMenuItem.size() - 1;
        FilePopupMenuItem.get(y).setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/play.png")));
        FilePopupMenuItem.get(y).setText("Run " + FileName);
        FilePopupMenuItem.get(y).setToolTipText("Execute command dofile(\"" + FileName + "\") for run this file");
        FilePopupMenuItem.get(y).setActionCommand(FileName);
        FilePopupMenuItem.get(y).addActionListener(evt -> btnSend("dofile(\"" + evt.getActionCommand() + "\")"));
        FilePopupMenu.get(x).add(FilePopupMenuItem.get(y));
    }

    private void AddMenuItemCompile(int x, String FileName) {
        int y;
        FilePopupMenuItem.add(new javax.swing.JMenuItem());
        y = FilePopupMenuItem.size() - 1;
        FilePopupMenuItem.get(y).setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/wizard.png")));
        FilePopupMenuItem.get(y).setText("Compile " + FileName + " to .lc");
        FilePopupMenuItem.get(y).setToolTipText("Execute command node.compile(\"" + FileName + "\")");
        FilePopupMenuItem.get(y).setActionCommand(FileName);
        FilePopupMenuItem.get(y).addActionListener(evt -> {
            btnSend("node.compile(\"" + evt.getActionCommand() + "\")");
            try {
                Thread.sleep(500L);
            } catch (Exception e) {
            }
            FileListReload.doClick();
        });
        FilePopupMenu.get(x).add(FilePopupMenuItem.get(y));
    }

    private void AddMenuItemRename(int x, String FileName) {
        int y;
        FilePopupMenuItem.add(new javax.swing.JMenuItem());
        y = FilePopupMenuItem.size() - 1;
        FilePopupMenuItem.get(y).setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/rename.png")));
        FilePopupMenuItem.get(y).setText("Rename " + FileName);
        FilePopupMenuItem.get(y).setToolTipText("Execute command file.rename(\"" + FileName + "\",\"NewName\")");
        FilePopupMenuItem.get(y).setActionCommand(FileName);
        FilePopupMenuItem.get(y).addActionListener(evt -> {
            FileRename.setText(evt.getActionCommand());
            FileRenameLabel.setText(evt.getActionCommand());
            FileRenamePanel.setEnabled(true);
            FileRenamePanel.setVisible(true);
            FileRename.grabFocus();
        });
        FilePopupMenu.get(x).add(FilePopupMenuItem.get(y));
    }

    private void AddMenuItemRemove(int x, String FileName) {
        int y;
        FilePopupMenuItem.add(new javax.swing.JMenuItem());
        y = FilePopupMenuItem.size() - 1;
        FilePopupMenuItem.get(y).setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/trash.png")));
        FilePopupMenuItem.get(y).setText("Remove " + FileName);
        FilePopupMenuItem.get(y).setToolTipText("Execute command file.remove(\"" + FileName + "\") and delete file from NodeMCU filesystem");
        FilePopupMenuItem.get(y).setActionCommand(FileName);
        FilePopupMenuItem.get(y).addActionListener(evt -> removeFileFromESP(evt.getActionCommand()));
        FilePopupMenu.get(x).add(FilePopupMenuItem.get(y));
    }

    private void AddMenuItemView(int x, String FileName) {
        int y;
        FilePopupMenuItem.add(new javax.swing.JMenuItem());
        y = FilePopupMenuItem.size() - 1;
        FilePopupMenuItem.get(y).setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/view.png")));
        FilePopupMenuItem.get(y).setText("View " + FileName);
        FilePopupMenuItem.get(y).setToolTipText("View content of file " + FileName + " on terminalArea");
        FilePopupMenuItem.get(y).setActionCommand(FileName);
        FilePopupMenuItem.get(y).addActionListener(evt -> ViewFile(evt.getActionCommand()));
        FilePopupMenu.get(x).add(FilePopupMenuItem.get(y));
    }

    private void AddMenuItemDump(int x, String FileName) {
        int y;
        FilePopupMenuItem.add(new javax.swing.JMenuItem());
        y = FilePopupMenuItem.size() - 1;
        FilePopupMenuItem.get(y).setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/dump.png")));
        FilePopupMenuItem.get(y).setText("HexDump " + FileName);
        FilePopupMenuItem.get(y).setToolTipText("View HexDump " + FileName + "in terminalArea");
        FilePopupMenuItem.get(y).setActionCommand(FileName);
        FilePopupMenuItem.get(y).addActionListener(evt -> HexDump(evt.getActionCommand()));
        FilePopupMenu.get(x).add(FilePopupMenuItem.get(y));
    }

    private void AddTab(String title, String content) {
        int i = FilesTabbedPane.getTabCount();

        FilePaneList.add(new JLayeredPane());

        RSyntaxTextArea textArea = Context.create1(FirmwareType.current);
        TextEditorList.add(textArea);

        openedfiles.add(new File(""));
        FileChanged.add(false);

        autoCompletions.add(new AutoCompletion(Context.create2(FirmwareType.current)));
        autoCompletions.get(i).install(textArea);

        textArea.addCaretListener(evt -> UpdateEditorButtons());
        textArea.addActiveLineRangeListener(evt -> UpdateEditorButtons());
        textArea.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                TextEditorCaretPositionChanged(evt);
            }

            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        textArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextEditorKeyTyped(evt);
            }
        });

        RTextScrollPane scrollPane = new RTextScrollPane();
        scrollPane.setViewportView(TextEditorList.get(i));
        scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setIconRowHeaderEnabled(false);
        scrollPane.setLineNumbersEnabled(true);
        scrollPane.setFoldIndicatorEnabled(true);

        GroupLayout layout = new javax.swing.GroupLayout(FilePaneList.get(i));
        FilePaneList.get(i).setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
        );

        FilePaneList.get(i).setLayer(scrollPane, javax.swing.JLayeredPane.DEFAULT_LAYER);

        FilesTabbedPane.addTab(title, FilePaneList.get(i));

        FilesTabbedPane.setSelectedIndex(i);
        iTab = i;
        updateTheme(false);
        FileLabelUpdate();

        textArea.setText(content);
    }

    public void updateTheme(boolean all) {
        int t = Config.ins.getColor_theme();

        String res;
        if (t == 1) {
            res = "/org/fife/ui/rsyntaxtextarea/themes/dark.xml";
        } else if (t == 2) {
            res = "/org/fife/ui/rsyntaxtextarea/themes/eclipse.xml";
        } else if (t == 3) {
            res = "/org/fife/ui/rsyntaxtextarea/themes/idea.xml";
        } else if (t == 4) {
            res = "/org/fife/ui/rsyntaxtextarea/themes/vs.xml";
        } else if (t == 5) {
            res = "/org/fife/ui/rsyntaxtextarea/themes/default-alt.xml";
        } else {
            res = "/org/fife/ui/rsyntaxtextarea/themes/default.xml";
        }
        try {
            Theme theme = Theme.load(getClass().getResourceAsStream(res));
            if (all) {
                for (int i = 0; i < FilesTabbedPane.getTabCount(); i++) {
                    theme.apply(TextEditorList.get(i));
                    TextEditorList.get(i).setFont(TextEditorList.get(i).getFont().deriveFont(Config.ins.getEditor_font_size()));
                }


                theme.apply(thandler.getRSyntaxTextArea());
                thandler.getRSyntaxTextArea().setFont(thandler.getRSyntaxTextArea().getFont().deriveFont(Config.ins.getTerminal_font_size()));
                themeTextBackground = thandler.getRSyntaxTextArea().getBackground();
                //SnippetText.setBackground(SnippetTopPane.getBackground());
                LOGGER.info("Set new color theme: Success.");
            } else {
                theme.apply(TextEditorList.get(iTab));
                TextEditorList.get(iTab).setFont(TextEditorList.get(iTab).getFont().deriveFont(Config.ins.getEditor_font_size()));
            }
        } catch (IOException e) {
            LOGGER.info(e.toString());
            LOGGER.info("Set new color theme: FAIL.");
        }
    }

    private void RemoveTab() {
        if (FilesTabbedPane.getTabCount() <= 1) {
            iTab = 0;
            TextEditorList.get(iTab).setText("");
            TextEditorList.get(iTab).discardAllEdits();
            FilesTabbedPane.setTitleAt(iTab, NewFile);
            openedfiles.set(iTab, new File(""));
            FileLabelUpdate();
            FileChanged.set(iTab, false);
            UpdateEditorButtons();
            LOGGER.info("FileTab cleared: Success.");
        } else {
            openedfiles.remove(iTab);
            FileChanged.remove(iTab);
            autoCompletions.remove(iTab);
            TextEditorList.remove(iTab);
            FilePaneList.remove(iTab);
            FilesTabbedPane.removeTabAt(iTab);
            FilesTabbedPane.setSelectedIndex(iTab);
            FileLabelUpdate();
            LOGGER.info("FileTab removed: Success.");
        }
    }

    private boolean isChanged() {
        try {
            if (FileChanged.get(iTab)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        if (TextEditorList.get(iTab).canUndo()) {
            return true;
        }
        return TextEditorList.get(iTab).canRedo();
    }

    private int Dialog(String msg, int btn) {
        this.setAlwaysOnTop(false);
        Toolkit.getDefaultToolkit().beep();
        int returnVal = JOptionPane.showConfirmDialog(null, msg, "Attention", btn, JOptionPane.WARNING_MESSAGE);
        this.setAlwaysOnTop(AlwaysOnTop.isSelected());
        return returnVal;
    }


    private void FileLabelUpdate() {
        iTab = FilesTabbedPane.getSelectedIndex();
        if (isFileNew() || iTab < 0 || openedfiles.isEmpty()) {
            FilePathLabel.setText("");
        } else {

            FilePathLabel.setText(openedfiles.get(iTab).getPath());
        }
        UpdateEditorButtons();
    }

    private int CloseFile() {
        if (isChanged()) {
            LOGGER.info("File changed. Ask before closing.");
            int returnVal = Dialog("Save file \"" + FilesTabbedPane.getTitleAt(iTab) + "\" before closing?", JOptionPane.YES_NO_CANCEL_OPTION);
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
        RemoveTab();
        LOGGER.info("File close: Success.");
        return JOptionPane.YES_OPTION;
    }

    private void ReloadFile() {
        if (isFileNew()) {
            return;
        }
        if (isChanged()) {
            LOGGER.info("File reload: File changed. Ask before reloading.");
            int returnVal = Dialog("Discard any changes and reload file from disk?", JOptionPane.YES_NO_OPTION);
            if (returnVal != JOptionPane.OK_OPTION) {
                LOGGER.info("File reload: FAIL (file not saved, reload cancelled by user choice)");
                return;
            } else {
                LOGGER.info("File reload: Reload anyway by user choice.");
            }
        }
        if (LoadFile()) {
            LOGGER.info("File reload: Success.");

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
                    if (portJustOpen) {
                        if (!data.trim().isEmpty()) {
                            if (data.contains("\r\n>>>")) {
                                thandler.echo("MicroPython firmware detected.", true);
                                btnSend("import sys; print(\"MicroPython ver:\",sys.version_info)");
                                SetFirmwareType(yh.espide.FirmwareType.MicroPython);
                            } else if (data.contains("\r\n>")) {
                                thandler.echo("NodeMCU firmware detected.", true);
                                btnSend("=node.info()");
                                SetFirmwareType(yh.espide.FirmwareType.NodeMCU);
                            } else if (data.contains("\r\nERR")) {
                                thandler.echo("AT-based firmware detected.", true);
                                btnSend("AT+GMR");
                                SetFirmwareType(yh.espide.FirmwareType.AT);
                            } else {
                                thandler.echo("未成功识别固件，重新打开串口或手动切换固件类型。", true);
                            }

                            portJustOpen = false;
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
                            ProgressBar.setValue((j * 100) / div);
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
                            ProgressBar.setValue((j * 100) / div);
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
        if (!Open.isSelected() || portJustOpen) {
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
        if (!Open.isSelected() || portJustOpen) {
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
            thandler.echo("Waiting answer from ESP - Timeout reached. Command aborted.", true);
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
        s = TextEditorList.get(iTab).getText().split("\r?\n");
        for (String subs : s) {
            sendBuf.add("w([==[" + subs + "]==]);");
        }
        sendBuf.add("file.close();");
        if (Config.ins.isFile_auto_run()) {
            sendBuf.add("dofile(\"" + ft + "\");");
        }
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
        int l = TextEditorList.get(iTab).getText().length();
        String fragment;
        while (pos1 <= l) {
            pos2 = pos1 + size;
            if (pos2 > l) {
                pos2 = l;
            }
            fragment = TextEditorList.get(iTab).getText().substring(pos1, pos2);
            sendBuf.add(fragment);
            pos1 += size;
        }
        sendBuf.add("ESP_cmd_close");
        sendBuf.add("\r\n");
        if (Config.ins.isFile_auto_run()) {
            sendBuf.add("dofile(\"" + ft + "\")");
        }
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
                        ProgressBar.setValue((j * 100) / div);
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
                        ProgressBar.setValue((j * 100) / div);
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
            Busy.setIcon(LED.BLUE);

        } else {
            Busy.setIcon(LED.RED);

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
            Busy.setIcon(LED.GREY);

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
        Busy.setBackground(new java.awt.Color(153, 0, 0)); // RED


        ProgressBar.setValue(0);
        ProgressBar.setVisible(true);
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
        FileSaveESP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/abort.png")));
        FileSaveESP.setSelected(true);
    }

    public void Idle() {
        Busy.setText("IDLE");
        Busy.setBackground(new java.awt.Color(0, 153, 0)); // GREEN
        Busy.setIcon(LED.GREY);


        ProgressBar.setVisible(false);
        FileSendESP.setSelected(true);
        UpdateButtons();
        UpdateEditorButtons();
    }

    public void UpdateLED() {
        if (!Open.isSelected()) {
            PortDTR.setIcon(LED.GREY);
            PortRTS.setIcon(LED.GREY);
            PortCTS.setIcon(LED.GREY);
            Open.setText("Open");
            Open.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/connect1.png")));
            PortOpenLabel.setIcon(LED.GREY);
        } else {
            Open.setText("Close");
            Open.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/disconnect1.png")));
            PortOpenLabel.setIcon(LED.GREEN);
            UpdateLedCTS();
            if (PortDTR.isSelected()) {
                PortDTR.setIcon(LED.GREEN);
            } else {
                PortDTR.setIcon(LED.GREY);
            }
            if (PortRTS.isSelected()) {
                PortRTS.setIcon(LED.GREEN);
            } else {
                PortRTS.setIcon(LED.GREY);
            }
            if (portJustOpen) {
                PortOpenLabel.setIcon(LED.RED);
            }
        }
    }

    public void SendUnLock() {
        Idle();
        FileSaveESP.setText("Save to ESP");
        FileSaveESP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/move.png")));
        FileSaveESP.setSelected(false);
        FileSendESP.setSelected(false);
    }


    boolean SaveDownloadedFile() {
        boolean success = false;
        LOGGER.info("Saving downloaded file...");
        chooser.rescanCurrentDirectory();
        File f = new File(DownloadedFileName);
        javax.swing.filechooser.FileFilter flt = chooser.getFileFilter();
        chooser.resetChoosableFileFilters();
        chooser.setSelectedFile(f);
        chooser.setDialogTitle("Save downloaded from ESP file \"" + DownloadedFileName + "\" As...");
        int returnVal = chooser.showSaveDialog(null);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            LOGGER.info("Saving abort by user.");
            return success;
        }
        f = chooser.getSelectedFile();
        chooser.setFileFilter(flt);
        DownloadedFileName = f.getName();
        SavePath();
        if (f.exists()) {
            LOGGER.info("File " + DownloadedFileName + " already exist, waiting user choice");
            int shouldWrite = Dialog("File " + DownloadedFileName + " already exist. Overwrite?", JOptionPane.YES_NO_OPTION);
            if (shouldWrite != JOptionPane.YES_OPTION) {
                LOGGER.info("Saving canceled by user, because file " + DownloadedFileName + " already exist");
                return success;
            } else {
                LOGGER.info("File " + DownloadedFileName + " will be overwriten by user choice");
            }
        } else { // we saving file, when open
            LOGGER.info("We saving new file " + DownloadedFileName);
        }
        try {
            LOGGER.info("Try to saving file " + DownloadedFileName + " ...");
            fos = new FileOutputStream(f);
            fos.write(PacketsByte);
            fos.flush();
            LOGGER.info("Save file " + DownloadedFileName + ": Success, size:" + Long.toString(f.length()));
            success = true;
        } catch (Exception e) {
            LOGGER.info("Save file " + DownloadedFileName + ": FAIL.");
            LOGGER.info(e.toString());
            JOptionPane.showMessageDialog(null, "Error, file " + DownloadedFileName + " not saved!");
        }
        try {
            if (fos != null) {
                fos.close();
            }
        } catch (IOException e) {
            LOGGER.info(e.toString());
        }
        return success;
    }

    private void UploadFiles() {
        if (!Open.isSelected()) {
            thandler.echo("Uploader: Serial port not open, operation FAILED.", true);
            return;
        }
        if (portJustOpen) {
            thandler.echo("Uploader: Communication with MCU not yet established.", true);
            return;
        }
        chooser.rescanCurrentDirectory();
        javax.swing.filechooser.FileFilter flt = chooser.getFileFilter();
        chooser.resetChoosableFileFilters();
        chooser.setDialogTitle("Select file to upload to ESP");
        chooser.setMultiSelectionEnabled(true);
        int returnVal = chooser.showOpenDialog(LeftBasePane);
        mFile = new ArrayList<>();
        LOGGER.info("Uploader: chooser selected file:" + chooser.getSelectedFiles().length);
        if (mFile.addAll(Arrays.asList(chooser.getSelectedFiles()))) {
//        if ( mFile.add(chooser.getSelectedFile()) ) {
            mFileIndex = 0;
        } else {
            mFileIndex = -1;
            LOGGER.info("Uploader: no file selected");
            return;
        }
        chooser.setFileFilter(flt);
//        chooser.setMultiSelectionEnabled(false);
        if (!(returnVal == JFileChooser.APPROVE_OPTION)) {
            LOGGER.info("Uploader: canceled by user");
            return;
        }
        SavePath();
        UploadFilesStart();
    }

    private void UploadFilesStart() {
        UploadFileName = mFile.get(mFileIndex).getName();
        sendBuf = new ArrayList<>();
        PacketsData = new ArrayList<>();
        PacketsCRC = new ArrayList<>();
        PacketsSize = new ArrayList<>();
        PacketsNum = new ArrayList<>();
        sendPacketsCRC = new ArrayList<>();
        rcvBuf = "";
        PacketsByte = new byte[0];
        rx_byte = new byte[0];
        tx_byte = new byte[0];

        if (!LoadBinaryFile(mFile.get(mFileIndex))) {
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
        thandler.echo("Uploading to ESP file " + UploadFileName + "...", true);
        timer.setInitialDelay(delay);
        WatchDog();
        timer.start();
    }

    private boolean LoadBinaryFile(File f) {
        boolean success = false;
        try {
            LOGGER.info("BinaryFileLoader: Try to load file " + f.getName() + " ...");
            fis = new FileInputStream(f);
            DataInputStream dis = new DataInputStream(fis);
            tx_byte = new byte[dis.available()];
            int size = dis.read(tx_byte);
            dis.close();
            fis.close();
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
            Busy.setIcon(LED.BLUE);

        } else {
            Busy.setIcon(LED.RED);

        }
        busyIcon = !busyIcon;
        try {
            //log("BytesSender sending:" + b.toString().replace("\r\n", "<CR><LF>"));
            serialPort.writeBytes(b);
        } catch (SerialPortException ex) {
            LOGGER.info("BytesSender send FAIL:" + b.toString().replace("\r\n", "<CR><LF>"));

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
                    ProgressBar.setValue(j * 100 / (sendBuf.size() + sendPackets.size() - 1));
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
                        thandler.echo("Success", true);
                        LOGGER.info("Uploader: Success");
                    } else {
                        thandler.echo("Fail", true);
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
                    if (mFileIndex != -1 && mFileIndex++ < mFile.size()) {
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

    private void ViewFile(String fn) {
        String cmd = "_view=function()\n"
                + "local _line\n"
                + "if file.open(\"" + fn + "\",\"r\") then \n"
                + "    print(\"--FileView start\")\n"
                + "    repeat _line = file.readline() \n"
                + "        if (_line~=nil) then \n"
                + "            print(string.sub(_line,1,-2)) \n"
                + "        end \n"
                + "    until _line==nil\n"
                + "    file.close() \n"
                + "    print(\"--FileView done.\") \n"
                + "else\n"
                + "  print(\"\\r--FileView error: can't open file\")\n"
                + "end\n"
                + "end\n"
                + "_view()\n"
                + "_view=nil\n";
        SendToESP(cmdPrep(cmd));
    }

    private void SetFirmwareType(FirmwareType ftype) {
        FirmwareType.current = ftype;
        firmware_type_label.setText(ftype.toString());
        chooser.resetChoosableFileFilters();
        switch (ftype) {
            case MicroPython:
                chooser.setFileFilter(FILTER_PYTHON);
                NodeFileMgrPane.setVisible(false);
                PyFileMgrPane.setVisible(true);
                thandler.getRSyntaxTextArea().setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);
                break;
            case NodeMCU:
                chooser.setFileFilter(FILTER_LUA);
                NodeFileMgrPane.setVisible(true);
                PyFileMgrPane.setVisible(false);
                thandler.getRSyntaxTextArea().setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LUA);
                break;
            case AT:
                NodeFileMgrPane.setVisible(false);
                PyFileMgrPane.setVisible(false);
                break;
        }


        updateCommandsSet();
    }


    private boolean pySaveFileESP(String ft) {
        LOGGER.info("pyFileSaveESP: Starting...");
        String[] content = TextEditorList.get(iTab).getText().split("\r?\n");
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
        f.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
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
        if (portJustOpen) {
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
            thandler.echo("Waiting answer from ESP - Timeout reached. Command aborted.", true);
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
                        thandler.echo("----------------------------", false);
                        for (String subs : s) {
                            thandler.echo(subs, false);
                            if (subs.trim().length() > 0) {
                                AddPyFileButton(subs);
                                LOGGER.info("FileManager found file " + subs);
                            }
                        }
                        if (PyFileAsButton.size() == 0) {
                            thandler.echo("No files found.", false);
                        }
                        thandler.echo("----------------------------", true);
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
