package com.mycompany.cloud.controller.test;

import org.springframework.context.annotation.Configuration;

/**
 * @author peter
 * @version V1.0 创建时间：17/10/18
 *          Copyright 2017 by PreTang
 */
@Configuration
//@EnableElasticsearchRepositories(basePackages = "com.pretang.cloud.es")
public class EsConfig {

/*
    @Value("${elasticsearch.host}")
    private String EsHost;

    @Value("${elasticsearch.port}")
    private int EsPort;

    @Value("${elasticsearch.clustername}")
    private String EsClusterName;

    @Bean
    public Client client() throws Exception {

        Settings esSettings = Settings.settingsBuilder().put("cluster.name", EsClusterName).build();//.put("cluster.name", EsClusterName)

        return TransportClient.builder()
                .settings(esSettings)
                .build()
                .addTransportAddress( new InetSocketTransportAddress(InetAddress.getLoopbackAddress(), EsPort));
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() throws Exception {
        return new ElasticsearchTemplate(client());
    }
*/



}

