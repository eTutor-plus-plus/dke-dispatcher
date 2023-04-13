package at.jku.dke.etutor.modules.fd.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@javax.persistence.Table(name = "exercise_dependency", schema = "public", catalog = "fd")
@javax.persistence.IdClass(at.jku.dke.etutor.modules.fd.entities.ExerciseToDepencenciesPK.class)
public class ExerciseToDepencencies {
    @Id
    @javax.persistence.Column(name = "exercise_id")
    private Long exerciseId;

    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    @Id
    @javax.persistence.Column(name = "dependencies_id")
    private Long dependencyId;

    public Long getDependencyId() {
        return dependencyId;
    }

    public void setDependencyId(Long dependencyId) {
        this.dependencyId = dependencyId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExerciseToDepencencies that = (ExerciseToDepencencies) o;
        return Objects.equals(exerciseId, that.exerciseId) && Objects.equals(dependencyId, that.dependencyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exerciseId, dependencyId);
    }
}
