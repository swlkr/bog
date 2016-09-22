-- name: insert-comment<!
-- Creates a comment
insert into comments (
  id,
  draft_id,
  name,
  content
) values (
  :id::uuid,
  :draft_id::uuid,
  :name,
  :content
)

-- name: get-comments-by-draft-id
-- Gets a list of comments by draft id
select *
from comments
where draft_id = :draft_id::uuid
order by created_at

-- name: delete-comment<!
-- Deletes a comment
delete
from comments
where id = :id::uuid
