package guide.protocol.custom.server;

import guide.protocol.custom.MessageType;
import guide.protocol.custom.struct.Header;
import guide.protocol.custom.struct.NettyMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class LoginAuthRespHandler extends ChannelHandlerAdapter {


    private Map<String, Boolean> nodeCheck = new ConcurrentHashMap<String, Boolean>();
    private String[] whitekList = {"127.0.0.1", "192.168.1.104"};

    /**
     * Calls {@link ChannelHandlerContext#fireChannelRead(Object)} to forward to
     * the next {@link ChannelHandler} in the {@link ChannelPipeline}.
     * <p>
     * Sub-classes may override this method to change behavior.
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        NettyMessage message = (NettyMessage) msg;

        // 如果是握手请求消息，处理，其它消息透传
        if (message.getHeader() != null
                && message.getHeader().getType() == MessageType.LOGIN_REQ
                .value()) {
            String nodeIndex = ctx.channel().remoteAddress().toString();
            NettyMessage loginResp = null;
            // 重复登陆，拒绝
            if (nodeCheck.containsKey(nodeIndex)) {
                loginResp = buildResponse((byte) -1);
            } else {
                InetSocketAddress address = (InetSocketAddress) ctx.channel()
                        .remoteAddress();
                String ip = address.getAddress().getHostAddress();
                boolean isOK = false;
                for (String WIP : whitekList) {
                    if (WIP.equals(ip)) {
                        isOK = true;
                        break;
                    }
                }
                loginResp = isOK ? buildResponse((byte) 0)
                        : buildResponse((byte) -1);
                if (isOK)
                    nodeCheck.put(nodeIndex, true);
            }
            System.out.println("The login response is : " + loginResp
                    + " body [" + loginResp.getBody() + "]");
            ctx.writeAndFlush(loginResp);
        } else {

            //fireChannelRead表示传递消息至下一个处理器，因为pipline的原因，我们可能有一个链式的处理队列，
            //这个队列有头和尾之分，那么消息通常从头处理器进入。
            //假设现有队列A、B、C，一条消息消息首先进入A，如果A不显示调用fireChannelRead将消息传递至B的话，
            // 那么B和C永远收不到消息。
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildResponse(byte result) {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_RESP.value());
        message.setHeader(header);
        message.setBody(result);
        return message;
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        nodeCheck.remove(ctx.channel().remoteAddress().toString());// 删除缓存
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }
}
