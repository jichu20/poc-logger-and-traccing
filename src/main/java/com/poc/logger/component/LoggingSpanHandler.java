package com.poc.logger.component;

import org.springframework.stereotype.Component;

import brave.handler.MutableSpan;
import brave.handler.SpanHandler;
import brave.handler.SpanHandler.Cause;
import brave.propagation.TraceContext;

@Component
public class LoggingSpanHandler extends SpanHandler {

    public LoggingSpanHandler() {
        System.out.println("arrancando*******");
    }

    @Override
    public boolean end(TraceContext context, MutableSpan span, Cause cause) {
        System.out.println("test*******");
        return true;
    }

}
