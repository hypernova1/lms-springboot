package org.sam.lms.infrastructure.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory

@Configuration
class KafkaConsumerConfig(
    @Value("\${spring.kafka.bootstrap-servers}")
    private val bootStrapServers: String
) {

    @Bean
    fun consumerFactory(): ConsumerFactory<String, Any> {
        val config: MutableMap<String, Any> = HashMap();
        config[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootStrapServers
        config[ConsumerConfig.GROUP_ID_CONFIG] = "group_1"
        config[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "latest"

        return DefaultKafkaConsumerFactory(config)
    }

    @Bean
    fun kafkaListenerContainer(): ConcurrentKafkaListenerContainerFactory<String, Any> {
        val factory: ConcurrentKafkaListenerContainerFactory<String, Any> = ConcurrentKafkaListenerContainerFactory()
        factory.consumerFactory = consumerFactory()
        return factory
    }

}