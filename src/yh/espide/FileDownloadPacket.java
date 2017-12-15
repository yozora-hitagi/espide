package yh.espide;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yozora on 2017/12/15.
 */
public class FileDownloadPacket {

    private static final Pattern p = Pattern.compile("~~~DATA-START~~~([\\s\\S]*?)~~~DATA-LENGTH~~~(\\d*?)~~~DATA-N~~~(\\d*?)~~~DATA-CRC~~~(\\d*?)~~~DATA-END~~~");

    String data;
    int len;
    int index;
    int crc;

    //  ~~~DATA-START~~~buf~~~DATA-LENGTH~~~string.len(buf)~~~DATA-N~~~i~~~DATA-CRC~~~CheckSum~~~DATA-END
    //0        1                  2                               3            4                     5
    public FileDownloadPacket(String buf) {
        Matcher m = p.matcher(buf);
        if (m.find()) {
            data = m.group(1);
            len = Integer.parseInt(m.group(2));
            index = Integer.parseInt(m.group(3));
            crc = Integer.parseInt(m.group(4));
        }


    }

    public static void main(String[] args) {
        FileDownloadPacket f = new FileDownloadPacket("~~~DATA-START~~~buf~~~DATA-LENGTH~~~12~~~DATA-N~~~12~~~DATA-CRC~~~12~~~DATA-END");
        System.out.println();
    }

}
