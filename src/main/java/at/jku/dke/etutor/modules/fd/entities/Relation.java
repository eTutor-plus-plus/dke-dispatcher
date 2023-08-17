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
@Table(name = "relation", schema = "fd", catalog = "fd")
public class Relation {
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;
    @Type(type = "string-array")
    @Column(name = "attributes", columnDefinition = "text[]")
    private String[] attributes;
    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "relation")
//    @JoinColumn(name = "relation_id", referencedColumnName = "id")
    private Set<FunctionalDependency> functionalDependencies;
    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "relation_id", referencedColumnName = "id")
    private Set<Closure> closures;
    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "relation_id", referencedColumnName = "id")
    private Set<Key> keys;
    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "relation_id", referencedColumnName = "id")
    private Set<MinimalCover> minimalCovers;
    @Column(name = "normal_form")
    @Enumerated(EnumType.STRING)
    private NF normalForm;
    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "relation")
//    @JoinColumn(name = "relation_id", referencedColumnName = "id")
    private Set<Task> tasks;

    public Relation() {
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String[] getAttributes() {
        return attributes;
    }
    public void setAttributes(String[] attributes) {
        this.attributes = attributes;
    }
    public Set<FunctionalDependency> getFunctionalDependencies() {
        return functionalDependencies;
    }

    public void setFunctionalDependencies(Set<FunctionalDependency> functionalDependencies) {
        this.functionalDependencies = functionalDependencies;
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

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public static String [] calculateRelation(Relation relation) {
        TreeSet<String> result = new TreeSet<>();
        for (Dependency functionalDependency : relation.getFunctionalDependencies()) {
            for (String left: functionalDependency.getLeftSide()) {
                result.add(left);
            }
            for (String right: functionalDependency.getRightSide()) {
                result.add(right);
            }
        }
        return result.toArray(new String[0]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Relation relation = (Relation) o;
        return Objects.equals(id, relation.id)
                && Arrays.equals(this.attributes, relation.attributes)
                && Objects.equals(functionalDependencies, relation.functionalDependencies)
//                && Objects.equals(closures, relation.closures)
//                && Objects.equals(keys, relation.keys)
//                && Objects.equals(minimalCovers, relation.minimalCovers)
              ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, Arrays.hashCode(attributes), functionalDependencies, closures, keys
                , minimalCovers
        );
    }

    @Override
    public String toString() {
        return "Relation{" +
                "id=" + id +
                ", relation='" + Arrays.toString(attributes) + '\'' +
                ", dependencies=" + functionalDependencies +
                ", closures=" + closures +
                "}\n";
    }
}
