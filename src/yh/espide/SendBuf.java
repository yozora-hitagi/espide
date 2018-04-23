package yh.espide;

import java.util.ArrayList;
import java.util.List;

public class SendBuf {

    private List<String> buf;

    private int index = 0;

    public SendBuf() {
        buf = new ArrayList();
    }

    public void add(String s) {
        if (null == s) {
            return;
        }
        buf.add(s);
    }

    public void add(List<String> l) {
        if (null == l) {
            return;
        }
        buf.addAll(l);
    }

    public boolean hasNext() {
        return index < buf.size();
    }


    public String next() {
        if (hasNext()) {
            return buf.get(index++);
        } else {
            return null;
        }
    }



    public double rate() {
        int div = buf.size() - 1;
        div = div == 0 ? 1 : div;
        return index * 1.0 / div;
    }

}
