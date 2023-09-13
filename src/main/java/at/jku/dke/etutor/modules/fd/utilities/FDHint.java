package at.jku.dke.etutor.modules.fd.utilities;

public class FDHint {
    private Long subId;
    private String hint;

    public FDHint() {
    }

    public FDHint(Long subId, String hint) {
        this.subId = subId;
        this.hint = hint;
    }

    public Long getSubId() {
        return subId;
    }

    public void setSubId(Long subId) {
        this.subId = subId;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    @Override
    public String toString() {
        return "FDHint{" +
                "subId=" + subId +
                ", hint='" + hint + '\'' +
                '}';
    }
}
