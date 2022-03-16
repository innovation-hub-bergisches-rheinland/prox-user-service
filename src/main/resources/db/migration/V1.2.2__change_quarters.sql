ALTER TABLE organizations
    ADD COLUMN quarter_location VARCHAR(255);

ALTER TABLE organization_profile_quarters
    DROP CONSTRAINT FK_ndcF4LGJchk6zGWb6aea;

DROP TABLE organization_profile_quarters;
