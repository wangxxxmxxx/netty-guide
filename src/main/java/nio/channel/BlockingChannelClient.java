package nio.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author wangxueming
 * @create 2020-02-13 0:44
 * @description
 */
public class BlockingChannelClient {
    public static void main(String[] args) {
        try {
            SocketChannel sc = SocketChannel.open();
            sc.connect(new InetSocketAddress("127.0.0.1", 1234));
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            writeString(buffer, sc,"hello");
            writeString(buffer, sc,"world");
            writeString(buffer, sc,"exit");
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeString(ByteBuffer buffer, SocketChannel sc,String str) {
        buffer.clear();
        buffer.put(str.getBytes()).flip();
        try {
            sc.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
