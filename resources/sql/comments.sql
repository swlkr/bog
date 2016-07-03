-- name: insert-comment<!
-- Creates a comment
insert into comments (
  post_id,
  name,
  content
) values (
  :post_id,
  :name,
  :content
)

-- name: get-comments-by-post-id
-- Gets a list of comments by post id
select *
from comments
where post_id = :post_id
order by created_at
