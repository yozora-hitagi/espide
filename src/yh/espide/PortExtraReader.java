package yh.espide;

import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import javax.swing.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class PortExtraReader implements SerialPortEventListener {

    Logger LOGGER = Logger.getLogger(PortExtraReader.class.getName());

    private String sendResp = "";

    EspIDE ide;

    SendBuf sendBuf;

    public PortExtraReader(EspIDE ide, ArrayList cmd) {
        this.ide = ide;
        sendBuf = new SendBuf();
        sendBuf.add(cmd);
    }


    public void start() {
        LOGGER.info("DataSender: start \"Smart Mode\"");
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
            String data = "";
            try {
                data = SerialObject.ins.readString(event.getEventValue());
            } catch (SerialPortException ex) {
                LOGGER.info(ex.toString());
            }
            sendResp = sendResp + data;

//            data = data.replace(">> ", "");
//            data = data.replace(">>", "");
//            data = data.replace("\r\n> ", "");
//            data = data.replace("\r\n\r\n", "\r\n");

            ide.thandler.add(data);
            if (sendResp.contains("> ") ){

                ide.watchDog.feed();
                sendResp = "";
                if (sendBuf.hasNext()) {
                    send();
                    Progress.ins.setValue((int) (sendBuf.rate() * 100));
                } else {  // send done
                    ide.StopSend();
                    ide.watchDog.stop();
                }
            }
            if (sendResp.contains("powered by Lua 5.")) {
                ide.StopSend();
                ide.watchDog.stop();
                String msg[] = {"ESP module reboot detected!", "Event: internal NodeMCU exception or power fail.", "Please, try again."};
                JOptionPane.showMessageDialog(null, msg);
            }
        } else if (event.isCTS())

        {
            ide.UpdateLedCTS();
        } else if (event.isERR())

        {
            LOGGER.info("FileManager: Unknown serial port error received.");
        }
    }
}