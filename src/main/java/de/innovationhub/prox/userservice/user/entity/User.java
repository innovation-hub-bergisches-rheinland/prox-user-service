package de.innovationhub.prox.userservice.user.entity;

import java.util.UUID;

public record User(UUID id, String name, String email) {}
