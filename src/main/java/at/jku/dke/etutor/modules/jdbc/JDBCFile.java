package at.jku.dke.etutor.modules.jdbc;

import java.io.Serializable;

public class JDBCFile implements Serializable {

    private String filename;
    private String content;

    public JDBCFile(String filename, String content) {
        this.filename = filename;
        this.content = content;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
