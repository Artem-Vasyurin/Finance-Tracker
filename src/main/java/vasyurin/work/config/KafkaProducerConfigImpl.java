package vasyurin.work.config;

import vasyurin.work.config.interfaces.KafkaProducerConfig;

public class KafkaProducerConfigImpl implements KafkaProducerConfig {

    public static final String BOOTSTRAPSERVERS = "localhost:9092";
    public static final String KEYSERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
    public static final String VALUESERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
    public static final String MAIN_TOPIC = "transactions";
    public static final String DLQ_TOPIC = "transactions.dlq";
    public static final int MAX_RETRIES = 3;


}