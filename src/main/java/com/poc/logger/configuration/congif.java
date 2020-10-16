package com.poc.logger.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import brave.Tracing;
import brave.propagation.B3Propagation;
import brave.propagation.ExtraFieldPropagation;
import brave.propagation.Propagation.Factory;

@Configuration
public class congif {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    Tracing tracing() {

        return Tracing.newBuilder()
                .propagationFactory(ExtraFieldPropagation.newFactoryBuilder(B3Propagation.FACTORY).addField("X-Rho-Traceid").build())
                .build();
    }

}
