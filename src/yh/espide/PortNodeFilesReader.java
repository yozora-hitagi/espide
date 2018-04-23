package yh.espide;

import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import java.util.Arrays;
import java.util.logging.Logger;

public class PortNodeFilesReader implements SerialPortEventListener {

    Logger LOGGER = Logger.getLogger(PortNodeFilesReader.class.getName());

    private String sendResp = "";

    String rcv_data = "";

    EspIDE ide;
    SendBuf sendBuf;

    public PortNodeFilesReader(EspIDE ide) {
        this.ide = ide;

        String cmd = Context.GetLua("/yh/espide/file_list.lua");
        sendBuf = new SendBuf();
        sendBuf.add(ide.cmdPrep(cmd));

//        String cmd = Context.GetLua("/yh/espide/file_list.lua");
//        String[] s = cmd.split("\r?\n");
//        for (String subs : s) {
//            sendBuf.add(subs);
//        }


    }

    public void start() {
        LOGGER.info("FileManager: Starting...");
        ide.watchDog.start();
        send();
    }

    private void send() {
        if (sendBuf.hasNext()) {
            ide.send(ide.addCR(sendBuf.next()), false);
        }

    }

    public void serialEvent(SerialPortEvent event) {

        if (event.isRXCHAR() && event.getEventValue() > 0) {
            try {
                String data = SerialObject.ins.readString(event.getEventValue());
                sendResp = sendResp + data;
                rcv_data = rcv_data + data;
            } catch (Exception e) {
                LOGGER.info(e.toString());
            }
            if (sendResp.contains("> ")) {
                ide.watchDog.feed();
                sendResp = "";
                send();
            }
            try {
                if (rcv_data.contains("~~~File list END~~~")) {
                    try {
                        ide.watchDog.stop();
                    } catch (Exception e) {
                        LOGGER.info(e.toString());
                    }
                    Progress.ins.setValue(100);
                    LOGGER.info("FileManager: File list found! Do parsing...");
                    try {
                        // parsing answer
                        int start = rcv_data.indexOf("~~~File list START~~~");
                        rcv_data = rcv_data.substring(start + 23, rcv_data.indexOf("~~~File list END~~~"));
                        String[] s = rcv_data.split("\r?\n");
                        Arrays.sort(s);
                        int usedSpace = 0;
                        ide.thandler.comment("----------------------------", false);
                        for (String subs : s) {
                            ide.thandler.comment(subs, false);
                            String[] parts = subs.split(":");
                            if (parts[0].trim().length() > 0) {
                                int size = Integer.parseInt(parts[1].trim().split(" ")[0]);
                                ide.AddNodeFileButton(parts[0].trim(), size);
                                usedSpace += size;
                                LOGGER.info("FileManager found file " + parts[0].trim());
                            }
                        }
                        if (ide.fileList.listsize() == 0) {
                            ide.thandler.comment("No files found.", true);
                        } else {
                            ide.thandler.comment("Total file(s)   : " + Integer.toString(s.length), false);
                            ide.thandler.comment("Total size      : " + Integer.toString(usedSpace) + " bytes", true);
                        }
                        ide.NodeFileMgrPane.invalidate();
                        ide.NodeFileMgrPane.doLayout();
                        ide.NodeFileMgrPane.repaint();
                        ide.NodeFileMgrPane.requestFocusInWindow();
                        LOGGER.info("FileManager: File list parsing done, found " + ide.fileList.listsize() + " file(s).");
                    } catch (Exception e) {
                        LOGGER.info(e.toString());
                    }
                    try {
                        SerialObject.ins.removeEventListener();
                    } catch (Exception e) {
                    }
                    SerialObject.ins.addEventListener(new PortReader(this.ide));
                    ide.SendUnLock();
                }
            } catch (SerialPortException ex) {
                LOGGER.info(ex.toString());
            }
        } else if (event.isCTS()) {
            ide.UpdateLedCTS();
        } else if (event.isERR()) {
            LOGGER.info("FileManager: Unknown serial port error received.");
        }
    }
}