package de.innovationhub.prox.userservice.core.data;

import java.io.InputStream;
import javax.ws.rs.FormParam;
import lombok.Data;

@Data
public class FormDataBody {
  @FormParam("file")
  private InputStream data;
}
