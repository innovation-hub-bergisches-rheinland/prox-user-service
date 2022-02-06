package de.innovationhub.prox.userservice.organization.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ViewAllOrganizationMembershipsDto(
    @JsonProperty("members") List<ViewOrganizationMembershipDto> members) {}
