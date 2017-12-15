package com.kuaijie.center.server.handler;

import com.joker.agreement.entity.Message;
import com.joker.agreement.entity.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class HeartBeatHandler extends SimpleChannelInboundHandler<Object>{
	private static final Logger logger = LoggerFactory.getLogger(HeartBeatHandler.class);
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		Message message = (Message) msg;
		byte cmd = message.getCmdType();
		if (cmd == MessageType.HEARTBEAT.value()) {
			logger.info("心跳请求消息 {}",message);
			buildHeartBeat(message);
			logger.info("心跳回复消息 {}",message);
			ctx.writeAndFlush(message);
		} else {
			logger.info("非心跳消息！");
			ctx.fireChannelRead(msg);
		}
	}

	private void buildHeartBeat(Message message) {
		message.setOpStatus(MessageType.Success.value());
	}
	
}
