package de.innovationhub.prox.userservice.core.exception;

import java.time.Instant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(imports = {Instant.class})
public interface ApiErrorMapper {
  ApiErrorMapper INSTANCE = Mappers.getMapper(ApiErrorMapper.class);
  @Mapping(target = "status", source = "status")
  @Mapping(target = "message", source = "e.message")
  @Mapping(target = "timestamp", expression = "java( Instant.now() )")
  ApiError toError(Integer status, Exception e);
}
