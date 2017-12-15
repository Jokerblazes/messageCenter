//package utils;
//
//import com.joker.agreement.entity.Head;
//import com.joker.agreement.entity.Message;
//
///**
// * Created by joker on 2017/12/12.
// */
//public class Utils {
//    private Utils() {}
//
//    public static Message changeMessage(com.kuaijie.center.server.entity.Message message) {
//        Message message1 = new Message();
//        Head head = new Head();
//        head.setHead(message.getHead().getHead());
//        head.setLength(message.getHead().getLength());
//        head.setUrl(message.getHead().getUrl());
//        message1.setHead(head);
//        message1.setLength(message.getLength());
//        message1.setChannel(message.getChannel());
//        message1.setDirection(message.getDirection());
//        message1.setSource(message.getSource());
//        message1.setDest(message.getDest());
//        message1.setCmdType(message.getCmdType());
//        message1.setOpStatus(message.getOpStatus());
//        message1.setOptionData(message.getOptionData());
//        message1.setCheckSum(message.getCheckSum());
//        message1.setId(message.getId());
//        message1.setFrame(message.getFrame());
//        return message1;
//    }
//}
