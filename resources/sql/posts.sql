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

-- name: update-post<!
-- Updates a post
update posts
set
  title = :title,
  content = :content,
  type = :type
where
  id = :id

-- name: get-posts
-- Gets a list of posts from all users
select *
from posts
where user_id = :user_id
order by sort_order asc, created_at desc

-- name: get-posts-by-id
-- Gets a list of posts by id
select *
from posts
where id = :id
