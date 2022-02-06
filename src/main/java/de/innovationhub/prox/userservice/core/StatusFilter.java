package de.innovationhub.prox.userservice.core;

import java.io.IOException;
import java.lang.annotation.Annotation;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
@Status
public class StatusFilter implements ContainerResponseFilter {

  @Override
  public void filter(
      ContainerRequestContext requestContext, ContainerResponseContext responseContext)
      throws IOException {
    if (responseContext.getStatus() == 200) {
      for (Annotation annotation : responseContext.getEntityAnnotations()) {
        if (annotation instanceof Status) {
          responseContext.setStatus(((Status) annotation).value());
          break;
        }
      }
    }
  }
}
