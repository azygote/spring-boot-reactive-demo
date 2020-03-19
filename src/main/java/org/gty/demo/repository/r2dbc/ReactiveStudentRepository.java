package org.gty.demo.repository.r2dbc;

import org.gty.demo.model.entity.r2dbc.Student;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ReactiveStudentRepository extends ReactiveCrudRepository<Student, Long> {

    @Query("SELECT * FROM t_student WHERE student_gender = :gender and delete_mark = :deleteFlag")
    Flux<Student> findAllByGenderAndDeleteFlag(String gender, Integer deleteFlag);
}
