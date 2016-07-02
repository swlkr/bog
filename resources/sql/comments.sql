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
