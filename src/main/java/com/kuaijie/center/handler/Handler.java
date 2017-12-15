package com.kuaijie.center.handler;

import com.joker.agreement.entity.Message;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by joker on 2017/12/8.
 */
public abstract class Handler {
    protected Handler handler;


    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public abstract void processRequest(Message requestMessage, String uri, ChannelHandlerContext ctx);


}

