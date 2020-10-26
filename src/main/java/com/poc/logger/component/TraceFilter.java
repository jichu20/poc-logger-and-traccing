package com.poc.logger.component;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.poc.logger.util.Constant;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import brave.Span;
import brave.Tracer;
import brave.propagation.ExtraFieldPropagation;

@Component
public class TraceFilter extends GenericFilterBean {

    @Value("${logger.namespace:${namespace:ns}}")
    private String namespace;

    @Value("${logger.monitorResource:mr}")
    private String monitorResource;

    private final Tracer tracer;

    TraceFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        Span currentSpan = this.tracer.currentSpan();

        if (currentSpan == null) {
            chain.doFilter(request, response);
            return;
        }

        checkxRhoTraceId(request, response);
        String xSpanId = checkxRhoSpanId(request, response);
        checkxRhoParentSpanId(request, response, xSpanId);

        chain.doFilter(request, response);
    }

    private String checkxRhoSpanId(ServletRequest request, ServletResponse response) {

        Span currentSpan = this.tracer.currentSpan();

        // Recuperamos el valor de la cabecera
        String xSpanId = ((HttpServletRequest) request).getHeader(Constant.RHO_SPAN_ID_HEADER);

        // Si la cabecera no viene y el valor es nulo, generamos un nuevo valor
        if (StringUtils.isBlank(xSpanId)) {
            xSpanId = UUID.randomUUID().toString();
            // Agregamos el valor a la cabecera
            ExtraFieldPropagation.set(currentSpan.context(), Constant.RHO_SPAN_ID_HEADER, xSpanId);
        }

        // Agrego la cabecera a la respuesta
        ((HttpServletResponse) response).addHeader(Constant.RHO_SPAN_ID_HEADER, xSpanId);
        currentSpan.tag(Constant.RHO_SPAN_ID_HEADER, xSpanId);

        // Hacemos accesible el valor de la cabecera para las trazas
        MDC.put(Constant.RHO_SPAN_ID_HEADER, xSpanId);
        return xSpanId;

    }

    private void checkxRhoParentSpanId(ServletRequest request, ServletResponse response, String spanId) {

        Span currentSpan = this.tracer.currentSpan();

        // Recuperamos el valor de la cabecera
        String xParentSpanId = ((HttpServletRequest) request).getHeader(Constant.RHO_PARENTSPANID_HEADER);

        // Si la cabecera no viene y el valor es nulo, generamos un nuevo valor
        if (StringUtils.isBlank(xParentSpanId)) {
            String parentSpanIdPattern = "ns/%s/mrs/%s/spans/%s";
            xParentSpanId = String.format(parentSpanIdPattern, namespace, monitorResource, spanId);

            // Agregamos el valor a la cabecera
            ExtraFieldPropagation.set(currentSpan.context(), Constant.RHO_PARENTSPANID_HEADER, xParentSpanId);
        }

        // Agrego la cabecera a la respuesta
        ((HttpServletResponse) response).addHeader(Constant.RHO_PARENTSPANID_HEADER, xParentSpanId);
        currentSpan.tag(Constant.RHO_PARENTSPANID_HEADER, xParentSpanId);

        // Hacemos accesible el valor de la cabecera para las trazas
        MDC.put(Constant.RHO_PARENTSPANID_HEADER, xParentSpanId);

    }

    private void checkxRhoTraceId(ServletRequest request, ServletResponse response) {

        Span currentSpan = this.tracer.currentSpan();

        // Recuperamos el valor de la cabecera
        String xTraceId = ((HttpServletRequest) request).getHeader(Constant.RHO_TRACE_ID_HEADER);

        // Si la cabecera no viene y el valor es nulo, generamos un nuevo valor
        if (StringUtils.isBlank(xTraceId)) {
            xTraceId = UUID.randomUUID().toString();
            // Agregamos el valor a la cabecera
            ExtraFieldPropagation.set(currentSpan.context(), Constant.RHO_TRACE_ID_HEADER, xTraceId);
        }

        // Agrego la cabecera a la respuesta
        ((HttpServletResponse) response).addHeader(Constant.RHO_TRACE_ID_HEADER, xTraceId);
        currentSpan.tag(Constant.RHO_TRACE_ID_HEADER, xTraceId);

        // Hacemos accesible el valor de la cabecera para las trazas
        MDC.put(Constant.RHO_TRACE_ID_HEADER, xTraceId);

    }

}
