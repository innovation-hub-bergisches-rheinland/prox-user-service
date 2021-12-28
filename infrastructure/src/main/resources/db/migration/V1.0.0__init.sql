create table users(
    id uuid,
    primary key(id)
);

create table organizations(
    id uuid,
    name varchar(255) not null,
    primary key (id)
);

create table organization_memberships(
    user_id uuid,
    organization_id uuid,
    role varchar (25),
    primary key (user_id, organization_id),
    foreign key (organization_id) references organizations,
    foreign key (user_id) references users
);