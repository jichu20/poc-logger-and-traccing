package com.poc.logger.converter;

import java.util.concurrent.TimeUnit;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class NanoSecondsConverter extends ClassicConverter {

    long start = System.nanoTime();

    @Override
    public String convert(ILoggingEvent event) {
        return Long.toString(TimeUnit.MILLISECONDS.toNanos(event.getTimeStamp()));
    }
}
