package com.transfer.transfer_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.transfer.transfer_api.repository.jpa")
@EnableElasticsearchRepositories(basePackages = "com.transfer.transfer_api.repository.elasticsearch")
public class DataConfig {

}
