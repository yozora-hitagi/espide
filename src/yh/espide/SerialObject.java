package yh.espide;

import jssc.SerialPort;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import java.util.logging.Logger;

public class SerialObject {

    public static final int portMask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS;


    public static SerialObject ins;


    Logger LOG = Logger.getLogger(SerialObject.class.getName());

    private SerialPort serialPort;
    private SerialPortStatus serialPortStatus = SerialPortStatus.CLOSED;

    public SerialObject(String name) {
        serialPort = new SerialPort(name);
    }

    public SerialPortStatus getSerialPortStatus() {
        return serialPortStatus;
    }

    public void setSerialPortStatus(SerialPortStatus status) {
        serialPortStatus = status;
    }

    public boolean writeString(String s) throws SerialPortException {
        return serialPort.writeString(s);
    }

    public String readString(int s) throws SerialPortException {
        return serialPort.readString(s);
    }

    public boolean writeByte(byte s) throws SerialPortException {
        return serialPort.writeByte(s);
    }

    public byte[] readBytes() throws SerialPortException {
        return serialPort.readBytes();
    }

    public boolean writeBytes(byte[] s) throws SerialPortException {
        return serialPort.writeBytes(s);
    }

    public boolean openPort() throws SerialPortException {
        if( serialPort.openPort()){
            serialPortStatus=SerialPortStatus.CONNECTED;
            return true;
        }else{
            return false;
        }
    }

    public boolean closePort() throws SerialPortException {

        if( serialPort.closePort()){
            serialPortStatus=SerialPortStatus.CLOSED;
            return true;
        }else{
            return false;
        }
    }


    public boolean setParams(int baudRate, int dataBits, int stopBits, int parity, boolean setRTS, boolean setDTR) throws SerialPortException {
        return serialPort.setParams(baudRate, dataBits, stopBits, parity, setRTS, setDTR);
    }

    public boolean isCTS() throws SerialPortException {
        return serialPort.isCTS();
    }

    public void setDTR(boolean dtr) throws SerialPortException {
        serialPort.setDTR(dtr);
    }

    public void setRTS(boolean dtr) throws SerialPortException {
        serialPort.setRTS(dtr);
    }


    public void removeEventListener() throws SerialPortException {
        if (serialPort != null) {
            serialPort.removeEventListener();
        }
    }

    public void addEventListener(SerialPortEventListener listener) throws SerialPortException {
        if (serialPort != null) {
            serialPort.addEventListener(listener, portMask);
        } else {
            LOG.warning("serialPort is null!");
        }
    }
}
