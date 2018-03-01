package com.mycompany.cloud.controller.test.netty.http.serverByMy.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * 处理服务端 channel.
 */
public class HttpServerNettyHandler extends ChannelInboundHandlerAdapter {

    public  boolean over = false;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf bufrecv = (ByteBuf) msg;
        byte[] message = new byte[bufrecv.readableBytes()];
        bufrecv.readBytes(message);
        String msgStr = new String(message);
        System.out.println("->Server : " + msgStr);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        System.out.println("----channelReadComplete ");
        ByteBuf buf = Unpooled.copiedBuffer("收到8081", CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
        //System.out.println(new String(response.content().array()));
        ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}