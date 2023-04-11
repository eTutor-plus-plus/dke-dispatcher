package at.jku.dke.etutor.modules.fd.entities;

import java.io.Serializable;
import java.util.Objects;

public class AssignmentPK implements Serializable {
    private Long studentId;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
    private Long exerciseId;

    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssignmentPK that)) return false;
        return Objects.equals(studentId, that.studentId) && Objects.equals(exerciseId, that.exerciseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, exerciseId);
    }
}
