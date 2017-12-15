package com.kuaijie.center.handler;

import com.joker.agreement.entity.Message;
import com.joker.agreement.entity.MessageType;
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;

/**
 * Created by joker on 2017/12/8.
 */
public class UriHandler extends Handler {

    @Override
    public void processRequest(Message requestMessage, String uri, ChannelHandlerContext ctx) {
        byte[] bytes = requestMessage.getHead().getUrl();
        if (bytes == null) {
            Message message = Message.messageResult(null, (byte)0,requestMessage.getHead());
            ctx.writeAndFlush(message);
        }
        try {
            uri = new String(bytes,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String[] uriArray = uri.split("/");
        handler.processRequest(requestMessage,uriArray[0],ctx);
    }
}
