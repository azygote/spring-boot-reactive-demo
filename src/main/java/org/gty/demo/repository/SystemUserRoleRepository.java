package org.gty.demo.repository;

import org.gty.demo.constant.DeleteMark;
import org.gty.demo.model.entity.SystemUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.annotation.Nonnull;
import java.util.Collection;

public interface SystemUserRoleRepository
        extends JpaRepository<SystemUserRole, SystemUserRole.SystemUserRoleId>,
        JpaSpecificationExecutor<SystemUserRole> {

    @Nonnull
    Collection<SystemUserRole> findByIdUsernameAndDeleteMark(@Nonnull String username,
                                                             @Nonnull DeleteMark deleteMark);
}
