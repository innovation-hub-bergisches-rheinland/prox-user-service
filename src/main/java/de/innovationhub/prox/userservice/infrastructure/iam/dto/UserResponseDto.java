package de.innovationhub.prox.userservice.infrastructure.iam.dto;

import java.util.UUID;
import lombok.Value;

@Value
public class UserResponseDto {
  UUID id;
  String name;
}
