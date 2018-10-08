CREATE TABLE "public"."t_student"
(
  "student_id"                BIGSERIAL PRIMARY KEY,
  "student_name"              VARCHAR(255),
  "student_gender"            VARCHAR(255),
  "student_age"               INT,
  "student_balance"           NUMERIC(30, 10),
  "student_other_information" TEXT,
  "student_photo"             OID,
  "delete_flag"               SMALLINT,
  "created_date"              BIGINT,
  "modified_date"             BIGINT
);
