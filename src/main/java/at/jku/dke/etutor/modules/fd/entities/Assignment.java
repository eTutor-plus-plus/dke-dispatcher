package at.jku.dke.etutor.modules.fd.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Assignment {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @Basic
    @Column(name="exercise_id")
    private Long exerciseId;

    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }
    @Basic
    @Column(name="student_id")
    private Long studentId;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    @Basic
    @Column(name = "type")
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "is_solved")
    private Boolean isSolved;

    public Boolean getSolved() {
        return isSolved;
    }

    public void setSolved(Boolean solved) {
        isSolved = solved;
    }

    @Basic
    @Column(name = "is_exam")
    private Boolean isExam;

    public Boolean getExam() {
        return isExam;
    }

    public void setExam(Boolean exam) {
        isExam = exam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assignment that = (Assignment) o;
        return Objects.equals(studentId, that.studentId) && Objects.equals(exerciseId, that.exerciseId) && Objects.equals(type, that.type) && Objects.equals(isSolved, that.isSolved) && Objects.equals(isExam, that.isExam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, exerciseId, type, isSolved, isExam);
    }
}
