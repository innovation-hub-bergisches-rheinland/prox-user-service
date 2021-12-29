package de.innovationhub.prox.userservice.application.user.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.innovationhub.prox.userservice.application.user.message.dto.UserDTO;
import java.util.UUID;

public record ReadUserResponse(@JsonProperty("user") UserDTO user) {}
