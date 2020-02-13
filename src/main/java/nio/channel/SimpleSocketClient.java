package nio.channel;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author wangxueming
 * @create 2020-02-13 0:40
 * @description
 */
public class SimpleSocketClient {public static void main(String[] args) {
    Socket socket = new Socket();
    try {
        socket.connect(new InetSocketAddress("127.0.0.1", 1234));
        OutputStream os = socket.getOutputStream();
        os.write("hello".getBytes());
        os.write("world".getBytes());
        os.write("exit".getBytes());
        os.close();
        socket.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

}
