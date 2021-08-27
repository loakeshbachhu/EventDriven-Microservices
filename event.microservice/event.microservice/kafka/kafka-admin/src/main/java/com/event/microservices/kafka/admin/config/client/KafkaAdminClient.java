package com.event.microservices.kafka.admin.config.client;

import com.event.microservices.config.data.KafkaConfigData;
import com.event.microservices.config.data.RetryConfigData;
import com.event.microservices.kafka.admin.config.exception.KafkaClientException;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class KafkaAdminClient {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaAdminClient.class);

    private final KafkaConfigData kafkaConfigData;
    private final RetryConfigData retryConfigData;
    private final AdminClient adminClient;
    private final RetryTemplate retryTemplate;

    public KafkaAdminClient(KafkaConfigData kafkaConfigData, RetryConfigData retryConfigData, AdminClient adminClient, RetryTemplate retryTemplate) {
        this.kafkaConfigData = kafkaConfigData;
        this.retryConfigData = retryConfigData;
        this.adminClient = adminClient;
        this.retryTemplate = retryTemplate;
    }

    public void createTopics(){
        CreateTopicsResult createTopicsResult;
        try {
            createTopicsResult = retryTemplate.execute(this::doCreateTopics);
        } catch (Throwable t){
            throw new KafkaClientException("Reached Max no of retry for creating kafka topic(s)", t);
        }
    checkTopicsCreated();
    }

    public void checkTopicsCreated(){

    }

    public CreateTopicsResult doCreateTopics(RetryContext retryContext){
        List<String> topicNames = kafkaConfigData.getTopicNamesToCreate();
        LOG.info("Creating {} topic(s), attempt {}", topicNames.size(), retryContext.getRetryCount());
         List<NewTopic> newTopics = topicNames.stream().map(topic -> new NewTopic(topic.trim(),kafkaConfigData.getNumOfPartitions(),kafkaConfigData.getReplicationFactor()))
                .collect(Collectors.toList());
         return adminClient.createTopics(newTopics);
    }

    private Collection<TopicListing> getTopics(){
        Collection<TopicListing> topics;

        try {
            topics = retryTemplate.execute(this::doGetTopics);
        } catch (Throwable t) {
            throw new KafkaClientException("Reached Max no of retry for creating kafka topic(s)", t);
        }
        return topics;
    }

    private Collection<TopicListing> doGetTopics(RetryContext retryContext) throws ExecutionException, InterruptedException {
        LOG.info("Reading Kafka topic {} , attempt {}", kafkaConfigData.getTopicNamesToCreate().toArray(), retryContext.getRetryCount());
        Collection<TopicListing> topics = adminClient.listTopics().listings().get();
        if(topics != null){
            topics.forEach(topic -> LOG.debug("Topic with Name {}",topic.name()));
        }
        return topics;
    }


}
