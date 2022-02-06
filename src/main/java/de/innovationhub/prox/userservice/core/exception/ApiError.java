package de.innovationhub.prox.userservice.core.exception;

import java.time.Instant;

public record ApiError(
    int status,
    String message,
    Instant timestamp
) {

}
