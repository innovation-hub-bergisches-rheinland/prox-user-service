create table representative(
    id uuid not null unique,
    owner_id uuid not null unique,
    name varchar (255),
    primary key(id)
);

create table organizations(
    id uuid not null unique,
    name varchar(255) not null,
    owner_id uuid not null,
    primary key (id)
);

create table organization_memberships(
    user_id uuid not null,
    organization_id uuid not null,
    member_role smallint not null,
    unique (user_id, organization_id),
    foreign key (organization_id) references organizations
);
