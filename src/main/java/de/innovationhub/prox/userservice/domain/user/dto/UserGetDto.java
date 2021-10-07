package de.innovationhub.prox.userservice.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record UserGetDto(@JsonProperty("id") UUID id) {}
