package com.event.microservices.twitter.to.kafka.service;

import com.event.microservices.config.data.TwitterToKafkaServiceConfigData;
import com.event.microservices.twitter.to.kafka.service.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@SpringBootApplication
@ComponentScan(basePackages = "com.event.microservices")
public class TwitterToKafkaServiceApplication implements CommandLineRunner {

    public static final Logger LOG = LoggerFactory.getLogger(TwitterToKafkaServiceApplication.class);

   public static void main(String[] args){
        SpringApplication.run(TwitterToKafkaServiceApplication.class,args);
    }

    private TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData;

    private StreamRunner streamRunner;


    public TwitterToKafkaServiceApplication(TwitterToKafkaServiceConfigData configData, StreamRunner runner){
        this.twitterToKafkaServiceConfigData = configData;
        this.streamRunner = runner;
    }

    @Override
    public void run(String... args) throws Exception {

        LOG.info("Application is running");
        LOG.info(Arrays.toString( twitterToKafkaServiceConfigData.getTwitterKeywords().toArray(new String [] {})));
        LOG.info(twitterToKafkaServiceConfigData.getWelcomeMessage());
        streamRunner.start();
    }
}
