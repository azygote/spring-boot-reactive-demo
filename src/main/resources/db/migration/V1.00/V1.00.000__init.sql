CREATE TABLE `t_student`
(
  `student_id`                BIGINT PRIMARY KEY AUTO_INCREMENT,
  `student_name`              VARCHAR(255),
  `student_gender`            VARCHAR(255),
  `student_age`               INT,
  `student_balance`           DECIMAL(30, 10),
  `student_other_information` LONGTEXT,
  `student_photo`             LONGBLOB,
  `delete_flag`               TINYINT,
  `created_date`              BIGINT,
  `modified_date`             BIGINT
);
