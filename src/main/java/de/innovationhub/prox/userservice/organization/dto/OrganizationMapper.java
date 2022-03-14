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
    imports = {UUID.class})
public interface OrganizationMapper {
  ViewOrganizationDto toDto(Organization organization);

  void updateOrganization(@MappingTarget Organization organization, CreateOrganizationDto dto);

  @Mapping(target = "id", expression = "java( UUID.randomUUID() )")
  @Mapping(target = "name", source = "dto.name")
  @Mapping(target = "owner", source = "owner")
  @Mapping(target = "members", ignore = true)
  @Mapping(target = "profile", source = "dto.profile")
  Organization createFromDto(CreateOrganizationDto dto, UUID owner);

  @Mapping(target = "headquarter", source = "headquarter.location")
  ViewOrganizationProfileDto toDto(OrganizationProfile organizationProfile);

  @Mapping(target = "headquarter.location", source = "headquarter")
  OrganizationProfile createFromDto(OrganizationProfileRequestDto profile);

  default String toString(Quarter quarter) {
    return quarter.getLocation();
  }

  @Mapping(target = "location", source = "s")
  Quarter createQuarterFromString(String s);

  default String toString(Branch branch) {
    return branch.getName();
  }

  @Mapping(target = "name", source = "s")
  Branch createBranchFromString(String s);
}
