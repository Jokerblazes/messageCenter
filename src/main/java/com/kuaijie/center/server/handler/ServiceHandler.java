package com.kuaijie.center.server.handler;



import com.joker.agreement.entity.Message;
import com.joker.agreement.entity.MessageType;
import com.joker.registration.container.StorageContainer;
import com.joker.registration.entity.MessageAction;
import com.joker.registration.entity.Storage;
import com.joker.registration.runnable.MessageActionDestroyer;
import com.joker.registration.utils.Constent;
import com.kuaijie.center.handler.Handler;
import com.kuaijie.center.handler.ResultHandler;
import com.kuaijie.center.handler.UriHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.concurrent.atomic.AtomicInteger;


public class ServiceHandler extends SimpleChannelInboundHandler<Object> {
	private static final Logger logger = LoggerFactory.getLogger(ServiceHandler.class);
	private AtomicInteger integer = new AtomicInteger();

	private Handler handler;
	public ServiceHandler() {
		Handler uriHandler = new UriHandler();
		Handler resultHandler = new ResultHandler();
		uriHandler.setHandler(resultHandler);
		handler = uriHandler;
		System.out.println(handler);
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		logger.info("ServiceHandler新的链接进入 {}",ctx.channel().toString());
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		Integer key = integer.incrementAndGet();
		ctx.channel().attr(Constent.ATTACHMENT_KEY).set(key);
		StorageContainer.getInstance().put(key,new Storage(10));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.info("ServiceHandler链接异常，关闭 {}",ctx.channel().toString());
		exceptionalProcess(ctx);

	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info("ServiceHandler链接断开 {}",ctx.channel().toString());
		exceptionalProcess(ctx);
	}

	//心跳检测
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if(evt instanceof IdleStateEvent){
			IdleStateEvent event = (IdleStateEvent)evt;
			if(event.state() == IdleState.ALL_IDLE){
				logger.info("长时间不读写，断开连接 {}",ctx.channel().toString());
				ctx.channel().close();
			}
		}else{
			super.userEventTriggered(ctx, evt);
		}
	}


	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.info("服务器收到消息 {}",msg);
		Message reqMessage = (Message)msg;
		Message message = null;
		//如果软件登录
		if(reqMessage.getCmdType() == MessageType.Login.value()) {
			message = reqMessage;
			message.setOpStatus(MessageType.Success.value());
		} else {
			//处理请求并获取数据
			handler.processRequest(reqMessage,"",ctx);
			Integer key = (Integer) ctx.channel().attr(Constent.ATTACHMENT_KEY).get();
			//MessageAction 的有界缓存
			Storage<MessageAction> storage = StorageContainer.getInstance().get(key);
			MessageActionDestroyer destroyer = new MessageActionDestroyer(storage);

			ctx.executor().execute(destroyer);
		}
		if (message != null)
			ctx.writeAndFlush(message);
//		if(message!=null) {
//			ctx.writeAndFlush(message);
//		}else {
//			logger.info("没有对应命令执行！或内部出错！");
//			ctx.channel().close();
//		}
	}
	//异常处理
	public  void  exceptionalProcess(ChannelHandlerContext ctx) {
	}

}
