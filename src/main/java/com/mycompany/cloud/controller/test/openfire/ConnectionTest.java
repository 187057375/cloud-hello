package com.mycompany.cloud.controller.test.openfire;

/**
 * @author peter
 * @version V1.0 创建时间：18/2/26
 *          Copyright 2018 by PreTang
 */


public class ConnectionTest {

    /**
     * @param args
     *
     * @author JohnGao
     */
/*    public static void main(String[] args) {
        *//* 定义连接信息 *//*
        ConnectionConfiguration config = new ConnectionConfiguration(
                "127.0.0.1", 5222);

        *//* 使用SASL验证 *//*
        config.setSASLAuthenticationEnabled(true);
        Connection conn = new XMPPConnection(config);
        try {
            *//* 尝试连接服务器 *//*
            conn.connect();

            *//* 尝试登陆服务器 *//*
            conn.login("JohnGao", "ll.520GG");
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }*/

   /* public static void main(String[] args) throws Exception {
        //SSLContext sc = SSLContext.getInstance();

        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword("ding", "123456")
                .setServiceName("sun-d601bacdc00")
                .setHost("172.16.9.88")
                .setPort(5222).setSecurityMode(ConnectionConfiguration.SecurityMode.disabled).setResource("work").build();


        AbstractXMPPConnection conn1 =new XMPPTCPConnection(config);
        conn1.connect();
        conn1.login();
        System.out.println(conn1.getHost());
        System.out.println(conn1.isConnected());
        System.out.println(conn1.getUser());


        Chat chat = ChatManager.getInstanceFor(conn1).createChat("xiaoming@sun-d601bacdc00");

        chat.addMessageListener(new ChatMessageListener() {
            public void processMessage(Chat chat, Message message) {
                System.out.println(message);
            }
        });
        while (true) {
            chat.sendMessage("lalalal");
            Thread.sleep(5000);
        }
    }*/

}