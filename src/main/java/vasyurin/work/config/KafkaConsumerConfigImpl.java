package vasyurin.work.config;

import vasyurin.work.config.interfaces.KafkaConsumerConfig;

public class KafkaConsumerConfigImpl implements KafkaConsumerConfig {
    private final String bootstrapServers;
    private final String topic;
    private final String groupId;
    private final String autoOffsetReset;
    private final String keyDeserializer;
    private final String valueDeserializer;

    public KafkaConsumerConfigImpl() {
        this.bootstrapServers = "localhost:9092";
        this.topic = "transactions";
        this.groupId = "transactions-group-test";
        this.autoOffsetReset = "earliest";
        this.keyDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";
        this.valueDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public String getTopic() {
        return topic;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getAutoOffsetReset() {
        return autoOffsetReset;
    }

    public String getKeyDeserializer() {
        return keyDeserializer;
    }

    public String getValueDeserializer() {
        return valueDeserializer;
    }
}