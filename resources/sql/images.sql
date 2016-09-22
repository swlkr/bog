-- name: insert-image<!
-- Creates an image
insert into images (
  id,
  draft_id,
  filename,
  url
) values (
  :id::uuid,
  :draft_id::uuid,
  :filename,
  :url
)

-- name: update-image<!
-- Updates an image
update images
set
  filename = coalesce(:filename, filename),
  url = coalesce(:url, url)
where
  id = :id::uuid

-- name: get-images
-- Gets a list of images for given draft
select *
from images
where draft_id = :draft_id::uuid
order by created_at

-- name: get-images-by-id
-- Gets a list of images by id
select *
from images
where id = :id::uuid

-- name: delete-image<!
-- Deletes an image
delete
from images
where id = :id::uuid
