package de.innovationhub.prox.userservice.organization.dto;

import de.innovationhub.prox.userservice.organization.dto.request.CreateOrganizationDto;
import de.innovationhub.prox.userservice.organization.dto.request.OrganizationProfileRequestDto;
import de.innovationhub.prox.userservice.organization.dto.response.ViewOrganizationDto;
import de.innovationhub.prox.userservice.organization.dto.response.ViewOrganizationProfileDto;
import de.innovationhub.prox.userservice.organization.entity.Organization;
import de.innovationhub.prox.userservice.organization.entity.profile.Branch;
import de.innovationhub.prox.userservice.organization.entity.profile.OrganizationProfile;
import de.innovationhub.prox.userservice.organization.entity.profile.Quarter;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
    componentModel = "cdi",
    uses = OrganizationPermissionsMapper.class,
    imports = {UUID.class})
public interface OrganizationMapper {
  @Mapping(source = "organization", target = "permissions")
  ViewOrganizationDto toDto(Organization organization);

  void updateOrganization(@MappingTarget Organization organization, CreateOrganizationDto dto);

  @Mapping(target = "id", expression = "java( UUID.randomUUID() )")
  @Mapping(target = "name", source = "dto.name")
  @Mapping(target = "members", ignore = true)
  @Mapping(target = "profile", source = "dto.profile")
  Organization createFromDto(CreateOrganizationDto dto);

  @Mapping(target = "headquarter", source = "headquarter.location")
  @Mapping(target = "quarters", source = "quarters.location")
  ViewOrganizationProfileDto toDto(OrganizationProfile organizationProfile);

  @Mapping(target = "headquarter", source = "headquarter")
  @Mapping(target = "quarters", source = "quarters")
  OrganizationProfile createFromDto(OrganizationProfileRequestDto profile);

  default String toString(Quarter quarter) {
    return quarter == null ? null : quarter.getLocation();
  }

  @Mapping(target = "location", source = "s")
  default Quarter createQuarterFromString(String s) {
    if (s != null && !s.isBlank()) {
      return new Quarter(s);
    }
    return null;
  }

  default String toString(Branch branch) {
    return branch == null ? null : branch.getName();
  }

  @Mapping(target = "name", source = "s")
  default Branch createBranchFromString(String s) {
    if (s != null && !s.isBlank()) {
      return new Branch(s);
    }
    return null;
  }
}
