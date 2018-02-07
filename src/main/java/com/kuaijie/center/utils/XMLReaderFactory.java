//package com.kuaijie.center.utils;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.NamedNodeMap;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import java.io.IOException;
//import java.util.Arrays;
//
///**
// * @Author Joker
// * @Description
// * @Date Create in 上午10:07 2018/2/3
// */
//public class XMLReaderFactory {
//
//    public static String[] readXML(String url) {
//        String [] objects = new String[4];
//        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//        DocumentBuilder db = null;
//        try {
//            db = dbf.newDocumentBuilder();
//            Document document = db.parse(url);//传入文件名可以是相对路径也可以是绝对路径
//            NodeList centerList = document.getElementsByTagName("node");
//            for (int i = 0; i < centerList.getLength(); i++) {
//                Node center = centerList.item(i);
//                NodeList childNodes = center.getChildNodes();
//                Node ip = childNodes.item(0).getNamedItem("ip");
//                Node port = attributes.getNamedItem("port");
//                Node registor = attributes.getNamedItem("registor");
//                NamedNodeMap registorAttributes = registor.getAttributes();
//                Node registorIp = registorAttributes.getNamedItem("ip");
//                Node registorPort = registorAttributes.getNamedItem("port");
//
//
//                if ("".equals(ip.getNodeValue()) || "".equals(port.getNodeValue()) ||
//                        "".equals(registorIp.getNodeValue()) || "".equals(registorPort.getNodeValue())) {
//                    throw new RuntimeException("xml内容不允许为空！");
//                }
//                objects[0] = ip.getNodeValue();
//                objects[1] = port.getNodeValue();
//                objects[2] = registorIp.getNodeValue();
//                objects[3] = registorPort.getNodeValue();
//            }
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        } catch (SAXException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return objects;
//    }
//
//    public static void main(String[] args) {
//        String[] strings = readXML("src/main/resources/config.xml");
//        System.out.println(Arrays.asList(strings));
//    }
//}
