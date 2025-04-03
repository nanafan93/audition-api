package com.audition.common.exception;

import java.io.Serial;
import lombok.Getter;

@Getter
public class SystemException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -5876728854007114881L;

    public static final String DEFAULT_TITLE = "API Error Occurred";
    private final Integer statusCode;
    private final String title;
    private final String detail;

    public SystemException(final String detail, final Integer errorCode) {
        super(detail);
        this.statusCode = errorCode;
        this.title = DEFAULT_TITLE;
        this.detail = detail;
    }
}
