package at.jku.dke.etutor.modules.fd.utilities;

public class FDSolve {
    private Long id;
    private String solution;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    @Override
    public String toString() {
        return "FDSolve{" +
                "id=" + id +
                ", solution='" + solution + '\'' +
                '}';
    }
}
