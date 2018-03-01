package com.mycompany.cloud.controller.test.netty.http.serverByNetty.message.impl;


import com.mycompany.cloud.controller.test.netty.http.serverByNetty.message.HttpMessageHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.util.List;


public class HttpMessagePushHandler implements HttpMessageHandler {


    public void hand(ChannelHandlerContext ctx, HttpRequest request) throws IOException {
        System.out.println("request info: -----------------------------------begin");
        System.out.println("uri:" + request.getUri());
        System.out.println("protocol:" + request.getProtocolVersion());
        System.out.println("method:" + request.getMethod().name());
        HttpHeaders headers = request.headers();
        System.out.println(headers.get("Accept-Encoding"));
        if (HttpMethod.POST.equals(request.getMethod())){
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(request);
            List<InterfaceHttpData> paramList = decoder.getBodyHttpDatas();
            for (InterfaceHttpData param : paramList) {
                Attribute data = (Attribute) param;
                System.out.println(data.getName()+" -- " + data.getValue());
            }
        }
        System.out.println("request info: -----------------------------------end");


        ByteBuf buf = Unpooled.copiedBuffer("收到8080="+request.getUri(), CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
        ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
