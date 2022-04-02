CREATE TABLE user_profiles (
    user_id uuid not null,
    affiliation varchar(255),
    college_page varchar(255),
    consultation_hour varchar(255),
    email varchar(255),
    homepage varchar(255),
    room varchar(255),
    telephone varchar(255),
    main_subject varchar(255),
    name varchar(255),
    vita text,
    avatar_object_key varchar(255),
    primary key (user_id)
);

create table user_publications (user_id uuid not null, publication varchar(1023));
create table user_research_subjects (user_id uuid not null, subject varchar(255));
