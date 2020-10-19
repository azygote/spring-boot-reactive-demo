package org.gty.demo.service;

import org.assertj.core.api.Assertions;
import org.gty.demo.constant.DeleteMark;
import org.gty.demo.mapper.StudentStudentVoMapper;
import org.gty.demo.model.entity.Student;
import org.gty.demo.model.vo.StudentVo;
import org.gty.demo.repository.StudentRepository;
import org.gty.demo.service.impl.StudentServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class StudentServiceTests {

    private static final long TEST_STUDENT_ID = 99L;

    @Mock
    private StudentRepository mockStudentRepository;

    @Mock
    private StudentStudentVoMapper mockStudentStudentVoMapper;

    private StudentService testStudentService;


    @Before
    public void setUp() {
        testStudentService = new StudentServiceImpl(mockStudentRepository, mockStudentStudentVoMapper);
    }

    @Test
    public void shouldReturnStudentVo_WhenIdCanBeFound() {
        final var student = BDDMockito.mock(Student.class);
        final var studentVo = BDDMockito.mock(StudentVo.class);

        final var optionalStudent = Optional.of(student);
        final var optionalStudentVo = Optional.of(studentVo);

        BDDMockito
            .willReturn(optionalStudent)
            .given(mockStudentRepository)
            .findByIdAndDeleteMark(BDDMockito.anyLong(), BDDMockito.any(DeleteMark.class));

        BDDMockito
            .willReturn(studentVo)
            .given(mockStudentStudentVoMapper)
            .studentToStudentVo(student);

        final var actualOptionalStudentVo = testStudentService.findById(TEST_STUDENT_ID);

        BDDMockito
            .then(mockStudentRepository)
            .should()
            .findByIdAndDeleteMark(BDDMockito.eq(TEST_STUDENT_ID), BDDMockito.eq(DeleteMark.NOT_DELETED));

        BDDMockito
            .then(mockStudentStudentVoMapper)
            .should()
            .studentToStudentVo(BDDMockito.eq(student));

        Assertions
            .assertThat(actualOptionalStudentVo)
            .isEqualTo(optionalStudentVo);
    }
}
