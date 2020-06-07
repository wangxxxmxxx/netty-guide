package netty.tomcat.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import netty.tomcat.config.CustomConfig;
import netty.tomcat.http.Request;
import netty.tomcat.http.Response;
import netty.tomcat.http.Servlet;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

/**
 * @author wangxueming
 * @create 2020-02-14 12:13
 * @description
 */
public class TomcatHandler extends ChannelInboundHandlerAdapter {


    private static final Map<Pattern, Class<?>> servletMapping = new HashMap<Pattern, Class<?>>();

    static {

        CustomConfig.load("netty/tomcat/web.properties");

        for (String key : CustomConfig.getKeys()) {
            if (key.startsWith("servlet")) {
                String name = key.replaceFirst("servlet.", "");
                if (name.indexOf(".") != -1) {
                    name = name.substring(0, name.indexOf("."));
                } else {
                    continue;
                }
                String pattern = CustomConfig.getString("servlet." + name + ".urlPattern");
                pattern = pattern.replaceAll("\\*", ".*");
                String className = CustomConfig.getString("servlet." + name + ".className");
                if (!servletMapping.containsKey(pattern)) {
                    try {
                        servletMapping.put(Pattern.compile(pattern), Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest r = (HttpRequest) msg;
            Request request = new Request(ctx, r);
            Response response = new Response(ctx, r);
            String uri = request.getUri();
            String method = request.getMethod();

            System.out.println("收到HTTP请求，uri[" + uri + "], method[" + method + "]");


            boolean hasPattern = false;
            for (Entry<Pattern, Class<?>> entry : servletMapping.entrySet()) {
                if (entry.getKey().matcher(uri).matches()) {
                    Servlet servlet = (Servlet) entry.getValue().newInstance();//正常生命周期，servlet应该值实例化一次
                    if ("get".equalsIgnoreCase(method)) {
                        servlet.doGet(request, response);
                    } else {
                        servlet.doPost(request, response);
                    }
                    hasPattern = true;
                }
            }

            if (!hasPattern) {
                String out = String.format("404 NotFound URL%s for method %s", uri, method);
                response.write(out, 404);
                return;
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}