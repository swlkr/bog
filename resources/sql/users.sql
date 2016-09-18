-- name: insert-user<!
-- Creates a user
insert into users (
  id,
  email,
  password
) values (
  :id::uuid,
  :email,
  :password
)

-- name: get-users-by-email
-- Gets a user by email
select *
from users
where email = :email

-- name: update-user<!
-- Updates a user by id
update users
set
  email = coalesce(:email, email),
  password = coalesce(:password, password)
where id = id::uuid
