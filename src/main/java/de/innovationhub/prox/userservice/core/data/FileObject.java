package de.innovationhub.prox.userservice.core.data;

import lombok.Value;

@Value
public class FileObject {
  private String key;
  private String mimeType;
  private byte[] data;
}
