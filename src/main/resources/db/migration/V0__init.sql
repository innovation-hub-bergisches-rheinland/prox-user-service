create table users(
    id uuid,
    primary key(id)
);

create table organizations(
    id uuid,
    name varchar(255) not null,
    primary key (id)
);

create table organization_owners(
    organization_id uuid not null,
    user_id uuid not null,
    foreign key (organization_id) references organizations,
    foreign key (user_id) references users
);

create table organization_members(
    organization_id uuid not null,
    user_id uuid not null,
    foreign key (organization_id) references organizations,
    foreign key (user_id) references users
);