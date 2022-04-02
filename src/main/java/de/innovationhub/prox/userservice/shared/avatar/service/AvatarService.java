package de.innovationhub.prox.userservice.shared.avatar.service;

import de.innovationhub.prox.userservice.core.data.FileObject;
import de.innovationhub.prox.userservice.core.data.FormDataBody;
import de.innovationhub.prox.userservice.core.data.ObjectStoreRepository;
import de.innovationhub.prox.userservice.shared.avatar.entity.Avatar;
import io.smallrye.common.constraint.Nullable;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

@ApplicationScoped
public class AvatarService {
  private final ObjectStoreRepository objectStoreRepository;

  @Inject
  public AvatarService(ObjectStoreRepository objectStoreRepository) {
    this.objectStoreRepository = objectStoreRepository;
  }

  public Response buildAvatarResponse(@Nullable Avatar avatar) throws IOException {
    if (avatar == null || avatar.getKey() == null || avatar.getKey().isBlank())
      return Response.status(404).build();

    var fileObj = objectStoreRepository.getObject(avatar.getKey());
    if (fileObj == null) return Response.status(404).build();

    ResponseBuilder response = Response.ok(fileObj.getData());
    response.header("Content-Disposition", "attachment;filename=" + fileObj.getKey());
    response.header("Content-Type", fileObj.getMimeType());

    if (!fileObj.getMimeType().equalsIgnoreCase("image/png")
        && !fileObj.getMimeType().equalsIgnoreCase("image/jpeg"))
      return Response.status(500).build();

    return response.build();
  }

  @Transactional
  public Avatar createAvatarFromFormBody(String key, FormDataBody formData) throws IOException {
    var bytes = formData.getData().readAllBytes();
    var mimeType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(bytes));

    // TODO: We could do some more validation here. Also, we could try to store the image in
    //  different resolutions

    // TODO: We should not throw a web application exception at this point
    if (mimeType == null
        || !mimeType.equalsIgnoreCase("image/png") && !mimeType.equalsIgnoreCase("image/jpeg"))
      throw new WebApplicationException(Status.BAD_REQUEST);

    String extension =
        switch (mimeType.trim().toLowerCase()) {
          case "image/png" -> ".png";
          case "image/jpeg" -> ".jpg";
          default -> "";
        };

    FileObject fileObject;
    if (key.endsWith(extension)) {
      fileObject = new FileObject(key, mimeType, bytes);
    } else {
      fileObject = new FileObject(key + extension, mimeType, bytes);
    }

    objectStoreRepository.saveObject(fileObject);
    return new Avatar(fileObject.getKey());
  }
}
