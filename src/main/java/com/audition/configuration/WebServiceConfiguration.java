package com.audition.configuration;

import com.audition.common.logging.AuditionLogger;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@Getter
public class WebServiceConfiguration implements WebMvcConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebServiceConfiguration.class);
    private static final String YEAR_MONTH_DAY_PATTERN = "yyyy-MM-dd";
    private static final Locale LOCALE = Locale.getDefault();
    private final AuditionLogger auditionLogger;

    public WebServiceConfiguration(final @Autowired AuditionLogger auditionLogger) {
        this.auditionLogger = auditionLogger;
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        final DateFormat df = new SimpleDateFormat(YEAR_MONTH_DAY_PATTERN, LOCALE);
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(df);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return objectMapper;
    }

    @Bean
    public RestTemplate restTemplate(final @Autowired RestTemplateErrorHandler restTemplateErrorHandler) {
        final RestTemplate restTemplate = new RestTemplate(
            new BufferingClientHttpRequestFactory(createClientFactory()));

        restTemplate.getMessageConverters()
            .add(0, new MappingJackson2HttpMessageConverter(objectMapper()));

        restTemplate.getInterceptors().add(
            (req, reqBody, ex) -> {
                final String requestLog = String.format("Requested: %s %s", req.getMethod(), req.getURI());
                auditionLogger.info(LOGGER, requestLog);
                final ClientHttpResponse response = ex.execute(req, reqBody);
                final String responseLog = String.format("Response: %s", response.getStatusCode());
                auditionLogger.info(LOGGER, responseLog);
                return response;
            }
        );

        restTemplate.setErrorHandler(restTemplateErrorHandler);

        return restTemplate;
    }

    private SimpleClientHttpRequestFactory createClientFactory() {
        final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        return requestFactory;
    }
}
