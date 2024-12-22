//package edu.rice.wecommunity.config;
////
//import org.apache.http.Header;
//import org.apache.http.HttpHeaders;
//import org.apache.http.HttpHost;
//import org.apache.http.message.BasicHeader;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestClientBuilder;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
//
////
//@Configuration
//public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {
//
//    private static final String ELASTICSEARCH_URL = "https://b20d6e47e1ab4244bd298876793a97c9.us-east-1.aws.found.io:443";
//    private static final String API_KEY = "NGx0WG41TUJ2TWU2MXZocURwdGQ6WDZxSGM4ZERTeS1hZ1dTcDdkaDU4dw==";
//
//    @Override
//    @Bean
//    public RestHighLevelClient elasticsearchClient() {
//        String encodedApiKey = "ApiKey " + API_KEY;
//
//        RestClientBuilder builder = RestClient.builder(
//                        HttpHost.create(ELASTICSEARCH_URL))
//                .setDefaultHeaders(new Header[]{new BasicHeader(HttpHeaders.AUTHORIZATION, encodedApiKey)});
//
//        return new RestHighLevelClient(builder);
//    }
//}
