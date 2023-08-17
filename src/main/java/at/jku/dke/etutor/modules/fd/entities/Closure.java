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
@Table(name = "closure", schema = "fd", catalog = "fd")
public class Closure implements Dependency{
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "relation_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Relation relation;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "closure")
    private Task task;

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public Closure() {
    }

    public Closure(String[] leftSide, String[] rightSide, Relation relation) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
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

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Closure that = (Closure) o;
        return Objects.equals(id, that.id) &&
                Arrays.equals(leftSide, that.leftSide) && Arrays.equals(rightSide, that.rightSide);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,
                Arrays.hashCode(leftSide), Arrays.hashCode(rightSide));
    }

    @Override
    public String toString() {
        return "Closure{" +
                "id=" + id +
                ", leftSide=" + Arrays.toString(leftSide) +
                ", rightSide=" + Arrays.toString(rightSide) +
                "}\n";
    }
}
