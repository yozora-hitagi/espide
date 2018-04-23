package yh.espide;

import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import java.util.logging.Logger;

public class PortReader implements SerialPortEventListener {

    Logger LOGGER= Logger.getLogger(PortReader.class.getName());

    EspIDE ide;

    public PortReader(EspIDE ide){
        this.ide=ide;
    }

    public void serialEvent(SerialPortEvent event) {
        if (event.isRXCHAR() && event.getEventValue() > 0) {
            String data = null;
            try {
                data = SerialObject.ins.readString(event.getEventValue());
            } catch (SerialPortException ex) {
                LOGGER.info(ex.toString());
            }
            if (null != data) {
                ide.thandler.add(data);
                if (SerialObject.ins.getSerialPortStatus().isConnected()) {
                    if (!data.trim().isEmpty()) {
                        if (data.contains("\r\n>>>")) {
                            ide.thandler.comment("MicroPython firmware detected.", true);
                            ide.btnSend("import sys; print(\"MicroPython ver:\",sys.version_info)");

                        } else if (data.contains("\r\n>")) {
                            ide.thandler.comment("NodeMCU firmware detected.", true);
                            ide.btnSend("=node.info()");

                        } else if (data.contains("\r\nERR")) {
                            ide.thandler.comment("AT-based firmware detected.", true);
                            ide.btnSend("AT+GMR");

                        } else {
                            ide.thandler.comment("Can't detect firmware.", true);
                        }

                        SerialObject.ins.setSerialPortStatus(SerialPortStatus.OPENED);
                        ide.UpdateButtons();
                    }
                }
            }


        } else if (event.isCTS()) {
            ide.UpdateLedCTS();
        } else if (event.isERR()) {
            LOGGER.info("FileManager: Unknown serial port error received.");
        }
    }
}
