package com.audition.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class ResponseHeaderInjector extends HttpFilter {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final String TRACE_ID = "traceId";
    private static final String SPAN_ID = "spanId";

    @Override
    protected void doFilter(final HttpServletRequest request, final HttpServletResponse response,
        final FilterChain chain)
        throws IOException, ServletException {
        final String traceId = MDC.get(TRACE_ID);
        final String spanId = MDC.get(SPAN_ID);
        response.addHeader(TRACE_ID, traceId);
        response.addHeader(SPAN_ID, spanId);
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(TRACE_ID);
            MDC.remove(SPAN_ID);
        }
    }
}

