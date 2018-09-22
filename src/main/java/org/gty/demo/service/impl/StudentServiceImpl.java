package org.gty.demo.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.gty.demo.constant.DeleteFlag;
import org.gty.demo.mapper.StudentMapper;
import org.gty.demo.model.po.Student;
import org.gty.demo.model.vo.StudentVo;
import org.gty.demo.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

    private StudentMapper studentMapper;

    @Autowired
    private void injectBeans(StudentMapper studentMapper) {
        this.studentMapper = Objects.requireNonNull(studentMapper, "studentMapper must not be null");
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, readOnly = true, rollbackFor = Throwable.class)
    @Cacheable(cacheNames = "students", keyGenerator = "keyGenerator")
    @Nullable
    @Override
    public Student findById(long id) {
        var example = Example.builder(Student.class).build();
        example.createCriteria()
                .andEqualTo("id", id)
                .andEqualTo("deleteFlag", DeleteFlag.NOT_DELETED.ordinal());

        var resultList = studentMapper.selectByExample(example);

        if (resultList == null || resultList.size() == 0) {
            return null;
        }

        return resultList.get(0);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, readOnly = true, rollbackFor = Throwable.class)
    @Cacheable(cacheNames = "students", keyGenerator = "keyGenerator")
    @Nullable
    @Override
    public Student findByName(@Nonnull String name) {
        Objects.requireNonNull(name, "name must not be null");

        var example = Example.builder(Student.class).build();
        example.createCriteria()
                .andEqualTo("name", name)
                .andEqualTo("deleteFlag", DeleteFlag.NOT_DELETED.ordinal());

        var resultList = studentMapper.selectByExample(example);

        if (resultList == null || resultList.isEmpty()) {
            return null;
        }

        return resultList.get(0);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, readOnly = true, rollbackFor = Throwable.class)
    @Cacheable(cacheNames = "students", keyGenerator = "keyGenerator")
    @Nonnull
    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public PageInfo<StudentVo> findByCondition(int pageNum, int pageSize, @Nonnull String orderBy) {
        Objects.requireNonNull(orderBy, "orderBy must not be null");

        var example = new Example(Student.class);

        if (StringUtils.isNotBlank(orderBy)) {
            var parameters = ImmutableList.copyOf(Splitter
                    .onPattern(" ")
                    .trimResults()
                    .omitEmptyStrings()
                    .split(orderBy));

            if (parameters.size() == 2
                    && (StringUtils.equals(parameters.get(1), "asc") || StringUtils.equals(parameters.get(1), "desc"))) {
                var orderByParameter = example.orderBy(parameters.get(0));
                if (StringUtils.equals(parameters.get(1), "asc")) {
                    orderByParameter.asc();
                } else {
                    orderByParameter.desc();
                }
            } else {
                throw new IllegalArgumentException("Unable to resolve SQL orderBy parameters: " + orderBy);
            }
        }

        example.createCriteria()
                .andEqualTo("deleteFlag", DeleteFlag.NOT_DELETED.ordinal());

        Page studentPage = PageHelper
                .startPage(pageNum, pageSize)
                .<Student>doSelectPage(() -> studentMapper.selectByExample(example));

        var it = studentPage.listIterator();
        while (it.hasNext()) {
            it.set(StudentVo.build((Student) it.next()));
        }

        return ((Page<StudentVo>) studentPage).toPageInfo();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Throwable.class)
    @CacheEvict(cacheNames = "students", keyGenerator = "keyGenerator", allEntries = true)
    @Override
    public void save(@Nonnull Student student) {
        Objects.requireNonNull(student, "student must not be null");

        int saveResult = studentMapper.insertUseGeneratedKeys(student);
        if (saveResult != 1) {
            throw new RuntimeException();
        }
    }
}
