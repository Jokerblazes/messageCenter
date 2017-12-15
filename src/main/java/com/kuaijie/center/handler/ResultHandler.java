package com.kuaijie.center.handler;

import com.joker.agreement.entity.Message;
import com.joker.registration.container.*;
import com.joker.registration.runnable.MessageCreater;
import com.joker.registration.utils.Constent;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.CheckUntils;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * 网络结果处理
 * Created by joker on 2017/12/8.
 * https://github.com/Jokerblazes/messageCenter.git
 */
public class ResultHandler extends Handler {
    private static final Logger logger = LoggerFactory.getLogger(ResultHandler.class);

    private AtomicInteger atomicInteger = new AtomicInteger();

    public void processRequest(Message requestMessage, String uri, final ChannelHandlerContext ctx) {
        //请求的唯一标示id
        int id = atomicInteger.incrementAndGet();
        //请求的全地址
        String totalUri = new String(requestMessage.getHead().getUrl());

        logger.info("请求的url {}",totalUri);
        //链接的唯一编号
        Integer key = (Integer) ctx.channel().attr(Constent.ATTACHMENT_KEY).get();
        if (key == null)
            throw new RuntimeException("链路未标示唯一编号！");
        logger.info("链路唯一标记 {}",key);

//        Action1<com.joker.agreement.entity.Message> action1 = new Action1<com.joker.agreement.entity.Message>() {
//            public void call(com.joker.agreement.entity.Message message) {
//                ctx.writeAndFlush(message);
//            }
//
//        };

        //将action存入到缓存中
        ChannelContainer<ChannelHandlerContext> channelContainer = ChannelContainer.getContainer();
        channelContainer.put(totalUri+"&id="+id,ctx);

        //根据策略获取服务者
        ProviderContainer container = ProviderContainer.getInstance();
        ProviderPO provider = container.getProvider(uri);
        if (!CheckUntils.checkNull(provider))
            throw new RuntimeException("未找到对应服务的生产者" + uri);
        requestMessage.setDest(id);
        requestMessage.setSource(key);
        Message message = requestMessage;
        //放入有界缓存中，等待消费者消费
        MessageCreater messageCreater = new MessageCreater(provider.getStorage(),message);
        ctx.executor().execute(messageCreater);
//        ChannelHandlerContext ctx1 = provider.getCtx();

//        ChannelFuture future = ctx1.writeAndFlush(test);

//        future.addListener(new ChannelFutueventLoopreListener() {
//            public void operationComplete(ChannelFuture future) {
//                // Perform post-closure operation
//                // ...
//                System.out.println("wancheng");
//            }
//        });

//        ctx1.write(requestMessage);
//        ctx1.flush();
//        System.out.println("发送消息发送消息"+requestMessage);
//        ctx1.writeAndFlush(requestMessage);
    }
}
