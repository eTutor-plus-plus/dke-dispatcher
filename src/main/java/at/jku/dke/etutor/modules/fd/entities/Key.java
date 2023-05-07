package at.jku.dke.etutor.modules.fd.entities;

import at.jku.dke.etutor.modules.fd.types.StringArrayType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@TypeDef(
        name = "string-array",
        typeClass = StringArrayType.class
)
@Entity
@Table(name = "key", schema = "fd", catalog = "fd")
public class Key implements Dependency {
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
    @ManyToOne (cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Exercise exercise;

    public Key() {
    }

    public Key(String[] leftSide, String[] rightSide, Exercise exercise) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.exercise = exercise;
    }
    public Key(Dependency dependency) {
        this.leftSide = dependency.getLeftSide();
        this.rightSide = dependency.getRightSide();
        this.exercise = dependency.getExercise();
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

    public void setRightSide(String[] rightSid) {
        this.rightSide = rightSid;
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
        Key key = (Key) o;
        return Objects.equals(id, key.id) && Arrays.equals(leftSide, key.leftSide) && Arrays.equals(rightSide, key.rightSide);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, Arrays.hashCode(leftSide), Arrays.hashCode(rightSide));
    }
}
