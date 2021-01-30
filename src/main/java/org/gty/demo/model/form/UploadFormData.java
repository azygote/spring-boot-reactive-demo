package org.gty.demo.model.form;

import com.google.common.base.MoreObjects;
import org.springframework.http.codec.multipart.FilePart;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public final class UploadFormData implements Serializable {

    @Serial
    private static final long serialVersionUID = -795063966946903971L;

    @NotNull
    private FilePart uploadFile;

    @NotBlank
    private String fileAdditionalInfo;

    public FilePart getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(FilePart uploadFile) {
        this.uploadFile = uploadFile;
    }

    public String getFileAdditionalInfo() {
        return fileAdditionalInfo;
    }

    public void setFileAdditionalInfo(String fileAdditionalInfo) {
        this.fileAdditionalInfo = fileAdditionalInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UploadFormData)) return false;
        UploadFormData that = (UploadFormData) o;
        return Objects.equals(uploadFile, that.uploadFile) &&
            Objects.equals(fileAdditionalInfo, that.fileAdditionalInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uploadFile, fileAdditionalInfo);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("uploadFile", uploadFile)
            .add("fileAdditionalInfo", fileAdditionalInfo)
            .toString();
    }
}
