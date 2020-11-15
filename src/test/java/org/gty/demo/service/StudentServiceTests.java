package org.gty.demo.service;

import org.assertj.core.api.BDDAssertions;
import org.gty.demo.constant.DeleteMark;
import org.gty.demo.mapper.StudentStudentVoMapper;
import org.gty.demo.model.entity.Student;
import org.gty.demo.model.vo.StudentVo;
import org.gty.demo.repository.StudentRepository;
import org.gty.demo.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTests {

    private static final long TEST_STUDENT_ID = 99L;

    @Mock
    private StudentRepository mockStudentRepository;

    @Mock
    private StudentStudentVoMapper mockStudentStudentVoMapper;

    private StudentService testStudentService;

    @BeforeEach
    public void setUp() {
        testStudentService = new StudentServiceImpl(mockStudentRepository, mockStudentStudentVoMapper);
    }

    @Test
    public void shouldReturnStudentVo_WhenIdCanBeFound() {
        final var student = BDDMockito.mock(Student.class);
        final var studentVo = BDDMockito.mock(StudentVo.class);

        final var optionalStudent = Optional.of(student);
        final var optionalStudentVo = Optional.of(studentVo);

        willReturn(optionalStudent)
            .given(mockStudentRepository)
            .findByIdAndDeleteMark(anyLong(), any(DeleteMark.class));

        willReturn(studentVo)
            .given(mockStudentStudentVoMapper)
            .studentToStudentVo(student);

        final var actualOptionalStudentVo = testStudentService.findById(TEST_STUDENT_ID);

        then(mockStudentRepository)
            .should()
            .findByIdAndDeleteMark(eq(TEST_STUDENT_ID), eq(DeleteMark.NOT_DELETED));

        then(mockStudentStudentVoMapper)
            .should()
            .studentToStudentVo(eq(student));

        BDDAssertions.then(actualOptionalStudentVo).isEqualTo(optionalStudentVo);
    }
}
