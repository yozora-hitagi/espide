package yh.espide;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class WatchDog {
    Logger LOGGER = Logger.getLogger(WatchDog.class.getName());

    Timer timeout;

    public WatchDog(EspIDE ide){
        if (Config.ins.isDumb_mode()) {
            return;
        }
        int delay = Config.ins.getAnswer_timeout() * 1000;
        if (delay == 0) {
            delay = 300;
        }
        timeout = new Timer(delay, evt -> {
            ide.StopSend();
            Toolkit.getDefaultToolkit().beep();
            ide.thandler.comment("Waiting answer from ESP - Timeout reached. Command aborted.", true);
            LOGGER.info("Waiting answer from ESP - Timeout reached. Command aborted.");
        });
        timeout.setRepeats(false);
        timeout.setInitialDelay(delay);

    }

    public void start(){
        timeout.start();
    }

    public void feed(){
        timeout.restart();
    }

    public void stop(){
        timeout.stop();
    }
}
