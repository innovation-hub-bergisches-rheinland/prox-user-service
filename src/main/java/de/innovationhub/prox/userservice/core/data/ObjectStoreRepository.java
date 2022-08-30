package de.innovationhub.prox.userservice.core.data;

import java.io.IOException;
import java.net.URL;

public interface ObjectStoreRepository {
  FileObject getObject(String key) throws IOException;

  URL getObjectUrl(String key) throws IOException;

  void saveObject(FileObject obj) throws IOException;
}
