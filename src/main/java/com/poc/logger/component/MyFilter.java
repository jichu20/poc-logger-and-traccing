package com.poc.logger.component;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import brave.propagation.B3Propagation;
import brave.propagation.ExtraFieldPropagation;

@Component
// @Order(TraceWebServletAutoConfiguration.TRACING_FILTER_ORDER + 1)
class MyFilter extends GenericFilterBean {

    private final Tracer tracer;

    MyFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Span currentSpan = this.tracer.currentSpan();
        if (currentSpan == null) {
            chain.doFilter(request, response);
            return;
        }

        String xTraceId = ((HttpServletRequest) request).getHeader("X-Rho-Traceid");

        if (StringUtils.isEmpty(xTraceId)) {
            xTraceId = UUID.randomUUID().toString();
            ExtraFieldPropagation.set(currentSpan.context(), "X-Rho-Traceid", xTraceId);
        }
        ((HttpServletResponse) response).addHeader("X-Rho-Traceid", xTraceId);

        MDC.put("X-Rho-Traceid", xTraceId);

        chain.doFilter(request, response);
    }

}
