package at.jku.dke.etutor.modules.fd.entities;

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
@Table(name = "exercise", schema = "fd", catalog = "fd")
public class Exercise {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;
    @Type(type = "string-array")
    @Column(name = "relation", columnDefinition = "text[]")
    private String[] relation;
    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "exercise_id", referencedColumnName = "id")
    private Set<FunctionalDependency> dependencies;
    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "exercise_id", referencedColumnName = "id")
    private Set<Closure> closures;
    @OneToMany (cascade =  CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "exercise_id", referencedColumnName = "id")
    private Set<Key> keys;
    @OneToMany (cascade =  CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "exercise_id", referencedColumnName = "id")
    private Set<MinimalCover> minimalCovers;
    @Column(name = "normal_form")
    @Enumerated(EnumType.STRING)
    private NF normalForm;

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

    public Set<FunctionalDependency> getDependencies() {
        return dependencies;
    }


    public void setDependencies(Set<FunctionalDependency> dependencies) {
        this.dependencies = dependencies;
    }

    public Set<Closure> getClosures() {
        return closures;
    }

    public void setClosures(Set<Closure> closures) {
        this.closures = closures;
    }

    public Set<Key> getKeys() {
        return keys;
    }

    public void setKeys(Set<Key> keys) {
        this.keys = keys;
    }

    public Set<MinimalCover> getMinimalCovers() {
        return minimalCovers;
    }

    public void setMinimalCovers(Set<MinimalCover> minimalCovers) {
        this.minimalCovers = minimalCovers;
    }

    public NF getNormalForm() {
        return normalForm;
    }

    public void setNormalForm(NF normalForm) {
        this.normalForm = normalForm;
    }

    public enum NF{
        BCNF, THIRD, SECOND, FIRST
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercise exercise = (Exercise) o;
        return Objects.equals(id, exercise.id)
                && Arrays.equals(relation, exercise.relation)
                && Objects.equals(dependencies, exercise.dependencies)
                && Objects.equals(closures, exercise.closures)
                && Objects.equals(keys, exercise.keys)
//              && Objects.equals(minimalCovers, exercise.minimalCovers)
              ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, Arrays.hashCode(relation), dependencies, closures, keys
//                , minimalCovers
        );
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", relation='" + Arrays.toString(relation) + '\'' +
                ", dependencies=" + dependencies +
//                ", closures=" + closures +
                "}\n";
    }
}
