package at.jku.dke.etutor.modules.fd.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Solution {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "keys")
    private String keys;
    @Basic
    @Column(name = "closure_combination")
    private String closureCombination;
    @Basic
    @Column(name = "closure_results")
    private String closureResults;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public String getClosureCombination() {
        return closureCombination;
    }

    public void setClosureCombination(String closureCombination) {
        this.closureCombination = closureCombination;
    }

    public String getClosureResults() {
        return closureResults;
    }

    public void setClosureResults(String closureResults) {
        this.closureResults = closureResults;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Solution solution = (Solution) o;
        return Objects.equals(id, solution.id) && Objects.equals(keys, solution.keys) && Objects.equals(closureCombination, solution.closureCombination) && Objects.equals(closureResults, solution.closureResults);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, keys, closureCombination, closureResults);
    }
}