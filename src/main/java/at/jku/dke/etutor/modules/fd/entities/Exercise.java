package at.jku.dke.etutor.modules.fd.entities;

import at.jku.dke.etutor.modules.fd.types.ListArrayType;
import at.jku.dke.etutor.modules.fd.types.StringArrayType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.*;

@TypeDef(
        name = "string-array",
        typeClass = StringArrayType.class
)
@Entity
public class Exercise {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;
    @Basic
    @Type(type = "string-array")
    @Column(name = "relation", columnDefinition = "text[]")
    private String[] relation;

    @OneToMany (cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Set<Dependency> dependencies;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String[] getRelation() {
        return relation;
    }

    public void setRelation(String[] relation) {
        this.relation = relation;
    }

    public Set<Dependency> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Set<Dependency> dependencies) {
        this.dependencies = dependencies;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercise exercise = (Exercise) o;
        return Objects.equals(id, exercise.id) && Objects.equals(relation, exercise.relation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, relation);
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", relation='" + Arrays.toString(relation) + '\'' +
                ", dependencies=" + dependencies +
                '}';
    }
}
