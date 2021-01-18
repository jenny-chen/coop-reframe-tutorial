-- Run this script as a superuser

CREATE ROLE reframe_test LOGIN PASSWORD 'refame_test' SUPERUSER;

CREATE DATABASE reframe_test
 WITH OWNER = replicator
      TEMPLATE template0
      ENCODING = 'UTF8'
      TABLESPACE = pg_default
      LC_COLLATE = 'en_US.UTF-8'
      LC_CTYPE = 'en_US.UTF-8'
      CONNECTION LIMIT = -1;
