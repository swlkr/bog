-- name: insert-user<!
-- Creates a user
insert into users (
  email,
  password
) values (
  :email,
  :password
)

-- name: get-users-by-email
-- Gets a user by email
select *
from users
where email = :email
