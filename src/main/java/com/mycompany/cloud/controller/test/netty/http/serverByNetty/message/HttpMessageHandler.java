package com.mycompany.cloud.controller.test.netty.http.serverByNetty.message;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

import java.io.IOException;

public interface HttpMessageHandler {

    void hand(ChannelHandlerContext ctx, HttpRequest httpRequest) throws IOException;
}
