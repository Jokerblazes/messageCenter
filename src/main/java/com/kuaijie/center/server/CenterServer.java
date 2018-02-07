package com.kuaijie.center.server;


import com.joker.agreement.codec.MessageDecoder;
import com.joker.agreement.codec.MessageEncoder;
import com.joker.agreement.entity.MessageConstant;
import com.joker.registration.client.Client;
import com.joker.registration.dto.CustomerDTO;
import com.joker.registration.dto.Node;
import com.joker.registration.utils.ClientType;
import com.kuaijie.center.server.handler.HeartBeatHandler;
import com.kuaijie.center.server.handler.ServiceHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.ArrayList;
import java.util.List;

public class CenterServer {

//	public void start() {
//		final String  registionIp = "127.0.0.1";
//		final int registionPort = 8001;
//		CustomerDTO customerDTO = new CustomerDTO();
//		Node node = new Node();
//		node.setId(2);
//		node.setIp("127.0.0.1");
//		node.setPort(8003);
//		customerDTO.setNode(node);
//		List<String> serviceNames = new ArrayList<String>();
//		serviceNames.add("order");
//		customerDTO.setServiceNames(serviceNames);
////		ProviderContainer.getInstance().initMap(serviceNames);
//
//
//		ServerBootstrap bootstrap = new ServerBootstrap();
//		//bossGroup监听端口线程组 workGroup工作线程组
//		EventLoopGroup bossGroup = new NioEventLoopGroup();
//		EventLoopGroup workGroup = new NioEventLoopGroup();
//		final Client client = new Client(ClientType.CUSTOMER.value(),customerDTO,workGroup,bossGroup);
//		client.initProviderMap(serviceNames);
//		try {
//			bootstrap.group(bossGroup,workGroup)
//					.channel(NioServerSocketChannel.class)
//					.option(ChannelOption.SO_BACKLOG, 2048)
//					.handler(new LoggingHandler(LogLevel.INFO))
//					.childHandler(new ChannelInitializer<SocketChannel>() {
//						@Override
//						protected void initChannel(SocketChannel ch) throws Exception {
//							ch.pipeline().addLast(
//									new MessageDecoder(
//											Integer.MAX_VALUE, MessageConstant.lengthFieldOffset,
//											MessageConstant.lengthFieldLength,MessageConstant.lengthAdjustment,MessageConstant.initialBytesToStrip));
//							ch.pipeline().addLast(new MessageEncoder());
//							ch.pipeline().addLast(new IdleStateHandler(2500, 2500, 5000));
//							ch.pipeline().addLast(new HeartBeatHandler());
//							ch.pipeline().addLast(new ServiceHandler());
//
//
//						}
//					});
//			bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);//维持链接的活跃，清除死链接
//			bootstrap.childOption(ChannelOption.TCP_NODELAY, true);//关闭延迟发送
//			ChannelFuture future = bootstrap.bind(8003).sync();
//			Runnable runnable = createRunnable(client,registionIp,registionPort);
//			bossGroup.execute(runnable);
//			future.channel().closeFuture().sync();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} finally{
//			//释放资源
//			client.close();
//			bossGroup.shutdownGracefully();
//			workGroup.shutdownGracefully();
//		}
//	}

	public void start() {
		final int defaultCapacity = 1;
		start(defaultCapacity);
	}

	public void start(int capacity) {
		final String  registionIp = "127.0.0.1";
		final int registionPort = 8001;
		CustomerDTO customerDTO = new CustomerDTO();
		Node node = new Node();
		node.setId(2);
		node.setIp("127.0.0.1");
		node.setPort(8003);
		customerDTO.setNode(node);
		List<String> serviceNames = new ArrayList<String>();
		serviceNames.add("order");
		customerDTO.setServiceNames(serviceNames);
//		ProviderContainer.getInstance().initMap(serviceNames);


		ServerBootstrap bootstrap = new ServerBootstrap();
		//bossGroup监听端口线程组 workGroup工作线程组
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		final Client client = new Client(ClientType.CUSTOMER.value(),customerDTO,workGroup,bossGroup);
		client.initProviderMap(serviceNames);
		try {
			bootstrap.group(bossGroup,workGroup)
					.channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 2048)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(
									new MessageDecoder(
											Integer.MAX_VALUE, MessageConstant.lengthFieldOffset,
											MessageConstant.lengthFieldLength,MessageConstant.lengthAdjustment,MessageConstant.initialBytesToStrip));
							ch.pipeline().addLast(new MessageEncoder());
							ch.pipeline().addLast(new IdleStateHandler(2500, 2500, 5000));
							ch.pipeline().addLast(new HeartBeatHandler());
							ch.pipeline().addLast(new ServiceHandler());


						}
					});
			bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);//维持链接的活跃，清除死链接
			bootstrap.childOption(ChannelOption.TCP_NODELAY, true);//关闭延迟发送
			ChannelFuture future = bootstrap.bind(node.getPort()).sync();
			Runnable runnable = createRunnable(client,registionIp,registionPort);
			for (int i = 0; i < capacity; i++) {
				bossGroup.execute(runnable);
			}
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally{
			//释放资源
			client.close();
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}



	private Runnable createRunnable(final Client client, final String host, final int port) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					client.connect(host, port);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		return runnable;
	}

	public static void main(String[] args) {
		CenterServer server = new CenterServer();
		server.start();
	}


}
