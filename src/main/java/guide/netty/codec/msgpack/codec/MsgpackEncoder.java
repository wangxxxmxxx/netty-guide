package guide.netty.codec.msgpack.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * @author wangxueming
 * @create 2020-06-09 0:16
 * @description
 */
public class MsgpackEncoder extends MessageToByteEncoder<Object> {
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        MessagePack msgpack = new MessagePack();

        //序列化
        byte[] raw = msgpack.write(msg);
        out.writeBytes(raw);
    }
}
