package com.lokesh.poc.bag.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfig {

    @Bean
//    @LoadBalanced
    public WebClient.Builder webClint(){
        return WebClient.builder();
    }
}
