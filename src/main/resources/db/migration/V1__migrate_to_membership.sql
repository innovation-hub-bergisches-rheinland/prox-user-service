create table organization_memberships(
    user_id uuid not null,
    organization_id uuid not null,
    type int4 not null ,
    primary key (user_id, organization_id),
    foreign key (user_id) references users,
    foreign key (organization_id) references organizations
);

insert into organization_memberships(user_id, organization_id, type)
    SELECT user_id, organization_id, true from organization_owners;

insert into organization_memberships(user_id, organization_id, type)
    SELECT user_id, organization_id, false from organization_members;

drop table organization_owners;
drop table organization_members;

