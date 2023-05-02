package at.jku.dke.etutor.modules.fd.entities;

import at.jku.dke.etutor.modules.fd.types.ListArrayType;
import at.jku.dke.etutor.modules.fd.types.StringArrayType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;

@TypeDefs({
        @TypeDef(
                name = "string-array",
                typeClass = at.jku.dke.etutor.modules.fd.types.StringArrayType.class
        ),
        @TypeDef(
                name = "string-list",
                typeClass = at.jku.dke.etutor.modules.fd.types.ListArrayType.class
        )
})
@Entity
@Table(name = "dependency", schema = "fd", catalog = "fd")
public class Dependency implements IDependency{
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
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Exercise exercise;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "minimal_cover_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MinimalCover minimalCover;

    public Dependency() {
    }

    public Dependency(String[] leftSide, String[] rightSide, Exercise exercise) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.exercise = exercise;
    }

//    public Dependency(String[] leftSide, String[] rightSide, Exercise exercise, MinimalCover minimalCover) {
//        this.leftSide = leftSide;
//        this.rightSide = rightSide;
//        this.exercise = exercise;
//        this.minimalCover = minimalCover;
//    }

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

//    public MinimalCover getMinimalCover() {
//        return minimalCover;
//    }
//
//    public void setMinimalCover(MinimalCover minimalCover) {
//        this.minimalCover = minimalCover;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dependency that = (Dependency) o;
        return Objects.equals(id, that.id) && Arrays.equals(leftSide, that.leftSide)
                && Arrays.equals(rightSide, that.rightSide);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, Arrays.hashCode(leftSide), Arrays.hashCode(rightSide));
    }

    @Override
    public String toString() {
        return "Dependency{" +
                "id=" + id +
                ", leftSide=" + Arrays.toString(leftSide) +
                ", rightSide=" + Arrays.toString(rightSide) +
//                ", minimalCover=" + minimalCover +
                "}\n";
    }

    public static int compare(Dependency e1, Dependency e2) {
        if (e1.getId() != null && e2.getId()!=null && e1.getId() != e2.getId()) {
            return Long.compare(e1.getId(),e2.getId());
        }
        else if (!Arrays.equals(e1.getLeftSide(),e2.getLeftSide())) {
            return Arrays.compare(e1.getLeftSide(), e2.getLeftSide());
        }
        else if (!Arrays.equals(e1.getRightSide(),e2.getRightSide())) {
            return Arrays.compare(e1.getRightSide(), e2.getRightSide());
        }
        return 0;
    }
}
