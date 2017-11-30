package yh.espide;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 * Created by yozora on 2017/11/30.
 */
public class Buffer extends ByteArrayOutputStream {


    public void compress() {
        buf = Arrays.copyOf(buf, count);
    }

    public void clean(){
        reset();
        compress();
    }

}
