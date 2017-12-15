package yh.espide;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by yozora on 2017/12/15.
 */
public class FileDownloadHandler {

    public static final ArrayList<FileDownloadPacket> PACKETS = new ArrayList<>();


    public static final FileDownloadHandler ins = new FileDownloadHandler();


//    PipedOutputStream pipeos =new PipedOutputStream();
//    PipedInputStream pipeis=new PipedInputStream();

    Buffer buffer = new Buffer();


    public void write(byte[] b) throws IOException {
        buffer.write(b);
    }


    public String get() {
        return new String(buffer.toByteArray());
    }


    public void reset() {
        buffer.clean();
    }


}
