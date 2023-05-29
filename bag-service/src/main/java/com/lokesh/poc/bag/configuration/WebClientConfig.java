package com.lokesh.poc.bag.configuration;

import com.lokesh.poc.bag.dataobject.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.tcp.TcpClient;
@Configuration
public class WebClientConfig {

    @Autowired
    WebClient.Builder clientBuilder;

    public Mono<UserDO> getUserFromUserService() {
        clientBuilder.baseUrl("http://localhost:8082/api/user/v1/")
                .build()
                .get();
//                .uri("/login/{emailId}", emailId)
        return null;
    }
}
