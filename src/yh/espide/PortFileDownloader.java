package yh.espide;

import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import javax.swing.*;
import java.io.IOException;
import java.util.logging.Logger;

public class PortFileDownloader implements SerialPortEventListener {
    Logger LOGGER = Logger.getLogger(PortFileDownloader.class.getName());

    private String filename;
    private boolean open;
    private int packet_size;

    private Buffer buffer = new Buffer();
    private int packet_count = 0;



    SendBuf sendBuf = new SendBuf();
    boolean sendPending = false;

    String sendResp = "";
    String rcv_data_buf = "";
    EspIDE ide;

    public PortFileDownloader(EspIDE ide, String filename, boolean open, int packet_size) {
        this.ide = ide;

        this.filename = filename;
        this.open = open;
        this.packet_size = packet_size;


        String cmd = Context.GetLua("/yh/espide/file_download.lua", filename);
        String[] s = cmd.split("\r?\n");
        for (String subs : s) {
            sendBuf.add(subs);
        }


    }

    private void send(){
        if (sendBuf.hasNext()) {
            ide.send(ide.addCR(sendBuf.next()), false);
        }
    }

    public void start() {
        LOGGER.info("Downloader: Start");
        ide.thandler.comment("Download file \"" + filename + "\"...", true);
        ide.watchDog.start();
        send();

    }


    public void serialEvent(SerialPortEvent event) {

        if (event.isRXCHAR() && event.getEventValue() > 0) {
            try {
                byte[] b = SerialObject.ins.readBytes();
                String data = new String(b);
                sendResp = sendResp + data;
                rcv_data_buf = rcv_data_buf + data;
                //TerminalAdd(data);
            } catch (SerialPortException e) {
                LOGGER.info(e.toString());
            }
            if (sendResp.contains("> ")) {
                try {
                    ide.watchDog.feed();
                } catch (Exception e) {
                    LOGGER.info(e.toString());
                }
                sendResp = "";
                send();
            }

            if ((rcv_data_buf.lastIndexOf("~~~DATA-END~~~") >= 0) && (rcv_data_buf.lastIndexOf("~~~DATA-START~~~") >= 0)) {
                // we got full packet
                String buf = rcv_data_buf.substring(0, rcv_data_buf.indexOf("~~~DATA-END~~~") + 14);
                FileDownloadPacket packet = new FileDownloadPacket(buf);
                packet_count++;

                rcv_data_buf = rcv_data_buf.substring(rcv_data_buf.indexOf("~~~DATA-END~~~") + 14); // and remove it from buf


                //有一段完整的数据， 进行验证
                if (packet.crc == ide.CRC(packet.data.getBytes())) {
                    try {
                        ide.watchDog.feed();
                    } catch (Exception e) {
                        LOGGER.info(e.toString());
                    }

                    try {
                        buffer.write(packet.data.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    LOGGER.info("Downloader: Receive packet: " + packet.index + "/" + Integer.toString(packet_size)
                            + ", size:" + packet.len
                            + ", CRC check: Success");
                } else {
                    try {
                        ide.watchDog.stop();
                    } catch (Exception e) {
                        LOGGER.info(e.toString());
                    }
                    LOGGER.info("Downloader: Receive packets: " + packet.index + "/" + Integer.toString(packet_size)
                            + ", size expected:" + packet.len
                            + ", size received:" + Integer.toString(buffer.size())
                            + "\r\n, CRC expected :" + packet.crc
                            + "  CRC received :" + Integer.toString(ide.CRC(packet.data.getBytes())));
                    LOGGER.info("Downloader: FAIL.");


                    buffer.clean();

                    ide.FileDownloadFinisher(null, this.filename, this.open);
                }

                Progress.ins.setValue(buffer.size() / this.packet_size);

            } else if (rcv_data_buf.lastIndexOf("~~~DATA-TOTAL-END~~~") >= 0) {
                String l = rcv_data_buf.substring(rcv_data_buf.lastIndexOf("~~~DATA-TOTAL-START~~~") + 22, rcv_data_buf.lastIndexOf("~~~DATA-TOTAL-END~~~"));
                try {
                    int c = Integer.parseInt(l);
                    if (c == this.packet_count) {
                        try {
                            ide.watchDog.stop();
                        } catch (Exception e) {
                            LOGGER.info(e.toString());
                        }
                        Progress.ins.setValue(100);
                        LOGGER.info("Downloader: Receive final sequense. File download: Success");
                        //log(rx_data);
                        ide.FileDownloadFinisher(buffer, this.filename, this.open);
                    }
                } catch (NumberFormatException e) {
                }

            }

        } else if (event.isCTS()) {
            ide.UpdateLedCTS();
        } else if (event.isERR()) {
            LOGGER.info("Downloader: Unknown serial port error received.");
            ide.FileDownloadFinisher(null, this.filename, this.open);
        }
    }
}