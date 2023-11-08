package at.jku.dke.etutor.modules.fd.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "task", schema = "fd", catalog = "fd")
public class Task {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name="relation_id", referencedColumnName = "id")
    private Relation relation;
    @Basic
    @Column(name = "type")
    private String type;
    @Basic
    @Column(name="closure_group_id")
    private Long closureGroupId;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "closure_id", referencedColumnName = "id")
    private Closure closure;

    public Task() {
    }
    public Task(Relation relation, String type, Long closureGroupId, Closure closure) {
        this.relation = relation;
        this.type = type;
        this.closureGroupId = closureGroupId;
        this.closure = closure;
    }
    public Task(Relation relation, String fDSubtype) {
        this.relation = relation;
        this.type = fDSubtype;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Long getClosureGroupId() {
        return closureGroupId;
    }
    public void setClosureGroupId(Long closureGroupId) {
        this.closureGroupId = closureGroupId;
    }
    public Closure getClosure() {
        return closure;
    }
    public void setClosure(Closure closure) {
        this.closure = closure;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", closureGroupId=" + closureGroupId +
                '}';
    }
}
