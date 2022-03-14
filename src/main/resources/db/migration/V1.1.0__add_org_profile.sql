ALTER TABLE organizations
    ADD COLUMN founding_date VARCHAR(255),
    ADD COLUMN number_of_employees VARCHAR(255),
    ADD COLUMN homepage VARCHAR(255),
    ADD COLUMN contact_email VARCHAR(255),
    ADD COLUMN vita TEXT,
    ADD COLUMN headquarter_location VARCHAR(255),
    ADD COLUMN facebook_handle VARCHAR(255),
    ADD COLUMN twitter_handle VARCHAR(255),
    ADD COLUMN instagram_handle VARCHAR(255),
    ADD COLUMN xing_handle VARCHAR(255),
    ADD COLUMN linkedin_handle VARCHAR(255);

CREATE TABLE organization_profile_quarters (
    organization_id uuid,
    location VARCHAR(255)
);

CREATE TABLE organization_profile_branches (
    organization_id uuid,
    name VARCHAR(255)
);

ALTER TABLE organization_profile_quarters ADD CONSTRAINT
    FK_ndcF4LGJchk6zGWb6aea FOREIGN KEY (organization_id) REFERENCES organizations(id);

ALTER TABLE organization_profile_branches ADD CONSTRAINT
    FK_IiCxPA1VCPkAHqyqGYKQ FOREIGN KEY (organization_id) REFERENCES organizations(id);
