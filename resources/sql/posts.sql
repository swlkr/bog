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

-- name: get-posts-by-user-id
-- Gets a list of posts by user id
select *
from posts
where user_id = :user_id
