package at.jku.dke.etutor.modules.fd.entities;

import at.jku.dke.etutor.modules.fd.types.StringArrayType;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "minimal_cover", schema = "fd", catalog = "fd")
public class MinimalCover implements Dependency{
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
    @Type(type = "string-array")
    @Column(name = "reasons" , columnDefinition = "text[]")
    private String[] reasons;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name="dependency_id")
    private FunctionalDependency dependency;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name="relation_id")
    private Relation relation;


    public MinimalCover() {
    }

    public MinimalCover(String[] leftSide, String[] rightSide, String[] reasons, FunctionalDependency dependency, Relation relation) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.reasons = reasons;
        this.dependency = dependency;
        this.relation = relation;
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

    public String[] getReasons() {
        return reasons;
    }

    public void setReasons(String[] reasons) {
        this.reasons = reasons;
    }

    public FunctionalDependency getDependency() {
        return dependency;
    }

    public void setDependency(FunctionalDependency dependency) {
        this.dependency = dependency;
    }

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MinimalCover that = (MinimalCover) o;
        return Objects.equals(id, that.id) && Arrays.equals(leftSide, that.leftSide) &&
                Arrays.equals(rightSide, that.rightSide) &&
                Objects.equals(reasons, that.reasons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, Arrays.hashCode(leftSide), Arrays.hashCode(rightSide), Arrays.hashCode(reasons));
    }

    @Override
    public String toString() {
        return "MinimalCover{" +
                "id=" + id +
                ", leftSide=" + Arrays.toString(leftSide) +
                ", rightSide=" + Arrays.toString(rightSide) +
                ", reason='" + reasons +
                "}\n";
    }

//    public static int compare(MinimalCover e1, MinimalCover e2) {
//        if (!Arrays.equals(e1.getLeftSide(),e2.getLeftSide())) {
//            return Arrays.compare(e1.getLeftSide(), e2.getLeftSide());
//        }
//        else if (!Arrays.equals(e1.getRightSide(),e2.getRightSide())) {
//            return Arrays.compare(e1.getRightSide(), e2.getRightSide());
//        }
//        return 0;
//    }
}
