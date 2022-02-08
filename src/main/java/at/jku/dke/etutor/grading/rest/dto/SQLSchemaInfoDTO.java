package at.jku.dke.etutor.grading.rest.dto;

import java.util.List;
import java.util.Map;

/**
 * Represents the information which is returned by the application when a new task-group is created
 */
public class SQLSchemaInfoDTO {
    private Map<String, List<String>> tableColumns;
    private int diagnoseConnectionId;

    public SQLSchemaInfoDTO(){

    }

    public Map<String, List<String>> getTableColumns() {
        return tableColumns;
    }

    public void setTableColumns(Map<String, List<String>> tableColumns) {
        this.tableColumns = tableColumns;
    }

    public int getDiagnoseConnectionId() {
        return diagnoseConnectionId;
    }

    public void setDiagnoseConnectionId(int diagnoseConnectionId) {
        this.diagnoseConnectionId = diagnoseConnectionId;
    }
}
