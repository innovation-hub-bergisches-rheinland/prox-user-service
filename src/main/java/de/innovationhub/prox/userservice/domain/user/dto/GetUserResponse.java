package de.innovationhub.prox.userservice.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import javax.validation.constraints.NotNull;

public record GetUserResponse(@JsonProperty("id") @NotNull UUID id) {}
