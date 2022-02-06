package de.innovationhub.prox.userservice.core.exception;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
  private final ApiErrorMapper mapper = ApiErrorMapper.INSTANCE;

  @Override
  public Response toResponse(ConstraintViolationException exception) {
    return Response.status(422).entity(mapper.toError(422, exception)).build();
  }
}
