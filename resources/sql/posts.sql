-- name: insert-post<!
-- Creates a post
insert into posts (
  user_id,
  title,
  content,
  type,
  sort_order
) values (
  :user_id,
  :title,
  :content,
  :type,
  :sort_order
)
