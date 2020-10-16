package com.poc.logger.controller;

import com.poc.logger.service.SleuthService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import brave.Tracing;
import brave.propagation.B3Propagation;
import brave.propagation.ExtraFieldPropagation;

@RestController
public class DefaultController {

    private static final Logger logger = LoggerFactory.getLogger(DefaultController.class);

    @Autowired
    private SleuthService sleuthService;

    @GetMapping("/")
    public ResponseEntity<String> dummy() throws InterruptedException {

        Tracing.newBuilder().propagationFactory(ExtraFieldPropagation.newFactory(B3Propagation.FACTORY, "x-vcap-request-id"));

        logger.info("Hello Sleuth");
        sleuthService.doSomeWorkSameSpan();
        return new ResponseEntity<String>("Arriba", HttpStatus.ACCEPTED);

    }
}
