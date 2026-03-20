package vasyurin.work.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import vasyurin.work.config.KafkaProducerConfigImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public class KafkaProducerService {

    private final KafkaProducer<String, String> kafkaProducer;

    public KafkaProducerService() {
        Properties props = new Properties();
        props.put("bootstrap.servers", KafkaProducerConfigImpl.BOOTSTRAPSERVERS);
        props.put("key.serializer", KafkaProducerConfigImpl.KEYSERIALIZER);
        props.put("value.serializer", KafkaProducerConfigImpl.VALUESERIALIZER);
        kafkaProducer = new KafkaProducer<>(props);
    }



    public void sendMessage(ProducerRecord<String, String> record) {
        for (int i = 1; i <= KafkaProducerConfigImpl.MAX_RETRIES; i++) {
            try {
                kafkaProducer.send(record);
                System.out.println("Message sent to topic: " + record.topic() + ", value: " + record.value());
                return;
            } catch (Exception exception) {
                System.out.println("Error sending message: " + record.value() + "Retries: " + i);
                exception.printStackTrace();
                if (i == KafkaProducerConfigImpl.MAX_RETRIES) {
                    sendToDeadLetterTopic(record, exception);
                }
            }
        }

    }

    private void sendToDeadLetterTopic(ProducerRecord<String, String> record, Exception e) {
        String DLTMessage =
                "targetTopic= " + record.topic() +
                ", message= " + record.value() +
                ", exception= " + e.getMessage();

        try {
            kafkaProducer.send(new ProducerRecord<>(KafkaProducerConfigImpl.DLQ_TOPIC, DLTMessage));
            System.out.println("Message sent to dead letter topic: " + DLTMessage);
        } catch (Exception dlqException) {
            System.out.println("Error sending message: " + DLTMessage);
            saveToFile(record);
        }
    }

    private void saveToFile(ProducerRecord<String, String> record) {
        writeToFile(record);
    }

    public void flush() {
        kafkaProducer.flush();
    }

    public void close() {
        kafkaProducer.close();
    }

    private void writeToFile(ProducerRecord<String, String> record) {
        try {
            Files.writeString(Path.of("deadLetterTopic.log"), record.value() + "\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}