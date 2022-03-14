CREATE TABLE organization_profiles (
    organization_id uuid primary key,
    founding_date VARCHAR(255),
    number_of_employees VARCHAR(255),
    homepage VARCHAR(255),
    contact_email VARCHAR(255),
    vita TEXT,
    headquarter_location VARCHAR(255),
    facebook_handle VARCHAR(255),
    twitter_handle VARCHAR(255),
    instagram_handle VARCHAR(255),
    xing_handle VARCHAR(255),
    linkedin_handle VARCHAR(255)
);

CREATE TABLE organization_profile_quarters (
    organization_profile_id uuid primary key,
    location VARCHAR(255)
);


CREATE TABLE organization_profile_branches (
    organization_profile_id uuid primary key,
    name VARCHAR(255)
);

ALTER TABLE organization_profiles ADD CONSTRAINT
    FK_KFF9qz0Mj0m1DEOETdcv FOREIGN KEY (organization_id) REFERENCES organizations(id);

ALTER TABLE organization_profile_quarters ADD CONSTRAINT
    FK_ndcF4LGJchk6zGWb6aea FOREIGN KEY (organization_profile_id) REFERENCES organization_profiles(organization_id);

ALTER TABLE organization_profile_branches ADD CONSTRAINT
    FK_IiCxPA1VCPkAHqyqGYKQ FOREIGN KEY (organization_profile_id) REFERENCES organization_profiles(organization_id);
