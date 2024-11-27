package edu.rice.wecommunity.config;
//
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

//
@Configuration
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {

    private static final String ELASTICSEARCH_URL = "https://19be876a21c44ba4888d232520bd1861.us-east-2.aws.elastic-cloud.com:443";
    private static final String API_KEY = "NHhXVEZKTUIzVjFqa2xZZ1Mtd006RUFFdVRyMEdTQTJLQ1JjMFpCVDc5QQ==";

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        String encodedApiKey = "ApiKey " + API_KEY;

        RestClientBuilder builder = RestClient.builder(
                        HttpHost.create(ELASTICSEARCH_URL))
                .setDefaultHeaders(new Header[]{new BasicHeader(HttpHeaders.AUTHORIZATION, encodedApiKey)});

        return new RestHighLevelClient(builder);
    }
}
