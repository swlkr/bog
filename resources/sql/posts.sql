-- name: insert-post<!
-- Creates a post
insert into posts (
  user_id,
  title,
  content,
  type
) values (
  :user_id,
  :title,
  :content,
  :type
)

-- name: get-posts
-- Gets a list of posts from all users (there's only one so this doesn't really matter)
select *
from posts
order by created_at
