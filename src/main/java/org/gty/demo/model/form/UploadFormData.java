package org.gty.demo.model.form;

import com.google.common.base.MoreObjects;
import org.springframework.http.codec.multipart.FilePart;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public final class UploadFormData implements Serializable {

    private static final long serialVersionUID = -795063966946903971L;

    @NotNull
    private FilePart uploadFile;

    @NotBlank
    private String foo;

    public FilePart getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(FilePart uploadFile) {
        this.uploadFile = uploadFile;
    }

    public String getFoo() {
        return foo;
    }

    public void setFoo(String foo) {
        this.foo = foo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UploadFormData)) return false;
        UploadFormData that = (UploadFormData) o;
        return Objects.equals(getUploadFile(), that.getUploadFile()) &&
            Objects.equals(getFoo(), that.getFoo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUploadFile(), getFoo());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("uploadFile", uploadFile)
            .add("foo", foo)
            .toString();
    }
}
