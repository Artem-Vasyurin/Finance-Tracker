package vasyurin.work.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import vasyurin.work.kafka.interfaces.KafkaProducerService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class KafkaBatchSchedulerService implements Runnable {
    private final KafkaProducerService kafkaProducerService;
    private final ScheduledExecutorService schedule = new ScheduledThreadPoolExecutor(1);
    private final List<ProducerRecord<String,String>> messages = new ArrayList<>();

    public KafkaBatchSchedulerService(KafkaProducerService kafkaProducerService){
        this.kafkaProducerService = new KafkaProducerServiceImpl();
        start();
    }

    public void addMessageToBatch(String topic, String message) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, message);
        synchronized (this) {
            messages.add(record);
        }
    }

    public void shutdown() {
        schedule.shutdown();
    }

    private void start() {
        schedule.scheduleWithFixedDelay(this, 1, 10, TimeUnit.SECONDS);
    }

    private void sendMessageToKafka() {
        List<ProducerRecord<String,String>> records;

        synchronized (this) {
            if (messages.isEmpty()) {
                return;
            }
            records = new ArrayList<>(messages);
            messages.clear();
        }
        for (ProducerRecord<String,String> record : records) {
            kafkaProducerService.sendMessage(record);
        }
        kafkaProducerService.flush();
    }

    @Override
    public void run() {
        sendMessageToKafka();
    }
}
