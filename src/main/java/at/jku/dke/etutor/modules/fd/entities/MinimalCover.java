package at.jku.dke.etutor.modules.fd.entities;

import at.jku.dke.etutor.modules.fd.types.StringArrayType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

@TypeDef(
        name = "string-array",
        typeClass = StringArrayType.class
)
@Entity
@Table(name = "minimal_cover", schema = "fd", catalog = "fd")
public class MinimalCover {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;
    @Type(type = "string-array")
    @Column(name = "left_side", columnDefinition = "text[]")
    private String[] leftSide;
    @Type(type = "string-array")
    @Column(name = "right_side", columnDefinition = "text[]")
    private String[] rightSide;
    @Column(name = "reason")
    private String reason;
    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name="minimal_cover_id", referencedColumnName = "id")
    private Set<Dependency> dependencies;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="exercise_id", referencedColumnName = "id")
    private Exercise exercise;


    public MinimalCover() {
    }

    public MinimalCover(String[] leftSide, String[] rightSide, Set<Dependency> dependencies) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.dependencies = dependencies;
    }

    public MinimalCover(String[] leftSide, String[] rightSide, String reason, Set<Dependency> dependencies) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.reason = reason;
        this.dependencies = dependencies;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public String[] getLeftSide() {
        return leftSide;
    }

    public void setLeftSide(String[] leftSide) {
        this.leftSide = leftSide;
    }

    public String[] getRightSide() {
        return rightSide;
    }

    public void setRightSide(String[] rightSide) {
        this.rightSide = rightSide;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Set<Dependency> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Set<Dependency> dependencies) {
        this.dependencies = dependencies;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MinimalCover that = (MinimalCover) o;
        return Objects.equals(id, that.id) && Arrays.equals(leftSide, that.leftSide) &&
                Arrays.equals(rightSide, that.rightSide) &&
                Objects.equals(reason, that.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, Arrays.hashCode(leftSide), Arrays.hashCode(rightSide), reason);
    }

    @Override
    public String toString() {
        return "MinimalCover{" +
                "id=" + id +
                ", leftSide=" + Arrays.toString(leftSide) +
                ", rightSide=" + Arrays.toString(rightSide) +
                ", reason='" + reason +
                "}\n";
    }

    public static int compare(MinimalCover e1, MinimalCover e2) {
        if (!Arrays.equals(e1.getLeftSide(),e2.getLeftSide())) {
            return Arrays.compare(e1.getLeftSide(), e2.getLeftSide());
        }
        else if (!Arrays.equals(e1.getRightSide(),e2.getRightSide())) {
            return Arrays.compare(e1.getRightSide(), e2.getRightSide());
        }
        return 0;
    }
}
