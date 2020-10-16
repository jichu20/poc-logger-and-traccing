package com.poc.logger.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import brave.propagation.Propagation.Factory;

@Configuration
public class congif {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    // Propaga las cabeceras
    // @Bean
    // public Factory propagationFactory() {
    // return
    // brave.propagation.ExtraFieldPropagation.newFactory(brave.propagation.B3Propagation.FACTORY,
    // "x-traceId");
    // }

}
