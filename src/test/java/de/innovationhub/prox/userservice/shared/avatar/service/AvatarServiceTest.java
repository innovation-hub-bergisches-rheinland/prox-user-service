package de.innovationhub.prox.userservice.shared.avatar.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.userservice.core.data.FileObject;
import de.innovationhub.prox.userservice.core.data.FormDataBody;
import de.innovationhub.prox.userservice.core.data.ObjectStoreRepository;
import de.innovationhub.prox.userservice.shared.avatar.entity.Avatar;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AvatarServiceTest {

  ObjectStoreRepository storeRepository;
  AvatarService avatarService;

  @BeforeEach
  void setUp() {
    storeRepository = mock(ObjectStoreRepository.class);
    avatarService = new AvatarService(storeRepository);
  }

  @Test
  void shouldReturnNotFoundWhenAvatarNull() throws Exception {
    assertThat(avatarService.buildAvatarResponse(null).getStatus()).isEqualTo(404);
  }

  @Test
  void shouldReturnNotFoundWhenAvatarKeyIsBlank() throws Exception {
    assertThat(avatarService.buildAvatarResponse(new Avatar(null)).getStatus()).isEqualTo(404);
    assertThat(avatarService.buildAvatarResponse(new Avatar("  ")).getStatus()).isEqualTo(404);
  }

  @Test
  void shouldReturnNotFoundWhenFileObjectNull() throws Exception {
    when(storeRepository.getObject(any())).thenReturn(null);
    assertThat(avatarService.buildAvatarResponse(getDummyAvatar()).getStatus()).isEqualTo(404);

    verify(storeRepository).getObject(any());
  }

  @Test
  void shouldReturn5xxOnInvalidMimeTime() throws Exception {
    var fileObj = new FileObject("abc", "document/txt", new byte[] {});

    when(storeRepository.getObject(any())).thenReturn(fileObj);
    assertThat(avatarService.buildAvatarResponse(getDummyAvatar()).getStatus() / 100).isEqualTo(5);

    verify(storeRepository).getObject(any());
  }

  @Test
  void shouldSetContentHeaders() throws Exception {
    var fileObj = new FileObject("abc", "image/jpeg", new byte[] {});

    when(storeRepository.getObject(any())).thenReturn(fileObj);
    var response = avatarService.buildAvatarResponse(getDummyAvatar());
    assertThat(response.getHeaders()).containsKeys("Content-Disposition", "Content-Type");

    verify(storeRepository).getObject(any());
  }

  @Test
  void shouldReturnByteStream() throws Exception {
    var fileObj =
        new FileObject("abc", "image/jpeg", new byte[] {0x00, 0x1B, 0x2B, 0x3B, 0x4B, 0x5B});

    when(storeRepository.getObject(any())).thenReturn(fileObj);
    var response = avatarService.buildAvatarResponse(getDummyAvatar());
    assertThat(response.getEntity())
        .isInstanceOf(byte[].class)
        .matches(arr -> Arrays.equals((byte[]) arr, fileObj.getData()));

    verify(storeRepository).getObject(any());
  }

  @Test
  void shouldStoreImage() throws Exception {
    var key = "batman";
    var formData = new FormDataBody();
    formData.setData(new ByteArrayInputStream(readPixelImage()));

    var response = avatarService.createAvatarFromFormBody(key, formData);
    assertThat(response.getKey()).isEqualTo("batman.png");

    var expectedFileObject = new FileObject("batman.png", "image/png", readPixelImage());
    verify(storeRepository).saveObject(eq("batman.png"), eq(expectedFileObject));
  }

  private Avatar getDummyAvatar() {
    return new Avatar("abc");
  }

  private byte[] readPixelImage() throws IOException {
    return getClass().getClassLoader().getResourceAsStream("img/pixel-image.png").readAllBytes();
  }
}
