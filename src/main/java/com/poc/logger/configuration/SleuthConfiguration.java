package com.poc.logger.configuration;

import com.poc.logger.util.Constant;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import brave.Tracing;
import brave.handler.MutableSpan;
import brave.handler.SpanHandler;
import brave.propagation.B3Propagation;
import brave.propagation.ExtraFieldPropagation;
import brave.propagation.TraceContext;

@Configuration
public class SleuthConfiguration {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    Tracing tracing() {

        return Tracing.newBuilder().propagationFactory(ExtraFieldPropagation.newFactoryBuilder(B3Propagation.FACTORY)
                .addField(Constant.RHO_TRACE_ID_HEADER).addField(Constant.RHO_PARENTSPANID_HEADER).build()).build();

    }

    @Bean
    public SpanHandler handlerOne() {
        return new SpanHandler() {
            @Override
            public boolean end(TraceContext traceContext, MutableSpan span, Cause cause) {
                System.out.println("test*******");
                return true;
            }
        };
    }
}
