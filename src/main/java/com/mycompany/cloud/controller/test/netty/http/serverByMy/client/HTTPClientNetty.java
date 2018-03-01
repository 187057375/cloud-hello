package com.mycompany.cloud.controller.test.netty.http.serverByMy.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class HTTPClientNetty extends ChannelInboundHandlerAdapter {



    private StringBuffer response = new StringBuffer();


    public static void main(String[] args) throws Exception {
        HTTPClientNetty client  = new HTTPClientNetty();
        System.out.println(client.send());

    }

    public String send() throws Exception {

        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(HTTPClientNetty.this);
                        }
                    });

            // Start the client.
            ChannelFuture f = b.connect("127.0.0.1", 8081).sync();
            StringBuilder reqMsg = new StringBuilder();
            reqMsg.append("POST ").append("/ccc/nnn").append(" HTTP/1.1\r\n");
            reqMsg.append("Cache-Control: no-cache\r\n");
            reqMsg.append("Pragma: no-cache\r\n");
            reqMsg.append("User-Agent: JavaSocket/").append(System.getProperty("java.version")).append("\r\n");
            reqMsg.append("Host: ").append("127.0.0.1").append("\r\n");
            reqMsg.append("Accept: text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2\r\n");
            reqMsg.append("Connection: keep-alive\r\n");
            reqMsg.append("Content-Type: application/x-www-form-urlencoded; charset=").append("UTF-8").append("\r\n");
            reqMsg.append("Content-Length: ").append(7).append("\r\n");
            reqMsg.append("\r\n");
            reqMsg.append("aaa=123");

            ByteBuf buf = Unpooled.copiedBuffer(reqMsg.toString().getBytes());
            f.channel().writeAndFlush(buf);

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
            System.out.println("-close---");
            return response.toString();
        } finally {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("----channelActive");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        byte[] message = new byte[buf.readableBytes()];
        buf.readBytes(message);
        System.out.println("----channelRead------");
        System.out.println(new String(message));
        System.out.println("----channelRead------");
        response.append(new String(message));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        System.out.println("----channelReadComplete "  );
        ctx.flush();
        //ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {         // Close the connection when an exception is raised.
        System.out.println("----exceptionCaught "  );
        cause.printStackTrace();
        ctx.close();
    }
}
