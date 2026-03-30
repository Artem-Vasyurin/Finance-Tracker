package kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import vasyurin.work.config.interfaces.KafkaConsumerConfig;
import vasyurin.work.config.KafkaConsumerConfigImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerService {
    private static final KafkaConsumerConfig kafkaConsumerConfig = new KafkaConsumerConfigImpl();

    public static void main(String[] args) {
        Properties props = new Properties();

        props.put("bootstrap.servers", kafkaConsumerConfig.getBootstrapServers());
        props.put("group.id", kafkaConsumerConfig.getGroupId());
        props.put("auto.offset.reset", kafkaConsumerConfig.getAutoOffsetReset());

        props.put("key.deserializer", kafkaConsumerConfig.getKeyDeserializer());
        props.put("value.deserializer", kafkaConsumerConfig.getValueDeserializer());

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Arrays.asList(kafkaConsumerConfig.getTopic()));
            System.out.println("Subscribed to topic: " + kafkaConsumerConfig.getTopic());
            System.out.println("Subscribed to group: " + kafkaConsumerConfig.getGroupId());

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("Event received: " + record.value());
                    writeToFile(record);
                }
            }
        }
    }

    private static void writeToFile(ConsumerRecord<String, String> record) {
        try {
            Files.writeString(Path.of("events.log"), record.value() + "\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}