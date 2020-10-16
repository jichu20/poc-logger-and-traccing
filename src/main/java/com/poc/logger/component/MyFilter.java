package com.poc.logger.component;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContextOrSamplingFlags;
import brave.propagation.TraceIdContext;
import brave.servlet.HttpServletRequestWrapper;

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
        // for readability we're returning trace id in a hex form
        // ((HttpServletResponse) response).addHeader("TraceId-Propio",
        // currentSpan.context().traceIdString());
        // ((HttpServletResponse) response).addHeader("SpanId-Propio",
        // currentSpan.context().spanIdString());

        String xTraceId = ((HttpServletRequest) request).getHeader("X-Rho-Traceid");
        if (StringUtils.isEmpty(xTraceId)) {
            xTraceId = currentSpan.context().traceIdString();
        }
        ((HttpServletResponse) response).addHeader("X-Rho-Traceid", xTraceId);

        currentSpan.tag("X-Rho-Traceid", xTraceId);

        // HttpServletRequest req = (HttpServletRequest) request;
        // MutableHttpServletRequest mutableRequest = new
        // MutableHttpServletRequest(req);
        // mutableRequest.putHeader("x-custom-header", "custom value");

        // CustomHttpServletRequest wrapper = new
        // CustomHttpServletRequest((HttpServletRequest) request);
        // wrapper.addHeader("ssoid-token", "ssss");

        // Enumeration headerNames = ((HttpServletRequest)
        // request).getHeaderNames();
        // ((HttpServletRequest) request).

        chain.doFilter(request, response);
    }

}
