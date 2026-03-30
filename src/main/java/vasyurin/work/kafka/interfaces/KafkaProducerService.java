package vasyurin.work.kafka.interfaces;

import org.apache.kafka.clients.producer.ProducerRecord;

public interface KafkaProducerService {
    void sendMessage(ProducerRecord<String, String> record);
    void flush();
    void close();
}
