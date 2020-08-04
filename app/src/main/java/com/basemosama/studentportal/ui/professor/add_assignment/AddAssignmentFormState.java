package com.basemosama.studentportal.ui.professor.add_assignment;

import androidx.annotation.Nullable;

public class AddAssignmentFormState {
    @Nullable
    private Integer titleError;
    @Nullable
    private Integer pathError;
    @Nullable
    private Integer departmentError;
    @Nullable
    private Integer gradeError;
    @Nullable
    private Integer subjectError;
    private boolean isDataValid;

    public AddAssignmentFormState(@Nullable Integer titleError, @Nullable Integer pathError, @Nullable Integer departmentError, @Nullable Integer gradeError, @Nullable Integer subjectError) {
        this.titleError = titleError;
        this.pathError = pathError;
        this.departmentError = departmentError;
        this.gradeError = gradeError;
        this.subjectError = subjectError;
        isDataValid = false;
    }

    public AddAssignmentFormState(boolean isDataValid) {
        titleError = null;
        pathError = null;
        departmentError = null;
        gradeError = null;
        subjectError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getTitleError() {
        return titleError;
    }

    @Nullable
    public Integer getPathError() {
        return pathError;
    }

    @Nullable
    public Integer getDepartmentError() {
        return departmentError;
    }

    @Nullable
    public Integer getGradeError() {
        return gradeError;
    }

    @Nullable
    public Integer getSubjectError() {
        return subjectError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}
