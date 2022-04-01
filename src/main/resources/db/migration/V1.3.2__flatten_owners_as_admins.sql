INSERT INTO organization_memberships(user_id, organization_id, member_role)
    SELECT owner_id as user_id, id as organization_id, 1 FROM organizations;

ALTER TABLE organizations
    DROP COLUMN owner_id;


