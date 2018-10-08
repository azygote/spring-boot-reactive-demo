CREATE TABLE "public"."t_sys_user"
(
  "username"      VARCHAR(255) PRIMARY KEY,
  "password"      VARCHAR(255),
  "roles"         TEXT,
  "delete_flag"   SMALLINT,
  "created_date"  BIGINT,
  "modified_date" BIGINT
);

INSERT INTO "public"."t_sys_user" ("username", "password", "roles", "delete_flag", "created_date", "modified_date")
VALUES ('gty',
        '$2a$10$CMXlTuPwCfI8TS3GhOHBaeYs5t3TkBc7qHCLeJuKNQm6NQkxcuWmi',
        '[user, actuator]',
        1,
        1527046876921,
        1527046876921);
