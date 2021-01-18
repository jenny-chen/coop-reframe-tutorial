-- :name create-user! :! :1
-- :doc creates a new user record
INSERT INTO users ("user-name", pass)
VALUES (:user-name, :pass) RETURNING *;

-- :name get-user :? :1
-- :doc retrieves a user record given the  user-name and password
SELECT * FROM users
WHERE "user-name" = :user-name and pass=:pass;

-- :name get-user-names :? :n
-- :doc get all user names
SELECT "user-name" FROM users;

-- :name create-user-text! :! :n
INSERT INTO texts ("user-id", "text")
VALUES (:user-id, :text);

-- :name update-user-text! :! :1
UPDATE texts set text=:text where "user-id"=:user-id RETURNING *;

-- :name get-text :? :1
-- :doc retrieves a user text given user-id
SELECT * FROM texts
 WHERE "user-id" = :user-id;
