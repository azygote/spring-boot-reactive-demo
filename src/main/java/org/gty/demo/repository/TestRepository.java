package org.gty.demo.repository;

import org.gty.demo.model.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TestRepository extends JpaRepository<Test, Long>, JpaSpecificationExecutor<Test> {
}
