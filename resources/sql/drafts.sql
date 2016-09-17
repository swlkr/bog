-- name: insert-draft<!
-- Creates a draft
insert into drafts (
  id,
  user_id,
  title,
  content,
  sort_order,
  type
) values (
  :id::uuid,
  :user_id,
  :title,
  :content,
  :sort_order,
  :type
)

-- name: update-draft<!
-- Updates a draft
update drafts
set
  title = :title,
  content = :content,
  type = :type
where
  id = :id

-- name: get-drafts
-- Gets a list of drafts for given user
select *
from drafts
where user_id = :user_id
order by created_at desc

-- name: get-drafts-by-id
-- Gets a list of drafts by id
select *
from drafts
where id = :id
