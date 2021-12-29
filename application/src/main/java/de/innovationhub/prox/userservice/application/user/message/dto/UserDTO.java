package de.innovationhub.prox.userservice.application.user.message.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record UserDTO(@JsonProperty("principal") String principal) { }
