package com.mycompany.cloud.controller.test.netty.http.serverByNetty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * 描述:
 * 创建人: SXF
 * 创建时间: 2016/12/26 17:33.
 * Version: 1.0.0
 * 修改人:
 * 修改时间:
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        if (HttpServer.isSSL) {
            SSLEngine sslEngine = NettySSLContext.createSSLEngine();
            sslEngine.setUseClientMode(false); // 服务器端模式
            sslEngine.setNeedClientAuth(false); // 不需要验证客户端
            pipeline.addLast("ssl", new SslHandler(sslEngine));
        }

        // 对request解码
        pipeline.addLast("decoder", new HttpRequestDecoder());
        // 对response编码
        pipeline.addLast("encoder", new HttpResponseEncoder());
        // HttpObjectAggregator会把多个消息转换为一个单一的FullHttpRequest或是FullHttpResponse.
        pipeline.addLast("aggregator", new HttpObjectAggregator(1048576));
        // 压缩，Compresses an HttpMessage and an HttpContent in gzip or deflate encoding
        // while respecting the "Accept-Encoding" header. If there is no matching encoding, no compression is done.
        pipeline.addLast("deflater", new HttpContentCompressor());
        // 自定义handler
        pipeline.addLast("httpServerHandler", new HttpServerHandler());
    }
}
