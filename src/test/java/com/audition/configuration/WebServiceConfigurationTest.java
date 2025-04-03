package com.audition.configuration;

import com.audition.model.PostComment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import java.util.Calendar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@Getter
class WebServiceConfigurationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    private static class TestDataClass {

        private String foo;
        private String bar;
    }


    @Test
    void objectMapperLoads() {
        Assertions.assertNotNull(objectMapper);
    }

    @Test
    void objectMapperDoesntFailForUnknownProperties() {
        final var foo = Assertions.assertDoesNotThrow(
            () -> objectMapper.readValue("{\"foo\":\"value_for_foo\",\"bar\":\"value_for_bar\",\"baz\":\"null\"}",
                TestDataClass.class));

        Assertions.assertNotNull(foo);
        Assertions.assertEquals("value_for_foo", foo.getFoo());
        Assertions.assertEquals("value_for_bar", foo.getBar());
    }

    @Test
    void objectMapperTest() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.JANUARY, 2, 13, 25, 25);

        final String result = Assertions.assertDoesNotThrow(() -> objectMapper.writeValueAsString(calendar.getTime()));
        final String expected = "\"2025-01-02\"";

        Assertions.assertEquals(expected, result);
    }

    @Test
    void objectMapperNamingStrategyTest() {
        Assertions.assertEquals(PropertyNamingStrategies.LOWER_CAMEL_CASE,
            objectMapper.getPropertyNamingStrategy());
    }

    @Test
    void objectMapperIncludeNullOrEmptyValues() {
        final String expected = "{\"foo\":\"foo\"}";

        final String actual = Assertions.assertDoesNotThrow(
            () -> objectMapper.writeValueAsString(new TestDataClass("foo", null)));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testDeserializePostComment() throws JsonProcessingException {
        final var postComment = objectMapper.readValue(
            "{\"postId\":1,\"id\":10,\"name\":\"Test User\",\"email\":\"test.user@example.com\",\"body\":\""
                + "This is a test comment.\"}",
            PostComment.class);
        Assertions.assertNotNull(postComment);
        Assertions.assertEquals(1, postComment.postId());
        Assertions.assertEquals(10, postComment.id());
        Assertions.assertEquals("Test User", postComment.name());
        Assertions.assertEquals("test.user@example.com", postComment.email());
        Assertions.assertEquals("This is a test comment.", postComment.body());
    }
}