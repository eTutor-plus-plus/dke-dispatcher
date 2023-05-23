package at.jku.dke.etutor.modules.fd.entities;

import java.util.Set;
import java.util.TreeSet;

public class Decomposition {

    private String[] relation;
    private Set<? extends Dependency> dependencies;
    private NF normalForm;


    public Decomposition(Set<? extends Dependency> dependencies) {
        this.dependencies = dependencies;
        this.relation = calculateRelation(dependencies);
    }

    public String[] getRelation() {
        return relation;
    }

    public void setRelation(String[] relation) {
        this.relation = relation;
    }

    public Set<? extends Dependency> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Set<? extends Dependency> dependencies) {
        this.dependencies = dependencies;
    }

    public NF getNormalForm() {
        return normalForm;
    }

    public void setNormalForm(NF normalForm) {
        this.normalForm = normalForm;
    }

    private static String [] calculateRelation(Set<? extends Dependency> dependencies) {
        TreeSet<String> result = new TreeSet<>();
        for (Dependency functionalDependency : dependencies) {
            for (String left: functionalDependency.getLeftSide()) {
                result.add(left);
            }
            for (String right: functionalDependency.getRightSide()) {
                result.add(right);
            }
        }
        return result.toArray(new String[0]);
    }
}
