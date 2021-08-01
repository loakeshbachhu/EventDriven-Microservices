package com.event.microservices.twitter.to.kafka.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class TwitterToKafkaServiceApplication implements CommandLineRunner {

    public static void main(String[] args){
        SpringApplication.run(TwitterToKafkaServiceApplication.class,args);
    }


    @Override
    public void run(String... args) throws Exception {

        System.out.println("Application is running");

    }
}
