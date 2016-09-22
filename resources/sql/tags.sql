-- name: insert-tag<!
-- Creates a tag
insert into tags (
  id,
  draft_id,
  name
) values (
  :id::uuid,
  :draft_id::uuid,
  :name
)

-- name: update-tag<!
-- Updates a tag
update tags
set
  name = coalesce(:name, name)
where
  id = :id::uuid

-- name: get-tags
-- Gets a list of tags for given draft
select *
from tags
where draft_id = :draft_id::uuid
order by name, created_at

-- name: get-tags-by-id
-- Gets a list of tags by id
select *
from tags
where id = :id::uuid

-- name: delete-tag<!
-- Deletes an tag
delete
from tags
where id = :id::uuid
