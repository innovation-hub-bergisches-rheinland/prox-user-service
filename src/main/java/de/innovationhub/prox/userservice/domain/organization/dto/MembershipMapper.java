package de.innovationhub.prox.userservice.domain.organization.dto;

import de.innovationhub.prox.userservice.domain.organization.Membership;
import de.innovationhub.prox.userservice.domain.user.dto.UserMapper;
import org.mapstruct.Mapper;

@Mapper(uses = { OrganizationMapper.class, UserMapper.class })
public interface MembershipMapper {
  MembershipOmitUserGetDto membershipToOmitUserGetDto(Membership membership);
  MembershipOmitOrganizationGetDto membershipToOmitOrganizationGetDto(
    Membership membership
  );
}
