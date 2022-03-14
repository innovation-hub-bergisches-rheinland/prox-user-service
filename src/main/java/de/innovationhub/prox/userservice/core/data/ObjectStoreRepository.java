package de.innovationhub.prox.userservice.core.data;

import java.io.IOException;

public interface ObjectStoreRepository {
  FileObject getObject(String key) throws IOException;

  void saveObject(FileObject obj) throws IOException;
}
