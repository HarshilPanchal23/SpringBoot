package com.demo.sqs.consumer;

@Service
@Log4j2
public class Consumer {

    @Value("${aws.queueName}")
    private String queueName;

    private final AmazonSQS amazonSQSClient;

    public Consumer(AmazonSQS amazonSQSClient) {
        this.amazonSQSClient = amazonSQSClient;
    }

    @Scheduled(fixedDelay = 5000) // It runs every 5 seconds.
    public void consumeMessages() {
        try {
            String queueUrl = amazonSQSClient.getQueueUrl(queueName).getQueueUrl();

            ReceiveMessageResult receiveMessageResult = amazonSQSClient.receiveMessage(queueUrl);

            if (!receiveMessageResult.getMessages().isEmpty()) {
                com.amazonaws.services.sqs.model.Message message = receiveMessageResult.getMessages().get(0);
                log.info("Read Message from queue: {}", message.getBody());
                amazonSQSClient.deleteMessage(queueUrl, message.getReceiptHandle());
            }

        } catch (Exception e) {
            log.error("Queue Exception Message: {}", e.getMessage());
        }
    }
}