package com.audition.configuration;

import com.audition.common.exception.SystemException;
import java.io.IOException;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResponseErrorHandler;

@Component
public class RestTemplateErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(final ClientHttpResponse httpResponse) throws IOException {
        return httpResponse.getStatusCode().isError();
    }

    @Override
    public void handleError(final ClientHttpResponse httpResponse) throws IOException {
        final var statusCode = httpResponse.getStatusCode();
        final var statusMessage = httpResponse.getStatusText();
        if (statusCode.is4xxClientError()) {
            throw new HttpClientErrorException(statusCode, statusMessage);
        } else if (statusCode.is5xxServerError()) {
            throw new HttpServerErrorException(statusCode, statusMessage);
        } else {
            throw new SystemException(statusMessage, 500);
        }
    }
}
