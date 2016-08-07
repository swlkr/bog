-- name: insert-post<!
-- Creates a post
insert into posts (
  user_id,
  title,
  content,
  type,
  draft
) values (
  :user_id,
  :title,
  :content,
  :type,
  :draft
)

-- name: update-post<!
-- Updates a post
update posts
set
  title = :title,
  content = :content,
  type = :type,
  draft = :draft
where
  id = :id

-- name: get-posts
-- Gets a list of posts from all users
select *
from posts
where draft = false
order by created_at

-- name: get-posts-by-id
-- Gets a list of posts by id
select *
from posts
where id = :id

-- name: get-drafts
-- Gets a list of drafts from all users
select *
from posts
where draft = true
order by created_at

-- name: get-drafts-by-id
-- Gets a list of drafts by id
select *
from posts
where
  draft = true
  and
  id = :id
order by created_at
