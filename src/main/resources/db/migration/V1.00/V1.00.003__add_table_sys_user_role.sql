create table "public"."t_sys_user_role"
(
	"username" VARCHAR(255),
	"role" VARCHAR(255),
	"delete_mark" SMALLINT,
	"created_date" BIGINT,
	"modified_date" BIGINT,
	primary key (username, role)
);

INSERT INTO "public"."t_sys_user_role" ("username", "role", "delete_mark", "created_date", "modified_date")
VALUES
('gty', 'user', 1, 1544339077285, 1544339077285),
('gty', 'actuator', 1, 1544339077285, 1544339077285);
