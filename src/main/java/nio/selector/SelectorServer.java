package nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author wangxueming
 * @create 2020-02-13 11:02
 * @description
 */
public class SelectorServer {
    private static final int PORT = 1234;
    private static ByteBuffer buffer = ByteBuffer.allocate(1024);

    public static void main(String[] args) {
        try {
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.bind(new InetSocketAddress(PORT));
            ssc.configureBlocking(false);	//如果一个 Channel 要注册到 Selector 中, 那么这个 Channel 必须是非阻塞的,
            //即channel.configureBlocking(false);不能将FileChannel与Selector一起使用,因为FileChannel不能切换到非阻塞模式
            //另外通道一旦被注册，将不能再回到阻塞状态，此时若调用通道的configureBlocking(true)将抛出BlockingModeException异常
            //1.register()
            Selector selector = Selector.open();
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("REGISTER CHANNEL , CHANNEL NUMBER IS:" + selector.keys().size());

            while (true) {
                //2.select()
                int n = selector.select();
                if (n == 0) {
                    continue;
                }
                //3.轮询SelectionKey
                Iterator<SelectionKey> iterator = (Iterator) selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    //如果满足Acceptable条件，则必定是一个ServerSocketChannel
                    if (key.isAcceptable()) {
                        ServerSocketChannel sscTemp = (ServerSocketChannel) key.channel();
                        //得到一个连接好的SocketChannel，并把它注册到Selector上，兴趣操作为READ
                        SocketChannel socketChannel = sscTemp.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("REGISTER CHANNEL , CHANNEL NUMBER IS:" + selector.keys().size());
                    }
                    if (key.isConnectable()) {
                        // a connection was established with a remote server.

                    }
                    if (key.isWritable()) {
                        // a channel is ready for writing
                    }
                    //如果满足Readable条件，则必定是一个SocketChannel
                    if (key.isReadable()) {
                        //读取通道中的数据
                        SocketChannel channel = (SocketChannel) key.channel();
                        readFromChannel(channel);
                    }
                    //4.remove SelectionKey
                    iterator.remove();
                    //在每次迭代时, 我们都调用 "keyIterator.remove()" 将这个 key 从迭代器中删除, 因为 select()
                    //方法仅仅是简单地将就绪的 IO 操作放到 selectedKeys 集合中, 因此如果我们从 selectedKeys 获取到一个 key,
                    //但是没有将它删除, 那么下一次 select 时, 这个 key 所对应的 IO 事件还在 selectedKeys 中.
                    //例如此时我们收到 OP_ACCEPT 通知, 然后我们进行相关处理, 但是并没有将这个 Key 从 SelectedKeys 中删除,
                    //那么下一次 select() 返回时 我们还可以在 SelectedKeys 中获取到 OP_ACCEPT 的 key.
                    //注意, 我们可以动态更改 SekectedKeys 中的 key 的 interest set. 例如在 OP_ACCEPT 中, 我们可以将
                    //interest set 更新为 OP_READ, 这样 Selector 就会将这个 Channel 的 读 IO 就绪事件包含进来了.
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readFromChannel(SocketChannel channel) {
        buffer.clear();
        try {
            while (channel.read(buffer) > 0) {
                buffer.flip();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                System.out.println("READ FROM CLIENT:" + new String(bytes));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
