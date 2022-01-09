package de.innovationhub.prox.userservice.application.organization.message;

import de.innovationhub.prox.userservice.application.organization.message.request.CreateOrganizationRequest;
import de.innovationhub.prox.userservice.application.organization.message.response.OrganizationResponse;
import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi", imports = {UUID.class})
public interface OrganizationMessageMapper {
  OrganizationMessageMapper INSTANCE = Mappers.getMapper(OrganizationMessageMapper.class);

  @Mapping(target = "id", source = "id.id")
  @Mapping(target = "name", source = "name")
  @Mapping(target = "ownerPrincipal", source = "owner.principal")
  OrganizationResponse createResponse(Organization organization);

  @Mapping(target = "id.id", expression = "java( UUID.randomUUID() )")
  @Mapping(target = "name", source = "name")
  @Mapping(target = "owner.principal", source = "ownerPrincipal")
  Organization fromRequest(CreateOrganizationRequest request);
}
