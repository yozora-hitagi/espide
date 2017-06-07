package yh.espide;

import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.ShorthandCompletion;

/**
 * Created by yozora on 2017/1/9.
 */
public class CompletionProviderCreator {
    public static CompletionProvider create(FirmwareType type) {

        DefaultCompletionProvider provider = new DefaultCompletionProvider();
        if (type.eq(FirmwareType.NodeMCU)) {
            provider.addCompletion(new BasicCompletion(provider, "function end"));
            provider.addCompletion(new BasicCompletion(provider, "function"));
            provider.addCompletion(new BasicCompletion(provider, "function return end"));
            provider.addCompletion(new BasicCompletion(provider, "end"));
            provider.addCompletion(new BasicCompletion(provider, "do"));
            provider.addCompletion(new BasicCompletion(provider, "print(\"\")"));
            provider.addCompletion(new BasicCompletion(provider, "if"));
            provider.addCompletion(new BasicCompletion(provider, "else"));
            provider.addCompletion(new BasicCompletion(provider, "elseif"));
            provider.addCompletion(new BasicCompletion(provider, "if else end"));
            provider.addCompletion(new BasicCompletion(provider, "while"));
            provider.addCompletion(new BasicCompletion(provider, "while do end"));
            provider.addCompletion(new BasicCompletion(provider, "do end"));
            provider.addCompletion(new BasicCompletion(provider, "for"));
            provider.addCompletion(new BasicCompletion(provider, "for do end"));
            provider.addCompletion(new BasicCompletion(provider, "repeat"));
            provider.addCompletion(new BasicCompletion(provider, "until"));
            provider.addCompletion(new BasicCompletion(provider, "repeat until"));
            provider.addCompletion(new BasicCompletion(provider, "for"));
            provider.addCompletion(new BasicCompletion(provider, "for key, value in pairs() do\r\nend"));
            provider.addCompletion(new BasicCompletion(provider, "for do end"));
            provider.addCompletion(new BasicCompletion(provider, "wifi.setmode(wifi.STATION)"));
            provider.addCompletion(new BasicCompletion(provider, "wifi.getmode()"));
            provider.addCompletion(new BasicCompletion(provider, "wifi.startsmart()"));
            provider.addCompletion(new BasicCompletion(provider, "wifi.stopsmart()"));
            provider.addCompletion(new BasicCompletion(provider, "wifi.sta.config(\"SSID\",\"password\")"));
            provider.addCompletion(new BasicCompletion(provider, "wifi.sta.connect()"));
            provider.addCompletion(new BasicCompletion(provider, "wifi.sta.disconnect()"));
            provider.addCompletion(new BasicCompletion(provider, "wifi.sta.autoconnect()"));
            provider.addCompletion(new BasicCompletion(provider, "wifi.sta.getip()"));
            provider.addCompletion(new BasicCompletion(provider, "wifi.sta.getmac()"));
            provider.addCompletion(new BasicCompletion(provider, "wifi.sta.getap()"));
            provider.addCompletion(new BasicCompletion(provider, "wifi.sta.status()"));
            provider.addCompletion(new BasicCompletion(provider, "wifi.ap.config()"));
            provider.addCompletion(new BasicCompletion(provider, "wifi.ap.getip()"));
            provider.addCompletion(new BasicCompletion(provider, "wifi.ap.getmac()"));
            provider.addCompletion(new BasicCompletion(provider, "gpio.mode(pin,gpio.OUTPUT)"));
            provider.addCompletion(new BasicCompletion(provider, "gpio.write(pin,gpio.HIGH)"));
            provider.addCompletion(new BasicCompletion(provider, "gpio.write(pin,gpio.LOW)"));
            provider.addCompletion(new BasicCompletion(provider, "gpio.read(pin)"));
            provider.addCompletion(new BasicCompletion(provider, "gpio.trig(0, \"act\",func)"));
            provider.addCompletion(new BasicCompletion(provider, "conn=net.createConnection(net.TCP, 0)"));
            provider.addCompletion(new BasicCompletion(provider, "net.createConnection(net.TCP, 0)"));
            provider.addCompletion(new BasicCompletion(provider, "on(\"receive\", function(conn, payload) print(payload) end )"));
            provider.addCompletion(new BasicCompletion(provider, "connect(80,\"0.0.0.0\")"));
            provider.addCompletion(new BasicCompletion(provider, "send(\"GET / HTTP/1.1\\r\\nHost: www.baidu.com\\r\\nConnection: keep-alive\\r\\nAccept: */*\\r\\n\\r\\n\")"));
            provider.addCompletion(new BasicCompletion(provider, "srv=net.createServer(net.TCP)"));
            provider.addCompletion(new BasicCompletion(provider, "srv:listen(80,function(conn) \nconn:on(\"receive\",function(conn,payload) \nprint(payload) \nconn:send(\"<h1> Hello, NodeMcu.</h1>\")\nend) \nconn:on(\"sent\",function(conn) conn:close() end)\nend)"));
            provider.addCompletion(new BasicCompletion(provider, "net.createServer(net.TCP, timeout)"));
            provider.addCompletion(new BasicCompletion(provider, "net.server.listen(port,[ip],function(net.socket))"));
            provider.addCompletion(new BasicCompletion(provider, "dns(domain, function(net.socket, ip))"));
            provider.addCompletion(new BasicCompletion(provider, "pwm.setduty(0,0)"));
            provider.addCompletion(new BasicCompletion(provider, "pwm.getduty(0)"));
            provider.addCompletion(new BasicCompletion(provider, "pwm.setup(0,0,0)"));
            provider.addCompletion(new BasicCompletion(provider, "pwm.start(0)"));
            provider.addCompletion(new BasicCompletion(provider, "pwm.close(0)"));
            provider.addCompletion(new BasicCompletion(provider, "pwm.setclock(0, 100)"));
            provider.addCompletion(new BasicCompletion(provider, "pwm.getclock(0)"));
            provider.addCompletion(new BasicCompletion(provider, "pwm.close(0)"));
            provider.addCompletion(new BasicCompletion(provider, "file.open(\"\",\"r\")"));
            provider.addCompletion(new BasicCompletion(provider, "file.writeline()"));
            provider.addCompletion(new BasicCompletion(provider, "file.readline()"));
            provider.addCompletion(new BasicCompletion(provider, "file.write()"));
            provider.addCompletion(new BasicCompletion(provider, "file.close()"));
            provider.addCompletion(new BasicCompletion(provider, "file.remove()"));
            provider.addCompletion(new BasicCompletion(provider, "file.flush()"));
            provider.addCompletion(new BasicCompletion(provider, "file.seek()"));
            provider.addCompletion(new BasicCompletion(provider, "file.list()"));
            provider.addCompletion(new BasicCompletion(provider, "node.restart()"));
            provider.addCompletion(new BasicCompletion(provider, "node.dsleep()"));
            provider.addCompletion(new BasicCompletion(provider, "node.chipid()"));
            provider.addCompletion(new BasicCompletion(provider, "node.heap()"));
            provider.addCompletion(new BasicCompletion(provider, "node.key(type, function())"));
            provider.addCompletion(new BasicCompletion(provider, "node.led()"));
            provider.addCompletion(new BasicCompletion(provider, "node.input()"));
            provider.addCompletion(new BasicCompletion(provider, "node.output()"));
            provider.addCompletion(new BasicCompletion(provider, "tmr.alarm(0,1000,1,function()\nend)"));
            provider.addCompletion(new BasicCompletion(provider, "tmr.delay()"));
            provider.addCompletion(new BasicCompletion(provider, "tmr.now()"));
            provider.addCompletion(new BasicCompletion(provider, "tmr.stop(id)"));
            provider.addCompletion(new BasicCompletion(provider, "tmr.wdclr()"));
            provider.addCompletion(new BasicCompletion(provider, "dofile(\"\")"));
        }
        if (type.eq(FirmwareType.MicroPython)) { // MicroPython
            provider.addCompletion(new BasicCompletion(provider, "import "));
            provider.addCompletion(new BasicCompletion(provider, "import network"));
            provider.addCompletion(new BasicCompletion(provider, "import time"));
            provider.addCompletion(new BasicCompletion(provider, "import Timer"));
            provider.addCompletion(new BasicCompletion(provider, "from machine import Pin"));
            provider.addCompletion(new BasicCompletion(provider, "from machine import Pin, PWM"));
            provider.addCompletion(new BasicCompletion(provider, "from machine import ADC"));
            provider.addCompletion(new BasicCompletion(provider, "from machine import Pin, SPI"));
            provider.addCompletion(new BasicCompletion(provider, "from machine import Pin, I2C"));
            provider.addCompletion(new BasicCompletion(provider, "import onewire"));
            provider.addCompletion(new BasicCompletion(provider, "from neopixel import NeoPixel"));
            provider.addCompletion(new BasicCompletion(provider, "import neopixel"));
            provider.addCompletion(new BasicCompletion(provider, "import esp"));
            provider.addCompletion(new BasicCompletion(provider, "json"));
            provider.addCompletion(new BasicCompletion(provider, "ubinascii.hexlify(data[, sep])"));
            provider.addCompletion(new BasicCompletion(provider, "ubinascii.unhexlify(data)"));
            provider.addCompletion(new BasicCompletion(provider, "ubinascii.a2b_base64(data)"));
            provider.addCompletion(new BasicCompletion(provider, "ubinascii.b2a_base64(data)"));
            provider.addCompletion(new BasicCompletion(provider, "machine.freq(160000000)"));
            provider.addCompletion(new BasicCompletion(provider, "esp.osdebug(None)"));
            provider.addCompletion(new BasicCompletion(provider, "esp.osdebug(0)"));
            provider.addCompletion(new BasicCompletion(provider, "wlan = network.WLAN(network.STA_IF)"));
            provider.addCompletion(new BasicCompletion(provider, "wlan.active(True)"));
            provider.addCompletion(new BasicCompletion(provider, "wlan.scan()"));
            provider.addCompletion(new BasicCompletion(provider, "wlan.isconnected()"));
            provider.addCompletion(new BasicCompletion(provider, "wlan.connect('essid', 'password')"));
            provider.addCompletion(new BasicCompletion(provider, "wlan.ifconfig()"));
            provider.addCompletion(new BasicCompletion(provider, "ap = network.WLAN(network.AP_IF)"));
            provider.addCompletion(new BasicCompletion(provider, "ap.active(True)"));
            provider.addCompletion(new BasicCompletion(provider, "ap.config(essid='ESP-AP')"));
            provider.addCompletion(new BasicCompletion(provider, "time.sleep(1)"));
            provider.addCompletion(new BasicCompletion(provider, "time.sleep_ms(500)"));
            provider.addCompletion(new BasicCompletion(provider, "time.sleep_us(10)"));
            provider.addCompletion(new BasicCompletion(provider, "time.ticks_diff(start, time.ticks_ms())"));
            provider.addCompletion(new BasicCompletion(provider, "tim = Timer(-1)"));
            provider.addCompletion(new BasicCompletion(provider, "tim.init(period=5000, mode=Timer.ONE_SHOT, callback=lambda t:print(1))"));
            provider.addCompletion(new BasicCompletion(provider, "tim.init(period=2000, mode=Timer.PERIODIC, callback=lambda t:print(2))"));
            provider.addCompletion(new BasicCompletion(provider, "Pin(0, Pin.OUT)"));
            provider.addCompletion(new BasicCompletion(provider, ".high()"));
            provider.addCompletion(new BasicCompletion(provider, ".low()"));
            provider.addCompletion(new BasicCompletion(provider, ".value(1)"));
            provider.addCompletion(new BasicCompletion(provider, "Pin(2, Pin.IN)"));
            provider.addCompletion(new BasicCompletion(provider, "Pin(4, Pin.IN, Pin.PULL_UP)"));
            provider.addCompletion(new BasicCompletion(provider, "Pin(5, Pin.OUT, value=1)"));
            provider.addCompletion(new BasicCompletion(provider, "PWM(Pin(0))"));
            provider.addCompletion(new BasicCompletion(provider, ".freq()"));
            provider.addCompletion(new BasicCompletion(provider, ".freq(1000)"));
            provider.addCompletion(new BasicCompletion(provider, ".duty()"));
            provider.addCompletion(new BasicCompletion(provider, ".duty(200)"));
            provider.addCompletion(new BasicCompletion(provider, ".deinit()"));
            provider.addCompletion(new BasicCompletion(provider, "PWM(Pin(2), freq=500, duty=512)"));
            provider.addCompletion(new BasicCompletion(provider, "ADC(0)"));
            provider.addCompletion(new BasicCompletion(provider, ".read()"));
            provider.addCompletion(new BasicCompletion(provider, "SPI(baudrate=100000, polarity=1, phase=0, sck=Pin(0), mosi=Pin(2), miso=Pin(4))"));
            provider.addCompletion(new BasicCompletion(provider, ".init(baudrate=200000)"));
            provider.addCompletion(new BasicCompletion(provider, ".readinto(buf)"));
            provider.addCompletion(new BasicCompletion(provider, ".write_readinto(b'1234', buf)"));
            provider.addCompletion(new BasicCompletion(provider, ".write_readinto(buf, buf)"));
            provider.addCompletion(new BasicCompletion(provider, "I2C(scl=Pin(5), sda=Pin(4), freq=100000)"));
            provider.addCompletion(new BasicCompletion(provider, "machine.RTC()"));
            provider.addCompletion(new BasicCompletion(provider, ".irq(trigger=rtc.ALARM0, wake=machine.DEEPSLEEP)"));
            provider.addCompletion(new BasicCompletion(provider, "machine.reset_cause()"));
            provider.addCompletion(new BasicCompletion(provider, "machine.DEEPSLEEP_RESET"));
            provider.addCompletion(new BasicCompletion(provider, "rtc.alarm(rtc.ALARM0, 10000)"));
            provider.addCompletion(new BasicCompletion(provider, ".scan()"));
            provider.addCompletion(new BasicCompletion(provider, ".reset()"));
            provider.addCompletion(new BasicCompletion(provider, "onewire.DS18B20(ow)"));
            provider.addCompletion(new BasicCompletion(provider, ".start_measure()"));
            provider.addCompletion(new BasicCompletion(provider, ".get_temp(rom)"));
            provider.addCompletion(new BasicCompletion(provider, "NeoPixel(pin, 8)"));
        }

        provider.addCompletion(new ShorthandCompletion(provider, "sysout",
                "System.out.println(", "System.out.println("));
        provider.addCompletion(new ShorthandCompletion(provider, "syserr",
                "System.err.println(", "System.err.println("));

        return provider;

    }
}
