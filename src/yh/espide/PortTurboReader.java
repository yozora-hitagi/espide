package yh.espide;

import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import java.util.List;
import java.util.logging.Logger;

public class PortTurboReader implements SerialPortEventListener {
    Logger LOGGER = Logger.getLogger(PortTurboReader.class.getName());

    private String sendResp = "";

    EspIDE ide;
    SendBuf sendBuf;

    public PortTurboReader(EspIDE ide, List<String> cmds) {
        this.ide = ide;
        sendBuf = new SendBuf();
        sendBuf.add(cmds);
    }

    public void start() {
        LOGGER.info("DataTurboSender: start \"Smart Mode\"");
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
            String l = data.replace("\r", "<CR>");
            l = l.replace("\n", "<LF>");
            l = l.replace("`", "<OK>");
            LOGGER.info("recv:" + l);
            ide.thandler.add(data);
            if (sendResp.contains("> ")) {
                ide.watchDog.feed();
                sendResp = "";
                if (sendBuf.hasNext()) {
                    send();
                    Progress.ins.setValue((int) (sendBuf.rate() * 100));
                }
            } else { // send done
                ide.watchDog.stop();
                ide.StopSend();
            }
        } else if (event.isCTS()) {
            ide.UpdateLedCTS();
        } else if (event.isERR()) {
            LOGGER.info("FileManager: Unknown serial port error received.");
        }
    }
}