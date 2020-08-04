package com.basemosama.studentportal.ui.professor.add_marks;

import androidx.annotation.Nullable;

public class AddMarksFormState {
    @Nullable
    private Integer attendanceError;
    @Nullable
    private Integer workError;
    @Nullable
    private Integer midtermError;
    @Nullable
    private Integer semesterError;
    @Nullable
    private Integer subjectError;
    private boolean isDataValid;

    public AddMarksFormState(@Nullable Integer attendanceError, @Nullable Integer workError, @Nullable Integer midtermError, @Nullable Integer semesterError, @Nullable Integer subjectError) {
        this.attendanceError = attendanceError;
        this.workError = workError;
        this.midtermError = midtermError;
        this.semesterError = semesterError;
        this.subjectError = subjectError;
        isDataValid = false;
    }

    public AddMarksFormState(boolean isDataValid) {
        attendanceError = null;
        workError = null;
        midtermError = null;
        semesterError = null;
        subjectError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getAttendanceError() {
        return attendanceError;
    }

    @Nullable
    public Integer getWorkError() {
        return workError;
    }

    @Nullable
    public Integer getMidtermError() {
        return midtermError;
    }

    @Nullable
    public Integer getSemesterError() {
        return semesterError;
    }

    @Nullable
    public Integer getSubjectError() {
        return subjectError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}
