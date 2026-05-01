package com.pravin.kafka.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.EmbeddedKafkaKraftBroker;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConfig {


    @Bean
    public EmbeddedKafkaBroker broker() {
        return new EmbeddedKafkaKraftBroker(1, 1).brokerListProperty("spring.kafka.bootstrap-servers");
    }

    @Bean
    @DependsOn("broker")
    public DefaultErrorHandler errorHandler(KafkaTemplate<Object, Object> kafkaTemplate) {

        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(kafkaTemplate,
                        (record, ex) -> new org.apache.kafka.common.TopicPartition(
                                record.topic() + "-dlt",
                                record.partition()
                        ));

        // Retry 3 times with 2 sec delay
        FixedBackOff backOff = new FixedBackOff(2000L, 3);

        return new DefaultErrorHandler(recoverer, backOff);
    }

}
