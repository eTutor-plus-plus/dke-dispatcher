package at.jku.dke.etutor.modules.fd.entities;

import java.io.Serializable;
import java.util.Objects;

public class ExerciseToDepencenciesPK implements Serializable {
    private Long exerciseId;

    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }
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
        if (!(o instanceof ExerciseToDepencenciesPK that)) return false;
        return Objects.equals(exerciseId, that.exerciseId) && Objects.equals(dependencyId, that.dependencyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exerciseId, dependencyId);
    }
}
