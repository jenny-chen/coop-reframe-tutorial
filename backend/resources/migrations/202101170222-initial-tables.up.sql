CREATE TABLE if not exists users
  (id SERIAL PRIMARY KEY,
  "user-name" TEXT UNIQUE,
  pass TEXT);
--;;
CREATE TABLE if not exists texts
  (id SERIAL PRIMARY KEY,
  "user-id" INTEGER references users (id),
  text TEXT);
