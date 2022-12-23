/*
This files contains scripts used to migrate data from the services database to prox-backend database.
*/

/* lecturer */
SELECT
    l.user_id as id,
    l.name as name,
    l.affiliation as affiliation,
    l.college_page as college_page,
    l.consultation_hour as consultation_hour,
    l.email as email,
    l.homepage as homepage,
    l.room as room,
    l.main_subject as subject,
    l.telephone as telephone,
    l.vita as vita,
    l.user_id as user_id,
    current_timestamp as created_at,
    current_timestamp as modified_at,
    true as visible_in_public_search
    FROM user_profiles l;

/* lecturer_publications */
SELECT
    p.user_id as lecturer_id,
    p.publication as publications
FROM user_publications p;

/* organization */
SELECT
    o.id,
    o.name,
    o.contact_email,
    o.founding_date,
    o.headquarter_location as headquarter,
    o.homepage,
    o.number_of_employees,
    o.vita,
    o.quarter_location as quarters,
    current_timestamp as created_at,
    current_timestamp as modified_at
    FROM organizations o;

/* member */
SELECT
    o.user_id as id,
    o.user_id
    FROM organization_memberships o;

/* membership */
SELECT
    o.user_id as member_id,
    o.member_role as role
    FROM organization_memberships o;

/* organization_members */
SELECT
    o.organization_id,
    o.user_id as members_member_id
    FROM organization_memberships o;

/* organization_social_media-handles */
SELECT
    o.id as organization_id,
    o.facebook_handle as social_media_handles,
    0 as social_media_handles_key
    FROM organizations o
    WHERE o.facebook_handle != ''
UNION
SELECT
    o.id as organization_id,
    o.twitter_handle as social_media_handles,
    1 as social_media_handles_key
    FROM organizations o
    WHERE o.twitter_handle != ''
UNION
SELECT
    o.id as organization_id,
    o.linkedin_handle as social_media_handles,
    2 as social_media_handles_key
    FROM organizations o
    WHERE o.linkedin_handle != ''
UNION
SELECT
    o.id as organization_id,
    o.xing_handle as social_media_handles,
    3 as social_media_handles_key
    FROM organizations o
    WHERE o.xing_handle != ''
UNION
SELECT
    o.id as organization_id,
    o.instagram_handle as social_media_handles,
    4 as social_media_handles_key
    FROM organizations o
    WHERE o.instagram_handle != ''
UNION
SELECT
    o.id as organization_id,
    o.youtube_handle as social_media_handles,
    5 as social_media_handles_key
    FROM organizations o
    WHERE o.youtube_handle != ''
;
