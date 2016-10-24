-- name: insert-draft<!
-- Creates a draft
insert into drafts (
  id,
  user_id,
  title,
  content,
  sort_order,
  type,
  published
) values (
  :id::uuid,
  :user_id::uuid,
  :title,
  :content,
  :sort_order,
  :type,
  :published
)

-- name: update-draft<!
-- Updates a draft
update drafts
set
  title = coalesce(:title, title),
  content = coalesce(:content, content),
  type = coalesce(:type, type),
  sort_order = coalesce(:sort_order, sort_order),
  published = coalesce(:published, published)
where
  id = :id::uuid

-- name: get-drafts
-- Gets a list of drafts for given user
select
  drafts.*,
  (CURRENT_DATE - created_at::date) days_ago
from drafts
where user_id = :user_id::uuid
order by sort_order asc, created_at desc
limit 10
offset 0

-- name: get-drafts-by-id
-- Gets a list of drafts by id
select
  drafts.*,
  (CURRENT_DATE - created_at::date) days_ago
from drafts
where id = :id::uuid

-- name: delete-draft<!
-- Deletes a draft
delete
from drafts
where id = :id::uuid

-- name: get-posts
-- Gets a list of published drafts
select
  drafts.*,
  (CURRENT_DATE - created_at::date) days_ago
from drafts
where published = true
order by sort_order, created_at desc
limit 10
offset 0

-- name: get-posts-by-id
-- Gets a published draft by id
select
  drafts.*,
  (CURRENT_DATE - created_at::date) days_ago
from drafts
where id = :id::uuid and published = true
