package com.poc.logger.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.SpanName;
import org.springframework.cloud.sleuth.autoconfig.TraceAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SleuthService {

    private static final Logger logger = LoggerFactory.getLogger(SleuthService.class);

    @Autowired
    RestTemplate restTemplate;

    public void doSomeWorkSameSpan() throws InterruptedException {

        logger.info("Doing some work");

        printAndslepp();

        ResponseEntity<String> response = restTemplate.getForEntity("https://webhook.site/1f66b8f8-d524-4a61-9ef9-b481b3cd53da",
                String.class);
    }

    @SpanName("test")
    private void printAndslepp() throws InterruptedException {

        logger.info("esperando a que pase algo");

        Thread.sleep(1000L);

    }
}
