package com.audition.web.advice;

import com.audition.common.exception.SystemException;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;

@SpringBootTest
@AutoConfigureMockMvc
@Getter
class ExceptionControllerAdviceTest {

    @Autowired
    private ExceptionControllerAdvice exceptionControllerAdvice;

    @Test
    void handleHttpClientException() {
        final var e = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad juju");
        final var res = exceptionControllerAdvice.handleHttpClientException(e);
        final var exp = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        exp.setTitle("API Error Occurred");
        exp.setDetail("400 Bad juju");
        Assertions.assertEquals(exp, res);
    }

    @Test
    void handleMainException() {
        final var e = new IllegalArgumentException("something");
        final var x = exceptionControllerAdvice.handleMainException(e);
        final var expected = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        expected.setTitle("API Error Occurred");
        expected.setDetail("something");
        Assertions.assertEquals(expected, x);
    }

    @Test
    void handleSystemException() {
        final var e = new SystemException("Bleh", 400);
        final var x = exceptionControllerAdvice.handleSystemException(e);
        final var exp = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        exp.setDetail("Bleh");
        exp.setTitle("API Error Occurred");
        Assertions.assertEquals(exp.getDetail(), x.getDetail());
        Assertions.assertEquals(exp.getTitle(), x.getTitle());
    }
}