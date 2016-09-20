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
  :user_id::uuid,
  :title,
  :content,
  :sort_order,
  :type
)

-- name: update-draft<!
-- Updates a draft
update drafts
set
  title = coalesce(:title, title),
  content = coalesce(:content, content),
  type = coalesce(:type, type),
  sort_order = coalesce(:sort_order, sort_order)
where
  id = :id::uuid

-- name: get-drafts
-- Gets a list of drafts for given user
select *
from drafts
where user_id = :user_id::uuid
order by sort_order asc, created_at desc

-- name: get-drafts-by-id
-- Gets a list of drafts by id
select *
from drafts
where id = :id::uuid

-- name: delete-draft<!
-- Deletes a draft
delete
from drafts
where id = :id::uuid
