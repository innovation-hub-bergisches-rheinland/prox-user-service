package de.innovationhub.prox.userservice.user.dto;

import java.util.UUID;
import lombok.Value;

@Value
public class UserSearchResponseDto {
  UUID id;
  String name;
}
