package com.poc.logger.component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.poc.logger.util.Constant;

import org.springframework.stereotype.Component;

import brave.baggage.BaggageField;
import brave.handler.MutableSpan;
import brave.handler.SpanHandler;
import brave.propagation.TraceContext;
import brave.internal.baggage.BaggageFields;

@Component
public class LoggingSpanHandler extends SpanHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingSpanHandler.class);

    @Override
    public boolean end(TraceContext context, MutableSpan span, Cause cause) {

        MDC.put("finishTime", String.valueOf(TimeUnit.NANOSECONDS.convert(span.finishTimestamp(), TimeUnit.MICROSECONDS)));
        MDC.put("startTime", String.valueOf(TimeUnit.NANOSECONDS.convert(span.startTimestamp(), TimeUnit.MICROSECONDS)));
        MDC.put("name", span.name());

        if (span.parentId() != null) {

            MDC.put("parentSpan", ((BaggageFields) context.extra().get(0)).getValue(BaggageField.create(Constant.RHO_PARENTSPANID_HEADER)));
            MDC.put(Constant.RHO_SPAN_ID_HEADER, UUID.randomUUID().toString());

            LOGGER.info("span son");

        } else {
            MDC.put("parentSpan", null);
            
            LOGGER.info("span father");
        }

        return true;
    }

}
