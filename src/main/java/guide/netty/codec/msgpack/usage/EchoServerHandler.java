package guide.netty.codec.msgpack.usage;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import org.msgpack.type.Value;

import java.util.List;

@Sharable
public class EchoServerHandler extends ChannelHandlerAdapter {

    int counter = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
//        UserInfo userInfo = (UserInfo) msg;
        System.out.println("This is " + ++counter + " times receive client");
        System.out.println("msg.class:" + msg.getClass());
        List<Value> list = (List<Value>) msg;
        Value value = (Value) msg;
        System.out.println("size:" + list.size());
        System.out.println("value:" + value);
        System.out.println();

        ctx.writeAndFlush(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();// 发生异常，关闭链路
    }
}
