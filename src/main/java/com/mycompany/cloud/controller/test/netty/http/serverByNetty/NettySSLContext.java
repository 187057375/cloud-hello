package com.mycompany.cloud.controller.test.netty.http.serverByNetty;


import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

public class NettySSLContext {

    private static final String PROTOCOL = "TLS";
    private static final String KEY_STORE_PATH = "/opt/nettydemo/nettySSLServer.jks";
    private static final String KEY_STORE_PASSWORD = "123456";
    private static final String KEY_PASSWORD = "123456";

    private static SSLContext sslContext = null;

    static {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            InputStream inputStream = new FileInputStream(KEY_STORE_PATH);
            keyStore.load(inputStream, KEY_STORE_PASSWORD.toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore,KEY_PASSWORD.toCharArray());

            sslContext = SSLContext.getInstance(PROTOCOL);
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    public static SSLEngine createSSLEngine() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        return sslContext.createSSLEngine();
    }
}
