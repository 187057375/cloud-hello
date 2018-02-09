package com.mycompany.cloud.controller.test.netty.simplechat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 客户端 channel
 * 
 *
 * @date 2015-2-26
 */
public class SimpleChatClientHandler extends  SimpleChannelInboundHandler<String> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
		System.out.println(s);
	}
}
