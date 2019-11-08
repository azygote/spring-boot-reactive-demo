package org.gty.demo.repository;

import org.gty.demo.constant.DeleteMark;
import org.gty.demo.model.entity.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface SystemUserRepository
    extends JpaRepository<SystemUser, String>, JpaSpecificationExecutor<SystemUser> {

    @Nonnull
    Optional<SystemUser> findByUsernameAndDeleteMark(@Nonnull String username,
                                                     @Nonnull DeleteMark deleteMark);
}
