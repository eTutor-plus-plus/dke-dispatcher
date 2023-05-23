package at.jku.dke.etutor.modules.fd.entities;

public interface Dependency {
    public Long getId();
    public void setId(Long id);
    public String[] getLeftSide();
    public void setLeftSide(String[] leftSide);
    public String[] getRightSide();
    public void setRightSide(String[] rightSide);
    public Relation getRelation();

    public void setRelation(Relation relation);
}
