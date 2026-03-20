package vasyurin.work.config.interfaces;

public interface KafkaConsumerConfig {
    String getBootstrapServers();

    String getTopic();

    String getGroupId();

    String getAutoOffsetReset();

    String getKeyDeserializer();

    String getValueDeserializer();
}
