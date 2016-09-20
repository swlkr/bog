-- name: insert-post<!
-- Creates a post
insert into posts (
  id,
  user_id,
  title,
  content,
  sort_order,
  type
) values (
  :id::uuid,
  :user_id::uuid,
  :title,
  :content,
  :sort_order,
  :type
)

-- name: update-post<!
-- Updates a post
update posts
set
  title = coalesce(:title, title),
  content = coalesce(:content, content),
  type = coalesce(:type, type),
  sort_order = coalesce(:sort_order, sort_order)
where
  id = :id::uuid

-- name: get-posts
-- Gets a list of posts from all users
select *
from posts
order by sort_order asc, created_at desc

-- name: get-posts-by-id
-- Gets a list of posts by id
select *
from posts
where id = :id::uuid

-- name: delete-post<!
-- Deletes a post
delete
from posts
where id = :id::uuid
