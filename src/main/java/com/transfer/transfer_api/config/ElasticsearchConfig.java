package com.transfer.transfer_api.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

        @Bean
        public ElasticsearchClient elasticsearchClient(RestClient restClient) {

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            JacksonJsonpMapper jacksonJsonpMapper = new JacksonJsonpMapper(mapper);

            RestClientTransport transport = new RestClientTransport(restClient, jacksonJsonpMapper);
            return new ElasticsearchClient(transport);
        }
}
