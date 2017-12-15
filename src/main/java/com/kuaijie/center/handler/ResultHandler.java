package com.kuaijie.center.handler;

import com.joker.agreement.entity.Message;
import com.joker.registration.container.*;
import com.joker.registration.runnable.MessageCreater;
import com.joker.registration.utils.Constent;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by joker on 2017/12/8.
 */
public class ResultHandler extends Handler {
    private AtomicInteger atomicInteger = new AtomicInteger();

    public void processRequest(Message requestMessage, String uri, final ChannelHandlerContext ctx) {
        //请求的唯一标示id
        int id = atomicInteger.incrementAndGet();
        //请求的全地址
        String totalUri = new String(requestMessage.getHead().getUrl());
        //链接的唯一编号
        Integer key = (Integer) ctx.channel().attr(Constent.ATTACHMENT_KEY).get();

//        Action1<com.joker.agreement.entity.Message> action1 = new Action1<com.joker.agreement.entity.Message>() {
//            public void call(com.joker.agreement.entity.Message message) {
//                ctx.writeAndFlush(message);
//            }
//
//        };

        //将action存入到缓存中
        ActionContainer<ChannelHandlerContext> actionContainer = ActionContainer.getContainer();
        actionContainer.put(totalUri+"&id="+id,ctx);

        //根据策略获取服务者
        ProviderContainer container = ProviderContainer.getInstance();
        ProviderPO provider = container.getProvider(uri);
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
