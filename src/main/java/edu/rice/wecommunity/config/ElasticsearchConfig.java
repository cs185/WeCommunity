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

    private static final String ELASTICSEARCH_URL = "https://3dc8deb0a9ef4faeb52fb0bbb7fa649b.us-east-1.aws.found.io:443";
    private static final String API_KEY = "b3FMaFNwUUJOcGNZM1NfWG53X1o6WkdqaXlSVFVSTXU5Vk9GZGQ4SVpBUQ==";

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