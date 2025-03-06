package com.demo.sqs.service;

@Service
@Log4j2
public class Publisher {

    @Value("${aws.queueName}")
    private String queueName;

    private final AmazonSQS amazonSQSClient;
    private final ObjectMapper objectMapper;

    public Publisher(AmazonSQS amazonSQSClient, ObjectMapper objectMapper) {
        this.amazonSQSClient = amazonSQSClient;
        this.objectMapper = objectMapper;
    }

    public void publishMessage(String id) {
        try {
            GetQueueUrlResult queueUrl = amazonSQSClient.getQueueUrl(queueName);
            var message = Message.builder()
                    .id(id)
                    .content("message")
                    .createdAt(new Date()).build();
            var result = amazonSQSClient.sendMessage(queueUrl.getQueueUrl(), objectMapper.writeValueAsString(message));
        } catch (Exception e) {
            log.error("Queue Exception Message: {}", e.getMessage());
        }

    }

}