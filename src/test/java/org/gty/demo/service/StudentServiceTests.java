package org.gty.demo.service;

import org.assertj.core.api.Assertions;
import org.gty.demo.constant.DeleteMark;
import org.gty.demo.model.entity.Student;
import org.gty.demo.model.vo.StudentVo;
import org.gty.demo.repository.StudentRepository;
import org.gty.demo.service.impl.StudentServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Optional;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({StudentVo.class, Long.class})
public class StudentServiceTests {

    @Mock
    private StudentRepository mockStudentRepository;

    private StudentService testStudentService;

    @Before
    public void setUp() {
        testStudentService = new StudentServiceImpl(mockStudentRepository);
    }

    @Test
    public void shouldReturnStudentVo_WhenIdCanBeFound() {
        final var id = PowerMockito.mock(Long.class);
        final var student = BDDMockito.mock(Student.class);
        final var studentVo = BDDMockito.mock(StudentVo.class);
        PowerMockito.mockStatic(StudentVo.class);

        final var optionalStudent = Optional.of(student);
        final var optionalStudentVo = Optional.of(studentVo);

        BDDMockito
            .willReturn(optionalStudent)
            .given(mockStudentRepository)
            .findByIdAndDeleteMark(BDDMockito.anyLong(), BDDMockito.any(DeleteMark.class));

        BDDMockito
            .given(StudentVo.build(BDDMockito.any(Student.class)))
            .willReturn(studentVo);

        final var actualOptionalStudentVo = testStudentService.findById(id);

        BDDMockito
            .then(mockStudentRepository)
            .should()
            .findByIdAndDeleteMark(BDDMockito.anyLong(), BDDMockito.any(DeleteMark.class));

        PowerMockito.verifyStatic(StudentVo.class);
        StudentVo.build(BDDMockito.any(Student.class));

        Assertions
            .assertThat(actualOptionalStudentVo)
            .isEqualTo(optionalStudentVo);
    }
}
