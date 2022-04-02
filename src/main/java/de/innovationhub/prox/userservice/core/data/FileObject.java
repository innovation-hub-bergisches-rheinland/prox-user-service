package de.innovationhub.prox.userservice.core.data;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import lombok.Value;

@Value
public class FileObject {
  private String key;
  private String mimeType;
  private byte[] data;

  public Response createResponse() {
    ResponseBuilder response = Response.ok(this.getData());
    response.header("Content-Disposition", "attachment;filename=" + this.getKey());
    response.header("Content-Type", this.getMimeType());

    if (!this.getMimeType().equalsIgnoreCase("image/png")
        && !this.getMimeType().equalsIgnoreCase("image/jpeg"))
      throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);

    return response.build();
  }
}
