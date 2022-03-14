package de.innovationhub.prox.userservice.core.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@QuarkusTest
@QuarkusTestResource(LocalStackS3Resource.class)
class S3ObjectStoreTest {

  @Inject S3Client s3Client;

  @Inject S3ObjectStore s3ObjectStore;

  @ConfigProperty(name = "bucket.name")
  String bucket;

  @Test
  void shouldNotFindFile() {
    assertThatThrownBy(() -> s3ObjectStore.getObject(UUID.randomUUID().toString()))
        .isInstanceOf(ObjectNotFoundException.class);
  }

  @BeforeEach
  void setUp() {

    try {
      s3Client.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
    } catch (NoSuchBucketException e) {
      s3Client.createBucket(CreateBucketRequest.builder().bucket(bucket).build());
    }
  }

  @Test
  void shouldFindFile() throws Exception {
    s3Client.putObject(
        PutObjectRequest.builder()
            .contentType("text/plain")
            .key("test-text")
            .bucket(bucket)
            .build(),
        RequestBody.fromString("Test test 123"));

    assertThat(s3ObjectStore.getObject("test-text"))
        .extracting(FileObject::getKey, FileObject::getMimeType, FileObject::getData)
        .containsExactly(
            "test-text", "text/plain", "Test test 123".getBytes(StandardCharsets.UTF_8));
  }

  @Test
  void shouldSaveFile() throws Exception {
    s3ObjectStore.saveObject(
        new FileObject(
            "test-text", "text/plain", "Test test 123".getBytes(StandardCharsets.UTF_8)));

    var response =
        s3Client.getObject(GetObjectRequest.builder().bucket(bucket).key("test-text").build());
    assertThat(response)
        .hasSameContentAs(
            new ByteArrayInputStream("Test test 123".getBytes(StandardCharsets.UTF_8)));
    assertThat(response.response()).matches(r -> r.contentType().equals("text/plain"));
  }

  @Test
  void shouldOverwriteFile() throws Exception {
    s3Client.putObject(
        PutObjectRequest.builder()
            .contentType("text/plain")
            .key("test-text")
            .bucket(bucket)
            .build(),
        RequestBody.fromString("Test test 123"));

    s3ObjectStore.saveObject(
        new FileObject(
            "test-text", "text/plain", "Test test 456".getBytes(StandardCharsets.UTF_8)));

    var response =
        s3Client.getObject(GetObjectRequest.builder().bucket(bucket).key("test-text").build());
    assertThat(response)
        .hasSameContentAs(
            new ByteArrayInputStream("Test test 456".getBytes(StandardCharsets.UTF_8)));
    assertThat(response.response()).matches(r -> r.contentType().equals("text/plain"));
  }
}
