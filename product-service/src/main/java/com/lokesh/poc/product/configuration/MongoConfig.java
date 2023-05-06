package com.lokesh.poc.product.configuration;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.lokesh.poc.product.repository")
public class MongoConfig extends AbstractReactiveMongoConfiguration {
    @Value("${port}")
    private String port;
    @Value("${dbname}")
    private String dbName;
    @Override
    public MongoClient reactiveMongoClient() {
        return MongoClients.create();
    }
    @Override
    protected String getDatabaseName() {
        return dbName;
    }
    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(reactiveMongoClient(), getDatabaseName());
    }
}
