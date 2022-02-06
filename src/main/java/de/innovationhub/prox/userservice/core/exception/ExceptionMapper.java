package de.innovationhub.prox.userservice.core.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {
  private final ApiErrorMapper mapper = ApiErrorMapper.INSTANCE;

  @Override
  public Response toResponse(Exception exception) {
    return Response.status(500).entity(mapper.toError(500, exception)).build();
  }
}
