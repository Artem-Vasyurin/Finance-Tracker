package vasyurin.work.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class KafkaBatchSchedulerService implements Runnable {
    private final KafkaProducerService kafkaProducerService = new KafkaProducerService();
    private final ScheduledExecutorService schedule = new ScheduledThreadPoolExecutor(1);
    private final List<ProducerRecord<String,String>> messages = new ArrayList<>();

    public KafkaBatchSchedulerService(){
        start();
    }

    public void addMessageToBath(String topic, String message) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, message);
        messages.add(record);
    }

    private void start() {
        schedule.scheduleWithFixedDelay(this, 1, 10, TimeUnit.SECONDS);
    }

    private void sendMessageToKafka() {
        if (messages.isEmpty()) {
            return;
        }
        for (ProducerRecord<String,String> record : messages) {
            kafkaProducerService.sendMessage(record);
        }
        kafkaProducerService.flush();
        messages.clear();
    }

    @Override
    public void run() {
        sendMessageToKafka();
    }
}
