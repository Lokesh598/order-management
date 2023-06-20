package com.lokesh.poc.bag.controller;

import com.lokesh.poc.bag.dto.ProductDto;
import io.netty.handler.flush.FlushConsolidationHandler;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@RestController
public class BagKafkaController {
    private static final Logger log = LoggerFactory.getLogger(BagKafkaController.class.getName());
    private String consumedData;
    List<String> messages = new ArrayList<>();

    private String TOPIC = "kafka.integrating.product-topic";

    private String GROUP_ID = "jsonGroup-1";

    KafkaConsumer<String, String> kafkaConsumer;

    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("HH:mm:ss:SSS z dd MMM yyyy");


    @GetMapping(value = "/consume/product-list/{id}")
    private void consumeProductList() {
        Properties configs = new Properties();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, "jsonGroup-1");
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        kafkaConsumer = new KafkaConsumer<String, String>(configs);
        kafkaConsumer.subscribe( Collections.singletonList("kafka.integrating.product-topic") );

        while ( true ) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofDays(1));
            System.out.println( records.count() );
            for ( ConsumerRecord record : records ) {
                System.out.println( "Topic : "+ record.topic() );
                System.out.println( "Key : "+ record.key().toString() );
                System.out.println( "Value : "+ record.value().toString() );
            }
        }
    }
}
