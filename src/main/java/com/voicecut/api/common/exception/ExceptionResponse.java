package com.voicecut.api.common.exception;

public record ExceptionResponse(
    String code,
    String message
) {
}

