-- name: insert-comment<!
-- Creates a comment
insert into comments (
  id,
  post_id,
  name,
  content
) values (
  :id::uuid,
  :post_id::uuid,
  :name,
  :content
)

-- name: get-comments-by-post-id
-- Gets a list of comments by post id
select *
from comments
where post_id = :post_id::uuid
order by created_at

-- name: delete-comment<!
-- Deletes a comment
delete
from comments
where id = :id::uuid
