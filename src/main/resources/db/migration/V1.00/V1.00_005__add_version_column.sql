/* t_student */
alter table "public"."t_student" add "version" BIGINT;
update "public"."t_student" set "version" = 0;

/* t_sys_user */
alter table "public"."t_sys_user" add "version" BIGINT;
update "public"."t_sys_user" set "version" = 0;

/* t_sys_user_role */
alter table "public"."t_sys_user_role" add "version" BIGINT;
update "public"."t_sys_user_role" set "version" = 0;
