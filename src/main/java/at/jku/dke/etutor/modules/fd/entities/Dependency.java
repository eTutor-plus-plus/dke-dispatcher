package at.jku.dke.etutor.modules.fd.entities;

import at.jku.dke.etutor.modules.fd.types.StringArrayType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.Objects;

@TypeDefs({
        @TypeDef(
                name = "string-array",
                typeClass = StringArrayType.class
        )
})
@Entity
@Table(name = "dependency", schema = "public", catalog = "fd")
public class Dependency {
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
    @Basic
    @Column(name = "violated_normalform")
    private String violatedNormalform;
    @Basic
    @Column(name = "has_redundant_part")
    private Boolean hasRedundantPart;
    @Basic
    @Column(name = "is_trivial")
    private Boolean isTrivial;

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

    public String getViolatedNormalform() {
        return violatedNormalform;
    }

    public void setViolatedNormalform(String violatedNormalform) {
        this.violatedNormalform = violatedNormalform;
    }

    public Boolean getHasRedundantPart() {
        return hasRedundantPart;
    }

    public void setHasRedundantPart(Boolean hasRedundantPart) {
        this.hasRedundantPart = hasRedundantPart;
    }

    public Boolean getTrivial() {
        return isTrivial;
    }

    public void setTrivial(Boolean trivial) {
        isTrivial = trivial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dependency that = (Dependency) o;
        return Objects.equals(id, that.id) && Objects.equals(leftSide, that.leftSide) && Objects.equals(rightSide, that.rightSide) && Objects.equals(violatedNormalform, that.violatedNormalform) && Objects.equals(hasRedundantPart, that.hasRedundantPart) && Objects.equals(isTrivial, that.isTrivial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, leftSide, rightSide, violatedNormalform, hasRedundantPart, isTrivial);
    }
}
