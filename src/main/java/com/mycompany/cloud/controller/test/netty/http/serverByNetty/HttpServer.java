package com.mycompany.cloud.controller.test.netty.http.serverByNetty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class HttpServer {

    private int port;
    public static boolean isSSL;
    public HttpServer(int port, boolean isSSL){
        this.port = port;
        this.isSSL = isSSL;
    }

    public void run(){
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            bootstrap.option(ChannelOption.TCP_NODELAY, true);// 不延迟,即刻发送消息
            bootstrap.option(ChannelOption.SO_KEEPALIVE, false);// 禁止长连接
            bootstrap.handler(new LoggingHandler(LogLevel.DEBUG)); // 日志级别
            bootstrap.childHandler(new HttpServerInitializer());

            bootstrap.bind(port).sync();
            System.out.println("HttpServer启动成功,端口:" + port + "," + (isSSL ? "" : "未") + "使用SSL证书.");
        } catch (Exception e) {
            e.printStackTrace();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        HttpServer httpServer = new HttpServer(8080,false);
        httpServer.run();
    }

}
