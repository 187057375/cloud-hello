package com.mycompany.cloud.controller.test.netty.http.serverByNetty;

import com.mycompany.cloud.controller.test.netty.http.serverByNetty.message.impl.HttpMessagePushHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;


public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    private static final String REQUEST_FAVICON_URI = "/favicon.ico";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest){
            HttpRequest request = (HttpRequest) msg;
            if (!REQUEST_FAVICON_URI.equals(request.getUri())){
                new HttpMessagePushHandler().hand(ctx, request);
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }


}
