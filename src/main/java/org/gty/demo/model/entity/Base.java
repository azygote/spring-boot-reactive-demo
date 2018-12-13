package org.gty.demo.model.entity;

import org.gty.demo.constant.DeleteMark;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Base implements Serializable {

    private static final long serialVersionUID = 7125532751153830997L;

    @Enumerated
    private DeleteMark deleteMark;

    @Version
    private Long version;

    @CreatedDate
    private Long createdDate;

    @LastModifiedDate
    private Long modifiedDate;

    public DeleteMark getDeleteMark() {
        return deleteMark;
    }

    public void setDeleteMark(DeleteMark deleteMark) {
        this.deleteMark = deleteMark;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public Long getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Base)) return false;
        Base base = (Base) o;
        return getDeleteMark() == base.getDeleteMark() &&
                Objects.equals(getVersion(), base.getVersion()) &&
                Objects.equals(getCreatedDate(), base.getCreatedDate()) &&
                Objects.equals(getModifiedDate(), base.getModifiedDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDeleteMark(), getVersion(), getCreatedDate(), getModifiedDate());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Base.class.getSimpleName() + "[", "]")
                .add("deleteMark=" + deleteMark)
                .add("version=" + version)
                .add("createdDate=" + createdDate)
                .add("modifiedDate=" + modifiedDate)
                .toString();
    }
}
