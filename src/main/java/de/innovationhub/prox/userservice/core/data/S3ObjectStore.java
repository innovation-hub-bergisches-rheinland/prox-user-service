package de.innovationhub.prox.userservice.core.data;

import io.quarkus.cache.CacheResult;
import java.io.IOException;
import java.net.URL;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@ApplicationScoped
public class S3ObjectStore implements ObjectStoreRepository {
  private final String bucket;
  private final S3Client s3Client;

  public S3ObjectStore(@ConfigProperty(name = "bucket.name") String bucket, S3Client s3Client) {
    this.bucket = bucket;
    this.s3Client = s3Client;
  }

  @Override
  public FileObject getObject(String key) throws IOException {
    var request = GetObjectRequest.builder().bucket(bucket).key(key).build();

    try {
      var obj = s3Client.getObject(request);
      return new FileObject(key, obj.response().contentType(), obj.readAllBytes());
    } catch (NoSuchKeyException e) {
      throw new ObjectNotFoundException();
    }
  }

  @Override
  @CacheResult(cacheName = "s3-object-url")
  public URL getObjectUrl(String key) {
    var request = GetUrlRequest.builder().bucket(bucket).key(key).build();

    try {
      return s3Client.utilities().getUrl(request);
    } catch (NoSuchKeyException e) {
      throw new ObjectNotFoundException();
    }
  }

  @Override
  public void saveObject(FileObject obj) throws IOException {
    s3Client.putObject(
        PutObjectRequest.builder()
            .bucket(bucket)
            .key(obj.getKey())
            .contentType(obj.getMimeType())
            .build(),
        RequestBody.fromBytes(obj.getData()));
  }
}
