package de.innovationhub.prox.userservice.application.user.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record ReadUserResponse(@JsonProperty("id") UUID id) {}
