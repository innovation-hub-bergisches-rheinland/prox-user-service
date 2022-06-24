package de.innovationhub.prox.userservice.user.entity;

import de.innovationhub.prox.userservice.user.entity.profile.UserProfile;
import java.util.Optional;
import java.util.UUID;

public record User(UUID id, String name, String email, Optional<UserProfile> profile) {}
